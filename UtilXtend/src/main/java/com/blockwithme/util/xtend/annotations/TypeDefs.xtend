package com.blockwithme.util.xtend.annotations

import java.util.List
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.TransformationParticipant
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration
import org.eclipse.xtext.common.types.JvmType

@Active(typeof(GenProcessor))
annotation Gen {
}

annotation typeDef {
	Class<?> type
	String initializer
}

/** (Tries to) Implement C++ style typedefs. */
class GenProcessor implements TransformationParticipant<MutableTypeDeclaration> {
	/** Performs Code generation for an interface annotated with @Gen annotation.*/
	override doTransform(List<? extends MutableTypeDeclaration> types, extension TransformationContext context) {
		types.forEach[ type |
			type.declaredFields.forEach[ field |
				val t = field.type
				val found = t.name.findTypeGlobally
				if(found != null && found instanceof TypeDeclaration){
					val fieldType = found as MutableClassDeclaration
					val tDef = fieldType.findAnnotation(typeof(typeDef).findTypeGlobally)
					if(tDef != null){
						field.type = (tDef.getValue('type') as JvmType).qualifiedName.newTypeReference
						field.initializer = ['''«(tDef.getValue('initializer'))»''']
					}
				}
			]
		]
	}
}
