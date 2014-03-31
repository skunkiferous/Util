/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blockwithme.util.shared;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PropertiesHelper contains static methods, to simulate a map of properties on
 * a small single array.
 *
 * TODO test!
 *
 * @author monster
 */
public class PropertiesHelper {
    private static final String[] NO_PROPS = new String[0];

    public static final int MIN_ARRAY_SIZE = 8;

    /** Returns a power of two, bigger or equal to the input. */
    public static int powerOfTwo(final int i) {
        // Don't create too small arrays
        return (i < MIN_ARRAY_SIZE) ? MIN_ARRAY_SIZE : SystemUtils
                .powerOfTwo(i);
    }

    /** Returns the count of non-null properties. */
    public static int getPropertiesCount(final Object[] properties) {
        return properties.length / 2;
    }

    /** Returns the non-null property keyes. */
    public static String[] getPropertyKeyes(final Object[] properties) {
        final int propertiesCount = getPropertiesCount(properties);
        if (propertiesCount == 0) {
            return NO_PROPS;
        }
        final String[] result = new String[propertiesCount];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) properties[i * 2];
        }
        return result;
    }

    /** Returns one property if present, otherwise null. */
    public static Object getProperty(final Object[] properties,
            final String name) {
        final int propertiesCount = getPropertiesCount(properties);
        for (int i = 0; i < propertiesCount; i++) {
            if (name.equals(properties[i * 2])) {
                return properties[(i * 2) + 1];
            }
        }
        return null;
    }

    /**
     * Sets one property. Null means "remove".
     * Returns the updated array.
     */
    public static Object[] setProperty(Object[] properties, final String name,
            final Object newValue) {
        if (newValue == null) {
            return removeProperty(properties, name);
        }
        final int propertiesCount = getPropertiesCount(properties);
        for (int i = 0; i < propertiesCount; i++) {
            if (name.equals(properties[i * 2])) {
                properties[(i * 2) + 1] = newValue;
                return properties;
            }
        }
        if (propertiesCount == properties.length) {
            final int capacity = powerOfTwo(propertiesCount + 1);
            if (propertiesCount == 0) {
                properties = new Object[capacity];
            } else {
                properties = Arrays.copyOf(properties, capacity);
            }
        }
        properties[propertiesCount] = newValue;
        return properties;
    }

    /**
     * Removes one property.
     * Returns the updated array.
     */
    public static Object[] removeProperty(final Object[] properties,
            final String name) {
        final int propertiesCount = getPropertiesCount(properties);
        for (int i = 0; i < propertiesCount; i++) {
            final int index = (i * 2);
            if (name.equals(properties[index])) {
                final Object[] result = new Object[properties.length - 2];
                System.arraycopy(properties, 0, result, 0, index);
                System.arraycopy(properties, index + 2, result, index,
                        (properties.length - index - 2));
                return result;
            }
        }
        return properties;
    }

    /** Set all the properties. */
    public static Object[] setProperties(final Object[] properties,
            final Map<String, Object> newProperties) {
        Objects.requireNonNull(newProperties);
        if (newProperties.isEmpty()) {
            return properties;
        }
        Map<String, Object> oldProperties = getProperties(properties);
        if (oldProperties.isEmpty()) {
            oldProperties = newProperties;
        } else {
            oldProperties.putAll(newProperties);
        }
        final Object[] result = new Object[oldProperties.size() * 2];
        int index = 0;
        for (final String name : oldProperties.keySet()) {
            properties[index++] = name;
            properties[index++] = oldProperties.get(name);
        }
        return result;
    }

    /** Returns all properties. */
    public static Map<String, Object> getProperties(final Object[] properties) {
        Map<String, Object> result = Collections.emptyMap();
        final int propertiesCount = getPropertiesCount(properties);
        if (propertiesCount > 0) {
            result = new HashMap<String, Object>();
            for (int i = 0; i < propertiesCount; i++) {
                final String key = (String) properties[i * 2];
                result.put(key, properties[(i * 2) + 1]);
            }
        }
        return result;
    }
}
