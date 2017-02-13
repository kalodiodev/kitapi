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

package eu.kalodiodev.kitapi;

import com.sun.javafx.application.LauncherImpl;
import eu.kalodiodev.kitapi.controller.MainController;
import eu.kalodiodev.kitapi.dao.DaoFactory;
import eu.kalodiodev.kitapi.db.SqliteDatabase;
import eu.kalodiodev.kitapi.service.CategoryService;
import eu.kalodiodev.kitapi.service.ICategoryService;
import eu.kalodiodev.kitapi.service.ITransactionService;
import eu.kalodiodev.kitapi.service.TransactionService;
import eu.kalodiodev.kitapi.utils.BuildResource;
import eu.kalodiodev.kitapi.utils.LanguageResource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;


public class Main extends Application {

    private BooleanProperty ready = new SimpleBooleanProperty(false);

    // Database type
    private static final int DATABASE_TYPE = DaoFactory.SQLITE;

    // Services
    private ICategoryService incomeCategoryService = new CategoryService();
    private ICategoryService expensesCategoryService = new CategoryService();
    private ITransactionService incomeService = new TransactionService();
    private ITransactionService expensesService = new TransactionService();


    private void setupServices() {

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Create database if not exists
                SqliteDatabase.create();

                // Category Services
                expensesCategoryService.setDao(DaoFactory.getFactory(DATABASE_TYPE).getExpensesCategoryDao());
                incomeCategoryService.setDao(DaoFactory.getFactory(DATABASE_TYPE).getIncomeCategoryDao());

                expensesCategoryService.all();
                incomeCategoryService.all();

                // Transaction Services
                expensesService.setDao(DaoFactory.getFactory(DATABASE_TYPE).getExpensesDao());
                incomeService.setDao(DaoFactory.getFactory(DATABASE_TYPE).getIncomeDao());

                // Load all
                expensesService.all();
                incomeService.all();

                // After init is ready, the app is ready to be shown
                ready.setValue(Boolean.TRUE);

                notifyPreloader(new Preloader.StateChangeNotification(
                        Preloader.StateChangeNotification.Type.BEFORE_START));

                return null;
            }
        };
        new Thread(task).start();
    }

    @Override
    public void init() throws Exception {
        super.init();

        setupServices();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Locale.setDefault(new Locale("fr","FR"));
        ResourceBundle bundle = LanguageResource.getResource();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"), bundle);
        Parent root = loader.load();
        primaryStage.getIcons().add(BuildResource.getApplicationIcon());
        primaryStage.setTitle(bundle.getString("app.title"));
        MainController mainController = loader.getController();
        primaryStage.setScene(new Scene(root, 1100, 600));

        // After the app is ready, show the stage
        ready.addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                Platform.runLater(() -> {
                    mainController.setCategoryServices(expensesCategoryService, incomeCategoryService);
                    mainController.setTransactionServices(expensesService, incomeService);
                    mainController.updateTotals();
                    primaryStage.show();
                });
            }
        });
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, SplashScreenLoader.class, args);
        //launch(args);
    }
}