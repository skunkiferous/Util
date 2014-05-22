package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.impl.JActorStTestPlantConfiguration;
import org.agilewiki.jactor2.core.impl.Plant;

public class GwtTestPlantTest extends BaseGWTTestCase {
    public void testa() throws Exception {
        final JActorStTestPlantConfiguration config = new JActorStTestPlantConfiguration();
        new Plant(new JActorStTestPlantConfiguration());
        try {
        } finally {
            Plant.close();
        }
    }
}
