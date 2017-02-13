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


import eu.kalodiodev.kitapi.dao.impl.SQLiteExpenseDaoImpl;
import eu.kalodiodev.kitapi.dao.impl.SQLiteExpensesCategoryDaoImpl;
import eu.kalodiodev.kitapi.dao.impl.SQLiteIncomeCategoryDaoImpl;
import eu.kalodiodev.kitapi.dao.impl.SQLiteIncomeDaoImpl;

/**
 * SQLite DAO Factory
 *
 * Provides appropriate SQLite Data Access Objects
 *
 * @author Raptodimos Thanos
 */
public class SQLiteFactory extends DaoFactory {

    /**
     * SQLite Income Categories Data Access Object
     *
     * @return categories dao
     */
    @Override
    public CategoryDao getIncomeCategoryDao() {
        return new SQLiteIncomeCategoryDaoImpl();
    }

    /**
     * SQLite Expenses Categories Data Access Object
     *
     * @return categories dao
     */
    @Override
    public CategoryDao getExpensesCategoryDao() {
        return new SQLiteExpensesCategoryDaoImpl();
    }

    /**
     * SQLite Expenses Data Access Object
     *
     * @return expenses dao
     */
    @Override
    public TransactionDao getExpensesDao() {
        return new SQLiteExpenseDaoImpl();
    }

    /**
     * Sqlite Income Data Access Object
     *
     * @return income dao
     */
    @Override
    public TransactionDao getIncomeDao() {
        return new SQLiteIncomeDaoImpl();
    }
}
