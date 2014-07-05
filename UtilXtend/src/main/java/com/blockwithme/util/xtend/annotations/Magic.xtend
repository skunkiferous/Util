package com.blockwithme.util.xtend.annotations

import com.blockwithme.util.xtend.AntiClassLoaderCache
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.xtend.core.macro.declaration.CompilationUnitImpl
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.CodeGenerationContext
import org.eclipse.xtend.lib.macro.CodeGenerationParticipant
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext
import org.eclipse.xtend.lib.macro.RegisterGlobalsParticipant
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.TransformationParticipant
import org.eclipse.xtend.lib.macro.declaration.Element
import org.eclipse.xtend.lib.macro.declaration.MutableNamedElement
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.NamedElement
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3

/**
 * Marks that *all types in this file* should be processed.
 *
 * @author monster
 */
@Active(MagicAnnotationProcessor)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
annotation Magic {
}

/**
 * Process classes annotated with @Magic
 *
 * Somehow, some of my code is executed *outside* of the active annotation processor
 * hook methods, and that code needs the cached state, so I cannot clear the cache
 * at the end of the API hook methods.
 *
 * @author monster
 */
class MagicAnnotationProcessor implements RegisterGlobalsParticipant<NamedElement>,
CodeGenerationParticipant<NamedElement>, TransformationParticipant<MutableNamedElement> {

	/** Cache key for the processor names */
	static val String PROCESSORS_NAMES = "PROCESSORS_NAMES"

	/** File containing the processor names */
	static val String PROCESSORS_NAMES_FILE = "META-INF/services/"+Processor.name

	/** The processors */
	static var Processor<?,?>[] PROCESSORS

	static val processorUtil = new ProcessorUtil

	static val ACCEPT = new HashMap<String,Boolean>

	private def <TD extends TypeDeclaration> void loop(
		List<? extends NamedElement> annotatedSourceElements,
		String phase, Procedure3<Map<String,Object>, Processor, TD> lambda,
		Procedure3<Map<String,Object>, Processor, String> lambdaAfter) {
		val compilationUnit = ProcessorUtil.getCompilationUnit(annotatedSourceElements)
		if (compilationUnit !== null) {
			val clazz = class
			// Must be called first, for logging correctly
			processorUtil.setElement(phase, annotatedSourceElements.get(0))
			// Manage "persistent" cache
			val pathName = compilationUnit.filePath.toString
			val register = ("register" == phase)
			val transform = ("transform" == phase)
			val generate = ("generate" == phase)
			if (!register) {
				AntiClassLoaderCache.clear(pathName+".register.")
			}
			if (!transform) {
				AntiClassLoaderCache.clear(pathName+".transform.")
			}
			if (!generate) {
				AntiClassLoaderCache.clear(pathName+".generate.")
			}
			// Lookup processors
			val processors = getProcessors(class, annotatedSourceElements)
			val allProcessors = newArrayList(processors)
			// Extract types to process from compilation unit (file)
			val allTypes = (if (transform) processorUtil.allMutableTypes else processorUtil.allXtendTypes).toList
			// "+1" causes the last value in types to be null, which indicates end-of-file
			val types = allTypes.toArray(<TypeDeclaration>newArrayOfSize(allTypes.size + 1))
			val todoTypes = new ArrayList(allTypes)
			val doneTypes = <TypeDeclaration>newArrayList()
			processorUtil.warn(clazz, "loop", null,
					"Top-Level Types: "+ProcessorUtil.qualifiedNames(allTypes))
			val processingContext = new HashMap<String,Object>()
			processingContext.put(Processor.PC_ALL_FILE_TYPES, allTypes)
			processingContext.put(Processor.PC_TODO_TYPES, todoTypes)
			processingContext.put(Processor.PC_DONE_TYPES, doneTypes)
			processingContext.put(Processor.PC_ALL_PROCESSORS, allProcessors)
			val pkgName = if (!allTypes.empty) {
				val qn = allTypes.get(0).qualifiedName
				val dot = qn.lastIndexOf(".")
				qn.substring(0, dot)
			} else ""
			processingContext.put(Processor.PC_PACKAGE, pkgName)
			processorUtil.debug(clazz, "loop", null,
				"IMPORTS: "+compilationUnit.xtendFile.importSection.importDeclarations.map[importedTypeName])
			for (t : compilationUnit.xtendFile.importSection.importDeclarations.map[importedTypeName]) {
				if (processorUtil.findTypeGlobally(t) === null) {
					var found = false
					try {
						found = (Class.forName(t)!=null)
					} catch(Exception e) {
						// NOP
					}

					processorUtil.error(clazz, "loop", null,
						"Import "+t+" cannot be resolved! (As Class: "+found+")")
				}
			}
			// Process all types
			for (td : types) {
				processingContext.put(Processor.PC_PROCESSED_TYPE, td)
				todoTypes.remove(td)
				val todoProcessors = newArrayList(processors)
				val doneProcessors = <Processor>newArrayList()
				processingContext.put(Processor.PC_TODO_PROCESSORS, todoProcessors)
				processingContext.put(Processor.PC_DONE_PROCESSORS, doneProcessors)
				val qualifiedName = if (td === null) null else td.qualifiedName
				// If unprocessed (for this phase), check all processors
				for (p : processors) {
					todoProcessors.remove(p)
					processingContext.put(Processor.PC_CURRENT_PROCESSOR, p)
					p.setProcessorUtil(processorUtil)
					try {
						if (td === null) {
							lambdaAfter.apply(processingContext, p, pkgName)
						} else {
							// Check if the processor is interested
							val acceptKey = p.class.name+':'+qualifiedName
							var accept = ACCEPT.get(acceptKey)
							if (accept === null) {
								accept = p.accept(processingContext, td)
								ACCEPT.put(acceptKey, accept)
								if (!accept) {
									processorUtil.debug(clazz, "loop", td,
											"NOT Calling: "+p+"."+phase+"("+qualifiedName+")")
								}
							}
							if (accept) {
								processorUtil.debug(clazz, "loop", td,
										"Calling: "+p+"."+phase+"("+qualifiedName+")")
								// Yes? Then call processor.
								lambda.apply(processingContext, p, td as TD)
							}
						}
					} catch (MissingTypeException ex) {
						processorUtil.error(clazz, "loop", td, p+": "
						+qualifiedName+" "+ex)
					} catch (Throwable t) {
						processorUtil.error(clazz, "loop", td, p+": "
						+qualifiedName, t)
					}
					doneProcessors.add(p)
				}
				doneTypes.add(td)
			}
		}
	}

	/** Implements the doRegisterGlobals() phase */
	override void doRegisterGlobals(List<? extends NamedElement> annotatedSourceElements,
			extension RegisterGlobalsContext context) {
		<TypeDeclaration>loop(annotatedSourceElements, "register",
			[processingContext,p,td|p.register(processingContext, td, context)],
			[processingContext,p,pkgName|p.afterRegister(processingContext, pkgName, context)])
	}

	/** Implements the doGenerateCode() phase */
	override doGenerateCode(List<? extends NamedElement> annotatedSourceElements,
			extension CodeGenerationContext context) {
		<TypeDeclaration>loop(annotatedSourceElements, "generate",
			[processingContext,p,td|p.generate(processingContext, td, context)],
			[processingContext,p,pkgName|p.afterGenerate(processingContext, pkgName, context)])
	}

	/** Implements the doTransform() phase */
	override doTransform(List<? extends MutableNamedElement> annotatedSourceElements,
			extension TransformationContext context) {
		<MutableTypeDeclaration>loop(annotatedSourceElements, "transform",
			[processingContext,p,mtd|p.transform(processingContext, mtd, context)],
			[processingContext,p,pkgName|p.afterTransform(processingContext, pkgName, context)])
	}

	/** Returns the list of processors. */

	private static synchronized def Processor<?,?>[] getProcessors(
		Class<?> magicClass,
		List<? extends NamedElement> annotatedSourceElements) {
		val cache = AntiClassLoaderCache.getCache()
		if (PROCESSORS == null) {
			val list = <Processor>newArrayList()
			val compilationUnit = ProcessorUtil.getCompilationUnit(annotatedSourceElements)
			val element = annotatedSourceElements.get(0)
			var String[] names = cache.get(PROCESSORS_NAMES) as String[]
			if (names == null) {
				names = findProcessorNames(magicClass, compilationUnit, element)
				cache.put(PROCESSORS_NAMES, names)
			}
			for (name : names) {
				try {
					var Class<?> cls = null
					try {
						cls = Class.forName(name)
					} catch (Exception e) {
						cls = Class.forName(name, true, magicClass.classLoader)
					}
					list.add(cls.newInstance as Processor<?,?>)
				} catch(Exception ex) {
					processorUtil.error(magicClass, "getProcessors", null,
						"Could not instantiate processor for '"+name+"'",ex)
				}
			}
//			for (p : Loader.load(Processor, MagicAnnotationProcessor.classLoader)) {
//				list.add(p)
//			}
			PROCESSORS = list.toArray(<Processor>newArrayOfSize(list.size))
			if (PROCESSORS.length === 0) {
				processorUtil.error(magicClass, "getProcessors", null,
					"No processor defined.")
			}
		}
		PROCESSORS
	}

	/** Returns the list of processor names */
	private static def String[] findProcessorNames(
		Class<?> magicClass,
		CompilationUnitImpl compilationUnit, Element element) {
		val list = <String>newArrayList()
		val root = compilationUnit.fileLocations.getProjectFolder(compilationUnit.filePath)
		val file = root.append(PROCESSORS_NAMES_FILE)
		val fileURI = compilationUnit.fileSystemSupport.toURI(file)
		if (compilationUnit.fileSystemSupport.exists(file)) {
			try {
				val content = compilationUnit.fileSystemSupport.getContents(file)
				val buf = new StringBuilder(content.length())
				buf.append(content)
				for (s : buf.toString.split("\n")) {
					val str = s.trim
					if (!str.empty) {
						list.add(str)
					}
				}
				if (list.empty) {
					processorUtil.error(magicClass, "findProcessorNames", null,
						"Could not find processors in '"+fileURI+"'")
				} else {
					// Test values once:
					for (name : list.toArray(newArrayOfSize(list.size))) {
						try {
							var Class<?> cls = null
							try {
								cls = Class.forName(name)
							} catch (Exception e) {
								cls = Class.forName(name, true, magicClass.classLoader)
							}
							// We test both the type and the ability to create them
							if (!(cls.newInstance instanceof Processor)) {
								throw new ClassCastException(name+" from "+fileURI+" is not a "+Processor.name)
							}
						} catch(Throwable t) {
							processorUtil.error(magicClass, "findProcessorNames", null,
								"Could not instantiate processor for '"+name+"' from "+fileURI,t)
							list.remove(name)
						}
					}
					processorUtil.warn(magicClass, "findProcessorNames", null,
						"Processors found in file '"+fileURI+"': "+list)
				}
			} catch(Throwable t) {
				processorUtil.error(magicClass, "findProcessorNames", null,
					"Could not read/process '"+fileURI+"'",t)
			}
		} else {
			processorUtil.error(magicClass, "findProcessorNames", null,
				"Could not find file '"+fileURI+"'")
		}
		list.toArray(newArrayOfSize(list.size))
	}
}
