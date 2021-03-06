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
package com.blockwithme.util.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * <code>SystemUtils</code> is a gateway abstracting System/platform functionality
 * that is not available on all System/platforms; for example, reflection in GWT.
 *
 * The *Java* application is in charge of initializing this class; this cannot
 * be done automatically, as any such code would require using functionality
 * that is not fully cross-platform. GWT, OTOH, has a concept of "module" which
 * allows the auto-initialization.
 *
 * The odd cross-platform helper method might also be put here, but defined directly
 * in the base class.
 *
 * The concrete implementation is responsible to call updateCurrentTimeMillis()
 * regularly, every few milliseconds.
 *
 * @author monster
 */
public abstract class SystemUtils {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(SystemUtils.class
            .getName());

    /**
     * Default currentTimeMillis() update interval, in milliseconds,
     * based on the JavaScript minimum standard "timeout".
     */
    public static final int CURRENT_TIME_MILLIS_UPDATE_INTERVAL = 5;

    /** Maximum integer value in a double. */
    public static final double MAX_DOUBLE_INT_VALUE = (1L << 52L);

    /** Minimum integer value in a double. */
    public static final double MIN_DOUBLE_INT_VALUE = -MAX_DOUBLE_INT_VALUE;

    /** True if we are in the GWT client. */
    public static final boolean GWT = isGWT();

    /** Random instance. */
    private static volatile Random random;

    /** The SystemUtils instance; must be initialized through setImplementation(). */
    private static volatile SystemUtils systemUtils;

    /** The system Timer, if any. */
    private static volatile Timer timer;

    /** The Application instance */
    private static volatile Application application;

    /** The source of time. */
    private static volatile TimeSource timeSource;

    /**
     * The approximate current time, in milliseconds.
     */
    private static volatile double currentTimeMillis = System
            .currentTimeMillis();

    /** No Provider! */
    @SuppressWarnings("rawtypes")
    protected static final Provider[] NO_PROVIDER = new Provider[0];

    /** The cached Providers. */
    @SuppressWarnings("rawtypes")
    private static final HashMap<Class, Provider[]> PROVIDERS = new HashMap<>();

    /** Returns the Class object associated with the class or interface with the supplied string name. */
    protected abstract Class<?> forNameImpl(String name);

    /** True if we are in the GWT client. */
    private static native boolean isGWTJS()
    /*-{
        return true;
    }-*/;

    /** True if we are in the GWT client. */
    private static boolean isGWT() {
        try {
            return isGWTJS();
        } catch (final Throwable t) {
            return false;
        }
    }

    /**
     * Returns the Class object associated with the class or interface with
     * the supplied string name, using the ClassLoader of "otherClass".
     */
    protected abstract Class<?> forNameImpl(String name, Class<?> otherClass);

    /** Determines if the class or interface represented by first Class parameter is either the same as, or is a superclass or superinterface of, the class or interface represented by the second Class parameter. */
    @SuppressWarnings("rawtypes")
    protected abstract boolean isAssignableFromImpl(Class c1, Class c2);

    /** Determines if the supplied Object is assignment-compatible with the object represented by supplied Class. */
    @SuppressWarnings("rawtypes")
    protected abstract boolean isInstanceImpl(Class c, Object obj);

    /**
     * Returns all known Providers for the supplied Class.
     * The Providers might return a subclass too.
     */
    protected abstract <T> Provider<T>[] providersForImpl(Class<T> clazz);

    /** Returns the UTC/GMT time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    protected abstract String utcImpl(Date date);

    /** Returns the local time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    protected abstract String localImpl(Date date);

    /**
     * Reports an exception caught at the "top level". This is
     * used in places where the browser calls into user code such as event
     * callbacks, timers, and RPC.
     */
    protected abstract void reportUncaughtExceptionImpl(Throwable e);

    /** Creates and returns a new WeakKeyMap. */
    protected abstract <KEY, VALUE> WeakKeyMap<KEY, VALUE> newWeakKeyMapImpl();

    /**
     * Returns some time value, in milliseconds, with the highest possible
     * precision.
     *
     * This method returns a time value that is usable to measure elapsed time
     * precisely. It does not (have to) represent the current time. It should
     * always move forward, independent of clock-changes, ...
     *
     * In Java, it will by "System.nanoTime()/1000000.0".
     */
    protected abstract double highResTimeMillisImpl();

