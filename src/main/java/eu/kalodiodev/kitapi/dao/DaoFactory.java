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


/**
 * Dao Factory Abstract Class
 *
 * Any DAO factory class must extend from this class
 * Dao Factory gives the possibility to switch between or work with multiple databases
 */
public abstract class DaoFactory {

    public static final int SQLITE = 0;

    /**
     * Get Income Category Dao
     * Abstract method that any Dao Factory Class must extend to provide tha appropriate database
     *
     * @return Income category Data Access Object
     */
    public abstract CategoryDao getIncomeCategoryDao();

    /**
     * Get Expenses Category Dao
     * Abstract method that any Dao Factory Class must extend to provide tha appropriate database
     *
     * @return Expenses category Data Access Object
     */
    public abstract CategoryDao getExpensesCategoryDao();

    /**
     * Get Expenses Dao
     * Abstract method that any Dao Factory Class must extend to provide tha appropriate database
     *
     * @return Expenses Data Access Object
     */
    public abstract TransactionDao getExpensesDao();

    /**
     * Get Income Dao
     * Abstract method that any Dao Factory Class must extend to provide tha appropriate database
     *
     * @return Income Data Access Object
     */
    public abstract TransactionDao getIncomeDao();

    /**
     * Get Factory that extends from this class
     * To work with the desired database
     *
     * @param type database type
     * @return database's Dao Factory
     */
    public static DaoFactory getFactory(int type) {
        switch (type) {
            case SQLITE:
                return new SQLiteFactory();
            default:
                return new SQLiteFactory();
        }
    }
}
