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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.blockwithme.util.base.SystemUtils;

/**
 * A generic implementation of Domain.
 *
 * @author monster
 */
public final class GenericLazyDomain<E> implements Domain<E> {
    /** 0 */
    private static final Integer ZERO = 0;

    /** The type of the Domain. */
    private final Class<E> type;

    /** If true, the "value.getClass() == type" is required, otherwise "value instanceof type" is required. */
    private final boolean exactType;

    /** Non-null, if null values are supported. */
    private final Integer nullID;

    /** The lazy mapping of IDs to values. */
    private final ArrayList<E> id2Value = new ArrayList<>();

    /** The lazy mapping of values to IDs. */
    private final HashMap<E, Integer> value2ID = new HashMap<>();

    /**
     * Creates a GenericLazyDomain.
     * @param type cannot be null, and must match the type of the objects.
     * @param exactType If true, the "value.getClass() == type" is required, otherwise "value instanceof type" is required.
     * @param nullID can be null. If not null, then null values are supported, and use the given ID.
     */
    public GenericLazyDomain(final Class<E> type, final boolean exactType,
            final Integer nullID) {
        this.type = Objects.requireNonNull(type, "type");
        this.exactType = exactType;
        this.nullID = nullID;
        if (nullID != null) {
            final int value = nullID.intValue();
            if ((value != -1) && (value != 0) && (value != Integer.MAX_VALUE)) {
                throw new IllegalArgumentException(
                        "nullID must be either -1, 0 or Integer.MAX_VALUE");
            }
        }
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
        return exactType;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#supportNull()
     */
    @Override
    public boolean supportsNull() {
        return nullID != null;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getID(java.lang.Object)
     */
    @Override
    public int getID(final E value) {
        if (value == null) {
            // Intentional NullPointerException
            return nullID.intValue();
        }
        final Class<?> valueType = value.getClass();
        if (!type.equals(valueType)) {
            if (exactType || !SystemUtils.isAssignableFrom(type, valueType)) {
                throw new IllegalArgumentException("Expecting " + type
                        + " but got " + valueType);
            }
        }
        synchronized (id2Value) {
            Integer result = value2ID.get(value);
            if (result == null) {
                int id = id2Value.size();
                if (ZERO.equals(nullID)) {
                    id++;
                }
                result = id;
                id2Value.set(id, value);
                value2ID.put(value, result);
            }
            return result;
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public E getValue(final int id) {
        if ((nullID != null) && (id == nullID.intValue())) {
            return null;
        }
        synchronized (id2Value) {
            if (ZERO.equals(nullID)) {
                return id2Value.get(id - 1);
            }
            return id2Value.get(id);
        }
    }
}
