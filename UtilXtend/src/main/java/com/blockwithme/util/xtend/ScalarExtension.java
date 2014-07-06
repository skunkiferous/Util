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

import java.nio.Buffer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import com.blockwithme.util.shared.GwtIncompatible;

/**
 * Xtend Extension related to "scalar" values.
 *
 * The goal is to allow to easily get the "scale/size/length" of a value.
 *
 * @author monster
 */
public class ScalarExtension extends JavaUtilLoggingExtension {

    /** Represents the boolean true value. */
    public static final int TRUE = 1;

    /** Represents the boolean false value. */
    public static final int FALSE = 0;

    /** Converts a boolean to an int (true == 1, false == 0). */
    @Pure
    @Inline("($1 ? 1 : 0)")
    public static int operator_plus(final boolean value) {
        return value ? TRUE : FALSE;
    }

    /** Converts a AtomicBoolean to an int (true == 1, null/false == 0). */
    @Pure
    @Inline("((($1 == null) || !$1.get()) ? 0 : 1)")
    public static int operator_plus(final AtomicBoolean value) {
        return ((value == null) || !value.get()) ? FALSE : TRUE;
    }

    /** Converts any Long to a long (null == 0). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.longValue())")
    public static long operator_plus(final Long value) {
        return (value == null) ? 0 : value.longValue();
    }

    /** Converts any Number to an int (null == 0). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.intValue())")
    public static int operator_plus(final Number value) {
        return (value == null) ? 0 : value.intValue();
    }

    /** Converts a Character to an int (null == 0). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.charValue())")
    public static int operator_plus(final Character value) {
        return (value == null) ? 0 : value.charValue();
    }

    /** Converts a Boolean to an int (true == 1, null/false == 0). */
    @Pure
    @Inline("((($1 == null) || !$1.booleanValue()) ? 0 : 1)")
    public static int operator_plus(final Boolean value) {
        return ((value == null) || !value.booleanValue()) ? FALSE : TRUE;
    }

    /** Converts any Date to a long (null == 0). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.getTime())")
    public static long operator_plus(final Date value) {
        return (value == null) ? 0 : value.getTime();
    }

    /** Converts any Calendar to a long (null == 0). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.getTimeInMillis())")
    public static long operator_plus(final Calendar value) {
        return (value == null) ? 0 : value.getTimeInMillis();
    }

    /** Converts a boolean[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final boolean[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a byte[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final byte[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a char[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final char[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a short[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final short[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a int[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final int[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a float[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final float[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a long[] to a an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final long[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a double[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final double[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a Object[] to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length)")
    public static int operator_plus(final Object[] value) {
        return (value == null) ? 0 : value.length;
    }

    /** Converts a Collection<?> to an int (null == 0, otherwise size). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.size())")
    public static int operator_plus(final Collection<?> value) {
        return (value == null) ? 0 : value.size();
    }

    /** Converts a Map<?,?> to an int (null == 0, otherwise size). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.size())")
    public static int operator_plus(final Map<?, ?> value) {
        return (value == null) ? 0 : value.size();
    }

    /** Converts a CharSequence to an int (null == 0, otherwise length). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.length())")
    public static int operator_plus(final CharSequence value) {
        return (value == null) ? 0 : value.length();
    }

    /** Converts a Enum<?> to an int (null == 0, otherwise ordinal). */
    @Pure
    @Inline("(($1 == null) ? 0 : $1.ordinal())")
    public static int operator_plus(final Enum<?> value) {
        return (value == null) ? 0 : value.ordinal();
    }

    /** Converts a Buffer to an int (null == 0, otherwise remaining). */
    @GwtIncompatible
    @Pure
    @Inline("(($1 == null) ? 0 : $1.remaining())")
    public static int operator_plus(final Buffer value) {
        return (value == null) ? 0 : value.remaining();
    }

    /** Converts a Dictionary to an int (null == 0, otherwise size). */
    @GwtIncompatible
    @Pure
    @Inline("(($1 == null) ? 0 : $1.size())")
    public static int operator_plus(final Dictionary<?, ?> value) {
        return (value == null) ? 0 : value.size();
    }

    /** Converts a String to an boolean (null == false, otherwise Boolean.parseBoolean(value)). */
    @Pure
    @Inline("(($1 == null) ? false : Boolean.parseBoolean($1.toString()))")
    public static boolean booleanValue(final CharSequence value) {
        return (value == null) ? false : Boolean.parseBoolean(value.toString());
    }

    /** Converts a String to a byte (null == 0, otherwise Byte.parseByte(value)). */
    @Pure
    @Inline("(($1 == null) ? (byte) 0 : Byte.parseByte($1.toString()))")
    public static byte byteValue(final CharSequence value) {
        return (value == null) ? (byte) 0 : Byte.parseByte(value.toString());
    }

    /** Converts a String to a char (null == 0, otherwise value.charAt(0)). */
    @Pure
    public static char charValue(final CharSequence value) {
        if (value == null) {
            return (char) 0;
        }
        if (value.length() == 1) {
            return value.charAt(0);
        }
        throw new NumberFormatException("Bad char: '" + value + "'");
    }

    /** Converts a String to a short (null == 0, otherwise Short.parseShort(value)). */
    @Pure
    @Inline("(($1 == null) ? (short) 0 : Short.parseShort($1.toString()))")
    public static short shortValue(final CharSequence value) {
        return (value == null) ? (short) 0 : Short.parseShort(value.toString());
    }

    /** Converts a String to a int (null == 0, otherwise Integer.parseInt(value)). */
    @Pure
    @Inline("(($1 == null) ? 0 : Integer.parseInt($1.toString()))")
    public static int intValue(final CharSequence value) {
        return (value == null) ? 0 : Integer.parseInt(value.toString());
    }

    /** Converts a String to a float (null == 0, otherwise Float.parseFloat(value)). */
    @Pure
    @Inline("(($1 == null) ? 0 : Float.parseFloat($1.toString()))")
    public static float floatValue(final CharSequence value) {
        return (value == null) ? 0 : Float.parseFloat(value.toString());
    }

    /** Converts a String to a long (null == 0, otherwise Long.parseLong(value)). */
    @Pure
    @Inline("(($1 == null) ? 0 : Long.parseLong($1.toString()))")
    public static long longValue(final CharSequence value) {
        return (value == null) ? 0 : Long.parseLong(value.toString());
    }

    /** Converts a String to a double (null == 0, otherwise Double.parseDouble(value)). */
    @Pure
    @Inline("(($1 == null) ? 0 : Double.parseDouble($1.toString()))")
    public static double doubleValue(final CharSequence value) {
        return (value == null) ? 0 : Double.parseDouble(value.toString());
    }
}
