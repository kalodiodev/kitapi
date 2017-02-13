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
import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.text.Collator;
import java.util.List;

/**
 * Category Service
 *
 * Categories operations, business logic belongs here
 *
 * @author Raptodimos Thanos
 */
public class CategoryService implements ICategoryService{

    /**
     * Category Data Access Object
     */
    private CategoryDao dao;

    /**
     * Categories Observable List
     */
    private ObservableList<Category> categories;


    /**
     * Category Service Constructor
     */
    public CategoryService() {
        categories = FXCollections.observableArrayList();
    }

    /**
     * Category Service Constructor
     *
     * @param dao Data Access Object to be used
     */
    public CategoryService(CategoryDao dao) {
        this.dao = dao;
        categories = FXCollections.observableArrayList();
    }

    /**
     * Get Categories observable list
     *
     * @return categories observable list
     */
    @Override
    public ObservableList<Category> getObservableList() {
        return categories;
    }

    /**
     * Get Categories sorted list
     *
     * @return categories sorted list
     */
    @Override
    public SortedList<Category> getSortedList() {
        return new SortedList<>(getObservableList(),
                (o1, o2) -> Collator.getInstance().compare(o1.getName(), o2.getName())).sorted();
    }

    /**
     * Get Categories Data Access Object
     *
     * @return categories dao
     */
    @Override
    public CategoryDao getDao() {
        return dao;
    }

    /**
     * Set Categories Data Access Object
     *
     * @param dao categories dao
     */
    @Override
    public void setDao(CategoryDao dao) {
        this.dao = dao;
    }

    /**
     * Get all categories
     *
     * @return categories list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Category> all() throws RequestFailException {
        try {
            // Get categories list
            List<Category> categoriesList = dao.all(GenericDao.OrderBy.NONE);
            // Set categories list to observable list
            categories.setAll(categoriesList);
            // Return categories list
            return categoriesList;
        } catch (DaoException e) {
            throw new RequestFailException("Get categories, persistence storage operation failed: " + e.getMessage());
        }
    }

    /**
     * Get all categories
     * Ordered by name in <strong>descending</strong> order
     *
     * @return categories list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Category> allDescOrder() throws RequestFailException {
        try {
            // Get categories list
            List<Category> categoriesList = dao.all(GenericDao.OrderBy.DESC);
            // Set categories list to observable list
            categories.setAll(categoriesList);
            // Return categories list
            return categoriesList;
        } catch (DaoException e) {
            throw new RequestFailException("Get categories, persistence operation failed: " + e.getMessage());
        }
    }

    /**
     * Get all categories
     * Ordered by name in <strong>ascending</strong> order
     *
     * @return categories list
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public List<Category> allAscOrder() throws RequestFailException {
        try {
            // Get categories list
            List<Category> categoriesList = dao.all(GenericDao.OrderBy.ASC);
            // Set categories list to observable list
            categories.setAll(categoriesList);
            // Return categories list
            return categoriesList;
        } catch (DaoException e) {
            throw new RequestFailException("Get categories, persistence operation failed: " + e.getMessage());
        }
    }

    /**
     * Get category
     *
     * @param name category's name
     * @return category
     * @throws NullInputException if name is null
     * @throws EmptyInputException if name is empty
     * @throws RequestFailException if persistence storage operation failed
     * @throws EntryNotFoundException if category entry not found
     */
    @Override
    public Category get(String name) throws NullInputException,
            EmptyInputException, RequestFailException, EntryNotFoundException {

        // Validation
        if(name == null)
            throw new NullInputException("Category's name is null.");
        if(name.isEmpty())
            throw new EmptyInputException("Category's name is empty.");

        // Get category
        try {
            return dao.get(name);
        } catch (DaoException e) {
            throw new RequestFailException("Get category, persistence operation failed: " + e.getMessage());
        } catch (DaoEntryNotFoundException e) {
            throw new EntryNotFoundException("Category not found");
        }
    }

