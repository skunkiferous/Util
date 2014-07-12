package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class GwtTestHungRequestTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant();
        try {
            final Hanger blade1 = new Hanger(new NonBlockingReactor());
            try {
                call(blade1.hiAOp());
            } catch (final ReactorClosedException sce) {
            }
            final Hung blade2 = new Hung(new NonBlockingReactor(), new Hanger(
                    new NonBlockingReactor()));
            try {
                call(blade2.hoAOp());
            } catch (final ReactorClosedException sce) {
            }
        } finally {
            Plant.close();
        }
    }
}

class Hanger extends NonBlockingBladeBase {

    public Hanger(final NonBlockingReactor mbox) throws Exception {
        super(mbox);
    }

    public AOp<String> hiAOp() {
        return new AOp<String>("hi", getReactor()) {
            @Override
            public void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<String> _asyncResponseProcessor)
                    throws Exception {
                System.out.println("    hang");
            }
        };
    }
}

class Hung extends NonBlockingBladeBase {

    private final Hanger hanger;

    public Hung(final NonBlockingReactor mbox, final Hanger _hanger)
            throws Exception {
        super(mbox);
        hanger = _hanger;
    }

    public AOp<String> hoAOp() {
        return new AOp<String>("ho", getReactor()) {
            @Override
            public void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<String> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.send(hanger.hiAOp(), _asyncResponseProcessor);
            }
        };
    }
}
