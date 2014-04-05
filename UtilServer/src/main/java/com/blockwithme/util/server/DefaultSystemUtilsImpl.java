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
package com.blockwithme.util.server;

import java.lang.reflect.Array;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import com.blockwithme.util.shared.ConcurrentMap;
import com.blockwithme.util.shared.SystemUtils;

/**
 * Default implementation of SystemUtils for "standard" Java platforms.
 *
 * @author monster
 */
public class DefaultSystemUtilsImpl extends SystemUtils {

    /** Implements com.blockwithme.util.ConcurrentMap */
    private static final class DefaultConcurrentMap<K, V> extends
            ConcurrentHashMap<K, V> implements ConcurrentMap<K, V> {
        /**  */
        private static final long serialVersionUID = 1L;

        /** Creates and returns a ConcurrentMap. */
        public DefaultConcurrentMap() {
            // NOP
        }

        /** Creates and returns a ConcurrentMap, with an initialCapacity. */
        public DefaultConcurrentMap(final int initialCapacity) {
            super(initialCapacity);
        }

        /** Creates and returns a ConcurrentMap, with an initialCapacity and concurrencyLevel. */
        public DefaultConcurrentMap(final int initialCapacity,
                final int concurrencyLevel) {
            super(initialCapacity, 0.75f, concurrencyLevel);
        }
    }

    /** Used by localImpl(). */
    private static final SimpleDateFormat LOCAL = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    /** Used by utcImpl(). */
    private static final SimpleDateFormat UTC = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    static {
        UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#forNameImpl(java.lang.String)
     */
    @Override
    protected Class<?> forNameImpl(final String name) {
        try {
            return Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#forNameImpl(java.lang.String, java.lang.Class)
     */
    @Override
    protected Class<?> forNameImpl(final String name, final Class<?> otherClass) {
        try {
            return otherClass.getClassLoader().loadClass(name);
        } catch (final ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#getSimpleNameImpl(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected String getSimpleNameImpl(final Class c) {
        return c.getSimpleName();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isAssignableFromImpl(java.lang.Class, java.lang.Class)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected boolean isAssignableFromImpl(final Class c1, final Class c2) {
        return c1.isAssignableFrom(c2);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isInstanceImpl(java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected boolean isInstanceImpl(final Class c, final Object obj) {
        return c.isInstance(obj);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newInstanceImpl(java.lang.Class)
     */
    @Override
    protected <T> T newInstanceImpl(final Class<T> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#getArrayElementImpl(java.lang.Object, int)
     */
    @Override
    protected Object getArrayElementImpl(final Object array, final int index) {
        return Array.get(array, index);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#getArrayLengthImpl(java.lang.Object)
     */
    @Override
    protected int getArrayLengthImpl(final Object array) {
        return Array.getLength(array);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newArrayInstanceImpl(java.lang.Class, int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Object newArrayInstanceImpl(final Class componentType,
            final int length) {
        return Array.newInstance(componentType, length);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#setArrayElementImpl(java.lang.Object, int, java.lang.Object)
     */
    @Override
    protected void setArrayElementImpl(final Object array, final int index,
            final Object value) {
        Array.set(array, index, value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl()
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl() {
        return new DefaultConcurrentMap();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl(int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl(final int initialCapacity) {
        return new DefaultConcurrentMap(initialCapacity);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#newConcurrentMapImpl(int, int)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected ConcurrentMap newConcurrentMapImpl(final int initialCapacity,
            final int concurrencyLevel) {
        return new DefaultConcurrentMap(initialCapacity, concurrencyLevel);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#getPropertyImpl(java.lang.String, java.lang.String)
     */
    @Override
    protected String getPropertyImpl(final String key, final String def) {
        return System.getProperty(key, def);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#setPropertyImpl(java.lang.String, java.lang.String)
     */
    @Override
    protected String setPropertyImpl(final String key, final String value) {
        return System.setProperty(key, value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#clearPropertyImpl(java.lang.String)
     */
    @Override
    protected String clearPropertyImpl(final String key) {
        return System.clearProperty(key);
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
     * @see com.blockwithme.util.SystemUtils#utcImpl(java.util.Date)
     */
    @Override
    protected String utcImpl(final Date date) {
        synchronized (UTC) {
            return UTC.format(date);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#localImpl(java.util.Date)
     */
    @Override
    protected String localImpl(final Date date) {
        synchronized (LOCAL) {
            return LOCAL.format(date);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isGWTClientImpl()
     */
    @Override
    protected boolean isGWTClientImpl() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#currentThreadNameImpl()
     */
    @Override
    protected String currentThreadNameImpl() {
        return Thread.currentThread().getName();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#floatToRawIntBitsImpl(float)
     */
    @Override
    protected int floatToRawIntBitsImpl(final float value) {
        return Float.floatToRawIntBits(value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#doubleToRawLongBitsImpl(double)
     */
    @Override
    protected long doubleToRawLongBitsImpl(final double value) {
        return Double.doubleToRawLongBits(value);
    }
}
