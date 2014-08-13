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

import com.blockwithme.fn1.BooleanFuncBoolean
import com.blockwithme.fn1.BooleanFuncByte
import com.blockwithme.fn1.BooleanFuncChar
import com.blockwithme.fn1.BooleanFuncDouble
import com.blockwithme.fn1.BooleanFuncFloat
import com.blockwithme.fn1.BooleanFuncInt
import com.blockwithme.fn1.BooleanFuncLong
import com.blockwithme.fn1.BooleanFuncObject
import com.blockwithme.fn1.BooleanFuncShort
import com.blockwithme.fn1.IntFuncObject
import com.google.common.base.Predicate
import com.google.common.collect.Iterables
import java.util.Collection
import java.util.Dictionary
import java.util.Map

/**
 * Xtend Extension providing all kinds of pre-defined Functors.
 *
 * @author monster
 */
class FunctorsExtension extends SafeCallExtension {
	/** Functor returns true if the value is null. */
	public static val BooleanFuncObject<?> NULL = [it === null]

	/** Functor returns true if the value is not null. */
	public static val BooleanFuncObject<?> NOT_NULL = [it !== null]

	/** Functor returns true if the value is true. */
	public static val BooleanFuncBoolean TRUE_BOOLEAN = [it]

	/** Functor returns true if the value is true. */
	public static val BooleanFuncBoolean FALSE_BOOLEAN = [!it]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncByte ZERO_BYTE = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncByte NOT_ZERO_BYTE = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncByte GT_ZERO_BYTE = [it>0]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncByte GE_ZERO_BYTE = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncByte LT_ZERO_BYTE = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncByte LE_ZERO_BYTE = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncChar ZERO_CHAR = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncChar NOT_ZERO_CHAR = [it!==0]

	// char can NEVER be less than 0, therefore "!= 0" <==> "> 0"

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncShort ZERO_SHORT = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncShort NOT_ZERO_SHORT = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncShort GT_ZERO_SHORT = [it>0]

	/** Functor returns true if the value is >0 0. */
	public static val BooleanFuncShort GE_ZERO_SHORT = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncShort LT_ZERO_SHORT = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncShort LE_ZERO_SHORT = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncInt ZERO_INT = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncInt NOT_ZERO_INT = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncInt GT_ZERO_INT = [it>0]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncInt GE_ZERO_INT = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncInt LT_ZERO_INT = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncInt LE_ZERO_INT = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncFloat ZERO_FLOAT = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncFloat NOT_ZERO_FLOAT = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncFloat GT_ZERO_FLOAT = [it>0]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncFloat GE_ZERO_FLOAT = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncFloat LT_ZERO_FLOAT = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncFloat LE_ZERO_FLOAT = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncLong ZERO_LONG = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncLong NOT_ZERO_LONG = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncLong GT_ZERO_LONG = [it>0]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncLong GE_ZERO_LONG = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncLong LT_ZERO_LONG = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncLong LE_ZERO_LONG = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncDouble ZERO_DOUBLE = [it===0]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncDouble NOT_ZERO_DOUBLE = [it!==0]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncDouble GT_ZERO_DOUBLE = [it>0]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncDouble GE_ZERO_DOUBLE = [it>=0]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncDouble LT_ZERO_DOUBLE = [it<0]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncDouble LE_ZERO_DOUBLE = [it<=0]

	/** Functor returns true if the value is 0. */
	public static val BooleanFuncObject<? extends Number> ZERO_NUMBER = [(it === null) || (it.doubleValue===0)]

	/** Functor returns true if the value is not 0. */
	public static val BooleanFuncObject<? extends Number> NOT_ZERO_NUMBER = [(it !== null) && (it.doubleValue!==0)]

	/** Functor returns true if the value is > 0. */
	public static val BooleanFuncObject<? extends Number> GT_ZERO_NUMBER = [(it !== null) && (it.doubleValue<0)]

	/** Functor returns true if the value is >= 0. */
	public static val BooleanFuncObject<? extends Number> GE_ZERO_NUMBER = [(it === null) || (it.doubleValue>=0)]

