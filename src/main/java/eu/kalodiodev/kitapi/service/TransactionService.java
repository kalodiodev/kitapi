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

package eu.kalodiodev.kitapi.service;

import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.dao.TransactionDao;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.time.LocalDate;
import java.util.List;


/**
 * Transaction Service
 *
 * <p>Provides transaction's operations</p>
 * <p>Transactions are also listed, added, removed or updated to an Observable list.</p>
 * <p>Transactions operations executed using transaction data access object, implemented for each type
 * of storage</p>
 *
 * @author Raptodimos Thanos
 */
public class TransactionService implements ITransactionService{

    private TransactionDao dao;

    /**
     * Categories Observable List
     */
    private ObservableList<Transaction> transactions;
    private FilteredList<Transaction> transactionFilteredList;

    /**
     * Transaction service constructor
     *
     * @param dao transaction data access object
     */
    public TransactionService(TransactionDao dao) {
        this.dao = dao;
        this.transactions = FXCollections.observableArrayList();
    }

    /**
     * Transaction service constructor
     */
    public TransactionService() {
        this.transactions = FXCollections.observableArrayList();
        this.transactionFilteredList = new FilteredList<>(transactions);
    }

    /**
     * Get data access object
     *
     * @return data access object
     */
    @Override
    public TransactionDao getDao() {
        return dao;
    }

    /**
     * Get transactions observable list
     *
     * @return transactions observable list
     */
    @Override
    public FilteredList<Transaction> getObservableList() {
        return transactionFilteredList;
    }

