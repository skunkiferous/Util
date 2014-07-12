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
 * <code>StringConverter</code> implements the conversion of some object type,
 * to and from Java String values.
 *
 * OK, String is not a primitive type, but it's still one that is normally
 * supported in most serialization APIs.
 */
public interface StringConverter<CONTEXT, E> extends Converter<CONTEXT, E> {
    /** The default bits. */
    int DEFAULT_BITS = -1;

    /** The default StringConverter<E>. */
    StringConverter<?, String> DEFAULT = new StringConverterBase<Object, String>(
            String.class) {
        @Override
        public String fromObject(final Object context, final String obj) {
            return obj;
        }

        @Override
        public String toObject(final Object context, final String value) {
            return value;
        }
    };

    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    String fromObject(CONTEXT context, final E obj);

    /** Converts to an object instance. */
    E toObject(CONTEXT context, final String value);
}
