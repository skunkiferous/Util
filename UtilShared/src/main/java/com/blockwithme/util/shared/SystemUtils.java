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
package com.blockwithme.util.shared;

import java.util.Date;
import java.util.Timer;

/**
 * <code>SystemUtils</code> is a gateway abstracting System/platform functionality
 * that is not available on all System/platforms; for example, reflection in GWT.
 *
 * The application is in charge of initializing this class; this cannot be done
 * automatically, as any such code would require using functionality that is not
 * fully cross-platform (GWT, again).
 *
 * The odd cross-platform helper method might also be put here, but defined directly
 * in the base class.
 *
 * @author monster
 */
public abstract class SystemUtils {

    /** The SystemUtils instance; must be initialized through setImplementation(). */
    private static volatile SystemUtils implementation;

    /** The system Timer, if any. */
    private static Timer timer;

    /** Returns the Class object associated with the class or interface with the supplied string name. */
    protected abstract Class<?> forNameImpl(String name);

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

    /** Creates a new instance of the class represented by the supplied Class. */
    protected abstract <T> T newInstanceImpl(Class<T> c);

    /**
     * Returns the current time, in milliseconds.
     *
     * The reason to define this here is, that many "clients" have a totally wrong
     * system time, and using this method allows the application to "correct"
     * the bad host system time.
     */
    protected abstract long currentTimeMillisImpl();

    /** Returns the UTC/GMT time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    protected abstract String utcImpl(Date date);

    /** Returns the local time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    protected abstract String localImpl(Date date);

    /** Returns true if we are in the GWT client. */
    protected abstract boolean isGWTClientImpl();

    /** Returns an int representation of the specified floating-point value */
    protected abstract int floatToRawIntBitsImpl(float value);

    /** Returns a long representation of the specified floating-point value */
    protected abstract long doubleToRawLongBitsImpl(double value);

    /** Checks if the implementation is initialized, and returns it. */
    private static SystemUtils getImplementation() {
        if (implementation == null) {
            throw new IllegalStateException(
                    "Not initialized! SystemUtils must be initialized"
                            + " by calling SystemUtils.setImplementation() "
                            + "before any code starts to use it.");
        }
        return implementation;
    }

    /** Specifies an implementation for SystemUtils. */
    public static synchronized void setImplementation(final SystemUtils impl) {
        if (impl == null) {
            throw new NullPointerException("impl");
        }
        if (implementation != null) {
            throw new IllegalStateException("Already initialized!");
        }
        implementation = impl;
    }

    /** Returns a power of two, bigger or equal to the input. */
    public static int powerOfTwo(final int i) {
        // If already power of two, then we are done
        if (2 * i == (i ^ (i - 1) + 1)) {
            return i;
        }
        // Not power of two, so "round up" by moving highest bit one notch up
        return 1 << (Integer.highestOneBit(i) + 1);
    }

    /** Returns the Class object associated with the class or interface with the supplied string name. */
    public static Class<?> forName(final String name) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name: " + name);
        }
        return getImplementation().forNameImpl(name);
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
        return getImplementation().forNameImpl(name, otherClass);
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
        return getImplementation().isAssignableFromImpl(c1, c2);
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
        return getImplementation().isInstanceImpl(clazz, obj);
    }

    /** Creates a new instance of the class represented by the supplied Class. */
    public static <T> T newInstance(final Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        return getImplementation().newInstanceImpl(clazz);
    }

    /**
     * Returns the current time, in milliseconds.
     *
     * The reason to define this here is, that many "clients" have a totally wrong
     * system time, and using this method allows the application to "correct"
     * the bad host system time.
     */
    public static long currentTimeMillis() {
        return getImplementation().currentTimeMillisImpl();
    }

    /** Creates a new Date, using *our* currentTimeMillis() method. */
    public static Date newDate() {
        return new Date(currentTimeMillis());
    }

    /** Returns the UTC/GMT time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    public static String utc(final Date date) {
        return getImplementation().utcImpl(date);
    }

    /** Returns the local time of the Date, in the format yyyy-MM-dd HH:mm:ss.SSS. */
    public static String local(final Date date) {
        return getImplementation().localImpl(date);
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

    /** Returns true if we are in the GWT client. */
    public static boolean isGWTClient() {
        return getImplementation().isGWTClientImpl();
    }

    /** Returns an int representation of the specified floating-point value */
    public static int floatToRawIntBits(final float value) {
        return getImplementation().floatToRawIntBitsImpl(value);
    }

    /** Returns a long representation of the specified floating-point value */
    public static long doubleToRawLongBits(final double value) {
        return getImplementation().doubleToRawLongBitsImpl(value);
    }

    /** Returns the lower 32 bits of a long. */
    public static int getLow(final long value) {
        return (int) value;
    }

    /** Returns the higher 32 bits of a long. */
    public static int getHigh(final long value) {
        return (int) (value >> 32);
    }

    /** Returns a long, from the lower and higher 32 bit parts. */
    public static long getLong(final int low, final int high) {
        return (long) high << 32 | low & 0xFFFFFFFFL;
    }

    /** Returns the System Timer. */
    public static Timer getTimer() {
        synchronized (SystemUtils.class) {
            if (timer == null) {
                timer = new Timer("System-Timer");
            }
        }
        return timer;
    }
}
