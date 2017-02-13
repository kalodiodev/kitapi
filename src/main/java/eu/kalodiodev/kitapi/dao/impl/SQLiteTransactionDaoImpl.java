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

package eu.kalodiodev.kitapi.dao.impl;

import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.dao.TransactionDao;
import eu.kalodiodev.kitapi.db.JdbcSqliteConnection;
import eu.kalodiodev.kitapi.exceptions.DaoEntryNotFoundException;
import eu.kalodiodev.kitapi.exceptions.DaoException;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.model.CategoryTable;
import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.model.TransactionTable;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Class of SQLite Transaction DAO
 *
 * Implements Transaction DAO Interface
 * All SQLite Transaction DAOs should extend from this class
 *
 * @author Raptodimos Thanos
 */
abstract class SQLiteTransactionDaoImpl implements TransactionDao {

    // Tables
    private TransactionTable transactionTable;
    private CategoryTable categoryTable;

    /**
     * SQLite Database Connection
     */
    private JdbcSqliteConnection connection = new JdbcSqliteConnection();

    /**
     * SQLite Transaction Data Access Object constructor
     *
     * @param transactionTable transactions table structure
     * @param categoryTable categories table structure
     */
    SQLiteTransactionDaoImpl(TransactionTable transactionTable, CategoryTable categoryTable) {

        this.transactionTable = transactionTable;
        this.categoryTable = categoryTable;
    }

    /**
     * Get all transactions
     *
     * @param orderBy order by for date column @see {@link GenericDao.OrderBy}
     * @return transactions list
     * @throws DaoException if get all entries database query failed
     */
    @Override
    public List<Transaction> all(GenericDao.OrderBy orderBy) throws DaoException {

        // Database query
        String query = "SELECT " +
                transactionTable.getTableName() + "." + transactionTable.getIdColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getNameColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDateColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getAmountColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getIdColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getNameColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getDescriptionColumn() +
                " FROM " + transactionTable.getTableName() +
                " INNER JOIN " + categoryTable.getTableName() +
                " ON " + transactionTable.getTableName() + "." + transactionTable.getCategoryColumn() +
                " = " + categoryTable.getTableName() + "." + categoryTable.getIdColumn();

        // Order By Query
        StringBuilder sb = new StringBuilder(query);
        if(!orderBy.equals(GenericDao.OrderBy.NONE)) {
            sb.append(" ORDER BY ");
            sb.append(transactionTable.getDateColumn());
            sb.append(" COLLATE NOCASE ");
            if(orderBy.equals(GenericDao.OrderBy.ASC)) {
                sb.append("ASC");
            } else {
                sb.append("DESC");
            }
        }

        // Get transactions
        synchronized (this) {
            try {
                return getTransactions(sb.toString());
            } catch (SQLException e) {
                throw new DaoException("Could not load transactions: " + e.getMessage());
            }
        }
    }

