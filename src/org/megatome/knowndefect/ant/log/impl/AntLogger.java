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

import org.apache.tools.ant.Project;
import org.megatome.knowndefect.ant.log.Logger;

import java.util.Map;

/**
 * Logger that uses Ant's logging facilities.
 */
public class AntLogger implements Logger {
    public static final String ANT_PROJECT_REF = "apr";
    private Project project;

    public void setProperties(Map<String, Object> properties) {
        this.project = (Project)properties.get(ANT_PROJECT_REF);
    }

    public void info(String message) {
        project.log(message);
    }

    public void verbose(String message) {
        project.log(message, Project.MSG_VERBOSE);
    }

    public void debug(String message) {
        project.log(message, Project.MSG_DEBUG);
    }

    public void info(String message, Throwable t) {
        project.log(message + " " + t.getLocalizedMessage());
    }

    public void verbose(String message, Throwable t) {
        project.log(message + " " + t.getLocalizedMessage(), Project.MSG_VERBOSE);
    }

    public void debug(String message, Throwable t) {
        project.log(message + " " + t.getLocalizedMessage(), Project.MSG_DEBUG);
    }
}
