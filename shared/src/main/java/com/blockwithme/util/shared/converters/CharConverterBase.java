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
package com.blockwithme.util.shared.converters;

import com.blockwithme.util.shared.Any;
import com.blockwithme.util.shared.AnyArray;

/**
 * Base class for CharConverter.
 *
 * @author monster
 */
public abstract class CharConverterBase<CONTEXT, E> extends
        ConverterBase<CONTEXT, E> implements CharConverter<CONTEXT, E> {

    /**
     * Creates a CharConverterBase.
     *
     * @param theType
     * @param theBits
     */
    protected CharConverterBase(final Class<E> theType) {
        super(theType, DEFAULT_BITS);
    }

    /**
     * Creates a CharConverterBase.
     *
     * @param theType
     * @param theBits
     */
    protected CharConverterBase(final Class<E> theType, final int theBits) {
        super(theType, theBits);
    }

    /** {@inheritDoc} */
    @Override
    public final void objectToAny(final CONTEXT context, final E obj,
            final Any any) {
        any.setChar(fromObject(context, obj));
    }

    /** {@inheritDoc} */
    @Override
    public final E anyToObject(final CONTEXT context, final Any any) {
        return toObject(context, any.getChar());
    }

    /** {@inheritDoc} */
    @Override
    public final void objectToAny(final CONTEXT context, final E obj,
            final AnyArray anyArray, final int index) {
        anyArray.setChar(index, fromObject(context, obj));
    }

    /** {@inheritDoc} */
    @Override
    public final E anyToObject(final CONTEXT context, final AnyArray anyArray,
            final int index) {
        return toObject(context, anyArray.getChar(index));
    }
}
