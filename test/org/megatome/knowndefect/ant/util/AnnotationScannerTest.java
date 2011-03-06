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
import org.megatome.knowndefect.annotations.KnownAndAcceptedDefect;
import org.megatome.knowndefect.annotations.KnownDefect;
import org.megatome.knowndefect.ant.info.AnnotationInformation;
import org.megatome.knowndefect.ant.info.KnownAcceptedDefectInformation;
import org.megatome.knowndefect.ant.info.KnownDefectInformation;
import org.megatome.knowndefect.ant.scan.AnnotationScanResults;
import org.megatome.knowndefect.ant.scan.AnnotationScanner;

import java.util.Set;

import static org.junit.Assert.*;

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
        final AnnotationScanResults asr = AnnotationScanner.findAnnotationsInPath(".");

        assertNotNull(asr);

        assertFalse(asr.getKnownDefectResults().isEmpty());
        assertFalse(asr.getKnownAcceptedDefectResults().isEmpty());

        verifyKnownDefectAnnotations(asr);
        verifyKnownAcceptedDefectAnnotations(asr);
    }

    private void verifyKnownDefectAnnotations(final AnnotationScanResults asr) {
        //assertTrue(foundAnnos.containsKey(KNOWN_DEFECT_ANNOTATION_CLASS));
        final Set<AnnotationInformation> kdAnnos = asr.getKnownDefectResults();
        boolean foundEmptyKD = false;
        boolean foundKD = false;
        for (final AnnotationInformation ai : kdAnnos) {
            assertTrue(ai instanceof KnownDefectInformation);
            final KnownDefectInformation kdi = (KnownDefectInformation)ai;
            if (this.getClass().getName().equals(kdi.getClassName())) {
                if ("emptyKDMethod".equals(kdi.getMethodName())) {
                    foundEmptyKD = true;
                } else if ("valueKDMethod".equals(kdi.getMethodName())) {
                    foundKD = true;
                    assertEquals("\"Sample value\"", kdi.getValue());
                }
            }
        }
        assertTrue("Did not find empty KnownDefect annotation", foundEmptyKD);
        assertTrue("Did not find KnownDefect annotation with value", foundKD);
    }

    private void verifyKnownAcceptedDefectAnnotations(final AnnotationScanResults asr) {
        final Set<AnnotationInformation> kdAnnos = asr.getKnownAcceptedDefectResults();
        boolean foundKD = false;
        for (final AnnotationInformation ai : kdAnnos) {
            assertTrue(ai instanceof KnownAcceptedDefectInformation);
            final KnownAcceptedDefectInformation kdi = (KnownAcceptedDefectInformation)ai;
            if (this.getClass().getName().equals(kdi.getClassName())) {
                if ("sampleKADMethod".equals(kdi.getMethodName())) {
                    foundKD = true;
                    assertEquals("\"iamthechad\"", kdi.getAuthor());
                    assertEquals("\"03/01/2011\"", kdi.getDate());
                    assertEquals("\"I like \"empty\" methods\"", kdi.getReason());
                }
            }
        }
        assertTrue("Did not find KnownAndAcceptedDefect annotation with values", foundKD);
    }

    @KnownDefect
    private void emptyKDMethod() {
        // Nothing here
    }

    @KnownDefect("Sample value")
    private void valueKDMethod() {
        // Nothing here
    }

    @KnownAndAcceptedDefect(author = "iamthechad", date = "03/01/2011", reason = "I like \"empty\" methods")
    private void sampleKADMethod() {
        // Nothing here
    }
}
