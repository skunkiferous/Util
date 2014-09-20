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
package com.blockwithme.util.server;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Weak object cache, where each object has a unique, immutable long ID.
 * No hard reference is kept for the object, therefore allowing GC.
 *
 * Long are no good as IDs in GWT.
 *
 * @author monster
 */
public class LongObjectCacheImpl<E> extends ReferenceQueue<E> implements
        LongObjectCache<E> {

    /** WeakReference with ID, so we can do quick remove on GC. */
    private static final class WeakReferenceWithID<E> extends WeakReference<E> {

        /** The object's ID. */
        public final long id;

        /** The object's name. */
        public final String name;

        /**
         * @param referent
         */
        public WeakReferenceWithID(final long theID, final String theName,
                final E obj, final ReferenceQueue<? super E> queue) {
            super(obj, queue);
            id = theID;
            name = theName;
        }
    }

    /** Hard reference with ID. */
    private static final class HardReferenceWithID<E> {

        /** The object's ID. */
        public final long id;

        /** The object's name. */
        public final String name;

        /** The object. */
        public final E obj;

        /**
         * @param referent
         */
        public HardReferenceWithID(final long theID, final String theName,
                final E theObj) {
            id = theID;
            name = theName;
            obj = theObj;
        }
    }

    /** The (ID or Name)-to-object-reference map. */
    private final ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();

    @SuppressWarnings("unchecked")
    private void processQueue() {
        WeakReferenceWithID<E> ref;
        while ((ref = (WeakReferenceWithID<E>) poll()) != null) {
            // If we have GCed references, then map cannot be null
            map.remove(ref.id);
            if (ref.name != null) {
                map.remove(ref.name);
            }
        }
    }

    /** Constructor. */
    public LongObjectCacheImpl() {
        // NOP
    }

    /** Returns the object with the ID, if any. */
    @SuppressWarnings("unchecked")
    @Override
    public E findObject(final long id) {
        processQueue();
        E result = null;
        final Object tmp = map.get(id);
        if (tmp instanceof WeakReferenceWithID<?>) {
            final WeakReferenceWithID<E> ref = (WeakReferenceWithID<E>) tmp;
            result = ref.get();
        } else if (tmp != null) {
            final HardReferenceWithID<E> ref = (HardReferenceWithID<E>) tmp;
            result = ref.obj;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.tactors.internal.LongObjectCache#findObject(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E findObject(final String name) {
        processQueue();
        E result = null;
        if (name != null) {
            final Object tmp = map.get(name);
            // Named objects are always a HardReferenceWithID ...
            if (tmp != null) {
                final HardReferenceWithID<E> ref = (HardReferenceWithID<E>) tmp;
                result = ref.obj;
            }
        }
        return result;
    }

    /** Adds the Object with the ID. */
    @Override
    public void cacheObject(final long id, final String name, final E obj,
            final boolean pin) {
        processQueue();
        final Object ref;
        if (pin || (name != null)) {
            ref = new HardReferenceWithID<E>(id, name, obj);
        } else {
            ref = new WeakReferenceWithID<E>(id, name, obj, this);
        }
        if (name != null) {
            if (map.putIfAbsent(name, ref) != null) {
                throw new IllegalStateException("Name " + name
                        + " already in use!");
            }
        }
        if (map.putIfAbsent(id, ref) != null) {
            if (name != null) {
                map.remove(name);
            }
            throw new IllegalStateException("ID " + id + " already in use!");
        }
    }

    /** Adds the IDedAndNamed object. */
    @SuppressWarnings("unchecked")
    @Override
    public void cacheObject(final IDedAndNamed obj, final boolean pin) {
        cacheObject(obj.id(), obj.name(), (E) obj, pin);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.tactors.internal.LongObjectCache#removePinnedObject(long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E removePinnedObject(final long id) {
        processQueue();
        final Object tmp = map.remove(id);
        if (tmp instanceof WeakReferenceWithID<?>) {
            final WeakReferenceWithID<E> ref = (WeakReferenceWithID<E>) tmp;
            if (ref.name != null) {
                map.remove(ref.name);
            }
            return ref.get();
        } else if (tmp != null) {
            final HardReferenceWithID<E> ref = (HardReferenceWithID<E>) tmp;
            if (ref != null) {
                if (ref.name != null) {
                    map.remove(ref.name);
                }
                return ref.obj;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.tactors.internal.LongObjectCache#removePinnedObject(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public E removePinnedObject(final String name) {
        processQueue();
        E result = null;
        if (name != null) {
            final Object tmp = map.remove(name);
            // Named objects are always a HardReferenceWithID ...
            if (tmp != null) {
                final HardReferenceWithID<E> ref = (HardReferenceWithID<E>) tmp;
                result = ref.obj;
                // If you have a name, then you also have an ID ...
                map.remove(ref.id);
            }
        }
        return result;
    }
}
