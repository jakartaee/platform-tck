/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.test.shrinkwrap.descriptors.ejb;

import static org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder.MessageDriven.newMessageDriven;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder;
import org.jboss.cdi.tck.tests.context.jms.QueueMessageDrivenBean;
import org.jboss.cdi.tck.tests.context.jms.TopicMessageDrivenBean;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.testng.annotations.Test;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Martin Kouba
 */
public class EjbJarDescriptorBuilderTest {

    @Test
    public void testDescriptorIsValid() throws ParserConfigurationException, SAXException, DescriptorExportException,
            IOException {

        EjbJarDescriptor ejbJarDescriptor = new EjbJarDescriptorBuilder().messageDrivenBeans(
                newMessageDriven("TestQueue", QueueMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Queue")
                        .addActivationConfigProperty("destinationLookup", "test_queue"),
                newMessageDriven("TestTopic", TopicMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Topic")
                        .addActivationConfigProperty("destinationLookup", "test_topic")).build();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new LSResourceResolver() {

            @Override
            public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
                try {
                    if (systemId.startsWith("http")) {
                        // Ugly workaround for xml.xsd
                        systemId = StringUtils.substringAfterLast(systemId, "/");
                    }
                    return new Input(publicId, systemId, new FileInputStream(new File("src/test/resources/xsd", systemId)));
                } catch (FileNotFoundException e) {
                    throw new IllegalStateException();
                }
            }
        });
        schemaFactory.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException exception) throws SAXException {
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });

        Source schemaFile = new StreamSource(new FileInputStream(new File("src/test/resources/xsd", "ejb-jar_3_1.xsd")));
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new ByteArrayInputStream(ejbJarDescriptor.exportAsString().getBytes())));
    }

}
