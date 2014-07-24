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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import com.blockwithme.util.shared.FuzzySource;

/**
 * Xtend Extension related to basic "fuzzy logic" operations.
 * It is assumed that all input values are already clamped to [0,1].
 * @author monster
 */
public class FuzzyExtension {

    /** "De-normalize" a fuzzy logic value to the boolean "range" (value >= 0.5f). */
    @Pure
    @Inline("($1 >= 0.5f)")
    public static boolean denormalize(final float value) {
        return (value >= 0.5f);
    }

    /** "De-normalize" a fuzzy logic "random event" to the boolean "range" (value >= 0.5f). */
    @Inline("($1.fuzzLevel() >= 0.5f)")
    public static boolean denormalize(final FuzzySource source) {
        return (source.fuzzLevel() >= 0.5f);
    }

    /** Is a fuzzy logic value "likely"? (value >= 2/3). */
    @Pure
    @Inline("($1 >= 2.0f/3.0f)")
    public static boolean likely(final float value) {
        return (value >= 2.0f / 3.0f);
    }

    /** Is a fuzzy logic "random event" "likely"? (value >= 2/3). */
    @Inline("($1.fuzzLevel() >= 2.0f/3.0f)")
    public static boolean likely(final FuzzySource source) {
        return (source.fuzzLevel() >= 2.0f / 3.0f);
    }

    /** Is a fuzzy logic value "unlikely"? (value <= 1/3). */
    @Pure
    @Inline("($1 <= 1.0f/3.0f)")
    public static boolean unlikely(final float value) {
        return (value <= 1.0f / 3.0f);
    }

    /** Is a fuzzy logic "random event" "unlikely"? (value <= 1/3). */
    @Inline("($1.fuzzLevel() <= 1.0f/3.0f)")
    public static boolean unlikely(final FuzzySource source) {
        return (source.fuzzLevel() <= 1.0f / 3.0f);
    }

    /** Is a fuzzy logic value "undecided"? (value > 1/3) && (value < 2/3). */
    @Pure
    @Inline("(($1 > 1.0f/3.0f)&&($1 < 2.0f/3.0f))")
    public static boolean undecided(final float value) {
        return (value > 1.0f / 3.0f) && (value < 2.0f / 3.0f);
    }

    /**
     * Is a fuzzy logic "random event" "undecided"? (value > 1/3) && (value < 2/3).
     *
     * Not @Inline, because it would not actually save a method call.
     */
    public static boolean undecided(final FuzzySource source) {
        final float v = source.fuzzLevel();
        return (v > 1.0f / 3.0f) && (v < 2.0f / 3.0f);
    }

    /**
     * Converts a fuzzy logic value to a "tri-state" value
     * (TRUE/likely, null/undecided, FALSE/unlikely)
     */
    @Pure
    @Inline("(($1 <= 1.0f/3.0f) ? Boolean.FALSE : ($1 >= 2.0f/3.0f ? Boolean.TRUE : null))")
    public static Boolean triState(final float value) {
        return (value <= 1.0f / 3.0f) ? Boolean.FALSE
                : (value >= 2.0f / 3.0f ? Boolean.TRUE : null);
    }

    /**
     * Converts a fuzzy logic "random event" to a "tri-state" value
     * (TRUE/likely, null/undecided, FALSE/unlikely)
     *
     * Not @Inline, because it would not actually save a method call.
     */
    public static Boolean triState(final FuzzySource source) {
        final float v = source.fuzzLevel();
        return (v <= 1.0f / 3.0f) ? Boolean.FALSE
                : (v >= 2.0f / 3.0f ? Boolean.TRUE : null);
    }

    /** Clamps a float value to the fuzzy logic range. */
    @Pure
    @Inline("($1 >= 0 ? ($1 <= 1f ? $1 : 1f): 0f)")
    public static float clamp(final float value) {
        return (value >= 0 ? (value <= 1 ? value : 1) : 0);
    }

    /** "Normalize" a boolean value to the fuzzy logic range. */
    @Pure
    @Inline("($1 ? 1f : 0f)")
    public static float normalize(final boolean value) {
        return (value ? 1 : 0);
    }

