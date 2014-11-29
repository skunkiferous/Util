package org.agilewiki.jactor2.core.impl.stReactors;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.impl.ReactorImpl;

/**
 * Internal implementation of UnboundReactor.
 */
public class IsolationReactorStImpl extends PoolThreadReactorStImpl {

    private final ConcurrentHashMap<IsolationReactorStImpl, Boolean> resources = new ConcurrentHashMap<IsolationReactorStImpl, Boolean>();

    /**
     * Create an IsolationReactorMtImpl.
     *
     * @param _parentReactor         The parent reactor.
     */
    public IsolationReactorStImpl(final NonBlockingReactor _parentReactor) {
        super(_parentReactor);
    }

    @Override
    public IsolationReactor asReactor() {
        return (IsolationReactor) getReactor();
    }

    @Override
    protected Inbox createInbox() {
        return new IsolationInbox();
    }

    @Override
    public void addResource(final ReactorImpl _reactorImpl) {
        if (_reactorImpl instanceof IsolationReactorStImpl) {
            final IsolationReactorStImpl isolationReactorMtImpl = (IsolationReactorStImpl) _reactorImpl;
            if (isolationReactorMtImpl.isResource(this)) {
                throw new IllegalStateException("circular resources");
            }
            resources.put(isolationReactorMtImpl, Boolean.TRUE);
        }
    }

    @Override
    public boolean isResource(final ReactorImpl _reactorImpl) {
        if (this == _reactorImpl)
            return true;
        if (_reactorImpl instanceof IsolationReactorStImpl) {
            if (!resources.contains(_reactorImpl)) {
                final Iterator<IsolationReactorStImpl> it = resources.keySet()
                        .iterator();
                while (it.hasNext()) {
                    final IsolationReactorStImpl i = it.next();
                    if (i.isResource(_reactorImpl))
                        return true;
                }
                return false;
            }
        }
        return true;
    }
}
