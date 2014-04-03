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

package com.blockwithme.util.base40;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import com.blockwithme.util.server.base40.Base40;
import com.blockwithme.util.server.base40.CharacterSet;
import com.blockwithme.util.server.base40.Enum40;

/**
 * Simple tests for Base40
 *
 *  TODO: Turn into jUnit Test!
 *
 * @author monster
 */
public class Base40Test {

    /** 64 Zeroes */
    private static final String BINARY_ZEROS = "0000000000000000000000000000000000000000000000000000000000000000";

    private static final CharacterSet CHAR_SET = Base40
            .getDefaultCharacterSet();

    /** The character set */
    private static final char[] CHARACTERS = CHAR_SET.getCharacterSet();

    /** The base-40 radix as a BigInteger */
    private static final BigInteger BI_RADIX = new BigInteger(
            String.valueOf(CharacterSet.RADIX));

    /** Returns the base-40 char[] representation of the value, treated as an
     * unsigned long. If fixedSize is true, it will be MAX_LEN character long. */
    private static char[] toCharArray2(final long value, final boolean fixedSize) {
        BigInteger n = Base40.toUnsigned(value);
        final char[] chars = new char[CharacterSet.MAX_LEN];
        int i = CharacterSet.MAX_LEN - 1;
        while (n.signum() != 0) {
            final BigInteger index = n.mod(BI_RADIX);
            chars[i--] = CHARACTERS[index.intValue()];
            n = n.divide(BI_RADIX);
        }
        int len = CharacterSet.MAX_LEN - i - 1;
        if (len == 0) {
            len = 1;
        }
        while (i >= 0) {
            chars[i--] = '0';
        }
        if (fixedSize || (CharacterSet.MAX_LEN == len)) {
            return chars;
        }
        return Arrays.copyOfRange(chars, CharacterSet.MAX_LEN - len,
                CharacterSet.MAX_LEN);
    }

