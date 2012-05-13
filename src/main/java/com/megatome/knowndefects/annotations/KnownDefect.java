/*******************************************************************************
 * Copyright (c) 2012 Megatome Technologies
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

package com.megatome.knowndefects.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.CLASS;
import java.lang.annotation.Target;

/**
 * Annotation to be used when creating characterization tests. Characterization tests
 * are commonly used when changing legacy code to document the current behavior of the
 * system. Quite often, this behavior contains defects. To make the characterization
 * test pass, it must be written to expect the defect. Use this annotation to mark
 * code written in this manner so the defect is explicitly called out and does not
 * get lost.
 *
 * @author cjohnston
 */
@Target(METHOD)
@Retention(CLASS)
@Documented
public @interface KnownDefect {
    /**
     * Simple description of the defect annotated.
     *
     * @return Description
     */
    String value() default "";
}
