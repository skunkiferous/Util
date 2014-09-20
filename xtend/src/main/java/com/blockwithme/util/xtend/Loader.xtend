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

import java.util.ServiceLoader

/**
 * Tiny wrapper over Service-Loader.
 *
 * Should allow future replacement when/if using OSGi, or whatever.
 *
 * This class does NOT specify if instances are cached or not.
 * In the case of the true ServiceLoader, it is to assume that
 * new instances will be created every time.
 *
 * @author monster
 */
class Loader<S> implements Iterable<S> {
	/** The desired type */
	val Class<S> service
	/** The source of instances. */
	val Iterable<S> instances

	private static def <S> Iterable<S> doLoad(Class<S> service, ClassLoader loader) {
		if (loader === null) {
			ServiceLoader.load(service)
		} else {
			ServiceLoader.load(service, loader)
		}
	}

	/** Returns a new Loader using the thread class loader */
    static def <S> Loader<S> load(Class<S> service) {
        return new Loader(service, doLoad(service, null))
    }

	/** Returns a new Loader using the specified class loader */
    static def <S> Loader<S> load(Class<S> service, ClassLoader loader) {
        return new Loader(service, doLoad(service, loader))
    }

	/** Constructor */
	private new(Class<S> service, Iterable<S> instances) {
		this.service = service
		this.instances = instances
	}

    override iterator() {
        instances.iterator()
    }

    override toString() {
        class.name+"[" + service.name + "]"
    }
}