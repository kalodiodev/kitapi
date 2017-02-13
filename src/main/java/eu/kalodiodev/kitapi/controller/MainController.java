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
import eu.kalodiodev.kitapi.service.ITransactionService;
import eu.kalodiodev.kitapi.utils.AlertDialog;
import eu.kalodiodev.kitapi.utils.DatePeriod;
import eu.kalodiodev.kitapi.utils.MoneyFormat;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Main Window Controller
 *
 * @author Raptodimos Thanos
 */
public class MainController implements Initializable {

    // Services
    private ICategoryService incomeCategoryService;
    private ICategoryService expensesCategoryService;
    private ITransactionService incomeService;
    private ITransactionService expensesService;

    @FXML
    private BorderPane mainPanel;
    @FXML
    private MenuItem incomeCategoriesMenuItem;
    @FXML
    private MenuItem expensesCategoriesMenuItem;
    @FXML
    private MenuItem incomeMenuItem;
    @FXML
    private MenuItem expensesMenuItem;
    @FXML
    private Button incomeCategoriesButton;
    @FXML
    private Button expensesCategoriesButton;
    @FXML
    public Button expensesButton;
    @FXML
    public Text totalIncomeField;
    @FXML
    public Text totalExpensesField;
    @FXML
    public Text lastMonthExpensesText;
    @FXML
    public Text lastMonthIncomeText;
    @FXML
    public Text currentMonthExpensesText;
    @FXML
    public Text currentMonthIncomeText;
    @FXML
    public Text currentYearExpensesText;
    @FXML
    public Text currentYearIncomeText;
    @FXML
    public Text balanceField;


    private ResourceBundle bundle;

    /**
     * Controller initialize
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
    }

    /**
     * Set category services
     *
     * @param expensesCategoryService expenses categories service
     * @param incomeCategoryService income categories service
     */
    public void setCategoryServices(ICategoryService expensesCategoryService, ICategoryService incomeCategoryService) {
        this.expensesCategoryService = expensesCategoryService;
        this.incomeCategoryService = incomeCategoryService;
    }

    /**
     * Set transactions services
     *
     * @param expensesService expenses service
     * @param incomeService income service
     */
    public void setTransactionServices(ITransactionService expensesService, ITransactionService incomeService) {
        this.expensesService = expensesService;
        this.incomeService = incomeService;
    }

    /**
     * Update review total summaries
     */
    public void updateTotals() {
        // Initial balance value
        balanceField.setText(MoneyFormat.format(0.0));

        // Totals
        SimpleDoubleProperty incomeTotal = new SimpleDoubleProperty(0);
        SimpleDoubleProperty expensesTotal = new SimpleDoubleProperty(0);

        // Totals value change listener, to calculate balance
        ChangeListener<Number> totalListener = (observable, oldValue, newValue) ->
                balanceField.setText(MoneyFormat.format(
                        incomeTotal.doubleValue() - expensesTotal.doubleValue()));
        // Set listeners
        incomeTotal.addListener(totalListener);
        expensesTotal.addListener(totalListener);

        // Incomes
        calculateTotals(incomeService, totalIncomeField,
                currentMonthIncomeText, lastMonthIncomeText, currentYearIncomeText, incomeTotal);

        // Expenses
        calculateTotals(expensesService, totalExpensesField,
                currentMonthExpensesText, lastMonthExpensesText, currentYearExpensesText, expensesTotal);
    }

    /**
     * Calculate and show totals
     *
     * @param service transaction service
     * @param totalField total Text control
     * @param currentMonthField current month Text control
     * @param lastMonthField last month Text control
     * @param currentYearField current year Text control
     * @param total total
     */
    private void calculateTotals(ITransactionService service, Text totalField, Text currentMonthField,
                                 Text lastMonthField, Text currentYearField, SimpleDoubleProperty total) {

        // Calculations
        total.set(service.calculateTotal());
        double expensesLastMonthTotal = service.calculateTotal(
                DatePeriod.lastMonthsStart(1), DatePeriod.lastMonthsEnd(1));
        double expensesCurrentMonthTotal = service.calculateTotal(
                DatePeriod.lastMonthsStart(0), LocalDate.now());
        double expensesCurrentYear = service.calculateTotal(DatePeriod.currentYearStart(), LocalDate.now());

        // Show results
        totalField.setText(MoneyFormat.format(total.get()));
        currentMonthField.setText(MoneyFormat.format(expensesCurrentMonthTotal));
        lastMonthField.setText(MoneyFormat.format(expensesLastMonthTotal));
        currentYearField.setText(MoneyFormat.format(expensesCurrentYear));
    }

