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
package com.blockwithme.util.xtend;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Tiny wrapper over Service-Loader.
 * 
 * Should allow future replacement when/if using OSGi, or whatever.
 * 
 * This class does NOT specify if instances are cached or not.
 * In the case of the true ServiceLoader, it is to assume that
 * new instances will be created every time.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class Loader<S extends Object> implements Iterable<S> {
  /**
   * The desired type
   */
  private final Class<S> service;
  
  /**
   * The source of instances.
   */
  private final Iterable<S> instances;
  
  private static <S extends Object> Iterable<S> doLoad(final Class<S> service, final ClassLoader loader) {
    ServiceLoader<S> _xifexpression = null;
    boolean _tripleEquals = (loader == null);
    if (_tripleEquals) {
      _xifexpression = ServiceLoader.<S>load(service);
    } else {
      _xifexpression = ServiceLoader.<S>load(service, loader);
    }
    return _xifexpression;
  }
  
  /**
   * Returns a new Loader using the thread class loader
   */
  public static <S extends Object> Loader<S> load(final Class<S> service) {
    Iterable<S> _doLoad = Loader.<S>doLoad(service, null);
    return new Loader<S>(service, _doLoad);
  }
  
  /**
   * Returns a new Loader using the specified class loader
   */
  public static <S extends Object> Loader<S> load(final Class<S> service, final ClassLoader loader) {
    Iterable<S> _doLoad = Loader.<S>doLoad(service, loader);
    return new Loader<S>(service, _doLoad);
  }
  
  /**
   * Constructor
   */
  private Loader(final Class<S> service, final Iterable<S> instances) {
    this.service = service;
    this.instances = instances;
  }
  
  public Iterator<S> iterator() {
    return this.instances.iterator();
  }
  
  public String toString() {
    Class<? extends Loader> _class = this.getClass();
    String _name = _class.getName();
    String _plus = (_name + "[");
    String _name_1 = this.service.getName();
    String _plus_1 = (_plus + _name_1);
    return (_plus_1 + "]");
  }
}
