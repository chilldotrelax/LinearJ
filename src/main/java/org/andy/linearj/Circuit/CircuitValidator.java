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

import java.util.Map;
import java.util.Objects;

/*
 * This class was designed to help users evaluate complete circuit designs statically before the solver runs.
 * It is designed to catch basic mistakes such as shorts, opens, or floating elements.
 * The reason why there are several methods is that they can be used in the future to perform one-off checks as needed.
 * You might ask: why can't the elements check themselves at run time? The answer is: I don't know!
 * This is a **very** early implementation; and will be heavily improved upon as time goes on!
 */

public final class CircuitValidator {
    private CircuitValidator() {}
    // right now, there are NO checks for the node hash map; they will be added in later.
    public static boolean passesStaticCircuitInspection(CircuitElement[] elementsArray, Map < Integer, CircuitNode > circuitNodeMap) {
        int count = 0;

        // check no. 1
        if (circuitAtLeastOneGroundNode(elementsArray)) {
            count += 1;
        }

        // check no. 2
        for (CircuitElement element: elementsArray) {
            if (!isElmShort(element) && !isElmFloating(element) && circuitAtLeastOneGroundNode(elementsArray)) {
                count++;
            }
        }

        return (count > 0);
    }

    private static boolean isElmShort(CircuitElement element) {
        return (Objects.equals(element.getBegNodeID(), element.getEndNodeID()));
    }

    private static boolean isElmFloating(CircuitElement element) {
        return (element.getBegNodeID() == null || element.getEndNodeID() == null);
    }

    // but no more than one!
    private static boolean circuitAtLeastOneGroundNode(CircuitElement[] elementsArray) {
        int groundNodesInElementArray = 0;

        for (CircuitElement element: elementsArray) {
            if (element instanceof GroundElement) {
                groundNodesInElementArray += 1;
            }
        }
        return (groundNodesInElementArray == 1);
    }
}