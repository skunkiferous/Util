package org.agilewiki.jactor2.core.impl.stCloseable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.agilewiki.jactor2.core.closeable.Closeable;
import org.agilewiki.jactor2.core.closeable.impl.CloseableImpl;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.reactors.impl.ReactorImpl;

/**
 * Implements multiple dependencies.
 */
public class CloseableStImpl implements CloseableImpl {
    private final Closeable closeable;

    private final Set<ReactorImpl> closers = new HashSet<ReactorImpl>();

    private volatile boolean closing;

    /**
     * Create a closeableImpl for a closeable.
     * @param _closeable    The closeable that will hold a reference to this implementation.
     */
    public CloseableStImpl(final Closeable _closeable) {
        closeable = _closeable;
    }

    @Override
    public void addReactor(final ReactorImpl _reactorImpl) {
        if (closing) {
            throw new ReactorClosedException("Closeable is closed");
        }
        closers.add(_reactorImpl);
    }

    @Override
    public void removeReactor(final ReactorImpl _reactorImpl) {
        if (closing) {
            return;
        }
        closers.remove(_reactorImpl);
    }

    @Override
    public void close() throws Exception {
        closing = true;
        final Iterator<ReactorImpl> it = closers.iterator();
        while (it.hasNext()) {
            final ReactorImpl reactorImpl = it.next();
            reactorImpl.removeCloseable(closeable);
        }
    }
}
