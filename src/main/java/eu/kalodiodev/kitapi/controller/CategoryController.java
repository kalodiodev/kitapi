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
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Categories Controller
 *
 * @author Raptodimos Thanos
 */
public class CategoryController implements Initializable{

    private ICategoryService categoryService;

    @FXML
    private TableView<Category> categoriesTableView;
    @FXML
    private Label categoriesTitleLabel;
    @FXML
    public Button editCategoryButton;
    @FXML
    public Button deleteCategoryButton;
    @FXML
    private BorderPane categoriesPanel;

    private ResourceBundle bundle;


    /**
     * Controller Initialize
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
    }

    /**
     * Show Categories
     */
    @FXML
    public void showCategories() {
        SortedList<Category> categorySortedList = categoryService.getSortedList();
        categorySortedList.comparatorProperty().bind(categoriesTableView.comparatorProperty());
        categoriesTableView.setItems(categorySortedList);
    }

    /**
     * Set title label
     *
     * @param title title label text
     */
    public void setTitle(String title) {
        categoriesTitleLabel.setText(title);
    }

    /**
     * Set Category Dao
     *
     * @param categoryService Category service
     */
    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Handle Add New Category Button Action
     */
    @FXML
    public void handleAddCategory() {
        CategoryDialogHandler categoryDialogHandler = new CategoryDialogHandler(
                categoriesPanel.getScene().getWindow(), categoryService, bundle);

        try {
            categoryDialogHandler.show();
        } catch (IOException e) {
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("category.addnew.title"), bundle.getString("dialog.load.fail"));
        }
    }

    /**
     * Handle Edit Category Button Action
     */
    @FXML
    public void handleEditCategory() {
        // Get selected category
        Category selectedCategory = categoriesTableView.getSelectionModel().getSelectedItem();

        // No selection warning
        if(selectedCategory == null) {
            AlertDialog.showAlertInformation(bundle.getString("category.selected.none"), null,
                    bundle.getString("category.edit.select"));
            return;
        }

        // Category Dialog Handler
        CategoryDialogHandler categoryDialogHandler = new CategoryDialogHandler(
                categoriesPanel.getScene().getWindow(), categoryService, bundle);

        // Show Dialog
        try {
            categoryDialogHandler.show(selectedCategory);
        } catch (IOException e) {
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("category.edit.title"), bundle.getString("dialog.load.fail"));
        }
    }

    /**
     * Handle Delete Category Button Action
     *
     * <p>Get's selected category and calls category deletion after user confirmation.</p>
     * <p>Category deleted by {@link CategoryDeleteHandler}</p>
     * <p>If no category is selected, an info alert is shown.</p>
     */
    @FXML
    public void handleDeleteCategory() {
        // Get selected category
        Category selectedCategory = categoriesTableView.getSelectionModel().getSelectedItem();

        // No selection warning
        if(selectedCategory == null) {
            AlertDialog.showAlertInformation(bundle.getString("category.selected.none"), null,
                    bundle.getString("category.delete.select"));
            return;
        }

        // Delete category using category delete handler
        CategoryDeleteHandler categoryDeleteHandler = new CategoryDeleteHandler(categoryService, bundle);
        categoryDeleteHandler.delete(selectedCategory);
    }

    /**
     * Handle close window action
     */
    @FXML
    public void handleCloseWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}