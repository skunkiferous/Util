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

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions;

/**
 * Implements a thread-safe "cache" which works across multiple class-loaders.
 * 
 * WARNING: It is strongly recommended to only put objects coming from the JRE in it,
 * *to avoid memory leaks*. If you need to use this cache, because you need it
 * within code that runs into temporary class loaders, then putting objects
 * in the cache that come from that temporary class loader will lock the class
 * loader into memory, therefore causing a memory leak.
 * 
 * @author monster
 */
@SuppressWarnings("all")
public class AntiClassLoaderCache {
  /**
   * System Property key that no one else should be using.
   */
  private final static String UNIQUE_KEY = "kljfgslkgs$dfgökfjughd%fiugsr";
  
  private static ConcurrentMap<String, Object> CACHE;
  
  /**
   * Cannot be instantiated.
   */
  private AntiClassLoaderCache() {
  }
  
  /**
   * Returns a thread-safe cache, that survives between multiple class-loaders.
   */
  public static ConcurrentMap<String, Object> getCache() {
    try {
      ConcurrentMap<String, Object> _xblockexpression = null;
      {
        boolean _tripleEquals = (AntiClassLoaderCache.CACHE == null);
        if (_tripleEquals) {
          final ReflectExtensions helper = new ReflectExtensions();
          Properties parent = System.getProperties();
          Properties child = null;
          boolean _dowhile = false;
          do {
            {
              child = parent;
              Properties _get = helper.<Properties>get(child, "defaults");
              parent = _get;
              boolean _tripleEquals_1 = (child == parent);
              if (_tripleEquals_1) {
                throw new IllegalStateException("child === parent");
              }
            }
            boolean _tripleNotEquals = (parent != null);
            _dowhile = _tripleNotEquals;
          } while(_dowhile);
          Object _get = child.get(AntiClassLoaderCache.UNIQUE_KEY);
          AntiClassLoaderCache.CACHE = ((ConcurrentMap<String, Object>) _get);
          boolean _tripleEquals_1 = (AntiClassLoaderCache.CACHE == null);
          if (_tripleEquals_1) {
            ConcurrentHashMap<String, Object> _concurrentHashMap = new ConcurrentHashMap<String, Object>();
            AntiClassLoaderCache.CACHE = _concurrentHashMap;
            child.put(AntiClassLoaderCache.UNIQUE_KEY, AntiClassLoaderCache.CACHE);
          }
        }
        _xblockexpression = AntiClassLoaderCache.CACHE;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Drops all mappings starting with the given prefix
   */
  public static void clear(final String prefix) {
    final ConcurrentMap<String, Object> cache = AntiClassLoaderCache.getCache();
    Set<String> _keySet = cache.keySet();
    Object[] _array = _keySet.toArray();
    for (final Object key : _array) {
      String _string = key.toString();
      boolean _startsWith = _string.startsWith(prefix);
      if (_startsWith) {
        cache.remove(key);
      }
    }
  }
  
  /**
   * Returns all mappings starting with the given prefix
   */
  public static Iterable<String> query(final String prefix) {
    ConcurrentMap<String, Object> _cache = AntiClassLoaderCache.getCache();
    Set<String> _keySet = _cache.keySet();
    final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
      public Boolean apply(final String it) {
        String _string = it.toString();
        return Boolean.valueOf(_string.startsWith(prefix));
      }
    };
    return IterableExtensions.<String>filter(_keySet, _function);
  }
  
  /**
   * Puts something in the cache.
   */
  public static void put(final String prefix, final String key, final Object value) {
    ConcurrentMap<String, Object> _cache = AntiClassLoaderCache.getCache();
    _cache.put((prefix + key), value);
  }
  
  /**
   * Gets something from the cache.
   */
  public static Object get(final String prefix, final String key) {
    ConcurrentMap<String, Object> _cache = AntiClassLoaderCache.getCache();
    return _cache.get((prefix + key));
  }
}
