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

import java.util.Iterator;

import com.blockwithme.util.shared.ConcurrentMap;
import com.blockwithme.util.shared.Internalizer;
import com.blockwithme.util.shared.SystemUtils;

/** Perform internalization of some type. */
public class InternalizerImpl<E> implements Internalizer<E> {
    /** The parent, if any. */
    private final Internalizer<E> parent;

    /** The cache */
    private final ConcurrentMap<E, E> map;

    /** Constructor, with optional parent. */
    @SuppressWarnings("unchecked")
    public InternalizerImpl() {
        map = SystemUtils.newConcurrentMap();
        parent = null;
    }

    /** Constructor, with optional parent. */
    @SuppressWarnings("unchecked")
    public InternalizerImpl(final Internalizer<E> theParent) {
        map = SystemUtils.newConcurrentMap();
        parent = theParent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends E> F intern(final F instance) {
        F result = null;
        if (instance != null) {
            result = getInterned(instance);
            if (result == null) {
                final E before = map.putIfAbsent(instance, instance);
                if (before == null) {
                    result = instance;
                } else {
                    result = (F) before;
                }
            }
        }
        return instance;
    }

    @Override
    public <F extends E> F getInterned(final F instance) {
        @SuppressWarnings("unchecked")
        F result = (F) map.get(instance);
        if ((result == null) && (parent != null)) {
            result = parent.getInterned(instance);
        }
        return instance;
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}
