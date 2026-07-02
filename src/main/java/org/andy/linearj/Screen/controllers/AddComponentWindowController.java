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
    private void initialize(){
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
    private void cancelButton() {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void okButton() {
        try{
            String choiceOfElement = elementChoice.getSelectionModel().getSelectedItem().substring(0, 1);
            String componentID = setComponentID.getText();

            CircuitElementFactory factory = new CircuitElementFactory();
            switch (choiceOfElement){
                case "R","C","V" ->{
                    Integer begNodeID = Integer.parseInt(setBegNode.getText());
                    Integer endNodeID = Integer.parseInt(setEndNode.getText());
                    Double componentValue = Double.parseDouble(setElementValue.getText());
                    circuitElementObservableList.add(factory.createElement(choiceOfElement,begNodeID,endNodeID,componentValue));
                    elementDataModelObservableList.add(new ElementDataModel(componentID, begNodeID, endNodeID, componentValue));
                }
                case "G" -> {
                    Integer begNodeID = Integer.parseInt(setBegNode.getText());
                    circuitElementObservableList.add(factory.createElement(componentID,begNodeID));
                    elementDataModelObservableList.add(new ElementDataModel(componentID, begNodeID, 0, 0.0));
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

    //TODO DEBUG ONLY --REMOVE ASAP AND REPLACE WITH DEDICATED TESTING!
    @FXML
    private void setDebugNetlist(){
        CircuitElementFactory factory = new CircuitElementFactory();
        //G0
        circuitElementObservableList.add(factory.createElement("G",0));
        elementDataModelObservableList.add(new ElementDataModel("G0",0,0,0.0));
        //I1
        circuitElementObservableList.add(factory.createElement("C",0,1,1.0));
        elementDataModelObservableList.add(new ElementDataModel("I1",0,1,1.0));
        //R2
        circuitElementObservableList.add(factory.createElement("R",1,0,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R2",1,0,1.0));
        //R3
        circuitElementObservableList.add(factory.createElement("R",1,2,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R3",1,2,1.0));
        //R4
        circuitElementObservableList.add(factory.createElement("R",2,0,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R4",2,0,1.0));
        //R5
        circuitElementObservableList.add(factory.createElement("R",2,3,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R5",2,3,1.0));
        //R6
        circuitElementObservableList.add(factory.createElement("R",3,0,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R6",3,0,1.0));
        //R7
        circuitElementObservableList.add(factory.createElement("R",3,4,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R7",3,4,1.0));
        //R8
        circuitElementObservableList.add(factory.createElement("R",4,0,1.0));
        elementDataModelObservableList.add(new ElementDataModel("R8",4,0,1.0));
        //I9
        circuitElementObservableList.add(factory.createElement("C",0,4,1.0));
        elementDataModelObservableList.add(new ElementDataModel("I9",0,4,1.0));

        Stage currentStage = (Stage) okButton.getScene().getWindow();
        currentStage.close();
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

    @FXML
    private void openHelpWindowABM(){
        PopupWindow helpMenu = new ABMHelpWindow();
        helpMenu.openWindow();;
    }

}
