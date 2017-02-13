/*
 * Copyright 2017 Athanasios Raptodimos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.kalodiodev.kitapi.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * About Handler class
 *
 * @author Raptodimos Thanos
 */
public class AboutHandler {

    private static final String TITLE_KEY = "about";
    private Window window;
    private ResourceBundle resourceBundle;

    /**
     * About handler constructor
     *
     * @param window parent window
     * @param resourceBundle resources bundle
     */
    public AboutHandler(Window window, ResourceBundle resourceBundle) {
        this.window = window;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Show About dialog
     *
     * @throws IOException if about dialog view could not be loaded
     */
    public void show() throws IOException {
        // About Dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(window);
        dialog.setTitle(resourceBundle.getString(TITLE_KEY));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about_dialog.fxml"), resourceBundle);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        dialog.getDialogPane().setContent(fxmlLoader.load());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }
}
