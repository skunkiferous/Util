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

package com.blockwithme.util.proto;

import javax.inject.Provider;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.shared.Registry;
import com.blockwithme.util.shared.RegistryImpl;
import com.blockwithme.util.shared.Statics;

/**
 * Simple class allowing the use of singleton,
 * while still being compatible with DI frameworks.
 *
 * First it checks if a provider was already registered.
 * If not, it checks if a type-name.provider system property
 * was defined, giving the name of the provider to use.
 * If not, then it creates a provider that always returns the
 * same instance, created from the class given as parameter.
 *
 * @author monster
 */
public class ProviderFactory {
    /** Singleton provider. */
    private static class SimpleProvider<E> implements Provider<E> {
        /** The singleton */
        private final E instance;

        /** COnstrucvtort */
        public SimpleProvider(final E theInstance) {
            instance = theInstance;
        }

        @Override
        public E get() {
            return instance;
        }
    }

    /** The registry key. */
    private static final String KEY = ProviderFactory.class.getName() + ".reg";

    /** Registry, for pre-registration through DI framework. */
    private static final Registry<Class<?>, Provider<?>> reg() {
        @SuppressWarnings("unchecked")
        Registry<Class<?>, Provider<?>> result = (Registry<Class<?>, Provider<?>>) Statics
                .get(KEY);
        if (result == null) {
            result = Statics.replace(KEY, null,
                    new RegistryImpl<Class<?>, Provider<?>>());
        }
        return result;
    }

    /** Allows pre-registration of providers. */
    public static <E> void registerProvider(final Class<E> type,
            final Provider<E> provider) {
        reg().register(type, provider, true);
    }

    /** Returns a provider for type. */
    @SuppressWarnings("unchecked")
    public static <E> Provider<E> providerFor(final Class<E> type,
            final Class<? extends E> implCls) {
        final Provider<E> provider = (Provider<E>) reg().find(type);
        if (provider != null) {
            return provider;
        }
        final String typeName = type.getName();
        final String implProviderProp = typeName + ".provider";
        final String tmp = System.getProperty(implProviderProp);
        final String implProviderName = (tmp == null) ? "" : tmp.trim();
        if (!implProviderName.isEmpty()) {
            final Class<?> implProviderCls = SystemUtils.forName(
                    implProviderName, type);
            return (Provider<E>) SystemUtils.newInstance(implProviderCls);
        }
        final E impl = SystemUtils.newInstance(implCls);
        return new SimpleProvider<E>(impl);
    }
}
