package org.agilewiki.jactor2.core.impl.requests;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;

/**
 * Test code.
 */
public class GwtTest3 extends BaseGWTTestCase {
    public void testb() throws Exception {
        new Plant();
        final Blade3 blade3 = new Blade3();
        call(blade3.hi3SOp());
        Plant.close();
    }
}
