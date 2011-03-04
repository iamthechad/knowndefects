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
import org.megatome.knowndefect.ant.util.AnnotationScanException;
import org.megatome.knowndefect.ant.util.AnnotationScanner;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

public class KnownDefectTask extends Task {
    private String classDir;
    private String output = "xml";
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
        if (null == classDir || classDir.isEmpty()) {
            throw new BuildException("classDir must be specified");
        }
        if (null == outputFile || outputFile.isEmpty()) {
            throw new BuildException("outputFile must be specified");
        }
        try {
            final Map<String, Set<AnnotationInformation>> foundAnnos = AnnotationScanner.findAnnotationsInPath(classDir);
            saveFoundAnnotations(foundAnnos);
        } catch (AnnotationScanException e) {
            throw new BuildException("Failed to find KnownDefect annotations", e);
        }
    }

    private void saveFoundAnnotations(final Map<String, Set<AnnotationInformation>> annos) {
        final File f = new File(outputFile);

    }
}
