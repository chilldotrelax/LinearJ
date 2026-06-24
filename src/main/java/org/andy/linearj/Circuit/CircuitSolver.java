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
import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;

import java.util.HashMap;

public class CircuitSolver {
    private final CircuitElement[] elementList;
    private final CircuitNode[] nodeLists;
    private final HashMap<Integer, Integer> nodesHashMap = new HashMap<>();
    private double[][] resistorMatrix;
    private double[] rightHandVector;
    private final double[] x;

    public CircuitSolver(CircuitElement[] elements, CircuitNode[] nodeLists ){
        this.elementList = elements;
        this.nodeLists = nodeLists;

        int numberOfVoltageElementsOffSet = 0;

        for (CircuitElement element: elementList){
            if (element instanceof VoltageSourceElement){
                numberOfVoltageElementsOffSet++;
            }
        }

        this.resistorMatrix = new double[this.nodeLists.length+numberOfVoltageElementsOffSet][this.nodeLists.length+numberOfVoltageElementsOffSet];
        this.rightHandVector = new double[this.nodeLists.length+numberOfVoltageElementsOffSet];
        this.x = new double[this.nodeLists.length+numberOfVoltageElementsOffSet];

        int numOfGroundNodes = 0;
        int index = 1;

        for (CircuitNode node: nodeLists){
            if (!node.isFloatingNode() && numOfGroundNodes < 1 && node.isGroundNode()){
                nodesHashMap.put(node.getNodeID(),0);
                numOfGroundNodes++;
            }
            else if (!node.isFloatingNode() && numOfGroundNodes >= 1 && !node.isGroundNode()){
                nodesHashMap.put(node.getNodeID(),index);
                index++;
            }
        }
    }

    private Integer getIndex(Integer elementNodeID){return nodesHashMap.get(elementNodeID);}

    private void stampElement() throws IllegalMatrixException {
        int offsetsCounter = 1;

        for (CircuitElement element: elementList){
            switch (element) {
                case ResistorElement resistorElement ->
                    resistorMatrix = resistorElement.stampSelf(resistorMatrix, getIndex(resistorElement.getBegNodeID()), getIndex(resistorElement.getEndNodeID()));
                case CurrentSourceElement currentSourceElement ->
                    rightHandVector = currentSourceElement.stampSelf(rightHandVector, getIndex(currentSourceElement.getBegNodeID()), getIndex(currentSourceElement.getEndNodeID()));
                case VoltageSourceElement voltageSourceElement ->{
                    if (offsetsCounter <= resistorMatrix.length - nodeLists.length){
                        rightHandVector = voltageSourceElement.stampSelf(rightHandVector, nodeLists.length,offsetsCounter);
                        resistorMatrix = voltageSourceElement.stampToGlobalMatrix(resistorMatrix,getIndex(voltageSourceElement.getBegNodeID()), getIndex(voltageSourceElement.getEndNodeID()),nodeLists.length,offsetsCounter);
                        offsetsCounter++;
                    }
                }
                default -> throw new IllegalMatrixException("Unable to stamp element." +"\n"+ "If you see this, this is almost certainly a programming bug and is not your fault.");
            }
        }
    }

    private double[][] reconstructResistorMatrix(double[][] inputMatrix){
        double[][] result = new double[inputMatrix.length - 1][inputMatrix.length - 1];
        for (int i = 0; i < result.length; i++){
            System.arraycopy(inputMatrix[i + 1], 1, result[i], 0, result.length);
        }
        return result;
    }
    
    private double[] reconstructVector (double[] rightHand){
        double[] result = new double[rightHand.length - 1];
        System.arraycopy(rightHand, 1, result, 0, result.length);
        return result;
    }

    public double[] solveCircuit(){
        stampElement();
        LUDecomposition lu = new LUDecomposition(reconstructResistorMatrix(resistorMatrix));
        return lu.solve(reconstructVector(rightHandVector), reconstructVector(x));
    }
}
