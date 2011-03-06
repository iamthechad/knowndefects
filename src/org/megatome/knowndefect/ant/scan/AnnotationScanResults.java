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

import java.util.HashSet;
import java.util.Set;

public class AnnotationScanResults {
    private final Set<AnnotationInformation> knownDefectResults = new HashSet<AnnotationInformation>();
    private final Set<AnnotationInformation> knownAcceptedDefectResults = new HashSet<AnnotationInformation>();

    public void addResult(AnnotationInformation info) {
        if (null == info) return;

        if (info instanceof KnownDefectInformation) {
            knownDefectResults.add(info);
        } else if (info instanceof KnownAcceptedDefectInformation) {
            knownAcceptedDefectResults.add(info);
        }
    }

    public Set<AnnotationInformation> getKnownDefectResults() {
        return knownDefectResults;
    }

    public Set<AnnotationInformation> getKnownAcceptedDefectResults() {
        return knownAcceptedDefectResults;
    }

    public boolean hasKnownDefectResults() {
        return !knownDefectResults.isEmpty();
    }

    public boolean hasKnownAcceptedDefectResults() {
        return !knownAcceptedDefectResults.isEmpty();
    }
}
