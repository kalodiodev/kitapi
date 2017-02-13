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

import eu.kalodiodev.kitapi.dao.CategoryDao;
import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.db.JdbcSqliteConnection;
import eu.kalodiodev.kitapi.exceptions.DaoEntryNotFoundException;
import eu.kalodiodev.kitapi.exceptions.DaoException;
import eu.kalodiodev.kitapi.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Abstract Class of SQLite Category DAO
 *
 * Implements Generic DAO Interface
 *
 * @author Raptodimos Thanos
 */
abstract class SQLiteCategoryDaoImpl implements CategoryDao {

    private final String table;
    private final String columnId;
    private final String columnName;
    private final String columnDescription;

    /**
     * SQLite Database Connection
     */
    private JdbcSqliteConnection connection = new JdbcSqliteConnection();

    /**
     * SQLite Category Data Access Object Constructor
     *
     * @param table categories table
     * @param columnId name of column with ids
     * @param columnName name of column with names
     * @param columnDescription name of column with descriptions
     */
    SQLiteCategoryDaoImpl(String table, String columnId, String columnName, String columnDescription) {
        this.table = table;
        this.columnId = columnId;
        this.columnName = columnName;
        this.columnDescription = columnDescription;
    }


    /**
     * Get all Categories
     *
     * @param orderBy order by for name column @see {@link GenericDao.OrderBy}
     * @return categories list
     * @throws DaoException if get all entries database query failed
     */
    @Override
    public List<Category> all(OrderBy orderBy) throws DaoException{

        // Database query
        String query = "SELECT " +
                columnId + ", " +
                columnName + ", " +
                columnDescription + " FROM " +
                table;

        List<Category> categories = new ArrayList<>();

        //Order By Query
        StringBuilder sb = new StringBuilder(query);
        if(!orderBy.equals(OrderBy.NONE)) {
            sb.append(" ORDER BY ");
            sb.append(columnName);
            sb.append(" COLLATE NOCASE ");
            if(orderBy.equals(OrderBy.ASC)) {
                sb.append("ASC");
            } else {
                sb.append("DESC");
            }
        }

        synchronized (this) {
            try (Connection conn = connection.connect();
                 Statement statement = conn.createStatement();
                 ResultSet results = statement.executeQuery(sb.toString())) {

                while (results.next()) {
                    // Get category
                    Category category = new Category();
                    category.setId(results.getInt(columnId));
                    category.setName(results.getString(columnName));
                    category.setDescription(results.getString(columnDescription));

                    // Add category to list
                    categories.add(category);
                }
                return categories;

            } catch (SQLException e) {
                throw new DaoException("Could not load categories: " + e.getMessage());
            }
        }
    }

