/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jws.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class DescriptionUtils implements DescriptionConstants, SOAPConstants,
    SchemaConstants, WSIConstants {

  /**
   * @param document
   * @return boolean
   */
  public static boolean isDescription(Document document) {
    return isElement(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_DEFINITIONS_LOCAL_NAME);
  }

  /**
   * @param document
   * @return boolean
   */
  public static boolean isSchema(Document document) {
    return isElement(document.getDocumentElement(), XSD_NAMESPACE_URI,
        XSD_SCHEMA_LOCAL_NAME);
  }

  /**
   * @param document
   * @return Element
   */
  public static Element getTypes(Document document) {
    Element[] children = getChildElements(document.getDocumentElement(),
        WSDL_NAMESPACE_URI, WSDL_TYPES_LOCAL_NAME);
    if (children.length != 0)
      return children[0];
    else
      return getTypesFromImports(document);
  }

  public static Element getTypesFromImports(Document document) {
    Element[] imports = getImports(document);
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      String namespace = imports[i].getAttribute(WSDL_NAMESPACE_ATTR);
      try {
        Document newDoc = DescriptionUtils.getDocumentFromLocation(location);
        Element element = getTypes(newDoc);
        if (element != null)
          return element;
      } catch (Exception e) {
        e.printStackTrace(System.err);
        break;
      }
    }
    return null;
  }

  /**
   * @param element
   * @return Element[]
   */
  public static Element[] getImports(Element element) {
    return getChildElements(element, WSDL_NAMESPACE_URI,
        WSDL_IMPORT_LOCAL_NAME);
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getImports(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_IMPORT_LOCAL_NAME);
  }

  public static Element[] getMessagesFromImports(Document document) {
    ArrayList children = new ArrayList();
    Element[] imports = getImports(document);
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      String namespace = imports[i].getAttribute(WSDL_NAMESPACE_ATTR);
      try {
        Document newDoc = DescriptionUtils.getDocumentFromLocation(location);
        Element[] elements = getMessages(newDoc);
        if (elements.length != 0)
          return elements;
      } catch (Exception e) {
        e.printStackTrace(System.err);
        break;
      }
    }
    return (Element[]) children.toArray(new Element[children.size()]);
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getMessages(Document document) {
    Element[] elements = getChildElements(document.getDocumentElement(),
        WSDL_NAMESPACE_URI, WSDL_MESSAGE_LOCAL_NAME);
    if (elements.length != 0)
      return elements;
    else
      return getMessagesFromImports(document);
  }

  /**
   * @param document
   * @param name
   * @return Element
   */
  public static Element getMessage(Document document, String name) {
    Element[] messages = getMessages(document);
    for (int i = 0; i < messages.length; i++) {
      if (name.equals(messages[i].getAttribute(WSDL_NAME_ATTR))) {
        return messages[i];
      }
    }
    return null;
  }

  /**
   * @param messageName
   * @return Element
   */
  public static Element getPartElement(Document document, String messageName) {
    Element message = getMessage(document, messageName);
    return getChildElement(message, WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
  }

  /**
   * @param messageName
   * @return Element[]
   */
  public static Element[] getPartElements(Document document,
      String messageName) {
    Element message = getMessage(document, messageName);
    return getChildElements(message, WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
  }

  /**
   * @param messageName
   * @return String
   */
  public static String getPartName(Document document, String messageName) {
    Element message = getMessage(document, messageName);
    Element part = getChildElement(message, WSDL_NAMESPACE_URI,
        WSDL_PART_LOCAL_NAME);
    return part.getAttribute(WSDL_NAME_ATTR);
  }

  /**
   * @param messageName
   * @return String[]
   */
  public static String[] getPartNames(Document document, String messageName) {
    Element message = getMessage(document, messageName);
    Element[] parts = getChildElements(message, WSDL_NAMESPACE_URI,
        WSDL_PART_LOCAL_NAME);
    String theParts[] = new String[parts.length];
    for (int i = 0; i < parts.length; i++)
      theParts[i] = parts[i].getAttribute(WSDL_NAME_ATTR);
    return theParts;
  }

  public static Element[] getPortTypesFromImports(Document document) {
    ArrayList children = new ArrayList();
    Element[] imports = getImports(document);
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      String namespace = imports[i].getAttribute(WSDL_NAMESPACE_ATTR);
      try {
        Document newDoc = DescriptionUtils.getDocumentFromLocation(location);
        Element[] elements = getPortTypes(newDoc);
        if (elements.length != 0)
          return elements;
      } catch (Exception e) {
        e.printStackTrace(System.err);
        break;
      }
    }
    return (Element[]) children.toArray(new Element[children.size()]);
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getPortTypes(Document document) {
    Element[] elements = getChildElements(document.getDocumentElement(),
        WSDL_NAMESPACE_URI, WSDL_PORTTYPE_LOCAL_NAME);
    if (elements.length != 0)
      return elements;
    else
      return getPortTypesFromImports(document);
  }

  /**
   * @param document
   * @param name
   * @return Element
   */
  public static Element getPortType(Document document, String name) {
    Element[] portTypes = getPortTypes(document);
    for (int i = 0; i < portTypes.length; i++) {
      if (name.equals(portTypes[i].getAttribute(WSDL_NAME_ATTR))) {
        return portTypes[i];
      }
    }
    return null;
  }

  /**
   * @param document
   * @param name
   * @return Element
   */
  public static Element getPort(Document document, String name) {
    Element[] services = getServices(document);
    for (int i = 0; i < services.length; i++) {
      Element port = getNamedChildElement(services[i], WSDL_NAMESPACE_URI,
          WSDL_PORT_LOCAL_NAME, name);
      if (port != null)
        return port;
    }
    return null;
  }

  /**
   * @param document
   * @param name
   * @return Element
   */
  public static Element getServiceName(Document document, String name) {
    Element[] services = getServices(document);
    for (int i = 0; i < services.length; i++) {
      if (name.equals(services[i].getAttribute(WSDL_NAME_ATTR))) {
        return services[i];
      }
    }
    return null;
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getBindings(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_BINDING_LOCAL_NAME);
  }

  /**
   * @param document
   * @param name
   * @return Element
   */
  public static Element getBinding(Document document, String name) {
    Element[] bindings = getBindings(document);
    for (int i = 0; i < bindings.length; i++) {
      if (name.equals(bindings[i].getAttribute(WSDL_NAME_ATTR))) {
        return bindings[i];
      }
    }
    return null;
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getServices(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_SERVICE_LOCAL_NAME);
  }

  /**
   * @param document
   * @param namespaceURI
   * @param localName
   * @return Element[]
   */
  public static Element[] getChildElements(Document document,
      String namespaceURI, String localName) {
    Element element = document.getDocumentElement();
    ArrayList children = new ArrayList();
    NodeList list = element.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element child = (Element) node;
        if ((namespaceURI != null)
            && (!namespaceURI.equals(child.getNamespaceURI()))) {
          continue;
        }
        if ((localName != null) && (!localName.equals(child.getLocalName()))) {
          continue;
        }
        children.add(child);
      }
    }
    if (children.size() == 0)
      return getChildElementsFromImports(element, namespaceURI, localName);
    else
      return (Element[]) children.toArray(new Element[children.size()]);
  }

  /**
   * @param element
   * @param namespaceURI
   * @param localName
   * @return Element[]
   */
  public static Element[] getChildElements(Element element, String namespaceURI,
      String localName) {
    ArrayList children = new ArrayList();
    NodeList list = element.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element child = (Element) node;
        if ((namespaceURI != null)
            && (!namespaceURI.equals(child.getNamespaceURI()))) {
          continue;
        }
        if ((localName != null) && (!localName.equals(child.getLocalName()))) {
          continue;
        }
        children.add(child);
      }
    }
    return (Element[]) children.toArray(new Element[children.size()]);
  }

  public static Element[] getChildElementsFromImports(Element element,
      String namespaceURI, String localName) {
    ArrayList children = new ArrayList();
    Element[] imports = getImports(element);
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      String namespace = imports[i].getAttribute(WSDL_NAMESPACE_ATTR);
      try {
        Document newDoc = DescriptionUtils.getDocumentFromLocation(location);
        Element[] elements = getChildElements(newDoc.getDocumentElement(),
            namespaceURI, localName);
        return elements;
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
    return (Element[]) children.toArray(new Element[children.size()]);
  }

  /**
   * @param document
   * @return String
   */
  public static String getTargetNamespaceAttr(Document document) {
    return document.getDocumentElement()
        .getAttribute(WSDL_TARGETNAMESPACE_ATTR);
  }

  /**
   * @param document
   * @return String
   */
  public static String getDefintionsNameAttr(Document document) {
    return document.getDocumentElement().getAttribute(WSDL_NAME_ATTR);
  }

  /**
   * @param document
   * @return String
   */
  public static boolean isPortTypeNameAttr(Document document,
      String portTypeName) {
    Element portType = getPortType(document, portTypeName);
    if (portType != null)
      return true;
    else
      return false;
  }

  /**
   * @param document
   * @return String
   */
  public static boolean isPortNameAttr(Document document, String portName) {
    Element port = getPort(document, portName);
    if (port != null)
      return true;
    else
      return false;
  }

  /**
   * @param document
   * @return String
   */
  public static boolean isServiceNameAttr(Document document,
      String serviceName) {
    Element service = getServiceName(document, serviceName);
    if (service != null)
      return true;
    else
      return false;
  }

  /**
   * @param document
   * @return String
   */
  public static String getInputMessageName(Element e) {
    Element input = getChildElement(e, WSDL_NAMESPACE_URI,
        WSDL_INPUT_LOCAL_NAME);
    String messageName = input.getAttribute(WSDL_MESSAGE_ATTR);
    int i = messageName.indexOf(":");
    if (i != -1)
      return messageName.substring(i + 1);
    else
      return messageName;
  }

  /**
   * @param document
   * @return String
   */
  public static String getOutputMessageName(Element e) {
    Element output = getChildElement(e, WSDL_NAMESPACE_URI,
        WSDL_OUTPUT_LOCAL_NAME);
    String messageName = output.getAttribute(WSDL_MESSAGE_ATTR);
    int i = messageName.indexOf(":");
    if (i != -1)
      return messageName.substring(i + 1);
    else
      return messageName;
  }

  /**
   * @param document
   * @param portTypeName
   * @param operationName
   * @return Element
   */
  public static Element getPortTypeOperationNameElement(Document document,
      String portTypeName, String operationName) {
    Element portType = getPortType(document, portTypeName);
    if (portType != null) {
      Element[] operations = getChildElements(portType, WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      for (int i = 0; i < operations.length; i++) {
        String theOperationName = operations[i].getAttribute(WSDL_NAME_ATTR);
        if (theOperationName.equals(operationName))
          return operations[i];
      }
    }
    return null;
  }

  /**
   * @param document
   * @param portTypeName
   * @return String[]
   */
  public static String[] getPortTypeOperationNames(Document document,
      String portTypeName) {
    Element portType = getPortType(document, portTypeName);
    if (portType != null) {
      Element[] operations = getChildElements(portType, WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      String opNames[] = new String[operations.length];
      for (int i = 0; i < operations.length; i++) {
        opNames[i] = operations[i].getAttribute(WSDL_NAME_ATTR);
      }
      return opNames;
    }
    return null;
  }

  /**
   * @param document
   * @param portTypeName
   * @return Element[]
   */
  public static Element[] getPortTypeOperationNameElements(Document document,
      String portTypeName) {
    Element portType = getPortType(document, portTypeName);
    if (portType != null) {
      Element[] operations = getChildElements(portType, WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      return operations;
    }
    return null;
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getBindingOperationNameElements(Document document) {
    Element[] bindings = getBindings(document);
    return getChildElements(bindings[0], WSDL_NAMESPACE_URI,
        WSDL_OPERATION_LOCAL_NAME);
  }

  /**
   * @param document
   * @param portTypeName
   * @return String[]
   */
  public static String[] getInputMessageNames(Document document,
      String portTypeName) {
    ArrayList alist = new ArrayList();
    Element portType = getPortType(document, portTypeName);
    if (portType != null) {
      Element[] operations = getChildElements(portType, WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      for (int i = 0; i < operations.length; i++) {
        Element input = DescriptionUtils.getChildElement(operations[i],
            WSDL_NAMESPACE_URI, WSDL_INPUT_LOCAL_NAME);
        String imsg = input.getAttribute(WSDL_MESSAGE_ATTR);
        alist.add(imsg.substring(imsg.indexOf(":") + 1));

      }
      return (String[]) alist.toArray(new String[alist.size()]);
    }
    return null;
  }

  /**
   * @param document
   * @param portTypeName
   * @return String[]
   */
  public static String[] getOutputMessageNames(Document document,
      String portTypeName) {
    ArrayList alist = new ArrayList();
    Element portType = getPortType(document, portTypeName);
    if (portType != null) {
      Element[] operations = getChildElements(portType, WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      for (int i = 0; i < operations.length; i++) {
        Element output = DescriptionUtils.getChildElement(operations[i],
            WSDL_NAMESPACE_URI, WSDL_OUTPUT_LOCAL_NAME);
        // Operation always has an input, may not have an output so let's check
        if (output != null) {
          String omsg = output.getAttribute(WSDL_MESSAGE_ATTR);
          alist.add(omsg.substring(omsg.indexOf(":") + 1));
        }

      }
      return (String[]) alist.toArray(new String[alist.size()]);
    }
    return null;
  }

  /**
   * @param operation
   * @return boolean
   */
  public static boolean isOneWay(Element operation) {
    return getChildElement(operation, WSDL_NAMESPACE_URI,
        WSDL_INPUT_LOCAL_NAME) != null
        && getChildElement(operation, WSDL_NAMESPACE_URI,
            WSDL_OUTPUT_LOCAL_NAME) == null;
  }

  /**
   * @param operation
   * @return boolean
   */
  public static boolean isTwoWay(Element operation) {
    return getChildElement(operation, WSDL_NAMESPACE_URI,
        WSDL_INPUT_LOCAL_NAME) != null
        && getChildElement(operation, WSDL_NAMESPACE_URI,
            WSDL_OUTPUT_LOCAL_NAME) != null;
  }

  /**
   * @param operation
   * @return boolean
   */
  public static boolean hasFault(Element operation) {
    Element[] faults = getChildElements(operation, WSDL_NAMESPACE_URI,
        WSDL_FAULT_LOCAL_NAME);
    if (faults.length > 0)
      return true;
    else
      return false;
  }

  /**
   * @param operation
   * @param faultName
   * @return boolean
   */
  public static boolean hasFault(Element operation, String faultName) {
    Element[] faults = getChildElements(operation, WSDL_NAMESPACE_URI,
        WSDL_FAULT_LOCAL_NAME);
    for (int i = 0; i < faults.length; i++) {
      if (faults[i].getAttribute(WSDL_NAME_ATTR).equals(faultName))
        return true;
    }
    return false;
  }

  /**
   * @param document
   * @param operationName
   * @return Element
   */
  public static Element getBindingOperationNameElement(Document document,
      String operationName) {
    Element[] bindings = getBindings(document);
    if (bindings.length != 1)
      return null;
    Element[] operations = getChildElements(bindings[0], WSDL_NAMESPACE_URI,
        WSDL_OPERATION_LOCAL_NAME);
    for (int i = 0; i < operations.length; i++) {
      String theOperationName = operations[i].getAttribute(WSDL_NAME_ATTR);
      if (theOperationName.equals(operationName))
        return operations[i];
    }
    return null;
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getSchemaImportElements(Document document) {
    Element types = getTypes(document);
    ArrayList alist = new ArrayList();
    Element[] schemas = null;
    if (types != null) {
      schemas = getChildElements(types, XSD_NAMESPACE_URI,
          XSD_SCHEMA_LOCAL_NAME);
      for (int i = 0; i < schemas.length; i++) {
        Element[] imports = getChildElements(schemas[i], XSD_NAMESPACE_URI,
            XSD_IMPORT_LOCAL_NAME);
        for (int j = 0; j < imports.length; j++)
          alist.add(imports[j]);
      }
    } else {
      Element[] imports = getChildElements(document.getDocumentElement(),
          XSD_NAMESPACE_URI, XSD_IMPORT_LOCAL_NAME);
      for (int i = 0; i < imports.length; i++)
        alist.add(imports[i]);
    }
    return (Element[]) alist.toArray(new Element[alist.size()]);
  }

  /**
   * @param document
   * @return Element
   */
  public static Element getSchemaElementName(Document document,
      String elementName) {
    Element types = getTypes(document);
    Element schema = getChildElement(types, XSD_NAMESPACE_URI,
        XSD_SCHEMA_LOCAL_NAME);
    Element[] elements;
    if (schema != null) {
      elements = getChildElements(schema, XSD_NAMESPACE_URI,
          XSD_ELEMENT_LOCAL_NAME);
      TestUtil.logMsg("elements=" + elements.length);
      for (int i = 0; i < elements.length; i++) {
        TestUtil.logMsg("name=" + elements[i].getAttribute(XSD_NAME_ATTR));
        if (elements[i].getAttribute(XSD_NAME_ATTR).equals(elementName))
          return elements[i];
      }
    }
    return null;
  }

  /**
   * @param document
   * @return String[]
   */
  public static String[] getSchemaNamespaces(Document document) {
    Element[] imports = getSchemaImportElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      alist.add(imports[i].getAttribute(XSD_NAMESPACE_ATTR));

    }
    if (alist.size() == 0)
      return null;
    else
      return (String[]) alist.toArray(new String[alist.size()]);
  }

  /**
   *
   * @param document
   * @param name
   * @return boolean
   * @throws EETest.Fault
   */
  public static boolean findGlobalElementByName(Document document, String name)
      throws EETest.Fault {
    Element[] elements = getChildElements(document.getDocumentElement(),
        XSD_NAMESPACE_URI, XSD_ELEMENT_LOCAL_NAME);
    for (int i = 0; i < elements.length; i++) {
      String nameAttr = elements[i].getAttribute(XSD_NAME_ATTR);
      if (nameAttr.equals(name))
        return true;
    }
    return false;
  }

  /**
   * @param element
   * @return boolean
   * @throws EETest.Fault
   */
  public static boolean findGlobalElementByName(Element element, String name)
      throws EETest.Fault {
    Element[] elements = getChildElements(element, XSD_NAMESPACE_URI,
        XSD_ELEMENT_LOCAL_NAME);
    for (int i = 0; i < elements.length; i++) {
      String nameAttr = elements[i].getAttribute(XSD_NAME_ATTR);
      if (nameAttr.equals(name))
        return true;
    }
    return false;
  }

  /**
   * @param document
   * @param base
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document[] getSchemaDocuments(Document document, String base)
      throws EETest.Fault {
    Element[] imports = getSchemaImportElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String schemaLocation = imports[i].getAttribute(XSD_SCHEMALOCATION_ATTR);
      Document thedocument = getDocumentFromLocation(
          "file:" + base + schemaLocation);
      thedocument.getDocumentElement().setAttribute("SchemaFile",
          schemaLocation);
      alist.add(thedocument);
    }
    if (alist.size() == 0)
      return null;
    else
      return (Document[]) alist.toArray(new Document[alist.size()]);
  }

  /**
   * @param document
   * @param base
   * @param prefix
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document getSchemaDocument(Document document, String prefix,
      String base) throws EETest.Fault {
    String ns = getNamespaceOfPrefix(document, prefix);
    Element[] imports = getSchemaImportElements(document);
    for (int i = 0; i < imports.length; i++) {
      String namespace = imports[i].getAttribute(XSD_NAMESPACE_ATTR);
      if (namespace.equals(ns)) {
        String schemaLocation = imports[i]
            .getAttribute(XSD_SCHEMALOCATION_ATTR);
        return getDocumentFromLocation("file:" + base + schemaLocation);
      }
    }
    return null;
  }

  /**
   * @param document
   * @param base
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document[] getWsdlDocuments(Document document, String base)
      throws EETest.Fault {
    Element[] imports = getImports(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      Document thedocument = getDocumentFromLocation("file:" + base + location);
      thedocument.getDocumentElement().setAttribute("WsdlFile", location);
      alist.add(thedocument);
    }
    if (alist.size() == 0)
      return null;
    else
      return (Document[]) alist.toArray(new Document[alist.size()]);
  }

  /**
   * @param document
   * @param url
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document[] getSchemaDocuments(Document document, URL url)
      throws EETest.Fault {
    Element[] imports = getSchemaImportElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String schemaLocation = imports[i].getAttribute(XSD_SCHEMALOCATION_ATTR);
      Document thedocument = getDocumentFromLocation(schemaLocation, url);
      thedocument.getDocumentElement().setAttribute("SchemaFile",
          schemaLocation);
      alist.add(thedocument);
    }
    if (alist.size() == 0)
      return null;
    else
      return (Document[]) alist.toArray(new Document[alist.size()]);
  }

  /**
   * @param document
   * @param prefix
   * @param url
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document getSchemaDocument(Document document, String prefix,
      URL url) throws EETest.Fault {
    String ns = getNamespaceOfPrefix(document, prefix);
    Element[] imports = getSchemaImportElements(document);
    for (int i = 0; i < imports.length; i++) {
      String namespace = imports[i].getAttribute(XSD_NAMESPACE_ATTR);
      if (namespace.equals(ns)) {
        String schemaLocation = imports[i]
            .getAttribute(XSD_SCHEMALOCATION_ATTR);
        return getDocumentFromLocation(schemaLocation, url);
      }
    }
    return null;
  }

  /**
   * @param document
   * @param url
   * @return Document[]
   * @throws EETest.Fault
   */
  public static Document[] getWsdlDocuments(Document document, URL url)
      throws EETest.Fault {
    Element[] imports = getImports(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      Document thedocument = getDocumentFromLocation(location, url);
      thedocument.getDocumentElement().setAttribute("WsdlFile", location);
      alist.add(thedocument);
    }
    if (alist.size() == 0)
      return null;
    else
      return (Document[]) alist.toArray(new Document[alist.size()]);
  }

  /**
   * @param document
   * @return String[]
   */
  public static String[] getSchemaPrefixes(Document document) {
    Element[] imports = getSchemaImportElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String NS = imports[i].getAttribute(XSD_NAMESPACE_ATTR);
      alist.add(findPrefixForNamespace(document, NS));
    }
    if (alist.size() == 0)
      return null;
    else
      return (String[]) alist.toArray(new String[alist.size()]);
  }

  /**
   * @param document
   * @return Element[]
   */
  public static String[] getAllPrefixAndNamespace(Document document) {
    Element description = document.getDocumentElement();
    Attr[] attributes = DescriptionUtils.getElementAttributes(description);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < attributes.length; i++) {
      String name = attributes[i].getName();
      String value = attributes[i].getValue();
      if (name.startsWith("xmlns:"))
        alist.add(name.substring(name.indexOf(":") + 1) + ":" + value);
    }
    return (String[]) alist.toArray(new String[alist.size()]);
  }

  /**
   * @param document
   * @return String
   */
  public static String getNamespaceOfPrefix(Document document, String prefix) {
    Element description = document.getDocumentElement();
    Attr[] attributes = DescriptionUtils.getElementAttributes(description);
    for (int i = 0; i < attributes.length; i++) {
      String name = attributes[i].getName();
      String value = attributes[i].getValue();
      if (name.startsWith("xmlns:")) {
        String nsprefix = name.substring(name.indexOf(":") + 1);
        if (nsprefix.equals(prefix))
          return value;
      }
    }
    return null;
  }

  /**
   * @param document
   * @return String
   */
  public static String getPrefixOfNamepace(Document document,
      String namespace) {
    Element description = document.getDocumentElement();
    Attr[] attributes = DescriptionUtils.getElementAttributes(description);
    for (int i = 0; i < attributes.length; i++) {
      String name = attributes[i].getName();
      String value = attributes[i].getValue();
      if (value.equals(namespace)) {
        if (name.startsWith("xmlns:"))
          return name.substring(name.indexOf(":") + 1);
      }
    }
    return null;
  }

  /**
   * @param document
   * @return String
   */
  public static String findPrefixForNamespace(Document document,
      String namespace) {
    Element description = document.getDocumentElement();
    Attr[] attributes = DescriptionUtils.getElementAttributes(description);
    for (int i = 0; i < attributes.length; i++) {
      String name = attributes[i].getName();
      String value = attributes[i].getValue();
      if (!value.equals(namespace))
        continue;
      if (name.startsWith("xmlns:"))
        return name.substring(name.indexOf(":") + 1);
    }
    return null;
  }

  /**
   * @param document
   * @param operationName
   * @return Element
   */
  public static Element getSoapBindingElement(Document document,
      String operationName) {
    Element[] bindings = getBindings(document);
    if (bindings.length != 1)
      return null;
    return getChildElement(bindings[0], SOAP_NAMESPACE_URI,
        SOAP_BINDING_LOCAL_NAME);
  }

  /**
   * @param document
   * @return Element[]
   */
  public static Element[] getSoapHeaderElements(Document document) {
    Element[] bindings = getBindings(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < bindings.length; i++) {
      Element[] operations = getChildElements(bindings[0], WSDL_NAMESPACE_URI,
          WSDL_OPERATION_LOCAL_NAME);
      for (int j = 0; j < operations.length; j++) {
        Element input = getChildElement(operations[j], WSDL_NAMESPACE_URI,
            WSDL_INPUT_LOCAL_NAME);
        Element header = getChildElement(input, SOAP_NAMESPACE_URI,
            SOAP_HEADER_LOCAL_NAME);
        if (header != null)
          alist.add(header);
      }
    }
    return (Element[]) alist.toArray(new Element[alist.size()]);
  }

  /**
   * @param document
   * @return String[]
   */
  public static String[] getSoapHeaderElementsPartAttr(Document document) {
    Element[] headers = getSoapHeaderElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < headers.length; i++)
      alist.add(headers[i].getAttribute(SOAP_PART_ATTR));
    return (String[]) alist.toArray(new String[alist.size()]);
  }

  /**
   * @param element
   * @return Element[]
   */
  public static Element[] getChildElements(Element element) {
    return getChildElements(element, null, null);
  }

  /**
   * @param element
   * @param namespaceURI
   * @param localName
   * @return Element
   */
  public static Element getChildElement(Element element, String namespaceURI,
      String localName) {
    Element[] children = getChildElements(element, namespaceURI, localName);
    if (children.length != 0) {
      return children[0];
    }
    return null;
  }

  /**
   * @param element
   * @param namespaceURI
   * @param localName
   * @param name
   * @return Element
   */
  public static Element getNamedChildElement(Element element,
      String namespaceURI, String localName, String name) {
    Element[] children = getChildElements(element, namespaceURI, localName);
    for (int i = 0; i < children.length; i++) {
      if (name.equals(children[i].getAttribute(WSDL_NAME_ATTR))) {
        return children[i];
      }
    }
    return null;
  }

  /**
   * @param element
   * @param namespaceURI
   * @param localName
   * @return boolean
   */
  public static boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (element == null) {
      return false;
    }
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    if (!localName.equals(element.getLocalName())) {
      return false;
    }
    return true;
  }

  /**
   * @param element
   * @return Attr[]
   */
  public static Attr[] getElementAttributes(Element element) {
    NamedNodeMap map = element.getAttributes();
    Attr[] attributes = new Attr[map.getLength()];
    for (int i = 0; i < attributes.length; i++) {
      attributes[i] = (Attr) map.item(i);
    }
    return attributes;
  }

  /**
   * @param element
   * @param prefix
   * @return boolean
   */
  public static boolean isNamespacePrefixDeclared(Element element,
      String prefix) {
    while (element != null) {
      Attr attribute = element.getAttributeNode("xmlns:" + prefix);
      if (attribute != null) {
        return true;
      }
      Node node = element.getParentNode();
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;
      } else {
        element = null;
      }
    }
    return false;
  }

  /**
   * @param element
   * @param prefix
   * @return String
   */
  public static String getNamespaceURI(Element element, String prefix) {
    while (element != null) {
      Attr attribute = element.getAttributeNode("xmlns:" + prefix);
      if (attribute != null) {
        return attribute.getValue();
      }
      Node node = element.getParentNode();
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;
      } else {
        element = null;
      }
    }
    return null;
  }

  /**
   * @param elements
   * @param namespaceURI
   * @param localName
   * @return int
   */
  public static int getIndexOf(Element[] elements, String namespaceURI,
      String localName) {
    for (int i = 0; i < elements.length; i++) {
      if (isElement(elements[i], namespaceURI, localName)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param elements
   * @param namespaceURI
   * @param localName
   * @return int
   */
  public static int getLastIndexOf(Element[] elements, String namespaceURI,
      String localName) {
    for (int i = elements.length - 1; i >= 0; i--) {
      if (isElement(elements[i], namespaceURI, localName)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param location
   * @return Document
   * @throws EETest.Fault
   */
  public static Document getDocumentFromLocation(String location)
      throws EETest.Fault {
    try {
      URL url = new URL(location);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(url.openStream());
    } catch (MalformedURLException e) {
      throw new EETest.Fault("The location '" + location + "' is invalid", e);
    } catch (FactoryConfigurationError e) {
      throw new EETest.Fault("Unable to obtain XML parser", e);
    } catch (ParserConfigurationException e) {
      throw new EETest.Fault("Unable to obtain XML parser", e);
    } catch (SAXException e) {
      throw new EETest.Fault(
          "The document at '" + location + "' is not valid XML", e);
    } catch (IOException e) {
      throw new EETest.Fault(
          "The document at '" + location + "' could not be read", e);
    }
  }

  /**
   * @param location
   * @param context
   * @return Document
   * @throws EETest.Fault
   */
  public static Document getDocumentFromLocation(String location, URL context)
      throws EETest.Fault {
    try {
      URL url;
      URI uri = null;
      if (location.startsWith("http"))
        url = new URL(location);
      else {
        uri = context.toURI();
        uri = uri.resolve(new URI(location));
        url = uri.toURL();
        System.out.println(" URL : " + url.toString());
      }
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(url.openStream());
      doc.setDocumentURI(url.toString());
      return doc;
    } catch (MalformedURLException e) {
      throw new EETest.Fault("The location '" + location + "' is invalid", e);
    } catch (FactoryConfigurationError e) {
      throw new EETest.Fault("Unable to obtain XML parser", e);
    } catch (ParserConfigurationException e) {
      throw new EETest.Fault("Unable to obtain XML parser", e);
    } catch (SAXException e) {
      throw new EETest.Fault(
          "The document at '" + location + "' is not valid XML", e);
    } catch (IOException e) {
      throw new EETest.Fault(
          "The document at '" + location + "' could not be read", e);
    } catch (URISyntaxException e) {
      throw new EETest.Fault(
          "The document at '" + location + "' could not be read", e);
    }
  }

  /**
   * Private to prevent instantiation.
   */
  protected DescriptionUtils() {
    super();
  }

  private static int spaces = 0;

  private static String spacesString = "";

  private static void setSpaces() {
    spacesString = "";
    for (int i = 0; i < spaces; i++)
      spacesString = spacesString + " ";
  }

  private static String getText(Node node) {
    String result = "";
    result = node.getNodeValue();
    if (result == null)
      result = "";
    result = result.trim();
    return result;
  }

  private static void processAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs != null) {
      for (int i = 0; i < attribs.getLength(); i++) {
        Node attnode = attribs.item(i);
        String attName = attnode.getNodeName();
        String attValue = attnode.getNodeValue();
        setSpaces();
        TestUtil.logMsg(spacesString + "<Attribute>" + attName + "=" + attValue
            + "</Attribute>");
      }
    }
  }

  private static boolean hasAttributes(Node root) {
    NamedNodeMap attribs = root.getAttributes();
    if (attribs == null || attribs.getLength() == 0)
      return false;
    else
      return true;
  }

  public static void dumpDOMNodes(Element element) {
    TestUtil.logMsg("Begin Dumping DOM Nodes");
    String rootNodeName = element.getNodeName();
    TestUtil.logMsg("<RootElement>" + rootNodeName + "</RootElement>");
    setSpaces();
    spaces += 2;
    processAttributes((Node) element);
    dumpDOMNodes_(element);
    TestUtil.logMsg("Done Dumping DOM Nodes");
  }

  public static void dumpDOMNodes(Node node) {
    TestUtil.logMsg("Begin Dumping DOM Nodes");
    String rootNodeName = node.getNodeName();
    TestUtil.logMsg("<RootElement>" + rootNodeName + "</RootElement>");
    setSpaces();
    spaces += 2;
    processAttributes(node);
    dumpDOMNodes_((Element) node);
    TestUtil.logMsg("Done Dumping DOM Nodes");
  }

  public static void dumpDOMNodes_(Element element) {
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      String nodeName = node.getNodeName();
      String nodeValue = node.getNodeValue();
      short nodeType = node.getNodeType();
      switch (nodeType) {
      case Node.ATTRIBUTE_NODE:
        setSpaces();
        TestUtil.logMsg(spacesString + "<Attribute>" + nodeName + "="
            + nodeValue + "</Attribute>");
        break;
      case Node.CDATA_SECTION_NODE:
        TestUtil.logMsg("<CDATA>" + nodeValue + "</CDATA>");
        break;
      case Node.COMMENT_NODE:
        TestUtil.logMsg("<Comment>" + nodeValue + "</Comment>");
        break;
      case Node.DOCUMENT_FRAGMENT_NODE:
        TestUtil.logMsg("<DocumentFragment/>");
        break;
      case Node.DOCUMENT_NODE:
        TestUtil.logMsg("<Document/>");
        break;
      case Node.DOCUMENT_TYPE_NODE:
        TestUtil.logMsg("<DocumentType>" + nodeName + "</DocumentType>");
        break;
      case Node.ELEMENT_NODE:
        setSpaces();
        TestUtil.logMsg(spacesString + "<Element>" + nodeName + "</Element>");
        spaces += 2;
        processAttributes(node);
        break;
      case Node.ENTITY_NODE:
        TestUtil.logMsg("<Entity>" + nodeValue + "</Entity>");
        break;
      case Node.ENTITY_REFERENCE_NODE:
        TestUtil.logMsg("<EntityReference>" + nodeValue + "</EntityReference>");
        break;
      case Node.NOTATION_NODE:
        TestUtil.logMsg("<Notation>" + nodeValue + "</Notation>");
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        TestUtil.logMsg(
            "<ProcessingInstruction>" + nodeName + "</ProcessingInstruction>");
        break;
      case Node.TEXT_NODE:
        String text = getText(node);
        if (!text.equals("")) {
          setSpaces();
          TestUtil.logMsg(spacesString + "<Text>" + text + "</text>");
        }
        break;
      default:
        TestUtil.logMsg("<" + nodeName + ">");
        break;
      }
      if (node instanceof Element) {
        dumpDOMNodes_((Element) node);
        spaces -= 2;
      }
    }
  }
}
