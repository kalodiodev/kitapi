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

package eu.kalodiodev.kitapi.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Common Alert Dialogs
 *
 * @author Raptodimos Thanos
 */
public class AlertDialog {

    private AlertDialog() {
        // Prevent instantiation - all methods are static
    }

    /**
     * Show error alert dialog
     *
     * @param title title of error dialog
     * @param header header of dialog
     * @param message message to setup
     */
    public static void showAlertError(String title, String header, String message) {
        alert(title, header, message, Alert.AlertType.ERROR).showAndWait();
    }

    /**
     * Show information alert dialog
     *
     * @param title title of information dialog
     * @param header header of dialog
     * @param message message to setup
     */
    public static void showAlertInformation(String title, String header, String message) {
        alert(title, header, message, Alert.AlertType.INFORMATION).showAndWait();
    }

    /**
     * Show confirmation alert dialog
     *
     * @param title title of confirmation dialog
     * @param header header of dialog
     * @param message message to setup
     * @return dialog result
     */
    public static Optional<ButtonType> showAlertConfirmation(String title, String header, String message) {

        ButtonType okButtonType = new ButtonType(LanguageResource.getResource().getString("action.ok"),
                ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(LanguageResource.getResource().getString("action.cancel"),
                ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, okButtonType, cancelButtonType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(BuildResource.getApplicationIcon());
        alert.getDialogPane().getStylesheets().add(AlertDialog.class.getClass().getResource("/styles/style.css").toExternalForm());

        return alert.showAndWait();
    }

    /**
     * Alert dialog
     *
     * @param title title of alert dialog
     * @param header header of alert dialog
     * @param message message of alert dialog
     * @param alertType type of alert dialog
     */
    private static Alert alert(String title, String header, String message, Alert.AlertType alertType){

        ButtonType okButtonType = new ButtonType(LanguageResource.getResource().getString("action.ok"),
                ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(alertType, message, okButtonType);
        alert.setTitle(title);
        alert.setHeaderText(header);

        DialogPane dialogPane = alert.getDialogPane();
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(BuildResource.getApplicationIcon());
        dialogPane.getStylesheets().add(AlertDialog.class.getResource("/styles/style.css").toExternalForm());

        return alert;
    }
}
