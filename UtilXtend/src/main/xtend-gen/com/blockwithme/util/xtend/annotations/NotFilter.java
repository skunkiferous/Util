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
 * Negates the result of another Filter
 */
@Data
@SuppressWarnings("all")
class NotFilter implements Filter {
  private final Filter _other;
  
  public Filter getOther() {
    return this._other;
  }
  
  public boolean apply(final Map<String, Object> processingContext, @Extension final ProcessorUtil processorUtil, final TypeDeclaration td) {
    Filter _other = this.getOther();
    boolean _apply = _other.apply(processingContext, processorUtil, td);
    return (!_apply);
  }
  
  public NotFilter(final Filter other) {
    super();
    this._other = other;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._other== null) ? 0 : this._other.hashCode());
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
    NotFilter other = (NotFilter) obj;
    if (this._other == null) {
      if (other._other != null)
        return false;
    } else if (!this._other.equals(other._other))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
