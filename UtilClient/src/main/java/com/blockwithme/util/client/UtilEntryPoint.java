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

import java.io.PrintStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

import com.blockwithme.util.shared.SystemUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.logging.client.ConsoleLogHandler;

/**
 * Util EntryPoint.
 *
 * Sets up Logging and Console output.
 *
 * @author monster
 */
public class UtilEntryPoint implements EntryPoint {
    private static final Logger ROOT = Logger.getLogger("");

    /**
     * Used to output Console text to the root logger, so that the
     * logging system takes charge of forwarding the output to the
     * right place.
     *
     * @author monster
     */
    private static class LoggingPrintStream extends PrintStream {
        private final boolean error;
        private byte[] buffer = new byte[1024];
        private int pos;

        public LoggingPrintStream(boolean error) {
            super(null);
            this.error = error;
        }

        /**
         * Writes one byte to the target stream. Only the low order byte of the
         * integer {@code oneByte} is written.
         *
         * @param oneByte
         *            the byte to be written.
         * @throws IOException
         *             if an I/O error occurs while writing to this stream.
         */
        @Override
        public void write(int oneByte) throws IOException {
            if (oneByte == '\n') {
                flush();
            } else {
                if (pos == buffer.length) {
                    final byte[] newBuffer = new byte[buffer.length*2];
                    System.arraycopy(buffer, 0, newBuffer, 0, pos);
                    buffer = newBuffer;
                }
                buffer[pos++] = (byte) oneByte;
            }
        }

        @Override
        public void flush() {
            if (pos > 0) {
                final String msg = new String(buffer, 0, pos);
                pos = 0;
                if (error) {
                    ROOT.severe(msg);
                } else {
                    ROOT.info(msg);
                }
            }
        }
    }

    @Override
    public void onModuleLoad() {
        // Should it be set here, in entry-point, or both?
        ROOT.addHandler(new ConsoleLogHandler());

        System.setOut(new LoggingPrintStream(false));
        System.setErr(new LoggingPrintStream(true));
        SystemUtils.setImplementation(new GWTSystemUtilsImpl());
        System.out.println("Util Module loaded");
    }
}
