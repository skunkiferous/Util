/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package java.util.concurrent.atomic;
import sun.misc.Unsafe;

import java.util.*;

/**
 * An {@code int} array in which elements may be updated atomically.
 * See the {@link java.util.concurrent.atomic} package
 * specification for description of the properties of atomic
 * variables.
 * @since 1.5
 * @author Doug Lea
 */
public class AtomicIntegerArray implements java.io.Serializable {
    private static final long serialVersionUID = 2862133569453604235L;

    private final int[] array;

    /**
     * Creates a new AtomicIntegerArray of given length.
     *
     * @param length the length of the array
     */
    public AtomicIntegerArray(int length) {
        array = new int[length];
    }

    /**
     * Creates a new AtomicIntegerArray with the same length as, and
     * all elements copied from, the given array.
     *
     * @param array the array to copy elements from
     * @throws NullPointerException if array is null
     */
    public AtomicIntegerArray(int[] array) {
        if (array == null)
            throw new NullPointerException();
        int length = array.length;
        this.array = new int[length];
        System.arraycopy(array, 0, this.array, 0, length);
    }

    /**
     * Returns the length of the array.
     *
     * @return the length of the array
     */
    public final int length() {
        return array.length;
    }

    /**
     * Gets the current value at position {@code i}.
     *
     * @param i the index
     * @return the current value
     */
    public final int get(int i) {
        return array[i];
    }

    /**
     * Sets the element at position {@code i} to the given value.
     *
     * @param i the index
     * @param newValue the new value
     */
    public final void set(int i, int newValue) {
        array[i] = newValue;
    }

    /**
     * Eventually sets the element at position {@code i} to the given value.
     *
     * @param i the index
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(int i, int newValue) {
        array[i] = newValue;
    }

    /**
     * Atomically sets the element at position {@code i} to the given
     * value and returns the old value.
     *
     * @param i the index
     * @param newValue the new value
     * @return the previous value
     */
    public final int getAndSet(int i, int newValue) {
        while (true) {
            int current = get(i);
            if (compareAndSet(i, current, newValue))
                return current;
        }
    }

    /**
     * Atomically sets the element at position {@code i} to the given
     * updated value if the current value {@code ==} the expected value.
     *
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int i, int expect, int update) {
        if (array[i] == expect) {
            array[i] = update;
            return true;
        }
        return false;
    }

    /**
     * Atomically sets the element at position {@code i} to the given
     * updated value if the current value {@code ==} the expected value.
     *
     * <p>May <a href="package-summary.html#Spurious">fail spuriously</a>
     * and does not provide ordering guarantees, so is only rarely an
     * appropriate alternative to {@code compareAndSet}.
     *
     * @param i the index
     * @param expect the expected value
     * @param update the new value
     * @return true if successful.
     */
    public final boolean weakCompareAndSet(int i, int expect, int update) {
        return compareAndSet(i, expect, update);
    }

    /**
     * Atomically increments by one the element at index {@code i}.
     *
     * @param i the index
     * @return the previous value
     */
    public final int getAndIncrement(int i) {
        while (true) {
            int current = get(i);
            int next = current + 1;
            if (compareAndSet(i, current, next))
                return current;
        }
    }

    /**
     * Atomically decrements by one the element at index {@code i}.
     *
     * @param i the index
     * @return the previous value
     */
    public final int getAndDecrement(int i) {
        while (true) {
            int current = get(i);
            int next = current - 1;
            if (compareAndSet(i, current, next))
                return current;
        }
    }

    /**
     * Atomically adds the given value to the element at index {@code i}.
     *
     * @param i the index
     * @param delta the value to add
     * @return the previous value
     */
    public final int getAndAdd(int i, int delta) {
        while (true) {
            int current = get(i);
            int next = current + delta;
            if (compareAndSet(i, current, next))
                return current;
        }
    }

    /**
     * Atomically increments by one the element at index {@code i}.
     *
     * @param i the index
     * @return the updated value
     */
    public final int incrementAndGet(int i) {
        while (true) {
            int current = get(i);
            int next = current + 1;
            if (compareAndSet(i, current, next))
                return next;
        }
    }

    /**
     * Atomically decrements by one the element at index {@code i}.
     *
     * @param i the index
     * @return the updated value
     */
    public final int decrementAndGet(int i) {
        while (true) {
            int current = get(i);
            int next = current - 1;
            if (compareAndSet(i, current, next))
                return next;
        }
    }

    /**
     * Atomically adds the given value to the element at index {@code i}.
     *
     * @param i the index
     * @param delta the value to add
     * @return the updated value
     */
    public final int addAndGet(int i, int delta) {
        while (true) {
            int current = get(i);
            int next = current + delta;
            if (compareAndSet(i, current, next))
                return next;
        }
    }

    /**
     * Returns the String representation of the current values of array.
     * @return the String representation of the current values of array.
     */
    public String toString() {
        return Arrays.toString(array);
    }
}
