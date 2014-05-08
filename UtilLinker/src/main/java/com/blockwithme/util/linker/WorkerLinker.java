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
package com.blockwithme.util.linker;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.linker.D8ScriptLinker;

/**
 * This linker removes unnecessary GWT stuff to make the generated JS work
 * inside HTML5 web worker
 */
@LinkerOrder(LinkerOrder.Order.PRIMARY)
public class WorkerLinker extends D8ScriptLinker {
    @Override
    protected String generateSelectionScript(final TreeLogger logger,
            final LinkerContext context, final ArtifactSet artifacts)
            throws UnableToCompleteException {
        String result = super.generateSelectionScript(logger, context,
                artifacts);
        final int start = result.indexOf("window.Object");
        result = "var load = importScripts;\n" //
                + "window = self;\n" //
                + "window.document = self;\n" //
                + "window.write = function(txt) {\n" //
                + "  var start = txt.search('src=')+5;\n" //
                + "  var end = txt.search('><\\/script>')-1;\n" //
                + "  var file = txt.substring(start, end);\n" //
                + "  load(file);\n" //
                + "};\n" //
                + "console = {};\n" //
                + "function log(level,msg) { self.postMessage({'_channel_':'java.util.logging','level':level,'loggerName':'global','message':msg,'millis':(new Date()).getTime()}); };\n" //
                + "console.info = function(msg) { log('INFO',msg); };\n" //
                + "console.error = function(msg) { log('SEVERE',msg); };\n" //
                + "var print = console.info;\n" //
                + result.substring(start).replace("console = {};", "");
        return result;
    }
}
