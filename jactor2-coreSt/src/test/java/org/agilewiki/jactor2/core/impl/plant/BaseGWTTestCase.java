/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agilewiki.jactor2.core.impl.plant;

import org.agilewiki.jactor2.core.impl.JActorStTestInjector;
import org.agilewiki.jactor2.core.impl.stRequests.RequestStImpl;
import org.agilewiki.jactor2.core.plant.impl.PlantImpl;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.*;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.client.UtilEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

/**
 * Base class for our tests.
 *
 * @author monster
 */
public abstract class BaseGWTTestCase extends GWTTestCase {

    public static interface CheckResult {
        void checkResult(Object result);
    }

    public static class DefaultCheckResult implements CheckResult {
        @Override
        public void checkResult(final Object result) {
            if (result instanceof RuntimeException) {
                throw (RuntimeException) result;
            }
            if (result instanceof Error) {
                throw (Error) result;
            }
            if (result instanceof Exception) {
                throw new RuntimeException((Exception) result);
            }
            onRealResult(result);
        }

        protected void onRealResult(final Object result) {
            // NOP
        }
    }

    private static final CheckResult DEFAULT_CHECKER = new DefaultCheckResult();

    private static class TestRunner<RESPONSE_TYPE> extends SOp<Void> {
        private final RequestStImpl<RESPONSE_TYPE> request;
        private volatile Object result;

        public Object getResult() {
            return (result == TestRunner.class) ? null : result;
        }

        public TestRunner(final RequestImpl<RESPONSE_TYPE> request)
                throws Exception {
            super("testRunner", new NonBlockingReactor());
            this.request = (RequestStImpl) request;
        }

        @Override
        public Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
            _requestImpl.getTargetReactor().asReactorImpl().setExceptionHandler(
                    new ExceptionHandler<RESPONSE_TYPE>() {
                        @Override
                        public RESPONSE_TYPE processException(final Exception e)
                                throws Exception {
                            result = e;
                            return null;
                        }
                    });
            request.doSend(_requestImpl.getTargetReactor().asReactorImpl(),
                    new AsyncResponseProcessor<RESPONSE_TYPE>() {
                        @Override
                        public void processAsyncResponse(
                                final RESPONSE_TYPE _response) throws Exception {
                            if (_response == null) {
                                result = TestRunner.class;
                            } else {
                                result = _response;
                            }
                        }
                    });
            return null;
        }
    }

    /** The injector */
    private static JActorStTestInjector injector;

    @Override
    protected void gwtSetUp() throws Exception {
        if (injector == null) {
            new UtilEntryPoint().onModuleLoad();
            injector = GWT.create(JActorStTestInjector.class);
            assertNotNull("TestInjector", injector);
        }
    }

    @Override
    protected void gwtTearDown() throws Exception {
        // Just in case, we don't get back to the event loop between
        // test methods
        SystemUtils.updateCurrentTimeMillis();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "org.agilewiki.jactor2.core.JActor2CoreSt";
    }

    protected <RESPONSE_TYPE> RESPONSE_TYPE call(final SOp<RESPONSE_TYPE> sOp) throws Exception {
        final TestRunner<RESPONSE_TYPE> runner = new TestRunner<RESPONSE_TYPE>(
                PlantImpl.getSingleton().createSyncRequestImpl(sOp, sOp.targetReactor));
        runner.signal();
        final Object result = runner.getResult();
        if (result instanceof Exception) {
            throw (Exception) result;
        }
        return (RESPONSE_TYPE) result;
    }

    protected <RESPONSE_TYPE> RESPONSE_TYPE call(final AOp<RESPONSE_TYPE> aOp) throws Exception {
        final TestRunner<RESPONSE_TYPE> runner = new TestRunner<RESPONSE_TYPE>(
                PlantImpl.getSingleton().createAsyncRequestImpl(aOp, aOp.targetReactor));
        runner.signal();
        final Object result = runner.getResult();
        if (result instanceof Exception) {
            throw (Exception) result;
        }
        return (RESPONSE_TYPE) result;
    }

    protected <RESPONSE_TYPE> void call(final AOp<RESPONSE_TYPE> aOp,
                                        final CheckResult checker, final int wait) throws Exception {
        final CheckResult checker2 = (checker == null) ? DEFAULT_CHECKER
                : checker;
        final TestRunner<RESPONSE_TYPE> runner = new TestRunner<RESPONSE_TYPE>(
                PlantImpl.getSingleton().createAsyncRequestImpl(aOp, aOp.targetReactor));
        runner.signal();
        new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                checker2.checkResult(runner.getResult());
                finishTest();
            }
        }.schedule(wait);
    }
}
