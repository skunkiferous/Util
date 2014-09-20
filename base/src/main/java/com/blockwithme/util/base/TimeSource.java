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
 * Returns the current time. It is acceptable for an instance of this type to
 * "take a while" to return the time, as the value will be cached.
 *
 * The reason to define this interface is, that many "clients" have a totally
 * wrong system time, and using this method allows the application to "correct"
 * the bad host system time.
 *
 * The lesser reason is that a call to System.currentTimeMillis() itself takes
 * a while, because it is a native call. So we replace this call with a field
 * which only represent the approximate time, but which is cheap to read.
 *
 * The reason why we use double instead of long is that long "causes issues"
 * within GWT. OTOH, converting long to double and back is cheap in the JVM,
 * so it should not cause any significant issues in Java.
 *
 * Beware that in some case, multiple concurrent calls to this method are
 * possible.
 *
 * @author monster
 */
public interface TimeSource {
    /**
     * Returns the current time, in milliseconds.
     */
    double currentTimeMillis();
}
