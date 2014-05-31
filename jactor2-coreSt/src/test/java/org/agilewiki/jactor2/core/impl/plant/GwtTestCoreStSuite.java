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
package org.agilewiki.jactor2.core.impl.plant;

import junit.framework.TestSuite;

import org.agilewiki.jactor2.core.impl.blades.GwtTest1;
import org.agilewiki.jactor2.core.impl.blades.GwtTest3;
import org.agilewiki.jactor2.core.impl.blades.GwtTestISMTransactionTest;
import org.agilewiki.jactor2.core.impl.blades.GwtTestPubSubTest;
import org.agilewiki.jactor2.core.impl.reactors.GwtTestCloseableSetTest;
import org.agilewiki.jactor2.core.impl.reactors.GwtTestRegistrationTest;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * @author monster
 *
 */
public class GwtTestCoreStSuite extends GWTTestSuite {
    public static TestSuite suite() {
        final TestSuite suite = new GWTTestSuite("All the CoreSt GWT tests");
        suite.addTestSuite(GwtTestAsyncTest.class);
        suite.addTestSuite(GwtTestIsolationTest.class);
        suite.addTestSuite(GwtTestLogTest.class);
        suite.addTestSuite(GwtTestPlantTest.class);
        suite.addTestSuite(GwtTestSyncTest.class);
        suite.addTestSuite(GwtTestISMTransactionTest.class);
        suite.addTestSuite(GwtTestPubSubTest.class);
        suite.addTestSuite(GwtTest1.class);
        suite.addTestSuite(GwtTest3.class);
        suite.addTestSuite(org.agilewiki.jactor2.core.impl.blades.transactions.GwtTestSyncTest.class);
        suite.addTestSuite(GwtTestCloseableSetTest.class);
        suite.addTestSuite(GwtTestRegistrationTest.class);
        suite.addTestSuite(org.agilewiki.jactor2.core.impl.requests.GwtTest1.class);
        suite.addTestSuite(org.agilewiki.jactor2.core.impl.requests.GwtTest3.class);
        suite.addTestSuite(GwtTestHungRequestTest.class);

        // Infinite loop!
//        suite.addTestSuite(GwtTestICloseTest.class);

        // Infinite loop!
//        suite.addTestSuite(org.agilewiki.jactor2.core.impl.blades.transactions.GwtTestAsyncTest.class);
        return suite;
    }
}
