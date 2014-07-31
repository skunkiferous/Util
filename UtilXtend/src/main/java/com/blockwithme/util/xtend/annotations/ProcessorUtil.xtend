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

import java.io.PrintWriter
import java.io.StringWriter
import java.lang.annotation.Annotation
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Collections
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Objects
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtend.core.macro.declaration.AbstractElementImpl
import org.eclipse.xtend.core.macro.declaration.CompilationUnitImpl
import org.eclipse.xtend.core.macro.declaration.ExpressionImpl
import org.eclipse.xtend.core.xtend.XtendConstructor
import org.eclipse.xtend.core.xtend.XtendEnumLiteral
import org.eclipse.xtend.core.xtend.XtendField
import org.eclipse.xtend.core.xtend.XtendFunction
import org.eclipse.xtend.core.xtend.XtendMember
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration
import org.eclipse.xtend.core.xtend.impl.XtendVariableDeclarationImpl
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit
import org.eclipse.xtend.lib.macro.declaration.ConstructorDeclaration
import org.eclipse.xtend.lib.macro.declaration.Element
import org.eclipse.xtend.lib.macro.declaration.EnumerationTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.ExecutableDeclaration
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration
import org.eclipse.xtend.lib.macro.declaration.InterfaceDeclaration
import org.eclipse.xtend.lib.macro.declaration.MemberDeclaration
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableConstructorDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableInterfaceDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.NamedElement
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration
import org.eclipse.xtend.lib.macro.declaration.Type
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeParameterDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeReference
import org.eclipse.xtend.lib.macro.services.ProblemSupport
import org.eclipse.xtend.lib.macro.services.Tracability
import org.eclipse.xtend.lib.macro.services.TypeReferenceProvider
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.lib.Functions.Function1
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions
import org.eclipse.xtend.core.macro.declaration.JvmTypeDeclarationImpl
import org.eclipse.xtend.lib.macro.services.AnnotationReferenceProvider
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1
import org.eclipse.xtend.lib.macro.services.AnnotationReferenceBuildContext

/**
 * Helper methods for active annotation processing.
 *
 * @author monster
 */
class ProcessorUtil implements TypeReferenceProvider, AnnotationReferenceProvider {
	static val TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS ")

	/** Debug output? */
	private static val DEBUG = false

	/** Output errors as warnings? */
	private static val ERROR_AS_WARNING = false

	/**
	 * The processed NamedElement
	 * (According to the Active Annotation API... We don't care
	 * about it, but we are forced to use it to do logging)
	 */
	var NamedElement element

	/** The ProblemSupport, for logging */
	var ProblemSupport problemSupport

	/** The CompilationUnitImpl that is the "cache key" */
	var CompilationUnitImpl compilationUnit

	/** The processed file */
	var String file

	/** The phase */
	var String phase

	/** A default prefix for cache get/put. */
	var String prefix

	/** The anti-class-loader cache */
//	var Map<String,Object> cache

	/** The List TypeReference */
	var TypeReference list

	/** The Arrays TypeReference */
	var TypeReference arrays

	/** The Objects TypeReference */
	var TypeReference objects

	/** The Override Annotation type */
	var Type _override

	/** The type cache */
	val types = new HashMap<String,TypeDeclaration>

	/** The direct parent cache */
	val directParents = new HashMap<TypeDeclaration,Iterable<? extends TypeDeclaration>>

	/** The parent cache */
	val parents = new HashMap<TypeDeclaration,Iterable<? extends TypeDeclaration>>

	/** The JvmDeclaredTypes cache */
	var Iterable<? extends MutableTypeDeclaration> jvmDeclaredTypes

	/** The XtendTypeDeclarations cache */
	var Iterable<? extends TypeDeclaration> xtendTypeDeclarations

	/** The identityCache of the compilationUnit */
	var Map<EObject, Object> identityCache = null

	/** Remembers what type should be an interface */
	static val SHOULD_BE_INTERFACE = new HashSet<String>

