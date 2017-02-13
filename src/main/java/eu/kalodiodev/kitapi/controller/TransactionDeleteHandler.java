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

import eu.kalodiodev.kitapi.exceptions.EntryNotFoundException;
import eu.kalodiodev.kitapi.exceptions.NullInputException;
import eu.kalodiodev.kitapi.exceptions.RequestFailException;
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.service.ITransactionService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Delete Transaction Handler
 *
 * @author Raptodimos Thanos
 */
public class TransactionDeleteHandler {

    private ITransactionService transactionService;
    private ResourceBundle resourceBundle;

    /**
     * Transaction's delete handler Constructor
     *
     * @param transactionService transaction service to be used
     * @param resourceBundle resources bundle
     */
    public TransactionDeleteHandler(ITransactionService transactionService, ResourceBundle resourceBundle) {
        this.transactionService = transactionService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Delete transaction action
     *
     * <p>Shows confirmation dialog and deletes transaction after confirm.</p>
     *
     * @param transactionToDelete transaction to be deleted
     */
    public void delete(Transaction transactionToDelete) {
        // Confirm Dialog
        Optional<ButtonType> result = AlertDialog.showAlertConfirmation(
                resourceBundle.getString("transaction.delete.confirmation"),
                resourceBundle.getString("transaction.delete.message") + "\n" + transactionToDelete.getName(),
                resourceBundle.getString("delete.confirm"));

        if (result.isPresent() && (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
            // Delete Transaction
            deleteTransaction(transactionService, transactionToDelete);
        }
    }

    /**
     * Delete transaction from storage
     *
     * <p>Delete transaction from storage using transaction's service.</p>
     *
     * @param transactionService transaction service to be used
     * @param transaction transaction to be deleted
     */
    private void deleteTransaction(ITransactionService transactionService, Transaction transaction) {
        try {
            transactionService.remove(transaction);

        } catch (RequestFailException | EntryNotFoundException | NullInputException e) {
            // Deletion failed dialog
            AlertDialog.showAlertError(resourceBundle.getString("fail"),
                    resourceBundle.getString("transaction.delete.fail"),
                    resourceBundle.getString("transaction.delete.fail.message"));
        }
    }
}