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

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.zip.ZipFile;

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

    /** Converts any integer number to a boolean (true mean non-zero). */
    public static boolean bool(final int value) {
        return (value != 0);
    }

    /** Converts any number to a boolean (true mean non-zero). */
    public static boolean bool(final double value) {
        return (value != FALSE);
    }

    /** Converts a AtomicBoolean to a boolean (null == false). */
    public static boolean bool(final AtomicBoolean value) {
        return (value != null) && value.get();
    }

    /** Converts any Number to a boolean (true mean non-zero). */
    public static boolean bool(final Number value) {
        return (value != null) && (value.doubleValue() != 0);
    }

    /** Converts a Matcher to a boolean (true mean matches()). */
    @GwtIncompatible
    public static boolean bool(final Matcher value) {
        return (value != null) && value.matches();
    }

    /** Converts a Character (because Character does not extend Number) to a boolean (true mean non-zero). */
    public static boolean bool(final Character value) {
        return (value != null) && (value.charValue() != 0);
    }

    /** Converts a Boolean to a boolean (null == false). */
    public static boolean bool(final Boolean value) {
        return (value != null) && value.booleanValue();
    }

    /** Converts any Date to a boolean (true mean non-zero). */
    public static boolean bool(final Date value) {
        return (value != null) && (value.getTime() != 0);
    }

    /** Converts any Calendar to a boolean (true mean non-zero). */
    public static boolean bool(final Calendar value) {
        return (value != null) && (value.getTimeInMillis() != 0);
    }

    /** Converts a boolean[] to a boolean (true mean non-empty). */
    public static boolean bool(final boolean[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a byte[] to a boolean (true mean non-empty). */
    public static boolean bool(final byte[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a char[] to a boolean (true mean non-empty). */
    public static boolean bool(final char[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a short[] to a boolean (true mean non-empty). */
    public static boolean bool(final short[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a int[] to a boolean (true mean non-empty). */
    public static boolean bool(final int[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a float[] to a boolean (true mean non-empty). */
    public static boolean bool(final float[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a long[] to a boolean (true mean non-empty). */
    public static boolean bool(final long[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a double[] to a boolean (true mean non-empty). */
    public static boolean bool(final double[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a Object[] to a boolean (true mean non-empty). */
    public static boolean bool(final Object[] value) {
        return (value != null) && (value.length > 0);
    }

    /** Converts a Collection<?> to a boolean (true mean non-empty). */
    public static boolean bool(final Collection<?> value) {
        return (value != null) && !value.isEmpty();
    }

    /** Converts a Map<?,?> to a boolean (true mean non-empty). */
    public static boolean bool(final Map<?, ?> value) {
        return (value != null) && !value.isEmpty();
    }

    /** Converts a Iterator<?> to a boolean (true mean non-empty/hasNext()). */
    public static boolean bool(final Iterator<?> value) {
        return (value != null) && value.hasNext();
    }

    /** Converts a Enumeration<?> to a boolean (true mean non-empty/hasMoreElements()). */
    public static boolean bool(final Enumeration<?> value) {
        return (value != null) && value.hasMoreElements();
    }

    /** Converts a Iterable<?> to a boolean (true mean non-empty/hasNext()). */
    public static boolean bool(final Iterable<?> value) {
        return (value != null) && value.iterator().hasNext();
    }

    /** Converts a CharSequence to a boolean (true mean non-empty). */
    public static boolean bool(final CharSequence value) {
        return (value != null) && (value.length() > 0);
    }

    /** Converts a File to a boolean (true mean non-empty/contains bytes(file) or files(directory)). */
    public static boolean bool(final File value) {
        try {
            if ((value != null) && value.exists()) {
                if (value.isFile()) {
                    return (value.length() > 0);
                } else if (value.isDirectory()) {
                    return bool(value.list());
                }
            }
        } catch (final Throwable t) {
            // NOP
        }
        return false;
    }

    /** Converts a RandomAccessFile to a boolean (true mean non-empty). */
    @GwtIncompatible
    public static boolean bool(final RandomAccessFile value) {
        try {
            return (value != null) && (value.length() > 0);
        } catch (final Throwable t) {
            return false;
        }
    }

    /** Converts a ZipFile to a boolean (true mean non-empty). */
    @GwtIncompatible
    public static boolean bool(final ZipFile value) {
        try {
            return (value != null) && (value.size() > 0);
        } catch (final Throwable t) {
            return false;
        }
    }

    /** Converts a Enum<?> to a boolean (true mean non-ordinal-0). */
    public static boolean bool(final Enum<?> value) {
        return (value != null) && (value.ordinal() > 0);
    }

    /** Converts a Buffer to a boolean (true mean hasRemaining()). */
    @GwtIncompatible
    public static boolean bool(final Buffer value) {
        return (value != null) && value.hasRemaining();
    }

    /** Converts a Dictionary to a boolean (true mean non-empty). */
    @GwtIncompatible
    public static boolean bool(final Dictionary<?, ?> value) {
        return (value != null) && !value.isEmpty();
    }

    /** Converts a boolean to a number (TRUE/FALSE). */
    public static double number(final boolean value) {
        return value ? TRUE : FALSE;
    }
}