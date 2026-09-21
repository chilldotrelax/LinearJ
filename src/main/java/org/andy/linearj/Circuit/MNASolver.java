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
import org.andy.linearj.Screen.exception.IllegalMatrixException;

import java.util.*;

/*
This class implements MNA analysis to compute the node voltages (and current through a voltage source) of any simple DC circuit containing the following elements:
    1) Independent DC Voltage Source (typically denoted in schematics as two horizontal bars with two terminals)
    2) Independent DC Current Source (typically denoted in schematics as a circle that contains an arrow that points in the direction of the current flow).
    3) A simple resistor element
    4) A simple ground element that terminates into earth (has no option for an "end node").
 */

public final class MNASolver extends AbstractCircuitSolver {
    private final List<Integer> temporaryListForIndexOnly;

    private double[][] resistorMatrix;
    private double[] rightHandVector;
    private double[] solutionVector;
    private final double[] x;

    private int groundNodeIndex;

    private final SimpleStringProperty computationResultProperty;

    // overload 1 (Does not contain SimpleStringProperty)
    public MNASolver(final AbstractCircuitElement[] elmList, final Map<Integer, CircuitNode> circuitNodeMap) {
        super(elmList, circuitNodeMap);

        this.computationResultProperty = null;
        this.temporaryListForIndexOnly = new ArrayList<>();

        int numberOfVoltageElementsOffSet = 0;

        for (AbstractCircuitElement element : getCircuitElementArray()) {
            if (element instanceof VoltageSourceElement) {
                numberOfVoltageElementsOffSet++;
            }
        }

        getCircuitNodeMap().forEach((nodeID, circuitNode) -> temporaryListForIndexOnly.add(circuitNode.getNodeID()));

        getCircuitNodeMap().forEach(((nodeID, circuitNode) -> {
            if (circuitNode.isGroundNode()) {
                this.groundNodeIndex = temporaryListForIndexOnly.indexOf(circuitNode.getNodeID());
            }
        }));

        this.resistorMatrix = new double[this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet][this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet];
        this.rightHandVector = new double[this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet];
        this.x = new double[super.getCircuitNodeMap().size()];

    }

    // overload 2 (contains SimpleStringProperty)
    public MNASolver(AbstractCircuitElement[] elmList, Map<Integer, CircuitNode> circuitNodeMap, SimpleStringProperty computationResultPropertyInput) {
        super(elmList, circuitNodeMap);
        this.computationResultProperty = computationResultPropertyInput;
        this.temporaryListForIndexOnly = new ArrayList<>();

        int numberOfVoltageElementsOffSet = 0;

        for (AbstractCircuitElement element : getCircuitElementArray()) {
            if (element instanceof VoltageSourceElement) {
                numberOfVoltageElementsOffSet++;
            }
        }

        getCircuitNodeMap().forEach((nodeID, circuitNode) -> temporaryListForIndexOnly.add(circuitNode.getNodeID()));

        getCircuitNodeMap().forEach(((nodeID, circuitNode) -> {
            if (circuitNode.isGroundNode()) {
                this.groundNodeIndex = temporaryListForIndexOnly.indexOf(circuitNode.getNodeID());
            }
        }));

        this.resistorMatrix = new double[this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet][this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet];
        this.rightHandVector = new double[this.getCircuitNodeMap().size() + numberOfVoltageElementsOffSet];
        this.x = new double[super.getCircuitNodeMap().size()];

    }

    // TODO replace instanceof spam with polymorphism.

    // aka remove the pattern matching! When we loop through the element array, MNASolver should not have
    // to worry about what the type of each element is; it simply knows that it must call its "stampSelf" method.

