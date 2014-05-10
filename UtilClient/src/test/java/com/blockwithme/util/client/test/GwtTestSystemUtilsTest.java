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
package com.blockwithme.util.client.test;

import java.util.Date;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.base.TimeSource;

/**
 * Tests GWTSystemUtilsImpl.
 *
 * @author monster
 */
public class GwtTestSystemUtilsTest extends BaseGWTTestCase {
    private static final long SOME_RND_LONG = 7795286066369484091L;

    /** Some time value, in milliseconds. */
    @SuppressWarnings("deprecation")
    private static final Date D_2015_01_01 = new Date(115/*2015*/, 0/*JAN*/,
            1);

    /** Regexp to match dates. */
    private static final String DATE_FORMAT = "^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d$";

    public void testRND() {
        // Just make sure it's there
        SystemUtils.getRandom().nextBoolean();
    }

    public void testCurrentTimeMillis() {
        final double sutime = SystemUtils.currentTimeMillis();
        final long stime = System.currentTimeMillis();
        // We should not have any special TimeSource in the tests
        // such that both must be about the same.
        // Delta is huge; idk why it takes so long. Maybe JIT kicks in?
        assertEquals("currentTimeMillis()", stime, sutime, 100.0);
    }

    public void testDoubleToRawLongBits() {
        boolean failed = true;
        try {
            SystemUtils.doubleToRawLongBits(123.456);
            failed = false;
        } catch (final Exception e) {
            // NOP
        }
        assertTrue("doubleToRawLongBits()", failed);
    }

    public void testFloatToRawIntBits() {
        boolean failed = true;
        try {
            SystemUtils.floatToRawIntBits(123.456f);
            failed = false;
        } catch (final Exception e) {
            // NOP SOME_RND_LONG
        }
        assertTrue("floatToRawIntBits()", failed);
    }

    public void testLowHighLong() {
        final int low = SystemUtils.getLow(SOME_RND_LONG);
        assertTrue("getLow() " + low, low != SOME_RND_LONG);
        final int high = SystemUtils.getHigh(SOME_RND_LONG);
        assertTrue("getHigh() " + high, high != SOME_RND_LONG);
        final long both = SystemUtils.getLong(low, high);
        assertEquals("getLong()", SOME_RND_LONG, both);
    }

    public void testGetTimer() {
        assertNotNull("getTimer()", SystemUtils.getTimer());
        // Rest of Timer functionality tested in Timer own Test
    }

    public void testGetTimeSource() {
        final TimeSource src = SystemUtils.getTimeSource();
        assertNotNull("getTimeSource()", src);
        try {
            SystemUtils.setTimeSource(new TimeSource() {
                @Override
                public double currentTimeMillis() {
                    return -1;
                }
            });
            SystemUtils.updateCurrentTimeMillis();
            assertEquals("currentTimeMillis()", -1.0,
                    SystemUtils.currentTimeMillis(), 0.1);
        } finally {
            SystemUtils.setTimeSource(src);
            SystemUtils.updateCurrentTimeMillis();
        }
    }

    public void testIsGWTClient() {
        assertTrue("isGWTClient()", SystemUtils.isGWTClient());
    }

    public void testPowerOfTwo() {
        assertEquals("powerOfTwo()", 64, SystemUtils.powerOfTwo(63));
    }

    public void testReportUncaughtException() {
        // Cannot be tested, since calling this causes JUnit to interpret
        // The given Exception as a "test failure"!
//        SystemUtils.reportUncaughtException(new Exception("Stack-Trace"));
    }

    public void testLocalUTC() {
        final Date date = SystemUtils.newDate();
        final long now = (long) SystemUtils.currentTimeMillis();
        assertTrue("newDate() vs currentTimeMillis()",
                Math.abs(date.getTime() - now) < 100L);
        assertEquals("local(Date) vs local()", SystemUtils.local(date)
                .substring(0, 20), SystemUtils.local().substring(0, 20));
        assertEquals("utc(Date) vs utc()",
                SystemUtils.utc(date).substring(0, 20), SystemUtils.utc()
                        .substring(0, 20));
        final String local = SystemUtils.local(D_2015_01_01);
        assertTrue("local: " + local, local.matches(DATE_FORMAT));
        final String utc = SystemUtils.utc(D_2015_01_01);
        assertTrue("utc: " + utc, utc.matches(DATE_FORMAT));
        assertFalse("utc==local", local.equals(utc));

        final String full = SystemUtils.utc();
        final String utcdate = SystemUtils.utcDate();
        final String utctime = SystemUtils.utcTime();
        assertEquals(full.substring(0, 10), utcdate);
        assertEquals(full.substring(11, 20), utctime.substring(0, 9));
    }

    public void testReflection() {
        // TODO SystemUtils.forName()
        // TODO SystemUtils.forName(String)
        // TODO SystemUtils.isInstance()
        // TODO SystemUtils.newInstance()
        // TODO SystemUtils.isAssignableFrom()
    }
}
