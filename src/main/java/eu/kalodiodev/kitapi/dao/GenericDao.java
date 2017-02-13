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

import eu.kalodiodev.kitapi.exceptions.DaoException;

import java.util.List;

/**
 * Generic Data Access Object Contract
 *
 * This Interface must be implemented by all DAOs
 *
 * @param <T> the type of the Data Access Object
 *
 * @author Raptodimos Thanos
 */
public interface GenericDao<T> {

    enum OrderBy {
        NONE,
        ASC,
        DESC
    }

    enum CountCriteria {
        EQUAL,
        LIKE
    }

    /**
     * Get all Entries
     *
     * @return list of entries
     * @throws DaoException if database query fails
     */
    List<T> all(OrderBy order) throws DaoException;

    /**
     * Count Entries
     *
     * @return number of entries
     */
    int count();

    /**
     * Count Entries
     *
     * @param name entry's name
     * @param countCriteria count by name criteria @see {@link CountCriteria}
     * @return number of entries
     */
    int count(String name, CountCriteria countCriteria);

    /**
     * Check if entry exists in database
     *
     * @param id entry's id
     * @return true if exists, false if not
     */
    boolean exists(int id) throws DaoException;

    /**
     * Add new entry item
     *
     * @param item item's name
     * @return new item id
     * @throws DaoException Data Access Object query fail
     */
    int add(T item) throws DaoException;

    /**
     * Update Entry Item
     *
     * @param item item to update
     * @param updatedItem update to this item
     * @throws DaoException if Dao update item database query failed
     */
    void update(T item, T updatedItem) throws DaoException;

    /**
     * Remove Entry Item
     *
     * @param item item to delete
     * @throws DaoException if delete item database query failed
     */
    void remove(T item) throws DaoException;

    /**
     * Remove all entries
     *
     * @throws DaoException if delete all entries database query failed
     */
    void removeAll() throws DaoException;
}
