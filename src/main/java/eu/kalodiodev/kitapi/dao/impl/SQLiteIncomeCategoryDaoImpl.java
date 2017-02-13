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

package eu.kalodiodev.kitapi.dao.impl;

import eu.kalodiodev.kitapi.dao.GenericDao;
import eu.kalodiodev.kitapi.db.SqliteDatabase;
import eu.kalodiodev.kitapi.model.Category;

/**
 * Income Category Data Access Object
 *
 * Implements Generic Data Access Object
 *
 * @author Raptodimos Thanos
 */
public class SQLiteIncomeCategoryDaoImpl extends SQLiteCategoryDaoImpl implements GenericDao<Category> {

    /**
     * SQLite Income Category Data Access Object Constructor
     */
    public SQLiteIncomeCategoryDaoImpl() {
        super(SqliteDatabase.TABLE_INCOME_CATEGORY,
                SqliteDatabase.INCOME_CATEGORY_COLUMN_ID,
                SqliteDatabase.INCOME_CATEGORY_COLUMN_NAME,
                SqliteDatabase.INCOME_CATEGORY_COLUMN_DESCRIPTION);
    }
}