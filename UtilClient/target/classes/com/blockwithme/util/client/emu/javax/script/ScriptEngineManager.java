/*
 * Copyright 2011 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.script;

import java.util.Map;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;

/**
 * The script engine interface for GWT.
 * @author karnok, 2011.02.20.
 *
 * Greatly expanded by Sebastien Diot.
 */
public class ScriptEngineManager {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(ScriptEngineManager.class.getName());

    /** Empty bindings. */
    private static final Bindings EMPTY = new SimpleBindings();

    private class JSScriptEngine implements ScriptEngine {

        /** Global bindings. */
        private Bindings globalBindings;

        /** Engine bindings. */
        private Bindings engineBindings;

        public Bindings createBindings() {
            return new SimpleBindings();
        }

        public void setBindings(Bindings bindings, int scope) {
            if (scope == ScriptContext.ENGINE_SCOPE) {
                engineBindings = bindings;
            } else if (scope == ScriptContext.GLOBAL_SCOPE) {
                globalBindings = bindings;
            } else {
                throw new IllegalArgumentException("scope=" + scope);
            }
        }

        public Bindings getBindings(int scope) {
            if (scope == ScriptContext.ENGINE_SCOPE) {
                if (engineBindings == null) {
                    engineBindings = createBindings();
                }
                return engineBindings;
            } else if (scope == ScriptContext.GLOBAL_SCOPE) {
                if (globalBindings == null) {
                    globalBindings = createBindings();
                }
                return globalBindings;
            } else {
                throw new IllegalArgumentException("scope=" + scope);
            }
        }

        @Override
        public Object get(String key) {
            if (engineBindings == null) {
                return null;
            }
            return engineBindings.get(key);
        }

        @Override
        public void put(String key, Object value) {
            getBindings(ScriptContext.ENGINE_SCOPE).put(key, value);
        }

        @Override
        public Object eval(String script) throws ScriptException {
            return eval(script, null);
        }

        @Override
        public Object eval(String script, Bindings bindings)
                throws ScriptException {
            try {
                return invoke(script, getEffectiveBindings(bindings));
            } catch (Throwable t) {
                throw new ScriptException(t);
            }
        }

        private boolean empty(Bindings bindings) {
            return ((bindings == null) || bindings.isEmpty());
        }

        private Bindings getEffectiveBindings(Bindings bindings) {
            if (empty(bindings)) {
                if (empty(engineBindings)) {
                    return empty(globalBindings) ? EMPTY : globalBindings;
                } else if (empty(globalBindings)) {
                    return engineBindings;
                } else {
                    Bindings result = new SimpleBindings(globalBindings);
                    result.putAll(engineBindings);
                    return result;
                }
            } else {
                if (empty(engineBindings)) {
                    if (empty(globalBindings)) {
                        return bindings;
                    }
                    Bindings result = new SimpleBindings(globalBindings);
                    result.putAll(bindings);
                    return result;
                } else if (empty(globalBindings)) {
                    Bindings result = new SimpleBindings(engineBindings);
                    result.putAll(bindings);
                    return result;
                } else {
                    Bindings result = new SimpleBindings(globalBindings);
                    result.putAll(engineBindings);
                    result.putAll(bindings);
                    return result;
                }
            }
        }

        /**
         * Prepare the bindings for the given sequence.
         * @param index the sequence number
         */
        native void prepareOnWindow(int index)
        /*-{
			$wnd["blockwithme_util_bindings_" + index] = new Array();
			// This makes sure I can use "$wnd" everywhere. In fact,
			// this is the only way to get everything to work cleanly.
			$wnd["$wnd"] = $wnd;
        }-*/;

        /**
         * Set a binding variable name and value on the given sequence.
         * @param index the sequence number
         * @param name the variable name
         * @param value the value
         */
        native void setOnWindow(int index, String name, boolean value)
        /*-{
			$wnd["blockwithme_util_bindings_" + index][name] = value;
        }-*/;

        /**
         * Set a binding variable name and value on the given sequence.
         * @param index the sequence number
         * @param name the variable name
         * @param value the value
         */
        native void setOnWindow(int index, String name, double value)
        /*-{
			$wnd["blockwithme_util_bindings_" + index][name] = value;
        }-*/;

        /**
         * Set a binding variable name and value on the given sequence.
         * @param index the sequence number
         * @param name the variable name
         * @param value the value
         */
        native void setOnWindow(int index, String name, String value)
        /*-{
			$wnd["blockwithme_util_bindings_" + index][name] = value;
        }-*/;

        /**
         * Set a binding variable name and value on the given sequence.
         * @param index the sequence number
         * @param name the variable name
         * @param value the value
         */
        native void setUnknownOnWindow(int index, String name, Object value)
        /*-{
			$wnd["blockwithme_util_bindings_" + index][name] = value;
        }-*/;

