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

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.andy.linearj.Circuit.CircuitElement;
import org.andy.linearj.Circuit.CircuitElementSimpleFactory;

public final class AddComponentWindowController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private ChoiceBox<String> elementChoiceBox;
    @FXML
    private TextField setComponentID;
    @FXML
    private TextField setBegNode;
    @FXML
    private TextField setEndNode;
    @FXML
    private TextField setElementValue;
    @FXML
    private Label setBegWarning, setEndWarning, setValueWarning;
    @FXML
    private String genResElm, indVolDCElm, indCurrDCElm, groundElm;

    private ObservableList<ElementDataModel> elementDataModelObservableList;
    private ObservableList<CircuitElement> circuitElementObservableList;

    public void setElementDataModelObservableList(ObservableList<ElementDataModel> elementDataModelObservableList) {
        if (elementDataModelObservableList != null) {
            this.elementDataModelObservableList = elementDataModelObservableList;
        }
    }

    public void setCircuitElementObservableList(ObservableList<CircuitElement> circuitElementObservableList) {
        if (circuitElementObservableList != null) {
            this.circuitElementObservableList = circuitElementObservableList;
        }
    }

    @FXML
    private void initialize() {

        elementChoiceBox.setOnAction(event -> {
            if (elementChoiceBox.getSelectionModel().getSelectedItem().equals(groundElm)) {
                setEndNode.setDisable(true);
                setElementValue.setDisable(true);
            } else {
                setEndNode.setDisable(false);
                setElementValue.setDisable(false);
            }
        });

        setBegNode.setOnKeyPressed(keyPressed -> {
            setBegWarning.setVisible(false);
            okButton.setDisable(false);
            if (keyPressed.getCode().isLetterKey()) {
                setBegNode.clear();
                setBegWarning.setVisible(true);
                okButton.setDisable(true);
            }
        });

        setEndNode.setOnKeyPressed(keyPressed -> {
            setEndWarning.setVisible(false);
            okButton.setDisable(false);
            if (keyPressed.getCode().isLetterKey()) {
                setEndWarning.setVisible(true);
                setEndNode.clear();
                okButton.setDisable(true);
            }
        });

        setElementValue.setOnKeyPressed(keyPressed -> {
            okButton.setDisable(false);
            setValueWarning.setVisible(false);
            if (keyPressed.getCode().isLetterKey()) {
                setValueWarning.setVisible(true);
                setElementValue.clear();
                okButton.setDisable(true);
            }
        });

    }

    @FXML
    private void cancelButton() {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void okButton() {
        try {
            var choiceOfElement = elementChoiceBox.getSelectionModel().getSelectedItem().substring(0, 1); //TODO check this statement
            var componentID = setComponentID.getText();

            //TODO Rewwrite this switch statement to increase maintainability (in case new elements are added).
            switch (choiceOfElement) {
                case "R", "I", "V" -> {
                    var begNodeID = Integer.parseInt(setBegNode.getText());
                    var endNodeID = Integer.parseInt(setEndNode.getText());
                    var componentValue = Double.parseDouble(setElementValue.getText());
                    circuitElementObservableList.add(CircuitElementSimpleFactory.createElement(choiceOfElement, begNodeID, endNodeID, componentValue));
                    elementDataModelObservableList.add(ElementDataModelSimpleFactory.createElement(componentID, begNodeID, endNodeID, componentValue));
                }
                case "G" -> {
                    var begNodeID = Integer.parseInt(setBegNode.getText());
                    circuitElementObservableList.add(CircuitElementSimpleFactory.createElement(componentID, begNodeID, null, null));
                    elementDataModelObservableList.add(ElementDataModelSimpleFactory.createElement(componentID, begNodeID, 0, 0.0));
                }
                default -> ErrorWindows.displayError("Something went wrong while creating objects.");
            }

            var currentStage = (Stage) okButton.getScene().getWindow();
            currentStage.close();
        } catch (NumberFormatException e) {
            ErrorWindows.displayError("Invalid or empty arguments detected. Please check and try again.");
        }
    }

    @FXML
    private void setDebugNetlist() {
        //G0
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("G1", 0, null, null));
        elementDataModelObservableList.add(ElementDataModelSimpleFactory.createElement("G0", 0, 0, 0.0));
        //I1
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("I1", 0, 1, 1.0));
        elementDataModelObservableList.add(ElementDataModelSimpleFactory.createElement("I1", 0, 1, 1.0));
        //R2
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R2", 1, 0, 1.0));
        elementDataModelObservableList.add(ElementDataModelSimpleFactory.createElement("R2", 1, 0, 1.0));
        //R3
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R3", 1, 2, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R3", 1, 2, 1.0));
        //R4
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R4", 2, 0, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R4", 2, 0, 1.0));
        //R5
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R5", 2, 3, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R5", 2, 3, 1.0));
        //R6
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R6", 3, 0, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R6", 3, 0, 1.0));
        //R7
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R7", 3, 4, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R7", 3, 4, 1.0));
        //R8
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("R8", 4, 0, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("R8", 4, 0, 1.0));
        //I9
        circuitElementObservableList.add(CircuitElementSimpleFactory.createElement("I9", 0, 4, 1.0));
        elementDataModelObservableList.add(new ElementDataModel("I9", 0, 4, 1.0));

        var currentStage = (Stage) okButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void openHelpWindowABM() {
        PopupWindow helpMenu = new ABMHelpWindow();
        helpMenu.openWindow();
    }

}