package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.JActorStTestPlantConfiguration;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.SIOp;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

public class GwtTestSyncTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant(new JActorStTestPlantConfiguration());
        try {
            final Sync1 sync1 = new Sync1();
            sync1.startSOp().signal();
        } finally {
            Plant.close();
        }
    }
}

class Sync1 extends NonBlockingBladeBase {
    Sync1() throws Exception {
        super(new NonBlockingReactor());
    }

    SIOp<Void> startSOp() {
        return new SIOp<Void>("start", getReactor()) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                System.out.println("Hi");
                return null;
            }
        };
    }
}
