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

import org.junit.Test;

import java.net.URL;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class AnnotationScannerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testScanNullClasspath() throws Exception {
        AnnotationScanner.findAnnotationsInPath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScanEmptyClasspath() throws Exception {
        AnnotationScanner.findAnnotationsInPath("");
    }

    @Test
    public void testScanTestClasspath() throws Exception {
        final Map<String, Set<String>> foundAnnos = AnnotationScanner.findAnnotationsInPath(".");

        assertNotNull(foundAnnos);

        assertFalse(foundAnnos.isEmpty());

        for (final Map.Entry<String, Set<String>> entry : foundAnnos.entrySet()) {
            System.out.println("Looking at: " + entry.getKey());
            for (final String value : entry.getValue()) {
                System.out.println("Value: " + value);
            }
        }
    }
}
