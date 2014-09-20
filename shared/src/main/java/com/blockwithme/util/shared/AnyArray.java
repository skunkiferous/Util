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

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.blockwithme.util.base.SystemUtils;

/**
 * Smallest possible mutable class that could contain a bunch of anything,
 * without requiring "boxing" of primitive values (except large long!).
 *
 * Note that it also supports the AnyType.Empty, which allows us to represent
 * "no data", for example, when querying the value of a non-existent property.
 *
 * TODO Define an AnyList, AnySet and AnyMap interface, and have AnyArray implement them all.
 *
 * @author monster
 */
public class AnyArray implements Serializable, Iterable<Any> {
    /** serialVersionUID */
    private static final long serialVersionUID = -2510171712544971710L;

    /** Maximum integer value in a double. */
    private static final long MAX_LONG_VALUE = (long) SystemUtils.MAX_DOUBLE_INT_VALUE;

    /** Minimum integer value in a double. */
    private static final long MIN_LONG_VALUE = (long) SystemUtils.MIN_DOUBLE_INT_VALUE;

    /** Empty double array. */
    private static final double[] NO_PRIMITIVE = new double[0];

    /** Empty Object array. */
    private static final Object[] NO_OBJ = new Object[0];

    /** The size. */
    protected int size;
    /** The primitive data. */
    protected double[] primitive = NO_PRIMITIVE;
    /** The object data. */
    protected Object[] object = NO_OBJ;

    /** Fail if the index is out of bounds. */
    private int checkIndex(final int index) {
        if (index > size) {
            throw new IndexOutOfBoundsException("index(" + index + ") >= size("
                    + size + ")");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("index(" + index + ") < 0");
        }
        return index;
    }

    /** Default empty AnyArray. */
    public AnyArray() {
        // NOP
    }

    /** AnyArray of specific size. */
    public AnyArray(final int size) {
        setSize(size);
    }

