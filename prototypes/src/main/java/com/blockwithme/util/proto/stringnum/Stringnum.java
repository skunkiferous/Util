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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Stringnum is an evil performance hack. It allows you to subvert the
 * String hashcode to store a unique integer ID, instead of the conventional
 * hashcode. The effect is that you can get much faster read access to your data.
 * The downsides are many:
 *
 * 1) You must actually reuse the same instance of the String, not just another
 *    String instance with the same content, to get the benefits.
 * 2) You cannot mix normal and hacked Strings in any context where hashing
 *    is involved (but preferably, you should not mix them at all).
 * 3) We are using reflection to manipulate the String on first introduction,
 *    so if some security setting prevents from using reflection, it won't work.
 * 4) The initial registration of a String is somewhat expensive, to make the
 *    later use as fast as possible.
 * 5) The hashcode 0 means "not computed yet", and therefore cannot be used.
 *    So we always add the "empty string" at index 0, so things run smoothly.
 * 6) Many of the List/Set methods are not implemented (set, remove, ...)
 *
 * This Object use a concurrency primitive to be thread-safe, but thread-safety
 * cannot be guaranteed for sub-classes.
 *
 * To get the best results, create an independent instance for every
 * "String Domain".
 *
 * @author monster
 */
public class Stringnum implements List<String>, Set<String> {

    /** Our ListIterator */
    private static final class ListItr implements ListIterator<String> {
        /** The strings. */
        private final String[] strings;
        /**
         * Index of element to be returned by subsequent call to next.
         */
        private int next = 0;

