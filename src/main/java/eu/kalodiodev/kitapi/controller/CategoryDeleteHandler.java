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


import eu.kalodiodev.kitapi.exceptions.EmptyInputException;
import eu.kalodiodev.kitapi.exceptions.EntryNotFoundException;
import eu.kalodiodev.kitapi.exceptions.NullInputException;
import eu.kalodiodev.kitapi.exceptions.RequestFailException;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Delete Category Handler
 *
 * @author Raptodimos Thanos
 */
public class CategoryDeleteHandler {

    private ICategoryService categoryService;
    private ResourceBundle resourceBundle;

    /**
     * Category delete handler Constructor
     *
     * @param categoryService category service to be used
     * @param resourceBundle resources bundle
     */
    public CategoryDeleteHandler(ICategoryService categoryService, ResourceBundle resourceBundle) {
        this.categoryService = categoryService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Delete category action
     *
     * <p>Shows confirmation dialog and deletes category after confirm.</p>
     *
     * @param categoryToDelete transaction to be deleted
     */
    public void delete(Category categoryToDelete) {

        Optional<ButtonType> result = AlertDialog.showAlertConfirmation(
                resourceBundle.getString("category.delete.confirmation"),
                resourceBundle.getString("category.delete.message") + "\n" + categoryToDelete.getName(),
                resourceBundle.getString("delete.confirm"));

        if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
            deleteCategory(categoryService, categoryToDelete);
        }
    }

    /**
     * Delete category from storage
     *
     * <p>Delete transaction from storage using category's service.</p>
     *
     * @param categoryService category service to be used
     * @param category category to be deleted
     */
    private void deleteCategory(ICategoryService categoryService, Category category) {
        try {
            categoryService.remove(category);

        } catch (RequestFailException | EntryNotFoundException |
                NullInputException | EmptyInputException e) {

            // Deletion failed dialog
            AlertDialog.showAlertError(resourceBundle.getString("fail"),
                    resourceBundle.getString("category.delete.fail"),
                    resourceBundle.getString("category.delete.fail.message"));
        }
    }
}