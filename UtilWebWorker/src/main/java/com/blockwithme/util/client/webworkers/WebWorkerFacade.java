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

import java.util.Objects;

import com.blockwithme.util.client.webworkers.impl.WebWorkerAPIImpl;

import elemental.html.Window;
import elemental.html.Worker;

/**
 * Primary implementation of a simplified Facade to using WebWorkers in GWT.
 *
 * Assumes the use of "workerlinker".
 *
 * @author monster
 */
public class WebWorkerFacade {

    private static native Window getWindow()
    /*-{
     return $wnd;
     }-*/;

    /** Creates and starts a new WebWorker from the "main thread". */
    public static WebWorker<Worker> newWorker(final String moduleName,
            final WebWorkerListener listener) {
        Objects.requireNonNull(moduleName, "moduleName");
        // TODO This should not be hard-wired.
        final String path = "../" + moduleName + "/" + moduleName
                + ".nocache.js";
        final Window window = getWindow();//elemental.client.Browser.getWindow()
        final Worker worker = window.newWorker(path);
        return new WebWorkerAPIImpl(worker, listener, path);
    }
}
