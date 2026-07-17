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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.andy.linearj.Circuit.*;
import org.andy.linearj.Screen.misc.ErrorWindows;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CircuitSolverUnitController {
    @FXML
    private Button solveCircuitBtn;

    private ObservableList<ElementDataModel> elmListInUnit;
    private ObservableList<CircuitElement> circuitElementObservableListInUnit;
    private final HashMap<Integer, CircuitNode> circuitNodeHashMap;

    private final SimpleStringProperty computationOutput = new SimpleStringProperty();

    public SimpleStringProperty computationOutputProperty() {
        return computationOutput;
    }

    public CircuitSolverUnitController() {
        this.circuitNodeHashMap = new HashMap<>();
    }

    public void setObservableLists(ObservableList<ElementDataModel> elmList, ObservableList<CircuitElement> elementObservableList) {
        this.elmListInUnit = elmList;
        this.circuitElementObservableListInUnit = elementObservableList;

        circuitElementObservableListInUnit.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends CircuitElement> c) {
                while (c.next()) {
                    if (!circuitElementObservableListInUnit.isEmpty() && c.wasAdded()) {
                        solveCircuitBtn.setDisable(false);
                        //Unchecked cast: 'javafx.collections.ListChangeListener.Change<capture<? extends org.andy.linearj.Circuit.CircuitElement>>' to 'javafx.collections.ListChangeListener.Change<org.andy.linearj.Circuit.CircuitElement>'
                        handleAddChange((Change<CircuitElement>) c);
                    }
                    if (!circuitElementObservableListInUnit.isEmpty() && c.wasRemoved()) {
                        solveCircuitBtn.setDisable(false);
                        //Unchecked cast: 'javafx.collections.ListChangeListener.Change<capture<? extends org.andy.linearj.Circuit.CircuitElement>>' to 'javafx.collections.ListChangeListener.Change<org.andy.linearj.Circuit.CircuitElement>'
                        handleRemoveChange((Change<CircuitElement>) c);
                    }
                    if ((circuitElementObservableListInUnit.size() <= 1)) {
                        solveCircuitBtn.setDisable(true);
                        //Unchecked cast: 'javafx.collections.ListChangeListener.Change<capture<? extends org.andy.linearj.Circuit.CircuitElement>>' to 'javafx.collections.ListChangeListener.Change<org.andy.linearj.Circuit.CircuitElement>'
                        handleRemoveChange((Change<CircuitElement>) c);
                    }
                }
            }
        });
    }

    private void handleAddChange(ListChangeListener.Change<CircuitElement> c) {
        CircuitElement elm = (CircuitElement) c.getAddedSubList().toArray()[0];

        if (!circuitNodeHashMap.containsKey(elm.getBegNodeID())) {
            circuitNodeHashMap.put(elm.getBegNodeID(), new CircuitNode(elm.getBegNodeID()));
            circuitNodeHashMap.get(elm.getBegNodeID()).addElement(elm);
        }
        if (!circuitNodeHashMap.containsKey(elm.getEndNodeID()) && !(elm instanceof GroundElement)) {
            circuitNodeHashMap.put(elm.getEndNodeID(), new CircuitNode(elm.getEndNodeID()));
            circuitNodeHashMap.get(elm.getEndNodeID()).addElement(elm);
        } else {
            circuitNodeHashMap.forEach((nodeNumber, circuitNodeElement) -> {
                if (elm.isNodeIDEqual(nodeNumber) && !circuitNodeElement.contains(elm)) {
                    circuitNodeElement.addElement(elm);
                }
            });
        }
    }

    private void handleRemoveChange(ListChangeListener.Change<CircuitElement> c) {
        List<CircuitElement> elm = c.getRemoved();

        if (elm.isEmpty()) {return;}

        for (CircuitElement circElm : elm) {
            if (circuitNodeHashMap.containsKey(circElm.getBegNodeID())) {
                circuitNodeHashMap.get(circElm.getBegNodeID()).removeElement(circElm);
            }
            if (circuitNodeHashMap.containsKey(circElm.getEndNodeID())) {
                circuitNodeHashMap.get(circElm.getEndNodeID()).removeElement(circElm);
            }
        }
    }

    @FXML
    private void triggerAddElementMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andy/linearj/AddComponentWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Component");
            stage.setScene(new Scene(root));
            stage.show();
            AddComponentWindowController window = loader.getController();

            window.setElementDataModelObservableList(elmListInUnit);
            window.setCircuitElementObservableList(circuitElementObservableListInUnit);
        } catch (IOException e) {
            ErrorWindows.displayError("Resource could not be found. This is not a normal, expected error message. Contact developer for help.");
        }
    }

    @FXML
    private void solveDCCircuit() {
        CircuitElement[] elementsList = circuitElementObservableListInUnit.toArray(new CircuitElement[0]);
        CircuitSolver solve = new CircuitSolver(elementsList, circuitNodeHashMap,computationOutput);
        solve.solveCircuit();

    }
}

