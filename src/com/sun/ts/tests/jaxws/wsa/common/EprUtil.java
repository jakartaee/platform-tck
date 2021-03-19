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
 * $Id: EprUtil.java 51075 2003-03-27 10:44:21Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.common;

import com.sun.ts.lib.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.transform.dom.DOMResult;
import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxws.wsa.common.DOMUtil;
import com.sun.ts.tests.jaxws.wsa.common.XmlUtil;
import com.sun.ts.tests.jaxws.common.XMLUtils;
import com.sun.ts.tests.jaxws.wsa.common.MemberSubmissionEndpointReference;

public class EprUtil {
  private static final String FAILED = "FAILED";

  private static final String W3C_WSA_NS = "http://www.w3.org/2005/08/addressing";

  private static final String W3C_WSAM_NS = "http://www.w3.org/2007/05/addressing/metadata";

  private static final String MS_EPR_NS = "http://schemas.xmlsoap.org/ws/2004/08/addressing";

  private static final QName WSDL_DEFINITIONS_NAME = new QName(
      "http://schemas.xmlsoap.org/wsdl/", "definitions");

  private static boolean debug = false;

  public static boolean validateEPR(Node node, Class epr,
      String endpointAddress, QName serviceName, QName portName,
      QName portTypeName, boolean hasWSDL, String wsdlLocation) {
    boolean pass1 = validateEPR(node, epr, endpointAddress, serviceName,
        portName, portTypeName, hasWSDL);
    boolean pass2 = validateWsdlLocation(node, wsdlLocation);
    if (pass1 && pass2)
      return true;
    else
      return false;
  }

