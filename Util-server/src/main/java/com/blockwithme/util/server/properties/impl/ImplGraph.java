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

import com.blockwithme.util.server.properties.Graph;
import com.blockwithme.util.server.properties.Properties;

/**
 * @author monster
 *
 * @param <TIME>
 */
public interface ImplGraph<TIME extends Comparable<TIME>> extends Graph<TIME> {

    /** Sets the root. */
    ImplGraph<TIME> root(final Properties<TIME> theRoot);

    /** Receives changes that will only be applied in the future. */
    void onFutureChange(final Properties<TIME> setter,
            final Properties<TIME> properties, final String localKey,
            final Object newValue, final boolean forceWrite, final TIME when);

    /** Receives changes that will only be applied in the future. */
    void onFutureChange(final Change<TIME> change);

    /** Informs the root of changes. */
    void onChange(final Properties<TIME> setter,
            final Properties<TIME> properties, final String localKey,
            final Object oldValue, final Object newValue);

    /**
     * Returns true, if the first instance has lower priority as the second
     * instance. The default implementation always returns false, which results
     * in the last-writer-wins semantic.
     */
    boolean lowerPriority(final Properties<TIME> setter1,
            final Properties<TIME> setter2);

}