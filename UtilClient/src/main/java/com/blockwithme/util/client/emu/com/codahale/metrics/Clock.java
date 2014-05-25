package com.codahale.metrics;

/**
 * An abstraction for how time passes. It is passed to {@link Timer} to track timing.
 */
public abstract class Clock {

    /** Initialize JavaScript high resolution time. */
    private static native void initHighResTime()
    /*-{
		Date.now = Date.now || function() {
			return new Date().getTime();
		};
		window.performance = window.performance || {};
		performance.now = (function() {
			return performance.now || performance.mozNow || performance.msNow
					|| performance.oNow || performance.webkitNow || Date.now;
		})();
    }-*/;

    static {
	 initHighResTime();
    }

    /**
     * Returns the current time tick.
     *
     * @return time tick in nanoseconds
     */
    public abstract long getTick();

    /**
     * Returns the current time in milliseconds.
     *
     * @return time in milliseconds
     */
    public long getTime() {
        return System.currentTimeMillis();
    }

    private static final Clock DEFAULT = new UserTimeClock();

    /**
     * The default clock to use.
     *
     * @return the default {@link Clock} instance
     *
     * @see Clock.UserTimeClock
     */
    public static Clock defaultClock() {
        return DEFAULT;
    }

    /**
     * A clock implementation which returns the current time in epoch nanoseconds.
     */
    public static class UserTimeClock extends Clock {

        private static native double highResTimeMillisImpl()
        /*-{
		return performance.now();
        }-*/;

        @Override
        public long getTick() {
            return (long) (highResTimeMillisImpl() * 1000000L);
        }
    }
}
