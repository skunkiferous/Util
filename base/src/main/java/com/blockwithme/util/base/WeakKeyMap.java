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
package com.blockwithme.util.base;

import java.util.Map;

/**
 * WeakKeyMap is just a thin wrapper over the platform-specific weak-key-map
 * implementations. JavaScript, and therefore GWT, have experimental support
 * for weak-key maps. Unfortunately, the API is missing many methods required
 * to create a java.util.Map implementation. This is why this interface does
 * not extend java.util.Map.
 *
 * @author monster
 */
public interface WeakKeyMap<KEY, VALUE> {
    /** Removes all key/value pairs from the WeakKeyMap object. */
    void clear();

    /**
     * Returns a boolean asserting whether a value has been associated to the
     * key in the WeakKeyMap object or not.
     *
     * @param key The key to be checked.
     * @return True if they key was present.
     */
    boolean containsKey(Object key);

    /**
     * Removes any value associated to the key. WeakKeyMap.containsKey(key)
     * will return false afterwards.
     *
     * @param key The key to be removed.
     * @return The previous value, if any.
     */
    VALUE remove(Object key);

    /**
     * Returns the value associated to the key if present, otherwise null.
     *
     * @param key The key to be read.
     * @return The current value, if any.
     */
    VALUE get(Object key);

    /**
     * Sets the value for the key in the WeakKeyMap object.
     * Note that null keys are not supported, and null values cause a
     * call to remove().
     *
     * @param key The key to be replaced.
     * @param value The new value.
     * @return The previous value, if any.
     */
    VALUE put(KEY key, VALUE value);

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object,Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws NullPointerException if the specified map is null, or if
     *         this map does not permit null keys or values, and the
     *         specified map contains null keys or values
     * @throws IllegalArgumentException if some property of a key or value in
     *         the specified map prevents it from being stored in this map
     */
    void putAll(Map<? extends KEY, ? extends VALUE> m);

}
