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

package eu.kalodiodev.kitapi.utils;

import java.text.DecimalFormat;

/**
 * Money Format Helper
 *
 * @author Raptodimos Thanos
 */
public class MoneyFormat {

    private static final String PATTERN = "#,##0.00";

    private MoneyFormat() {
        // Prevent instantiation - all methods are static
    }

    /**
     * Format value to money
     *
     * @param value value to format
     * @return formatted value
     */
    public static String format(Double value) {
        return new DecimalFormat(PATTERN).format(value);
    }
}