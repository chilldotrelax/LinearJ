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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import org.andy.linearj.Circuit.CircuitElement;

public class ParentWindowController {
    @FXML
    private MatrixCalculatorUnitController matrixCalculatorUnitController;
    @FXML
    private NetlistUnitController netlistUnitController;
    @FXML
    private OutputUnitController outputConsoleController;
    @FXML
    private CircuitSolverUnitController circuitSolverUnitController;
    @FXML
    private VBox debugVBox;
    @FXML
    private CheckBox debugUtilsCheckbox;

    private ObservableList<ElementDataModel> elementDataModelObservableList;
    private ObservableList<CircuitElement> circuitElementObservableList;

    public ParentWindowController(){
        this.elementDataModelObservableList = FXCollections.observableArrayList();
        this.circuitElementObservableList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        netlistUnitController.setObservableLists(elementDataModelObservableList,circuitElementObservableList);
        circuitSolverUnitController.setObservableLists(elementDataModelObservableList,circuitElementObservableList);

        matrixCalculatorUnitController.computationResultProperty().addListener(((observable, oldValue, newValue) -> {outputConsoleController.setOutputBox(newValue);}));
        circuitSolverUnitController.computationOutputProperty().addListener(((observable, oldValue, newValue) -> {outputConsoleController.setOutputBox(newValue);}));
        netlistUnitController.getClrNetlistOutputProperty().addListener((observable, oldValue, newValue) -> {outputConsoleController.setOutputBox(newValue);});
    }

    @FXML
    private void setDebugUtilsVisibility(){
        debugVBox.setVisible(true);
        if (!debugUtilsCheckbox.isSelected()){debugVBox.setVisible(false);}
    }

    @FXML
    private void triggerAboutMenu() {
        PopupWindow aboutWindow = new AboutWindow();
        aboutWindow.openWindow();
    }

    @FXML
    private void quitApp() {Platform.exit();}
}


