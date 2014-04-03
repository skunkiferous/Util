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
 * Represents a relationship between concepts.
 *
 * @author monster
 */
public interface Relationship extends Properties<Long> {
    /** The relationship name. */
    String name();

    /** Relationships can have a reciprocal, which could be itself. */
    Relationship reciprocal();

    /** The concept that is the target of the relationship. */
    Concept target();

    /** The minimum arity. */
    int minArity();

    /** The maximum arity. */
    int maxArity();
}
