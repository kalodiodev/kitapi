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
 * Income Data Access Object
 *
 * Extends SQLiteTransactionDaoImpl
 *
 * @author Raptodimos Thanos
 */
public class SQLiteIncomeDaoImpl extends SQLiteTransactionDaoImpl{

    private static TransactionTable.TableBuilder incomeTableBuilder = new TransactionTable.TableBuilder();
    private static CategoryTable.TableBuilder incomeCategoryTableBuilder = new CategoryTable.TableBuilder();

    // Create Tables Structure Details
    static {
        incomeTableBuilder.setTableName(SqliteDatabase.TABLE_INCOME)
                .setIdColumn(SqliteDatabase.INCOME_COLUMN_ID)
                .setNameColumn(SqliteDatabase.INCOME_COLUMN_NAME)
                .setDescriptionColumn(SqliteDatabase.INCOME_COLUMN_DESCRIPTION)
                .setDateColumn(SqliteDatabase.INCOME_COLUMN_DATE)
                .setAmountColumn(SqliteDatabase.INCOME_COLUMN_AMOUNT)
                .setCategoryColumn(SqliteDatabase.INCOME_COLUMN_CATEGORY);

        incomeCategoryTableBuilder.setTableName(SqliteDatabase.TABLE_INCOME_CATEGORY)
                .setIdColumn(SqliteDatabase.INCOME_CATEGORY_COLUMN_ID)
                .setNameColumn(SqliteDatabase.INCOME_CATEGORY_COLUMN_NAME)
                .setDescriptionColumn(SqliteDatabase.INCOME_CATEGORY_COLUMN_DESCRIPTION);
    }

    /**
     * SQLite Income Dao Constructor
     */
    public SQLiteIncomeDaoImpl() {
        super(incomeTableBuilder.build(), incomeCategoryTableBuilder.build());
    }
}