    /**
     * Check if entry exists in database
     *
     * @param id entry's id
     * @return true if exists, false if not
     */
    @Override
    public boolean exists(int id) throws DaoException {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + transactionTable.getTableName() +
                " WHERE " + transactionTable.getIdColumn() + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement queryCategory = conn.prepareStatement(query)) {

                queryCategory.setInt(1, id);

                try (ResultSet results = queryCategory.executeQuery()) {
                    return results.getInt("count") > 0;
                }
            } catch (SQLException e) {
                throw new DaoException("Count transactions query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Count Transaction Entries
     *
     * @return number of entries, returns -1 on fail
     */
    @Override
    public int count() {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + transactionTable.getTableName();

        synchronized (this) {
            try (Connection conn = connection.connect();
                 Statement queryCategory = conn.createStatement();
                 ResultSet results = queryCategory.executeQuery(query)) {

                return results.getInt("count");

            } catch (SQLException e) {
                return -1;
            }
        }
    }

    /**
     * Count Transaction Entries
     *
     * @param name transaction's name
     * @param criteria count by name criteria @see {@link CountCriteria}
     * @return number of entries, returns -1 on fail
     */
    @Override
    public int count(String name, CountCriteria criteria) {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + transactionTable.getTableName() +
                " WHERE " + transactionTable.getNameColumn();

        if (criteria.equals(CountCriteria.EQUAL)) {
            query += " = ?";
        } else {
            query += " LIKE ?";
            name = '%' + name + '%';
        }

        synchronized (this) {
            return SQLiteHelper.executeNameCountQuery(connection, query, name, "count");
        }
    }

    /**
     * Add new Transaction
     *
     * @param transaction transaction to add
     * @return new transaction's id
     * @throws DaoException if insert transaction database query failed
     */
    @Override
    public int add(Transaction transaction) throws DaoException {
        // Database query
        String query = "INSERT INTO " + transactionTable.getTableName() + "(" +
                transactionTable.getNameColumn() + ", " +
                transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getDateColumn() + ", " +
                transactionTable.getAmountColumn() + ", " +
                transactionTable.getCategoryColumn() +
                ") VALUES(?, ?, ?, ?, ?)";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement insertIntoTransactions = conn.prepareStatement(query)) {
                // Set Data to query
                insertIntoTransactions.setString(1, transaction.getName());
                insertIntoTransactions.setString(2, transaction.getDescription());
                insertIntoTransactions.setDate(3, Date.valueOf(transaction.getDate()));
                insertIntoTransactions.setLong(4, transaction.getAmount());
                insertIntoTransactions.setInt(5, transaction.getCategory().getId());

                // Execute query and get affected rows
                int affectedRows = insertIntoTransactions.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("The transaction insert failed!");
                }
                // Get Generated Key
                return SQLiteHelper.getGeneratedKey(insertIntoTransactions.getGeneratedKeys(), 1);

            } catch (Exception e) {
                System.out.println("Insert transaction exception: " + e.getMessage());
                throw new DaoException("Insert new transaction query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Update Transaction
     *
     * @param transaction transaction to update
     * @param updatedTransaction update transaction with this one
     * @throws DaoException if database query failed
     */
    @Override
    public void update(Transaction transaction, Transaction updatedTransaction) throws DaoException {

        // Database query
        String query = "UPDATE " + transactionTable.getTableName() + " SET " +
                transactionTable.getNameColumn() + " = ? ," +
                transactionTable.getDescriptionColumn() + " = ? ," +
                transactionTable.getDateColumn() + " = ? ," +
                transactionTable.getAmountColumn() + " = ? ," +
                transactionTable.getCategoryColumn() + " = ? " +
                " WHERE " + transactionTable.getIdColumn() + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect()) {

                try (PreparedStatement updateCategory = conn.prepareStatement(query)) {

                    conn.setAutoCommit(false);

                    //Set Data to query
                    updateCategory.setString(1, updatedTransaction.getName());
                    updateCategory.setString(2, updatedTransaction.getDescription());
                    updateCategory.setDate(3, Date.valueOf(updatedTransaction.getDate()));
                    updateCategory.setLong(4, updatedTransaction.getAmount());
                    updateCategory.setInt(5, updatedTransaction.getCategory().getId());
                    updateCategory.setInt(6, transaction.getId());

                    //Execute query and get affected rows
                    int affectedRows = updateCategory.executeUpdate();
                    if (affectedRows == 1) {
                        conn.commit();
                    } else {
                        // No affected rows, throw exception
                        throw new SQLException("The transaction update query failed!");
                    }

                } catch (Exception e) {
                    // Statement failed, Rollback
                    try {
                        conn.rollback();
                    } catch (SQLException e2) {
                        // RollBack failed
                        throw new DaoException("Update transaction failed, rolling back failed: " + e2.getMessage());
                    }
                    // Throw DaoException, statement execution failed
                    throw new DaoException("Update transaction query failed!");
                } finally {
                    // Reset auto commit to default
                    try {
                        connection.getConnection().setAutoCommit(true);
                    } catch (SQLException e) {
                        // Failed to reset auto-commit
                        System.out.println("Couldn't reset auto-commit! " + e.getMessage());
                    }
                }
            } catch (SQLException e) {
                // Connection failed
                throw new DaoException("Update transaction query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Remove Transaction
     *
     * @param transaction transaction to delete
     * @throws DaoException if delete transaction database query failed
     */
    @Override
    public void remove(Transaction transaction) throws DaoException {
        // Query
        String query = "DELETE FROM " + transactionTable.getTableName() +
                " WHERE " + transactionTable.getIdColumn() + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement deleteTransaction = conn.prepareStatement(query)) {
                // Delete transaction
                deleteTransaction.setInt(1, transaction.getId());
                deleteTransaction.executeUpdate();

            } catch (SQLException e) {
                // Failed throw DaoException
                throw new DaoException("Delete transaction query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Remove all transactions
     *
     * @throws DaoException if delete all transactions database query failed
     */
    @Override
    public void removeAll() throws DaoException {
        // Database query
        String query = "DELETE FROM " + transactionTable.getTableName();

        synchronized (this) {
            try (Connection conn = connection.connect();
                 Statement deleteTransactions = conn.createStatement()) {
                // Delete all transactions
                deleteTransactions.executeUpdate(query);

            } catch (SQLException e) {
                throw new DaoException("Delete all transactions query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Get transaction
     *
     * @param id item's id
     * @return transaction
     * @throws DaoException if database query failed
     * @throws DaoEntryNotFoundException if transaction entry not found
     */
    @Override
    public Transaction get(int id) throws DaoException, DaoEntryNotFoundException {
        // Database query
        String query = "SELECT " +
                transactionTable.getTableName() + "." + transactionTable.getIdColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getNameColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDateColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getAmountColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getIdColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getNameColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getDescriptionColumn() +
                " FROM " + transactionTable.getTableName() +
                " INNER JOIN " + categoryTable.getTableName() +
                " ON " + transactionTable.getTableName() + "." + transactionTable.getCategoryColumn() +
                " = " + categoryTable.getTableName() + "." + categoryTable.getIdColumn() +
                " WHERE " + transactionTable.getTableName() + "." + transactionTable.getIdColumn() + " = ?";


        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement queryCategory = conn.prepareStatement(query)) {

                queryCategory.setInt(1, id);

                // Execute query and get result
                try (ResultSet results = queryCategory.executeQuery()) {
                    if (results.next()) {
                        // Get Transaction
                        Transaction transaction = new Transaction();
                        transaction.setId(results.getInt(1));
                        transaction.setName(results.getString(2));
                        transaction.setDescription(results.getString(3));
                        transaction.setDate(results.getDate(4).toLocalDate());
                        transaction.setAmount(results.getLong(5));

                        //Get Transaction's category
                        Category category = new Category();
                        category.setId(results.getInt(6));
                        category.setName(results.getString(7));
                        category.setDescription(results.getString(8));

                        // Set Category to transaction
                        transaction.setCategory(category);

                        return transaction;
                    } else {
                        throw new DaoEntryNotFoundException("Transaction entry not found!");
                    }
                }
            } catch (SQLException e) {
                throw new DaoException("Failed to get transaction: " + e.getMessage());
            }
        }
    }

    /**
     * Get latest transactions
     *
     * @param rows number of row to return
     * @return transactions list limited to number of rows provided
     * @throws DaoException if getting transactions database query failed
     */
    @Override
    public List<Transaction> latest(int rows) throws DaoException {
        // Database query
        String query = "SELECT " +
                transactionTable.getTableName() + "." + transactionTable.getIdColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getNameColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDateColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getAmountColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getIdColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getNameColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getDescriptionColumn() +
                " FROM " + transactionTable.getTableName() +
                " INNER JOIN " + categoryTable.getTableName() +
                " ON " + transactionTable.getTableName() + "." + transactionTable.getCategoryColumn() +
                " = " + categoryTable.getTableName() + "." + categoryTable.getIdColumn() +
                " ORDER BY " + transactionTable.getNameColumn() + "." + transactionTable.getDateColumn() +
                " COLLATE NOCASE DESC LIMIT " + rows;

        synchronized (this) {
            try {
                return getTransactions(query);
            } catch (SQLException e) {
                throw new DaoException("Could not load transactions: " + e.getMessage());
            }
        }
    }

    /**
     * Get latest transactions
     *
     * @param since since date
     * @return transactions list since date provided
     * @throws DaoException if getting transactions database query failed
     */
    @Override
    public List<Transaction> latest(LocalDate since) throws DaoException {

        // Database query
        String query = "SELECT " +
                transactionTable.getTableName() + "." + transactionTable.getIdColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getNameColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDateColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getAmountColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getIdColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getNameColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getDescriptionColumn() +
                " FROM " + transactionTable.getTableName() +
                " INNER JOIN " + categoryTable.getTableName() +
                " ON " + transactionTable.getTableName() + "." + transactionTable.getCategoryColumn() +
                " = " + categoryTable.getTableName() + "." + categoryTable.getIdColumn() +
                " WHERE " + transactionTable.getTableName() + "." + transactionTable.getDateColumn() + " >= ? " +
                " ORDER BY " + transactionTable.getNameColumn() + "." + transactionTable.getDateColumn() +
                " COLLATE NOCASE DESC";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setDate(1, Date.valueOf(since));

                try (ResultSet results = statement.executeQuery()) {
                    return resultsToTransactionList(results);
                }
            } catch (SQLException e) {
                throw new DaoException("Could not load transactions: " + e.getMessage());
            }
        }
    }

    /**
     * Get all transactions
     *
     * @return transactions list ordered by date, latest first
     * @throws DaoException if database query failed
     */
    @Override
    public List<Transaction> latest() throws DaoException {
        // Database query
        String query = "SELECT " +
                transactionTable.getTableName() + "." + transactionTable.getIdColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getNameColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDescriptionColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getDateColumn() + ", " +
                transactionTable.getTableName() + "." + transactionTable.getAmountColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getIdColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getNameColumn() + ", " +
                categoryTable.getTableName() + "." + categoryTable.getDescriptionColumn() +
                " FROM " + transactionTable.getTableName() +
                " INNER JOIN " + categoryTable.getTableName() +
                " ON " + transactionTable.getTableName() + "." + transactionTable.getCategoryColumn() +
                " = " + categoryTable.getTableName() + "." + categoryTable.getIdColumn() +
                " ORDER BY " + transactionTable.getNameColumn() + "." + transactionTable.getDateColumn() +
                " COLLATE NOCASE DESC";

        try {
            return getTransactions(query);
        } catch (SQLException e) {
            throw new DaoException("Could not load transactions: " + e.getMessage());
        }
    }

    /**
     * Get total amount
     *
     * @return transactions amount summary
     * @throws DaoException if database query failed
     */
    @Override
    public long getTotalAmount() throws DaoException {

        String query = "SELECT SUM(" + transactionTable.getAmountColumn() +
                ") AS summary FROM " + transactionTable.getTableName();

        try(Connection conn = connection.connect();
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(query)) {

            if(results.next()) {
                return results.getLong("summary");
            }
            return 0L;
        }catch (SQLException e) {
            throw new DaoException("Transactions amount summary query failed: " + e.getMessage());
        }
    }

    /**
     * Get total amount
     *
     * @param start summary calculations since date
     * @param end summary calculations until date
     * @return transactions amount summary
     * @throws  DaoException if database query failed
     */
    @Override
    public long getTotalAmount(LocalDate start, LocalDate end) throws DaoException {
        // Query
        String query = "SELECT SUM(" + transactionTable.getAmountColumn() +
                ") AS summary FROM " + transactionTable.getTableName() +
                " WHERE " + transactionTable.getDateColumn() + " BETWEEN ? AND ?";

        // Connection
        try(Connection conn = connection.connect();
            PreparedStatement statement = conn.prepareStatement(query)) {

            // Set dates
            statement.setDate(1, Date.valueOf(start));
            statement.setDate(2, Date.valueOf(end));

            // Get Result
            try(ResultSet results = statement.executeQuery()) {
                if(results.next()) {
                    return results.getLong("summary");
                }
            }

            return 0L;
        }catch (SQLException e) {
            throw new DaoException("Transactions amount summary query failed: " + e.getMessage());
        }
    }

    /**
     * Get total amount
     *
     * @param since summary calculation since date
     * @return transactions amount summary
     * @throws DaoException if database query failed
     */
    @Override
    public long getTotalAmount(LocalDate since) throws DaoException {
        // Query
        String query = "SELECT SUM(" + transactionTable.getAmountColumn() +
                ") AS summary FROM " + transactionTable.getTableName() +
                " WHERE " + transactionTable.getDateColumn() + " >= ? ";

        // Connect
        try(Connection conn = connection.connect();
            PreparedStatement statement = conn.prepareStatement(query)) {

            // Set Since date
            statement.setDate(1, Date.valueOf(since));

            // Get result
            try(ResultSet results = statement.executeQuery()) {
                if(results.next()) {
                    return results.getLong("summary");
                }
            }
            return 0L;
        }catch (SQLException e) {
            throw new DaoException("Transactions amount summary query failed: " + e.getMessage());
        }
    }

    /**
     * Get transactions
     *
     * @param query get transactions database query
     * @return transactions list
     * @throws SQLException if database query failed
     */
    private List<Transaction> getTransactions(String query) throws SQLException {

        try(Connection conn = connection.connect();
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(query)) {

            return resultsToTransactionList(results);
        }
    }

    /**
     * Get Transactions list from statement query execution results
     *
     * @param results result set
     * @return transactions array list
     * @throws SQLException if sql query fails
     */
    private List<Transaction> resultsToTransactionList(ResultSet results) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        while (results.next()) {
            // Get Transaction
            Transaction transaction = resultsToTransaction(results);
            //Get Transaction's category
            Category category = resultsToCategory(results);
            // Set category to transaction
            transaction.setCategory(category);
            // Add transaction to list
            transactions.add(transaction);
        }
        return transactions;
    }

    /**
     * Get transaction from results
     *
     * @param results result set
     * @return transaction
     * @throws SQLException if sql query fails
     */
    private Transaction resultsToTransaction(ResultSet results) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(results.getInt(1));
        transaction.setName(results.getString(2));
        transaction.setDescription(results.getString(3));
        transaction.setDate(results.getDate(4).toLocalDate());
        transaction.setAmount(results.getLong(5));

        return transaction;
    }

    /**
     * Get category from results
     *
     * @param results result set
     * @return category
     * @throws SQLException if sql query fails
     */
    private Category resultsToCategory(ResultSet results) throws SQLException {
        Category category = new Category();
        category.setId(results.getInt(6));
        category.setName(results.getString(7));
        category.setDescription(results.getString(8));

        return category;
    }
}