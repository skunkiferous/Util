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

import java.util.EnumSet;

import com.blockwithme.util.base.SystemUtils;

/**
 * <code>EnumSetConverter</code> implements a LongConverter for some enum type.
 * It is assumed that there are no more then 64 values for this enum.
 *
 * @author monster
 *
 * @param <E>
 */
public class EnumSetConverter<CONTEXT, E extends Enum<E>> extends
        LongConverterBase<CONTEXT, EnumSet<E>> implements
        ConfiguredConverter<CONTEXT, EnumSet<E>> {

    /** Real enum type. */
    private final Class<E> enumType;

    /** The Enum constants. */
    private final E[] constants;

    /** Constructor takes the enum type. */
    @SuppressWarnings("unchecked")
    public EnumSetConverter(final Class<E> theEnumType) {
        super((Class<EnumSet<E>>) EnumSet.noneOf(theEnumType).getClass());
        // theEnumType must be a valid enum type if we get here.
        enumType = theEnumType;
        constants = theEnumType.getEnumConstants();
        if (constants.length > 64) {
            throw new IllegalArgumentException(type + " has too many constants");
        }
    }

    /** Constructor takes the enum type name. */
    @SuppressWarnings("unchecked")
    public EnumSetConverter(final String theEnumType) {
        this((Class<E>) SystemUtils.forName(theEnumType));
    }

    @Override
    public long fromObject(final CONTEXT context, final EnumSet<E> theSet) {
        long result = 0;
        if ((theSet != null) && !theSet.isEmpty()) {
            boolean check = true;
            for (final E e : theSet) {
                if (check) {
                    check = false;
                    if (!enumType.equals(e.getDeclaringClass())) {
                        throw new IllegalArgumentException("Wrong enum type: "
                                + e.getDeclaringClass() + " should be "
                                + enumType);
                    }
                }
                // TODO ordinal() is evil, and should never be used for serialization
                result |= 1L << e.ordinal();
            }
        }
        return result;
    }

    @Override
    public final EnumSet<E> toObject(final CONTEXT context, final long theValue) {
        final EnumSet<E> result = EnumSet.noneOf(enumType);
        for (int i = 0; i < constants.length; i++) {
            if ((theValue & 1L << i) != 0) {
                result.add(constants[i]);
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getConfiguration() {
        return enumType.getName();
    }
}
