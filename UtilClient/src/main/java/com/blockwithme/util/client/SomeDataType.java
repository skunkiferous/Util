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
package com.blockwithme.util.client;

import com.google.gwt.core.client.JavaScriptObject;

public class SomeDataType extends JavaScriptObject {
    /** Are we export yet? */
    private static boolean typeExported;

    /** "Registers" (export) this type in JavaScript, so it can be used from there too. */
    private static final native void doExportType() /*-{
		if (!$wnd.com) {
			$wnd.com = {};
		}

		var com = $wnd.com
		if (!com.blockwithme) {
			com.blockwithme = {};
		}
		if (!com.blockwithme.util) {
			com.blockwithme.util = {};
		}
		if (!com.blockwithme.util.client) {
			com.blockwithme.util.client = {};
		}

		function SomeDataType() {
			this.age = 42;
		}
		SomeDataType.prototype.name = function() {
			return "John";
		}
		SomeDataType.prototype.setAge = function(newAge) {
			this.age = newAge;
		}
		SomeDataType.prototype.getAge = function() {
			return this.age;
		}

		com.blockwithme.util.client.SomeDataType = SomeDataType;

		console.log(com);
    }-*/;

    /** "Registers" (export) this type in JavaScript, so it can be used from there too. */
    public static void exportType() {
        if (!typeExported) {
            typeExported = true;
            doExportType();
        }
    }

    public static SomeDataType create() {
        exportType();
        return (SomeDataType) doCreate().cast();
    }

    /** Does the instance initialization. */
    private static final native JavaScriptObject doCreate() /*-{
		return new $wnd.com.blockwithme.util.client.SomeDataType();
    }-*/;

    /** Constructor; must always be protected without parameter for overlay types. */
    protected SomeDataType() {
        // NOP
    }

    public final native String name() /*-{
		return this.name();
    }-*/;

    public final native int getAge() /*-{
		return this.getAge();
    }-*/;

    public final native void setAge(int newAge) /*-{
		this.setAge(newAge);
    }-*/;

}