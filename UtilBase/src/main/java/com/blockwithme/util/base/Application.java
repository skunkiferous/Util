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
package com.blockwithme.util.base;

/**
 * Application represents functionality that the "application"
 * (game?) should offer.
 *
 * @author monster
 */
public interface Application extends Runnable {
    /** Posts a {@link Runnable} on the main loop thread.
     *
     * @param runnable the runnable. */
    void postRunnable(Runnable runnable);

    /** @return the time span between the current frame and the last frame in seconds. */
    float getDeltaTime();
}
