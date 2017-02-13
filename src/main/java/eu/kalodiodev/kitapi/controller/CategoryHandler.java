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

import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.utils.BuildResource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;


/**
 * Handle Show Categories
 *
 * @author Raptodimos Thanos
 */
public class CategoryHandler {

    private ICategoryService categoryService;
    private ResourceBundle resourceBundle;

    /**
     * Category Table handler
     *
     * @param categoryService category service
     * @param resourceBundle resources bundle
     */
    public CategoryHandler(ICategoryService categoryService, ResourceBundle resourceBundle) {
        this.categoryService = categoryService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Show Stage
     *
     * @param windowTitle stage title
     * @param tableTitle categories table title
     * @throws IOException if categories view could not be loaded
     */
    public void show(String windowTitle, String tableTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/categories.fxml"), resourceBundle);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(windowTitle);
        stage.getIcons().add(BuildResource.getApplicationIcon());
        stage.setScene(new Scene(root, 800, 500));

        // Controller
        CategoryController categoryController = fxmlLoader.getController();
        categoryController.setCategoryService(categoryService);
        categoryController.setTitle(tableTitle);
        categoryController.showCategories();

        stage.showAndWait();
    }
}