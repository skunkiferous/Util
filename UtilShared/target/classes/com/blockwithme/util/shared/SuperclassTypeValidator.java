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

package com.blockwithme.util.shared;

import com.blockwithme.util.base.SystemUtils;

/**
 * <code>SuperclassTypeValidator</code> validates that the values are not null
 * and of the given type, or a subtype.
 *
 * @author monster
 *
 * @param <E>
 */
public class SuperclassTypeValidator<E> implements Validator<E> {

    /** The expected type. */
    private final Class<E> type;

    /** Creates an SuperclassTypeValidator */
    public SuperclassTypeValidator(final Class<E> theType) {
        if (theType == null) {
            throw new NullPointerException("theType");
        }
        type = theType;
    }

    @Override
    public String validate(final E value, final Object name) {
        if (value == null) {
            return name + " is null";
        }
        if (!SystemUtils.isInstance(type, value)) {
            return name + " is a " + value.getClass() + " but a " + type
                    + " was expected";
        }
        return null;
    }
}
