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

import java.util.ArrayList;
import java.util.List;

public class CircuitNode {
    private final int nodeID;
    private boolean isGroundNode = false;
    private double nodeVoltage;
    private final List<CircuitElement> elementsConnected;

    public CircuitNode(int nodeID) {
        this.nodeID = nodeID;
        this.elementsConnected = new ArrayList<>();
    }

    public final boolean isGroundNode() {return isGroundNode;}
    public final int getNodeID() {return nodeID;}
    public final double getNodeVoltage(){return nodeVoltage;}
    public final void setNodeVoltage(double volt){this.nodeVoltage = volt;}

    public final boolean isFloatingNode() {return elementsConnected.size() <= 1;}

    public void removeElement(CircuitElement particularElement) throws IllegalArgumentException {

        if ((particularElement.getBegNodeID() == nodeID || particularElement.getEndNodeID() == nodeID)){
            elementsConnected.remove(particularElement);
        }
        else{
            throw new IllegalArgumentException("Could not find the element in this node");
        }
    }

    public void addElement(CircuitElement particularElement) {
        if (particularElement instanceof GroundElement){
            elementsConnected.add(particularElement);
            this.isGroundNode = true;
        }
        else{
            elementsConnected.add(particularElement);
        }
    }

    public boolean contains(CircuitElement particularElement) {return elementsConnected.contains(particularElement);}

}
