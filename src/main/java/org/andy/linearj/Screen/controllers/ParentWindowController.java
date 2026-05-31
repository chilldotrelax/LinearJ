package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXML;
import org.andy.linearj.Maths.MatrixMath;
import org.andy.linearj.Screen.misc.ErrorWindows;
import org.andy.linearj.Screen.misc.exception.EmptyInputException;
import org.andy.linearj.Screen.misc.exception.MatrixNotSquareException;
import org.andy.linearj.Screen.misc.exception.NonMatchingMatricesException;
import org.ejml.data.SingularMatrixException;

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
    public void initialize() {
        buttonsChildComponentLeftController.setParentController(this);
        buttonsChildComponentRightController.setParentController(this);
    }

    public void addMatrix() {
        try{
            String[] input = inputBoxController.getText().split(" ");
            double[][] result = MatrixMath.addMatrix(castStringToDouble(input[0]),castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Sum: " + Arrays.deepToString(result));
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch (MatrixNotSquareException e){
            ErrorWindows.displayError("Cannot compute because matrix is not square.");
        }
    }

    public void subtractMatrix(){
        try{
            String[] input = inputBoxController.getText().split(" ");
            double[][] result = MatrixMath.subtractMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Difference: " + Arrays.deepToString(result));
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch (MatrixNotSquareException e){
            ErrorWindows.displayError("Cannot compute because matrix is not square.");
        }
    }

    public void multiplyMatrix(){
        try{
            String[] input = inputBoxController.getText().split(" ");
            double[][] result = MatrixMath.multiplyMatrix(castStringToDouble(input[0]), castStringToDouble(input[1]));
            outputBoxController.setOutputBox("Product: " + Arrays.deepToString(result));
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch(NonMatchingMatricesException e){
            ErrorWindows.displayError("Cannot compute because row and column of respective matrix is not the same.");
        }
    }

    public void computeInversion(){
        try{
            double[][] result = MatrixMath.computeInverse(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Inverse: " + Arrays.deepToString(result));
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch(SingularMatrixException e) {
            ErrorWindows.displayError("Cannot compute because matrix is singular and has no general solution.");
        }

    }

    public void computeDeterminant(){
        try{
            double result = MatrixMath.computeDeterminant(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Determinant: " + result);
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch (ArithmeticException e){
            ErrorWindows.displayError("Cannot compute because matrix is not a square matrix.");
        }
    }

    public void computeTranspose(){
        try{
            double[][] result = MatrixMath.transposeMatrix(castStringToDouble(inputBoxController.getText()));
            outputBoxController.setOutputBox("Transpose: " + Arrays.deepToString(result));
        }
        catch(EmptyInputException e){
            ErrorWindows.displayError("Empty Input Box. Press OK to acknowledge.");
        }
        catch(Exception e){
            ErrorWindows.displayError("Cannot compute because something went wrong.");
        }
    }

    public void triggerAboutMenu() {
        AboutWindow aboutMenu = new AboutWindow();
        aboutMenu.openAboutWindow();
    }

//    @FXML
//    public void triggerPopupMenu(){
//
//    }
//
    public void quitApp() {
        System.exit(0);
    }

}


