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

import eu.kalodiodev.kitapi.dao.TransactionDao;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.model.Transaction;

import java.time.LocalDate;

/**
 * Transaction Service Interface
 *
 * Extends @see {@link IGenericService}
 *
 * @author Raptodimos Thanos
 */
public interface ITransactionService extends IGenericService<Transaction>{

    /**
     * Set data access object
     *
     * @param dao data access object to be used
     */
    void setDao(TransactionDao dao);

    /**
     * Get transaction
     *
     * @param id transaction's id
     * @return transaction entry
     * @throws EntryNotFoundException if transaction with this id not found
     * @throws RequestFailException if persistence storage operation failed
     * @throws InvalidIdException if transaction's id is invalid (negative)
     */
    Transaction get(int id) throws EntryNotFoundException, RequestFailException, InvalidIdException;

    /**
     * Add transaction
     *
     * @param transaction transaction to add
     * @return transaction's id
     * @throws NullInputException if transaction is null
     * @throws EmptyInputException if transaction's name is empty
     * @throws RequestFailException if persistence storage operation failed
     */
    int add(Transaction transaction) throws NullInputException, EmptyInputException, RequestFailException, EmptyDateException;

    /**
     * Update transaction
     *
     * @param currentTransaction transaction to be updated
     * @param updatedTransaction transaction to update
     * @throws NullInputException if transaction is null
     * @throws EntryNotFoundException if transaction to be updated not found
     * @throws RequestFailException if persistence storage operation failed
     * @throws EmptyInputException if transaction's name is empty
     */
    void update(Transaction currentTransaction,Transaction updatedTransaction) throws NullInputException,
            EntryNotFoundException, RequestFailException, EmptyInputException, EmptyDateException;

    /**
     * Remove transaction
     *
     * @param transaction transaction to remove
     * @throws NullInputException if transaction is null
     * @throws EntryNotFoundException if transaction to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    void remove(Transaction transaction) throws NullInputException, EntryNotFoundException, RequestFailException;

    /**
     * Check if transaction exists
     *
     * @param id transaction's id
     * @return true if exists, otherwise false
     * @throws RequestFailException if persistence storage operation failed
     */
    boolean exists(int id) throws RequestFailException;

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of all entries</p>
     * <p>Calculates amount summary of all entries</p>
     *
     * @return total amount
     */
    double listTotalAmount();

    /**
     * Filter list and get total amount
     *
     * <p>Filtered list of given category.</p>
     * <p>Calculates amount summary of given category's transaction entries.</p>
     *
     * @param category transactions of this category
     * @return total amount
     */
    double listTotalAmount(Category category);

    /**
     * Filter list and get total amount
     *
     * <p>Filtering Filtered list of given time period.</p>
     * <p>Calculates transaction's amount summary of given time period.</p>
     *
     * @param start period start date
     * @param end period end date
     * @return total amount
     */
    double listTotalAmount(LocalDate start, LocalDate end) throws EmptyDateException;

    /**
     * Filter list ang get total amount
     *
     * <p>Filtering Filtered list of given category, start date and end date.</p>
     * <p>Calculates amount summary of given category and time period transaction entries.</p>
     *
     * @param category transactions of this category
     * @param start period start date
     * @param end period end date
     * @return total amount
     */
    double listTotalAmount(Category category, LocalDate start, LocalDate end) throws EmptyDateException;

    /**
     * Calculate total amount
     *
     * @return total amount
     */
    double calculateTotal();

    /**
     * Calculate total amount
     *
     * @param since since date
     * @param until until date
     * @return total amount
     */
    double calculateTotal(LocalDate since, LocalDate until);
}