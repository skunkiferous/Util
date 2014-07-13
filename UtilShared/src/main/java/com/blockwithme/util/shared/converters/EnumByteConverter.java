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
package com.blockwithme.util.shared.converters;

import com.blockwithme.util.base.SystemUtils;

/**
 * <code>EnumByteConverter</code> implements a ByteConverter for some enum type.
 * It is assumed that there are no more then 256 values for this enum.
 *
 * @author monster
 *
 * @param <E>
 */
public class EnumByteConverter<CONTEXT, E extends Enum<E>> extends
        ByteConverterBase<CONTEXT, E> implements
        ConfiguredConverter<CONTEXT, E> {

    /** The Enum constants. */
    private final E[] constants;

    /** Initialize */
    private E[] init() {
        if (!type.isEnum()) {
            throw new IllegalArgumentException(type + " is not an Enum");
        }
        final E[] result = type.getEnumConstants();
        if (result.length > 256) {
            throw new IllegalArgumentException(type + " has too many constants");
        }
        return result;
    }

    /** Constructor takes the enum type. */
    public EnumByteConverter(final Class<E> theEnumType) {
        super(theEnumType);
        constants = init();
    }

    /** Constructor takes the enum type name. */
    @SuppressWarnings("unchecked")
    public EnumByteConverter(final String theEnumType) {
        super((Class<E>) SystemUtils.forName(theEnumType));
        constants = init();
    }

    @Override
    public byte fromObject(final CONTEXT context, final E obj) {
        return (byte) obj.ordinal();
    }

    @Override
    public E toObject(final CONTEXT context, final byte value) {
        final int ordinal = value & 0xFF;
        return constants[ordinal];
    }

    /** {@inheritDoc} */
    @Override
    public String getConfiguration() {
        return type.getName();
    }
}
