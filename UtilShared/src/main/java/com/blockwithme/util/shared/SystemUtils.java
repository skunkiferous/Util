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
 * TODO Wrap java.util.Timer
 *
 * @author monster
 */
public abstract class SystemUtils {

    /** The SystemUtils instance; must be initialized through setImplementation(). */
    private static volatile SystemUtils implementation;

    /** Returns the Class object associated with the class or interface with the supplied string name. */
    protected abstract Class<?> forNameImpl(String name);

    /**
     * Returns the Class object associated with the class or interface with
     * the supplied string name, using the ClassLoader of "otherClass".
     */
    protected abstract Class<?> forNameImpl(String name, Class<?> otherClass);

    /** Returns the simple name of the underlying class as supplied in the source code. */
    @SuppressWarnings("rawtypes")
    protected abstract String getSimpleNameImpl(Class c);

    /** Determines if the class or interface represented by first Class parameter is either the same as, or is a superclass or superinterface of, the class or interface represented by the second Class parameter. */
    @SuppressWarnings("rawtypes")
    protected abstract boolean isAssignableFromImpl(Class c1, Class c2);

    /** Determines if the supplied Object is assignment-compatible with the object represented by supplied Class. */
    @SuppressWarnings("rawtypes")
    protected abstract boolean isInstanceImpl(Class c, Object obj);

    /** Creates a new instance of the class represented by the supplied Class. */
    protected abstract <T> T newInstanceImpl(Class<T> c);

    /** Returns the value of the indexed component in the supplied array. */
    protected abstract Object getArrayElementImpl(Object array, int index);

    /** Returns the length of the supplied array. */
    protected abstract int getArrayLengthImpl(Object array);

    /** Creates and returns a ConcurrentMap. */
    @SuppressWarnings("rawtypes")
    protected abstract ConcurrentMap newConcurrentMapImpl();

    /** Creates and returns a ConcurrentMap, with an initialCapacity. */
    @SuppressWarnings("rawtypes")
    protected abstract ConcurrentMap newConcurrentMapImpl(int initialCapacity);

    /** Creates and returns a ConcurrentMap, with an initialCapacity and concurrencyLevel. */
    @SuppressWarnings("rawtypes")
    protected abstract ConcurrentMap newConcurrentMapImpl(int initialCapacity,
            int concurrencyLevel);

    /** Creates a new array with the specified component type and length. */
    @SuppressWarnings("rawtypes")
    protected abstract Object newArrayInstanceImpl(Class c, int size);

    /** Sets the value of the indexed component in the supplied array to the supplied value. */
    protected abstract void setArrayElementImpl(Object array, int index,
            Object value);

    /** Returns the value of the System property, or the given default value, if not found. */
    protected abstract String getPropertyImpl(String key, String def);

    /** Sets the value of the System property. */
    protected abstract String setPropertyImpl(String key, String value);

    /** Clears the System Property. */
    protected abstract String clearPropertyImpl(String key);

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

    /** Returns the current Thread Name. */
    protected abstract String currentThreadNameImpl();

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

    /** Returns the simple name of the underlying class as supplied in the source code. */
    @SuppressWarnings("rawtypes")
    public static String getSimpleName(final Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        return getImplementation().getSimpleNameImpl(clazz);
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

    /** Returns the value of the indexed component in the supplied array. */
    public static Object getArrayElement(final Object array, final int index) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        return getImplementation().getArrayElementImpl(array, index);
    }

    /** Sets the value of the indexed component in the supplied array to the supplied value. */
    public static void setArrayElement(final Object array, final int index,
            final Object value) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        getImplementation().setArrayElementImpl(array, index, value);
    }

    /** Returns the length of the supplied array. */
    public static int getArrayLength(final Object array) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        return getImplementation().getArrayLengthImpl(array);
    }

    /** Creates a new array with the specified component type and length. */
    @SuppressWarnings("rawtypes")
    public static Object newArrayInstance(final Class clazz, final int size) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        return getImplementation().newArrayInstanceImpl(clazz, size);
    }

    /** Creates and returns a ConcurrentMap. */
    @SuppressWarnings("unchecked")
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return getImplementation().newConcurrentMapImpl();
    }

    /** Creates and returns a ConcurrentMap, with an initialCapacity. */
    @SuppressWarnings("unchecked")
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap(
            final int initialCapacity) {
        return getImplementation().newConcurrentMapImpl(initialCapacity);
    }

    /** Creates and returns a ConcurrentMap, with an initialCapacity and concurrencyLevel. */
    @SuppressWarnings("unchecked")
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap(
            final int initialCapacity, final int concurrencyLevel) {
        return getImplementation().newConcurrentMapImpl(initialCapacity,
                concurrencyLevel);
    }

    /** Returns the value of the System property, or null, if not found. */
    public static String getProperty(final String key) {
        return getImplementation().getPropertyImpl(key, null);
    }

    /** Returns the value of the System property, or the given default value, if not found. */
    public static String getProperty(final String key, final String def) {
        return getImplementation().getPropertyImpl(key, def);
    }

    /** Sets the value of the System property. */
    public static String setProperty(final String key, final String value) {
        return getImplementation().setPropertyImpl(key, value);
    }

    /** Clears the System Property. */
    public static String clearProperty(final String key) {
        return getImplementation().clearPropertyImpl(key);
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

    /** Returns the current Thread Name. */
    public static String currentThreadName() {
        return getImplementation().currentThreadNameImpl();
    }

    /** Returns an int representation of the specified floating-point value */
    public static int floatToRawIntBits(final float value) {
        return getImplementation().floatToRawIntBitsImpl(value);
    }

    /** Returns a long representation of the specified floating-point value */
    public static long doubleToRawLongBits(final double value) {
        return getImplementation().doubleToRawLongBitsImpl(value);
    }
}
