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
package com.blockwithme.util.server;

import java.util.Arrays;
import java.util.Map;

import com.blockwithme.util.shared.PropertiesHelper;

/**
 * A generic object that can contain anything.
 *
 * Primitive values are stored efficiently, without creating new objects/wrappers.
 *
 * Internally, new arrays are always allocated using a power of two, to reduce
 * temporary object creation, in the case of growth.
 *
 * Since the arrays could be reused, and are only used for temporary storage
 * (or at least, that is what I plain to use it for), I don't try to save the
 * last bit of RAM, but rather aims for fast read/write speed. This means for
 * example that a boolean takes as much room as an long.
 *
 * Using the built-in indexes, it is also possible to let the generic object
 * manage the position in the arrays itself.
 *
 * TODO test
 *
 * Moved to non-GWT because unlikely to be used at all; I have a whole project
 * now implementing generic-objects.
 *
 * @author monster
 */
public class GenericObject {
    public static final Object[] NO_OBJECTS = new Object[0];
    public static final long[] NO_DATA = new long[0];

    /** The object values. Never null. */
    private Object[] objects;

    /** The data values. Never null. */
    private long[] data;

    /** The object array index. */
    private int objectIndex;

    /** The data array index. */
    private int dataIndex;

    /**
     * Arbitrary properties. Odd index is the property name (String), and even
     * index is the property value.
     */
    private Object[] properties;

    /** Creates a default generic object. */
    public GenericObject() {
        objects = NO_OBJECTS;
        data = NO_DATA;
        properties = NO_OBJECTS;
    }

    /**
     * Creates a generic object with the given arrays.
     *
     * Note that on growth of the arrays, the original array can then not be
     * used anymore.
     *
     * Null arrays are allowed, but not recommended. Use the empty array
     * constants instead.
     *
     * @param objects the array containing the objects, if any
     * @param data the array containing the primitive data, if any
     */
    public GenericObject(final Object[] objects, final long[] data) {
        this.objects = (objects == null) ? NO_OBJECTS : objects;
        this.data = (data == null) ? NO_DATA : data;
        properties = NO_OBJECTS;
    }

    /**
     * Creates a generic object with the given capacities.
     *
     * @param minObjectsCapacity The minimum size for the object array. Actual size might be bigger.
     * @param minDataCapacity The minimum size for the data array. Actual size might be bigger.
     */
    public GenericObject(final int minObjectsCapacity, final int minDataCapacity) {
        this.objects = (minObjectsCapacity <= 0) ? NO_OBJECTS
                : new Object[PropertiesHelper.powerOfTwo(minObjectsCapacity)];
        this.data = (minDataCapacity <= 0) ? NO_DATA
                : new long[PropertiesHelper.powerOfTwo(minDataCapacity)];
        properties = NO_OBJECTS;
    }

    /** Returns the count of non-null properties. */
    public final int getPropertiesCount() {
        return PropertiesHelper.getPropertiesCount(properties);
    }

    /** Returns the non-null property keyes. */
    public final String[] getPropertyKeyes() {
        return PropertiesHelper.getPropertyKeyes(properties);
    }

    /** Returns one property if present, otherwise null. */
    public final Object getProperty(final String name) {
        return PropertiesHelper.getProperty(properties, name);
    }

    /**
     * Sets one property. Null means "remove".
     * Returns the old value.
     */
    public final GenericObject setProperty(final String name,
            final Object newValue) {
        properties = PropertiesHelper.setProperty(properties, name, newValue);
        return this;
    }

    /** Removes one property. Returns the old value. */
    public final GenericObject removeProperty(final String name) {
        properties = PropertiesHelper.removeProperty(properties, name);
        return this;
    }

    /** Set all the properties. */
    public final GenericObject setProperties(
            final Map<String, Object> newProperties) {
        properties = PropertiesHelper.setProperties(properties, newProperties);
        return this;
    }

    /** Returns all properties. */
    public final Map<String, Object> getProperties() {
        return PropertiesHelper.getProperties(properties);
    }

