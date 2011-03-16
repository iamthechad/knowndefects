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

import org.megatome.knowndefect.ant.log.Logger;
import org.megatome.knowndefect.ant.log.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileIterator implements StreamIterator {
    private static final Logger log = LoggerFactory.getLogger();

    private List<File> files = new ArrayList<File>();
    private int index = 0;

    public FileIterator(File file, Filter filter) {
        try {
            create(this.files, file, filter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static void create(List<File> list, File dir, Filter filter) throws Exception {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                create(list, f, filter);
            } else {
                if ((filter != null) && (!filter.accepts(f.getAbsolutePath()))) {
                    log.debug("File not accepted by filter");
                    continue;
                }
                log.debug("Adding file to scan list");
                list.add(f);
            }
        }
    }

    public InputStream next() throws AnnotationScanException {
        if (this.index >= this.files.size()) return null;
        File fp = this.files.get(this.index++);
        try {
            return new FileInputStream(fp);
        } catch (FileNotFoundException e) {
            throw new AnnotationScanException(e);
        }
    }
}
