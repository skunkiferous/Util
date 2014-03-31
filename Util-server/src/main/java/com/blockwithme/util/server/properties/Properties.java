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
package com.blockwithme.util.server.properties;

import java.util.List;

/**
 * Represents an object that contains properties.
 *
 * Properties have a String key, and a value. Values of type Generator are
 * handled specially, since the generate the real value on the fly, instead
 * of being the value themselves. Since Properties should form a tree-structure,
 * reference to other (non-direct child/parent) properties among the tree can
 * be achieved by using links. Links are generated automatically when needed.
 * Links are implemented are Generators.
 *
 * If the value type on setting is either an Object array, or an Iterable
 * (Collection), that contains properties, the actual value is replaced with
 * a new Properties instance, generated on the fly, which uses the position
 * of the collection/array content as property name within itself. This allows
 * using the index as part of the path when querying the Properties later.
 *
 * A generator is a function-object, which "computes" the value to the property.
 * A generator constructor is expected to receive a String as parameter, to
 * allow for "generic configuration".
 *
 * A "link" is a kind of generator: a path that cause delegation to another
 * Properties object in the hierarchy.
 *
 * The links remove the need to repeat the same values in multiple places.
 *
 * The methods recognize relative path, like in a file system, such that links
 * can just be implemented as a path.
 *
 * Keyes should be composed exclusively of the following characters:
 * [a-z|A-Z|0-9|_|$]
 *
 * The path separator is the Unix(tm) path separator: /
 * Relative path to parent is also as Unix(tm): ..
 *
 * When iterated, a Properties returns it's keyes. Note that a value can be set
 * to null, but it still stays present, therefore it's name as still returned
 * when iterating over the Properties object.
 *
 * While reading properties should feel relatively "natural", the Properties
 * support some advanced features, which makes setting a property more involved.
 * Firstly, the root of the hierarchy supports added functionality. The visible
 * additions in the API are the current application "time". It is global to the
 * whole hierarchy. The time is required, so that it is possible to set the
 * value of properties *in the future*. This feature is critical to the
 * coordination of reconfigurations. All the changed parameters can be set to
 * take effect at a point in the future, such that they will all be
 * automatically, and *atomically*, activated at that point in time. As long
 * as all the new parameters are set before the time changes, it will seem to
 * the code as if all the changed happened at the same time.
 *
 * It is important to realize that the time of the root is not tied to any real
 * time. It can in fact be any comparable data type, and the API user is
 * responsible for moving the time forward (The API does not let the time go
 * backward).
 *
 * The second critical feature, is a prioritisation of the value setters. The
 * problem is, that multiple pieces of code might write to the same property
 * of the same Properties instance, and if the application is multi-threaded,
 * the order of write might not be guaranteed. Therefore, we have required that
 * all calls to a setter take a Properties object identifying who the "setter"
 * is, such that the setter with the highest priority will win, and so that
 * the current value will reflect the highest priority setter, and not the
 * last writer. This prioritisation happens in the root, and can be customized.
 * The last-writer-wins semantic is still the default, but the root can change
 * this to allow prioritisation based on application specific requirements.
 *
 * For example, in OSGi, Properties instances of the "application core" could
 * be made to have higher priority then Properties instances from application
 * dependencies.
 *
 * It is critical to realize that this code is not multi-threaded safe on
 * purpose. This seems to be the only easy way to allow atomic changes when
 * the time changes.
 *
 * @author monster
 */
public interface Properties<TIME extends Comparable<TIME>> extends
        Iterable<String> {

    /** The path separator. */
    char SEPATATOR = '/';

    /** Returns the graph of the Properties. */
    Graph<TIME> graph();

    /** Returns the key of this Properties, within it's parent ("" for root) */
    String localKey();

    /** Returns the keys matching the given value, or empty list if not found. */
    List<String> query(final Filter query);

    /** Returns the keys matching the given value, or empty list if not found. */
    List<String> keysOf(final Object value);

    /** Returns true if the value was found (!keysOf(value).isEmpty()). */
    boolean contains(final Object value);

    /**
     * Returns the property value, if any. Null if absent.
     * Generators are executed, if executeGenerators is true.
     */
    <E> E findRaw(final String path, final boolean executeGenerators);

    /**
     * Returns the property value, if any, or the default value.
     * An exception is thrown, if the property exists, but has the wrong type.
     * Generators are executed (including Links).
     */
    <E> E find(final String path, final Class<E> type, final E defaultValue);

    /**
     * Returns the property value, if any, or null.
     * An exception is thrown, if the property exists, but has the wrong type.
     * Generators are executed (including Links).
     */
    <E> E find(final String path, final Class<E> type);

    /**
     * Returns the property value, if present, or throw an exception.
     * An exception is also thrown, if the property exists, but has the wrong type.
     * Generators are executed (including Links).
     */
    <E> E get(final String path, final Class<E> type);

    /**
     * Sets a property "immediately". Setter is usually "self".
     *
     * Note that if the Root implements prioritisation of writes, the new value
     * might get ignored, in the case that this property was set before by a
     * writer with a higher priority.
     */
    void set(final Properties<TIME> setter, final String path,
            final Object value);

    /**
     * Sets a property at some point in time. Setter is usually "self".
     * If when is null, or in the past, the changes are going to be performed
     * immediately. Otherwise, they will be buffered, until the time comes.
     *
     * Time "in the past" is handled as the current time would be.
     *
     * Note that if the Root implements prioritisation of writes, the new value
     * might get ignored, in the case that this property was set before by a
     * writer with a higher priority. Use forceWrite to force overwrite.
     */
    void set(final Properties<TIME> setter, final String path,
            final Object value, final TIME when, final boolean forceWrite);

    /** Clears the list back to empty, with optional filter. */
    void clear(final Properties<TIME> setter, final Filter query);

    // List-oriented processing ...

    /**
     * Search all the property keys, and find the highest integer property key,
     * for all keys >= 0. Then adds one. Returns 0 when no integer property exists.
     * Does not consider keys with a null value.
     */
    String nextIndex();

    /** Returns true if the list is nextIndex() == "0". */
    boolean isEmptyList();

    /**
     * Iterates over all *indexed* values, and fill them in an array.
     * Fails if a value does not match the given type. All nulls are dropped.
     * If onlyIndexed is true, only "indexed" properties are searched,
     * otherwise all of them.
     */
    <E> E[] listValues(final Class<E> expectedType, final boolean onlyIndexed);

    /**
     * Calls listValues() on a child if present, otherwise returns empty array.
     * All nulls are dropped.
     * If onlyIndexed is true, only "indexed" properties are searched,
     * otherwise all of them.
     */
    <E> E[] listChildValues(final String property, final Class<E> expectedType,
            final boolean onlyIndexed);
}
