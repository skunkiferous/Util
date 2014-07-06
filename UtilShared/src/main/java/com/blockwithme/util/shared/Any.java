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

import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.shared.converters.BooleanConverter;
import com.blockwithme.util.shared.converters.Converter;
import com.blockwithme.util.shared.converters.LongConverter;
import com.blockwithme.util.shared.converters.StringConverter;

/**
 * Smallest possible mutable class that could contain anything,
 * without requiring "boxing" of primitive values (except long!).
 *
 * Note that it also supports the AnyType.Empty, which allows us to represent
 * "no data", for example, when querying the value of a non-existent property.
 *
 * It will usually consume 12 bytes of memory, plus the standard Object overhead.
 *
 * @author monster
 */
public class Any implements Serializable {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(Any.class.getName());

    /** serialVersionUID */
    private static final long serialVersionUID = -2510171712544971710L;

    /** Maximum integer value in a double. */
    private static final long MAX_LONG_VALUE = (long) SystemUtils.MAX_DOUBLE_INT_VALUE;

    /** Minimum integer value in a double. */
    private static final long MIN_LONG_VALUE = (long) SystemUtils.MIN_DOUBLE_INT_VALUE;

    /** The primitive data. */
    double primitive;
    /** The object data. */
    Object object;

    /** Default empty Any. */
    public Any() {
        object = AnyType.Empty;
    }

    /**
     * Copy constructor.
     */
    Any(final double _primitive, final Object _object) {
        primitive = _primitive;
        object = _object;
    }

    /** Any with Object. */
    public Any(final Object obj) {
        setObject(obj);
    }

    /** Any with boolean. */
    public Any(final boolean value) {
        setBoolean(value);
    }

    /** Any with byte. */
    public Any(final byte value) {
        setByte(value);
    }

    /** Any with short. */
    public Any(final short value) {
        setShort(value);
    }

    /** Any with char. */
    public Any(final char value) {
        setChar(value);
    }

    /** Any with int. */
    public Any(final int value) {
        setInt(value);
    }

    /** Any with long. */
    public Any(final long value) {
        setLong(value);
    }

    /** Any with float. */
    public Any(final float value) {
        setFloat(value);
    }

