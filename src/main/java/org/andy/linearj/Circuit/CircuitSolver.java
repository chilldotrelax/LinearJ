/*
 * MIT License
 *
 * Copyright © 2026 Andy Huang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.andy.linearj.Circuit;

import javafx.beans.property.SimpleStringProperty;

import org.andy.linearj.Maths.LUDecomposition;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;

import java.util.*;

public class CircuitSolver {
    private final CircuitElement[] elementList;
    private final HashMap<Integer, CircuitNode> nodeHashMap;
    private double[][] resistorMatrix;
    private double[] rightHandVector;
    private final double[] x; //Remove this reference later and relegate it to the solve method.
    private final List<Integer> temporaryListForIndexOnly;

    private int groundNodeIndex;
    private SimpleStringProperty computationResultProperty;

    public CircuitSolver(final CircuitElement[] elements, final Map<Integer, CircuitNode> nodes){
        this.elementList = elements;
        this.nodeHashMap = new HashMap<>(nodes);//Check this
        this.temporaryListForIndexOnly = new ArrayList<>();

        int numberOfVoltageElementsOffSet = 0;

        for (CircuitElement element: elementList){
            if (element instanceof VoltageSourceElement){
                numberOfVoltageElementsOffSet++;
            }
        }

        nodeHashMap.forEach((nodeID, circuitNode) -> temporaryListForIndexOnly.add(circuitNode.getNodeID()));
        nodeHashMap.forEach(((nodeID, circuitNode) -> {
            if (circuitNode.isGroundNode()){
                this.groundNodeIndex = temporaryListForIndexOnly.indexOf(circuitNode.getNodeID());
            }
        }));

        this.resistorMatrix = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet][this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
        this.rightHandVector = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
        this.x = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
    }

    public CircuitSolver(final CircuitElement[] elements, final Map<Integer, CircuitNode> nodes, SimpleStringProperty compResultProp){
        this.elementList = elements;
        this.nodeHashMap = new HashMap<>(nodes);//Check this
        this.computationResultProperty = compResultProp;
        this.temporaryListForIndexOnly = new ArrayList<>();

        int numberOfVoltageElementsOffSet = 0;

        for (CircuitElement element: elementList){
            if (element instanceof VoltageSourceElement){
                numberOfVoltageElementsOffSet++;
            }
        }

        nodeHashMap.forEach((nodeID, circuitNode) -> temporaryListForIndexOnly.add(circuitNode.getNodeID()));
        nodeHashMap.forEach(((nodeID, circuitNode) -> {
            if (circuitNode.isGroundNode()){
                this.groundNodeIndex = temporaryListForIndexOnly.indexOf(circuitNode.getNodeID());
            }
        }));

        this.resistorMatrix = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet][this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
        this.rightHandVector = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
        this.x = new double[this.nodeHashMap.size()+numberOfVoltageElementsOffSet];
    }

    private void stampElement() throws IllegalMatrixException {
        int offsetsCounter = 1;

        for (CircuitElement element: elementList){
            switch (element) { 
                case ResistorElement resistorElement ->
                    resistorMatrix = resistorElement.stampSelf(resistorMatrix, temporaryListForIndexOnly.indexOf(resistorElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(resistorElement.getEndNodeID()));
                case CurrentSourceElement currentSourceElement ->
                    rightHandVector = currentSourceElement.stampSelf(rightHandVector, temporaryListForIndexOnly.indexOf(currentSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(currentSourceElement.getEndNodeID()));
                case VoltageSourceElement voltageSourceElement ->{
                    if (offsetsCounter <= resistorMatrix.length - nodeHashMap.size()){
                        rightHandVector = voltageSourceElement.stampSelf(rightHandVector, nodeHashMap.size(),offsetsCounter);
                        resistorMatrix = voltageSourceElement.stampToGlobalMatrix(resistorMatrix,temporaryListForIndexOnly.indexOf(voltageSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(voltageSourceElement.getEndNodeID()),nodeHashMap.size(),offsetsCounter);
                        offsetsCounter++;
                    }
                }
                case GroundElement ignored ->{
                    // Ground element cannot stamp on its own.
                }
                default -> throw new IllegalMatrixException("Unable to stamp element." +"\n"+ "If you see this, this is almost certainly a programming bug and is not your fault.");
            }
        }
    }


    public void solveCircuit(){
        stampElement();

        final double[][] twoDInputArray = MatrixMath.reduce2DMatrixToSubmatrix(resistorMatrix,groundNodeIndex);
        final double[] oneDRightHandVector = MatrixMath.reduceVectorToSubvector(rightHandVector,groundNodeIndex);
        final double[] oneDBlankSolutionVector = MatrixMath.reduceVectorToSubvector(x,groundNodeIndex);

        LUDecomposition lu = new LUDecomposition(twoDInputArray);

        //Note: solution vector may contain both node voltages & currents across voltage sources.
        final double[] solutionVector = lu.solve(oneDRightHandVector, oneDBlankSolutionVector);
        final Collection<CircuitNode> nodesCollection = nodeHashMap.values();

        int count = 0;

        if (computationResultProperty == null){
            //As of now, there are no implementations for this particular block.
        }

        else{
            for (CircuitNode node: nodesCollection){
                if (!node.isGroundNode()){
                    node.setNodeVoltage(solutionVector[count]);
                    computationResultProperty.set("The voltage at node " + node.getNodeID() + "(w.r.t ground node) is: " + solutionVector[count] + "V");
                    count++;
                }
            }
            //if there are voltage sources present
            if (solutionVector.length > nodeHashMap.size() && count == nodeHashMap.size()){
                for (CircuitElement elmIsVSS: elementList){
                    if (Objects.requireNonNull(elmIsVSS) instanceof VoltageSourceElement voltElm) {
                        voltElm.setCurrent(solutionVector[count]);
                        computationResultProperty.set("The current of the voltage source" + elmIsVSS.getComponentID() + "is: " + solutionVector[count] + "A");
                        count++;
                    }
                }
            }
        }
    }
}
