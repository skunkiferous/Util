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
package com.blockwithme.util.client.test;

import java.util.Timer;
import java.util.TimerTask;

import com.blockwithme.util.shared.SystemUtils;

/**
 * Tests GwtTestTimerTest.
 *
 * @author monster
 */
public class GwtTestTimerTest extends BaseGWTTestCase {
    public void testScheduleLong() {
        final Timer timer = SystemUtils.getTimer();
        assertNotNull("getTimer()", timer);
        final boolean[] marker = new boolean[] { false };
        delayTestFinish(1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("testScheduleLong() task executed");
                marker[0] = true;
                // We don't need to assertTrue("marker[0]", marker[0]); we know it worked.
                finishTest();
            }
        }, 10);
//
//        new com.google.gwt.user.client.Timer() {
//            @Override
//            public void run() {
//                marker[0] = true;
//            }
//        }.schedule(10);

        assertFalse("marker[0]", marker[0]);
    }

    public void testScheduleLongLong() {
        // TODO Timer.schedule(long,long)
    }

    public void testScheduleDate() {
        // TODO Timer.schedule(Date)
    }

    public void testScheduleDateLong() {
        // TODO Timer.schedule(Date,long)
    }

    public void testScheduleAtFixedRateLongLong() {
        // TODO Timer.scheduleAtFixedRate(long,long)
    }

    public void testScheduleAtFixedRateDateLong() {
        // TODO Timer.scheduleAtFixedRate(Date,long)
    }
}
