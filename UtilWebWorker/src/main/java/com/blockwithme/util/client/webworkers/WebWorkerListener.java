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

/**
 * Receives events and errors from a web-worker (or the main thread).
 *
 * @author monster
 */
public interface WebWorkerListener {
    /**
     * Called when a message is received.
     *
     * The data can be any object that can be passed to {@link WebWorker#postMessage(String,Object)}
     * Note that the object is a *copy*, and changing it will not affect the original.
     *
     * @param channel The channel on which this message was received.
     * @param message The message.
     */
    void onMessage(String channel, Object message);
}
