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

import com.blockwithme.util.base.SystemUtils;

/**
 * A bunch of default converters.
 *
 * TODO: Date, Calendar, AtomicBoolean, AtomicInteger, AtomicLong, BigDecimal,
 * BigInteger, ...
 *
 * @author monster
 */
public interface Converters {

    /** The default StringConverter<E>. */
    @SuppressWarnings("rawtypes")
    StringConverter<?, Class> CLASS = new StringConverterBase<Object, Class>(
            Class.class) {
        @Override
        public String fromObject(final Object context, final Class obj) {
            return (obj == null) ? "" : obj.getName();
        }

        @Override
        public Class toObject(final Object context, final String value) {
            if ((value == null) || value.isEmpty()) {
                return null;
            }
            return SystemUtils.forName(value);
        }
    };

    @SuppressWarnings("rawtypes")
    Converter[] ALL = new Converter[] { CLASS };
}
