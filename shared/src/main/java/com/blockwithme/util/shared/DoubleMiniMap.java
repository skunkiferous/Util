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
 * DoubleMiniMap contains static methods, to simulate a map of double (or any
 * other non-long primitive type) on a small single array.
 *
 * The array is kept as small as possible. Zero value map are
 * interpreted as a "remove" call (because get from a non-existent key returns
 * 0). Since a new array is always returned, the array can effectively be
 * treated like a thread-safe immutable object (if not otherwise modified).
 *
 * @author monster
 */
public class DoubleMiniMap {
    private static final double[] EMPTY = new double[0];

    /** Returns the count of entries. */
    public static int getSize(final double[] map) {
        return map.length / 2;
    }

    /** Returns the map keyes. */
    public static double[] getKeyes(final double[] map) {
        final int size = getSize(map);
        if (size == 0) {
            return EMPTY;
        }
        final double[] result = new double[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = map[i * 2];
        }
        return result;
    }

    /** Returns the map values. */
    public static double[] getValues(final double[] map) {
        final int size = getSize(map);
        if (size == 0) {
            return EMPTY;
        }
        final double[] result = new double[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = map[(i * 2) + 1];
        }
        return result;
    }

    /** Returns a value if present, otherwise defaultValue. */
    public static double getValue(final double[] map, final double key,
            final double defaultValue) {
        final int size = getSize(map);
        for (int i = 0; i < size; i++) {
            final int index = i * 2;
            if (key == map[index]) {
                return map[index + 1];
            }
        }
        return defaultValue;
    }

    /** Returns a value if present, otherwise 0. */
    public static double getValue(final double[] map, final double key) {
        return getValue(map, key, 0);
    }

    /**
     * Sets a value. Zero value means "remove" value.
     * Returns the unchanged array, or a new updated array.
     */
    public static double[] setValue(final double[] map, final double key,
            final double newValue) {
        if (newValue == 0) {
            return removeValue(map, key);
        }
        final int size = getSize(map);
        for (int i = 0; i < size; i++) {
            final int index = i * 2;
            if (key == map[index]) {
                // GWT doesn't like clone() ...
                final double[] result = new double[map.length];
                System.arraycopy(map, 0, result, 0, map.length);
                result[index + 1] = newValue;
                return result;
            }
        }
        final double[] result = new double[map.length + 2];
        System.arraycopy(map, 0, result, 0, map.length);
        result[map.length] = key;
        result[map.length + 1] = newValue;
        return result;
    }

    /**
     * Removes a value.
     * Returns the unchanged array, or a new updated array.
     */
    public static double[] removeValue(final double[] map, final double key) {
        final int size = getSize(map);
        for (int i = 0; i < size; i++) {
            final int index = (i * 2);
            if (key == map[index]) {
                final double[] result = new double[map.length - 2];
                System.arraycopy(map, 0, result, 0, index);
                System.arraycopy(map, index + 2, result, index, (map.length
                        - index - 2));
                return result;
            }
        }
        return map;
    }

    /**
     * Set all the map. Null value means "remove" value.
     * Returns the unchanged array, or a new updated array.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static double[] setValues(final double[] map,
            final Map<? extends Number, ? extends Number> newValues) {
        Objects.requireNonNull(newValues);
        if (newValues.isEmpty()) {
            return map;
        }
        Map<? extends Number, ? extends Number> oldValues = getAsMap(map);
        if (oldValues.isEmpty()) {
            oldValues = newValues;
        } else {
            oldValues.putAll((Map) newValues);
        }
        double[] result = new double[oldValues.size() * 2];
        int index = 0;
        for (final Number key : oldValues.keySet()) {
            final Number value = oldValues.get(key);
            if ((value != null) && (value.doubleValue() != 0)) {
                result[index++] = key.doubleValue();
                result[index++] = value.doubleValue();
            }
            // else: null/0 means "remove"
        }
        if (index < result.length) {
            // We had at least one "null/0" value
            final double[] tmp = result;
            result = new double[index];
            System.arraycopy(tmp, 0, result, 0, index);
        }
        return result;
    }

    /** Returns as a map. */
    public static Map<Double, Double> getAsMap(final double[] map) {
        Map<Double, Double> result = Collections.emptyMap();
        final int size = getSize(map);
        if (size > 0) {
            result = new HashMap<Double, Double>();
            for (int i = 0; i < size; i++) {
                final int index = i * 2;
                result.put(map[index], map[index + 1]);
            }
        }
        return result;
    }
}
