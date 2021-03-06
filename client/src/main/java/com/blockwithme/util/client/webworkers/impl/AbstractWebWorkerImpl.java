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
package com.blockwithme.util.client.webworkers.impl;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.client.webworkers.WebWorker;
import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.thread.impl.WebWorkerLogHandler;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import elemental.events.ErrorEvent;
import elemental.events.EventListener;
import elemental.events.MessageEvent;

/** The abstract WebWorker implementation */
public abstract class AbstractWebWorkerImpl<WORKER> implements
        WebWorker<WORKER>, EventListener {
    /** The real web worker. */
    protected final WORKER worker;

    /** The listener */
    protected final WebWorkerListener listener;

    /** toString value */
    private final String name;

    /** The Logger */
    private final Logger logger;

    /** The constructor */
    public AbstractWebWorkerImpl(final WORKER worker,
            final WebWorkerListener listener, final String name) {
        Objects.requireNonNull(worker, "worker");
        Objects.requireNonNull(listener, "listener");
        Objects.requireNonNull(name, "name");
        this.worker = worker;
        this.listener = listener;
        this.name = name;
        logger = Logger.getLogger(name);
    }

    @Override
    public final WORKER getWorker() {
        return worker;
    }

    private static native String stringify(Object jso)
    /*-{
		return JSON.stringify(jso);
    }-*/;

    @Override
    public final void handleEvent(final elemental.events.Event evt) {
        if (evt instanceof MessageEvent) {
            final Object data = ((MessageEvent) evt).getData();
            if (data == null) {
                logger.severe("got MessageEvent with null data");
            } else {
                try {
                    JSONObject message = new JSONObject((JavaScriptObject) data);
                    if (message.isObject() == null) {
                        logger.log(Level.SEVERE, "Problem parsing data: "
                                + stringify(data) + " : data not a JSONObject");
                    } else {
                        if (message.containsKey("jsObject")) {
                            final JSONValue jsObject = message.get("jsObject");
                            if (jsObject.isObject() != null) {
                                message = jsObject.isObject();
                            } else {
                                logger.log(
                                        Level.SEVERE,
                                        "Problem parsing data: "
                                                + stringify(data)
                                                + " : data.jsObject not a JSONObject");
                                message = null;
                            }
                        }
                        if (message != null) {
                            // Get and remove channel at the same time
                            final JSONValue channel = message.put("_channel_",
                                    null);
                            String ch = "";
                            if ((channel != null)
                                    && (channel != JSONNull.getInstance())) {
                                final JSONString str = channel.isString();
                                if (str != null) {
                                    // Cannot be null
                                    ch = str.stringValue();
                                } else {
                                    ch = stringify(channel);
                                }
                            }
                            if ("java.util.logging".equals(ch)) {
                                final LogRecord record = WebWorkerLogHandler
                                        .fromJSONObject(message);
                                if (record != null) {
                                    setThreadName(record, name);
                                    Logger.getLogger(record.getLoggerName())
                                            .log(record);
                                }
                            } else if ("set.thread.name".equals(ch)) {
                                final String name = message.get("value")
                                        .isString().stringValue();
                                setThreadName(Thread.currentThread(), name);
                            } else {
                                listener.onMessage(ch, message);
                            }
                        }
                    }
                } catch (final Throwable t) {
                    logger.log(Level.SEVERE, "Problem parsing data: "
                            + stringify(data), t);
                }
            }
        } else if (evt instanceof ErrorEvent) {
            final ErrorEvent errEvt = (ErrorEvent) evt;
            final String filename = errEvt.getFilename();
            final int lineno = errEvt.getLineno();
            final String message = errEvt.getMessage();
            logger.severe(filename + ":" + lineno + ": " + message);
        } else if (evt != null) {
            logger.severe("Cannot handle message of type " + evt.getClass()
                    + " : " + stringify(evt));
        }
    }

    private static native void postMessage2(final Object worker,
            final JavaScriptObject message)
    /*-{
		worker.postMessage(message);
    }-*/;

    private static native void setThreadName(final Object object,
            final String threadName)
    /*-{
		object.threadName = threadName;
    }-*/;

    @Override
    public final void postMessage(final String channel, final JSONObject message) {
        final String ch = (channel == null) ? "" : channel;
        if (message != null) {
            message.put("_channel_", new JSONString(ch));
            try {
                postMessage2(worker, message.getJavaScriptObject());
            } finally {
                message.put("_channel_", null);
            }
        } else {
            // We don't send null messages!
            throw new NullPointerException("message");
        }
    }

    @Override
    public final void postMessage(final String channel, final String value) {
        if (value != null) {
            final JSONObject message = new JSONObject();
            message.put("value", new JSONString(value));
            postMessage(channel, message);
        } else {
            // We don't send null messages!
            throw new NullPointerException("message");
        }
    }

    @Override
    public final void postMessage(final String channel, final double value) {
        final JSONObject message = new JSONObject();
        message.put("value", new JSONNumber(value));
        postMessage(channel, message);
    }

    @Override
    public final void postMessage(final String channel, final long value) {
        final JSONObject message = new JSONObject();
        if ((value <= SystemUtils.MAX_DOUBLE_INT_VALUE)
                && (value >= SystemUtils.MIN_DOUBLE_INT_VALUE)) {
            message.put("value", new JSONNumber(value));
        } else {
            message.put("value", new JSONString("0x" + Long.toHexString(value)));
        }
        postMessage(channel, message);
    }

    @Override
    public final void postMessage(final String channel, final boolean value) {
        final JSONObject message = new JSONObject();
        message.put("value", JSONBoolean.getInstance(value));
        postMessage(channel, message);
    }

    @Override
    public final String toString() {
        return name;
    }
}