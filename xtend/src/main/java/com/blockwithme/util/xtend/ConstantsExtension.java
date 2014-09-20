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
package com.blockwithme.util.xtend;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Often-used constants.
 *
 * @author monster
 */
public class ConstantsExtension extends FunctorsExtension {
    /** Empty boolean array. */
    public static final boolean[] EMPTY_BOOLEAN = new boolean[0];

    /** Empty byte array. */
    public static final byte[] EMPTY_BYTE = new byte[0];

    /** Empty char array. */
    public static final char[] EMPTY_CHAR = new char[0];

    /** Empty short array. */
    public static final short[] EMPTY_SHORT = new short[0];

    /** Empty int array. */
    public static final int[] EMPTY_INT = new int[0];

    /** Empty float array. */
    public static final float[] EMPTY_FLOAT = new float[0];

    /** Empty long array. */
    public static final long[] EMPTY_LONG = new long[0];

    /** Empty double array. */
    public static final double[] EMPTY_DOUBLE = new double[0];

    /** Empty Object array. */
    public static final Object[] EMPTY_OBJECT = new Object[0];

    /** Empty Object Object array. */
    public static final Object[][] EMPTY_OBJECT_OBJECT = new Object[0][];

    /** Empty String array. */
    public static final String[] EMPTY_STRING = new String[0];

    /** Empty File array. */
    public static final File[] EMPTY_FILE = new File[0];

    /** Empty Date array. */
    public static final Date[] EMPTY_DATE = new Date[0];

    /** Empty Class array. */
    @SuppressWarnings("rawtypes")
    public static final Class[] EMPTY_CLASS = new Class[0];

    /** Empty Set array. */
    @SuppressWarnings("rawtypes")
    public static final Set[] EMPTY_SET_ARRAY = new Set[0];

    /** Empty List array. */
    @SuppressWarnings("rawtypes")
    public static final List[] EMPTY_LIST_ARRAY = new List[0];

    /** Empty Map array. */
    @SuppressWarnings("rawtypes")
    public static final Map[] EMPTY_MAP_ARRAY = new Map[0];

}
