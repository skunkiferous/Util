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

/**
 * <code>Registry</code> is a generic registry.
 */
public interface Registry<K, V> {
    /**
     * Registers a key-value pair.
     * Both key and value must pass the validation tests.
     * If the key was already registered, it will be returned.
     * If the key was already registered, and update is true, the value will be replaced.
     */
    V register(final K key, final V value, final boolean update);

    /** Resolve a value, from a key. Returns null if not resolved. */
    V find(final K key);

    /**
     * Resolve a value, from a key.
     * If no value can be resolved, an exception will be thrown.
     * @throws java.lang.IllegalStateException
     */
    V get(final K key);
}
