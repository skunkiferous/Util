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
import java.util.Properties;

/**
 * Tests java.lang.System emulation.
 *
 * @author monster
 */
public class GwtTestSystemTest extends BaseGWTTestCase {

    /** Time that must be before the test real execution time. */
    @SuppressWarnings("deprecation")
    private static final long PAST_TIME = new Date(100/*2000*/, 0/*JAN*/, 1)
            .getTime();

    /** Time that must be after the test real execution time. */
    @SuppressWarnings("deprecation")
    private static final long FUTURE_TIME = new Date(1100/*3000*/, 0/*JAN*/,
            1).getTime();

    /**
     * Tests System.out
     */
    public void testOut() {
        System.out.print('H');
        System.out.print("ello w");
        System.out.print(0);
        System.out.println("rld!");
        System.out.flush();
    }

    /**
     * Tests System.err
     */
    public void testErr() {
        System.err.print('H');
        System.err.print("ello w");
        System.err.print(0);
        System.err.println("rld!");
        System.err.flush();
    }

    /**
     * Tests System.arraycopy(Object src, int srcOfs, Object dest,
            int destOfs, int len)
     */
    public void testArraycopy() {
        // System.arraycopy() impl comes from GWT itself.
        // I will simply assume it works...
    }

    /**
     * Tests System.currentTimeMillis()
     */
    public void testCurrentTimeMillis() {
        // Hard to test; we cannot sleep to see it changes...
        final long now = System.currentTimeMillis();
        assertFalse("System.currentTimeMillis()", 0L == now);
        double value = Long.MAX_VALUE;
        // Waste time, to see if time changes
        int loop = 0;
        while (loop < 1000000) {
            value = Math.sqrt(value);
            if (now < System.currentTimeMillis()) {
                break;
            }
            loop++;
        }
        assertTrue("Time changes", loop != 1000000);
        final long datetime = new Date().getTime();
        // Could theoretically fail sometimes
        assertTrue("new Date().getTime() (now=" + now + ",datetime=" + datetime
                + ")", (now <= datetime) && (datetime <= now + 50));
        assertTrue("Not in past (past=" + PAST_TIME + ", future=" + FUTURE_TIME
                + ", now=" + now + ")", PAST_TIME < now);
        assertTrue("Not in future (past=" + PAST_TIME + ", future="
                + FUTURE_TIME + ", now=" + now + ")", FUTURE_TIME > now);
    }

    /**
     * Tests System.currentTimeMillis()
     */
    public void testGc() {
        System.gc();
    }

    /**
     * Tests System.lineSeparator()
     */
    public void testLineSeparator() {
        assertEquals("lineSeparator()", "\n", System.lineSeparator());
    }

    /**
     * Tests System.identityHashCode()
     */
    public void testIdentityHashCode() {
        final Object o1 = new Object();
        final Object o2 = new Object();
        final int hash1 = System.identityHashCode(o1);
        final int hash2 = System.identityHashCode(o2);
        assertTrue("System.identityHashCode()",
                hash1 == System.identityHashCode(o1));
        assertTrue("System.identityHashCode()", hash1 != hash2);
    }

    /**
     * Tests System Properties
     */
    public void testProperties() {
        final Properties props = System.getProperties();

        // Make sure some standard system properties are set
        assertTrue("System.getProperties()", props.size() >= 10);
        assertEquals("os.name", "GWT", System.getProperty("os.name"));
        assertEquals("file.encoding", "UTF-8",
                System.getProperty("file.encoding"));

        // Test set/get/clear of non-standard property
        assertEquals("foobar", null, System.getProperty("foobar"));
        assertEquals("foobar", "bleep", System.getProperty("foobar", "bleep"));
        System.setProperty("foobar", "test");
        assertEquals("foobar", "test", System.getProperty("foobar"));
        assertEquals("foobar", "test", System.getProperty("foobar", "bleep"));
        System.clearProperty("foobar");
        assertEquals("foobar", null, System.getProperty("foobar"));
        assertEquals("foobar", "bleep", System.getProperty("foobar", "bleep"));
    }
}
