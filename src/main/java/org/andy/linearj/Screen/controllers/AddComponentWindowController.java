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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.andy.linearj.Circuit.CircuitElement;
import org.andy.linearj.Circuit.CircuitElementFactory;
import org.andy.linearj.Screen.misc.ErrorWindows;


public class AddComponentWindowController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private ChoiceBox<String> elementChoice;
    @FXML
    private TextField setComponentID;
    @FXML
    private TextField setBegNode;
    @FXML
    private TextField setEndNode;
    @FXML
    private TextField setElementValue;

    private ObservableList<ElementDataModel> elementDataModelObservableList;
    private ObservableList<CircuitElement> circuitElementObservableList;

    @FXML
    public void initialize(){
        elementChoice.setOnAction(event -> {
            if (elementChoice.getSelectionModel().getSelectedItem().charAt(0) == 'G'){
                setEndNode.setDisable(true);
                setElementValue.setDisable(true);
            }
            else{
                setEndNode.setDisable(false);
                setElementValue.setDisable(false);
            }
        });
    }

    @FXML
    public void cancelButton() {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void okButton() {
        try{
            String choiceOfElement = elementChoice.getSelectionModel().getSelectedItem().substring(0, 1);
            String componentID = setComponentID.getText();
            Integer begNodeID = Integer.parseInt(setBegNode.getText());
            Integer endNodeID = Integer.parseInt(setEndNode.getText());
            Double componentValue = Double.parseDouble(setElementValue.getText());

            CircuitElementFactory factory = new CircuitElementFactory();
            switch (choiceOfElement){
                case "R","C","V" ->{
                    circuitElementObservableList.add(factory.createElement(choiceOfElement,begNodeID,endNodeID,componentValue));
                    elementDataModelObservableList.add(new ElementDataModel(componentID, begNodeID, endNodeID, componentValue));
                }
                case "G" -> {
                    circuitElementObservableList.add(factory.createElement(componentID,begNodeID));
                    elementDataModelObservableList.add(new ElementDataModel(componentID, begNodeID, endNodeID, componentValue));
                }
                default ->
                        ErrorWindows.displayError("Something went wrong while creating objects.");
            }
            Stage currentStage = (Stage) okButton.getScene().getWindow();
            currentStage.close();
        }
        catch (NumberFormatException e){
            ErrorWindows.displayError("Invalid or empty arguments detected. Please check and try again.");
        }
    }
    public void setElementDataModelObservableList(ObservableList<ElementDataModel> elementDataModelObservableList) {
        if (elementDataModelObservableList != null) {
            this.elementDataModelObservableList = elementDataModelObservableList;
        }
    }

    public void setCircuitElementObservableList(ObservableList<CircuitElement> circuitElementObservableList){
        if (circuitElementObservableList != null){
            this.circuitElementObservableList = circuitElementObservableList;
        }
    }

}
