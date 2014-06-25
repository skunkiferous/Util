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
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.core.requests.Request;
import org.agilewiki.jactor2.core.requests.SyncRequest;

import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.client.UtilEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

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

    private static class TestRunner<RESPONSE_TYPE> extends SyncRequest<Void> {
        private final Request<RESPONSE_TYPE> request;
        private volatile Object result;

        public Object getResult() {
            return (result == TestRunner.class) ? null : result;
        }

        public TestRunner(final Request<RESPONSE_TYPE> request)
                throws Exception {
            super(new NonBlockingReactor());
            this.request = request;
        }

        @Override
        public Void processSyncRequest() throws Exception {
            getTargetReactor().asReactorImpl().setExceptionHandler(
                    new ExceptionHandler<RESPONSE_TYPE>() {
                        @Override
                        public RESPONSE_TYPE processException(final Exception e)
                                throws Exception {
                            result = e;
                            return null;
                        }
                    });
            request.asRequestImpl().doSend(getTargetReactor().asReactorImpl(),
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

    @SuppressWarnings("unchecked")
    protected <RESPONSE_TYPE> RESPONSE_TYPE call(
            final Request<RESPONSE_TYPE> request) throws Exception {
        final TestRunner<RESPONSE_TYPE> runner = new TestRunner<RESPONSE_TYPE>(
                request);
        runner.signal();
        final Object result = runner.getResult();
        if (result instanceof Exception) {
            throw (Exception) result;
        }
        return (RESPONSE_TYPE) result;
    }

    protected <RESPONSE_TYPE> void call(final Request<RESPONSE_TYPE> request,
            final CheckResult checker, final int wait) throws Exception {
        final CheckResult checker2 = (checker == null) ? DEFAULT_CHECKER
                : checker;
        final TestRunner<RESPONSE_TYPE> runner = new TestRunner<RESPONSE_TYPE>(
                request);
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
