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

package com.blockwithme.util.proto.base40;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.blockwithme.util.shared.Statics;

/**
 * Helper class allows creating pseudo-enumerations out of Enum40 instances.
 *
 * The purpose is to create a long-term-serialization friendly class, which
 * which allows "extension" of the value set, and has most of the benefits of
 * real Enums.
 *
 * Usage: create an class, and create one Enum40 field per enumeration
 * value/tag/label that you want to have, like this:
 *
 * You basically have two options; you can either support handlers, or not.
 * If you don't want to support handlers, you could do it like this:
 *
 * <code>
 * public class TestEnum40 extends Enum40<TestEnum40> {
 *     public static final TestEnum40 two = new TestEnum40(2);
 *     public static final TestEnum40 one = new TestEnum40(1);
 *
 *      // Normal constructor
 *      private TestEnum40(int n) {
 *          super(TestEnum40.class);
 *          // ...
 *      }
 *
 *      protected void postInit(TestEnum40[] allSet) {
 *          // ...
 *          super.postInit(allSet);
 *      }
 *
 *      // ...
 * }
 * </code>
 *
 * Otherwise, if you want to support handlers, you need to write more code:
 *
 * <code>
 * // Enum40ObjectHandler for TestEnum40
 * public interface TestEnum40ObjectHandler<INPUT, OUTPUT>
 * extends Enum40ObjectHandler<TestEnum40,INPUT,OUTPUT> {
 *     OUTPUT one(final INPUT input);
 *     OUTPUT two(final INPUT input);
 * }
 *
 * // Enum40LongHandler for TestEnum40
 * public interface TestEnum40LongHandler
 * extends Enum40LongHandler<TestEnum40> {
 *     long one(final long input);
 *     long two(final long input);
 * }
 *
 * public class TestEnum40 extends Enum40<TestEnum40> {
 *     public static final TestEnum40 two = new TestEnum40(2) {
 *         public <INPUT, OUTPUT> OUTPUT handle(final Enum40ObjectHandler
 *             <E, INPUT, OUTPUT> handler, final INPUT input) {
 *             return ((TestEnum40ObjectHandler<INPUT, OUTPUT>) handler).two(input);
 *         }
 *         public long handle(final Enum40LongHandler<E> handler, final long input) {
 *             return ((TestEnum40LongHandler) handler).two(input);
 *         }
 *     };
 *     public static final TestEnum40 one = new TestEnum40(1) {
 *         public <INPUT, OUTPUT> OUTPUT handle(final Enum40ObjectHandler
 *             <E, INPUT, OUTPUT> handler, final INPUT input) {
 *             return ((TestEnum40ObjectHandler<INPUT, OUTPUT>) handler).one(input);
 *         }
 *         public long handle(final Enum40LongHandler<E> handler, final long input) {
 *             return ((TestEnum40LongHandler) handler).one(input);
 *         }
 *     };
 *
 *      // Normal constructor
 *      private TestEnum40(int n) {
 *          super(TestEnum40.class);
 *          // ...
 *      }
 *
 *      // "Generic" constructor
 *      private TestEnum40(final long base40) {
 *          super(TestEnum40.class, base40);
 *      }
 *
 *      protected void postInit(TestEnum40[] allSet) {
 *          // ...
 *          super.postInit(allSet);
 *      }
 *
 *      protected TestEnum40 newInstance(final long base40) {
 *          return new TestEnum40(base40);
 *      }
 *
 *      // ...
 * }
 * </code>
 *
 * What the handlers support gives you, is that it is a safe way to simulate
 * switch/case statements. In fact, it is better because it is extensible.
 * This allows using derived Enum40 easily. Since the value of the ordinal is
 * allocated dynamically, it is not safe to use it in traditional switch/case
 * statements. The only exception being switch/case statements defined
 * *within* the Enum40 type itself. But those are hard to make extensible.
 * If the enum is small, just using if-then-else might be enough.
 *
 * The toString() method returns the variable-length *capitalized* String
 * representation, so it it recommended you use the same for the field names.
 *
 * Please note that it is only possible to validate the consistency of the
 * returned values after returning them, so if there is any error, if will be
 * detected on the next returned value.
 *
 * If de-serialization crates a new Enum40 that is not registered, it is marked
 * as invalid, and has a (potentially) non-unique ordinal (but different from
 * all the valid enums).
 *
 * TODO: Test
 *
 * @author monster
 */
