/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package java.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.blockwithme.util.shared.SystemUtils;

/** Executes tasks in the future on the main loop thread.
 * @author Nathan Sweet */
public class Timer {
    static final int CANCELLED = -1;
    static final int FOREVER = -2;

    private static int nextID = 0;

    private final ArrayList<TimerTask> tasks = new ArrayList();
    private boolean stopped, posted;
    private String name;

    private final Runnable timerRunnable = new Runnable() {
        public void run() {
            update();
        }
    };

    public Timer() {
        this("Timer-" + (++nextID), false);
    }

    public Timer(String name) {
        this(name, false);
    }

    public Timer(boolean isDaemon) {
        this("Timer-" + (++nextID), isDaemon);
    }

    public Timer(String name, /* ignored*/boolean isDaemon) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    public String toString() {
        return name;
    }

    /** Schedules a task to occur once after the specified delay and then a number of additional times at the specified interval. */
    private void sched(TimerTask task, float delaySeconds,
            float intervalSeconds, int repeatCount) {
        if (stopped) {
            throw new IllegalStateException("Timer cancelled");
        }
        if (task.repeatCount != CANCELLED)
            throw new IllegalArgumentException(
                    "The same task may not be scheduled twice.");
        task.delaySeconds = delaySeconds;
        task.intervalSeconds = intervalSeconds;
        task.repeatCount = repeatCount;
        tasks.add(task);
        postRunnable();
    }

    /**
     * Schedule a task for single execution. If {@code when} is less than the
     * current time, it will be scheduled to be executed as soon as possible.
     *
     * @param task
     *            the task to schedule.
     * @param when
     *            time of execution.
     * @throws IllegalArgumentException
     *                if {@code when.getTime() < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void schedule(TimerTask task, Date when) {
        long delay = when.getTime() - System.currentTimeMillis();
        if (delay < 0) {
            delay = 0;
        }
        schedule(task, delay);
    }

    /**
     * Schedule a task for single execution after a specified delay.
     *
     * @param task
     *            the task to schedule.
     * @param delay
     *            amount of time in milliseconds before execution.
     * @throws IllegalArgumentException
     *                if {@code delay < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void schedule(TimerTask task, long delay) {
        if (delay < 0) {
            throw new IllegalArgumentException();
        }
        sched(task, delay/1000.0f, 0, 0);
    }

    /**
     * Schedule a task for repeated fixed-delay execution after a specific delay.
     *
     * @param task
     *            the task to schedule.
     * @param delay
     *            amount of time in milliseconds before first execution.
     * @param period
     *            amount of time in milliseconds between subsequent executions.
     * @throws IllegalArgumentException
     *                if {@code delay < 0} or {@code period < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void schedule(TimerTask task, long delay, long period) {
        if (delay < 0 || period <= 0) {
            throw new IllegalArgumentException();
        }
        sched(task, delay/1000.0f, period/1000.0f, FOREVER);
    }

    /**
     * Schedule a task for repeated fixed-delay execution after a specific time
     * has been reached.
     *
     * @param task
     *            the task to schedule.
     * @param when
     *            time of first execution.
     * @param period
     *            amount of time in milliseconds between subsequent executions.
     * @throws IllegalArgumentException
     *                if {@code when.getTime() < 0} or {@code period < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void schedule(TimerTask task, Date when, long period) {
        long delay = when.getTime() - System.currentTimeMillis();
        if (delay < 0) {
            delay = 0;
        }
        schedule(task, delay, period);
    }

    /**
     * Schedule a task for repeated fixed-rate execution after a specific delay
     * has passed.
     *
     * @param task
     *            the task to schedule.
     * @param delay
     *            amount of time in milliseconds before first execution.
     * @param period
     *            amount of time in milliseconds between subsequent executions.
     * @throws IllegalArgumentException
     *                if {@code delay < 0} or {@code period < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (delay < 0 || period <= 0) {
            throw new IllegalArgumentException();
        }
        sched(task, delay/1000.0f, period/1000.0f, FOREVER);
    }

    /**
     * Schedule a task for repeated fixed-rate execution after a specific time
     * has been reached.
     *
     * @param task
     *            the task to schedule.
     * @param when
     *            time of first execution.
     * @param period
     *            amount of time in milliseconds between subsequent executions.
     * @throws IllegalArgumentException
     *                if {@code when.getTime() < 0} or {@code period < 0}.
     * @throws IllegalStateException
     *                if the {@code Timer} has been canceled, or if the task has been
     *                scheduled or canceled.
     */
    public void scheduleAtFixedRate(TimerTask task, Date when, long period) {
        long delay = when.getTime() - System.currentTimeMillis();
        if (delay < 0) {
            delay = 0;
        }
        scheduleAtFixedRate(task, delay, period);
    }

    /** Stops the timer, tasks will not be executed and time that passes will not be applied to the task delays. */
    public void cancel() {
        purge();
        stopped = true;
    }

    /** Cancels all tasks. */
    public int purge() {
        int result = tasks.size();
        for (int i = 0, n = result; i < n; i++)
            tasks.get(i).cancel();
        tasks.clear();
        return result;
    }

    private void postRunnable() {
        if (stopped || posted)
            return;
        posted = true;
        postRunnable(timerRunnable);
    }

    void update() {
        if (stopped) {
            posted = false;
            return;
        }

        float delta = SystemUtils.getDeltaTime();
        for (int i = 0, n = tasks.size(); i < n; i++) {
            TimerTask task = tasks.get(i);
            task.delaySeconds -= delta;
            if (task.delaySeconds > 0)
                continue;
            if (task.repeatCount != CANCELLED) {
                if (task.repeatCount == 0)
                    task.repeatCount = CANCELLED; // Set cancelled before run so it may be rescheduled in run.
                task.run();
            }
            if (task.repeatCount == CANCELLED) {
                tasks.remove(i);
                i--;
                n--;
            } else {
                task.delaySeconds = task.intervalSeconds;
                if (task.repeatCount > 0)
                    task.repeatCount--;
            }
        }

        if (tasks.isEmpty())
            posted = false;
        else
            postRunnable(timerRunnable);
    }

    /** Adds a Runnable to be executed at the *next application cycle(loop)*. */
    private void postRunnable(final Runnable runnable) {
        SystemUtils.postRunnable(runnable);
    }
}