    /** Normalize an *unsigned* byte value to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) ((($1 & 0xFF) - ($2 & 0xFF)) / (($3 & 0xFF) - ($2 & 0xFF) + 1.0f)))")
    public static float unormalize(final byte value, final byte minInclusive,
            final byte maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        // Clamps value, just in case.
        return clamp(((value & 0xFF) - (minInclusive & 0xFF))
                / ((maxInclusive & 0xFF) - (minInclusive & 0xFF) + 1.0f));
    }

    /** Normalize an *unsigned* byte value starting at 0 to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) (($1 & 0xFF) / (($2 & 0xFF) + 1.0f)))")
    public static float uonormalize(final byte value, final byte maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        // Clamps value, just in case.
        return clamp((float) ((value & 0xFF) / ((maxInclusive & 0xFF) + 1.0)));
    }

    /** Normalize an integer value to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) (($1 - $2) / ($3 - $2 + 1.0)))")
    public static float normalize(final int value, final int minInclusive,
            final int maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        // Clamps value, just in case.
        return clamp((float) ((value - minInclusive) / (maxInclusive
                - minInclusive + 1.0)));
    }

    /** Normalize a long value to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) (($1 - $2) / ($3 - $2 + 1.0)))")
    public static float normalize(final long value, final long minInclusive,
            final long maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire long range is used
        // Clamps value, just in case.
        return clamp((float) ((value - minInclusive) / (maxInclusive
                - minInclusive + 1.0)));
    }

    /** Normalize a float value to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp(($1 - $2) / ($3 - $2))")
    public static float normalize(final float value, final float minInclusive,
            final float maxInclusive) {
        // Clamps value, just in case.
        return clamp((value - minInclusive) / (maxInclusive - minInclusive));
    }

    /** Normalize a double value to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) (($1 - $2) / ($3 - $2)))")
    public static float normalize(final double value,
            final double minInclusive, final double maxInclusive) {
        // Clamps value, just in case.
        return clamp((float) ((value - minInclusive) / (maxInclusive - minInclusive)));
    }

    /** Normalize an integer value starting at 0 to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) ($1 / ($2 + 1.0)))")
    public static float onormalize(final int value, final int maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        // Clamps value, just in case.
        return clamp((float) (value / (maxInclusive + 1.0)));
    }

    /** Normalize a long value starting at 0 to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) ($1 / ($2 + 1.0)))")
    public static float onormalize(final long value, final long maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire long range is used
        // Clamps value, just in case.
        return clamp((float) (value / (maxInclusive + 1.0)));
    }

    /** Normalize a float value starting at 0 to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp($1 / $2)")
    public static float onormalize(final float value, final float maxInclusive) {
        // Clamps value, just in case.
        return clamp(value / maxInclusive);
    }

    /** Normalize a double value starting at 0 to the fuzzy logic range. */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.clamp((float) ($1 / $2))")
    public static float onormalize(final double value, final double maxInclusive) {
        // Clamps value, just in case.
        return clamp((float) (value / maxInclusive));
    }

    /** Negates a fuzzy logic value. */
    @Pure
    @Inline("(1 - $1)")
    public static float not(final float value) {
        return (1 - value);
    }

    /** Negates a fuzzy logic "random event". */
    @Inline("(1 - $1.fuzzLevel())")
    public static float not(final FuzzySource source) {
        return (1 - source.fuzzLevel());
    }

    /** Rounds a fuzzy logic value to either 0 or 1. */
    @Pure
    @Inline("(($1 >= 0.5f) ? 1f : 0f)")
    public static float decide(final float value) {
        return (value >= 0.5f) ? 1f : 0f;
    }

    /** Rounds a fuzzy logic "random event" to either 0 or 1. */
    @Inline("(($1.fuzzLevel() >= 0.5f) ? 1f : 0f)")
    public static float decide(final FuzzySource source) {
        return (source.fuzzLevel() >= 0.5f) ? 1f : 0f;
    }

    /** ANDs 2 fuzzy logic values. */
    @Pure
    @Inline("($1 < $2 ? $1 : $2)")
    public static float and(final float a, final float b) {
        return (a < b ? a : b);
    }

    /** ANDs 2 fuzzy logic values. */
    public static float and(final FuzzySource a, final float b) {
        final float aa = a.fuzzLevel();
        return (aa < b ? aa : b);
    }

    /** ANDs 2 fuzzy logic values. */
    public static float and(final float a, final FuzzySource b) {
        final float bb = b.fuzzLevel();
        return (a < bb ? a : bb);
    }

    /** ANDs 2 fuzzy logic values. */
    public static float and(final FuzzySource a, final FuzzySource b) {
        final float aa = a.fuzzLevel();
        final float bb = b.fuzzLevel();
        return (aa < bb ? aa : bb);
    }

    /** not(ANDs 2 fuzzy logic values). */
    @Pure
    @Inline("(1 - ($1 < $2 ? $1 : $2))")
    public static float nand(final float a, final float b) {
        return (1 - (a < b ? a : b));
    }

