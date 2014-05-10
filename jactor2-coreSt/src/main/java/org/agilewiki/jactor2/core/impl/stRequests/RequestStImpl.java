package org.agilewiki.jactor2.core.impl.stRequests;

import org.agilewiki.jactor2.core.impl.stPlant.PlantStImpl;
import org.agilewiki.jactor2.core.impl.stReactors.ReactorStImpl;
import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.reactors.ReactorImpl;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.core.requests.RequestImpl;

import java.util.concurrent.Semaphore;

/**
 * Base class for internal reactor implementations.
 *
 * @param <RESPONSE_TYPE>
 */
public abstract class RequestStImpl<RESPONSE_TYPE> implements RequestImpl<RESPONSE_TYPE> {

    /**
     * Assigned to current time when Facility.DEBUG.
     */
    private Long debugTimestamp;

    /**
     * A request can only be used once.
     */
    protected boolean used;

    /**
     * The reactor where this Request Object is passed for processing. The thread
     * owned by this targetReactor will process the Request.
     */
    protected final Reactor targetReactor;

    /**
     * The reactor impl where this Request Object is passed for processing. The thread
     * owned by this reactor impl will process the Request.
     */
    protected final ReactorStImpl targetReactorImpl;

    /**
     * The source reactor or Pender that will receive the results.
     */
    protected ReactorStImpl requestSource;

    /**
     * The request targeted to the source reactor which, when processed,
     * resulted in this message.
     */
    protected RequestStImpl oldMessage;

    /**
     * The exception handler that was active in the source reactor
     * when this message was created.
     */
    protected ExceptionHandler sourceExceptionHandler;

    /**
     * The application object that will process the results.
     */
    protected AsyncResponseProcessor responseProcessor;

    /**
     * True when a response to this message has not yet been determined.
     */
    protected boolean incomplete = true;

    /**
     * True when this reactor impl is closed.
     */
    protected boolean closed = false;

    /**
     * True when the request is, directly or indirectly, from an IsolationReactor that awaits a response.
     */
    private boolean isolated;

    /**
     * The response created when this request impl is evaluated.
     */
    protected Object response;

    /**
     * True when this request impl has been canceled.
     */
    protected boolean canceled;

    /**
     * Create a RequestStImpl.
     *
     * @param _targetReactor The targetReactor where this Request Object is passed for processing.
     *                       The thread owned by this reactor will process this Request.
     */
    public RequestStImpl(final Reactor _targetReactor) {
        if (_targetReactor == null) {
            throw new NullPointerException("targetMessageProcessor");
        }
        targetReactor = _targetReactor;
        targetReactorImpl = (ReactorStImpl) targetReactor.asReactorImpl();
    }

    /**
     * Returns true when the target reactor is not the request source.
     *
     * @return True when the target reactor is not the request source.
     */
    public boolean isForeign() {
        return targetReactor != requestSource;
    }

    /**
     * Returns true when the request does not pass back a result.
     *
     * @return True when the request does not pass back a result.
     */
    public boolean isOneWay() {
        return responseProcessor == OneWayResponseProcessor.SINGLETON ||
                responseProcessor == SignalResponseProcessor.SINGLETON;
    }

    /**
     * Returns true when the request was passed using the signal method.
     * @return True when the request was passed using the signal method.
     */
    public boolean isSignal() {
        return responseProcessor == SignalResponseProcessor.SINGLETON;
    }

    /**
     * Returns the Reactor to which this Request is bound and to which this Request is to be passed.
     *
     * @return The target Reactor.
     */
    public ReactorImpl getTargetReactorImpl() {
        return targetReactorImpl;
    }

    public Reactor getTargetReactor() {
        return targetReactor;
    }

    @Override
    public Reactor getSourceReactor() {
        return requestSource.asReactor();
    }

    public ReactorStImpl getRequestSource() {
        return requestSource;
    }


    /**
     * Marks the request as having been used, or throws an
     * exception if the request was already used.
     */
    protected void use() {
        if (used) {
            throw new IllegalStateException("Already used");
        }
        used = true;
    }

