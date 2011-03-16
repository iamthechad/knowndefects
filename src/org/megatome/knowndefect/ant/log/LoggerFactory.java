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

import org.megatome.knowndefect.ant.log.impl.StandardOutLogger;

public class LoggerFactory {
    private LoggerFactory() {}

    private static LoggingContext defaultContext = LoggingContext.STDOUT_CONTEXT;

    public static void setDefaultContext(final LoggingContext context) {
        defaultContext = context;
    }

    public static Logger getLogger() {
        try {
            Class c = Class.forName(defaultContext.getContextClass().getClassName());
            Logger l = (Logger)c.newInstance();
            l.setProperties(defaultContext.getProperties());
            return l;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StandardOutLogger();
    }
}
