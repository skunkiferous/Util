package org.agilewiki.jactor2.core.impl.blades;

import java.io.IOException;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;

/**
 * Test code.
 */
public class GwtTest1 extends BaseGWTTestCase {
    public void testI() throws Exception {
        new Plant();
        final Reactor reactor = new IsolationReactor();
        final BladeA bladeA = new BladeA(reactor);
        try {
            call(bladeA.throwRequest);
        } catch (final IOException se) {
            Plant.close();
            return;
        }
        throw new Exception("IOException was not caught");
    }

}
