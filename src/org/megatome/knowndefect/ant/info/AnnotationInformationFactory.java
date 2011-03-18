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

package org.megatome.knowndefect.ant.info;

import static org.megatome.knowndefect.Constants.*;

/**
 * Construct an appropriate AnnotationInformation object based on the annotation class name.
 * @see AnnotationInformation
 */
public class AnnotationInformationFactory {
    private AnnotationInformationFactory() {}

    /**
     * Create and return an AnnotationInformation instance.
     * @param className Class name to create instance for.
     * @return Created class, or null if the class is not one of the KnownDefect types.
     */
    public static AnnotationInformation createInformation(final String className) {
        if (KNOWN_DEFECT_ANNOTATION_CLASS.equals(className)) {
            return new KnownDefectInformation();
        } else if (KNOWN_ACCEPTED_DEFECT_ANNOTATION_CLASS.equals(className)) {
            return new KnownAcceptedDefectInformation();
        }

        return null;
    }
}
