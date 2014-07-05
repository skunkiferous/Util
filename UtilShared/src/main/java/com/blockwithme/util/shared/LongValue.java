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

import java.io.Serializable;

/** Wrapper for long values */
final class LongValue implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 0;

    public LongValue(final long v) {
        value = v;
    }

    public final long value;

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof LongValue)
                && (((LongValue) obj).value == value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}