/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;

/**
 * The {@code TimerTask} class represents a task to run at a specified time. The task
 * may be run once or repeatedly.
 *
 * @see Timer
 * @see java.lang.Object#wait(long)
 */
public abstract class TimerTask implements Runnable {
    float delaySeconds;
    float intervalSeconds;
    int repeatCount = Timer.CANCELLED;

    /** If this is the last time the task will be ran or the task is first cancelled, it may be scheduled again in this method. */
    abstract public void run();

    /** Cancels the task. It will not be executed until it is scheduled again. This method can be called at any time. */
    public boolean cancel() {
        delaySeconds = 0;
        boolean result = isScheduled();
        repeatCount = Timer.CANCELLED;
        return result;
    }

    /** Returns true if this task is scheduled to be executed in the future by a timer. */
    public boolean isScheduled() {
        return repeatCount != Timer.CANCELLED;
    }

    public long scheduledExecutionTime() {
        // Very approximate impl! Used only for copmateTo().
        long now = System.currentTimeMillis();
        if (repeatCount == Timer.FOREVER) {
            return now + (long) (intervalSeconds / 1000L) / 2;
        }
        return now + (long) (delaySeconds / 1000L) / 2;
    }
}
