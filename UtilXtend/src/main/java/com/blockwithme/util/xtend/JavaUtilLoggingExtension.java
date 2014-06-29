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
package com.blockwithme.util.xtend;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.xtext.xbase.lib.Inline;

/**
 * Xtend Extension related to JUL.
 *
 * @author monster
 */
public class JavaUtilLoggingExtension {

    /** What Level to use for debug? */
    public static final Level JUL_DEBUG = Level.FINE;

    /**
     * Log a DEBUG message.
     * @param   msg     The message
     */
    @Inline("$1.log(com.blockwithme.util.xtend.JavaUtilLoggingExtension.JUL_DEBUG, $2)")
    public static void debug(final Logger log, final String msg) {
        log.log(JUL_DEBUG, msg);
    }

    /**
     * Log a DEBUG message.
     * @param   error     The error
     */
    @Inline("$1.log(com.blockwithme.util.xtend.JavaUtilLoggingExtension.JUL_DEBUG, String.valueOf($2), $2)")
    public static void debug(final Logger log, final Throwable error) {
        log.log(JUL_DEBUG, String.valueOf(error), error);
    }

    /**
     * Log a DEBUG message.
     * @param   msg     The message
     */
    @Inline("$1.log(com.blockwithme.util.xtend.JavaUtilLoggingExtension.JUL_DEBUG, String.valueOf($2))")
    public static void debug(final Logger log, final Object msg) {
        log.log(JUL_DEBUG, String.valueOf(msg));
    }

    /**
     * Log a DEBUG message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(com.blockwithme.util.xtend.JavaUtilLoggingExtension.JUL_DEBUG, $2, $3)")
    public static void debug(final Logger log, final String msg,
            final Throwable error) {
        log.log(JUL_DEBUG, msg, error);
    }

    /**
     * Log a DEBUG message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(com.blockwithme.util.xtend.JavaUtilLoggingExtension.JUL_DEBUG, String.valueOf($2), $3)")
    public static void debug(final Logger log, final Object msg,
            final Throwable error) {
        log.log(JUL_DEBUG, String.valueOf(msg), error);
    }

    /**
     * Log an INFO message.
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.INFO, String.valueOf($2), $2)")
    public static void info(final Logger log, final Throwable error) {
        log.log(Level.INFO, String.valueOf(error), error);
    }

    /**
     * Log an INFO message.
     * @param   msg     The message
     */
    @Inline("$1.log(java.util.logging.Level.INFO, String.valueOf($2))")
    public static void info(final Logger log, final Object msg) {
        log.log(Level.INFO, String.valueOf(msg));
    }

    /**
     * Log an INFO message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.INFO, $2, $3)")
    public static void info(final Logger log, final String msg,
            final Throwable error) {
        log.log(Level.INFO, msg, error);
    }

    /**
     * Log an INFO message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.INFO, String.valueOf($2), $3)")
    public static void info(final Logger log, final Object msg,
            final Throwable error) {
        log.log(Level.INFO, String.valueOf(msg), error);
    }

    /**
     * Log a WARNING message.
     * @param   msg     The message
     */
    @Inline("$1.log(java.util.logging.Level.WARNING, $2)")
    public static void warn(final Logger log, final String msg) {
        log.log(Level.WARNING, msg);
    }

    /**
     * Log a WARNING message.
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.WARNING, String.valueOf($2), $2)")
    public static void warn(final Logger log, final Throwable error) {
        log.log(Level.WARNING, String.valueOf(error), error);
    }

    /**
     * Log a WARNING message.
     * @param   msg     The message
     */
    @Inline("$1.log(java.util.logging.Level.WARNING, String.valueOf($2))")
    public static void warn(final Logger log, final Object msg) {
        log.log(Level.WARNING, String.valueOf(msg));
    }

    /**
     * Log a WARNING message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.WARNING, $2, $3)")
    public static void warn(final Logger log, final String msg,
            final Throwable error) {
        log.log(Level.WARNING, msg, error);
    }

    /**
     * Log a WARNING message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.WARNING, String.valueOf($2), $3)")
    public static void warn(final Logger log, final Object msg,
            final Throwable error) {
        log.log(Level.WARNING, String.valueOf(msg), error);
    }

    /**
     * Log an ERROR message.
     * @param   msg     The message
     */
    @Inline("$1.log(java.util.logging.Level.SEVERE, $2)")
    public static void error(final Logger log, final String msg) {
        log.log(Level.SEVERE, msg);
    }

    /**
     * Log an ERROR message.
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.SEVERE, String.valueOf($2), $2)")
    public static void error(final Logger log, final Throwable error) {
        log.log(Level.SEVERE, String.valueOf(error), error);
    }

    /**
     * Log an ERROR message.
     * @param   msg     The message
     */
    @Inline("$1.log(java.util.logging.Level.SEVERE, String.valueOf($2))")
    public static void error(final Logger log, final Object msg) {
        log.log(Level.SEVERE, String.valueOf(msg));
    }

    /**
     * Log an ERROR message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.SEVERE, $2, $3)")
    public static void error(final Logger log, final String msg,
            final Throwable error) {
        log.log(Level.SEVERE, msg, error);
    }

    /**
     * Log an ERROR message.
     * @param   msg     The message
     * @param   error     The error
     */
    @Inline("$1.log(java.util.logging.Level.SEVERE, String.valueOf($2), $3)")
    public static void error(final Logger log, final Object msg,
            final Throwable error) {
        log.log(Level.SEVERE, String.valueOf(msg), error);
    }
}
