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

import com.blockwithme.util.client.webworkers.WebWorker;
import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
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
    private final String toString;

    /** The constructor */
    public AbstractWebWorkerImpl(final WORKER worker,
            final WebWorkerListener listener, final String toString) {
        Objects.requireNonNull(worker, "worker");
        Objects.requireNonNull(listener, "listener");
        Objects.requireNonNull(toString, "toString");
        this.worker = worker;
        this.listener = listener;
        this.toString = toString;
    }

    @Override
    public final WORKER getWorker() {
        return worker;
    }

    @Override
    public final void handleEvent(final elemental.events.Event evt) {
        if (evt instanceof MessageEvent) {
            final Object data = ((MessageEvent) evt).getData();
            if (data == null) {
                listener.onMessage("error", "got MessageEvent with null data");
            } else {
                try {
                    final JSONObject json = new JSONObject(
                            (JavaScriptObject) data);
                    final JSONValue channel = json.get("channel");
                    final JSONValue message = json.get("message");
                    listener.onMessage(
                            (channel == null) ? null : channel.toString(),
                            message);
                } catch (final Exception e) {
                    listener.onMessage("error", "Problem parsing data: " + data
                            + " => " + e);
                }
            }
        } else if (evt instanceof ErrorEvent) {
            // TODO Not sure if toString() does a good job here
            listener.onMessage("error", evt.toString());
        } else if (evt != null) {
            listener.onMessage("error",
                    "Cannot handle message of type " + evt.getClass() + " : "
                            + evt);
        }
    }

    private static native void postMessage2(final Object worker,
            final String channel, final Object message)
    /*-{
        // TODO When using postMessage() to pass actual logging messages
        // to the main thread, as there is no other way to get them
        // out of the web-worker, we might need to change this
        // code too.
           worker.postMessage({'channel':channel,'message':message});
       }-*/;

    @Override
    public final void postMessage(final String channel, final Object message) {
        postMessage2(worker, channel, message);
    }

    @Override
    public final String toString() {
        return toString;
    }
}