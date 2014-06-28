/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package com.blockwithme.util.xtend

import com.blockwithme.util.shared.GwtIncompatible
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Matcher
import java.util.Calendar
import java.util.Collection
import java.util.Map
import java.util.Iterator
import java.util.Enumeration
import java.io.File
import java.io.RandomAccessFile
import java.util.zip.ZipFile
import java.nio.Buffer
import java.util.Dictionary
import java.util.Date

/**
 * Xtend Extension related to Boolean values.
 *
 * Due to Xtend currently badly lacking unary operators, all values are
 * converted to "not true?" instead of "true?"
 *
 * @author monster
 */
class BooleanExtension {

    /** Represents the boolean true value. */
    public static val TRUE = 1.0

    /** Represents the boolean false value. */
    public static val FALSE = 0.0

//    static val ฿ = 'B'

    /** Converts any integer number to a boolean (false means non-zero). */
//    @Pure
//    @Inline("($1 == 0)")
    static def !(int value) {
        (value == 0)
    }

    /** Converts any number to a boolean (false means non-zero). */
//    @Pure
//    @Inline("($1 == 0)")
    static def !(double value) {
        (value == FALSE)
    }

    /** Converts a AtomicBoolean to a boolean (null == false). */
//    @Pure
//    @Inline("(($1 == null) || !$1.get())")
    static def !(AtomicBoolean value) {
        (value == null) || !value.get()
    }

    /** Converts any Number to a boolean (false means non-zero). */
//    @Pure
//    @Inline("(($1 == null) || ($1.doubleValue() == 0))")
    static def !(Number value) {
        (value == null) || (value.doubleValue() == 0)
    }

    /** Converts a Matcher to a boolean (false means matches()). */
    @GwtIncompatible
//    @Pure
//    @Inline("(($1 == null) || !$1.matches())")
    static def !(Matcher value) {
        (value == null) || !value.matches()
    }

    /** Converts a Character (because Character does not extend Number) to a boolean (false means non-zero). */
//    @Pure
//    @Inline("(($1 == null) || ($1.charValue() == 0))")
    static def !(Character value) {
        (value == null) || (value.charValue() == 0)
    }

    /** Converts a Boolean to a boolean (null == false). */
//    @Pure
//    @Inline("(($1 == null) || !$1.booleanValue())")
    static def !(Boolean value) {
        (value == null) || !value.booleanValue()
    }

    /** Converts any Date to a boolean (false means non-zero). */
//    @Pure
//    @Inline("(($1 == null) || ($1.getTime() == 0))")
    static def !(Date value) {
        (value == null) || (value.getTime() == 0)
    }

    /** Converts any Calendar to a boolean (false means non-zero). */
//    @Pure
//    @Inline("(($1 == null) || ($1.getTimeInMillis() == 0))")
    static def !(Calendar value) {
        (value == null) || (value.getTimeInMillis() == 0)
    }

    /** Converts a boolean[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(boolean[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a byte[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(byte[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a char[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(char[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a short[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(short[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a int[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(int[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a float[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(float[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a long[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(long[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a double[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(double[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a Object[] to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length == 0))")
    static def !(Object[] value) {
        (value == null) || (value.length == 0)
    }

    /** Converts a Collection<?> to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || $1.isEmpty())")
    static def !(Collection<?> value) {
        (value == null) || value.isEmpty()
    }

    /** Converts a Map<?,?> to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || $1.isEmpty())")
    static def !(Map<?, ?> value) {
        (value == null) || value.isEmpty()
    }

    /** Converts a Iterator<?> to a boolean (false means non-empty/hasNext()). */
//    @Pure
//    @Inline("(($1 == null) || $1.hasNext())")
    static def !(Iterator<?> value) {
        (value == null) || !value.hasNext()
    }

    /** Converts a Enumeration<?> to a boolean (false means non-empty/hasMoreElements()). */
//    @Pure
//    @Inline("((%1 == null) || !$1.hasMoreElements())")
    static def !(Enumeration<?> value) {
        (value == null) || !value.hasMoreElements()
    }

    /** Converts a Iterable<?> to a boolean (false means non-empty/hasNext()). */
//    @Pure
//    @Inline("(($1 == null) || !$1.iterator().hasNext())")
    static def !(Iterable<?> value) {
        (value == null) || !value.iterator().hasNext()
    }

    /** Converts a CharSequence to a boolean (false means non-empty). */
//    @Pure
//    @Inline("(($1 == null) || ($1.length() == 0))")
    static def !(CharSequence value) {
        (value == null) || (value.length() == 0)
    }

    /** Converts a File to a boolean (false means non-empty/contains bytes(file) or files(directory)). */
    static def !(File value) {
        try {
            if ((value != null) && value.exists()) {
                if (value.isFile()) {
                    (value.length() == 0)
                } else if (value.isDirectory()) {
                    !(value.list())
                }
            }
        } catch (Throwable t) {
            // NOP
        }
        true
    }

    /** Converts a RandomAccessFile to a boolean (false means non-empty). */
    @GwtIncompatible
    static def !(RandomAccessFile value) {
        try {
            (value == null) || (value.length() == 0)
        } catch (Throwable t) {
            false
        }
    }

    /** Converts a ZipFile to a boolean (false means non-empty). */
    @GwtIncompatible
    static def !(ZipFile value) {
        try {
            (value == null) || (value.size() == 0)
        } catch (Throwable t) {
            false
        }
    }

    /** Converts a Enum<?> to a boolean (false means non-ordinal-0). */
//    @Pure
//    @Inline("(($1 == null) || ($1.ordinal() == 0))")
    static def !(Enum<?> value) {
        (value == null) || (value.ordinal() == 0)
    }

    /** Converts a Buffer to a boolean (false means hasRemaining()). */
    @GwtIncompatible
//    @Pure
//    @Inline("(($1 == null) || !$1.hasRemaining())")
    static def !(Buffer value) {
        (value == null) || !value.hasRemaining()
    }

    /** Converts a Dictionary to a boolean (false means non-empty). */
    @GwtIncompatible
//    @Pure
//    @Inline("(($1 == null) || $1.isEmpty())")
    static def !(Dictionary<?, ?> value) {
        (value == null) || value.isEmpty()
    }

    /** Converts a boolean to a number (TRUE/FALSE). */
//    @Pure
//    @Inline("($1 ? 1.0 : 0.0)")
    static def +(boolean value) {
        if (value) TRUE else FALSE
    }
}