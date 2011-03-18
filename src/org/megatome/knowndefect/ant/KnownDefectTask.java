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
import org.megatome.knowndefect.ant.log.LoggerFactory;
import org.megatome.knowndefect.ant.log.LoggingContext;
import org.megatome.knowndefect.ant.log.LoggingContextClass;
import org.megatome.knowndefect.ant.log.impl.AntLogger;
import org.megatome.knowndefect.ant.scan.AnnotationScanResults;
import org.megatome.knowndefect.ant.scan.AnnotationScanner;
import org.megatome.knowndefect.ant.util.XMLBuilder;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Ant task for find and reporting instances of KnownDefect and KnownAndAcceptedDefect
 */
public class KnownDefectTask extends Task {
    private String classDir;
    //private String output = "xml";
    private String outputFile;

    public void setClassDir(String classDir) {
        this.classDir = classDir;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void execute() throws BuildException {
        System.out.println("Executing KnownDefectTask for location " + classDir);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(AntLogger.ANT_PROJECT_REF, this.getProject());
        LoggerFactory.setDefaultContext(new LoggingContext(LoggingContextClass.ANT_LOGGER, props));
        if (null == classDir || classDir.isEmpty()) {
            throw new BuildException("classDir must be specified");
        }
        if (null == outputFile || outputFile.isEmpty()) {
            throw new BuildException("outputFile must be specified");
        }
        try {
            final AnnotationScanResults foundAnnos = AnnotationScanner.findAnnotationsInPath(classDir);
            log("Found " + foundAnnos.getKnownDefectResults().size() + " @KnownDefect annotations");
            log("Found " + foundAnnos.getKnownAcceptedDefectResults().size() + " @KnownAndAcceptedDefect annotations");
            saveFoundAnnotations(foundAnnos);
        } catch (Exception e) {
            throw new BuildException("Failed to find KnownDefect annotations", e);
        }
    }

    private void saveFoundAnnotations(final AnnotationScanResults results) throws Exception {
        final String xml = XMLBuilder.convertToXML(results);
        final Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        try {
            out.write(xml);
        } finally {
            out.close();
        }
    }
}
