/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;

import java.util.Iterator;

/**
 * A {@code Properties} object is a {@code Hashtable} where the keys and values
 * must be {@code String}s. Each property can have a default
 * {@code Properties} list which specifies the default
 * values to be used when a given key is not found in this {@code Properties}
 * instance.
 *
 * @see Hashtable
 * @see java.lang.System#getProperties
 */
public class Properties extends HashMap<String, String> {

    /**
     * The default values for keys not found in this {@code Properties}
     * instance.
     */
    protected Properties defaults;

    /**
     * Constructs a new {@code Properties} object.
     */
    public Properties() {
        super();
    }

    /**
     * Constructs a new {@code Properties} object using the specified default
     * {@code Properties}.
     *
     * @param properties
     *            the default {@code Properties}.
     */
    public Properties(Properties properties) {
        defaults = properties;
    }

    /**
     * Searches for the property with the specified name. If the property is not
     * found, the default {@code Properties} are checked. If the property is not
     * found in the default {@code Properties}, {@code null} is returned.
     *
     * @param name
     *            the name of the property to find.
     * @return the named property value, or {@code null} if it can't be found.
     */
    public String getProperty(String name) {
        String property = super.get(name);
        if (property == null && defaults != null) {
            property = defaults.getProperty(name);
        }
        return property;
    }

    /**
     * Searches for the property with the specified name. If the property is not
     * found, it looks in the default {@code Properties}. If the property is not
     * found in the default {@code Properties}, it returns the specified
     * default.
     *
     * @param name
     *            the name of the property to find.
     * @param defaultValue
     *            the default value.
     * @return the named property value.
     */
    public String getProperty(String name, String defaultValue) {
        String property = super.get(name);
        if (property == null && defaults != null) {
            property = defaults.getProperty(name);
        }
        if (property == null) {
            return defaultValue;
        }
        return property;
    }

    /**
     * Returns all of the property names that this {@code Properties} object
     * contains.
     *
     * @return an {@code Enumeration} containing the names of all properties
     *         that this {@code Properties} object contains.
     */
    public Enumeration<String> propertyNames() {
        final Iterator<String> iter = keySet().iterator();
        return new Enumeration<String>() {
            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            public String nextElement() {
                return iter.next();
            }
        };
    }

    /**
     * Answers a set of keys in this property list whose key and value are
     * strings.
     *
     * @return a set of keys in the property list
     *
     * @since 1.6
     */
    public Set<String> stringPropertyNames() {
        return Collections.unmodifiableSet(new HashSet<String>(keySet()));
    }

    /**
     * Maps the specified key to the specified value. If the key already exists,
     * the old value is replaced. The key and value cannot be {@code null}.
     *
     * @param name
     *            the key.
     * @param value
     *            the value.
     * @return the old value mapped to the key, or {@code null}.
     */
    public Object setProperty(String name, String value) {
        return put(name, value);
    }
}
