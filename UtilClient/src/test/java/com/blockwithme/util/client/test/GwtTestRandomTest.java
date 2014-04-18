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
package com.blockwithme.util.client.test;

import java.util.Arrays;
import java.util.Random;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests java.util.Random emulation.
 *
 * @author monster
 */
public class GwtTestRandomTest extends GWTTestCase {
    private static final long SEED = -7795286066369484091L;
    private static final byte[] BYTES = new byte[] { -57, -58, -125, -87, 79,
            37, 90, 76 };

    /* (non-Javadoc)
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "com.blockwithme.util.UtilClient";
    }

    public void testRandom() {
        final Random rnd = new Random(SEED);
        assertEquals("rnd.nextInt(1234567)", 78409, rnd.nextInt(1234567));
        assertEquals("rnd.nextInt()", 1034033965, rnd.nextInt());
        assertEquals("rnd.nextLong()", 5029067226702039502L, rnd.nextLong());
        assertEquals("rnd.nextDouble()", 0.37137361926407, rnd.nextDouble(),
                0.0000000001);
        assertEquals("rnd.nextFloat()", 0.8633307f, rnd.nextFloat(), 0.000001f);
        assertEquals("rnd.nextGaussian()", 0.6217829577875392,
                rnd.nextGaussian(), 0.0000000001);
        assertEquals("rnd.nextBoolean()", true, rnd.nextBoolean());
        final byte[] bytes = new byte[8];
        rnd.nextBytes(bytes);
        assertTrue("Arrays.equals(BYTES, bytes)", Arrays.equals(BYTES, bytes));
    }
}
