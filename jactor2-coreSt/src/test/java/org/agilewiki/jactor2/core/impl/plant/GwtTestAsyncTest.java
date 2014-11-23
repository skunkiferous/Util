package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.JActorStTestPlantConfiguration;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AIOp;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class GwtTestAsyncTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant(new JActorStTestPlantConfiguration());
        try {
            final Async1 async1 = new Async1();
            async1.startAOp().signal();
        } finally {
            Plant.close();
        }
    }
}

class Async1 extends NonBlockingBladeBase {
    Async1() throws Exception {
        super(new NonBlockingReactor());
    }

    AIOp<Void> startAOp() {
        return new AIOp<Void>("start", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                final Async2 async2 = new Async2();
                _asyncRequestImpl.send(async2.getAOp(), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(final String _response)
                            throws Exception {
                        System.out.println(_response);
                        _asyncResponseProcessor.processAsyncResponse(null);
                    }
                });
            }
        };
    }
}

class Async2 extends NonBlockingBladeBase {
    Async2() throws Exception {
        super(new NonBlockingReactor());
    }

    AOp<String> getAOp() {
        return new AOp<String>("get", getReactor()) {
            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<String> _asyncResponseProcessor)
                    throws Exception {
                _asyncResponseProcessor.processAsyncResponse("Hi");
            }
        };
    }
}
