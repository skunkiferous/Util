/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package java.util.concurrent;
import java.util.*;

/**
 * A scalable concurrent {@link NavigableSet} implementation based on
 * a {@link ConcurrentSkipListMap}.  The elements of the set are kept
 * sorted according to their {@linkplain Comparable natural ordering},
 * or by a {@link Comparator} provided at set creation time, depending
 * on which constructor is used.
 *
 * <p>This implementation provides expected average <i>log(n)</i> time
 * cost for the <tt>contains</tt>, <tt>add</tt>, and <tt>remove</tt>
 * operations and their variants.  Insertion, removal, and access
 * operations safely execute concurrently by multiple threads.
 * Iterators are <i>weakly consistent</i>, returning elements
 * reflecting the state of the set at some point at or since the
 * creation of the iterator.  They do <em>not</em> throw {@link
 * ConcurrentModificationException}, and may proceed concurrently with
 * other operations.  Ascending ordered views and their iterators are
 * faster than descending ones.
 *
 * <p>Beware that, unlike in most collections, the <tt>size</tt>
 * method is <em>not</em> a constant-time operation. Because of the
 * asynchronous nature of these sets, determining the current number
 * of elements requires a traversal of the elements. Additionally, the
 * bulk operations <tt>addAll</tt>, <tt>removeAll</tt>,
 * <tt>retainAll</tt>, and <tt>containsAll</tt> are <em>not</em>
 * guaranteed to be performed atomically. For example, an iterator
 * operating concurrently with an <tt>addAll</tt> operation might view
 * only some of the added elements.
 *
 * <p>This class and its iterators implement all of the
 * <em>optional</em> methods of the {@link Set} and {@link Iterator}
 * interfaces. Like most other concurrent collection implementations,
 * this class does not permit the use of <tt>null</tt> elements,
 * because <tt>null</tt> arguments and return values cannot be reliably
 * distinguished from the absence of elements.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author Doug Lea
 * @param <E> the type of elements maintained by this set
 * @since 1.6
 */
public class ConcurrentSkipListSet<E>
    extends TreeSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = -2479143111061671589L;

    /**
     * Constructs a new, empty set that orders its elements according to
     * their {@linkplain Comparable natural ordering}.
     */
    public ConcurrentSkipListSet() {
        // NOP
    }

    /**
     * Constructs a new, empty set that orders its elements according to
     * the specified comparator.
     *
     * @param comparator the comparator that will be used to order this set.
     *        If <tt>null</tt>, the {@linkplain Comparable natural
     *        ordering} of the elements will be used.
     */
    public ConcurrentSkipListSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * Constructs a new set containing the elements in the specified
     * collection, that orders its elements according to their
     * {@linkplain Comparable natural ordering}.
     *
     * @param c The elements that will comprise the new set
     * @throws ClassCastException if the elements in <tt>c</tt> are
     *         not {@link Comparable}, or are not mutually comparable
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    public ConcurrentSkipListSet(Collection<? extends E> c) {
        super(c);
    }

    /**
     * Constructs a new set containing the same elements and using the
     * same ordering as the specified sorted set.
     *
     * @param s sorted set whose elements will comprise the new set
     * @throws NullPointerException if the specified sorted set or any
     *         of its elements are null
     */
    public ConcurrentSkipListSet(SortedSet<E> s) {
        super(s);
    }
}
