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
package com.blockwithme.util.server.properties;

/**
 * Generates a property value on the fly.
 *
 * Links are implemented using a Generator.
 *
 * @author monster
 */
public interface Generator {
    /**
     * Generates the value of a property.
     *
     * It receives the Properties instance from which it was executed, the
     * property name with which it was associated, and the expected data type
     * to return. The expected data-type might be null.
     */
    <E> E generate(final Properties<?> prop, final String name,
            final Class<E> expectedType);
}
