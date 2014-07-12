package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.JActorStTestPlantConfiguration;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class GwtTestIsolationTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant(new JActorStTestPlantConfiguration());
        try {
            final Iso1 iso1 = new Iso1();
            iso1.startAOp().signal();
        } finally {
            Plant.close();
        }
    }
}

class Iso1 extends NonBlockingBladeBase {
    Iso1() throws Exception {
        super(new NonBlockingReactor());
    }

    AOp<Void> startAOp() {
        return new AOp<Void>("start", getReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                AsyncResponseProcessor<Void> doResponseProcessor = new AsyncResponseProcessor<Void>() {
                    @Override
                    public void processAsyncResponse(final Void _response)
                            throws Exception {
                        if (_asyncRequestImpl.getPendingResponseCount() == 0) {
                            _asyncResponseProcessor.processAsyncResponse(null);
                        }
                    }
                };

                final Iso2 iso2 = new Iso2(new IsolationReactor());
                _asyncRequestImpl.send(iso2.fooAOp(), doResponseProcessor);
                _asyncRequestImpl.send(iso2.fooAOp(), doResponseProcessor);
                _asyncRequestImpl.send(iso2.fooAOp(), doResponseProcessor);
            }
        };
    }
}

class Iso2 extends BladeBase {
    Iso2(final Reactor reactor) {
        _initialize(reactor);
    }

    AOp<Void> fooAOp() {
        return new AOp<Void>("foo", getReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                final Iso3 iso3 = new Iso3();
                System.out.println("begin");
                _asyncRequestImpl.send(iso3.barAOp(), new AsyncResponseProcessor<Void>() {
                    @Override
                    public void processAsyncResponse(final Void _response)
                            throws Exception {
                        System.out.println("end");
                        _asyncResponseProcessor.processAsyncResponse(null);
                    }
                });
            }
        };
    }
}

class Iso3 extends NonBlockingBladeBase {
    Iso3() throws Exception {
        super(new NonBlockingReactor());
    }

    AOp<Void> barAOp() {
        return new AOp<Void>("bar", getReactor()) {
            @Override
            public void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }
}
