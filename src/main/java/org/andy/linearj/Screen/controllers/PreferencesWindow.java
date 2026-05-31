package org.andy.linearj.Screen.controllers;


import org.andy.linearj.Screen.misc.ErrorWindows;

import java.io.IOException;

public class PreferencesWindow extends PopupWindow {
    public PreferencesWindow(){
        super("/org/andy/linearj/PreferencesPopupMenu.fxml","Preferences");
    }

    public void openPreferencesWindow(){
        try{
            super.openWindow();
        }
        catch (IOException e){
            ErrorWindows.displayError("Something went wrong here.");
        }
    }
}
