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
package com.blockwithme.util.xtend

import java.util.concurrent.Callable
import java.util.logging.Logger
import java.util.logging.Level
import org.eclipse.xtext.xbase.lib.Functions.Function0
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3
import org.eclipse.xtext.xbase.lib.Procedures.Procedure4
import org.eclipse.xtext.xbase.lib.Procedures.Procedure5
import org.eclipse.xtext.xbase.lib.Procedures.Procedure6
import org.eclipse.xtext.xbase.lib.Functions.Function1
import org.eclipse.xtext.xbase.lib.Functions.Function2
import org.eclipse.xtext.xbase.lib.Functions.Function3
import org.eclipse.xtext.xbase.lib.Functions.Function4
import org.eclipse.xtext.xbase.lib.Functions.Function5
import org.eclipse.xtext.xbase.lib.Functions.Function6

/**
 * Xtend Extension allowing "safe" code execution.
 *
 * @author monster
 */
class SafeCallExtension extends TupleExtension {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(SafeCallExtension.getName());

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static boolean attempt(Runnable code) {
		try {
			code.run()
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <E> attempt(Callable<E> code, E onError) {
		try {
			return code.call
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, null)")
	def static <E> attempt(Callable<E> code) {
		attempt(code, null);
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static boolean attempt(Procedure0 code) {
		try {
			code.apply()
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1> boolean attempt(Procedure1<E1> code, E1 p1) {
		try {
			code.apply(p1)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1,E2> boolean attempt(Procedure2<E1,E2> code, E1 p1, E2 p2) {
		try {
			code.apply(p1, p2)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1,E2,E3> boolean attempt(Procedure3<E1,E2,E3> code, E1 p1, E2 p2, E3 p3) {
		try {
			code.apply(p1, p2, p3)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1,E2,E3,E4> boolean attempt(Procedure4<E1,E2,E3,E4> code, E1 p1, E2 p2, E3 p3, E4 p4) {
		try {
			code.apply(p1, p2, p3, p4)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1,E2,E3,E4,E5> boolean attempt(Procedure5<E1,E2,E3,E4,E5> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5) {
		try {
			code.apply(p1, p2, p3, p4, p5)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Runs some code safely.
	 * Never throws an Exception.
	 * @return true on success.
	 */
	def static <E1,E2,E3,E4,E5,E6> boolean attempt(Procedure6<E1,E2,E3,E4,E5,E6> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5, E6 p6) {
		try {
			code.apply(p1, p2, p3, p4, p5, p6)
			true
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			false
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R> attempt(Function0<R> code, R onError) {
		try {
			return code.apply
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, null)")
	def static <R> attempt(Function0<R> code) {
		attempt(code, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1> attempt(Function1<E1,R> code, E1 p1, R onError) {
		try {
			return code.apply(p1)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, null)")
	def static <R,E1> attempt(Function1<E1,R> code, E1 p1) {
		attempt(code, p1, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1,E2> attempt(Function2<E1,E2,R> code, E1 p1, E2 p2, R onError) {
		try {
			return code.apply(p1, p2)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, null)")
	def static <R,E1,E2> attempt(Function2<E1,E2,R> code, E1 p1, E2 p2) {
		attempt(code, p1, p2, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1,E2,E3> attempt(Function3<E1,E2,E3,R> code, E1 p1, E2 p2, E3 p3, R onError) {
		try {
			return code.apply(p1, p2, p3)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, null)")
	def static <R,E1,E2,E3> attempt(Function3<E1,E2,E3,R> code, E1 p1, E2 p2, E3 p3) {
		attempt(code, p1, p2, p3, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1,E2,E3,E4> attempt(Function4<E1,E2,E3,E4,R> code, E1 p1, E2 p2, E3 p3, E4 p4, R onError) {
		try {
			return code.apply(p1, p2, p3, p4)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, null)")
	def static <R,E1,E2,E3,E4> attempt(Function4<E1,E2,E3,E4,R> code, E1 p1, E2 p2, E3 p3, E4 p4) {
		attempt(code, p1, p2, p3, p4, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1,E2,E3,E4,E5> attempt(Function5<E1,E2,E3,E4,E5,R> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5, R onError) {
		try {
			return code.apply(p1, p2, p3, p4, p5)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, $5, null)")
	def static <R,E1,E2,E3,E4,E5> attempt(Function5<E1,E2,E3,E4,E5,R> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5) {
		attempt(code, p1, p2, p3, p4, p5, null);
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return onError if the code fails, otherwise the return value of call().
	 */
	def static <R,E1,E2,E3,E4,E5,E6> attempt(Function6<E1,E2,E3,E4,E5,E6,R> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5, E6 p6, R onError) {
		try {
			return code.apply(p1, p2, p3, p4, p5, p6)
		} catch (Throwable t) {
			LOG.log(Level.SEVERE, t.message, t)
			return onError
		}
	}

	/**
	 * Calls some code safely.
	 * Never throws an Exception.
	 * @return null if the code fails, otherwise the return value of call().
	 */
	@Inline("com.blockwithme.util.xtend.SafeCallExtension.attempt($1, $2, $3, $4, $5, $6, null)")
	def static <R,E1,E2,E3,E4,E5, E6> attempt(Function6<E1,E2,E3,E4,E5,E6,R> code, E1 p1, E2 p2, E3 p3, E4 p4, E5 p5, E6 p6) {
		attempt(code, p1, p2, p3, p4, p5, p6, null);
	}
}