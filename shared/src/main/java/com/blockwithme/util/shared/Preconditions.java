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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Some non-Xtend compatible validation code
 *
 * @author monster
 */
public class Preconditions {
    public static <T> T requireNonNull(final T obj, final Object name) {
        if (obj == null)
            throw new NullPointerException(String.valueOf(name));
        return obj;
    }

    public static String requireNonEmpty(final String obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.isEmpty()) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static boolean[] requireNonEmpty(final boolean[] obj,
            final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static byte[] requireNonEmpty(final byte[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static short[] requireNonEmpty(final short[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static char[] requireNonEmpty(final char[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static int[] requireNonEmpty(final int[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static long[] requireNonEmpty(final long[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static float[] requireNonEmpty(final float[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static double[] requireNonEmpty(final double[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static <T> T[] requireNonEmpty(final T[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.length == 0) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static <T extends Collection<?>> T requireNonEmpty(final T obj,
            final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.isEmpty()) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static <T extends Map<?, ?>> T requireNonEmpty(final T obj,
            final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (obj.isEmpty()) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static <T extends Iterator<?>> T requireNonEmpty(final T obj,
            final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        if (!obj.hasNext()) {
            throw new IllegalArgumentException("Empty: " + name);
        }
        return obj;
    }

    public static <T> T requireContains(final T obj, final T[] array,
            final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        for (final T o : array) {
            if (o == obj) {
                return obj;
            }
        }
        throw new IllegalArgumentException("Unknown: " + name + " " + obj);
    }

    public static <T> T[] requireContainsNoNull(final T[] obj, final Object name) {
        if (obj == null) {
            throw new NullPointerException(String.valueOf(name));
        }
        for (int i = 0; i < obj.length; i++) {
            final T o = obj[i];
            if (o == null) {
                final String msg = name + "[" + i + "] ARRAY: "
                        + Arrays.asList(obj);
                throw new NullPointerException(msg);
            }
        }
        return obj;
    }
}
