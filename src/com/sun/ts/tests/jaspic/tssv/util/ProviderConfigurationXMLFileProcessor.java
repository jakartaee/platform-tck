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

package com.sun.ts.tests.jaspic.tssv.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Raja Perumal
 */
public class ProviderConfigurationXMLFileProcessor {

  private static Collection<ProviderConfigurationEntry> providerConfigurationEntriesCollection = new Vector<ProviderConfigurationEntry>();

  private static Document document = null;

  private static File providerConfigFile = null;

  /** Creates a new instance of ProviderConfigurationXMLFileReader */
  public ProviderConfigurationXMLFileProcessor(String fileName)
      throws Exception {

    try {
      providerConfigurationEntriesCollection.clear(); // XXXX:

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();

      documentBuilder.setEntityResolver(new DTDResolver());

      if (fileName != null)
        providerConfigFile = new File(fileName);

      if (!providerConfigFile.exists()) {
        throw new Exception(
            "Provider Config File : " + fileName + " does not exists");
      } else {

        FileInputStream fis = new FileInputStream(providerConfigFile);

        // Parse of the content of the file into Document
        document = documentBuilder.parse(fis);

        // get the root element "provider-config"
        Element rootElement = document.getDocumentElement();

        // get the childNodes inside the rootElement
        // The ChildNodes are instances of "provider-config-entry" elements
        NodeList nodes = rootElement.getChildNodes();

        // For each "provider-config-entry" element load all the properties
        // and add the ProviderConfigurationEntry into the collection
        setProviderConfigEntryCollection(nodes);
      }

    } catch (ParserConfigurationException pce) {
      throw new Exception("PaserConfigurationException :" + pce.getMessage());
    } catch (SAXException se) {
      throw new Exception("SAXException :" + se.getMessage());
    } catch (IOException ioe) {
      throw new Exception("IOException :" + ioe.getMessage());

    } catch (SecurityException se) {
      throw new Exception("SecurityException :" + se.getMessage());
    }
  }

  private void setProviderConfigEntryCollection(NodeList nodes)
      throws Exception {

    Node providerConfigEntryNode;
    NodeList providerConfigEntryNodeChildren;

    for (int i = 0; i < nodes.getLength(); i++) {
      // Take the first node
      providerConfigEntryNode = nodes.item(i);
      providerConfigEntryNodeChildren = providerConfigEntryNode.getChildNodes();

      // Skip empty text node processing
      if (providerConfigEntryNode.getNodeName().equals("#text"))
        continue;

      ProviderConfigurationEntry pce = new ProviderConfigurationEntry(
          providerConfigEntryNode);
      providerConfigurationEntriesCollection.add(pce);
    }
  }

  public Collection<ProviderConfigurationEntry> getProviderConfigurationEntriesCollection() {
    return providerConfigurationEntriesCollection;
  }

  // This method creates a new provider-config-entry node and adds it to the
  // root (provider-config) element.
  // On successful insertion of provider-config-entry this method returns true
  public static boolean addProviderConfigEntry(String className, Map props,
      String messageLayer, String appContextId, String description) {

    boolean result = false;

    Node providerConfigEntry = createProviderConfigEntry(className, props,
        messageLayer, appContextId, description);

    boolean alreadyExists = checkIfAlreadyPresent(providerConfigEntry);

    // If the currentNode doesn't exists in the configuration file then
    // add the current node
    if (!alreadyExists) {

      // get the root element "provider-config"
      Element rootElement = document.getDocumentElement();
      try {
        rootElement.appendChild(providerConfigEntry);
        updateProviderConfigurationXMLFile();
        result = true;
      } catch (DOMException dome) {
        result = false;
      }
    }

    return result;
  }

