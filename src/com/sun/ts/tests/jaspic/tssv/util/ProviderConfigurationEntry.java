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

package com.sun.ts.tests.jaspic.tssv.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Raja Perumal
 */
public class ProviderConfigurationEntry implements Serializable {

  private String providerClassName = null;

  private Map<String,String> properties = null;

  private String messageLayer = null;

  private String applicationContextId = null;

  private String registrationDescription = null;

  /** Creates a new instance of ProviderConfigurationEntry */
  public ProviderConfigurationEntry(Node providerConfigEntryNode)
      throws Exception {
    Node childNode;
    String nodeName;

    // make sure the nodename is provider-config-entry
    if (!providerConfigEntryNode.getNodeName()
        .equals("provider-config-entry")) {
      throw new Exception(
          "Unexpected tag :" + providerConfigEntryNode.getNodeName());
    }
    NodeList nodes = providerConfigEntryNode.getChildNodes();

    for (int i = 0; i < nodes.getLength(); i++) {

      childNode = nodes.item(i);
      nodeName = childNode.getNodeName();

      // Skip empty text node processing
      if (nodeName.equals("#text"))
        continue;

      if (nodeName.equals("provider-class")) {
        providerClassName = getText(childNode);

      } else if (nodeName.equals("properties")) {
        properties = loadProperties(childNode);

      } else if (nodeName.equals("message-layer")) {
        messageLayer = getText(childNode);

      } else if (nodeName.equals("app-context-id")) {
        applicationContextId = getText(childNode);

      } else if (nodeName.equals("reg-description")) {
        registrationDescription = getText(childNode);
      }
    }

  }

  // This method loads a given Properties node such as the one shown below
  // and stores the values into a properties object called "properties"
  // <properties>
  // <entry key="AuthStatus_SEND_SUCCESS">true</entry>
  // <entry key="requestPolicy">USER_NAME_PASSWORD</entry>
  // </properties>
  private static Map<String,String> loadProperties(Node node) {
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

    HashMap<String,String> map = new HashMap<>();
    nodeProperties.stringPropertyNames().forEach((tmpkey) -> map.put(tmpkey, nodeProperties.getProperty(tmpkey)));
    return map;
  }

  public String getProviderClassName() {
    return providerClassName;
  }

  public String getMessageLayer() {
    return messageLayer;
  }

  public String getApplicationContextId() {
    return applicationContextId;
  }

  public String getRegistrationDescription() {
    return registrationDescription;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public String getText(Node textNode) {
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
}
