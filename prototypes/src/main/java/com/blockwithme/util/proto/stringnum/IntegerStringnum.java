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

/**
 * IntegerStringnum is a Stringnum that only accepts integers > 0, in the
 * natural order.
 *
 * Note that if onNewString() fails, the object will be inconsistent and
 * cannot be safely used anymore.
 *
 * TODO Test
 *
 * @author monster
 */
public class IntegerStringnum extends Stringnum {

    /** Constructor. */
    public IntegerStringnum() {
        // NOP
    }

    /** Constructor that allows pre-creating the IDs. */
    public IntegerStringnum(final int generateUpToInclusive) {
        for (int i = 1; i <= generateUpToInclusive; i++) {
            add(String.valueOf(i));
        }
    }

    /**
     * Generates the next ID.
     *
     * Note that this method is NOT thread-safe.
     */
    public String next() {
        return intern(String.valueOf(size()));
    }

    /** Called when a new String is indexed. */
    @Override
    protected void onNewString(final String original, final String hacked) {
        // Fails if not integer
        final int value = Integer.parseInt(hacked);
        if (value != hacked.hashCode()) {
            throw new IllegalStateException("Added " + hacked + " at position "
                    + hacked.hashCode());
        }
    }
}
