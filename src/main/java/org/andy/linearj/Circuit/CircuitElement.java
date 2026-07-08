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

import java.util.Objects;

@SuppressWarnings("ALL")

public abstract class CircuitElement {
    private Integer beginningNodeID;
    private Integer endNodeID;

    protected CircuitElement(Integer begNode){
        if (begNode.toString().isEmpty()){
            throw new IllegalArgumentException("Empty arguments");
        }
        else{
            this.beginningNodeID = begNode;
        }
    }

    protected CircuitElement(Integer begNode, Integer endNode){
        if (begNode.toString().isEmpty() || endNode.toString().isEmpty()){
            throw new IllegalArgumentException("Empty arguments @ begNode or endNode.");
        }
        else{
            this.beginningNodeID = begNode;
            this.endNodeID = endNode;
        }
    }

    public final Integer getBegNodeID(){ return beginningNodeID; }
    public final Integer getEndNodeID(){ return endNodeID;}
    public final boolean isNodeIDEqual(Integer nodeID){return (Objects.equals(beginningNodeID, nodeID) || Objects.equals(endNodeID, nodeID));}
    public final void setBeginningNode(Integer newBegNode){beginningNodeID = newBegNode;}
    public final void setEndNode(Integer newEndNode){endNodeID = newEndNode;}
    public abstract double getElementValue();
    public abstract void setElementValue(double newValue);
    public abstract String getComponentID();
    public abstract void setComponentID(String newComponentID);
    public abstract double calculateCurrent(double[] voltage);
    public abstract double calculateVoltage(double current);

}
