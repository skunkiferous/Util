/*
 * Copyright 2011-2013 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.lang;

import com.google.gwt.core.client.GWT;

/**
 * The GWT thread concept, basically a current thread executor.
 * @author karnok, 2011.02.20.
 */
public class Thread implements Runnable {

    /**
     * The minimum priority that a thread can have.
     */
    public final static int MIN_PRIORITY = 1;

   /**
     * The default priority that is assigned to a thread.
     */
    public final static int NORM_PRIORITY = 5;

    /**
     * The maximum priority that a thread can have.
     */
    public final static int MAX_PRIORITY = 10;

    /** The constant thread. */
    protected static final Thread CURRENT_THREAD = new Thread();

    /** Is the thread interrupted? */
    boolean interruptedFlag;

    @Override
    public void run() {
        throw new UnsupportedOperationException();
    }

    /** @return is the current thread interrupted? */
    public boolean isInterrupted() {
        return interruptedFlag;
    }

    /** Interrupt the thread. */
    public void interrupt() {
        interruptedFlag = true;
    }

    public boolean isDaemon() {
        return false;
    }

    public boolean isAlive() {
        return true;
    }

    public long getId() {
        return 1;
    }

    public String getName() {
        return "jsmain";
    }

    public int getPriority() {
        return NORM_PRIORITY;
    }

    public String toString() {
        return "Thread[" + getName() + "," + getPriority() + ",]";
    }

    public static int activeCount() {
        return 1;
    }

    /** @return check if the thread is interrupted and clear the flag. */
    public static boolean interrupted() {
        boolean itf = CURRENT_THREAD.interruptedFlag;
        CURRENT_THREAD.interruptedFlag = false;
        return itf;
    }

    /** @return the current thread object. */
    public static Thread currentThread() {
        return CURRENT_THREAD;
    }

    public static void yield() {
        // This is fine.
    }

    public static void sleep(long millis) throws InterruptedException {
        if (millis > 1) {
            // We really should not sleep in JavaScript.
            // I would have removed it, but it was in libGDX,
            // so I left it to see who calls it, if anyone.
            throw new UnsupportedOperationException();
        } // else we assume they just wanted to yield(), which is fine.
    }

    public static void setDefaultUncaughtExceptionHandler(
            final Thread.UncaughtExceptionHandler javaHandler) {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                javaHandler.uncaughtException(CURRENT_THREAD, e);
            }
        });
    }

    public static interface UncaughtExceptionHandler {
        void uncaughtException(Thread t, Throwable e);
    }
}
