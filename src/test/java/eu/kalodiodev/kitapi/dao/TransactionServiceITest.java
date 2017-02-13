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

package eu.kalodiodev.kitapi.dao;

import eu.kalodiodev.kitapi.db.SqliteDatabase;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.service.CategoryService;
import eu.kalodiodev.kitapi.service.TransactionService;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Transaction Data Access Object Test class
 *
 * Contains common tests for every dao which implements TransactionDao Interface @see {@link TransactionDao}
 *
 * @author Raptodimos Thanos
 */
abstract public class TransactionServiceITest {

    private TransactionService transactionService = new TransactionService();
    private CategoryService categoryService = new CategoryService();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Transaction Dao Test Constructor
     *
     * @param transactionDao transaction data access object to be used
     * @param categoryDao category data access object to be used
     */
    TransactionServiceITest(TransactionDao transactionDao, CategoryDao categoryDao) {
        this.transactionService.setDao(transactionDao);
        this.categoryService.setDao(categoryDao);
    }

    /**
     * Setup before all - run once
     */
    @org.junit.BeforeClass
    public static void setupOnce() {
        SqliteDatabase.create();
    }

    /**
     * Setup before each test
     *
     * Clear Transaction table
     */
    @org.junit.Before
    public void setup() {
        System.out.println(getClass().getName() + ": Setup");

        clearTable(transactionService, categoryService);
    }

    /**
     * tear down after each test
     *
     * Clear Transaction table
     */
    @org.junit.After
    public void tearDown() {
        clearTable(transactionService, categoryService);
    }


    /**
     * Add Transaction Test
     *
     * Test adding new transaction entry, entries with same name are also allowed
     */
    @org.junit.Test
    public void add() throws RequestFailException,
            NullInputException, DuplicateEntryException, EmptyInputException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test Category", "My Test Category");

