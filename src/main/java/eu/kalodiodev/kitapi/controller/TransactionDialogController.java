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
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Transactions Dialog Controller
 *
 * @author Raptodimos Thanos
 */
public class TransactionDialogController implements Initializable{

    private DecimalFormat format = new DecimalFormat( "#.00");
    private ICategoryService categoryService;
    private ResourceBundle bundle;

    @FXML
    public ComboBox<Category> categoryComboBox;
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField amountField;
    @FXML
    public Label errorMessageLabel;


    /**
     * Controller initialization
     * <p>This method is executed automatically</p>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;

        // Date picker set date to now
        datePicker.setValue(LocalDate.now());

        // Categories comboBox, setup only categories names
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                return object.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        // Amount field input format
        amountField.setTextFormatter( new TextFormatter<>(text -> {
            if (text.getControlNewText().isEmpty()) {
                return text;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( text.getControlNewText(), parsePosition );

            if (object == null || parsePosition.getIndex() < text.getControlNewText().length()) {
                return null;
            }
            else {
                return text;
            }
        }));
    }

    /**
     * Get New Transaction
     *
     * Insert data to transaction from dialog fields.
     *
     * @return new transaction
     */
    public Transaction getNewTransaction() {
        Category category = categoryComboBox.getSelectionModel().getSelectedItem();
        String name = nameField.getText();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();
        double amount = 0;
        if(!amountField.getText().isEmpty()) {
            String tmpAmount = amountField.getText();
            if(tmpAmount.contains(","))
                tmpAmount = tmpAmount.replace(',','.');

            amount = Double.valueOf(tmpAmount);
        }

        Transaction transaction = new Transaction();
        transaction.setCategory(category);
        transaction.setName(name);
        transaction.setDescription(description);
        transaction.setDate(date);
        transaction.setAmountInCents(amount);

        return transaction;
    }

    /**
     * Set error message label
     *
     * @param message error message to setup
     */
    public void setErrorMessageLabel(String message) {
        errorMessageLabel.setText(message);
    }

    /**
     * Set categories list to ComboBox
     *
     * @param observableList categories observable list
     */
    public void setCategoriesList(ObservableList<Category> observableList) {
        categoryComboBox.setItems(observableList);
        categoryComboBox.getSelectionModel().selectFirst();
    }

    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Edit transaction
     *
     * Fill dialog fields with transaction's data
     *
     * @param transaction transaction to edit
     */
    public void editTransaction(Transaction transaction) {
        categoryComboBox.getSelectionModel().select(transaction.getCategory());
        nameField.setText(transaction.getName());
        descriptionField.setText(transaction.getDescription());
        datePicker.setValue(transaction.getDate());
        amountField.setText(format.format(transaction.getAmount() / 100.0));
    }

    /**
     * Handle Add category
     */
    public void handleAddCategory() {
        CategoryDialogHandler categoryDialogHandler = new CategoryDialogHandler(
                categoryComboBox.getScene().getWindow(), categoryService, bundle);

        try {
            categoryDialogHandler.show();
        } catch (IOException e) {
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("category.addnew.title"), bundle.getString("dialog.load.fail"));
        }
    }
}