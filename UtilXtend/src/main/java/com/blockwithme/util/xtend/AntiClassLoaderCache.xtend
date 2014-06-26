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
package com.blockwithme.util.xtend

import java.util.Properties
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions

/**
 * Implements a thread-safe "cache" which works across multiple class-loaders.
 *
 * WARNING: It is strongly recommended to only put objects coming from the JRE in it,
 * *to avoid memory leaks*. If you need to use this cache, because you need it
 * within code that runs into temporary class loaders, then putting objects
 * in the cache that come from that temporary class loader will lock the class
 * loader into memory, therefore causing a memory leak.
 *
 * @author monster
 */
class AntiClassLoaderCache {
	/** System Property key that no one else should be using. */
	static val UNIQUE_KEY = "kljfgslkgs$dfgökfjughd%fiugsr"
	static var ConcurrentMap<String,Object> CACHE

	/** Cannot be instantiated. */
	private new() {
		// NOP
	}

	/** Returns a thread-safe cache, that survives between multiple class-loaders. */
	static def ConcurrentMap<String,Object> getCache() {
		if (CACHE === null) {
			// What System.properties returns is a *temporary* wrapper
			// over the true System.properties. It is a *different instance*
			// every time, and so useless for caching. Therefore, we need
			// to go back up to the real System.properties.
			val helper = new ReflectExtensions()
			var Properties parent = System.properties
			var Properties child = null
			do {
				child = parent
				parent = helper.get(child, "defaults")
				if (child === parent) {
					throw new IllegalStateException("child === parent")
				}
			} while (parent !== null);
			// Lazily create the cache
			CACHE = child.get(UNIQUE_KEY) as ConcurrentMap<String,Object>
			if (CACHE === null) {
				// Thread-safe Map
				CACHE = new ConcurrentHashMap<String,Object>
				child.put(UNIQUE_KEY, CACHE)
			}
		}
		CACHE
	}

	/** Drops all mappings starting with the given prefix */
	static def void clear(String prefix) {
		val cache = getCache()
		for (key : cache.keySet.toArray) {
			if (key.toString.startsWith(prefix)) {
				cache.remove(key)
			}
		}
	}

	/** Returns all mappings starting with the given prefix */
	static def Iterable<String> query(String prefix) {
		getCache().keySet.filter[toString.startsWith(prefix)]
	}

	/** Puts something in the cache. */
	static def void put(String prefix, String key, Object value) {
		getCache().put(prefix+key, value)
	}

	/** Gets something from the cache. */
	static def Object get(String prefix, String key) {
		getCache().get(prefix+key)
	}
}