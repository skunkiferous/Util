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
package com.blockwithme.util.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.blockwithme.util.base.SystemUtils;
import com.google.gwt.logging.impl.StackTracePrintStream;

/**
 * Formats LogRecords into 2 lines of text.
 *
 * Note that each Logger has it's own Formatter. This is good because
 * we can buffer for a specific Logger.
 *
 * @author monster
 */
public class UtilLogFormatter extends Formatter {
    /** Did we already say SystemUtils is not ready? */
    private static boolean saidNotReady;

    /** Buffer until SystemUtils is initialized. */
    private ArrayList<LogRecord> tempBuf;

    @Override
    public String format(final LogRecord event) {
        if (SystemUtils.getImplementation() == null) {
            if (!saidNotReady) {
                saidNotReady = true;
                return "SystemUtils NOT READY YET; buffering log events...";
            }
            if (tempBuf == null) {
                tempBuf = new ArrayList<>();
            }
            tempBuf.add(event);
            return "";
        } else {
            if (tempBuf != null) {
                final ArrayList<LogRecord> tempBuf2 = tempBuf;
                tempBuf = null;
                for (final LogRecord buffered : tempBuf2) {
                    Logger.getLogger(buffered.getLoggerName()).log(buffered);
                }
                // We have to repost the current event too, otherwise it
                // will come out before the earlier ones.
                Logger.getLogger(event.getLoggerName()).log(event);
                return "";
            }
            return doFormat(event);
        }
    }

    private String doFormat(final LogRecord event) {
        // TODO Once we have added real thread names in WebWorkers,
        // We should output it here too.
        final StringBuilder message = new StringBuilder();
        final Date date = new Date(event.getMillis());
        message.append(SystemUtils.utc(date));
        message.append(" ");
        message.append(event.getLevel().getName());
        message.append(" ");
        message.append(event.getLoggerName());
        message.append(" ");
        message.append(event.getMessage());
        final Throwable t = event.getThrown();
        if (t != null) {
            t.printStackTrace(new StackTracePrintStream(message));
        }
        return message.toString();
    }
}
