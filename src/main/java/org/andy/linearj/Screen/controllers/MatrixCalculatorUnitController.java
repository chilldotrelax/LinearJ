package org.andy.linearj.Screen.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;
import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;

import java.util.Arrays;

public class MatrixCalculatorUnitController {
    @FXML
    private TextArea matrixInputBox;

    private static final String EMPTY_INPUT_BOX_ERROR = "Empty Input Box. Press OK to acknowledge.";

    private SimpleStringProperty computationResult = new SimpleStringProperty();
    public SimpleStringProperty computationResultProperty(){return computationResult;}

    @FXML
    private void addMatricesClick(){
        try{
            if (!matrixInputBox.getText().isEmpty()){
                double[][] result = MatrixMath.addMatrix(MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(1));
                computationResult.set("Sum: " + Arrays.deepToString(result));
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
            }
        }
        catch (MatrixNotEquivalentException e){
            ErrorWindows.displayError("Cannot compute because matrix dimensions are not equivalent!");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError("You are either: (1) Missing a second matrix and/or incorrectly formatted matrix. Press HELP for help.");
        }
    }

    @FXML
    private void subtractMatricesClick(){
        try{
            if (!matrixInputBox.getText().isEmpty()){
                double[][] result = MatrixMath.subtractMatrix(MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(1));
                computationResult.set("Difference: " + Arrays.deepToString(result));
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
            }
        }
        catch (MatrixNotEquivalentException e){
            ErrorWindows.displayError("Cannot compute because matrix dimensions are not equivalent!");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError("You are either: (1) Missing a second matrix and/or incorrectly formatted matrix. Press HELP for help.");
        }
    }

    @FXML
    private void multiplyMatricesClick(){
        try{
            if (!matrixInputBox.getText().isEmpty()){
                double[][] result = MatrixMath.multiplyMatrix(MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputBox.getText()).get(1));
                computationResult.set("Product: " + Arrays.deepToString(result));
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
            }
        }
        catch (NonMatchingMatricesException e){
            ErrorWindows.displayError("Cannot compute because row and column of respective matrix is not the same.");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError("You are either: (1) Missing a second matrix and/or incorrectly formatted matrix. Press HELP for help.");
        }
    }

    @FXML
    private void transposeMatrixClick(){
        if (!matrixInputBox.getText().isEmpty()){
            double[][] result = MatrixMath.transposeMatrix(MatrixMath.castStringToDouble(matrixInputBox.getText()));
            computationResult.set("Transposition: " + Arrays.deepToString(result));
        }
        else{
            ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
        }
    }

    @FXML
    private void computeInversionClick(){
        try{
            if (!matrixInputBox.getText().isEmpty()){
                double[][] result = MatrixMath.computeInverse(MatrixMath.castStringToDouble(matrixInputBox.getText()));
                computationResult.set("Inversion: " + Arrays.deepToString(result));
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
            }
        }
        catch (SingularMatrixException e){
            ErrorWindows.displayError("Cannot compute because matrix is singular and has no general solution.");
        }
    }

    @FXML
    private void computeDeterminantClick(){
        try{
            if (!matrixInputBox.getText().isEmpty()){
                double result = MatrixMath.computeDeterminant(MatrixMath.castStringToDouble(matrixInputBox.getText()));
                computationResult.set("Determinant: " + result);
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_BOX_ERROR);
            }
        }
        catch (ArithmeticException e){
            ErrorWindows.displayError("Cannot compute because matrix is not a square matrix.");
        }
    }

    @FXML
    private void clearInputBox(){matrixInputBox.clear();}

    @FXML
    private void openHelpWindow(){
        PopupWindow helpWindow = new HelpWindow();
        helpWindow.openWindow();
    }

}