    /** Clears the generic object. */
    public final GenericObject clear() {
        Arrays.fill(objects, null);
        Arrays.fill(data, 0);
        Arrays.fill(properties, null);
        dataIndex = objectIndex = 0;
        return this;
    }

    /** Returns the current objects capacity. */
    public final int getObjectsCapacity() {
        return objects.length;
    }

    /** Returns the current data capacity. */
    public final int getDataCapacity() {
        return data.length;
    }

    /** Returns the current combined capacity of data and objects together. */
    public final int getCombinedCapacity() {
        return data.length + objects.length;
    }

    /** Ensures the minimum total object capacity. */
    public final GenericObject ensureTotalObjectCapacity(
            final int minObjectsCapacity) {
        final int oldCapacity = getObjectsCapacity();
        if (minObjectsCapacity > oldCapacity) {
            final int capacity = PropertiesHelper
                    .powerOfTwo(minObjectsCapacity);
            if (oldCapacity == 0) {
                objects = new Object[capacity];
            } else {
                objects = Arrays.copyOf(objects, capacity);
            }
        }
        return this;
    }

    /** Ensures the minimum total data capacity. */
    public final GenericObject ensureTotalDataCapacity(final int minDataCapacity) {
        final int oldCapacity = getDataCapacity();
        if (minDataCapacity > oldCapacity) {
            final int newCapacity = PropertiesHelper
                    .powerOfTwo(minDataCapacity);
            if (oldCapacity == 0) {
                data = new long[newCapacity];
            } else {
                data = Arrays.copyOf(data, newCapacity);
            }
        }
        return this;
    }

