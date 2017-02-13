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

import eu.kalodiodev.kitapi.db.SqliteDatabase;
import eu.kalodiodev.kitapi.model.CategoryTable;
import eu.kalodiodev.kitapi.model.TransactionTable;

/**
 * Expenses Data Access Object
 *
 * Extends SQLiteTransactionDaoImpl
 *
 * @author Raptodimos Thanos
 */
public class SQLiteExpenseDaoImpl extends SQLiteTransactionDaoImpl{

    private static TransactionTable.TableBuilder expensesTableBuilder = new TransactionTable.TableBuilder();
    private static CategoryTable.TableBuilder expensesCategoryTableBuilder = new CategoryTable.TableBuilder();

    // Create Tables Structure Details
    static {
        expensesTableBuilder.setTableName(SqliteDatabase.TABLE_EXPENSES)
                .setIdColumn(SqliteDatabase.EXPENSES_COLUMN_ID)
                .setNameColumn(SqliteDatabase.EXPENSES_COLUMN_NAME)
                .setDescriptionColumn(SqliteDatabase.EXPENSES_COLUMN_DESCRIPTION)
                .setDateColumn(SqliteDatabase.EXPENSES_COLUMN_DATE)
                .setAmountColumn(SqliteDatabase.EXPENSES_COLUMN_AMOUNT)
                .setCategoryColumn(SqliteDatabase.EXPENSES_COLUMN_CATEGORY);

        expensesCategoryTableBuilder.setTableName(SqliteDatabase.TABLE_EXPENSES_CATEGORY)
                .setIdColumn(SqliteDatabase.EXPENSES_CATEGORY_COLUMN_ID)
                .setNameColumn(SqliteDatabase.EXPENSES_CATEGORY_COLUMN_NAME)
                .setDescriptionColumn(SqliteDatabase.EXPENSES_CATEGORY_COLUMN_DESCRIPTION);
    }

    /**
     * SQLite Expenses Dao Constructor
     */
    public SQLiteExpenseDaoImpl() {
        super(expensesTableBuilder.build(), expensesCategoryTableBuilder.build());
    }
}