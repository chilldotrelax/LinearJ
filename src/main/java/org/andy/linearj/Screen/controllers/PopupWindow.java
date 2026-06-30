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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static java.lang.System.Logger.Level.INFO;

abstract class PopupWindow {
    private final String resourcePath;
    private final String windowTitle;
    private final Stage stageRoot;
    private final System.Logger logger;

    PopupWindow(String resourcePath, String windowTitle){
        this.resourcePath = resourcePath;
        this.windowTitle = windowTitle;
        this.stageRoot = new Stage();
        this.logger = System.getLogger("org.andy.linearJ.");
    }

    public void openWindow()  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Objects.requireNonNull(this.resourcePath)));

        try{
            Parent root = loader.load();
            this.stageRoot.setTitle(this.windowTitle);
            this.stageRoot.setScene(new Scene(root));
            this.stageRoot.show();
        } catch (IOException e) {
            logger.log(INFO,()-> "Unable to load window. This is most likely due to a missing/invalid resource path. Trying again. ");
        }
    }
}

