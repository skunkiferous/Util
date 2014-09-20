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

package com.blockwithme.util.proto.base40;

import java.math.BigInteger;

/**
 * <code>Base40</code> represents a 64bit non-negative base-40 value.
 * The value is expressed as *lower-case*. It covers the whole range of a long,
 * treating it as an unsigned long. Leading zeroes are not part of the String by
 * default, and are ignored when converting a String to a Base40.
 *
 * The character set can be changed, globally, once, by calling
 * <code>Base40CharacterSetLoader.setCharacterSet()</code> BEFORE the
 * first access to Base40.
 *
 * The real benefit comes when the Base40 value is stored internally as a long
 * in your objects, instead of as an instance of Base40. The same applies to
 * serialization.
 *
 * Since all the important functionality is available from public static
 * methods, it is easy to add base-40 functionality to any class.
 *
 * LIMITATION: The textual value can be either a single '0', or a string that
 * does not start with 0. This is because Base40 is not really a String, but
 * rather a number, and like most numbers, the leading zeros are not
 * significant, and therefore omitted.
 *
 * WARNING: Do not change your character set, after you started saving data
 * with it, otherwise the names/IDs you saved with the old character set,
 * will come out different when you read them with the new one. I have no plan
 * to change this in the future, as it would make the serialized footprint
 * much bigger.
 */
public final class Base40 extends AbstractBase40<Base40> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

//
//    /** Static cache. */
//    private static volatile Internalizer<Base40> INTERN = new InternalizerImpl<>();
//
//    /** Sets the static cache, if desired. */
//    public static void setInternalizer(
//            final Internalizer<Base40> theInternalizer) {
//        final Internalizer<Base40> old = INTERN;
//        INTERN = theInternalizer;
//        if ((old != null) && (theInternalizer != null)) {
//            for (final Base40 base40 : old) {
//                theInternalizer.intern(base40);
//            }
//        }
//    }
//
//    /** Statically cache a Base40 instance. */
//    public static Base40 intern(final Base40 base40) {
//        final Internalizer<Base40> i = INTERN;
//        if (i == null) {
//            throw new IllegalStateException("No Internalizer registered");
//        }
//        return i.intern(base40);
//    }

    ///////////////////////
    // Instance methods. //
    ///////////////////////

    /** Constructor. Accepts any value. */
    public Base40(final CharacterSet theCharacterSet, final long value) {
        super(theCharacterSet, value);
    }

    /** Constructor. Only accepts valid names. */
    public Base40(final CharacterSet theCharacterSet, final String name) {
        super(theCharacterSet, name);
    }

//
//    /** Statically cache a Base40 instance. */
//    public Base40 intern() {
//        return intern(this);
//    }

    // TODO Move to SystemUtils
    /** Returns a BigInteger equivalent to the unsigned version of the long. */
    public static BigInteger toUnsigned(final long value) {
        if (value == 0L) {
            return BigInteger.ZERO;
        }
        if (value > 0L) {
            return new BigInteger(String.valueOf(value));
        }
        return new BigInteger(String.valueOf(value & ~(1L << 63))).setBit(63);
    }

    /** To simplify things, we need to stick to one single character set. */
    public static CharacterSet getDefaultCharacterSet() {
        return CharacterSet.newLowerDefaultCharacterSet();
    }
}
