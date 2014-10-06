/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package java.util.concurrent;
import java.util.*;

/**
 * A {@link java.util.Set} that uses an internal {@link CopyOnWriteArrayList}
 * for all of its operations.  Thus, it shares the same basic properties:
 * <ul>
 *  <li>It is best suited for applications in which set sizes generally
 *       stay small, read-only operations
 *       vastly outnumber mutative operations, and you need
 *       to prevent interference among threads during traversal.
 *  <li>It is thread-safe.
 *  <li>Mutative operations (<tt>add</tt>, <tt>set</tt>, <tt>remove</tt>, etc.)
 *      are expensive since they usually entail copying the entire underlying
 *      array.
 *  <li>Iterators do not support the mutative <tt>remove</tt> operation.
 *  <li>Traversal via iterators is fast and cannot encounter
 *      interference from other threads. Iterators rely on
 *      unchanging snapshots of the array at the time the iterators were
 *      constructed.
 * </ul>
 *
 * <p> <b>Sample Usage.</b> The following code sketch uses a
 * copy-on-write set to maintain a set of Handler objects that
 * perform some action upon state updates.
 *
 * <pre>
 * class Handler { void handle(); ... }
 *
 * class X {
 *    private final CopyOnWriteArraySet&lt;Handler&gt; handlers
 *       = new CopyOnWriteArraySet&lt;Handler&gt;();
 *    public void addHandler(Handler h) { handlers.add(h); }
 *
 *    private long internalState;
 *    private synchronized void changeState() { internalState = ...; }
 *
 *    public void update() {
 *       changeState();
 *       for (Handler handler : handlers)
 *          handler.handle();
 *    }
 * }
 * </pre>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @see CopyOnWriteArrayList
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
public class CopyOnWriteArraySet<E> extends ArrayList<E>
        implements Set<E>, java.io.Serializable {
    private static final long serialVersionUID = 5457747651344034263L;

    /**
     * Creates an empty set.
     */
    public CopyOnWriteArraySet() {
        // NOP
    }

    /**
     * Creates a set containing all of the elements of the specified
     * collection.
     *
     * @param c the collection of elements to initially contain
     * @throws NullPointerException if the specified collection is null
     */
    public CopyOnWriteArraySet(Collection<? extends E> c) {
        addAll(c);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * More formally, adds the specified element <tt>e</tt> to this set if
     * the set contains no element <tt>e2</tt> such that
     * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns <tt>false</tt>.
     *
     * @param e element to be added to this set
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element
     */
    public boolean add(E e) {
        if (!contains(e)) {
            super.add(e);
            return true;
        }
        return false;
    }

    /**
     * Adds all of the elements in the specified collection to this set if
     * they're not already present.  If the specified collection is also a
     * set, the <tt>addAll</tt> operation effectively modifies this set so
     * that its value is the <i>union</i> of the two sets.  The behavior of
     * this operation is undefined if the specified collection is modified
     * while the operation is in progress.
     *
     * @param  c collection containing elements to be added to this set
     * @return <tt>true</tt> if this set changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            if (!contains(e)) {
                super.add(e);
                result = true;
            }
        }
        return result;
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Compares the specified object with this set for equality.
     * Returns {@code true} if the specified object is the same object
     * as this object, or if it is also a {@link Set} and the elements
     * returned by an {@linkplain List#iterator() iterator} over the
     * specified set are the same as the elements returned by an
     * iterator over this set.  More formally, the two iterators are
     * considered to return the same elements if they return the same
     * number of elements and for every element {@code e1} returned by
     * the iterator over the specified set, there is an element
     * {@code e2} returned by the iterator over this set such that
     * {@code (e1==null ? e2==null : e1.equals(e2))}.
     *
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Set))
            return false;
        Set<?> set = (Set<?>)(o);
        return containsAll(set) && set.containsAll(this);
    }
}
