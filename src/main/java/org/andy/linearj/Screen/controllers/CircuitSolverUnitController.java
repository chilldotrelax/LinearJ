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
import org.andy.linearj.Circuit.CircuitElement;
import org.andy.linearj.Circuit.CircuitNode;
import org.andy.linearj.Circuit.CircuitSolver;
import org.andy.linearj.Circuit.GroundElement;
import org.andy.linearj.Screen.misc.ErrorWindows;

import java.io.IOException;
import java.util.HashMap;

public class CircuitSolverUnitController {
    @FXML
    private Button solveCircuitBtn;

    private  ObservableList<ElementDataModel> elmListInUnit;
    private ObservableList<CircuitElement> circuitElementObservableListInUnit;
    private final HashMap<Integer, CircuitNode> circuitNodeHashMap;

    private final SimpleStringProperty computationOutput = new SimpleStringProperty();
    public SimpleStringProperty computationOutputProperty(){return computationOutput;}

    public CircuitSolverUnitController(){this.circuitNodeHashMap = new HashMap<>();}

    public void setDataModel(ObservableList<ElementDataModel> elmList,ObservableList<CircuitElement> elementObservableList){
        this.elmListInUnit = elmList;
        this.circuitElementObservableListInUnit = elementObservableList;

        circuitElementObservableListInUnit.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends CircuitElement> c) {
                while (c.next()) {
                    if (!circuitElementObservableListInUnit.isEmpty() && c.wasAdded()) {
                        solveCircuitBtn.setDisable(false);
                        handleAddChange((Change<CircuitElement>) c);
                    } else if (!circuitElementObservableListInUnit.isEmpty() && c.wasRemoved()) {
                        solveCircuitBtn.setDisable(false);
                        handleRemoveChange((Change<CircuitElement>) c);
                    }
                    if ((circuitElementObservableListInUnit.size() <= 1)) {
                        solveCircuitBtn.setDisable(true);
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

    private void handleRemoveChange(ListChangeListener.Change<CircuitElement> c) {
        CircuitElement elm = (CircuitElement) c.getAddedSubList().toArray()[0];

        if (circuitNodeHashMap.containsKey(elm.getBegNodeID())) {
            circuitNodeHashMap.get(elm.getBegNodeID()).removeElement(elm);
        }
        else if (circuitNodeHashMap.containsKey(elm.getEndNodeID())){
            circuitNodeHashMap.get(elm.getEndNodeID()).removeElement(elm);
        }
    }

    @FXML
    private void triggerAddElementMenu(){
        try{
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
    private void solveDCCircuit(){
        CircuitElement[] elementsList = circuitElementObservableListInUnit.toArray(new CircuitElement[0]);

        CircuitSolver solve = new CircuitSolver(elementsList,circuitNodeHashMap);

        double[] temp = solve.solveCircuit();

        for (int i = 0; i < temp.length; i++){
            if (i < circuitNodeHashMap.size()){
                computationOutput.set("The voltage across node "+i+" is: "+temp[i] +"V");
            }
            else if (i > circuitNodeHashMap.size()){
                computationOutput.set("The current across "+i+" is: " + temp[i]+"A");
            }
        }
    }

}
