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
 * Helper class to compute the approximate memory footprint of an object.
 *
 * TODO Add methods to compute the size of java.util.Collections, to make
 * size computation easier...
 *
 * FYI: I have computed the size of the Boolean class itself, to about
 * 630 bytes of the JVM ram. This should give us an order of magnitude
 * about the size of Class themselves.
 *
 * @author monster
 */
public class Footprint {

    /** Are we in 64 bits? */
    public static final boolean JVM_64_BITS = System.getProperty("os.arch")
            .contains("64");

    /** The architecture "word" size. */
    private static final int WORD = JVM_64_BITS ? 8 : 4;

    /** How big is an object without data (approximately)? */
    public static final int OBJECT_SIZE = JVM_64_BITS ? 16 : 8;

    /** How big is an empty array (approximately)? */
    public static final int ARRAY_SIZE = JVM_64_BITS ? 24 : 12;

    /** How big is an object reference? (We assume compressed pointers in 64 bit) */
    public static final int REFERENCE = 4;

    /** Rounds the footprint to an appropriate multiple of the architecture "word" size. */
    public static int round(final int footprint) {
        final int rest = footprint % WORD;
        return (rest == 0) ? footprint : (footprint - rest + WORD);
    }

    /**
     * Returns the (rounded) footprint for an array of the given type, with the given size.
     * For objects, does NOT contain the size of the objects themselves, but only
     * the size taken by the object-reference within the array.
     */
    public static int array(final AnyType type, final int size) {
        return round(ARRAY_SIZE + type.sizeInBytes * size);
    }

    /**
     * Returns the footprint for an array as array(AnyType.Object, size),
     * plus size * avgObjectFootprint. Note that avgObjectFootprint should
     * take null, ... into consideration, when deciding on an average.
     */
    public static int objectArray(final int size, final int avgObjectFootprint) {
        return array(AnyType.Object, size) + (avgObjectFootprint * size);
    }

    /**
     * Returns the (rounded) size of an object with the given fields. Does NOT
     * take into consideration, the size of the child objects, pointed to by
     * the references. Remember to include the fields of the base-classes.
     */
    public static int object(final AnyType... fields) {
        int sumFields = 0;
        for (final AnyType type : fields) {
            sumFields += type.sizeInBytes;
        }
        return round(OBJECT_SIZE + sumFields);
    }
}
