package org.agilewiki.jactor2.core.impl.requests;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;

/**
 * Test code.
 */
public class GwtTest1 extends BaseGWTTestCase {
    public void testa() throws Exception {
        new Plant();
        final IsolationReactor reactor = new IsolationReactor();
        final Blade11 blade1 = new Blade11(reactor);
        final String result = call(blade1.hiSReq());
        assertEquals("Hello world!", result);
        Plant.close();
    }
}