    /**
     * Count Categories Entries
     *
     * @return number of entries, returns -1 on fail
     */
    @Override
    public int count() {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + table;

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
     * Count Categories Entries
     *
     * @param name category's name
     * @param criteria count by name criteria @see {@link CountCriteria}
     * @return number of entries, returns -1 on fail
     */
    @Override
    public int count(String name, CountCriteria criteria) {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + table + " WHERE " + columnName;

        if(criteria.equals(CountCriteria.EQUAL)) {
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
     * Check if category entry exists in database
     *
     * @param id category's id
     * @return true if exists, false if not
     */
    @Override
    public boolean exists(int id) throws DaoException {
        // Database query
        String query = "SELECT COUNT(*) AS count FROM " + table +
                " WHERE " + columnId + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement queryCategory = conn.prepareStatement(query)) {

                queryCategory.setInt(1, id);

                // Execute query
                try (ResultSet results = queryCategory.executeQuery()) {
                    return results.getInt("count") > 0;
                }
            } catch (SQLException e) {
                throw new DaoException("Count categories query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Get Category with Name
     *
     * @param name category's name
     * @return category
     * @throws DaoException if get category database query failed
     * @throws DaoEntryNotFoundException if category with this name not found
     */
    @Override
    public Category get(String name) throws DaoException, DaoEntryNotFoundException {
        // Database query
        String query = "SELECT " + columnId + ", " + columnName + ", " + columnDescription +
                " FROM " + table + " WHERE " + columnName + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement queryCategory = conn.prepareStatement(query)) {

                queryCategory.setString(1, name);

                // Execute query
                try (ResultSet results = queryCategory.executeQuery()) {
                    if (results.next()) {
                        // Get category
                        Category category = new Category();
                        category.setId(results.getInt(columnId));
                        category.setName(results.getString(columnName));
                        category.setDescription(results.getString(columnDescription));

                        return category;
                    } else {
                        throw new DaoEntryNotFoundException("Category entry not found!");
                    }
                }
            } catch (SQLException e) {
                throw new DaoException("Failed to get category: " + e.getMessage());
            }
        }
    }

    /**
     * Add new Category
     *
     * @param category category to insert
     * @return new category's id
     * @throws DaoException if insert category database query fail
     */
    @Override
    public int add(Category category) throws DaoException {

        // Database query
        String query = "INSERT INTO " + table + "("
                + columnName + ", " + columnDescription + ") VALUES(?, ?)";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement insertIntoCategories = conn.prepareStatement(query)) {

                // Set data to query
                insertIntoCategories.setString(1, category.getName());
                insertIntoCategories.setString(2, category.getDescription());

                // Execute query and get affected rows
                int affectedRows = insertIntoCategories.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("The category insert failed!");
                }
                //Get Generated Key
                return SQLiteHelper.getGeneratedKey(insertIntoCategories.getGeneratedKeys(), 1);

            } catch (Exception e) {
                throw new DaoException("Insert new Category query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Update Category
     *
     * @param category       category to update
     * @param updatedCategory update to this category
     * @throws DaoException if update category database query failed
     */
    @Override
    public void update(Category category, Category updatedCategory) throws DaoException {

        // Database query
        String query = "UPDATE " + table + " SET " + columnName + " = ? ," + columnDescription + " = ? " +
                "WHERE " + columnName + " = ?";

        String oldName = category.getName();
        String newName = updatedCategory.getName();
        String newDescription = updatedCategory.getDescription();

        synchronized (this) {
            try (Connection conn = connection.connect()) {

                try (PreparedStatement updateCategory = conn.prepareStatement(query)) {

                    conn.setAutoCommit(false);

                    // Set data to query
                    updateCategory.setString(1, newName);
                    updateCategory.setString(2, newDescription);
                    updateCategory.setString(3, oldName);

                    // Execute query and get affected rows
                    int affectedRows = updateCategory.executeUpdate();

                    if (affectedRows == 1) {
                        conn.commit();
                    } else {
                        throw new SQLException("The category update failed!");
                    }
                } catch (Exception e) {
                    // Statement failed, Rollback
                    try {
                        conn.rollback();
                    } catch (SQLException e2) {
                        // RollBack failed
                        throw new DaoException("Update category failed, rolling back failed: " + e2.getMessage());
                    }
                    // Throw DaoException, statement execution failed
                    throw new DaoException("Update category query failed!");
                } finally {
                    // Reset auto-commit to default
                    try {
                        conn.setAutoCommit(true);
                    } catch (SQLException e) {
                        // Failed to reset auto-commit
                        System.out.println("Update category, couldn't reset auto-commit: " + e.getMessage());
                    }
                }
            } catch (SQLException e) {
                // Connection failed
                throw new DaoException("Update category query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Remove Category with name
     *
     * @param name category name
     * @throws DaoException if delete category database query failed
     */
    @Override
    public void remove(String name) throws DaoException {
        // Database query
        String query = "DELETE FROM " + table + " WHERE " + columnName + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement deleteCategory = conn.prepareStatement(query)) {

                deleteCategory.setString(1, name);
                deleteCategory.executeUpdate();

            } catch (SQLException e) {
                throw new DaoException("Delete category query failed: " + e.getMessage());
            }
        }
    }

    /**
     * Remove Category
     *
     * @param category category to delete
     * @throws DaoException if delete category database query failed
     */
    @Override
    public void remove(Category category) throws DaoException {
        // Database query
        String query = "DELETE FROM " + table + " WHERE " + columnId + " = ?";

        synchronized (this) {
            try (Connection conn = connection.connect();
                 PreparedStatement deleteCategory = conn.prepareStatement(query)) {

                deleteCategory.setInt(1, category.getId());
                deleteCategory.executeUpdate();

            } catch (SQLException e) {
                throw new DaoException("Delete category query failed");
            }
        }
    }

    /**
     * Remove all categories
     *
     * @throws DaoException if delete all categories database query failed
     */
    @Override
    public void removeAll() throws DaoException {
        // Database query
        String query = "DELETE FROM " + table;

        synchronized (this) {
            try (Connection conn = connection.connect();
                 Statement deleteCategory = conn.createStatement()) {

                deleteCategory.executeUpdate(query);

            } catch (SQLException e) {
                throw new DaoException("Delete all categories failed " + e.getMessage());
            }
        }
    }
}