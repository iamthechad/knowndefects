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

package org.megatome.knowndefect.findbugs;

import edu.umd.cs.findbugs.BugReporter;

/**
 * Detector for FindBugs that looks for instances of the KnownAndAcceptedDefect annotation.
 *
 * @author cjohnston
 */
public final class KnownAndAcceptedDefectAnnotationDetector extends
        AbstractAnnotationDetector {

    private static final String KAD_TYPE = "KD_KNOWN_AND_ACCEPTED_DEFECT"; //$NON-NLS-1$
    private static final String KNOWN_ACCEPTED_DEFECT_ANNOTATION_CLASS = "org.megatome.knowndefect.annotations.KnownAndAcceptedDefect"; //$NON-NLS-1$

    /**
     * Create an instance of this detector
     *
     * @param bugReporter BugReporter
     */
    public KnownAndAcceptedDefectAnnotationDetector(final BugReporter bugReporter) {
        super(bugReporter);
    }

    /**
     * Get the class for the KnownAndAcceptedDefect annotation
     *
     * @return org.megatome.knowndefect.annotations.KnownAndAcceptedDefect
     * @see AbstractAnnotationDetector#getAnnotationClass()
     */
    @Override
    public String getAnnotationClass() {
        return KNOWN_ACCEPTED_DEFECT_ANNOTATION_CLASS;
    }

    /**
     * Get the bug type for KnownAndAcceptedDefect annotation
     *
     * @return KD_KNOWN_AND_ACCEPTED_DEFECT
     * @see AbstractAnnotationDetector#getBugType()
     */
    @Override
    public String getBugType() {
        return KAD_TYPE;
    }

}
