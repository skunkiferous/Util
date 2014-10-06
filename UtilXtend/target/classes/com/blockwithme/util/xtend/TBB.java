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
public class TBB {
  private final boolean _p0;
  
  public boolean isP0() {
    return this._p0;
  }
  
  private final boolean _p1;
  
  public boolean isP1() {
    return this._p1;
  }
  
  public String toString() {
    boolean _isP0 = this.isP0();
    String _plus = ("(" + Boolean.valueOf(_isP0));
    String _plus_1 = (_plus + ",");
    boolean _isP1 = this.isP1();
    String _plus_2 = (_plus_1 + Boolean.valueOf(_isP1));
    return (_plus_2 + ")");
  }
  
  public TBB(final boolean p0, final boolean p1) {
    super();
    this._p0 = p0;
    this._p1 = p1;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this._p0 ? 1231 : 1237);
    result = prime * result + (this._p1 ? 1231 : 1237);
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
    TBB other = (TBB) obj;
    if (other._p0 != this._p0)
      return false;
    if (other._p1 != this._p1)
      return false;
    return true;
  }
}
