package org.agilewiki.jactor2.core.impl.blades;

import java.io.IOException;

import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class BladeA {
    public final AOp<Void> throwAOp;

    public BladeA(final Reactor mbox) {
        throwAOp = new AOp<Void>("throw", mbox) {
            @Override
            public void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                throw new IOException("thrown on request");
            }
        };
    }
}
