/**
 *
 */
package com.blockwithme.util.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for DoubleMiniMap.
 *
 * @author monster
 */
@SuppressWarnings("all")
public class DoubleMiniMapTest {
    private static final double ONE_KEY = 0.1;
    private static final double ONE_VALUE = 1;
    private static final double TWO_KEY = 0.2;
    private static final double TWO_VALUE = 2;
    private static final double THREE_KEY = 0.3;
    private static final double THREE_VALUE = 3;
    private static final double FOUR_KEY = 0.4;
    private static final double FOUR_VALUE = 4;
    private static final double FIVE_KEY = 0.5;
    private static final double FIVE_VALUE = 5;

    private static final double[] KEYS = new double[] { ONE_KEY, TWO_KEY,
            THREE_KEY, FOUR_KEY, FIVE_KEY };
    private static final double[] VALUES = new double[] { ONE_VALUE, TWO_VALUE,
            THREE_VALUE, FOUR_VALUE, FIVE_VALUE };

    private static void assertArrayEquals(final double[] expected,
            final double[] actual) {
        if (expected == null) {
            assertEquals(null, actual);
        } else {
            assertNotNull(actual);
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], actual[i], 0);
            }
        }
    }

    @Test
    public void testEmpty() {
        final double[] empty = new double[0];
        assertEquals(0, DoubleMiniMap.getSize(empty));
        assertEquals(Collections.emptyMap(), DoubleMiniMap.getAsMap(empty));
        assertArrayEquals(empty, DoubleMiniMap.getKeyes(empty));
        assertArrayEquals(empty, DoubleMiniMap.getValues(empty));
        assertEquals(0, DoubleMiniMap.getValue(empty, 42), 0);
        assertEquals(84, DoubleMiniMap.getValue(empty, 42, 84), 0);
        assertArrayEquals(empty, DoubleMiniMap.removeValue(empty, 42));
        final double[] oneValue = DoubleMiniMap.setValue(empty, 42, 84);
        assertEquals(2, oneValue.length);
        assertEquals(42, oneValue[0], 0);
        assertEquals(84, oneValue[1], 0);
    }

    @Test
    public void testNotEmpty() {
        double[] properties = new double[0];
        final Map<Double, Double> map = new HashMap<Double, Double>();
        for (int i = 0; i < KEYS.length; i++) {
            properties = DoubleMiniMap.setValue(properties, KEYS[i], VALUES[i]);
            map.put(KEYS[i], VALUES[i]);
        }

        assertEquals(KEYS.length, DoubleMiniMap.getSize(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    DoubleMiniMap.getValue(properties, KEYS[i]), 0);
        }

        for (int i = 0; i < KEYS.length; i++) {
            properties = DoubleMiniMap.removeValue(properties, KEYS[i]);
        }
        assertEquals(0, DoubleMiniMap.getSize(properties));

        properties = DoubleMiniMap.setValues(properties, map);
        assertEquals(KEYS.length, DoubleMiniMap.getSize(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    DoubleMiniMap.getValue(properties, KEYS[i]), 0);
        }

        assertEquals(map, DoubleMiniMap.getAsMap(properties));

        final double[] keys = KEYS.clone();
        Arrays.sort(keys);
        final double[] actualKeys = DoubleMiniMap.getKeyes(properties);
        Arrays.sort(actualKeys);
        assertArrayEquals(keys, actualKeys);
        final double[] values = VALUES.clone();
        Arrays.sort(values);
        final double[] actualValues = DoubleMiniMap.getValues(properties);
        Arrays.sort(actualValues);
        assertArrayEquals(values, actualValues);
    }
}