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

/**
 * Pre-defined Domain instances.
 *
 * @author monster
 */
public interface Domains {
    /** Number of values cached for primitive domains. */
    int CACHE_SIZE = 1024;

    /** The Boolean Domain */
    BooleanDomain BOOLEAN = new BooleanDomain();

    /** The Byte Domain */
    ByteDomain BYTE = new ByteDomain();

    /** The Character Domain */
    CharacterDomain CHARACTER = new CharacterDomain();

    /** The Short Domain */
    ShortDomain SHORT = new ShortDomain();

    /** The Integer Domain */
    IntegerDomain INTEGER = new IntegerDomain();

    /** The Float Domain */
    FloatDomain FLOAT = new FloatDomain();

    // Long and double cannot be mapped into 32 bits.

    /** The String Domain */
    GenericLazyDomain<String> STRING = new GenericLazyDomain<String>(
            String.class, true, Integer.MAX_VALUE);
}
