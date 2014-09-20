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

import java.io.Serializable;

import com.blockwithme.util.shared.AsLong;

/**
 * <code>Path</code> represents a 64bit non-negative base-40 "path".
 *
 * For a description of the base-40 encoding, see <code>Base40</code>.
 *
 * TODO: Test!
 */
public final class Path implements Serializable, Comparable<Path>, AsLong {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** The path separator character */
    public static final char SEP = '/';

    /** The parent, if any */
    private final Path parent;

    /** The base-40 value */
    private final long base40;

    /** The character set. */
    private final CharacterSet characterSet;

    /** The textual form. */
    private transient String name;

    /** The hashcode. */
    private transient int hashcode;

    /** The depth. */
    private transient int depth;

    /** Creates a path from it's string representation. */
    public static Path fromString(final CharacterSet characterSet,
            final String path) {
        if ((path == null) || path.isEmpty()) {
            return null;
        }
        final String[] paths = path.split(String.valueOf(SEP));
        Path result = new Path(characterSet, paths[0]);
        for (int i = 1; i < paths.length; i++) {
            result = new Path(characterSet, result, paths[i]);
        }
        return result;
    }

    /** Constructor */
    public Path(final CharacterSet characterSet, final long value) {
        this(characterSet, null, value);
    }

    /** Constructor */
    public Path(final CharacterSet characterSet, final String name) {
        this(characterSet, null, name);
    }

    /** Constructor */
    public Path(final CharacterSet characterSet, final Path parent,
            final String name) {
        this(characterSet, parent, characterSet.toLong(name));
    }

    /** Constructor */
    public Path(final CharacterSet characterSet, final Path parent,
            final long value) {
        this.characterSet = characterSet;
        this.parent = parent;
        this.base40 = value;
    }

    /* (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object) */
    @Override
    public int compareTo(final Path o) {
        if (o == null) {
            return 1;
        }
        return compareTo(depth(), o.depth(), o);
    }

    /** Compares to another path. */
    private int compareTo(final int myDepth, final int otherDepth,
            final Path other) {
        int result;
        if (other == this) {
            result = 0;
        } else if ((myDepth == 1) && (otherDepth == 1)) {
            result = name().compareTo(other.name());
        } else {
            if (myDepth > otherDepth) {
                result = parent.compareTo(myDepth - 1, otherDepth, other);
                if (result == 0) {
                    // I'm longer, so I'm bigger
                    result = 1;
                }
            } else if (myDepth < otherDepth) {
                result = -other.parent.compareTo(otherDepth - 1, myDepth, this);
                if (result == 0) {
                    // I'm shorter, so I'm smaller
                    result = -1;
                }
            } else {
                // Same depth, and > 1
                result = parent.compareTo(myDepth - 1, otherDepth - 1,
                        other.parent);
                if (result == 0) {
                    // Our parents are equal!
                    result = name().compareTo(other.name());
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Path other = (Path) obj;
        return (compareTo(other) == 0);
    }

    @Override
    public int hashCode() {
        if (hashcode == 0) {
            hashcode = (parent == null) ? 31 : parent.hashCode() * 31;
            hashcode += (int) (base40 ^ (base40 >>> 32));
            if (hashcode == 0) {
                hashcode = 1;
            }
        }
        return hashcode;
    }

    /** Returns the "depth of the path. */
    public int depth() {
        if (depth == 0) {
            Path p = this;
            while (p != null) {
                depth++;
                p = p.parent;
            }
        }
        return depth;
    }

    /** Returns the path as as array of long. */
    public long[] toLongArray() {
        final int depth = depth();
        final long[] result = new long[depth];
        Path p = this;
        for (int i = depth - 1; i >= 0; i--) {
            result[i] = p.base40;
            p = p.parent;
        }
        return result;
    }

    /**
     * Returns the fixed-size String representation.
     * It is not cached.
     */
    public String toFixedString() {
        final StringBuilder buf = new StringBuilder();
        toFixedString(buf);
        return buf.toString();
    }

    /** Builds the string recursively in buf. */
    private void toFixedString(final StringBuilder buf) {
        if (parent != null) {
            parent.toFixedString(buf);
            buf.append(SEP);
        }
        buf.append(characterSet.toString(base40, true, false));
    }

    /**
     * Returns the String representation.
     */
    public String name() {
        if (name == null) {
            name(new StringBuilder());
        }
        return name;
    }

    /** Returns the base-40 value */
    @Override
    public long asLong() {
        return base40;
    }

    /** Returns the parent, if any */
    public Path parent() {
        return parent;
    }

    /**
     * Returns the String representation.
     */
    @Override
    public String toString() {
        return name();
    }

    /** Builds the string recursively in buf. */
    private void name(final StringBuilder buf) {
        if (name == null) {
            if (parent != null) {
                parent.name(buf);
                buf.append(SEP);
            }
            buf.append(characterSet.toString(base40, false, false));
            name = buf.toString();
        } else {
            buf.append(name);
        }
    }
}
