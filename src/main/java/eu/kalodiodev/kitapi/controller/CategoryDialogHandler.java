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

import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Handle Add or Edit Category
 *
 * <p>Show dialog to add or edit category and then store or update category to storage</p>
 *
 * @author Raptodimos Thanos
 */
public class CategoryDialogHandler {

    private Window window;
    private CategoryDialogController dialogController;
    private ICategoryService categoryService;
    private ResourceBundle bundle;

    /**
     * Category Dialog Handler Constructor
     *
     * @param window parent window
     * @param categoryService category service to be used
     * @param resourceBundle resources bundle
     */
    public CategoryDialogHandler(Window window, ICategoryService categoryService, ResourceBundle resourceBundle) {
        this.window = window;
        this.categoryService = categoryService;
        this.bundle = resourceBundle;
    }

    /**
     * Show Add new category dialog
     *
     * <p>Show dialog to input category's data and add category to storage.</p>
     * <p>Add category dialog shows until valid data inserted.</p>
     *
     * @return true on success, false on cancel
     */
    public boolean show() throws IOException {
        // Setup dialog
        Dialog<ButtonType> dialog = setupDialog(bundle.getString("category.addnew.title"));

        // Loop until a valid category is given or cancel pressed
        while(true) {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
                // Get new category
                Category newCategory = dialogController.getNewCategory();

                // Add Category
                if(addCategory(categoryService, newCategory, dialogController)) {
                    // Category added successfully
                    return true;
                }
            } else {
                // Cancelled
                return false;
            }
        }
    }

    /**
     * Show edit category dialog
     *
     * <p>Show dialog to edit category's data and update category to storage.</p>
     * <p>Edit category dialog shows until valid data inserted.</p>
     *
     * @return true on success, false on cancel
     */
    public boolean show(Category categoryToEdit) throws IOException {
        // Setup dialog
        Dialog<ButtonType> dialog = setupDialog(bundle.getString("category.edit.title"));

        dialogController.editCategory(categoryToEdit);

        // Loop until a valid category is given or cancel pressed
        while(true) {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {

                Category updatedCategory = dialogController.getNewCategory();
                // Update Category
                if(updateCategory(categoryService, categoryToEdit, updatedCategory, dialogController)) {
                    // Category updated successfully
                    return true;
                }
            } else {
                // Cancelled
                return false;
            }
        }
    }

    /**
     * Setup Category's dialog
     *
     * @param title dialog's title
     * @return category dialog
     * @throws IOException if category's dialog view could not be loaded
     */
    private Dialog<ButtonType> setupDialog(String title) throws IOException {

        // Category Dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(window);
        dialog.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/categorydialog.fxml"));
        fxmlLoader.setResources(bundle);
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType(bundle.getString("action.ok"), ButtonBar.ButtonData.OK_DONE));
        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType(bundle.getString("action.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE));

        // Add transaction dialog controller
        dialogController = fxmlLoader.getController();

        return dialog;
    }

    /**
     * Add new Category
     *
     * @param service category service @see {@link ICategoryService}
     * @param newCategory category to add
     * @param dialogController category dialog controller
     * @return true on success, false on fail
     */
    private boolean addCategory(ICategoryService service, Category newCategory,
                                CategoryDialogController dialogController) {

        try {
            // Add Category
            service.add(newCategory);
            return true;
        } catch (DuplicateEntryException e) {
            // Category with this name already exists
            dialogController.setErrorMessageLabel(bundle.getString("category.error.already_exists"));
            return false;
        } catch (EmptyInputException e) {
            // Empty category's name
            dialogController.setErrorMessageLabel(bundle.getString("error.empty.name"));
            return false;
        } catch (NullInputException | RequestFailException e) {
            // Input is null or failed to execute query
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("category.add.fail"),
                    bundle.getString("category.add.fail.message") + "\n" + newCategory.getName());
            return false;
        }
    }

    /**
     * Update Category
     *
     * @param service category service @see {@link ICategoryService}
     * @param oldCategory Category to Update
     * @param updatedCategory Updated Category
     * @param dialogController category dialog controller
     * @return true on success, false on fail
     */
    private boolean updateCategory(ICategoryService service, Category oldCategory,
                                   Category updatedCategory, CategoryDialogController dialogController) {

        try {
            // Update category
            service.update(oldCategory, updatedCategory);
            return true;
        } catch (DuplicateEntryException e) {
            // Category with same name already exists
            dialogController.setErrorMessageLabel(bundle.getString("category.error.already_exists"));
        } catch (EmptyInputException e) {
            // Category's name is empty
            dialogController.setErrorMessageLabel(bundle.getString("error.empty.name"));
        } catch (EntryNotFoundException e) {
            // Category to be updated not found
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("category.update.fail"),
                    bundle.getString("category.fail.not_found"));
        } catch (RequestFailException | NullInputException e) {
            // Null input or query failed
            dialogController.setErrorMessageLabel(bundle.getString("category.update.fail"));
        }
        return false;
    }
}