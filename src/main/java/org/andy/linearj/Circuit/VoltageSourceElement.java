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

public class VoltageSourceElement extends CircuitElement{
    private double voltage;
    private double current;
    private String componentID;

    public VoltageSourceElement(int begNode, int endNode, String componentID, double voltage){
        super(begNode, endNode);
        this.componentID = componentID;
        this.voltage = voltage;
    }

    @Override
    public String getComponentID(){return componentID;}

    @Override
    public void setComponentID(final String newComponentID) {componentID = newComponentID;}

    @Override
    public double getElementValue(){return voltage;}

    @Override
    public void setElementValue(final double newValue){
        voltage = newValue;
    }

    public void setCurrent(final double newCurrent){current = newCurrent;}

    public double[] stampSelf(double[] rightHandVector,int preShiftedIndex,int selfOffset){
        rightHandVector[preShiftedIndex + selfOffset] += voltage;
        return rightHandVector;
    }
    
    public double[][] stampToGlobalMatrix(double[][] resistorMatrix, int begNode, int endNode, int preShiftIndex, int offset){
        resistorMatrix[preShiftIndex + offset][begNode] += 1;
        resistorMatrix[preShiftIndex + offset][endNode] -= 1;
        resistorMatrix[begNode][preShiftIndex + offset] += 1;
        resistorMatrix[endNode][preShiftIndex + offset] -= 1;

        return resistorMatrix;
    }

}
