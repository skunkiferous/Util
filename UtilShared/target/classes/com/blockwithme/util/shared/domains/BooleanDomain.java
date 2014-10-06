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
 * A Domain implementation for Booleans.
 *
 * @author monster
 */
public final class BooleanDomain implements Domain<Boolean> {
    /**
     * Creates the BooleanDomain.
     */
    BooleanDomain() {
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getType()
     */
    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
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
    public int getID(final Boolean value) {
        if (value == null) {
            return Integer.MAX_VALUE;
        }
        return value ? 1 : 0;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.Domain#getValue(int)
     */
    @Override
    public Boolean getValue(final int id) {
        if (id == Integer.MAX_VALUE) {
            return null;
        }
        if (id == 0) {
            return Boolean.FALSE;
        }
        if (id == 1) {
            return Boolean.TRUE;
        }
        throw new IllegalArgumentException(String.valueOf(id));
    }
}
