/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.lang;

import com.google.gwt.core.client.impl.Impl;

import java.io.PrintStream;
import java.util.Properties;

/**
 * General-purpose low-level utility methods. GWT only supports a limited subset
 * of these methods due to browser limitations. Only the documented methods are
 * available.
 */
public final class System {

    /**
     * Does nothing in web mode. To get output in web mode, subclass PrintStream
     * and call {@link #setErr(PrintStream)}.
     */
    public static final PrintStream err = new PrintStream(null);

    /**
     * Does nothing in web mode. To get output in web mode, subclass
     * {@link PrintStream} and call {@link #setOut(PrintStream)}.
     */
    public static final PrintStream out = new PrintStream(null);

    /** The system properties :D */
    private static Properties systemProperties;

    static {
        ensureProperties();
    }

    public static void arraycopy(Object src, int srcOfs, Object dest,
            int destOfs, int len) {
        if (src == null || dest == null) {
            throw new NullPointerException();
        }

        Class<?> srcType = src.getClass();
        Class<?> destType = dest.getClass();
        if (!srcType.isArray() || !destType.isArray()) {
            throw new ArrayStoreException("Must be array types");
        }

        Class<?> srcComp = srcType.getComponentType();
        Class<?> destComp = destType.getComponentType();
        if (srcComp.modifiers != destComp.modifiers
                || (srcComp.isPrimitive() && !srcComp.equals(destComp))) {
            throw new ArrayStoreException("Array types must match");
        }
        int srclen = getArrayLength(src);
        int destlen = getArrayLength(dest);
        if (srcOfs < 0 || destOfs < 0 || len < 0 || srcOfs + len > srclen
                || destOfs + len > destlen) {
            throw new IndexOutOfBoundsException();
        }
        /*
         * If the arrays are not references or if they are exactly the same type, we
         * can copy them in native code for speed. Otherwise, we have to copy them
         * in Java so we get appropriate errors.
         */
        if ((!srcComp.isPrimitive() || srcComp.isArray())
                && !srcType.equals(destType)) {
            // copy in Java to make sure we get ArrayStoreExceptions if the values
            // aren't compatible
            Object[] srcArray = (Object[]) src;
            Object[] destArray = (Object[]) dest;
            if (src == dest && srcOfs < destOfs) {
                // TODO(jat): how does backward copies handle failures in the middle?
                // copy backwards to avoid destructive copies
                srcOfs += len;
                for (int destEnd = destOfs + len; destEnd-- > destOfs;) {
                    destArray[destEnd] = srcArray[--srcOfs];
                }
            } else {
                for (int destEnd = destOfs + len; destOfs < destEnd;) {
                    destArray[destOfs++] = srcArray[srcOfs++];
                }
            }
        } else {
            nativeArraycopy(src, srcOfs, dest, destOfs, len);
        }
    }

    public static long currentTimeMillis() {
        return (long) currentTimeMillis0();
    };

    /**
     * Has no effect; just here for source compatibility.
     *
     * @skip
     */
    public static void gc() {
    };

    public static int identityHashCode(Object o) {
        return (o == null) ? 0 : (!(o instanceof String)) ? Impl.getHashCode(o)
                : String.HashCache.getHashCode((String) o);
    }

    public static native void setErr(PrintStream err) /*-{
		@java.lang.System::err = err;
    }-*/;

    public static native void setOut(PrintStream out) /*-{
		@java.lang.System::out = out;
    }-*/;

    private static native double currentTimeMillis0() /*-{
		return (new Date()).getTime();
    }-*/;

    /**
     * Returns the length of an array via Javascript.
     */
    private static native int getArrayLength(Object array) /*-{
		return array.length;
    }-*/;

    /**
     * Copy an array using native Javascript. The destination array must be a real
     * Java array (ie, already has the GWT type info on it). No error checking is
     * performed -- the caller is expected to have verified everything first.
     *
     * @param src source array for copy
     * @param srcOfs offset into source array
     * @param dest destination array for copy
     * @param destOfs offset into destination array
     * @param len number of elements to copy
     */
    private static native void nativeArraycopy(Object src, int srcOfs,
            Object dest, int destOfs, int len) /*-{
		// TODO(jgw): using Function.apply() blows up for large arrays (around 8k items at least).
		if (src == dest && srcOfs < destOfs) {
			srcOfs += len;
			for (var destEnd = destOfs + len; destEnd-- > destOfs;) {
				dest[destEnd] = src[--srcOfs];
			}
		} else {
			for (var destEnd = destOfs + len; destOfs < destEnd;) {
				dest[destOfs++] = src[srcOfs++];
			}
		}

		//    Array.prototype.splice.apply(dest, [destOfs, len].concat(src.slice(srcOfs, srcOfs + len)));
    }-*/;

