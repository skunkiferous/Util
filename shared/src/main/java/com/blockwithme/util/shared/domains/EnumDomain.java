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
package com.blockwithme.util.shared.domains;

import java.util.Objects;

import com.blockwithme.util.base.SystemUtils;

/**
 * A implementation of Domain for Enums.
 *
 * @author monster
 */
public final class EnumDomain<E extends Enum<E>> implements Domain<E> {
    /** The type of the Domain. */
    private final Class<E> type;

    /** All Enum constants. */
    private final E[] constants;

    /**
     * Creates a GenericLazyDomain.
     * @param type cannot be null, and must match the type of the objects.
     * @param exactType If true, the "value.getClass() == type" is required, otherwise "value instanceof type" is required.
     * @param nullID can be null. If not null, then null values are supported, and use the given ID.
     */
    public EnumDomain(final Class<E> type, final boolean exactType,
            final Integer nullID) {
        this.type = Objects.requireNonNull(type, "type");
        if (!SystemUtils.isAssignableFrom(type, Enum.class)) {
            throw new IllegalArgumentException(
                    "Expecting and Enum subclass but got " + type);
        }
        constants = type.getEnumConstants();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<E> getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#exactType()
     */
    @Override
    public boolean exactType() {
        // In many Enums, special subclasses are created for specific instances of that enum
        return false;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#supportNull()
     */
    @Override
    public boolean supportsNull() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getID(java.lang.Object)
     */
    @Override
    public int getID(final E value) {
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        // We do not check the input type, to get a better speed
        return value.ordinal();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public E getValue(final int id) {
        // Intentional Array-out-of-bounds exception
        return (id == Integer.MAX_VALUE) ? null : constants[id];
    }
}
