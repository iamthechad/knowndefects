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

import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AnnotationScanner {
    //private static final AnnotationDB db = new AnnotationDB();
    private static final List<String> ignoredPackages = new ArrayList<String>(Arrays.asList("javax", "java", "sun", "com.sun", "javassist"));

    public static Map<String, Set<String>> findAnnotationsInPath(final String basePath) throws AnnotationScanException {
        if ((null == basePath) || (basePath.isEmpty()) || ".".equals(basePath)) {
            throw new IllegalArgumentException("Base path cannot be null");
        }
        final URL classPath = findResourceBase(basePath);
        /*for (final String str : db.getIgnoredPackages()) {
            System.out.println(str);
        }
        System.out.println("URL: " + classPath.toString());*/
        try {
            scanArchives(classPath);
        } catch (IOException e) {
            throw new AnnotationScanException("Error scanning for annotations", e);
        }

        return db.getAnnotationIndex();
    }

    private static URL findResourceBase(final String path) throws AnnotationScanException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL url = loader.getResource(path);
        String urlString = url.toString();
        int idx = urlString.lastIndexOf(path);
        urlString = urlString.substring(0, idx);
        URL deployUrl = null;
        try {
            deployUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new AnnotationScanException("URL was somehow corrupt");
        }
        return deployUrl;
    }

    private static void scanArchives(URL... urls) {
        for (URL url : urls) {
            Filter filter = new Filter() {
                public boolean accepts(String filename) {
                    if (filename.endsWith(".class")) {
                        if (filename.startsWith("/")) filename = filename.substring(1);
                        if (!AnnotationDB.this.ignoreScan(filename.replace('/', '.'))) return true;
                    }

                    return false;
                }
            };
            StreamIterator it = IteratorFactory.create(url, filter);
            InputStream stream;
            while ((stream = it.next()) != null) scanClass(stream);
        }
    }

    private boolean ignoreScan(String intf) {
        for (String ignored : this.ignoredPackages) {
            if (intf.startsWith(ignored + ".")) {
                return true;
            }

        }

        return false;
    }
}
