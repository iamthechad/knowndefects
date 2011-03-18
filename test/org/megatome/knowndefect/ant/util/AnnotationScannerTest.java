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

import org.junit.Before;
import org.junit.Test;
import org.megatome.knowndefect.annotations.KnownAndAcceptedDefect;
import org.megatome.knowndefect.annotations.KnownDefect;
import org.megatome.knowndefect.ant.info.AnnotationInformation;
import org.megatome.knowndefect.ant.info.KnownAcceptedDefectInformation;
import org.megatome.knowndefect.ant.info.KnownDefectInformation;
import org.megatome.knowndefect.ant.log.LoggerFactory;
import org.megatome.knowndefect.ant.log.LoggingContext;
import org.megatome.knowndefect.ant.scan.AnnotationScanResults;
import org.megatome.knowndefect.ant.scan.AnnotationScanner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class AnnotationScannerTest {
    @Before
    public void before() {
        LoggerFactory.setDefaultContext(LoggingContext.NULL_CONTEXT);
    }

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
        final Map<String, List<AnnotationInformation>> kdAnnos = asr.getKnownDefectResults();
        assertTrue(kdAnnos.containsKey(this.getClass().getName()));
        boolean foundEmptyKD = false;
        boolean foundKD = false;
        for (final AnnotationInformation ai : kdAnnos.get(this.getClass().getName())) {
            assertTrue(ai instanceof KnownDefectInformation);
            final KnownDefectInformation kdi = (KnownDefectInformation)ai;
            if ("emptyKDMethod".equals(kdi.getMethodName())) {
                foundEmptyKD = true;
            } else if ("valueKDMethod".equals(kdi.getMethodName())) {
                foundKD = true;
                assertEquals("Sample\nvalue", kdi.getValue());
            }
        }
        assertTrue("Did not find empty KnownDefect annotation", foundEmptyKD);
        assertTrue("Did not find KnownDefect annotation with value", foundKD);
    }

    private void verifyKnownAcceptedDefectAnnotations(final AnnotationScanResults asr) {
        final Map<String, List<AnnotationInformation>> kdAnnos = asr.getKnownAcceptedDefectResults();
        assertTrue(kdAnnos.containsKey(this.getClass().getName()));
        boolean foundKD = false;
        for (final AnnotationInformation ai : kdAnnos.get(this.getClass().getName())) {
            assertTrue(ai instanceof KnownAcceptedDefectInformation);
            final KnownAcceptedDefectInformation kdi = (KnownAcceptedDefectInformation)ai;
            if ("sampleKADMethod".equals(kdi.getMethodName())) {
                foundKD = true;
                assertEquals("iamthechad", kdi.getAuthor());
                assertEquals("03/01/2011", kdi.getDate());
                assertEquals("I like \"empty\" methods", kdi.getReason());
            }
        }
        assertTrue("Did not find KnownAndAcceptedDefect annotation with values", foundKD);
    }

    @KnownDefect
    private void emptyKDMethod() {
        // Nothing here
    }

    @KnownDefect("Sample\nvalue")
    private void valueKDMethod() {
        // Nothing here
    }

    @KnownAndAcceptedDefect(author = "iamthechad", date = "03/01/2011", reason = "I like \"empty\" methods")
    private void sampleKADMethod() {
        // Nothing here
    }
}
