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

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;

/**
 */
public class NamespaceAttributeVerifier
    implements DescriptionConstants, SOAPConstants {
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
   * The WSDL targets.
   */
  private String[] wsdlTargets;

  /**
   * The SOAP targets.
   */
  private String[] soapTargets;

  /**
   * The forbidden flag.
   */
  private boolean forbidden;

  /**
   * 
   * @param document
   * @param requirement
   */
  public NamespaceAttributeVerifier(Document document, int requirement) {
    super();
    this.document = document;
    this.requirement = requirement;
  }

  /**
   * 
   * @throws EETest.Fault
   */
  public void verify() throws EETest.Fault {
    switch (requirement) {
    case 2716:
      style = SOAP_DOCUMENT;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME, WSDL_FAULT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_BODY_LOCAL_NAME, SOAP_HEADER_LOCAL_NAME,
          SOAP_HEADERFAULT_LOCAL_NAME, SOAP_FAULT_LOCAL_NAME };
      forbidden = true;
      break;

    case 2717:
      style = SOAP_RPC;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_BODY_LOCAL_NAME };
      forbidden = false;
      break;

    case 2726:
      style = SOAP_RPC;
      wsdlTargets = new String[] { WSDL_INPUT_LOCAL_NAME,
          WSDL_OUTPUT_LOCAL_NAME, WSDL_FAULT_LOCAL_NAME };
      soapTargets = new String[] { SOAP_HEADER_LOCAL_NAME,
          SOAP_HEADERFAULT_LOCAL_NAME, SOAP_FAULT_LOCAL_NAME };
      forbidden = true;
      break;

    default:
      throw new EETest.Fault(
          "The requirement 'R" + requirement + "' not supported");
    }

    Element[] bindings = DescriptionUtils.getBindings(document);
    for (int i = 0; i < bindings.length; i++) {
      verifyBinding(bindings[i]);
    }
  }

  protected void verifyBinding(Element binding) throws EETest.Fault {
    Element soapBinding = DescriptionUtils.getChildElement(binding,
        SOAP_NAMESPACE_URI, SOAP_BINDING_LOCAL_NAME);
    if (soapBinding == null) {
      return;
    }
    String style = soapBinding.getAttribute(SOAP_STYLE_ATTR);
    if (!style.equals(this.style)) {
      throw new EETest.Fault(
          "The literal style attribute does not match, expected " + this.style
              + "-literal, received " + style
              + "-literal, no further parsing test failed ...");
    }
    Element[] operations = DescriptionUtils.getChildElements(binding,
        WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME);
    for (int i = 0; i < operations.length; i++) {
      verifyOperation(operations[i]);
    }
  }

  protected void verifyOperation(Element operation) throws EETest.Fault {
    Element[] children = DescriptionUtils.getChildElements(operation,
        WSDL_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      String localName = children[i].getLocalName();
      for (int j = 0; j < wsdlTargets.length; j++) {
        if (localName.equals(wsdlTargets[j])) {
          verifyElement(children[i]);
          break;
        }
      }
    }
  }

  protected void verifyElement(Element element) throws EETest.Fault {
    Element[] children = DescriptionUtils.getChildElements(element,
        SOAP_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      String localName = children[i].getLocalName();
      for (int j = 0; j < soapTargets.length; j++) {
        if (localName.equals(soapTargets[j])) {
          verifySOAPElement(children[i]);
          break;
        }
      }
    }
  }

  protected void verifySOAPElement(Element element) throws EETest.Fault {
    String use = element.getAttribute(SOAP_USE_ATTR);
    if (use.length() == 0) {
      use = SOAP_LITERAL;
    }
    if (!use.equals(SOAP_LITERAL)) {
      return;
    }
    Attr attr = element.getAttributeNode(SOAP_NAMESPACE_ATTR);
    if (attr != null) {
      if (forbidden) {
        throw new EETest.Fault("The " + style + "-literal element 'soap:"
            + element.getLocalName()
            + "' has 'namespace' attribute defined (BP-R" + requirement + ")");
      }
      String namespace = attr.getValue();
      try {
        URL url = new URL(namespace);
      } catch (MalformedURLException e) {
        throw new EETest.Fault(
            "The " + style + "-literal element 'soap:" + element.getLocalName()
                + "' namespace attribute '" + namespace
                + "' is not a valid, absolute URI (BP-R" + requirement + ")",
            e);
      }
    } else {
      if (!forbidden) {
        throw new EETest.Fault(
            "The " + style + "-literal element 'soap:" + element.getLocalName()
                + "' does not have 'namespace' attribute defined (BP-R"
                + requirement + ")");
      }
    }
  }
}
