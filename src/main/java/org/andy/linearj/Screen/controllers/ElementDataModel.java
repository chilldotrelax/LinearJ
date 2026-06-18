package org.andy.linearj.Screen.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//One simple class to avoid violating the DRY (Don't repeat yourself principle).
public class ElementDataModel {
    private final SimpleStringProperty componentID;
    private final SimpleIntegerProperty begNodeID;
    private final SimpleIntegerProperty endNodeID;
    private final SimpleDoubleProperty componentValue;

    public ElementDataModel(String componentID, Integer begNodeID, Integer endNodeID, Double componentValue ){
        this.componentID = new SimpleStringProperty(componentID);
        this.begNodeID = new SimpleIntegerProperty(begNodeID);
        this.endNodeID = new SimpleIntegerProperty(endNodeID);
        this.componentValue = new SimpleDoubleProperty(componentValue);
    }

    public final SimpleStringProperty componentIDProperty(){return componentID;}
    public final SimpleIntegerProperty begNodeIDProperty(){return begNodeID;}
    public final SimpleIntegerProperty endNodeIDProperty(){return endNodeID;}
    public final SimpleDoubleProperty componentValueProperty(){return componentValue;}

    public final void setComponentID(String newID){componentID.set(newID);}
    public final void setBegNodeID(Integer newID){begNodeID.set(newID);}
    public final void setEndNodeID(Integer newID){endNodeID.set(newID);}
    public final void setComponentValue(Double newID){componentValue.set(newID);}



}
