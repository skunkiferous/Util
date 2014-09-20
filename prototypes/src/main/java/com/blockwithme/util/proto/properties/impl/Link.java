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
package com.blockwithme.util.proto.properties.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.blockwithme.util.proto.properties.Generator;
import com.blockwithme.util.proto.properties.Properties;

/**
 * A link, used as a Property value in a Properties object, simply returns some
 * other property from some (possibly other) Properties. It is an indirection,
 * and is generated automatically in most cases, where a Properties refers to
 * another Properties, which is not a direct child.
 *
 * @author monster
 */
public final class Link implements Generator {

    /** Static cache. */
    private static final ConcurrentHashMap<String, Link> CACHE = new ConcurrentHashMap<>();

    /** The link path. */
    private final String path;

    /** Creates a link with the given path */
    public Link(final String path) {
        PropertiesImpl.checkPath(path, "path");
        this.path = path;
    }

    /** toString */
    @Override
    public String toString() {
        return "Link(" + path + ")";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Link other = (Link) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Generator#generate(com.blockwithme.properties.Properties, java.lang.String, java.lang.Class)
     */
    @Override
    public <E> E generate(final Properties<?> prop, final String name,
            final Class<E> expectedType) {
        return prop.find(path, expectedType);
    }

    /** Returned a statically cached Link. */
    public static Link cache(final String path) {
        final Link newLink = new Link(path);
        final Link oldLink = CACHE.putIfAbsent(path, newLink);
        return (oldLink == null) ? newLink : oldLink;
    }
}
