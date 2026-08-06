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

package org.andy.linearj.Screen.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.andy.linearj.Circuit.CircuitNode;

import java.util.Map;

public class NodePickerWindowController {
    @FXML
    private ChoiceBox<Integer> firstNodeChoiceBox;
    @FXML
    private ChoiceBox<Integer> secondNodeChoiceBox;

    private SimpleStringProperty computationResultProperty;

    @FXML
    private void initialize() {
        //
    }

    public void populateChoiceBoxes(Map<Integer, CircuitNode> circuitNodeMap) {
        if (circuitNodeMap.isEmpty()) {
            return;
        }

        circuitNodeMap.forEach((nodeID, node) -> {
            firstNodeChoiceBox.getItems().add(nodeID);
            secondNodeChoiceBox.getItems().add(nodeID);
        });

    }

    @FXML
    private void solveTheveninBtn() {
        final var selectedItemFirst = firstNodeChoiceBox.getSelectionModel().getSelectedItem();
        final var selectedItemSecond = secondNodeChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedItemFirst.equals(selectedItemSecond)) {
            ErrorWindows.displayError("You may not choose the same two nodes.");
            return;
        }

//        AbstractCircuitSolver thevSolver = new TheveninSolver(null, null);
        
        closeWindow();
    }

    @FXML
    private void closeWindow() {
        var currentStage = (Stage) firstNodeChoiceBox.getScene().getWindow();
        currentStage.close();
    }

}