    private static String toDebugString(final char[] chars) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            buf.append(c).append('(').append(CHAR_SET.indexOf(c)).append(')');
        }
        return buf.toString();
    }

    /** Returns the base-40 String representation of the value, treated as an
     * unsigned long. If fixedSize is true, it will be MAX_LEN character long. */
    private static String toString2(final long value, final boolean fixedSize) {
        return new String(toCharArray2(value, fixedSize));
    }

    /** Just for tests. */
    @SuppressWarnings("unused")
    private static void print(final long value) {
        final Base40 b40 = new Base40(CHAR_SET, value);
        System.out.println(value + " " + b40 + " " + b40.fixedName() + " "
                + new Base40(CHAR_SET, b40.toString()).asLong());
    }

    private static void printloop(final long l) {
        final BigInteger n = Base40.toUnsigned(l);
        String lb = Long.toBinaryString(l);
        int missing = 64 - lb.length();
        if (missing > 0) {
            lb = BINARY_ZEROS.substring(0, missing) + lb;
        }
        final char[] longChars = CHAR_SET.toCharArray(l, true, false);
        System.out.println(lb + ":");
        System.out.println("L: " + String.valueOf(l) + " ==> "
                + new String(longChars) + " ==> " + toDebugString(longChars));
        String bb = n.toString(2);
        missing = 64 - bb.length();
        if (missing > 0) {
            bb = BINARY_ZEROS.substring(0, missing) + bb;
        }
        final char[] biChars = toCharArray2(l, true);
        final int lastL = CHAR_SET.indexOf(longChars[CharacterSet.MAX_LEN - 1]);
        final int lastBI = CHAR_SET.indexOf(biChars[CharacterSet.MAX_LEN - 1]);
        System.out.println("B: " + n + " ==> " + new String(biChars) + " ==> "
                + toDebugString(biChars) + " DIFF: " + (lastBI - lastL));
        System.out.println();
    }

    /** For Enum40  testing */
    public static class TestEnum40 extends Enum40<TestEnum40> {
        /**  */
        private static final long serialVersionUID = 1L;
        public static final TestEnum40 Two = new TestEnum40();
        public static final TestEnum40 One = new TestEnum40();

        private TestEnum40() {
            this(TestEnum40.class);
        }

        protected TestEnum40(final Class<? extends TestEnum40> type) {
            super(CHAR_SET, type);
        }
    }

    /** For Enum40  testing */
    public static class TestEnum40Child extends TestEnum40 {
        /**  */
        private static final long serialVersionUID = 1L;
        // Test that extension also works.
        public static final TestEnum40Child Three = new TestEnum40Child();
        public static final TestEnum40Child Four = new TestEnum40Child();

        private TestEnum40Child() {
            super(TestEnum40Child.class);
        }
    }

    /** For Enum40  testing */
    public static class TestEnum40GrandChild extends TestEnum40 {
        /**  */
        private static final long serialVersionUID = 1L;
        // Test that extension of extension also works.
        public static final TestEnum40GrandChild Five = new TestEnum40GrandChild();
        public static final TestEnum40GrandChild Six = new TestEnum40GrandChild();

        private TestEnum40GrandChild() {
            super(TestEnum40GrandChild.class);
        }
    }

    /** Just for tests. */
    public static void main(final String[] args) {
        for (final long l : new long[] { Long.MIN_VALUE, -100, -1, 0, 1, 100,
                Long.MAX_VALUE }) {
            printloop(l);
        }

        final Random rnd = new Random();
        final int loops = 1000000;
        int bad = 0;
        for (int i = 0; i < loops; i++) {
            final long test = rnd.nextLong();
            final String lstr = CHAR_SET.toString(test, true, false);
            final String bistr = toString2(test, true);

            final Base40 a = new Base40(CHAR_SET, test);
            final Base40 b = new Base40(CHAR_SET, a.toString());
            if ((b.asLong() != test) || !lstr.equals(bistr)) {
                bad++;
                printloop(test);
            }
        }
        System.out.println(bad + "/" + loops + " bad results");

        // System.out.println("-10 % 8 = " + ((-10) % 8));
        // System.out.println("(1L << 62)=" + Long.toBinaryString((1L << 62)));
        // for (int i = 0; i < 256; i++) {
        // System.out.println("i=" + i + " b=" + ((byte) i) + " binary="
        // + Integer.toBinaryString(i));
        // }
        // print(0);
        // print(1);
        // print(40);
        // for (int i = 2; i < 20; i++) {
        // print(i * i);
        // }
        // print(-1);
        // print(Long.MAX_VALUE);
        // print(Long.MIN_VALUE);
        //
        // final Random rnd = new Random();
        // for (int i = 0; i < 100000; i++) {
        // final long test = rnd.nextLong();
        // final Base40 a = get(test);
        // final Base40 b = get(a.toString());
        // if (b.value != test) {
        // throw new IllegalStateException("" + test);
        // }
        // }

        final String one = TestEnum40.One.toString();
        if (!"One".equals(one) || (TestEnum40.One.ordinal() != 2)) {
            throw new IllegalStateException("one=" + one + " ordinal: "
                    + TestEnum40.One.ordinal());
        }
        final String two = TestEnum40.Two.toString();
        if (!"Two".equals(two) || (TestEnum40.Two.ordinal() != 1)) {
            throw new IllegalStateException("two=" + two + " ordinal: "
                    + TestEnum40.Two.ordinal());
        }
        final String three = TestEnum40Child.Three.toString();
        if (!"Three".equals(three) || (TestEnum40Child.Three.ordinal() != 65)) {
            throw new IllegalStateException("Three=" + three + " ordinal: "
                    + (int) TestEnum40Child.Three.ordinal());
        }
        final String four = TestEnum40Child.Four.toString();
        if (!"Four".equals(four) || (TestEnum40Child.Four.ordinal() != 66)) {
            throw new IllegalStateException("Four=" + four + " ordinal: "
                    + (int) TestEnum40Child.Four.ordinal());
        }
        final String five = TestEnum40GrandChild.Five.toString();
        if (!"Five".equals(five)
                || (TestEnum40GrandChild.Five.ordinal() != 129)) {
            throw new IllegalStateException("Five=" + five + " ordinal: "
                    + (int) TestEnum40GrandChild.Five.ordinal());
        }
        final String six = TestEnum40GrandChild.Six.toString();
        if (!"Six".equals(six) || (TestEnum40GrandChild.Six.ordinal() != 130)) {
            throw new IllegalStateException("Six=" + six + " ordinal: "
                    + (int) TestEnum40GrandChild.Six.ordinal());
        }
    }

}
