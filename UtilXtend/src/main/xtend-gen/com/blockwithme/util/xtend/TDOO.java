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
public class TDOO<E1 extends Object, E2 extends Object> {
  private final double _p0;
  
  public double getP0() {
    return this._p0;
  }
  
  private final E1 _p1;
  
  public E1 getP1() {
    return this._p1;
  }
  
  private final E2 _p2;
  
  public E2 getP2() {
    return this._p2;
  }
  
  public String toString() {
    double _p0 = this.getP0();
    String _plus = ("(" + Double.valueOf(_p0));
    String _plus_1 = (_plus + ",");
    E1 _p1 = this.getP1();
    String _plus_2 = (_plus_1 + _p1);
    String _plus_3 = (_plus_2 + ",");
    E2 _p2 = this.getP2();
    String _plus_4 = (_plus_3 + _p2);
    return (_plus_4 + ")");
  }
  
  public TDOO(final double p0, final E1 p1, final E2 p2) {
    super();
    this._p0 = p0;
    this._p1 = p1;
    this._p2 = p2;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(this._p0) ^ (Double.doubleToLongBits(this._p0) >>> 32));
    result = prime * result + ((this._p1== null) ? 0 : this._p1.hashCode());
    result = prime * result + ((this._p2== null) ? 0 : this._p2.hashCode());
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
    TDOO other = (TDOO) obj;
    if (Double.doubleToLongBits(other._p0) != Double.doubleToLongBits(this._p0))
      return false;
    if (this._p1 == null) {
      if (other._p1 != null)
        return false;
    } else if (!this._p1.equals(other._p1))
      return false;
    if (this._p2 == null) {
      if (other._p2 != null)
        return false;
    } else if (!this._p2.equals(other._p2))
      return false;
    return true;
  }
}
