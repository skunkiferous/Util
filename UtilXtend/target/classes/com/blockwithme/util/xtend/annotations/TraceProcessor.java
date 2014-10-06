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

import com.blockwithme.util.xtend.annotations.Processor;
import java.util.Map;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;

/**
 * Gives traces about all types "seen".
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class TraceProcessor extends Processor<TypeDeclaration, MutableTypeDeclaration> {
  public TraceProcessor() {
    super(null);
  }
  
  /**
   * Register new types, to be generated later.
   */
  public void register(final Map<String, Object> processingContext, final TypeDeclaration td, final RegisterGlobalsContext context) {
    String _qualifiedName = td.getQualifiedName();
    this.processorUtil.warn(TraceProcessor.class, "register", td, _qualifiedName);
  }
  
  /**
   * Transform types, new or old.
   */
  public void transform(final Map<String, Object> processingContext, final MutableTypeDeclaration mtd, final TransformationContext context) {
    String _qualifiedName = mtd.getQualifiedName();
    String _plus = ("transform: " + _qualifiedName);
    String _plus_1 = (_plus + "\n ");
    String _describeTypeDeclaration = this.processorUtil.describeTypeDeclaration(mtd, context);
    String _plus_2 = (_plus_1 + _describeTypeDeclaration);
    this.processorUtil.warn(TraceProcessor.class, "transform", mtd, _plus_2);
  }
  
  /**
   * Generate new types, registered earlier.
   */
  public void generate(final Map<String, Object> processingContext, final TypeDeclaration td, final CodeGenerationContext context) {
    String _qualifiedName = td.getQualifiedName();
    this.processorUtil.warn(TraceProcessor.class, "generate", td, _qualifiedName);
  }
}
