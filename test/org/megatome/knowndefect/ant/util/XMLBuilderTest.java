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
import org.megatome.knowndefect.ant.AnnotationInformation;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class XMLBuilderTest {
    @Test
    public void testBuildNullMap() throws Exception {
        assertNull(XMLBuilder.convertToXML(null));
    }

    @Test
    public void testBuildXML() throws Exception {
        final Map<String, Set<AnnotationInformation>> foundAnnos = AnnotationScanner.findAnnotationsInPath(".");
        assertNotNull(foundAnnos);
        final String xmlResult = XMLBuilder.convertToXML(foundAnnos);
        assertNotNull(xmlResult);
        //System.out.println(xmlResult);

        // If we have a schema, assume we want to validate
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        //final URL schemaLocation = this.getClass().getClassLoader().getResource(schema);
        File f = new File("src/org/megatome/knowndefect/ant/util/kd_report.xsd");
        Schema s = factory.newSchema(f);

        Validator validator = s.newValidator();
        Source source = new StreamSource(new StringReader(xmlResult));
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