    /** Specifies the Injector instance. */
    private static <E> E setInstance(final String type, final E oldInstance,
            final E newInstance) {
        if (newInstance == null) {
            if (oldInstance != null) {
                LOG.warning("Clearing " + type + " instance");
            }
        } else if (oldInstance != null) {
            LOG.warning("Replacing " + type + " instance");
        } else {
            LOG.info("Setting " + type + " instance");
        }
        return newInstance;
    }

    /** Specifies an implementation for SystemUtils. */
    @Inject
    public static synchronized void setImplementation(
            final SystemUtils systemUtils) {
        SystemUtils.systemUtils = setInstance("SystemUtils",
                SystemUtils.systemUtils, systemUtils);
    }

    /** Returns the implementation. */
    public static SystemUtils getImplementation() {
        return systemUtils;
    }

    /** Sets the System Timer. */
    @Inject
    public static synchronized void setTimer(final Timer timer) {
        if (SystemUtils.timer != null) {
            SystemUtils.timer.cancel();
        }
        SystemUtils.timer = setInstance("Timer", SystemUtils.timer, timer);
    }

    /** Returns the System Timer. */
    public static Timer getTimer() {
        return timer;
    }

    /** Specifies the Application instance. */
    @Inject
    public static synchronized void setApplication(final Application application) {
        SystemUtils.application = setInstance("Application",
                SystemUtils.application, application);
    }

    /** Returns the Application. */
    public static Application getApplication() {
        return application;
    }

    /**
     * Sets the new time source.
     *
     * @param timeSource the timeSource to set
     *
     * @throws java.lang.NullPointerException if timeSource is null
     */
    @Inject
    public static synchronized void setTimeSource(final TimeSource timeSource) {
        SystemUtils.timeSource = setInstance("TimeSource",
                SystemUtils.timeSource, timeSource);
    }

    /**
     * Returns the "source of time" instance.
     */
    public static TimeSource getTimeSource() {
        return timeSource;
    }

    /** Specifies a Random instance. */
    @Inject
    public static synchronized void setRandom(final Random random) {
        SystemUtils.random = setInstance("Random", SystemUtils.random, random);
    }

    /** Returns the Random instance. */
    public static Random getRandom() {
        return random;
    }

