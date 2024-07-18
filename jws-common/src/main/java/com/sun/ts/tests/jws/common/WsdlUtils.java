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

/*
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jws.common;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

public class WsdlUtils implements DescriptionConstants, SOAPConstants,
    SchemaConstants, WSIConstants {

  private static final String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

  private static final String SOAP_NS = "http://schemas.xmlsoap.org/wsdl/soap/";

  private static final String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";

  private static URL baseURL = null;

  public static void setBaseUrl(String url) throws Exception {
    baseURL = new URL(url);
  }

  public static QName findBindingAttribute(Document document,
      QName serviceQName, QName portQName) throws Exception {

    Attr att = null;
    Attr bindingAttr = null;

    // Fix me find the binding element from the service/port element.

    Element elm = findServiceElement(document, serviceQName);

    if (elm == null)
      return null;

    Element portElement = findPortElement(document, elm, portQName);

    if (portElement != null) {

      att = portElement.getAttributeNode("name");

      if (att != null && att.getValue().equals(portQName.getLocalPart())) {

        // check if we need to use the QName here while getting the
        // Attribute

        bindingAttr = portElement.getAttributeNode("binding");

        if (bindingAttr != null) {

          return createQName(portElement, bindingAttr.getValue());
        }

      }
    }

    return null;

  }

  public static Element findGlobalElement(Document document, QName elementName)
      throws Exception {
    String elementTargetNS = elementName.getNamespaceURI();
    if (isEmpty(elementTargetNS)) {
      NodeList nodeList = document.getElementsByTagNameNS(WSDL_NS,
          "definitions");
      for (int i = 0; isEmpty(elementTargetNS)
          && i < nodeList.getLength(); i++) {
        Element definition = (Element) nodeList.item(i);
        elementTargetNS = definition.getAttribute("targetNamespace");
      }
    }

    NodeList nodeList = document.getElementsByTagNameNS(SCHEMA_NS, "schema");
    TestUtil.logMsg(" Length " + nodeList.getLength());
    for (int i = 0; i < nodeList.getLength(); i++) {
      Element schemaElement = (Element) nodeList.item(i);
      TestUtil.logMsg(" schemaElement " + schemaElement.getLocalName());
      String schemaTargetNS = schemaElement.getAttribute("targetNamespace");
      if (schemaTargetNS != null && schemaTargetNS.equals(elementTargetNS)) {
        NodeList globalElements = schemaElement.getChildNodes();
        for (int j = 0; j < globalElements.getLength(); j++) {
          Node node = globalElements.item(j);
          if (node instanceof Element) {
            Element globalElement = (Element) node;
            String globaleElementName = globalElement.getAttribute("name");
            if (globaleElementName != null
                && globaleElementName.equals(elementName.getLocalPart()))
              return globalElement;
          }
        }
      }
    }
    return null;
  }

  // parameter elementName is never used please remove it

  public static NodeList findImportStatements(Node node) {

    short nodeType = node.getNodeType();

    switch (nodeType) {

    case Node.DOCUMENT_NODE:

      return ((Document) node).getDocumentElement().getChildNodes();

    case Node.ELEMENT_NODE:

      return ((Element) node).getChildNodes();

    }

    return null;

  }

  private static Element findInputOutputOperationElement(Document document,
      Element operationElement, String direction) throws Exception {

    NodeList nodeList = null;

    if (document == null)
      return null;

    nodeList = operationElement.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (!(node instanceof Element))
        continue;
      Element inputOperationElement = (Element) nodeList.item(i);

      TestUtil.logMsg("inputOperationElement  : " + inputOperationElement);

      if (new QName(inputOperationElement.getNamespaceURI(),
          inputOperationElement.getLocalName())
              .equals(new QName(WSDL_NS, direction)))
        return inputOperationElement;

    }

    return null;

  }

  public static Element findMessageElement(Document document,
      QName portTypeName, QName operationQName, String direction)
      throws Exception {

    if (document == null)
      return null;

    String elementTargetNS;
    NodeList importList = null;
    String importUrl = null;
    Element definitions = null;
    Element portTypeElement = null;
    Element operationElement = null;
    QName messageQName = null;

    portTypeElement = findPortTypeElement(document, portTypeName);

    if (portTypeElement == null)
      return null;

    operationElement = findPortTypeOperationElement(portTypeElement,
        operationQName);

    if (operationElement == null)
      return null;

    messageQName = findMessageQName(operationElement, direction);

    if (messageQName == null)
      return null;

    NodeList nodeList = document.getDocumentElement().getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);
      if (!(node instanceof Element))
        continue;
      Element messageElement = (Element) node;

      if (!new QName(messageElement.getNamespaceURI(),
          messageElement.getLocalName()).equals(new QName(WSDL_NS, "message")))
        continue;

      String message = messageElement.getAttribute("name");

      QName messageName = new QName(elementTargetNS, message);

      if (message != null && messageQName.equals(messageName)) {
        return messageElement;
      }
    }

    importList = findImportStatements(document);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findMessageElement(document, portTypeName, operationQName,
            direction);

      }
    }

    return null;

  }

  public static QName findMessageQName(Node operation, String direction)
      throws Exception {

    if (operation == null)
      return null;

    NodeList nodeList = null;
    NodeList importList = null;
    String importUrl = null;
    Attr messageAttribute = null;

    // Can move the following snippet into a separate method

    short nodeType = operation.getNodeType();

    switch (nodeType) {

    case Node.DOCUMENT_NODE:

      nodeList = ((Document) operation).getDocumentElement().getChildNodes();

      break;

    case Node.ELEMENT_NODE:

      nodeList = ((Element) operation).getChildNodes();
      break;

    }

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (!(node instanceof Element))
        continue;
      Element messageElement = (Element) nodeList.item(i);

      if (!new QName(messageElement.getNamespaceURI(),
          messageElement.getLocalName()).equals(new QName(WSDL_NS, direction)))
        continue;

      messageAttribute = messageElement.getAttributeNode("message");

      if (messageAttribute != null) {

        return createQName(messageElement, messageAttribute.getValue());

      }

    }

    importList = findImportStatements(operation);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        Document document = getDocument(importUrl);
        return findMessageQName(document, direction);

      }
    }

    return null;

  }

  public static Element findOperationElement(Document document,
      QName operationName, QName serviceQName, QName portQName)
      throws Exception {

    NodeList nodeList = null;
    String elementTargetNS;
    Element definitions = null;

    elementTargetNS = getTargetNamespace(document);

    QName bindingQName = WsdlUtils.findBindingAttribute(document, serviceQName,
        portQName);

    if (bindingQName != null) {

      Element bindingElement = findSoapBindingElement(document, bindingQName);

      if (bindingElement != null) {

        nodeList = bindingElement.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

          Node node = nodeList.item(i);

          if (!(node instanceof Element))
            continue;
          Element operationElement = (Element) nodeList.item(i);

          if (!new QName(operationElement.getNamespaceURI(),
              operationElement.getLocalName())
                  .equals(new QName(WSDL_NS, "operation")))
            continue;

          String operation = operationElement.getAttribute("name");
          QName operationQName = new QName(elementTargetNS, operation);

          TestUtil.logMsg(" operation : " + operationQName);
          TestUtil.logMsg(" Expected operation : " + operationName);

          if (operationName.equals(operationQName))
            return operationElement;

        }

      }

    }

    return null;

  }

  public static Element findPartElement(Document document,
      Element messageElement, QName partName) throws Exception {

    return findPartElement(document, messageElement, partName, false);

  }

  public static Element findPartElement(Document document,
      Element messageElement, QName partName, boolean byElement)
      throws Exception {

    if (document == null)
      return null;

    String elementTargetNS;
    NodeList importList = null;
    String importUrl = null;
    Element definitions = null;

    NodeList nodeList = messageElement.getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);
      if (!(node instanceof Element))
        continue;
      Element partElement = (Element) node;

      if (!new QName(partElement.getNamespaceURI(), partElement.getLocalName())
          .equals(new QName(WSDL_NS, "part")))
        continue;

      QName partQName = null;
      String part = null;

      if (!byElement) {

        part = partElement.getAttribute("name");

        partQName = new QName(elementTargetNS, part);
      } else {
        part = partElement.getAttribute("element");

        partQName = createQName(partElement, part);
      }

      if (part != null && partName.equals(partQName)) {
        return partElement;
      }
    }

    importList = findImportStatements(messageElement);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findPartElement(document, messageElement, partName);

      }
    }

    return null;

  }

  public static Element findPortElement(Document document,
      Element serviceElement, QName portName) throws Exception {

    if (document == null)
      return null;

    String elementTargetNS;

    NodeList portElements = serviceElement.getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int j = 0; j < portElements.getLength(); j++) {
      Node node = portElements.item(j);
      if (node instanceof Element) {
        Element element = (Element) node;
        if (element != null && element.getLocalName().equals("port") && element
            .getNamespaceURI().equals(serviceElement.getNamespaceURI())) {
          String name = element.getAttribute("name");
          QName portQName = new QName(elementTargetNS,
              name.substring(name.indexOf(":") + 1));
          if (portQName.equals(portName))
            return element;
        }
      }
    }

    return null;
  }

  public static Element findPortTypeElement(Document document,
      QName portTypeName) throws Exception {

    if (document == null)
      return null;

    NodeList importList = null;
    String importUrl = null;
    String elementTargetNS;

    NodeList nodeList = document.getDocumentElement().getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);
      if (!(node instanceof Element))
        continue;
      Element portTypeElement = (Element) node;

      if (!new QName(portTypeElement.getNamespaceURI(),
          portTypeElement.getLocalName())
              .equals(new QName(WSDL_NS, "portType")))
        continue;

      String portType = portTypeElement.getAttribute("name");

      TestUtil.logMsg(" portType Name : " + portType);

      QName portTypeQName = new QName(elementTargetNS, portType);

      TestUtil.logMsg(" portType : " + portTypeQName.toString());
      TestUtil.logMsg(" Expected portType : " + portTypeName.toString());

      if (portTypeName.equals(portTypeQName)) {
        return portTypeElement;
      }
    }

    importList = findImportStatements(document);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findPortTypeElement(document, portTypeName);

      }
    }

    return null;
  }

  public static Element findPortTypeOperationElement(Node portType,
      QName operationQName) throws Exception {

    if (portType == null)
      return null;

    NodeList nodeList = null;
    NodeList importList = null;
    String importUrl = null;
    String elementTargetNS;

    Document document = null;

    short nodeType = portType.getNodeType();

    switch (nodeType) {

    case Node.DOCUMENT_NODE:

      nodeList = ((Document) portType).getDocumentElement().getChildNodes();
      document = (Document) portType;

      break;

    case Node.ELEMENT_NODE:

      nodeList = ((Element) portType).getChildNodes();

      document = ((Element) portType).getOwnerDocument();

    }

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (!(node instanceof Element))
        continue;
      Element operationElement = (Element) nodeList.item(i);

      if (!new QName(operationElement.getNamespaceURI(),
          operationElement.getLocalName())
              .equals(new QName(WSDL_NS, "operation")))
        continue;

      String operation = operationElement.getAttribute("name");

      QName operationName = new QName(elementTargetNS, operation);
      TestUtil.logMsg(" operation : " + operationName);
      TestUtil.logMsg(" Expected operation : " + operationQName);

      if (operationQName.equals(operationName)) {

        return operationElement;
      }

    }

    importList = findImportStatements(portType);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findPortTypeOperationElement(document, operationQName);

      }
    }

    return null;

  }

  // parameter elementName is never used please remove it

  public static Element findServiceElement(Document document, QName serviceName)
      throws Exception {

    if (document == null)
      return null;

    String elementTargetNS = null;
    NodeList importList = null;
    String importUrl = null;
    Element definitions = null;

    NodeList nodeList = document.getDocumentElement().getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);
      if (!(node instanceof Element))
        continue;
      Element serviceElement = (Element) node;

      if (!new QName(serviceElement.getNamespaceURI(),
          serviceElement.getLocalName()).equals(new QName(WSDL_NS, "service")))
        continue;

      String service = serviceElement.getAttribute("name");
      QName serviceQName = new QName(elementTargetNS,
          service.substring(service.indexOf(":") + 1));

      if (serviceName.equals(serviceQName)) {
        return serviceElement;
      }
    }

    importList = findImportStatements(document);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findServiceElement(document, serviceName);

      }
    }

    return null;
  }

  // parameter elementName is never used please remove it

  public static Element findSoapBindingElement(Document document,
      QName bindingName) throws Exception {

    if (document == null)
      return null;

    NodeList importList = null;
    String importUrl = null;
    String elementTargetNS = null;
    Element definitions = null;

    NodeList nodeList = document.getDocumentElement().getChildNodes();

    elementTargetNS = getTargetNamespace(document);

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);
      if (!(node instanceof Element))
        continue;
      Element bindingElement = (Element) nodeList.item(i);

      if (!new QName(bindingElement.getNamespaceURI(),
          bindingElement.getLocalName()).equals(new QName(WSDL_NS, "binding")))
        continue;

      String binding = bindingElement.getAttribute("name");

      QName bindingQName = new QName(elementTargetNS, binding);

      TestUtil.logMsg(" binding : " + bindingQName.toString());
      TestUtil.logMsg(" Expected binding : " + bindingName.toString());

      if (bindingName.equals(bindingQName)) {
        return bindingElement;
      }
    }

    importList = findImportStatements(document);

    TestUtil.logMsg(" Checking the imports");

    for (int k = 0; k < importList.getLength(); k++) {

      Node importNode = nodeList.item(k);

      if (!(importNode instanceof Element))
        continue;

      Element importElement = (Element) importNode;

      if (!new QName(importElement.getNamespaceURI(),
          importElement.getLocalName()).equals(new QName(WSDL_NS, "import")))
        continue;

      Attr locationAtt = importElement.getAttributeNode("location");

      if (locationAtt != null)
        importUrl = locationAtt.getValue();

      TestUtil.logMsg(" ImportedUrl : " + importUrl);

      if (importUrl != null) {

        document = getDocument(importUrl);
        return findSoapBindingElement(document, bindingName);

      }
    }

    return null;

  }

  public static Element findSoapHeaderElement(Document document,
      Element operationElement, String direction, String partName,
      QName portTypeQName, QName operationQName) throws Exception {

    NodeList nodeList = null;
    Element portTypeElement = null;
    Element operation = null;
    QName messageQName = null;

    if (document == null)
      return null;

    Element element = findInputOutputOperationElement(document,
        operationElement, direction);

    nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (!(node instanceof Element))
        continue;
      Element headerElement = (Element) nodeList.item(i);

      TestUtil.logMsg("headerElement  : " + headerElement);

      if (!new QName(headerElement.getNamespaceURI(),
          headerElement.getLocalName()).equals(new QName(SOAP_NS, "header")))
        continue;

      String part = headerElement.getAttribute("part");

      String message = headerElement.getAttribute("message");

      portTypeElement = findPortTypeElement(document, portTypeQName);

      if (portTypeElement == null)
        return null;

      operation = findPortTypeOperationElement(portTypeElement, operationQName);

      if (operation == null)
        return null;

      messageQName = findMessageQName(operation, direction);

      if (messageQName == null)
        return null;

      QName messageName = createQName(headerElement, message);

      TestUtil
          .logMsg(" partName = " + part + " MessageQname = " + messageQName);

      if (part.equals(partName) && messageQName.equals(messageName))
        return headerElement;

    }

    return null;

  }

  public static Element findSoapOperationElement(Document document,
      String soapAction, Element operationElement) throws Exception {

    NodeList nodeList = null;

    if (document == null)
      return null;

    nodeList = operationElement.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {

      Node node = nodeList.item(i);

      if (!(node instanceof Element))
        continue;
      Element soapOperationElement = (Element) nodeList.item(i);

      TestUtil.logMsg("soapOperationElement  : " + soapOperationElement);

      if (!new QName(soapOperationElement.getNamespaceURI(),
          soapOperationElement.getLocalName())
              .equals(new QName(SOAP_NS, "operation")))
        continue;

      String action = soapOperationElement.getAttribute("soapAction");

      if (soapAction.equals(action))
        return soapOperationElement;

    }

    return null;

  }

  private static String getTargetNamespace(Document document) {

    String elementTargetNS = null;

    Element rootElement = (Element) document.getDocumentElement();

    if (rootElement != null) {

      elementTargetNS = rootElement.getAttribute("targetNamespace");

      if (elementTargetNS == null)
        elementTargetNS = getDefaultNamespace(rootElement);
    }

    return elementTargetNS == null ? "" : elementTargetNS;

  }

  public static Document getDocument(String uri) throws Exception {
    return getDocument(uri, baseURL);
  }

  /**
   * @param location
   * @param context
   * @return Document
   * @throws com.sun.ts.lib.harness.EETest.Fault
   *
   */
  public static Document getDocument(String location, URL context)
      throws Exception {

    URL url;
    if (location.startsWith("http"))
      url = new URL(location);
    else
      url = new URL(context, location);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(url.openStream());

  }

  /**
   * returns the namespace for the prefix.
   *
   * @param prefix
   *          prefix
   * @param element
   *          element from which to findout the namespace.
   * @return namespace for this prefix. null if this prefix is not defined.
   */
  private static String getNamespace(String prefix, Element element) {
    String namespace = null;

    do {
      namespace = element.getAttribute("xmlns:" + prefix);
      if (namespace != null && !namespace.equals("")) {
        return namespace;
      }

      Node parent = element.getParentNode();
      if (parent instanceof Element)
        element = (Element) parent;
      else
        element = null;
    } while (element != null);

    return null;
  }

  private static boolean isEmpty(String s) {
    return (s == null || s.trim().length() == 0);
  }

  public static Element getElement(Node[] nodes) throws Exception {

    for (int i = 0; i < nodes.length; i++) {
      if (nodes[i] instanceof Element)
        return ((Element) nodes[i]);

    }
    return null;
  }

  public static Document getDocument(Node[] nodes) throws Exception {

    for (int i = 0; i < nodes.length; i++) {
      if (nodes[i] instanceof Document)
        return ((Document) nodes[i]);

    }
    return null;
  }

  public static Node[] getWrapperElement(Document doc, String ns, URL baseURL,
      String messageName) throws Exception {

    Document schemaDocs[] = getSchemaDocuments(doc, ns, baseURL);
    ArrayList aList = new ArrayList();
    Element wrapperElement = null;
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        wrapperElement = DescriptionUtils.getNamedChildElement(
            schemaDocs[i].getDocumentElement(), XSD_NAMESPACE_URI,
            XSD_ELEMENT_LOCAL_NAME, messageName);
        if (wrapperElement == null) {
          if (schemaDocs[i].getDocumentURI() != null)
            baseURL = new URL(schemaDocs[i].getDocumentURI());
          return getWrapperElement(schemaDocs[i], ns, baseURL, messageName);
        } else {
          aList.add(wrapperElement);
          aList.add(schemaDocs[i]);
          return (Node[]) aList.toArray(new Node[aList.size()]);
        }
      }
    }

    return null;
  }

  public static Node[] getComplexType(Document doc, Element element,
      URL baseURL) throws Exception {

    String type = element.getAttribute(XSD_TYPE_ATTR);
    String ref = element.getAttribute(XSD_REF_ATTR);
    createQName(element, ref);
    Node[] nodes = null;
    int index;
    String nameSpaceUri = null;

    if (type != null) {
      QName typeQName = createQName(element, type);
      nodes = getComplexTypeElementAndDocFromTypes(doc, baseURL, typeQName);
      if (nodes == null)
        nodes = getComplexTypeElementAndDoc(doc, baseURL, typeQName);
    } else if (ref != null) {

      // ref needs to be handeled.

    }

    return nodes;

  }

  public static Node[] getComplexTypeElementAndDocFromTypes(Document doc,
      URL baseURL, QName qName) throws Exception {

    ArrayList alist = new ArrayList();
    Element types = getTypes(doc, baseURL);
    if (types != null) {
      Element[] schemas = DescriptionUtils.getChildElements(types,
          XSD_NAMESPACE_URI, XSD_SCHEMA_LOCAL_NAME);
      Element[] elements = null;
      String nameSpace = null;
      TestUtil.logMsg("Trying to find ComplexType" + qName);
      for (int i = 0; i < schemas.length; i++) {
        nameSpace = schemas[i].getAttribute(XSD_TARGETNAMESPACE_ATTR);
        if (!schemas[i].getAttribute(XSD_TARGETNAMESPACE_ATTR)
            .equals((qName.getNamespaceURI())))
          continue;
        elements = DescriptionUtils.getChildElements(schemas[i],
            XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);

        for (int j = 0; j < elements.length; j++) {
          String nameAttr = elements[j].getAttribute(XSD_NAME_ATTR);
          if (nameAttr != null) {
            QName name = new QName(nameSpace, nameAttr);
            if (name.equals(qName)) {
              alist.add(doc);
              alist.add(elements[j]);
              return (Node[]) alist.toArray(new Node[alist.size()]);
            }
          }
        }
      }
    }
    return null;
  }

  private static Node[] getComplexTypeElementAndDocfromImports(
      Document[] schemaDocs, URL baseURL, QName qName) throws Exception {
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        return getComplexTypeElementAndDoc(schemaDocs[i], baseURL, qName);
      }
    }
    return null;
  }

  public static Node[] getComplexTypeElementAndDoc(Document doc, URL baseURL,
      QName qName) throws Exception {
    Document schemaDocs[] = getSchemaDocuments(doc, qName.getNamespaceURI(),
        baseURL);
    ArrayList aList = new ArrayList();
    Element complexTypeElement = null;
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        complexTypeElement = DescriptionUtils.getNamedChildElement(
            schemaDocs[i].getDocumentElement(), XSD_NAMESPACE_URI,
            XSD_COMPLEXTYPE_LOCAL_NAME, qName.getLocalPart());
        if (complexTypeElement != null) {
          aList.add(complexTypeElement);
          aList.add(schemaDocs[i]);
          return (Node[]) aList.toArray(new Node[aList.size()]);
        }
      }
      return getComplexTypeElementAndDocfromImports(schemaDocs, baseURL, qName);
    }
    return null;
  }

  public static Element getParameterRefElement(NodeList list, QName qName)
      throws Exception {
    for (int i = 0; i < list.getLength(); i++) {
      Element ele = ((Element) list.item(i));
      String refAttr = ele.getAttribute(XSD_REF_ATTR);
      if (refAttr != null) {
        QName name = createQName(ele, refAttr);
        if (name.equals(qName)) {
          return ele;
        }
      }
    }
    return null;
  }

  public static Element getParameterElement(Document doc, NodeList list,
      QName qName) throws Exception {

    for (int i = 0; i < list.getLength(); i++) {
      Element ele = ((Element) list.item(i));
      String nameAttr = ele.getAttribute(XSD_NAME_ATTR);
      if (nameAttr != null) {
        QName name = new QName(DescriptionUtils.getTargetNamespaceAttr(doc),
            nameAttr);
        if (name.equals(qName)) {
          return ele;
        }
      }
    }
    return null;
  }

  public static Node[] getParameterElement(Document doc, URL baseURL,
      QName qName) throws Exception {

    ArrayList alist = new ArrayList();
    Element[] elements = DescriptionUtils.getChildElements(
        doc.getDocumentElement(), XSD_NAMESPACE_URI, XSD_ELEMENT_LOCAL_NAME);
    for (int i = 0; i < elements.length; i++) {
      String nameAttr = elements[i].getAttribute(XSD_NAME_ATTR);
      if (nameAttr != null) {
        QName name = new QName(DescriptionUtils.getTargetNamespaceAttr(doc),
            nameAttr);
        if (name.equals(qName)) {
          alist.add(doc);
          alist.add(elements[i]);
          return (Node[]) alist.toArray(new Node[alist.size()]);
        }
      }
    }
    return getParameterElementfromImports(doc, baseURL, qName);
  }

  public static Node[] getParameterElementfromImports(Document doc, URL baseURL,
      QName qName) throws Exception {

    Document schemaDocs[] = getSchemaDocuments(doc, qName.getNamespaceURI(),
        baseURL);
    ArrayList aList = new ArrayList();
    Element parameterElement = null;
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        parameterElement = DescriptionUtils.getNamedChildElement(
            schemaDocs[i].getDocumentElement(), XSD_NAMESPACE_URI,
            XSD_ELEMENT_LOCAL_NAME, qName.getLocalPart());
        if (parameterElement == null) {
          return getParameterElementfromImports(schemaDocs[i], baseURL, qName);
        } else {
          aList.add(parameterElement);
          aList.add(schemaDocs[i]);
          return (Node[]) aList.toArray(new Node[aList.size()]);
        }
      }
    }

    return null;
  }

  /**
   * @param document
   * @return Element
   */
  public static Element getSchemasElementName(Document document, QName qName,
      URL baseURL) {
    Element types = getTypes(document, baseURL);
    if (types != null) {
      Element[] schemas = DescriptionUtils.getChildElements(types,
          XSD_NAMESPACE_URI, XSD_SCHEMA_LOCAL_NAME);
      Element[] elements;
      for (int i = 0; i < schemas.length; i++) {
        elements = DescriptionUtils.getChildElements(schemas[i],
            XSD_NAMESPACE_URI, XSD_ELEMENT_LOCAL_NAME);
        String targetNameSpace = schemas[i]
            .getAttribute(XSD_TARGETNAMESPACE_ATTR);
        for (int j = 0; j < elements.length; j++) {
          if (elements[j].getAttribute(XSD_NAME_ATTR)
              .equals(qName.getLocalPart())
              && targetNameSpace.equals(qName.getNamespaceURI()))
            return elements[j];
        }
      }
    }
    return null;
  }

  /**
   * @param document
   * @return Element
   */
  public static Element getTypes(Document document, URL baseURL) {
    Element[] children = DescriptionUtils.getChildElements(
        document.getDocumentElement(), WSDL_NAMESPACE_URI,
        WSDL_TYPES_LOCAL_NAME);
    if (children.length != 0)
      return children[0];
    else
      return getTypesFromImports(document, baseURL);
  }

  public static Element getTypesFromImports(Document document, URL baseURL) {
    Element[] imports = DescriptionUtils.getImports(document);
    for (int i = 0; i < imports.length; i++) {
      String location = imports[i].getAttribute(WSDL_LOCATION_ATTR);
      String namespace = imports[i].getAttribute(WSDL_NAMESPACE_ATTR);
      try {
        Document newDoc = DescriptionUtils.getDocumentFromLocation(location,
            baseURL);
        if (newDoc.getDocumentURI() != null)
          baseURL = new URL(newDoc.getDocumentURI());
        Element element = getTypes(newDoc, baseURL);
        if (element != null)
          return element;
      } catch (Exception e) {
        e.printStackTrace(System.err);
        break;
      }
    }
    return null;
  }

  public static String[] checkParameterOrder(Document document, URL baseURL,
      Element complexType, QName[] qNames) throws Exception {

    String targetNameSpace = null;
    boolean pass = false;
    String names[] = new String[3];

    targetNameSpace = DescriptionUtils.getTargetNamespaceAttr(document);
    TestUtil.logMsg("Got Complex Type Element : " + targetNameSpace + ":"
        + complexType.getAttribute(XSD_NAME_ATTR));
    NodeList list = document.getDocumentElement()
        .getElementsByTagNameNS(XSD_NAMESPACE_URI, XSD_ELEMENT_LOCAL_NAME);
    for (int i = 0; i < qNames.length; i++) {
      Element parameterRefElement = getParameterRefElement(list, qNames[i]);
      if (parameterRefElement != null) {
        Node[] parameterNodes = getParameterElement(document, baseURL,
            qNames[i]);
        if (parameterNodes != null) {
          Element parameterElement = getElement(parameterNodes);
          if (parameterElement != null) {
            String name = createQName(parameterElement,
                parameterElement.getAttribute(XSD_NAME_ATTR)).getLocalPart();
            names[i] = name;
          }
        }
      } else {
        Element parameterElement = getParameterElement(document, list,
            qNames[i]);
        if (parameterElement != null) {
          String name = createQName(parameterElement,
              parameterElement.getAttribute(XSD_NAME_ATTR)).getLocalPart();
          names[i] = name;
        }
      }
    }
    return names;
  }

  public static boolean checkQNamesEquals(String[] names1, String[] names2) {

    if (names1 == null && names2 == null)
      return true;
    if (names1 == null || names2 == null)
      return false;

    if (names1.length != names2.length)
      return false;

    for (int i = 0; i < names1.length; i++) {
      if (!names1[i].trim().equals(names2[i].trim())) {
        return false;
      }
    }

    return true;

  }

  /**
   * create a QName from an attribute value.
   *
   * @param element
   *          element form which to resolve the namespace from the prefix.
   * @param value
   *          qname value like "prefix:value"
   * @param value
   *          qname value like "prefix:value"
   * @return QName created form the prefix
   * @throws Exception
   *           failed to find namespace for the specified prefix.
   */

  public static QName createQName(Element element, String value)
      throws Exception {

    int index = (value == null ? -1 : value.indexOf(":"));

    if (index == -1) {
      String ns = getDefaultNamespace(element);
      return ns == null ? new QName(value) : new QName(ns, value);
    }

    String prefix = value.substring(0, index);
    String namespace = getNamespaceOfPrefix(element, prefix);

    if (namespace == null) {
      throw new Exception("Unable to find namespace for prefix '" + prefix
          + "'. This is used in element " + element);
    }

    return new QName(namespace, value.substring(index + 1, value.length()));
  }

  /**
   * @param document
   * @param url
   * @return Document[]
   * @throws com.sun.ts.lib.harness.EETest.Fault
   *
   */
  public static Document[] getSchemaDocuments(Document document, String ns,
      URL url) throws EETest.Fault {

    Element[] imports = DescriptionUtils.getSchemaImportElements(document);
    ArrayList alist = new ArrayList();
    for (int i = 0; i < imports.length; i++) {
      String namespace = imports[i].getAttribute(XSD_NAMESPACE_ATTR);
      if (namespace.equals(ns)) {
        String schemaLocation = imports[i]
            .getAttribute(XSD_SCHEMALOCATION_ATTR);
        Document thedocument = DescriptionUtils
            .getDocumentFromLocation(schemaLocation, url);
        thedocument.getDocumentElement().setAttribute("SchemaFile",
            schemaLocation);
        alist.add(thedocument);

      }
    }

    if (alist.size() == 0)
      return null;
    else
      return (Document[]) alist.toArray(new Document[alist.size()]);

  }

  /**
   * returns the namespace for the prefix.
   *
   * @param prefix
   *          prefix
   * @param element
   *          element from which to findout the namespace.
   * @return namespace for this prefix. null if this prefix is not defined.
   */
  public static String getNamespaceOfPrefix(Element element, String prefix) {
    String namespace = null;
    if (prefix == null)
      return getDefaultNamespace(element);
    do {
      namespace = element.getAttribute("xmlns:" + prefix);
      if (namespace != null && !namespace.equals("")) {
        return namespace;
      }

      Node parent = element.getParentNode();
      if (parent instanceof Element)
        element = (Element) parent;
      else
        element = null;
    } while (element != null);

    return null;
  }

  /**
   * Returns the default namespace for the element, or null if there is none.
   *
   * @param element
   *          element for which to return default ns.
   * @return default namespace, or null.
   */
  public static String getDefaultNamespace(Element element) {
    String namespace = null;

    do {
      namespace = element.getAttribute("xmlns");
      if (namespace != null) {
        if (namespace.length() == 0) {
          // The attribute value in a default namespace declaration
          // MAY
          // be empty. This has the same effect, within the scope of
          // the
          // declaration, of there being no default namespace.
          return null;
        } else {
          return namespace;
        }
      }

      Node parent = element.getParentNode();
      if (parent instanceof Element)
        element = (Element) parent;
      else
        element = null;
    } while (element != null);

    return null;
  }
}
