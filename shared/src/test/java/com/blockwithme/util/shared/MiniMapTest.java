/**
 *
 */
package com.blockwithme.util.shared;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

/** Test class for MiniMap.
 *
 * @author monster
 *
 */
@SuppressWarnings("all")
public class MiniMapTest {
    private static final String ONE_KEY = "one";
    private static final Integer ONE_VALUE = 1;
    private static final String TWO_KEY = "two";
    private static final Integer TWO_VALUE = 2;
    private static final String THREE_KEY = "three";
    private static final Integer THREE_VALUE = 3;
    private static final String FOUR_KEY = "four";
    private static final Integer FOUR_VALUE = 4;
    private static final String FIVE_KEY = "five";
    private static final Integer FIVE_VALUE = 5;

    private static final String[] KEYS = new String[] { ONE_KEY, TWO_KEY,
            THREE_KEY, FOUR_KEY, FIVE_KEY };
    private static final Integer[] VALUES = new Integer[] { ONE_VALUE,
            TWO_VALUE, THREE_VALUE, FOUR_VALUE, FIVE_VALUE };

    @Test
    public void testEmpty() {
        final Object[] empty = new Object[0];
        assertEquals(0, MiniMap.getPropertiesCount(empty));
        assertEquals(Collections.emptyMap(),
                MiniMap.getProperties(empty));
        assertArrayEquals(empty, MiniMap.getPropertyKeyes(empty));
        assertArrayEquals(empty, MiniMap.getPropertyValues(empty));
        assertEquals(null, MiniMap.getProperty(empty, "empty"));
        assertEquals("empty",
                MiniMap.getProperty(empty, "empty", "empty"));
        assertArrayEquals(empty,
                MiniMap.removeProperty(empty, "missing"));
        final Object[] oneValue = MiniMap
                .setProperty(empty, "zero", 0);
        assertEquals(2, oneValue.length);
        assertEquals("zero", oneValue[0]);
        assertEquals(0, oneValue[1]);
    }

    @Test
    public void testNotEmpty() {
        Object[] properties = new Object[0];
        final Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < KEYS.length; i++) {
            properties = MiniMap.setProperty(properties, KEYS[i],
                    VALUES[i]);
            map.put(KEYS[i], VALUES[i]);
        }

        assertEquals(KEYS.length,
                MiniMap.getPropertiesCount(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    MiniMap.getProperty(properties, KEYS[i]));
        }

        for (int i = 0; i < KEYS.length; i++) {
            properties = MiniMap.removeProperty(properties, KEYS[i]);
        }
        assertEquals(0, MiniMap.getPropertiesCount(properties));

        properties = MiniMap.setProperties(properties, map);
        assertEquals(KEYS.length,
                MiniMap.getPropertiesCount(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    MiniMap.getProperty(properties, KEYS[i]));
        }

        assertEquals(map, MiniMap.getProperties(properties));

        assertEquals(
                new HashSet(Arrays.asList(KEYS)),
                new HashSet(Arrays.asList(MiniMap
                        .getPropertyKeyes(properties))));
        assertEquals(
                new HashSet(Arrays.asList(VALUES)),
                new HashSet(Arrays.asList(MiniMap
                        .getPropertyValues(properties))));
    }
}