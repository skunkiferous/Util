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

import com.blockwithme.util.base.SystemUtils;

/**
 * <code>AnyAccessor</code> is an helper class that allows the benefits of using
 * <code>Any</code>, which are to store any value, without causing the instantiation
 * of primitive wrappers (and therefore the creation of garbage), but without
 * the "cost" of having to instantiate an <code>Any</code> instance per value
 * being store. This class is meant to be used as singleton, and directly access
 * the fields within a domain class. This class also allows the creation of multiple
 * such accessor pointing to the same domain class, but accessing different fields.
 *
 * The cost of using this class is one added indirection.
 *
 * @author monster
 */
public abstract class AnyAccessor<E> {
    /** Maximum integer value in a double. */
    private static final long MAX_LONG_VALUE = (long) SystemUtils.MAX_DOUBLE_INT_VALUE;

    /** Minimum integer value in a double. */
    private static final long MIN_LONG_VALUE = (long) SystemUtils.MIN_DOUBLE_INT_VALUE;

    /** Is this any empty? Empty mean no value set; it is different from having set "null" as object. */
    public final boolean isEmpty(final E holder) {
        return (getObjectUnsafe(holder) == AnyType.Empty);
    }

    /** Returns the current type of the data. */
    public final AnyType type(final E holder) {
        return Any.type(getObjectUnsafe(holder));
    }

    /** Returns the current JSON type of the data. */
    public final JSONType jsonType(final E holder) {
        return Any.jsonType(getObjectUnsafe(holder));
    }

    /**  Clears this Any. */
    public final void clear(final E holder) {
        setObjectPrimitive(holder, AnyType.Empty, 0);
    }

    /** Sets the Any with an Object. */
    public final void setObject(final E holder, final Object obj) {
        if (obj instanceof AnyType) {
            throw new IllegalArgumentException("Cannot contain AnyType!");
        }
        setObjectPrimitive(holder, obj, 0);
    }

    /**
     * Return the Object.
     * @throws java.lang.IllegalStateException if Any does not contain an Object.
     * @return the Object.
     */
    public final Object getObject(final E holder) {
        final Object o = getObjectUnsafe(holder);
        if (o instanceof AnyType) {
            throw new IllegalStateException("Not an Object: " + o);
        }
        if (o instanceof BigLongValue) {
            throw new IllegalStateException("Not an Object: " + AnyType.Long);
        }
        return o;
    }

    /** Sets the Any with a boolean. */
    public final void setBoolean(final E holder, final boolean value) {
        setObjectPrimitive(holder, AnyType.Boolean, value ? 1 : 0);
    }

