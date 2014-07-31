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
package com.blockwithme.util.shared;

/**
 * A Domain implementation for Integers.
 *
 * @author monster
 */
public final class IntegerDomain implements Domain<Integer> {
    private static final Integer[] CACHE = new Integer[Domains.CACHE_SIZE];
    static {
        for (int i = 0; i < Domains.CACHE_SIZE; i++) {
            CACHE[i] = i;
        }
    }

    /**
     * Creates the IntDomain.
     */
    IntegerDomain() {
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<Integer> getType() {
        return Integer.class;
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
        return false;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getID(java.lang.Object)
     */
    @Override
    public int getID(final Integer value) {
        // Intentional NullPointerException
        return value;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public Integer getValue(final int id) {
        if ((id >= 0) && (id < Domains.CACHE_SIZE)) {
            return CACHE[id];
        }
        return id;
    }
}
