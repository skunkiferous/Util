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
package com.blockwithme.util.proto.properties.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.blockwithme.util.proto.properties.Filter;
import com.blockwithme.util.proto.properties.Generator;
import com.blockwithme.util.proto.properties.Graph;
import com.blockwithme.util.proto.properties.Properties;

/**
 * Base class for Properties.
 *
 * @author monster
 */
public class PropertiesImpl<TIME extends Comparable<TIME>> implements
        Properties<TIME> {

    /** Keeps a record of the setter with the value, so the strongest setter wins. */
    private static final class SetterValue<TIME extends Comparable<TIME>> {
        public Properties<TIME> setter;
        public Object value;
    }

    /** The root path. */
    protected static final String ROOT_PATH = String.valueOf(SEPATATOR);

    /** The local key. */
    private final String localKey;

    /** The graph. */
    private final ImplGraph<TIME> graph;

    /** All the own properties. */
    private final TreeMap<String, SetterValue<TIME>> properties = new TreeMap<>(
            NumbersLastStringComparator.CMP);

    /** Validates a local key. */
    public static void checkLocalKey(final String localKey,
            final String designation, final String fullPath) {
        if (localKey == null) {
            throw new IllegalArgumentException(designation + " is null");
        }
        if (localKey.isEmpty()) {
            throw new IllegalArgumentException(designation + " is empty");
        }
        for (final char c : localKey.toCharArray()) {
            final boolean lower = (c >= 'a') && (c <= 'z');
            final boolean upper = (c >= 'A') && (c <= 'Z');
            final boolean digit = (c >= '0') && (c <= '9');
            final boolean other = ((c == '_') || (c == '$'));
            if (!(lower || upper || digit) || other) {
                throw new IllegalArgumentException(designation + "(" + fullPath
                        + ") contains illegal character: '" + c + "'");
            }
        }
    }

    /** Validates path. */
    public static void checkPath(final String path, final String designation) {
        Objects.requireNonNull(path, designation);
        if (ROOT_PATH.equals(path)) {
            throw new IllegalArgumentException(designation + " cannot only be "
                    + ROOT_PATH);
        }
        String rest = path.startsWith(ROOT_PATH) ? path.substring(1) : path;
        int index = rest.indexOf(SEPATATOR);
        while (index > 0) {
            final String localKey = rest.substring(0, index);
            if (localKey.isEmpty()) {
                throw new IllegalArgumentException("component of "
                        + designation + " " + path + " is empty");
            }
            checkLocalKey(localKey, designation, path);
            rest = rest.substring(index + 1);
            index = rest.indexOf(SEPATATOR);
        }
        if (rest.isEmpty()) {
            throw new IllegalArgumentException(designation
                    + " cannot end with " + ROOT_PATH);
        }
        checkLocalKey(rest, designation, path);
    }

    /**
     * Returns true if the key is a local key, and valid.
     * Returns false if not a local key.
     * Throws an exception if local key but not valid.
     */
    public static boolean isLocalKey(final String path,
            final String designation, final String fullPath) {
        final boolean local = (Objects.requireNonNull(path, designation)
                .indexOf(SEPATATOR) < 0);
        if (local) {
            checkLocalKey(path, designation, fullPath);
        }
        return local;
    }

    /**
     * Returns a common path prefix.
     * We only match "whole" local keys, so /abc/def and /abc/deg must
     * return /abc/, not /abc/de
     */
    public static String getCommonPrefix(final String path1, final String path2) {
        final int len1 = path1.length();
        final int len2 = path2.length();
        final int max = Math.min(len1, len2);
        int end = 0;
        int lastSep = -1;
        while (end < max) {
            final char c1 = path1.charAt(end);
            final char c2 = path2.charAt(end);
            if (c1 != c2) {
                break;
            }
            if (c1 == SEPATATOR) {
                lastSep = end;
            }
            end++;
        }
        final boolean end1OrSep = (end == len1)
                || (path1.charAt(end) == SEPATATOR);
        final boolean end2OrSep = (end == len2)
                || (path2.charAt(end) == SEPATATOR);
        if (!(end1OrSep && end2OrSep)) {
            end = lastSep;
        }
        return path1.substring(0, end);
    }

    /** Returns the common ancestor type. */
    @SuppressWarnings("unchecked")
    public static Class<? extends Properties<?>> ancestor(
            final Class<? extends Properties<?>> type, final Object value) {
        if (value != null) {
            final Class<? extends Properties<?>> ctype = (Class<? extends Properties<?>>) value
                    .getClass();
            if (type == null) {
                return ctype;
            } else if (type != ctype) {
                Class<? extends Properties<?>> t1 = type;
                Class<? extends Properties<?>> t2 = ctype;
                while (true) {
                    if (t1.isAssignableFrom(t2)) {
                        return t1;
                    }
                    if (t2.isAssignableFrom(t1)) {
                        return t2;
                    }
                    t1 = (Class<? extends Properties<?>>) t1.getSuperclass();
                    t2 = (Class<? extends Properties<?>>) t2.getSuperclass();
                }
            }
        }
        return type;
    }

    /** Constructs a PropertiesImpl. */
    public PropertiesImpl(final ImplGraph<TIME> graph, final String localKey) {
        this(graph, localKey, null);
    }

    /** Constructs a PropertiesImpl. */
    public PropertiesImpl(final ImplGraph<TIME> graph, final String localKey,
            final TIME when) {
        this.graph = Objects.requireNonNull(graph);
        checkLocalKey(localKey, "localKey", localKey);
        this.localKey = localKey;
    }

    /** toString */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(256);
        buf.append(getClass().getSimpleName());
        for (final String prop : this) {
            buf.append(", ").append(prop).append("=");
            final Object value = findLocalRaw(prop);
            if (value instanceof Properties<?>) {
                buf.append(value.getClass().getSimpleName());
            } else {
                buf.append(value);
            }
        }
        buf.append(")");
        return buf.toString();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#localKey()
     */
    @Override
    public final String localKey() {
        return localKey;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#root()
     */
    @Override
    public Graph<TIME> graph() {
        return graph;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#find(java.lang.String, java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <E> E find(final String path, final Class<E> type,
            final E defaultValue) {
        Objects.requireNonNull(type, "type");
        final Object obj = findRaw(path, type, true);
        if (obj == null) {
            return defaultValue;
        }
        if (!type.isInstance(obj)) {
            throw new IllegalStateException("Property '" + path + "' type is: "
                    + obj.getClass().getName() + " but expected type is: "
                    + type);
        }
        return (E) obj;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#find(java.lang.String, java.lang.Class)
     */
    @Override
    public final <E> E find(final String path, final Class<E> type) {
        return find(path, type, null);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#get(java.lang.String, java.lang.Class)
     */
    @Override
    public final <E> E get(final String path, final Class<E> type) {
        final E result = find(path, type, null);
        if (result == null) {
            throw new IllegalStateException("Property '" + path
                    + "' not found!");
        }
        return result;
    }

    /**
     * If value is not a generator, it does not check the type, and just returns it.
     * If it is a generator, then the generator is called.
     */
    private static Object resolve(final Object value,
            final PropertiesImpl<?> owner, final String localKey,
            final Class<?> type, final boolean executeGenerators) {
        if (executeGenerators && (value instanceof Generator)) {
            final Generator gen = (Generator) value;
            final Object result = gen.generate(owner, localKey, type);
            if (result instanceof Generator) {
                throw new IllegalStateException("Generator "
                        + gen.getClass().getName() + " for property "
                        + localKey + " returned another generator: "
                        + result.getClass().getName());
            }
            return result;
        }
        return value;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#findRaw(java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    private final Object findRaw(final String path, final Class<?> type,
            final boolean executeGenerators) {
        if (isLocalKey(path, "path", path)) {
            return resolve(findLocalRaw(path), this, path, type,
                    executeGenerators);
        }
        PropertiesImpl<TIME> prop = this;
        String relPath = path;
        if (path.charAt(0) == SEPATATOR) {
            // Absolute path ...
            prop = (PropertiesImpl<TIME>) graph();
            relPath = path.substring(1);
            if (relPath.isEmpty()) {
                return prop;
            }
        }
        while (true) {
            // Property from ancestor
            if (isLocalKey(relPath, "path component", path)) {
                return resolve(prop.findLocalRaw(relPath), prop, relPath, type,
                        executeGenerators);
            }
            // Not ancestor; go back down again ...
            final int index = relPath.indexOf(SEPATATOR);
            final String head = relPath.substring(0, index);
            // We cannot use the type here, because we are not at the end of the path.
            final Object value = resolve(prop.findLocalRaw(head), prop, head,
                    null, executeGenerators);
            if (value == null) {
                return null;
            }
            if (value instanceof PropertiesImpl) {
                prop = (PropertiesImpl<TIME>) value;
                relPath = relPath.substring(index + 1);
            } else {
                throw new IllegalArgumentException("Encontered "
                        + value.getClass().getName()
                        + " while expecting a Properties");
            }
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#findRaw(java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <E> E findRaw(final String path,
            final boolean executeGenerators) {
        return (E) findRaw(path, null, executeGenerators);
    }

    /**
     * Only the parent of Properties can directly reference it, so we have to
     * convert reference to Properties into links (or generated Properties).
     */
    protected Object unresolve(final Properties<?> setter, final Object value,
            final PropertiesImpl<?> parent, final String localKey,
            final TIME when) {
        if (value instanceof Properties<?>) {
            // Direct reference: either direct child, or must become link
            final Properties<?> prop = (Properties<?>) value;
            if (parent.graph() != prop.graph()) {
                throw new IllegalArgumentException("The "
                        + value.getClass().getName()
                        + " does not have a common ancestor!");
            }
        }
        if ((value instanceof Iterable<?>) && !(value instanceof Properties<?>)) {
            for (final Object content : (Iterable<?>) value) {
                if (content instanceof Properties) {
                    throw new IllegalArgumentException(
                            "Collections cannot contain Properties: Use ListProperties instead");
                }
            }
        } else if (value instanceof Object[]) {
            // object array
            for (final Object content : (Object[]) value) {
                if (content instanceof Properties) {
                    throw new IllegalArgumentException(
                            "Arrays cannot contain Properties: Use ListProperties instead");
                }
            }
        } else if (value instanceof Map<?, ?>) {
            final Map<?, ?> map = (Map<?, ?>) value;
            for (final Object key : map.keySet()) {
                if (key instanceof Properties) {
                    throw new IllegalArgumentException(
                            "Map keyes cannot contain Properties");
                }
            }
            for (final Object key : map.values()) {
                if (key instanceof Properties) {
                    throw new IllegalArgumentException(
                            "Map values cannot contain Properties");
                }
            }
        }
        return value;
    }

    /** Sets a property. */
    @Override
    public final void set(final Properties<TIME> setter, final String path,
            final Object value, final TIME when, final boolean forceWrite) {
        Objects.requireNonNull(setter, "setter");
        if (isLocalKey(path, "path", path)) {
            // direct property. Must only unresolve
            setLocalProperty(setter, path,
                    unresolve(setter, value, this, path, when), when,
                    forceWrite);
        } else {
            // must first find target, then unresolve
            final int index = path.lastIndexOf(SEPATATOR);
            final String ancestorPath = path.substring(0, index);
            final String localKey = path.substring(index + 1);
            @SuppressWarnings("unchecked")
            final PropertiesImpl<TIME> ancestor = get(ancestorPath,
                    PropertiesImpl.class);
            ancestor.setLocalProperty(setter, localKey,
                    unresolve(setter, value, ancestor, localKey, when), when,
                    forceWrite);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#set(java.lang.String, java.lang.Object)
     */
    @Override
    public final void set(final Properties<TIME> setter, final String path,
            final Object value) {
        set(setter, path, value, null, false);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#isEmptyList()
     */
    @Override
    public final boolean isEmptyList() {
        return "0".equals(nextIndex());
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#contains(java.lang.Object)
     */
    @Override
    public final boolean contains(final Object value) {
        return !keysOf(value).isEmpty();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#keysOf(java.lang.Object)
     */
    @Override
    public final List<String> keysOf(final Object value) {
        return query(new Filter() {
            @Override
            public boolean accept(final String key, final Object obj) {
                return Objects.equals(value, obj);
            }
        });
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#nextIndex()
     */
    @Override
    public final String nextIndex() {
        int last = -1;
        for (final String key : this) {
            try {
                final int i = Integer.parseInt(key, 10);
                if (i > last) {
                    final Object value = findRaw(key, true);
                    if (value != null) {
                        last = i;
                    }
                }
            } catch (final Exception e) {
                // NOP
            }
        }
        return String.valueOf(last + 1);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#query(com.blockwithme.properties.Filter)
     */
    @Override
    public final List<String> query(final Filter query) {
        List<String> result = null;
        for (final String key : this) {
            if (query.accept(key, findRaw(key, true))) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(key);
            }
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#clear(com.blockwithme.properties.Properties, com.blockwithme.properties.Filter)
     */
    @Override
    public final void clear(final Properties<TIME> setter, final Filter query) {
        if (query == null) {
            for (final String key : this) {
                set(setter, key, null);
            }
        } else {
            for (final String key : query(query)) {
                set(setter, key, null);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#listValues(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <E> E[] listValues(final Class<E> expectedType,
            final boolean onlyIndexed) {
        final ArrayList<E> result = new ArrayList<>();
        if (onlyIndexed) {
            for (final String key : this) {
                try {
                    final int index = Integer.parseInt(key, 10);
                    final E obj = find(key, expectedType);
                    if (obj != null) {
                        // ArrayList doesn't have set size, so we need to grow it
                        while (result.size() <= index) {
                            result.add(null);
                        }
                        result.set(index, obj);
                    }
                } catch (final NumberFormatException e) {
                    // NOP
                }
            }
            // Drop all null
            while (result.remove(null)) {
                // NOP
            }
        } else {
            // By default, we get string order of properties, so result is sorted.
            for (final String key : this) {
                try {
                    result.add(get(key, expectedType));
                } catch (final Exception e) {
                    // NOP
                }
            }
        }
        return result.toArray((E[]) Array.newInstance(expectedType,
                result.size()));
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Properties#listChildValues(java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <E> E[] listChildValues(final String proerpty,
            final Class<E> expectedType, final boolean onlyIndexed) {
        final Properties<TIME> prop = find(proerpty, Properties.class);
        if (prop != null) {
            return prop.listValues(expectedType, onlyIndexed);
        }
        return (E[]) Array.newInstance(expectedType, 0);
    }

    /**
     * Returns the property value, if any. Null if absent.
     */
    protected Object findLocalRaw(final String localKey) {
        final SetterValue<TIME> sv = properties.get(localKey);
        return (sv == null) ? null : sv.value;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<String> iterator() {
        return properties.keySet().iterator();
    }

    /** Returns true, if this is a built-in property. */
    protected boolean builtIn(final String localKey) {
        return false;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.impl.PropertiesImpl#setLocalProperty(java.lang.String, java.lang.Object)
     */
    protected void setLocalProperty(final Properties<TIME> setter,
            final String localKey, final Object value, final TIME when,
            final boolean forceWrite) {
        checkLocalKey(localKey, "localKey", localKey);
        if (builtIn(localKey)) {
            throw new UnsupportedOperationException(
                    "Cannot remove a built-in property: " + localKey);
        }
        final ImplGraph<TIME> root = (ImplGraph<TIME>) graph();
        if (when != null) {
            // Future property (when past/preset, then Root will re-set using null)
            root.onFutureChange(setter, this, localKey, value, forceWrite, when);
        } else {
            // We never remove values, because we need to keep track of the
            // setter priority, even if the value is null.
            SetterValue<TIME> sv = properties.get(localKey);
            if (sv == null) {
                // First set
                final Object oldValue = null;
                sv = new SetterValue<>();
                sv.setter = setter;
                sv.value = value;
                properties.put(localKey, sv);
                if (!Objects.equals(oldValue, value)) {
                    root.onChange(setter, this, localKey, oldValue, value);
                }
                // Else check if set required
            } else if (forceWrite || !root.lowerPriority(setter, sv.setter)) {
                final Object oldValue = sv.value;
                sv.setter = setter;
                sv.value = value;
                if (!Objects.equals(oldValue, value)) {
                    root.onChange(setter, this, localKey, oldValue, value);
                }
            }
        }
    }
}