    /**
     * Passes this Request to the target Reactor without any result being passed back.
     * I.E. The signal method results in a 1-way message being passed.
     * If an exception is thrown while processing this Request,
     * that exception is simply logged as a warning.
     */
    public void signal() {
        use();
        responseProcessor = SignalResponseProcessor.SINGLETON;
        targetReactorImpl.unbufferedAddMessage(this, false);
        PlantStImpl plantStImpl = PlantStImpl.getSingleton();
        if (plantStImpl.currentReactorImpl == null)
            plantStImpl.processMessages();
    }

    /**
     * Passes this RequestImpl together with the AsyncResponseProcessor to the target Reactor.
     * Responses are passed back via the source reactor and processed by the
     * provided AsyncResponseProcessor. Any exceptions
     * raised while processing the request are processed by the exception handler active when
     * the doSend method was called.
     *
     * @param _source            The source reactor impl on whose thread this method was invoked and which
     *                           will buffer this Request and subsequently receive the result for
     *                           processing on the same thread.
     * @param _responseProcessor Passed with this request and then returned with the result, the
     *                           AsyncResponseProcessor is used to process the result on the same thread
     *                           that originally invoked this method. If null, then no response is returned.
     */
    public void doSend(final ReactorImpl _source,
                       final AsyncResponseProcessor<RESPONSE_TYPE> _responseProcessor) {
        final ReactorStImpl source = (ReactorStImpl) _source;
        if (!source.isRunning()) {
            throw new IllegalStateException(
                    "A valid source sourceReactor can not be idle");
        }
        if ((oldMessage != null) && oldMessage.isIsolated()) {
            isolated = true;
        }
        isolated = !source.isCommonReactor();
        if (!(targetReactor instanceof CommonReactor)) {
            if (isolated && (_responseProcessor != null)) {
                throw new UnsupportedOperationException(
                        "Isolated requests can not be nested, even indirectly.");
            }
            isolated = true;
        }
        use();
        AsyncResponseProcessor<RESPONSE_TYPE> rp = _responseProcessor;
        if (rp == null) {
            rp = (AsyncResponseProcessor<RESPONSE_TYPE>) OneWayResponseProcessor.SINGLETON;
        }
        requestSource = source;
        oldMessage = source.getCurrentRequest();
        sourceExceptionHandler = source.getExceptionHandler();
        responseProcessor = rp;
        final boolean local = targetReactor == source.asReactor();
        targetReactorImpl.unbufferedAddMessage(this, local);
    }

    /**
     * Passes this Request to the target Reactor and blocks the current thread until
     * a result is returned. The call method sends the message directly without buffering,
     * as there is no source reactor. The response message is buffered, though thread migration is
     * not possible.
     *
     * @return The response value from applying this Request to the target reactor.
     * @throws Exception If the result is an exception, it is thrown rather than being returned.
     */
    public RESPONSE_TYPE call() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Assigns a response value.
     *
     * @param _response      The response value.
     * @param _activeReactor The responding reactor.
     */
    protected void setResponse(final Object _response,
                               final ReactorStImpl _activeReactor) {
        _activeReactor.requestEnd(this);
        incomplete = false;
        response = _response;
    }

    /**
     * The processObjectResponse method accepts the response value of a request.
     * <p>
     * This method need not be thread-safe, as it
     * is always invoked from the same light-weight thread (target reactor) that received the
     * Request.
     * </p>
     *
     * @param _response The response to a request.
     * @return True when this is the first response.
     */
    protected boolean processObjectResponse(final Object _response) {
        if (!incomplete) {
            return false;
        }
        setResponse(_response, targetReactorImpl);
        if (!isOneWay()) {
            requestSource.incomingResponse(RequestStImpl.this, targetReactorImpl);
        } else {
            if (_response instanceof Throwable) {
                targetReactor.asReactorImpl().warn("Uncaught throwable",
                        (Throwable) _response);
            }
        }
        return true;
    }

    @Override
    public boolean isCanceled() throws ReactorClosedException {
        if (closed)
            throw new ReactorClosedException();
        return canceled;
    }

    public boolean _isCanceled() {
        return canceled;
    }

    public boolean isComplete() {
        return !incomplete;
    }

    public boolean isIsolated() {
        return isolated;
    }

    @Override
    public void close() {
        if (!incomplete) {
            return;
        }
        incomplete = false;
        closed = true;
        response = new ReactorClosedException();
        if (requestSource != null)
            requestSource.incomingResponse(this, null);
    }

    /**
     * Cancel this request.
     */
    public void cancel() {
        if (canceled)
            return;
        canceled = true;
    }

