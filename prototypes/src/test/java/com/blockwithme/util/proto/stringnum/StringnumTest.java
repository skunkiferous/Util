/**
 *
 */
package com.blockwithme.util.proto.stringnum;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blockwithme.util.proto.stringnum.Stringnum;

/**
 * TODO Complete the tests.
 */
public class StringnumTest {
    @Test
    public void testStringnum() {
        final Stringnum sn = new Stringnum();
        assertEquals(1, sn.putString("a"));
        assertEquals(2, sn.putString("b"));
        assertEquals(3, sn.putString("c"));
        assertEquals(1, sn.putString("a"));
        final String b = sn.intern("b");
        assertEquals("b", b);
        assertEquals(2, b.hashCode());
        assertEquals(4, sn.size());
        final StringBuilder buf = new StringBuilder("monster");
        buf.append("magnet");
        final String str = buf.toString();
        final String hacked = sn.intern(str);
        assertEquals(4, hacked.hashCode());
        final String interned = hacked.intern();
        assertEquals(4, hacked.hashCode());
        assertEquals(interned, hacked);
    }
}