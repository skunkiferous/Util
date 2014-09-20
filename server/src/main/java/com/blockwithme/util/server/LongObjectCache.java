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


/**
 * Weak object cache, where each object has a unique, immutable long ID.
 * No hard reference is kept for the object, therefore allowing GC.
 *
 * Long are no good as IDs in GWT.
 *
 * @author monster
 */
public interface LongObjectCache<E> {
    /** Returns the object with the ID, if any. */
    E findObject(final long id);

    /** Returns the object with the name, if any. */
    E findObject(final String name);

    /**
     * Adds the Object with the ID to the cache.
     *
     * Optionally pin it, if pin is true, or name not null. Pinned objects have a hard reference.
     * Others only a weak reference.
     *
     * If the name is not null, then the object is automatically pinned, and
     * can be retrieved not only with ID, but also with name.
     */
    void cacheObject(final long id, final String name, final E obj,
            final boolean pin);

    /**
     * Adds the IDedAndNamed object.
     * It must *also* be an E ...
     *
     * @param obj
     * @param pin
     */
    void cacheObject(final IDedAndNamed obj, final boolean pin);

    /** Removes the object with the ID, and returns it, if any. */
    E removePinnedObject(final long id);

    /** Removes the object with the name, and returns it, if any. */
    E removePinnedObject(final String name);
}
