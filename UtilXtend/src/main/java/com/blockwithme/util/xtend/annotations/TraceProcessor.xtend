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

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.Map
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration

/**
 * Annotation for "traits"
 *
 * @author monster
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Inherited
@Active(MagicAnnotationProcessor)
annotation Trace {}

/**
 * Gives traces about all types "seen".
 *
 * @author monster
 */
class TraceProcessor extends Processor<TypeDeclaration, MutableTypeDeclaration> {

	new() {
		super(null)
	}

	/** Register new types, to be generated later. */
	override void register(Map<String,Object> processingContext, TypeDeclaration td, RegisterGlobalsContext context) {
		warn(TraceProcessor, "register", td, td.qualifiedName)
	}

	/** Transform types, new or old. */
	override void transform(Map<String,Object> processingContext, MutableTypeDeclaration mtd, TransformationContext context) {
		warn(TraceProcessor, "transform", mtd, "transform: "+mtd.qualifiedName+"\n "
			+mtd.describeTypeDeclaration(context))
	}

	/** Generate new types, registered earlier. */
	override void generate(Map<String,Object> processingContext, TypeDeclaration td, CodeGenerationContext context) {
		warn(TraceProcessor, "generate", td, td.qualifiedName)
	}
}