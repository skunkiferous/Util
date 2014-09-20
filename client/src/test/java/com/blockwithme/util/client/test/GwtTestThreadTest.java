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

/**
 * Tests java.lang.Thread emulation.
 *
 * @author monster
 *
 * TODO Test UncaughtExceptionHandler somehow
 */
public class GwtTestThreadTest extends BaseGWTTestCase {
    /**
     * Tests Thread.currentThread().run()
     */
    public void testRun() {
        boolean failed = true;
        try {
            Thread.currentThread().run();
            failed = false;
        } catch (final Exception e) {
            // NOP
        }
        assertTrue("Failed run()", failed);
    }

    /**
     * Tests Thread.currentThread().isDaemon()
     */
    public void testIsDaemon() {
        assertFalse("isDaemon()", Thread.currentThread().isDaemon());
    }

    /**
     * Tests Thread.currentThread().isAlive()
     */
    public void testIsAlive() {
        assertTrue("isAlive()", Thread.currentThread().isAlive());
    }

    /**
     * Tests Thread.currentThread().getId()
     */
    public void testGetId() {
        assertEquals("getId()", 1L, Thread.currentThread().getId());
    }

    /**
     * Tests Thread.currentThread().getName()
     */
    public void testGetName() {
        assertEquals("main", Thread.currentThread().getName());
    }

    /**
     * Tests Thread.currentThread()
     */
    public void testCurrentThread() {
        assertNotNull("currentThread()", Thread.currentThread());
    }

    /**
     * Tests Thread.activeCount()
     */
    public void testActiveCount() {
        assertEquals("activeCount()", 1, Thread.activeCount());
    }

    /**
     * Tests Thread.currentThread().getPriority()
     */
    public void testPetPriority() {
        assertEquals("getPriority()", Thread.NORM_PRIORITY, Thread
                .currentThread().getPriority());
    }

    /**
     * Tests Thread.isInterrupted() ...
     */
    public void testInterrupted() {
        assertFalse("isInterrupted()", Thread.currentThread().isInterrupted());
        Thread.currentThread().interrupt();
        assertTrue("isInterrupted()", Thread.currentThread().isInterrupted());
        // Calling twice is on purpose; isInterrupted() should not change flag state
        assertTrue("isInterrupted()", Thread.currentThread().isInterrupted());
        assertTrue("interrupted()", Thread.interrupted());
        // Calling twice is on purpose; interrupted() should change flag state
        assertFalse("interrupted()", Thread.interrupted());
        assertFalse("isInterrupted()", Thread.currentThread().isInterrupted());
    }

    /**
     * Tests Thread.yield()
     */
    public void testYield() {
        Thread.yield();
    }

    /**
     * Tests Thread.sleep()
     */
    public void testSleep() {
        boolean failed = true;
        try {
            Thread.sleep(0);
            failed = false;
        } catch (final Exception e) {
            // NOP
        }
        assertFalse("Failed sleep(0)", failed);

        failed = true;
        try {
            Thread.sleep(1);
            failed = false;
        } catch (final Exception e) {
            // NOP
        }
        assertFalse("Failed sleep(1)", failed);

        failed = true;
        try {
            Thread.sleep(2);
            failed = false;
        } catch (final Exception e) {
            // NOP
        }
        assertTrue("Failed sleep(2)", failed);
//
//        failed = true;
//        final long before = System.currentTimeMillis();
//        try {
//            Thread.sleep(1000);
//            failed = false;
//        } catch (final Exception e) {
//            // NOP
//        }
//        final long after = System.currentTimeMillis();
//        assertFalse("Failed sleep(1000)", failed);
//        assertEquals("sleep(1000)", 1L, Math.round((after - before) / 1000.0));
    }
}
