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

import org.andy.linearj.Screen.misc.exception.FloatingNodeException;

import java.util.ArrayList;
import java.util.List;

public class CircuitNode {
    private final int nodeID;
    private boolean isGroundNode;
    private double nodeVoltage;
    private final List<CircuitElement> elementsConnected = new ArrayList<>() ;

    public CircuitNode(int nodeID, List<CircuitElement> elements, boolean isGroundNodeFlag) {
        this.nodeID = nodeID;

        for (CircuitElement element: elements){
            if (element.getBegNodeID() == nodeID || element.getEndNodeID() == nodeID){
                elementsConnected.add(element);
            }
        }
        this.isGroundNode = isGroundNodeFlag;
    }

    public CircuitNode(int nodeID, List<CircuitElement> elements, boolean isGroundNodeFlag, double nodeVoltage) {
        this.nodeID = nodeID;
        for (CircuitElement element: elements){
            if (element.getBegNodeID() == nodeID || element.getEndNodeID() == nodeID){
                elementsConnected.add(element);
            }
        }
        this.isGroundNode = isGroundNodeFlag;
        this.nodeVoltage = nodeVoltage;
    }

    public final boolean isGroundNode() {return isGroundNode;}
    public final int getNodeID() {return nodeID;}
    public final double getNodeVoltage(){return nodeVoltage;}
    public final void setNodeVoltage(double newVoltage){nodeVoltage = newVoltage;}
    public final List<CircuitElement> getElementsConnected() {return elementsConnected;}

    public final boolean isFloatingNode() {
        return elementsConnected.size() <= 1;
    }

    public void removeElement(CircuitElement particularElement) throws IllegalArgumentException {
        for (CircuitElement element : elementsConnected) {
            if (element == particularElement && (particularElement.getBegNodeID() == nodeID || particularElement.getEndNodeID() == nodeID)) {
                elementsConnected.remove(element);
            } else {
                throw new IllegalArgumentException("Could not find the element in this node");
            }
        }
    }

    public void addElement(CircuitElement particularElement) {
        elementsConnected.add(particularElement);
    }

    public final void setGroundNode(boolean flag) throws FloatingNodeException {
        if (isFloatingNode()) {
            isGroundNode = flag;
        } else {
            throw new FloatingNodeException("Cannot assign a floating node as ground node ");
        }
    }
}
