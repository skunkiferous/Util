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
package com.blockwithme.util.server;

import static com.blockwithme.util.shared.SystemUtils.CURRENT_TIME_MILLIS_UPDATE_INTERVAL;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.blockwithme.util.shared.Application;
import com.blockwithme.util.shared.DefaultApplication;
import com.blockwithme.util.shared.DefaultTimeSource;
import com.blockwithme.util.shared.SystemUtils;
import com.blockwithme.util.shared.TimeSource;
import com.google.inject.AbstractModule;

/**
 * Guice module for Util-Server.
 *
 * @author monster
 */
public class UtilServerModule extends AbstractModule {

    /** The default loop duration in milliseconds. */
    private static final long APPLICATION_LOOP = 20;

    /** Logger */
    private static final Logger LOG = Logger.getLogger(UtilServerModule.class
            .getName());

    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        final Application app = newApplication();
        final Timer timer = newTimer();
        final TimeSource timeSource = newTimeSource();
        final SystemUtils systemUtils = newSystemUtils();
        final Random random = newRandom();

        bind(TimeSource.class).toInstance(timeSource);
        bind(Application.class).toInstance(app);
        bind(SystemUtils.class).toInstance(systemUtils);
        bind(Timer.class).toInstance(timer);
        bind(Random.class).toInstance(random);

        setupApplicationLoop(app, timer);
        setupTimeUpdater();
    }

    /** Creates the Application. */
    protected Application newApplication() {
        return new DefaultApplication();
    }

    /** Creates the Timer. */
    protected Timer newTimer() {
        return new Timer("System-Timer");
    }

    /** Creates the TimeSource. */
    protected TimeSource newTimeSource() {
        return new DefaultTimeSource();
    }

    /** Creates the SystemUtils. */
    protected SystemUtils newSystemUtils() {
        return new DefaultSystemUtilsImpl();
    }

    /** Creates the Random. */
    protected Random newRandom() {
        return new Random();
    }

    /** Sets up the application loop. */
    protected void setupApplicationLoop(final Application app, final Timer timer) {
        timer.scheduleAtFixedRate((TimerTask) app, APPLICATION_LOOP,
                APPLICATION_LOOP);
    }

    /**
     * We need to make sure the current time gets updated.
     * And we do not use Timer or similar, because the "other tasks"
     * would cause large fluctuations in the update rate.
     */
    protected void setupTimeUpdater() {
        final Thread timeUpdater = new Thread("Time Updater") {
            @Override
            public void run() {
                // Wait until TimeSource is installed...
                while (SystemUtils.getTimeSource() == null) {
                    try {
                        sleep((long) CURRENT_TIME_MILLIS_UPDATE_INTERVAL);
                    } catch (final InterruptedException e) {
                        LOG.warning("Time Updater interrupted; stopping");
                        return;
                    }
                }

                double nextTime = SystemUtils.updateCurrentTimeMillis()
                        + CURRENT_TIME_MILLIS_UPDATE_INTERVAL;
                while (true) {
                    final double now = SystemUtils.updateCurrentTimeMillis();
                    final double sleep = nextTime - now;
                    nextTime += CURRENT_TIME_MILLIS_UPDATE_INTERVAL;
                    if (sleep >= 1) {
                        try {
                            sleep((long) sleep);
                        } catch (final InterruptedException e) {
                            LOG.warning("Time Updater interrupted; stopping");
                            return;
                        }
                    } else {
                        // In case of overload or "too long" sleep ...
                        while (nextTime <= now) {
                            nextTime += CURRENT_TIME_MILLIS_UPDATE_INTERVAL;
                        }
                    }
                }
            }
        };
        timeUpdater.setDaemon(true);
        timeUpdater.start();
    }
}
