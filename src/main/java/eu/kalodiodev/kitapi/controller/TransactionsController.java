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

import eu.kalodiodev.kitapi.exceptions.EmptyDateException;
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.service.ITransactionService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import eu.kalodiodev.kitapi.utils.MoneyFormat;
import eu.kalodiodev.kitapi.view.AmountCellFactory;
import eu.kalodiodev.kitapi.view.CategoryComboBox;
import eu.kalodiodev.kitapi.view.CategoryTableColumnCellValueFactory;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Transaction Controller
 *
 * @author Raptodimos Thanos
 */
public class TransactionsController implements Initializable{

    // Services
    private ICategoryService categoryService;
    private ITransactionService transactionService;

    private ResourceBundle bundle;

    @FXML
    private BorderPane transactionsPanel;
    @FXML
    private CategoryComboBox categoriesComboBox;
    @FXML
    private TableView<Transaction> transactionsTableView;
    @FXML
    private TableColumn<Transaction, String> categoryColumn;
    @FXML
    private TableColumn<Transaction, Long> amountColumn;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label transactionsTitleLabel;
    @FXML
    private DatePicker sinceDatePicker;
    @FXML
    private DatePicker untilDatePicker;
    @FXML
    private RadioButton allCategoriesRadioButton;
    @FXML
    private RadioButton allPeriodRadioButton;
    @FXML
    private Label sinceLabel;
    @FXML
    private Label untilLabel;


    /**
     * Transactions Controller initialization
     * <p>This method is called automatically.</p>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;

        // Category's column, setup only category's name
        categoryColumn.setCellValueFactory(new CategoryTableColumnCellValueFactory());
        // Amount column, cell factory
        amountColumn.setCellFactory(new AmountCellFactory());

        // RadioButtons, transactions not filtered by default
        allCategoriesRadioButton.setSelected(true);
        allPeriodRadioButton.setSelected(true);

        // Toggle categories ComboBox status
        allCategoriesRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            categoriesComboBox.setDisable(newValue);
        });

        // Toggle time period input status
        allPeriodRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            sinceLabel.setDisable(newValue);
            untilLabel.setDisable(newValue);
            sinceDatePicker.setDisable(newValue);
            untilDatePicker.setDisable(newValue);
        });

        // Filtering date pickers initial value
        sinceDatePicker.setValue(LocalDate.now());
        untilDatePicker.setValue(LocalDate.now());
    }

    /**
     * Transactions table title
     *
     * @param title table title
     */
    public void setTableTitle(String title) {
        transactionsTitleLabel.setText(title);
    }

    /**
     * Show transactions
     *
     * Show transactions, using @see {@link ITransactionService}
     * Show available categories, using @see {@link ICategoryService}
     */
    public void showData() {
        SortedList<Transaction> transactionSortedList = transactionService.getSortedList();
        transactionSortedList.comparatorProperty().bind(transactionsTableView.comparatorProperty());

        transactionsTableView.setItems(transactionSortedList);

        // Categories comboBox
        categoriesComboBox.setItems(categoryService.getSortedList());
        categoriesComboBox.getSelectionModel().selectFirst();

        totalAmountLabel.setText(MoneyFormat.format(transactionService.listTotalAmount()));
    }

    /**
     * Set Category Service
     *
     * <p>Set category service to be used for categories(income or expenses) operations</p>
     *
     * @param categoryService category service to be used
     */
    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Set Transaction Service
     *
     * <p>Set transaction service to be used for transactions(income or expenses) operations</p>
     *
     * @param transactionService transaction service to be used
     */
    public void setTransactionService(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handle add new transaction action
     */
    @FXML
    public void handleAddTransaction() {
        TransactionDialogHandler transactionDialogHandler = new TransactionDialogHandler(
                transactionsPanel.getScene().getWindow(), transactionService, categoryService, bundle);

        // Show Dialog
        try {
            transactionDialogHandler.show();
        } catch (IOException e) {
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("transaction.addnew.title"), bundle.getString("dialog.load.fail"));
        }
    }

