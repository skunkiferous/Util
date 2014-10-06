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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.blockwithme.util.shared.Any;
import com.blockwithme.util.shared.AnyArray;

/**
 * The Base Interface for all the converter interfaces.
 */
public interface Converter<CONTEXT, E> {

    final class Helper {
        private static Map<String, Converter<?, ?>> init() {
            final HashMap<String, Converter<?, ?>> result = new HashMap<String, Converter<?, ?>>();
            result.put(BooleanConverter.DEFAULT.getClass().getName(),
                    BooleanConverter.DEFAULT);
            result.put(ByteConverter.DEFAULT.getClass().getName(),
                    ByteConverter.DEFAULT);
            result.put(CharConverter.DEFAULT.getClass().getName(),
                    CharConverter.DEFAULT);
            result.put(IntConverter.DEFAULT.getClass().getName(),
                    IntConverter.DEFAULT);
            result.put(LongConverter.DEFAULT.getClass().getName(),
                    LongConverter.DEFAULT);
            result.put(ShortConverter.DEFAULT.getClass().getName(),
                    ShortConverter.DEFAULT);
            result.put(FloatConverter.DEFAULT.getClass().getName(),
                    FloatConverter.DEFAULT);
            result.put(DoubleConverter.DEFAULT.getClass().getName(),
                    DoubleConverter.DEFAULT);
            for (@SuppressWarnings("rawtypes")
            final Converter c : Converters.ALL) {
                result.put(c.getClass().getName(), c);
            }
            return Collections.unmodifiableMap(result);
        }
    }

    /** Maps class names of default converters to instances. */
    Map<String, Converter<?, ?>> DEFAULTS = Helper.init();

    /**
     * The type of Object being converted.
     *
     * @return the class (type) of the Object that is converted by this Converter interface.
     */
    Class<E> type();

    /**
     * Returns the number of bits required to store the object data.
     *
     * Use the primitive type size, if unsure/variable.
     * Use 0 or -1 if not a primitive type converter.
     */
    int bits();

    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    void objectToAny(CONTEXT context, final E obj, final Any any);

    /** Converts to an object instance. */
    E anyToObject(CONTEXT context, final Any any);

    /**
     * Converts from object instance.
     *
     * The expected behavior when receiving null is left on purpose unspecified,
     * as it depends on your application needs.
     */
    void objectToAny(CONTEXT context, final E obj, final AnyArray anyArray,
            final int index);

    /** Converts to an object instance. */
    E anyToObject(CONTEXT context, final AnyArray anyArray, final int index);

}