    /** not(ANDs 2 fuzzy logic values). */
    public static float nand(final FuzzySource a, final float b) {
        final float aa = a.fuzzLevel();
        return (1 - (aa < b ? aa : b));
    }

    /** not(ANDs 2 fuzzy logic values). */
    public static float nand(final float a, final FuzzySource b) {
        final float bb = b.fuzzLevel();
        return (1 - (a < bb ? a : bb));
    }

    /** not(ANDs 2 fuzzy logic values). */
    public static float nand(final FuzzySource a, final FuzzySource b) {
        final float aa = a.fuzzLevel();
        final float bb = b.fuzzLevel();
        return (1 - (aa < bb ? aa : bb));
    }

    /** ORs 2 fuzzy logic values. */
    @Pure
    @Inline("($1 > $2 ? $1 : $2)")
    public static float or(final float a, final float b) {
        return (a > b ? a : b);
    }

    /** ORs 2 fuzzy logic values. */
    public static float or(final FuzzySource a, final float b) {
        final float aa = a.fuzzLevel();
        return (aa > b ? aa : b);
    }

    /** ORs 2 fuzzy logic values. */
    public static float or(final float a, final FuzzySource b) {
        final float bb = b.fuzzLevel();
        return (a > bb ? a : bb);
    }

    /** ORs 2 fuzzy logic values. */
    public static float or(final FuzzySource a, final FuzzySource b) {
        final float aa = a.fuzzLevel();
        final float bb = b.fuzzLevel();
        return (aa > bb ? aa : bb);
    }

    /** not(ORs 2 fuzzy logic values). */
    @Pure
    @Inline("(1 - ($1 > $2 ? $1 : $2))")
    public static float nor(final float a, final float b) {
        return (1 - (a > b ? a : b));
    }

    /** not(ORs 2 fuzzy logic values). */
    public static float nor(final FuzzySource a, final float b) {
        final float aa = a.fuzzLevel();
        return (1 - (aa > b ? aa : b));
    }

    /** not(ORs 2 fuzzy logic values). */
    public static float nor(final float a, final FuzzySource b) {
        final float bb = b.fuzzLevel();
        return (1 - (a > bb ? a : bb));
    }

    /** not(ORs 2 fuzzy logic values). */
    public static float nor(final FuzzySource a, final FuzzySource b) {
        final float aa = a.fuzzLevel();
        final float bb = b.fuzzLevel();
        return (1 - (aa > bb ? aa : bb));
    }

    /** ANDs 3 fuzzy logic values. */
    @Pure
    @Inline("($1 < $2 ? ($1 < $3 ? $1 : $3) : ($2 < $3 ? $2 : $3))")
    public static float and(final float a, final float b, final float c) {
        return (a < b ? (a < c ? a : c) : (b < c ? b : c));
    }

    /** not(ANDs 3 fuzzy logic values). */
    @Pure
    @Inline("(1 - ($1 < $2 ? ($1 < $3 ? $1 : $3) : ($2 < $3 ? $2 : $3)))")
    public static float nand(final float a, final float b, final float c) {
        return (1 - (a < b ? (a < c ? a : c) : (b < c ? b : c)));
    }

    /** ORs 3 fuzzy logic values. */
    @Pure
    @Inline("($1 > $2 ? ($1 > $3 ? $1 : $3) : ($2 > $3 ? $2 : $3))")
    public static float or(final float a, final float b, final float c) {
        return (a > b ? (a > c ? a : c) : (b > c ? b : c));
    }

    /** not(ORs 3 fuzzy logic values). */
    @Pure
    @Inline("(1 - ($1 > $2 ? ($1 > $3 ? $1 : $3) : ($2 > $3 ? $2 : $3)))")
    public static float nor(final float a, final float b, final float c) {
        return (1 - (a > b ? (a > c ? a : c) : (b > c ? b : c)));
    }

    /** ANDs N fuzzy logic values. */
    @Pure
    public static float and(final float... values) {
        if (values.length == 0) {
            return 0;
        }
        float result = 1;
        for (final float f : values) {
            if (f < result) {
                result = f;
            }
        }
        return result;
    }

    /** not(ANDs N fuzzy logic values). */
    @Pure
    public static float nand(final float... values) {
        return (1 - and(values));
    }

    /** ORs N fuzzy logic values. */
    @Pure
    public static float or(final float... values) {
        float result = 0;
        for (final float f : values) {
            if (f > result) {
                result = f;
            }
        }
        return result;
    }

    /** not(ORs N fuzzy logic values). */
    @Pure
    public static float nor(final float... values) {
        return (1 - or(values));
    }

