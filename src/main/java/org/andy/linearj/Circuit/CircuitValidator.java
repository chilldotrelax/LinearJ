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

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class CircuitValidator {
    private CircuitValidator() {
    }

    public static boolean passesStaticCircuitInspection(AbstractCircuitElement[] elementsArray, Map<Integer, CircuitNode> circuitNodeMap) {
        int count = 0;

        if (circuitAtLeastOneGroundNode(elementsArray)) {
            count += 1;
        }

        for (AbstractCircuitElement element : elementsArray) {
            if (!isElmShort(element) && circuitAtLeastOneGroundNode(elementsArray)) {
                count++;
            }
        }

        if (noFloatingNodes(circuitNodeMap)) {
            count += 1;
        }

        return (count > 0);
    }

    private static boolean isElmShort(AbstractCircuitElement element) {
        return (Objects.equals(element.getBegNodeID(), element.getEndNodeID()));
    }

    private static boolean circuitAtLeastOneGroundNode(AbstractCircuitElement[] elementsArray) {
        int groundNodesInElementArray = 0;

        for (AbstractCircuitElement element : elementsArray) {
            if (element instanceof GroundElement) {
                groundNodesInElementArray += 1;
            }
        }
        return (groundNodesInElementArray == 1);
    }


    // TODO remove this method, or at least make it much more "deep".
    private static boolean noFloatingNodes(Map<Integer, CircuitNode> circuitNodeMap) {
        final Collection<CircuitNode> nodeCollection = circuitNodeMap.values();

        for (CircuitNode node : nodeCollection) {
            if (node.isFloatingNode()) {
                return false;
            }
        }
        return true;
    }

}