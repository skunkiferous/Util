package org.agilewiki.jactor2.core.impl.stReactors;

import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.impl.ReactorImpl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internal implementation of UnboundReactor.
 */
public class IsolationReactorStImpl extends PoolThreadReactorStImpl {

    private final Set<IsolationReactorStImpl> resources =
            Collections.newSetFromMap(new ConcurrentHashMap<IsolationReactorStImpl, Boolean>());

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
    public void addResource(ReactorImpl _reactorImpl) {
        if (_reactorImpl instanceof IsolationReactorStImpl) {
            IsolationReactorStImpl isolationReactorMtImpl = (IsolationReactorStImpl) _reactorImpl;
            if (isolationReactorMtImpl.isResource(this)) {
                throw new IllegalStateException("circular resources");
            }
            resources.add(isolationReactorMtImpl);
        }
    }

    @Override
    public boolean isResource(ReactorImpl _reactorImpl) {
        if (this == _reactorImpl)
            return true;
        if (_reactorImpl instanceof IsolationReactorStImpl) {
            if (!resources.contains(_reactorImpl)) {
                Iterator<IsolationReactorStImpl> it = resources.iterator();
                while (it.hasNext()) {
                    IsolationReactorStImpl i = it.next();
                    if (i.isResource(_reactorImpl))
                        return true;
                }
                return false;
            }
        }
        return true;
    }
}
