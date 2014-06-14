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
 * <code>IntConverter</code> implements the conversion of some object type,
 * to and from Java primitive int values.
 */
public interface IntConverter<CONTEXT, E> extends Converter<E> {
    /** The default bits. */
    int DEFAULT_BITS = 32;

    /** The default IntConverter<E>. */
    IntConverter<?, Integer> DEFAULT = new IntConverter<Object, Integer>() {
        @Override
        public Class<Integer> type() {
            return Integer.class;
        }

        @Override
        public int bits() {
            return DEFAULT_BITS;
        }

        @Override
        public int fromObject(final Object context, final Integer obj) {
            return (obj == null) ? 0 : obj;
        }

        @Override
        public Integer toObject(final Object context, final int value) {
            return value;
        }
    };

    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    int fromObject(CONTEXT context, final E obj);

    /** Converts to an object instance. */
    E toObject(CONTEXT context, final int value);
}
