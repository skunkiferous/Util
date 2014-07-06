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

/**
 * <code>EnumStringConverter</code> implements a StringConverter for some enum type.
 *
 * @author monster
 *
 * @param <E>
 */
public class EnumStringConverter<E extends Enum<E>> extends
        ClassConfiguredConverter<Object, E, E> implements
        StringConverter<Object, E> {

    /** Constructor takes the enum type. */
    public EnumStringConverter(final Class<E> theEnumType) {
        super(theEnumType);
        if (!theEnumType.isEnum()) {
            throw new IllegalArgumentException(theEnumType + " is not an Enum");
        }
    }

    /** Constructor takes the enum type name. */
    public EnumStringConverter(final String theEnumType) {
        super(theEnumType);
        if (!type.isEnum()) {
            throw new IllegalArgumentException(theEnumType + " is not an Enum");
        }
    }

    @Override
    public String fromObject(final Object context, final E obj) {
        return obj.name();
    }

    @Override
    public E toObject(final Object context, final String value) {
        return Enum.valueOf(type, value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.prim.Converter#bits()
     */
    @Override
    public int bits() {
        return 0;
    }
}