        private ListItr(final String[] strings, final int index) {
            this.strings = strings;
            next = index;
            if ((index < 0) || (index >= strings.length)) {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public boolean hasNext() {
            return next != strings.length;
        }

        @Override
        public String next() {
            try {
                return strings[next++];
            } catch (final IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return next != 0;
        }

        @Override
        public String previous() {
            try {
                return strings[--next];
            } catch (final IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return next;
        }

        @Override
        public int previousIndex() {
            return next - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final String e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(final String e) {
            throw new UnsupportedOperationException();
        }
    }

    /** The hashcode field of the String. */
    private static final Field HASHCODE;

    static {
        try {
            HASHCODE = String.class.getDeclaredField("hash");
            HASHCODE.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new UnsupportedOperationException(
                    "Cannot access String's hashcode field", e);
        }
    }

    /** CAS field updater. */
    private static final AtomicReferenceFieldUpdater<Stringnum, String[]> UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(Stringnum.class, String[].class, "strings");

    /** Hacked Strings. */
    private volatile String[] strings = new String[] { "" };

    /** Replace the array. */
    private boolean replace(final String[] before, final String[] after) {
        return UPDATER.compareAndSet(this, before, after);
    }

    /** Adds a new String, if not presents yet, and return it's index. */
    private static String putStringNoCheck(final String[] array,
            final String str, final int index) {
        final String hacked = new String(str.toCharArray());
        try {
            HASHCODE.set(hacked, index);
        } catch (final Exception e) {
            throw new UnsupportedOperationException(
                    "Cannot access String's hashcode field", e);
        }
        array[index] = hacked;
        return hacked;
    }

    /** Returns the next index. */
    @Override
    public final int size() {
        return strings.length;
    }

    /**
     * Returns the String at the give index.
     * @throws java.lang.IndexOutOfBoundsException on bad index.
     */
    @Override
    public final String get(final int index) {
        return strings[index];
    }

    /**
     * Searches for a String, and returns it's index if found, otherwise -1.
     * @throws java.lang.NullPointerException if str is null
     */
    public final int indexOf(final String str) {
        int result = indexOfHacked(str);
        if (result < 0) {
            final String hacked = findHacked(str);
            if (hacked != null) {
                result = hacked.hashCode();
            }
        }
        return result;
    }

    /**
     * Searches for a String, and returns it's index if found, otherwise -1.
     *
     * Only works with *hacked* Strings!
     *
     * @throws java.lang.NullPointerException if str is null
     */
    public final int indexOfHacked(final String str) {
        final int hashcode = str.hashCode();
        final int index = strings.length;
        if ((hashcode >= 0) && (hashcode < index) && (strings[hashcode] == str)) {
            return hashcode;
        }
        return -1;
    }

    /**
     * Adds a new String, if not presents yet, and return it's index.
     * @throws java.lang.NullPointerException if str is null
     */
    public final int putString(final String str) {
        while (true) {
            int index = indexOf(str);
            if (index == -1) {
                final String[] before = strings;
                index = before.length;
                final String[] after = Arrays.copyOf(before, index + 1);
                final String hacked = putStringNoCheck(after, str, index);
                if (replace(before, after)) {
                    onGrow(1);
                    onNewString(str, hacked);
                    return index;
                }
            } else {
                return index;
            }
        }
    }

    /**
     * Adds a new String, if not presents yet, and return true if added.
     * @throws java.lang.NullPointerException if str is null
     */
    @Override
    public final boolean add(final String str) {
        final String[] before = strings;
        putString(str);
        return (before != strings);
    }

    /**
     * "Interns" a String, such that the return value is a hacked indexed String.
     * @throws java.lang.NullPointerException if str is null
     */
    public final String intern(final String str) {
        // Note: Do NOT merge those two lines! There is a JVM bug causing an
        // ArrayIndexOutOfBounds if you do that!
        final int index = putString(str);
        return strings[index];
    }

    /**
     * Adds new Strings, if not presents yet.
     * @throws java.lang.NullPointerException if strs or content is null
     */
    public final boolean addAll(final String... strs) {
        while (true) {
            final String[] before = strings;
            final String[] toAdd = new String[strs.length];
            int missing = 0;
            for (int i = 0; i < strs.length; i++) {
                final String str = strs[i];
                final int index = indexOf(str);
                if (index == -1) {
                    toAdd[i] = str;
                    missing++;
                }
            }
            if (missing > 0) {
                int index = strs.length;
                final String[] after = Arrays.copyOf(before, index + missing);
                for (int i = 0; i < toAdd.length; i++) {
                    final String str = toAdd[i];
                    if (str != null) {
                        toAdd[i] = putStringNoCheck(after, str, index++);
                    }
                }
                if (replace(before, after)) {
                    onGrow(missing);
                    for (int i = 0; i < toAdd.length; i++) {
                        final String hacked = toAdd[i];
                        if (hacked != null) {
                            final String str = strs[i];
                            onNewString(str, hacked);
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Adds new Strings, if not presents yet.
     * @throws java.lang.NullPointerException if strs or content is null
     */
    public final String[] intern(final String... strs) {
        addAll(strs);
        final String[] result = new String[strs.length];
        final String[] array = strings;
        for (int i = 0; i < result.length; i++) {
            result[i] = array[indexOf(strs[i])];
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.List#isEmpty()
     */
    @Override
    public final boolean isEmpty() {
        return strings.length == 0;
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    public final boolean contains(final String str) {
        return indexOf(str) >= 0;
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public final boolean contains(final Object o) {
        return indexOf(o) >= 0;
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray()
     */
    @Override
    public final Object[] toArray() {
        return strings.clone();
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray(T[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] toArray(T[] a) {
        final String[] array = strings;
        if (a.length < array.length) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(),
                    array.length);
        }
        System.arraycopy(array, 0, a, 0, array.length);
        return a;
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public final int indexOf(final Object o) {
        if (o instanceof String) {
            return indexOf((String) o);
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public final int lastIndexOf(final Object o) {
        if (o instanceof String) {
            // There can be only one single instance with the same value ...
            return indexOf((String) o);
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see java.util.List#iterator()
     */
    @Override
    public final Iterator<String> iterator() {
        return listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    @Override
    public final ListIterator<String> listIterator() {
        return listIterator(0);
    }

    /* (non-Javadoc)
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public final boolean containsAll(final Collection<?> c) {
        for (final Object object : c) {
            if (!contains(object)) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public final boolean addAll(final Collection<? extends String> c) {
        return addAll(c.toArray(new String[c.size()]));
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    @Override
    public final ListIterator<String> listIterator(final int index) {
        return new ListItr(strings, index);
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    @Override
    public final List<String> subList(final int fromIndex, final int toIndex) {
        return Collections.unmodifiableList(Arrays.asList(strings).subList(
                fromIndex, toIndex));
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public final boolean addAll(final int index,
            final Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#removeAll(java.util.Collection)
     */
    @Override
    public final boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Override
    public final boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#clear()
     */
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public final String set(final int index, final String element) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public final void add(final int index, final String element) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    @Override
    public final String remove(final int index) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    @Override
    public final boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    /** Finds the hacked String matching the non-hacked input String. */
    protected String findHacked(final String str) {
        final String[] array = strings;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(str)) {
                return array[i];
            }
        }
        return null;
    }

    /** Called when the array grows. */
    protected void onGrow(final int amount) {
        // NOP
    }

    /** Called when a new String is indexed. */
    protected void onNewString(final String original, final String hacked) {
        // NOP
    }
}
