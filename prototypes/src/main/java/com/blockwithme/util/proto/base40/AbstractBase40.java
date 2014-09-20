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

/**
 * <code>AbstractBase40</code> represents an abstract base class for 64bit
 * non-negative base-40 values.
 *
 * For a description of the base-40 encoding, see <code>Base40</code>.
 */
public abstract class AbstractBase40<E extends AbstractBase40<E>> extends
        AbstractLightweightBase40<E> implements CharSequence {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** The base-40 character set. */
    private final CharacterSet characterSet;

    /** The variable-length textual form. */
    private transient String name;

    /** The "capitalized" textual form. */
    private transient String capitalizedName;

    /** The fixed-length textual form. */
    private transient String fixedName;

    /** Constructor. Accepts any value. */
    protected AbstractBase40(final CharacterSet theCharacterSet,
            final long value) {
        super(value);
        characterSet = theCharacterSet;
    }

    /** Constructor. Only accepts valid names. */
    protected AbstractBase40(final CharacterSet theCharacterSet,
            final String name) {
        super(theCharacterSet.toLong(name));
        characterSet = theCharacterSet;
    }

    /**
     * Returns the base-40 character set.
     * @return the characterSet
     */
    @Override
    public final CharacterSet getCharacterSet() {
        return characterSet;
    }

    /**
     * Returns the fixed-size String representation.
     */
    @Override
    public final String fixedName() {
        if (fixedName == null) {
            fixedName = super.fixedName();
        }
        return fixedName;
    }

    /**
     * Returns the variable-length capitalized String representation.
     */
    @Override
    public final String capitalizedName() {
        if (capitalizedName == null) {
            capitalizedName = super.capitalizedName();
        }
        return capitalizedName;
    }

    /**
     * Returns the variable-length String representation.
     */
    @Override
    public final String name() {
        if (name == null) {
            name = super.name();
        }
        return name;
    }

    @Override
    public final int length() {
        return name().length();
    }

    @Override
    public final char charAt(final int index) {
        return name().charAt(index);
    }

    @Override
    public final CharSequence subSequence(final int start, final int end) {
        return name().subSequence(start, end);
    }
}
