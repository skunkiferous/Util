/*
 * Copyright 2011-2013 David Karnok
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The simple implementation of the Bindings interface
 * by using a HashMap.
 * @author karnok, 2011.02.20.
 */
public class SimpleBindings extends HashMap<String, Object> implements Bindings {
	/** Constructs an empty SimpleBindings. */
	public SimpleBindings() {
	    // NOP
	}

	/**
	 * Constructs a SimpleBindings with the contents
	 * of the supplied map.
	 * @param m the map to add
	 */
	public SimpleBindings(Map<String, Object> m) {
	    putAll(m);
	}
}
