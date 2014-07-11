package org.agilewiki.jactor2.core.impl.blades.transactions;

import org.agilewiki.jactor2.core.blades.transactions.AsyncTransaction;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;
import org.agilewiki.jactor2.core.blades.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.impl.plant.BaseGWTTestCase;
import org.agilewiki.jactor2.core.plant.DelayAOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;

public class GwtTestAsyncTest extends BaseGWTTestCase {
    public void testI() throws Exception {
        new Plant();
        delayTestFinish(400);

        final AsyncTransaction<String> addGood = new AsyncTransaction<String>() {
            @Override
            protected void update(final ImmutableSource<String> source,
                    final AsyncResponseProcessor<Void> asyncResponseProcessor)
                    throws Exception {
                applyAReq.send(new DelayAOp(100),
                        new AsyncResponseProcessor<Void>() {
                            @Override
                            public void processAsyncResponse(
                                    final Void _response) throws Exception {
                                immutable = "good " + source.getImmutable();
                                asyncResponseProcessor
                                        .processAsyncResponse(null);
                            }
                        });
            }
        };

        final SyncTransaction<String> addMoreGood = new SyncTransaction<String>(
                addGood) {
            @Override
            public void update(final ImmutableSource<String> source) {
                immutable = "more " + source.getImmutable();
            }
        };

        final ImmutableReference<String> m = new ImmutableReference<String>(
                "fun");
        System.out.println(m.getImmutable()); // fun
        call(addGood.applyAReq(m), new DefaultCheckResult() {
            @Override
            public void onRealResult(final Object result) {
                System.out.println(m.getImmutable()); // good fun
                final ImmutableReference<String> m2;
                try {
                    m2 = new ImmutableReference<String>("grapes");
                } catch (final RuntimeException e) {
                    try {
                        Plant.close();
                    } catch (final Exception e1) {
                        // NOP
                    }
                    throw e;
                } catch (final Exception e) {
                    try {
                        Plant.close();
                    } catch (final Exception e1) {
                        // NOP
                    }
                    throw new RuntimeException(e);
                }
                System.out.println(m2.getImmutable()); // grapes
                try {
                    call(addMoreGood.applyAReq(m2), new DefaultCheckResult() {
                        @Override
                        public void checkResult(final Object result) {
                            System.out.println(m2.getImmutable()); // more good grapes
                            try {
                                Plant.close();
                            } catch (final Exception e1) {
                                // NOP
                            }
                        }
                    }, 150);
                } catch (final RuntimeException e) {
                    try {
                        Plant.close();
                    } catch (final Exception e1) {
                        // NOP
                    }
                    throw e;
                } catch (final Exception e) {
                    try {
                        Plant.close();
                    } catch (final Exception e1) {
                        // NOP
                    }
                    throw new RuntimeException(e);
                }
            }
        }, 150);
    }
}