public abstract class Enum40<E extends Enum40<E>> extends AbstractBase40<E> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3797818068469930636L;

    /**
     * Maximum number of values per Enum40. We reserve one ID for invalid
     * values per type.
     */
    private static final int MAX_VALUES = 63;

    /** Maximum number of children per Enum40. */
    private static final int MAX_CHILDREN = ((1 + Character.MAX_VALUE) / (1 + MAX_VALUES)) - 1;

    /** Keeps track of which enum value will be returned next. */
    private static class Data<E extends Enum40<E>> {
        /** The enum class. */
        private final Class<E> enumClass;
        /** The parent Data object, if any. */
        private final Data<?> parent;
        /** The first id. It depends on the presence of a parent. */
        private final int firstID;
        /** The id of the next value. */
        private int count;
        /** All the values. */
        private final List<E> values = new ArrayList<>();
        /** All the fields. */
        private final List<Field> fields = new ArrayList<>();
        /** Maps names to values. */
        private final Map<String, E> nameToValue = new HashMap<>();
        /** Maps base-40 value to values. */
        private final Map<Long, E> base40ToValue = new HashMap<>();
        /** All types extending this type, which also provide Enum40 constants. */
        private final List<Data<E>> children = new ArrayList<>();

        /** Constructors computes the enum values. */
        public Data(final Class<E> theEnumClass, final Data<?> theParent,
                final int theFirstID) {
            if (theEnumClass == null) {
                throw new IllegalStateException("theEnumClass is null");
            }
            enumClass = theEnumClass;
            parent = theParent;
            firstID = theFirstID;
            final Logger log = Logger.getLogger(Enum40.class.getName());
            log.info("Registering new Enum40 enum: " + enumClass);
            for (final Field f : enumClass.getFields()) {
                final int mod = f.getModifiers();
                if (Modifier.isPublic(mod) && Modifier.isStatic(mod)
                        && Modifier.isFinal(mod)
                        && (theEnumClass == f.getType())) {
                    fields.add(f);
                }
            }
            if (fields.isEmpty()) {
                throw new IllegalStateException("No values found in "
                        + theEnumClass);
            }
            if (fields.size() >= MAX_VALUES) {
                throw new IllegalStateException("Too many values found in "
                        + theEnumClass + " : " + fields.size());
            }
            log.info("Enum40 fields for " + enumClass + " " + fields);
        }

        public E[] toArray() {
            @SuppressWarnings("unchecked")
            final E[] result = (E[]) Array
                    .newInstance(enumClass, values.size());
            return values.toArray(result);
        }
    }

    /** Key for already computed Data for classes. */
    private static final String DATA = Enum40.class.getName() + ".data";

    /* The ordinal + 1 of this Enum40. The +1 allows detection of invalids. */
    private transient final char ordinal;

    /** The declaring Class. */
    private transient final Class<E> declaringClass;

    /** The object that will be returned in writeReplace(). */
    private transient E writeReplace;

    /** Already computed Data for classes. */
    private static final Map<Class<?>, Data<?>> getData() {
        @SuppressWarnings("unchecked")
        Map<Class<?>, Data<?>> result = (Map<Class<?>, Data<?>>) Statics
                .get(DATA);
        if (result == null) {
            result = Statics.replace(DATA, null,
                    new HashMap<Class<?>, Data<?>>());
        }
        return result;
    }

    /** Checks an already returned field. */
    private static void checkField(final Data<?> data, final int fieldNumber) {
        final Class<?> enumClass = data.enumClass;
        final Field previous = data.fields.get(fieldNumber);
        try {
            final Object value = previous.get(null);
            if (value == null) {
                throw new IllegalStateException("Previous field value for "
                        + enumClass + " is null");
            }
            if (!(value instanceof Enum40)) {
                throw new IllegalStateException("Previous field value for "
                        + enumClass + " is not Base40: " + value.getClass());
            }
            if (!previous.getName().equalsIgnoreCase(value.toString())) {
                throw new IllegalStateException("Previous field value for "
                        + enumClass + " is not " + previous.getName());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(
                    "Failed to get previous field value for " + enumClass, e);
        }
    }

    /** Returns the Data for the given "enumeration" class. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <E extends Enum40<E>> Data<E> dataFor(
            final Class<E> enumClass) {
        final Map<Class<?>, Data<?>> map = getData();
        final Data<?> toCheck = map.get(Object.class);
        if (toCheck != null) {
            checkField(toCheck, toCheck.count - 1);
            final Enum40<?>[] all = toCheck.toArray();
            for (final Enum40 e : all) {
                e.postInit(all);
            }
            map.remove(Object.class);
        }
        Data<E> data = (Data<E>) map.get(enumClass);
        if (data == null) {
            // Check parents ...
            final Class<?> parent = enumClass.getSuperclass();
            int firstID = 0;
            Data<?> parentData = null;
            if (parent != Enum40.class) {
                // We assume that any "Enum40" carrying parent of enumClass
                // will have been initialized by now.
                parentData = map.get(parent);
                if (parentData == null) {
                    throw new IllegalStateException(parent
                            + " has not yet been initialized!");
                } else {
                    // We want to find the deepest parent ...
                    while (parentData.parent != null) {
                        parentData = parentData.parent;
                    }
                    if (parentData.children.size() >= MAX_CHILDREN) {
                        throw new IllegalStateException(parent
                                + " cannot have more then " + MAX_CHILDREN
                                + " children!");
                    }
                    // This ensures that all children have an independent ID space.
                    firstID = (parentData.children.size() + 1)
                            * (MAX_VALUES + 1);
                    data = new Data<E>(enumClass, parentData, firstID);
                    parentData.children.add((Data) data);
                }
            } else {
                data = new Data<E>(enumClass, parentData, firstID);
            }
            map.put(enumClass, data);
        }
        return data;
    }

    /** Small helper class, to allow returning multiple values. */
    private static final class NameAndOrdinal<E extends Enum40<E>> {
        public Data<E> data;
        public String name;
        public int ordinal;
    }

    /**
     * Returns the next value for the given "enumeration" class.
     */
    private static <E extends Enum40<E>> NameAndOrdinal<E> nextFor(
            final Class<E> enumClass, final long base40ForGeneric) {
        final Map<Class<?>, Data<?>> map = getData();
        synchronized (map) {
            final Data<E> data = dataFor(enumClass);
            final NameAndOrdinal<E> result = new NameAndOrdinal<E>();
            result.data = data;
            if (base40ForGeneric == 0) {
                // Normal "singleton" instance
                final int next = data.count++;
                if (next >= data.fields.size()) {
                    throw new IllegalStateException(
                            "Too many values requested for " + enumClass);
                }
                if (next > 0) {
                    checkField(data, next - 1);
                }
                if (data.count == data.fields.size()) {
                    // Last one!
                    map.put(Object.class, data);
                }
                final Field field = data.fields.get(next);
                result.name = field.getName();
                result.ordinal = next;
            } else {
                // Generic instance
                result.name = getDefaultCharacterSet().toString(
                        base40ForGeneric, false, true);
                result.ordinal = 0;
            }
            return result;
        }
    }

    /** Returns the all values for the given "enumeration" class. */
    public static <E extends Enum40<E>> E[] values(final Class<E> enumClass) {
        synchronized (getData()) {
            return dataFor(enumClass).toArray();
        }
    }

    /** Returns the value for the given name for the "enumeration" class. */
    public static <E extends Enum40<E>> E valueOf(final Class<E> enumClass,
            final String name) {
        if (name == null) {
            throw new IllegalStateException("Name is null");
        }
        synchronized (getData()) {
            final E result = dataFor(enumClass).nameToValue.get(name);
            if (result == null) {
                throw new IllegalStateException("Enum40 " + name
                        + " not found in " + enumClass);
            }
            return result;
        }
    }

    /** Returns the value for the given base-40 ID for the "enumeration" class. */
    public static <E extends Enum40<E>> E valueOf(final Class<E> enumClass,
            final long base40) {
        synchronized (getData()) {
            final E result = dataFor(enumClass).base40ToValue.get(base40);
            if (result == null) {
                throw new IllegalStateException("Enum40 "
                        + getDefaultCharacterSet().toString(base40, false,
                                false) + " not found in " + enumClass);
            }
            return result;
        }
    }

    ///////////////////////
    // Instance methods. //
    ///////////////////////

    /** Normal constructor; needs the Enum40 type. */
    protected Enum40(final Class<? extends E> enumClass) {
        this(getDefaultCharacterSet(), enumClass);
    }

    /**
     * Special constructor for "generic instances"; needs the Enum40 type,
     * and the base40 ID.
     */
    protected Enum40(final Class<? extends E> enumClass,
            final long base40ForGeneric) {
        this(getDefaultCharacterSet(), enumClass, base40ForGeneric);
    }

    /** Normal constructor; needs the Enum40 type. */
    @SuppressWarnings("unchecked")
    protected Enum40(final CharacterSet theCharacterSet,
            final Class<? extends E> enumClass) {
        this(theCharacterSet, nextFor((Class<E>) enumClass, 0L));
    }

    /**
     * Special constructor for "generic instances"; needs the Enum40 type,
     * and the base40 ID.
     */
    @SuppressWarnings("unchecked")
    protected Enum40(final CharacterSet theCharacterSet,
            final Class<? extends E> enumClass, final long base40ForGeneric) {
        this(theCharacterSet, nextFor((Class<E>) enumClass, base40ForGeneric));
    }

    /** Constructor. Only accepts valid names. */
    private Enum40(final CharacterSet theCharacterSet,
            final NameAndOrdinal<E> nameAndOrdinal) {
        this(theCharacterSet, nameAndOrdinal.data, nameAndOrdinal.name,
                nameAndOrdinal.ordinal);
    }

    /** Constructor. Only accepts valid names. */
    @SuppressWarnings("unchecked")
    private Enum40(final CharacterSet theCharacterSet, final Data<E> data,
            final String name, final int theOrdinal) {
        super(theCharacterSet, name);
        final int ord = data.firstID + theOrdinal + 1;
        if ((ord < 0) || (ord > Character.MAX_VALUE)) {
            throw new IllegalStateException("Bad ordinal: " + ord);
        }
        ordinal = (char) ord;
        declaringClass = data.enumClass;
        writeReplace = (E) this;
        synchronized (getData()) {
            final E e = (E) this;
            data.base40ToValue.put(asLong(), e);
            data.nameToValue.put(toString(), e);
            // In case there is a "case difference" ...
            data.nameToValue.put(data.fields.get(theOrdinal).getName(), e);
            data.values.add(e);
        }
    }

    /**
     * Enum40 instances are normally only equal to themselves.
     */
    @Override
    public boolean equals(final Object obj) {
        return (this == obj);
    }

    /** Resolves the Enum40. */
    @SuppressWarnings("unchecked")
    private Object readResolve() throws ObjectStreamException {
        synchronized (getData()) {
            final Data<E> data = dataFor(getClass());
            E result = data.base40ToValue.get(asLong());
            if (result == null) {
                final Logger log = Logger.getLogger(Enum40.class.getName());
                log.warning("Enum40 "
                        + getCharacterSet().toString(asLong(), false, false)
                        + " not found in " + getClass());
                result = (E) this;
                // Through de-serialization, the ordinal becomes -1.
                data.base40ToValue.put(asLong(), result);
                data.nameToValue.put(toString(), result);
                data.values.add(result);
            }
            return result;
        }
    }

    /** Replaces the Enum40, before serializing. */
    private Object writeReplace() throws ObjectStreamException {
        return writeReplace;
    }

    /**
     * Returns the variable-length capitalized String representation.
     */
    @Override
    public String toString() {
        return capitalizedName();
    }

    /**
     * Returns the ordinal of this Enum40. 0 for an invalid Enum40.
     * To allow for extension, the whole range of the char must be supported.
     */
    public final char ordinal() {
        return ordinal;
    }

    /** Is this Enum40 valid? An invalid Enum40 can only come from de-serialization. */
    public final boolean valid() {
        return (ordinal > 0);
    }

    /**
     * Returns the Class object corresponding to this enum constant's
     * enum type.  Two enum constants e1 and  e2 are of the
     * same enum type if and only if
     *   e1.getDeclaringClass() == e2.getDeclaringClass().
     * (The value returned by this method may differ from the one returned
     * by the {@link Object#getClass} method for enum constants with
     * constant-specific class bodies.)
     *
     * @return the Class object corresponding to this enum constant's
     *     enum type
     */
    public final Class<E> getDeclaringClass() {
        return declaringClass;
    }

    /**
     * The postInit() method will be called after the Enum40 values were fully
     * initialized. If it is overwritten, the base-class method must be called.
     */
    protected void postInit(final E[] allSet) {
        if (getClass() != declaringClass) {
            writeReplace = newInstance(asLong());
        }
    }

    /**
     * Calls the appropriate handler method on the handler.
     *
     * Should be overwritten by every enum40 value instance!
     */
    @SuppressWarnings("unchecked")
    public long handle(final Enum40LongHandler<E> handler, final long input) {
        return handler.unknown((E) this, input);
    }

    /**
     * Calls the appropriate handler method on the handler.
     *
     * Should be overwritten by every enum40 value instance!
     */
    @SuppressWarnings("unchecked")
    public <INPUT, OUTPUT> OUTPUT handle(
            final Enum40ObjectHandler<E, INPUT, OUTPUT> handler,
            final INPUT input) {
        return handler.unknown((E) this, input);
    }

    /**
     * Creates and returns a generic instance of the Enum40.
     * This method is required, for Enum40 types, which value-specific
     * implementation of certain methods, like "handle()".
     */
    protected E newInstance(final long base40) {
        throw new IllegalStateException(getClass()
                + " must implement newInstance(long)!");
    }

    /**
     * Compares two instances of Enum40, allowing "sub-typing".
     * This method should be overridden, to specify that an Enum40 value
     * represent a "sub-type" (specialization) of another Enum40 value.
     *
     * It can be used, where the "parent" enum definitions define general
     * categories, like car, planes, ... and a sub-class defines some more
     * specific categories, like BMW, which are sub-categories of parent categories.
     *
     * If you do not match, you must delegate to the superclass
     * method, to allow for transitivity. The default implementation
     * just delegates to equals.
     */
    public boolean isA(final Enum40<?> other) {
        return equals(other);
    }

    /** To simplify things, we need to stick to one single character set. */
    public static CharacterSet getDefaultCharacterSet() {
        return CharacterSet.newLowerIDCharacterSet();
    }
}
