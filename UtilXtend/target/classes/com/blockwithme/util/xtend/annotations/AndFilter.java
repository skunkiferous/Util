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
package com.blockwithme.util.xtend.annotations;

import com.blockwithme.util.xtend.annotations.Filter;
import com.blockwithme.util.xtend.annotations.ProcessorUtil;
import java.util.Map;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

/**
 * Combines Filters using the AND logic operator
 */
@Data
@SuppressWarnings("all")
class AndFilter implements Filter {
  private final Filter[] _others;
  
  public Filter[] getOthers() {
    return this._others;
  }
  
  public boolean apply(final Map<String, Object> processingContext, @Extension final ProcessorUtil processorUtil, final TypeDeclaration td) {
    boolean _xblockexpression = false;
    {
      Filter[] _others = this.getOthers();
      for (final Filter f : _others) {
        boolean _apply = f.apply(processingContext, processorUtil, td);
        boolean _not = (!_apply);
        if (_not) {
          return false;
        }
      }
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  public AndFilter(final Filter[] others) {
    super();
    this._others = others;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._others== null) ? 0 : this._others.hashCode());
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
    AndFilter other = (AndFilter) obj;
    if (this._others == null) {
      if (other._others != null)
        return false;
    } else if (!this._others.equals(other._others))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
