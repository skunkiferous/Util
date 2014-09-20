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
package com.blockwithme.util.proto.stringnum;

import java.util.Arrays;

/**
 * StringnumWithData is a Stringnum that associate each interned Strings with
 * with some user-defined object.
 *
 * We do not actually check the type of the data objects, so it could be
 * anything or null.
 *
 * This object is NOT thread-safe.
 *
 * @author monster
 */
public class StringnumWithData<E> extends Stringnum {

    /** The associated data, if any. */
    private Object[] data = new Object[0];

    /** Called when the array grows. */
    @Override
    protected void onGrow(final int amount) {
        data = Arrays.copyOf(data, data.length + amount);
    }

    /** Adds a new String, if not presents yet, associate it with data, and return it's index. */
    public int putString(final String str, final E theData) {
        final int result = putString(str);
        data[result] = theData;
        return result;
    }

    /**
     * Returns the Data at the given index.
     * @throws java.lang.IndexOutOfBoundsException on bad index.
     */
    @SuppressWarnings("unchecked")
    public E getDataAt(final int index) {
        return (E) data[index];
    }

    /**
     * Sets Data at the given index
     */
    public E setDataAt(final int index, final E theData) {
        @SuppressWarnings("unchecked")
        final E result = (E) data[index];
        data[index] = theData;
        return result;
    }
}
