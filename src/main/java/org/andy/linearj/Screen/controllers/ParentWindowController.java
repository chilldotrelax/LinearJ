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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.andy.linearj.Circuit.CircuitElement;
import org.andy.linearj.Circuit.CircuitNode;
import org.andy.linearj.Circuit.CircuitSolver;
import org.andy.linearj.Circuit.GroundElement;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.EmptyInputException;
import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static org.andy.linearj.Maths.MatrixMath.castStringToDouble;

//TODO Goal: Run this circuit simulations on background thread.
public class ParentWindowController {
    @FXML
    private MatrixCalculatorUnitController matrixCalculatorUnitController;
    @FXML
    private NetListController netlistTableController;
    @FXML
    private OutputDisplayController outputBoxController;
    
    //Exclusive to the parent (not subclassed).
    @FXML
    private Button solveCircuit;
    @FXML
    private Button clearNetlist;
    @FXML
    private Button removeElement;
    @FXML
    private VBox debugVBox;
    @FXML
    private CheckBox debugUtilsBox;

    private ObservableList<ElementDataModel> elementDataModelObservableList;
    private ObservableList<CircuitElement> circuitElementObservableList;
    private HashMap<Integer, CircuitNode> circuitNodeHashMap;

    public ParentWindowController(){
        this.elementDataModelObservableList = FXCollections.observableArrayList();
        this.circuitElementObservableList = FXCollections.observableArrayList();
        this.circuitNodeHashMap = new HashMap<>();
    }

    private void handleAddChange(ListChangeListener.Change c) {
        CircuitElement elm = (CircuitElement) c.getAddedSubList().toArray()[0];

        if (!circuitNodeHashMap.containsKey(elm.getBegNodeID())) {
            circuitNodeHashMap.put(elm.getBegNodeID(), new CircuitNode(elm.getBegNodeID()));
            circuitNodeHashMap.get(elm.getBegNodeID()).addElement(elm);
        }
        if (!circuitNodeHashMap.containsKey(elm.getEndNodeID()) && !(elm instanceof GroundElement)){
            circuitNodeHashMap.put(elm.getEndNodeID(), new CircuitNode(elm.getEndNodeID()));
            circuitNodeHashMap.get(elm.getEndNodeID()).addElement(elm);
        }
        else {
            circuitNodeHashMap.forEach((nodeNumber,circuitNodeElement) ->{
                if (elm.isNodeIDEqual(nodeNumber) && !circuitNodeElement.contains(elm)){
                    circuitNodeElement.addElement(elm);
                }
            } );
        }
    }

    private void handleRemoveChange(ListChangeListener.Change c) {
        CircuitElement elm = (CircuitElement) c.getAddedSubList().toArray()[0];

        if (circuitNodeHashMap.containsKey(elm.getBegNodeID())) {
            circuitNodeHashMap.get(elm.getBegNodeID()).removeElement(elm);
        }
        else if (circuitNodeHashMap.containsKey(elm.getEndNodeID())){
            circuitNodeHashMap.get(elm.getEndNodeID()).removeElement(elm);
        }
    }

    @FXML
    public void initialize() {
       //TODO remove all these parent controllers and find alternatives (decoupling).
        netlistTableController.setObservableList(elementDataModelObservableList);

        circuitElementObservableList.addListener(new ListChangeListener<CircuitElement>(){
            @Override
            public void onChanged(Change<? extends CircuitElement> c) {
                while (c.next()) {
                    if (!circuitElementObservableList.isEmpty() && c.wasAdded()){
                        handleAddChange(c);
                    }
                    else if (!circuitElementObservableList.isEmpty() && c.wasRemoved()){
                        handleRemoveChange(c);
                    }

                    if ((circuitElementObservableList.size() > 1 && c.wasAdded()) || (circuitElementObservableList.size() > 1 && c.wasRemoved())){
                        solveCircuit.setDisable(false);
                    }
                    else{
                        solveCircuit.setDisable(true);
                    }
                }
            }
        });

        matrixCalculatorUnitController.computationResultProperty().addListener(((observable, oldValue, newValue) -> {
            outputBoxController.setOutputBox(newValue);
        }));
    }

    @FXML
    private void solveDCCircuit(){
        //TODO fix this part :)
        CircuitElement[] elementsList = circuitElementObservableList.toArray(new CircuitElement[circuitElementObservableList.size()]);

        CircuitSolver solve = new CircuitSolver(elementsList,circuitNodeHashMap);

        double[] temp = solve.solveCircuit();

        for (int i = 0; i < temp.length; i++){
            if (i < circuitNodeHashMap.size()){
                outputBoxController.setOutputBox("The voltage across node "+i+" is: "+temp[i] +"V");
            }
            else if (i > circuitNodeHashMap.size()){
                outputBoxController.setOutputBox("The current across "+i+" is: " + temp[i]+"A");
            }
        }
    }

    public void triggerAddElementMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andy/linearj/AddComponentWindow.fxml"));
        //debug only
        try{
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Component");
            stage.setScene(new Scene(root));
            stage.show();
            AddComponentWindowController window = loader.getController();

            window.setElementDataModelObservableList(this.elementDataModelObservableList);
            window.setCircuitElementObservableList(this.circuitElementObservableList);
        } catch (IOException e) {
            ErrorWindows.displayError("Resource could not be found.");
        }
    }

    @FXML
    private void setDebugUtilsVisibility(){
        if (debugUtilsBox.isSelected()){
            debugVBox.setVisible(true);
        }
        if (!debugUtilsBox.isSelected()){
            debugVBox.setVisible(false);
        }
    }

    public void triggerAboutMenu() {
        AboutWindow aboutMenu = new AboutWindow();
        aboutMenu.openAboutWindow();
    }

    public void quitApp() {Platform.exit();}
}