    /** Any with double. */
    public Any(final double value) {
        setDouble(value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final long l = (long) primitive;
        if (l == primitive) {
            // primitive is an integral value, so don't show decimals.
            return "Any [primitive=" + l + ", object=" + object + "]";
        }
        return "Any [primitive=" + primitive + ", object=" + object + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((object == null) ? 0 : object.hashCode());
        final long l = Double.doubleToLongBits(primitive);
        result = prime * result + (int) (l ^ (l >>> 32));
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Any other = (Any) obj;
        if (object == null) {
            return (other.object == null);
        } else if (!object.equals(other.object)) {
            return false;
        }
        if (primitive != other.primitive)
            return false;
        return true;
    }

    /** Returns a copy. */
    public final Any copy() {
        return new Any(primitive, object);
    }

    /** Is this any empty? Empty mean no value set; it is different from having set "null" as object. */
    public final boolean isEmpty() {
        return (object == AnyType.Empty);
    }

    /** Returns the current type of the data. */
    public final AnyType type() {
        return type(object);
    }

    /** Returns the current JSON type of the data. */
    public final JSONType jsonType() {
        return jsonType(object);
    }

    /**  Clears this Any. */
    public final void clear() {
        object = AnyType.Empty;
        primitive = 0;
    }

    /**
     * Copy method.
     *
     * @throws NullPointerException if other is null.
     */
    public final void copyFrom(final Any other) {
        primitive = other.primitive;
        object = other.object;
    }

    /**
     * Internal Copy method.
     */
    final void copyFrom(final double _primitive, final Object _object) {
        primitive = _primitive;
        object = _object;
    }

    /** Sets the Any with an Object. */
    public final void setObject(final Object obj) {
        if (obj instanceof AnyType) {
            throw new IllegalArgumentException("Cannot contain AnyType!");
        }
        if (obj instanceof Any) {
            final Any other = (Any) obj;
            object = other.object;
            primitive = other.primitive;
        } else {
            object = obj;
            primitive = 0;
        }
    }

    /**
     * Return the Object.
     * @throws java.lang.IllegalStateException if Any does not contain an Object.
     * @return the Object.
     */
    public final Object getObject() {
        if (object instanceof AnyType) {
            throw new IllegalStateException("Not an Object: " + object);
        }
        if (object instanceof BigLongValue) {
            throw new IllegalStateException("Not an Object: " + AnyType.Long);
        }
        return object;
    }

    /**
     * Return the Object, without validation.
     * @return the Object.
     */
    public final Object getObjectUnsafe() {
        return object;
    }

    /** Sets the Any with a boolean. */
    public final void setBoolean(final boolean value) {
        object = AnyType.Boolean;
        primitive = value ? 1 : 0;
    }

    /**
     * Return the boolean.
     * @throws java.lang.IllegalStateException if Any does not contain an boolean.
     * @return the boolean.
     */
    public final boolean getBoolean() {
        if (object != AnyType.Boolean) {
            throw new IllegalStateException("Not a boolean: " + object);
        }
        return (primitive != 0);
    }

    /**
     * Return the boolean, without validation.
     * @return the boolean.
     */
    public final boolean getBooleanUnsafe() {
        return (primitive != 0);
    }

    /** Sets the Any with a byte. */
    public final void setByte(final byte value) {
        object = AnyType.Byte;
        primitive = value;
    }

    /**
     * Return the byte.
     * @throws java.lang.IllegalStateException if Any does not contain an byte.
     * @return the byte.
     */
    public final byte getByte() {
        if (object != AnyType.Byte) {
            throw new IllegalStateException("Not a byte: " + object);
        }
        return (byte) primitive;
    }

    /**
     * Return the byte, without validation.
     * @return the byte.
     */
    public final byte getByteUnsafe() {
        return (byte) primitive;
    }

    /** Sets the Any with a char. */
    public final void setChar(final char value) {
        object = AnyType.Char;
        primitive = value;
    }

    /**
     * Return the char.
     * @throws java.lang.IllegalStateException if Any does not contain an char.
     * @return the char.
     */
    public final char getChar() {
        if (object != AnyType.Char) {
            throw new IllegalStateException("Not a char: " + object);
        }
        return (char) primitive;
    }

    /**
     * Return the char, without validation.
     * @return the char.
     */
    public final char getCharUnsafe() {
        return (char) primitive;
    }

    /** Sets the Any with a short. */
    public final void setShort(final short value) {
        object = AnyType.Short;
        primitive = value;
    }

    /**
     * Return the short.
     * @throws java.lang.IllegalStateException if Any does not contain an short.
     * @return the short.
     */
    public final short getShort() {
        if (object != AnyType.Short) {
            throw new IllegalStateException("Not a short: " + object);
        }
        return (short) primitive;
    }

    /**
     * Return the short, without validation.
     * @return the short.
     */
    public final short getShortUnsafe() {
        return (short) primitive;
    }

    /** Sets the Any with a int. */
    public final void setInt(final int value) {
        object = AnyType.Int;
        primitive = value;
    }

    /**
     * Return the int.
     * @throws java.lang.IllegalStateException if Any does not contain an int.
     * @return the int.
     */
    public final int getInt() {
        if (object != AnyType.Int) {
            throw new IllegalStateException("Not a int: " + object);
        }
        return (int) primitive;
    }

    /**
     * Return the int, without validation.
     * @return the int.
     */
    public final int getIntUnsafe() {
        return (int) primitive;
    }

    /** Sets the Any with a long. */
    public final void setLong(final long value) {
        if ((value < MIN_LONG_VALUE) || (value > MAX_LONG_VALUE)) {
            object = new BigLongValue(value);
            primitive = 0;
        } else {
            object = AnyType.Long;
            primitive = value;
        }
    }

    /**
     * Return the long.
     * @throws java.lang.IllegalStateException if Any does not contain an long.
     * @return the long.
     */
    public final long getLong() {
        if (object == AnyType.Long) {
            return (long) primitive;
        }
        if (!(object instanceof BigLongValue)) {
            throw new IllegalStateException("Not a long: " + object);
        }
        return ((BigLongValue) object).value;
    }

    /**
     * Return the long.
     * @return the long.
     */
    public final long getLongUnsafe() {
        if (object == AnyType.Long) {
            return (long) primitive;
        }
        return ((BigLongValue) object).value;
    }

    /** Sets the Any with a float. */
    public final void setFloat(final float value) {
        object = AnyType.Float;
        primitive = value;
    }

    /**
     * Return the float.
     * @throws java.lang.IllegalStateException if Any does not contain an float.
     * @return the float.
     */
    public final float getFloat() {
        if (object != AnyType.Float) {
            throw new IllegalStateException("Not a float: " + object);
        }
        return (float) primitive;
    }

    /**
     * Return the float, without validation.
     * @return the float.
     */
    public final float getFloatUnsafe() {
        return (float) primitive;
    }

    /** Sets the Any with a double. */
    public final void setDouble(final double value) {
        object = AnyType.Double;
        primitive = value;
    }

    /**
     * Return the double.
     * @throws java.lang.IllegalStateException if Any does not contain an double.
     * @return the double.
     */
    public final double getDouble() {
        if (object != AnyType.Double) {
            throw new IllegalStateException("Not a double: " + object);
        }
        return primitive;
    }

    /**
     * Return the double, without validation.
     * @return the double.
     */
    public final double getDoubleUnsafe() {
        return primitive;
    }

    /** Returns the current type of the data. */
    static AnyType type(final Object object) {
        if (object instanceof AnyType) {
            // Not an object
            return (AnyType) object;
        }
        if (object instanceof BigLongValue) {
            return AnyType.Long;
        }
        // Must be an object, including null
        return AnyType.Object;
    }

    /** Returns the current JSON type of the data. */
    static JSONType jsonType(final Object object) {
        if (object == null) {
            return JSONType.Null;
        }
        if (object instanceof BigLongValue) {
            return JSONType.String;
        }
        if (object instanceof AnyType) {
            switch ((AnyType) object) {
            case Empty:
                return JSONType.Null;

            case Boolean:
                return JSONType.Boolean;

            default:
                // if at == Long, it means it's a 'small' long, so a Number
                return JSONType.Number;
            }
        }
        if ((object instanceof CharSequence) || (object instanceof Class<?>)
                || (object instanceof File)) {
            return JSONType.String;
        }
        if ((object instanceof Iterable<?>) || (object instanceof Iterator<?>)
                || (object instanceof Enumeration<?>)) {
            return JSONType.Array;
        }
        final Class<?> type = object.getClass();
        if (type.isArray()) {
            return JSONType.Array;
        }
        final Converter<?> c = ConverterRegistry.instance().find(type);
        if (c != null) {
            if (c instanceof BooleanConverter) {
                return JSONType.Boolean;
            }
            if (c instanceof StringConverter) {
                return JSONType.String;
            }
            if (c instanceof LongConverter) {
                // We need to check ...
                try {
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    final long value = ((LongConverter) c).fromObject(null,
                            object);
                    if ((value < MIN_LONG_VALUE) || (value > MAX_LONG_VALUE)) {
                        return JSONType.String;
                    }
                    return JSONType.Number;
                } catch (final Exception e) {
                    // NOP (we still don't know)
                }
            }
        }
        if (type.isEnum()) {
            LOG.warning("Enum type " + type
                    + " has not been registered in the ConverterRegistry");
            return JSONType.String;
        }
        // Unknown; assume Object
        return JSONType.Object;
    }
}
