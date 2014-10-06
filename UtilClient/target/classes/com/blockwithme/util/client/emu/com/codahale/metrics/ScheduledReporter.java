package com.codahale.metrics;

import java.io.Closeable;
import java.util.SortedMap;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blockwithme.util.base.SystemUtils;

/**
 * The abstract base class for all scheduled reporters (i.e., reporters which process a registry's
 * metrics periodically).
 *
 * @see ConsoleReporter
 * @see CsvReporter
 * @see Slf4jReporter
 */
public abstract class ScheduledReporter implements Closeable, Reporter {

    private static final Logger LOG = LoggerFactory
            .getLogger(ScheduledReporter.class);

    private final MetricRegistry registry;
    private final MetricFilter filter;
    private final double durationFactor;
    private final String durationUnit;
    private final double rateFactor;
    private final String rateUnit;
    private final String name;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                report();
            } catch (final RuntimeException e) {
                LOG.error("Failed to report from " + getClass().getName() + "/"
                        + name, e);
            }
        }
    };

    /**
     * Creates a new {@link ScheduledReporter} instance.
     *
     * @param registry the {@link com.codahale.metrics.MetricRegistry} containing the metrics this
     *                 reporter will report
     * @param name     the reporter's name
     * @param filter   the filter for which metrics to report
     */
    protected ScheduledReporter(final MetricRegistry registry,
            final String name, final MetricFilter filter,
            final TimeUnit rateUnit, final TimeUnit durationUnit) {
        this.registry = registry;
        this.filter = filter;
        this.rateFactor = rateUnit.toSeconds(1);
        this.rateUnit = calculateRateUnit(rateUnit);
        this.durationFactor = 1.0 / durationUnit.toNanos(1);
        this.durationUnit = durationUnit.toString().toLowerCase();
        this.name = name;
    }

    /** toString */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Starts the reporter polling at the given period.
     *
     * @param period the amount of time between polls
     * @param unit   the unit for {@code period}
     */
    public void start(final long period, final TimeUnit unit) {
        final java.util.Timer timer = SystemUtils.getTimer();
        if (timer == null) {
            throw new IllegalStateException("Timer not available!");
        }
        try {
            final long value = TimeUnit.MILLISECONDS.convert(period, unit);
            LOG.info("Reporting every " + value + " milliseconds for "
                    + getClass().getName() + "/" + name);
            timer.scheduleAtFixedRate(timerTask, value, value);
        } catch (final RuntimeException e) {
            throw new IllegalArgumentException(
                    "Could not start Reporter with parameters: start(" + period
                            + "," + unit + ")", e);
        }
    }

    /**
     * Stops the reporter and shuts down its thread of execution.
     *
     * Uses the shutdown pattern from http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
     */
    public void stop() {
        timerTask.cancel();
    }

    /**
     * Stops the reporter and shuts down its thread of execution.
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * Report the current values of all metrics in the registry.
     */
    public void report() {
        report(registry.getGauges(filter), registry.getCounters(filter),
                registry.getHistograms(filter), registry.getMeters(filter),
                registry.getTimers(filter));
    }

    /**
     * Called periodically by the polling thread. Subclasses should report all the given metrics.
     *
     * @param gauges     all of the gauges in the registry
     * @param counters   all of the counters in the registry
     * @param histograms all of the histograms in the registry
     * @param meters     all of the meters in the registry
     * @param timers     all of the timers in the registry
     */
    public abstract void report(SortedMap<String, Gauge> gauges,
            SortedMap<String, Counter> counters,
            SortedMap<String, Histogram> histograms,
            SortedMap<String, Meter> meters, SortedMap<String, Timer> timers);

    protected String getRateUnit() {
        return rateUnit;
    }

    protected String getDurationUnit() {
        return durationUnit;
    }

    protected double convertDuration(final double duration) {
        return duration * durationFactor;
    }

    protected double convertRate(final double rate) {
        return rate * rateFactor;
    }

    private String calculateRateUnit(final TimeUnit unit) {
        final String s = unit.toString().toLowerCase();
        return s.substring(0, s.length() - 1);
    }
}
