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

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.Functions.Function3;
import org.eclipse.xtext.xbase.lib.Functions.Function4;
import org.eclipse.xtext.xbase.lib.Functions.Function5;
import org.eclipse.xtext.xbase.lib.Functions.Function6;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure4;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure5;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure6;

/**
 * Xtend Extension allowing "safe" (exception-free) code execution.
 *
 * To prevent clashes, the methods that accept Runnable and Callable
 * need to be renamed.
 *
 * @author monster
 */
public class SafeCallExtension extends TupleExtension {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(SafeCallExtension.class
            .getName());

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static boolean attempt(final Procedure0 code) {
        try {
            code.apply();
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1> boolean attempt(final Procedure1<E1> code, final E1 p1) {
        try {
            code.apply(p1);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1, E2> boolean attempt(final Procedure2<E1, E2> code,
            final E1 p1, final E2 p2) {
        try {
            code.apply(p1, p2);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1, E2, E3> boolean attempt(
            final Procedure3<E1, E2, E3> code, final E1 p1, final E2 p2,
            final E3 p3) {
        try {
            code.apply(p1, p2, p3);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1, E2, E3, E4> boolean attempt(
            final Procedure4<E1, E2, E3, E4> code, final E1 p1, final E2 p2,
            final E3 p3, final E4 p4) {
        try {
            code.apply(p1, p2, p3, p4);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1, E2, E3, E4, E5> boolean attempt(
            final Procedure5<E1, E2, E3, E4, E5> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5) {
        try {
            code.apply(p1, p2, p3, p4, p5);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static <E1, E2, E3, E4, E5, E6> boolean attempt(
            final Procedure6<E1, E2, E3, E4, E5, E6> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5, final E6 p6) {
        try {
            code.apply(p1, p2, p3, p4, p5, p6);
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R> R attempt(final Function0<R> code, final R onError) {
        try {
            return code.apply();
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, null)")
    public static <R> R attempt(final Function0<R> code) {
        return attempt(code, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1> R attempt(final Function1<E1, R> code, final E1 p1,
            final R onError) {
        try {
            return code.apply(p1);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, null)")
    public static <R, E1> R attempt(final Function1<E1, R> code, final E1 p1) {
        return attempt(code, p1, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1, E2> R attempt(final Function2<E1, E2, R> code,
            final E1 p1, final E2 p2, final R onError) {
        try {
            return code.apply(p1, p2);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, null)")
    public static <R, E1, E2> R attempt(final Function2<E1, E2, R> code,
            final E1 p1, final E2 p2) {
        return attempt(code, p1, p2, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1, E2, E3> R attempt(
            final Function3<E1, E2, E3, R> code, final E1 p1, final E2 p2,
            final E3 p3, final R onError) {
        try {
            return code.apply(p1, p2, p3);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, null)")
    public static <R, E1, E2, E3> R attempt(
            final Function3<E1, E2, E3, R> code, final E1 p1, final E2 p2,
            final E3 p3) {
        return attempt(code, p1, p2, p3, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1, E2, E3, E4> R attempt(
            final Function4<E1, E2, E3, E4, R> code, final E1 p1, final E2 p2,
            final E3 p3, final E4 p4, final R onError) {
        try {
            return code.apply(p1, p2, p3, p4);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, null)")
    public static <R, E1, E2, E3, E4> R attempt(
            final Function4<E1, E2, E3, E4, R> code, final E1 p1, final E2 p2,
            final E3 p3, final E4 p4) {
        return attempt(code, p1, p2, p3, p4, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1, E2, E3, E4, E5> R attempt(
            final Function5<E1, E2, E3, E4, E5, R> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5, final R onError) {
        try {
            return code.apply(p1, p2, p3, p4, p5);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, $5, null)")
    public static <R, E1, E2, E3, E4, E5> R attempt(
            final Function5<E1, E2, E3, E4, E5, R> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5) {
        return attempt(code, p1, p2, p3, p4, p5, null);
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <R, E1, E2, E3, E4, E5, E6> R attempt(
            final Function6<E1, E2, E3, E4, E5, E6, R> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5, final E6 p6,
            final R onError) {
        try {
            return code.apply(p1, p2, p3, p4, p5, p6);
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, $5, $6, null)")
    public static <R, E1, E2, E3, E4, E5, E6> R attempt(
            final Function6<E1, E2, E3, E4, E5, E6, R> code, final E1 p1,
            final E2 p2, final E3 p3, final E4 p4, final E5 p5, final E6 p6) {
        return attempt(code, p1, p2, p3, p4, p5, p6, null);
    }

    /**
     * Runs some code safely.
     * Never throws an Exception.
     * @return return true; on success.
     */
    public static boolean attempt2(final Runnable code) {
        try {
            code.run();
            return true;
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return false;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return onError; if the code fails, otherwise the return value of call().
     */
    public static <E> E attempt2(final Callable<E> code, final E onError) {
        try {
            return code.call();
        } catch (final Throwable t) {
            LOG.log(Level.SEVERE, t.getMessage(), t);
            return onError;
        }
    }

    /**
     * Calls some code safely.
     * Never throws an Exception.
     * @return
     * @return null if the code fails, otherwise the return value of call().
     */
    @Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, null)")
    public static <E> E attempt2(final Callable<E> code) {
        return attempt2(code, null);
    }
}
