/**
 *
 */
package com.blockwithme.util.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

/**
 * Test class for AnyArray.
 *
 * @author monster
 */
@SuppressWarnings("all")
public class AnyArrayTest {
    private void standardChecks(final AnyArray array, final int index,
            final AnyType type, final JSONType jsonType) {
        if (type == AnyType.Empty) {
            assertTrue(array.isEmpty(index));
        } else {
            assertFalse(array.isEmpty(index));
        }

        assertEquals(type, array.type(index));
        assertEquals(jsonType, array.jsonType(index));
        final Any copy = array.copy(index);
        assertEquals(type, copy.type());
        assertEquals(jsonType, copy.jsonType());
        assertTrue(array.equals(index, copy));

        boolean failed = false;
        if (!type.object) {
            try {
                array.getObject(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Object", failed);
            failed = false;
        }
        if (type != AnyType.Boolean) {
            try {
                array.getBoolean(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Boolean", failed);
            failed = false;
        }
        if (type != AnyType.Byte) {
            try {
                array.getByte(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Byte", failed);
            failed = false;
        }
        if (type != AnyType.Char) {
            try {
                array.getChar(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Char", failed);
            failed = false;
        }
        if (type != AnyType.Short) {
            try {
                array.getShort(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Short", failed);
            failed = false;
        }
        if (type != AnyType.Int) {
            try {
                array.getInt(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Int", failed);
            failed = false;
        }
        if (type != AnyType.Float) {
            try {
                array.getFloat(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Float", failed);
            failed = false;
        }
        if (type != AnyType.Long) {
            try {
                array.getLong(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Long", failed);
            failed = false;
        }
        if (type != AnyType.Double) {
            try {
                array.getDouble(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Double", failed);
        }

        failed = false;
        if (type != AnyType.Object) {
            try {
                array.getObjectUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ObjectUnsafe", failed);
        }
        if (type != AnyType.Boolean) {
            try {
                array.getBooleanUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("BooleanUnsafe", failed);
        }
        if (type != AnyType.Byte) {
            try {
                array.getByteUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ByteUnsafe", failed);
        }
        if (type != AnyType.Char) {
            try {
                array.getCharUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("CharUnsafe", failed);
        }
        if (type != AnyType.Short) {
            try {
                array.getShortUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ShortUnsafe", failed);
        }
        if (type != AnyType.Int) {
            try {
                array.getIntUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("IntUnsafe", failed);
        }
        if (type != AnyType.Float) {
            try {
                array.getFloatUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("FloatUnsafe", failed);
        }
        // any.getLongUnsafe() WILL throw, if (type != AnyType.Long)
        if (type != AnyType.Double) {
            try {
                array.getDoubleUnsafe(index);
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("DoubleUnsafe", failed);
        }

        array.clear(index);
        assertEquals(AnyType.Empty, array.type(index));
    }

    @Test
    public void testSize() {
        final AnyArray array = new AnyArray(3);
        assertFalse(array.isEmpty());
        assertEquals(3, array.getSize());
        array.clear();
        assertTrue(array.isEmpty());
        assertEquals(0, array.getSize());
        array.setSize(3);
        assertFalse(array.isEmpty());
        assertEquals(3, array.getSize());
    }

    @Test
    public void testEquals() {
        final AnyArray a = new AnyArray(3);
        a.setBoolean(0, true);
        a.setInt(1, 42);
        a.setObject(2, "x");
        final AnyArray b = new AnyArray(3);
        assertFalse(a.equals(b));
        b.setBoolean(0, true);
        b.setInt(1, 42);
        b.setObject(2, "x");
        assertEquals(a, b);
    }

    @Test
    public void testCopy() {
        final AnyArray a = new AnyArray(3);
        a.setBoolean(0, true);
        a.setInt(1, 42);
        a.setObject(2, "x");
        final AnyArray b = a.copy();
        assertFalse(a == b);
        assertEquals(a, b);
        final AnyArray c = new AnyArray();
        c.copyFrom(a);
        assertEquals(a, c);
        final Any any = new Any();
        a.copyTo(0, any);
        assertEquals(true, any.getBoolean());
        a.copyFrom(2, any);
        assertEquals(true, a.getBoolean(2));
    }

    @Test
    public void testIterable() {
        final AnyArray a = new AnyArray(3);
        a.setBoolean(0, true);
        a.setInt(1, 42);
        a.setObject(2, "x");
        int index = 0;
        for (final Any v : a) {
            if (index == 0) {
                assertEquals(true, v.getBoolean());
            } else if (index == 1) {
                assertEquals(42, v.getInt());
            } else if (index == 2) {
                assertEquals("x", v.getObject());
            } else {
                throw new IllegalStateException("Size > 3");
            }
            index++;
        }
        assertEquals(3, index);
    }

    @Test
    public void testEmpty() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            checkEmpty(array, i);
        }
        array.clear();
        assertTrue(array.isEmpty());
        assertEquals(0, array.getSize());
    }

    private void checkEmpty(final AnyArray array, final int index) {
        standardChecks(array, index, AnyType.Empty, JSONType.Null);
    }

    @Test
    public void testBoolean() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkBoolean(array, i);
        }
    }

    private void checkBoolean(final AnyArray array, final int index) {
        array.setBoolean(index, true);
        assertEquals(AnyType.Boolean, array.type(index));
        assertTrue(array.getBoolean(index));
        assertTrue(array.getBooleanUnsafe(index));
        array.clear(index);
        array.setBoolean(index, false);
        assertEquals(AnyType.Boolean, array.type(index));
        assertFalse(array.getBoolean(index));
        assertFalse(array.getBooleanUnsafe(index));
        standardChecks(array, index, AnyType.Boolean, JSONType.Boolean);
    }

    @Test
    public void testByte() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkByte(array, i);
        }
    }

    private void checkByte(final AnyArray array, final int index) {
        array.setByte(index, (byte) 0);
        assertEquals(AnyType.Byte, array.type(index));
        assertEquals((byte) 0, array.getByte(index));
        assertEquals((byte) 0, array.getByteUnsafe(index));
        array.clear(index);
        array.setByte(index, (byte) 1);
        assertEquals(AnyType.Byte, array.type(index));
        assertEquals((byte) 1, array.getByte(index));
        assertEquals((byte) 1, array.getByteUnsafe(index));
        standardChecks(array, index, AnyType.Byte, JSONType.Number);
    }

    @Test
    public void testChar() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkChar(array, i);
        }
    }

    private void checkChar(final AnyArray array, final int index) {
        array.setChar(index, (char) 0);
        assertEquals(AnyType.Char, array.type(index));
        assertEquals((char) 0, array.getChar(index));
        assertEquals((char) 0, array.getCharUnsafe(index));
        array.clear(index);
        array.setChar(index, (char) 1);
        assertEquals(AnyType.Char, array.type(index));
        assertEquals((char) 1, array.getChar(index));
        assertEquals((char) 1, array.getCharUnsafe(index));
        standardChecks(array, index, AnyType.Char, JSONType.Number);
    }

    @Test
    public void testShort() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkShort(array, i);
        }
    }

    private void checkShort(final AnyArray array, final int index) {
        array.setShort(index, (short) 0);
        assertEquals(AnyType.Short, array.type(index));
        assertEquals((short) 0, array.getShort(index));
        assertEquals((short) 0, array.getShortUnsafe(index));
        array.clear(index);
        array.setShort(index, (short) 1);
        assertEquals(AnyType.Short, array.type(index));
        assertEquals((short) 1, array.getShort(index));
        assertEquals((short) 1, array.getShortUnsafe(index));
        standardChecks(array, index, AnyType.Short, JSONType.Number);
    }

    @Test
    public void testInt() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkInt(array, i);
        }
    }

    private void checkInt(final AnyArray array, final int index) {
        array.setInt(index, 0);
        assertEquals(AnyType.Int, array.type(index));
        assertEquals(0, array.getInt(index));
        assertEquals(0, array.getIntUnsafe(index));
        array.clear(index);
        array.setInt(index, 1);
        assertEquals(AnyType.Int, array.type(index));
        assertEquals(1, array.getInt(index));
        assertEquals(1, array.getIntUnsafe(index));
        standardChecks(array, index, AnyType.Int, JSONType.Number);
    }

    @Test
    public void testFloat() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkFloat(array, i);
        }
    }

    private void checkFloat(final AnyArray array, final int index) {
        array.setFloat(index, 0f);
        assertEquals(AnyType.Float, array.type(index));
        assertEquals(0f, array.getFloat(index), 0f);
        assertEquals(0f, array.getFloatUnsafe(index), 0f);
        array.clear(index);
        array.setFloat(index, 1f);
        assertEquals(AnyType.Float, array.type(index));
        assertEquals(1f, array.getFloat(index), 0f);
        assertEquals(1f, array.getFloatUnsafe(index), 0f);
        standardChecks(array, index, AnyType.Float, JSONType.Number);
    }

    @Test
    public void testLong() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkLong(array, i);
        }
    }

    private void checkLong(final AnyArray array, final int index) {
        array.setLong(index, 0l);
        assertEquals(AnyType.Long, array.type(index));
        assertEquals(0l, array.getLong(index));
        assertEquals(0l, array.getLongUnsafe(index));
        array.clear(index);
        array.setLong(index, 1l);
        assertEquals(AnyType.Long, array.type(index));
        assertEquals(1l, array.getLong(index));
        assertEquals(1l, array.getLongUnsafe(index));
        standardChecks(array, index, AnyType.Long, JSONType.Number);

        array.setLong(index, Long.MAX_VALUE);
        assertEquals(JSONType.String, array.jsonType(index));
        array.setLong(index, Long.MIN_VALUE);
        assertEquals(JSONType.String, array.jsonType(index));
    }

    @Test
    public void testDouble() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkDouble(array, i);
        }
    }

    private void checkDouble(final AnyArray array, final int index) {
        array.setDouble(index, 0.0);
        assertEquals(AnyType.Double, array.type(index));
        assertEquals(0.0, array.getDouble(index), 0.0);
        assertEquals(0.0, array.getDoubleUnsafe(index), 0.0);
        array.clear(index);
        array.setDouble(index, 1.0);
        assertEquals(AnyType.Double, array.type(index));
        assertEquals(1.0, array.getDouble(index), 0.0);
        assertEquals(1.0, array.getDoubleUnsafe(index), 0.0);
        standardChecks(array, index, AnyType.Double, JSONType.Number);
    }

    @Test
    public void testObject() {
        final AnyArray array = new AnyArray(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array.clear(j);
            }
            checkObject(array, i);
        }
    }

    private void checkObject(final AnyArray array, final int index) {
        array.setObject(index, Object.class);
        assertEquals(AnyType.Object, array.type(index));
        assertEquals(Object.class, array.getObject(index));
        assertEquals(Object.class, array.getObjectUnsafe(index));
        array.clear(index);
        array.setObject(index, "x");
        assertEquals(AnyType.String, array.type(index));
        assertEquals("x", array.getObject(index));
        assertEquals("x", array.getObjectUnsafe(index));
        standardChecks(array, index, AnyType.String, JSONType.String);

        // Class maps to String
        array.setObject(index, Integer.class);
        assertEquals(JSONType.String, array.jsonType(index));
        // CharacterSequence maps to String
        array.setObject(index, new StringBuilder());
        assertEquals(JSONType.String, array.jsonType(index));
        // Array maps to Array
        array.setObject(index, new int[0]);
        assertEquals(JSONType.Array, array.jsonType(index));
        // Iterable maps to Array
        array.setObject(index, Collections.emptySet());
        assertEquals(JSONType.Array, array.jsonType(index));
        // Iterator maps to Array
        array.setObject(index, Collections.emptySet().iterator());
        assertEquals(JSONType.Array, array.jsonType(index));
        // Enumeration maps to Array
        array.setObject(index, System.getProperties().elements());
        assertEquals(JSONType.Array, array.jsonType(index));
        // null maps to Null
        array.setObject(index, null);
        assertEquals(JSONType.Null, array.jsonType(index));
        // Unregistered Enum maps to String
        array.setObject(index, JSONType.Boolean);
        assertEquals(JSONType.String, array.jsonType(index));
        // Anything else maps to Object
        array.setObject(index, System.out);
        assertEquals(JSONType.Object, array.jsonType(index));
    }
}