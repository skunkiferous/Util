/**
 *
 */
package com.blockwithme.util.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for Any.
 *
 * @author monster
 */
@SuppressWarnings("all")
public class AnyTest {
    private void standardChecks(final Any any, final AnyType type) {
        if (type == AnyType.Empty) {
            assertTrue(any.isEmpty());
        } else {
            assertFalse(any.isEmpty());
        }

        assertEquals(type, any.type());
        final Any clone = any.clone();
        assertEquals(type, clone.type());
        assertEquals(any, clone);
        final Any copy = new Any(any);
        assertEquals(type, copy.type());
        assertEquals(any, copy);

        boolean failed = false;
        if (type != AnyType.Object) {
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
        standardChecks(any, AnyType.Empty);
    }

    @Test
    public void testObject() {
        final Any any = new Any("");
        assertEquals(AnyType.Object, any.type());
        assertEquals("", any.getObject());
        assertEquals("", any.getObjectUnsafe());
        any.clear();
        any.setObject("x");
        assertEquals(AnyType.Object, any.type());
        assertEquals("x", any.getObject());
        assertEquals("x", any.getObjectUnsafe());
        standardChecks(any, AnyType.Object);
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
        standardChecks(any, AnyType.Boolean);
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
        standardChecks(any, AnyType.Byte);
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
        standardChecks(any, AnyType.Char);
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
        standardChecks(any, AnyType.Short);
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
        standardChecks(any, AnyType.Int);
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
        standardChecks(any, AnyType.Float);
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
        standardChecks(any, AnyType.Long);
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
        standardChecks(any, AnyType.Double);
    }
}