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

package com.blockwithme.util.proto.base40;

import java.io.Serializable;

import com.blockwithme.util.shared.AsLong;

/**
 * <code>AbstractLightweightBase40</code> represents an abstract base class for
 * 64bit non-negative base-40 values.
 *
 * For a description of the base-40 encoding, see <code>Base40</code>.
 */
public abstract class AbstractLightweightBase40<E extends AbstractLightweightBase40<E>>
        implements Serializable, Comparable<E>, AsLong {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** The base-40 value */
    private final long base40;

    /** Constructor. Accepts any value. */
    protected AbstractLightweightBase40(final long value) {
        base40 = value;
    }

    /* (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object) */
    @Override
    public int compareTo(final E o) {
        if (o == null) {
            return 1;
        }
        final AbstractLightweightBase40<?> other = o;
        return name().compareTo(other.name());
    }

    @Override
    public boolean equals(final Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final AbstractLightweightBase40<?> other = (AbstractLightweightBase40<?>) obj;
        return (base40 == other.base40);
    }

    @Override
    public int hashCode() {
        return (31 + (int) (base40 ^ (base40 >>> 32)));
    }

    /**
     * Returns the variable-length String representation.
     */
    @Override
    public String toString() {
        return name();
    }

    /**
     * Returns the fixed-size String representation.
     */
    public String fixedName() {
        return getCharacterSet().toString(base40, true, false);
    }

    /**
     * Returns the variable-length capitalized String representation.
     */
    public String capitalizedName() {
        return getCharacterSet().toString(base40, false, true);
    }

    /**
     * Returns the variable-length String representation.
     */
    public String name() {
        return getCharacterSet().toString(base40, false, false);
    }

    /** Returns the base-40 value */
    @Override
    public final long asLong() {
        return base40;
    }

    /**
     * Returns the base-40 character set.
     * @return the characterSet
     */
    public abstract CharacterSet getCharacterSet();
}
