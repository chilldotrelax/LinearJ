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

import java.util.Map;

/*
 * This class implements thevenin analysis to compute the thevenin voltage & resistance.
 */

public final class TheveninSolver extends AbstractCircuitSolver {
    private final SimpleStringProperty computationResultProperty;

    public TheveninSolver(CircuitElement[] input, Map<Integer, CircuitNode> circuitNodeHashMap) {
        super(input, circuitNodeHashMap);
        this.computationResultProperty = null;

    }

    public TheveninSolver(CircuitElement[] input, Map<Integer, CircuitNode> circuitNodeHashMap, SimpleStringProperty computationResultPropertyInput) {
        super(input, circuitNodeHashMap);
        this.computationResultProperty = computationResultPropertyInput;
    }

    private void collapseCircuit(int firstNode, int secondNode) {
        final CircuitElement[] elmList = super.getCircuitElementArray();
        final Map<Integer, CircuitNode> circuitNodeMap = super.getCircuitNodeMap();

        // Replace voltage source element with short and replace current source with open
        // To do this, we simply replace the voltage element with a resistor with a value of Double.MIN_VALUE.
        // And for opens, we replace the current source element with a resistor that has a value of ~ Infinity or some other solution.
        // instance of spam might be a red flag of abstraction that is leaking.

        // replace element with either a special resistor or wire element.

        for (CircuitElement elm : elmList) {
            final var doubMax = Double.MAX_VALUE;

            // TODO verify validaty of this solution.
            if (elm instanceof VoltageSourceElement) {
                circuitNodeMap.get(elm.getBegNodeID()).replaceElement(CircuitElementSimpleFactory.createElement(elm.getComponentID(), elm.getBegNodeID(), elm.getEndNodeID(), Double.MIN_VALUE));
                circuitNodeMap.get(elm.getEndNodeID()).replaceElement(CircuitElementSimpleFactory.createElement(elm.getComponentID(), elm.getBegNodeID(), elm.getEndNodeID(), Double.MIN_VALUE));
            } else if (elm instanceof CurrentSourceElement) {
                circuitNodeMap.get(elm.getBegNodeID()).replaceElement(CircuitElementSimpleFactory.createElement(elm.getComponentID(), elm.getBegNodeID(), elm.getEndNodeID(), doubMax));
                circuitNodeMap.get(elm.getEndNodeID()).replaceElement(CircuitElementSimpleFactory.createElement(elm.getComponentID(), elm.getBegNodeID(), elm.getEndNodeID(), doubMax));
            }
        }

    }

    @Override
    public void solveCircuit() {
        final CircuitElement[] elmList = super.getCircuitElementArray();
        final Map<Integer, CircuitNode> circuitNodeMap = super.getCircuitNodeMap();

        AbstractCircuitSolver mnaSolver = new MNASolver(elmList, circuitNodeMap);

        // For MNA solver only
//        final double[] solution = mnaSolver.solveCircuit();
        // Instead, it should be using the get solution vector method.
    }

    @Override
    void reportResultsToConsole() {
        if (computationResultProperty == null) {
            throw new IllegalStateException();
        }

        computationResultProperty.set("The thevenin voltage is: ");
        computationResultProperty.set("The thevenin resistance is: ");

    }

}