	/** Sets the NamedElement currently being processed. */
	package final def void setElement(String phase, NamedElement element) {
		this.phase = phase
		val beforeCU = compilationUnit
		val beforeElement = this.element
		if (this.element !== element) {
			this.element = element
			if (element !== null) {
				compilationUnit = element.compilationUnit as CompilationUnitImpl
			} else {
				compilationUnit = null
			}
			if (beforeCU !== compilationUnit) {
				types.clear()
				directParents.clear()
				parents.clear()
				jvmDeclaredTypes = null
				xtendTypeDeclarations = null
				if (element != null) {
					problemSupport = compilationUnit.problemSupport
					file = compilationUnit.filePath.toString
//					cache = AntiClassLoaderCache.getCache()
					prefix = file+"/"+element.qualifiedName+"/"
					val typeReferenceProvider = compilationUnit.typeReferenceProvider
					list = Objects.requireNonNull(typeReferenceProvider.newTypeReference(List), "newTypeReference(List)")
					arrays = Objects.requireNonNull(typeReferenceProvider.newTypeReference(Arrays), "newTypeReference(Arrays)")
					objects = Objects.requireNonNull(typeReferenceProvider.newTypeReference(Objects), "newTypeReference(Objects)")
					_override = Objects.requireNonNull(compilationUnit.typeLookup.findTypeGlobally(Override), "typeLookup.findTypeGlobally(Override)")
				} else {
					problemSupport = null
					file = null
//					cache = null
					prefix = null
					list = null
					arrays = null
					objects = null
					_override = null
				}
			}
		}
	}

	/** Returns a String version of the current time. */
	private static def time() {
		TIME_FORMAT.format(new Date())
	}

	/** Returns the CompilationUnit of those elements */
	static def CompilationUnitImpl getCompilationUnit(
		List<? extends NamedElement> annotatedSourceElements) {
		if (!annotatedSourceElements.empty) {
			val element = annotatedSourceElements.get(0)
			return element.compilationUnit as CompilationUnitImpl
		}
		null
	}

	/** The CompilationUnitImpl that is the "cache key" */
	final def getCompilationUnit() {
		compilationUnit
	}

	/** The processed file */
	final def getFile() {
		file
	}

	/** The phase */
	final def getPhase() {
		phase
	}

	/** Should this type be an interface? */
	def getShouldBeInterface(String qualifiedName) {
		SHOULD_BE_INTERFACE.contains(qualifiedName)
	}

	/** Should this type be an interface? */
	def setShouldBeInterface(String qualifiedName) {
		if (SHOULD_BE_INTERFACE.add(qualifiedName)) {
			debug(ProcessorUtil, "setShouldBeInterface", null, "setShouldBeInterface("+qualifiedName+")")
		}
	}

	/** Returns the top-level Xtend types of this CompilationUnit */
	final def Iterable<? extends TypeDeclaration> getXtendTypes() {
		if (xtendTypeDeclarations === null) {
			xtendTypeDeclarations = compilationUnit.xtendFile.xtendTypes
				.map[compilationUnit.toXtendTypeDeclaration(it)]
			for (x : xtendTypeDeclarations) {
				if (x instanceof InterfaceDeclaration) {
					setShouldBeInterface(x.qualifiedName)
				}
			}
		}
		xtendTypeDeclarations
	}

	/** Returns the top-level mutable types of this CompilationUnit */
	final def Iterable<? extends MutableTypeDeclaration> getMutableTypes() {
		if (jvmDeclaredTypes === null) {
			// For the side-effect of initializing SHOULD_BE_INTERFACE
			getXtendTypes()

			val tmp = compilationUnit.xtendFile.eResource.contents.filter(JvmDeclaredType)
			jvmDeclaredTypes = tmp.map[
				var triedToFix = false
				var Boolean isInterface = null
				if (it instanceof JvmGenericType) {
					isInterface = it.interface
					if (!it.interface) {
						if (getShouldBeInterface(it.qualifiedName)) {
							it.interface = true
							triedToFix = true
						}
					} else {
						setShouldBeInterface(it.qualifiedName)
					}
				}
				var td = compilationUnit.toTypeDeclaration(it)
				if (getShouldBeInterface(td.qualifiedName) && !(td instanceof InterfaceDeclaration)) {
					if (identityCache === null) {
						identityCache = new ReflectExtensions().get(compilationUnit, "identityCache")
							as Map<EObject, Object>
					}
					identityCache.remove(it)
					td = compilationUnit.toTypeDeclaration(it)
					if (!(td instanceof InterfaceDeclaration)) {
						error(ProcessorUtil, "getMutableTypes", null, "ShouldBeInterfaceButIsnt: "
							+td.qualifiedName+" triedToFix: "+triedToFix+" it.interface: "
							+isInterface+" it.qualifiedName: "+it.qualifiedName)
					}
				}
				td as MutableTypeDeclaration
			]
		}
		jvmDeclaredTypes
	}

	/** Recursively returns the Xtend types of this CompilationUnit */
	final def Iterable<? extends TypeDeclaration> getAllXtendTypes() {
		doRecursivelyN(getXtendTypes()) [
			((it.declaredClasses + it.declaredInterfaces) as Iterable) as Iterable<? extends TypeDeclaration>
		]
	}