    /**
     * Copy constructor.
     */
    AnyArray(final int _size, final double[] _primitive, final Object[] _object) {
        size = _size;
        primitive = _primitive;
        object = _object;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        String prefix = "AnyArray[";
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                buf.append(prefix);
                prefix = ", ";
                buf.append('(');
                toString(buf, i);
                buf.append(')');
            }
        } else {
            buf.append(prefix);
        }
        buf.append("]");
        return buf.toString();
    }

    /** Allows adding additional data in toString for each index. */
    protected void toString(final StringBuilder buf, final int index) {
        final double p = primitive[index];
        final long l = (long) p;
        buf.append("primitive=");
        if (l == p) {
            // primitive is an integral value, so don't show decimals.
            buf.append(l);
        } else {
            buf.append(p);
        }
        buf.append(", object=").append(object[index]);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int size = this.size;
        int result = 1;
        for (int i = 0; i < size; i++) {
            final Object obj = object[i];
            result = prime * result + ((obj == null) ? 0 : obj.hashCode());
            final long l = Double.doubleToLongBits(primitive[i]);
            result = prime * result + (int) (l ^ (l >>> 32));
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AnyArray other = (AnyArray) obj;
        final int size = this.size;
        if (size != other.size) {
            return false;
        }
        final double[] primitive = this.primitive;
        final Object[] object = this.object;
        final double[] otherPrimitive = other.primitive;
        final Object[] otherObject = other.object;
        for (int i = 0; i < size; i++) {
            final Object value = object[i];
            if (value == null) {
                return (otherObject[i] == null);
            } else if (!value.equals(otherObject[i])) {
                return false;
            }
            if (primitive[i] != otherPrimitive[i])
                return false;
        }
        return true;
    }

    /** Returns a copy. */
    public final AnyArray copy() {
        final int size = this.size;
        final double[] _primitive = new double[size];
        System.arraycopy(primitive, 0, _primitive, 0, size);
        final Object[] _object = new Object[size];
        System.arraycopy(object, 0, _object, 0, size);
        return new AnyArray(size, _primitive, _object);
    }

    /** Returns the size. */
    public final int getSize() {
        return size;
    }

    /** Sets the size. */
    public final AnyArray setSize(final int newSize) {
        final int oldSize = size;
        if (newSize != oldSize) {
            size = newSize;
            if (newSize > primitive.length) {
                final double[] oldPrimitive = primitive;
                final Object[] oldObject = object;
                primitive = new double[newSize];
                object = new Object[newSize];
                System.arraycopy(oldPrimitive, 0, primitive, 0, oldSize);
                System.arraycopy(oldObject, 0, object, 0, oldSize);
            }
            final double[] primitive = this.primitive;
            final Object[] object = this.object;
            if (newSize > oldSize) {
                for (int i = oldSize; i < newSize; i++) {
                    object[i] = AnyType.Empty;
                    primitive[i] = 0;
                }
            } else {
                for (int i = newSize; i < oldSize; i++) {
                    object[i] = AnyType.Empty;
                    primitive[i] = 0;
                }
            }
        }
        return this;
    }

    /** Is this AnyArray empty (size == 0)? */
    public final boolean isEmpty() {
        return (size == 0);
    }

    /**  Clears this AnyArray. */
    public final AnyArray clear() {
        return setSize(0);
    }

    /**
     * Copy method.
     *
     * @throws NullPointerException if other is null.
     */
    public final AnyArray copyFrom(final AnyArray other) {
        final int oldSize = size;
        final int newSize = other.size;
        size = newSize;
        if (newSize > primitive.length) {
            primitive = new double[newSize];
            object = new Object[newSize];
        } else {
            final double[] primitive = this.primitive;
            final Object[] object = this.object;
            for (int i = newSize; i < oldSize; i++) {
                object[i] = AnyType.Empty;
                primitive[i] = 0;
            }
        }
        System.arraycopy(other.primitive, 0, primitive, 0, newSize);
        System.arraycopy(other.object, 0, object, 0, newSize);
        return this;
    }

    /** Is this[index] empty? Empty mean no value set; it is different from having set "null" as object. */
    public final boolean isEmpty(final int index) {
        return (object[checkIndex(index)] == AnyType.Empty);
    }

    /** Returns the current type of the data at [index]. */
    public final AnyType type(final int index) {
        return Any.type(object[checkIndex(index)]);
    }

    /** Returns the current JSON type of the data at [index]. */
    public final JSONType jsonType(final int index) {
        return Any.jsonType(object[checkIndex(index)]);
    }

    /**  Clears this[index]. */
    public final AnyArray clear(final int index) {
        object[checkIndex(index)] = AnyType.Empty;
        primitive[index] = 0;
        return this;
    }

    /** Sets this[index] with an Object. */
    public final AnyArray setObject(final int index, final Object obj) {
        if (obj instanceof AnyType) {
            throw new IllegalArgumentException("Cannot contain AnyType!");
        }
        object[checkIndex(index)] = obj;
        primitive[index] = 0;
        return this;
    }

    /**
     * Return the Object at this[index]
     * @throws java.lang.IllegalStateException if this[index] does not contain an Object.
     * @return the Object.
     */
    public final Object getObject(final int index) {
        final Object obj = object[checkIndex(index)];
        if (obj instanceof AnyType) {
            throw new IllegalStateException("Not an Object: " + obj);
        }
        if (obj instanceof BigLongValue) {
            throw new IllegalStateException("Not an Object: " + AnyType.Long);
        }
        return obj;
    }

    /**
     * Return the Object at this[index], without validation.
     * @return the Object.
     */
    public final Object getObjectUnsafe(final int index) {
        return object[index];
    }

    /** Sets this[index] with a boolean. */
    public final AnyArray setBoolean(final int index, final boolean value) {
        object[checkIndex(index)] = AnyType.Boolean;
        primitive[index] = value ? 1 : 0;
        return this;
    }

    /**
     * Return the boolean at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an boolean.
     * @return the boolean.
     */
    public final boolean getBoolean(final int index) {
        if (object[checkIndex(index)] != AnyType.Boolean) {
            throw new IllegalStateException("Not a boolean: " + object);
        }
        return (primitive[index] != 0);
    }

    /**
     * Return the boolean at this[index], without validation.
     * @return the boolean.
     */
    public final boolean getBooleanUnsafe(final int index) {
        return (primitive[index] != 0);
    }

    /** Sets this[index] with a byte. */
    public final AnyArray setByte(final int index, final byte value) {
        object[checkIndex(index)] = AnyType.Byte;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the byte at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an byte.
     * @return the byte.
     */
    public final byte getByte(final int index) {
        if (object[checkIndex(index)] != AnyType.Byte) {
            throw new IllegalStateException("Not a byte: " + object);
        }
        return (byte) primitive[index];
    }

    /**
     * Return the byte at this[index], without validation.
     * @return the byte.
     */
    public final byte getByteUnsafe(final int index) {
        return (byte) primitive[index];
    }

    /** Sets this[index] with a char. */
    public final AnyArray setChar(final int index, final char value) {
        object[checkIndex(index)] = AnyType.Char;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the char at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an char.
     * @return the char.
     */
    public final char getChar(final int index) {
        if (object[checkIndex(index)] != AnyType.Char) {
            throw new IllegalStateException("Not a char: " + object);
        }
        return (char) primitive[index];
    }

    /**
     * Return the char at this[index], without validation.
     * @return the char.
     */
    public final char getCharUnsafe(final int index) {
        return (char) primitive[index];
    }

    /** Sets this[index] with a short. */
    public final AnyArray setShort(final int index, final short value) {
        object[checkIndex(index)] = AnyType.Short;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the short at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an short.
     * @return the short.
     */
    public final short getShort(final int index) {
        if (object[checkIndex(index)] != AnyType.Short) {
            throw new IllegalStateException("Not a short: " + object);
        }
        return (short) primitive[index];
    }

    /**
     * Return the short at this[index], without validation.
     * @return the short.
     */
    public final short getShortUnsafe(final int index) {
        return (short) primitive[index];
    }

    /** Sets this[index] with a int. */
    public final AnyArray setInt(final int index, final int value) {
        object[checkIndex(index)] = AnyType.Int;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the int at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an int.
     * @return the int.
     */
    public final int getInt(final int index) {
        if (object[checkIndex(index)] != AnyType.Int) {
            throw new IllegalStateException("Not a int: " + object);
        }
        return (int) primitive[index];
    }

    /**
     * Return the int at this[index], without validation.
     * @return the int.
     */
    public final int getIntUnsafe(final int index) {
        return (int) primitive[index];
    }

    /** Sets this[index] with a long. */
    public final AnyArray setLong(final int index, final long value) {
        if ((value < MIN_LONG_VALUE) || (value > MAX_LONG_VALUE)) {
            object[checkIndex(index)] = new BigLongValue(value);
            primitive[index] = 0;
        } else {
            object[checkIndex(index)] = AnyType.Long;
            primitive[index] = value;
        }
        return this;
    }

    /**
     * Return the long at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an long.
     * @return the long.
     */
    public final long getLong(final int index) {
        final Object obj = object[checkIndex(index)];
        if (obj == AnyType.Long) {
            return (long) primitive[index];
        }
        if (!(obj instanceof BigLongValue)) {
            throw new IllegalStateException("Not a long: " + object);
        }
        return ((BigLongValue) obj).value;
    }

    /**
     * Return the long at this[index].
     * @return the long.
     */
    public final long getLongUnsafe(final int index) {
        final Object obj = object[index];
        if (obj == AnyType.Long) {
            return (long) primitive[index];
        }
        return ((BigLongValue) obj).value;
    }

    /** Sets this[index] with a float. */
    public final AnyArray setFloat(final int index, final float value) {
        object[checkIndex(index)] = AnyType.Float;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the float at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an float.
     * @return the float.
     */
    public final float getFloat(final int index) {
        if (object[checkIndex(index)] != AnyType.Float) {
            throw new IllegalStateException("Not a float: " + object);
        }
        return (float) primitive[index];
    }

    /**
     * Return the float at this[index], without validation.
     * @return the float.
     */
    public final float getFloatUnsafe(final int index) {
        return (float) primitive[index];
    }

    /** Sets this[index] with a double. */
    public final AnyArray setDouble(final int index, final double value) {
        object[checkIndex(index)] = AnyType.Double;
        primitive[index] = value;
        return this;
    }

    /**
     * Return the double at this[index].
     * @throws java.lang.IllegalStateException if this[index] does not contain an double.
     * @return the double.
     */
    public final double getDouble(final int index) {
        if (object[checkIndex(index)] != AnyType.Double) {
            throw new IllegalStateException("Not a double: " + object);
        }
        return primitive[index];
    }

    /**
     * Return the double at this[index], without validation.
     * @return the double.
     */
    public final double getDoubleUnsafe(final int index) {
        return primitive[index];
    }

    /**
     * Copies the value at this[index] into an Any.
     * @return The Any
     */
    public final Any copy(final int index) {
        return new Any(primitive[checkIndex(index)], object[index]);
    }

    /**
     * Copies the value at this[index] into an Any.
     *
     * @throws NullPointerException if any is null.
     */
    public final AnyArray copyTo(final int index, final Any any) {
        any.copyFrom(primitive[checkIndex(index)], object[index]);
        return this;
    }

    /**
     * Copies the value at this[index] into an Any, without validation.
     *
     * @throws NullPointerException if any is null.
     */
    final void copyToUnsafe(final int index, final Any any) {
        any.copyFrom(primitive[index], object[index]);
    }

    /**
     * Copies the value of an Any into this[index].
     *
     * @throws NullPointerException if any is null.
     */
    public final AnyArray copyFrom(final int index, final Any any) {
        primitive[checkIndex(index)] = any.primitive;
        object[index] = any.object;
        return this;
    }

    /**
     * Returns true if this[index] equals an Any.
     *
     * @throws NullPointerException if any is null.
     */
    public final boolean equals(final int index, final Any any) {
        return (any.primitive == primitive[checkIndex(index)])
                && ((any.object == null) ? (object[index] == null) : any.object
                        .equals(object[index]));
    }

    /**
     * Returns an Iterator over this AnyArray.
     *
     * Note that for efficiency reasons, the *same* Any is returned every time.
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<Any> iterator() {
        return new AnyIterator();
    }

    /** Iterator over the AnyArray. */
    private final class AnyIterator extends Any implements Iterator<Any> {
        /** serialVersionUID */
        private static final long serialVersionUID = 2406047614015224926L;

        private int next;

        @Override
        public boolean hasNext() {
            return next < size;
        }

        @Override
        public Any next() {
            if (next < size) {
                copyToUnsafe(next++, this);
                return this;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
