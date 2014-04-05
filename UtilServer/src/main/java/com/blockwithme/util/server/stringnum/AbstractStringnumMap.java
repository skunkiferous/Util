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

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
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
 * Note that the data array size is proportional to the Stringum size.
 *
 * It is NOT thread-safe.
 *
 * TODO Test!
 *
 * @author monster
 */
public abstract class AbstractStringnumMap<V> implements Map<String, V> {

    /** Empty array. */
    protected static final Object[] EMPTY = new Object[0];

    /** Our stringnum */
    protected final Stringnum stringnum;

    /** The associated data, if any. */
    protected Object[] data = EMPTY;

    /** The optional entry set. */
    private Set<java.util.Map.Entry<String, V>> entrySet;

    /** Make sure data is at least as big as stringnums. */
    protected final void ensureDataCapacity() {
        final int size = stringnum.size();
        if (data.length < size) {
            data = Arrays.copyOf(data, size);
        }
    }

    /** Constructor. */
    protected AbstractStringnumMap(final Stringnum stringnum) {
        this.stringnum = Objects.requireNonNull(stringnum);
    }

    /** Our stringnum */
    public final Stringnum stringnum() {
        return stringnum;
    }

    /* (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }

    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    public final V remove(final String key) {
        return put(key, null);
    }

    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public final V remove(final Object key) {
        return (key instanceof String) ? put((String) key, null) : null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public final V get(final String key) {
        final int index = stringnum.indexOf(key);
        if ((index >= 0) && (index < data.length)) {
            return (V) data[index];
        }
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final V get(final Object key) {
        final int index = stringnum.indexOf(key);
        if ((index >= 0) && (index < data.length)) {
            return (V) data[index];
        }
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    @Override
    public final Set<java.util.Map.Entry<String, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<java.util.Map.Entry<String, V>>() {
                @Override
                public Iterator<java.util.Map.Entry<String, V>> iterator() {
                    return new Iterator<Map.Entry<String, V>>() {
                        private int next;
                        private int count;
                        private java.util.Map.Entry<String, V> last;

                        @Override
                        public boolean hasNext() {
                            return count < size();
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public java.util.Map.Entry<String, V> next() {
                            int index;
                            V tmp;
                            do {
                                index = next++;
                                tmp = (V) ((index < data.length) ? data[index]
                                        : null);
                            } while (!acceptValue(tmp));
                            count++;
                            final String key = stringnum.get(index);
                            final V value = tmp;
                            last = new java.util.Map.Entry<String, V>() {

                                @Override
                                public String getKey() {
                                    return key;
                                }

                                @Override
                                public V getValue() {
                                    return value;
                                }

                                @Override
                                public V setValue(final V value) {
                                    return put(key, value);
                                }
                            };
                            return last;
                        }

                        @Override
                        public void remove() {
                            if (last != null) {
                                last.setValue(null);
                                last = null;
                            }
                        }
                    };
                }

                @Override
                public int size() {
                    return AbstractStringnumMap.this.size();
                }
            };
        }
        return entrySet;
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public final boolean containsValue(final Object value) {
        if (acceptValue(value)) {
            final Object[] array = data;
            for (final Object object : array) {
                if (Objects.equals(value, object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public final V put(final String key, final V value) {
        final int index = stringnum.putString(key);
        if (data.length <= index) {
            data = Arrays.copyOf(data, index + 1);
        }
        @SuppressWarnings("unchecked")
        final V result = (V) data[index];
        replace(index, value);
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public final void putAll(final Map<? extends String, ? extends V> m) {
        stringnum.addAll(m.keySet());
        ensureDataCapacity();
        for (final Map.Entry<? extends String, ? extends V> e : m.entrySet()) {
            final int index = stringnum.indexOf(e.getKey());
            replace(index, e.getValue());
        }
    }

    /** Replaces a value. */
    protected void replace(final int index, final V value) {
        data[index] = value;
    }

    /* Do we accept this value? */
    protected abstract boolean acceptValue(final Object value);
}
