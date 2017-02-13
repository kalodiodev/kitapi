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

package eu.kalodiodev.kitapi.view;

import eu.kalodiodev.kitapi.model.Category;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Class tha extends from a {@link ComboBox} representing a popup list of
 * {@link Category} items, where only their names should be shown in the list.
 *
 * @author Raptodimos Thanos
 */
public class CategoryComboBox extends ComboBox<Category> {

    /**
     * Default converter property
     *
     * @return categories simple object property
     */
    @Override
    public ObjectProperty<StringConverter<Category>> converterProperty() {
        StringConverter<Category> stringConverter = new StringConverter<Category>() {

            @Override
            public String toString(Category category) {
                return category.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        };

        return new SimpleObjectProperty<>(stringConverter);
    }
}
