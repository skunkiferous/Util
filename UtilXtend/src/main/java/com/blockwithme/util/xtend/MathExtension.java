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
package com.blockwithme.util.xtend;

import java.util.Objects;

import com.blockwithme.util.shared.RandomEventSource;

/**
 * Xtend Extension related to numbers and mathematics values.
 *
 * @author monster
 */
public class MathExtension extends BooleanExtension {
    // TODO

    /** Wraps an existing RandomEventSource, to produce normally distributed "random event". */
    private static final class NextGaussianRandomEventSource implements
            RandomEventSource {
        private final RandomEventSource source;

        /** Constructor */
        public NextGaussianRandomEventSource(final RandomEventSource theSource) {
            source = Objects.requireNonNull(theSource);
        }

        /* (non-Javadoc)
         * @see com.blockwithme.util.shared.RandomEventSource#nextDouble()
         */
        @Override
        public double nextDouble() {
            return nextGaussian(source);
        }
    }

    /** Something like the biggest number less then 1 in the double format. */
    public static final double BEFORE_ONE = 1.0 - 2.2204460492503130808472633361816E-16;

    /**
     * Generates a normally distributed "random event".
     * @return a normally distributed "random event", where the range is between 0 inclusive and 1 exclusive.
     */
    public static double nextGaussian(final RandomEventSource source) {
        double v1, v2, s;
        do {
            // Generates two independent random variables U1, U2
            v1 = 2 * source.nextDouble() - 1;
            v2 = 2 * source.nextDouble() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        final double norm = Math.sqrt(-2 * Math.log(s) / s);
        final double result = v1 * norm;
        // On 1,000,000 calls, this would usually yield about -5 minimum value,
        // and +5 maximum value. So we pretend the range is [-5.5,5.5] and normalize
        // it to [0,1], clamping if needed.
        final double normalized = (result + 5.5) / 11.0;
        return (normalized < 0) ? 0 : (normalized > BEFORE_ONE ? BEFORE_ONE
                : normalized);
    }

    /**
     * Generates a normally distributed "random event".
     * @return a normally distributed "random event", where the range is between 0 inclusive and 1 exclusive.
     */
    public static RandomEventSource nextGaussianWrapper(
            final RandomEventSource source) {
        if (source instanceof NextGaussianRandomEventSource) {
            return source;
        }
        return new NextGaussianRandomEventSource(source);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final double value, final double min,
            final double max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final long value, final long min, final long max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final int value, final int min, final int max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final float value, final float min,
            final float max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final char value, final char min, final char max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final short value, final short min,
            final short max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    /** Clamps the value between min and max, both inclusive. */
    public static double clamp(final byte value, final byte min, final byte max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }
}
