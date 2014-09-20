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
package com.blockwithme.util.proto;

import groovy.lang.GroovyShell;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.blockwithme.util.shared.annotations.E;
import com.blockwithme.util.shared.annotations.KVMap;

/**
 * Helper class for the KVMap.
 *
 * TODO: Test!
 *
 * @author monster
 */
public class KVMapHelper {
    /** Groovy stuff */
    private static final class Groovy {
        /** Groovy itself. */
        private final GroovyShell SHELL = new GroovyShell();

        /** Evaluates a Groovy script. */
        public Object evaluate(final String script) {
            return SHELL.evaluate(script);
        }

        /** The singleton */
        public static final Groovy GROOVY = new Groovy();
    }

    /**
     * Returns the KVMap annotation, if any, of this AnnotatedElement, or null.
     */
    public static KVMap findMap(final AnnotatedElement elem) {
        return Objects.requireNonNull(elem, "elem").getAnnotation(KVMap.class);
    }

    /** Returns the KVMap annotation, if any, or null. */
    public static KVMap findClassMap(final String name) {
        Objects.requireNonNull(name, "name");
        try {
            return findMap(Class.forName(name));
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("ClassNotFound: " + name);
        }
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing. Null map returns null.
     */
    public static String[] findRawValues(final String owner, final KVMap map,
            final Object key) {
        final String tmp = Objects.requireNonNull(key, "key").toString();
        final String keyStr = Objects.requireNonNull(tmp, "key.toString()");
        if (map != null) {
            final E[] entries = map.value();
            if (entries != null) {
                String[] result = null;
                for (final E e : entries) {
                    final String[] array = e.value();
                    if (array != null) {
                        final int len = array.length;
                        if (len > 0) {
                            final String ekey = array[0];
                            if (keyStr.equals(ekey)) {
                                if (result == null) {
                                    result = new String[len - 1];
                                    System.arraycopy(array, 1, result, 0,
                                            len - 1);
                                } else {
                                    throw new IllegalStateException(
                                            "Multiple occurences of " + key
                                                    + " in " + owner);
                                }
                            }
                        }
                    }
                }
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing. Null map returns null.
     */
    public static String[] findRawValues(final AnnotatedElement elem,
            final Object key) {
        return findRawValues(elem.toString(), findMap(elem), key);
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     */
    public static String[] findClassRawValues(final String className,
            final Object key) {
        return findRawValues(className, findClassMap(className), key);
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static String findRawValue(final String owner, final KVMap map,
            final Object key) {
        final String[] values = findRawValues(owner, map, key);
        if (values != null) {
            final int len = values.length;
            if (len == 1) {
                return values[0];
            }
            if (len > 1) {
                throw new IllegalStateException(len + " values for " + owner
                        + "/" + key);
            }
        }
        return null;
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static String findRawValue(final AnnotatedElement elem,
            final Object key) {
        return findRawValue(elem.toString(), findMap(elem), key);
    }

    /**
     * Returns the raw value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static String findClassRawValue(final String className,
            final Object key) {
        return findRawValue(className, findClassMap(className), key);
    }

    /** Find all keyes in the Map. */
    public static String[] findKeyes(final KVMap map) {
        if (map != null) {
            final Set<String> result = new HashSet<>();
            final E[] entries = map.value();
            if (entries != null) {
                for (final E e : entries) {
                    final String[] array = e.value();
                    if (array != null) {
                        final int len = array.length;
                        if (len > 0) {
                            result.add(array[0]);
                        }
                    }
                }
            }
            return result.toArray(new String[result.size()]);
        }
        return null;
    }

    /** Find all keyes in the Map. */
    public static String[] findKeyes(final AnnotatedElement elem) {
        return findKeyes(findMap(elem));
    }

    /** Returns the content of the KVMap as a raw java.util.Map. */
    public static Map<String, String[]> getAsRawMap(final KVMap map) {
        if (map != null) {
            final Map<String, String[]> result = new HashMap<String, String[]>();
            final E[] entries = map.value();
            if (entries != null) {
                for (final E e : entries) {
                    final String[] array = e.value();
                    if (array != null) {
                        final int len = array.length;
                        if (len > 0) {
                            final String key = array[0];
                            final String[] value = new String[len - 1];
                            System.arraycopy(array, 1, value, 0, len - 1);
                            result.put(key, value);
                        }
                    }
                }
            }
            return result;
        }
        return null;
    }

    /** Returns the content of the KVMap as a raw java.util.Map. */
    public static Map<String, String[]> getAsRawMap(final AnnotatedElement elem) {
        return getAsRawMap(findMap(elem));
    }

    /** Evaluates the strings as Groovy scripts */
    private static Object evaluate(final String script) {
        if (script == null) {
            return null;
        }
        if (script.isEmpty()) {
            return "";
        }
        return Groovy.GROOVY.evaluate(script);
    }

    /** Evaluates the strings as Groovy scripts */
    private static Object[] evaluate(final String[] scripts) {
        if (scripts == null) {
            return null;
        }
        final Object[] result = new Object[scripts.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = evaluate(scripts[i]);
        }
        return result;
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing. Null map returns null.
     */
    public static Object[] findGroovyValues(final String owner,
            final KVMap map, final Object key) {
        return evaluate(findRawValues(owner, map, key));
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     */
    public static Object[] findClassGroovyValues(final String className,
            final Object key) {
        return evaluate(findClassRawValues(className, key));
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static Object findGroovyValue(final String owner, final KVMap map,
            final Object key) {
        return evaluate(findRawValue(owner, map, key));
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static Object findGroovyValue(final AnnotatedElement elem,
            final Object key) {
        return evaluate(findRawValue(elem, key));
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing.
     * If the array contains more then one value, an exception is thrown.
     */
    public static Object findClassGroovyValue(final String className,
            final Object key) {
        return evaluate(findClassRawValue(className, key));
    }

    /**
     * Returns the Groovy value of a key. Any object key is converted to a String.
     * Null is returned if the key (or value) is missing. Null map returns null.
     */
    public static Object[] findGroovyValues(final AnnotatedElement elem,
            final Object key) {
        return evaluate(findRawValues(elem.toString(), findMap(elem), key));
    }

    /** Returns the content of the KVMap as a Groovy java.util.Map. */
    public static Map<String, Object[]> getAsGroovyMap(final KVMap map) {
        final Map<String, String[]> ssaMap = getAsRawMap(map);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Map<String, Object[]> result = (Map) ssaMap;
        if (ssaMap != null) {
            for (final String key : ssaMap.keySet().toArray(
                    new String[ssaMap.size()])) {
                result.put(key, evaluate(ssaMap.get(key)));
            }
        }
        return result;
    }

    /** Returns the content of the KVMap as a Groovy java.util.Map. */
    public static Map<String, Object[]> getAsGroovyMap(
            final AnnotatedElement elem) {
        return getAsGroovyMap(findMap(elem));
    }
}
