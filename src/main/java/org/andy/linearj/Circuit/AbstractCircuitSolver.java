package org.andy.linearj.Circuit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public abstract class AbstractCircuitSolver {
    private final AbstractCircuitElement[] elmList;
    private final Map<Integer, CircuitNode> circuitNodeMap;

    protected AbstractCircuitSolver(AbstractCircuitElement[] elmList, Map<Integer, CircuitNode> circMap) {
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

    protected AbstractCircuitElement[] getCircuitElementArray() {
        return elmList.clone();
    }

    public abstract void solveCircuit();

    abstract void reportResultsToConsole();

}
