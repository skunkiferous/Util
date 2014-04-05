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

import java.lang.reflect.Constructor;

import com.blockwithme.util.proto.properties.Generator;
import com.blockwithme.util.proto.properties.Properties;

/**
 * Lazy generator. Delegates to another generator on demand, once only, then
 * caches the result.
 *
 * It is mostly there as an example of what Generators can be used for.
 *
 * @author monster
 */
public class LazyGen implements Generator {

    /** The class name of the generator to create. */
    private final String genType;

    /** The parameter the generator to create. */
    private final String genParam;

    /**
     * Creates a LazyGen with a parameter in the form:
     * full-class-name-of-real-generator(parameter-to-real-generator)
     */
    public LazyGen(final String param) {
        final int openIndex = param.indexOf('(');
        final int closeIndex = param.lastIndexOf(')');
        if ((openIndex <= 0) || (closeIndex < param.length() - 1)) {
            throw new IllegalArgumentException(param);
        }
        genType = param.substring(0, openIndex);
        genParam = param.substring(openIndex + 1, closeIndex);
    }

    /** toString */
    @Override
    public String toString() {
        return "LazyGen(" + genType + "(" + genParam + "))";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((genParam == null) ? 0 : genParam.hashCode());
        result = prime * result + ((genType == null) ? 0 : genType.hashCode());
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
        final LazyGen other = (LazyGen) obj;
        if (genParam == null) {
            if (other.genParam != null)
                return false;
        } else if (!genParam.equals(other.genParam))
            return false;
        if (genType == null) {
            if (other.genType != null)
                return false;
        } else if (!genType.equals(other.genType))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see com.blockwithme.properties.Generator#generate(com.blockwithme.properties.Properties, java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E generate(final Properties<?> prop, final String name,
            final Class<E> expectedType) {
        // Creates an instance of type genType using parameter genParam ...
        final Class<Generator> type;
        try {
            type = (Class<Generator>) Class.forName(genType);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Cannot find/load class " + genType, e);
        }
        final Constructor<Generator> ctr;
        try {
            ctr = type.getConstructor(new Class[] { String.class });
        } catch (final Exception e) {
            throw new IllegalStateException(
                    "Cannot find String constructor for class " + genType, e);
        }
        final Generator realGen;
        try {
            realGen = ctr.newInstance(genParam);
        } catch (final Exception e) {
            throw new IllegalStateException("Cannot create class instance of "
                    + genType + " with '" + genParam + "'", e);
        }
        // Calls real generator
        @SuppressWarnings("rawtypes")
        final Properties untyped = prop;
        final E value = realGen.generate(untyped, name, expectedType);
        // Replace self in Properties with generated value!
        untyped.set(untyped, name, value);
        return value;
    }
}
