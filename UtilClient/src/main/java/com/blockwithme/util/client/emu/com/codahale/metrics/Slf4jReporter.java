package com.codahale.metrics;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * A reporter class for logging metrics values to a SLF4J {@link Logger} periodically, similar to
 * {@link ConsoleReporter} or {@link CsvReporter}, but using the SLF4J framework instead. It also
 * supports specifying a {@link Marker} instance that can be used by custom appenders and filters
 * for the bound logging toolkit to further process metrics reports.
 */
public class Slf4jReporter extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link Slf4jReporter}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link Slf4jReporter}
     */
    public static Builder forRegistry(final MetricRegistry registry) {
        return new Builder(registry);
    }

    /**
     * A builder for {@link CsvReporter} instances. Defaults to logging to {@code metrics}, not
     * using a marker, converting rates to events/second, converting durations to milliseconds, and
     * not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private Logger logger;
        private Marker marker;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(final MetricRegistry registry) {
            this.registry = registry;
            this.logger = LoggerFactory.getLogger("metrics");
            this.marker = null;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
        }

        /**
         * Log metrics to the given logger.
         *
         * @param logger an SLF4J {@link Logger}
         * @return {@code this}
         */
        public Builder outputTo(final Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Mark all logged metrics with the given marker.
         *
         * @param marker an SLF4J {@link Marker}
         * @return {@code this}
         */
        public Builder markWith(final Marker marker) {
            this.marker = marker;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(final TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(final TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(final MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Builds a {@link Slf4jReporter} with the given properties.
         *
         * @return a {@link Slf4jReporter}
         */
        public Slf4jReporter build() {
            return new Slf4jReporter(registry, logger, marker, rateUnit,
                    durationUnit, filter);
        }
    }

    private final Logger logger;
    private final Marker marker;

    private Slf4jReporter(final MetricRegistry registry, final Logger logger,
            final Marker marker, final TimeUnit rateUnit,
            final TimeUnit durationUnit, final MetricFilter filter) {
        super(registry, "logger-reporter", filter, rateUnit, durationUnit);
        this.logger = logger;
        this.marker = marker;
    }

    @Override
    public void report(final SortedMap<String, Gauge> gauges,
            final SortedMap<String, Counter> counters,
            final SortedMap<String, Histogram> histograms,
            final SortedMap<String, Meter> meters,
            final SortedMap<String, Timer> timers) {
        for (final Entry<String, Gauge> entry : gauges.entrySet()) {
            logGauge(entry.getKey(), entry.getValue());
        }

        for (final Entry<String, Counter> entry : counters.entrySet()) {
            logCounter(entry.getKey(), entry.getValue());
        }

        for (final Entry<String, Histogram> entry : histograms.entrySet()) {
            logHistogram(entry.getKey(), entry.getValue());
        }

        for (final Entry<String, Meter> entry : meters.entrySet()) {
            logMeter(entry.getKey(), entry.getValue());
        }

        for (final Entry<String, Timer> entry : timers.entrySet()) {
            logTimer(entry.getKey(), entry.getValue());
        }
    }

    private void logTimer(final String name, final Timer timer) {
        final Snapshot snapshot = timer.getSnapshot();
        logger.info(
                marker,
                format("type=TIMER, name={}, count={}, min={}, max={}, mean={}, stddev={}, median={}, "
                        + "p75={}, p95={}, p98={}, p99={}, p999={}, mean_rate={}, m1={}, m5={}, "
                        + "m15={}, rate_unit={}, duration_unit={}", name,
                        timer.getCount(), convertDuration(snapshot.getMin()),
                        convertDuration(snapshot.getMax()),
                        convertDuration(snapshot.getMean()),
                        convertDuration(snapshot.getStdDev()),
                        convertDuration(snapshot.getMedian()),
                        convertDuration(snapshot.get75thPercentile()),
                        convertDuration(snapshot.get95thPercentile()),
                        convertDuration(snapshot.get98thPercentile()),
                        convertDuration(snapshot.get99thPercentile()),
                        convertDuration(snapshot.get999thPercentile()),
                        convertRate(timer.getMeanRate()),
                        convertRate(timer.getOneMinuteRate()),
                        convertRate(timer.getFiveMinuteRate()),
                        convertRate(timer.getFifteenMinuteRate()),
                        getRateUnit(), getDurationUnit()));
    }

    private void logMeter(final String name, final Meter meter) {
        logger.info(
                marker,
                format("type=METER, name={}, count={}, mean_rate={}, m1={}, m5={}, m15={}, rate_unit={}",
                        name, meter.getCount(),
                        convertRate(meter.getMeanRate()),
                        convertRate(meter.getOneMinuteRate()),
                        convertRate(meter.getFiveMinuteRate()),
                        convertRate(meter.getFifteenMinuteRate()),
                        getRateUnit()));
    }

    private void logHistogram(final String name, final Histogram histogram) {
        final Snapshot snapshot = histogram.getSnapshot();
        logger.info(
                marker,
                format("type=HISTOGRAM, name={}, count={}, min={}, max={}, mean={}, stddev={}, "
                        + "median={}, p75={}, p95={}, p98={}, p99={}, p999={}",
                        name, histogram.getCount(), snapshot.getMin(),
                        snapshot.getMax(), snapshot.getMean(),
                        snapshot.getStdDev(), snapshot.getMedian(),
                        snapshot.get75thPercentile(),
                        snapshot.get95thPercentile(),
                        snapshot.get98thPercentile(),
                        snapshot.get99thPercentile(),
                        snapshot.get999thPercentile()));
    }

    private void logCounter(final String name, final Counter counter) {
        logger.info(
                marker,
                format("type=COUNTER, name={}, count={}", name,
                        counter.getCount()));
    }

    private void logGauge(final String name, final Gauge gauge) {
        logger.info(marker,
                format("type=GAUGE, name={}, value={}", name, gauge.getValue()));
    }

    /** Since our slf4j does NOT support "parameters", we have to format ourselves. */
    private static String format(final String format,
            final Object... parameters) {
        final char[] chars = format.toCharArray();
        final StringBuilder buf = new StringBuilder(chars.length * 3);
        // Next to use in array
        int next = 0;
        // Start of format String section
        int start = 0;
        // End of format String section
        int index = 0;
        while (index < chars.length) {
            while ((index < chars.length) && (chars[index] != '{')) {
                index++;
            }
            if (chars[index] == '{') {
                buf.append(format.substring(start, index));
                // Skip current and next char, assuming chars[index+1] == '}'
                index += 2;
                // Update start
                start = index;
                if (next < parameters.length) {
                    buf.append(parameters[next++]);
                } else {
                    // Should not happen!
                    buf.append("null");
                }
            }
        }
        if (start < chars.length) {
            buf.append(format.substring(start, chars.length));
        }
        return buf.toString();
    }

    @Override
    protected String getRateUnit() {
        return "events/" + super.getRateUnit();
    }
}
