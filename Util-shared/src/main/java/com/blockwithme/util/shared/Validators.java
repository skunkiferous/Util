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

package com.blockwithme.util.shared;

/**
 * Some pre-defined validators.
 *
 * @author monster
 */
public class Validators {
    /** Returns true for any value */
    private static final class AnyValidator implements Validator<Object> {
        @Override
        public String validate(final Object e) {
            return null;
        }
    };

    /** Returns true for non-null values. */
    private static final class NotNullValidator implements Validator<Object> {
        @Override
        public String validate(final Object e) {
            return (e == null) ? "cannot be null" : null;
        }
    };

    /** Returns true for any value */
    public static Validator<Object> any() {
        return new AnyValidator();
    }

    /** Returns true for non-null values. */
    public static Validator<Object> nonNull() {
        return new NotNullValidator();
    }
}
