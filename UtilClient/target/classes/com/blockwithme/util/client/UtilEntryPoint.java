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

import java.util.logging.Handler;
import java.util.logging.Logger;

import com.blockwithme.util.base.SystemUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * The Util module EntryPoint.
 *
 * We need this in addition to GIN, since the GIN modules will
 * NOT BE EXECUTED ON THE CLIENT.
 *
 * @author monster
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

            setupRootLogger();
            setupSystemErrOut();
            setupUncaughtExceptionHandler();
            startCurrentTimeMillisUpdate();

            // This should come out as an "info" log message.
            System.out.println("[System.out] Util Module initialized");
            final Logger log = Logger.getLogger(getClass().getName());
            log.info("[log.info] Util Module initialized");
        }
    }

    /** Specifies our custom "Logging" Streams for System.err/System.out */
    private void setupSystemErrOut() {
        System.setOut(new LoggingPrintStream(false));
        System.setErr(new LoggingPrintStream(true));
    }

    /** Setup Handler of "root logger" */
    private void setupRootLogger() {
        final Logger root = Logger.getLogger("");
        for (final Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root.addHandler(new UtilConsoleLogHandler());
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
        try {
            new com.google.gwt.user.client.Timer() {
                @Override
                public void run() {
                    SystemUtils.updateCurrentTimeMillis();
                }
            }.scheduleRepeating(SystemUtils.CURRENT_TIME_MILLIS_UPDATE_INTERVAL);
        } catch (final/*UnsatisfiedLinkError*/Error e) {
            if (!e.getClass().getSimpleName().equals("UnsatisfiedLinkError")) {
                throw e;
            }
            // GWT Compiler issue; This class is instantiated at compile time,
            // (for whatever reason, by GIN), and at that time,
            // com.google.gwt.user.client.Timer is not available, causing a
            // UnsatisfiedLinkError. But if I catch it as a UnsatisfiedLinkError,
            // then it does not compile because UnsatisfiedLinkError does not
            // exist in GWT! Argh!
        }
    }
}
