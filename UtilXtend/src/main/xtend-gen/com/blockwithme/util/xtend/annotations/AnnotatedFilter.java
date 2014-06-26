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
 * Filter for Annotations, that take the (optional) inherited nature of annotations into account.
 */
@Data
@SuppressWarnings("all")
class AnnotatedFilter implements Filter {
  private final String _name;
  
  public String getName() {
    return this._name;
  }
  
  private final boolean _inherited;
  
  public boolean isInherited() {
    return this._inherited;
  }
  
  private static boolean check(@Extension final ProcessorUtil processorUtil, final TypeDeclaration orig, final TypeDeclaration td, final String name) {
    return processorUtil.hasDirectAnnotation(td, name);
  }
  
  public boolean apply(final Map<String, Object> processingContext, @Extension final ProcessorUtil processorUtil, final TypeDeclaration td) {
    boolean _xblockexpression = false;
    {
      boolean _isInherited = this.isInherited();
      if (_isInherited) {
        Iterable<? extends TypeDeclaration> _findParents = processorUtil.findParents(td);
        for (final TypeDeclaration parent : _findParents) {
          String _name = this.getName();
          boolean _check = AnnotatedFilter.check(processorUtil, td, parent, _name);
          if (_check) {
            return true;
          }
        }
        return false;
      }
      String _name_1 = this.getName();
      _xblockexpression = AnnotatedFilter.check(processorUtil, td, td, _name_1);
    }
    return _xblockexpression;
  }
  
  public AnnotatedFilter(final String name, final boolean inherited) {
    super();
    this._name = name;
    this._inherited = inherited;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._name== null) ? 0 : this._name.hashCode());
    result = prime * result + (this._inherited ? 1231 : 1237);
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
    AnnotatedFilter other = (AnnotatedFilter) obj;
    if (this._name == null) {
      if (other._name != null)
        return false;
    } else if (!this._name.equals(other._name))
      return false;
    if (other._inherited != this._inherited)
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
