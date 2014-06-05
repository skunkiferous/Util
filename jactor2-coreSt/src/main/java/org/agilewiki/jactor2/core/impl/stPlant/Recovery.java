package org.agilewiki.jactor2.core.impl.stPlant;

import org.agilewiki.jactor2.core.impl.stRequests.RequestStImpl;
import org.agilewiki.jactor2.core.reactors.ReactorImpl;

/**
 * Base class for managing failure detection and recovery.
 * The default Recovery is created by PlantConfiguration.
 */
public class Recovery {

    /**
     * Handles hung request. Default action: close the reactor.
     *
     * @param _requestImpl The reactor with the hung request.
     */
    public void onHungRequest(final RequestStImpl<?> _requestImpl)
            throws Exception {
        final ReactorImpl reactor = _requestImpl.getTargetReactorImpl();
        reactor.error("request hung -> reactor close");
        reactor.fail("hung request");
    }

    /**
     * Handles StackOverflowError. Default action: close the reactor.
     *
     * @param _requestImpl The reactor with the hung request.
     * @param _error       The StackOverflowError.
     */
    public void onStackOverflowError(final RequestStImpl<?> _requestImpl,
            final StackOverflowError _error) {
        final ReactorImpl reactor = _requestImpl.getTargetReactorImpl();
        reactor.error("stack overflow error -> reactor close", _error);
        try {
            reactor.fail("stack overflow");
        } catch (final Exception e) {

        }
    }

    /**
     * Handles RuntimeException. Default action: close the reactor.
     *
     * @param _requestImpl The reactor with the hung request.
     * @param _exception   The runtime exception
     */
    public void onRuntimeException(final RequestStImpl<?> _requestImpl,
            final RuntimeException _exception) {
        final ReactorImpl reactor = _requestImpl.getTargetReactorImpl();
        reactor.error("runtime exception -> reactor close", _exception);
        try {
            reactor.fail("runtime exception");
        } catch (final Exception e) {

        }
    }
}
