/*******************************************************************************
 * Copyright 2014 Sebastien Diot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.blockwithme.util.proto.base40;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.blockwithme.util.shared.prim.ClassConfiguredConverter;
import com.blockwithme.util.shared.prim.LongConverter;

/**
 * <code>EnumLongConverter</code> implements a LongConverter for some enum type.
 * It is assumed that all enum constant names can be converted to unique Base40 long.
 *
 * @author monster
 *
 * @param <E>
 */
public class EnumLongConverter<E extends Enum<E>> extends
        ClassConfiguredConverter<E, E> implements LongConverter<E> {

    /** The Long to Enum map. */
    private final Map<Long, E> mapLongToEnum = new HashMap<>();

    /** The Enum to Long map. */
    private final Map<E, Long> mapEnumToLong = new HashMap<>();

    /** The character set. */
    private final CharacterSet characterSet = Enum40.getDefaultCharacterSet();

    /** Initialize the maps. */
    private void init() {
        if (!type.isEnum()) {
            throw new IllegalArgumentException(type + " is not an Enum");
        }
        final E[] constants = type.getEnumConstants();
        final long[] longs = new long[constants.length];
        for (int i = 0; i < constants.length; i++) {
            final E e = constants[i];
            @SuppressWarnings("boxing")
            final Long l = longs[i] = characterSet.toLong(e.name());
            mapLongToEnum.put(l, e);
            mapEnumToLong.put(e, l);
        }
        Arrays.sort(longs);
        long last = longs[0];
        for (int i = 1; i < longs.length; i++) {
            final long l = longs[i];
            if (l == last) {
                throw new IllegalArgumentException(
                        type
                                + " has multiple constants that map to the same base40 values");
            }
            last = l;
        }
    }

    /** Constructor takes the enum type name. */
    public EnumLongConverter(final String theEnumType) {
        super(theEnumType);
        init();
    }

    /** Constructor takes the enum type. */
    public EnumLongConverter(final Class<E> theEnumType) {
        super(theEnumType);
        init();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("boxing")
    @Override
    public long fromObject(final E obj) {
        return mapEnumToLong.get(obj);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("boxing")
    @Override
    public E toObject(final long value) {
        return mapLongToEnum.get(value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.prim.Converter#bits()
     */
    @Override
    public int bits() {
        return 64;
    }
}