  // This method creates a new provider-config-entry node
  //
  public static Node createProviderConfigEntry(String className, Map props,
      String messageLayer, String appContextId, String description) {

    Element providerConfigEntry = null;

    // get the root element "provider-config"
    Element rootElement = document.getDocumentElement();

    // create provider-config-entry
    providerConfigEntry = (Element) document
        .createElement("provider-config-entry");

    if (className != null) {
      // create provider-class
      Element providerClassEntry = (Element) document
          .createElement("provider-class");
      Text classNameText = document.createTextNode(className);
      providerClassEntry.appendChild(classNameText);

      // Add provider-class to provider-config-entry
      providerConfigEntry.appendChild(providerClassEntry);
    }

    if (props != null) {
      // create properties
      Element propertiesNode = (Element) document.createElement("properties");

      // Iterate through the map of properties(props) and add all the
      // entries
      Set entries = props.entrySet();
      Iterator iterator = entries.iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry) iterator.next();
        Element entryNode = (Element) document.createElement("entry");
        entryNode.setAttribute("key", entry.getKey().toString());
        Text keyValueText = document
            .createTextNode(entry.getValue().toString());
        entryNode.appendChild(keyValueText);
        propertiesNode.appendChild(entryNode);
        // System.out.println(entry.getKey() + " : "
        // + entry.getValue());
      }
      providerConfigEntry.appendChild(propertiesNode);
    }

    if (messageLayer != null) {
      // create message-layer
      Element messageLayerEntry = (Element) document
          .createElement("message-layer");
      Text messageLayerText = document.createTextNode(messageLayer);
      messageLayerEntry.appendChild(messageLayerText);

      // Add message-layer to provider-config-entry
      providerConfigEntry.appendChild(messageLayerEntry);
    }

    if (appContextId != null) {
      // create app-context-id
      Element appContextIdEntry = (Element) document
          .createElement("app-context-id");
      Text appContextIdText = document.createTextNode(appContextId);
      appContextIdEntry.appendChild(appContextIdText);

      // Add app-context-id to provider-config-entry
      providerConfigEntry.appendChild(appContextIdEntry);
    }

    if (description != null) {
      // create reg-description
      Element regDescriptionEntry = (Element) document
          .createElement("reg-description");
      Text regDescriptionText = document.createTextNode(description);
      regDescriptionEntry.appendChild(regDescriptionText);

      // Add provider-class to provider-config-entry
      providerConfigEntry.appendChild(regDescriptionEntry);
    }

    return providerConfigEntry;
  }

  // This method uses the given string (className, messageLayer,
  // appContextId and description) and compares them with the contents of
  // provider-config-entry nodes, if the given strings matches for a given node
  // then that node will be deleted. This method return false if the given
  // strings doesn't match with any of the provider-config-entry nodes.
  public static boolean deleteProviderConfigEntry(String className,
      String messageLayer, String appContextId, String description) {

    boolean result = false;

    Node providerConfigEntry = createProviderConfigEntry(className, null,
        messageLayer, appContextId, description);

    boolean alreadyExists = checkIfAlreadyPresent(providerConfigEntry);

    // get the root element "provider-config"
    Element rootElement = document.getDocumentElement();

    if (alreadyExists) {
      try {
        rootElement.removeChild(providerConfigEntry);
        updateProviderConfigurationXMLFile();
        result = true;
      } catch (DOMException dome) {
        result = false;
      }

    }

    return result;
  }

  // This method uses the given string (className, messageLayer,
  // appContextId and description) and compares them with the contents of
  // provider-config-entry nodes, if the all strings matches for a given node
  // then this method returns the corresponding provider-config-entry node,
  // if the given strings doesn't match with any of the provider-config-entry
  // node then this method returns null
  public static boolean checkIfAlreadyPresent(Node node) {

    Node topLevelChildNode = null;
    String topLevelNodeName;
    boolean result = false;

    // get the root element "provider-config"
    Element rootElement = document.getDocumentElement();

    NodeList nodes = rootElement.getChildNodes();

    for (int i = 0; i < nodes.getLength(); i++) {

      topLevelChildNode = nodes.item(i);
      topLevelNodeName = topLevelChildNode.getNodeName();

      // Skip empty text node processing
      if (topLevelNodeName.equals("#text"))
        continue;

      // Check whether the given node is same as the current
      // provider-config-entry node
      if (topLevelNodeName.equals("provider-config-entry")) {
        topLevelChildNode.normalize();
        node.normalize();

        // printNode(topLevelChildNode, i );
        // printNode(node, i);

        if (compareNode(topLevelChildNode, node)) {
          return true;
        }
      }
    }

    return result;
  }

  private static boolean compareNode(Node source, Node target) {

    NodeList topLevelChildren;
    Node childNode;
    String nodeName;

    boolean classNameMatch = false;
    boolean propertiesMatch = true;
    boolean messageLayerMatch = false;
    boolean appContextIdMatch = false;
    boolean descriptionMatch = false;

    String className = null;
    String messageLayer = null;
    String appContextId = null;
    Map properties = null;
    String description = null;

    String targetClassName = null;
    String targetMessageLayer = null;
    String targetAppContextId = null;
    String targetDescription = null;

    // get the root element "provider-config"

    // Process Source node and get className, messageLayer,
    // properties, appContextId and description values
    topLevelChildren = source.getChildNodes();
    for (int j = 0; j < topLevelChildren.getLength(); j++) {

      childNode = topLevelChildren.item(j);
      nodeName = childNode.getNodeName();

      // Skip empty text node processing
      if (nodeName.equals("#text"))
        continue;

      if (nodeName.equals("provider-class")) {
        className = childNode.getFirstChild().getNodeValue().trim();

      }
      if (nodeName.equals("properties")) {
        properties = getPropertiesMap(childNode);

      } else if (nodeName.equals("message-layer")) {
        messageLayer = childNode.getFirstChild().getNodeValue().trim();

      } else if (nodeName.equals("app-context-id")) {
        appContextId = childNode.getFirstChild().getNodeValue().trim();

      } else if (nodeName.equals("reg-description")) {
        description = childNode.getFirstChild().getNodeValue().trim();
      }
    }

    // Process target node and get className, messageLayer,
    // properties, appContextId and description values
    topLevelChildren = target.getChildNodes();
    for (int j = 0; j < topLevelChildren.getLength(); j++) {

      childNode = topLevelChildren.item(j);
      nodeName = childNode.getNodeName();

      // Skip empty text node processing
      if (nodeName.equals("#text"))
        continue;

      if (nodeName.equals("provider-class")) {
        targetClassName = childNode.getFirstChild().getNodeValue().trim();

      }
      if (nodeName.equals("properties")) {
        // targetProperties=getPropertiesMap(childNode);
        // propertiesMatch = matchProperties(childNode, properties);

      } else if (nodeName.equals("message-layer")) {
        targetMessageLayer = childNode.getFirstChild().getNodeValue().trim();

      } else if (nodeName.equals("app-context-id")) {
        targetAppContextId = childNode.getFirstChild().getNodeValue().trim();

      } else if (nodeName.equals("reg-description")) {
        targetDescription = childNode.getFirstChild().getNodeValue().trim();
      }
    }

    if (stringCompare(className, targetClassName)) {
      classNameMatch = true;
    }
    if (stringCompare(messageLayer, targetMessageLayer)) {
      messageLayerMatch = true;
    }
    if (stringCompare(appContextId, targetAppContextId)) {
      appContextIdMatch = true;
    }
    if (stringCompare(description, targetDescription)) {
      descriptionMatch = true;
    }

    if (classNameMatch && propertiesMatch && messageLayerMatch
        && appContextIdMatch && descriptionMatch) {
      // if both node values are same then return true;
      return true;
    }

    return false;
  }

  private static boolean stringCompare(String source, String target) {
    boolean result = false;

    if ((source == null) && (target == null)) {
      result = true;
    } else if ((source != null) && target != null) {
      if (source.equals(target)) {
        result = true;
      }
    }
    return result;
  }

  // This method reads a Properties node such as the one shown below
  // and returns properties Map from its values
  //
  // <properties>
  // <entry key="AuthStatus_SEND_SUCCESS">true</entry>
  // <entry key="requestPolicy">USER_NAME_PASSWORD</entry>
  // </properties>
  private static Map getPropertiesMap(Node node) {
    Node topLevelChildNode = null;
    String topLevelNodeName;
    String key = null;
    String value = null;
    NamedNodeMap namedNodeMap = null;
    Map nodeProperties = new Properties();

    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {

      topLevelChildNode = nodes.item(i);
      topLevelNodeName = topLevelChildNode.getNodeName();

      // Skip empty text node processing
      if (topLevelNodeName.equals("#text"))
        continue;

      if (topLevelNodeName.equals("entry")) {
        namedNodeMap = topLevelChildNode.getAttributes();
        Node tempKeyNode = namedNodeMap.getNamedItem("key");
        key = tempKeyNode.getNodeValue();
        value = topLevelChildNode.getFirstChild().getNodeValue();
        nodeProperties.put(key, value);
      }

    }

    return nodeProperties;
  }

  // This method reads a Properties node such as the one shown below
  // and constructs a properties Map from its values and compares that map with
  // the passed verifyProperties Map, if both are equal this
  // method returns true else false
  //
  // <properties>
  // <entry key="AuthStatus_SEND_SUCCESS">true</entry>
  // <entry key="requestPolicy">USER_NAME_PASSWORD</entry>
  // </properties>
  private static boolean matchProperties(Node node, Map verifyProperties) {
    Node topLevelChildNode = null;
    String topLevelNodeName;
    String key = null;
    String value = null;
    NamedNodeMap namedNodeMap = null;
    Map nodeProperties = new Properties();
    boolean result = false;

    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {

      topLevelChildNode = nodes.item(i);
      topLevelNodeName = topLevelChildNode.getNodeName();

      // Skip empty text node processing
      if (topLevelNodeName.equals("#text"))
        continue;

      if (topLevelNodeName.equals("entry")) {
        namedNodeMap = topLevelChildNode.getAttributes();
        Node tempKeyNode = namedNodeMap.getNamedItem("key");
        key = tempKeyNode.getNodeValue();
        value = topLevelChildNode.getFirstChild().getNodeValue();
        nodeProperties.put(key, value);
      }

    }
    if (nodeProperties.equals(verifyProperties))
      result = true;

    return result;
  }

  private static void updateProviderConfigurationXMLFile() {

    // Style Sheet to indent a given XML file
    String styleSheet = "<xsl:stylesheet "
        + "    xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" "
        + "    xmlns:xalan=\"http://xml.apache.org/xslt\">"
        + "  <xsl:output method=\"xml\" indent=\"yes\" xalan:indent-amount=\"4\"/>"
        + "  <xsl:template match=\"* | @*\">" + "    <xsl:copy>"
        + "      <xsl:copy-of select=\"@*\"/>" + "      <xsl:apply-templates/>"
        + "    </xsl:copy>" + "  </xsl:template>" + "</xsl:stylesheet>";

    try {

      StreamSource styleSource = new StreamSource(
          new ByteArrayInputStream(styleSheet.getBytes()));

      // Use a Transformer for output
      TransformerFactory tFactory = TransformerFactory.newInstance();

      // Apply transformation specified by the stylesheet to indent the
      // generated XML file
      Transformer transformer = tFactory.newTransformer(styleSource);

      // If indentation affects performance, then we can also use
      // simple transformation as shown below
      // Transformer transformer = tFactory.newFtransformer();

      DOMSource source = new DOMSource(document);

      // Get the DOCTYPE
      String systemValue = (new File(document.getDoctype().getSystemId()))
          .getName();

      // Delete the original ProviderConfiguration file
      if (providerConfigFile.exists()) {
        providerConfigFile.delete();
        providerConfigFile.createNewFile();
      }

      FileOutputStream fos = new FileOutputStream(providerConfigFile);

      // StreamResult result = new StreamResult(System.out);
      StreamResult result = new StreamResult(fos);

      // Add Doctype to the output xml file
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);

      // Indent the XML file
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      // set the output type as xml
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");

      // Do identity transformation into the result stream
      transformer.transform(source, result);

      fos.close();

    } catch (TransformerConfigurationException tce) {
      // Error generated by the parser
      System.out.println("\n** Transformer Factory error");
      System.out.println("   " + tce.getMessage());

      // Use the contained exception, if any
      Throwable x = tce;
      if (tce.getException() != null)
        x = tce.getException();
      x.printStackTrace();

    } catch (TransformerException te) {
      // Error generated by the parser
      System.out.println("\n** Transformation error");
      System.out.println("   " + te.getMessage());

      // Use the contained exception, if any
      Throwable x = te;
      if (te.getException() != null)
        x = te.getException();
      x.printStackTrace();

    } catch (IOException ioe) {
      // I/O error
      ioe.printStackTrace();
    }

  }

  private static void printNodes(NodeList nodes) {

    Node topLevelChildNode;
    String topLevelNodeName;

    int nodeCount = 1;

    for (int i = 0; i < nodes.getLength(); i++) {

      topLevelChildNode = nodes.item(i);
      topLevelNodeName = topLevelChildNode.getNodeName();

      // Skip empty text node processing
      if (topLevelNodeName.equals("#text"))
        continue;

      printNode(topLevelChildNode, nodeCount++);

    }
  }

  private static void printNode(Node node, int index) {
    Node childNode;
    String nodeName;
    NodeList topLevelChildren;

    System.out.println(" ");
    topLevelChildren = node.getChildNodes();

    for (int j = 0; j < topLevelChildren.getLength(); j++) {

      childNode = topLevelChildren.item(j);
      nodeName = childNode.getNodeName();

      // Skip empty text node processing
      if (nodeName.equals("#text"))
        continue;

      if (nodeName.equals("provider-class")) {
        System.out
            .println(index + " ) " + "provider-class = " + getText(childNode));

      } else if (nodeName.equals("properties")) {
        printProperties(childNode);

      } else if (nodeName.equals("message-layer")) {
        System.out
            .println(index + " ) " + "message-layer = " + getText(childNode));

      } else if (nodeName.equals("app-context-id")) {
        System.out
            .println(index + " ) " + "app-context-id = " + getText(childNode));

      } else if (nodeName.equals("reg-description")) {
        System.out
            .println(index + " ) " + "reg-description = " + getText(childNode));
      }
    }
    System.out.println("-----------------");

  }

  // This method prints a Properties node such as the one shown below
  //
  // <properties>
  // <entry key="AuthStatus_SEND_SUCCESS">true</entry>
  // <entry key="requestPolicy">USER_NAME_PASSWORD</entry>
  // </properties>
  private static void printProperties(Node node) {
    Node topLevelChildNode = null;
    String topLevelNodeName;
    String key = null;
    String value = null;
    NamedNodeMap namedNodeMap = null;
    Properties nodeProperties = new Properties();

    NodeList nodes = node.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {

      topLevelChildNode = nodes.item(i);
      topLevelNodeName = topLevelChildNode.getNodeName();

      // Skip empty text node processing
      if (topLevelNodeName.equals("#text"))
        continue;

      if (topLevelNodeName.equals("entry")) {
        namedNodeMap = topLevelChildNode.getAttributes();
        Node tempKeyNode = namedNodeMap.getNamedItem("key");
        key = tempKeyNode.getNodeValue();
        value = topLevelChildNode.getFirstChild().getNodeValue();
        nodeProperties.put(key, value);
      }
    }

    if ((nodeProperties != null) && (!nodeProperties.isEmpty())) {
      nodeProperties.list(System.out);
    } else {
      System.out.println("No Properties to list");
    }
  }

  private static String getText(Node textNode) {
    String result = "";
    NodeList nodes = textNode.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.TEXT_NODE) {
        result = node.getNodeValue();
        break;
      }
    }
    if (result != null)
      result = result.trim();
    return result;
  }

  public class DTDResolver implements EntityResolver {
    public InputSource resolveEntity(String publicID, String systemId)
        throws SAXException {
      String providerConfigurationFile = null;
      String providerConfigurationFileLocation = null;
      int indexof = 0;

      // Obtain the full path for the location of ProviderConfiguration.xml file
      // From this value identify the directory of this file using indexOf()
      providerConfigurationFile = System
          .getProperty("provider.configuration.file");
      if (providerConfigurationFile != null) {

        indexof = providerConfigurationFile
            .indexOf("ProviderConfiguration.xml");
        if (indexof > 0) {
          // Get the directory location of ProviderConfiguration.xml file
          providerConfigurationFileLocation = providerConfigurationFile
              .substring(0, indexof);
        }

        if (systemId.contains("provider-configuration.xsd")) {
          // location of schema is at <javaee.home.ri>/lib/schemas(from ts.jte)
          String schemaLocation = System.getProperty("schema.file.location");
          return new InputSource(
              schemaLocation + File.separator + "provider-configuration.xsd");
        }
      }
      // if there is no match
      return null;
    }
  }

}
