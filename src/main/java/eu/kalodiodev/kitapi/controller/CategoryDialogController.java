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

import eu.kalodiodev.kitapi.model.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Category Dialog Controller
 *
 * @author Raptodimos Thanos
 */
public class CategoryDialogController {

    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public Label errorMessageLabel;

    /**
     * Get New Category
     *
     * @return new Category
     */
    public Category getNewCategory() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        return new Category(name, description);
    }

    /**
     * Edit Category
     *
     * @param category category to edit
     */
    public void editCategory(Category category) {
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
    }

    /**
     * Set error message label
     *
     * @param message error message to setup
     */
    public void setErrorMessageLabel(String message) {
        errorMessageLabel.setText(message);
    }
}