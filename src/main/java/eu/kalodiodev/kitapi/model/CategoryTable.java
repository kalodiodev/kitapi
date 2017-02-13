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

package eu.kalodiodev.kitapi.model;

/**
 * Category Table Structure Model
 *
 * @author Raptodimos Thanos
 */
public class CategoryTable {

    private String tableName;
    private String idColumn;
    private String nameColumn;
    private String descriptionColumn;

    /**
     * Category Table Constructor
     *
     * @param builder table builder
     */
    private CategoryTable(TableBuilder builder) {
        this.tableName = builder.tableName;
        this.idColumn = builder.idColumn;
        this.nameColumn = builder.nameColumn;
        this.descriptionColumn = builder.descriptionColumn;
    }

    /**
     * Get table name
     *
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Get ID column
     *
     * @return id column name
     */
    public String getIdColumn() {
        return idColumn;
    }

    /**
     * Get name column
     *
     * @return name column
     */
    public String getNameColumn() {
        return nameColumn;
    }

    /**
     * Get description column
     *
     * @return description column name
     */
    public String getDescriptionColumn() {
        return descriptionColumn;
    }


    /**
     * Table builder class
     */
    public static class TableBuilder {

        private String tableName;
        private String idColumn;
        private String nameColumn;
        private String descriptionColumn;

        /**
         * Set table name
         *
         * @param tableName table's name
         * @return table builder
         */
        public TableBuilder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Set Id column name
         *
         * @param idColumn id column name
         * @return table builder
         */
        public TableBuilder setIdColumn(String idColumn) {
            this.idColumn = idColumn;
            return this;
        }

        /**
         * Set name column name
         *
         * @param nameColumn name column name
         * @return table builder
         */
        public TableBuilder setNameColumn(String nameColumn) {
            this.nameColumn = nameColumn;
            return this;
        }

        /**
         * Set description column name
         *
         * @param descriptionColumn description column name
         * @return table builder
         */
        public TableBuilder setDescriptionColumn(String descriptionColumn) {
            this.descriptionColumn = descriptionColumn;
            return this;
        }

        /**
         * Build table
         *
         * @return category table
         */
        public CategoryTable build() {
            return new CategoryTable(this);
        }
    }
}
