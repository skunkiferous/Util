package org.agilewiki.jactor2.core.impl.stReactors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
        if (isResource(_reactorImpl))
            return;
        IsolationReactorStImpl isolationReactorMtImpl = (IsolationReactorStImpl) _reactorImpl;
        if (isolationReactorMtImpl.isResource(this)) {
            throw new IllegalStateException("circular resources");
        }
        resources.put(isolationReactorMtImpl, Boolean.TRUE);
    }

    @Override
    public boolean isResource(final ReactorImpl _reactorImpl) {
        if (!(_reactorImpl instanceof IsolationReactorStImpl))
            return true;
        if (this == _reactorImpl)
            return true;
        if (resources.containsKey(_reactorImpl))
            return true;
        Set<IsolationReactorStImpl> rs = new HashSet<IsolationReactorStImpl>(resources.size());
        while (rs.size() > 0) {
            IsolationReactorStImpl i = rs.iterator().next();
            if (i.resources.containsKey(_reactorImpl)) {
                resources.put((IsolationReactorStImpl) _reactorImpl, Boolean.TRUE);
                return true;
            }
            rs.addAll(i.resources.keySet());
            rs.remove(i);
        }
        return false;
    }
}
