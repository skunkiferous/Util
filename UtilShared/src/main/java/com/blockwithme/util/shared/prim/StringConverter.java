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

package com.blockwithme.util.shared.prim;

/**
 * <code>StringConverter</code> implements the conversion of some object type,
 * to and from Java String values.
 *
 * OK, String is not a primitive type, but it's still one that is normally
 * supported in most serialization APIs.
 */
public interface StringConverter<E> extends Converter<E> {
    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    String fromObject(final E obj);

    /** Converts to an object instance. */
    E toObject(final String value);
}
