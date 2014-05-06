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

import com.blockwithme.util.client.webworkers.WebWorker;
import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.thread.ThreadFacade;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONObject;

import elemental.js.html.JsDedicatedWorkerGlobalScope;

public final class SampleWorker implements EntryPoint, WebWorkerListener {

    private WebWorker<JsDedicatedWorkerGlobalScope> worker;

    // Export the brige method when the application is loaded
    @Override
    public void onModuleLoad() {
        worker = ThreadFacade.newWorker(this);
        worker.postMessage("info", "SampleWorker setup");
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.client.webworkers.WebWorkerListener#onMessage(java.lang.String,java.lang.Object)
     */
    @Override
    public void onMessage(final String channel, final Object data) {
        worker.postMessage("debug", data);
        try {
            worker.postMessage(null, "----====----");
            worker.postMessage(null,
                    "Current timestamp " + System.currentTimeMillis());
            worker.postMessage(null, "Received message \"" + data + "\"");
        } catch (final Exception e) {
            worker.postMessage("error", e);
            throw new RuntimeException("worker: "
                    + new JSONObject(worker.getWorker()).toString(), e);
        }
    }
}
