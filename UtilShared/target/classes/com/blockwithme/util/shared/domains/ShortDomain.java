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

/**
 * A Domain implementation for Shorts.
 *
 * @author monster
 */
public final class ShortDomain implements Domain<Short> {
    private static final Short[] CACHE = new Short[Domains.CACHE_SIZE];
    static {
        for (int i = 0; i < Domains.CACHE_SIZE; i++) {
            CACHE[i] = (short) i;
        }
    }

    /**
     * Creates the ShortDomain.
     */
    ShortDomain() {
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<Short> getType() {
        return Short.class;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#exactType()
     */
    @Override
    public boolean exactType() {
        return true;
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
    public int getID(final Short value) {
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        return value;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public Short getValue(final int id) {
        if (id == Integer.MAX_VALUE) {
            return null;
        }
        if ((id >= 0) && (id < Domains.CACHE_SIZE)) {
            return CACHE[id];
        }
        if ((id >= Short.MIN_VALUE) && (id <= Short.MIN_VALUE)) {
            return (short) id;
        }
        throw new IllegalArgumentException(String.valueOf(id));
    }
}
