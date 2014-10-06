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
package com.blockwithme.util.client.webworkers.thread.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.google.gwt.logging.impl.LoggerImplNull;

/**
 * Web-Worker LoggerImpl, delegates all messages to the main thread.
 *
 * @author monster
 */
public class WebWorkerLoggerImpl extends LoggerImplNull {
    private static class MyLogger extends Logger {
        public MyLogger(final String name) {
            super(name, null);
        }
    }

    private static final Handler[] NO_HANDLER = new Handler[0];

    private static LogManager MANAGER;

    private Level level;

    private String name;

    private Logger parent;

    private boolean useParentHandlers = true;

    private Handler[] handlers = NO_HANDLER;

    @Override
    public Level getLevel() {
        Level result = level;
        if (result == null) {
            Logger p = parent;
            while ((result == null) && (p != null)) {
                result = p.getLevel();
                p = p.getParent();
            }
            if (result == null) {
                result = Level.INFO;
            }
            level = result;
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Logger getParent() {
        return parent;
    }

    @Override
    public boolean getUseParentHandlers() {
        return useParentHandlers;
    }

    @Override
    public void setLevel(final Level newLevel) {
        level = newLevel;
    }

    @Override
    public void setName(final String newName) {
        name = newName;
    }

    @Override
    public void setParent(final Logger newParent) {
        parent = newParent;
    }

    @Override
    public void setUseParentHandlers(final boolean newUseParentHandlers) {
        useParentHandlers = newUseParentHandlers;
    }

    @Override
    public void config(final String msg) {
        log(Level.CONFIG, msg, null);
    }

    @Override
    public void fine(final String msg) {
        log(Level.FINE, msg, null);
    }

    @Override
    public void finer(final String msg) {
        log(Level.FINER, msg, null);
    }

    @Override
    public void finest(final String msg) {
        log(Level.FINEST, msg, null);
    }

    @Override
    public void severe(final String msg) {
        log(Level.SEVERE, msg, null);
    }

    @Override
    public void warning(final String msg) {
        log(Level.WARNING, msg, null);
    }

    @Override
    public void info(final String msg) {
        log(Level.INFO, msg, null);
    }

    @Override
    public void log(final Level level, final String msg) {
        log(level, msg, null);
    }

    @Override
    public void addHandler(final Handler handler) {
        final ArrayList<Handler> list = new ArrayList<Handler>(
                Arrays.asList(handlers));
        if (!list.contains(handler)) {
            list.add(handler);
            handlers = list.toArray(new Handler[list.size()]);
        }
    }

    @Override
    public void removeHandler(final Handler handler) {
        final ArrayList<Handler> list = new ArrayList<Handler>(
                Arrays.asList(handlers));
        if (list.remove(handler)) {
            handlers = list.toArray(new Handler[list.size()]);
        }
    }

    @Override
    public Handler[] getHandlers() {
        return handlers;
    }

    @Override
    public void log(final Level level, final String msg, final Throwable thrown) {
        final LogRecord record = new LogRecord(level, msg);
        record.setLoggerName(name);
        record.setThrown(thrown);
        record.setMillis(System.currentTimeMillis());
        log(record);
    }

    @Override
    public boolean isLoggable(final Level messageLevel) {
        if (messageLevel == null) {
            throw new NullPointerException("messageLevel");
        }
        return getLevel().intValue() <= messageLevel.intValue();
    }

    @Override
    public void log(final LogRecord record) {
        if (isLoggable(record.getLevel())) {
            Handler[] h = getHandlers();
            if (useParentHandlers) {
                Logger p = parent;
                while ((h.length == 0) && (p != null)) {
                    h = p.getHandlers();
                    p = p.getParent();
                }
            }
            if (h.length != 0) {
                for (final Handler handler : h) {
                    handler.publish(record);
                }
            }
        }
    }

    @Override
    public Logger getLoggerHelper(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (MANAGER == null) {
            MANAGER = LogManager.getLogManager();
        }
        Logger result = MANAGER.getLogger(name);
        if (result == null) {
            result = new MyLogger(name);
            if (!MANAGER.addLogger(result)) {
                result = MANAGER.getLogger(name);
            }
        }
        return result;
    }
}
