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

import com.blockwithme.util.shared.ConverterRegistry;
import com.blockwithme.util.shared.converters.ConfiguredConverter;
import com.blockwithme.util.shared.converters.LongConverter;

/**
 * <code>Base40Converter</code> converts Base40 to and from Java long.
 */
public class Base40Converter implements LongConverter<Object, Base40>,
        ConfiguredConverter<Object, Base40> {

    /** Marker character for lower-case character set. */
    private static final char LOWER = 'l';

    /** Marker character for upper-case character set. */
    private static final char UPPER = 'U';

    /** The character set. */
    private final CharacterSet characterSet;

    /** Converts the configuration string to a character set. */
    private static CharacterSet toCharSet(final String theCharacterSet) {
        // (characterSet.lower ? LOWER : UPPER) + characterSet.characters;
        if ((theCharacterSet != null) && (theCharacterSet.length() == 5)) {
            final char first = theCharacterSet.charAt(0);
            final String last4Characters = theCharacterSet.substring(1);
            if (first == LOWER) {
                return new CharacterSet(true, last4Characters);
            }
            if (first == UPPER) {
                return new CharacterSet(false, last4Characters);
            }
        }
        throw new IllegalArgumentException("Invalid character set encoding: "
                + theCharacterSet);
    }

    /** Creates a Base40Converter with the given Base40 CharacterSet. */
    public Base40Converter(final CharacterSet theCharacterSet) {
        characterSet = theCharacterSet;
    }

    /**
     * Creates a Base40Converter with the given Base40 CharacterSet.
     * The string is the encoded form of the character-set,
     * as returned by getConfiguration().
     */
    public Base40Converter(final String theCharacterSet) {
        characterSet = toCharSet(theCharacterSet);
    }

    /** Creates a Base40Converter with the Base40 default CharacterSet. */
    public Base40Converter() {
        this(Base40.getDefaultCharacterSet());
    }

    /** Registers this converter globally. */
    public static void register() {
        ConverterRegistry.instance().register(new Base40Converter(),
                Base40.class);
    }

    @Override
    public long fromObject(final Object context, final Base40 obj) {
        return obj == null ? 0L : obj.asLong();
    }

    @Override
    public Base40 toObject(final Object context, final long value) {
        return new Base40(characterSet, value);
    }

    /** {@inheritDoc} */
    @Override
    public Class<Base40> type() {
        return Base40.class;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.prim.Converter#bits()
     */
    @Override
    public int bits() {
        return 64;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.prim.ConfiguredConverter#getConfiguration()
     */
    @Override
    public String getConfiguration() {
        return (characterSet.lower ? LOWER : UPPER) + characterSet.characters;
    }
}
