package com.blockwithme.util.xtend.annotations;

import com.blockwithme.util.xtend.annotations.typeDef;
import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.TransformationParticipant;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * (Tries to) Implement C++ style typedefs.
 */
@SuppressWarnings("all")
public class GenProcessor implements TransformationParticipant<MutableTypeDeclaration> {
  /**
   * Performs Code generation for an interface annotated with @Gen annotation.
   */
  public void doTransform(final List<? extends MutableTypeDeclaration> types, @Extension final TransformationContext context) {
    final Procedure1<MutableTypeDeclaration> _function = new Procedure1<MutableTypeDeclaration>() {
      public void apply(final MutableTypeDeclaration type) {
        Iterable<? extends MutableFieldDeclaration> _declaredFields = type.getDeclaredFields();
        final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
          public void apply(final MutableFieldDeclaration field) {
            final TypeReference t = field.getType();
            String _name = t.getName();
            final Type found = context.findTypeGlobally(_name);
            boolean _and = false;
            boolean _notEquals = (!Objects.equal(found, null));
            if (!_notEquals) {
              _and = false;
            } else {
              _and = (found instanceof TypeDeclaration);
            }
            if (_and) {
              final MutableClassDeclaration fieldType = ((MutableClassDeclaration) found);
              Type _findTypeGlobally = context.findTypeGlobally(typeDef.class);
              final AnnotationReference tDef = fieldType.findAnnotation(_findTypeGlobally);
              boolean _notEquals_1 = (!Objects.equal(tDef, null));
              if (_notEquals_1) {
                Object _value = tDef.getValue("type");
                String _qualifiedName = ((JvmType) _value).getQualifiedName();
                TypeReference _newTypeReference = context.newTypeReference(_qualifiedName);
                field.setType(_newTypeReference);
                final CompilationStrategy _function = new CompilationStrategy() {
                  public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                    StringConcatenation _builder = new StringConcatenation();
                    Object _value = tDef.getValue("initializer");
                    _builder.append(_value, "");
                    return _builder;
                  }
                };
                field.setInitializer(_function);
              }
            }
          }
        };
        IterableExtensions.forEach(_declaredFields, _function);
      }
    };
    IterableExtensions.forEach(types, _function);
  }
}