    // TODO(jgw)
    public static native void exit(int code) /*-{ }-*/;

    /**
     * If systemProperties is unset, then create a new one based on the values
     * provided by the virtual machine.
     */
    private static void ensureProperties() {
        systemProperties = new Properties();

        systemProperties.put("os.encoding", "UTF-8");
        systemProperties.put("file.encoding", "UTF-8");
        systemProperties.put("console.encoding", "UTF-8");

        systemProperties.put("java.version", "1.7 subset");
        systemProperties.put("java.specification.version", "1.7");
        systemProperties.put("java.specification.vendor", "Google");
        systemProperties.put("java.specification.name", "GWT");
        systemProperties.put("java.vendor", "Google");
        systemProperties.put("os.name", "GWT");
        systemProperties.put("os.arch", "GWT");

        systemProperties.put("line.separator", "\n");

        systemProperties.put("user.home", "/");
        systemProperties.put("user.dir", "/");
        systemProperties.put("file.separator", "/");
        systemProperties.put("path.separator", ":");
    }

    /**
     * Returns the system properties. Note that this is not a copy, so that
     * changes made to the returned Properties object will be reflected in
     * subsequent calls to getProperty and getProperties.
     *
     * @return the system properties.
     * @throws SecurityException
     *             if a {@link SecurityManager} is installed and its {@code
     *             checkPropertiesAccess()} method does not allow the operation.
     */
    public static Properties getProperties() {
        return systemProperties;
    }

    /**
     * Returns the value of a particular system property or {@code null} if no
     * such property exists.
     * <p>
     * The properties currently provided by the virtual machine are:
     *
     * <pre>
     *        java.vendor.url
     *        java.class.path
     *        user.home
     *        java.class.version
     *        os.version
     *        java.vendor
     *        user.dir
     *        user.timezone
     *        path.separator
     *        os.name
     *        os.arch
     *        line.separator
     *        file.separator
     *        user.name
     *        java.version
     *        java.home
     * </pre>
     *
     * @param prop
     *            the name of the system property to look up.
     * @return the value of the specified system property or {@code null} if the
     *         property doesn't exist.
     * @throws SecurityException
     *             if a {@link SecurityManager} is installed and its {@code
     *             checkPropertyAccess()} method does not allow the operation.
     */
    public static String getProperty(String prop) {
        return getProperty(prop, null);
    }

    /**
     * Returns the value of a particular system property. The {@code
     * defaultValue} will be returned if no such property has been found.
     *
     * @param prop
     *            the name of the system property to look up.
     * @param defaultValue
     *            the return value if the system property with the given name
     *            does not exist.
     * @return the value of the specified system property or the {@code
     *         defaultValue} if the property does not exist.
     * @throws SecurityException
     *             if a {@link SecurityManager} is installed and its {@code
     *             checkPropertyAccess()} method does not allow the operation.
     */
    public static String getProperty(String prop, String defaultValue) {
        if (prop.length() == 0) {
            throw new IllegalArgumentException();
        }
        return systemProperties.getProperty(prop, defaultValue);
    }

    /**
     * Sets the value of a particular system property.
     *
     * @param prop
     *            the name of the system property to be changed.
     * @param value
     *            the value to associate with the given property {@code prop}.
     * @return the old value of the property or {@code null} if the property
     *         didn't exist.
     * @throws SecurityException
     *             if a security manager exists and write access to the
     *             specified property is not allowed.
     */
    public static String setProperty(String prop, String value) {
        if (prop.length() == 0) {
            throw new IllegalArgumentException();
        }
        return (String) systemProperties.setProperty(prop, value);
    }

    /**
     * Removes a specific system property.
     *
     * @param key
     *            the name of the system property to be removed.
     * @return the property value or {@code null} if the property didn't exist.
     * @throws NullPointerException
     *             if the argument {@code key} is {@code null}.
     * @throws IllegalArgumentException
     *             if the argument {@code key} is empty.
     * @throws SecurityException
     *             if a security manager exists and write access to the
     *             specified property is not allowed.
     * @since 1.5
     */
    public static String clearProperty(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException();
        }

        return (String) systemProperties.remove(key);
    }

    /**
     * Sets all system properties. Note that the object which is passed in
     * not copied, so that subsequent changes made to the object will be
     * reflected in calls to getProperty and getProperties.
     *
     * @param p
     *            the new system property.
     * @throws SecurityException
     *             if a {@link SecurityManager} is installed and its {@code
     *             checkPropertiesAccess()} method does not allow the operation.
     */
    public static void setProperties(Properties p) {
        if (p == null) {
            ensureProperties();
        } else {
            systemProperties = p;
        }
    }

    /**
     * Returns the system-dependent line separator string; {@code "\n"}.
     */
    public static String lineSeparator() {
        return "\n";
    }
}