	/** Functor returns true if the value is < 0. */
	public static val BooleanFuncObject<? extends Number> LT_ZERO_NUMBER = [(it !== null) && (it.doubleValue<0)]

	/** Functor returns true if the value is <= 0. */
	public static val BooleanFuncObject<? extends Number> LE_ZERO_NUMBER = [(it === null) || (it.doubleValue<=0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<boolean[]> EMPTY_ARRAY_BOOLEAN = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<boolean[]> NOT_EMPTY_ARRAY_BOOLEAN = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<byte[]> EMPTY_ARRAY_BYTE = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<byte[]> NOT_EMPTY_ARRAY_BYTE = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<char[]> EMPTY_ARRAY_CHAR = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<char[]> NOT_EMPTY_ARRAY_CHAR = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<short[]> EMPTY_ARRAY_SHORT = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<short[]> NOT_EMPTY_ARRAY_SHORT = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<int[]> EMPTY_ARRAY_INT = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<int[]> NOT_EMPTY_ARRAY_INT = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<float[]> EMPTY_ARRAY_FLOAT = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<float[]> NOT_EMPTY_ARRAY_FLOAT = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<long[]> EMPTY_ARRAY_LONG = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<long[]> NOT_EMPTY_ARRAY_LONG = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<Object[]> EMPTY_ARRAY_OBJECT = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<Object[]> NOT_EMPTY_ARRAY_OBJECT = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<CharSequence> EMPTY_STRING = [(it === null) || (it.length===0)]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<CharSequence> NOT_EMPTY_STRING = [(it !== null) && (it.length>0)]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<Collection<?>> EMPTY_COLLECTON = [(it === null) || it.empty]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<Collection<?>> NOT_EMPTY_COLLECTON = [(it !== null) && !it.empty]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<Map<?,?>> EMPTY_MAP = [(it === null) || it.empty]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<Map<?,?>> NOT_EMPTY_MAP = [(it !== null) && !it.empty]

	/** Functor returns true if the value is empty/null. */
	public static val BooleanFuncObject<Dictionary<?,?>> EMPTY_DICT = [(it === null) || it.empty]

	/** Functor returns true if the value is not empty/null. */
	public static val BooleanFuncObject<Dictionary<?,?>> NOT_EMPTY_DICT = [(it !== null) && !it.empty]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<boolean[]> SIZEOF_ARRAY_BOOLEAN = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<byte[]> SIZEOF_ARRAY_BYTE = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<char[]> SIZEOF_ARRAY_CHAR = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<short[]> SIZEOF_ARRAY_SHORT = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<int[]> SIZEOF_ARRAY_INT = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<float[]> SIZEOF_ARRAY_FLOAT = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<long[]> SIZEOF_ARRAY_LONG = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<double[]> SIZEOF_ARRAY_DOUBLE = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<Object[]> SIZEOF_ARRAY_OBJECT = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<CharSequence> SIZEOF_ARRAY_STRING = [if (it === null) 0 else it.length]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<Collection<?>> SIZEOF_ARRAY_COLLECTON = [if (it === null) 0 else it.size]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<Map<?,?>> SIZEOF_ARRAY_MAP = [if (it === null) 0 else it.size]

	/** Functor returns the size/length of the value (0 if null). */
	public static val IntFuncObject<Dictionary<?,?>> SIZEOF_ARRAY_DICT = [if (it === null) 0 else it.size]

	/** Functor returns the ordinal of the value (0 if null). */
	public static val IntFuncObject<Enum<?>> ORDINAL = [if (it === null) 0 else it.ordinal]

  /**
   * Removes, from an iterable, every element that satisfies the provided
   * predicate.
   *
   * @param removeFrom the iterable to (potentially) remove elements from
   * @param predicate a predicate that determines whether an element should
   *     be removed
   * @return {@code true} if any elements were removed from the iterable
   *
   * @throws UnsupportedOperationException if the iterable does not support
   *     {@code remove()}.
   * @since 2.0
   */
  static def <T> boolean removeIf(
      Iterable<T> removeFrom, Predicate<? super T> predicate) {
    return Iterables.removeIf(removeFrom, predicate);
  }
}