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

import javafx.fxml.FXML;
import org.andy.linearj.Screen.misc.ErrorWindows;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class AboutWindow extends PopupWindow {

    public AboutWindow() {
        super("/org/andy/linearj/AboutPopupWindow.fxml", "About");
    }

    @FXML
    private void openLicenseFile() {
        try {
            File licenseFile = new File(Paths.get("License").toUri());
            if (licenseFile.exists()) {
                Desktop.getDesktop().open(licenseFile);
            }
        } catch (InvalidPathException e) {
            ErrorWindows.displayError("Could not find the file path for LICENSES");
        } catch (IOException e) {
            ErrorWindows.displayError("Could not find the file LICENSES");
        }
    }
}