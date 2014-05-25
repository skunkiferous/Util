package com.codahale.metrics;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.blockwithme.util.base.SystemUtils;

/**
 * A reporter which outputs measurements to a {@link PrintStream}, like {@code System.out}.
 */
public class ConsoleReporter extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link ConsoleReporter}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link ConsoleReporter}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    /**
     * A builder for {@link ConsoleReporter} instances. Defaults to using the default locale and
     * time zone, writing to {@code System.out}, converting rates to events/second, converting
     * durations to milliseconds, and not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private PrintStream output;
        private Clock clock;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.output = System.out;
            this.clock = Clock.defaultClock();
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
        }

        /**
         * Write to the given {@link PrintStream}.
         *
         * @param output a {@link PrintStream} instance.
         * @return {@code this}
         */
        public Builder outputTo(PrintStream output) {
            this.output = output;
            return this;
        }

        /**
         * Use the given {@link Clock} instance for the time.
         *
         * @param clock a {@link Clock} instance
         * @return {@code this}
         */
        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Builds a {@link ConsoleReporter} with the given properties.
         *
         * @return a {@link ConsoleReporter}
         */
        public ConsoleReporter build() {
            return new ConsoleReporter(registry,
                                       output,
                                       clock,
                                       rateUnit,
                                       durationUnit,
                                       filter);
        }
    }

    private static final int CONSOLE_WIDTH = 80;

    private final PrintStream output;
    private final Clock clock;

    private ConsoleReporter(MetricRegistry registry,
                            PrintStream output,
                            Clock clock,
                            TimeUnit rateUnit,
                            TimeUnit durationUnit,
                            MetricFilter filter) {
        super(registry, "console-reporter", filter, rateUnit, durationUnit);
        this.output = output;
        this.clock = clock;
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {
        final String dateTime = SystemUtils.local(new Date(clock.getTime()));
        printWithBanner(dateTime, '=');
        output.println();

        if (!gauges.isEmpty()) {
            printWithBanner("-- Gauges", '-');
            for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
                output.println(entry.getKey());
                printGauge(entry);
            }
            output.println();
        }

        if (!counters.isEmpty()) {
            printWithBanner("-- Counters", '-');
            for (Map.Entry<String, Counter> entry : counters.entrySet()) {
                output.println(entry.getKey());
                printCounter(entry);
            }
            output.println();
        }

        if (!histograms.isEmpty()) {
            printWithBanner("-- Histograms", '-');
            for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
                output.println(entry.getKey());
                printHistogram(entry.getValue());
            }
            output.println();
        }

        if (!meters.isEmpty()) {
            printWithBanner("-- Meters", '-');
            for (Map.Entry<String, Meter> entry : meters.entrySet()) {
                output.println(entry.getKey());
                printMeter(entry.getValue());
            }
            output.println();
        }

        if (!timers.isEmpty()) {
            printWithBanner("-- Timers", '-');
            for (Map.Entry<String, Timer> entry : timers.entrySet()) {
                output.println(entry.getKey());
                printTimer(entry.getValue());
            }
            output.println();
        }

        output.println();
        output.flush();
    }

    private void printMeter(Meter meter) {
        printf("             count = %d%n", meter.getCount());
        printf("         mean rate = %2.2f events/%s%n", convertRate(meter.getMeanRate()), getRateUnit());
        printf("     1-minute rate = %2.2f events/%s%n", convertRate(meter.getOneMinuteRate()), getRateUnit());
        printf("     5-minute rate = %2.2f events/%s%n", convertRate(meter.getFiveMinuteRate()), getRateUnit());
        printf("    15-minute rate = %2.2f events/%s%n", convertRate(meter.getFifteenMinuteRate()), getRateUnit());
    }

    private void printCounter(Map.Entry<String, Counter> entry) {
        printf("             count = %d%n", entry.getValue().getCount());
    }

    private void printGauge(Map.Entry<String, Gauge> entry) {
        printf("             value = %s%n", String.valueOf(entry.getValue().getValue()));
    }

    private void printHistogram(Histogram histogram) {
        printf("             count = %d%n", histogram.getCount());
        Snapshot snapshot = histogram.getSnapshot();
        printf("               min = %d%n", snapshot.getMin());
        printf("               max = %d%n", snapshot.getMax());
        printf("              mean = %2.2f%n", snapshot.getMean());
        printf("            stddev = %2.2f%n", snapshot.getStdDev());
        printf("            median = %2.2f%n", snapshot.getMedian());
        printf("              75%% <= %2.2f%n", snapshot.get75thPercentile());
        printf("              95%% <= %2.2f%n", snapshot.get95thPercentile());
        printf("              98%% <= %2.2f%n", snapshot.get98thPercentile());
        printf("              99%% <= %2.2f%n", snapshot.get99thPercentile());
        printf("            99.9%% <= %2.2f%n", snapshot.get999thPercentile());
    }

    private void printTimer(Timer timer) {
        final Snapshot snapshot = timer.getSnapshot();
        printf("             count = %d%n", timer.getCount());
        printf("         mean rate = %2.2f calls/%s%n", convertRate(timer.getMeanRate()), getRateUnit());
        printf("     1-minute rate = %2.2f calls/%s%n", convertRate(timer.getOneMinuteRate()), getRateUnit());
        printf("     5-minute rate = %2.2f calls/%s%n", convertRate(timer.getFiveMinuteRate()), getRateUnit());
        printf("    15-minute rate = %2.2f calls/%s%n", convertRate(timer.getFifteenMinuteRate()), getRateUnit());

        printf("               min = %2.2f %s%n", convertDuration(snapshot.getMin()), getDurationUnit());
        printf("               max = %2.2f %s%n", convertDuration(snapshot.getMax()), getDurationUnit());
        printf("              mean = %2.2f %s%n", convertDuration(snapshot.getMean()), getDurationUnit());
        printf("            stddev = %2.2f %s%n", convertDuration(snapshot.getStdDev()), getDurationUnit());
        printf("            median = %2.2f %s%n", convertDuration(snapshot.getMedian()), getDurationUnit());
        printf("              75%% <= %2.2f %s%n", convertDuration(snapshot.get75thPercentile()), getDurationUnit());
        printf("              95%% <= %2.2f %s%n", convertDuration(snapshot.get95thPercentile()), getDurationUnit());
        printf("              98%% <= %2.2f %s%n", convertDuration(snapshot.get98thPercentile()), getDurationUnit());
        printf("              99%% <= %2.2f %s%n", convertDuration(snapshot.get99thPercentile()), getDurationUnit());
        printf("            99.9%% <= %2.2f %s%n", convertDuration(snapshot.get999thPercentile()), getDurationUnit());
    }

    private void printWithBanner(String s, char c) {
        output.print(s);
        output.print(' ');
        for (int i = 0; i < (CONSOLE_WIDTH - s.length() - 1); i++) {
            output.print(c);
        }
        output.println();
    }

    // Quick hack
    private String format2_2f(final double p1) {
        String num = String.valueOf(((int) (p1 * 100))/100.0);
        final int index = num.indexOf('.');
        if (index < 0) {
            num += ".00";
        } else {
            if (num.length() - index > 3) {
                num = num.substring(0, index+3);
            }
            while (num.length() - index < 3) {
                num += "0";
            }
        }
        if (index == 0) {
            num = " 0"+num;
        }
        if (index == 1) {
            num = " "+num;
        }
        return num;
    }
    private void printf(String pattern, double p1) {
        final String num = format2_2f(p1);
        output.print(pattern.replace("%2.2f", num).replace("%n", "\n"));
    }
    private void printf(String pattern, long p1) {
        final String num = String.valueOf(p1);
        output.print(pattern.replace("%d", num).replace("%n", "\n"));
    }
    private void printf(String pattern, double p1, String p2) {
        final String num = format2_2f(p1);
        output.print(pattern.replace("%2.2f", num).replace("%s", p2).replace("%n", "\n"));
    }
    private void printf(String pattern, String p1) {
        output.print(pattern.replace("%s", p1).replace("%n", "\n"));
    }
}
