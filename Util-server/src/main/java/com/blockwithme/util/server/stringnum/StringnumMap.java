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
package com.blockwithme.util.server.stringnum;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A lightweight map from String to anything.
 *
 * It uses a Stringnum internally, and supports reuse of the Stringnum in
 * multiple maps.
 *
 * The performance, when accessing with non-hacked Strings depends on the
 * concrete Stringnum implementation.
 *
 * Note that no real remove is possible, and so remove/clear is implemented
 * by setting the value of an existing key to null. The size the number of
 * keys, independent of the values.
 *
 * It is NOT thread-safe.
 *
 * TODO Test!
 *
 * @author monster
 */
public final class StringnumMap<V> extends AbstractStringnumMap<V> {

    /** Constructor. */
    public StringnumMap(final Stringnum stringnum) {
        super(stringnum);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final String key) {
        return stringnum.contains(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return stringnum.contains(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        return stringnum;
    }

    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return stringnum.size();
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        ensureDataCapacity();
        return Collections
                .unmodifiableCollection((List<V>) Arrays.asList(data));
    }

    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        data = EMPTY;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.stringnum.AbstractStringnumMap#acceptValue(java.lang.Object)
     */
    @Override
    protected boolean acceptValue(final Object value) {
        return true;
    }
}