    /**
     * Get transactions sorted list
     *
     * @return transactions sorted list
     */
    @Override
    public SortedList<Transaction> getSortedList() {

        return new SortedList<>(getObservableList(),
                (o1, o2) -> {
                    if(o1.getDate().isAfter(o2.getDate())) {
                        return -1;
                    } else if (o1.getDate().isBefore(o2.getDate())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).sorted();
    }

    /**
     * Set data access object
     *
     * @param dao data access object to be used
     */
    @Override
    public void setDao(TransactionDao dao) {
        this.dao = dao;
    }

    /**
     * Get transactions
     *
     * @return transactions list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Transaction> all() throws RequestFailException {
        try {
            List<Transaction> transactionList = dao.all(GenericDao.OrderBy.NONE);
            transactions.setAll(transactionList);
            return transactionList;
        } catch (DaoException e) {
            throw new RequestFailException("Get transactions, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Get transactions
     * Order by date, <b>Descending</b> ordering
     *
     * @return transactions list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Transaction> allDescOrder() throws RequestFailException {
        try {
            List<Transaction> transactionList = dao.all(GenericDao.OrderBy.DESC);
            transactions.setAll(transactionList);
            return transactionList;
        } catch (DaoException e) {
            throw new RequestFailException("Get transactions, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Get transactions
     * Order by date, <b>Ascending</b> ordering
     *
     * @return transactions list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Transaction> allAscOrder() throws RequestFailException {
        try {
            List<Transaction> transactionList = dao.all(GenericDao.OrderBy.ASC);
            transactions.setAll(transactionList);
            return transactionList;
        } catch (DaoException e) {
            throw new RequestFailException("Get transactions, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Get transaction
     *
     * @param id transaction's id
     * @return transaction
     * @throws EntryNotFoundException if transaction not found
     * @throws RequestFailException if persistence storage operation failed
     * @throws InvalidIdException if id is invalid (negative)
     */
    @Override
    public Transaction get(int id) throws EntryNotFoundException, RequestFailException, InvalidIdException {
        // Validation
        if(id < 0)
            throw new InvalidIdException("Get transaction, id cannot be negative.");

        // Get transaction
        try {
            return dao.get(id);
        } catch (DaoException e) {
            throw new RequestFailException("Get transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        } catch (DaoEntryNotFoundException e) {
            throw new EntryNotFoundException("Get transaction failed, transaction not found.");
        }
    }

    /**
     * Add transaction
     *
     * @param transaction transaction to add
     * @return transaction's added id
     * @throws NullInputException if transaction is null
     * @throws EmptyInputException if transaction's name is null
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public int add(Transaction transaction) throws
            NullInputException, EmptyInputException, RequestFailException, EmptyDateException {

        // Validation
        if((transaction == null) || (transaction.getName() == null))
            throw new NullInputException("Adding transaction failed, transaction is null.");
        if(transaction.getName().isEmpty())
            throw new EmptyInputException("Adding transaction failed, transaction's name is empty.");
        if(transaction.getDate() == null)
            throw new EmptyDateException("Adding transaction failed, transaction's date is null.");

        // Add transaction
        try {
            int id = dao.add(transaction);
            transaction.setId(id);
            transactions.add(transaction);
            return id;
        } catch (DaoException e) {
            throw new RequestFailException("Add transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Update transaction
     *
     * @param currentTransaction transaction to be updated
     * @param updatedTransaction transaction to update
     * @throws NullInputException if transaction is null
     * @throws EntryNotFoundException if transaction to be updated not found
     * @throws RequestFailException if persistence storage operation failed
     * @throws EmptyInputException if updated transaction's name is empty
     * @throws EmptyDateException if updated transaction's date is empty
     */
    @Override
    public void update(Transaction currentTransaction, Transaction updatedTransaction) throws
            NullInputException, EntryNotFoundException, RequestFailException, EmptyInputException, EmptyDateException {

        // Validation
        if((currentTransaction == null) || (updatedTransaction == null))
            throw new NullInputException("Update transaction failed, transaction is null.");
        if(updatedTransaction.getName().isEmpty())
            throw new EmptyInputException("Updated transaction's name is empty, update failed.");
        if(updatedTransaction.getDate() == null)
            throw new EmptyDateException("Updating transaction failed, transaction's date is null.");

        try {
            if(!dao.exists(currentTransaction.getId()))
                throw new  EntryNotFoundException("Transaction to be updated not found.");
        } catch (DaoException e) {
            throw new RequestFailException("Update transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        }

        // Update transaction
        try {
            dao.update(currentTransaction, updatedTransaction);

            currentTransaction.setName(updatedTransaction.getName());
            currentTransaction.setDescription(updatedTransaction.getDescription());
            currentTransaction.setDate(updatedTransaction.getDate());
            currentTransaction.setAmount(updatedTransaction.getAmount());
            currentTransaction.setCategory(updatedTransaction.getCategory());

        } catch (DaoException e) {
            throw new RequestFailException("Update transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Remove transaction
     *
     * @param transaction transaction to remove
     * @throws NullInputException if transaction is null
     * @throws EntryNotFoundException if transaction to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void remove(Transaction transaction) throws
            NullInputException, EntryNotFoundException, RequestFailException {

        // Validation
        if(transaction == null)
            throw new NullInputException("Remove transaction failed, transaction is null.");
        try {
            if(!dao.exists(transaction.getId()))
                throw new  EntryNotFoundException("Transaction to be remove not found.");
        } catch (DaoException e) {
            throw new RequestFailException("Remove transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        }

        // Remove transaction
        try {
            dao.remove(transaction);
            transactions.remove(transaction);
        } catch (DaoException e) {
            throw new RequestFailException("Remove transaction, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Remove all transactions
     *
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void removeAll() throws RequestFailException {
        try {
            dao.removeAll();
            transactions.removeAll();
        } catch (DaoException e) {
            throw new RequestFailException("Remove all transactions, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Count transactions
     * Where name is <b>Equal</b> to given name
     *
     * @param name entry's name
     * @return number of transactions
     */
    @Override
    public int countEqual(String name) {
        return dao.count(name, GenericDao.CountCriteria.EQUAL);
    }

    /**
     * Count transactions
     * Where name is <b>Like</b> to given name
     *
     * @param name entry's name
     * @return number of transactions
     */
    @Override
    public int countLike(String name) {
        return dao.count(name, GenericDao.CountCriteria.LIKE);
    }

    /**
     * Count all transactions
     *
     * @return number of transactions
     */
    @Override
    public int count() {
        return dao.count();
    }

    /**
     * Check if transaction with provided id, exists in storage
     *
     * @param id transaction's id
     * @return true if exists, otherwise false
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public boolean exists(int id) throws RequestFailException {
        try {
            return dao.exists(id);
        } catch (DaoException e) {
            throw new RequestFailException("Check if transaction exists, " +
                    "persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of all entries</p>
     * <p>Calculates amount summary of all entries</p>
     *
     * @return total amount
     */
    public double listTotalAmount() {
        // Filter Transactions Filter List (All)
        transactionFilteredList.setPredicate(transaction -> true);
        // Return amounts summary
        return transactionFilteredList.stream().mapToDouble(Transaction::getAmount).sum() / 100d;
    }

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of given category.</p>
     * <p>Calculates amount summary of given category's transaction entries.</p>
     *
     * @param category transactions of this category
     * @return total amount
     */
    public double listTotalAmount(Category category) {
        // Filter Transactions of category
        transactionFilteredList.setPredicate(transaction -> transaction.getCategory().equals(category));
        // Return amounts summary
        return transactionFilteredList.stream().mapToDouble(Transaction::getAmount).sum() / 100.0d;
    }

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of given category, start date and end date.</p>
     * <p>Calculates amount summary of given category and time period transaction entries.</p>
     *
     * @param category transactions of this category
     * @param start period start date
     * @param end period end date
     * @return total amount
     * @throws EmptyDateException if date is null
     */
    public double listTotalAmount(Category category, LocalDate start, LocalDate end) throws EmptyDateException {

        if(start == null || end == null)
            throw new EmptyDateException("Cannot filter transaction list, date is null");

        // Filter category's transactions of given time period
        transactionFilteredList.setPredicate(transaction -> {
            // Check if transaction belongs to given category
            if((transaction.getCategory().equals(category))) {
                // Check if transaction belongs to given time period
                if((transaction.getDate().isAfter(start.minusDays(1)) && transaction.getDate().isBefore(end.plusDays(1)))) {
                    // Transaction meets given criteria
                    return true;
                }
            }
            // Transaction does not meet given criteria
            return false;
        });
        // Return amounts summary
        return transactionFilteredList.stream().mapToDouble(Transaction::getAmount).sum() / 100d;
    }

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of given time period.</p>
     * <p>Calculates transaction's amount summary of given time period.</p>
     *
     * @param start period start date
     * @param end period end date
     * @return total amount
     * @throws EmptyDateException if date is null
     */
    public double listTotalAmount(LocalDate start, LocalDate end) throws EmptyDateException {

        if(start == null || end == null)
            throw new EmptyDateException("Cannot filter transaction list, date is null");

        // Filter transactions of given time period
        transactionFilteredList.setPredicate(transaction ->
                ((transaction.getDate().isAfter(start.minusDays(1)) && transaction.getDate().isBefore(end.plusDays(1)))));
        // Return amounts summary
        return transactionFilteredList.stream().mapToDouble(Transaction::getAmount).sum() / 100d;
    }

    /**
     * Calculate total amount
     *
     * @return total amount
     */
    public double calculateTotal() {
        synchronized (this) {
            return transactions.stream().mapToDouble(Transaction::getAmount).sum() / 100d;
        }
    }

    /**
     * Calculate total amount
     *
     * @param since since date
     * @param until until date
     * @return total amount
     */
    public double calculateTotal(LocalDate since, LocalDate until) {
        synchronized (this) {
            return transactions.stream()
                    .filter(transaction -> (
                            transaction.getDate()
                                    .isAfter(since.minusDays(1)) && transaction.getDate().isBefore(until.plusDays(1))))
                    .mapToDouble(Transaction::getAmount).sum() / 100d;
        }
    }
}