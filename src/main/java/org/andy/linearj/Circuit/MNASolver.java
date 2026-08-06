package org.andy.linearj.Circuit;

import javafx.beans.property.SimpleStringProperty;
import org.andy.linearj.Maths.LUDecomposition;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.exception.IllegalMatrixException;

import java.util.*;

public class MNASolver extends AbstractCircuitSolver {
    private final CircuitElement[] elementList;
    private final HashMap<Integer, CircuitNode> nodeHashMap;
    private final List<Integer> temporaryListForIndexOnly;
    private double[][] resistorMatrix;
    private double[] rightHandVector;
    private double[] solutionVector;
    private final double[] x;
    private int groundNodeIndex;

    private final SimpleStringProperty computationResultProperty;
    public MNASolver(CircuitElement[] elmList, Map< Integer, CircuitNode > circuitNodeMap){
        super(elmList,circuitNodeMap);
        this.computationResultProperty = null;

    }

    public MNASolver(CircuitElement[] elmList, Map< Integer, CircuitNode > circuitNodeMap, SimpleStringProperty computationResultPropertyInput){
        super(elmList,circuitNodeMap);
        this.computationResultProperty = computationResultPropertyInput;

    }

    private void stampCircuit(){
        int offsetsCounter = 0;

        for (CircuitElement element : elementList) {
            switch (element) {
                case ResistorElement resistorElement ->
                        resistorMatrix = resistorElement.stampSelf(resistorMatrix, temporaryListForIndexOnly.indexOf(resistorElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(resistorElement.getEndNodeID()));
                case CurrentSourceElement currentSourceElement ->
                        rightHandVector = currentSourceElement.stampSelf(rightHandVector, temporaryListForIndexOnly.indexOf(currentSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(currentSourceElement.getEndNodeID()));
                case VoltageSourceElement voltageSourceElement -> {
                    if (offsetsCounter <= resistorMatrix.length - nodeHashMap.size()) {
                        rightHandVector = voltageSourceElement.stampSelf(rightHandVector, nodeHashMap.size(), offsetsCounter);
                        resistorMatrix = voltageSourceElement.stampToGlobalMatrix(resistorMatrix, temporaryListForIndexOnly.indexOf(voltageSourceElement.getBegNodeID()), temporaryListForIndexOnly.indexOf(voltageSourceElement.getEndNodeID()), nodeHashMap.size(), offsetsCounter);
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
    void solveCircuit() {
        stampElement();

        if (!CircuitValidator.passesStaticCircuitInspection(elementList, nodeHashMap)) {
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

        final Collection<CircuitNode> nodesCollection = nodeHashMap.values();

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
        if (solutionVector.length + 1 > nodeHashMap.size()) {
            for (CircuitElement elmIsVSS : elementList) {
                if (Objects.requireNonNull(elmIsVSS) instanceof VoltageSourceElement voltElm) {
                    voltElm.setCurrent(solutionVector[count]);
                    count++;
                }
            }
        }
        reportResultsToConsole();
    }

    @Override
    void reportResultsToConsole(){
        if (computationResultProperty == null) {
            // Should be replaced by a logger!
            System.out.println("The node number(s) displayed in the results below are the same as the node ID.");

            nodeHashMap.forEach((nodeId, node) -> {
                if (!node.isGroundNode()) {
                    System.out.println("The voltage at node " + node.getNodeID() + " (w.r.t ground node " + this.groundNodeIndex + ") is: " + roundValue(node.getNodeVoltage()) + "V");
                }
            });

            for (CircuitElement element : elementList) {
                if (element instanceof VoltageSourceElement voltageSourceElement) {
                    System.out.println("The current of the voltage source " + element.getComponentID() + " is: " + roundValue(voltageSourceElement.getVoltageSourceCurrent()) + "A");
                }
            }

        } else {
            computationResultProperty.set("The node number(s) displayed in the results below are the same as the node ID.");
            computationResultProperty.set("Recall that the voltage at the ground node is 0 V.");

            nodeHashMap.forEach((nodeId, node) -> {
                if (!node.isGroundNode()) {
                    computationResultProperty.set("The voltage at node " + node.getNodeID() + " (w.r.t ground node " + this.groundNodeIndex + ") is: " + roundValue(node.getNodeVoltage()) + "V");
                }
            });

            for (CircuitElement element : elementList) {
                if (element instanceof VoltageSourceElement voltageSourceElement) {
                    computationResultProperty.set("The current of the voltage source " + element.getComponentID() + " is: " + roundValue(voltageSourceElement.getVoltageSourceCurrent()) + "A");
                }
            }
        }
    }



    double[] reportResultsAsVector(){
        return null;
    }
}
