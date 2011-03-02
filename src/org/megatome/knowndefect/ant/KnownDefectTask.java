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

package org.megatome.knowndefect.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class KnownDefectTask extends Task {
    private String classDir;

    public void setClassDir(String classDir) {
        this.classDir = classDir;
    }

    @Override
    public void execute() throws BuildException {
        System.out.println("Executing KnownDefectTask for location " + classDir);
        final URL classPath = ClasspathUrlFinder.findResourceBase(classDir);
        AnnotationDB db = new AnnotationDB();
        try {
            db.scanArchives(classPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BuildException("Error scanning classpath " + e.getMessage());
        }
        Map<String, Set<String>> map = db.getAnnotationIndex();
        for (final Map.Entry<String, Set<String>> entry : map.entrySet()) {
            System.out.println("Looking at: " + entry.getKey());
            for (final String value : entry.getValue()) {
                System.out.println("Value: " + value);
            }
        }
    }
}
