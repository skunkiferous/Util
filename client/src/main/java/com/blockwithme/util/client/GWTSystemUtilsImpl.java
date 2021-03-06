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

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;

import javax.inject.Provider;

import com.badlogic.gwtref.client.ReflectionCache;
import com.badlogic.gwtref.client.Type;
import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.base.TimeSource;
import com.blockwithme.util.base.WeakKeyMap;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

/**
 * GWT implementation of SystemUtils.
 *
 * @author monster
 */
public class GWTSystemUtilsImpl extends SystemUtils {

    /** GWTTimeSource skips the double-to-long-to-double conversion. */
    private static final class GWTTimeSource implements TimeSource {
        /* (non-Javadoc)
         * @see com.blockwithme.util.base.TimeSource#currentTimeMillis()
         */
        @Override
        public native double currentTimeMillis() /*-{
			return (new Date()).getTime();
        }-*/;
    }

    /** DateTimeFormat used by utcImpl(). */
    private static final DateTimeFormat UTC = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** TimeZone used by utcImpl(). */
    private static final TimeZone UTC_TZ = TimeZone.createTimeZone(0);

    /** DateTimeFormat used by localImpl(). */
    private static final DateTimeFormat LOCAL = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** Check if JavaScript WeakMap is available. */
    private static native boolean isJSWeakMapAvailable()
    /*-{
		return !!window.WeakMap;
    }-*/;

    /** Initialize JavaScript high resolution time. */
    private static native void initHighResTime()
    /*-{
		Date.now = Date.now || function() {
			return new Date().getTime();
		};
		window.performance = window.performance || {};
		performance.now = (function() {
			return performance.now || performance.mozNow || performance.msNow
					|| performance.oNow || performance.webkitNow || Date.now;
		})();
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#forNameImpl(java.lang.String, java.lang.Class)
     */
    @Override
    protected Class<?> forNameImpl(final String name, final Class<?> otherClass) {
        return forNameImpl(name);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#forNameImpl(java.lang.String)
     */
    @Override
    protected Class<?> forNameImpl(final String name) {
        try {
            return ReflectionCache.forName(name).getClassOfType();
        } catch (final ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isAssignableFromImpl(java.lang.Class, java.lang.Class)
     */
    @SuppressWarnings({ "rawtypes" })
    @Override
    protected boolean isAssignableFromImpl(final Class c1, final Class c2) {
        final Type c1Type = ReflectionCache.getType(c1);
        final Type c2Type = ReflectionCache.getType(c2);
        return c2Type.isAssignableFrom(c1Type);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isInstanceImpl(java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected boolean isInstanceImpl(final Class c, final Object obj) {
        return (obj == null) ? false : isAssignableFromImpl(c, obj.getClass());
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#providersForImpl(java.lang.Class)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected <T> Provider<T>[] providersForImpl(final Class<T> clazz) {
        return new Provider[] { new Provider() {
            @Override
            public Object get() {
                try {
                    return ReflectionCache.getType(clazz).newInstance();
                } catch (final NoSuchMethodException e) {
                    throw new RuntimeException(
                            "Could not instansiate " + clazz, e);
                }
            }
        } };
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#utcImpl(java.util.Date)
     */
    @Override
    protected String utcImpl(final Date date) {
        return UTC.format(date, UTC_TZ);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#localImpl(java.util.Date)
     */
    @Override
    protected String localImpl(final Date date) {
        return LOCAL.format(date);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#reportUncaughtExceptionImpl(java.util.Throwable)
     */
    @Override
    protected void reportUncaughtExceptionImpl(final Throwable e) {
        com.google.gwt.core.client.GWT.reportUncaughtException(e);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#newWeakKeyMapImpl()
     */
    @Override
    protected <KEY, VALUE> WeakKeyMap<KEY, VALUE> newWeakKeyMapImpl() {
        if (isJSWeakMapAvailable()) {
            return new RealWeakKeyMap<KEY, VALUE>();
        }
        return new FakeWeakKeyMap<KEY, VALUE>();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.SystemUtils#highResTimeMillisImpl()
     */
    @Override
    protected native double highResTimeMillisImpl()
    /*-{
		return performance.now();
    }-*/;

    /** Constructor */
    public GWTSystemUtilsImpl() {
        initHighResTime();
        setTimeSource(new GWTTimeSource());
        // We use a "native" GWT Timer, because we want updates independent
        // of the application (game) cycle speed. We do this, because if some
        // processing *within* the application cycle takes long, it will still
        // see the time changing.
        new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                updateCurrentTimeMillis();
            }
        }.scheduleRepeating(CURRENT_TIME_MILLIS_UPDATE_INTERVAL);
    }
}
