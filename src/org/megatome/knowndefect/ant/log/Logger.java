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

import java.util.Map;

/**
 * Interface for logging. The log levels correspond to the Ant log levels, with debug being more verbose
 * than the verbose level.
 */
public interface Logger {
    /**
     * Set any properties that the logger may need to initialize correctly.
     * @param properties Properties
     */
    public void setProperties(Map<String, Object> properties);

    /**
     * Log a message at info level.
     * @param message Message
     */
    public void info(String message);

    /**
     * Log a message at verbose level.
     * @param message Message
     */
    public void verbose(String message);

    /**
     * Log a message at debug level.
     * @param message Message
     */
    public void debug(String message);

    /**
     * Log a message at info level.
     * @param message Message
     * @param t Throwable to capture in the log
     */
    public void info(String message, Throwable t);

    /**
     * Log a message at info level.
     * @param message Message
     * @param t Throwable to capture in the log
     */
    public void verbose(String message, Throwable t);

    /**
     * Log a message at info level.
     * @param message Message
     * @param t Throwable to capture in the log
     */
    public void debug(String message, Throwable t);
}