  public static boolean validateEPR(Node node, Class epr,
      String endpointAddress, QName serviceName, QName portName,
      QName portTypeName, boolean hasWSDL) {

    boolean pass = true;

    if (node.getNodeType() == Node.DOCUMENT_NODE)
      node = node.getFirstChild();

    if (endpointAddress != null)
      TestUtil.logMsg("EndpointAddress=" + endpointAddress);
    if (serviceName != null)
      TestUtil.logMsg("ServiceName=" + serviceName.getLocalPart());
    if (portName != null)
      TestUtil.logMsg("PortName=" + portName.getLocalPart());
    if (portTypeName != null)
      TestUtil.logMsg("PortTypeName=" + portTypeName.getLocalPart());
    if (hasWSDL)
      TestUtil.logMsg("WSDL=" + hasWSDL);
    TestUtil.logMsg("EPR class=" + epr);

    if (epr.isAssignableFrom(W3CEndpointReference.class)) {
      TestUtil.logMsg("Verify <EndpointReference> element");
      if (!node.getNamespaceURI().equals(W3C_WSA_NS)) {
        TestUtil.logErr("Incorrect namespace uri, got: "
            + node.getNamespaceURI() + " expected: " + W3C_WSA_NS);
        pass = false;
      }
      if (!node.getLocalName().equals("EndpointReference")) {
        TestUtil.logErr("Incorrect element name, got: " + node.getLocalName()
            + " expected: EndpointReference");
        pass = false;
      }

      if (endpointAddress == null)
        return pass;

      Element elm = (Element) node;
      Element add = DOMUtil.getFirstElementChild(node);
      String value = XmlUtil.getTextForNode(add);
      TestUtil.logMsg("Verify <Address> element");
      if (!value.equals(endpointAddress)) {
        TestUtil.logErr("Incorrect endpoint address, got: " + value
            + " expected: " + endpointAddress);
        pass = false;
      }
      if (serviceName == null && portName == null && !hasWSDL) {
        // Need not check metadata
        return pass;
      }
      TestUtil.logMsg("Verify the EPR MetaData");
      NodeList nl = elm.getElementsByTagNameNS(W3C_WSA_NS, "Metadata");
      if (nl.getLength() != 1) {
        TestUtil.logErr("Could not find <MetaData>");
        return false;
      }
      Element metdata = (Element) nl.item(0);
      String qname;
      String prefix;
      String name;
      String ns;
      // service
      if (serviceName != null) {
        TestUtil.logMsg("Verify <ServiceName> element");
        nl = metdata.getElementsByTagNameNS(W3C_WSAM_NS, "ServiceName");
        if (nl.getLength() != 1) {
          TestUtil.logErr("Could not find <ServiceName> in <MetaData>");
          return false;
        }
        Node service = nl.item(0);
        qname = XmlUtil.getTextForNode(service);
        prefix = XmlUtil.getPrefix(qname);
        name = XmlUtil.getLocalPart(qname);
        ns = service.lookupNamespaceURI(prefix);
        if (debug)
          XMLUtils.xmlDumpDOMNodes(service);
        if (debug)
          TestUtil.logMsg(
              "prefix=" + prefix + " localPart=" + name + " namespace=" + ns);
        if (!serviceName.getNamespaceURI().equals(ns)) {
          TestUtil.logErr("Incorrect namespace uri, got: " + ns + " expected: "
              + serviceName.getNamespaceURI());
          pass = false;
          if (ns == null || ns.equals(""))
            TestUtil
                .logErr("Namespace uri is null for QName element: " + qname);
        }
        if (!serviceName.getLocalPart().equals(name)) {
          TestUtil.logErr("Incorrect service name, got: "
              + serviceName.getLocalPart() + " expected: " + name);
          pass = false;
        }
        // port
        if (portName != null) {
          TestUtil.logMsg("Verify <EndpointName> element");
          String port = ((Element) service).getAttribute("EndpointName");
          prefix = XmlUtil.getPrefix(port);
          name = XmlUtil.getLocalPart(port);
          ns = service.lookupNamespaceURI(prefix);
          if (debug)
            TestUtil.logMsg(
                "prefix=" + prefix + " localPart=" + name + " namespace=" + ns);
          if (!portName.getLocalPart().equals(name)) {
            TestUtil.logErr("Incorrect port name, got: " + name + " expected: "
                + portName.getLocalPart());
            pass = false;
          }
        }
      }

      if (hasWSDL) {
        // validate portType only if it is present
        TestUtil.logMsg("Verify <InterfaceName> element");
        nl = metdata.getElementsByTagNameNS(W3C_WSAM_NS, "InterfaceName");
        if (nl.getLength() != 1) {
          TestUtil.logMsg(
              "The <InterfaceName> element is not present in <MetaData>");
          return true;
        }
        Node portType = nl.item(0);
        qname = XmlUtil.getTextForNode(portType);
        prefix = XmlUtil.getPrefix(qname);
        name = XmlUtil.getLocalPart(qname);
        ns = portType.lookupNamespaceURI(prefix);
        if (debug)
          XMLUtils.xmlDumpDOMNodes(portType);
        if (debug)
          TestUtil.logMsg(
              "prefix=" + prefix + " localPart=" + name + " namespace=" + ns);
        if (!portTypeName.getNamespaceURI().equals(ns)) {
          TestUtil.logErr("Incorrect namespace uri, got: " + ns + " expected:"
              + portTypeName.getNamespaceURI());
          pass = false;
          if (ns == null || ns.equals(""))
            TestUtil
                .logErr("Namespace uri is null for QName element: " + qname);
        }
        if (!portTypeName.getLocalPart().equals(name)) {
          TestUtil.logErr("Incorrect port type name, got: " + name
              + " expected:" + portTypeName.getLocalPart());
          pass = false;
        }

        // validate WSDL
        nl = metdata.getElementsByTagNameNS(
            WSDL_DEFINITIONS_NAME.getNamespaceURI(),
            WSDL_DEFINITIONS_NAME.getLocalPart());
        Node wsdl = nl.item(0);
        // TODO:What else to do to validate this WSDL?
      }
      return pass;
    } else if (epr.isAssignableFrom(MemberSubmissionEndpointReference.class)) {
      TestUtil.logMsg("Verify <EndpointReference> element");
      if (!node.getNamespaceURI().equals(MS_EPR_NS)) {
        TestUtil.logErr("Incorrect namespace uri, got: "
            + node.getNamespaceURI() + " expected: " + MS_EPR_NS);
        pass = false;
      }
      if (!node.getLocalName().equals("EndpointReference")) {
        TestUtil.logErr("Incorrect element name, got: " + node.getLocalName()
            + " expected: EndpointReference");
        pass = false;
      }

      Element elm = (Element) node;
      Element add = DOMUtil.getFirstElementChild(node);
      String value = XmlUtil.getTextForNode(add);
      TestUtil.logMsg("Verify <Address> element");
      if (!value.equals(endpointAddress)) {
        TestUtil.logErr("Incorrect endpoint address, got: " + value
            + " expected: " + endpointAddress);
        pass = false;
      }
      NodeList nl;
      String qname;
      String prefix;
      String name;
      String ns;
      // service
      if (serviceName != null) {
        TestUtil.logMsg("Verify <ServiceName> element");
        nl = elm.getElementsByTagNameNS(MS_EPR_NS, "ServiceName");
        if (nl.getLength() != 1) {
          TestUtil.logErr("Could not find <ServiceName> in <MetaData>");
          return false;
        }
        Node service = nl.item(0);
        qname = XmlUtil.getTextForNode(service);
        prefix = XmlUtil.getPrefix(qname);
        name = XmlUtil.getLocalPart(qname);
        ns = service.lookupNamespaceURI(prefix);
        if (!serviceName.getNamespaceURI().equals(ns)) {
          TestUtil.logErr("Incorrect namespace uri, got: " + ns + " expected: "
              + serviceName.getNamespaceURI());
          pass = false;
          if (ns == null || ns.equals(""))
            TestUtil
                .logErr("Namespace uri is null for QName element: " + qname);
        }
        if (!serviceName.getLocalPart().equals(name)) {
          TestUtil.logErr("Incorrect service name, got: "
              + serviceName.getLocalPart() + " expected: " + name);
          pass = false;
        }
        // port
        if (portName != null) {
          TestUtil.logMsg("Verify <EndpointName> element");
          String port = ((Element) service).getAttribute("PortName");
          if (!portName.getLocalPart().equals(port)) {
            TestUtil.logErr("Incorrect port name, got: " + port + " expected:"
                + portName.getLocalPart());
            pass = false;
          }
        }
      }

      if (hasWSDL) {
        // validate portType
        TestUtil.logMsg("Verify <InterfaceName> element");
        nl = elm.getElementsByTagNameNS(MS_EPR_NS, "PortType");
        if (nl.getLength() != 1) {
          TestUtil.logMsg(
              "The <InterfaceName> element is not present in <MetaData>");
          return true;
        }
        Node portType = nl.item(0);
        qname = XmlUtil.getTextForNode(portType);
        prefix = XmlUtil.getPrefix(qname);
        name = XmlUtil.getLocalPart(qname);
        ns = portType.lookupNamespaceURI(prefix);
        if (!portTypeName.getNamespaceURI().equals(ns)) {
          TestUtil.logErr("Incorrect namespace uri, got: " + ns + " expected:"
              + portTypeName.getNamespaceURI());
          pass = false;
          if (ns == null || ns.equals(""))
            TestUtil
                .logErr("Namespace uri is null for QName element: " + qname);
        }
        if (!portTypeName.getLocalPart().equals(name)) {
          TestUtil.logErr("Incorrect port type name, got: " + name
              + " expected:" + portTypeName.getLocalPart());
          pass = false;
        }
      }
      if (hasWSDL) {
        // validate WSDL
        nl = elm.getElementsByTagNameNS(WSDL_DEFINITIONS_NAME.getNamespaceURI(),
            WSDL_DEFINITIONS_NAME.getLocalPart());
        Node wsdl = nl.item(0);
        // TODO:What else to do to validate this WSDL?
      }

      return pass;
    }
    return false;
  }

