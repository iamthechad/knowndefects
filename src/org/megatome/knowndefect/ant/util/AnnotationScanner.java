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

package org.megatome.knowndefect.ant.util;


import static org.megatome.knowndefect.Constants.*;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import org.megatome.knowndefect.ant.AnnotationInformation;
import org.megatome.knowndefect.ant.AnnotationInformationFactory;

import java.io.*;
import java.util.*;

public class AnnotationScanner {
    private static final Map<String, Set<AnnotationInformation>> classIndex = new HashMap<String, Set<AnnotationInformation>>();
    private static final Map<String, Set<AnnotationInformation>> annotationIndex = new HashMap<String, Set<AnnotationInformation>>();
    private static final List<String> ignoredPackages = new ArrayList<String>(Arrays.asList("javax", "java", "sun", "com.sun", "javassist"));
    private static final Set<String> classTypes = new HashSet<String>(Arrays.asList(KNOWN_DEFECT_ANNOTATION_CLASS, KNOWN_ACCEPTED_DEFECT_ANNOTATION_CLASS));

    public static Map<String, Set<AnnotationInformation>> findAnnotationsInPath(final String basePath) throws AnnotationScanException {
        if ((null == basePath) || (basePath.isEmpty())) {
            throw new IllegalArgumentException("Base path cannot be null");
        }
        try {
            scanArchives(new File(basePath));
        } catch (IOException e) {
            throw new AnnotationScanException("Error scanning for annotations", e);
        }

        return annotationIndex;
    }

    private static void scanArchives(File f) throws IOException {
        Filter filter = new Filter() {
            public boolean accepts(String filename) {
                if (filename.endsWith(".class")) {
                    if (filename.startsWith("/")) filename = filename.substring(1);
                    if (!ignoreScan(filename.replace('/', '.'))) return true;
                }

                return false;
            }
        };
        StreamIterator it = new FileIterator(f, filter);
        InputStream stream;
        while ((stream = it.next()) != null) scanClass(stream);
    }

    private static boolean ignoreScan(String intf) {
        for (String ignored : ignoredPackages) {
            if (intf.startsWith(ignored + ".")) {
                return true;
            }
        }
        return false;
    }

    private static void scanClass(InputStream bits)
            throws IOException {
        DataInputStream dstream = new DataInputStream(new BufferedInputStream(bits));
        try {
            final ClassFile cf = new ClassFile(dstream);
            classIndex.put(cf.getName(), new HashSet<AnnotationInformation>());
            scanMethods(cf);
        } finally {
            dstream.close();
            bits.close();
        }
    }

    private static void scanMethods(ClassFile cf) {
        final List methods = cf.getMethods();
        if (methods == null) return;
        for (final Object obj : methods) {
            final MethodInfo method = (MethodInfo) obj;
            final AnnotationsAttribute visible = (AnnotationsAttribute) method.getAttribute("RuntimeVisibleAnnotations");
            final AnnotationsAttribute invisible = (AnnotationsAttribute) method.getAttribute("RuntimeInvisibleAnnotations");

            if (visible != null) populate(visible.getAnnotations(), cf.getName());
            if (invisible != null) populate(invisible.getAnnotations(), cf.getName());
        }

    }

    private static void populate(Annotation[] annotations, String className) {
        if (annotations == null) return;
        //System.out.println("ClassName: " + className);
        //Set<AnnotationInformation> classAnnotations = classIndex.get(className);
        for (Annotation ann : annotations) {
            final String annotationClass = ann.getTypeName();
            if (classTypes.contains(annotationClass)) {
                final AnnotationInformation info = AnnotationInformationFactory.createInformation(annotationClass);
                info.setClassName(className);
                Set<AnnotationInformation> classes = annotationIndex.get(ann.getTypeName());
                Set memberNames = ann.getMemberNames();
                if (null != memberNames) {
                    for (Object obj : memberNames) {
                        String mName = (String)obj;
                        info.setMethodValue(mName, ann.getMemberValue(mName).toString());
                        //System.out.println("MName: " + mName + " -> " + ann.getMemberValue(mName));
                    }
                }
                if (classes == null) {
                    classes = new HashSet<AnnotationInformation>();
                    annotationIndex.put(ann.getTypeName(), classes);
                }
                classes.add(info);
            //classAnnotations.add(ann.getTypeName());
            }
        }
    }
}
