package org.andy.linearj.Circuit;

import java.util.Map;

public class NortonSolver extends AbstractCircuitSolver {
    private final int firstNodeID;
    private final int secondNodeID;

    private int NortResistance;
    private int nortVoltage;

    public NortonSolver(final AbstractCircuitElement[] input, final Map<Integer, CircuitNode> circuitNodeHashMap, final int firstNode, final int secondNode){
        super(input,circuitNodeHashMap);

        this.firstNodeID = firstNode;
        this.secondNodeID = secondNode;

    }

    @Override
    public void solveCircuit() {

    }

    @Override
    void reportResultsToConsole() {

    }
}
