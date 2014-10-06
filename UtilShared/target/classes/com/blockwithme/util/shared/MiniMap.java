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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MiniMap contains static methods, to simulate a map of properties on
 * a small single array.
 *
 * The array is kept as small as possible. Null value properties are interpreted
 * as a "remove" call. Since a new array is always returned, the array can
 * effectively be treated like a thread-safe immutable object (if not otherwise
 * modified).
 *
 * @author monster
 */
public class MiniMap {
    private static final String[] NO_PROPS = new String[0];

    private static final Object[] NO_VALUES = new Object[0];

    /** Returns the count of properties. */
    public static int getPropertiesCount(final Object[] properties) {
        return properties.length / 2;
    }

    /** Returns the property keyes. */
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

    /** Returns the property values. */
    public static Object[] getPropertyValues(final Object[] properties) {
        final int propertiesCount = getPropertiesCount(properties);
        if (propertiesCount == 0) {
            return NO_VALUES;
        }
        final Object[] result = new Object[propertiesCount];
        for (int i = 0; i < result.length; i++) {
            result[i] = properties[(i * 2) + 1];
        }
        return result;
    }

    /** Returns one property value if present, otherwise null. */
    public static Object getProperty(final Object[] properties,
            final String name) {
        final int propertiesCount = getPropertiesCount(properties);
        for (int i = 0; i < propertiesCount; i++) {
            final int index = i * 2;
            if (name.equals(properties[index])) {
                return properties[index + 1];
            }
        }
        return null;
    }

    /** Returns one property value if present, otherwise defaultValue. */
    public static Object getProperty(final Object[] properties,
            final String name, final Object defaultValue) {
        final Object result = getProperty(properties, name);
        return (result == null) ? defaultValue : result;
    }

    /**
     * Sets one property. Null value means "remove" property.
     * Returns the unchanged array, or a new updated array.
     */
    public static Object[] setProperty(final Object[] properties,
            final String name, final Object newValue) {
        if (newValue == null) {
            return removeProperty(properties, name);
        }
        final int propertiesCount = getPropertiesCount(properties);
        for (int i = 0; i < propertiesCount; i++) {
            final int index = i * 2;
            if (name.equals(properties[index])) {
                // GWT doesn't like clone() ...
                final Object[] result = new Object[properties.length];
                System.arraycopy(properties, 0, result, 0, properties.length);
                result[index + 1] = newValue;
                return result;
            }
        }
        final Object[] result = new Object[properties.length + 2];
        System.arraycopy(properties, 0, result, 0, properties.length);
        result[properties.length] = name;
        result[properties.length + 1] = newValue;
        return result;
    }

    /**
     * Removes one property.
     * Returns the unchanged array, or a new updated array.
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

    /**
     * Set all the properties. Null value means "remove" property.
     * Returns the unchanged array, or a new updated array.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object[] setProperties(final Object[] properties,
            final Map<String, ?> newProperties) {
        Objects.requireNonNull(newProperties);
        if (newProperties.isEmpty()) {
            return properties;
        }
        Map<String, ?> oldProperties = getProperties(properties);
        if (oldProperties.isEmpty()) {
            oldProperties = newProperties;
        } else {
            oldProperties.putAll((Map) newProperties);
        }
        Object[] result = new Object[oldProperties.size() * 2];
        int index = 0;
        for (final String name : oldProperties.keySet()) {
            final Object value = oldProperties.get(name);
            if (value != null) {
                result[index++] = name;
                result[index++] = value;
            }
            // else: null means "remove"
        }
        if (index < result.length) {
            // We had at least one "null" value property
            final Object[] tmp = result;
            result = new Object[index];
            System.arraycopy(tmp, 0, result, 0, index);
        }
        return result;
    }

    /** Returns all properties. */
    public static Map<String, ?> getProperties(final Object[] properties) {
        Map<String, Object> result = Collections.emptyMap();
        final int propertiesCount = getPropertiesCount(properties);
        if (propertiesCount > 0) {
            result = new HashMap<String, Object>();
            for (int i = 0; i < propertiesCount; i++) {
                final int index = i * 2;
                final String key = (String) properties[index];
                result.put(key, properties[index + 1]);
            }
        }
        return result;
    }
}
