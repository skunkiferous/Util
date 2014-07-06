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
 * Defines the current type of data in an Any, as a JSON type.
 *
 * All Iterables/Iterators are assumed to be arrays.
 *
 * All Enumeration are assumed to be arrays.
 *
 * All CharacterSequences are assumed to be Strings.
 *
 * Classes are assumed to be Strings.
 *
 * Files are assumed to be Strings.
 *
 * Note that Java long can be either a Number or an Hexadecimal String,
 * based on it's value.
 *
 * Then check in the ConverterRegistry if it can be mapped to something else.
 *
 * Non-registered enums are assumed to be String.
 *
 * Object is used in case it is an Object of unknown type (which might be
 * mappable to something else).
 *
 * @author monster
 */
public enum JSONType {
    Null, Boolean, Number, String, Array, Object;
}
