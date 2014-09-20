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
package javax.script;

/**
 * @author monster
 *
 */
public interface ScriptContext {

    /**
     * EngineScope attributes are visible during the lifetime of a single
     * <code>ScriptEngine</code> and a set of attributes is maintained for each
     * engine.
     */
    public static final int ENGINE_SCOPE = 100;

    /**
     * GlobalScope attributes are visible to all engines created by same ScriptEngineFactory.
     */
    public static final int GLOBAL_SCOPE = 200;

}
