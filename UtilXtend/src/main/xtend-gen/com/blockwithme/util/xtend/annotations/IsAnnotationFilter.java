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
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

/**
 * Returns true, if the TypeDeclaration is an annotation
 */
@Data
@SuppressWarnings("all")
class IsAnnotationFilter implements Filter {
  public boolean apply(final Map<String, Object> processingContext, @Extension final ProcessorUtil processorUtil, final TypeDeclaration td) {
    return (td instanceof AnnotationTypeDeclaration);
  }
  
  public IsAnnotationFilter() {
    super();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    IsAnnotationFilter other = (IsAnnotationFilter) obj;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
