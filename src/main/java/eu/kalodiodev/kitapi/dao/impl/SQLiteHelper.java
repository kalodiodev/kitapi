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

import eu.kalodiodev.kitapi.db.JdbcSqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLite Helper Class
 *
 * @author Raptodimos Thanos
 */
public class SQLiteHelper {

    /**
     * Execute count query by name
     *
     * @param connection sqlite connection
     * @param query query to execute
     * @param nameToCount count entries with this name
     * @param countLabel count result label
     * @return number of entries
     */
    public static int executeNameCountQuery(JdbcSqliteConnection connection,
                                            String query, String nameToCount, String countLabel) {

        try (Connection conn = connection.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, nameToCount);
            try (ResultSet results = preparedStatement.executeQuery()) {
                return results.getInt(countLabel);
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Get Generated key from result set
     *
     * @param generatedKeys Generated keys result set
     * @return key id
     * @throws SQLException sql error
     */
    public static int getGeneratedKey(ResultSet generatedKeys, int columnIndex) throws SQLException{
        if(generatedKeys.next()) {
            return generatedKeys.getInt(columnIndex);
        } else {
            throw new SQLException("Couldn't get _id for entry");
        }
    }
}