    /**
     * XORs 2 fuzzy logic values.
     * Implemented as: (a & !b) | (b & !a)
     */
    @Pure
    public static float xor(final float a, final float b) {
        final float not_a = (1 - a);
        final float not_b = (1 - b);
        final float and_a_not_b = (a < not_b ? a : not_b);
        final float and_not_a_b = (not_a < b ? not_a : b);
        return (and_a_not_b > and_not_a_b ? and_a_not_b : and_not_a_b);
    }

    /**
     * XORs 2 fuzzy logic values.
     * Implemented as: (a & !b) | (b & !a)
     */
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.xor($1.fuzzLevel(), $2)")
    public static float xor(final FuzzySource a, final float b) {
        return xor(a.fuzzLevel(), b);
    }

    /**
     * XORs 2 fuzzy logic values.
     * Implemented as: (a & !b) | (b & !a)
     */
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.xor($1, $2.fuzzLevel())")
    public static float xor(final float a, final FuzzySource b) {
        return xor(a, b.fuzzLevel());
    }

    /**
     * XORs 2 fuzzy logic values.
     * Implemented as: (a & !b) | (b & !a)
     */
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.xor($1.fuzzLevel(), $2.fuzzLevel())")
    public static float xor(final FuzzySource a, final FuzzySource b) {
        return xor(a.fuzzLevel(), b.fuzzLevel());
    }

    /** not(XORs 2 fuzzy logic values). */
    @Pure
    @Inline("(1 - com.blockwithme.util.xtend.FuzzyExtension.xor($1, $2))")
    public static float nxor(final float a, final float b) {
        return (1 - xor(a, b));
    }

    /** not(XORs 2 fuzzy logic values). */
    @Inline("(1 - com.blockwithme.util.xtend.FuzzyExtension.xor($1.fuzzLevel(), $2))")
    public static float nxor(final FuzzySource a, final float b) {
        return (1 - xor(a.fuzzLevel(), b));
    }

    /** not(XORs 2 fuzzy logic values). */
    @Inline("(1 - com.blockwithme.util.xtend.FuzzyExtension.xor($1, $2.fuzzLevel()))")
    public static float nxor(final float a, final FuzzySource b) {
        return (1 - xor(a, b.fuzzLevel()));
    }

    /** not(XORs 2 fuzzy logic values). */
    @Inline("(1 - com.blockwithme.util.xtend.FuzzyExtension.xor($1.fuzzLevel(), $2.fuzzLevel()))")
    public static float nxor(final FuzzySource a, final FuzzySource b) {
        return (1 - xor(a.fuzzLevel(), b.fuzzLevel()));
    }

