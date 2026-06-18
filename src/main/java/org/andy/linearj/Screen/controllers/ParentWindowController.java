package org.andy.linearj.Screen.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.EmptyInputException;
import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;

import java.io.IOException;
import java.util.Arrays;

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

    private ObservableList<ElementDataModel> elementDataModelObservableList;

    public ParentWindowController(){
        this.elementDataModelObservableList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        buttonsChildComponentLeftController.setParentController(this);
        buttonsChildComponentRightController.setParentController(this);
        netlistTableController.setParentController(this);
        netlistTableController.setObservableList(elementDataModelObservableList);
    }

    public void addMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.addMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Sum: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not equivalent.");
        }
    }
    public void subtractMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.subtractMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Difference: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not square.");
        }
    }
    public void multiplyMatrix() {
        try {
            String[] input = inputBoxController.getText().split("/");
            double[][] result = MatrixMath.multiplyMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Product: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (NonMatchingMatricesException e) {
            ErrorWindows.displayError("Cannot compute because row and column of respective matrix is not the same.");
        }
    }
    public void computeInversion() {
        try {
            double[][] result = MatrixMath.computeInverse(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Inverse: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (SingularMatrixException e) {
            ErrorWindows.displayError("Cannot compute because matrix is singular and has no general solution.");
        }

    }
    public void computeDeterminant() {
        try {
            double result = MatrixMath.computeDeterminant(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Determinant: " + result);
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (ArithmeticException e) {
            ErrorWindows.displayError("Cannot compute because matrix is not a square matrix.");
        }
    }
    public void computeTranspose() {
        try {
            double[][] result = MatrixMath.transposeMatrix(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Transpose: " + Arrays.deepToString(result));
        } catch (EmptyInputException e) {
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        } catch (Exception e) {
            ErrorWindows.displayError("Cannot compute because something went wrong.");
        }
    }

    public void triggerAddElementMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andy/linearj/AddComponentWindow.fxml"));
        //debug
        try{
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Component -- Debug");
            stage.setScene(new Scene(root));
            stage.show();
            AddComponentWindowController window = loader.getController();
            window.setElementDataModelObservableList(this.elementDataModelObservableList);
        } catch (IOException e) {
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


