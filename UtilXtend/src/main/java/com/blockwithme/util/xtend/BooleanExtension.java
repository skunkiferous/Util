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
package com.blockwithme.util.xtend;

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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import com.blockwithme.util.shared.GwtIncompatible;

/**
 * Xtend Extension related to Boolean values.
 *
 * Due to Xtend currently badly lacking unary operators, all values are
 * converted to "not true?" instead of "true?"
 *
 * @author monster
 */
public class BooleanExtension extends ScalarExtension {

//    static val ฿ = 'B'

    /** Converts any integer number to a boolean (false means non-zero). */
    @Pure
    @Inline("($1 == 0)")
    public static boolean operator_not(final int value) {
        return (value == 0);
    }

    /** Converts any number to a boolean (false means non-zero). */
    @Pure
    @Inline("($1 == 0)")
    public static boolean operator_not(final double value) {
        return (value == 0);
    }

    /** Converts a AtomicBoolean to a boolean (null == false). */
    @Pure
    @Inline("(($1 == null) || !$1.get())")
    public static boolean operator_not(final AtomicBoolean value) {
        return (value == null) || !value.get();
    }

    /** Converts any Number to a boolean (false means non-zero). */
    @Pure
    @Inline("(($1 == null) || ($1.doubleValue() == 0))")
    public static boolean operator_not(final Number value) {
        return (value == null) || (value.doubleValue() == 0);
    }

    /** Converts a Matcher to a boolean (false means matches()). */
    @GwtIncompatible
    @Pure
    @Inline("(($1 == null) || !$1.matches())")
    public static boolean operator_not(final Matcher value) {
        return (value == null) || !value.matches();
    }

    /** Converts a Character (because Character does not extend Number) to a boolean (false means non-zero). */
    @Pure
    @Inline("(($1 == null) || ($1.charValue() == 0))")
    public static boolean operator_not(final Character value) {
        return (value == null) || (value.charValue() == 0);
    }

    /** Converts a Boolean to a boolean (null == false). */
    @Pure
    @Inline("(($1 == null) || !$1.booleanValue())")
    public static boolean operator_not(final Boolean value) {
        return (value == null) || !value.booleanValue();
    }

    /** Converts any Date to a boolean (false means non-zero). */
    @Pure
    @Inline("(($1 == null) || ($1.getTime() == 0))")
    public static boolean operator_not(final Date value) {
        return (value == null) || (value.getTime() == 0);
    }

    /** Converts any Calendar to a boolean (false means non-zero). */
    @Pure
    @Inline("(($1 == null) || ($1.getTimeInMillis() == 0))")
    public static boolean operator_not(final Calendar value) {
        return (value == null) || (value.getTimeInMillis() == 0);
    }

    /** Converts a boolean[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final boolean[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a byte[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final byte[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a char[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final char[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a short[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final short[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a int[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final int[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a float[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final float[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a long[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final long[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a double[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final double[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a Object[] to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length == 0))")
    public static boolean operator_not(final Object[] value) {
        return (value == null) || (value.length == 0);
    }

    /** Converts a Collection<?> to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || $1.isEmpty())")
    public static boolean operator_not(final Collection<?> value) {
        return (value == null) || value.isEmpty();
    }

    /** Converts a Map<?,?> to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || $1.isEmpty())")
    public static boolean operator_not(final Map<?, ?> value) {
        return (value == null) || value.isEmpty();
    }

    /** Converts a Iterator<?> to a boolean (false means non-empty/hasNext()). */
    @Pure
    @Inline("(($1 == null) || $1.hasNext())")
    public static boolean operator_not(final Iterator<?> value) {
        return (value == null) || !value.hasNext();
    }

    /** Converts a Enumeration<?> to a boolean (false means non-empty/hasMoreElements()). */
    @Pure
    @Inline("((%1 == null) || !$1.hasMoreElements())")
    public static boolean operator_not(final Enumeration<?> value) {
        return (value == null) || !value.hasMoreElements();
    }

    /** Converts a Iterable<?> to a boolean (false means non-empty/hasNext()). */
    @Pure
    @Inline("(($1 == null) || !$1.iterator().hasNext())")
    public static boolean operator_not(final Iterable<?> value) {
        return (value == null) || !value.iterator().hasNext();
    }

    /** Converts a CharSequence to a boolean (false means non-empty). */
    @Pure
    @Inline("(($1 == null) || ($1.length() == 0))")
    public static boolean operator_not(final CharSequence value) {
        return (value == null) || (value.length() == 0);
    }

    /** Converts a File to a boolean (false means non-empty/contains bytes(file) or files(directory)). */
    public static boolean operator_not(final File value) {
        try {
            if ((value != null) && value.exists()) {
                if (value.isFile()) {
                    return (value.length() == 0);
                } else if (value.isDirectory()) {
                    final String[] list = value.list();
                    return (list == null) || (list.length == 0);
                }
            }
        } catch (final Throwable t) {
            // NOP
        }
        return true;
    }

    /** Converts a RandomAccessFile to a boolean (false means non-empty). */
    @GwtIncompatible
    public static boolean operator_not(final RandomAccessFile value) {
        try {
            return (value == null) || (value.length() == 0);
        } catch (final Throwable t) {
            return false;
        }
    }

    /** Converts a ZipFile to a boolean (false means non-empty). */
    @GwtIncompatible
    public static boolean operator_not(final ZipFile value) {
        try {
            return (value == null) || (value.size() == 0);
        } catch (final Throwable t) {
            return false;
        }
    }

    /** Converts a Enum<?> to a boolean (false means non-ordinal-0). */
    @Pure
    @Inline("(($1 == null) || ($1.ordinal() == 0))")
    public static boolean operator_not(final Enum<?> value) {
        return (value == null) || (value.ordinal() == 0);
    }

    /** Converts a Buffer to a boolean (false means hasRemaining()). */
    @GwtIncompatible
    @Pure
    @Inline("(($1 == null) || !$1.hasRemaining())")
    public static boolean operator_not(final Buffer value) {
        return (value == null) || !value.hasRemaining();
    }

    /** Converts a Dictionary to a boolean (false means non-empty). */
    @GwtIncompatible
    @Pure
    @Inline("(($1 == null) || $1.isEmpty())")
    public static boolean operator_not(final Dictionary<?, ?> value) {
        return (value == null) || value.isEmpty();
    }
}
