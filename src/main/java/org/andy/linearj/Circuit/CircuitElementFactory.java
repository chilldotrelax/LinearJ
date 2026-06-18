package org.andy.linearj.Circuit;

public class CircuitElementFactory {
    public CircuitElement createElement(String compID, Integer nodeBegID, Integer nodeEndID, Double value) {

        if (compID.isEmpty() || value.isNaN() || nodeBegID.toString().isEmpty() || nodeEndID.toString().isEmpty()) {
            throw new IllegalArgumentException("Invalid Arguments");
        }
        return switch (compID) {
            case "I" -> new CurrentSourceElement(nodeBegID,nodeEndID,compID, value);
            case "R" -> new ResistorElement(nodeBegID, nodeEndID, compID, value);
            case null,default -> throw new IllegalArgumentException("Unexpected value: \" + compID.substring(0, 0)");
        };
    }

    //For voltage source ONLY.

    /*
    * ==GUIDE==
    * The offset must be sequential (aka must be decoupled from the compID and increases as each voltage source element is created)
     */

    public CircuitElement createElement(String compID, Integer nodeBegID, Integer nodeEndID, Double value, int offset){
        try{
            return new VoltageSourceElement(nodeBegID, nodeEndID, compID, value, offset);
        }
        //Subject to refactoring.
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Malfunction 54");
        }
    }
}
