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

import eu.kalodiodev.kitapi.exceptions.DaoEntryNotFoundException;
import eu.kalodiodev.kitapi.exceptions.DaoException;
import eu.kalodiodev.kitapi.model.Transaction;

import java.time.LocalDate;
import java.util.List;


/**
 * Transaction Data Access Object Interface
 *
 * @author Raptodimos Thanos
 */
public interface TransactionDao extends GenericDao<Transaction>{

    /**
     * Get Entry Item
     *
     * @param id item's id
     * @return entry object
     * @throws DaoException              if database query fails
     * @throws DaoEntryNotFoundException if entry with this name not found
     */
    Transaction get(int id) throws DaoException, DaoEntryNotFoundException;

    /**
     * Get latest transactions
     *
     * @param rows number of transactions rows
     * @return transactions list
     * @throws DaoException if database query failed
     */
    List<Transaction> latest(int rows) throws DaoException;

    /**
     * Get latest transactions
     *
     * @param since get entries since date
     * @return transactions list
     * @throws DaoException if database query failed
     */
    List<Transaction> latest(LocalDate since) throws DaoException;

    /**
     * Get latest transactions
     *
     * <p>Get all transactions, ordered by date in descending order</p>
     *
     * @return transactions list
     * @throws DaoException if database query failed
     */
    List<Transaction> latest() throws DaoException;

    /**
     * Get total transactions amount
     *
     * @return transactions amount summary
     * @throws DaoException if database query failed
     */
    long getTotalAmount() throws DaoException;

    /**
     * Get total transactions amount
     *
     * <p>Amounts summary of specific time period.</p>
     *
     * @param start calculate amounts summary since date
     * @param end calculate amounts summary until date
     * @return transactions amount summary
     * @throws DaoException if database query failed
     */
    long getTotalAmount(LocalDate start, LocalDate end) throws DaoException;

    /**
     * Get total transactions amount
     *
     * <p>Amounts summary since date until now.</p>
     *
     * @param since calculate amounts summary since date
     * @return transactions amount summary
     * @throws DaoException if database query failed
     */
    long getTotalAmount(LocalDate since) throws DaoException;
}
