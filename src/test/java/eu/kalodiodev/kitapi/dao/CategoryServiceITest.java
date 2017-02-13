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

import eu.kalodiodev.kitapi.db.SqliteDatabase;
import eu.kalodiodev.kitapi.exceptions.*;
import eu.kalodiodev.kitapi.model.Category;
import eu.kalodiodev.kitapi.service.CategoryService;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Category Data Access Object Test class
 *
 * Contains common tests for every dao which implements Category Dao Interface
 *
 * @author Raptodimos Thanos
 */
abstract public class CategoryServiceITest {

    private CategoryService categoryService = new CategoryService();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Category Dao Test constructor
     *
     * @param categoryDao category dao
     */
    CategoryServiceITest(CategoryDao categoryDao) {
        this.categoryService.setDao(categoryDao);

    }

    /**
     * Setup before all - run only once
     */
    @org.junit.BeforeClass
    public static void setupOnce() {
        SqliteDatabase.create();
    }

    /**
     * Setup before each test
     */
    @org.junit.Before
    public void setup() {
        System.out.println(getClass().getName() + ": Setup");

        clearTable();
    }

    /**
     * tear down after each test
     */
    @org.junit.After
    public void tearDown() {
        clearTable();
    }


    /**
     * Add Category Test
     *
     * Test adding new category
     */
    @org.junit.Test
    public void add() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException {

        // Add Categories
        Category category1 = addCategory("Test", "Test Category");
        Category category2 = addCategory("Test 2", "Test 2 Category");

        // Get Categories
        List<Category> categories = categoryService.all();

        if(categories == null)
            throw new NullPointerException("Add Test: Categories list is null");

        // Assertions
        assertEquals("Should be two categories in Categories table.",
                2, categories.size());
        assertEquals("These two categories should be the same.",
                category1, categories.get(0));
        assertEquals("These two categories should be the same.",
                category2, categories.get(1));
    }

    /**
     * Add Category Test
     *
     * Test adding a category, with a name that already exists.
     * Name duplication is not permitted, it should throw DuplicateEntryException
     */
    @org.junit.Test
    public void addNameDuplicationException() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException {

        // Expected Exception
        thrown.expect(DuplicateEntryException.class);
        thrown.expectMessage("Category's name already exists.");

        // Add Categories
        addCategory("Test", "Test Category");
        addCategory("Test", "Test 2 Category");
    }

    /**
     * Add Category Test - Avoid empty category's name
     *
     * Test adding a category, with an empty name.
     * Empty name is not permitted, it should throw EmptyInputException
     */
    @org.junit.Test
    public void addEmptyNameException() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException {

        // Expected Exception
        thrown.expect(EmptyInputException.class);
        thrown.expectMessage("Category's name is empty.");

        // Add Categories
        addCategory("", "Test Category");
    }

    /**
     * Get Category Test
     *
     * Test getting a stored category
     */
    @org.junit.Test
    public void get() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EntryNotFoundException {

        // Add Categories
        Category category1 = addCategory("Test", "Test Category");
        addCategory("Test 2", "Test 2 Category");

        // Get Category
        Category category = categoryService.get("Test");

        // Assertions
        assertEquals("These two categories should be the same.", category1, category);
    }

    /**
     * Get Category Test - Not found entry Exception
     *
     * Test getting a category that does not exist, it should throw EntryNotFoundException
     */
    @org.junit.Test
    public void getNotFoundException() throws RequestFailException,
            EmptyInputException, EntryNotFoundException, NullInputException {

        // Expected Exception
        thrown.expect(EntryNotFoundException.class);
        thrown.expectMessage("Category not found");

        // Get Category
        categoryService.get("Test");
    }

    /**
     * Update Category
     *
     * Test updating a category
     */
    @org.junit.Test
    public void update() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EntryNotFoundException {

        // Add Categories
        Category category1 = addCategory("Test", "Test Category");

        // Update Category 1 - Must update
        categoryService.update(category1, new Category("Test name changed", "New Description"));

        List<Category> categories = categoryService.all();

        if(categories == null)
            throw new NullPointerException("Update Category: Categories list is null");

        Category updatedCategory1 = categories.get(0);

        // Assertions
        assertEquals("Category should have name updated.",
                "Test name changed", updatedCategory1.getName());

        assertEquals("Category should have description updated.",
                "New Description", updatedCategory1.getDescription());
    }

