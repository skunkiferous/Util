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

    private static class TestRunner<RESPONSE_TYPE> extends SyncRequest<Void> {
        private final Request<RESPONSE_TYPE> request;
        private volatile Object result;

        @SuppressWarnings("unchecked")
        public Object runAndWait() throws InterruptedException {
            signal();
            while (result == null) {
                Thread.sleep(1);
            }
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

    protected <RESPONSE_TYPE> RESPONSE_TYPE call(
            final Request<RESPONSE_TYPE> request) throws Exception {
        final Object result = new TestRunner<RESPONSE_TYPE>(request)
                .runAndWait();
        if (result instanceof Exception) {
            throw (Exception) result;
        }
        return (RESPONSE_TYPE) result;
    }
}
