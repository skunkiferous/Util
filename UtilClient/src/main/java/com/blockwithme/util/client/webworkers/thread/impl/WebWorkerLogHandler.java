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

import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.blockwithme.util.client.webworkers.WebWorker;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import elemental.js.html.JsDedicatedWorkerGlobalScope;

/**
 * Forward the logging messages to the main thread.
 *
 * TODO Have the main thread specify the default log level,
 *      so we drop message that are not logged anyway.
 * TODO Add buffering, with a timer calling flush regularly.
 *
 * @author monster
 */
public class WebWorkerLogHandler extends Handler {
    private static class JsonLogRecordThrowable extends Throwable {
        /**  */
        private static final long serialVersionUID = -5989043208781005256L;
        private final String type;

        public JsonLogRecordThrowable(final String type, final String message) {
            super(message);
            this.type = type;
        }

        @Override
        public Throwable fillInStackTrace() {
            return this;
        }

        @Override
        public String toString() {
            return getMessage() != null ? type + ": " + getMessage() : type;
        }

    }

    /** Points to the main thread. */
    private final WebWorker<JsDedicatedWorkerGlobalScope> worker;

    /** Constructor */
    public WebWorkerLogHandler(
            final WebWorker<JsDedicatedWorkerGlobalScope> worker2) {
        this.worker = Objects.requireNonNull(worker2, "worker");
        setLevel(Level.ALL);
    }

    /* (non-Javadoc)
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     */
    @Override
    public void publish(final LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        final Level level = record.getLevel();
        final String loggerName = record.getLoggerName();
        final String message = record.getMessage();
        final long millis = record.getMillis();
        final Throwable thrown = record.getThrown();
        final JSONObject msg = new JSONObject();
        if (level != null) {
            msg.put("level", new JSONString(level.toString()));
        }
        if (loggerName != null) {
            msg.put("loggerName", new JSONString(loggerName));
        }
        if (message != null) {
            msg.put("message", new JSONString(message));
        }
        if (millis != 0) {
            msg.put("millis", new JSONNumber(millis));
        }
        if (thrown != null) {
            msg.put("thrown_type", new JSONString(thrown.getClass().getName()));
            final String thrownMessage = thrown.getMessage();
            if (thrownMessage != null) {
                msg.put("thrown_msg", new JSONString(thrownMessage));
            }
            // Forget about the Stack trace ...
        }
        worker.postMessage("java.util.logging", msg);
    }

    /** Reads LogRecord from a JSONObject. */
    public static LogRecord fromJSONObject(final JSONObject json) {
        if (json == null) {
            return null;
        }
        Level level = Level.INFO;
        String loggerName = "";
        String message = null;
        long millis = 0;
        String thrown_type = null;

        final JSONValue levelValue = json.get("level");
        if ((levelValue != null) && (levelValue.isString() != null)) {
            try {
                level = Level.parse(levelValue.isString().stringValue());
            } catch (final Throwable t) {
                // NOP
            }
        }
        final JSONValue loggerNameValue = json.get("loggerName");
        if ((loggerNameValue != null) && (loggerNameValue.isString() != null)) {
            loggerName = loggerNameValue.isString().stringValue();
        }
        final JSONValue messageValue = json.get("message");
        if ((messageValue != null) && (messageValue.isString() != null)) {
            message = messageValue.isString().stringValue();
        }
        final JSONValue millisValue = json.get("millis");
        if ((millisValue != null) && (millisValue.isNumber() != null)) {
            millis = (long) millisValue.isNumber().doubleValue();
        }
        final JSONValue thrown_typeValue = json.get("thrown_type");
        if ((thrown_typeValue != null) && (thrown_typeValue.isString() != null)) {
            thrown_type = thrown_typeValue.isString().stringValue();
        }

        final LogRecord result = new LogRecord(level, message);
        result.setLoggerName(loggerName);
        result.setMillis(millis);
        if (thrown_type != null) {
            final JSONValue thrown_msgValue = json.get("thrown_msg");
            String thrown_msg = null;
            if ((thrown_msgValue != null)
                    && (thrown_msgValue.isString() != null)) {
                thrown_msg = thrown_msgValue.isString().stringValue();
            }
            result.setThrown(new JsonLogRecordThrowable(thrown_type, thrown_msg));
        } else if ((message == null) || message.isEmpty()) {
            return null;
        }

        return result;
    }

    /* (non-Javadoc)
     * @see java.util.logging.Handler#flush()
     */
    @Override
    public void flush() {
        // NOP
    }

    /* (non-Javadoc)
     * @see java.util.logging.Handler#close()
     */
    @Override
    public void close() {
        // NOP
    }
}