    /**
     * Add category
     *
     * @param category category to add
     * @throws NullInputException if category or category's name is null
     * @throws EmptyInputException if category's name is empty
     * @throws DuplicateEntryException if category's name already exists
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public int add(Category category) throws NullInputException,
            EmptyInputException, DuplicateEntryException, RequestFailException {

        // Validation
        if((category == null) || (category.getName() == null))
            throw new NullInputException("Category or category's name is null.");

        if(category.getName().isEmpty())
            throw new EmptyInputException("Category's name is empty.");

        if(dao.count(category.getName(), GenericDao.CountCriteria.EQUAL) > 0)
            throw new DuplicateEntryException("Category's name already exists.");

        // Add Category
        try {
            int id = dao.add(category);
            // Add category to observable list
            category.setId(id);
            categories.add(category);

            return id;
        } catch (DaoException e) {
            throw new RequestFailException("Failed to execute request in database");
        }
    }

    /**
     * Update Category
     *
     * @param currentCategory category to be updated
     * @param updatedCategory updated category
     * @throws EmptyInputException if category's name is empty
     * @throws NullInputException if categories or their names are null
     * @throws DuplicateEntryException if updated category's name already exists
     * @throws EntryNotFoundException if category to be updated not found
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void update(Category currentCategory, Category updatedCategory) throws EmptyInputException,
            NullInputException, DuplicateEntryException, EntryNotFoundException, RequestFailException {

        // Validate input
        if((currentCategory == null) || (currentCategory.getName() == null))
            throw new NullInputException("Category to be updated or category's name is null.");

        if((updatedCategory == null) || (updatedCategory.getName() == null))
            throw new NullInputException("Updated category or category's name is null.");

        if(dao.count(currentCategory.getName(), GenericDao.CountCriteria.EQUAL) == 0)
            throw new EntryNotFoundException("Category to be updated, not found.");

        if(updatedCategory.getName().isEmpty())
            throw new EmptyInputException("Updated category's name is empty");

        if((!currentCategory.getName().equals(updatedCategory.getName())) &&
                (dao.count(updatedCategory.getName(), GenericDao.CountCriteria.EQUAL) > 0))
            throw new DuplicateEntryException("Category's new name already exists.");

        // Update Category
        try {
            dao.update(currentCategory, updatedCategory);

            currentCategory.setName(updatedCategory.getName());
            currentCategory.setDescription(updatedCategory.getDescription());
        } catch (DaoException e) {
            throw new RequestFailException("Failed to execute request in database");
        }
    }

    /**
     * Remove category
     *
     * @param category category to be removed
     * @throws NullInputException category or category's name is null
     * @throws EmptyInputException category's name is empty
     * @throws EntryNotFoundException category to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void remove(Category category) throws NullInputException,
            EmptyInputException, EntryNotFoundException, RequestFailException {

        // Validation
        if(category == null)
            throw new NullInputException("Category is null.");

        remove(category.getName());
        // Remove from observable list
        categories.remove(category);
    }

    /**
     * Remove category
     *
     * @param name category's name to be removed
     * @throws NullInputException category's name is null
     * @throws EmptyInputException category's name is empty
     * @throws EntryNotFoundException category to be removed not found
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void remove(String name) throws RequestFailException,
            EmptyInputException, EntryNotFoundException, NullInputException {

        // Validation
        if(name == null)
            throw new NullInputException("Category's name is null.");

        if(name.isEmpty())
            throw new EmptyInputException("Category's name is empty.");

        if(dao.count(name, GenericDao.CountCriteria.EQUAL) == 0)
            throw new EntryNotFoundException("Category to delete not found.");

        // Delete Category
        try {
            dao.remove(name);
        } catch (DaoException e) {
            throw new RequestFailException("Failed to execute request in database");
        }
    }

    /**
     * Remove all categories
     *
     * @throws RequestFailException if persistence storage operation failed
     */
    @Override
    public void removeAll() throws RequestFailException {
        try {
            dao.removeAll();
            // Remove all from observable list
            categories.removeAll();
        } catch (DaoException e) {
            throw new RequestFailException("Failed to execute request in database");
        }
    }

    /**
     * Count entries
     * Equal with the name given
     *
     * @param name entry's name
     * @return number of entries
     */
    @Override
    public int countEqual(String name) {
        return dao.count(name, GenericDao.CountCriteria.EQUAL);
    }

    /**
     * Count entries
     * With name like the given
     *
     * @param name entry's name
     * @return number of entries
     */
    @Override
    public int countLike(String name) {
        return dao.count(name, GenericDao.CountCriteria.LIKE);
    }

    /**
     * Count all entries
     *
     * @return number of entries
     */
    @Override
    public int count() {
        return dao.count();
    }
}