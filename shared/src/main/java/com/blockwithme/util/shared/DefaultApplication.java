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
package com.blockwithme.util.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.blockwithme.util.base.Application;
import com.blockwithme.util.base.SystemUtils;

/**
 * The default Application implementation.
 * It can be used outside of Timer too.
 *
 * TODO Make the Runnables async and faster.
 *
 * @author monster
 */
public class DefaultApplication extends TimerTask implements Application {

    /** All the Runnables to call on the next loop. */
    private final List<Runnable> runnables = new ArrayList<>();

    /** Last loop time. */
    private double lastLoop = System.currentTimeMillis();

    /** Last delta duration. */
    private float lastDelta;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.Application#postRunnable(java.lang.Runnable)
     */
    @Override
    public synchronized void postRunnable(final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable");
        }
        runnables.add(runnable);
    }

    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public synchronized void run() {
        final double now = SystemUtils.updateCurrentTimeMillis();
        lastDelta = (float) ((now - lastLoop) / 1000.0);
        lastLoop = now;
        if (!runnables.isEmpty()) {
            final Runnable[] array = runnables.toArray(new Runnable[runnables
                    .size()]);
            runnables.clear();
            for (final Runnable runnable : array) {
                runnable.run();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.Application#getDeltaTime()
     */
    @Override
    public synchronized float getDeltaTime() {
        return lastDelta;
    }
}