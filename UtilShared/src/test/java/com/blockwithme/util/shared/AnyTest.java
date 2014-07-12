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
 * Test class for Any.
 *
 * @author monster
 */
@SuppressWarnings("all")
public class AnyTest {
    private void standardChecks(final Any any, final AnyType type,
            final JSONType jsonType) {
        if (type == AnyType.Empty) {
            assertTrue(any.isEmpty());
        } else {
            assertFalse(any.isEmpty());
        }

        assertEquals(type, any.type());
        assertEquals(jsonType, any.jsonType());
        final Any clone = any.copy();
        assertEquals(type, clone.type());
        assertEquals(jsonType, clone.jsonType());
        assertEquals(any, clone);
        final Any copy = new Any();
        copy.copyFrom(any);
        assertEquals(type, copy.type());
        assertEquals(jsonType, copy.jsonType());
        assertEquals(any, copy);

        boolean failed = false;
        if (!type.object) {
            try {
                any.getObject();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Object", failed);
            failed = false;
        }
        if (type != AnyType.Boolean) {
            try {
                any.getBoolean();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Boolean", failed);
            failed = false;
        }
        if (type != AnyType.Byte) {
            try {
                any.getByte();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Byte", failed);
            failed = false;
        }
        if (type != AnyType.Char) {
            try {
                any.getChar();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Char", failed);
            failed = false;
        }
        if (type != AnyType.Short) {
            try {
                any.getShort();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Short", failed);
            failed = false;
        }
        if (type != AnyType.Int) {
            try {
                any.getInt();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Int", failed);
            failed = false;
        }
        if (type != AnyType.Float) {
            try {
                any.getFloat();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Float", failed);
            failed = false;
        }
        if (type != AnyType.Long) {
            try {
                any.getLong();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Long", failed);
            failed = false;
        }
        if (type != AnyType.Double) {
            try {
                any.getDouble();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertTrue("Double", failed);
        }

        failed = false;
        if (type != AnyType.Object) {
            try {
                any.getObjectUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ObjectUnsafe", failed);
        }
        if (type != AnyType.Boolean) {
            try {
                any.getBooleanUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("BooleanUnsafe", failed);
        }
        if (type != AnyType.Byte) {
            try {
                any.getByteUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ByteUnsafe", failed);
        }
        if (type != AnyType.Char) {
            try {
                any.getCharUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("CharUnsafe", failed);
        }
        if (type != AnyType.Short) {
            try {
                any.getShortUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("ShortUnsafe", failed);
        }
        if (type != AnyType.Int) {
            try {
                any.getIntUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("IntUnsafe", failed);
        }
        if (type != AnyType.Float) {
            try {
                any.getFloatUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("FloatUnsafe", failed);
        }
        // any.getLongUnsafe() WILL throw, if (type != AnyType.Long)
        if (type != AnyType.Double) {
            try {
                any.getDoubleUnsafe();
            } catch (final RuntimeException e) {
                failed = true;
            }
            assertFalse("DoubleUnsafe", failed);
        }

        any.clear();
        assertEquals(AnyType.Empty, any.type());
    }

    @Test
    public void testEmpty() {
        final Any any = new Any();
        standardChecks(any, AnyType.Empty, JSONType.Null);
    }

    @Test
    public void testObject() {
        final Any any = new Any(Object.class);
        assertEquals(AnyType.Object, any.type());
        assertEquals(Object.class, any.getObject());
        assertEquals(Object.class, any.getObjectUnsafe());
        any.clear();
        any.setObject("x");
        assertEquals(AnyType.String, any.type());
        assertEquals("x", any.getObject());
        assertEquals("x", any.getObjectUnsafe());
        standardChecks(any, AnyType.String, JSONType.String);

        // Class maps to String
        any.setObject(Integer.class);
        assertEquals(JSONType.String, any.jsonType());
        // CharacterSequence maps to String
        any.setObject(new StringBuilder());
        assertEquals(JSONType.String, any.jsonType());
        // Array maps to Array
        any.setObject(new int[0]);
        assertEquals(JSONType.Array, any.jsonType());
        // Iterable maps to Array
        any.setObject(Collections.emptySet());
        assertEquals(JSONType.Array, any.jsonType());
        // Iterator maps to Array
        any.setObject(Collections.emptySet().iterator());
        assertEquals(JSONType.Array, any.jsonType());
        // Enumeration maps to Array
        any.setObject(System.getProperties().elements());
        assertEquals(JSONType.Array, any.jsonType());
        // null maps to Null
        any.setObject(null);
        assertEquals(JSONType.Null, any.jsonType());
        // Unregistered Enum maps to String
        any.setObject(JSONType.Boolean);
        assertEquals(JSONType.String, any.jsonType());
        // Anything else maps to Object
        any.setObject(System.out);
        assertEquals(JSONType.Object, any.jsonType());
    }

    @Test
    public void testBoolean() {
        final Any any = new Any(true);
        assertEquals(AnyType.Boolean, any.type());
        assertTrue(any.getBoolean());
        assertTrue(any.getBooleanUnsafe());
        any.clear();
        any.setBoolean(false);
        assertEquals(AnyType.Boolean, any.type());
        assertFalse(any.getBoolean());
        assertFalse(any.getBooleanUnsafe());
        standardChecks(any, AnyType.Boolean, JSONType.Boolean);
    }

    @Test
    public void testByte() {
        final Any any = new Any((byte) 0);
        assertEquals(AnyType.Byte, any.type());
        assertEquals((byte) 0, any.getByte());
        assertEquals((byte) 0, any.getByteUnsafe());
        any.clear();
        any.setByte((byte) 1);
        assertEquals(AnyType.Byte, any.type());
        assertEquals((byte) 1, any.getByte());
        assertEquals((byte) 1, any.getByteUnsafe());
        standardChecks(any, AnyType.Byte, JSONType.Number);
    }

    @Test
    public void testChar() {
        final Any any = new Any((char) 0);
        assertEquals(AnyType.Char, any.type());
        assertEquals((char) 0, any.getChar());
        assertEquals((char) 0, any.getCharUnsafe());
        any.clear();
        any.setChar((char) 1);
        assertEquals(AnyType.Char, any.type());
        assertEquals((char) 1, any.getChar());
        assertEquals((char) 1, any.getCharUnsafe());
        standardChecks(any, AnyType.Char, JSONType.Number);
    }

    @Test
    public void testShort() {
        final Any any = new Any((short) 0);
        assertEquals(AnyType.Short, any.type());
        assertEquals((short) 0, any.getShort());
        assertEquals((short) 0, any.getShortUnsafe());
        any.clear();
        any.setShort((short) 1);
        assertEquals(AnyType.Short, any.type());
        assertEquals((short) 1, any.getShort());
        assertEquals((short) 1, any.getShortUnsafe());
        standardChecks(any, AnyType.Short, JSONType.Number);
    }

    @Test
    public void testInt() {
        final Any any = new Any(0);
        assertEquals(AnyType.Int, any.type());
        assertEquals(0, any.getInt());
        assertEquals(0, any.getIntUnsafe());
        any.clear();
        any.setInt(1);
        assertEquals(AnyType.Int, any.type());
        assertEquals(1, any.getInt());
        assertEquals(1, any.getIntUnsafe());
        standardChecks(any, AnyType.Int, JSONType.Number);
    }

    @Test
    public void testFloat() {
        final Any any = new Any(0f);
        assertEquals(AnyType.Float, any.type());
        assertEquals(0f, any.getFloat(), 0f);
        assertEquals(0f, any.getFloatUnsafe(), 0f);
        any.clear();
        any.setFloat(1f);
        assertEquals(AnyType.Float, any.type());
        assertEquals(1f, any.getFloat(), 0f);
        assertEquals(1f, any.getFloatUnsafe(), 0f);
        standardChecks(any, AnyType.Float, JSONType.Number);
    }

    @Test
    public void testLong() {
        final Any any = new Any(0l);
        assertEquals(AnyType.Long, any.type());
        assertEquals(0l, any.getLong());
        assertEquals(0l, any.getLongUnsafe());
        any.clear();
        any.setLong(1l);
        assertEquals(AnyType.Long, any.type());
        assertEquals(1l, any.getLong());
        assertEquals(1l, any.getLongUnsafe());
        standardChecks(any, AnyType.Long, JSONType.Number);

        any.setLong(Long.MAX_VALUE);
        assertEquals(JSONType.String, any.jsonType());
        assertEquals(JSONType.String, any.copy().jsonType());
        final Any copy = new Any();
        copy.copyFrom(any);
        assertEquals(JSONType.String, copy.jsonType());
        any.setLong(Long.MIN_VALUE);
        assertEquals(JSONType.String, any.jsonType());
        assertEquals(JSONType.String, any.copy().jsonType());
        assertEquals(JSONType.String, copy.jsonType());
    }

    @Test
    public void testDouble() {
        final Any any = new Any(0.0);
        assertEquals(AnyType.Double, any.type());
        assertEquals(0.0, any.getDouble(), 0.0);
        assertEquals(0.0, any.getDoubleUnsafe(), 0.0);
        any.clear();
        any.setDouble(1.0);
        assertEquals(AnyType.Double, any.type());
        assertEquals(1.0, any.getDouble(), 0.0);
        assertEquals(1.0, any.getDoubleUnsafe(), 0.0);
        standardChecks(any, AnyType.Double, JSONType.Number);
    }

    @Test
    public void testType() {
        assertEquals(AnyType.String, Any.type("x"));
        assertEquals(AnyType.BooleanArray, Any.type(new boolean[0]));
        assertEquals(AnyType.ByteArray, Any.type(new byte[0]));
        assertEquals(AnyType.CharArray, Any.type(new char[0]));
        assertEquals(AnyType.ShortArray, Any.type(new short[0]));
        assertEquals(AnyType.IntArray, Any.type(new int[0]));
        assertEquals(AnyType.FloatArray, Any.type(new float[0]));
        assertEquals(AnyType.LongArray, Any.type(new long[0]));
        assertEquals(AnyType.DoubleArray, Any.type(new double[0]));
        assertEquals(AnyType.StringArray, Any.type(new String[0]));
        assertEquals(AnyType.ObjectArray, Any.type(new Integer[0]));
    }
}