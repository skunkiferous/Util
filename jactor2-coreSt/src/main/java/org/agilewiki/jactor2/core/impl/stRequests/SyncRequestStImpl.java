package org.agilewiki.jactor2.core.impl.stRequests;

import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SyncRequest;
import org.agilewiki.jactor2.core.util.Timer;

/**
 * Internal implementation of a SyncRequest.
 *
 * @param <RESPONSE_TYPE> The response value type.
 */
public class SyncRequestStImpl<RESPONSE_TYPE> extends
        RequestStImpl<RESPONSE_TYPE> {

    private final SyncRequest<RESPONSE_TYPE> syncRequest;

    /**
     * Create a SyncRequestStImpl and bind it to its target reactor.
     *
     * @param _syncRequest   The request being implemented.
     * @param _targetReactor The target reactor.
     */
    public SyncRequestStImpl(final SyncRequest _syncRequest,
            final Reactor _targetReactor) {
        super(_targetReactor);
        syncRequest = _syncRequest;
    }

    @Override
    public SyncRequest asRequest() {
        return syncRequest;
    }

    @Override
    protected void processRequestMessage() throws Exception {
        final Timer timer = syncRequest.getTimer();
        final long start = timer.nanos();
        boolean success = false;
        final RESPONSE_TYPE result;
        try {
            result = syncRequest.processSyncRequest();
            success = true;
        } finally {
            timer.updateNanos(timer.nanos() - start, success);
        }

        processObjectResponse(result);
    }
}
