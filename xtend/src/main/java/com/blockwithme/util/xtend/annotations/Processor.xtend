/*
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
package com.blockwithme.util.xtend.annotations

import java.lang.annotation.Annotation
import java.lang.annotation.Inherited
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.EnumerationTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.InterfaceDeclaration
import java.util.Map

/** Filter used to allow composition of "requirements" in the accept() method of a Processor */
interface Filter {
	def boolean apply(Map<String,Object> processingContext, ProcessorUtil processorUtil, TypeDeclaration typeDeclaration)
}

/**
 * Filter for Annotations, that take the (optional) inherited nature of annotations into account.
 */
@Data
package class AnnotatedFilter implements Filter {
	String name
	boolean inherited

	private static def check(extension ProcessorUtil processorUtil, TypeDeclaration orig, TypeDeclaration td, String name) {
		hasDirectAnnotation(td, name)
	}

	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		if (inherited) {
			for (parent : findParents(td)) {
				if (check(processorUtil, td, parent, name)) {
					return true
				}
			}
			return false
		}
		check(processorUtil, td, td, name)
	}
}

/**
 * Combines Filters using the AND logic operator
 */
@Data
package class AndFilter implements Filter {
	Filter[] others

	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		for (f : others) {
			if (!f.apply(processingContext, processorUtil, td)) {
				return false
			}
		}
		true
	}
}

/**
 * Combines Filters using the OR logic operator
 */
@Data
package class OrFilter implements Filter {
	Filter[] others

	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		for (f : others) {
			if (f.apply(processingContext, processorUtil, td)) {
				return true
			}
		}
		false
	}
}

/**
 * Negates the result of another Filter
 */
@Data
package class NotFilter implements Filter {
	Filter other

	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		!other.apply(processingContext, processorUtil, td)
	}
}

/**
 * Accepts, if the validated type has the desired type as parent
 * (either base class or implemented interface)
 */
@Data
package class ParentFilter implements Filter {
	String parent

	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		for (p : processorUtil.findParents(td)) {
			if (p.qualifiedName == parent) {
				return true
			}
		}
		false
	}
}

/**
 * Returns true, if the TypeDeclaration is an annotation
 */
@Data
package class IsAnnotationFilter implements Filter {
	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		td instanceof AnnotationTypeDeclaration
	}
}

/**
 * Returns true, if the TypeDeclaration is a class
 */
@Data
package class IsClassFilter implements Filter {
	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		td instanceof ClassDeclaration
	}
}

/**
 * Returns true, if the TypeDeclaration is an Enum
 */
@Data
package class IsEnumFilter implements Filter {
	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		td instanceof EnumerationTypeDeclaration
	}
}

/**
 * Returns true, if the TypeDeclaration is an Interface
 */
@Data
package class IsInterfaceFilter implements Filter {
	override apply(Map<String,Object> processingContext, extension ProcessorUtil processorUtil, TypeDeclaration td) {
		td instanceof InterfaceDeclaration
	}
}

/**
 * Base class for our own Processors (annotation-bound or not).
 *
 * The actual processor instances must have a public no-argument constructor.
 *
 * @author monster
 */
class Processor<T extends TypeDeclaration, M extends MutableTypeDeclaration> {
	/** processingContext key for the complete list of types in the compilation unit.  */
	public static val PC_ALL_FILE_TYPES = "PC_ALL_FILE_TYPES"

	/** processingContext key for the list of not-yet-processed types in the compilation unit.  */
	public static val PC_TODO_TYPES = "PC_TODO_TYPES"

	/** processingContext key for the list of processed types in the compilation unit.  */
	public static val PC_DONE_TYPES = "PC_DONE_TYPES"

	/** processingContext key for the currently processed type in the compilation unit.  */
	public static val PC_PROCESSED_TYPE = "PC_PROCESSED_TYPE"

	/** processingContext key for the package of the processed types in the compilation unit.  */
	public static val PC_PACKAGE = "PC_PACKAGE"

	/** processingContext key for the complete list of processors.  */
	public static val PC_ALL_PROCESSORS = "PC_ALL_PROCESSORS"

	/** processingContext key for the list of not-yet-called processors for the currently processed type.  */
	public static val PC_TODO_PROCESSORS = "PC_TODO_PROCESSORS"

	/** processingContext key for the list of called processors for the currently processed type.  */
	public static val PC_DONE_PROCESSORS = "PC_DONE_PROCESSORS"

	/** processingContext key for the currently called processor for the currently processed type.  */
	public static val PC_CURRENT_PROCESSOR = "PC_CURRENT_PROCESSOR"

	/** The optional FIlter, used in accept() */
	val Filter filter

	/** The "shared" ProcessorUtil instance */
	protected var extension ProcessorUtil processorUtil

	protected static def Filter withAnnotation(String name, boolean inherited) {
		new AnnotatedFilter(name, inherited)
	}

	protected static def Filter withAnnotation(Class<? extends Annotation> type) {
		withAnnotation(type.name, type.annotations.exists[annotationType === Inherited])
	}

	protected static def Filter and(Filter ... filters) {
		new AndFilter(filters)
	}

	protected static def Filter or(Filter ... filters) {
		new OrFilter(filters)
	}

	protected static def Filter not(Filter filter) {
		new NotFilter(filter)
	}

	protected static def Filter hasParent(String qualifiedName) {
		new ParentFilter(qualifiedName)
	}

	protected static def Filter hasParent(Class<?> parent) {
		new ParentFilter(parent.name)
	}

	protected static val Filter isAnnotation = new IsAnnotationFilter

	protected static val Filter isClass = new IsClassFilter

	protected static val Filter isEnum = new IsEnumFilter

	protected static val Filter isInterface = new IsInterfaceFilter

	/**
	 * Creates a processor with an *optional* filter.
	 * If specified, the filter must return *true* to accept a type.
	 */
	protected new(Filter filter) {
		this.filter = filter
	}

	/** Returns the configuration as a string */
	protected def config() {
		"filter="+filter
	}

	override final toString() {
		class.name+"("+config+")"
	}

	/** Sets the fields */
	package final def void setProcessorUtil(ProcessorUtil processorUtil) {
		this.processorUtil = processorUtil
		if (processorUtil != null) {
			init()
		} else {
			deinit()
		}
	}

	/** Called before processing a file. */
	def void init() {
		// NOP
	}

	/** Called after processing a file. */
	def void deinit() {
		// NOP
	}

	/** Returns true, if this type should be processed. */
	def boolean accept(Map<String,Object> processingContext, TypeDeclaration td) {
		(filter === null) || filter.apply(processingContext, processorUtil,td)
	}

	/** Register new types, to be generated later. */
	def void register(Map<String,Object> processingContext, T td, RegisterGlobalsContext context) {
		// NOP
	}

	/** Transform types, new or old. */
	def void transform(Map<String,Object> processingContext, M mtd, TransformationContext context) {
		// NOP
	}

	/** Generate new types, registered earlier. */
	def void generate(Map<String,Object> processingContext, T td, CodeGenerationContext context) {
		// NOP
	}

	/** Called when the register phase for the current file is done. */
	def void afterRegister(Map<String,Object> processingContext, String pkgName, RegisterGlobalsContext context) {
		// NOP
	}

	/** Called when the transform phase for the current file is done. */
	def void afterTransform(Map<String,Object> processingContext, String pkgName, TransformationContext context) {
		// NOP
	}

	/** Called when the generate phase for the current file is done. */
	def void afterGenerate(Map<String,Object> processingContext, String pkgName, CodeGenerationContext context) {
		// NOP
	}
}