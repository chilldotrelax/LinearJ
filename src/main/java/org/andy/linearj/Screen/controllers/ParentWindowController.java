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
import javafx.stage.Stage;
import org.andy.linearj.Circuit.CircuitElement;
import org.andy.linearj.Circuit.CircuitNode;
import org.andy.linearj.Circuit.CircuitSolver;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.EmptyInputException;
import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.andy.linearj.Maths.MatrixMath.castStringToDouble;

public class ParentWindowController {
    @FXML
    private InputBoxController inputBoxController;
    @FXML
    private OutputDisplayController outputBoxController;
    @FXML
    private ButtonsController buttonsChildComponentLeftController;
    @FXML
    private ButtonsController buttonsChildComponentRightController;
    @FXML
    private NetListController netlistTableController;
    @FXML
    private Button solveCircuit;
    @FXML
    private Button clearNetlist;
    @FXML
    private Button removeElement;

    private ObservableList<ElementDataModel> elementDataModelObservableList;
    private ObservableList<CircuitElement> circuitElementObservableList;
    private List<CircuitNode> circuitNodeList;
    private HashMap<Integer, CircuitNode> circuitNodeHashMap;

    public ParentWindowController(){
        this.elementDataModelObservableList = FXCollections.observableArrayList();
        this.circuitElementObservableList = FXCollections.observableArrayList();
        this.circuitNodeList = new ArrayList<>();
        this.circuitNodeHashMap = new HashMap<>();
    }

    private void handleAddChange(ListChangeListener.Change c) {
        CircuitElement elm = (CircuitElement) c.getAddedSubList().toArray()[0];

        if (!circuitNodeHashMap.containsKey(elm.getBegNodeID())) {
            circuitNodeHashMap.put(elm.getBegNodeID(), new CircuitNode(elm.getBegNodeID()));
            circuitNodeHashMap.get(elm.getBegNodeID()).addElement(elm);
        }
        else if (!circuitNodeHashMap.containsKey(elm.getEndNodeID())){
            circuitNodeHashMap.put(elm.getEndNodeID(), new CircuitNode(elm.getEndNodeID()));
            circuitNodeHashMap.get(elm.getEndNodeID()).addElement(elm);
        }
        else{
            circuitNodeHashMap.forEach((nodeNumber,circuitNodeElement) ->{
                if (elm.isNodeIDEqual(nodeNumber)){
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
        buttonsChildComponentLeftController.setParentController(this);
        buttonsChildComponentRightController.setParentController(this);
        netlistTableController.setParentController(this);
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
    }
    //TODO Merge the add and subtract matrix to one method.
    public void addMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.addMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Sum: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError(InputBoxController.EMPTY_INPUT_BOX_ERROR);
        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not equivalent.");
        }
    }
    //TODO Merge the add and subtract matrix to one method.
    public void subtractMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.subtractMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Difference: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not equivalent.");
        }
    }

    public void multiplyMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.multiplyMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Product: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError(InputBoxController.EMPTY_INPUT_BOX_ERROR);
        } catch (NonMatchingMatricesException e) {
            ErrorWindows.displayError("Cannot compute because row and column of respective matrix is not the same.");
        }
    }

    public void computeInversion() {
        try {
            double[][] result = MatrixMath.computeInverse(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Inverse: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError(InputBoxController.EMPTY_INPUT_BOX_ERROR);
        } catch (SingularMatrixException e) {
            ErrorWindows.displayError("Cannot compute because matrix is singular and has no general solution.");
        }

    }

    public void computeDeterminant() {
        try {
            double result = MatrixMath.computeDeterminant(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Determinant: " + result);
        } catch (EmptyInputException e) {
            ErrorWindows.displayError(InputBoxController.EMPTY_INPUT_BOX_ERROR);
        } catch (ArithmeticException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not a square matrix.");
        }
    }

    public void computeTranspose() {
        try {
            double[][] result = MatrixMath.transposeMatrix(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Transpose: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError(InputBoxController.EMPTY_INPUT_BOX_ERROR);
        }
    }

    @FXML
    public void solveDCCircuit(){
        //TODO fix this part :)
        CircuitElement[] elementsList = circuitElementObservableList.toArray(new CircuitElement[circuitElementObservableList.size()]);

        CircuitSolver solve = new CircuitSolver(elementsList,circuitNodeHashMap);

        double[] temp = solve.solveCircuit();

        for (int i = 0; i < temp.length; i++){
            if (i < circuitNodeList.size()){
                outputBoxController.setOutputBox("The voltage across node"+i+" is: "+temp[i]);
            }
            else if (i > circuitNodeList.size()){
                outputBoxController.setOutputBox("The current across "+i+" is: " + temp[i]);
            }
        }
    }

    //TODO evaluate if this method is nesccary.
    @FXML
    public void removeItem(){
        //Empty cause can
    }

    public void enableRemoveItemButton(){
       removeElement.setDisable(false);
    }

    public void disableRemoveElementButton(){removeElement.setDisable(true);}

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
        } catch (IOException e) { //Generic catch exception; should be altered or removed.
            ErrorWindows.displayError("Something went wrong");
        }
    }

    public void triggerHelpMenu() {
        HelpWindow helpMenu = new HelpWindow();
        helpMenu.openHelpWindow();
    }

    public void triggerAboutMenu() {
        AboutWindow aboutMenu = new AboutWindow();
        aboutMenu.openAboutWindow();
    }

    public void clearInput() {inputBoxController.clearInputBox();}
    public void quitApp() {Platform.exit();}

}


