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

package com.sun.ts.tests.jaxws.wsi.j2w;

import java.util.StringTokenizer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;

/**
 * 
 */
public class PartAttributeVerifier
    implements DescriptionConstants, SOAPConstants {
  /**
   * The WSDL targets.
   */
  private static final String[] WSDL_TARGETS = { WSDL_INPUT_LOCAL_NAME,
      WSDL_OUTPUT_LOCAL_NAME, WSDL_FAULT_LOCAL_NAME };

  /**
   * The SOAP targets.
   */
  private static final String[] SOAP_TARGETS = { SOAP_BODY_LOCAL_NAME,
      SOAP_HEADER_LOCAL_NAME, SOAP_HEADERFAULT_LOCAL_NAME };

  /**
   * The document.
   */
  private Document document;

  /**
   * The requirement.
   */
  private int requirement;

  /**
   * The style (document or rpc).
   */
  private String style;

  /**
   * The attribute (element or type).
   */
  private String attribute;

  /**
   * The WSDL targets.
   */
  private String[] wsdlTargets;

  /**
   * The SOAP targets.
   */
  private String[] soapTargets;

  /**
   * The reverse check flag.
   */
  private boolean reverse;

  /**
   * 
   * @param document
   * @param requirement
   */
  public PartAttributeVerifier(Document document, int requirement) {
    super();
    this.document = document;
    this.requirement = requirement;
  }

  public void verify() throws EETest.Fault {
    switch (requirement) {
    case 2203:
      style = SOAP_RPC;
      attribute = WSDL_TYPE_ATTR;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_BODY_LOCAL_NAME };
      reverse = false;
      break;

    case 2204:
      style = SOAP_DOCUMENT;
      attribute = WSDL_ELEMENT_ATTR;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_BODY_LOCAL_NAME };
      reverse = false;
      break;

    case 2205:
      style = null;
      attribute = WSDL_ELEMENT_ATTR;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME, WSDL_FAULT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_HEADER_LOCAL_NAME,
          SOAP_HEADERFAULT_LOCAL_NAME, SOAP_FAULT_LOCAL_NAME };
      reverse = false;
      break;

    case 2207:
      style = SOAP_RPC;
      attribute = WSDL_ELEMENT_ATTR;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_BODY_LOCAL_NAME };
      reverse = true;
      break;

    default:
      throw new EETest.Fault(
          "The requirement 'R" + requirement + "' not supported");
    }
    System.out.println("Check for bindings on wsdl ...");
    Element[] bindings = DescriptionUtils.getBindings(document);
    if (!doBindings(bindings)) {
      System.out.println("Check for bindings on wsdl imports ...");
      Element[] imports = DescriptionUtils.getImports(document);
      for (int i = 0; i < imports.length; i++) {
        bindings = getBindingFromImport(imports[i]);
        if (doBindings(bindings))
          break;
      }
    }
  }

  protected boolean doBindings(Element[] bindings) throws EETest.Fault {
    boolean done;
    if (bindings.length != 0) {
      System.out.println("Got bindings so verify ...");
      for (int i = 0; i < bindings.length; i++) {
        verifyBinding(bindings[i]);
      }
      done = true;
    } else
      done = false;
    return done;
  }

  protected Element[] getBindingFromImport(Element element)
      throws EETest.Fault {
    String location = element.getAttribute(WSDL_LOCATION_ATTR);
    String namespace = element.getAttribute(WSDL_NAMESPACE_ATTR);
    Document document = DescriptionUtils.getDocumentFromLocation(location);
    return DescriptionUtils.getBindings(document);
  }

  protected void verifyBinding(Element binding) throws EETest.Fault {
    Element soapBinding = DescriptionUtils.getChildElement(binding,
        SOAP_NAMESPACE_URI, SOAP_BINDING_LOCAL_NAME);
    if (soapBinding == null) {
      return;
    }
    String style = soapBinding.getAttribute(SOAP_STYLE_ATTR);
    if ((this.style != null) && (!this.style.equals(style))) {
      return;
    }
    Element[] operations = DescriptionUtils.getChildElements(binding,
        WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME);
    for (int i = 0; i < operations.length; i++) {
      verifyOperation(binding, operations[i]);
    }
  }

  protected void verifyOperation(Element binding, Element operation)
      throws EETest.Fault {
    Element[] children = DescriptionUtils.getChildElements(operation,
        WSDL_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      if (isWSDLTarget(children[i])) {
        verifyElement(binding, operation, children[i]);
      }
    }
  }

  protected void verifyElement(Element binding, Element operation,
      Element element) throws EETest.Fault {
    String name = element.getAttribute(WSDL_NAME_ATTR);
    Element message = getMessage(binding, operation, element.getLocalName(),
        name);
    Element[] children = DescriptionUtils.getChildElements(element,
        SOAP_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      if (isSOAPTarget(children[i])) {
        verifySOAPElement(children[i], message);
      }
    }
  }

  protected Element getMessage(Element binding, Element operation,
      String localName, String name) throws EETest.Fault {
    String type;
    int index;
    type = binding.getAttribute(WSDL_TYPE_ATTR);
    index = type.indexOf(':');
    if (index > 0) {
      type = type.substring(index + 1);
    }
    Element portType = DescriptionUtils.getPortType(document, type);
    if (portType == null) {
      throw new EETest.Fault("Required wsdl:portType element named '" + type
          + "' not found (BP-R" + requirement + ")");
    }
    String operationName = operation.getAttribute(WSDL_NAME_ATTR);
    operation = DescriptionUtils.getNamedChildElement(portType,
        WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME, operationName);
    if (operation == null) {
      throw new EETest.Fault("Required wsdl:operation element named '"
          + operationName + "' not found (BP-R" + requirement + ")");
    }
    Element element = DescriptionUtils.getNamedChildElement(operation,
        WSDL_NAMESPACE_URI, localName, name);
    if (element == null) {
      throw new EETest.Fault("Required wsdl:" + localName + " element named '"
          + name + "' not found (BP-R" + requirement + ")");
    }
    type = element.getAttribute(WSDL_MESSAGE_ATTR);
    index = type.indexOf(':');
    if (index > 0) {
      type = type.substring(index + 1);
    }
    Element message = DescriptionUtils.getMessage(document, type);
    if (message == null) {
      throw new EETest.Fault("Required wsdl:message named '" + type
          + "' not found (BP-R" + requirement + ")");
    }
    return message;
  }

  protected void verifySOAPElement(Element element, Element message)
      throws EETest.Fault {
    String use = element.getAttribute(SOAP_USE_ATTR);
    if (use.length() == 0) {
      use = SOAP_LITERAL;
    }
    if (!use.equals(SOAP_LITERAL)) {
      return;
    }
    Attr attr = element.getAttributeNode(SOAP_PARTS_ATTR);
    if (attr == null) {
      return;
    }
    String parts = attr.getValue();
    StringTokenizer tokenizer = new StringTokenizer(parts, " ");
    while (tokenizer.hasMoreTokens()) {
      String part = tokenizer.nextToken();
      verifyMessagePart(message, part);
    }
  }

  protected void verifyMessagePart(Element message, String name)
      throws EETest.Fault {
    Element part = DescriptionUtils.getNamedChildElement(message,
        WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME, name);
    if (part == null) {
      throw new EETest.Fault("Referenced wsdl:part named '" + name
          + "' not found (BP-R" + requirement + ")");
    }
    Attr attr = part.getAttributeNode(attribute);
    if (attr != null) {
      if (reverse) {
        throw new EETest.Fault(
            "Referenced wsdl:part named '" + name + "' defined using '"
                + attribute + "' attribute (BP-R" + requirement + ")");
      }
    } else {
      if (!reverse) {
        throw new EETest.Fault(
            "Referenced wsdl:part named '" + name + "' not defined using '"
                + attribute + "' attribute (BP-R" + requirement + ")");
      }
    }
  }

  protected boolean isWSDLTarget(Element element) {
    if (!element.getNamespaceURI().equals(WSDL_NAMESPACE_URI)) {
      return false;
    }
    String localName = element.getLocalName();
    for (int i = 0; i < wsdlTargets.length; i++) {
      if (localName.equals(wsdlTargets[i])) {
        return true;
      }
    }
    return false;
  }

  protected boolean isSOAPTarget(Element element) {
    if (!element.getNamespaceURI().equals(SOAP_NAMESPACE_URI)) {
      return false;
    }
    String localName = element.getLocalName();
    for (int i = 0; i < soapTargets.length; i++) {
      if (localName.equals(soapTargets[i])) {
        return true;
      }
    }
    return false;
  }

  protected void logMsg(String message) {
    TestUtil.logMsg(message);
  }
}
