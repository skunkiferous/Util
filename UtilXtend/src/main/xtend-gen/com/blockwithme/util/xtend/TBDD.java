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
public class TBDD {
  private final boolean _p0;
  
  public boolean isP0() {
    return this._p0;
  }
  
  private final double _p1;
  
  public double getP1() {
    return this._p1;
  }
  
  private final double _p2;
  
  public double getP2() {
    return this._p2;
  }
  
  public String toString() {
    boolean _isP0 = this.isP0();
    String _plus = ("(" + Boolean.valueOf(_isP0));
    String _plus_1 = (_plus + ",");
    double _p1 = this.getP1();
    String _plus_2 = (_plus_1 + Double.valueOf(_p1));
    String _plus_3 = (_plus_2 + ",");
    double _p2 = this.getP2();
    String _plus_4 = (_plus_3 + Double.valueOf(_p2));
    return (_plus_4 + ")");
  }
  
  public TBDD(final boolean p0, final double p1, final double p2) {
    super();
    this._p0 = p0;
    this._p1 = p1;
    this._p2 = p2;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this._p0 ? 1231 : 1237);
    result = prime * result + (int) (Double.doubleToLongBits(this._p1) ^ (Double.doubleToLongBits(this._p1) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this._p2) ^ (Double.doubleToLongBits(this._p2) >>> 32));
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
    TBDD other = (TBDD) obj;
    if (other._p0 != this._p0)
      return false;
    if (Double.doubleToLongBits(other._p1) != Double.doubleToLongBits(this._p1))
      return false;
    if (Double.doubleToLongBits(other._p2) != Double.doubleToLongBits(this._p2))
      return false;
    return true;
  }
}
