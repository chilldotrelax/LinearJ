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
import org.andy.linearj.Screen.misc.ErrorWindows;

public class AddComponentWindowController {
    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private ChoiceBox<String> elementChoice;
    @FXML
    private TextField setBegNode;
    @FXML
    private TextField setEndNode;
    @FXML
    private TextField setElementValue;
    private ObservableList<ElementDataModel> elementDataModelObservableList;

    public void setElementDataModelObservableList(ObservableList elementDataModelObservableList) {
        this.elementDataModelObservableList = elementDataModelObservableList;
    }

    @FXML
    public void cancelButton() {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void okButton() {
        //No input validation
        try {
            String choiceOfElement = elementChoice.getSelectionModel().getSelectedItem().substring(0, 1);
            Integer begNodeID = Integer.parseInt(setBegNode.getText());
            Integer endNodeID = Integer.parseInt(setEndNode.getText());
            Double componentValue = Double.parseDouble(setElementValue.getText());

            elementDataModelObservableList.add(new ElementDataModel(choiceOfElement, begNodeID, endNodeID, componentValue));
            Stage currentStage = (Stage) okButton.getScene().getWindow();
            currentStage.close();
        } catch (IllegalArgumentException e) {
            ErrorWindows.displayError("Bad arguments. Please try again.");
        }
    }

//    private boolean isValidArgument(String choice, Integer beg, Integer end, Double comp) {
//        if (choice.isEmpty() || beg.toString().isEmpty() || end.toString().isEmpty() || comp.toString().isEmpty()) {
//            return false;
//        }
//        return true;
//    }
}
