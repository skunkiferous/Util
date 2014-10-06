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

import com.blockwithme.util.xtend.annotations.AndFilter;
import com.blockwithme.util.xtend.annotations.AnnotatedFilter;
import com.blockwithme.util.xtend.annotations.Filter;
import com.blockwithme.util.xtend.annotations.IsAnnotationFilter;
import com.blockwithme.util.xtend.annotations.IsClassFilter;
import com.blockwithme.util.xtend.annotations.IsEnumFilter;
import com.blockwithme.util.xtend.annotations.IsInterfaceFilter;
import com.blockwithme.util.xtend.annotations.NotFilter;
import com.blockwithme.util.xtend.annotations.OrFilter;
import com.blockwithme.util.xtend.annotations.ParentFilter;
import com.blockwithme.util.xtend.annotations.ProcessorUtil;
import com.google.common.base.Objects;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.util.Map;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Base class for our own Processors (annotation-bound or not).
 * 
 * The actual processor instances must have a public no-argument constructor.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class Processor<T extends TypeDeclaration, M extends MutableTypeDeclaration> {
  /**
   * processingContext key for the complete list of types in the compilation unit.
   */
  public final static String PC_ALL_FILE_TYPES = "PC_ALL_FILE_TYPES";
  
  /**
   * processingContext key for the list of not-yet-processed types in the compilation unit.
   */
  public final static String PC_TODO_TYPES = "PC_TODO_TYPES";
  
  /**
   * processingContext key for the list of processed types in the compilation unit.
   */
  public final static String PC_DONE_TYPES = "PC_DONE_TYPES";
  
  /**
   * processingContext key for the currently processed type in the compilation unit.
   */
  public final static String PC_PROCESSED_TYPE = "PC_PROCESSED_TYPE";
  
  /**
   * processingContext key for the package of the processed types in the compilation unit.
   */
  public final static String PC_PACKAGE = "PC_PACKAGE";
  
  /**
   * processingContext key for the complete list of processors.
   */
  public final static String PC_ALL_PROCESSORS = "PC_ALL_PROCESSORS";
  
  /**
   * processingContext key for the list of not-yet-called processors for the currently processed type.
   */
  public final static String PC_TODO_PROCESSORS = "PC_TODO_PROCESSORS";
  
  /**
   * processingContext key for the list of called processors for the currently processed type.
   */
  public final static String PC_DONE_PROCESSORS = "PC_DONE_PROCESSORS";
  
  /**
   * processingContext key for the currently called processor for the currently processed type.
   */
  public final static String PC_CURRENT_PROCESSOR = "PC_CURRENT_PROCESSOR";
  
  /**
   * The optional FIlter, used in accept()
   */
  private final Filter filter;
  
  /**
   * The "shared" ProcessorUtil instance
   */
  @Extension
  protected ProcessorUtil processorUtil;
  
  protected static Filter withAnnotation(final String name, final boolean inherited) {
    return new AnnotatedFilter(name, inherited);
  }
  
  protected static Filter withAnnotation(final Class<? extends Annotation> type) {
    String _name = type.getName();
    Annotation[] _annotations = type.getAnnotations();
    final Function1<Annotation, Boolean> _function = new Function1<Annotation, Boolean>() {
      public Boolean apply(final Annotation it) {
        Class<? extends Annotation> _annotationType = it.annotationType();
        return Boolean.valueOf((_annotationType == Inherited.class));
      }
    };
    boolean _exists = IterableExtensions.<Annotation>exists(((Iterable<Annotation>)Conversions.doWrapArray(_annotations)), _function);
    return Processor.withAnnotation(_name, _exists);
  }
  
  protected static Filter and(final Filter... filters) {
    return new AndFilter(filters);
  }
  
  protected static Filter or(final Filter... filters) {
    return new OrFilter(filters);
  }
  
  protected static Filter not(final Filter filter) {
    return new NotFilter(filter);
  }
  
  protected static Filter hasParent(final String qualifiedName) {
    return new ParentFilter(qualifiedName);
  }
  
  protected static Filter hasParent(final Class<?> parent) {
    String _name = parent.getName();
    return new ParentFilter(_name);
  }
  
  protected final static Filter isAnnotation = new IsAnnotationFilter();
  
  protected final static Filter isClass = new IsClassFilter();
  
  protected final static Filter isEnum = new IsEnumFilter();
  
  protected final static Filter isInterface = new IsInterfaceFilter();
  
  /**
   * Creates a processor with an *optional* filter.
   * If specified, the filter must return *true* to accept a type.
   */
  protected Processor(final Filter filter) {
    this.filter = filter;
  }
  
  /**
   * Returns the configuration as a string
   */
  protected String config() {
    return ("filter=" + this.filter);
  }
  
  public final String toString() {
    Class<? extends Processor> _class = this.getClass();
    String _name = _class.getName();
    String _plus = (_name + "(");
    String _config = this.config();
    String _plus_1 = (_plus + _config);
    return (_plus_1 + ")");
  }
  
  /**
   * Sets the fields
   */
  final void setProcessorUtil(final ProcessorUtil processorUtil) {
    this.processorUtil = processorUtil;
    boolean _notEquals = (!Objects.equal(processorUtil, null));
    if (_notEquals) {
      this.init();
    } else {
      this.deinit();
    }
  }
  
  /**
   * Called before processing a file.
   */
  public void init() {
  }
  
  /**
   * Called after processing a file.
   */
  public void deinit() {
  }
  
  /**
   * Returns true, if this type should be processed.
   */
  public boolean accept(final Map<String, Object> processingContext, final TypeDeclaration td) {
    boolean _or = false;
    boolean _tripleEquals = (this.filter == null);
    if (_tripleEquals) {
      _or = true;
    } else {
      boolean _apply = this.filter.apply(processingContext, this.processorUtil, td);
      _or = _apply;
    }
    return _or;
  }
  
  /**
   * Register new types, to be generated later.
   */
  public void register(final Map<String, Object> processingContext, final T td, final RegisterGlobalsContext context) {
  }
  
  /**
   * Transform types, new or old.
   */
  public void transform(final Map<String, Object> processingContext, final M mtd, final TransformationContext context) {
  }
  
  /**
   * Generate new types, registered earlier.
   */
  public void generate(final Map<String, Object> processingContext, final T td, final CodeGenerationContext context) {
  }
  
  /**
   * Called when the register phase for the current file is done.
   */
  public void afterRegister(final Map<String, Object> processingContext, final String pkgName, final RegisterGlobalsContext context) {
  }
  
  /**
   * Called when the transform phase for the current file is done.
   */
  public void afterTransform(final Map<String, Object> processingContext, final String pkgName, final TransformationContext context) {
  }
  
  /**
   * Called when the generate phase for the current file is done.
   */
  public void afterGenerate(final Map<String, Object> processingContext, final String pkgName, final CodeGenerationContext context) {
  }
}