	/** Recursively returns the top-level mutable types of this CompilationUnit */
	final def Iterable<? extends MutableTypeDeclaration> getAllMutableTypes() {
		(doRecursivelyN(getMutableTypes()) [
			((it.declaredClasses + it.declaredInterfaces) as Iterable) as Iterable<? extends TypeDeclaration>
		] as Iterable) as Iterable<? extends MutableTypeDeclaration>
	}

	/** Sometimes, Xtend "forgets" to set the "isInterface" flag on types! */
	private def fixInterface(JvmType type, boolean isInterface) {
		if (type instanceof JvmGenericTypeImpl) {
			// Horrible, horrible hack!
			if (!type.isInterface) {
				type.setInterface(isInterface)
			}
		}
	}

	/**
	 * Lookup a type by name.
	 *
	 * @param td	the type that needs the lookup.
	 * @param isInterface	should the searched-for type be an interface? (Workaround for Xtend issue)
	 * @param typeName	The name of the type we are looking for.
	 */
	private def TypeDeclaration lookup(TypeDeclaration td,
		boolean isInterface, String typeName) {
		var result = types.get(typeName)
		if (result === null) {
			result = lookup2(td, isInterface, typeName)
			types.put(typeName, result)
		}
		result
	}

	/**
	 * Lookup a type by name.
	 *
	 * @param td	the type that needs the lookup.
	 * @param isInterface	should the searched-for type be an interface? (Workaround for Xtend issue)
	 * @param typeName	The name of the type we are looking for.
	 */
	private def TypeDeclaration lookup2(TypeDeclaration td,
		boolean isInterface, String typeName) {
		val index = typeName.indexOf("<")
		val typeName2 = if (index > 0) typeName.substring(0,index) else typeName
		val compilationUnit = td.compilationUnit as CompilationUnitImpl
		val parentIsJvmType = (td instanceof JvmType) || (td instanceof JvmTypeDeclarationImpl)
		if (parentIsJvmType) {
			val tmp = getMutableTypes().findFirst[it.qualifiedName == typeName2]
			if (tmp !== null) {
				return tmp
			}
//			if (jvmDeclaredTypes === null) {
//				jvmDeclaredTypes = compilationUnit.xtendFile.eResource.contents.filter(JvmDeclaredType)
//			}
//			for (t : jvmDeclaredTypes) {
//				if (t.qualifiedName == typeName) {
//					fixInterface(t, isInterface)
//					return compilationUnit.toTypeDeclaration(t)
//				}
//			}
		} else {
			val tmp = getXtendTypes().findFirst[it.qualifiedName == typeName2]
			if (tmp !== null) {
				return tmp
			}
		}
		// Not found. Maybe it lives outside the file?
		val foreign = compilationUnit.typeReferences.findDeclaredType(typeName2, compilationUnit.xtendFile)
		if (foreign == null) {
			val foreign2 = findTypeGlobally(typeName2)
			if (foreign2 instanceof TypeDeclaration) {
				return foreign2
			}
			// Ouch!
			throw new IllegalStateException("Could not find parent type "+typeName2+" of type "+td.qualifiedName)
		} else {
			fixInterface(foreign, isInterface)
			return compilationUnit.toType(foreign) as TypeDeclaration
		}
	}

