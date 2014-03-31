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

package com.blockwithme.util.server.base40;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A Base40 character set is used to convert Base-40 values to and from String.
 *
 * @author monster
 */
public final class CharacterSet implements Serializable,
        Comparable<CharacterSet> {

    /** serialVersionUID */
    private static final long serialVersionUID = 8720467617946506243L;

    /** The base-40 radix */
    public static final long RADIX = 40;

    /** The base-40 radix maximal length. */
    public static final int MAX_LEN = 13;

    /** The lower-case characters */
    public static final String LOWER = "0123456789abcdefghijklmnopqrstuvwxyz";

    /** The upper-case characters */
    public static final String UPPER = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** The default character set final 4 characters. */
    public static final String LAST_4 = "_-.'";

    /**
     * The "identifier" character set final 4 characters.
     * Note that the second character is 0xAD, not 0x2D (normal minus sign).
     */
    public static final String LAST_4_ID = "_­$¢";

    /** The "lower" "default" character set. */
    private static final String LOWER_DEFAULT_CHAR_SET = computeCharacterSet(
            true, LAST_4);

    /** The "upper" "default" character set. */
    private static final String UPPER_DEFAULT_CHAR_SET = computeCharacterSet(
            false, LAST_4);

    /** The "lower" "identifier" character set. */
    private static final String LOWER_ID_CHAR_SET = computeCharacterSet(true,
            LAST_4_ID);

    /** The "upper" "identifier" character set. */
    private static final String UPPER_ID_CHAR_SET = computeCharacterSet(false,
            LAST_4_ID);

    /** The character set, as String */
    public final String characters;

    /** The character set, as char[] */
    private transient final char[] chars;

    /** Is the character set lower-case? */
    public transient final boolean lower;

    /** The character set at 36 */
    public transient final char c36;

    /** The character set at 37 */
    public transient final char c37;

    /** The character set at 38 */
    public transient final char c38;

    /** The character set at 39 */
    public transient final char c39;

    /**
     * Creates the character set.
     *
     * @param lowerCase specifies if lower-case, or upper-case letters will be used.
     * @param last4Characters the last 4 characters, forming the complete base-40 character set.
     * @throws java.lang.NullPointerException if last4Characters is null.
     * @throws java.lang.IllegalArgumentException if we don't like what's in last4Characters ...
     * @throws java.lang.IllegalStateException if the character set was already set.
     * @throws java.lang.IllegalStateException if the character contains Path.SEP.
     */
    private static String computeCharacterSet(final boolean lowerCase,
            final String last4Characters) {
        if (last4Characters == null) {
            throw new NullPointerException("last4Characters");
        }
        if (last4Characters.length() != 4) {
            throw new IllegalArgumentException(
                    "last4Characters.length() must be 4, but is "
                            + last4Characters.length());
        }
        final String chosen = (lowerCase ? LOWER : UPPER);
        for (int i = 0; i < 4; i++) {
            final int c = last4Characters.codePointAt(i);
            if ((c < 0) || (c > 255)) {
                throw new IllegalArgumentException(
                        "last4Characters characters must be in the range [0,255]");
            }
            if (chosen.indexOf(c) >= 0) {
                throw new IllegalArgumentException(
                        "last4Characters characters must not be in the selected character set: "
                                + chosen);
            }
        }
        if (last4Characters.contains(String.valueOf(Path.SEP))) {
            throw new IllegalArgumentException(
                    "last4Characters characters must not contian: " + Path.SEP);
        }
        return (chosen + last4Characters).intern();
    }

    /**
     * Creates the character set.
     *
     * @param lowerCase specifies if lower-case, or upper-case letters will be used.
     * @param last4Characters the last 4 characters, forming the complete base-40 character set.
     * @throws java.lang.NullPointerException if last4Characters is null.
     * @throws java.lang.IllegalArgumentException if we don't like what's in last4Characters ...
     * @throws java.lang.IllegalStateException if the character set was already set.
     * @throws java.lang.IllegalStateException if the character contains Path.SEP.
     */
    public CharacterSet(final boolean lowerCase, final String last4Characters) {
        this(computeCharacterSet(lowerCase, last4Characters));
    }

    /** Creates a character set from a String. */
    private CharacterSet(final String theCharacters) {
        characters = theCharacters;
        chars = characters.toCharArray();
        lower = (chars[10] == 'a');
        c36 = chars[36];
        c37 = chars[37];
        c38 = chars[38];
        c39 = chars[39];
    }

    /** toString() */
    @Override
    public String toString() {
        return characters;
    }

    /** hashCode() */
    @Override
    public int hashCode() {
        return characters.hashCode();
    }

    /** equals() */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CharacterSet)) {
            return false;
        }
        final CharacterSet other = (CharacterSet) obj;
        return characters.equals(other.characters);
    }

    /** compareTo() */
    @Override
    public int compareTo(final CharacterSet other) {
        if (other == null) {
            return 1;
        }
        return characters.compareTo(other.characters);
    }

    /** readResolve() */
    private Object readResolve() throws ObjectStreamException {
        return new CharacterSet(characters.intern());
    }

    /** Returns the long representation of this base-40 encoded String
     * (non-case-sensitive). */
    public long toLong(final String value) {
        final int len = value.length();
        if (len > MAX_LEN) {
            throw new IllegalArgumentException("Maximum length is: " + MAX_LEN
                    + " value: \"" + value + "\"");
        }
        final String cased = lower ? value.toLowerCase() : value.toUpperCase();
        final char[] chars = cased.toCharArray();
        long v = 0;
        for (int i = 0; i < chars.length; i++) {
            v = v * RADIX + indexOf(chars[i]);
        }
        return v;
    }

    /**
     * Returns the long representation of this base-40 encoded String
     * (non-case-sensitive). This function will replace any inappropriate
     * character with "CHARACTERS[36]", and will truncate the input string,
     * if needed.
     */
    public long toLongLenient(final String value) {
        final String cased = lower ? value.toLowerCase() : value.toUpperCase();
        final char[] chars = cased.toCharArray();
        final int len = Math.min(chars.length, MAX_LEN);
        long v = 0;
        for (int i = 0; i < len; i++) {
            int index = find(chars[i]);
            if (index == -1) {
                index = 36;
            }
            v = v * RADIX + index;
        }
        return v;
    }

    /**
     * Returns the base-40 char[] representation of the value, treated as an
     * unsigned long. If fixedSize is true, it will be MAX_LEN character long.
     * If capitalize is true, and the character set is lower-case, then the
     * first character, if it is a letter, and any letter afterward, if it
     * follows a non-letter.
     */
    public char[] toCharArray(long value, final boolean fixedSize,
            final boolean capitalize) {
        final char[] array = new char[MAX_LEN];
        int i = MAX_LEN - 1;
        if (value < 0) {
            int index = (int) (value % RADIX);
            if (index < 0) {
                index += RADIX;
            }

            // I don't understand why I need to do this:
            // (Probably I somehow compute the modulo wrong. Any help welcome here!)
            if (index >= 24) {
                index -= 24;
            } else {
                index += 16;
            }

            array[i--] = chars[index];
            // To turn a byte to the integer range, you add 256 (2^8 == 1 << 8),
            // so to do that with longs, you need to add (1 << 64) ...
            // value = (value + (1L << 64))/RADIX;
            // value = ((value + (1L << 64))/4)/(RADIX/4);
            // value = (value/4 + (1L << 64)/4)/(RADIX/4);
            value = ((value >> 2) + (1L << 62L)) / (RADIX / 4L);
        }
        while (value != 0) {
            final int index = (int) (value % RADIX);
            array[i--] = chars[index];
            value /= RADIX;
        }
        int len = MAX_LEN - i - 1;
        if (len == 0) {
            len = 1;
        }
        while (i >= 0) {
            array[i--] = '0';
        }
        final char[] result;
        if (fixedSize || (MAX_LEN == len)) {
            result = array;
        } else {
            result = Arrays.copyOfRange(array, MAX_LEN - len, MAX_LEN);
        }
        if (lower && capitalize) {
            boolean up = true;
            for (int j = 0; j < result.length; j++) {
                final char c = result[j];
                if (up && Character.isLowerCase(c)) {
                    result[j] = Character.toUpperCase(c);
                }
                up = !Character.isLetter(c);
            }
        }
        return result;
    }

    /** Returns the base-40 String representation of the value, treated as an
     * unsigned long. If fixedSize is true, it will be MAX_LEN character long. */
    public String toString(final long value, final boolean fixedSize,
            final boolean capitalize) {
        return new String(toCharArray(value, fixedSize, capitalize));
    }

    /**
     * Takes a character, and returns the position in CHARACTERS.
     *
     * @param c
     * @return the index, or -1 if not found.
     */
    public int find(final char c) {
        if (('0' <= c) && (c <= '9')) {
            return (c - '0');
        }
        if (lower) {
            if (('a' <= c) && (c <= 'z')) {
                return (c - ('a' - 10));
            }
        } else {
            if (('A' <= c) && (c <= 'Z')) {
                return (c - ('A' - 10));
            }
        }
        if (c == c36) {
            return 36;
        }
        if (c == c37) {
            return 37;
        }
        if (c == c38) {
            return 38;
        }
        if (c == c39) {
            return 39;
        }
        return -1;
    }

    /**
     * Takes a character, and returns the position in CHARACTERS.
     *
     * @param c
     * @return the index.
     * @throws java.lang.IllegalArgumentException if not found
     */
    public int indexOf(final char c) {
        final int result = find(c);
        if (result == -1) {
            throw new IllegalArgumentException("Invalid character (#"
                    + ((int) c) + ") " + c);
        }
        return result;
    }

    /** Returns the character set */
    public char[] getCharacterSet() {
        return chars.clone();
    }

    /** Returns a new instance of the "lower" "default" character set. */
    public static CharacterSet newLowerDefaultCharacterSet() {
        return new CharacterSet(LOWER_DEFAULT_CHAR_SET);
    }

    /** Returns a new instance of the "upper" "default" character set. */
    public static CharacterSet newUpperDefaultCharacterSet() {
        return new CharacterSet(UPPER_DEFAULT_CHAR_SET);
    }

    /** Returns a new instance of the "lower" "ID" character set. */
    public static CharacterSet newLowerIDCharacterSet() {
        return new CharacterSet(LOWER_ID_CHAR_SET);
    }

    /** Returns a new instance of the "upper" "ID" character set. */
    public static CharacterSet newUpperIDCharacterSet() {
        return new CharacterSet(UPPER_ID_CHAR_SET);
    }
}
