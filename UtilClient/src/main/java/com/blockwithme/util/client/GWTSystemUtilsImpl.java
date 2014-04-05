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
package com.blockwithme.util.client;

import java.util.Date;
import java.util.HashMap;

import com.blockwithme.util.shared.ConcurrentMap;
import com.blockwithme.util.shared.SystemUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

/**
 * GWT implementation of SystemUtils.
 *
 * This class is missing all the methods related to Reflection. Those  will
 * be provided by libGDX's own Reflection API emulation.
 *
 * @author monster
 */
public abstract class GWTSystemUtilsImpl extends SystemUtils {

    /** Implements com.blockwithme.util.ConcurrentMap */
    private static final class FakeConcurrentMap<K, V> extends HashMap<K, V>
            implements ConcurrentMap<K, V> {
        /**  */
        private static final long serialVersionUID = 1L;

        /** Creates and returns a ConcurrentMap. */
        public FakeConcurrentMap() {
            // NOP
        }

        /** Creates and returns a ConcurrentMap, with an initialCapacity. */
        public FakeConcurrentMap(final int initialCapacity) {
            super(initialCapacity);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.shared.ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
         */
        @Override
        public V putIfAbsent(final K key, final V value) {
            if (!containsKey(key))
                return put(key, value);
            else
                return get(key);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.shared.ConcurrentMap#remove(java.lang.Object, java.lang.Object)
         */
        @Override
        public boolean remove(final Object key, final Object value) {
            if (containsKey(key) && get(key).equals(value)) {
                remove(key);
                return true;
            } else
                return false;
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.shared.ConcurrentMap#replace(java.lang.Object, java.lang.Object, java.lang.Object)
         */
        @Override
        public boolean replace(final K key, final V oldValue, final V newValue) {
            if (containsKey(key) && get(key).equals(oldValue)) {
                put(key, newValue);
                return true;
            } else
                return false;
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.shared.ConcurrentMap#replace(java.lang.Object, java.lang.Object)
         */
        @Override
        public V replace(final K key, final V value) {
            if (containsKey(key)) {
                return put(key, value);
            } else
                return null;
        }
    }

    /** DateTimeFormat used by utcImpl(). */
    private static final DateTimeFormat UTC = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** TimeZone used by utcImpl(). */
    private static final TimeZone UTC_TZ = TimeZone.createTimeZone(0);

    /** DateTimeFormat used by localImpl(). */
    private static final DateTimeFormat LOCAL = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** The System Properties :D */
    private final HashMap<String, String> systemProperties = new HashMap<String, String>();

    /** Constructor */
    public GWTSystemUtilsImpl() {
        // TODO Setup default values for System Properties
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl()
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl() {
        return new FakeConcurrentMap();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl(int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl(final int initialCapacity) {
        return new FakeConcurrentMap(initialCapacity);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl(int, int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl(final int initialCapacity,
            final int concurrencyLevel) {
        return new FakeConcurrentMap(initialCapacity);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#currentTimeMillisImpl()
     */
    @Override
    protected long currentTimeMillisImpl() {
        // TODO Make sure we return the *correct* time.
        return System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isGWTClientImpl()
     */
    @Override
    protected boolean isGWTClientImpl() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#getSimpleNameImpl(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected String getSimpleNameImpl(final Class c) {
        final String name = c.getName();
        final int dot = name.lastIndexOf('.');
        return (dot < 0) ? name : name.substring(dot + 1);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#forNameImpl(java.lang.String, java.lang.Class)
     */
    @Override
    protected Class<?> forNameImpl(final String name, final Class<?> otherClass) {
        return forNameImpl(name);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#getPropertyImpl(java.lang.String, java.lang.String)
     */
    @Override
    protected String getPropertyImpl(final String key, final String def) {
        final String result = systemProperties.get(key);
        return (result == null) ? def : result;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#setPropertyImpl(java.lang.String, java.lang.String)
     */
    @Override
    protected String setPropertyImpl(final String key, final String value) {
        return systemProperties.put(key, value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#clearPropertyImpl(java.lang.String)
     */
    @Override
    protected String clearPropertyImpl(final String key) {
        return systemProperties.remove(key);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#utcImpl(java.util.Date)
     */
    @Override
    protected String utcImpl(final Date date) {
        return UTC.format(date, UTC_TZ);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#localImpl(java.util.Date)
     */
    @Override
    protected String localImpl(final Date date) {
        return LOCAL.format(date);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#currentThreadNameImpl()
     */
    @Override
    protected String currentThreadNameImpl() {
        return "js-main";
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#floatToRawIntBitsImpl(float)
     */
    @Override
    protected int floatToRawIntBitsImpl(final float value) {
        return Float.floatToIntBits(value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#doubleToRawLongBitsImpl(double)
     */
    @Override
    protected long doubleToRawLongBitsImpl(final double value) {
        return Double.doubleToLongBits(value);
    }
}