  public static boolean validateEPR(EndpointReference epr,
      String endpointAddress, QName serviceName, QName portName,
      QName portTypeName, boolean hasWSDL) {
    if (epr != null) {
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      Node node = dr.getNode();
      return validateEPR(node, epr.getClass(), endpointAddress, serviceName,
          portName, portTypeName, hasWSDL);
    } else
      return false;
  }

  public static boolean validateEPR(EndpointReference epr,
      String endpointAddress, QName serviceName, QName portName,
      QName portTypeName, boolean hasWSDL, String wsdlLocation) {
    if (epr != null) {
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      Node node = dr.getNode();
      return validateEPR(node, epr.getClass(), endpointAddress, serviceName,
          portName, portTypeName, hasWSDL, wsdlLocation);
    } else
      return false;
  }

  public static boolean validateReferenceParameter(Node n, String refParamName,
      String refParamValue) {
    TestUtil.logMsg("Verify Reference Parameter " + refParamName
        + " with value " + refParamValue);
    String actual = XMLUtils.getNodeValue_(n, refParamName);
    if ((actual != null) && (actual.equals(refParamValue))) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean validateWsdlLocation(Node n, String wsdlLocation) {
    TestUtil.logMsg(
        "Verify <wsdlLocation> attribute with value of " + wsdlLocation);
    Node node1 = XMLUtils.findNode_(n, "Metadata");
    String actual = XMLUtils.getAttrValue_(node1, "wsdlLocation", wsdlLocation);
    if (debug) {
      TestUtil.logMsg("wsdlLocation=" + wsdlLocation);
      TestUtil.logMsg("actual=" + actual);
    }
    if (actual != null) {
      int index = wsdlLocation.indexOf('?');
      if (index != -1) {
        wsdlLocation = wsdlLocation.substring(0, index)
            + wsdlLocation.substring(index).toUpperCase();
      }
      index = actual.indexOf('?');
      if (index != -1) {
        actual = actual.substring(0, index)
            + actual.substring(index).toUpperCase();
      }
    }
    if ((actual != null) && (actual.indexOf(wsdlLocation) != -1)) {
      return true;
    }
    /*****
     * WE DON'T NEED TO SEARCH for wsdl:import TestUtil.logMsg("Search
     * wsdl:import element for location attribute"); Node node2 =
     * XMLUtils.findNode_(n, "import"); actual = XMLUtils.getAttrValue_(node2,
     * "location", wsdlLocation); if((actual != null) &&
     * (actual.indexOf(wsdlLocation) != -1)){ return true;
     *****/
    TestUtil.logErr("<wsdlLocation> attribute with value of '" + wsdlLocation
        + "' was not found");
    return false;
  }
}
