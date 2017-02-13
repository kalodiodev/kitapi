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
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.service.ITransactionService;
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
 * Handle Add or Edit Transaction
 *
 * <p>Show dialog to add or edit transaction and then store or update transaction to storage</p>
 *
 * @author Raptodimos Thanos
 */
public class TransactionDialogHandler {

    private Window window;
    private TransactionDialogController dialogController;
    private ICategoryService categoryService;
    private ITransactionService transactionService;
    private ResourceBundle resourceBundle;

    /**
     * Transaction Dialog Handler Constructor
     *
     * @param window parent window
     * @param transactionService transaction service to be used
     * @param categoryService category service to be used
     * @param resourceBundle resources bundle
     */
    public TransactionDialogHandler(Window window, ITransactionService transactionService,
                                    ICategoryService categoryService, ResourceBundle resourceBundle) {
        this.window = window;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Show Add new transaction dialog
     *
     * <p>Show dialog to input transaction's data and add transaction to storage.</p>
     * <p>Add transaction dialog shows until valid data inserted.</p>
     * <p>If none category found, a dialog will ask to add a category.</p>
     *
     * @return true on success, false on cancel
     */
    public boolean show() throws IOException {

        // Check for available transactions
        if(categoryService.getSortedList().size() == 0) {
            // Confirm Dialog
            Optional<ButtonType> result = AlertDialog.showAlertConfirmation(
                    resourceBundle.getString("transaction.addnew.title"),
                    resourceBundle.getString("no.categories"),
                    resourceBundle.getString("ask.category.add"));

            if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
                if(!addCategoryDialog(categoryService)) {
                    // Category not added
                    return false;
                }
            }else {
                // Canceled
                return false;
            }
        }

        // At least one category exists, transaction can be added
        // Add Transaction, setup dialog
        Dialog<ButtonType> dialog = setupDialog(resourceBundle.getString("transaction.addnew.title"));

        // Loop until a valid transaction is given or cancel pressed
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
                // Get new Transaction from input
                Transaction newTransaction = dialogController.getNewTransaction();
                // Add Transaction
                if(addTransaction(transactionService, newTransaction, dialogController)) {
                    // Transaction successfully stored
                    return true;
                }
            } else {
                // Cancelled
                return false;
            }
        }
    }

    /**
     * Show Edit transaction dialog
     *
     * <p>Show dialog to edit transaction's data and update transaction to storage.</p>
     * <p>Edit transaction dialog shows until valid data inserted.</p>
     *
     * @return true on success, false on cancel
     */
    public boolean show(Transaction transactionToEdit) throws IOException {

        Dialog<ButtonType> dialog = setupDialog(resourceBundle.getString("transaction.edit.title"));

        dialogController.editTransaction(transactionToEdit);

        // Loop until a valid transaction is given or cancel pressed
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {

                Transaction updatedTransaction = dialogController.getNewTransaction();
                // Update Category
                if(updateTransaction(transactionToEdit, updatedTransaction, dialogController)) {
                    // Transaction successfully updated
                    return true;
                }
            } else {
                // Cancelled
                return false;
            }
        }
    }

    /**
     * Setup Transaction dialog
     *
     * @param title dialog's title
     * @return transaction dialog
     * @throws IOException if transaction's dialog view could not be loaded
     */
    private Dialog<ButtonType> setupDialog(String title) throws IOException {

        // Transaction Dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(window);
        dialog.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/transaction_dialog.fxml"));
        fxmlLoader.setResources(resourceBundle);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        dialog.getDialogPane().setContent(fxmlLoader.load());

        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType(resourceBundle.getString("action.ok"), ButtonBar.ButtonData.OK_DONE));
        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType(resourceBundle.getString("action.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE));

        // Add transaction dialog controller
        dialogController = fxmlLoader.getController();
        dialogController.setCategoriesList(categoryService.getObservableList());
        dialogController.setCategoryService(categoryService);

        return dialog;
    }

    /**
     * Add Category show dialog and handle
     *
     * @return true on successfully added category, false on fail or cancel
     */
    private boolean addCategoryDialog(ICategoryService categoryService) {
        CategoryDialogHandler categoryDialogHandler = new CategoryDialogHandler(window, categoryService, resourceBundle);

        try {
            return categoryDialogHandler.show();
        } catch (IOException e) {
            // Failed to load dialog
            AlertDialog.showAlertError(resourceBundle.getString("problem"),
                    resourceBundle.getString("category.addnew.title"),
                    resourceBundle.getString("dialog.load.fail"));
        }
        return false;
    }

    /**
     * Add Transaction to Storage
     *
     * <p>Store new transaction using transaction service.</p>
     * <p>If error occurs, feedback is provided through dialog controller.</p>
     *
     * @param transactionService transaction service
     * @param newTransaction transaction to be stored
     * @param dialogController dialog controller transaction inserted, used to provide feedback on error
     * @return true if transaction stored successfully, false if storing transaction failed
     */
    private boolean addTransaction(ITransactionService transactionService, Transaction newTransaction,
                                   TransactionDialogController dialogController) {
        try {
            // Add Transaction
            transactionService.add(newTransaction);
            return true;
        } catch (NullInputException | RequestFailException e) {
            // Null input or query fail
            AlertDialog.showAlertError(resourceBundle.getString("problem"),
                    resourceBundle.getString("transaction.add.fail") ,
                    resourceBundle.getString("transaction.add.fail.message") + "\n" + newTransaction.getName());
            return false;
        } catch (EmptyInputException e) {
            // Empty input
            dialogController.setErrorMessageLabel(resourceBundle.getString("error.empty.name"));
        } catch (EmptyDateException e) {
            // Empty date
            dialogController.setErrorMessageLabel(resourceBundle.getString("transaction.empty.date"));
        }
        return false;
    }

    /**
     * Update transaction in storage
     *
     * <p>Update transaction in storage, using transaction service.</p>
     * <p>If error occurs, feedback is provided through dialog controller.</p>
     *
     * @param selectedTransaction transaction to be updated
     * @param updatedTransaction updated transaction, this one should replace current transaction
     * @param dialogController transaction's data input dialog controller, used to provide feedback on error
     * @return true if updated successfully, false if updating transaction failed
     */
    private boolean updateTransaction(Transaction selectedTransaction, Transaction updatedTransaction,
                                      TransactionDialogController dialogController) {
        try {
            transactionService.update(selectedTransaction, updatedTransaction);
            return true;
        } catch (NullInputException | RequestFailException e) {
            // Input is null or query failed
            AlertDialog.showAlertError(resourceBundle.getString("problem"),
                    resourceBundle.getString("transaction.update.fail"),
                    resourceBundle.getString("transaction.update.fail.message") + "\n" + selectedTransaction.getName());
            return false;
        } catch (EntryNotFoundException e) {
            // Entry to update not found
            AlertDialog.showAlertError("Problem",
                    resourceBundle.getString("transaction.update.fail"),
                    resourceBundle.getString("transaction.update.fail.notfound"));
            return false;
        } catch (EmptyInputException e) {
            // Empty input
            dialogController.setErrorMessageLabel(resourceBundle.getString("error.empty.name"));
        } catch (EmptyDateException e) {
            // Empty date
            dialogController.setErrorMessageLabel(resourceBundle.getString("transaction.empty.date"));
        }
        return false;
    }
}