    /** Returns the Class object associated with the class or interface with the supplied string name. */
    public static Class<?> forName(final String name) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name: " + name);
        }
        return systemUtils.forNameImpl(name);
    }

    /**
     * Returns the Class object associated with the class or interface with
     * the supplied string name, using the ClassLoader of "otherClass".
     */
    public static Class<?> forName(final String name, final Class<?> otherClass) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name: " + name);
        }
        if (otherClass == null) {
            throw new NullPointerException("otherClass");
        }
        return systemUtils.forNameImpl(name, otherClass);
    }

    /** Determines if the class or interface represented by first Class parameter is either the same as, or is a superclass or superinterface of, the class or interface represented by the second Class parameter. */
    @SuppressWarnings("rawtypes")
    public static boolean isAssignableFrom(final Class c1, final Class c2) {
        if (c1 == null) {
            throw new NullPointerException("c1");
        }
        if (c2 == null) {
            throw new NullPointerException("c2");
        }
        return systemUtils.isAssignableFromImpl(c1, c2);
    }

    /** Determines if the supplied Object is assignment-compatible with the object represented by supplied Class. */
    @SuppressWarnings("rawtypes")
    public static boolean isInstance(final Class clazz, final Object obj) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        return systemUtils.isInstanceImpl(clazz, obj);
    }

    /**
     * Returns all known Providers for the supplied Class.
     * The Providers might return a subclass too.
     * Do NOT modify the content of the array!
     */
    @SuppressWarnings("unchecked")
    public static <T> Provider<T>[] providersFor(final Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        Provider<T>[] result;
        synchronized (PROVIDERS) {
            result = PROVIDERS.get(clazz);
            if (result == null) {
                try {
                    result = systemUtils.providersForImpl(clazz);
                } catch (final Throwable t) {
                    // NOP
                }
                if (result == null) {
                    result = NO_PROVIDER;
                }
                PROVIDERS.put(clazz, result);
            }
        }
        return result;
    }

    /**
     * Creates a new instance of the class represented by the supplied Class.
     * Delegates to providersFor(clazz)[0].
     * Will fail if no provider is found.
     */
    public static <T> T newInstance(final Class<T> clazz) {
        final Provider<T>[] providers = providersFor(clazz);
        if (providers.length > 0) {
            return providers[0].get();
        }
        throw new IllegalArgumentException("No Provider found for "
                + clazz.getName());
    }

    /**
     * Updates the current time, using the time source.
     *
     * The concrete implementation is expected to call this method regularly,
     * every few milliseconds.
     */
    public static double updateCurrentTimeMillis() {
        final TimeSource ts = timeSource;
        if (ts != null) {
            return currentTimeMillis = ts.currentTimeMillis();
        }
        // No TimeSource yet; cannot update.
        return currentTimeMillis;
    }

    /**
     * Returns the current time, in milliseconds.
     *
     * The reason to define this here is, that many "clients" have a totally wrong
     * system time, and using this method allows the application to "correct"
     * the bad host system time.
     */
    public static double currentTimeMillis() {
        return currentTimeMillis;
    }

    /**
     * Returns some time value, in milliseconds, with the highest possible
     * precision.
     *
     * This method returns a time value that is usable to measure elapsed time
     * precisely. It does not (have to) represent the current time. It should
     * always move forward, independent of clock-changes, ...
     *
     * In Java, it will by "System.nanoTime()/1000000.0".
     */
    public static double highResTimeMillis() {
        return systemUtils.highResTimeMillisImpl();
    }

    /** Creates a new Date, using *our* currentTimeMillis() method. */
    public static Date newDate() {
        // That is the downside of using double for time.
        // Hopefully, this won't be called too often.
        return new Date((long) currentTimeMillis());
    }

    /** Returns the UTC/GMT time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    public static String utc(final Date date) {
        return systemUtils.utcImpl(date);
    }

    /** Returns the local time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    public static String local(final Date date) {
        return systemUtils.localImpl(date);
    }

    /**
     * Returns the current UTC/GMT time, in the format yyyy-MM-dd HH:mm:ss.SSS,
     * using *our* currentTimeMillis() method.
     */
    public static String utc() {
        return utc(newDate());
    }

    /**
     * Returns the current local time, in the format yyyy-MM-dd HH:mm:ss.SSS,
     * using *our* currentTimeMillis() method.
     */
    public static String local() {
        return local(newDate());
    }

    /**
     * Returns the current UTC/GMT time, in the format yyyy-MM-dd,
     * using *our* currentTimeMillis() method.
     */
    public static String utcDate() {
        return utc().substring(0, 10);
    }

    /**
     * Returns the current local time, in the format yyyy-MM-dd,
     * using *our* currentTimeMillis() method.
     */
    public static String localDate() {
        return local().substring(0, 10);
    }

    /**
     * Returns the current UTC/GMT time, in the format HH:mm:ss.SSS,
     * using *our* currentTimeMillis() method.
     */
    public static String utcTime() {
        return utc().substring(11);
    }

    /**
     * Returns the current local time, in the format HH:mm:ss.SSS,
     * using *our* currentTimeMillis() method.
     */
    public static String localTime() {
        return local().substring(11);
    }

    /**
     * Reports an exception caught at the "top level". This is
     * used in places where the browser calls into user code such as event
     * callbacks, timers, and RPC.
     */
    public static void reportUncaughtException(final Throwable e) {
        systemUtils.reportUncaughtExceptionImpl(e);
    }

    /** Posts a {@link Runnable} on the main loop thread.
     *
     * @param runnable the runnable. */
    public static void postRunnable(final Runnable runnable) {
        application.postRunnable(runnable);
    }

    /** @return the time span between the current frame and the last frame in seconds. */
    public static float getDeltaTime() {
        return application.getDeltaTime();
    }

    /** Returns the lower 32 bits of a long. */
    public static int getLow(final long value) {
        return (int) (value & 0xFFFFFFFFL);
    }

    /** Returns the higher 32 bits of a long. */
    public static int getHigh(final long value) {
        return (int) ((value >> 32) & 0xFFFFFFFFL);
    }

    /** Returns a long, from the lower and higher 32 bit parts. */
    public static long getLong(final int low, final int high) {
        return (((long) high) << 32) | (low & 0xFFFFFFFFL);
    }

    /** Returns a power of two, bigger or equal to the input. */
    public static int powerOfTwo(final int i) {
        // If already power of two, then we are done
        if (2 * i == (i ^ (i - 1) + 1)) {
            return i;
        }
        // Not power of two, so "round up" by moving highest bit one notch up
        return Integer.highestOneBit(i) << 1;
    }

    /** Creates an returns a new WeakKeyMap. */
    public static <KEY, VALUE> WeakKeyMap<KEY, VALUE> newWeakKeyMap() {
        return systemUtils.newWeakKeyMapImpl();
    }
}