    /**
     * Clamps float values to the fuzzy logic range.
     * @return a new array.
     */
    @Pure
    public static float[] clamp(final float[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = values[i];
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /**
     * Clamps "locally" float values to the fuzzy logic range.
     * This method has therefore "side-effects" (unpure).
     * @return the original, modified, array.
     */
    public static float[] clampLocal(final float[] values) {
        for (int i = 0; i < values.length; i++) {
            final float value = values[i];
            values[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return values;
    }

    /** "Normalize" a boolean array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final boolean[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i] ? 1 : 0;
        }
        return result;
    }

    /** Normalize a byte array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final byte[] values,
            final byte minInclusive, final byte maxInclusive) {
        // We count in float, to prevent issues with overflows if the entire int range is used
        final float div = (maxInclusive - minInclusive + 1.0f);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (values[i] - minInclusive) / div;
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize an *unsigned* byte array to the fuzzy logic range. */
    @Pure
    public static float[] unormalize(final byte[] values,
            final byte minInclusive, final byte maxInclusive) {
        // We count in float, to prevent issues with overflows if the entire int range is used
        final int uminInclusive = minInclusive & 0xFF;
        final int umaxInclusive = maxInclusive & 0xFF;
        final float div = (umaxInclusive - uminInclusive + 1.0f);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = ((values[i] & 0xFF) - uminInclusive) / div;
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize a char array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final char[] values,
            final char minInclusive, final char maxInclusive) {
        // We count in float, to prevent issues with overflows if the entire int range is used
        final float div = (maxInclusive - minInclusive + 1.0f);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (values[i] - minInclusive) / div;
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize a short array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final short[] values,
            final short minInclusive, final short maxInclusive) {
        // We count in float, to prevent issues with overflows if the entire int range is used
        final float div = (maxInclusive - minInclusive + 1.0f);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (values[i] - minInclusive) / div;
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize an integer array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final int[] values, final int minInclusive,
            final int maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        final double div = (maxInclusive - minInclusive + 1.0);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (float) ((values[i] - minInclusive) / div);
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize a long array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final long[] values,
            final long minInclusive, final long maxInclusive) {
        // We count in double, to prevent issues with overflows if the entire int range is used
        final double div = (maxInclusive - minInclusive + 1.0);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (float) ((values[i] - minInclusive) / div);
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize a float array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final float[] values,
            final float minInclusive, final float maxInclusive) {
        final float div = (maxInclusive - minInclusive);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (values[i] - minInclusive) / div;
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /** Normalize a double array to the fuzzy logic range. */
    @Pure
    public static float[] normalize(final double[] values,
            final double minInclusive, final double maxInclusive) {
        final double div = (maxInclusive - minInclusive);
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            final float value = (float) ((values[i] - minInclusive) / div);
            // Clamps value, just in case.
            result[i] = (value >= 0 ? (value <= 1 ? value : 1) : 0);
        }
        return result;
    }

    /**
     * Negates a fuzzy logic value array.
     * @return a new array.
     */
    @Pure
    public static float[] not(final float[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (1 - values[i]);
        }
        return result;
    }

    /**
     * Negates "locally" a fuzzy logic value array.
     * This method has therefore "side-effects" (unpure).
     * @return the original, modified, array.
     */
    public static float[] notLocal(final float[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (1 - values[i]);
        }
        return values;
    }

    /**
     * Rounds fuzzy logic values to either 0 or 1.
     * @return a new array.
     */
    @Pure
    public static float[] decide(final float[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (values[i] >= 0.5f) ? 1f : 0f;
        }
        return result;
    }

    /**
     * Rounds "locally" fuzzy logic values to either 0 or 1.
     * This method has therefore "side-effects" (unpure).
     * @return the original, modified, array.
     */
    public static float[] decideLocal(final float[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (values[i] >= 0.5f) ? 1f : 0f;
        }
        return values;
    }

    /**
     * ANDs 2 fuzzy logic value arrays.
     * @return a new array.
     */
    @Pure
    public static float[] and(final float[] a, final float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("a.length=" + a.length
                    + ", b.length=" + b.length);
        }
        final float[] result = new float[a.length];
        for (int i = 0; i < result.length; i++) {
            final float ai = a[i];
            final float bi = b[i];
            result[i] = (ai < bi ? ai : bi);
        }
        return result;
    }

    /**
     * not(ANDs 2 fuzzy logic value arrays).
     * @return a new array.
     */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.notLocal(com.blockwithme.util.xtend.FuzzyExtension.and($1, $2))")
    public static float[] nand(final float[] a, final float[] b) {
        return notLocal(and(a, b));
    }

    /**
     * ORs 2 fuzzy logic value arrays.
     * @return a new array.
     */
    @Pure
    public static float[] or(final float[] a, final float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("a.length=" + a.length
                    + ", b.length=" + b.length);
        }
        final float[] result = new float[a.length];
        for (int i = 0; i < result.length; i++) {
            final float ai = a[i];
            final float bi = b[i];
            result[i] = (ai > bi ? ai : bi);
        }
        return result;
    }

    /**
     * not(ORs 2 fuzzy logic value arrays).
     * @return a new array.
     */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.notLocal(com.blockwithme.util.xtend.FuzzyExtension.or($1, $2))")
    public static float[] nor(final float[] a, final float[] b) {
        return notLocal(or(a, b));
    }

    /**
     * XORs 2 fuzzy logic value arrays.
     * @return a new array.
     */
    @Pure
    public static float[] xor(final float[] a, final float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("a.length=" + a.length
                    + ", b.length=" + b.length);
        }
        final float[] result = new float[a.length];
        for (int i = 0; i < result.length; i++) {
            final float ai = a[i];
            final float bi = b[i];
            final float not_ai = (1 - ai);
            final float not_bi = (1 - bi);
            final float and_ai_not_bi = (ai < not_bi ? ai : not_bi);
            final float and_not_ai_bi = (not_ai < bi ? not_ai : bi);
            result[i] = (and_ai_not_bi > and_not_ai_bi ? and_ai_not_bi
                    : and_not_ai_bi);
        }
        return result;
    }

    /**
     * not(XORs 2 fuzzy logic value arrays).
     * @return a new array.
     */
    @Pure
    @Inline("com.blockwithme.util.xtend.FuzzyExtension.notLocal(com.blockwithme.util.xtend.FuzzyExtension.xor($1, $2))")
    public static float[] nxor(final float[] a, final float[] b) {
        return notLocal(xor(a, b));
    }
}