        // Add Transaction
        Transaction transaction1 = addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 110, category);

        Transaction transaction2 = addTransaction("Test 2", "Test 2 Transaction",
                LocalDate.now(), 120, category);

        // Transaction with same name with first transaction: should be normally stored
        Transaction transaction3 = addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 110, category);

        // Load Transactions
        List<Transaction> transactions = transactionService.all();

        //Assertions
        assertEquals("Should be three transactions in expenses table.", 3, transactions.size());
        assertEquals("These two transactions should be the same.", transaction1, transactions.get(0));
        assertEquals("These two transactions should be the same.", transaction2, transactions.get(1));
        assertNotEquals("These two transactions should not be the the same," +
                " even if their data are same.", transaction3, transactions.get(0));
        assertEquals("These two transactions should be the same.", transaction3, transactions.get(2));
    }

    /**
     * Add Transaction Test
     *
     * Must throw EmptyInputException, as transaction with empty name is not allowed
     */
    @org.junit.Test
    public void addEmptyNameException() throws NullInputException,
            RequestFailException, EmptyInputException, DuplicateEntryException, EmptyDateException {

        // Expected Exception
        thrown.expect(EmptyInputException.class);
        thrown.expectMessage("Adding transaction failed, transaction's name is empty.");

        // Add Category
        Category category = addCategory("Test Category", "My Test Category");

        // Add Transaction
        addTransaction("", "Test 1 Transaction", LocalDate.now(), 110, category);
    }

    /**
     * Delete Transaction Test
     */
    @org.junit.Test
    public void remove() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EntryNotFoundException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction transaction = addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        // Delete Transaction
        transactionService.remove(transaction);

        List<Transaction> transactions = transactionService.all();

        if(transactions == null) return;

        //Assertions
        assertEquals("Should be one transaction in expenses table.", 1, transactions.size());
    }

    /**
     * Remove all Transactions Test
     */
    @org.junit.Test
    public void removeAll() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        // Delete Transaction
        transactionService.removeAll();

        List<Transaction> transactions = transactionService.all();

        //Assertions
        assertEquals("Transactions table should be empty", 0, transactions.size());
    }

    /**
     * Get Transaction Test
     */
    @org.junit.Test
    public void get() throws RequestFailException, EmptyInputException,
            NullInputException, DuplicateEntryException, EntryNotFoundException, InvalidIdException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction transaction = addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        Transaction dbTransaction = transactionService.get(transaction.getId());

        //Assertions
        assertEquals("These two transactions should be the same.", transaction, dbTransaction);
    }

    /**
     * Get Transaction Test
     *
     * Test getting a transaction that does not exist in transactions table
     * Must throw EntryNotFoundException
     */
    @org.junit.Test
    public void getEntryNotFoundException() throws InvalidIdException,
            EntryNotFoundException, RequestFailException {

        // Expected Exception
        thrown.expect(EntryNotFoundException.class);
        thrown.expectMessage("Get transaction failed, transaction not found.");

        // Get transaction with id 10
        transactionService.get(10);
    }

    /**
     * Update Transaction Test
     */
    @org.junit.Test
    public void update() throws RequestFailException, EmptyInputException,
            NullInputException, DuplicateEntryException, EntryNotFoundException, InvalidIdException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction initialTransaction = addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        // Get Transaction to update from DB
        if(initialTransaction == null)
            throw new NullPointerException("Initial transaction is null");

        Transaction transaction = transactionService.get(initialTransaction.getId());

        // Update Transaction's fields
        transaction.setName("Updated Name");
        transaction.setDescription("Updated Description");
        transaction.setAmount(200);
        transaction.setDate(LocalDate.now());

        // Update Transaction in Database
        transactionService.update(initialTransaction, transaction);

        // Get Updated transaction
        Transaction updatedTransaction = transactionService.get(transaction.getId());

        //Assertions
        assertEquals("These two transactions should be the same.", transaction, updatedTransaction);
    }

    /**
     * Update Transaction
     *
     * Test updating a transaction to an empty name. Must throw EmptyInputException
     */
    @org.junit.Test
    public void updateEmptyNameException() throws RequestFailException, EmptyInputException,
            NullInputException, DuplicateEntryException, EntryNotFoundException, InvalidIdException, EmptyDateException {

        // Expected Exception
        thrown.expect(EmptyInputException.class);
        thrown.expectMessage("Updated transaction's name is empty, update failed.");

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction initialTransaction = addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        // Get Transaction to update from DB
        if(initialTransaction == null)
            throw new NullPointerException("Initial transaction is null");

        Transaction transaction = transactionService.get(initialTransaction.getId());

        // Update Transaction's fields
        transaction.setName("");
        transaction.setDescription("Updated Description");
        transaction.setAmount(200);
        transaction.setDate(LocalDate.now());

        // Update Transaction in Database
        transactionService.update(initialTransaction, transaction);
    }

    /**
     * Update Entry
     *
     * Test updating a transaction that does not exist. Must throw EntryNotFoundException.
     */
    @org.junit.Test
    public void updateEntryNotFoundException() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EntryNotFoundException, EmptyDateException {

        // Expected Exception
        thrown.expect(EntryNotFoundException.class);
        thrown.expectMessage("Transaction to be updated not found.");

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction transaction = new Transaction();
        transaction.setId(44);
        transaction.setName("Test");
        transaction.setDescription("Updated Description");
        transaction.setAmount(200);
        transaction.setDate(LocalDate.now());
        transaction.setCategory(category);

        // Transaction to Update to
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(44);
        updatedTransaction.setName("Test");
        updatedTransaction.setDescription("Updated Description");
        updatedTransaction.setAmount(200);
        updatedTransaction.setDate(LocalDate.now());
        updatedTransaction.setCategory(category);

        // Update Transaction in Database
        transactionService.update(transaction, updatedTransaction);
    }

    /**
     * Count All Transactions Test
     */
    @org.junit.Test
    public void countAll() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        assertEquals("Should be two transactions in transactions table.",
                2, transactionService.count());
    }

    /**
     * Count Transactions
     *
     * Count all transactions that name equals or is like the given name
     */
    @org.junit.Test
    public void countByName() throws RequestFailException, EmptyInputException, NullInputException, DuplicateEntryException, EmptyDateException {
        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        addTransaction("Transaction name", "Transaction Description",
                LocalDate.now(), 135, category);

        assertEquals("Should count one transaction with this name (equal) in expenses table.",
                1, transactionService.countEqual("Test"));
        assertEquals("Should count two with transactions with this name (like) in expenses table.",
                2, transactionService.countLike("Test"));
    }

    /**
     * Test Transaction's Exists check
     */
    @org.junit.Test
    public void exists() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transaction
        Transaction transaction = addTransaction("Test", "Test Transaction",
                LocalDate.now(), 100, category);

        addTransaction("Test 1", "Test 1 Transaction",
                LocalDate.now(), 135, category);

        assertTrue("Transaction must exist in database", transactionService.exists(transaction.getId()));
    }

    /**
     * Test Calculate transaction's total amount summary
     */
    @org.junit.Test
    public void calculateTotal() throws RequestFailException, EmptyInputException,
            NullInputException, DuplicateEntryException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transactions
        addTransaction("Test", "Test Transaction", LocalDate.now(), 100, category);
        addTransaction("Test 1", "Test 1 Transaction", LocalDate.now(), 135, category);

        assertEquals("Transactions total amount", 235, transactionService.calculateTotal(),0.1);
    }

    /**
     * Test Calculate transaction's total amount summary of specific time period
     */
    @org.junit.Test
    public void calculateTotalOfPeriod() throws RequestFailException, EmptyInputException,
            NullInputException, DuplicateEntryException, EmptyDateException {

        // Add Category
        Category category = addCategory("Test", "Test Category");

        // Add Transactions
        addTransaction("Test", "Test Transaction", LocalDate.now().minusDays(3), 100, category);
        addTransaction("Test 1", "Test 1 Transaction", LocalDate.now().minusDays(1), 135, category);
        addTransaction("Test 2", "Test 2 Transaction", LocalDate.now(), 200, category);

        assertEquals("Transactions total amount", 335,
                transactionService.calculateTotal(LocalDate.now().minusDays(1), LocalDate.now()),0.1);
    }

    /**
     * Add Category
     *
     * @param name category's name
     * @param description category's description
     * @return category
     * @throws RequestFailException if database query failed
     * @throws EmptyInputException if category's name is empty
     * @throws DuplicateEntryException if category with this name already exists
     */
    private Category addCategory(String name, String description)
            throws RequestFailException, EmptyInputException, NullInputException, DuplicateEntryException {

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        int categoryId = categoryService.add(category);
        category.setId(categoryId);

        return category;
    }

    /**
    * Add Transaction
    *
    * @param name transaction's name
    * @param description transaction's description
    * @param date transactions's date
    * @param amount transaction's amount
    * @param category transaction's category
    * @return transaction
    * @throws RequestFailException if database query fails
    * @throws EmptyInputException if transaction's name is empty
    * @throws NullInputException if transaction to add is null
    */
    private Transaction addTransaction(String name, String description, LocalDate date, int amount, Category category)
            throws NullInputException, RequestFailException, EmptyInputException, EmptyDateException {

        // New transaction
        Transaction transaction = new Transaction();
        transaction.setName(name);
        transaction.setDescription(description);
        transaction.setDate(date);
        // Set amount in cents
        transaction.setAmount(amount * 100);
        transaction.setCategory(category);

        // Get transaction's id
        int transactionId = transactionService.add(transaction);
        transaction.setId(transactionId);

        return transaction;
    }

    /**
     * Clear Table entries
     */
    private void clearTable(TransactionService transactionService, CategoryService categoryService) {

        try {
            transactionService.removeAll();
            categoryService.removeAll();
        } catch (RequestFailException e) {
            e.printStackTrace();
        }
    }
}