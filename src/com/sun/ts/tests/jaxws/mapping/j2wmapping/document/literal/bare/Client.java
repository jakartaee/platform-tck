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

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.bare;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.DescriptionClient;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SchemaConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;
import com.sun.ts.tests.jaxws.wsi.j2w.NamespaceAttributeVerifier;

import com.sun.ts.tests.jaxws.common.*;

public class Client extends ServiceEETest
    implements DescriptionConstants, SOAPConstants, SchemaConstants {
  /*
   * The client.
   */
  private DescriptionClient client;

  private DescriptionClient client2;

  /*
   * The document.
   */
  private Document document;

  /**
   * The generated WSDL file.
   */
  private static final String WSDLLOC_URL = "j2wdlbare.wsdlloc.1";

  private static final String WSDLLOC_URL2 = "j2wdlbare.wsdlloc.2";

  private static final String TSHOME = "ts.home";

  private String baseURL, wsdlFile, wsdlFile2;

  private String tshome = null;

  private String wsdlFileUrl = null;

  private String wsdlFileUrl2 = null;

  private static final String EXPECTED_TARGETNAMESPACE = "http://doclitservice.org/wsdl";

  private static final String EXPECTED_PORTTYPE_NAME = "J2WDLSharedEndpoint";

  private static final String EXPECTED_PORT_NAME = "J2WDLSharedEndpointPort";

  private static final String EXPECTED_SERVICE_NAME = "J2WDLSharedService";

  private static final String EXPECTED_ADDRESSING_PORTTYPE_NAME = "AddressingEndpoint";

  private boolean debug = false;

  // private methods

  private boolean verifyGlobalElement(String prefix, String name)
      throws EETest.Fault {

    TestUtil.logMsg("Verifying Global Element " + prefix + ":" + name);

    Document schemaDoc = DescriptionUtils
        .getSchemaDocument(client.getDocument(), prefix, tshome + baseURL);
    if (schemaDoc != null) {
      boolean found = DescriptionUtils.findGlobalElementByName(schemaDoc, name);
      if (found) {
        TestUtil.logMsg("global element found in schema doc for " + name);
        return true;
      } else {
        TestUtil.logErr("global element not found in schema doc for " + name);
        return false;
      }
    } else {
      if (DescriptionUtils.getSchemaElementName(client.getDocument(),
          name) != null) {
        TestUtil.logMsg("global element found in schema types for " + name);
        return true;
      } else {
        TestUtil.logErr("global element not found in schema types for " + name);
        return false;
      }

    }
  }

  private boolean verifyPrefixNamespace(String prefix) throws EETest.Fault {

    TestUtil.logMsg("Verifying prefix namespace matches targetNamespace");

    String targetNamespace = DescriptionUtils
        .getTargetNamespaceAttr(client.getDocument());
    String namespace = DescriptionUtils
        .getNamespaceOfPrefix(client.getDocument(), prefix);
    if (namespace == null || targetNamespace == null) {
      return false;
    } else if (!namespace.equals(targetNamespace)) {
      TestUtil.logErr(
          "The namespace of global element does not match targetNamespace of WSDL definitions:");
      TestUtil.logErr("Target Namespace=" + targetNamespace);
      TestUtil.logErr("Element Namespace=" + namespace);
      return false;
    }
    return true;
  }

  /*
   * Test entry point.
   * 
   */
  public static void main(String[] args) {
    Client test = new Client();
    Status status = test.run(args, System.out, System.err);
    status.exit();
  }

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: ts.home;
   */
  public void setup(String[] args, Properties properties) throws Fault {
    tshome = properties.getProperty(TSHOME);
    try {
      wsdlFile = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    } catch (Exception e) {
      throw new Fault("Failed getting wsdl prop", e);
    }
    baseURL = wsdlFile.substring(0, wsdlFile.lastIndexOf("/") + 1);
    wsdlFileUrl = "file:" + tshome + wsdlFile;
    TestUtil.logMsg("wsdlFileUrl=" + wsdlFileUrl);
    client = new DescriptionClient();
    client.setURL(wsdlFileUrl);
    try {
      wsdlFile2 = JAXWS_Util.getURLFromProp(WSDLLOC_URL2);
    } catch (Exception e) {
      throw new Fault("Failed getting wsdl prop", e);
    }
    baseURL = wsdlFile2.substring(0, wsdlFile2.lastIndexOf("/") + 1);
    wsdlFileUrl2 = "file:" + tshome + wsdlFile2;
    TestUtil.logMsg("wsdlFileUrl2=" + wsdlFileUrl2);
    client2 = new DescriptionClient();
    client2.setURL(wsdlFileUrl2);
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  /*
   * @testName: VerifyServiceNameWithAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3037;
   *
   * @test_Strategy: Verify the wsdl:service element name is correct in the
   * generated WSDL. Conformance requirement done: - service naming
   *
   */
  public void VerifyServiceNameWithAnnotation() throws Fault {
    TestUtil.logMsg("VerifyServiceNameWithAnnotation");
    boolean pass = true;

    TestUtil.logMsg(
        "Checking for service name verification of: " + EXPECTED_SERVICE_NAME);
    pass = DescriptionUtils.isServiceNameAttr(client.getDocument(),
        EXPECTED_SERVICE_NAME);
    if (!pass)
      throw new Fault("VerifyServiceNameWithAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyPortTypeNameWithAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify the wsdl:portType name is correct in the generated
   * WSDL. Conformance requirement done: - portType naming
   *
   *
   */
  public void VerifyPortTypeNameWithAnnotation() throws Fault {
    TestUtil.logMsg("VerifyPortTypeNameWithAnnotation");
    boolean pass = true;

    TestUtil.logMsg("Checking for portType name verification of: "
        + EXPECTED_PORTTYPE_NAME);
    pass = DescriptionUtils.isPortTypeNameAttr(client.getDocument(),
        EXPECTED_PORTTYPE_NAME);
    if (!pass)
      throw new Fault("VerifyPortTypeNameWithAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyPortNameWithoutAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify the wsdl:port name is correct in the generated WSDL.
   * Conformance requirement done: - port naming
   *
   *
   */
  public void VerifyPortNameWithoutAnnotation() throws Fault {
    TestUtil.logMsg("VerifyPortNameWithoutAnnotation");
    boolean pass = true;

    TestUtil.logMsg(
        "Checking for port name verification of: " + EXPECTED_PORT_NAME);
    pass = DescriptionUtils.isPortNameAttr(client.getDocument(),
        EXPECTED_PORT_NAME);
    if (!pass)
      throw new Fault("VerifyPortNameWithoutAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyWsdlMessagePartAndAttrStyle
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3026; JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3035;
   * JAXWS:SPEC:3033; JAXWS:SPEC:3034;
   *
   * @test_Strategy: SOAPBinding annotation with style of DOCUMENT, a use of
   * LITERAL and a parameterStyle of BARE. Verify each message has a single
   * wsdl:part child element that refers, via an element attribute to a global
   * element declaration. The namespace name of the input and output global
   * elements is the value of the targetNamespace attribute of the WSDL
   * definitions element.
   *
   */
  public void VerifyWsdlMessagePartAndAttrStyle() throws Fault {
    TestUtil.logMsg("VerifyWsdlMessagePartAndAttrStyle");
    boolean pass = true;

    TestUtil
        .logMsg("Getting all input and output messages for portType operations"
            + " in portType " + EXPECTED_PORTTYPE_NAME);
    String imessages[] = DescriptionUtils
        .getInputMessageNames(client.getDocument(), EXPECTED_PORTTYPE_NAME);
    Arrays.sort(imessages);
    for (int i = 0; i < imessages.length; i++)
      TestUtil.logMsg("input message: " + imessages[i]);
    String omessages[] = DescriptionUtils
        .getOutputMessageNames(client.getDocument(), EXPECTED_PORTTYPE_NAME);
    Arrays.sort(omessages);
    for (int i = 0; i < omessages.length; i++)
      TestUtil.logMsg("output message: " + omessages[i]);

    Element messages[] = DescriptionUtils.getMessages(client.getDocument());
    for (int i = 0; i < messages.length; i++) {
      String messageName = messages[i].getAttribute(WSDL_NAME_ATTR);
      TestUtil.logMsg("Checking message: " + messageName);
      if (Arrays.binarySearch(imessages, messageName) >= 0
          || Arrays.binarySearch(omessages, messageName) >= 0) {
        Element[] parts = DescriptionUtils.getChildElements(messages[i],
            WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
        for (int j = 0; j < parts.length; j++) {
          String part = parts[j].getAttribute(WSDL_NAME_ATTR);
          String element = parts[j].getAttribute(WSDL_ELEMENT_ATTR);
          TestUtil.logMsg("    part=" + part + ", element=" + element);
          if (element == null || element.equals("")) {
            TestUtil.logErr("    no element attribute for this part");
            pass = false;
          } else {
            String prefix = element.substring(0, element.indexOf(":"));
            String name = element.substring(element.indexOf(":") + 1);
            if (!verifyGlobalElement(prefix, name))
              pass = false;
            if (!verifyPrefixNamespace(prefix))
              pass = false;

          }
        }
      } else
        TestUtil.logMsg("Skipping message parts checking ...");
    }

    if (!pass)
      throw new Fault("VerifyWsdlMessagePartAndAttrStyle failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: ActionFaultActionAndAddressingAnnotationsTest
   *
   * @assertion_ids: JAXWS:JAVADOC:130; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:144; JAXWS:SPEC:3043; JAXWS:SPEC:3044; JAXWS:SPEC:3045;
   * JAXWS:SPEC:3049; JAXWS:SPEC:3050; JAXWS:SPEC:3051; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   * 
   * @test_Strategy: Verify the Action and FaultAction annotations are correct
   * in the generated WSDL.
   */
  public void ActionFaultActionAndAddressingAnnotationsTest() throws Fault {
    TestUtil.logTrace("ActionFaultActionAndAddressingAnnotationsTest");
    boolean pass = true;

    if (!VerifyBindingHasAddressing())
      pass = false;
    if (!VerifyActionFaultAction())
      pass = false;

    if (!pass)
      throw new Fault("ActionFaultActionAndAddressingAnnotationsTest failed");
  }

  private boolean VerifyBindingHasAddressing() throws Fault {
    TestUtil.logMsg("VerifyBindingHasAddressing");
    boolean pass = false;

    Element[] bindings = DescriptionUtils.getBindings(client2.getDocument());
    TestUtil.logMsg(
        "Find wsam:Addressing policys via [wsp:Policy] or [wsp:PolicyReference] tags");
    NodeList policyrefs = bindings[0].getElementsByTagNameNS(WSP_NAMESPACE_URI,
        WSP_POLICYREFERENCE_LOCAL_NAME);
    Element[] policys = DescriptionUtils.getChildElements(client2.getDocument(),
        WSP_NAMESPACE_URI, WSP_POLICY_LOCAL_NAME);
    if (policyrefs.getLength() == 0 && policys.length == 0) {
      TestUtil.logErr("No wsam:Addressing policy element was found on binding");
      return false;
    }
    TestUtil.logMsg(
        "Search for wsam:Addressing policy element via wsp:PolicyReference tag on binding");
    for (int i = 0; i < policyrefs.getLength(); i++) {
      Element element = (Element) policyrefs.item(i);
      String uri = element.getAttribute("URI");
      for (int j = 0; j < policys.length; j++) {
        NamedNodeMap map = policys[j].getAttributes();
        for (int k = 0; k < map.getLength(); k++) {
          Node node = map.item(k);
          String nodeValue = "#" + node.getNodeValue();
          if (uri.equals(nodeValue)) {
            NodeList list = policys[j].getElementsByTagNameNS(
                WSAM_NAMESPACE_URI, WSAM_ADDRESSING_LOCAL_NAME);
            Element addressing = (Element) list.item(0);
            if (addressing != null) {
              pass = true;
              TestUtil.logMsg("See if for wsp:Optional attribute exists");
              NamedNodeMap map2 = addressing.getAttributes();
              for (int m = 0; m < map2.getLength(); m++) {
                Node node2 = map2.item(m);
                if (node2.getNodeName().equals(WSP_OPTIONAL_ATTR)
                    && !node2.getNodeValue().equals("true")) {
                  TestUtil.logErr(
                      "wsp:Optional attribute should exist and should be true");
                  pass = false;
                  break;
                }
              }
            }
          }
        }
        if (!pass)
          break;
      }
    }
    if (!pass) {
      TestUtil.logMsg(
          "Search for wsam:Addressing policy element via explicit wsp:Policy tag on binding");
      Element addressing = DescriptionUtils.getChildElement(bindings[0],
          WSAM_NAMESPACE_URI, WSAM_ADDRESSING_LOCAL_NAME);
      if (addressing == null) {
        TestUtil
            .logErr("No wsam:Addressing policy element was found on binding");
        pass = false;
      } else {
        pass = true;
        TestUtil.logMsg("See if for wsp:Optional attribute exists");
        NamedNodeMap map2 = addressing.getAttributes();
        for (int m = 0; m < map2.getLength(); m++) {
          Node node2 = map2.item(m);
          if (node2.getNodeName().equals(WSP_OPTIONAL_ATTR)
              && !node2.getNodeValue().equals("true")) {
            TestUtil.logErr(
                "wsp:Optional attribute should exist and should be true");
            pass = false;
            break;
          }
        }
      }
    }
    return pass;
  }

  private boolean VerifyActionFaultAction() throws Fault {
    TestUtil.logMsg("VerifyActionFaultAction");
    boolean pass = true;

    TestUtil.logMsg("Checking for operation names for portType: "
        + EXPECTED_ADDRESSING_PORTTYPE_NAME);
    Element operations[] = DescriptionUtils.getPortTypeOperationNameElements(
        client2.getDocument(), EXPECTED_ADDRESSING_PORTTYPE_NAME);

    for (int i = 0; i < operations.length; i++) {
      String name = operations[i].getAttribute(WSDL_NAME_ATTR);
      TestUtil.logMsg("Operation-> " + name);

      Element input = DescriptionUtils.getChildElement(operations[i],
          WSDL_NAMESPACE_URI, WSDL_INPUT_LOCAL_NAME);
      Element output = DescriptionUtils.getChildElement(operations[i],
          WSDL_NAMESPACE_URI, WSDL_OUTPUT_LOCAL_NAME);
      Element fault = DescriptionUtils.getChildElement(operations[i],
          WSDL_NAMESPACE_URI, WSDL_FAULT_LOCAL_NAME);

      String iaction = "", oaction = "", faction = "";

      if (input != null)
        iaction = input.getAttributeNS(WSAM_NAMESPACE_URI,
            WSAM_ACTION_LOCAL_NAME);
      if (output != null)
        oaction = output.getAttributeNS(WSAM_NAMESPACE_URI,
            WSAM_ACTION_LOCAL_NAME);
      if (fault != null)
        faction = fault.getAttributeNS(WSAM_NAMESPACE_URI,
            WSAM_ACTION_LOCAL_NAME);

      TestUtil.logMsg("iaction=" + iaction);
      TestUtil.logMsg("oaction=" + oaction);
      TestUtil.logMsg("faction=" + faction);

      if (name.equals("address1")) {
        if (iaction.equals("input1") && oaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address1Response")
            && faction.equals(""))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }
      } else if (name.equals("address2")) {
        if (iaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address2Request")
            && oaction.equals("output2") && faction.equals(""))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }

      } else if (name.equals("address3")) {
        if (iaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address3Request")
            && oaction.equals(
                "http://doclitservice.org/wsdl/AddressingEndpoint/address3Response")
            && faction.equals("fault1"))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }

      } else if (name.equals("address4")) {
        if (iaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address4Request")
            && oaction.equals("output4") && faction.equals("fault1"))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }

      } else if (name.equals("address5")) {
        if (iaction.equals("input5") && oaction.equals("output5")
            && faction.equals("fault1"))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }

      } else if (name.equals("address6")) {
        if (iaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address6Request")
            && oaction.equals(
                "http://doclitservice.org/wsdl/AddressingEndpoint/address6Response")
            && faction.equals(
                "http://doclitservice.org/wsdl/AddressingEndpoint/address6/Fault/MyFault1"))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }

      } else if (name.equals("address7")) {
        if (iaction.equals(
            "http://doclitservice.org/wsdl/AddressingEndpoint/address7Request")
            && oaction.equals(
                "http://doclitservice.org/wsdl/AddressingEndpoint/address7Response")
            && faction.equals(
                "http://doclitservice.org/wsdl/AddressingEndpoint/address7/Fault/MyFault1"))
          TestUtil.logMsg("verification PASSED for " + name);
        else {
          TestUtil.logErr("verification FAILED for " + name);
          pass = false;
        }
      }
    }
    return pass;
  }

}
