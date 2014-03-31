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

import com.blockwithme.util.shared.prim.BooleanConverter;
import com.blockwithme.util.shared.prim.ByteConverter;
import com.blockwithme.util.shared.prim.CharConverter;
import com.blockwithme.util.shared.prim.DoubleConverter;
import com.blockwithme.util.shared.prim.FloatConverter;
import com.blockwithme.util.shared.prim.IntConverter;
import com.blockwithme.util.shared.prim.LongConverter;
import com.blockwithme.util.shared.prim.ShortConverter;

/**
 * <code>ConverterRegistry</code> is a registry for primitive converters.
 *
 * It is thread-safe, and can delegate/fallback to another registry.
 */
public class ConverterRegistry {

    /** Registered converters. */
    private final RegistryImpl<Class<?>, Object> registry;

    /** Returns the global registry. */
    public static final ConverterRegistry instance() {
        return ProviderFactory.providerFor(ConverterRegistry.class,
                ConverterRegistry.class).get();
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
    public Object find(final Class<?> type) {
        return registry.find(type);
    }

    /**
     * Registers a boolean converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final BooleanConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a byte converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final ByteConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a char converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final CharConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a short converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final ShortConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a int converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final IntConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a long converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final LongConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a float converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final FloatConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }

    /**
     * Registers a double converter. Returns the old converter.
     * The result is untyped, because the previous converter might have been to another type.
     */
    public <E> Object register(final DoubleConverter<E> converter,
            final Class<E> type) {
        return registry.register(type, converter, true);
    }
}