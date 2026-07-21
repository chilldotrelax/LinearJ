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

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ElementDataModel {
    private final SimpleStringProperty componentID;
    private final SimpleIntegerProperty begNodeID;
    private final SimpleIntegerProperty endNodeID;
    private final SimpleDoubleProperty componentValue;

    public ElementDataModel(String componentID, Integer begNodeID, Integer endNodeID, Double componentValue) {
        this.componentID = new SimpleStringProperty(componentID);
        this.begNodeID = new SimpleIntegerProperty(begNodeID);
        this.endNodeID = new SimpleIntegerProperty(endNodeID);
        this.componentValue = new SimpleDoubleProperty(componentValue);
    }
    public final SimpleStringProperty componentIDProperty() {
        return componentID;
    }
    public final SimpleIntegerProperty begNodeIDProperty() {
        return begNodeID;
    }
    public final SimpleIntegerProperty endNodeIDProperty() {
        return endNodeID;
    }
    public final SimpleDoubleProperty componentValueProperty() {
        return componentValue;
    }
    public final void setComponentID(String newID) {componentID.set(newID);}
    public final void setBegNodeID(Integer newID) {
        begNodeID.set(newID);
    }
    public final void setEndNodeID(Integer newID) {endNodeID.set(newID);}
    public final void setComponentValue(Double newID) {
        componentValue.set(newID);
    }

}