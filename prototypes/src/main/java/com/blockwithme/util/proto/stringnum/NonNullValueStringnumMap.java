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
package com.blockwithme.util.proto.stringnum;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
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
 * Note that null value is allowed.
 *
 * Note that the data array size is proportional to the Stringum size.
 *
 * It is NOT thread-safe.
 *
 * TODO Test!
 *
 * @author monster
 */
public final class NonNullValueStringnumMap<V> extends AbstractStringnumMap<V> {

    /** Number of non-null values. */
    private int size;

    /** The optional keyset. */
    private Set<String> keySet;

    /** The optional values. */
    private Collection<V> values;

    /* (non-Javadoc)
     * @see com.blockwithme.util.stringnum.AbstractStringnumMap#acceptValue(java.lang.Object)
     */
    @Override
    protected boolean acceptValue(final Object value) {
        return value != null;
    }

    /** Constructor. */
    public NonNullValueStringnumMap(final Stringnum stringnum) {
        super(stringnum);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final String key) {
        return get(key) != null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return size;
    }

    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        data = EMPTY;
        size = 0;
    }

    /** Replaces a value. */
    @Override
    protected void replace(final int index, final V value) {
        final Object before = data[index];
        data[index] = value;
        if (value == null) {
            if (before != null) {
                size--;
            }
        } else {
            if (before == null) {
                size++;
            }
        }
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<String> keySet() {
        if (keySet == null) {
            keySet = new AbstractSet<String>() {
                @Override
                public Iterator<String> iterator() {
                    final Iterator<java.util.Map.Entry<String, V>> iter = entrySet()
                            .iterator();
                    return new Iterator<String>() {
                        private String last;

                        @Override
                        public boolean hasNext() {
                            return iter.hasNext();
                        }

                        @Override
                        public String next() {
                            return (last = iter.next().getKey());
                        }

                        @Override
                        public void remove() {
                            put(last, null);
                        }
                    };
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return keySet;
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new AbstractCollection<V>() {
                @Override
                public Iterator<V> iterator() {
                    final Iterator<java.util.Map.Entry<String, V>> iter = entrySet()
                            .iterator();
                    return new Iterator<V>() {
                        @Override
                        public boolean hasNext() {
                            return iter.hasNext();
                        }

                        @Override
                        public V next() {
                            return iter.next().getValue();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return values;
    }
}