    /**
     * Process a request or the response.
     */
    public void eval() {
        if (incomplete) {
            targetReactorImpl.setExceptionHandler(null);
            targetReactorImpl.setCurrentRequest(this);
            targetReactorImpl.requestBegin(this);
            try {
                processRequestMessage();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (final RuntimeException re) {
                processException(targetReactorImpl, new ReactorClosedException());
                targetReactorImpl.getRecovery().onRuntimeException(this, re);
            } catch (final Exception e) {
                processException(targetReactorImpl, e);
            } catch (final StackOverflowError soe) {
                processException(targetReactorImpl, new ReactorClosedException());
                targetReactorImpl.getRecovery().onStackOverflowError(this, soe);
            }
        } else {
            processResponseMessage();
        }
    }

    /**
     * Process a request.
     */
    abstract protected void processRequestMessage() throws Exception;

    /**
     * A response has been received for a subordinate request.
     * @param request    A subordinate request.
     */
    public void responseReceived(RequestImpl request) {
    }

    /**
     * A response value from a subordinate request has been processed.
     */
    public void responseProcessed() {
    }

    /**
     * Process a response.
     */
    protected void processResponseMessage() {
        oldMessage.responseReceived(this);
        final ReactorStImpl sourceMessageProcessor = (ReactorStImpl) requestSource;
        sourceMessageProcessor.setExceptionHandler(sourceExceptionHandler);
        sourceMessageProcessor.setCurrentRequest(oldMessage);
        if (response instanceof Exception) {
            oldMessage.processException(sourceMessageProcessor,
                    (Exception) response);
            oldMessage.responseProcessed();
            return;
        }
        try {
            responseProcessor.processAsyncResponse(response);
        } catch (final Exception e) {
            oldMessage.processException(sourceMessageProcessor, e);
        }
        oldMessage.responseProcessed();
    }

    /**
     * Process the exception on the current thread in the facility of the active reactor.
     *
     * @param _activeReactor The reactor providing the facility for processing the throwable.
     * @param _e             The exception to be processed.
     */
    public void processException(final ReactorStImpl _activeReactor,
                                 final Exception _e) {
        final ReactorStImpl activeMessageProcessor = _activeReactor;
        final ExceptionHandler<RESPONSE_TYPE> exceptionHandler = activeMessageProcessor
                .getExceptionHandler();
        if (exceptionHandler != null) {
            try {
                exceptionHandler.processException(_e, new AsyncResponseProcessor() {
                    @Override
                    public void processAsyncResponse(Object _response) {
                        processObjectResponse(_response);
                    }
                });
            } catch (final Throwable u) {
                if (!isOneWay()) {
                    if (!incomplete) {
                        return;
                    }
                    setResponse(u, activeMessageProcessor);
                    requestSource
                            .incomingResponse(this, activeMessageProcessor);
                } else {
                    activeMessageProcessor
                            .error("Thrown by exception handler and uncaught "
                                    + exceptionHandler.getClass().getName(), _e);
                }
            }
        } else {
            if (!incomplete) {
                return;
            }
            setResponse(_e, activeMessageProcessor);
            if (!isOneWay()) {
                requestSource.incomingResponse(this, activeMessageProcessor);
            } else {
                activeMessageProcessor.warn("Uncaught throwable",
                        _e);
            }
        }
    }

    @Override
    public String toString() {
        return "message=" + asRequest() +
                ", isComplete=" + isComplete() +
                ", isOneWay=" + isOneWay() +
                ", source=" + (requestSource == null ? "null" : requestSource) +
                ", target=" + getTargetReactor().asReactorImpl() +
                ", this=" + super.toString() +
                (oldMessage == null ? "" : "\n" + oldMessage.toString());
    }

    /**
     * A subclass of AsyncResponseProcessor that is used as a place holder when the RequestStImpl.call
     * method is used.
     */
    final private static class CallResponseProcessor implements
            AsyncResponseProcessor<Object> {
        /**
         * The singleton.
         */
        public static final CallResponseProcessor SINGLETON = new CallResponseProcessor();

        /**
         * Restrict the use of this class to being a singleton.
         */
        private CallResponseProcessor() {
        }

        @Override
        public void processAsyncResponse(final Object response) {
            throw new UnsupportedOperationException();
        }
    }
}
