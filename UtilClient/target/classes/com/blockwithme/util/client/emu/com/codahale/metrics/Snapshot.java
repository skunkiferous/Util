package com.codahale.metrics;

import static java.lang.Math.floor;

import java.util.Arrays;
import java.util.Collection;

/**
 * A statistical snapshot of a {@link Snapshot}.
 */
public class Snapshot {
    private final long[] values;

    /**
     * Create a new {@link Snapshot} with the given values.
     *
     * @param values    an unordered set of values in the reservoir
     */
    public Snapshot(final Collection<Long> values) {
        final Object[] copy = values.toArray();
        this.values = new long[copy.length];
        for (int i = 0; i < copy.length; i++) {
            this.values[i] = (Long) copy[i];
        }
        Arrays.sort(this.values);
    }

    /**
     * Create a new {@link Snapshot} with the given values.
     *
     * @param values    an unordered set of values in the reservoir
     */
    public Snapshot(final long[] values) {
        this.values = new long[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);
        Arrays.sort(this.values);
    }

    /**
     * Returns the value at the given quantile.
     *
     * @param quantile    a given quantile, in {@code [0..1]}
     * @return the value in the distribution at {@code quantile}
     */
    public double getValue(final double quantile) {
        if (quantile < 0.0 || quantile > 1.0) {
            throw new IllegalArgumentException(quantile + " is not in [0..1]");
        }

        if (values.length == 0) {
            return 0.0;
        }

        final double pos = quantile * (values.length + 1);

        if (pos < 1) {
            return values[0];
        }

        if (pos >= values.length) {
            return values[values.length - 1];
        }

        final double lower = values[(int) pos - 1];
        final double upper = values[(int) pos];
        return lower + (pos - floor(pos)) * (upper - lower);
    }

    /**
     * Returns the number of values in the snapshot.
     *
     * @return the number of values
     */
    public int size() {
        return values.length;
    }

    /**
     * Returns the median value in the distribution.
     *
     * @return the median value
     */
    public double getMedian() {
        return getValue(0.5);
    }

    /**
     * Returns the value at the 75th percentile in the distribution.
     *
     * @return the value at the 75th percentile
     */
    public double get75thPercentile() {
        return getValue(0.75);
    }

    /**
     * Returns the value at the 95th percentile in the distribution.
     *
     * @return the value at the 95th percentile
     */
    public double get95thPercentile() {
        return getValue(0.95);
    }

    /**
     * Returns the value at the 98th percentile in the distribution.
     *
     * @return the value at the 98th percentile
     */
    public double get98thPercentile() {
        return getValue(0.98);
    }

    /**
     * Returns the value at the 99th percentile in the distribution.
     *
     * @return the value at the 99th percentile
     */
    public double get99thPercentile() {
        return getValue(0.99);
    }

    /**
     * Returns the value at the 99.9th percentile in the distribution.
     *
     * @return the value at the 99.9th percentile
     */
    public double get999thPercentile() {
        return getValue(0.999);
    }

    /**
     * Returns the entire set of values in the snapshot.
     *
     * @return the entire set of values
     */
    public long[] getValues() {
        final long[] result = new long[values.length];
        System.arraycopy(values, 0, result, 0, values.length);
        return result;
    }

    /**
     * Returns the highest value in the snapshot.
     *
     * @return the highest value
     */
    public long getMax() {
        if (values.length == 0) {
            return 0;
        }
        return values[values.length - 1];
    }

    /**
     * Returns the lowest value in the snapshot.
     *
     * @return the lowest value
     */
    public long getMin() {
        if (values.length == 0) {
            return 0;
        }
        return values[0];
    }

    /**
     * Returns the arithmetic mean of the values in the snapshot.
     *
     * @return the arithmetic mean
     */
    public double getMean() {
        if (values.length == 0) {
            return 0;
        }

        double sum = 0;
        for (final long value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    /**
     * Returns the standard deviation of the values in the snapshot.
     *
     * @return the standard value
     */
    public double getStdDev() {
        // two-pass algorithm for variance, avoids numeric overflow

        if (values.length <= 1) {
            return 0;
        }

        final double mean = getMean();
        double sum = 0;

        for (final long value : values) {
            final double diff = value - mean;
            sum += diff * diff;
        }

        final double variance = sum / (values.length - 1);
        return Math.sqrt(variance);
    }
}