    /**
     * Return the boolean.
     * @throws java.lang.IllegalStateException if Any does not contain an boolean.
     * @return the boolean.
     */
    public final boolean getBoolean(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Boolean) {
            throw new IllegalStateException("Not a boolean: "
                    + getObjectUnsafe(holder));
        }
        return (getDoubleUnsafe(holder) != 0);
    }

    /**
     * Return the boolean, without validation.
     * @return the boolean.
     */
    public final boolean getBooleanUnsafe(final E holder) {
        return (getDoubleUnsafe(holder) != 0);
    }

    /** Sets the Any with a byte. */
    public final void setByte(final E holder, final byte value) {
        setObjectPrimitive(holder, AnyType.Byte, value);
    }

    /**
     * Return the byte.
     * @throws java.lang.IllegalStateException if Any does not contain an byte.
     * @return the byte.
     */
    public final byte getByte(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Byte) {
            throw new IllegalStateException("Not a byte: "
                    + getObjectUnsafe(holder));
        }
        return (byte) getDoubleUnsafe(holder);
    }

    /**
     * Return the byte, without validation.
     * @return the byte.
     */
    public final byte getByteUnsafe(final E holder) {
        return (byte) getDoubleUnsafe(holder);
    }

    /** Sets the Any with a char. */
    public final void setChar(final E holder, final char value) {
        setObjectPrimitive(holder, AnyType.Char, value);
    }

    /**
     * Return the char.
     * @throws java.lang.IllegalStateException if Any does not contain an char.
     * @return the char.
     */
    public final char getChar(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Char) {
            throw new IllegalStateException("Not a char: "
                    + getObjectUnsafe(holder));
        }
        return (char) getDoubleUnsafe(holder);
    }

    /**
     * Return the char, without validation.
     * @return the char.
     */
    public final char getCharUnsafe(final E holder) {
        return (char) getDoubleUnsafe(holder);
    }

    /** Sets the Any with a short. */
    public final void setShort(final E holder, final short value) {
        setObjectPrimitive(holder, AnyType.Short, value);
    }

    /**
     * Return the short.
     * @throws java.lang.IllegalStateException if Any does not contain an short.
     * @return the short.
     */
    public final short getShort(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Short) {
            throw new IllegalStateException("Not a short: "
                    + getObjectUnsafe(holder));
        }
        return (short) getDoubleUnsafe(holder);
    }

    /**
     * Return the short, without validation.
     * @return the short.
     */
    public final short getShortUnsafe(final E holder) {
        return (short) getDoubleUnsafe(holder);
    }

    /** Sets the Any with a int. */
    public final void setInt(final E holder, final int value) {
        setObjectPrimitive(holder, AnyType.Int, value);
    }

    /**
     * Return the int.
     * @throws java.lang.IllegalStateException if Any does not contain an int.
     * @return the int.
     */
    public final int getInt(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Int) {
            throw new IllegalStateException("Not a int: "
                    + getObjectUnsafe(holder));
        }
        return (int) getDoubleUnsafe(holder);
    }

    /**
     * Return the int, without validation.
     * @return the int.
     */
    public final int getIntUnsafe(final E holder) {
        return (int) getDoubleUnsafe(holder);
    }

    /** Sets the Any with a long. */
    public final void setLong(final E holder, final long value) {
        if ((value < MIN_LONG_VALUE) || (value > MAX_LONG_VALUE)) {
            setObjectPrimitive(holder, new BigLongValue(value), 0);
        } else {
            setObjectPrimitive(holder, AnyType.Long, value);
        }
    }

    /**
     * Return the long.
     * @throws java.lang.IllegalStateException if Any does not contain an long.
     * @return the long.
     */
    public final long getLong(final E holder) {
        final Object o = getObjectUnsafe(holder);
        if (o == AnyType.Long) {
            return (long) getDoubleUnsafe(holder);
        }
        if (!(o instanceof BigLongValue)) {
            throw new IllegalStateException("Not a long: " + o);
        }
        return ((BigLongValue) o).value;
    }

    /**
     * Return the long.
     * @return the long.
     */
    public final long getLongUnsafe(final E holder) {
        final Object o = getObjectUnsafe(holder);
        if (o == AnyType.Long) {
            return (long) getDoubleUnsafe(holder);
        }
        return ((BigLongValue) o).value;
    }

    /** Sets the Any with a float. */
    public final void setFloat(final E holder, final float value) {
        setObjectPrimitive(holder, AnyType.Float, value);
    }

    /**
     * Return the float.
     * @throws java.lang.IllegalStateException if Any does not contain an float.
     * @return the float.
     */
    public final float getFloat(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Float) {
            throw new IllegalStateException("Not a float: "
                    + getObjectUnsafe(holder));
        }
        return (float) getDoubleUnsafe(holder);
    }

    /**
     * Return the float, without validation.
     * @return the float.
     */
    public final float getFloatUnsafe(final E holder) {
        return (float) getDoubleUnsafe(holder);
    }

    /** Sets the Any with a double. */
    public final void setDouble(final E holder, final double value) {
        setObjectPrimitive(holder, AnyType.Double, value);
    }

    /**
     * Return the double.
     * @throws java.lang.IllegalStateException if Any does not contain an double.
     * @return the double.
     */
    public final double getDouble(final E holder) {
        if (getObjectUnsafe(holder) != AnyType.Double) {
            throw new IllegalStateException("Not a double: "
                    + getObjectUnsafe(holder));
        }
        return getDoubleUnsafe(holder);
    }

    /**
     * Return the double, without validation.
     * @return the double.
     */
    public abstract double getDoubleUnsafe(E holder);

    /**
     * Return the Object, without validation.
     * @return the Object.
     */
    public abstract Object getObjectUnsafe(E holder);

    /** Sets the Object and primitive data. */
    protected abstract void setObjectPrimitive(E holder, Object object,
            double primitive);
}
