/*******************************************************************************
 * Copyright (c) 2011 Megatome Technologies
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
 ******************************************************************************/

package org.megatome.knowndefect.ant.log;

import java.util.HashMap;
import java.util.Map;

public class LoggingContext {
    private final LoggingContextClass contextClass;
    private final Map<String, Object> properties;

    public LoggingContext(LoggingContextClass contextClass, Map<String, Object> properties) {
        this.contextClass = contextClass;
        this.properties = (properties == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(properties);
    }

    public LoggingContextClass getContextClass() {
        return contextClass;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public static final LoggingContext STDOUT_CONTEXT = new LoggingContext(LoggingContextClass.STDOUT_LOGGER, null);
    public static final LoggingContext NULL_CONTEXT = new LoggingContext(LoggingContextClass.NULL_LOGGER, null);
}
