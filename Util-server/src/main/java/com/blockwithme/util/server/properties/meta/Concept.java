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
package com.blockwithme.util.server.properties.meta;

import com.blockwithme.util.server.properties.Properties;

/**
 * The meta-meta-level!
 *
 * A concept represent one of the other classes in the packages:
 *
 * com.blockwithme.meta.types
 * com.blockwithme.meta.infrastructure
 *
 * It can be used to describe the relationships between the parts of the meta
 * API.
 *
 * @author monster
 */
public interface Concept extends Properties<Long> {
    /** The concept name. */
    String name();

    /**
     * A concept has some relationship to other concepts (and possibly to
     * itself).
     */
    Relationship[] relationships();

    /** Returns the relationship with the given name, if any. */
    Relationship findRelationship(final String name);
}
