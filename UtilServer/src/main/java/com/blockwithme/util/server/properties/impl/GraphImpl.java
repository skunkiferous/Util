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
package com.blockwithme.util.server.properties.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import com.blockwithme.util.server.properties.Graph;
import com.blockwithme.util.server.properties.Properties;
import com.blockwithme.util.server.properties.meta.Concept;

/**
 * Default Root implementation.
 *
 * It must manage the time, and the postponed updates.
 *
 * @author monster
 */
public class GraphImpl<TIME extends Comparable<TIME>> implements Graph<TIME>,
        ImplGraph<TIME> {

    /** Buffered future changes. */
    private final TreeMap<TIME, List<Change<TIME>>> changes = new TreeMap<>();

    /** The graph root. */
    private Properties<TIME> root;

    /** The current time. */
    private TIME now;

    /**
     * @param now
     */
    public GraphImpl(final TIME now) {
        this.now = Objects.requireNonNull(now, "now");
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Root#getTime()
     */
    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#getTime()
     */
    @Override
    public final TIME getTime() {
        return now;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Root#setTime(java.lang.Object)
     */
    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#setTime(TIME)
     */
    @Override
    public final void setTime(final TIME newTime) {
        Objects.requireNonNull(newTime, "newTime");
        final int cmp = now.compareTo(newTime);
        if (cmp > 0) {
            throw new IllegalArgumentException("Time cannot go backward: now="
                    + now + " newTime=" + newTime);
        }
        if (cmp < 0) {
            // Perform buffered updates
            for (final TIME when : new ArrayList<>(changes.keySet())) {
                if (newTime.compareTo(when) >= 0) {
                    for (final Change<TIME> change : changes.remove(when)) {
                        change.perform();
                    }
                }
            }
            now = newTime;
        }
    }

    /** Records a future change. */
    private void recordChange(final Change<TIME> change) {
        List<Change<TIME>> changeList = changes.get(change.when);
        if (changeList == null) {
            changeList = new ArrayList<>();
            changes.put(change.when, changeList);
        }
        changeList.add(change);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#onFutureChange(com.blockwithme.properties.Properties, com.blockwithme.properties.Properties, java.lang.String, java.lang.Object, boolean, TIME)
     */
    @Override
    public final void onFutureChange(final Properties<TIME> setter,
            final Properties<TIME> properties, final String localKey,
            final Object newValue, final boolean forceWrite, final TIME when) {
        if (now.compareTo(when) >= 0) {
            // OK time was past/present, so perform now
            properties.set(setter, localKey, newValue, null, forceWrite);
        } else {
            // Save for later
            final Change<TIME> change = new Change<TIME>();
            change.properties = properties;
            change.localKey = localKey;
            change.newValue = newValue;
            change.setter = setter;
            change.forceWrite = forceWrite;
            change.when = when;
            recordChange(change);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#onFutureChange(com.blockwithme.properties.impl.Change)
     */
    @Override
    public final void onFutureChange(final Change<TIME> change) {
        if (now.compareTo(change.when) >= 0) {
            // OK time was past/present, so perform now
            change.perform();
        } else {
            // Save for later
            recordChange(change);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#onChange(com.blockwithme.properties.Properties, com.blockwithme.properties.Properties, java.lang.String, java.lang.Object, java.lang.Object)
     */
    @Override
    public void onChange(final Properties<TIME> setter,
            final Properties<TIME> properties, final String localKey,
            final Object oldValue, final Object newValue) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.Graph#lowerPriority(com.blockwithme.properties.Properties, com.blockwithme.properties.Properties)
     */
    @Override
    public boolean lowerPriority(final Properties<TIME> setter1,
            final Properties<TIME> setter2) {
        return false;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Graph#root()
     */
    @Override
    public Properties<TIME> root() {
        return root;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.ImplGraph#root(com.blockwithme.properties.Properties)
     */
    @Override
    public ImplGraph<TIME> root(final Properties<TIME> theRoot) {
        root = theRoot;
        return this;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Graph#concepts()
     */
    @Override
    public Concept[] concepts() {
        return root.listValues(Concept.class, false);
    }
}
