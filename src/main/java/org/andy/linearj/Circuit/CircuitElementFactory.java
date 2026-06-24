package org.andy.linearj.Circuit;

public class CircuitElementFactory {

    public CircuitElement createElement(String compID, Integer nodeBegID){return new GroundElement(nodeBegID,compID);}

    public CircuitElement createElement(String compID, Integer nodeBegID, Integer nodeEndID, Double value) {
        return switch (compID) {
            case "C" -> new CurrentSourceElement(nodeBegID,nodeEndID,compID, value);
            case "R" -> new ResistorElement(nodeBegID, nodeEndID, compID, value);
            case "V" -> new VoltageSourceElement(nodeBegID,nodeEndID,compID,value);
            case null,default -> throw new IllegalArgumentException("Unexpected value");
        };
    }
}
