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

import eu.kalodiodev.kitapi.utils.BuildResource;
import eu.kalodiodev.kitapi.utils.LanguageResource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * About Controller
 *
 * @author Raptodimos Thanos
 */
public class AboutController {

    @FXML
    private Label versionLabel;
    @FXML
    private Label copyrightLabel;

    /**
     * Initialize Controller
     */
    public void initialize() {
        setupVersion();
        setupCopyright();
    }

    /**
     * Setup version label
     */
    private void setupVersion() {
        versionLabel.setText(BuildResource.getVersion());
    }

    /**
     * Setup copyright
     */
    private void setupCopyright() {
        String copyrightText;

        String copyrightStart = BuildResource.getCopyrightStart();
        String copyrightEnd = BuildResource.getCopyrightEnd();

        if(copyrightStart.equals(copyrightEnd)) {
            copyrightText = LanguageResource.getResource().getString("copyright.text.singledate");
            copyrightLabel.setText(String.format(copyrightText, copyrightStart));
        } else {
            copyrightText = LanguageResource.getResource().getString("copyright.text.period");
            copyrightLabel.setText(String.format(copyrightText, copyrightStart, copyrightEnd));
        }
    }

    /**
     * Handle website action
     *
     * @param event event called
     */
    @FXML
    public void handleWebsite(ActionEvent event) {
        try {
            URI uri = new URI(((Hyperlink) event.getSource()).getText());
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
 }
