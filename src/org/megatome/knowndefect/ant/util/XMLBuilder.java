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

import org.megatome.knowndefect.ant.info.AnnotationInformation;
import org.megatome.knowndefect.ant.info.KnownAcceptedDefectInformation;
import org.megatome.knowndefect.ant.info.KnownDefectInformation;
import org.megatome.knowndefect.ant.scan.AnnotationScanResults;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Set;

public class XMLBuilder {
    public static String convertToXML(final AnnotationScanResults asr) throws Exception {
        if (null == asr) {
            return null;
        }

        //We need a Document
        final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        final DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        final Document doc = docBuilder.newDocument();

        ////////////////////////
        //Creating the XML tree

        //create the root element and add it to the document
        final Element root = doc.createElement("knowndefects");
        doc.appendChild(root);

        if (asr.hasKnownDefectResults()) {
            final Element child = doc.createElement("knowndefect");
            root.appendChild(child);

            buildClassNodes(doc, child, asr.getKnownDefectResults());
        }

        if (asr.hasKnownAcceptedDefectResults()) {
            final Element child = doc.createElement("knownandaccepteddefect");
            root.appendChild(child);

            buildClassNodes(doc, child, asr.getKnownAcceptedDefectResults());
        }

        //set up a transformer
        final TransformerFactory transfac = TransformerFactory.newInstance();
        final Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        final StringWriter sw = new StringWriter();
        final StreamResult result = new StreamResult(sw);
        final DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        return sw.toString();
    }

    private static void buildClassNodes(final Document doc, final Element parent, final Set<AnnotationInformation> infoSet) {
        for (final AnnotationInformation info : infoSet) {
            final Element infoNode = doc.createElement("class");
            infoNode.setAttribute("name", info.getClassName());
            parent.appendChild(infoNode);

            final Element methodNode = doc.createElement("method");
            methodNode.setAttribute("name", info.getMethodName());
            infoNode.appendChild(methodNode);

            buildValueNodes(doc, methodNode, info);
        }
    }

    private static void buildValueNodes(final Document doc, final Element methodNode, final AnnotationInformation info) {
        if (info instanceof KnownDefectInformation) {
            buildKDValueNodes(doc, methodNode, (KnownDefectInformation)info);
        } else if (info instanceof KnownAcceptedDefectInformation) {
            buildKADValueNodes(doc, methodNode, (KnownAcceptedDefectInformation) info);
        }
    }

    private static void buildKDValueNodes(final Document doc, final Element methodNode, final KnownDefectInformation info) {
        if (null != info.getValue()) {
            buildAndAppendNode(doc, methodNode, "value", info.getValue());
        }
    }

    private static void buildKADValueNodes(final Document doc, final Element methodNode, final KnownAcceptedDefectInformation info) {
        buildAndAppendNode(doc, methodNode, "author", info.getAuthor());
        buildAndAppendNode(doc, methodNode, "reason", info.getReason());
        buildAndAppendNode(doc, methodNode, "date", info.getDate());
    }

    private static void buildAndAppendNode(final Document doc, final Element parent, final String nodeName, final String nodeText) {
        final Element node = doc.createElement(nodeName);
        final Text text = doc.createTextNode(nodeText);
        node.appendChild(text);
        parent.appendChild(node);
    }
}
