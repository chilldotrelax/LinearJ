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

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NetListController {
    @FXML
    private TableView<ElementDataModel> circuitElementTableView;
    @FXML
    private TableColumn<ElementDataModel, String> componentIDColumn;
    @FXML
    private TableColumn<ElementDataModel, Integer> begNodeIDColumn;
    @FXML
    private TableColumn<ElementDataModel, Integer> endNodeIDColumn;
    @FXML
    private TableColumn<ElementDataModel,Double> componentValueColumn;

    private ObservableList<ElementDataModel> elementDataModelObservableList;

    public void setObservableList(ObservableList<ElementDataModel> elmList){
        this.elementDataModelObservableList = elmList;
        circuitElementTableView.setItems(this.elementDataModelObservableList);
    }

    @FXML
    public void initialize(){
        componentIDColumn.setCellValueFactory(cellData -> cellData.getValue().componentIDProperty());
        begNodeIDColumn.setCellValueFactory(cellData -> cellData.getValue().begNodeIDProperty().asObject());
        endNodeIDColumn.setCellValueFactory(cellData -> cellData.getValue().endNodeIDProperty().asObject());
        componentValueColumn.setCellValueFactory(cellData -> cellData.getValue().componentValueProperty().asObject());

        circuitElementTableView.getColumns().forEach(column -> column.setReorderable(false));
    }
}
