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
package com.blockwithme.client;

import com.blockwithme.util.client.webworkers.WebWorker;
import com.blockwithme.util.client.webworkers.WebWorkerListener;
import com.blockwithme.util.client.webworkers.main.WebWorkerFacade;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import elemental.html.Window;
import elemental.html.Worker;

public class WebApp implements EntryPoint {

    private static native Window getWindow()
    /*-{
     return $wnd;
     }-*/;

    @Override
    public void onModuleLoad() {
        final Button startButton = new Button("Send msg to worker");
        RootPanel.get("container1").add(startButton);

        final Window window = getWindow();//elemental.client.Browser.getWindow();

        final WebWorker<Worker> worker = WebWorkerFacade.newWorker(
                "sampleworker", new WebWorkerListener() {
                    @Override
                    public void onMessage(final String channel,
                            final JSONObject message) {
                        final Label l = new Label("Worker says: " + channel
                                + " => " + message);
                        RootPanel.get("container2").insert(l, 0);
                    }
                });

        startButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                try {
                    worker.postMessage(null, "Hello worker");
                } catch (final Exception e) {
                    window.alert("Error message from worker: " + e);
                }
            }
        });
    }
}
