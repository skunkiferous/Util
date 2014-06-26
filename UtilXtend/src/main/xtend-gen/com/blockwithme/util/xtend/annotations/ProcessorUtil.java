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

import com.blockwithme.util.xtend.annotations.MissingTypeException;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend.core.macro.declaration.AbstractElementImpl;
import org.eclipse.xtend.core.macro.declaration.CompilationUnitImpl;
import org.eclipse.xtend.core.macro.declaration.ExpressionImpl;
import org.eclipse.xtend.core.macro.declaration.JvmTypeDeclarationImpl;
import org.eclipse.xtend.core.macro.declaration.TypeLookupImpl;
import org.eclipse.xtend.core.macro.declaration.XtendTypeDeclarationImpl;
import org.eclipse.xtend.core.xtend.XtendConstructor;
import org.eclipse.xtend.core.xtend.XtendEnumLiteral;
import org.eclipse.xtend.core.xtend.XtendField;
import org.eclipse.xtend.core.xtend.XtendFile;
import org.eclipse.xtend.core.xtend.XtendFunction;
import org.eclipse.xtend.core.xtend.XtendMember;
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration;
import org.eclipse.xtend.core.xtend.impl.XtendVariableDeclarationImpl;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationUnit;
import org.eclipse.xtend.lib.macro.declaration.ConstructorDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Element;
import org.eclipse.xtend.lib.macro.declaration.EnumerationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ExecutableDeclaration;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.InterfaceDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MemberDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableConstructorDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableInterfaceDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableNamedElement;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.NamedElement;
import org.eclipse.xtend.lib.macro.declaration.ParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeParameterDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeParameterDeclarator;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.expression.Expression;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtend.lib.macro.services.AnnotationReferenceBuildContext;
import org.eclipse.xtend.lib.macro.services.AnnotationReferenceProvider;
import org.eclipse.xtend.lib.macro.services.ProblemSupport;
import org.eclipse.xtend.lib.macro.services.Tracability;
import org.eclipse.xtend.lib.macro.services.TypeReferenceProvider;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

