/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

/*
 * @(#)DescriptionUtils.java	1.2 03/05/16
 */

package com.sun.ts.tests.jaxrpc.wsi.utils;

import java.io.IOException;
import java.net.MalformedURLException;
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
import com.sun.ts.tests.jaxrpc.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxrpc.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxrpc.wsi.constants.SchemaConstants;
import com.sun.ts.tests.jaxrpc.wsi.constants.WSIConstants;

public class DescriptionUtils implements DescriptionConstants, SOAPConstants,
    SchemaConstants, WSIConstants {
  /**
   * 
   * @param document
   * @return boolean
   */
  public static boolean isDescription(Document document) {
    return isElement(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_DEFINITIONS_LOCAL_NAME);
  }

  /**
   * 
   * @param document
   * @return boolean
   */
  public static boolean isSchema(Document document) {
    return isElement(document.getDocumentElement(), XSD_NAMESPACE_URI,
        XSD_SCHEMA_LOCAL_NAME);
  }

  /**
   * 
   * @param document
   * @return Element
   */
  public static Element getTypes(Document document) {
    Element[] children = getChildElements(document.getDocumentElement(),
        WSDL_NAMESPACE_URI, WSDL_TYPES_LOCAL_NAME);
    if (children.length != 0) {
      return children[0];
    }
    return null;
  }

  /**
   * 
   * @param document
   * @return Element[]
   */
  public static Element[] getImports(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_IMPORT_LOCAL_NAME);
  }

  /**
   * 
   * @param document
   * @return Element[]
   */
  public static Element[] getMessages(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_MESSAGE_LOCAL_NAME);
  }

  /**
   * 
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
   * 
   * @param document
   * @return Element[]
   */
  public static Element[] getPortTypes(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_PORTTYPE_LOCAL_NAME);
  }

  /**
   * 
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
   * 
   * @param document
   * @return Element[]
   */
  public static Element[] getBindings(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_BINDING_LOCAL_NAME);
  }

  /**
   * 
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
   * 
   * @param document
   * @return Element[]
   */
  public static Element[] getServices(Document document) {
    return getChildElements(document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_SERVICE_LOCAL_NAME);
  }

  /**
   * 
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

  /**
   * 
   * @param element
   * @return Element[]
   */
  public static Element[] getChildElements(Element element) {
    return getChildElements(element, null, null);
  }

  /**
   * 
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
   * 
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
      if (name.equals(children[i].getAttribute("name"))) {
        return children[i];
      }
    }
    return null;
  }

  /**
   * 
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
   * 
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
   * 
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
   * 
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
   * 
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
   * 
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
   * 
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
   * Private to prevent instantiation.
   */
  private DescriptionUtils() {
    super();
  }
}
