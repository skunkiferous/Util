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

/** Test class for PropertiesHelper.
 *
 * @author monster
 *
 */
@SuppressWarnings("all")
public class PropertiesHelperTest {
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
        assertEquals(0, PropertiesHelper.getPropertiesCount(empty));
        assertEquals(Collections.emptyMap(),
                PropertiesHelper.getProperties(empty));
        assertArrayEquals(empty, PropertiesHelper.getPropertyKeyes(empty));
        assertArrayEquals(empty, PropertiesHelper.getPropertyValues(empty));
        assertEquals(null, PropertiesHelper.getProperty(empty, "empty"));
        assertEquals("empty",
                PropertiesHelper.getProperty(empty, "empty", "empty"));
        assertArrayEquals(empty,
                PropertiesHelper.removeProperty(empty, "missing"));
        final Object[] oneValue = PropertiesHelper
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
            properties = PropertiesHelper.setProperty(properties, KEYS[i],
                    VALUES[i]);
            map.put(KEYS[i], VALUES[i]);
        }

        assertEquals(KEYS.length,
                PropertiesHelper.getPropertiesCount(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    PropertiesHelper.getProperty(properties, KEYS[i]));
        }

        for (int i = 0; i < KEYS.length; i++) {
            properties = PropertiesHelper.removeProperty(properties, KEYS[i]);
        }
        assertEquals(0, PropertiesHelper.getPropertiesCount(properties));

        properties = PropertiesHelper.setProperties(properties, map);
        assertEquals(KEYS.length,
                PropertiesHelper.getPropertiesCount(properties));
        for (int i = 0; i < KEYS.length; i++) {
            assertEquals(VALUES[i],
                    PropertiesHelper.getProperty(properties, KEYS[i]));
        }

        assertEquals(map, PropertiesHelper.getProperties(properties));

        assertEquals(
                new HashSet(Arrays.asList(KEYS)),
                new HashSet(Arrays.asList(PropertiesHelper
                        .getPropertyKeyes(properties))));
        assertEquals(
                new HashSet(Arrays.asList(VALUES)),
                new HashSet(Arrays.asList(PropertiesHelper
                        .getPropertyValues(properties))));
    }
}