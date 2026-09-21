package org.andy.linearj.Circuit;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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


/*
 * This class implements thevenin analysis to compute the thevenin voltage & resistance.
 */

public final class TheveninSolver extends AbstractCircuitSolver {
    private final SimpleStringProperty computationResultProperty;

    private final int firstNode;
    private final int secondNode;

    private double thevResistance;
    private double thevVoltage;

    private final AbstractCircuitElement[] elmList;
    private final List<AbstractCircuitElement> elmListAsArrayList = new ArrayList<>();
    private final Map<Integer, CircuitNode> circuitNodeMap;

    // overload 1
    public TheveninSolver(
            AbstractCircuitElement[] input,
            Map<Integer, CircuitNode> circuitNodeHashMap,
            final int firstNode,
            final int secondNode) {
        super(input, circuitNodeHashMap);

        this.elmList = super.getCircuitElementArray();
        this.circuitNodeMap = super.getCircuitNodeMap();

        this.computationResultProperty = null;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    // overload 2
    public TheveninSolver(
            AbstractCircuitElement[] input,
            Map<Integer, CircuitNode> circuitNodeHashMap,
            SimpleStringProperty computationResultPropertyInput,
            final int firstNode,
            final int secondNode) {
        super(input, circuitNodeHashMap);

        this.elmList = super.getCircuitElementArray();
        this.circuitNodeMap = super.getCircuitNodeMap();

        this.computationResultProperty = computationResultPropertyInput;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    /*
     * The collapseCircuit() method 'shrinks' the circuit by doing the following:
     * 1) Shorting any DC voltage sources and/or open any DC current source to DC.
     * 2) Simplify any resistor networks down to one resistor that runs across the terminals of the
     * desired two nodes.
     * 3) Creating a temporary netlist, which will act as the main source of information for the
     * solveCircuit() method.
     */

    private void collapseCircuit() {
        Collections.addAll(elmListAsArrayList, elmList);
        // Apply modifications to voltage and/or current sources
        // replace element with either a special resistor (of infinite resistance) or wire element (no
        // resistance).

        // This enhanced for-loop replaces any element with either a resistor with zero resistance or
        // a resistor with infinite resistance, depending on what type of source element it is
        // (voltage or current source element)

        for (AbstractCircuitElement elm : elmList) {
            final int begNodeID = elm.getBegNodeID();
            final int endNodeID = elm.getEndNodeID();

            if (elm instanceof VoltageSourceElement) {
                circuitNodeMap.forEach(
                        (integer, circuitNode) -> {
                            if (integer == begNodeID || integer == endNodeID) {
                                AbstractCircuitElement resistorElm1 =
                                        new ResistorElement(
                                                begNodeID, endNodeID, elm.getComponentID() + " RP", 0);
                                circuitNode.replaceElement(resistorElm1);
                                elmListAsArrayList.remove(elm);
                                elmListAsArrayList.add(resistorElm1);
                            }
                        });
            } else if (elm instanceof CurrentSourceElement) {
                circuitNodeMap.forEach(
                        ((integer, circuitNode) -> {
                            if (integer == begNodeID || integer == endNodeID) {
                                AbstractCircuitElement resistorElm2 =
                                        new ResistorElement(
                                                begNodeID,
                                                endNodeID,
                                                elm.getComponentID() + " RP",
                                                Double.MAX_VALUE);
                                circuitNode.replaceElement(resistorElm2);
                                elmListAsArrayList.remove(elm);
                                elmListAsArrayList.add(elm);
                            }
                        }));
            }
        }

        // here, we perform the actual compaction. Now that the circuit is source-free, we can focus on
        // reducing the circuit down to only one resistor across the two desired nodes (aka Rth). Once
        // we have that, we can report the value!

        // if the resistor shares at least one node and no other element is in the node (other than the
        // infinite or zero resistor), combine them.

        // it may be easier to combine all the series elements first, then move onto compressing all the
        // parallel elements, except that contains the same beg and end node as that of the desired two
        // nodes as required by the thevenin solver.

        circuitNodeMap.forEach(
                ((integer, circuitNode) -> {
                    final List<AbstractCircuitElement> temp = circuitNode.getElementsConnected();

                    int resistorCount = 0;
                    double newCombinedSeriesResistance = 0.0;
                    double newCombinedParallelResistance = 0.0;

                    int tempCount = 0;
                    for (AbstractCircuitElement element : temp) {
                        if (element instanceof ResistorElement) {
                            tempCount++;
                        }
                        if (tempCount > 2) {
                            return;
                        }
                    }
                    // Series traversal and combination
                    for (AbstractCircuitElement element : temp) {
                        if (element instanceof ResistorElement && tempCount == 2) {
                            var combinedResistorElement =
                                    new ResistorElement(
                                            element.getBegNodeID(),
                                            element.getEndNodeID(),
                                            "R_Combined_New_Series_" + Math.random(),
                                            newCombinedSeriesResistance);
                            circuitNode.replaceElementNew(element, combinedResistorElement);

                            elmListAsArrayList.remove(element);
                            elmListAsArrayList.add(combinedResistorElement);
                        }
                    }

                    // Parallel traversal and combination
                    for (AbstractCircuitElement element : temp) {
                        if (element instanceof ResistorElement) {
                            for (AbstractCircuitElement element2 : temp) {
                                if (element != element2
                                        && element.isNodeIDEqual(element2.getBegNodeID())
                                        && element.isNodeIDEqual(element2.getEndNodeID())) {
                                    newCombinedParallelResistance +=
                                            1 / ((1 / element.getElementValue()) + (1 / element2.getElementValue()));
                                    var resistorCombinedParaElm =
                                            new ResistorElement(
                                                    element.getBegNodeID(),
                                                    element.getEndNodeID(),
                                                    "R_Combined_New_Parallel_" + Math.random(),
                                                    newCombinedParallelResistance);

                                    circuitNode.replaceElementNew(element, resistorCombinedParaElm);
                                    circuitNode.removeElement(element2);

                                    elmListAsArrayList.remove(element);
                                    elmListAsArrayList.remove(element2);
                                    elmListAsArrayList.add(resistorCombinedParaElm);
                                }
                            }
                        }
                    }
                    this.thevResistance = newCombinedParallelResistance;
                }));

        // Next, some validation to ensure that all the circuit components were replaced correctly.
//        if (circuitNodeMap.values().iterator(). != 1){
//
//        }
//        else{
//            ErrorWindows.displayError("Unable to compact circuit!");
//        }
    }

    @Override
    public void solveCircuit() {
        final AbstractCircuitSolver solveCircuitFirstStep = new MNASolver(super.getCircuitElementArray(), super.getCircuitNodeMap());

        solveCircuitFirstStep.solveCircuit();

        solveCircuitFirstStep.getCircuitNodeMap().values().forEach((circuitNode -> {
            if (circuitNode.getNodeID() == this.firstNode || circuitNode.getNodeID() == this.secondNode) {
                this.thevVoltage = circuitNode.getNodeVoltage();
            }
        }));

        reportResultsToConsole();
    }

    @Override
    void reportResultsToConsole() {
        if (computationResultProperty == null) {
            throw new IllegalStateException();
        }

        computationResultProperty.set("The Thevenin voltage is: " + this.thevVoltage);
        computationResultProperty.set("The Thevenin resistance is: " + this.thevResistance);
    }

}

