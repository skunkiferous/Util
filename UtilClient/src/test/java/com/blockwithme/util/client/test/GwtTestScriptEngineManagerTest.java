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

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Tests javax.script.ScriptEngineManager emulation.
 *
 * @author monster
 */
public class GwtTestScriptEngineManagerTest extends BaseGWTTestCase {

    public void testStrings() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("global_A", "abc");
        final ScriptEngine engine = manager.getEngineByName("js");
        final Bindings bindings = engine.createBindings();
        bindings.put("engine_B", "def");
        try {
            final Object result = engine.eval("global_A + engine_B;", bindings);
            assertEquals("abcdef", result);
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void testNumbers() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("global_A", 3.0);
        final ScriptEngine engine = manager.getEngineByName("js");
        final Bindings bindings = engine.createBindings();
        bindings.put("engine_B", 6.0);
        try {
            final Object result = engine.eval("global_A + engine_B;", bindings);
            assertEquals(new Double(9.0), result);
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
