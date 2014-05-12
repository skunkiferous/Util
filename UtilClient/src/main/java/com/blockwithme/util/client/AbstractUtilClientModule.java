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
package com.blockwithme.util.client;

import java.util.Random;
import java.util.Timer;

import com.blockwithme.util.base.Application;
import com.blockwithme.util.base.SystemUtils;
import com.blockwithme.util.base.TimeSource;
import com.blockwithme.util.shared.DefaultApplication;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Guice module for Util-Server.
 *
 * WARNING: This code is ONLY EXECUTED IN THE COMPILER.
 *
 * In other words, the GIN plugin just checks which object get injected where,
 * but stuff like constructor, or other code in configure(), will NOT get
 * executed later when the client actually runs.
 *
 * @author monster
 */
public abstract class AbstractUtilClientModule extends AbstractGinModule {

    /** GWTTimeSource skips the double-to-long-to-double conversion. */
    private static final class GWTTimeSource implements TimeSource {
        /* (non-Javadoc)
         * @see com.blockwithme.util.base.TimeSource#currentTimeMillis()
         */
        @Override
        public native double currentTimeMillis() /*-{
			return (new Date()).getTime();
        }-*/;
    }

    /** The Application */
    private final Application app;

    /** Constructor */
    protected AbstractUtilClientModule(final Application app) {
        this.app = app;
    }

    /** Constructor */
    public AbstractUtilClientModule() {
        this(new DefaultApplication());
    }

    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
//        install(new GinFactoryModuleBuilder().build(AssistedInjectionFactory.class));

        new UtilEntryPoint().onModuleLoad();
        requestStaticInjection(SystemUtils.class);
    }

    /** Creates the Application. */
    @Provides
    @Singleton
    final Application provideApplication() {
        return app;
    }

    /** Creates the Timer. */
    @Provides
    @Singleton
    final Timer provideTimer() {
        return new Timer("System-Timer");
    }

    /** Creates the TimeSource. */
    @Provides
    @Singleton
    final TimeSource provideTimeSource() {
        return new GWTTimeSource();
    }

    /** Creates the SystemUtils. */
    @Provides
    @Singleton
    final SystemUtils provideSystemUtils() {
        return new GWTSystemUtilsImpl();
    }

    /** Creates the Random. */
    @Provides
    @Singleton
    final Random provideRandom() {
        return new Random();
    }
}
