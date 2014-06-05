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

import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.base.WeakKeyMap;

/**
 * Default implementation of SystemUtils for "standard" Java platforms.
 *
 * @author monster
 */
public class DefaultSystemUtilsImpl extends SystemUtils {

    /** Implements WeakKeyMap<KEY, VALUE> .*/
    private static final class MyWeakKeyMap<KEY, VALUE> extends
            WeakHashMap<KEY, VALUE> implements WeakKeyMap<KEY, VALUE> {

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#clear()
         */
        @Override
        public synchronized void clear() {
            super.clear();
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#containsKey(java.lang.Object)
         */
        @Override
        public synchronized boolean containsKey(final Object key) {
            return super.containsKey(key);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#remove(java.lang.Object)
         */
        @Override
        public synchronized VALUE remove(final Object key) {
            return super.remove(key);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#get(java.lang.Object)
         */
        @Override
        public synchronized VALUE get(final Object key) {
            return super.get(key);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#put(java.lang.Object, java.lang.Object)
         */
        @Override
        public synchronized VALUE put(final KEY key, final VALUE value) {
            if (key == null) {
                throw new NullPointerException("key");
            }
            if (value == null) {
                return super.remove(key);
            }
            return super.put(key, value);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.base.WeakKeyMap#putAll(java.util.Map)
         */
        @Override
        public synchronized void putAll(
                final Map<? extends KEY, ? extends VALUE> m) {
            for (final java.util.Map.Entry<? extends KEY, ? extends VALUE> e : m
                    .entrySet()) {
                final KEY key = e.getKey();
                if (key == null) {
                    throw new NullPointerException("key");
                }
                final VALUE value = e.getValue();
                if (value == null) {
                    super.remove(key);
                } else {
                    super.put(key, value);
                }
            }
        }
    }

    /** Logger */
    private static final Logger LOG = Logger
            .getLogger(DefaultSystemUtilsImpl.class.getName());

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
     * @see com.blockwithme.util.base.SystemUtils#reportUncaughtExceptionImpl(java.lang.Throwable)
     */
    @Override
    protected void reportUncaughtExceptionImpl(final Throwable e) {
        LOG.log(Level.SEVERE, "Uncaught Exception", e);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#newWeakKeyMapImpl()
     */
    @Override
    protected <KEY, VALUE> WeakKeyMap<KEY, VALUE> newWeakKeyMapImpl() {
        return new MyWeakKeyMap<KEY, VALUE>();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#highResTimeMillisImpl()
     */
    @Override
    protected double highResTimeMillisImpl() {
        return System.nanoTime() / 1000000.0;
    }
}
