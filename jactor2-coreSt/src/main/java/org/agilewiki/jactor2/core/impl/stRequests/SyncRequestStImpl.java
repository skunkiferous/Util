package org.agilewiki.jactor2.core.impl.stRequests;

import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SyncNativeRequest;
import org.agilewiki.jactor2.core.requests.SyncOperation;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;
import org.agilewiki.jactor2.core.util.Timer;

/**
 * Internal implementation of a SyncRequest.
 *
 * @param <RESPONSE_TYPE> The response value type.
 */
public class SyncRequestStImpl<RESPONSE_TYPE> extends
        RequestStImpl<RESPONSE_TYPE> implements SyncNativeRequest<RESPONSE_TYPE> {

    private final SyncOperation<RESPONSE_TYPE> syncOperation;

    /**
     * Create a SyncRequestStImpl and bind it to its operation and target reactor.
     *
     * @param _syncOperation   The request being implemented.
     * @param _targetReactor The target reactor.
     */
    public SyncRequestStImpl(final SyncOperation<RESPONSE_TYPE> _syncOperation,
            final Reactor _targetReactor) {
        super(_targetReactor);
        syncOperation = _syncOperation;
    }

    @Override
    public SyncOperation<RESPONSE_TYPE> asOperation() {
        return syncOperation;
    }

    @Override
    protected void processRequestMessage() throws Exception {
        final Timer timer = syncOperation.getTimer();
        final long start = timer.nanos();
        boolean success = false;
        final RESPONSE_TYPE result;
        try {
            result = syncOperation.doSync(this);
            success = true;
        } finally {
            timer.updateNanos(timer.nanos() - start, success);
        }

        processObjectResponse(result);
    }

    @Override
    public RESPONSE_TYPE doSync(final RequestImpl _requestImpl) throws Exception {
        if (!_requestImpl.getTargetReactor().asReactorImpl().isRunning())
            throw new IllegalStateException(
                    "Not thread safe: not called from within an active request");
        return processSyncOperation(_requestImpl);
    }

    public RESPONSE_TYPE processSyncOperation(final RequestImpl _requestImpl) throws Exception {
        throw new IllegalStateException();
    }
}
