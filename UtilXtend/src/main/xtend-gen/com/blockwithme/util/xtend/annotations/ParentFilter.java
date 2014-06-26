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
import com.google.common.base.Objects;
import java.util.Map;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

/**
 * Accepts, if the validated type has the desired type as parent
 * (either base class or implemented interface)
 */
@Data
@SuppressWarnings("all")
class ParentFilter implements Filter {
  private final String _parent;
  
  public String getParent() {
    return this._parent;
  }
  
  public boolean apply(final Map<String, Object> processingContext, @Extension final ProcessorUtil processorUtil, final TypeDeclaration td) {
    boolean _xblockexpression = false;
    {
      Iterable<? extends TypeDeclaration> _findParents = processorUtil.findParents(td);
      for (final TypeDeclaration p : _findParents) {
        String _qualifiedName = p.getQualifiedName();
        String _parent = this.getParent();
        boolean _equals = Objects.equal(_qualifiedName, _parent);
        if (_equals) {
          return true;
        }
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  public ParentFilter(final String parent) {
    super();
    this._parent = parent;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._parent== null) ? 0 : this._parent.hashCode());
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
    ParentFilter other = (ParentFilter) obj;
    if (this._parent == null) {
      if (other._parent != null)
        return false;
    } else if (!this._parent.equals(other._parent))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
