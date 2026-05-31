package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXML;

public class ButtonsController {
    private ParentWindowController parentController;

    //Passing the parent controller to here.
    public void setParentController(ParentWindowController parentController){
        this.parentController = parentController;
    }

    @FXML
    private void addMatricesClick(){

        parentController.addMatrix();
    }

    @FXML
    private void subtractMatricesClick(){
        parentController.subtractMatrix();
    }

    @FXML
    private void multiplyMatricesClick(){
        parentController.multiplyMatrix();
    }

    @FXML
    private void computeInversionClick(){
        parentController.computeInversion();
    }

    @FXML
    private void computeDeterminantClick(){
        parentController.computeDeterminant();
    }
    @FXML
    private void transposeMatrixClick(){
        parentController.computeTranspose();
    }

}
