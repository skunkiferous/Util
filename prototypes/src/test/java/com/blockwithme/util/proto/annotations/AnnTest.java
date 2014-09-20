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
package com.blockwithme.util.proto.annotations;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.blockwithme.util.proto.KVMapHelper;
import com.blockwithme.util.shared.annotations.E;
import com.blockwithme.util.shared.annotations.KVMap;

/**
 * @author monster
 *
 */
@KVMap({ @E({ "abc", "123" }), @E({ "def", "4*5" }) })
public class AnnTest {
    @Test
    public void testIt() {
        final Map<String, Object[]> map = KVMapHelper
                .getAsGroovyMap(getClass());
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Assert.assertTrue(map.containsKey("abc"));
        Assert.assertTrue(map.containsKey("def"));
        final Object[] abc = map.get("abc");
        final Object[] def = map.get("def");
        Assert.assertEquals(1, abc.length);
        Assert.assertEquals(1, def.length);
        Assert.assertEquals(123, abc[0]);
        Assert.assertEquals(20, def[0]);
    }
}
