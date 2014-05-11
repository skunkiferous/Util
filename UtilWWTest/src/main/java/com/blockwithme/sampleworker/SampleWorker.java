/************************************************************************
 * This is a part of gwtwwlinker project
 * https://github.com/tomekziel/gwtwwlinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **************************************************************************/
package com.blockwithme.sampleworker;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import com.blockwithme.util.client.webworkers.WebWorker;
import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.thread.ThreadFacade;
import com.blockwithme.util.client.webworkers.thread.impl.LogHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONObject;

import elemental.js.html.JsDedicatedWorkerGlobalScope;

public final class SampleWorker implements EntryPoint, WebWorkerListener {

    /** The Logger */
    private static final Logger LOG = Logger.getLogger("SampleWorker");

    /** The SLF4J Logger */
    private static final org.slf4j.Logger SLF4K_LOG = LoggerFactory
            .getLogger("SampleWorker");

    private WebWorker<JsDedicatedWorkerGlobalScope> worker;

    @Override
    public void onModuleLoad() {
        worker = ThreadFacade.newWorker(this);
        final Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(new LogHandler(worker));
        LOG.info("SampleWorker setup");
        SLF4K_LOG.info("SLF4K_LOG: SampleWorker setup");
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.client.webworkers.WebWorkerListener#onMessage(java.lang.String,java.lang.Object)
     */
    @Override
    public void onMessage(final String channel, final JSONObject message) {
        LOG.fine("Got message: " + message);
        try {
            worker.postMessage(null, "----====----");
            worker.postMessage(null,
                    "Current timestamp " + System.currentTimeMillis());
            worker.postMessage(null, "Received message \"" + message + "\"");
        } catch (final Exception e) {
            LOG.log(Level.SEVERE, "Failed to process message: " + message, e);
        }
    }
}
