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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Used to output Console text to the root logger, so that the
 * logging system takes charge of forwarding the output to the
 * right place.
 *
 * @author monster
 */
public class LoggingPrintStream extends PrintStream {
    /** The global logger */
    private static final Logger GLOBAL = Logger.getLogger("global");

    /** Dummy OutputStream */
    private static final OutputStream DUMMY = new OutputStream() {
        @Override
        public void write(final int b) throws IOException {
            // NOP
        }
    };

    private final boolean error;
    private byte[] buffer = new byte[1024];
    private int pos;

    public LoggingPrintStream(final boolean error) {
        super(DUMMY);
        this.error = error;
    }

    /**
     * Writes one byte to the target stream. Only the low order byte of the
     * integer {@code oneByte} is written.
     *
     * @param oneByte
     *            the byte to be written.
     */
    @Override
    public void write(final int oneByte) {
        if (oneByte == '\n') {
            flush();
        } else {
            if (pos == buffer.length) {
                final byte[] newBuffer = new byte[buffer.length * 2];
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
                GLOBAL.severe(msg);
            } else {
                GLOBAL.info(msg);
            }
        }
    }
}