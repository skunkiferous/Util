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

import com.blockwithme.util.shared.RegistryImpl;

/**
 * <code>ConverterRegistry</code> is a registry for primitive converters.
 *
 * Converters must match the type exactly, because otherwise the "toObject()"
 * conversion would not usually return the correct type.
 *
 * It is thread-safe, and can delegate/fallback to another registry.
 */
public class ConverterRegistry {
    /** The singleton */
    private static final ConverterRegistry GLOBAL = new ConverterRegistry();

    static {
        for (final Converter<?, ?> c : Converter.DEFAULTS.values()) {
            GLOBAL.registry.register(c.type(), c, true);
        }
    }

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

    /**
     * Returns a registered converter, if any.
     *
     * Converters must match the type exactly, because otherwise the "toObject()"
     * conversion would not usually return the correct type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> find(final Class<E> type) {
        return (Converter<?, E>) registry.find(type);
    }

    /**
     * Registers a boolean converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final BooleanConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a byte converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final ByteConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a char converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final CharConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a short converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final ShortConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a int converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final IntConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a long converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final LongConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a float converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final FloatConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a double converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final DoubleConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }

    /**
     * Registers a String converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    @SuppressWarnings("unchecked")
    public <E> Converter<?, E> register(final StringConverter<?, E> converter,
            final Class<E> type) {
        return (Converter<?, E>) registry.register(type, converter, true);
    }
}