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
package com.blockwithme.util.client.linker;

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
                + "window.write = function(txt) {\n" // Assumes we want to load a script.
                + "  var start = txt.search('src=');\n" // No point in loading HTML ... in a WebWorker.
                + "  var end = txt.search('><\\/script>');\n" //
                + "  if ((start != -1) && (end != -1)) {\n" //
                + "    importScripts(txt.substring(start+5, end-1));\n" //
                + "  } else {\n" //
                + "    console.error('Cannot parse window.write('+txt+')');\n;" //
                + "  };\n" //
                + "};\n" //
                + "function log(level,msg) { self.postMessage({'_channel_':'java.util.logging','level':level,'loggerName':'global','message':msg,'millis':(new Date()).getTime()}); };\n" //
                + "console = {};\n" //
                + "console.debug = function(msg) { log('FINE',msg); };\n" //
                + "console.info = function(msg) { log('INFO',msg); };\n" //
                + "console.warn = function(msg) { log('WARNING',msg); };\n" //
                + "console.error = function(msg) { log('SEVERE',msg); };\n" //
                + "console.group = function(grp) {};\n" //
                + "console.groupCollapsed = function(grp) {};\n" //
                + "console.groupEnd = function() {};\n" //
                + "console.exception = console.error;\n" //
                + "console.time = function(name) { log('INFO','START:'+name); };\n" //
                + "console.timeEnd = function(name) { log('INFO','END:'+name); };\n" //
                + "console.trace = function() { console.info(Error().stack); };\n" //
                + "var print = console.debug;\n" //
                + result.substring(start).replace("console = {};", "");
        return result;
    }
}
