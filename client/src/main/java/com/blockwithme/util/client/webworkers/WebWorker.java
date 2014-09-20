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
package com.blockwithme.util.client.webworkers;

import com.google.gwt.json.client.JSONObject;

/**
 * Simplified interface representing a WebWorker.
 *
 * It is used on "both sides"; from the "main thread", and from the
 * "background threads".
 *
 * @author monster
 */
public interface WebWorker<WORKER> {
    /**
     * Sends a message to the worker's inner scope. This accepts a single parameter, which is the data to send to the worker. The data may be any value or JavaScript object that does not contain functions or cyclical references (since the object is converted to <a class="internal" title="En/JSON" rel="internal" href="https://developer.mozilla.org/en/JSON">JSON</a> internally).
     *
     * @param channel The the "channel" to which this message is sent. Default channel is null.
     * @param message The object to deliver to the worker; this will be in the data field in the event delivered to the <code>onmessage</code> handler.
     */
    void postMessage(String channel, JSONObject message);

    /**
     * Sends a message to the worker's inner scope. This accepts a single parameter, which is the data to send to the worker.
     *
     * @param channel The the "channel" to which this message is sent. Default channel is null.
     * @param value The message to deliver to the worker; this will be in the data field {'value':value} in the event delivered to the <code>onmessage</code> handler.
     */
    void postMessage(String channel, String value);

    /**
     * Sends a message to the worker's inner scope. This accepts a single parameter, which is the data to send to the worker.
     *
     * @param channel The the "channel" to which this message is sent. Default channel is null.
     * @param value The message to deliver to the worker; this will be in the data field {'value':value} in the event delivered to the <code>onmessage</code> handler.
     */
    void postMessage(String channel, double value);

    /**
     * Sends a message to the worker's inner scope. This accepts a single parameter, which is the data to send to the worker.
     * Note that Java "long" is problematic. Not all of it's possible values can be represented correctly as a JSON number.
     * Therefore, for those non-representable values, an hexadecimal String is used (0x...).
     *
     * @param channel The the "channel" to which this message is sent. Default channel is null.
     * @param value The message to deliver to the worker; this will be in the data field {'value':value} in the event delivered to the <code>onmessage</code> handler.
     */
    void postMessage(String channel, long value);

    /**
     * Sends a message to the worker's inner scope. This accepts a single parameter, which is the data to send to the worker.
     *
     * @param channel The the "channel" to which this message is sent. Default channel is null.
     * @param value The message to deliver to the worker; this will be in the data field {'value':value} in the event delivered to the <code>onmessage</code> handler.
     */
    void postMessage(String channel, boolean value);

    /**
     * Immediately terminates the worker. This does not offer the worker an opportunity to finish its operations; it is simply stopped at once.
     */
    void close();

    /**
     * Returns the actual Worker.
     *
     * Note that a default handler for error and messages has already been registered, that delegates to the <code>WebWorkerListener</code> passed to the <code>WorkerFacade</code>.
     */
    WORKER getWorker();
}