/**
 * Helper methods for active annotation processing.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class ProcessorUtil implements TypeReferenceProvider, AnnotationReferenceProvider {
  private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS ");
  
  /**
   * Debug output?
   */
  private final static boolean DEBUG = false;
  
  /**
   * Output errors as warnings?
   */
  private final static boolean ERROR_AS_WARNING = false;
  
  /**
   * The processed NamedElement
   * (According to the Active Annotation API... We don't care
   * about it, but we are forced to use it to do logging)
   */
  private NamedElement element;
  
  /**
   * The ProblemSupport, for logging
   */
  private ProblemSupport problemSupport;
  
  /**
   * The CompilationUnitImpl that is the "cache key"
   */
  private CompilationUnitImpl compilationUnit;
  
  /**
   * The processed file
   */
  private String file;
  
  /**
   * The phase
   */
  private String phase;
  
  /**
   * A default prefix for cache get/put.
   */
  private String prefix;
  
  /**
   * The List TypeReference
   */
  private TypeReference list;
  
  /**
   * The Arrays TypeReference
   */
  private TypeReference arrays;
  
  /**
   * The Objects TypeReference
   */
  private TypeReference objects;
  
  /**
   * The Override Annotation type
   */
  private Type _override;
  
  /**
   * The type cache
   */
  private final HashMap<String, TypeDeclaration> types = new HashMap<String, TypeDeclaration>();
  
  /**
   * The direct parent cache
   */
  private final HashMap<TypeDeclaration, Iterable<? extends TypeDeclaration>> directParents = new HashMap<TypeDeclaration, Iterable<? extends TypeDeclaration>>();
  
  /**
   * The parent cache
   */
  private final HashMap<TypeDeclaration, Iterable<? extends TypeDeclaration>> parents = new HashMap<TypeDeclaration, Iterable<? extends TypeDeclaration>>();
  
  /**
   * The JvmDeclaredTypes cache
   */
  private Iterable<? extends MutableTypeDeclaration> jvmDeclaredTypes;
  
  /**
   * The XtendTypeDeclarations cache
   */
  private Iterable<? extends TypeDeclaration> xtendTypeDeclarations;
  
  /**
   * The identityCache of the compilationUnit
   */
  private Map<EObject, Object> identityCache = null;
  
  /**
   * Remembers what type should be an interface
   */
  private final static HashSet<String> SHOULD_BE_INTERFACE = new HashSet<String>();
  
  /**
   * Sets the NamedElement currently being processed.
   */
  final void setElement(final String phase, final NamedElement element) {
    this.phase = phase;
    final CompilationUnitImpl beforeCU = this.compilationUnit;
    final NamedElement beforeElement = this.element;
    boolean _tripleNotEquals = (this.element != element);
    if (_tripleNotEquals) {
      this.element = element;
      boolean _tripleNotEquals_1 = (element != null);
      if (_tripleNotEquals_1) {
        CompilationUnit _compilationUnit = element.getCompilationUnit();
        this.compilationUnit = ((CompilationUnitImpl) _compilationUnit);
      } else {
        this.compilationUnit = null;
      }
      boolean _tripleNotEquals_2 = (beforeCU != this.compilationUnit);
      if (_tripleNotEquals_2) {
        this.types.clear();
        this.directParents.clear();
        this.parents.clear();
        this.jvmDeclaredTypes = null;
        this.xtendTypeDeclarations = null;
        boolean _notEquals = (!Objects.equal(element, null));
        if (_notEquals) {
          ProblemSupport _problemSupport = this.compilationUnit.getProblemSupport();
          this.problemSupport = _problemSupport;
          Path _filePath = this.compilationUnit.getFilePath();
          String _string = _filePath.toString();
          this.file = _string;
          String _qualifiedName = ProcessorUtil.qualifiedName(element);
          String _plus = ((this.file + "/") + _qualifiedName);
          String _plus_1 = (_plus + "/");
          this.prefix = _plus_1;
          final TypeReferenceProvider typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
          TypeReference _newTypeReference = typeReferenceProvider.newTypeReference(List.class);
          TypeReference _requireNonNull = java.util.Objects.<TypeReference>requireNonNull(_newTypeReference, "newTypeReference(List)");
          this.list = _requireNonNull;
          TypeReference _newTypeReference_1 = typeReferenceProvider.newTypeReference(Arrays.class);
          TypeReference _requireNonNull_1 = java.util.Objects.<TypeReference>requireNonNull(_newTypeReference_1, "newTypeReference(Arrays)");
          this.arrays = _requireNonNull_1;
          TypeReference _newTypeReference_2 = typeReferenceProvider.newTypeReference(java.util.Objects.class);
          TypeReference _requireNonNull_2 = java.util.Objects.<TypeReference>requireNonNull(_newTypeReference_2, "newTypeReference(Objects)");
          this.objects = _requireNonNull_2;
          TypeLookupImpl _typeLookup = this.compilationUnit.getTypeLookup();
          Type _findTypeGlobally = _typeLookup.findTypeGlobally(Override.class);
          Type _requireNonNull_3 = java.util.Objects.<Type>requireNonNull(_findTypeGlobally, "typeLookup.findTypeGlobally(Override)");
          this._override = _requireNonNull_3;
        } else {
          this.problemSupport = null;
          this.file = null;
          this.prefix = null;
          this.list = null;
          this.arrays = null;
          this.objects = null;
          this._override = null;
        }
      }
    }
  }
  
  /**
   * Returns a String version of the current time.
   */
  private static String time() {
    Date _date = new Date();
    return ProcessorUtil.TIME_FORMAT.format(_date);
  }
  
  /**
   * Returns the CompilationUnit of those elements
   */
  public static CompilationUnitImpl getCompilationUnit(final List<? extends NamedElement> annotatedSourceElements) {
    CompilationUnitImpl _xblockexpression = null;
    {
      boolean _isEmpty = annotatedSourceElements.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final NamedElement element = annotatedSourceElements.get(0);
        CompilationUnit _compilationUnit = element.getCompilationUnit();
        return ((CompilationUnitImpl) _compilationUnit);
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  /**
   * The CompilationUnitImpl that is the "cache key"
   */
  public final CompilationUnitImpl getCompilationUnit() {
    return this.compilationUnit;
  }
  
  /**
   * The processed file
   */
  public final String getFile() {
    return this.file;
  }
  
  /**
   * The phase
   */
  public final String getPhase() {
    return this.phase;
  }
  
  /**
   * Should this type be an interface?
   */
  public boolean getShouldBeInterface(final String qualifiedName) {
    return ProcessorUtil.SHOULD_BE_INTERFACE.contains(qualifiedName);
  }
  
  /**
   * Should this type be an interface?
   */
  public void setShouldBeInterface(final String qualifiedName) {
    boolean _add = ProcessorUtil.SHOULD_BE_INTERFACE.add(qualifiedName);
    if (_add) {
      this.debug(ProcessorUtil.class, "setShouldBeInterface", null, (("setShouldBeInterface(" + qualifiedName) + ")"));
    }
  }
  
  /**
   * Returns the top-level Xtend types of this CompilationUnit
   */
  public final Iterable<? extends TypeDeclaration> getXtendTypes() {
    Iterable<? extends TypeDeclaration> _xblockexpression = null;
    {
      boolean _tripleEquals = (this.xtendTypeDeclarations == null);
      if (_tripleEquals) {
        XtendFile _xtendFile = this.compilationUnit.getXtendFile();
        EList<XtendTypeDeclaration> _xtendTypes = _xtendFile.getXtendTypes();
        final Function1<XtendTypeDeclaration, XtendTypeDeclarationImpl<? extends XtendTypeDeclaration>> _function = new Function1<XtendTypeDeclaration, XtendTypeDeclarationImpl<? extends XtendTypeDeclaration>>() {
          public XtendTypeDeclarationImpl<? extends XtendTypeDeclaration> apply(final XtendTypeDeclaration it) {
            return ProcessorUtil.this.compilationUnit.toXtendTypeDeclaration(it);
          }
        };
        List<XtendTypeDeclarationImpl<? extends XtendTypeDeclaration>> _map = ListExtensions.<XtendTypeDeclaration, XtendTypeDeclarationImpl<? extends XtendTypeDeclaration>>map(_xtendTypes, _function);
        this.xtendTypeDeclarations = _map;
        for (final TypeDeclaration x : this.xtendTypeDeclarations) {
          if ((x instanceof InterfaceDeclaration)) {
            String _qualifiedName = ((InterfaceDeclaration)x).getQualifiedName();
            this.setShouldBeInterface(_qualifiedName);
          }
        }
      }
      _xblockexpression = this.xtendTypeDeclarations;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the top-level mutable types of this CompilationUnit
   */
  public final Iterable<? extends MutableTypeDeclaration> getMutableTypes() {
    Iterable<? extends MutableTypeDeclaration> _xblockexpression = null;
    {
      boolean _tripleEquals = (this.jvmDeclaredTypes == null);
      if (_tripleEquals) {
        this.getXtendTypes();
        XtendFile _xtendFile = this.compilationUnit.getXtendFile();
        Resource _eResource = _xtendFile.eResource();
        EList<EObject> _contents = _eResource.getContents();
        final Iterable<JvmDeclaredType> tmp = Iterables.<JvmDeclaredType>filter(_contents, JvmDeclaredType.class);
        final Function1<JvmDeclaredType, MutableTypeDeclaration> _function = new Function1<JvmDeclaredType, MutableTypeDeclaration>() {
          public MutableTypeDeclaration apply(final JvmDeclaredType it) {
            try {
              MutableTypeDeclaration _xblockexpression = null;
              {
                boolean triedToFix = false;
                Boolean isInterface = null;
                if ((it instanceof JvmGenericType)) {
                  boolean _isInterface = ((JvmGenericType)it).isInterface();
                  isInterface = Boolean.valueOf(_isInterface);
                  boolean _isInterface_1 = ((JvmGenericType)it).isInterface();
                  boolean _not = (!_isInterface_1);
                  if (_not) {
                    String _qualifiedName = ((JvmGenericType)it).getQualifiedName();
                    boolean _shouldBeInterface = ProcessorUtil.this.getShouldBeInterface(_qualifiedName);
                    if (_shouldBeInterface) {
                      ((JvmGenericType)it).setInterface(true);
                      triedToFix = true;
                    }
                  } else {
                    String _qualifiedName_1 = ((JvmGenericType)it).getQualifiedName();
                    ProcessorUtil.this.setShouldBeInterface(_qualifiedName_1);
                  }
                }
                TypeDeclaration td = ProcessorUtil.this.compilationUnit.toTypeDeclaration(it);
                boolean _and = false;
                String _qualifiedName_2 = td.getQualifiedName();
                boolean _shouldBeInterface_1 = ProcessorUtil.this.getShouldBeInterface(_qualifiedName_2);
                if (!_shouldBeInterface_1) {
                  _and = false;
                } else {
                  _and = (!(td instanceof InterfaceDeclaration));
                }
                if (_and) {
                  boolean _tripleEquals = (ProcessorUtil.this.identityCache == null);
                  if (_tripleEquals) {
                    ReflectExtensions _reflectExtensions = new ReflectExtensions();
                    Object _get = _reflectExtensions.<Object>get(ProcessorUtil.this.compilationUnit, "identityCache");
                    ProcessorUtil.this.identityCache = ((Map<EObject, Object>) _get);
                  }
                  ProcessorUtil.this.identityCache.remove(it);
                  TypeDeclaration _typeDeclaration = ProcessorUtil.this.compilationUnit.toTypeDeclaration(it);
                  td = _typeDeclaration;
                  if ((!(td instanceof InterfaceDeclaration))) {
                    String _qualifiedName_3 = td.getQualifiedName();
                    String _plus = ("ShouldBeInterfaceButIsnt: " + _qualifiedName_3);
                    String _plus_1 = (_plus + " triedToFix: ");
                    String _plus_2 = (_plus_1 + Boolean.valueOf(triedToFix));
                    String _plus_3 = (_plus_2 + " it.interface: ");
                    String _plus_4 = (_plus_3 + isInterface);
                    String _plus_5 = (_plus_4 + " it.qualifiedName: ");
                    String _qualifiedName_4 = it.getQualifiedName();
                    String _plus_6 = (_plus_5 + _qualifiedName_4);
                    ProcessorUtil.this.error(ProcessorUtil.class, "getMutableTypes", null, _plus_6);
                  }
                }
                _xblockexpression = ((MutableTypeDeclaration) td);
              }
              return _xblockexpression;
            } catch (Throwable _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
        Iterable<MutableTypeDeclaration> _map = IterableExtensions.<JvmDeclaredType, MutableTypeDeclaration>map(tmp, _function);
        this.jvmDeclaredTypes = _map;
      }
      _xblockexpression = this.jvmDeclaredTypes;
    }
    return _xblockexpression;
  }
  
  /**
   * Recursively returns the Xtend types of this CompilationUnit
   */
  public final Iterable<? extends TypeDeclaration> getAllXtendTypes() {
    Iterable<? extends TypeDeclaration> _xtendTypes = this.getXtendTypes();
    final Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> _function = new Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>>() {
      public Iterable<? extends TypeDeclaration> apply(final TypeDeclaration it) {
        Iterable<? extends ClassDeclaration> _declaredClasses = it.getDeclaredClasses();
        Iterable<? extends InterfaceDeclaration> _declaredInterfaces = it.getDeclaredInterfaces();
        Iterable<MemberDeclaration> _plus = Iterables.<MemberDeclaration>concat(_declaredClasses, _declaredInterfaces);
        return ((Iterable<? extends TypeDeclaration>) ((Iterable) _plus));
      }
    };
    return this.doRecursivelyN(_xtendTypes, _function);
  }
  
  /**
   * Recursively returns the top-level mutable types of this CompilationUnit
   */
  public final Iterable<? extends MutableTypeDeclaration> getAllMutableTypes() {
    Iterable<? extends MutableTypeDeclaration> _mutableTypes = this.getMutableTypes();
    final Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> _function = new Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>>() {
      public Iterable<? extends TypeDeclaration> apply(final TypeDeclaration it) {
        Iterable<? extends ClassDeclaration> _declaredClasses = it.getDeclaredClasses();
        Iterable<? extends InterfaceDeclaration> _declaredInterfaces = it.getDeclaredInterfaces();
        Iterable<MemberDeclaration> _plus = Iterables.<MemberDeclaration>concat(_declaredClasses, _declaredInterfaces);
        return ((Iterable<? extends TypeDeclaration>) ((Iterable) _plus));
      }
    };
    Iterable<TypeDeclaration> _doRecursivelyN = this.doRecursivelyN(_mutableTypes, _function);
    return ((Iterable<? extends MutableTypeDeclaration>) ((Iterable) _doRecursivelyN));
  }
  
  /**
   * Sometimes, Xtend "forgets" to set the "isInterface" flag on types!
   */
  private void fixInterface(final JvmType type, final boolean isInterface) {
    if ((type instanceof JvmGenericTypeImpl)) {
      boolean _isInterface = ((JvmGenericTypeImpl)type).isInterface();
      boolean _not = (!_isInterface);
      if (_not) {
        ((JvmGenericTypeImpl)type).setInterface(isInterface);
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
  private TypeDeclaration lookup(final TypeDeclaration td, final boolean isInterface, final String typeName) {
    TypeDeclaration _xblockexpression = null;
    {
      TypeDeclaration result = this.types.get(typeName);
      boolean _tripleEquals = (result == null);
      if (_tripleEquals) {
        TypeDeclaration _lookup2 = this.lookup2(td, isInterface, typeName);
        result = _lookup2;
        this.types.put(typeName, result);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Lookup a type by name.
   * 
   * @param td	the type that needs the lookup.
   * @param isInterface	should the searched-for type be an interface? (Workaround for Xtend issue)
   * @param typeName	The name of the type we are looking for.
   */
  private TypeDeclaration lookup2(final TypeDeclaration td, final boolean isInterface, final String typeName) {
    final int index = typeName.indexOf("<");
    String _xifexpression = null;
    if ((index > 0)) {
      _xifexpression = typeName.substring(0, index);
    } else {
      _xifexpression = typeName;
    }
    final String typeName2 = _xifexpression;
    CompilationUnit _compilationUnit = td.getCompilationUnit();
    final CompilationUnitImpl compilationUnit = ((CompilationUnitImpl) _compilationUnit);
    boolean _or = false;
    if ((td instanceof JvmType)) {
      _or = true;
    } else {
      _or = (td instanceof JvmTypeDeclarationImpl);
    }
    final boolean parentIsJvmType = _or;
    if (parentIsJvmType) {
      Iterable<? extends MutableTypeDeclaration> _mutableTypes = this.getMutableTypes();
      final Function1<MutableTypeDeclaration, Boolean> _function = new Function1<MutableTypeDeclaration, Boolean>() {
        public Boolean apply(final MutableTypeDeclaration it) {
          String _qualifiedName = it.getQualifiedName();
          return Boolean.valueOf(Objects.equal(_qualifiedName, typeName2));
        }
      };
      final MutableTypeDeclaration tmp = IterableExtensions.findFirst(_mutableTypes, _function);
      boolean _tripleNotEquals = (tmp != null);
      if (_tripleNotEquals) {
        return tmp;
      }
    } else {
      Iterable<? extends TypeDeclaration> _xtendTypes = this.getXtendTypes();
      final Function1<TypeDeclaration, Boolean> _function_1 = new Function1<TypeDeclaration, Boolean>() {
        public Boolean apply(final TypeDeclaration it) {
          String _qualifiedName = it.getQualifiedName();
          return Boolean.valueOf(Objects.equal(_qualifiedName, typeName2));
        }
      };
      final TypeDeclaration tmp_1 = IterableExtensions.findFirst(_xtendTypes, _function_1);
      boolean _tripleNotEquals_1 = (tmp_1 != null);
      if (_tripleNotEquals_1) {
        return tmp_1;
      }
    }
    TypeReferences _typeReferences = compilationUnit.getTypeReferences();
    XtendFile _xtendFile = compilationUnit.getXtendFile();
    final JvmType foreign = _typeReferences.findDeclaredType(typeName2, _xtendFile);
    boolean _equals = Objects.equal(foreign, null);
    if (_equals) {
      final Type foreign2 = this.findTypeGlobally(typeName2);
      if ((foreign2 instanceof TypeDeclaration)) {
        return ((TypeDeclaration)foreign2);
      }
      String _qualifiedName = td.getQualifiedName();
      String _plus = ((("Could not find parent type " + typeName2) + " of type ") + _qualifiedName);
      throw new IllegalStateException(_plus);
    } else {
      this.fixInterface(foreign, isInterface);
      Type _type = compilationUnit.toType(foreign);
      return ((TypeDeclaration) _type);
    }
  }
  
  /**
   * Converts TypeReferences to TypeDeclarations
   */
  private Iterable<? extends TypeDeclaration> convert(final TypeDeclaration td, final boolean isInterface, final Iterable<? extends TypeReference> refs) {
    final Function1<TypeReference, TypeDeclaration> _function = new Function1<TypeReference, TypeDeclaration>() {
      public TypeDeclaration apply(final TypeReference it) {
        String _localQualifiedName = ProcessorUtil.this.localQualifiedName(it);
        return ProcessorUtil.this.lookup(td, isInterface, _localQualifiedName);
      }
    };
    return IterableExtensions.map(refs, _function);
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  public String localQualifiedName(final TypeReference element) {
    String _xblockexpression = null;
    {
      String result = element.getName();
      String _simpleName = element.getSimpleName();
      boolean _equals = Objects.equal(result, _simpleName);
      if (_equals) {
        final String pattern = ("." + result);
        XtendFile _xtendFile = this.compilationUnit.getXtendFile();
        XImportSection _importSection = _xtendFile.getImportSection();
        EList<XImportDeclaration> _importDeclarations = _importSection.getImportDeclarations();
        for (final XImportDeclaration d : _importDeclarations) {
          String _importedTypeName = d.getImportedTypeName();
          boolean _endsWith = _importedTypeName.endsWith(pattern);
          if (_endsWith) {
            String _importedTypeName_1 = d.getImportedTypeName();
            result = _importedTypeName_1;
          }
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the direct parents
   */
  public final Iterable<? extends TypeDeclaration> findDirectParents(final TypeDeclaration td) {
    Iterable<? extends TypeDeclaration> _xblockexpression = null;
    {
      Object _requireNonNull = java.util.Objects.<Object>requireNonNull(td, "td");
      Iterable<? extends TypeDeclaration> result = this.directParents.get(_requireNonNull);
      boolean _tripleEquals = (result == null);
      if (_tripleEquals) {
        Iterable<? extends TypeDeclaration> _findDirectParents2 = this.findDirectParents2(td);
        result = _findDirectParents2;
        this.directParents.put(td, result);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the direct parents
   */
  private Iterable<? extends TypeDeclaration> _findDirectParents2(final TypeDeclaration td) {
    return Collections.<TypeDeclaration>emptyList();
  }
  
  /**
   * Returns the direct parents
   */
  private Iterable<? extends TypeDeclaration> _findDirectParents2(final ClassDeclaration td) {
    ArrayList<TypeDeclaration> _xblockexpression = null;
    {
      final ArrayList<TypeDeclaration> result = CollectionLiterals.<TypeDeclaration>newArrayList();
      Iterable<? extends TypeReference> _implementedInterfaces = td.getImplementedInterfaces();
      Iterable<? extends TypeDeclaration> _convert = this.convert(td, true, _implementedInterfaces);
      Iterables.<TypeDeclaration>addAll(result, _convert);
      final TypeReference extendedClass = td.getExtendedClass();
      boolean _and = false;
      boolean _tripleNotEquals = (extendedClass != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _isAnyType = extendedClass.isAnyType();
        boolean _not = (!_isAnyType);
        _and = _not;
      }
      if (_and) {
        try {
          Set<TypeReference> _singleton = Collections.<TypeReference>singleton(extendedClass);
          Iterable<? extends TypeDeclaration> _convert_1 = this.convert(td, false, _singleton);
          Iterables.<TypeDeclaration>addAll(result, _convert_1);
        } catch (final Throwable _t) {
          if (_t instanceof RuntimeException) {
            final RuntimeException ex = (RuntimeException)_t;
            this.error(ProcessorUtil.class, "findDirectParents2", td, ("Problems with " + extendedClass), ex);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the direct parents
   */
  private Iterable<? extends TypeDeclaration> _findDirectParents2(final InterfaceDeclaration td) {
    Iterable<? extends TypeReference> _extendedInterfaces = td.getExtendedInterfaces();
    return this.convert(td, true, _extendedInterfaces);
  }
  
  /**
   * Returns the direct parents
   */
  private Iterable<? extends TypeDeclaration> _findDirectParents2(final EnumerationTypeDeclaration td) {
    return Collections.<TypeDeclaration>emptyList();
  }
  
  /**
   * Returns the all parents, *including the type itself
   */
  public final Iterable<? extends TypeDeclaration> findParents(final TypeDeclaration td) {
    Iterable<? extends TypeDeclaration> _xblockexpression = null;
    {
      Object _requireNonNull = java.util.Objects.<Object>requireNonNull(td, "td");
      Iterable<? extends TypeDeclaration> result = this.parents.get(_requireNonNull);
      boolean _tripleEquals = (result == null);
      if (_tripleEquals) {
        Iterable<? extends TypeDeclaration> _findParents2 = this.findParents2(td);
        result = _findParents2;
        this.parents.put(td, result);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Performs some operation recursively on the TypeDeclaration.
   */
  public final Iterable<TypeDeclaration> doRecursively1(final TypeDeclaration td, final Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> lambda) {
    Set<TypeDeclaration> _singleton = Collections.<TypeDeclaration>singleton(td);
    return this.doRecursivelyN(_singleton, lambda);
  }
  
  /**
   * Performs some operation recursively on the TypeDeclarations.
   */
  public final Iterable<TypeDeclaration> doRecursivelyN(final Iterable<? extends TypeDeclaration> tds, final Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> lambda) {
    ArrayList<TypeDeclaration> _xblockexpression = null;
    {
      final ArrayList<TypeDeclaration> todo = CollectionLiterals.<TypeDeclaration>newArrayList();
      final ArrayList<TypeDeclaration> done = CollectionLiterals.<TypeDeclaration>newArrayList();
      Iterables.<TypeDeclaration>addAll(todo, tds);
      boolean _isEmpty = todo.isEmpty();
      boolean _not = (!_isEmpty);
      boolean _while = _not;
      while (_while) {
        {
          final TypeDeclaration next = todo.remove(0);
          done.add(next);
          Iterable<? extends TypeDeclaration> _apply = lambda.apply(next);
          for (final TypeDeclaration parent : _apply) {
            boolean _and = false;
            boolean _contains = todo.contains(parent);
            boolean _not_1 = (!_contains);
            if (!_not_1) {
              _and = false;
            } else {
              boolean _contains_1 = done.contains(parent);
              boolean _not_2 = (!_contains_1);
              _and = _not_2;
            }
            if (_and) {
              todo.add(parent);
            }
          }
        }
        boolean _isEmpty_1 = todo.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        _while = _not_1;
      }
      _xblockexpression = done;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the all parents, *including the type itself
   */
  private Iterable<? extends TypeDeclaration> findParents2(final TypeDeclaration td) {
    final Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>> _function = new Function1<TypeDeclaration, Iterable<? extends TypeDeclaration>>() {
      public Iterable<? extends TypeDeclaration> apply(final TypeDeclaration it) {
        return ProcessorUtil.this.findDirectParents(it);
      }
    };
    return this.doRecursively1(td, _function);
  }
  
  /**
   * Does the given type directly bares the desired annotation?
   */
  public final boolean hasDirectAnnotation(final TypeDeclaration td, final String annotationName) {
    Iterable<? extends AnnotationReference> _annotations = td.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        String _qualifiedName = _annotationTypeDeclaration.getQualifiedName();
        return Boolean.valueOf(Objects.equal(_qualifiedName, annotationName));
      }
    };
    return IterableExtensions.exists(_annotations, _function);
  }
  
  /**
   * Does the given type directly bares the desired annotation?
   */
  public final boolean hasDirectAnnotation(final TypeDeclaration td, final Class<? extends Annotation> annotation) {
    String _name = annotation.getName();
    return this.hasDirectAnnotation(td, _name);
  }
  
  /**
   * Returns the name, annotations, base class and implemented interfaces of the type (not the content).
   */
  public final String describeTypeDeclaration(final TypeDeclaration td, @Extension final Tracability tracability) {
    String _xblockexpression = null;
    {
      java.util.Objects.<TypeDeclaration>requireNonNull(td, "td");
      final TypeDeclaration td2 = this.findXtend(td);
      final AbstractElementImpl aei = ((AbstractElementImpl) td2);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("simpleName >> ");
      String _simpleName = td2.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("toString  >> ");
      _builder.append(td2, "");
      _builder.newLineIfNotEmpty();
      _builder.append("file  >> ");
      CompilationUnit _compilationUnit = td2.getCompilationUnit();
      Path _filePath = _compilationUnit.getFilePath();
      _builder.append(_filePath, "");
      _builder.newLineIfNotEmpty();
      _builder.append("generated >> ");
      boolean _isGenerated = tracability.isGenerated(td2);
      _builder.append(_isGenerated, "");
      _builder.newLineIfNotEmpty();
      _builder.append("source >> ");
      boolean _isSource = tracability.isSource(td2);
      _builder.append(_isSource, "");
      _builder.newLineIfNotEmpty();
      _builder.append("primaryGeneratedJavaElement >> ");
      MutableNamedElement _primaryGeneratedJavaElement = tracability.getPrimaryGeneratedJavaElement(td2);
      _builder.append(_primaryGeneratedJavaElement, "");
      _builder.newLineIfNotEmpty();
      _builder.append("delegate class >> ");
      Object _delegate = aei.getDelegate();
      Class<?> _class = _delegate.getClass();
      _builder.append(_class, "");
      _builder.newLineIfNotEmpty();
      _builder.append("annotations >> ");
      Iterable<? extends AnnotationReference> _annotations = td2.getAnnotations();
      String _qualifiedNames = ProcessorUtil.qualifiedNames(_annotations);
      _builder.append(_qualifiedNames, "");
      _builder.newLineIfNotEmpty();
      {
        if ((td2 instanceof InterfaceDeclaration)) {
          _builder.append("implemented interfaces >> ");
          Iterable<? extends TypeReference> _extendedInterfaces = ((InterfaceDeclaration)td2).getExtendedInterfaces();
          String _qualifiedNames_1 = ProcessorUtil.qualifiedNames(_extendedInterfaces);
          _builder.append(_qualifiedNames_1, "");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if ((td2 instanceof ClassDeclaration)) {
          {
            TypeReference _extendedClass = ((ClassDeclaration)td2).getExtendedClass();
            boolean _tripleNotEquals = (_extendedClass != null);
            if (_tripleNotEquals) {
              _builder.append("super >> ");
              TypeReference _extendedClass_1 = ((ClassDeclaration)td2).getExtendedClass();
              String _name = _extendedClass_1.getName();
              _builder.append(_name, "");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("implemented interfaces >> ");
          Iterable<? extends TypeReference> _implementedInterfaces = ((ClassDeclaration)td2).getImplementedInterfaces();
          String _qualifiedNames_2 = ProcessorUtil.qualifiedNames(_implementedInterfaces);
          _builder.append(_qualifiedNames_2, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("---------------------------------");
      _builder.newLine();
      {
        Iterable<? extends MethodDeclaration> _declaredMethods = td2.getDeclaredMethods();
        for(final MethodDeclaration m : _declaredMethods) {
          String _describeMethod = this.describeMethod(m, tracability);
          _builder.append(_describeMethod, "");
          _builder.newLineIfNotEmpty();
          _builder.append("-------------");
          _builder.newLine();
        }
      }
      _builder.append("---------------------------------");
      _builder.newLine();
      {
        Iterable<? extends FieldDeclaration> _declaredFields = td2.getDeclaredFields();
        for(final FieldDeclaration f : _declaredFields) {
          String _describeField = this.describeField(f, tracability);
          _builder.append(_describeField, "");
          _builder.newLineIfNotEmpty();
          _builder.append("-------------");
          _builder.newLine();
        }
      }
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  /**
   * Returns a long trace of all the info of that method.
   */
  public final String describeMethod(final MethodDeclaration m, @Extension final Tracability tracability) {
    String _xblockexpression = null;
    {
      java.util.Objects.<MethodDeclaration>requireNonNull(m, "m");
      Expression _body = m.getBody();
      final ExpressionImpl bdy = ((ExpressionImpl) _body);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("simpleName >> ");
      String _simpleName = m.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("signature  >> ");
      String _signature = this.signature(m);
      _builder.append(_signature, "");
      _builder.newLineIfNotEmpty();
      _builder.append("toString  >> ");
      _builder.append(bdy, "");
      _builder.newLineIfNotEmpty();
      {
        boolean _tripleNotEquals = (bdy != null);
        if (_tripleNotEquals) {
          _builder.append("file  >> ");
          CompilationUnitImpl _compilationUnit = bdy.getCompilationUnit();
          Path _filePath = _compilationUnit.getFilePath();
          _builder.append(_filePath, "");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("file  >> null");
          _builder.newLine();
        }
      }
      _builder.append("generated >> ");
      boolean _isGenerated = tracability.isGenerated(m);
      _builder.append(_isGenerated, "");
      _builder.newLineIfNotEmpty();
      _builder.append("source >> ");
      boolean _isSource = tracability.isSource(m);
      _builder.append(_isSource, "");
      _builder.newLineIfNotEmpty();
      _builder.append("primaryGeneratedJavaElement >> ");
      MutableNamedElement _primaryGeneratedJavaElement = tracability.getPrimaryGeneratedJavaElement(m);
      _builder.append(_primaryGeneratedJavaElement, "");
      _builder.newLineIfNotEmpty();
      {
        boolean _tripleNotEquals_1 = (bdy != null);
        if (_tripleNotEquals_1) {
          _builder.append("delegate class >> ");
          XExpression _delegate = bdy.getDelegate();
          Class<? extends XExpression> _class = _delegate.getClass();
          _builder.append(_class, "");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("delegate class >> null");
          _builder.newLine();
        }
      }
      _builder.append("annotations >> ");
      Iterable<? extends AnnotationReference> _annotations = m.getAnnotations();
      String _qualifiedNames = ProcessorUtil.qualifiedNames(_annotations);
      _builder.append(_qualifiedNames, "");
      _builder.newLineIfNotEmpty();
      _builder.append("---------------------------------");
      _builder.newLine();
      {
        boolean _tripleNotEquals_2 = (bdy != null);
        if (_tripleNotEquals_2) {
          {
            XExpression _delegate_1 = bdy.getDelegate();
            EList<XExpression> _expressions = ((XBlockExpression) _delegate_1).getExpressions();
            for(final XExpression e : _expressions) {
              _builder.append("expression >> ");
              _builder.append(e, "");
              _builder.newLineIfNotEmpty();
              _builder.append("expression class>> ");
              Class<? extends XExpression> _class_1 = e.getClass();
              _builder.append(_class_1, "");
              _builder.newLineIfNotEmpty();
              {
                if ((e instanceof XtendVariableDeclarationImpl)) {
                  _builder.append("simpleName >> ");
                  String _simpleName_1 = ((XtendVariableDeclarationImpl)e).getSimpleName();
                  _builder.append(_simpleName_1, "");
                  _builder.newLineIfNotEmpty();
                  _builder.append("qualifiedName >> ");
                  String _qualifiedName = ((XtendVariableDeclarationImpl)e).getQualifiedName();
                  _builder.append(_qualifiedName, "");
                  _builder.newLineIfNotEmpty();
                  _builder.append("right >> ");
                  XExpression _right = ((XtendVariableDeclarationImpl)e).getRight();
                  _builder.append(_right, "");
                  _builder.newLineIfNotEmpty();
                  {
                    JvmTypeReference _type = ((XtendVariableDeclarationImpl)e).getType();
                    boolean _tripleNotEquals_3 = (_type != null);
                    if (_tripleNotEquals_3) {
                      _builder.append("type qualifiedName >> ");
                      JvmTypeReference _type_1 = ((XtendVariableDeclarationImpl)e).getType();
                      String _qualifiedName_1 = _type_1.getQualifiedName();
                      _builder.append(_qualifiedName_1, "");
                      _builder.newLineIfNotEmpty();
                      _builder.append("type identifier >> ");
                      JvmTypeReference _type_2 = ((XtendVariableDeclarationImpl)e).getType();
                      String _identifier = _type_2.getIdentifier();
                      _builder.append(_identifier, "");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                  _builder.append("value >> ");
                  String _name = ((XtendVariableDeclarationImpl)e).getName();
                  _builder.append(_name, "");
                  _builder.newLineIfNotEmpty();
                  _builder.append("-------------------");
                  _builder.newLine();
                }
              }
              _builder.append("-------------");
              _builder.newLine();
            }
          }
        }
      }
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  /**
   * Returns a long trace of all the info of that field.
   */
  public final String describeField(final FieldDeclaration f, @Extension final Tracability tracability) {
    String _xblockexpression = null;
    {
      java.util.Objects.<FieldDeclaration>requireNonNull(f, "f");
      Expression _initializer = f.getInitializer();
      final ExpressionImpl bdy = ((ExpressionImpl) _initializer);
      TypeReference type = f.getType();
      boolean _tripleEquals = (type == null);
      if (_tripleEquals) {
        MutableNamedElement _primaryGeneratedJavaElement = tracability.getPrimaryGeneratedJavaElement(f);
        TypeReference _type = ((FieldDeclaration) _primaryGeneratedJavaElement).getType();
        type = _type;
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("simpleName >> ");
      String _simpleName = f.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.newLineIfNotEmpty();
      _builder.append("type >> ");
      String _qualifiedName = ProcessorUtil.qualifiedName(type);
      _builder.append(_qualifiedName, "");
      _builder.newLineIfNotEmpty();
      {
        boolean _tripleNotEquals = (bdy != null);
        if (_tripleNotEquals) {
          _builder.append("toString  >> ");
          _builder.append(bdy, "");
          _builder.newLineIfNotEmpty();
          _builder.append("file  >> ");
          CompilationUnitImpl _compilationUnit = bdy.getCompilationUnit();
          Path _filePath = _compilationUnit.getFilePath();
          _builder.append(_filePath, "");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("toString  >> null");
          _builder.newLine();
          _builder.append("file  >> null");
          _builder.newLine();
        }
      }
      _builder.append("generated >> ");
      boolean _isGenerated = tracability.isGenerated(f);
      _builder.append(_isGenerated, "");
      _builder.newLineIfNotEmpty();
      _builder.append("source >> ");
      boolean _isSource = tracability.isSource(f);
      _builder.append(_isSource, "");
      _builder.newLineIfNotEmpty();
      _builder.append("primaryGeneratedJavaElement >> ");
      MutableNamedElement _primaryGeneratedJavaElement_1 = tracability.getPrimaryGeneratedJavaElement(f);
      _builder.append(_primaryGeneratedJavaElement_1, "");
      _builder.newLineIfNotEmpty();
      {
        boolean _tripleNotEquals_1 = (bdy != null);
        if (_tripleNotEquals_1) {
          _builder.append("delegate class >> ");
          XExpression _delegate = bdy.getDelegate();
          Class<? extends XExpression> _class = _delegate.getClass();
          _builder.append(_class, "");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("delegate class >> null");
          _builder.newLine();
        }
      }
      _builder.append("annotations >> ");
      Iterable<? extends AnnotationReference> _annotations = f.getAnnotations();
      String _qualifiedNames = ProcessorUtil.qualifiedNames(_annotations);
      _builder.append(_qualifiedNames, "");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the signature of a method/constructor as a string
   */
  public final String signature(final ExecutableDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    String _simpleName = it.getSimpleName();
    _builder.append(_simpleName, "");
    _builder.append("(");
    Iterable<? extends ParameterDeclaration> _parameters = it.getParameters();
    final Function1<ParameterDeclaration, TypeReference> _function = new Function1<ParameterDeclaration, TypeReference>() {
      public TypeReference apply(final ParameterDeclaration p) {
        return p.getType();
      }
    };
    Iterable<TypeReference> _map = IterableExtensions.map(_parameters, _function);
    final Function1<TypeReference, String> _function_1 = new Function1<TypeReference, String>() {
      public String apply(final TypeReference it) {
        return it.getName();
      }
    };
    String _join = IterableExtensions.<TypeReference>join(_map, ",", _function_1);
    _builder.append(_join, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final Void element) {
    return null;
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final Element element) {
    Class<? extends Element> _class = element.getClass();
    String _plus = (_class + " cannot be processed");
    throw new IllegalArgumentException(_plus);
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final TypeReference element) {
    return element.getName();
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final Type element) {
    return element.getQualifiedName();
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final AnnotationReference element) {
    AnnotationTypeDeclaration _annotationTypeDeclaration = element.getAnnotationTypeDeclaration();
    return _annotationTypeDeclaration.getQualifiedName();
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final CompilationUnit element) {
    Path _filePath = element.getFilePath();
    return _filePath.toString();
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final MemberDeclaration element) {
    String _xblockexpression = null;
    {
      if ((element instanceof Type)) {
        return ((Type)element).getQualifiedName();
      }
      TypeDeclaration _declaringType = element.getDeclaringType();
      String _plus = (element + ".declaringType");
      java.util.Objects.<TypeDeclaration>requireNonNull(_declaringType, _plus);
      TypeDeclaration _declaringType_1 = element.getDeclaringType();
      String _qualifiedName = _declaringType_1.getQualifiedName();
      String _plus_1 = (_qualifiedName + ".");
      String _simpleName = element.getSimpleName();
      _xblockexpression = (_plus_1 + _simpleName);
    }
    return _xblockexpression;
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final ParameterDeclaration element) {
    String _xblockexpression = null;
    {
      ExecutableDeclaration _declaringExecutable = element.getDeclaringExecutable();
      String _plus = (element + ".getDeclaringExecutable()");
      java.util.Objects.<ExecutableDeclaration>requireNonNull(_declaringExecutable, _plus);
      ExecutableDeclaration _declaringExecutable_1 = element.getDeclaringExecutable();
      String _qualifiedName = ProcessorUtil.qualifiedName(_declaringExecutable_1);
      String _plus_1 = (_qualifiedName + ".");
      String _simpleName = element.getSimpleName();
      _xblockexpression = (_plus_1 + _simpleName);
    }
    return _xblockexpression;
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final TypeParameterDeclaration element) {
    String _xblockexpression = null;
    {
      TypeParameterDeclarator _typeParameterDeclarator = element.getTypeParameterDeclarator();
      String _plus = (element + ".getTypeParameterDeclarator()");
      java.util.Objects.<TypeParameterDeclarator>requireNonNull(_typeParameterDeclarator, _plus);
      TypeParameterDeclarator _typeParameterDeclarator_1 = element.getTypeParameterDeclarator();
      String _qualifiedName = ProcessorUtil.qualifiedName(_typeParameterDeclarator_1);
      String _plus_1 = (_qualifiedName + ".");
      String _simpleName = element.getSimpleName();
      _xblockexpression = (_plus_1 + _simpleName);
    }
    return _xblockexpression;
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  protected static String _qualifiedName(final XtendMember element) {
    String _xblockexpression = null;
    {
      if ((element instanceof XtendTypeDeclaration)) {
        return ((XtendTypeDeclaration)element).getName();
      }
      XtendTypeDeclaration _declaringType = element.getDeclaringType();
      String _plus = (element + ".declaringType");
      java.util.Objects.<XtendTypeDeclaration>requireNonNull(_declaringType, _plus);
      XtendTypeDeclaration _declaringType_1 = element.getDeclaringType();
      String _name = _declaringType_1.getName();
      String _plus_1 = (_name + ".");
      String _simpleName = ProcessorUtil.simpleName(element);
      _xblockexpression = (_plus_1 + _simpleName);
    }
    return _xblockexpression;
  }
  
  /**
   * Tries to find and return the qualifiedName of the given element.
   */
  public static String qualifiedNames(final Iterable<?> elements) {
    final Function1<Object, String> _function = new Function1<Object, String>() {
      public String apply(final Object it) {
        return ProcessorUtil.qualifiedName(it);
      }
    };
    Iterable<String> _map = IterableExtensions.map(elements, _function);
    return _map.toString();
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final Void element) {
    return null;
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final XtendConstructor element) {
    XtendTypeDeclaration _declaringType = element.getDeclaringType();
    return _declaringType.getName();
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final XtendField element) {
    return element.getName();
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final XtendEnumLiteral element) {
    return element.getName();
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final XtendFunction element) {
    return element.getName();
  }
  
  /**
   * Tries to find and return the simpleName of the given XtendMember.
   */
  protected static String _simpleName(final XtendTypeDeclaration element) {
    final String qualifiedName = element.getName();
    final char dot = '.';
    final int index = qualifiedName.lastIndexOf(dot);
    if ((index < 0)) {
      return qualifiedName;
    }
    return qualifiedName.substring((index + 1));
  }
  
  /**
   * Tries to find "non-Xtend" matching type
   */
  public final TypeDeclaration findNonXtend(final TypeDeclaration td) {
    if ((td instanceof XtendTypeDeclaration)) {
      final String name = td.getQualifiedName();
      Iterable<? extends MutableTypeDeclaration> _mutableTypes = this.getMutableTypes();
      final Function1<MutableTypeDeclaration, Boolean> _function = new Function1<MutableTypeDeclaration, Boolean>() {
        public Boolean apply(final MutableTypeDeclaration it) {
          String _qualifiedName = it.getQualifiedName();
          return Boolean.valueOf(Objects.equal(_qualifiedName, name));
        }
      };
      final MutableTypeDeclaration result = IterableExtensions.findFirst(_mutableTypes, _function);
      boolean _tripleNotEquals = (result != null);
      if (_tripleNotEquals) {
        return result;
      }
    }
    return td;
  }
  
  /**
   * Tries to find Xtend matching type
   */
  public final TypeDeclaration findXtend(final TypeDeclaration td) {
    if ((td instanceof MutableTypeDeclaration)) {
      final String name = ((MutableTypeDeclaration)td).getQualifiedName();
      Iterable<? extends TypeDeclaration> _xtendTypes = this.getXtendTypes();
      final Function1<TypeDeclaration, Boolean> _function = new Function1<TypeDeclaration, Boolean>() {
        public Boolean apply(final TypeDeclaration it) {
          String _qualifiedName = it.getQualifiedName();
          return Boolean.valueOf(Objects.equal(_qualifiedName, name));
        }
      };
      final TypeDeclaration result = IterableExtensions.findFirst(_xtendTypes, _function);
      boolean _tripleNotEquals = (result != null);
      if (_tripleNotEquals) {
        return result;
      }
    }
    return td;
  }
  
  /**
   * Builds the standard log message format
   */
  private String buildMessage(final boolean debug, final Class<?> who, final String where, final String message, final Throwable t) {
    String _xblockexpression = null;
    {
      String _xifexpression = null;
      if (debug) {
        _xifexpression = ("DEBUG: " + this.phase);
      } else {
        _xifexpression = this.phase;
      }
      String phase = _xifexpression;
      boolean _tripleEquals = (phase == null);
      if (_tripleEquals) {
        phase = "phase?";
      }
      String msg = "null";
      boolean _tripleNotEquals = (message != null);
      if (_tripleNotEquals) {
        String _replaceAll = message.replaceAll("com.blockwithme.util.xtend.annotations.", "cbuxa.");
        String _replaceAll_1 = _replaceAll.replaceAll("com.blockwithme.util.xtend.", "cbux.");
        msg = _replaceAll_1;
      }
      boolean _notEquals = (!Objects.equal(t, null));
      if (_notEquals) {
        String _asString = this.asString(t);
        String _plus = ((msg + "\n") + _asString);
        msg = _plus;
      }
      String who2 = "who?";
      boolean _tripleNotEquals_1 = (who != null);
      if (_tripleNotEquals_1) {
        String _simpleName = who.getSimpleName();
        who2 = _simpleName;
      }
      String _time = ProcessorUtil.time();
      String _plus_1 = (_time + phase);
      String _plus_2 = (_plus_1 + ": ");
      String _plus_3 = (_plus_2 + who2);
      String _plus_4 = (_plus_3 + ".");
      String _plus_5 = (_plus_4 + where);
      String _plus_6 = (_plus_5 + ": ");
      _xblockexpression = (_plus_6 + msg);
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the element to use when logging
   */
  private Element extractLoggingElement(final Element what) {
    NamedElement _xifexpression = null;
    boolean _and = false;
    boolean _tripleNotEquals = (what != null);
    if (!_tripleNotEquals) {
      _and = false;
    } else {
      _and = (!(what instanceof CompilationUnit));
    }
    if (_and) {
      if ((what instanceof AbstractElementImpl)) {
        final AbstractElementImpl<? extends EObject> element = ((AbstractElementImpl<? extends EObject>) what);
        EObject _delegate = element.getDelegate();
        final Resource resource = _delegate.eResource();
        XtendFile _xtendFile = this.compilationUnit.getXtendFile();
        Resource _eResource = _xtendFile.eResource();
        boolean _equals = Objects.equal(resource, _eResource);
        if (_equals) {
          return what;
        } else {
          return this.element;
        }
      } else {
        if ((what instanceof XtendTypeDeclaration)) {
          return this.findNonXtend(((TypeDeclaration) what));
        } else {
          Class<? extends Element> _class = what.getClass();
          String _plus = ("Element type: " + _class);
          throw new IllegalArgumentException(_plus);
        }
      }
    } else {
      _xifexpression = this.element;
    }
    return _xifexpression;
  }
  
  /**
   * Converts a Throwable stack-trace to a String
   */
  public final String asString(final Throwable t) {
    String _xblockexpression = null;
    {
      final StringWriter sw = new StringWriter();
      PrintWriter _printWriter = new PrintWriter(sw);
      t.printStackTrace(_printWriter);
      _xblockexpression = sw.toString();
    }
    return _xblockexpression;
  }
  
  /**
   * Records an error for the given element
   * 
   * @param who The class in which the message was produced.
   * @param where The method where message was produced.
   * @param what The element to which associate the message
   * @param message The message
   */
  public final void error(final Class<?> who, final String where, final Element what, final String message) {
    final Element logElem = this.extractLoggingElement(what);
    if (ProcessorUtil.ERROR_AS_WARNING) {
      String _buildMessage = this.buildMessage(false, who, where, message, null);
      this.problemSupport.addWarning(logElem, _buildMessage);
    } else {
      String _buildMessage_1 = this.buildMessage(false, who, where, message, null);
      this.problemSupport.addError(logElem, _buildMessage_1);
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
  public final void error(final Class<?> who, final String where, final Element what, final String message, final Throwable t) {
    final Element logElem = this.extractLoggingElement(what);
    if (ProcessorUtil.ERROR_AS_WARNING) {
      String _buildMessage = this.buildMessage(false, who, where, message, t);
      this.problemSupport.addWarning(logElem, _buildMessage);
    } else {
      String _buildMessage_1 = this.buildMessage(false, who, where, message, t);
      this.problemSupport.addError(logElem, _buildMessage_1);
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
  public final void warn(final Class<?> who, final String where, final Element what, final String message) {
    final Element logElem = this.extractLoggingElement(what);
    String _buildMessage = this.buildMessage(false, who, where, message, null);
    this.problemSupport.addWarning(logElem, _buildMessage);
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
  public final void warn(final Class<?> who, final String where, final Element what, final String message, final Throwable t) {
    final Element logElem = this.extractLoggingElement(what);
    String _buildMessage = this.buildMessage(false, who, where, message, t);
    this.problemSupport.addWarning(logElem, _buildMessage);
  }
  
  /**
   * Records a warning for the given element
   * 
   * @param who The class in which the message was produced.
   * @param where The method where message was produced.
   * @param what The element to which associate the message
   * @param message The message
   */
  public final void debug(final Class<?> who, final String where, final Element what, final String message) {
    if (ProcessorUtil.DEBUG) {
      final Element logElem = this.extractLoggingElement(what);
      String _buildMessage = this.buildMessage(true, who, where, message, null);
      this.problemSupport.addWarning(logElem, _buildMessage);
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
  public final void debug(final Class<?> who, final String where, final Element what, final String message, final Throwable t) {
    if (ProcessorUtil.DEBUG) {
      final Element logElem = this.extractLoggingElement(what);
      String _buildMessage = this.buildMessage(true, who, where, message, t);
      this.problemSupport.addWarning(logElem, _buildMessage);
    }
  }
  
  /**
   * Returns true, if the given element is a marker interface
   */
  public final boolean isMarker(final Element element) {
    boolean _xblockexpression = false;
    {
      if ((element instanceof InterfaceDeclaration)) {
        Iterable<? extends TypeDeclaration> _findParents = this.findParents(((TypeDeclaration)element));
        for (final TypeDeclaration parent : _findParents) {
          boolean _notEquals = (!Objects.equal(parent, element));
          if (_notEquals) {
            boolean _isMarker = this.isMarker(parent);
            boolean _not = (!_isMarker);
            if (_not) {
              return false;
            }
          }
        }
        return true;
      }
      if ((element instanceof TypeReference)) {
        Type _type = ((TypeReference)element).getType();
        return this.isMarker(_type);
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  /**
   * Utility method that removes generics type arguments from the class.qualifiedName
   */
  public final String removeGeneric(final String className) {
    String _xblockexpression = null;
    {
      final int index = className.indexOf("<");
      String _xifexpression = null;
      if ((index > 0)) {
        _xifexpression = className.substring(0, index);
      } else {
        _xifexpression = className;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Utility method checks if a type is a String type.
   */
  public final boolean isString(final TypeReference fieldType) {
    TypeReference _string = this.getString();
    return _string.isAssignableFrom(fieldType);
  }
  
  /**
   * Checks if fieldType is of 'java.util.List' type.
   */
  public final boolean isList(final TypeReference fieldType) {
    boolean _xifexpression = false;
    List<TypeReference> _actualTypeArguments = fieldType.getActualTypeArguments();
    int _size = _actualTypeArguments.size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      TypeReference _list = this.getList();
      String _name = fieldType.getName();
      String _removeGeneric = this.removeGeneric(_name);
      TypeReference _newTypeReference = this.newTypeReference(_removeGeneric);
      _xifexpression = _list.isAssignableFrom(_newTypeReference);
    } else {
      TypeReference _list_1 = this.getList();
      _xifexpression = _list_1.isAssignableFrom(fieldType);
    }
    return _xifexpression;
  }
  
  public final Type findTypeGlobally(final Class<?> type) {
    TypeLookupImpl _typeLookup = this.compilationUnit.getTypeLookup();
    return _typeLookup.findTypeGlobally(type);
  }
  
  public final Type findTypeGlobally(final String typeName) {
    TypeLookupImpl _typeLookup = this.compilationUnit.getTypeLookup();
    return _typeLookup.findTypeGlobally(typeName);
  }
  
  /**
   * Utility method that finds an interface in the global context, returns null if not found
   */
  public MutableInterfaceDeclaration findInterface(final String name) {
    MutableInterfaceDeclaration _xblockexpression = null;
    {
      final Type found = this.findTypeGlobally(name);
      MutableInterfaceDeclaration _xifexpression = null;
      if ((found instanceof MutableInterfaceDeclaration)) {
        _xifexpression = ((MutableInterfaceDeclaration) found);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Utility method that finds an interface in the global context, fails if not found
   */
  public MutableInterfaceDeclaration getInterface(final String name) {
    MutableInterfaceDeclaration _xblockexpression = null;
    {
      final MutableInterfaceDeclaration result = this.findInterface(name);
      boolean _tripleEquals = (result == null);
      if (_tripleEquals) {
        throw new MissingTypeException(name);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Utility method that finds a class in the global context, returns null if not found
   */
  public MutableClassDeclaration findClass(final String name) {
    MutableClassDeclaration _xblockexpression = null;
    {
      final Type found = this.findTypeGlobally(name);
      MutableClassDeclaration _xifexpression = null;
      if ((found instanceof MutableClassDeclaration)) {
        _xifexpression = ((MutableClassDeclaration) found);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Utility method that finds a class in the global context, fails if not found
   */
  public MutableClassDeclaration getClass(final String name) {
    MutableClassDeclaration _xblockexpression = null;
    {
      final MutableClassDeclaration result = this.findClass(name);
      boolean _tripleEquals = (result == null);
      if (_tripleEquals) {
        throw new MissingTypeException(name);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  /**
   * Searches for the *default* constructor.
   */
  public final ConstructorDeclaration findConstructor(final TypeDeclaration clazz) {
    ConstructorDeclaration _xblockexpression = null;
    {
      Iterable<? extends ConstructorDeclaration> _declaredConstructors = clazz.getDeclaredConstructors();
      for (final ConstructorDeclaration c : _declaredConstructors) {
        Iterable<? extends ParameterDeclaration> _parameters = c.getParameters();
        boolean _isEmpty = IterableExtensions.isEmpty(_parameters);
        if (_isEmpty) {
          return c;
        }
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  /**
   * Searches for the *default* constructor.
   */
  public final MutableConstructorDeclaration findConstructor(final MutableClassDeclaration clazz) {
    ConstructorDeclaration _findConstructor = this.findConstructor(((TypeDeclaration) clazz));
    return ((MutableConstructorDeclaration) _findConstructor);
  }
  
  /**
   * Searches for the method with the given name and parameters.
   */
  public final MethodDeclaration findMethod(final TypeDeclaration clazz, final String name, final TypeReference... parameterTypes) {
    MethodDeclaration _xblockexpression = null;
    {
      final List<TypeReference> tmp = IterableExtensions.<TypeReference>toList(((Iterable<TypeReference>)Conversions.doWrapArray(parameterTypes)));
      Iterable<? extends MethodDeclaration> _declaredMethods = clazz.getDeclaredMethods();
      for (final MethodDeclaration m : _declaredMethods) {
        boolean _and = false;
        String _simpleName = m.getSimpleName();
        boolean _equals = Objects.equal(_simpleName, name);
        if (!_equals) {
          _and = false;
        } else {
          Iterable<? extends ParameterDeclaration> _parameters = m.getParameters();
          List<? extends ParameterDeclaration> _list = IterableExtensions.toList(_parameters);
          boolean _equals_1 = Objects.equal(_list, tmp);
          _and = _equals_1;
        }
        if (_and) {
          return m;
        }
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  /**
   * Searches for the method with the given name and parameters.
   */
  public final MutableMethodDeclaration findMethod(final MutableClassDeclaration clazz, final String name, final TypeReference... parameterTypes) {
    MethodDeclaration _findMethod = this.findMethod(((TypeDeclaration) clazz), name, parameterTypes);
    return ((MutableMethodDeclaration) _findMethod);
  }
  
  /**
   * The List TypeReference
   */
  public final TypeReference getList() {
    return this.list;
  }
  
  /**
   * The Arrays TypeReference
   */
  public final TypeReference getArrays() {
    return this.arrays;
  }
  
  /**
   * The Objects TypeReference
   */
  public final TypeReference getObjects() {
    return this.objects;
  }
  
  /**
   * The Override Annotation Type
   */
  public final Type getOverride() {
    return this._override;
  }
  
  public TypeReference getAnyType() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _anyType = _typeReferenceProvider.getAnyType();
    return java.util.Objects.<TypeReference>requireNonNull(_anyType, "getAnyType()");
  }
  
  public TypeReference getList(final TypeReference param) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _list = _typeReferenceProvider.getList(param);
    return java.util.Objects.<TypeReference>requireNonNull(_list, "getList(param)");
  }
  
  public TypeReference getObject() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _object = _typeReferenceProvider.getObject();
    return java.util.Objects.<TypeReference>requireNonNull(_object, "getObject()");
  }
  
  public TypeReference getPrimitiveBoolean() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveBoolean = _typeReferenceProvider.getPrimitiveBoolean();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveBoolean, "getPrimitiveBoolean()");
  }
  
  public TypeReference getPrimitiveByte() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveByte = _typeReferenceProvider.getPrimitiveByte();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveByte, "getPrimitiveByte()");
  }
  
  public TypeReference getPrimitiveChar() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveChar = _typeReferenceProvider.getPrimitiveChar();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveChar, "getPrimitiveChar()");
  }
  
  public TypeReference getPrimitiveDouble() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveDouble = _typeReferenceProvider.getPrimitiveDouble();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveDouble, "getPrimitiveDouble()");
  }
  
  public TypeReference getPrimitiveFloat() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveFloat = _typeReferenceProvider.getPrimitiveFloat();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveFloat, "getPrimitiveFloat()");
  }
  
  public TypeReference getPrimitiveInt() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveInt = _typeReferenceProvider.getPrimitiveInt();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveInt, "getPrimitiveInt()");
  }
  
  public TypeReference getPrimitiveLong() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveLong = _typeReferenceProvider.getPrimitiveLong();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveLong, "getPrimitiveLong()");
  }
  
  public TypeReference getPrimitiveShort() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveShort = _typeReferenceProvider.getPrimitiveShort();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveShort, "getPrimitiveShort()");
  }
  
  public TypeReference getPrimitiveVoid() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _primitiveVoid = _typeReferenceProvider.getPrimitiveVoid();
    return java.util.Objects.<TypeReference>requireNonNull(_primitiveVoid, "getPrimitiveVoid()");
  }
  
  public TypeReference getSet(final TypeReference param) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _set = _typeReferenceProvider.getSet(param);
    return java.util.Objects.<TypeReference>requireNonNull(_set, "getSet(param)");
  }
  
  public TypeReference getString() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _string = _typeReferenceProvider.getString();
    return java.util.Objects.<TypeReference>requireNonNull(_string, "getString()");
  }
  
  public TypeReference getClassTypeRef() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newTypeReference = _typeReferenceProvider.newTypeReference(Class.class);
    return java.util.Objects.<TypeReference>requireNonNull(_newTypeReference, "newTypeReference(Class)");
  }
  
  public TypeReference newArrayTypeReference(final TypeReference componentType) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newArrayTypeReference = _typeReferenceProvider.newArrayTypeReference(componentType);
    return java.util.Objects.<TypeReference>requireNonNull(_newArrayTypeReference, (("newArrayTypeReference(" + componentType) + ")"));
  }
  
  public TypeReference newTypeReference(final String typeName, final TypeReference... typeArguments) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newTypeReference = _typeReferenceProvider.newTypeReference(typeName, typeArguments);
    return java.util.Objects.<TypeReference>requireNonNull(_newTypeReference, (("newTypeReference(" + typeName) + ")"));
  }
  
  public TypeReference newTypeReference(final Type typeDeclaration, final TypeReference... typeArguments) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newTypeReference = _typeReferenceProvider.newTypeReference(typeDeclaration, typeArguments);
    return java.util.Objects.<TypeReference>requireNonNull(_newTypeReference, (("newTypeReference(" + typeDeclaration) + ")"));
  }
  
  public TypeReference newTypeReference(final Class<?> clazz, final TypeReference... typeArguments) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newTypeReference = _typeReferenceProvider.newTypeReference(clazz, typeArguments);
    return java.util.Objects.<TypeReference>requireNonNull(_newTypeReference, (("newTypeReference(" + clazz) + ")"));
  }
  
  public TypeReference newWildcardTypeReference() {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newWildcardTypeReference = _typeReferenceProvider.newWildcardTypeReference();
    return java.util.Objects.<TypeReference>requireNonNull(_newWildcardTypeReference, "newWildcardTypeReference()");
  }
  
  public TypeReference newWildcardTypeReference(final TypeReference upperBound) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newWildcardTypeReference = _typeReferenceProvider.newWildcardTypeReference(upperBound);
    return java.util.Objects.<TypeReference>requireNonNull(_newWildcardTypeReference, "newWildcardTypeReference(upperBound)");
  }
  
  public TypeReference newWildcardTypeReferenceWithLowerBound(final TypeReference lowerBound) {
    TypeReferenceProvider _typeReferenceProvider = this.compilationUnit.getTypeReferenceProvider();
    TypeReference _newWildcardTypeReferenceWithLowerBound = _typeReferenceProvider.newWildcardTypeReferenceWithLowerBound(lowerBound);
    return java.util.Objects.<TypeReference>requireNonNull(_newWildcardTypeReferenceWithLowerBound, "newWildcardTypeReferenceWithLowerBound(lowerBound)");
  }
  
  public TypeReference newTypeReferenceWithGenerics(final String typeName) {
    TypeReference _xblockexpression = null;
    {
      boolean _endsWith = typeName.endsWith(">");
      if (_endsWith) {
        final ArrayList<TypeReference> list = CollectionLiterals.<TypeReference>newArrayList();
        final int index = typeName.indexOf("<");
        int _length = typeName.length();
        int _minus = (_length - 1);
        final String typeParams = typeName.substring((index + 1), _minus);
        String[] _split = typeParams.split(",");
        for (final String t : _split) {
          {
            final String t2 = t.trim();
            boolean _isEmpty = t2.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              TypeReference _newTypeReferenceWithGenerics = this.newTypeReferenceWithGenerics(t2);
              list.add(_newTypeReferenceWithGenerics);
            }
          }
        }
        String _substring = typeName.substring(0, index);
        return this.newTypeReference(_substring, ((TypeReference[])Conversions.unwrapArray(list, TypeReference.class)));
      }
      _xblockexpression = this.newTypeReference(typeName);
    }
    return _xblockexpression;
  }
  
  public AnnotationReference newAnnotationReference(final String annotationTypeName) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationTypeName);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final Type annotationTypeDelcaration) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationTypeDelcaration);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final Class<?> annotationClass) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationClass);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final AnnotationReference annotationReference) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationReference);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final String annotationTypeName, final Procedure1<AnnotationReferenceBuildContext> initializer) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationTypeName, initializer);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final Type annotationTypeDelcaration, final Procedure1<AnnotationReferenceBuildContext> initializer) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationTypeDelcaration, initializer);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final Class<?> annotationClass, final Procedure1<AnnotationReferenceBuildContext> initializer) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationClass, initializer);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  public AnnotationReference newAnnotationReference(final AnnotationReference annotationReference, final Procedure1<AnnotationReferenceBuildContext> initializer) {
    AnnotationReferenceProvider _annotationReferenceProvider = this.compilationUnit.getAnnotationReferenceProvider();
    AnnotationReference _newAnnotationReference = _annotationReferenceProvider.newAnnotationReference(annotationReference, initializer);
    return java.util.Objects.<AnnotationReference>requireNonNull(_newAnnotationReference);
  }
  
  private Iterable<? extends TypeDeclaration> findDirectParents2(final TypeDeclaration td) {
    if (td instanceof ClassDeclaration) {
      return _findDirectParents2((ClassDeclaration)td);
    } else if (td instanceof EnumerationTypeDeclaration) {
      return _findDirectParents2((EnumerationTypeDeclaration)td);
    } else if (td instanceof InterfaceDeclaration) {
      return _findDirectParents2((InterfaceDeclaration)td);
    } else if (td != null) {
      return _findDirectParents2(td);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(td).toString());
    }
  }
  
  public static String qualifiedName(final Object element) {
    if (element instanceof MemberDeclaration) {
      return _qualifiedName((MemberDeclaration)element);
    } else if (element instanceof ParameterDeclaration) {
      return _qualifiedName((ParameterDeclaration)element);
    } else if (element instanceof TypeParameterDeclaration) {
      return _qualifiedName((TypeParameterDeclaration)element);
    } else if (element instanceof XtendMember) {
      return _qualifiedName((XtendMember)element);
    } else if (element instanceof CompilationUnit) {
      return _qualifiedName((CompilationUnit)element);
    } else if (element instanceof Type) {
      return _qualifiedName((Type)element);
    } else if (element instanceof AnnotationReference) {
      return _qualifiedName((AnnotationReference)element);
    } else if (element instanceof TypeReference) {
      return _qualifiedName((TypeReference)element);
    } else if (element == null) {
      return _qualifiedName((Void)null);
    } else if (element instanceof Element) {
      return _qualifiedName((Element)element);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(element).toString());
    }
  }
  
  public static String simpleName(final XtendMember element) {
    if (element instanceof XtendConstructor) {
      return _simpleName((XtendConstructor)element);
    } else if (element instanceof XtendFunction) {
      return _simpleName((XtendFunction)element);
    } else if (element instanceof XtendEnumLiteral) {
      return _simpleName((XtendEnumLiteral)element);
    } else if (element instanceof XtendField) {
      return _simpleName((XtendField)element);
    } else if (element instanceof XtendTypeDeclaration) {
      return _simpleName((XtendTypeDeclaration)element);
    } else if (element == null) {
      return _simpleName((Void)null);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(element).toString());
    }
  }
}
