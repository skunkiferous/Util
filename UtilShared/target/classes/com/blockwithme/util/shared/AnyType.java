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
 * Defines the current type of data in an Any.
 *
 * @author monster
 */
public enum AnyType {
    Empty(Void.class, 0, false, false), Boolean(java.lang.Boolean.TYPE, 1,
            false, false), Byte(java.lang.Byte.TYPE, 1, false, false), Char(
            java.lang.Character.TYPE, 2, false, false), Short(
            java.lang.Short.TYPE, 2, false, false), Int(java.lang.Integer.TYPE,
            4, false, false), Long(java.lang.Long.TYPE, 8, false, false), Float(
            java.lang.Float.TYPE, 4, false, false), Double(
            java.lang.Double.TYPE, 8, false, false), String(String.class,
            Footprint.REFERENCE, true, false), Object(Object.class,
            Footprint.REFERENCE, true, false), BooleanArray(boolean[].class,
            Footprint.REFERENCE, true, true), ByteArray(byte[].class,
            Footprint.REFERENCE, true, true), CharArray(char[].class,
            Footprint.REFERENCE, true, true), ShortArray(short[].class,
            Footprint.REFERENCE, true, true), IntArray(int[].class,
            Footprint.REFERENCE, true, true), LongArray(long[].class,
            Footprint.REFERENCE, true, true), FloatArray(float[].class,
            Footprint.REFERENCE, true, true), DoubleArray(double[].class,
            Footprint.REFERENCE, true, true), StringArray(String[].class,
            Footprint.REFERENCE, true, false), ObjectArray(Object[].class,
            Footprint.REFERENCE, true, true);

    /** The Class representing the type. */
    public final Class<?> type;
    /** The size in bytes of one value (reference for Object) of that type. */
    public final int sizeInBytes;
    /** Does this type represent an object? */
    public final boolean object;
    /** Does this type represent an array? */
    public final boolean array;

    private AnyType(final Class<?> theType, final int theSizeInBytes,
            final boolean theObject, final boolean theArray) {
        type = theType;
        sizeInBytes = theSizeInBytes;
        object = theObject;
        array = theArray;
    }
}
