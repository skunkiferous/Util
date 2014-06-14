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

/**
 * <code>BooleanConverter</code> implements the conversion of some object type,
 * to and from Java primitive boolean values.
 */
public interface BooleanConverter<CONTEXT, E> extends Converter<E> {
    /** The default bits. */
    int DEFAULT_BITS = 1;

    /** The default BooleanConverter<E>. */
    BooleanConverter<?, Boolean> DEFAULT = new BooleanConverter<Object, Boolean>() {
        @Override
        public Class<Boolean> type() {
            return Boolean.class;
        }

        @Override
        public int bits() {
            return DEFAULT_BITS;
        }

        @Override
        public boolean fromObject(final Object context, final Boolean obj) {
            return (obj == null) ? Boolean.FALSE : obj;
        }

        @Override
        public Boolean toObject(final Object context, final boolean value) {
            return value;
        }
    };

    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    boolean fromObject(CONTEXT context, final E obj);

    /** Converts to an object instance. */
    E toObject(CONTEXT context, final boolean value);
}