        /**
         * Clear the binding variables on the given sequence number.
         * @param index the sequence number
         */
        native void clearOnWindow(int index)
        /*-{
			$wnd["blockwithme_util_bindings_" + index] = null;
			$wnd["$wnd"] = null;
        }-*/;

        /**
         * Invoke a Javascript script.
         * <p>The method auto-boxes {@code boolean} into {@code java.lang.Boolean}
         *  and {@code number} into {@code java.lang.Double} objects.
         * @param script the script to execute
         * @return the return value
         */
        native Object invoke(String script)
        /*-{
			var result = $wnd.eval(script);
			if (typeof (result) == "boolean") {
				return result ? @java.lang.Boolean::TRUE
						: @java.lang.Boolean::FALSE;
			} else if (typeof (result) == "number") {
				return @java.lang.Double::valueOf(D)(result);
			}
			return result;
        }-*/;

        /**
         * Set a binding variable name and value on the given sequence.
         * @param index the sequence number
         * @param name the variable name
         * @param value the value
         */
        private void setOnWindow(int index, String name, Object value) {
            if (value != null) {
                if (value instanceof Boolean) {
                    setOnWindow(index, name, ((Boolean) value).booleanValue());
                } else if (value instanceof Number) {
                    setOnWindow(index, name, ((Number) value).doubleValue());
                } else if ((value instanceof CharSequence) || (value instanceof Character)) {
                    setOnWindow(index, name, value.toString());
                } else {
                    setUnknownOnWindow(index, name, value);
                }
            }
        }

        /**
         * Invoke the script with the given mappings of variables.
         * @param script the script to invoke
         * @param bindings the variable bindings
         * @return the returned value
         */
        private Object invoke(String script, Bindings bindings) {
            int seq = bindSequence++;
            try {
                StringBuilder script2 = new StringBuilder();
                prepareOnWindow(seq);
                final String key = "blockwithme_util_bindings_"+seq;
                script2.append("var $tmp = $wnd[\"").append(key).append("\"];\n");
                script2.append("$tmp[\"").append(key)
                    .append("\"] = function() {\n");
                script2.append("$tmp[\"").append(key)
                .append("\"] = undefined;\n");
                for (Map.Entry<String, Object> e : bindings.entrySet()) {
                    final Object value = e.getValue();
                    setOnWindow(seq, e.getKey(), value);
                    script2.append("var ").append(e.getKey()).append(" = ")
                            .append("$tmp[\"").append(e.getKey())
                            .append("\"];\n");
                }
                script2.append("\n").append(script).append("\n}\n");
                script2.append("$tmp[\"").append(key)
                .append("\"]();\n");
                LOG.info("RUNNING SCRIPT: "+script2);
                return invoke(script2.toString());
            } finally {
                clearOnWindow(seq);
            }
        }
    }

    /** Global bindings associated with script engines created by this manager. */
    private Bindings globalScope = new SimpleBindings();

    /**
     * <code>setBindings</code> stores the specified <code>Bindings</code>
     * in the <code>globalScope</code> field. ScriptEngineManager sets this
     * <code>Bindings</code> as global bindings for <code>ScriptEngine</code>
     * objects created by it.
     *
     * @param bindings The specified <code>Bindings</code>
     * @throws IllegalArgumentException if bindings is null.
     */
    public void setBindings(Bindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("Global scope cannot be null.");
        }

        globalScope = bindings;
    }

    /**
     * <code>getBindings</code> returns the value of the <code>globalScope</code> field.
     * ScriptEngineManager sets this <code>Bindings</code> as global bindings for
     * <code>ScriptEngine</code> objects created by it.
     *
     * @return The globalScope field.
     */
    public Bindings getBindings() {
        return globalScope;
    }

    /**
     * Sets the specified key/value pair in the Global Scope.
     * @param key Key to set
     * @param value Value to set.
     * @throws NullPointerException if key is null.
     * @throws IllegalArgumentException if key is empty string.
     */
    public void put(String key, Object value) {
        globalScope.put(key, value);
    }

    /**
     * Gets the value for the specified key in the Global Scope
     * @param key The key whose value is to be returned.
     * @return The value for the specified key.
     */
    public Object get(String key) {
        return globalScope.get(key);
    }

    /**
     * Returns a script engine for the given name.
     * If the engine is not supported, this returns null.
     * @param name the engine name
     * @return the script engine or null
     */
    public ScriptEngine getEngineByName(String name) {
        if (name.equalsIgnoreCase("js") || name.equalsIgnoreCase("javascript")) {
            final ScriptEngine result = new JSScriptEngine();
            if (!globalScope.isEmpty()) {
                result.getBindings(ScriptContext.GLOBAL_SCOPE).putAll(
                        globalScope);
            }
            return result;
        }
        return null;
    }

    /** The current bind sequence. */
    static int bindSequence;
}
