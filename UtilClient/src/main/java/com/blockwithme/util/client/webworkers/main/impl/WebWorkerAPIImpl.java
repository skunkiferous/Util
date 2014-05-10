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
package com.blockwithme.util.client.webworkers.main.impl;

import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.impl.AbstractWebWorkerImpl;

import elemental.html.Worker;

/** The WebWorker API implementation, on the "main thread" side. */
public class WebWorkerAPIImpl extends AbstractWebWorkerImpl<Worker> {
    /** The constructor */
    public WebWorkerAPIImpl(final Worker worker,
            final WebWorkerListener listener, final String toString) {
        super(worker, listener, toString);
        worker.setOnmessage(this);
        worker.setOnerror(this);
    }

    @Override
    public final void close() {
        worker.terminate();
    }
}