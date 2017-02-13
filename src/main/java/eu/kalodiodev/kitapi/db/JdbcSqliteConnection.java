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

package eu.kalodiodev.kitapi.db;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection to Sqlite SqliteDatabase
 *
 * @author Raptodimos Thanos
 */
public class JdbcSqliteConnection {

    private static final String DB_NAME="kitapi.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    private Connection conn = null;


    /**
     * Connect to database
     *
     * @return sqlite connection
     */
    public Connection connect() {
        try {
            if ((conn == null) || (conn.isClosed())) {
                SQLiteConfig config = new SQLiteConfig();
                config.enforceForeignKeys(true);
                conn = DriverManager.getConnection(CONNECTION_STRING, config.toProperties());
            }
            return conn;

        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Disconnect from database
     */
    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed!");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    /**
     * Get database connection
     *
     * @return sqlite connection
     */
    public Connection getConnection() {
        return conn;
    }
}
