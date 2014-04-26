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
package com.blockwithme.util.client;

import java.util.logging.Logger;

import com.blockwithme.util.shared.SystemUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.ConsoleLogHandler;

/**
 * @author monster
 *
 */
public class UtilEntryPoint implements EntryPoint {

    /** Loaded? */
    private static boolean loaded;

    /* (non-Javadoc)
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {
        if (!loaded) {
            loaded = true;
            setupSystemErrOut();
            setupRootLogger();
            setupUncaughtExceptionHandler();
            startCurrentTimeMillisUpdate();

            // This should come out as an "info" log message.
            System.out.println("Util Module initialized");
        }
    }

    /** Specifies our custom "Logging" Streams for System.err/System.out */
    private void setupSystemErrOut() {
        System.setOut(new LoggingPrintStream(false));
        System.setErr(new LoggingPrintStream(true));
    }

    /** Setup Handler of "root logger" */
    private void setupRootLogger() {
        Logger.getLogger("").addHandler(new ConsoleLogHandler());
    }

    /** Setups the GWT UncaughtExceptionHandler. */
    private void setupUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
    }

    /**
     * We use a "native" GWT Timer, because we want updates independent
     * of the application (game) cycle speed. We do this, because if some
     * processing *within* the application cycle takes long, it will still
     * see the time changing (hopefully; not sure of that anymore).
     */
    private void startCurrentTimeMillisUpdate() {
        new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                SystemUtils.updateCurrentTimeMillis();
            }
        }.scheduleRepeating(SystemUtils.CURRENT_TIME_MILLIS_UPDATE_INTERVAL);
    }
}