    private void stampCircuit() {
        int offsetsCounter = 0;

        for (AbstractCircuitElement element : getCircuitElementArray()) {
            switch (element) {
                case ResistorElement resistorElement ->
                        resistorMatrix = resistorElement.stampSelf(resistorMatrix, temporaryListForIndexOnly.indexOf(resistorElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(resistorElement.getEndNodeID()));
                case CurrentSourceElement currentSourceElement ->
                        rightHandVector = currentSourceElement.stampSelf(rightHandVector, temporaryListForIndexOnly.indexOf(currentSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(currentSourceElement.getEndNodeID()));
                case VoltageSourceElement voltageSourceElement -> {
                    if (offsetsCounter <= resistorMatrix.length - getCircuitNodeMap().size()) {
                        rightHandVector = voltageSourceElement.stampSelf(rightHandVector, getCircuitNodeMap().size(), offsetsCounter);
                        resistorMatrix = voltageSourceElement.stampToGlobalMatrix(resistorMatrix, temporaryListForIndexOnly.indexOf(voltageSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(voltageSourceElement.getEndNodeID()), getCircuitNodeMap().size(), offsetsCounter);
                        offsetsCounter++;
                    }
                }
                case GroundElement ignored -> {
                    // Ground element has no stamp.
                }
                default ->
                        throw new IllegalMatrixException("Unable to stamp element." + "\n" + "If you see this, this is almost certainly a programming bug and is not your fault.");
            }
        }
    }

    @Override
    public void solveCircuit() {
        stampCircuit();

        if (!CircuitValidator.passesStaticCircuitInspection(getCircuitElementArray(), getCircuitNodeMap())) {
            throw new IllegalStateException();
        } else {
            computationResultProperty.set("Passed circuit inspection; circuit is valid.");
        }

        final double[][] twoDInputArray = MatrixMath.reduce2DMatrixToSubmatrix(resistorMatrix, groundNodeIndex);
        final double[] oneDRightHandVector = MatrixMath.reduceVectorToSubvector(rightHandVector, groundNodeIndex);
        final double[] oneDBlankSolutionVector = MatrixMath.reduceVectorToSubvector(x, groundNodeIndex);

        LUDecomposition lu = new LUDecomposition(twoDInputArray);

        //Note: solution vector may contain both node voltages & currents across voltage sources.
        solutionVector = lu.solve(oneDRightHandVector, oneDBlankSolutionVector);

        final Collection<CircuitNode> nodesCollection = getCircuitNodeMap().values();

        int count = 0;

        for (CircuitNode node : nodesCollection) {
            if (!node.isGroundNode()) {
                node.setNodeVoltage(solutionVector[count]);
                count++;
//                System.out.println("Solution vector: " + Arrays.toString(solutionVector));
//                System.out.println("Current count: " + count + "solution vector length : " + solutionVector.length + "node hashmap size: " + nodeHashMap.size());
            }
        }
        // Since the circuit solver automatically removes ground node, the solution vector will naturally be equal to the size of node hash map. This should
        // not be the case as for any circuits containing voltage sources, the solution output will not include the voltage current.
        // To amend this, we create a condition that MUST be met:
        // 1) if the solution vector's length plus one (the one represents the reference node)
        if (solutionVector.length + 1 > getCircuitNodeMap().size()) {
            for (AbstractCircuitElement elmIsVSS : getCircuitElementArray()) {
                if (Objects.requireNonNull(elmIsVSS) instanceof VoltageSourceElement voltElm) {
                    voltElm.setCurrent(solutionVector[count]);
                    count++;
                }
            }
        }
        reportResultsToConsole();
    }
    
    public double[] reportResultsAsVector() {
        return Arrays.copyOf(solutionVector, solutionVector.length);
    }

    @Override
    public void reportResultsToConsole() {
        if (computationResultProperty == null) {
            // Should be replaced by a logger!
            System.out.println("The node number(s) displayed in the results below are the same as the node ID.");

            getCircuitNodeMap().forEach((nodeId, node) -> {
                if (!node.isGroundNode()) {
                    System.out.println("The voltage at node " + node.getNodeID() + " (w.r.t ground node " + this.groundNodeIndex + ") is: " + roundNumbers(node.getNodeVoltage()) + "V");
                }
            });

            for (AbstractCircuitElement element : getCircuitElementArray()) {
                if (element instanceof VoltageSourceElement voltageSourceElement) {
                    System.out.println("The current of the voltage source " + element.getComponentID() + " is: " + roundNumbers(voltageSourceElement.getVoltageSourceCurrent()) + "A");
                }
            }

        } else {
            computationResultProperty.set("The node number(s) displayed in the results below are the same as the node ID.");
            computationResultProperty.set("Recall that the voltage at the ground node is 0 V.");

            getCircuitNodeMap().forEach((nodeId, node) -> {
                if (!node.isGroundNode()) {
                    computationResultProperty.set("The voltage at node " + node.getNodeID() + " (w.r.t ground node " + this.groundNodeIndex + ") is: " + roundNumbers(node.getNodeVoltage()) + "V");
                }
            });

            for (AbstractCircuitElement element : getCircuitElementArray()) {
                if (element instanceof VoltageSourceElement voltageSourceElement) {
                    computationResultProperty.set("The current of the voltage source " + element.getComponentID() + " is: " + roundNumbers(voltageSourceElement.getVoltageSourceCurrent()) + "A");
                }
            }
        }
    }

}
