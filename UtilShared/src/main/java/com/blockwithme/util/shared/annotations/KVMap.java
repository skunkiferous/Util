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
package com.blockwithme.util.shared.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * KVMap is a generic annotation that can be applied to anything, and
 * represents a map of key-value pairs. Due to the limitations of Java
 * annotations, in particular, the lack of inheritance and polymorphism,
 * The only supported type that can be used to represent anything is the String.
 * So keys and values will be Strings. This can give us a terse syntax. We can
 * simply model the map entries as an array of Strings. The first String is the
 * key, and the rest are values. Therefore, a value could itself be an array,
 * or we could even have no value at all. One problem is that an empty array
 * is also valid, such that we would have no key either. Such array would be
 * ignored.
 *
 * If we had limited ourselves to exactly one value per key, we could have just
 * used a single String[] for both keyes and values, but that would have been
 * very error prone. So each key-value pair must be itself an annotation.
 *
 * To maximize the flexibility, we allow the content of the values to be
 * interpreted as Groovy source code.
 *
 * @author monster
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface KVMap {
    E[] value();
}
