package org.agilewiki.jactor2.core.impl.stReactors;

import java.util.concurrent.LinkedBlockingQueue;

import org.agilewiki.jactor2.core.impl.stRequests.RequestStImpl;

/**
 * The inbox used by IsolationReactor, the next request is not made available for processing
 * until a result is assigned to the previous request. This is implemented using
 * two ArrayDeques as the doLocal queues, one for requests and the other for events and
 * responses.
 */
public class IsolationInbox extends Inbox {

    /**
     * The request being processed to completion.
     */
    private RequestStImpl<?> processingRequest;

    /**
     * Local response-pending (requests) queue for same-thread exchanges.
     */
    private final LinkedBlockingQueue<RequestStImpl<?>> localResponsePendingQueue;

    /**
     * Local no-response-pending (events and responses) queue for same-thread exchanges.
     */
    private final LinkedBlockingQueue<RequestStImpl<?>> localNoResponsePendingQueue;

    /**
     * Creates an IsolationInbox.
     *
     */
    public IsolationInbox() {
        localResponsePendingQueue = new LinkedBlockingQueue<RequestStImpl<?>>();
        localNoResponsePendingQueue = new LinkedBlockingQueue<RequestStImpl<?>>();
    }

    @Override
    protected void offerLocal(final RequestStImpl<?> msg) {
        if (msg.isComplete() || msg.isSignal()) {
            localNoResponsePendingQueue.offer(msg);
            return;
        }
        if (msg.getSourceReactor() != null && msg.getSourceReactor() == msg.getTargetReactor()) {
            localNoResponsePendingQueue.offer(msg);
            return;
        }
        RequestStImpl<?> oldMsg = msg.getOldRequest();
        if (oldMsg != null && oldMsg.getIsolationReactor() != null) {
            localNoResponsePendingQueue.offer(msg);
            return;
        }
        localResponsePendingQueue.offer(msg);
    }

    @Override
    public boolean isEmpty() {
        return localResponsePendingQueue.isEmpty()
                && localNoResponsePendingQueue.isEmpty();
    }

    @Override
    public boolean isIdle() {
        return null == processingRequest && isEmpty();
    }

    @Override
    public boolean hasWork() {
        if (localNoResponsePendingQueue.isEmpty()
                && (processingRequest != null || localResponsePendingQueue.isEmpty())) {
            return false;
        }
        return true;
    }

    @Override
    public RequestStImpl<?> poll() {
        if (!hasWork()) {
            return null;
        }
        final RequestStImpl<?> msg = localNoResponsePendingQueue.poll();
        if (msg != null) {
            return msg;
        } else {
            return localResponsePendingQueue.poll();
        }
    }

    @Override
    public void requestBegin(final RequestStImpl<?> _requestImpl) {
        if (_requestImpl.isSignal()) {
            return;
        }
        if (processingRequest != null) {
            return;
        }
        processingRequest = _requestImpl;
    }

    @Override
    public void requestEnd(final RequestStImpl<?> _requestImpl) {
        if (_requestImpl.isSignal()) {
            return;
        }
        if (processingRequest == null) {
            throw new IllegalStateException("not processing request:\n" + _requestImpl.toString());
        }
        if (processingRequest != _requestImpl)
            return;
        processingRequest = null;
    }
}
