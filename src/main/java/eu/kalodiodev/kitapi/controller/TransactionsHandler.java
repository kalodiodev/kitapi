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

package eu.kalodiodev.kitapi.controller;

import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.service.ITransactionService;
import eu.kalodiodev.kitapi.utils.BuildResource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Show Transactions Table Handler
 *
 * @author Raptodimos Thanos
 */
public class TransactionsHandler {

    private ITransactionService transactionService;
    private ICategoryService categoryService;
    private ResourceBundle resourceBundle;

    /**
     * Show transactions Handler Constructor
     *
     * @param transactionService transactions service
     * @param categoryService categories service
     * @param resourceBundle resources bundle
     */
    public TransactionsHandler(ITransactionService transactionService,
                               ICategoryService categoryService, ResourceBundle resourceBundle) {

        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Show Stage
     *
     * @param windowTitle stage title
     * @throws IOException if transactions view could not be loaded
     */
    public void show(String windowTitle, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/transactions.fxml"), resourceBundle);
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(windowTitle);
        stage.getIcons().add(BuildResource.getApplicationIcon());
        stage.setScene(new Scene(root, 800, 500));

        // Controller
        TransactionsController transactionsController = fxmlLoader.getController();
        transactionsController.setTransactionService(transactionService);
        transactionsController.setCategoryService(categoryService);
        transactionsController.showData();
        transactionsController.setTableTitle(title);

        stage.showAndWait();
    }
}
