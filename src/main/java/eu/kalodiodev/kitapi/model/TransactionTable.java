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
 * Transaction table structure model
 *
 * @author Raptodimos Thanos
 */
public class TransactionTable {

    private String tableName;
    private String idColumn;
    private String nameColumn;
    private String descriptionColumn;
    private String dateColumn;
    private String amountColumn;
    private String categoryColumn;

    /**
     * Transaction table constructor
     *
     * @param builder table builder
     */
    private TransactionTable(TableBuilder builder) {
        this.tableName = builder.tableName;
        this.idColumn = builder.idColumn;
        this.nameColumn = builder.nameColumn;
        this.descriptionColumn = builder.descriptionColumn;
        this.dateColumn = builder.dateColumn;
        this.amountColumn = builder.amountColumn;
        this.categoryColumn = builder.categoryColumn;
    }

    /**
     * Get table name
     *
     * @return table's name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Get id column name
     *
     * @return id column name
     */
    public String getIdColumn() {
        return idColumn;
    }

    /**
     * Get name's column name
     *
     * @return name's column name
     */
    public String getNameColumn() {
        return nameColumn;
    }

    /**
     * Get description's column name
     *
     * @return description's column name
     */
    public String getDescriptionColumn() {
        return descriptionColumn;
    }

    /**
     * Get date's column name
     *
     * @return date's column name
     */
    public String getDateColumn() {
        return dateColumn;
    }

    /**
     * Get amount's column name
     *
     * @return amount's column name
     */
    public String getAmountColumn() {
        return amountColumn;
    }

    /**
     * Get category's column name
     *
     * @return category's column name
     */
    public String getCategoryColumn() {
        return categoryColumn;
    }

    /**
     * Table Builder Class
     */
    public static class TableBuilder {

        private String tableName;
        private String idColumn;
        private String nameColumn;
        private String descriptionColumn;
        private String dateColumn;
        private String amountColumn;
        private String categoryColumn;

        /**
         * Set table's name
         *
         * @param tableName table's name
         * @return table builder
         */
        public TableBuilder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Set id's column name
         *
         * @param idColumn id's column name
         * @return table builder
         */
        public TableBuilder setIdColumn(String idColumn) {
            this.idColumn = idColumn;
            return this;
        }

        /**
         * Set name's column name
         *
         * @param nameColumn name\s column name
         * @return table builder
         */
        public TableBuilder setNameColumn(String nameColumn) {
            this.nameColumn = nameColumn;
            return this;
        }

        /**
         * Set description's column name
         *
         * @param descriptionColumn description's column name
         * @return table builder
         */
        public TableBuilder setDescriptionColumn(String descriptionColumn) {
            this.descriptionColumn = descriptionColumn;
            return this;
        }

        /**
         * Set date's column name
         *
         * @param dateColumn date's column name
         * @return table builder
         */
        public TableBuilder setDateColumn(String dateColumn) {
            this.dateColumn = dateColumn;
            return this;
        }

        /**
         * Set amount's column name
         *
         * @param amountColumn amount's column name
         * @return table builder
         */
        public TableBuilder setAmountColumn(String amountColumn) {
            this.amountColumn = amountColumn;
            return this;
        }

        /**
         * Set category's column name
         *
         * @param categoryColumn category's column name
         * @return table builder
         */
        public TableBuilder setCategoryColumn(String categoryColumn) {
            this.categoryColumn = categoryColumn;
            return this;
        }

        /**
         * Table build
         *
         * @return transaction table
         */
        public TransactionTable build() {
            return new TransactionTable(this);
        }
    }
}
