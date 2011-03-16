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

package org.megatome.knowndefect.ant.log.impl;

import org.megatome.knowndefect.ant.log.Logger;

import java.util.Map;

public class StandardOutLogger implements Logger {

    final private static String DEBUG = "DEBUG: ";
    final private static String INFO = "INFO: ";
    final private static String VERBOSE = "VERBOSE: ";

    public void info(String message) {
        System.out.println(INFO + message);
    }

    public void verbose(String message) {
        System.out.println(VERBOSE + message);
    }

    public void debug(String message) {
        System.out.println(DEBUG + message);
    }

    public void info(String message, Throwable t) {
        System.out.println(INFO + message);
         if (t != null) {
            t.printStackTrace();
         }
    }

    public void verbose(String message, Throwable t) {
        System.out.println(VERBOSE + message);
         if (t != null) {
            t.printStackTrace();
         }
    }

    public void debug(String message, Throwable t) {
        System.out.println(DEBUG + message);
         if (t != null) {
            t.printStackTrace();
         }
    }

    public void setProperties(Map<String, Object> properties) {}
}
