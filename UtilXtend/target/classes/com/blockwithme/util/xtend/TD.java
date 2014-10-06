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

import org.eclipse.xtend.lib.Data;

@Data
@SuppressWarnings("all")
public class TD {
  private final double _p0;
  
  public double getP0() {
    return this._p0;
  }
  
  public String toString() {
    double _p0 = this.getP0();
    String _plus = ("(" + Double.valueOf(_p0));
    return (_plus + ")");
  }
  
  public TD(final double p0) {
    super();
    this._p0 = p0;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(this._p0) ^ (Double.doubleToLongBits(this._p0) >>> 32));
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TD other = (TD) obj;
    if (Double.doubleToLongBits(other._p0) != Double.doubleToLongBits(this._p0))
      return false;
    return true;
  }
}
