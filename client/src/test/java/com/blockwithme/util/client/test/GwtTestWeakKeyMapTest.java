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

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.base.WeakKeyMap;

/**
 * Tests WeakKeyMap implementation.
 *
 * @author monster
 */
public class GwtTestWeakKeyMapTest extends BaseGWTTestCase {
    public void test() {
        final Date key = new Date();
        final WeakKeyMap<Date, String> map = SystemUtils.newWeakKeyMap();
        assertFalse("map.containsKey(key)", map.containsKey(key));
        assertEquals("map.put(object, 'xxx')", null, map.put(key, "xxx"));
        assertTrue("map.containsKey(key)", map.containsKey(key));
        assertEquals("map.get(object)", "xxx", map.get(key));
        assertEquals("map.put(object, 'yyy')", "xxx", map.put(key, "yyy"));
        assertEquals("map.put(object, 'yyy')", "yyy", map.remove(key));
        assertFalse("map.containsKey(key)", map.containsKey(key));

        assertEquals("map.put(object, 'xxx')", null, map.put(key, "xxx"));
        assertTrue("map.containsKey(key)", map.containsKey(key));
        map.clear();
        assertFalse("map.containsKey(key)", map.containsKey(key));

        final Map<Date, String> input = Collections.singletonMap(key, "xxx");
        map.putAll(input);
        assertTrue("map.containsKey(key)", map.containsKey(key));
        assertEquals("map.get(object)", "xxx", map.get(key));
    }
}
