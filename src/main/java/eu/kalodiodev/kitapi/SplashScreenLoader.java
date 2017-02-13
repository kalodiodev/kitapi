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

package eu.kalodiodev.kitapi;

import eu.kalodiodev.kitapi.utils.LanguageResource;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Splash Screen Preloader
 *
 * @author Raptodimos Thanos
 */
public class SplashScreenLoader extends Preloader {
    private Stage splashScreen;

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        splashScreen = stage;
        splashScreen.setScene(createScene());
        splashScreen.show();
    }

    /**
     * Create scene
     *
     * @return scene
     */
    public Scene createScene() {

        Label title = new Label(LanguageResource.getResource().getString("app.title"));
        title.setStyle("-fx-font-size: 2.3em; -fx-text-fill: whitesmoke;");

        Label footer = new Label(LanguageResource.getResource().getString("splash.author"));
        footer.setStyle("-fx-font-size: 0.95em; -fx-text-fill: whitesmoke; -fx-font-style: oblique;");

        Label subtitle = new Label(LanguageResource.getResource().getString("splash.title"));
        subtitle.setStyle("-fx-font-size: 1.5em; -fx-text-fill: white;");

        VBox root = new VBox();
        root.setSpacing(10.0);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, subtitle, new ProgressIndicator());

        BorderPane pane = new BorderPane(root);
        pane.setBottom(footer);
        pane.setStyle("-fx-background-color: #646D7E;");

        return new Scene(pane, 480, 320, Color.ALICEBLUE);
    }

    /**
     * Application notification
     *
     * @param notification state change notification
     */
    @Override
    public void handleApplicationNotification(PreloaderNotification notification) {
        if (notification instanceof StateChangeNotification) {
            splashScreen.hide();
        }
    }
}
