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
 * A Domain implementation for Bytes.
 *
 * @author monster
 */
public final class ByteDomain implements Domain<Byte> {
    /**
     * Creates the ByteDomain.
     */
    ByteDomain() {
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<Byte> getType() {
        return Byte.class;
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
    public int getID(final Byte value) {
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        return value;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public Byte getValue(final int id) {
        if (id == Integer.MAX_VALUE) {
            return null;
        }
        if ((id >= Byte.MIN_VALUE) && (id <= Byte.MAX_VALUE)) {
            // All Byte instances are cached, so this is OK.
            return (byte) id;
        }
        throw new IllegalArgumentException(String.valueOf(id));
    }
}
