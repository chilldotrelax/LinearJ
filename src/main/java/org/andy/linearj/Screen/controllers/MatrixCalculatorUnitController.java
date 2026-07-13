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
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.IllegalMatrixException;
import org.andy.linearj.Screen.misc.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.MatrixDimensionException;
import org.ejml.data.SingularMatrixException;

import java.util.Arrays;

public class MatrixCalculatorUnitController {
    @FXML
    private TextArea matrixInputField;
    
    private static final String EMPTY_INPUT_FIELD_ERROR = "The input field must not be empty.";
    private static final String GENERIC_MATRIX_ERROR = "You are either: (1) Missing a second matrix AND/OR (2) Incorrectly formatted matrix. Click HELP for a formatting guide.";

    private final SimpleStringProperty computationResult = new SimpleStringProperty();
    public SimpleStringProperty computationResultProperty(){return computationResult;}

    @FXML
    private void addMatricesClick(){
        try{
            if (!matrixInputField.getText().isEmpty()){
                double[][] result = MatrixMath.addMatrix(MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(1));
                computationResult.set("Sum: " + Arrays.deepToString(result));
                computationResult.set("");
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            }
        }
        catch (MatrixNotEquivalentException e){
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError(GENERIC_MATRIX_ERROR);
        }
    }

    @FXML
    private void subtractMatricesClick(){
        try{
            if (!matrixInputField.getText().isEmpty()){
                double[][] result = MatrixMath.subtractMatrix(MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(1));
                computationResult.set("Difference: " + Arrays.deepToString(result));
                computationResult.set("");
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            }
        }
        catch (MatrixNotEquivalentException e){
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError(GENERIC_MATRIX_ERROR);
        }
    }

    @FXML
    private void multiplyMatricesClick(){
        try{
            if (!matrixInputField.getText().isEmpty()){
                double[][] result = MatrixMath.multiplyMatrix(MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(0),MatrixMath.castStringToTwoDouble(matrixInputField.getText()).get(1));
                computationResult.set("Product: " + Arrays.deepToString(result));
                computationResult.set("");
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            }
        }
        catch (NonMatchingMatricesException e){
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
        catch (IllegalMatrixException | ArrayIndexOutOfBoundsException e){
            ErrorWindows.displayError(GENERIC_MATRIX_ERROR);
        }
    }
    //Disabled as no testing has been done with the transpose method in the MatrixMath class.
    @FXML
    private void transposeMatrixClick(){
        if (!matrixInputField.getText().isEmpty()){
            double[][] result = MatrixMath.transposeMatrix(MatrixMath.castStringToDouble(matrixInputField.getText()));
            computationResult.set("Transposition: " + Arrays.deepToString(result));
            computationResult.set("");
        }
        else{
            ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
        }
    }

    @FXML
    private void computeInversionClick(){
        try{
            if (!matrixInputField.getText().isEmpty()){
                double[][] result = MatrixMath.computeInverse(MatrixMath.castStringToDouble(matrixInputField.getText()));
                computationResult.set("Inversion: " + Arrays.deepToString(result));
                computationResult.set("");
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            }
        }
        catch (SingularMatrixException e){
            ErrorWindows.displayError("Cannot compute over singular matrices.");
        }
        catch (MatrixDimensionException e){
            ErrorWindows.displayError("Cannot compute over non-square matrices.");
        }
    }

    @FXML
    private void computeDeterminantClick(){
        try{
            if (!matrixInputField.getText().isEmpty()){
                double result = MatrixMath.computeDeterminant(MatrixMath.castStringToDouble(matrixInputField.getText()));
                computationResult.set("Determinant: " + result);
                computationResult.set("");
            }
            else{
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            }
        }
        catch (ArithmeticException e){
            ErrorWindows.displayError("Cannot compute over non-square matrices.");
        }
    }

    @FXML
    private void clearInputField(){
        matrixInputField.clear();}

    @FXML
    private void openHelpWindow(){
        PopupWindow helpWindow = new HelpWindow();
        helpWindow.openWindow();
    }

}
