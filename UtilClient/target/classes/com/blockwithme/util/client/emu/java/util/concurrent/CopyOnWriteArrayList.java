/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements a {@link java.util.ArrayList} variant that is thread-safe. All
 * write operation result in a new copy of the underlying data being created.
 * Iterators reflect the state of the CopyOnWriteArrayList at the time they were
 * created. They are not updated to reflect subsequent changes to the list. In
 * addition, these iterators cannot be used for modifying the underlying
 * CopyOnWriteArrayList.
 *
 * @param <E> the element type
 */
public class CopyOnWriteArrayList<E> extends ArrayList<E> implements RandomAccess, Cloneable, Serializable {

    private static final long serialVersionUID = 8673264195747942595L;

    /**
     * Creates a new, empty instance of CopyOnWriteArrayList.
     */
    public CopyOnWriteArrayList() {
    }

    /**
     * Creates a new instance of CopyOnWriteArrayList and fills it with the
     * contents of a given Collection.
     *
     * @param c     the collection the elements of which are to be copied into
     *              the new instance.
     */
    public CopyOnWriteArrayList(Collection<? extends E> c) {
        super(c);
    }

    /**
     * Creates a new instance of CopyOnWriteArrayList and fills it with the
     * contents of a given array.
     *
     * @param array the array the elements of which are to be copied into the
     *              new instance.
     */
    public CopyOnWriteArrayList(E[] array) {
        super(Arrays.asList(array));
    }

    /**
     * Adds to this CopyOnWriteArrayList all those elements from a given
     * collection that are not yet part of the list.
     *
     * @param c     the collection from which the potential new elements are
     *              taken.
     *
     * @return the number of elements actually added to this list.
     */
    public int addAllAbsent(Collection<? extends E> c) {
        int result = 0;
        if (!c.isEmpty()) {
            for (E e : c) {
                if (!contains(e)) {
                    add(e);
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Adds to this CopyOnWriteArrayList another element, given that this
     * element is not yet part of the list.
     *
     * @param e     the potential new element.
     *
     * @return true if the element was added, or false otherwise.
     */
    public boolean addIfAbsent(E e) {
        if (!contains(e)) {
            add(e);
            return true;
        }
        return false;
    }
}