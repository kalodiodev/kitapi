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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This Class provides Category Model
 *
 * @author Raptodimos Thanos
 */
public class Category {

    private SimpleIntegerProperty id = new SimpleIntegerProperty(-1);
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();

    /**
     * Category Constructor
     */
    public Category() {
        this.id.set(-1);
    }

    /**
     * Category Constructor
     *
     * @param id category's id
     * @param name category's name
     * @param description category's description
     */
    public Category(int id, String name, String description) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
    }

    /**
     * Category Constructor
     *
     * @param name category's name
     * @param description category's description
     */
    public Category(String name, String description) {
        this.name.set(name);
        this.description.set(description);
        this.id.set(-1);
    }

    /**
     * Get Category's id
     *
     * @return category's id
     */
    public int getId() {
        return id.get();
    }

    /**
     * Get Category's id property
     *
     * @return id property
     */
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    /**
     * Set Category's id
     *
     * @param id category's id
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Get Category's name
     *
     * @return category's name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Get Category's name property
     *
     * @return name property
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Set Category's name
     *
     * @param name category's name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Get Category's description
     *
     * @return category's description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Get Category's description property
     *
     * @return description property
     */
    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    /**
     * Set Category's description
     *
     * @param description category's description
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(getClass() != obj.getClass())
            return false;

        Category other = (Category) obj;

        if(name.get() == null) {
            if(other.name.get() != null) {
                return false;
            }
        } else if (!name.get().equals(other.name.get())) {
            return false;
        }

        if(description.get() == null) {
            if(other.description.get() != null) {
                return false;
            }
        } else if(!description.get().equals(other.description.get())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name.get() + '\'' +
                ", description='" + description.get() + '\'' +
                '}';
    }
}