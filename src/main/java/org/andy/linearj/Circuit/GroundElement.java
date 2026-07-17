package org.andy.linearj.Circuit;

public class GroundElement extends CircuitElement {
    private String componentID;

    public GroundElement(Integer begNodeID, String componentID){
        super(begNodeID);
        this.componentID = componentID;
    }

    @Override
    public double getElementValue() {return 0;}

    @Override
    public void setElementValue(double newValue) {}

    @Override
    public String getComponentID() {return componentID;}

    @Override
    public void setComponentID(String newComponentID) {componentID = newComponentID;}

}
