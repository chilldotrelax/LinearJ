package org.andy.linearj.Screen.controllers;

import org.andy.linearj.Screen.misc.ErrorWindows;

import java.io.IOException;

public class AboutWindow extends PopupWindow {

    public AboutWindow(){
        super("/org/andy/linearj/AboutPopupWindow.fxml","About");
    }

    public void openAboutWindow(){
        try{
            super.openWindow();
        }
        catch (IOException e){
            ErrorWindows.displayError("Something went wrong here.");
        }
    }

    public void getSettingsState(){

    }

    public void saveSettings(){

    }

}
