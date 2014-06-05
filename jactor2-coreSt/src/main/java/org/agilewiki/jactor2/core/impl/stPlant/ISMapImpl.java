package org.agilewiki.jactor2.core.impl.stPlant;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;

/**
 * <p>
 * A brute-force implementation of ISMap.
 * </p>
 * <pre>
 * Sample:
 *
 * public class ISMapImplSample {
 *     public static void main(final String[] args) {
 *         ISMapImpl&lt;String&gt; ip = IsMapImpl.empty();
 *         ip = ip.plus("one", "1");
 *         ip = ip.plus("two", "2");
 *         ISMapImpl&lt;String&gt; ip2 = ip;
 *         ip = ip.plus("three", "3");
 *         System.out.println(ip2.sortedKeySet());
 *         System.out.println(ip.subMap("t").sortedKeySet());
 *     }
 * }
 *
 * Output:
 *
 * [one, two]
 * [three, two]
 * </pre>
 *
 */
public class ISMapImpl<VALUE> implements ISMap<VALUE> {
    /**
     * Make an empty ISMapImpl instance.
     *
     * @return The empty instance.
     */
    public static <V> ISMapImpl<V> empty() {
        return new ISMapImpl<V>();
    }

    /**
     * Make an ISMapImpl instance with a single key/value pair.
     *
     * @param key   The key to be included.
     * @param value The value to be included.
     * @return The instance with one key/value pair.
     */
    public static <V> ISMapImpl<V> singleton(final String key, final V value) {
        return new ISMapImpl<V>(key, value);
    }

    /**
     * Make an ISMapImpl instance that includes a copy of a map.
     *
     * @param m   The map to be included.
     * @return The instance that includes the map.
     */
    public static <V> ISMapImpl<V> from(final Map<String, V> m) {
        return new ISMapImpl<V>(m);
    }

    private final SortedMap<String, VALUE> base;

    private ISMapImpl() {
        base = Collections.unmodifiableSortedMap(new TreeMap<String, VALUE>());
    }

    private ISMapImpl(final String key, final VALUE value) {
        final TreeMap<String, VALUE> tm = new TreeMap<String, VALUE>();
        tm.put(key, value);
        base = Collections.unmodifiableSortedMap(tm);
    }

    private ISMapImpl(final Map<String, VALUE> m) {
        base = Collections.unmodifiableSortedMap(new TreeMap<String, VALUE>(m));
    }

    private ISMapImpl(final SortedMap<String, VALUE> immutableMap) {
        base = Collections.unmodifiableSortedMap(immutableMap);
    }

    @Override
    public ISMapImpl<VALUE> minus(final String key) {
        final TreeMap<String, VALUE> tm = new TreeMap<String, VALUE>(base);
        tm.remove(key);
        return new ISMapImpl<VALUE>(tm);
    }

    @Override
    public ISMapImpl<VALUE> plus(final String key, final VALUE value) {
        final TreeMap<String, VALUE> tm = new TreeMap<String, VALUE>(base);
        tm.put(key, value);
        return new ISMapImpl<VALUE>(tm);
    }

    @Override
    public ISMapImpl<VALUE> plusAll(final Map<String, VALUE> m) {
        final TreeMap<String, VALUE> tm = new TreeMap<String, VALUE>(base);
        tm.putAll(m);
        return new ISMapImpl<VALUE>(tm);
    }

    @Override
    public ISMapImpl<VALUE> subMap(final String keyPrefix) {
        return new ISMapImpl<VALUE>(base.subMap(keyPrefix, keyPrefix
                + Character.MAX_VALUE));
    }

    @Override
    public VALUE get(final Object key) {
        return base.get(key);
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return base.containsKey(key);
    }

    @Override
    public SortedSet<String> sortedKeySet() {
        return Collections.unmodifiableSortedSet(new TreeSet<String>(base
                .keySet()));
    }

    @Override
    public String toString() {
        return new TreeMap<String, Object>(base).toString();
    }

    @Override
    public Set<String> keySet() {
        return sortedKeySet();
    }

    @Override
    public Collection<VALUE> values() {
        return Collections.unmodifiableCollection(base.values());
    }

    @Override
    public Set<Entry<String, VALUE>> entrySet() {
        return Collections.unmodifiableSet(base.entrySet());
    }

    @Override
    public boolean containsValue(final Object value) {
        return base.containsValue(value);
    }

    @Override
    @Deprecated
    public VALUE put(final String key, final VALUE value) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public VALUE remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void putAll(final Map<? extends String, ? extends VALUE> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