	/** Converts TypeReferences to TypeDeclarations */
	private def Iterable<? extends TypeDeclaration> convert(
		TypeDeclaration td, boolean isInterface, Iterable<? extends TypeReference> refs) {
		refs.map[lookup(td, isInterface, localQualifiedName(it))]
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def String localQualifiedName(TypeReference element) {
		var result = element.name
		if (result == element.simpleName) {
			val pattern = "."+result
			for (d : compilationUnit.xtendFile.importSection.importDeclarations) {
				if (d.importedTypeName.endsWith(pattern)) {
					// Last one wins
					result = d.importedTypeName
				}
			}
		}
		result
	}

	/** Returns the direct parents */
	final def Iterable<? extends TypeDeclaration> findDirectParents(TypeDeclaration td) {
		var result = directParents.get(Objects.requireNonNull(td, "td"))
		if (result === null) {
			result = findDirectParents2(td)
			directParents.put(td, result)
		}
		result
	}

	/** Returns the direct parents */
	private dispatch def Iterable<? extends TypeDeclaration> findDirectParents2(TypeDeclaration td) {
		Collections.emptyList
	}

	/** Returns the direct parents */
	private dispatch def Iterable<? extends TypeDeclaration> findDirectParents2(ClassDeclaration td) {
		val result = <TypeDeclaration>newArrayList()
		result.addAll(convert(td, true, td.implementedInterfaces))
		val extendedClass = td.extendedClass
		if ((extendedClass !== null) && !extendedClass.anyType) {
			try {
				result.addAll(convert(td, false, Collections.singleton(extendedClass)))
			} catch (RuntimeException ex) {
				error(ProcessorUtil, "findDirectParents2", td, "Problems with "+extendedClass, ex)
			}
		}
		result
	}

	/** Returns the direct parents */
	private dispatch def Iterable<? extends TypeDeclaration> findDirectParents2(InterfaceDeclaration td) {
		convert(td, true, td.extendedInterfaces)
	}

	/** Returns the direct parents */
	private dispatch def Iterable<? extends TypeDeclaration> findDirectParents2(EnumerationTypeDeclaration td) {
		// TODO: td.implementedInterfaces is not implemented in EnumerationTypeDeclaration yet!
		Collections.emptyList
	}

	/** Returns the all parents, *including the type itself* */
	final def Iterable<? extends TypeDeclaration> findParents(TypeDeclaration td) {
		var result = parents.get(Objects.requireNonNull(td, "td"))
		if (result === null) {
			result = findParents2(td)
			parents.put(td, result)
		}
		result
	}

	/** Performs some operation recursively on the TypeDeclaration. */
	final def Iterable<TypeDeclaration> doRecursively1(TypeDeclaration td,
		Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> lambda) {
		doRecursivelyN(Collections.singleton(td), lambda)
	}

	/** Performs some operation recursively on the TypeDeclarations. */
	final def Iterable<TypeDeclaration> doRecursivelyN(
		Iterable<? extends TypeDeclaration> tds,
		Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> lambda) {
		val todo = <TypeDeclaration>newArrayList()
		val done = <TypeDeclaration>newArrayList()
		todo.addAll(tds)
		while (!todo.empty) {
			val next = todo.remove(0)
			done.add(next)
			for (parent : lambda.apply(next)) {
				if (!todo.contains(parent) && !done.contains(parent)) {
					todo.add(parent)
				}
			}
		}
		done
	}

	/** Returns the all parents, *including the type itself* */
	private def Iterable<? extends TypeDeclaration> findParents2(TypeDeclaration td) {
		doRecursively1(td) [ findDirectParents(it) ]
	}

	/** Does the given type directly bares the desired annotation? */
	final def hasDirectAnnotation(TypeDeclaration td, String annotationName) {
		td.annotations.exists[annotationTypeDeclaration.qualifiedName == annotationName]
	}

	/** Does the given type directly bares the desired annotation? */
	final def hasDirectAnnotation(TypeDeclaration td, Class<? extends Annotation> annotation) {
		hasDirectAnnotation(td, annotation.name)
	}

	/** Returns the name, annotations, base class and implemented interfaces of the type (not the content). */
	final def String describeTypeDeclaration(TypeDeclaration td, extension Tracability tracability) {
		Objects.requireNonNull(td, "td")
		val td2 = findXtend(td)
		val aei = td2 as AbstractElementImpl
		'''
		simpleName >> «td2.simpleName»
		toString  >> «td2»
		file  >> «td2.compilationUnit.filePath»
		generated >> «td2.generated»
		source >> «td2.source»
		primaryGeneratedJavaElement >> «td2.primaryGeneratedJavaElement»
		delegate class >> «aei.delegate.class»
		annotations >> «qualifiedNames(td2.annotations)»
		«IF td2 instanceof InterfaceDeclaration»
			implemented interfaces >> «qualifiedNames(td2.extendedInterfaces)»
		«ENDIF»
		«IF td2 instanceof ClassDeclaration»
			«IF td2.extendedClass !== null»
				super >> «td2.extendedClass.name»
			«ENDIF»
			implemented interfaces >> «qualifiedNames(td2.implementedInterfaces)»
		«ENDIF»
		---------------------------------
		«FOR m : td2.declaredMethods»
			«m.describeMethod(tracability)»
			-------------
		«ENDFOR»
		---------------------------------
		«FOR f : td2.declaredFields»
			«f.describeField(tracability)»
			-------------
		«ENDFOR»
		'''
	}

	/** Returns a long trace of all the info of that method. */
	final def String describeMethod(MethodDeclaration m, extension Tracability tracability) {
		Objects.requireNonNull(m, "m")
		val bdy =  m.body as ExpressionImpl

		'''
		simpleName >> «m.simpleName»
		signature  >> «signature(m)»
		toString  >> «bdy»
		«IF bdy !== null /* Abstract methods do not have a body! */»
		file  >> «bdy.compilationUnit.filePath»
		«ELSE»
		file  >> null
		«ENDIF»
		generated >> «m.generated»
		source >> «m.source»
		primaryGeneratedJavaElement >> «m.primaryGeneratedJavaElement»
		«IF bdy !== null /* Abstract methods do not have a body! */»
		delegate class >> «bdy.delegate.class»
		«ELSE»
		delegate class >> null
		«ENDIF»
		annotations >> «qualifiedNames(m.annotations)»
		---------------------------------
		«IF bdy !== null /* Abstract methods do not have a body! */»
			«FOR e : (bdy.delegate as XBlockExpression).expressions»
				expression >> «e»
				expression class>> «e.class»
				«IF e instanceof XtendVariableDeclarationImpl»
					simpleName >> « e.simpleName»
					qualifiedName >> « e.qualifiedName»
					right >> « e.right»
					«IF e.type !== null»
						type qualifiedName >> « e.type.qualifiedName»
						type identifier >> « e.type.identifier»
					«ENDIF»
					value >> « e.name»
					-------------------
				«ENDIF»
				-------------
			«ENDFOR»
		«ENDIF»
		'''
	}

	/** Returns a long trace of all the info of that field. */
	final def String describeField(FieldDeclaration f, extension Tracability tracability) {
		Objects.requireNonNull(f, "f")
		val bdy =  f.initializer as ExpressionImpl
		var type = f.type
		if (type === null) {
			type = (f.primaryGeneratedJavaElement as FieldDeclaration).type
		}
		'''
		simpleName >> «f.simpleName»
		type >> «qualifiedName(type)»
		«IF bdy !== null /* Abstract methods do not have a body! */»
		toString  >> «bdy»
		file  >> «bdy.compilationUnit.filePath»
		«ELSE»
		toString  >> null
		file  >> null
		«ENDIF»
		generated >> «f.generated»
		source >> «f.source»
		primaryGeneratedJavaElement >> «f.primaryGeneratedJavaElement»
		«IF bdy !== null /* Abstract methods do not have a body! */»
		delegate class >> «bdy.delegate.class»
		«ELSE»
		delegate class >> null
		«ENDIF»
		annotations >> «qualifiedNames(f.annotations)»
		'''
	}

	/** Returns the signature of a method/constructor as a string */
	final def String signature(ExecutableDeclaration it) {
		'''«simpleName»(«parameters.map[p|p.type].join(",")[name]»)'''
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(Void element) {
		null
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(Element element) {
		throw new IllegalArgumentException(element.class+" cannot be processed")
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(TypeReference element) {
		element.name
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(Type element) {
		element.getQualifiedName()
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(AnnotationReference element) {
		element.annotationTypeDeclaration.getQualifiedName()
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(CompilationUnit element) {
		element.getFilePath().toString
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(MemberDeclaration element) {
		if (element instanceof Type) {
			return element.getQualifiedName()
		}
		Objects.requireNonNull(element.declaringType,element+".declaringType")
		element.declaringType.qualifiedName+"."+element.simpleName
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(ParameterDeclaration element) {
		Objects.requireNonNull(element.getDeclaringExecutable(),element+".getDeclaringExecutable()")
		element.getDeclaringExecutable().qualifiedName+"."+element.simpleName
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(TypeParameterDeclaration element) {
		Objects.requireNonNull(element.getTypeParameterDeclarator(),element+".getTypeParameterDeclarator()")
		element.getTypeParameterDeclarator().qualifiedName+"."+element.simpleName
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static dispatch String qualifiedName(XtendMember element) {
		if (element instanceof XtendTypeDeclaration) {
			return element.getName()
		}
		Objects.requireNonNull(element.declaringType,element+".declaringType")
		element.declaringType.getName()+"."+element.simpleName
	}

	/** Tries to find and return the qualifiedName of the given element. */
	def static String qualifiedNames(Iterable<?> elements) {
		elements.map[qualifiedName].toString
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(Void element) {
		null
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(XtendConstructor element) {
		element.declaringType.name
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(XtendField element) {
		element.name
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(XtendEnumLiteral element) {
		element.name
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(XtendFunction element) {
		element.name
	}

	/** Tries to find and return the simpleName of the given XtendMember. */
	def static dispatch String simpleName(XtendTypeDeclaration element) {
		val qualifiedName = element.name
		val char dot = '.'
		val index = qualifiedName.lastIndexOf(dot)
		if (index < 0)
			return qualifiedName
		return qualifiedName.substring(index+1)
	}

	/** Tries to find "non-Xtend" matching type */
	final def TypeDeclaration findNonXtend(TypeDeclaration td) {
		if (td instanceof XtendTypeDeclaration) {
			val name = td.qualifiedName
			val result = mutableTypes.findFirst[qualifiedName == name]
			if (result !== null) {
				return result
			}
		}
		return td
	}

	/** Tries to find Xtend matching type */
	final def TypeDeclaration findXtend(TypeDeclaration td) {
		if (td instanceof MutableTypeDeclaration) {
			val name = td.qualifiedName
			val result = xtendTypes.findFirst[qualifiedName == name]
			if (result !== null) {
				return result
			}
		}
		return td
	}

	/** Builds the standard log message format */
	private def buildMessage(boolean debug, Class<?> who, String where, String message, Throwable t) {
		var phase = if (debug) "DEBUG: "+this.phase else phase
		if (phase === null) {
			phase = "phase?"
		}
		var msg = "null"
		if (message !== null)
			msg = message.replaceAll("com.blockwithme.util.xtend.annotations.","cbuxa.")
				.replaceAll("com.blockwithme.util.xtend.","cbux.")
		if (t != null) {
			msg = msg+"\n"+asString(t)
		}
		var who2 = "who?"
		if (who !== null) {
			who2 = who.simpleName
		}
		ProcessorUtil.time+phase+": "+who2+"."+where+": "+msg
	}

	/** Returns the element to use when logging */
	private def Element extractLoggingElement(Element what) {
		if ((what !== null) && !(what instanceof CompilationUnit)) {
			if (what instanceof AbstractElementImpl) {
				val element = what as AbstractElementImpl<? extends EObject>
				val resource = element.delegate.eResource
				if (resource == compilationUnit.xtendFile.eResource) {
					return what
				} else {
					// Wrong file!?!
//					throw new IllegalArgumentException("Element.delegate.eResource: "+resource
//						+" Expected: "+compilationUnit.xtendFile.eResource
//					)
					return this.element
				}
			} else if (what instanceof XtendTypeDeclaration) {
				return findNonXtend(what as TypeDeclaration)
			} else {
				throw new IllegalArgumentException("Element type: "+what.class)
			}
		} else {
			element
		}
	}

	/** Converts a Throwable stack-trace to a String */
	final def asString(Throwable t) {
		val sw = new StringWriter()
		t.printStackTrace(new PrintWriter(sw))
		sw.toString
	}

	/**
	 * Records an error for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 */
	final def void error(Class<?> who, String where, Element what, String message) {
		val logElem = extractLoggingElement(what)
		if (ERROR_AS_WARNING) {
			problemSupport.addWarning(logElem, buildMessage(false, who, where, message, null))
		} else {
			problemSupport.addError(logElem, buildMessage(false, who, where, message, null))
		}
	}

	/**
	 * Records an error for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 * @param t The error
	 */
	final def void error(Class<?> who, String where, Element what, String message, Throwable t) {
		val logElem = extractLoggingElement(what)
		if (ERROR_AS_WARNING) {
			problemSupport.addWarning(logElem, buildMessage(false, who, where, message, t))
		} else {
			problemSupport.addError(logElem, buildMessage(false, who, where, message, t))
		}
	}

	/**
	 * Records a warning for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 */
	final def void warn(Class<?> who, String where, Element what, String message) {
		val logElem = extractLoggingElement(what)
		problemSupport.addWarning(logElem, buildMessage(false, who, where, message, null))
	}

	/**
	 * Records a warning for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 * @param t The error
	 */
	final def void warn(Class<?> who, String where, Element what, String message, Throwable t) {
		val logElem = extractLoggingElement(what)
		problemSupport.addWarning(logElem, buildMessage(false, who, where, message, t))
	}

	/**
	 * Records a warning for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 */
	final def void debug(Class<?> who, String where, Element what, String message) {
		if (DEBUG) {
			val logElem = extractLoggingElement(what)
			problemSupport.addWarning(logElem, buildMessage(true, who, where, message, null))
		}
	}

	/**
	 * Records a warning for the given element
	 *
	 * @param who The class in which the message was produced.
	 * @param where The method where message was produced.
	 * @param what The element to which associate the message
	 * @param message The message
	 * @param t The error.
	 */
	final def void debug(Class<?> who, String where, Element what, String message, Throwable t) {
		if (DEBUG) {
			val logElem = extractLoggingElement(what)
			problemSupport.addWarning(logElem, buildMessage(true, who, where, message, t))
		}
	}
//
//	/** Reads from the cache, using a specified prefix. */
//	final def Object get(String prefix, String key) {
//		cache.get(prefix+key)
//	}
//
//	/** Writes to the cache, using a specified prefix. */
//	final def Object put(String prefix, String key, Object newValue) {
//		if (newValue === null) {
//			cache.remove(prefix+key)
//		} else {
//			cache.put(prefix+key, newValue)
//		}
//	}
//
//	/** Reads from the cache, using the default prefix. */
//	final def Object get(String key) {
//		cache.get(prefix+key)
//	}
//
//	/** Writes to the cache, using the default prefix. */
//	final def Object put(String key, Object newValue) {
//		if (newValue === null) {
//			cache.remove(prefix+key)
//		} else {
//			cache.put(prefix+key, newValue)
//		}
//	}

	/** Returns true, if the given element is a marker interface */
	final def boolean isMarker(Element element) {
		if (element instanceof InterfaceDeclaration) {
			for (parent : findParents(element)) {
				if (parent != element) {
					if (!isMarker(parent)) {
						return false
					}
				}
			}
			return true
		}
		if (element instanceof TypeReference) {
			return isMarker(element.getType())
		}
		false
	}

	/** Utility method that removes generics type arguments from the class.qualifiedName */
	final def removeGeneric(String className) {
		val index = className.indexOf("<")
		if (index > 0)
			className.substring(0, index)
		else
			className
	}

	/** Utility method checks if a type is a String type. */
	final def isString(TypeReference fieldType) {
		getString().isAssignableFrom(fieldType)
	}

	/** Checks if fieldType is of 'java.util.List' type. */
	final def isList(TypeReference fieldType) {
		if (fieldType.actualTypeArguments.size > 0) {
			getList().isAssignableFrom(fieldType.name.removeGeneric.newTypeReference)
		} else
			getList().isAssignableFrom(fieldType)
	}

	final def Type findTypeGlobally(Class<?> type) {
		compilationUnit.typeLookup.findTypeGlobally(type)
	}

	final def Type findTypeGlobally(String typeName) {
	    switch (typeName) {
	      case "boolean": findTypeGlobally(Boolean.TYPE)
	      case "byte": findTypeGlobally(Byte.TYPE)
	      case "char": findTypeGlobally(Character.TYPE)
	      case "short": findTypeGlobally(Short.TYPE)
	      case "int": findTypeGlobally(Integer.TYPE)
	      case "float": findTypeGlobally(Float.TYPE)
	      case "long": findTypeGlobally(Long.TYPE)
	      case "double": findTypeGlobally(Double.TYPE)
	      case "void": findTypeGlobally(Void.TYPE)
	      default: compilationUnit.typeLookup.findTypeGlobally(typeName)
	    }
	}

	/** Utility method that finds an interface in the global context, returns null if not found */
	def findInterface(String name) {
		val found = findTypeGlobally(name)
		if (found instanceof MutableInterfaceDeclaration)
			found as MutableInterfaceDeclaration
		else
			null
	}

	/** Utility method that finds an interface in the global context, fails if not found */
	def getInterface(String name) {
		val result = findInterface(name)
		if (result === null) {
			throw new MissingTypeException(name)
		}
		result
	}

	/** Utility method that finds a class in the global context, returns null if not found */
	def MutableClassDeclaration findClass(String name) {
		val found = findTypeGlobally(name)
		if (found instanceof MutableClassDeclaration)
			found as MutableClassDeclaration
		else
			null
	}

	/** Utility method that finds a class in the global context, fails if not found */
	def MutableClassDeclaration getClass(String name) {
		val result = findClass(name)
		if (result === null) {
			throw new MissingTypeException(name)
		}
		result
	}

	/** Searches for the *default* constructor. */
	final def ConstructorDeclaration findConstructor(TypeDeclaration clazz) {
		for (c : clazz.declaredConstructors) {
			if (c.parameters.isEmpty) {
				return c
			}
		}
		null
	}

	/** Searches for the *default* constructor. */
	final def MutableConstructorDeclaration findConstructor(MutableClassDeclaration clazz) {
		findConstructor(clazz as TypeDeclaration) as MutableConstructorDeclaration
	}

	/** Searches for the method with the given name and parameters. */
	final def MethodDeclaration findMethod(TypeDeclaration clazz,
		String name, TypeReference ... parameterTypes) {
		val tmp = parameterTypes.toList
		for (m : clazz.declaredMethods) {
			if ((m.simpleName == name) && (m.parameters.toList == tmp)) {
				return m
			}
		}
		null
	}

	/** Searches for the method with the given name and parameters. */
	final def MutableMethodDeclaration findMethod(MutableClassDeclaration clazz,
		String name, TypeReference ... parameterTypes) {
		findMethod(clazz as TypeDeclaration, name, parameterTypes) as MutableMethodDeclaration
	}

	/** The List TypeReference */
	final def getList() {
		list
	}

	/** The Arrays TypeReference */
	final def getArrays() {
		arrays
	}

	/** The Objects TypeReference */
	final def getObjects() {
		objects
	}

	/** The Override Annotation Type */
	final def getOverride() {
		_override
	}

	override getAnyType() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getAnyType(), "getAnyType()")
	}

	override getList(TypeReference param) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getList(param), "getList(param)")
	}

	override getObject() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getObject(), "getObject()")
	}

	override getPrimitiveBoolean() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveBoolean(), "getPrimitiveBoolean()")
	}

	override getPrimitiveByte() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveByte(), "getPrimitiveByte()")
	}

	override getPrimitiveChar() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveChar(), "getPrimitiveChar()")
	}

	override getPrimitiveDouble() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveDouble(), "getPrimitiveDouble()")
	}

	override getPrimitiveFloat() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveFloat(), "getPrimitiveFloat()")
	}

