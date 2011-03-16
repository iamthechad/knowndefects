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

package org.megatome.knowndefect.ant.scan;

import org.megatome.knowndefect.ant.info.AnnotationInformation;
import org.megatome.knowndefect.ant.info.KnownAcceptedDefectInformation;
import org.megatome.knowndefect.ant.info.KnownDefectInformation;

import java.util.*;

public class AnnotationScanResults {
    private final Map<String, List<AnnotationInformation>> knownDefectResults = new HashMap<String, List<AnnotationInformation>>();
    private final Map<String, List<AnnotationInformation>> knownAcceptedDefectResults = new HashMap<String, List<AnnotationInformation>>();

    public void addResult(final String className, final AnnotationInformation info) {
        if (null == info) return;

        if (info instanceof KnownDefectInformation) {
            addToResults(knownDefectResults, className, info);
            /*List<AnnotationInformation> l = knownDefectResults.get(className);
            if (null == l) {
                l = new ArrayList<AnnotationInformation>();
            }
            l.add(info);
            knownDefectResults.put(className, l);*/
        } else if (info instanceof KnownAcceptedDefectInformation) {
            addToResults(knownAcceptedDefectResults, className, info);
            //knownAcceptedDefectResults.add(info);
        }
    }

    private void addToResults(final Map<String, List<AnnotationInformation>> map, String className, AnnotationInformation info) {
        List<AnnotationInformation> l = map.get(className);
        if (null == l) {
            l = new ArrayList<AnnotationInformation>();
        }
        l.add(info);
        map.put(className, l);
    }

    public Map<String, List<AnnotationInformation>> getKnownDefectResults() {
        return knownDefectResults;
    }

    public Map<String, List<AnnotationInformation>> getKnownAcceptedDefectResults() {
        return knownAcceptedDefectResults;
    }

    public boolean hasKnownDefectResults() {
        return !knownDefectResults.isEmpty();
    }

    public boolean hasKnownAcceptedDefectResults() {
        return !knownAcceptedDefectResults.isEmpty();
    }
}
