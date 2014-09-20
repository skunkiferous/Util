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

import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.impl.AbstractWebWorkerImpl;

import elemental.js.html.JsDedicatedWorkerGlobalScope;

/** The WebWorker API implementation, on the "main thread" side. */
public class WebWorkerRealImpl extends
        AbstractWebWorkerImpl<JsDedicatedWorkerGlobalScope> {

    /** "self" IS the worker, so just cast self to the right type! */
    private static native JsDedicatedWorkerGlobalScope toWorker()
    /*-{
        return self;
      }-*/;

    /** The constructor */
    public WebWorkerRealImpl(final WebWorkerListener listener) {
        super(toWorker(), listener, "worker-impl");
        worker.setOnmessage(this);
        worker.setOnerror(this);
    }

    @Override
    public final void close() {
        worker.close();
    }
}