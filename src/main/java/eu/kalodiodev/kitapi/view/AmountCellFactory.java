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

import eu.kalodiodev.kitapi.model.Transaction;
import eu.kalodiodev.kitapi.utils.MoneyFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Transactions amount cell factory
 *
 * amount's cell data displayed
 *
 * @author Raptodimos Thanos
 */
public class AmountCellFactory implements
        Callback<TableColumn<Transaction, Long>, TableCell<Transaction, Long>> {

    @Override
    public TableCell<Transaction, Long> call(TableColumn<Transaction, Long> param) {
        return new TableCell<Transaction, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    // Amount is converted from cents to dollars
                    setText(MoneyFormat.format(item / 100d));
                }
            }
        };
    }
}