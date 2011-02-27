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

package org.megatome.knowndefect.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.CLASS;
import java.lang.annotation.Target;

/**
 * Annotation to be used when a KnownDefect will not be fixed, but should still
 * be documented.
 *
 * @author cjohnston
 */
@Target(METHOD)
@Retention(CLASS)
@Documented
public @interface KnownAndAcceptedDefect {
    /**
     * Initials or name of the person that marked this defect as accepted.
     *
     * @return Author
     */
    String author();

    /**
     * The date this defect was accepted. Should be of form "MM/DD/YYYY", but anything is accepted.
     *
     * @return Date
     */
    String date();

    /**
     * The reason this defect was accepted.
     *
     * @return Reason
     */
    String reason();
}