    /** Ensures the minimum *free* object capacity. */
    public final GenericObject ensureFreeObjectCapacity(final int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Bad increment: " + increment);
        }
        return ensureTotalObjectCapacity(objectIndex + increment);
    }

    /** Ensures the minimum *free* data capacity. */
    public final GenericObject ensureFreeDataCapacity(final int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Bad increment: " + increment);
        }
        return ensureTotalDataCapacity(dataIndex + increment);
    }

    /** Returns the current underlying object array. */
    public final Object[] getObjects() {
        return objects;
    }

    /**
     * Sets the current underlying object array.
     *
     * Null arrays are allowed, but not recommended. Use the empty array
     * constants instead.
     */
    public final GenericObject setObjects(final Object[] newObjects) {
        objects = (newObjects == null) ? NO_OBJECTS : newObjects;
        return this;
    }

    /** Returns the current underlying data array. */
    public final long[] getData() {
        return data;
    }

    /**
     * Sets the current underlying data array.
     *
     * Null arrays are allowed, but not recommended. Use the empty array
     * constants instead.
     */
    public final GenericObject setData(final long[] newData) {
        data = (newData == null) ? NO_DATA : newData;
        return this;
    }

    /** Returns the object array index. */
    public final int getObjectIndex() {
        return objectIndex;
    }

    /**
     * Returns the data array index + object array index.
     * It is equivalent to a "global" (object and data together) index,
     * but one cannot read a value at a specific combined index.
     */
    public final int getCombinedIndex() {
        return dataIndex + objectIndex;
    }

    /**
     * Sets the object array index.
     *
     * @param newObjectIndex the new object array index. Not validation performed!
     */
    public final GenericObject setObjectIndex(final int newObjectIndex) {
        objectIndex = newObjectIndex;
        return this;
    }

    /** Returns the data array index. */
    public final int getDataIndex() {
        return dataIndex;
    }

    /**
     * Sets the data array index.
     *
     * @param newDataIndex the new object array index. Not validation performed!
     */
    public final GenericObject setDataIndex(final int newDataIndex) {
        dataIndex = newDataIndex;
        return this;
    }

    /**
     * Return the boolean at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final boolean getBoolean(final int dataIndex) {
        return data[dataIndex] != 0;
    }

    /**
     * Sets the boolean at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setBoolean(final int dataIndex,
            final boolean value) {
        data[dataIndex] = value ? 1 : 0;
        return this;
    }

    /**
     * Return the byte at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final byte getByte(final int dataIndex) {
        return (byte) data[dataIndex];
    }

    /**
     * Sets the byte at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setByte(final int dataIndex, final byte value) {
        data[dataIndex] = value;
        return this;
    }

    /**
     * Return the short at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final short getShort(final int dataIndex) {
        return (short) data[dataIndex];
    }

    /**
     * Sets the short at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setShort(final int dataIndex, final short value) {
        data[dataIndex] = value;
        return this;
    }

    /**
     * Return the char at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final char getChar(final int dataIndex) {
        return (char) data[dataIndex];
    }

    /**
     * Sets the char at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setChar(final int dataIndex, final char value) {
        data[dataIndex] = value;
        return this;
    }

    /**
     * Return the int at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final int getInt(final int dataIndex) {
        return (int) data[dataIndex];
    }

    /**
     * Sets the int at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setInt(final int dataIndex, final int value) {
        data[dataIndex] = value;
        return this;
    }

    /**
     * Return the float at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final float getFloat(final int dataIndex) {
        return Float.intBitsToFloat((int) data[dataIndex]);
    }

    /**
     * Sets the float at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setFloat(final int dataIndex, final float value) {
        data[dataIndex] = Float.floatToRawIntBits(value);
        return this;
    }

    /**
     * Return the long at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final long getLong(final int dataIndex) {
        return data[dataIndex];
    }

    /**
     * Sets the long at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setLong(final int dataIndex, final long value) {
        data[dataIndex] = value;
        return this;
    }

    /**
     * Return the double at the given *data* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final double getDouble(final int dataIndex) {
        return Double.longBitsToDouble(getLong(dataIndex));
    }

    /**
     * Sets the double at the given *data* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setDouble(final int dataIndex, final double value) {
        return setLong(dataIndex, Double.doubleToRawLongBits(value));
    }

    /**
     * Return the object at the given *object* index.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored object.
     *
     * @throws java.lang.NullPointerException if objects is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    @SuppressWarnings("unchecked")
    public final <E> E getObject(final int objectIndex) {
        return (E) objects[objectIndex];
    }

    /**
     * Sets the object at the given *object* index.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if objects is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setObject(final int objectIndex,
            final Object value) {
        objects[objectIndex] = value;
        return this;
    }

    /**
     * Return the next boolean.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final boolean getBoolean() {
        return getBoolean(dataIndex++);
    }

    /**
     * Sets the next boolean.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setBoolean(final boolean value) {
        return setBoolean(dataIndex++, value);
    }

    /**
     * Sets the next boolean. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setBooleanSafe(final boolean value) {
        ensureFreeDataCapacity(1);
        return setBoolean(value);
    }

    /**
     * Return the next byte.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final byte getByte() {
        return getByte(dataIndex++);
    }

    /**
     * Sets the next byte.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setByte(final byte value) {
        return setByte(dataIndex++, value);
    }

    /**
     * Sets the next byte. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setByteSafe(final byte value) {
        ensureFreeDataCapacity(1);
        return setByte(value);
    }

    /**
     * Return the next short.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final short getShort() {
        return getShort(dataIndex++);
    }

    /**
     * Sets the next short.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setShort(final short value) {
        return setShort(dataIndex++, value);
    }

    /**
     * Sets the next short. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setShortSafe(final short value) {
        ensureFreeDataCapacity(1);
        return setShort(value);
    }

    /**
     * Return the next char.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final char getChar() {
        return getChar(dataIndex++);
    }

    /**
     * Sets the next char.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setChar(final char value) {
        return setChar(dataIndex++, value);
    }

    /**
     * Sets the next char. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setCharSafe(final char value) {
        ensureFreeDataCapacity(1);
        return setChar(value);
    }

    /**
     * Return the next int.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final int getInt() {
        return getInt(dataIndex++);
    }

    /**
     * Sets the next int.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setInt(final int value) {
        return setInt(dataIndex++, value);
    }

    /**
     * Sets the next int. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setIntSafe(final int value) {
        ensureFreeDataCapacity(1);
        return setInt(value);
    }

    /**
     * Return the next float.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final float getFloat() {
        return getFloat(dataIndex++);
    }

    /**
     * Sets the next float.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setFloat(final float value) {
        return setFloat(dataIndex++, value);
    }

    /**
     * Sets the next float. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setFloatSafe(final float value) {
        ensureFreeDataCapacity(1);
        return setFloat(value);
    }

    /**
     * Return the next long.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final long getLong() {
        final long result = getLong(dataIndex++);
        return result;
    }

    /**
     * Sets the next long.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setLong(final long value) {
        setLong(dataIndex++, value);
        return this;
    }

    /**
     * Sets the next long. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setLongSafe(final long value) {
        ensureFreeDataCapacity(1);
        return setLong(value);
    }

    /**
     * Return the next double.
     *
     * Note that there is no validation against the type of the currently stored data.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final double getDouble() {
        final double result = getDouble(dataIndex++);
        return result;
    }

    /**
     * Sets the next double.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setDouble(final double value) {
        setDouble(dataIndex++, value);
        return this;
    }

    /**
     * Sets the next double. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if data is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setDoubleSafe(final double value) {
        ensureFreeDataCapacity(1);
        return setDouble(value);
    }

    /**
     * Return the next object.
     *
     * Note that the data and object index are independent. Also note that
     * there is no validation against the type of the currently stored object.
     *
     * @throws java.lang.NullPointerException if objects is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final <E> E getObject() {
        return getObject(objectIndex++);
    }

    /**
     * Sets the next object.
     *
     * Note that the data and object index are independent.
     *
     * @throws java.lang.NullPointerException if objects is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setObject(final Object value) {
        return setObject(objectIndex++, value);
    }

    /**
     * Sets the next object. Grows the array if needed.
     *
     * @throws java.lang.NullPointerException if objects is null
     * @throws java.lang.ArrayIndexOutOfBoundsException if index outside valid range
     */
    public final GenericObject setObjectSafe(final Object value) {
        ensureFreeObjectCapacity(1);
        return setObject(objectIndex++, value);
    }

    /**
     * Insert count slots into the data array, at the given index.
     * The data index is appropriately updated.
     */
    public final GenericObject insertDataSlots(final int index, final int count) {
        if (index >= data.length) {
            throw new IllegalArgumentException("Bad index: " + index);
        }
        ensureFreeDataCapacity(count);
        System.arraycopy(data, index, data, index + count, count);
        if (dataIndex >= index) {
            dataIndex += count;
        }
        return this;
    }

    /**
     * Insert count slots into the object array, at the given index.
     * The object index is appropriately updated.
     */
    public final GenericObject insertObjectSlots(final int index,
            final int count) {
        if (index >= objects.length) {
            throw new IllegalArgumentException("Bad index: " + index);
        }
        ensureFreeObjectCapacity(count);
        System.arraycopy(objects, index, objects, index + count, count);
        if (objectIndex >= index) {
            objectIndex += count;
        }
        return this;
    }

    /**
     * Remove count slots out of the data array, at the given index.
     * The data index is appropriately updated.
     */
    public final GenericObject removeDataSlots(final int index, final int count) {
        if (index >= data.length) {
            throw new IllegalArgumentException("Bad index: " + index);
        }
        if (index + count > data.length) {
            throw new IllegalArgumentException("Bad count: " + count);
        }
        System.arraycopy(data, index + count, data, index, count);
        if (dataIndex >= index) {
            if (dataIndex >= index + count) {
                dataIndex -= count;
            } else {
                dataIndex = index;
            }
        }
        return this;
    }

    /**
     * Remove count slots out of the object array, at the given index.
     * The object index is appropriately updated.
     */
    public final GenericObject removeObjectSlots(final int index,
            final int count) {
        if (index >= objects.length) {
            throw new IllegalArgumentException("Bad index: " + index);
        }
        if (index + count > objects.length) {
            throw new IllegalArgumentException("Bad count: " + count);
        }
        System.arraycopy(objects, index + count, objects, index, count);
        if (objectIndex >= index) {
            if (objectIndex >= index + count) {
                objectIndex -= count;
            } else {
                objectIndex = index;
            }
        }

        return this;
    }
}
