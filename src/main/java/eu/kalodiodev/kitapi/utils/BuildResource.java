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

import javafx.scene.image.Image;

import java.util.ResourceBundle;

/**
 * Build Resource Class
 *
 * @author Raptodimos Thanos
 */
public class BuildResource {

    private static final String SOURCE = "properties/build";

    private static final String VERSION_KEY = "version";
    private static final String APPLICATION_ICON_KEY = "application.icon";
    private static final String COPYRIGHT_DATE_START_KEY = "copyright.date.start";
    private static final String COPYRIGHT_DATE_END_KEY = "copyright.date.end";

    private BuildResource() {
        // Prevent instantiation - all methods are static
    }

    /**
     * Get build resource bundle
     *
     * @return resource bundle
     */
    public static ResourceBundle getResource() {
        return ResourceBundle.getBundle(SOURCE);
    }

    /**
     * Get Build version
     *
     * @return version
     */
    public static String getVersion() {
        return getResource().getString(VERSION_KEY);
    }

    /**
     * Get Application Icon
     *
     * @return icon
     */
    public static Image getApplicationIcon() {
        return new Image("/icons/" + getResource().getString(APPLICATION_ICON_KEY));
    }


    public static String getCopyrightStart() {
        return getResource().getString(COPYRIGHT_DATE_START_KEY);
    }

    public static String getCopyrightEnd() {
        return getResource().getString(COPYRIGHT_DATE_END_KEY);
    }
}
