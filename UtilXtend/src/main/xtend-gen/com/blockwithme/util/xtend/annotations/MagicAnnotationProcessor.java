package com.blockwithme.util.xtend.annotations;

import com.blockwithme.util.xtend.AntiClassLoaderCache;
import com.blockwithme.util.xtend.annotations.MissingTypeException;
import com.blockwithme.util.xtend.annotations.Processor;
import com.blockwithme.util.xtend.annotations.ProcessorUtil;
import com.google.common.base.Objects;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend.core.macro.declaration.CompilationUnitImpl;
import org.eclipse.xtend.core.xtend.XtendFile;
import org.eclipse.xtend.lib.macro.CodeGenerationContext;
import org.eclipse.xtend.lib.macro.CodeGenerationParticipant;
import org.eclipse.xtend.lib.macro.RegisterGlobalsContext;
import org.eclipse.xtend.lib.macro.RegisterGlobalsParticipant;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.TransformationParticipant;
import org.eclipse.xtend.lib.macro.declaration.Element;
import org.eclipse.xtend.lib.macro.declaration.MutableNamedElement;
import org.eclipse.xtend.lib.macro.declaration.MutableTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.NamedElement;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeDeclaration;
import org.eclipse.xtend.lib.macro.file.FileLocations;
import org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport;
import org.eclipse.xtend.lib.macro.file.Path;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

