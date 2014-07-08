package org.agilewiki.jactor2.core.impl.requests;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.SyncRequest;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

/**
 * Test code.
 */
public class Blade1 extends NonBlockingBladeBase {

    public Blade1(final NonBlockingReactor mbox) throws Exception {
        super(mbox);
    }

    public SOp<String> hiSOp() {
        return new SOp<String>("hi", getReactor()) {
            @Override
            public String processSyncOperation(RequestImpl _requestImpl) throws Exception {
                return "Hello world!";
            }
        };
    }
}
