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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Helper class for using boolean where double is expected, and many other
 * things, when a boolean is expected.
 *
 * @author monster
 */
class Bool {

    /** Represents the boolean true value. */
    public static final double TRUE = 1.0;

    /** Represents the boolean false value. */
    public static final double FALSE = 0.0;

    /** Converts any number to a boolean. */
    public static boolean bool(final double value) {
        return (value != FALSE);
    }

    /** Converts any Number to a boolean. */
    public static boolean bool(final Number value) {
        return (value != null) && (value.doubleValue() != 0);
    }

    /** Converts a boolean[] to a boolean. */
    public static boolean bool(final boolean[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a byte[] to a boolean. */
    public static boolean bool(final byte[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a char[] to a boolean. */
    public static boolean bool(final char[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a short[] to a boolean. */
    public static boolean bool(final short[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a int[] to a boolean. */
    public static boolean bool(final int[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a float[] to a boolean. */
    public static boolean bool(final float[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a long[] to a boolean. */
    public static boolean bool(final long[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a double[] to a boolean. */
    public static boolean bool(final double[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a Object[] to a boolean. */
    public static boolean bool(final Object[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a Collection<?> to a boolean. */
    public static boolean bool(final Collection<?> value) {
        return (value != null) && !value.isEmpty();
    }

    /** Converts a Map<?,?> to a boolean. */
    public static boolean bool(final Map<?, ?> value) {
        return (value != null) && !value.isEmpty();
    }

    /** Converts a Iterator<?> to a boolean. */
    public static boolean bool(final Iterator<?> value) {
        return (value != null) && value.hasNext();
    }

    /** Converts a Iterable<?> to a boolean. */
    public static boolean bool(final Iterable<?> value) {
        return (value != null) && value.iterator().hasNext();
    }

    /** Converts a CharSequence to a boolean. */
    public static boolean bool(final CharSequence value) {
        return (value != null) && (value.length() > 0);
    }

    /** Converts a boolean to a number. */
    public static double number(final boolean value) {
        return value ? TRUE : FALSE;
    }
}