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
import org.megatome.knowndefect.ant.log.LoggerFactory;
import org.megatome.knowndefect.ant.log.LoggingContext;
import org.megatome.knowndefect.ant.scan.AnnotationScanResults;
import org.megatome.knowndefect.ant.scan.AnnotationScanner;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class XMLBuilderTest {
    @Before
    public void before() {
        LoggerFactory.setDefaultContext(LoggingContext.NULL_CONTEXT);
    }

    @Test
    public void testBuildNullMap() throws Exception {
        final String xmlResult = XMLBuilder.convertToXML(null);
        assertNotNull(xmlResult);
        verifyXMLStructure(xmlResult);
    }

    @Test
    public void testBuildXML() throws Exception {
        final AnnotationScanResults asr = AnnotationScanner.findAnnotationsInPath(".");
        assertNotNull(asr);
        final String xmlResult = XMLBuilder.convertToXML(asr);
        assertNotNull(xmlResult);

        verifyXMLStructure(xmlResult);
    }

    private void verifyXMLStructure(final String xml) throws Exception {
        final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        final File f = new File("src/org/megatome/knowndefect/ant/scan/kd_report.xsd");
        final Schema s = factory.newSchema(f);

        final Validator validator = s.newValidator();
        final Source source = new StreamSource(new StringReader(xml));
        try {
            validator.validate(source);
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
