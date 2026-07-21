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

import org.andy.linearj.Screen.misc.exception.NegativeNumberException;

public class ResistorElement extends CircuitElement {
    private double resistance;
    private String componentID;

    public ResistorElement(final Integer begNodeID, final Integer endNodeID, final String componentID, final double resistanceVal) {
        super(begNodeID, endNodeID);
        if (resistanceVal < 0) {
            throw new NegativeNumberException("Resistance cannot be negative!");
        } else {
            this.resistance = resistanceVal;
            this.componentID = componentID;
        }
    }

    @Override
    public String getComponentID() {
        return componentID;
    }

    @Override
    public void setComponentID(final String newComponentID) {
        componentID = newComponentID;
    }

    @Override
    public double getElementValue() {
        return resistance;
    }

    @Override
    public void setElementValue(final double newValue) {
        resistance = newValue;
    }

    public double[][] stampSelf(double[][] matrix, int begIndex, int endIndex) {
        double conductance = 1 / getElementValue();

        matrix[begIndex][begIndex] += conductance;
        matrix[endIndex][endIndex] += conductance;
        matrix[begIndex][endIndex] -= conductance;
        matrix[endIndex][begIndex] -= conductance;

        return matrix;
    }
}