/**
 * Process classes annotated with @Magic
 * 
 * Somehow, some of my code is executed *outside* of the active annotation processor
 * hook methods, and that code needs the cached state, so I cannot clear the cache
 * at the end of the API hook methods.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class MagicAnnotationProcessor implements RegisterGlobalsParticipant<NamedElement>, CodeGenerationParticipant<NamedElement>, TransformationParticipant<MutableNamedElement> {
  /**
   * Cache key for the processor names
   */
  private final static String PROCESSORS_NAMES = "PROCESSORS_NAMES";
  
  /**
   * File containing the processor names
   */
  private final static String PROCESSORS_NAMES_FILE = ("META-INF/services/" + Processor.class.getName());
  
  /**
   * The processors
   */
  private static Processor<?, ?>[] PROCESSORS;
  
  private final static ProcessorUtil processorUtil = new ProcessorUtil();
  
  private final static HashMap<String, Boolean> ACCEPT = new HashMap<String, Boolean>();
  
  private <TD extends TypeDeclaration> void loop(final List<? extends NamedElement> annotatedSourceElements, final String phase, final Procedure3<Map<String, Object>, Processor, TD> lambda, final Procedure3<Map<String, Object>, Processor, String> lambdaAfter) {
    final CompilationUnitImpl compilationUnit = ProcessorUtil.getCompilationUnit(annotatedSourceElements);
    boolean _tripleNotEquals = (compilationUnit != null);
    if (_tripleNotEquals) {
      final Class<? extends MagicAnnotationProcessor> clazz = this.getClass();
      NamedElement _get = annotatedSourceElements.get(0);
      MagicAnnotationProcessor.processorUtil.setElement(phase, _get);
      Path _filePath = compilationUnit.getFilePath();
      final String pathName = _filePath.toString();
      final boolean register = Objects.equal("register", phase);
      final boolean transform = Objects.equal("transform", phase);
      final boolean generate = Objects.equal("generate", phase);
      if ((!register)) {
        AntiClassLoaderCache.clear((pathName + ".register."));
      }
      if ((!transform)) {
        AntiClassLoaderCache.clear((pathName + ".transform."));
      }
      if ((!generate)) {
        AntiClassLoaderCache.clear((pathName + ".generate."));
      }
      Class<? extends MagicAnnotationProcessor> _class = this.getClass();
      final Processor<?, ?>[] processors = MagicAnnotationProcessor.getProcessors(_class, annotatedSourceElements);
      final ArrayList<Processor<?, ?>> allProcessors = CollectionLiterals.<Processor<?, ?>>newArrayList(processors);
      Iterable<? extends TypeDeclaration> _xifexpression = null;
      if (transform) {
        _xifexpression = MagicAnnotationProcessor.processorUtil.getAllMutableTypes();
      } else {
        _xifexpression = MagicAnnotationProcessor.processorUtil.getAllXtendTypes();
      }
      final List<? extends TypeDeclaration> allTypes = IterableExtensions.toList(_xifexpression);
      int _size = allTypes.size();
      int _plus = (_size + 1);
      TypeDeclaration[] _newArrayOfSize = new TypeDeclaration[_plus];
      final TypeDeclaration[] types = allTypes.<TypeDeclaration>toArray(_newArrayOfSize);
      final ArrayList<TypeDeclaration> todoTypes = new ArrayList<TypeDeclaration>(allTypes);
      final ArrayList<TypeDeclaration> doneTypes = CollectionLiterals.<TypeDeclaration>newArrayList();
      String _qualifiedNames = ProcessorUtil.qualifiedNames(allTypes);
      String _plus_1 = ("Top-Level Types: " + _qualifiedNames);
      MagicAnnotationProcessor.processorUtil.warn(clazz, "loop", null, _plus_1);
      final HashMap<String, Object> processingContext = new HashMap<String, Object>();
      processingContext.put(Processor.PC_ALL_FILE_TYPES, allTypes);
      processingContext.put(Processor.PC_TODO_TYPES, todoTypes);
      processingContext.put(Processor.PC_DONE_TYPES, doneTypes);
      processingContext.put(Processor.PC_ALL_PROCESSORS, allProcessors);
      String _xifexpression_1 = null;
      boolean _isEmpty = allTypes.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        String _xblockexpression = null;
        {
          TypeDeclaration _get_1 = allTypes.get(0);
          final String qn = _get_1.getQualifiedName();
          final int dot = qn.lastIndexOf(".");
          _xblockexpression = qn.substring(0, dot);
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        _xifexpression_1 = "";
      }
      final String pkgName = _xifexpression_1;
      processingContext.put(Processor.PC_PACKAGE, pkgName);
      XtendFile _xtendFile = compilationUnit.getXtendFile();
      XImportSection _importSection = _xtendFile.getImportSection();
      EList<XImportDeclaration> _importDeclarations = _importSection.getImportDeclarations();
      final Function1<XImportDeclaration, String> _function = new Function1<XImportDeclaration, String>() {
        public String apply(final XImportDeclaration it) {
          return it.getImportedTypeName();
        }
      };
      List<String> _map = ListExtensions.<XImportDeclaration, String>map(_importDeclarations, _function);
      String _plus_2 = ("IMPORTS: " + _map);
      MagicAnnotationProcessor.processorUtil.debug(clazz, "loop", null, _plus_2);
      XtendFile _xtendFile_1 = compilationUnit.getXtendFile();
      XImportSection _importSection_1 = _xtendFile_1.getImportSection();
      EList<XImportDeclaration> _importDeclarations_1 = _importSection_1.getImportDeclarations();
      final Function1<XImportDeclaration, String> _function_1 = new Function1<XImportDeclaration, String>() {
        public String apply(final XImportDeclaration it) {
          return it.getImportedTypeName();
        }
      };
      List<String> _map_1 = ListExtensions.<XImportDeclaration, String>map(_importDeclarations_1, _function_1);
      for (final String t : _map_1) {
        Type _findTypeGlobally = MagicAnnotationProcessor.processorUtil.findTypeGlobally(t);
        boolean _tripleEquals = (_findTypeGlobally == null);
        if (_tripleEquals) {
          boolean found = false;
          try {
            Class<?> _forName = Class.forName(t);
            boolean _notEquals = (!Objects.equal(_forName, null));
            found = _notEquals;
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception e = (Exception)_t;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
          MagicAnnotationProcessor.processorUtil.error(clazz, "loop", null, 
            (((("Import " + t) + " cannot be resolved! (As Class: ") + Boolean.valueOf(found)) + ")"));
        }
      }
      for (final TypeDeclaration td : types) {
        {
          processingContext.put(Processor.PC_PROCESSED_TYPE, td);
          todoTypes.remove(td);
          final ArrayList<Processor<?, ?>> todoProcessors = CollectionLiterals.<Processor<?, ?>>newArrayList(processors);
          final ArrayList<Processor> doneProcessors = CollectionLiterals.<Processor>newArrayList();
          processingContext.put(Processor.PC_TODO_PROCESSORS, todoProcessors);
          processingContext.put(Processor.PC_DONE_PROCESSORS, doneProcessors);
          String _xifexpression_2 = null;
          boolean _tripleEquals_1 = (td == null);
          if (_tripleEquals_1) {
            _xifexpression_2 = null;
          } else {
            _xifexpression_2 = td.getQualifiedName();
          }
          final String qualifiedName = _xifexpression_2;
          for (final Processor<?, ?> p : processors) {
            {
              todoProcessors.remove(p);
              processingContext.put(Processor.PC_CURRENT_PROCESSOR, p);
              p.setProcessorUtil(MagicAnnotationProcessor.processorUtil);
              try {
                boolean _tripleEquals_2 = (td == null);
                if (_tripleEquals_2) {
                  lambdaAfter.apply(processingContext, p, pkgName);
                } else {
                  Class<? extends Processor> _class_1 = p.getClass();
                  String _name = _class_1.getName();
                  String _plus_3 = (_name + ":");
                  final String acceptKey = (_plus_3 + qualifiedName);
                  Boolean accept = MagicAnnotationProcessor.ACCEPT.get(acceptKey);
                  boolean _tripleEquals_3 = (accept == null);
                  if (_tripleEquals_3) {
                    boolean _accept = p.accept(processingContext, td);
                    accept = Boolean.valueOf(_accept);
                    MagicAnnotationProcessor.ACCEPT.put(acceptKey, accept);
                    if ((!(accept).booleanValue())) {
                      MagicAnnotationProcessor.processorUtil.debug(clazz, "loop", td, 
                        (((((("NOT Calling: " + p) + ".") + phase) + "(") + qualifiedName) + ")"));
                    }
                  }
                  if ((accept).booleanValue()) {
                    MagicAnnotationProcessor.processorUtil.debug(clazz, "loop", td, 
                      (((((("Calling: " + p) + ".") + phase) + "(") + qualifiedName) + ")"));
                    lambda.apply(processingContext, p, ((TD) td));
                  }
                }
              } catch (final Throwable _t_1) {
                if (_t_1 instanceof MissingTypeException) {
                  final MissingTypeException ex = (MissingTypeException)_t_1;
                  String _plus_4 = (p + ": ");
                  String _plus_5 = (_plus_4 + qualifiedName);
                  String _plus_6 = (_plus_5 + " ");
                  String _plus_7 = (_plus_6 + ex);
                  MagicAnnotationProcessor.processorUtil.error(clazz, "loop", td, _plus_7);
                } else if (_t_1 instanceof Throwable) {
                  final Throwable t_1 = (Throwable)_t_1;
                  String _plus_8 = (p + ": ");
                  String _plus_9 = (_plus_8 + qualifiedName);
                  MagicAnnotationProcessor.processorUtil.error(clazz, "loop", td, _plus_9, t_1);
                } else {
                  throw Exceptions.sneakyThrow(_t_1);
                }
              }
              doneProcessors.add(p);
            }
          }
          doneTypes.add(td);
        }
      }
    }
  }
  
  /**
   * Implements the doRegisterGlobals() phase
   */
  public void doRegisterGlobals(final List<? extends NamedElement> annotatedSourceElements, @Extension final RegisterGlobalsContext context) {
    final Procedure3<Map<String, Object>, Processor, TypeDeclaration> _function = new Procedure3<Map<String, Object>, Processor, TypeDeclaration>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final TypeDeclaration td) {
        p.register(processingContext, td, context);
      }
    };
    final Procedure3<Map<String, Object>, Processor, String> _function_1 = new Procedure3<Map<String, Object>, Processor, String>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final String pkgName) {
        p.afterRegister(processingContext, pkgName, context);
      }
    };
    this.<TypeDeclaration>loop(annotatedSourceElements, "register", _function, _function_1);
  }
  
  /**
   * Implements the doGenerateCode() phase
   */
  public void doGenerateCode(final List<? extends NamedElement> annotatedSourceElements, @Extension final CodeGenerationContext context) {
    final Procedure3<Map<String, Object>, Processor, TypeDeclaration> _function = new Procedure3<Map<String, Object>, Processor, TypeDeclaration>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final TypeDeclaration td) {
        p.generate(processingContext, td, context);
      }
    };
    final Procedure3<Map<String, Object>, Processor, String> _function_1 = new Procedure3<Map<String, Object>, Processor, String>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final String pkgName) {
        p.afterGenerate(processingContext, pkgName, context);
      }
    };
    this.<TypeDeclaration>loop(annotatedSourceElements, "generate", _function, _function_1);
  }
  
  /**
   * Implements the doTransform() phase
   */
  public void doTransform(final List<? extends MutableNamedElement> annotatedSourceElements, @Extension final TransformationContext context) {
    final Procedure3<Map<String, Object>, Processor, MutableTypeDeclaration> _function = new Procedure3<Map<String, Object>, Processor, MutableTypeDeclaration>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final MutableTypeDeclaration mtd) {
        p.transform(processingContext, mtd, context);
      }
    };
    final Procedure3<Map<String, Object>, Processor, String> _function_1 = new Procedure3<Map<String, Object>, Processor, String>() {
      public void apply(final Map<String, Object> processingContext, final Processor p, final String pkgName) {
        p.afterTransform(processingContext, pkgName, context);
      }
    };
    this.<MutableTypeDeclaration>loop(annotatedSourceElements, "transform", _function, _function_1);
  }
  
  /**
   * Returns the list of processors.
   */
  private static synchronized Processor<?, ?>[] getProcessors(final Class<?> magicClass, final List<? extends NamedElement> annotatedSourceElements) {
    Processor<?, ?>[] _xblockexpression = null;
    {
      final ConcurrentMap<String, Object> cache = AntiClassLoaderCache.getCache();
      boolean _equals = Objects.equal(MagicAnnotationProcessor.PROCESSORS, null);
      if (_equals) {
        final ArrayList<Processor> list = CollectionLiterals.<Processor>newArrayList();
        final CompilationUnitImpl compilationUnit = ProcessorUtil.getCompilationUnit(annotatedSourceElements);
        final NamedElement element = annotatedSourceElements.get(0);
        Object _get = cache.get(MagicAnnotationProcessor.PROCESSORS_NAMES);
        String[] names = ((String[]) _get);
        boolean _equals_1 = Objects.equal(names, null);
        if (_equals_1) {
          String[] _findProcessorNames = MagicAnnotationProcessor.findProcessorNames(magicClass, compilationUnit, element);
          names = _findProcessorNames;
          cache.put(MagicAnnotationProcessor.PROCESSORS_NAMES, names);
        }
        for (final String name : names) {
          try {
            Class<?> cls = null;
            try {
              Class<?> _forName = Class.forName(name);
              cls = _forName;
            } catch (final Throwable _t) {
              if (_t instanceof Exception) {
                final Exception e = (Exception)_t;
                ClassLoader _classLoader = magicClass.getClassLoader();
                Class<?> _forName_1 = Class.forName(name, true, _classLoader);
                cls = _forName_1;
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
            Object _newInstance = cls.newInstance();
            list.add(((Processor<?, ?>) _newInstance));
          } catch (final Throwable _t_1) {
            if (_t_1 instanceof Exception) {
              final Exception ex = (Exception)_t_1;
              MagicAnnotationProcessor.processorUtil.error(magicClass, "getProcessors", null, 
                (("Could not instantiate processor for \'" + name) + "\'"), ex);
            } else {
              throw Exceptions.sneakyThrow(_t_1);
            }
          }
        }
        int _size = list.size();
        Processor[] _newArrayOfSize = new Processor[_size];
        Processor[] _array = list.<Processor>toArray(_newArrayOfSize);
        MagicAnnotationProcessor.PROCESSORS = _array;
        int _length = MagicAnnotationProcessor.PROCESSORS.length;
        boolean _tripleEquals = (_length == 0);
        if (_tripleEquals) {
          MagicAnnotationProcessor.processorUtil.error(magicClass, "getProcessors", null, 
            "No processor defined.");
        } else {
          final Function1<Processor, String> _function = new Function1<Processor, String>() {
            public String apply(final Processor it) {
              Class _class = it.getClass();
              return _class.getName();
            }
          };
          List<String> _map = ListExtensions.<Processor, String>map(list, _function);
          String _plus = ("Active processors: " + _map);
          MagicAnnotationProcessor.processorUtil.warn(magicClass, "getProcessors", null, _plus);
        }
      }
      _xblockexpression = MagicAnnotationProcessor.PROCESSORS;
    }
    return _xblockexpression;
  }
  
  /**
   * Returns the list of processor names
   */
  private static String[] findProcessorNames(final Class<?> magicClass, final CompilationUnitImpl compilationUnit, final Element element) {
    String[] _xblockexpression = null;
    {
      final ArrayList<String> list = CollectionLiterals.<String>newArrayList();
      FileLocations _fileLocations = compilationUnit.getFileLocations();
      Path _filePath = compilationUnit.getFilePath();
      final Path root = _fileLocations.getProjectFolder(_filePath);
      final Path file = root.append(MagicAnnotationProcessor.PROCESSORS_NAMES_FILE);
      MutableFileSystemSupport _fileSystemSupport = compilationUnit.getFileSystemSupport();
      final URI fileURI = _fileSystemSupport.toURI(file);
      MutableFileSystemSupport _fileSystemSupport_1 = compilationUnit.getFileSystemSupport();
      boolean _exists = _fileSystemSupport_1.exists(file);
      if (_exists) {
        try {
          MutableFileSystemSupport _fileSystemSupport_2 = compilationUnit.getFileSystemSupport();
          final CharSequence content = _fileSystemSupport_2.getContents(file);
          int _length = content.length();
          final StringBuilder buf = new StringBuilder(_length);
          buf.append(content);
          String _string = buf.toString();
          String[] _split = _string.split("\n");
          for (final String s : _split) {
            {
              final String str = s.trim();
              boolean _isEmpty = str.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                list.add(str);
              }
            }
          }
          boolean _isEmpty = list.isEmpty();
          if (_isEmpty) {
            MagicAnnotationProcessor.processorUtil.error(magicClass, "findProcessorNames", null, 
              (("Could not find processors in \'" + fileURI) + "\'"));
          } else {
            int _size = list.size();
            String[] _newArrayOfSize = new String[_size];
            String[] _array = list.<String>toArray(_newArrayOfSize);
            for (final String name : _array) {
              try {
                Class<?> cls = null;
                try {
                  Class<?> _forName = Class.forName(name);
                  cls = _forName;
                } catch (final Throwable _t) {
                  if (_t instanceof Exception) {
                    final Exception e = (Exception)_t;
                    ClassLoader _classLoader = magicClass.getClassLoader();
                    Class<?> _forName_1 = Class.forName(name, true, _classLoader);
                    cls = _forName_1;
                  } else {
                    throw Exceptions.sneakyThrow(_t);
                  }
                }
                Object _newInstance = cls.newInstance();
                if ((!(_newInstance instanceof Processor))) {
                  String _name = Processor.class.getName();
                  String _plus = ((((name + " from ") + fileURI) + " is not a ") + _name);
                  throw new ClassCastException(_plus);
                }
              } catch (final Throwable _t_1) {
                if (_t_1 instanceof Throwable) {
                  final Throwable t = (Throwable)_t_1;
                  MagicAnnotationProcessor.processorUtil.error(magicClass, "findProcessorNames", null, 
                    ((("Could not instantiate processor for \'" + name) + "\' from ") + fileURI), t);
                  list.remove(name);
                } else {
                  throw Exceptions.sneakyThrow(_t_1);
                }
              }
            }
            MagicAnnotationProcessor.processorUtil.warn(magicClass, "findProcessorNames", null, 
              ((("Processors found in file \'" + fileURI) + "\': ") + list));
          }
        } catch (final Throwable _t_2) {
          if (_t_2 instanceof Throwable) {
            final Throwable t_1 = (Throwable)_t_2;
            MagicAnnotationProcessor.processorUtil.error(magicClass, "findProcessorNames", null, 
              (("Could not read/process \'" + fileURI) + "\'"), t_1);
          } else {
            throw Exceptions.sneakyThrow(_t_2);
          }
        }
      } else {
        MagicAnnotationProcessor.processorUtil.error(magicClass, "findProcessorNames", null, 
          (("Could not find file \'" + fileURI) + "\'"));
      }
      int _size_1 = list.size();
      String[] _newArrayOfSize_1 = new String[_size_1];
      _xblockexpression = list.<String>toArray(_newArrayOfSize_1);
    }
    return _xblockexpression;
  }
}
