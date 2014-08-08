package org.agilewiki.jactor2.core.impl.reactors;

import org.agilewiki.jactor2.core.blades.IsolationBladeBase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;
import org.agilewiki.jactor2.core.plant.DelayAOp;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class GwtTestICloseTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant();
        try {
            delayTestFinish(150);
            call(new IHang().goAOp(), null, 100);
        } finally {
            Plant.close();
        }
    }
}

class IHang extends NonBlockingBladeBase {

    IHang() throws Exception {
    }

    AOp<Void> goAOp() {
        return new AOp<Void>("go", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                IHung iHung = new IHung();
                final AOp<Void> noRspAReq = iHung.noRspAOp();
                final AsyncRequestImpl<Void> noRsp = _asyncRequestImpl.send(noRspAReq, _asyncResponseProcessor);
                _asyncRequestImpl.send(iHung.getReactor().nullSOp(), _asyncResponseProcessor);
                _asyncRequestImpl.send(new DelayAOp(50), new AsyncResponseProcessor<Void>() {
                    @Override
                    public void processAsyncResponse(final Void _response) {
                        _asyncRequestImpl.cancel(noRsp);
                    }
                });
            }
        };
    }
}

class IHung extends IsolationBladeBase {

    IHung() throws Exception {
    }

    AOp<Void> noRspAOp() {
        return new AOp<Void>("noRsp", getReactor()) {
            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.setNoHungRequestCheck();
                System.out.println("hi");
            }
        };
    }
}
