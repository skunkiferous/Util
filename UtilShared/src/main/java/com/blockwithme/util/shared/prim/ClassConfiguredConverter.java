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
package com.blockwithme.util.shared.prim;

import com.blockwithme.util.base.SystemUtils;

/**
 * <code>EnumByteConverter</code> implements a ByteConverter for some enum type.
 * It is assumed that there are no more then 256 values for this enum.
 *
 * @author monster
 *
 * @param <E>
 */
public abstract class ClassConfiguredConverter<E, C> implements
        ConfiguredConverter<E> {

    /** The type. */
    protected final Class<C> type;

    /** Constructor takes the type. */
    protected ClassConfiguredConverter(final Class<C> theType) {
        if (theType == null) {
            throw new IllegalArgumentException(theType + " is null");
        }
        type = theType;
    }

    /** Constructor takes the type. */
    @SuppressWarnings("unchecked")
    protected ClassConfiguredConverter(final String theType) {
        if (theType == null) {
            throw new IllegalArgumentException(theType + " is null");
        }
        type = (Class<C>) SystemUtils.forName(theType);
    }

    /** {@inheritDoc} */
    @Override
    public String getConfiguration() {
        // TODO Using simple class names in unsafe in OSGi
        return type.getName();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Class<E> type() {
        // Assumes the type is the class given as parameter
        return (Class<E>) type;
    }
}
