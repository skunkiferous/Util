package org.agilewiki.jactor2.core.impl.stPlant;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.agilewiki.jactor2.core.closeable.Closeable;
import org.agilewiki.jactor2.core.closeable.impl.CloseableImpl;
import org.agilewiki.jactor2.core.impl.DefaultPlantConfiguration;
import org.agilewiki.jactor2.core.impl.stCloseable.CloseableStImpl;
import org.agilewiki.jactor2.core.impl.stReactors.IsolationReactorStImpl;
import org.agilewiki.jactor2.core.impl.stReactors.NonBlockingReactorStImpl;
import org.agilewiki.jactor2.core.impl.stRequests.AsyncRequestStImpl;
import org.agilewiki.jactor2.core.impl.stRequests.AsyncRequestStImplWithData;
import org.agilewiki.jactor2.core.impl.stRequests.SyncRequestStImpl;
import org.agilewiki.jactor2.core.impl.stRequests.SyncRequestStImplWithData;
import org.agilewiki.jactor2.core.plant.PlantScheduler;
import org.agilewiki.jactor2.core.plant.impl.PlantImpl;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.reactors.impl.PoolThreadReactorImpl;
import org.agilewiki.jactor2.core.reactors.impl.ReactorImpl;
import org.agilewiki.jactor2.core.requests.AsyncOperation;
import org.agilewiki.jactor2.core.requests.AsyncRequestImplWithData;
import org.agilewiki.jactor2.core.requests.SyncOperation;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;
import org.agilewiki.jactor2.core.requests.impl.RequestImplWithData;

public class PlantStImpl extends PlantImpl {

    /**
     * Returns this singleton.
     *
     * @return This singleton.
     */
    public static PlantStImpl getSingleton() {
        return (PlantStImpl) PlantImpl.getSingleton();
    }

    /**
     * System property flag, jactor.debug, to turn on debug;
     */
    public static final boolean DEBUG = "true".equals(System
            .getProperty("jactor.debug"));

    private final PlantConfiguration plantConfiguration;

    private final Facility internalFacility;

    public ReactorImpl currentReactorImpl;

    private final Queue<PoolThreadReactorImpl> pendingReactors = new LinkedBlockingQueue<PoolThreadReactorImpl>();

    /**
     * Create the singleton with the given configuration.
     *
     * @param _plantConfiguration The configuration to be used by the singleton.
     */
    public PlantStImpl(final PlantConfiguration _plantConfiguration)
            throws Exception {
        if (DEBUG) {
            System.out.println("\n*** jactor.debug = true ***\n");
        }
        plantConfiguration = _plantConfiguration;
        internalFacility = createInternalFacility();
    }

    /**
     * @throws Exception
     *
     */
    public PlantStImpl() throws Exception {
        this(new DefaultPlantConfiguration());
    }

    @Override
    public ReactorImpl getCurrentReactorImpl() {
        return currentReactorImpl;
    }

    @Override
    public ReactorImpl createNonBlockingReactorImpl(
            final IsolationReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        return new NonBlockingReactorStImpl(_parentReactor);
    }

    @Override
    public ReactorImpl createBlockingReactorImpl(
            final IsolationReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReactorImpl createIsolationReactorImpl(
            final IsolationReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        return new IsolationReactorStImpl(_parentReactor);
    }

    @Override
    public ReactorImpl createSwingBoundReactorImpl(
            final IsolationReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReactorImpl createThreadBoundReactorImpl(
            final IsolationReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize,
            final Runnable _boundProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <RESPONSE_TYPE> RequestImpl<RESPONSE_TYPE> createSyncRequestImpl(
            final SyncOperation<RESPONSE_TYPE> _syncOperation,
            final Reactor _targetReactor) {
        return new SyncRequestStImpl<RESPONSE_TYPE>(_syncOperation,
                _targetReactor);
    }

    @Override
    public <RESPONSE_TYPE> AsyncRequestImpl<RESPONSE_TYPE> createAsyncRequestImpl(
            final AsyncOperation<RESPONSE_TYPE> _asyncOperation,
            final Reactor _targetReactor) {
        return new AsyncRequestStImpl<RESPONSE_TYPE>(_asyncOperation,
                _targetReactor);
    }

    @Override
    public <RESPONSE_TYPE> RequestImplWithData<RESPONSE_TYPE> createSyncRequestImplWithData(
            final SyncOperation<RESPONSE_TYPE> _syncOperation,
            final Reactor _targetReactor) {
        return new SyncRequestStImplWithData<RESPONSE_TYPE>(_syncOperation,
                _targetReactor);
    }

    @Override
    public <RESPONSE_TYPE> AsyncRequestImplWithData<RESPONSE_TYPE> createAsyncRequestImplWithData(
            final AsyncOperation<RESPONSE_TYPE> _asyncOperation,
            final Reactor _targetReactor) {
        return new AsyncRequestStImplWithData<RESPONSE_TYPE>(_asyncOperation,
                _targetReactor);
    }

    @Override
    public CloseableImpl createCloseableImpl(final Closeable _closeable) {
        return new CloseableStImpl(_closeable);
    }

    /**
     * Close the Plant.
     */
    @Override
    public void close() throws Exception {
        if (getSingleton() == null) {
            return;
        }
        try {
            getInternalFacility().close();
        } finally {
            final PlantScheduler plantScheduler = getPlantScheduler();
            if (plantScheduler != null) {
                plantScheduler.close();
            }
            super.close();
        }
    }

    /**
     * Returns the Plant's configuration.
     *
     * @return The singleton's configuration.
     */
    public PlantConfiguration getPlantConfiguration() {
        return plantConfiguration;
    }

    /**
     * Return the scheduler that is a part of the Plant's configuration.
     *
     * @return The scheduler.
     */
    @Override
    public PlantScheduler getPlantScheduler() {
        return plantConfiguration.getPlantScheduler();
    }

    /**
     * Create the Plant's internal reactor.
     *
     * @return The reactor belonging to the singleton.
     */
    protected Facility createInternalFacility() throws Exception {
        return new Facility(PLANT_INTERNAL_FACILITY_NAME, null);
    }

    /**
     * Returns the Plant's internal reactor.
     *
     * @return The reactor belonging to the singleton.
     */
    @Override
    public Facility getInternalFacility() {
        return internalFacility;
    }

    /**
     * Returns 16.
     *
     * @return The reactor default initial local message queue size.
     */
    @Override
    public int getInitialLocalMessageQueueSize() {
        return 0;
    }

    /**
     * Returns 16.
     *
     * @return The reactor default initial buffer size.
     */
    @Override
    public int getInitialBufferSize() {
        return 0;
    }

    /**
     * Submit a Reactor for subsequent execution.
     *
     * @param _reactor The targetReactor to be run.
     */
    public final void submit(final PoolThreadReactorImpl _reactor) {
        pendingReactors.add(_reactor);
    }

    /**
     * Process messages until there are no more.
     */
    public void processMessages() {
        while (true) {
            currentReactorImpl = pendingReactors.poll();
            if (currentReactorImpl == null) {
                return;
            }
            try {
                currentReactorImpl.run();
            } finally {
                currentReactorImpl = null;
            }
        }
    }
}
