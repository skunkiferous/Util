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

import com.badlogic.gwtref.client.ReflectionCache;
import com.badlogic.gwtref.client.Type;
import com.blockwithme.util.shared.SystemUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

/**
 * GWT implementation of SystemUtils.
 *
 * @author monster
 */
public class GWTSystemUtilsImpl extends SystemUtils {

    /** DateTimeFormat used by utcImpl(). */
    private static final DateTimeFormat UTC = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** TimeZone used by utcImpl(). */
    private static final TimeZone UTC_TZ = TimeZone.createTimeZone(0);

    /** DateTimeFormat used by localImpl(). */
    private static final DateTimeFormat LOCAL = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * I cannot call a method that takes or returns "long" from JavaScript.
     */
    @SuppressWarnings("unused")
    private static void updateCurrentTimeMillis2() {
        updateCurrentTimeMillis();
    }

    /** Causes updateCurrentTimeMillis() to be called repeatedly. */
    private static native void scheduleTimeUpdater() /*-{
		window
				.setInterval(
						function() {
							@com.blockwithme.util.client.GWTSystemUtilsImpl::updateCurrentTimeMillis2()();
						}, 4);
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.SystemUtils#isGWTClientImpl()
     */
    @Override
    protected boolean isGWTClientImpl() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#forNameImpl(java.lang.String, java.lang.Class)
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
     * @see com.blockwithme.util.SystemUtils#newInstanceImpl(java.lang.Class)
     */
    @Override
    protected <T> T newInstanceImpl(final Class<T> c) {
        return (T) ReflectionCache.getType(c).newInstance();
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
     * @see com.blockwithme.util.shared.SystemUtils#floatToRawIntBitsImpl(float)
     */
    @Override
    protected int floatToRawIntBitsImpl(final float value) {
        // TODO This messes up the data, if the value is a NaN :(
//      return Float.floatToIntBits(value);
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#doubleToRawLongBitsImpl(double)
     */
    @Override
    protected long doubleToRawLongBitsImpl(final double value) {
        // TODO This messes up the data, if the value is a NaN :(
//        return Double.doubleToLongBits(value);
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.shared.SystemUtils#reportUncaughtExceptionImpl(java.util.Throwable)
     */
    @Override
    protected void reportUncaughtExceptionImpl(final Throwable e) {
        GWT.reportUncaughtException(e);
    }

    /** Constructor */
    public GWTSystemUtilsImpl() {
        scheduleTimeUpdater();
    }
}