    /**
     * Handle Show Categories
     *
     * @param actionEvent action event
     */
    @FXML
    public void handleShowCategories(ActionEvent actionEvent) {
        // Income or Expenses Categories
        if((actionEvent.getSource().equals(incomeCategoriesMenuItem)) ||
                (actionEvent.getSource().equals(incomeCategoriesButton))) {
            // Income Categories
            showCategoriesDialog(incomeCategoryService, bundle.getString("category.income.window.title"),
                    bundle.getString("category.income.list"));

        } else {
            // Expenses Categories
            showCategoriesDialog(expensesCategoryService, bundle.getString("category.expenses.window.title"),
                    bundle.getString("category.expenses.list"));
        }

        // Update totals
        updateTotals();
    }

    /**
     * Show Categories Dialog
     *
     * @param service categories service to be uses
     * @param windowTitle window title
     * @param title table title
     */
    private void showCategoriesDialog(ICategoryService service, String windowTitle, String title) {

        CategoryHandler categoryHandler = new CategoryHandler(service, bundle);
        try {
            categoryHandler.show(windowTitle, title);
        } catch (IOException e) {
            System.out.println("Could not load categories: " + e.getMessage());
        }
    }

    /**
     * Handle setup transactions action
     */
    @FXML
    public void handleShowTransactions(ActionEvent actionEvent) {
        // Set Dialog title
        if(actionEvent.getSource().equals(expensesButton) || actionEvent.getSource().equals(expensesMenuItem)) {
            // Income Categories
            showTransactionsDialog(expensesService, expensesCategoryService,
                    bundle.getString("expenses"), bundle.getString("transaction.expenses.list"));
        }else {
            // Income Categories
            showTransactionsDialog(incomeService, incomeCategoryService,
                    bundle.getString("income"), bundle.getString("transaction.income.list"));
        }
        // Update review totals
        updateTotals();
    }

    /**
     * Show Transactions Dialog
     *
     * @param transactionService transactions service to use
     * @param categoryService category service to use
     * @param windowTitle window title
     * @param title table title
     */
    private void showTransactionsDialog(ITransactionService transactionService,
                                        ICategoryService categoryService, String windowTitle, String title) {

        TransactionsHandler transactionsHandler = new TransactionsHandler(transactionService, categoryService, bundle);
        // Show Stage
        try {
            transactionsHandler.show(windowTitle, title);
        } catch (IOException e) {
            System.out.println("Could not load transactions: " + e.getMessage());
        }
    }

    /**
     * Handle show about dialog
     */
    @FXML
    public void handleShowAbout() {
        AboutHandler aboutHandler = new AboutHandler(mainPanel.getScene().getWindow(), bundle);
        try {
            aboutHandler.show();
        } catch (IOException e) {
            AlertDialog.showAlertInformation("Problem","About","Could not load about.");
        }
    }

    /**
     * Handle Add new Expense
     */
    @FXML
    public void handleAddExpense() {
        try {
            addTransactionDialog(expensesService, expensesCategoryService);
        } catch (IOException e) {
            AlertDialog.showAlertInformation("Problem","Add Expense","Could not load dialog.");
        }

        updateTotals();
    }

    /**
     * Handle Add new Income
     */
    @FXML
    public void handleAddIncome() {
        try {
            addTransactionDialog(incomeService, incomeCategoryService);
        } catch (IOException e) {
            AlertDialog.showAlertInformation("Problem","Add Income","Could not load dialog.");
        }

        updateTotals();
    }

    /**
     * Show 'Add Transaction' dialog
     *
     * @param transactionService transaction service to be used
     * @param categoryService category service to be used
     * @throws IOException if failed to load dialog view
     */
    private void addTransactionDialog(ITransactionService transactionService,
                                      ICategoryService categoryService) throws IOException {

        TransactionDialogHandler transactionDialogHandler = new TransactionDialogHandler(
                mainPanel.getScene().getWindow(), transactionService, categoryService, bundle);

        transactionDialogHandler.show();
    }

    /**
     * Handle exit Menu item
     */
    @FXML
    public void handleExit() {
        Platform.exit();
    }
}