    /**
     * Handle edit transaction action
     */
    @FXML
    public void handleEditTransaction() {
        // Get selected category
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();

        // No selection warning
        if(selectedTransaction == null) {
            AlertDialog.showAlertInformation(bundle.getString("transaction.selected.none"), null,
                    bundle.getString("transaction.edit.select"));
            return;
        }

        // Transactions Dialog Handler
        TransactionDialogHandler transactionDialogHandler = new TransactionDialogHandler(
                transactionsPanel.getScene().getWindow(), transactionService, categoryService, bundle);

        // Show dialog
        try {
           transactionDialogHandler.show(selectedTransaction);
        } catch (IOException e) {
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("transaction.edit.title"), bundle.getString("dialog.load.fail"));
        }
    }

    /**
     * Handle delete transaction Action
     *
     * <p>Get's selected transaction and calls transaction deletion after user confirmation.</p>
     * <p>Transaction deleted by {@link TransactionDeleteHandler}</p>
     * <p>If no transaction is selected, an info alert is shown.</p>
     */
    @FXML
    public void handleDeleteTransaction() {
        // Get selected category
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();

        // No selection warning
        if(selectedTransaction == null) {
            AlertDialog.showAlertInformation(bundle.getString("transaction.selected.none"), null,
                    bundle.getString("transaction.delete.select"));
            return;
        }

        // Delete transaction using transaction delete handler
        TransactionDeleteHandler transactionDeleteHandler = new TransactionDeleteHandler(transactionService, bundle);
        transactionDeleteHandler.delete(selectedTransaction);
    }

    /**
     * Handle Filter Action
     */
    @FXML
    public void handleFilter() {
        try {
            totalAmountLabel.setText(MoneyFormat.format(
                    filterCalculation(!allCategoriesRadioButton.isSelected(), !allPeriodRadioButton.isSelected())
            ));
        } catch (EmptyDateException e) {
            // Empty Date
            AlertDialog.showAlertError(bundle.getString("problem"),
                    bundle.getString("filter.fail"), bundle.getString("transaction.empty.date"));
        }
    }

    /**
     * Filter entries and calculate transactions amount summary
     *
     * @param filterByCategory if should be filtered by category
     * @param filterByPeriod if should be filtered by time period
     * @return total amount
     * @throws EmptyDateException if date input is empty
     */
    private double filterCalculation(boolean filterByCategory, boolean filterByPeriod) throws EmptyDateException {

        if(filterByPeriod) {
            // Transactions should be filtered by time period, getting time period
            LocalDate since = sinceDatePicker.getValue();
            LocalDate until = untilDatePicker.getValue();

            if(since == null || until == null)
                throw new EmptyDateException("Failed to filter, date is empty");

            // Swap dates if since date is after until
            if(since.isAfter(until)){
                LocalDate tmp = since;
                since = until;
                until = tmp;
            }
            // Total amount, Filtered by period and optionally by category
            return calculateFilteredByPeriod(since, until, filterByCategory);
        }
        // Total amount, filtered by given category
        return calculateFilteredByCategory(filterByCategory);
    }

    /**
     * Calculate transactions amount summary
     * Transactions filtered by time period and optionally by category
     *
     * @param start period start date
     * @param end period end date
     * @param filterByCategory filter also by category
     * @return total amount
     * @throws EmptyDateException if date input is empty
     */
    private double calculateFilteredByPeriod(LocalDate start, LocalDate end,
                                             boolean filterByCategory) throws EmptyDateException {

        if (filterByCategory) {
            // Transactions filtered by time period and category
            return transactionService.listTotalAmount(
                    categoriesComboBox.getSelectionModel().getSelectedItem(), start, end);
        } else {
            // Transactions filtered only by time period
            return transactionService.listTotalAmount(start, end);
        }
    }

    /**
     * Calculate transactions amount summary
     * Transactions may be filtered by category
     *
     * @param filterByCategory is filtering by category enabled
     * @return total amount
     */
    private double calculateFilteredByCategory(boolean filterByCategory) {
        if(filterByCategory) {
            // Transactions filtered only by category
            return transactionService.listTotalAmount(categoriesComboBox.getSelectionModel().getSelectedItem());
        } else {
            // Transactions not filtered
            return transactionService.listTotalAmount();
        }
    }

    /**
     * Handle close window
     */
    @FXML
    public void handleCloseWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}