package org.andy.linearj.Circuit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public abstract class AbstractCircuitSolver {
    private final CircuitElement[] elmList;
    private final Map<Integer, CircuitNode> circuitNodeMap;

    protected AbstractCircuitSolver(CircuitElement[] elmList, Map<Integer, CircuitNode> circMap) {
        this.elmList = elmList;
        this.circuitNodeMap = circMap;
    }

    protected static final int PREFERRED_ROUNDING_VALUE = 4;

    protected static double roundNumbers(final double input) {

        if (AbstractCircuitSolver.PREFERRED_ROUNDING_VALUE < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(input);
        return bd.setScale(AbstractCircuitSolver.PREFERRED_ROUNDING_VALUE, RoundingMode.HALF_UP).doubleValue();
    }

    protected Map<Integer, CircuitNode> getCircuitNodeMap() {
        return circuitNodeMap;
    }

    protected CircuitElement[] getCircuitElementArray() {
        return elmList;
    }

    public abstract void solveCircuit();

    abstract void reportResultsToConsole();

}