	override getPrimitiveInt() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveInt(), "getPrimitiveInt()")
	}

	override getPrimitiveLong() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveLong(), "getPrimitiveLong()")
	}

	override getPrimitiveShort() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveShort(), "getPrimitiveShort()")
	}

	override getPrimitiveVoid() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getPrimitiveVoid(), "getPrimitiveVoid()")
	}

	override getSet(TypeReference param) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getSet(param), "getSet(param)")
	}

	override getString() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.getString(), "getString()")
	}

	def getClassTypeRef() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newTypeReference(Class), "newTypeReference(Class)")
	}

	override newArrayTypeReference(TypeReference componentType) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newArrayTypeReference(componentType), "newArrayTypeReference("+componentType+")")
	}

	override newTypeReference(String typeName, TypeReference... typeArguments) {
		if ((typeArguments === null) || (typeArguments.length == 0)) {
		    return switch (typeName) {
		      case "void": compilationUnit.typeReferenceProvider.primitiveVoid
		      case "boolean": compilationUnit.typeReferenceProvider.primitiveBoolean
		      case "byte": compilationUnit.typeReferenceProvider.primitiveByte
		      case "char": compilationUnit.typeReferenceProvider.primitiveChar
		      case "short": compilationUnit.typeReferenceProvider.primitiveShort
		      case "int": compilationUnit.typeReferenceProvider.primitiveInt
		      case "float": compilationUnit.typeReferenceProvider.primitiveFloat
		      case "long": compilationUnit.typeReferenceProvider.primitiveLong
		      case "double": compilationUnit.typeReferenceProvider.primitiveDouble
		      default: Objects.requireNonNull(compilationUnit.typeReferenceProvider.newTypeReference(typeName, typeArguments), "newTypeReference("+typeName+")")
		    }
		}
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newTypeReference(typeName, typeArguments), "newTypeReference("+typeName+","+newArrayList(typeArguments)+")")
	}

	override newTypeReference(Type typeDeclaration, TypeReference... typeArguments) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newTypeReference(typeDeclaration, typeArguments), "newTypeReference("+typeDeclaration+")")
	}

	override newTypeReference(Class<?> clazz, TypeReference... typeArguments) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newTypeReference(clazz, typeArguments), "newTypeReference("+clazz+")")
	}

	override newWildcardTypeReference() {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newWildcardTypeReference(), "newWildcardTypeReference()")
	}

	override newWildcardTypeReference(TypeReference upperBound) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newWildcardTypeReference(upperBound), "newWildcardTypeReference(upperBound)")
	}

	override newWildcardTypeReferenceWithLowerBound(TypeReference lowerBound) {
		Objects.requireNonNull(compilationUnit.typeReferenceProvider.newWildcardTypeReferenceWithLowerBound(lowerBound), "newWildcardTypeReferenceWithLowerBound(lowerBound)")
	}

	def TypeReference newTypeReferenceWithGenerics(String typeName) {
		if (typeName.endsWith(">")) {
			val list = <TypeReference>newArrayList()
			val index = typeName.indexOf("<")
			val typeParams = typeName.substring(index+1, typeName.length-1)
			for (t : typeParams.split(",")) {
				val t2 = t.trim()
				if (!t2.empty) {
					list.add(newTypeReferenceWithGenerics(t2))
				}
			}
			return newTypeReference(typeName.substring(0,index), list)
		}
		newTypeReference(typeName)
	}

	override newAnnotationReference(String annotationTypeName) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationTypeName))
	}

	override newAnnotationReference(Type annotationTypeDelcaration) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationTypeDelcaration))
	}

	override newAnnotationReference(Class<?> annotationClass) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationClass))
	}

	override newAnnotationReference(AnnotationReference annotationReference) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationReference))
	}

	override newAnnotationReference(String annotationTypeName, Procedure1<AnnotationReferenceBuildContext> initializer) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationTypeName, initializer))
	}

	override newAnnotationReference(Type annotationTypeDelcaration, Procedure1<AnnotationReferenceBuildContext> initializer) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationTypeDelcaration, initializer))
	}

	override newAnnotationReference(Class<?> annotationClass, Procedure1<AnnotationReferenceBuildContext> initializer) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationClass, initializer))
	}

	override newAnnotationReference(AnnotationReference annotationReference, Procedure1<AnnotationReferenceBuildContext> initializer) {
		Objects.requireNonNull(compilationUnit.annotationReferenceProvider.newAnnotationReference(annotationReference, initializer))
	}

}
