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

import com.blockwithme.util.shared.converters.BooleanConverter;
import com.blockwithme.util.shared.converters.ByteConverter;
import com.blockwithme.util.shared.converters.CharConverter;
import com.blockwithme.util.shared.converters.Converter;
import com.blockwithme.util.shared.converters.DoubleConverter;
import com.blockwithme.util.shared.converters.FloatConverter;
import com.blockwithme.util.shared.converters.IntConverter;
import com.blockwithme.util.shared.converters.LongConverter;
import com.blockwithme.util.shared.converters.ShortConverter;
import com.blockwithme.util.shared.converters.StringConverter;

/**
 * <code>ConverterRegistry</code> is a registry for primitive converters.
 *
 * It is thread-safe, and can delegate/fallback to another registry.
 */
public class ConverterRegistry {
    /** The singleton */
    private static final ConverterRegistry GLOBAL = new ConverterRegistry();

    /** Registered converters. */
    private final RegistryImpl<Class<?>, Object> registry;

    /** Returns the global registry. */
    public static final ConverterRegistry instance() {
        return GLOBAL;
    }

    /** Creates a new ConverterRegistry, without a parent. */
    public ConverterRegistry() {
        registry = new RegistryImpl<Class<?>, Object>(null);
    }

    /** Constructor, with optional parent. */
    public ConverterRegistry(final ConverterRegistry optionalParent) {
        final RegistryImpl<Class<?>, Object> parent = optionalParent == null ? null
                : optionalParent.registry;
        registry = new RegistryImpl<Class<?>, Object>(parent);
    }

    /** Returns a registered converter, if any. */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> find(final Class<E> type) {
        return (Converter<E>) registry.find(type);
    }

    /**
     * Registers a boolean converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final BooleanConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a byte converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final ByteConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a char converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final CharConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a short converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final ShortConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a int converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final IntConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a long converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final LongConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a float converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final FloatConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a double converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final DoubleConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }

    /**
     * Registers a String converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<E> register(final StringConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<E>) registry.register(type, converter, true);
    }
}