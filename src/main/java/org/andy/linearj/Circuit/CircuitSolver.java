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

import org.andy.linearj.Maths.LUDecomposition;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CircuitSolver {
    private final CircuitElement[] elementList;
    private final HashMap<Integer, CircuitNode> nodeHashMap;
    private double[][] resistorMatrix;
    private double[] rightHandVector;
    private final double[] x;
    private final List<Integer> temporaryListForIndexOnly;
    private int groundNodeIndex;

    public CircuitSolver(final CircuitElement[] elements, final HashMap<Integer, CircuitNode> nodes ){
        this.elementList = elements;
        this.nodeHashMap = nodes;
        this.temporaryListForIndexOnly = new ArrayList<>();

        int numberOfVoltageElementsOffSet = 0;

        for (CircuitElement element: elementList){
            if (element instanceof VoltageSourceElement){
                numberOfVoltageElementsOffSet++;
            }
        }

        nodeHashMap.forEach((integer, circuitNode) -> temporaryListForIndexOnly.add(circuitNode.getNodeID()));
        nodeHashMap.forEach(((integer, circuitNode) -> {
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

    public double[] solveCircuit(){
        stampElement();

        LUDecomposition lu = new LUDecomposition(MatrixMath.reduce2DMatrixToSubmatrix(resistorMatrix,groundNodeIndex));

        //TODO ensure that the solveCircuit() returns a clean output that can be directly parsed by the controller for display.

        return lu.solve(MatrixMath.reduceVectorToSubvector(rightHandVector,groundNodeIndex), MatrixMath.reduceVectorToSubvector(x,groundNodeIndex));
    }
}
