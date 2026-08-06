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
import org.andy.linearj.Screen.exception.MatrixNotEquivalentException;
import org.andy.linearj.Screen.exception.NonMatchingMatricesException;
import org.ejml.MatrixDimensionException;
import org.ejml.data.SingularMatrixException;

import java.util.Arrays;

public final class MatrixCalculatorUnitController {
    @FXML
    private TextArea matrixInputTextArea;

    private static final String EMPTY_INPUT_FIELD_ERROR = "The input field must not be empty.";

    private final SimpleStringProperty computationResult = new SimpleStringProperty();

    public SimpleStringProperty computationResultProperty() {
        return computationResult;
    }

    @FXML
    private void addMatricesClick() {
        try {
            final String input = matrixInputTextArea.getText();

            if (isInputEmpty(input)) {
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
                return;
            }

            computationResult.set("Sum: " + Arrays.deepToString(MatrixMath.addMatrix(MatrixMath.castStringToTwoDouble(input).get(0), MatrixMath.castStringToTwoDouble(input).get(1))));

        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
    }

    @FXML
    private void subtractMatricesClick() {
        try {
            final String input = matrixInputTextArea.getText();

            if (isInputEmpty(input)) {
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
                return;
            }

            computationResult.set("Difference: " + Arrays.deepToString(MatrixMath.subtractMatrix(MatrixMath.castStringToTwoDouble(input).get(0), MatrixMath.castStringToTwoDouble(input).get(1))));

        } catch (MatrixNotEquivalentException e) {
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
    }

    @FXML
    private void multiplyMatricesClick() {
        try {
            final String input = matrixInputTextArea.getText();

            if (isInputEmpty(input)) {
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
                return;
            }

            computationResult.set("Product: " + Arrays.deepToString(MatrixMath.multiplyMatrix(MatrixMath.castStringToTwoDouble(input).get(0), MatrixMath.castStringToTwoDouble(input).get(1))));

        } catch (NonMatchingMatricesException e) {
            ErrorWindows.displayError("Cannot compute matrices with non-equivalent dimensions.");
        }
    }

    //Disabled as no testing has been done with the transpose method in the MatrixMath class.
    @FXML
    private void transposeMatrixClick() {
        final String input = matrixInputTextArea.getText();

        if (isInputEmpty(input)) {
            ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
            return;
        }

        computationResult.set("Transposition: " + Arrays.deepToString(MatrixMath.transposeMatrix(MatrixMath.castStringToDouble(input))));

    }

    @FXML
    private void computeInversionClick() {
        try {
            final String input = matrixInputTextArea.getText();

            if (isInputEmpty(input)) {
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
                return;
            }

            computationResult.set("Inversion: " + Arrays.deepToString(MatrixMath.computeInverse(MatrixMath.castStringToDouble(input))));

        } catch (SingularMatrixException e) {
            ErrorWindows.displayError("Cannot compute over singular matrices.");
        } catch (MatrixDimensionException e) {
            ErrorWindows.displayError("Cannot compute over non-square matrices.");
        }
    }

    @FXML
    private void computeDeterminantClick() {
        try {
            final String input = matrixInputTextArea.getText();

            if (isInputEmpty(input)) {
                ErrorWindows.displayError(EMPTY_INPUT_FIELD_ERROR);
                return;
            }

            computationResult.set("Determinant: " + MatrixMath.computeDeterminant(MatrixMath.castStringToDouble(input)));

        } catch (ArithmeticException e) {
            ErrorWindows.displayError("Cannot compute over non-square matrices.");
        }
    }

    @FXML
    private void clearInputField() {
        matrixInputTextArea.clear();
    }

    @FXML
    private void openHelpWindow() {
        PopupWindow helpWindow = new HelpWindow();
        helpWindow.openWindow();
    }

    private boolean isInputEmpty(String input) {
        return input.isEmpty();
    }

}