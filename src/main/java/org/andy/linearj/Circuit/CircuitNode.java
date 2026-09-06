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

public final class CircuitNode {
    private final int nodeID;
    private boolean isGroundNode;
    private double nodeVoltage;
    private final List<AbstractCircuitElement> elementsConnected;

    public CircuitNode(int nodeID) {
        this.nodeID = nodeID;
        this.elementsConnected = new ArrayList<>();
        this.isGroundNode = false;
    }

    public int getNodeID() {
        return nodeID;
    }

    public double getNodeVoltage() {
        return nodeVoltage;
    }

    public void setNodeVoltage(double volt) {
        this.nodeVoltage = volt;
    }

    public boolean isFloatingNode() {
        return elementsConnected.size() <= 1;
    }

    public boolean isGroundNode() {
        return isGroundNode;
    }

    public void addElement(AbstractCircuitElement particularElement) {
        if (particularElement == null || !particularElement.isNodeIDEqual(nodeID)){
            return;
        }

        if (particularElement instanceof GroundElement) {
            this.isGroundNode = true;
        }

        elementsConnected.add(particularElement);
    }

    public void removeElement(AbstractCircuitElement particularElement) throws IllegalArgumentException {
        if ((particularElement != null) && (particularElement.getBegNodeID() == nodeID || particularElement.getEndNodeID() == nodeID)) {
            elementsConnected.remove(particularElement);
        } else {
            throw new IllegalArgumentException("Unable to find the element in this node or the element might be null.");
        }
    }

    public void replaceElement(AbstractCircuitElement newElement) {
        if (newElement == null) {
            throw new IllegalArgumentException();
        }

        int count = 0;
        for (AbstractCircuitElement elm : elementsConnected) {
            if ((newElement.isNodeIDEqual(elm.getBegNodeID()) || newElement.isNodeIDEqual(elm.getEndNodeID()) && (count <= 1))) {
                elementsConnected.remove(elm);
                elementsConnected.add(newElement);
                count++;
            }
        }
    }

    public void replaceElementNew (AbstractCircuitElement oldElm , AbstractCircuitElement newElement){
        if (oldElm == null || newElement == null){
            throw new IllegalArgumentException();
        }

        this.removeElement(oldElm);
        this.addElement(newElement);

    }

    public boolean contains(AbstractCircuitElement particularElement) {
        return elementsConnected.contains(particularElement);
    }

    public List<AbstractCircuitElement> getElementsConnected(){
        return List.copyOf(elementsConnected);
    }

}