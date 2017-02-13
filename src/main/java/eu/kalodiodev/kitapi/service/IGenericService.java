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

import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.exceptions.RequestFailException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.List;

/**
 * Generic Service Interface
 *
 * @author Raptodimos Thanos
 */
public interface IGenericService<T> {

    /**
     * Get Data Access Object used
     *
     * @return data access object
     */
    GenericDao<T> getDao();

    /**
     * Get entries observable list
     *
     * @return entries observable list
     */
    ObservableList<T> getObservableList();

    /**
     * Get entries sorted list
     *
     * @return entries sorted list
     */
    SortedList<T> getSortedList();

    /**
     * Get all entries
     *
     * @return list of entries
     * @throws RequestFailException if persistence storage operation failed
     */
    List<T> all() throws RequestFailException;

    /**
     * Get all entries
     * Ordered by name in <strong>Descending</strong> order
     *
     * @return list of entries
     * @throws RequestFailException if persistence storage operation failed
     */
    List<T> allDescOrder() throws RequestFailException;

    /**
     * Get all entries
     * Ordered by name in <strong>Ascending</strong> order
     *
     * @return list of entries
     * @throws RequestFailException if persistence storage operation failed
     */
    List<T> allAscOrder() throws RequestFailException;

    /**
     * Remove all entries from table
     *
     * @throws RequestFailException if persistence storage operation failed
     */
    void removeAll() throws RequestFailException;

    /**
     * Count entries
     * Where name <b>Equals</b> the given name
     *
     * @param name entry's name
     * @return number of entries
     */
    int countEqual(String name);

    /**
     * Count entries
     * Where name <b>Like</b> the given name
     *
     * @param name entry's name
     * @return number of entries
     */
    int countLike(String name);

    /**
     * Count all entries
     *
     * @return number of entries
     */
    int count();
}