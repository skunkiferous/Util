package org.agilewiki.jactor2.core.impl.blades.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;
import org.agilewiki.jactor2.core.blades.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;

public class GwtTestSyncTest extends BaseGWTTestCase {
    public void testI() throws Exception {
        new Plant();

        final SyncTransaction<String> addGood = new SyncTransaction<String>() {
            @Override
            protected void update(final ImmutableSource<String> source)
                    throws Exception {
                immutable = "good " + source.getImmutable();
            }
        };

        final SyncTransaction<String> addMoreGood = new SyncTransaction<String>(
                addGood) {
            @Override
            public void update(final ImmutableSource<String> source) {
                immutable = "more " + source.getImmutable();
            }
        };

        final SyncTransaction<String> bogus = new SyncTransaction<String>(
                addGood) {
            @Override
            public void update(final ImmutableSource<String> source)
                    throws Exception {
                throw new NullPointerException();
            }
        };

        try {
            ImmutableReference m = new ImmutableReference<String>("fun");
            System.out.println(m.getImmutable()); // fun
            call(addGood.applyAReq(m));
            System.out.println(m.getImmutable()); // good fun
            m = new ImmutableReference<String>("grapes");
            System.out.println(m.getImmutable()); // grapes
            call(addMoreGood.applyAReq(m));
            System.out.println(m.getImmutable()); // more good grapes
            m = new ImmutableReference<String>("times");
            System.out.println(m.getImmutable()); // times
            try {
                call(bogus.applyAReq(m));
            } catch (final Exception e) {
                System.err.println(e.getMessage());
            }
            System.out.println(m.getImmutable()); // times
        } finally {
            Plant.close();
        }
    }
}
