package org.andy.linearj.Screen.controllers;

public final class ElementDataModelSimpleFactory {
    private ElementDataModelSimpleFactory() {
        //
    }
    public static ElementDataModel createElement(String compID, int begNodeID, int endNodeID, double componentValue) {
        return new ElementDataModel(compID, begNodeID, endNodeID, componentValue);
    }

}