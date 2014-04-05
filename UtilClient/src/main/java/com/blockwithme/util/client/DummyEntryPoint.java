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

import java.text.SimpleDateFormat;

import com.google.gwt.core.client.EntryPoint;

/**
 * Dummy EntryPoint, to force compilation.
 *
 * @author monster
 */
public class DummyEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
        // Should produce a GWT compile error, if we got something wrong with super-source
        System.out.println(SimpleDateFormat.class.getSimpleName());
    }
}
