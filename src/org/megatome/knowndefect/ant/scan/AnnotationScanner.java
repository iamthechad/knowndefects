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
/****************************************************************
 * Code in this class borrowed and adapted from the Scannotation library
 * available from http://scannotation.sourceforge.net/
 ****************************************************************/
package org.megatome.knowndefect.ant.scan;


import static org.megatome.knowndefect.Constants.*;

import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import org.megatome.knowndefect.ant.info.AnnotationInformation;
import org.megatome.knowndefect.ant.info.AnnotationInformationFactory;
import org.megatome.knowndefect.ant.log.Logger;
import org.megatome.knowndefect.ant.log.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsible for scanning archives to find the KD annotations.
 * <p>Code in this class borrowed and adapted from the Scannotation library
 * available from <a href="http://scannotation.sourceforge.net/">http://scannotation.sourceforge.net/</a></p>
 */
public class AnnotationScanner {
    private static final Pattern QUOTE_PATTERN = Pattern.compile("^\"(.*)\"$", Pattern.DOTALL);
    private static final AnnotationScanResults results = new AnnotationScanResults();
    private static final List<String> ignoredPackages = new ArrayList<String>(Arrays.asList("javax", "java", "sun", "com.sun", "javassist"));
    private static final Set<String> classTypes = new HashSet<String>(Arrays.asList(KNOWN_DEFECT_ANNOTATION_CLASS, KNOWN_ACCEPTED_DEFECT_ANNOTATION_CLASS));

    private static final Logger log = LoggerFactory.getLogger();

    private AnnotationScanner() {}

    /**
     * Scan the classes found at the base path for the KD annotations. Recurses through subdirectories.
     * @param basePath Path to begin the scan at
     * @return Object containing all found annotation results
     * @throws AnnotationScanException If an error occurs
     */
    public static AnnotationScanResults findAnnotationsInPath(final String basePath) throws AnnotationScanException {
        if ((null == basePath) || (basePath.isEmpty())) {
            throw new IllegalArgumentException("Base path cannot be null");
        }
        try {
            scanArchives(new File(basePath));
        } catch (IOException e) {
            throw new AnnotationScanException("Error scanning for annotations", e);
        }

        return results;
    }

    private static void scanArchives(final File f) throws IOException, AnnotationScanException {
        final Filter filter = new Filter() {
            public boolean accepts(String filename) {
                if (filename.endsWith(".class")) {
                    if (filename.startsWith("/")) filename = filename.substring(1);
                    if (!ignoreScan(filename.replace('/', '.'))) {
                        log.debug(filename + " will be included in scan");
                        return true;
                    }
                }

                return false;
            }
        };
        final StreamIterator it = new FileIterator(f, filter);
        InputStream stream;
        while ((stream = it.next()) != null) scanClass(stream);
    }

    private static boolean ignoreScan(String intf) {
        for (final String ignored : ignoredPackages) {
            if (intf.startsWith(ignored + ".")) {
                log.debug("Ignoring package " + intf);
                return true;
            }
        }
        log.debug("Package " + intf + " not ignored");
        return false;
    }

    private static void scanClass(InputStream bits)
            throws IOException {
        final DataInputStream dstream = new DataInputStream(new BufferedInputStream(bits));
        try {
            final ClassFile cf = new ClassFile(dstream);
            scanMethods(cf);
        } finally {
            dstream.close();
            bits.close();
        }
    }

    private static void scanMethods(ClassFile cf) {
        log.verbose("Scanning " + cf.getName());
        final List methods = cf.getMethods();
        if (methods == null) {
            log.verbose("Did not find any methods in " + cf.getName());
            return;
        }
        for (final Object obj : methods) {
            final MethodInfo method = (MethodInfo) obj;
            log.debug("Looking at method " + method.getName());

            final AnnotationsAttribute visible = (AnnotationsAttribute) method.getAttribute("RuntimeVisibleAnnotations");
            final AnnotationsAttribute invisible = (AnnotationsAttribute) method.getAttribute("RuntimeInvisibleAnnotations");

            if (visible != null) populate(visible.getAnnotations(), method.getName(), method.getLineNumber(0), cf.getName());
            if (invisible != null) populate(invisible.getAnnotations(), method.getName(), method.getLineNumber(0), cf.getName());
        }

    }

    private static void populate(Annotation[] annotations, String methodName, int lineNumber, String className) {
        if (annotations == null) return;
        for (final Annotation ann : annotations) {
            final String annotationClass = ann.getTypeName();
            if (classTypes.contains(annotationClass)) {
                log.verbose("Found " + annotationClass + " in " + className + "." + methodName);
                final AnnotationInformation info = AnnotationInformationFactory.createInformation(annotationClass);
                info.setClassName(className);
                info.setMethodName(methodName);
                info.setLineNumber(lineNumber);
                final Set memberNames = ann.getMemberNames();
                if (null != memberNames) {
                    for (final Object obj : memberNames) {
                        final String mName = (String)obj;
                        String value = ann.getMemberValue(mName).toString();
                        final Matcher m = QUOTE_PATTERN.matcher(value);
                        if (m.matches()) {
                            value = m.group(1);
                        }
                        info.setMethodValue(mName, value);
                    }
                }
                results.addResult(className, info);
            }
        }
    }
}
