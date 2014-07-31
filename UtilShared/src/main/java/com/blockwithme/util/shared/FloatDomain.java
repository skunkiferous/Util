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
 * A Domain implementation for Floats.
 *
 * @author monster
 */
public final class FloatDomain implements Domain<Float> {
    /**
     * If we want to use Float.intBitsToFloat() and Float.floatToIntBits(),
     * 0 is the only cacheable value.
     */
    private static final Float ZERO = 0f;

    /**
     * Creates the ShortDomain.
     */
    FloatDomain() {
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<Float> getType() {
        return Float.class;
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
    public int getID(final Float value) {
        if (value == null) {
            // Integer.MAX_VALUE actually does happen to be an "invalid"
            // bit-pattern for a float.
            return Integer.MAX_VALUE;
        }
        return Float.floatToIntBits(value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public Float getValue(final int id) {
        if (id == 0f) {
            return ZERO;
        }
        // Integer.MAX_VALUE actually does happen to be an "invalid"
        // bit-pattern for a float.
        if (id == Integer.MAX_VALUE) {
            return null;
        }
        return Float.intBitsToFloat(id);
    }
}
