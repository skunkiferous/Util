package org.agilewiki.jactor2.core.impl.stPlant;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.closeable.Closeable;
import org.agilewiki.jactor2.core.closeable.CloseableImpl;
import org.agilewiki.jactor2.core.impl.DefaultPlantConfiguration;
import org.agilewiki.jactor2.core.impl.stCloseable.CloseableStImpl;
import org.agilewiki.jactor2.core.impl.stReactors.IsolationReactorStImpl;
import org.agilewiki.jactor2.core.impl.stReactors.NonBlockingReactorStImpl;
import org.agilewiki.jactor2.core.impl.stRequests.AsyncRequestStImpl;
import org.agilewiki.jactor2.core.impl.stRequests.SyncRequestStImpl;
import org.agilewiki.jactor2.core.plant.PlantImpl;
import org.agilewiki.jactor2.core.plant.PlantScheduler;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.PoolThreadReactorImpl;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.reactors.ReactorImpl;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncRequestImpl;
import org.agilewiki.jactor2.core.requests.RequestImpl;
import org.agilewiki.jactor2.core.requests.SyncRequest;

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
            final NonBlockingReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        return new NonBlockingReactorStImpl(_parentReactor);
    }

    @Override
    public ReactorImpl createBlockingReactorImpl(
            final NonBlockingReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReactorImpl createIsolationReactorImpl(
            final NonBlockingReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        return new IsolationReactorStImpl(_parentReactor);
    }

    @Override
    public ReactorImpl createSwingBoundReactorImpl(
            final NonBlockingReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReactorImpl createThreadBoundReactorImpl(
            final NonBlockingReactor _parentReactor,
            final int _initialOutboxSize, final int _initialLocalQueueSize,
            final Runnable _boundProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <RESPONSE_TYPE> RequestImpl<RESPONSE_TYPE> createSyncRequestImpl(
            final SyncRequest<RESPONSE_TYPE> _syncRequest,
            final Reactor _targetReactor) {
        return new SyncRequestStImpl<RESPONSE_TYPE>(_syncRequest,
                _targetReactor);
    }

    @Override
    public <RESPONSE_TYPE> AsyncRequestImpl<RESPONSE_TYPE> createAsyncRequestImpl(
            final AsyncRequest<RESPONSE_TYPE> _asyncRequest,
            final Reactor _targetReactor) {
        return new AsyncRequestStImpl<RESPONSE_TYPE>(_asyncRequest,
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

    @Override
    public <V> ISMap<V> createISMap() {
        return ISMapImpl.empty();
    }

    @Override
    public <V> ISMap<V> createISMap(final String key, final V value) {
        return ISMapImpl.singleton(key, value);
    }

    @Override
    public <V> ISMap<V> createISMap(final Map<String, V> m) {
        return ISMapImpl.from(m);
    }
}
