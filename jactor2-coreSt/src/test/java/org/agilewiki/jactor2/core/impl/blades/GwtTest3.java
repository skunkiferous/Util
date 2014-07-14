package org.agilewiki.jactor2.core.impl.blades;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;

/**
 * Test code.
 */
public class GwtTest3 extends BaseGWTTestCase {
    public void testI() throws Exception {
        new Plant();
        final BladeC bladeC = new BladeC();
        final String result = call(bladeC.throwAOp());
        assertEquals("java.io.IOException: thrown on request", result);
        Plant.close();
    }
}
