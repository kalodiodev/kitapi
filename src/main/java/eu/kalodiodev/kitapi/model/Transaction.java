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

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * This Class provides Transaction model
 *
 * Amount (of money) must be in cents.
 *
 * @author Raptodimos Thanos
 */
public class Transaction {

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleLongProperty amount = new SimpleLongProperty();
    private SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Category> category = new SimpleObjectProperty<>();


    /**
     * Get Transaction's Id
     *
     * @return id
     */
    public int getId() {
        return id.get();
    }

    /**
     * Set Transaction's Id
     *
     * @param id transaction's id
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Get Transaction's Id property
     *
     * @return id property
     */
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    /**
     * Get Transaction's name
     *
     * @return name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Get transaction's name property
     *
     * @return name property
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Set Transaction's name
     *
     * @param name transaction's name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Get Transaction's description
     *
     * @return description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Get Transaction's description property
     *
     * @return description property
     */
    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    /**
     * Set Transaction's description
     *
     * @param description transaction's description
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Get Transaction's amount
     *
     * @return amount
     */
    public long getAmount() {
        return amount.get();
    }

    /**
     * Get Transaction's amount property
     *
     * <p>Value is in cents</p>
     *
     * @return amount property
     */
    public SimpleLongProperty amountProperty() {
        return amount;
    }

    /**
     * Set amount in cents
     *
     * @param amount amount
     */
    public void setAmountInCents(double amount) {
        this.amount.set(Math.round(amount * 100));
    }

    /**
     * Set Transaction's amount
     *
     * @param amount transaction's amount
     */
    public void setAmount(long amount) {
        this.amount.set(amount);
    }

    /**
     * Get Transaction's date
     *
     * @return local date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Get Transaction's date property
     *
     * @return date property
     */
    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Set Transaction's date
     *
     * @param date transaction's local date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * Get Transaction's category
     *
     * @return category @see {@link Category}
     */
    public Category getCategory() { return category.get(); }

    /**
     * Get Transaction's category property
     *
     * @return category property
     */
    public SimpleObjectProperty<Category> categoryProperty() {
        return category;
    }

    /**
     * Set Transaction's category
     *
     * @param category transaction's category @see {@link Category}
     */
    public void setCategory(Category category) { this.category.set(category); }


    @Override
    public String toString() {
        return "Transaction{" +
                "name='" + name.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", amount=" + amount.get() +
                ", date=" + date.get() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Transaction that = (Transaction) obj;

        if (id.get() != that.id.get())
            return false;

        if (amount.get() != that.amount.get())
            return false;

        if (name != null ? !name.get().equals(that.name.get()) : that.name != null)
            return false;

        if (description != null ? !description.get().equals(that.description.get()) : that.description != null)
            return false;

        if (date != null ? !date.get().equals(that.date.get()) : that.date != null)
            return false;

        return category != null ? category.get().equals(that.category.get()) : that.category == null;
    }

    @Override
    public int hashCode() {
        int result = id.get();
        result = 31 * result + (name != null ? name.get().hashCode() : 0);
        result = 31 * result + (description != null ? description.get().hashCode() : 0);
        result = 31 * result + (int) amount.get();
        result = 31 * result + (date != null ? date.get().hashCode() : 0);
        result = 31 * result + (category != null ? category.get().hashCode() : 0);
        return result;
    }
}