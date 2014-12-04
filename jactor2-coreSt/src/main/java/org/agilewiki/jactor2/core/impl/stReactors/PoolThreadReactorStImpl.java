package org.agilewiki.jactor2.core.impl.stReactors;

import org.agilewiki.jactor2.core.impl.stPlant.PlantStImpl;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.impl.PoolThreadReactorImpl;

abstract public class PoolThreadReactorStImpl extends ReactorStImpl implements
        PoolThreadReactorImpl {
    private Runnable onIdle;

    /**
     * Create an PoolThreadReactorStImpl.
     *
     * @param _parentReactor         The parent reactor.
     */
    public PoolThreadReactorStImpl(final IsolationReactor _parentReactor) {
        super(_parentReactor);
    }

    @Override
    protected void notBusy() throws Exception {
        if ((onIdle != null) && inbox.isIdle()) {
            onIdle.run();
        }
    }

    @Override
    protected void afterAdd() {
        final PlantStImpl plantStImpl = PlantStImpl.getSingleton();
        plantStImpl.submit(this);
    }

    /**
     * The object to be run when the inbox is emptied and before the threadReference is cleared.
     */
    @Override
    public Runnable getOnIdle() {
        return onIdle;
    }

    @Override
    public void setOnIdle(final Runnable onIdle) {
        this.onIdle = onIdle;
    }
}