    /**
     * Update Category
     *
     * Test updating category's name with a name that already exists,
     * should throw DuplicateEntryException
     */
    @org.junit.Test
    public void updateNameDuplicationException() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException, EntryNotFoundException {

        // Expected Exception
        thrown.expect(DuplicateEntryException.class);
        thrown.expectMessage("Category's new name already exists.");

        // Add Categories
        Category category1 = addCategory("Test", "Test Category");
        addCategory("Test 2", "Test 2 Category");

        // Update Category 1 - Must throw DuplicateEntryException
        categoryService.update(category1, new Category("Test 2", "New Description"));
    }

    /**
     * Update Category
     *
     * Test updating category's name with an empty name, should throw EmptyInputException
     */
    @org.junit.Test
    public void updateEmptyNameException() throws EntryNotFoundException,
            EmptyInputException, RequestFailException, NullInputException, DuplicateEntryException {

        // Expected Exception
        thrown.expect(EmptyInputException.class);
        thrown.expectMessage("Updated category's name is empty");

        // Add Categories
        Category category1 = addCategory("Test", "Test Category");

        // Update Category 1 - Must throw DuplicateEntryException
        categoryService.update(category1, new Category("", "New Description"));
    }

    /**
     * Remove Category
     *
     * Test removing a category entry
     */
    @org.junit.Test
    public void remove() throws EntryNotFoundException,
            EmptyInputException, RequestFailException, NullInputException, DuplicateEntryException {
        // Add Categories
        Category category = addCategory("Test", "Test Category");
        Category category1 = addCategory("Test 1", "Test 1 Category");

        // Delete Category
        categoryService.remove(category);

        List<Category> categories = categoryService.all();

        if(categories == null)
            throw new NullPointerException("Categories list is null");

        //Assertions
        assertEquals("Should be one category in table.", 1, categories.size());
        assertEquals("Remained category should be equal with this.", category1, categories.get(0));
    }

    /**
     * Remove Category with name
     *
     * Test removing category by it's name
     */
    @org.junit.Test
    public void removeWithName() throws RequestFailException,
            NullInputException, DuplicateEntryException, EmptyInputException, EntryNotFoundException {

        // Add Categories
        addCategory("Test", "Test Category");
        Category category1 = addCategory("Test 1", "Test 1 Category");

        // Delete Category
        categoryService.remove("Test");

        List<Category> categories = categoryService.all();

        if(categories == null)
            throw new NullPointerException("Categories list is null");

        //Assertions
        assertEquals("Should be one category in table.", 1, categories.size());
        assertEquals("Remained category should be equal with this.", category1, categories.get(0));
    }

    /**
     * Remove all categories test
     *
     * Test removing all categories from table
     */
    @org.junit.Test
    public void removeAll() throws RequestFailException,
            NullInputException, DuplicateEntryException, EmptyInputException {

        // Add Categories
        addCategory("Test", "Test Category");
        addCategory("Test 1", "Test 1 Category");

        // Remove all entries
        categoryService.removeAll();

        List<Category> categories = categoryService.all();

        if(categories == null)
            throw new NullPointerException("Categories list is null");

        //Assertions
        assertEquals("Should be none category in table.", 0, categories.size());
    }

    /**
     * Count all categories
     *
     * Test counting all categories in table
     */
    @org.junit.Test
    public void count() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException {

        // Add Categories
        addCategory("Test", "Test Category");
        addCategory("Test 1", "Test 1 Category");

        // Get entries count
        int count = categoryService.count();

        // Assertions
        assertEquals("Should be 2 categories in table.", 2, count);
    }

    /**
     * Count Categories with name
     *
     * Test counting categories that name is equal or like the given name.
     */
    @org.junit.Test
    public void countByName() throws RequestFailException,
            EmptyInputException, NullInputException, DuplicateEntryException {

        // Add Categories
        addCategory("Test", "Test Category");
        addCategory("Test 1", "Test 1 Category");
        addCategory("Category name", "Category description");

        // Get entries count
        int countLike = categoryService.countLike("Test");
        int countEqual = categoryService.countEqual("Test");

        // Assertions
        assertEquals("Should be 2 categories in table with name (like)", 2, countLike);
        assertEquals("Should be 1 category in table with name (equal)", 1, countEqual);
    }

    /**
     * Add Category
     *
     * @param name category's name
     * @param description category's description
     * @return category added
     */
    private Category addCategory(String name, String description) throws
            RequestFailException, EmptyInputException, NullInputException, DuplicateEntryException {

        // New Category
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        //Add Category to database
        int categoryId = categoryService.add(category);

        category.setId(categoryId);

        return category;
    }

    /**
     * Clear Table entries
     */
    private void clearTable() {
        try {
            categoryService.removeAll();
        } catch (RequestFailException e) {
            System.out.println(e.getMessage());
        }
    }
}