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

import eu.kalodiodev.kitapi.dao.CategoryDao;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;

/**
 * Category Service Interface
 *
 * Extends @see {@link IGenericService}
 *
 * @author Raptodimos Thanos
 */
public interface ICategoryService extends IGenericService<Category> {

    /**
     * Set data access object
     *
     * @param dao data access object to be used
     */
    void setDao(CategoryDao dao);

    /**
     * Get category
     *
     * @param name category's name
     * @return category
     * @throws NullInputException if category's name given is null
     * @throws EmptyInputException if category's name given is empty
     * @throws RequestFailException if persistence storage operation failed
     * @throws EntryNotFoundException if category not found
     */
    Category get(String name) throws NullInputException,
            EmptyInputException, RequestFailException, EntryNotFoundException;

    /**
     * Add category
     *
     * @param category category to add
     * @return category's id
     * @throws NullInputException if category is null
     * @throws EmptyInputException if category's name is null
     * @throws DuplicateEntryException if category with this name already exists
     * @throws RequestFailException if persistence storage operation failed
     */
    int add(Category category) throws NullInputException,
            EmptyInputException, DuplicateEntryException, RequestFailException;

    /**
     * Update category
     *
     * @param currentCategory category to be updated
     * @param updatedCategory updated category
     * @throws EmptyInputException if category's name is empty
     * @throws NullInputException if category is null
     * @throws DuplicateEntryException if updated category's name already exists
     * @throws EntryNotFoundException if category to be updated not found
     * @throws RequestFailException if persistence storage operation failed
     */
    void update(Category currentCategory, Category updatedCategory) throws EmptyInputException,
            NullInputException, DuplicateEntryException, EntryNotFoundException, RequestFailException;

    /**
     * Remove category
     *
     * @param category category to be removed
     * @throws NullInputException if category is null
     * @throws EmptyInputException if category's name is empty
     * @throws EntryNotFoundException if category to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    void remove(Category category) throws NullInputException,
            EmptyInputException, EntryNotFoundException, RequestFailException;

    /**
     * Remove category
     *
     * @param name name of category to be removed
     * @throws NullInputException if category is null
     * @throws EmptyInputException if category's name is empty
     * @throws EntryNotFoundException if category to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    void remove(String name) throws EmptyInputException,
            EntryNotFoundException, RequestFailException, NullInputException;
}
