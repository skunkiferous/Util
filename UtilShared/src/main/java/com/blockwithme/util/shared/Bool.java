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
 * Helper class for using boolean where double is expected, and vice-versa.
 *
 * @author monster
 */
public class Bool {

    /** Represents the boolean true value. */
    public static final double TRUE = 1;

    /** Represents the boolean false value. */
    public static final double FALSE = 0;

    /** Converts a number to a boolean. */
    public static boolean bool(final double value) {
        return (value != 0);
    }

    /** Converts a boolean to a number. */
    public static double number(final boolean value) {
        return value ? 1 : 0;
    }
}
