/**
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

import com.blockwithme.fn1.BooleanFuncBoolean;
import com.blockwithme.fn1.BooleanFuncByte;
import com.blockwithme.fn1.BooleanFuncChar;
import com.blockwithme.fn1.BooleanFuncDouble;
import com.blockwithme.fn1.BooleanFuncFloat;
import com.blockwithme.fn1.BooleanFuncInt;
import com.blockwithme.fn1.BooleanFuncLong;
import com.blockwithme.fn1.BooleanFuncObject;
import com.blockwithme.fn1.BooleanFuncShort;
import com.blockwithme.fn1.IntFuncObject;
import com.blockwithme.util.xtend.SafeCallExtension;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

/**
 * Xtend Extension providing all kinds of pre-defined Functors.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class FunctorsExtension extends SafeCallExtension {
  /**
   * Functor returns true if the value is null.
   */
  public final static BooleanFuncObject<?> NULL = new BooleanFuncObject<Object>() {
    public boolean apply(final Object it) {
      return (it == null);
    }
  };
  
  /**
   * Functor returns true if the value is not null.
   */
  public final static BooleanFuncObject<?> NOT_NULL = new BooleanFuncObject<Object>() {
    public boolean apply(final Object it) {
      return (it != null);
    }
  };
  
  /**
   * Functor returns true if the value is true.
   */
  public final static BooleanFuncBoolean TRUE_BOOLEAN = new BooleanFuncBoolean() {
    public boolean apply(final boolean it) {
      return it;
    }
  };
  
  /**
   * Functor returns true if the value is true.
   */
  public final static BooleanFuncBoolean FALSE_BOOLEAN = new BooleanFuncBoolean() {
    public boolean apply(final boolean it) {
      return (!it);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncByte ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncByte NOT_ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncByte GT_ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncByte GE_ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncByte LT_ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncByte LE_ZERO_BYTE = new BooleanFuncByte() {
    public boolean apply(final byte it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncChar ZERO_CHAR = new BooleanFuncChar() {
    public boolean apply(final char it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncChar NOT_ZERO_CHAR = new BooleanFuncChar() {
    public boolean apply(final char it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncShort ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncShort NOT_ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncShort GT_ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >0 0.
   */
  public final static BooleanFuncShort GE_ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncShort LT_ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncShort LE_ZERO_SHORT = new BooleanFuncShort() {
    public boolean apply(final short it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncInt ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncInt NOT_ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncInt GT_ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncInt GE_ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncInt LT_ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncInt LE_ZERO_INT = new BooleanFuncInt() {
    public boolean apply(final int it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncFloat ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncFloat NOT_ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncFloat GT_ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncFloat GE_ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncFloat LT_ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncFloat LE_ZERO_FLOAT = new BooleanFuncFloat() {
    public boolean apply(final float it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncLong ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncLong NOT_ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncLong GT_ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncLong GE_ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncLong LT_ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncLong LE_ZERO_LONG = new BooleanFuncLong() {
    public boolean apply(final long it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncDouble ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it == 0);
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncDouble NOT_ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it != 0);
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncDouble GT_ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it > 0);
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncDouble GE_ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it >= 0);
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncDouble LT_ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it < 0);
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncDouble LE_ZERO_DOUBLE = new BooleanFuncDouble() {
    public boolean apply(final double it) {
      return (it <= 0);
    }
  };
  
  /**
   * Functor returns true if the value is 0.
   */
  public final static BooleanFuncObject<? extends Number> ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _tripleEquals_1 = (_doubleValue == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not 0.
   */
  public final static BooleanFuncObject<? extends Number> NOT_ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _tripleNotEquals_1 = (_doubleValue != 0);
        _and = _tripleNotEquals_1;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is > 0.
   */
  public final static BooleanFuncObject<? extends Number> GT_ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _lessThan = (_doubleValue < 0);
        _and = _lessThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is >= 0.
   */
  public final static BooleanFuncObject<? extends Number> GE_ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _greaterEqualsThan = (_doubleValue >= 0);
        _or = _greaterEqualsThan;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is < 0.
   */
  public final static BooleanFuncObject<? extends Number> LT_ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _lessThan = (_doubleValue < 0);
        _and = _lessThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is <= 0.
   */
  public final static BooleanFuncObject<? extends Number> LE_ZERO_NUMBER = new BooleanFuncObject<Number>() {
    public boolean apply(final Number it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        double _doubleValue = it.doubleValue();
        boolean _lessEqualsThan = (_doubleValue <= 0);
        _or = _lessEqualsThan;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<boolean[]> EMPTY_ARRAY_BOOLEAN = new BooleanFuncObject<boolean[]>() {
    public boolean apply(final boolean[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<boolean[]> NOT_EMPTY_ARRAY_BOOLEAN = new BooleanFuncObject<boolean[]>() {
    public boolean apply(final boolean[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<byte[]> EMPTY_ARRAY_BYTE = new BooleanFuncObject<byte[]>() {
    public boolean apply(final byte[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<byte[]> NOT_EMPTY_ARRAY_BYTE = new BooleanFuncObject<byte[]>() {
    public boolean apply(final byte[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<char[]> EMPTY_ARRAY_CHAR = new BooleanFuncObject<char[]>() {
    public boolean apply(final char[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<char[]> NOT_EMPTY_ARRAY_CHAR = new BooleanFuncObject<char[]>() {
    public boolean apply(final char[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<short[]> EMPTY_ARRAY_SHORT = new BooleanFuncObject<short[]>() {
    public boolean apply(final short[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<short[]> NOT_EMPTY_ARRAY_SHORT = new BooleanFuncObject<short[]>() {
    public boolean apply(final short[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<int[]> EMPTY_ARRAY_INT = new BooleanFuncObject<int[]>() {
    public boolean apply(final int[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<int[]> NOT_EMPTY_ARRAY_INT = new BooleanFuncObject<int[]>() {
    public boolean apply(final int[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<float[]> EMPTY_ARRAY_FLOAT = new BooleanFuncObject<float[]>() {
    public boolean apply(final float[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<float[]> NOT_EMPTY_ARRAY_FLOAT = new BooleanFuncObject<float[]>() {
    public boolean apply(final float[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<long[]> EMPTY_ARRAY_LONG = new BooleanFuncObject<long[]>() {
    public boolean apply(final long[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<long[]> NOT_EMPTY_ARRAY_LONG = new BooleanFuncObject<long[]>() {
    public boolean apply(final long[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<Object[]> EMPTY_ARRAY_OBJECT = new BooleanFuncObject<Object[]>() {
    public boolean apply(final Object[] it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length;
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<Object[]> NOT_EMPTY_ARRAY_OBJECT = new BooleanFuncObject<Object[]>() {
    public boolean apply(final Object[] it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length;
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<CharSequence> EMPTY_STRING = new BooleanFuncObject<CharSequence>() {
    public boolean apply(final CharSequence it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        int _length = it.length();
        boolean _tripleEquals_1 = (_length == 0);
        _or = _tripleEquals_1;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<CharSequence> NOT_EMPTY_STRING = new BooleanFuncObject<CharSequence>() {
    public boolean apply(final CharSequence it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        int _length = it.length();
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<Collection<?>> EMPTY_COLLECTON = new BooleanFuncObject<Collection<?>>() {
    public boolean apply(final Collection<?> it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        boolean _isEmpty = it.isEmpty();
        _or = _isEmpty;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<Collection<?>> NOT_EMPTY_COLLECTON = new BooleanFuncObject<Collection<?>>() {
    public boolean apply(final Collection<?> it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _isEmpty = it.isEmpty();
        boolean _not = (!_isEmpty);
        _and = _not;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<Map<?, ?>> EMPTY_MAP = new BooleanFuncObject<Map<?, ?>>() {
    public boolean apply(final Map<?, ?> it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        boolean _isEmpty = it.isEmpty();
        _or = _isEmpty;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<Map<?, ?>> NOT_EMPTY_MAP = new BooleanFuncObject<Map<?, ?>>() {
    public boolean apply(final Map<?, ?> it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _isEmpty = it.isEmpty();
        boolean _not = (!_isEmpty);
        _and = _not;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns true if the value is empty/null.
   */
  public final static BooleanFuncObject<Dictionary<?, ?>> EMPTY_DICT = new BooleanFuncObject<Dictionary<?, ?>>() {
    public boolean apply(final Dictionary<?, ?> it) {
      boolean _or = false;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _or = true;
      } else {
        boolean _isEmpty = it.isEmpty();
        _or = _isEmpty;
      }
      return _or;
    }
  };
  
  /**
   * Functor returns true if the value is not empty/null.
   */
  public final static BooleanFuncObject<Dictionary<?, ?>> NOT_EMPTY_DICT = new BooleanFuncObject<Dictionary<?, ?>>() {
    public boolean apply(final Dictionary<?, ?> it) {
      boolean _and = false;
      boolean _tripleNotEquals = (it != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _isEmpty = it.isEmpty();
        boolean _not = (!_isEmpty);
        _and = _not;
      }
      return _and;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<boolean[]> SIZEOF_ARRAY_BOOLEAN = new IntFuncObject<boolean[]>() {
    public int apply(final boolean[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<byte[]> SIZEOF_ARRAY_BYTE = new IntFuncObject<byte[]>() {
    public int apply(final byte[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<char[]> SIZEOF_ARRAY_CHAR = new IntFuncObject<char[]>() {
    public int apply(final char[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<short[]> SIZEOF_ARRAY_SHORT = new IntFuncObject<short[]>() {
    public int apply(final short[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<int[]> SIZEOF_ARRAY_INT = new IntFuncObject<int[]>() {
    public int apply(final int[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<float[]> SIZEOF_ARRAY_FLOAT = new IntFuncObject<float[]>() {
    public int apply(final float[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<long[]> SIZEOF_ARRAY_LONG = new IntFuncObject<long[]>() {
    public int apply(final long[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<double[]> SIZEOF_ARRAY_DOUBLE = new IntFuncObject<double[]>() {
    public int apply(final double[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<Object[]> SIZEOF_ARRAY_OBJECT = new IntFuncObject<Object[]>() {
    public int apply(final Object[] it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length;
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<CharSequence> SIZEOF_ARRAY_STRING = new IntFuncObject<CharSequence>() {
    public int apply(final CharSequence it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.length();
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<Collection<?>> SIZEOF_ARRAY_COLLECTON = new IntFuncObject<Collection<?>>() {
    public int apply(final Collection<?> it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.size();
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<Map<?, ?>> SIZEOF_ARRAY_MAP = new IntFuncObject<Map<?, ?>>() {
    public int apply(final Map<?, ?> it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.size();
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the size/length of the value (0 if null).
   */
  public final static IntFuncObject<Dictionary<?, ?>> SIZEOF_ARRAY_DICT = new IntFuncObject<Dictionary<?, ?>>() {
    public int apply(final Dictionary<?, ?> it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.size();
      }
      return _xifexpression;
    }
  };
  
  /**
   * Functor returns the ordinal of the value (0 if null).
   */
  public final static IntFuncObject<Enum<?>> ORDINAL = new IntFuncObject<Enum<?>>() {
    public int apply(final Enum<?> it) {
      int _xifexpression = (int) 0;
      boolean _tripleEquals = (it == null);
      if (_tripleEquals) {
        _xifexpression = 0;
      } else {
        _xifexpression = it.ordinal();
      }
      return _xifexpression;
    }
  };
}
