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

package eu.kalodiodev.kitapi.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sqlite Database Creation or upgrade
 *
 * @author Raptodimos Thanos
 */
public class SqliteDatabase {

    //Database
    private static final int VERSION = 1;
    //private static final String DATABASE_NAME = "kitapi.db";

    //Expenses Category
    public static final String TABLE_EXPENSES_CATEGORY = "expenses_category";
    public static final String EXPENSES_CATEGORY_COLUMN_ID = "_id";
    public static final String EXPENSES_CATEGORY_COLUMN_NAME = "name";
    public static final String EXPENSES_CATEGORY_COLUMN_DESCRIPTION = "description";
    public static final String CREATE_EXPENSES_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_EXPENSES_CATEGORY + "(" +
            EXPENSES_CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EXPENSES_CATEGORY_COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
            EXPENSES_CATEGORY_COLUMN_DESCRIPTION + " TEXT)";

    //Income Category
    public static final String TABLE_INCOME_CATEGORY = "income_category";
    public static final String INCOME_CATEGORY_COLUMN_ID = "_id";
    public static final String INCOME_CATEGORY_COLUMN_NAME = "name";
    public static final String INCOME_CATEGORY_COLUMN_DESCRIPTION = "description";
    public static final String CREATE_INCOME_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INCOME_CATEGORY + "(" +
            INCOME_CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INCOME_CATEGORY_COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
            INCOME_CATEGORY_COLUMN_DESCRIPTION + " TEXT)";


    //Expenses
    public static final String TABLE_EXPENSES = "expenses";
    public static final String EXPENSES_COLUMN_ID = "_id";
    public static final String EXPENSES_COLUMN_NAME = "name";
    public static final String EXPENSES_COLUMN_DESCRIPTION = "description";
    public static final String EXPENSES_COLUMN_AMOUNT = "amount";
    public static final String EXPENSES_COLUMN_DATE = "date";
    public static final String EXPENSES_COLUMN_CATEGORY = "category";
    public static final String CREATE_EXPENSES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + "(" +
            EXPENSES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EXPENSES_COLUMN_NAME + " TEXT NOT NULL, " +
            EXPENSES_COLUMN_DESCRIPTION + " TEXT, " +
            EXPENSES_COLUMN_AMOUNT + " INTEGER, " +
            EXPENSES_COLUMN_DATE + " DATE, " +
            EXPENSES_COLUMN_CATEGORY + " INTEGER, " +
            " FOREIGN KEY(" + EXPENSES_COLUMN_CATEGORY +") REFERENCES " +
            TABLE_EXPENSES_CATEGORY + "(" + EXPENSES_CATEGORY_COLUMN_ID + ") ON DELETE CASCADE)";

    //Income
    public static final String TABLE_INCOME = "income";
    public static final String INCOME_COLUMN_ID = "_id";
    public static final String INCOME_COLUMN_NAME = "name";
    public static final String INCOME_COLUMN_DESCRIPTION = "description";
    public static final String INCOME_COLUMN_AMOUNT = "amount";
    public static final String INCOME_COLUMN_DATE = "date";
    public static final String INCOME_COLUMN_CATEGORY = "category";
    public static final String CREATE_INCOME_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INCOME + "(" +
            INCOME_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INCOME_COLUMN_NAME + " TEXT NOT NULL, " +
            INCOME_COLUMN_DESCRIPTION + " TEXT, " +
            INCOME_COLUMN_AMOUNT + " INTEGER, " +
            INCOME_COLUMN_DATE + " DATE, " +
            INCOME_COLUMN_CATEGORY + " INTEGER, " +
            " FOREIGN KEY(" + INCOME_COLUMN_CATEGORY +") REFERENCES " +
            TABLE_INCOME_CATEGORY + "(" + INCOME_CATEGORY_COLUMN_ID + ") ON DELETE CASCADE)";


    private static JdbcSqliteConnection connection = new JdbcSqliteConnection();


    /**
     * Create database tables
     *
     * @return true if tables creation successful, false on fail
     */
    public static boolean create() {

        try (Connection conn = connection.connect()) {

            try (Statement statement = conn.createStatement()) {
                conn.setAutoCommit(false);
                statement.execute(CREATE_EXPENSES_CATEGORY_TABLE);
                statement.execute(CREATE_INCOME_CATEGORY_TABLE);
                statement.execute(CREATE_EXPENSES_TABLE);
                statement.execute(CREATE_INCOME_TABLE);

                conn.commit();

                return true;
            } catch (Exception e){
                System.out.println("Table creation failed: " + e.getMessage());
                try {
                    System.out.println("Performing rollback");
                    conn.rollback();
                    return false;
                } catch (SQLException e2) {
                    System.out.println("Rolling back failed: " + e2.getMessage());
                    return false;
                }
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Couldn't reset auto-commit! " + e.getMessage());
                }
            }
        }catch (SQLException e) {
            // Connection failed
            System.out.println("Creating tables query failed: " + e.getMessage());
            return false;
        }
    }
}
