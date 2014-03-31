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
package com.blockwithme.util.server.properties.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator that compare integers as numbers, and all non-integers as smaller then integers.
 *
 * @author monster
 */
public class NumbersLastStringComparator implements Comparator<String> {

    /** Singleton instance. */
    public static final Comparator<String> CMP = new NumbersLastStringComparator();

    /** To use double first, as it's cheaper. */
    private Double toDouble(final String str) {
        final char c = str.isEmpty() ? ' ' : str.charAt(0);
        if ((c != '-') && (c < '0') || (c > '9')) {
            return null;
        }
        try {
            return new Double(str);
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    /** To correctly compare both doubles and longs, we need something bigger then both */
    private BigDecimal toBigDecimal(final String str) {
        try {
            return new BigDecimal(str);
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    @Override
    public int compare(final String o1, final String o2) {
        if (Objects.equals(o1, o2)) {
            return 0;
        }
        final Double n1 = toDouble(o1);
        final Double n2 = toDouble(o2);
        if (n1 == null) {
            if (n2 == null) {
                return o1.compareTo(o2);
            }
            return -1;
        }
        if (n2 != null) {
            final int result = n1.compareTo(n2);
            if (result == 0) {
                // OK, we have two *different* strings that compare equals?
                // It's probably a rounding issue...
                final BigDecimal bd1 = toBigDecimal(o1);
                final BigDecimal bd2 = toBigDecimal(o2);
                if ((bd1 != null) && (bd2 != null)) {
                    return bd1.compareTo(bd2);
                }
            }
            return result;
        }
        return -1;
    }
}
