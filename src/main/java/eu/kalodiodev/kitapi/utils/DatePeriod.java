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


import java.time.LocalDate;

/**
 * Class provides methods with date calculations
 */
public class DatePeriod {

    private DatePeriod() {
        // Prevent instantiation - all methods are static
    }

    /**
     * Last month start date
     *
     * @param monthsAgo how many months ago
     * @return date
     */
    public static LocalDate lastMonthsStart(int monthsAgo) {
        return LocalDate.of(
                LocalDate.now().minusMonths(monthsAgo).getYear(),
                LocalDate.now().getMonth().minus(monthsAgo),
                1);
    }

    /**
     * Last month end date
     *
     * @param monthsAgo how many months ago
     * @return date
     */
    public static LocalDate lastMonthsEnd(int monthsAgo) {
        return LocalDate.of(
                LocalDate.now().minusMonths(monthsAgo).getYear(),
                LocalDate.now().getMonth().minus(monthsAgo),
                LocalDate.now().minusMonths(monthsAgo).lengthOfMonth());
    }

    /**
     * Current year start date
     *
     * @return date
     */
    public static LocalDate currentYearStart() {
        return LocalDate.of(LocalDate.now().getYear() , 1, 1);
    }
}
