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
package com.blockwithme.util.shared.domains;

/**
 * Represents a finite, but normally lazy, set of values, each with an integer
 * ID.
 *
 * @author monster
 */
public interface Domain<E> {
    /** Returns the type of the Domain. */
    Class<E> getType();

    /** Returns true, if null values are supported. */
    boolean supportsNull();

    /**
     * Returns true, if "value.getClass() == type" is required, otherwise
     * "value instanceof type" is required.
     */
    boolean exactType();

    /**
     * Returns the ID for a value.
     * @throws RuntimeException if supportNull() is false.
     */
    int getID(E value);

    /**
     * Returns the value for this ID.
     * @throws RuntimeException if id is invalid.
     */
    E getValue(int id);
}
