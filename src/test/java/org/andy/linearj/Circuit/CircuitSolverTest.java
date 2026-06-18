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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CircuitSolverTest {
    private CircuitElementFactory factory;
    private CircuitSolver solve;


    @BeforeEach
    void setupEnv(){
        factory = new CircuitElementFactory();

        //TODO Future goal: Implement a smarter circuit element & node factory.

        //I1
        CircuitElement currentElement1 = factory.createElement("I", 0, 1, 1.0);
        //I2
        CircuitElement currentElement2 = factory.createElement("I",0,4,1.0);
        //R1
        CircuitElement resistorElement1 = factory.createElement("R",1,0,1.0);
        //R2
        CircuitElement resistorElement2 = factory.createElement("R",1,2,1.0);
        //R3
        CircuitElement resistorElement3 = factory.createElement("R",2,0,1.0);
        //R4
        CircuitElement resistorElement4 = factory.createElement("R",2,3,1.0);
        //R5
        CircuitElement resistorElement5 = factory.createElement("R",3,0,1.0);
        //R6
        CircuitElement resistorElement6 = factory.createElement("R",3,4,1.0);
        //R7
        CircuitElement resistorElement7 = factory.createElement("R",4,0,1.0);


        ArrayList<CircuitElement> circuitElements = new ArrayList<>();

        circuitElements.add(currentElement1);
        circuitElements.add(currentElement2);
        circuitElements.add(resistorElement1);
        circuitElements.add(resistorElement2);
        circuitElements.add(resistorElement3);
        circuitElements.add(resistorElement4);
        circuitElements.add(resistorElement5);
        circuitElements.add(resistorElement6);
        circuitElements.add(resistorElement7);

        ArrayList<CircuitNode> nodes = new ArrayList<>();
        CircuitNode node0 = new CircuitNode(0,circuitElements,true);
        CircuitNode node1 = new CircuitNode(1,circuitElements,false);
        CircuitNode node2 = new CircuitNode(2,circuitElements,false);
        CircuitNode node3 = new CircuitNode(3,circuitElements,false);
        CircuitNode node4 = new CircuitNode(4,circuitElements,false);

        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);

        solve = new CircuitSolver(circuitElements,nodes);

    }


    @Test
    @DisplayName("Solve and output result")
    void solveCircuit() {
        solve.stampElement();
        try{
            solve.solveCircuit();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}