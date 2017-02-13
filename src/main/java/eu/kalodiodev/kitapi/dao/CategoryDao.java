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
import eu.kalodiodev.kitapi.model.Category;


/**
 * Category Data Access Object Interface
 *
 * @author Raptodimos Thanos
 */
public interface CategoryDao extends GenericDao<Category> {

    /**
     * Get Entry Item
     *
     * @param name item's name
     * @return entry object
     * @throws DaoException              if database query fails
     * @throws DaoEntryNotFoundException if entry with this name not found
     */
    Category get(String name) throws DaoException, DaoEntryNotFoundException;

    /**
     * Remove Entry Item
     *
     * @param name item's name
     * @throws DaoException if Dao remove item database query failed
     */
    void remove(String name) throws DaoException;
}
