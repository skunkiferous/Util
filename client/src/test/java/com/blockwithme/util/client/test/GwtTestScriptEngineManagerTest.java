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

import com.blockwithme.util.client.SomeDataType;

/**
 * Tests javax.script.ScriptEngineManager emulation.
 *
 * @author monster
 */
public class GwtTestScriptEngineManagerTest extends BaseGWTTestCase {

    public void testStrings() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("va", "abc");
        final ScriptEngine engine = manager.getEngineByName("js");
        final Bindings bindings = engine.createBindings();
        bindings.put("vb", "def");
        try {
            final Object result = engine.eval("return va + vb;", bindings);
            assertEquals("abcdef", result);
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void testNumbers() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("vc", 3.0);
        final ScriptEngine engine = manager.getEngineByName("javascript");
        final Bindings bindings = engine.createBindings();
        bindings.put("vd", 6.0);
        try {
            final Object result = engine.eval("return vc + vd;", bindings);
            assertEquals(new Double(9.0), result);
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void testExport() {
        assertEquals("John", SomeDataType.create().name());
    }

    public void testCreateObjects() {
        SomeDataType.exportType();
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            final Object result = engine
                    .eval("var sdt = new $wnd.com.blockwithme.util.client.SomeDataType();\n" // NOP
                            + "sdt.age = sdt.name().length;\n" // NOP
                            + "return sdt;" // NOP
                    );
            assertTrue(result instanceof SomeDataType);
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void testObjects() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("javascript");
        final Bindings bindings = engine.createBindings();
        final SomeDataType data = SomeDataType.create();
        bindings.put("ve", data);
        try {
            final Object result = engine.eval(
                    "var result = ve.name()+ve.age.toString();" + "ve.age=-1;"
                            + "return result;", bindings);
            assertEquals("John42", result);
            assertEquals(-1, data.getAge());
        } catch (final javax.script.ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
