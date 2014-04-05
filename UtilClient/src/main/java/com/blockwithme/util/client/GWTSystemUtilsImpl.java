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

    /** DateTimeFormat used by utcImpl(). */
    private static final DateTimeFormat UTC = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** TimeZone used by utcImpl(). */
    private static final TimeZone UTC_TZ = TimeZone.createTimeZone(0);

    /** DateTimeFormat used by localImpl(). */
    private static final DateTimeFormat LOCAL = DateTimeFormat
            .getFormat("yyyy-MM-dd HH:mm:ss.SSS");

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
     * @see com.blockwithme.util.shared.SystemUtils#forNameImpl(java.lang.String, java.lang.Class)
     */
    @Override
    protected Class<?> forNameImpl(final String name, final Class<?> otherClass) {
        return forNameImpl(name);
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
}
