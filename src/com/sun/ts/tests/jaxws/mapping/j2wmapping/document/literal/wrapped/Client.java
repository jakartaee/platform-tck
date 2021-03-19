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
 * $Id$
 */

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped;

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

import java.lang.annotation.Annotation;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;
import jakarta.activation.DataHandler;

import com.sun.ts.tests.jaxws.common.*;

public class Client extends ServiceEETest
    implements DescriptionConstants, SOAPConstants, SchemaConstants {
  /*
   * The client.
   */
  private DescriptionClient client;

  /*
   * The document.
   */
  private Document document;

  /**
   * The generated WSDL file.
   */
  private static final String WSDLLOC_URL = "j2wdlwrapped.wsdlloc.1";

  private static final String TSHOME = "ts.home";

  private String baseURL, wsdlFile;

  private String tshome = null;

  private String wsdlFileUrl = null;

  private static final String EXPECTED_TARGETNAMESPACE = "http://doclitservice.org/wsdl";

  private static final String EXPECTED_PORTTYPE_NAME = "MYJ2WDLSharedEndpoint";

  private static final String EXPECTED_PORT_NAME = "J2WDLSharedEndpointPort";

  private static final String EXPECTED_SERVICE_NAME = "J2WDLSharedService";

  private static final String ONEWAY_METHOD = "oneWayOperation";

  private static final String TWOWAY_METHOD_WITH_FAULTS = "operationWithHeaderAndHeaderFaultAndFault";

  private static final String FAULT = "MyFault";

  private static final String HEADERFAULT = "ConfigHeaderFault";

  private static final String PARAMETER_RESULT_NAMING_METHOD = "stringOperation";

  private static final String INPUT_PART_NAME = "ivalue0";

  private static final String OUTPUT_PART_NAME = "rvalue";

  private static final String THREE_INPUT_PART_METHOD = "oneTwoThree";

  private static final String[] EXPECTED_OPERATION_NAMES = {
      "arrayOperationFromClient", "getBean", "arrayOperation",
      "stringOperation", "stringOperation2", "oneTwoThree",
      "holderMethodDefault", "holderMethodInOut", "holderMethodOut",
      "helloWorld", "helloWorld2", "oneWayOperation",
      "operationWithHeaderAndHeaderFaultAndFault", "hello", "bye",
      "methodWithNoReturn", "methodWithNoReturn2",
      "operationThatThrowsAFault", };

  private static final int[][] EXPECTED_PART_COUNTS = { { 1, 1, 0 }, // arrayOperationFromClient
      { 1, 1, 0 }, // getBean
      { 1, 1, 0 }, // arrayOperation
      { 1, 1, 0 }, // stringOperation
      { 1, 1, 0 }, // stringOperation2
      { 1, 1, 0 }, // oneTwoThree
      { 1, 1, 0 }, // holderMethodDefault
      { 1, 1, 0 }, // holderMethodInOut
      { 1, 1, 0 }, // holderMethodOut
      { 1, 1, 0 }, // helloWorld
      { 1, 1, 0 }, // helloWorld2
      { 1, 0, 0 }, // oneWayOperation
      { 2, 1, 2 }, // operationWithHeaderAndHeaderFaultAndFault
      { 1, 1, 0 }, // hello
      { 1, 1, 0 }, // bye
      { 1, 1, 0 }, // methodWithNoReturn
      { 1, 1, 0 }, // methodWithNoReturn2
      { 1, 1, 1 }, // operationThatThrowsAFault
  };

  private static final String[][] EXPECTED_TYPE_ELEMENT_MAPPINGS = {
      { "arrayOperationFromClient", "arrayOperationFromClientResponse", "", "",
          "", "" }, // arrayOperationFromClient
      { "getBean", "getBeanResponse", "", "", "", "" }, // getBean
      { "arrayOperation", "arrayOperationResponse", "", "", "", "" }, // arrayOperation
      { "stringOperation", "stringOperationResponse", "", "", "", "" }, // stringOperation
      { "myStringOperation", "myStringOperationResponse", "", "", "", "" }, // stringOperation2
      { "oneTwoThree", "oneTwoThreeResponse", "", "", "", "" }, // oneTwoThree
      { "holderMethodDefault", "holderMethodDefaultResponse", "", "", "", "" }, // holderMethodDefault
      { "holderMethodInOut", "holderMethodInOutResponse", "", "", "", "" }, // holderMethodInOut
      { "holderMethodOut", "holderMethodOutResponse", "", "", "", "" }, // holderMethodOut
      { "helloWorld", "helloWorldResponse", "", "", "", "" }, // helloWorld
      { "helloWorld2", "helloWorld2Response", "", "", "", "" }, // helloWorld2
      { "oneWayOperation", "", "", "", "", "" }, // oneWayOperation
      { "operationWithHeaderAndHeaderFaultAndFault", "ConfigHeader",
          "operationWithHeaderAndHeaderFaultAndFaultResponse",
          "ConfigHeaderFault", "MyFault", "" }, // operationWithHeaderAndHeaderFaultAndFault
      { "hello", "helloResponse", "", "", "", "" }, // hello
      { "bye", "byeResponse", "", "", "", "" }, // bye
      { "methodWithNoReturn", "methodWithNoReturnResponse", "", "", "", "" }, // methodWithNoReturn
      { "methodWithNoReturn2", "methodWithNoReturn2Response", "", "", "", "" }, // methodWithNoReturn2
      { "operationThatThrowsAFault", "MyOtherFault",
          "operationThatThrowsAFaultResponse", "", "", "" }, // operationThatThrowsAFault
  };

  // For verifying document/wrapped default naming
  private static final String DEFAULT_DOCUMENT_WRAPPED_METHOD = "stringOperation";

  private static final String DEFAULT_STRING_OPERATION_REQUEST = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.StringOperation";

  private static final String DEFAULT_STRING_OPERATION_RESPONSE = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.StringOperationResponse";

  // For verifying document/wrapped customized naming
  private static final String CUSTOMIZED_DOCUMENT_WRAPPED_METHOD = "stringOperation2";

  private static final String CUSTOMIZED_STRING_OPERATION_REQUEST = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MyStringOperation";

  private static final String CUSTOMIZED_STRING_OPERATION_RESPONSE = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MyStringOperationResponse";

  private static final String JAXB_ANNOTATIONS_TEST1 = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.JaxbAnnotationsTest1";

  private static final String JAXB_ANNOTATIONS_TEST2 = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.JaxbAnnotationsTest2";

  private static final String JAXB_ANNOTATIONS_TEST3 = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.JaxbAnnotationsTest3";

  private static final String JAXB_ANNOTATIONS_TEST4 = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.JaxbAnnotationsTest4";

  private static final String JAXB_ANNOTATIONS_FAULTBEAN = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.jaxws.MyFaultBean";

  private boolean debug = false;

  Hashtable ht = new Hashtable();

  Hashtable ht2 = new Hashtable();

  // private methods here
  private boolean ProcessWsdlDocument(Document document) throws Fault {
    boolean pass = true;

    // Process wsdl <import> elements (imported wsdl's)
    Document[] wsdlDocs = DescriptionUtils.getWsdlDocuments(document,
        tshome + baseURL);
    if (wsdlDocs != null) {
      for (int i = 0; i < wsdlDocs.length; i++) {
        TestUtil.logMsg("Process imported wsdl file: "
            + wsdlDocs[i].getDocumentElement().getAttribute("WsdlFile"));
        wsdlDocs[i].getDocumentElement().removeAttribute("WsdlFile");
        pass = ProcessWsdlDocument(wsdlDocs[i]);
      }
    }

    // Process wsdl <types> element (If none then we are done)
    Element types = DescriptionUtils.getTypes(document);
    if (types == null)
      return pass;

    // Verify namespaceURI on wsdl <schema> elements if any
    Element[] schemas = DescriptionUtils.getChildElements(types, null,
        WSDL_SCHEMA_LOCAL_NAME);
    TestUtil.logMsg("Verify namespaceURI on wsdl <schema> elements");
    if (schemas != null) {
      for (int i = 0; i < schemas.length; i++) {
        String namespaceURI = schemas[i].getNamespaceURI();
        Attr[] attributes = DescriptionUtils.getElementAttributes(schemas[i]);
        for (int j = 0; j < attributes.length; j++) {
          String aname = attributes[j].getName();
          String avalue = attributes[j].getValue();
          if (aname.startsWith("xmlns:")) {
            String aprefix = aname.substring(6, aname.length());
            ht.put(aprefix, avalue);
          }
        }

        if (!namespaceURI.equals(XSD_NAMESPACE_URI)) {
          TestUtil
              .logErr("Encountered 'schema' element with invalid namespaceURI '"
                  + namespaceURI);
          pass = false;
        }
      }
    }

    // Verify schema element types defined in wsdl <types> element if any
    NodeList list = types.getElementsByTagNameNS(XSD_NAMESPACE_URI,
        XSD_ELEMENT_LOCAL_NAME);
    TestUtil
        .logMsg("Verify schema element types defined in wsdl <types> element");
    if (list.getLength() != 0) {
      if (!VerifyElementTypes(list))
        pass = false;
    }

    // Process schema <import> elements (imported schema's)
    Document[] schemaDocs = DescriptionUtils.getSchemaDocuments(document,
        tshome + baseURL);
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        TestUtil.logMsg("Process imported schema file: "
            + schemaDocs[i].getDocumentElement().getAttribute("SchemaFile"));
        schemaDocs[i].getDocumentElement().removeAttribute("SchemaFile");
        pass = ProcessSchemaDocument(schemaDocs[i]);
      }
    }

    return pass;
  }

  private boolean ProcessSchemaDocument(Document document) throws Fault {
    boolean pass = true;

    // Process schema <import> elements (imported schema's)
    Document[] schemaDocs = DescriptionUtils.getSchemaDocuments(document,
        tshome + baseURL);
    if (schemaDocs != null) {
      for (int i = 0; i < schemaDocs.length; i++) {
        TestUtil.logMsg("Process imported schema file: "
            + schemaDocs[i].getDocumentElement().getAttribute("SchemaFile"));
        schemaDocs[i].getDocumentElement().removeAttribute("SchemaFile");
        pass = ProcessSchemaDocument(schemaDocs[i]);
      }
    }

    // Verify schema element types defined in imported schema if any
    TestUtil.logMsg("Verify schema element types defined in imported schema");
    ht2 = new Hashtable();
    String[] s = DescriptionUtils.getAllPrefixAndNamespace(document);
    for (int i = 0; i < s.length; i++) {
      String prefix = s[i].substring(0, s[i].indexOf(":"));
      String namespace = s[i].substring(s[i].indexOf(":") + 1, s[i].length());
      ht2.put(prefix, namespace);
    }
    NodeList list = document.getDocumentElement()
        .getElementsByTagNameNS(XSD_NAMESPACE_URI, XSD_ELEMENT_LOCAL_NAME);
    if (!VerifyElementTypes(list))
      pass = false;

    return pass;
  }

  private boolean VerifyElementTypes(NodeList list) {
    boolean pass = true;
    for (int j = 0; j < list.getLength(); j++) {
      boolean ok = false;
      Element element = (Element) list.item(j);
      String type = element.getAttribute(XSD_TYPE_ATTR);
      String name = element.getAttribute(XSD_NAME_ATTR);
      String prefix = element.getPrefix();
      String uri = element.getNamespaceURI();
      ht.put(prefix, uri);
      if (type.contains(":")) {
        prefix = type.substring(0, type.indexOf(":"));
        Attr attributes[] = DescriptionUtils.getElementAttributes(element);
        for (int k = 0; k < attributes.length; k++) {
          String aname = attributes[k].getName();
          String avalue = attributes[k].getValue();
          if (aname.startsWith("xmlns:")) {
            String aprefix = aname.substring(6, aname.length());
            ht.put(aprefix, avalue);
            if (prefix.equals(aprefix)) {
              uri = avalue;
            }
          }
        }
      }
      // Verify element type and prefix has associated (prefix,namespace) in
      // hashtable
      TestUtil.logMsg("Verify 'element' name=" + name + " type=" + type
          + " prefix=" + prefix);
      // Check hashtable prefix's of current element
      Enumeration e = ht.keys();
      while (e.hasMoreElements()) {
        String cprefix = (String) e.nextElement();
        if (prefix.equals(cprefix)) {
          ok = true;
          break;
        }
      }

      // Check hashtable prefix's of document root element
      if (!ok) {
        e = ht2.keys();
        while (e.hasMoreElements()) {
          String cprefix = (String) e.nextElement();
          if (prefix.equals(cprefix)) {
            ok = true;
            break;
          }
        }
      }
      if (!ok) {
        TestUtil.logErr(
            "Encountered 'element' " + name + " with invalid prefix " + prefix);
        pass = false;
      }
    }
    return pass;
  }

  private boolean VerifyTypeElementMappings(Element[] iparts, Element[] oparts,
      Element[] fparts, String opname) throws Fault {
    boolean ok = true;

    if (!PartCountsOk(iparts, oparts, fparts, opname))
      ok = false;
    if (!TypesElementsOk(iparts, oparts, fparts, opname))
      ok = false;

    return ok;
  }

  private boolean PartCountsOk(Element[] iparts, Element[] oparts,
      Element[] fparts, String opname) {
    boolean ok = true;
    int index, icnt, ocnt, fcnt;

    TestUtil.logMsg("Verify part counts for operation: " + opname);
    index = GetIndex(opname);
    if (index == -1) {
      TestUtil.logErr("Unexpected operation name: " + opname);
      ok = false;
    } else {
      icnt = EXPECTED_PART_COUNTS[index][0];
      ocnt = EXPECTED_PART_COUNTS[index][1];
      fcnt = EXPECTED_PART_COUNTS[index][2];
      if ((iparts == null && icnt != 0)
          || (iparts != null && iparts.length != icnt)) {
        TestUtil.logErr("Incorrect number of input parts");
        ok = false;
      }
      if ((oparts == null && ocnt != 0)
          || (oparts != null && oparts.length != ocnt)) {
        TestUtil.logErr("Incorrect number of output parts");
        ok = false;
      }
      if ((fparts == null && fcnt != 0)
          || (fparts != null && fparts.length != fcnt)) {
        TestUtil.logErr("Incorrect number of fault parts");
        ok = false;
      }
    }
    return ok;
  }

  private boolean TypesElementsOk(Element[] iparts, Element[] oparts,
      Element[] fparts, String opname) {
    boolean ok = true;
    int index;

    TestUtil.logMsg("Verify types for operation: " + opname);
    index = GetIndex(opname);
    if (index == -1) {
      TestUtil.logErr("Unexpected operation name: " + opname);
      ok = false;
    } else {
      ArrayList alist1 = new ArrayList();
      ArrayList alist2 = new ArrayList();
      String expectedtypes[] = null;
      String types[] = null;
      String str = null;
      for (int i = 0; i < 6; i++) {
        if (!EXPECTED_TYPE_ELEMENT_MAPPINGS[index][i].equals(""))
          alist1.add(EXPECTED_TYPE_ELEMENT_MAPPINGS[index][i]);
      }
      if (alist1.size() > 0)
        expectedtypes = (String[]) alist1.toArray(new String[alist1.size()]);
      if (iparts != null) {
        for (int i = 0; i < iparts.length; i++) {
          str = iparts[i].getAttribute(XSD_ELEMENT_ATTR);
          alist2.add(str.substring(str.indexOf(":") + 1));
        }
      }
      if (oparts != null) {
        for (int i = 0; i < oparts.length; i++) {
          str = oparts[i].getAttribute(XSD_ELEMENT_ATTR);
          alist2.add(str.substring(str.indexOf(":") + 1));
        }
      }
      if (fparts != null) {
        for (int i = 0; i < fparts.length; i++) {
          str = fparts[i].getAttribute(XSD_ELEMENT_ATTR);
          alist2.add(str.substring(str.indexOf(":") + 1));
        }
      }
      if (alist2.size() > 0)
        types = (String[]) alist2.toArray(new String[alist2.size()]);
      if (!CompareTypesElementsOk(expectedtypes, types, opname))
        ok = false;
    }

    return ok;
  }

  private int GetIndex(String opname) {
    for (int i = 0; i < EXPECTED_OPERATION_NAMES.length; i++) {
      if (opname.equals(EXPECTED_OPERATION_NAMES[i]))
        return i;
    }
    return -1;
  }

  private boolean CompareTypesElementsOk(String[] expectedtypes, String[] types,
      String opname) {
    boolean ok = true;
    boolean found = false;

    if (expectedtypes == null && types == null) {
      ;
    } else if (expectedtypes == null && types != null) {
      TestUtil.logErr(
          "Incorrect number of types: got " + types.length + ", expected null");
      ok = false;
    } else if (expectedtypes != null && types == null) {
      TestUtil.logErr("Incorrect number of types: got null " + ", expected "
          + expectedtypes.length);
      ok = false;
    } else if (types != null && types.length != expectedtypes.length) {
      TestUtil.logErr("Incorrect number of types: got " + types.length
          + ", expected " + expectedtypes.length);
      ok = false;
    } else {
      if (types != null) {
        for (int i = 0; i < types.length; i++) {
          for (int j = 0; j < types.length; j++) {
            if (types[i].equals(expectedtypes[j]))
              found = true;
          }
          if (!found)
            ok = false;
          else
            found = false;
        }
        StringBuffer got = new StringBuffer("[ ");
        StringBuffer exp = new StringBuffer("[ ");
        for (int i = 0; i < types.length; i++) {
          got.append(types[i] + " ");
          exp.append(expectedtypes[i] + " ");
        }
        got.append("]");
        exp.append("]");
        if (!ok)
          TestUtil.logErr("Incorrect types: got " + got.toString()
              + ", expected " + exp.toString());
        else
          TestUtil.logMsg("Types are correct: " + got.toString());
      }
    }
    return ok;
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
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  /*
   * @testName: VerifyTargetNamespaceWithAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify that the targetNamespace in the generated WSDL
   * matches the targetNamespace as specified in the jakarta.jws.WebService
   * annotation. (Java to WSDL 1.1 Mapping). Conformance requirement done: -
   * Package name mapping
   *
   */
  public void VerifyTargetNamespaceWithAnnotation() throws Fault {
    TestUtil.logMsg("VerifyTargetNamespaceWithAnnotation");
    boolean pass = true;

    try {
      String targetNamespace = DescriptionUtils
          .getTargetNamespaceAttr(client.getDocument());
      TestUtil
          .logMsg("Verify that targetNamespace is correct in generated WSDL");
      if (!targetNamespace.equals(EXPECTED_TARGETNAMESPACE)) {
        TestUtil.logErr("TargetNamespace is incorrect, expected: "
            + EXPECTED_TARGETNAMESPACE + ", got: " + targetNamespace);
        pass = false;
      } else
        TestUtil.logMsg("TargetNamespace is correct: " + targetNamespace);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception:" + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyTargetNamespaceWithAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifySOAPElementNamespaceUseAttributeWithAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify the namespace and use attributes for all SOAP
   * Elements in the generated WSDL (Java to WSDL 1.1 Mapping and BP1.1).
   * Conformance requirement done: - verify use attribute of literal for all
   * SOAP elements - verify no namespace attribute on all soap:body elements
   *
   *
   */
  public void VerifySOAPElementNamespaceUseAttributeWithAnnotation()
      throws Fault {
    TestUtil.logMsg("VerifySOAPElementNamespaceUseAttributeWithAnnotation");
    boolean pass = true;

    Document document = client.getDocument();
    TestUtil.logMsg(
        "Verify that no namespace and use attribute does exist on soap:body elements");
    NamespaceAttributeVerifier verifier = new NamespaceAttributeVerifier(
        document, 2716);
    verifier.verify();
    if (!pass)
      throw new Fault(
          "VerifySOAPElementNamespaceUseAttributeWithAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifySOAPBindingTransportStyleAttributeWithAnnotation
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify the soap:binding transport and style attributes in
   * the generated WSDL. (Java to WSDL 1.1 Mapping and BP1.1). Conformance
   * requirement done: - SOAP binding support - SOAP binding style required -
   * SOAP binding transport required
   *
   *
   */
  public void VerifySOAPBindingTransportStyleAttributeWithAnnotation()
      throws Fault {
    TestUtil.logMsg("VerifySOAPBindingTransportStyleAttributeWithAnnotation");
    boolean pass = true;

    Document document = client.getDocument();
    TestUtil.logMsg("Verify soap:binding transport and style attribute");
    Element[] bindings = DescriptionUtils.getBindings(document);
    for (int i = 0; i < bindings.length; i++) {
      if (debug)
        DescriptionUtils.dumpDOMNodes(bindings[i]);
      Element soapBinding = DescriptionUtils.getChildElement(bindings[i],
          SOAP_NAMESPACE_URI, SOAP_BINDING_LOCAL_NAME);
      if (soapBinding == null) {
        TestUtil.logErr("soap:binding is null unexpected");
        pass = false;
      } else {
        String style = soapBinding.getAttribute(SOAP_STYLE_ATTR);
        if (!style.equals(SOAP_DOCUMENT)) {
          TestUtil.logErr("soap:binding style attribute incorrect, expected: "
              + SOAP_DOCUMENT + ", got: " + style);
          pass = false;
        } else
          TestUtil.logMsg("soap:binding style attribute is correct: " + style);
        String transport = soapBinding.getAttribute(SOAP_TRANSPORT_ATTR);
        if (!transport.equals(SOAP_TRANSPORT)) {
          TestUtil
              .logErr("soap:binding transport attribute incorrect, expected: "
                  + SOAP_TRANSPORT + ", got: " + transport);
          pass = false;
        } else
          TestUtil.logMsg(
              "soap:binding transport attribute is correct: " + transport);
      }
    }
    if (!pass)
      throw new Fault(
          "VerifySOAPBindingTransportStyleAttributeWithAnnotation failed");
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
   * @testName: VerifyPortNameWithAnnotation
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
  public void VerifyPortNameWithAnnotation() throws Fault {
    TestUtil.logMsg("VerifyPortNameWithAnnotation");
    boolean pass = true;

    TestUtil.logMsg(
        "Checking for port name verification of: " + EXPECTED_PORT_NAME);
    pass = DescriptionUtils.isPortNameAttr(client.getDocument(),
        EXPECTED_PORT_NAME);
    if (!pass)
      throw new Fault("VerifyPortNameWithAnnotation failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyOperationNames
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3010; JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020;
   * JAXWS:SPEC:3033; JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3003;
   * JAXWS:SPEC:7004; JAXWS:SPEC:7005;
   *
   * @test_Strategy: Verify wsdl:portType operation names are correct in the
   * generated WSDL. Verify all public method mapping to wsdl:operation
   * elements. Conformance requirement done: - Inheritance flattening -
   * Operation naming - Method name disambiguation
   * 
   *
   */
  public void VerifyOperationNames() throws Fault {
    TestUtil.logMsg("VerifyOperationNames");
    boolean pass = true;

    TestUtil.logMsg(
        "Checking for operation names for portType: " + EXPECTED_PORTTYPE_NAME);
    String opNames[] = DescriptionUtils.getPortTypeOperationNames(
        client.getDocument(), EXPECTED_PORTTYPE_NAME);
    if (opNames.length == EXPECTED_OPERATION_NAMES.length)
      TestUtil.logMsg("operation name count is as expected: "
          + EXPECTED_OPERATION_NAMES.length);
    else {
      pass = false;
      TestUtil.logErr("operation count expected: "
          + EXPECTED_OPERATION_NAMES.length + ", received: " + opNames.length);
    }
    int cnt = 0;
    for (int i = 0; i < opNames.length; i++) {
      boolean found = false;
      for (int j = 0; j < EXPECTED_OPERATION_NAMES.length; j++) {
        if (opNames[i].equals(EXPECTED_OPERATION_NAMES[j])) {
          found = true;
          cnt++;
          TestUtil.logMsg("Matched operation name ... " + opNames[i]);
          break;
        }
      }
      if (!found) {
        TestUtil.logErr("UnMatched operation name: " + opNames[i]);
        pass = false;
      }
    }
    if (cnt == EXPECTED_OPERATION_NAMES.length)
      TestUtil.logMsg("All operation names matched expected ...");

    if (!pass)
      throw new Fault("VerifyOperationNames failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyOneWayAndTwoWayOperations
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3011; JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020;
   * JAXWS:SPEC:3033; JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3014;
   *
   * @test_Strategy: Verify operations are correctly identifed as OneWay or
   * TwoWay. Conformance requirement done: - One-way mapping - Two-way mapping -
   * Two-way mapping with faults - Method and parameters where expected in both
   * wsdl:portType and wsdl:binding
   * 
   *
   */
  public void VerifyOneWayAndTwoWayOperations() throws Fault {
    TestUtil.logMsg("VerifyOneWayAndTwoWayOperations");
    boolean pass = true;

    TestUtil.logMsg(
        "Checking for operation names for portType: " + EXPECTED_PORTTYPE_NAME);
    Element operations[] = DescriptionUtils.getPortTypeOperationNameElements(
        client.getDocument(), EXPECTED_PORTTYPE_NAME);
    for (int i = 0; i < operations.length; i++) {
      String name = operations[i].getAttribute(WSDL_NAME_ATTR);
      Element e = DescriptionUtils
          .getBindingOperationNameElement(client.getDocument(), name);
      if (name.equals(ONEWAY_METHOD)) {
        TestUtil
            .logMsg("Checking for one way operation for operation: " + name);
        TestUtil.logMsg("verify in portType operation ...");
        if (DescriptionUtils.isOneWay(operations[i]))
          TestUtil.logMsg("is One way");
        else {
          TestUtil.logErr("is not One way");
          pass = false;
        }
        TestUtil.logMsg("verify in binding operation ...");
        if (DescriptionUtils.isOneWay(e))
          TestUtil.logMsg("is One way");
        else {
          TestUtil.logErr("is not One way");
          pass = false;
        }
      } else {
        TestUtil
            .logMsg("Checking for two way operation for operation: " + name);
        TestUtil.logMsg("verify in portType operation ...");
        if (DescriptionUtils.isTwoWay(operations[i]))
          TestUtil.logMsg("is Two way");
        else {
          TestUtil.logErr("is not Two way");
          pass = false;
        }
        TestUtil.logMsg("verify in binding operation ...");
        if (DescriptionUtils.isTwoWay(e))
          TestUtil.logMsg("is Two way");
        else {
          TestUtil.logErr("is not Two way");
          pass = false;
        }
        if (name.equals(TWOWAY_METHOD_WITH_FAULTS)) {
          TestUtil.logMsg("Checking for faults for operation: " + name);
          TestUtil.logMsg("Expected faults are: " + FAULT + "," + HEADERFAULT);
          TestUtil.logMsg("verify in portType operation ...");
          if (DescriptionUtils.hasFault(operations[i], FAULT)
              && DescriptionUtils.hasFault(operations[i], HEADERFAULT))
            TestUtil.logMsg("fault match");
          else {
            TestUtil.logErr("fault mismatch, did not get expected faults: "
                + FAULT + "," + HEADERFAULT);
            pass = false;
          }
          TestUtil.logMsg("verify in binding operation ...");
          if (DescriptionUtils.hasFault(e, FAULT)
              && DescriptionUtils.hasFault(e, HEADERFAULT))
            TestUtil.logMsg("fault match");
          else {
            TestUtil.logErr("fault mismatch, did not get expected faults: "
                + FAULT + "," + HEADERFAULT);
            pass = false;
          }
        }
      }
    }
    if (!pass)
      throw new Fault("VerifyOneWayAndTwoWayOperations failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyParameterAndResultNaming
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3017; JAXWS:SPEC:3018;
   *
   * @test_Strategy: Verify use of annotations to name parameters and results.
   * Conformance requirement done: - Parameter naming - Result naming
   * 
   *
   */
  public void VerifyParameterAndResultNaming() throws Fault {
    TestUtil.logMsg("VerifyParameterAndResultNaming");
    boolean pass = true;

    Document document = client.getDocument();

    TestUtil
        .logMsg("Checking for operation name " + PARAMETER_RESULT_NAMING_METHOD
            + " in portType " + EXPECTED_PORTTYPE_NAME);
    Element operation = DescriptionUtils.getPortTypeOperationNameElement(
        document, EXPECTED_PORTTYPE_NAME, PARAMETER_RESULT_NAMING_METHOD);
    Element input = DescriptionUtils.getChildElement(operation,
        WSDL_NAMESPACE_URI, WSDL_INPUT_LOCAL_NAME);
    Element output = DescriptionUtils.getChildElement(operation,
        WSDL_NAMESPACE_URI, WSDL_OUTPUT_LOCAL_NAME);
    String imsg = input.getAttribute(WSDL_MESSAGE_ATTR);
    String omsg = output.getAttribute(WSDL_MESSAGE_ATTR);
    int iidx = imsg.indexOf(":");
    int oidx = omsg.indexOf(":");
    String imessage, omessage;
    if (iidx != -1) {
      imessage = imsg.substring(iidx + 1);
      omessage = omsg.substring(oidx + 1);
    } else {
      imessage = imsg;
      omessage = omsg;
    }
    TestUtil.logMsg("input message name is " + imessage);
    TestUtil.logMsg("output message name is " + omessage);
    Element ipart = DescriptionUtils.getPartElement(document, imessage);
    String ielement = ipart.getAttribute(WSDL_ELEMENT_ATTR);
    String ielementName = ielement;
    if (ielementName.indexOf(":") >= 0) {
      ielementName = ielementName.substring(ielementName.indexOf(":") + 1,
          ielementName.length());
    }
    String iprefix = ielement.substring(0, ielement.indexOf(":"));
    Element opart = DescriptionUtils.getPartElement(document, omessage);
    String oelement = opart.getAttribute(WSDL_ELEMENT_ATTR);
    String oelementName = oelement;
    if (oelementName.indexOf(":") >= 0) {
      oelementName = oelementName.substring(oelementName.indexOf(":") + 1,
          oelementName.length());
    }
    String oprefix = oelement.substring(0, oelement.indexOf(":"));
    TestUtil.logMsg("input prefix " + iprefix);
    TestUtil.logMsg("output prefix " + oprefix);

    Document schemaDoc = null;
    NodeList list = null;

    // Search first types section of WSDL to see if element declared there
    // else look in schema imports as below.
    Element element = DescriptionUtils.getSchemaElementName(document,
        ielementName);
    String type = null;
    Element complexType = null;
    if (element != null) {
      type = element.getAttribute(WSDL_TYPE_ATTR);
      if (type.indexOf(":") != -1)
        type = type.substring(type.indexOf(":") + 1, type.length());
      complexType = DescriptionUtils.getSchemaComplexTypeName(document, type);
      if (complexType != null)
        list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
            XSD_ELEMENT_LOCAL_NAME);
    } else {
      element = DescriptionUtils.getSchemaElementName(document, ielementName);
      if (element != null) {
        type = element.getAttribute(WSDL_TYPE_ATTR);
        if (type.indexOf(":") != -1)
          type = type.substring(type.indexOf(":") + 1, type.length());
        complexType = DescriptionUtils.getSchemaComplexTypeName(document, type);
        if (complexType != null)
          list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
              XSD_ELEMENT_LOCAL_NAME);
      } else
        type = ielementName;
      // Find complex type of imessage in schema and element name matches
      schemaDoc = DescriptionUtils.getSchemaDocument(document, iprefix,
          tshome + baseURL);
      if (schemaDoc != null) {
        complexType = DescriptionUtils.getNamedChildElement(
            schemaDoc.getDocumentElement(), XSD_NAMESPACE_URI,
            XSD_COMPLEXTYPE_LOCAL_NAME, type);
        if (complexType != null)
          list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
              XSD_ELEMENT_LOCAL_NAME);
      }
    }
    String ipartName = null;
    if (list != null) {
      if (list.getLength() == 1) {
        Element e = (Element) list.item(0);
        ipartName = e.getAttribute(XSD_NAME_ATTR);
      }
    }
    TestUtil.logMsg("ipartName=" + ipartName);

    element = DescriptionUtils.getSchemaElementName(document, oelementName);
    list = null;
    if (element != null) {
      type = element.getAttribute(WSDL_TYPE_ATTR);
      if (type.indexOf(":") != -1)
        type = type.substring(type.indexOf(":") + 1, type.length());
      complexType = DescriptionUtils.getSchemaComplexTypeName(document, type);
      if (complexType != null)
        list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
            XSD_ELEMENT_LOCAL_NAME);
    } else {
      element = DescriptionUtils.getSchemaElementName(document, oelementName);
      if (element != null) {
        type = element.getAttribute(WSDL_TYPE_ATTR);
        if (type.indexOf(":") != -1)
          type = type.substring(type.indexOf(":") + 1, type.length());
        complexType = DescriptionUtils.getSchemaComplexTypeName(document, type);
        if (complexType != null)
          list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
              XSD_ELEMENT_LOCAL_NAME);
      } else
        type = oelementName;
      // Find complex type of omessage in schema and element name matches
      if (!oprefix.equals(iprefix))
        schemaDoc = DescriptionUtils.getSchemaDocument(document, oprefix,
            tshome + baseURL);
      if (schemaDoc != null) {
        complexType = DescriptionUtils.getNamedChildElement(
            schemaDoc.getDocumentElement(), XSD_NAMESPACE_URI,
            XSD_COMPLEXTYPE_LOCAL_NAME, omessage);
        if (complexType != null)
          list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
              XSD_ELEMENT_LOCAL_NAME);
      }
    }
    String opartName = null;
    if (list != null) {
      if (list.getLength() == 1) {
        Element e = (Element) list.item(0);
        opartName = e.getAttribute(XSD_NAME_ATTR);
      }
    }
    TestUtil.logMsg("opartName=" + opartName);

    // Check that both annotated input and output arguments are correctly named
    // in wsdl.
    if (ipartName != null) {
      if (ipartName.equals(INPUT_PART_NAME))
        TestUtil.logMsg("Parameter naming passed, expected: " + INPUT_PART_NAME
            + ", received: " + ipartName);
      else {
        TestUtil.logErr("Parameter naming failed, expected: " + INPUT_PART_NAME
            + ", received: " + ipartName);
        pass = false;
      }
    } else {
      TestUtil.logMsg("Failed to find partName of " + INPUT_PART_NAME);
      pass = false;
    }

    if (opartName != null) {
      if (opartName.equals(OUTPUT_PART_NAME))
        TestUtil.logMsg("Result naming passed, expected: " + OUTPUT_PART_NAME
            + ", received: " + opartName);
      else {
        TestUtil.logErr("Result naming failed, expected: " + OUTPUT_PART_NAME
            + ", received: " + opartName);
        pass = false;
      }
    } else {
      TestUtil.logMsg("Failed to find partName of " + OUTPUT_PART_NAME);
      pass = false;
    }

    if (!pass)
      throw new Fault("VerifyParameterAndResultNaming failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyWsdlMessagePartAndAttrStyle
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify each message has a single wsdl:part child element
   * that refers, via an element attribute to a named type declaration.
   * Conformance requirement done: - A single wsdl:part child element that
   * refers, via an element attribute, to a global element declaration in the
   * wsdl:types section.
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

    // Header parts are not checked but filtered out
    String headerParts[] = DescriptionUtils
        .getSoapHeaderElementsPartAttr(client.getDocument());
    for (int i = 0; i < headerParts.length; i++)
      TestUtil.logMsg("part=" + headerParts[i]);

    Element messages[] = DescriptionUtils.getMessages(client.getDocument());
    for (int i = 0; i < messages.length; i++) {
      String messageName = messages[i].getAttribute(WSDL_NAME_ATTR);
      TestUtil.logMsg("Checking message: " + messageName);
      if (Arrays.binarySearch(imessages, messageName) >= 0
          || Arrays.binarySearch(omessages, messageName) >= 0) {
        Element[] parts = DescriptionUtils.getChildElements(messages[i],
            WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
        if (parts.length == 0) {
          TestUtil.logErr("    no parts exist for message " + messageName);
          pass = false;
          continue;
        } else
          TestUtil.logMsg(
              "    check that element attribute exists for all its message parts ...");
        int partCnt = 0;
        for (int j = 0; j < parts.length; j++) {
          String part = parts[j].getAttribute(WSDL_NAME_ATTR);
          String element = parts[j].getAttribute(WSDL_ELEMENT_ATTR);
          String type = parts[j].getAttribute(WSDL_TYPE_ATTR);
          TestUtil.logMsg(
              "    part=" + part + ", element=" + element + ", type=" + type);
          boolean isHeader = false;
          for (int k = 0; k < headerParts.length; k++) {
            if (part.equals(headerParts[k])) {
              isHeader = true;
              TestUtil.logMsg("    not counting this as a part");
              break;
            } else
              partCnt++;
          }
          if (element == null || element.equals("")) {
            TestUtil.logErr("    no element attribute for this part");
            pass = false;
          }
          if (type != null && !type.equals("")) {
            TestUtil.logErr("    a type attribute exists for this part");
            pass = false;
          }
        }
        TestUtil.logMsg("message: " + messageName + ", parts: " + partCnt);
        if (partCnt > 1) {
          TestUtil.logMsg(
              "    more than a single part exists for this message which is not a header");
          pass = false;
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
   * @testName: VerifyWsdlPartNamesAndMessageNamesForMessages
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3057; JAXWS:JAVADOC:214;
   * JAXWS:JAVADOC:215; JAXWS:JAVADOC:221;
   *
   * @test_Strategy: Verify the wsdl:part names for each input and output
   * message Conformance requirement done: - (Default Wrapper wsdl:part names):
   * The name of wsdl:part for the wrapper must be named as "parameters" for
   * input messages in the generated WSDL. If a SEI method doesn't have any
   * header parameters or return type, then the name of wsdl:part for the
   * wrapper must be named as "parameters" for output messages in the generated
   * WSDL, otherwise it would be named as "result". - (Customizing Wrapper
   * wsdl:part names): Non-default partName values of the
   * jakarta.xml.ws.RequestWrapper and jakarta.xml.ws.ResponseWrapper annotations,
   * if specified on SEI method, MUST be used as wsdl:part name for input and
   * output messages respectively in the generated WSDL. - (wsdl:message naming
   * using WebFault): If an exception has @WebFault, then messageName MUST be
   * the name of the corresponding wsdl:message element - (wsdl:message naming):
   * In the absence of customizations, the name of the wsdl:message element MUST
   * be the name of the Java exception.
   */
  public void VerifyWsdlPartNamesAndMessageNamesForMessages() throws Fault {
    TestUtil.logMsg("VerifyWsdlPartNamesAndMessageNamesForMessages");
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
    String fmessages[] = DescriptionUtils
        .getFaultMessageNames(client.getDocument(), EXPECTED_PORTTYPE_NAME);
    Arrays.sort(fmessages);
    for (int i = 0; i < fmessages.length; i++)
      TestUtil.logMsg("fault message: " + fmessages[i]);

    // Header parts are not checked but filtered out
    String headerParts[] = DescriptionUtils
        .getSoapHeaderElementsPartAttr(client.getDocument());

    Element messages[] = DescriptionUtils.getMessages(client.getDocument());
    for (int i = 0; i < messages.length; i++) {
      String messageName = messages[i].getAttribute(WSDL_NAME_ATTR);
      TestUtil.logMsg("Checking partname for message: " + messageName);
      if (Arrays.binarySearch(imessages, messageName) >= 0
          || Arrays.binarySearch(omessages, messageName) >= 0) {
        Element[] parts = DescriptionUtils.getChildElements(messages[i],
            WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
        if (parts.length == 0) {
          TestUtil.logErr("    no parts exist for message " + messageName);
          pass = false;
          continue;
        }
        int partCnt = 0;
        String partname = null;
        String previousPartname = null;
        for (int j = 0; j < parts.length; j++) {
          previousPartname = partname;
          partname = parts[j].getAttribute(WSDL_NAME_ATTR);
          boolean isHeader = false;
          for (int k = 0; k < headerParts.length; k++) {
            if (partname.equals(headerParts[k])) {
              isHeader = true;
              partname = previousPartname;
              break;
            } else
              partCnt++;
          }
        }
        if (partCnt > 1) {
          TestUtil.logMsg(
              "    more than a single part exists for this message which is not a header");
          pass = false;
        }
        String expectedPartname = "parameters";
        if (messageName.contains("Response")) {
          if (messageName
              .equals("operationWithHeaderAndHeaderFaultAndFaultResponse"))
            expectedPartname = "result";
          if (messageName.equals("methodWithNoReturn2Response"))
            expectedPartname = "response";
          if (!partname.equals(expectedPartname)) {
            TestUtil.logErr("    partname is <" + partname + "> expected <"
                + expectedPartname + ">");
            pass = false;
          } else {
            TestUtil
                .logMsg("    partname is expected <" + expectedPartname + ">");
          }
        } else {
          if (messageName.equals("methodWithNoReturn2"))
            expectedPartname = "request";
          if (!partname.equals(expectedPartname)) {
            TestUtil.logErr("    partname is <" + partname + "> expected: <"
                + expectedPartname + ">");
            pass = false;
          } else {
            TestUtil
                .logMsg("    partname is expected <" + expectedPartname + ">");
          }
        }
      }
    }
    for (int i = 0; i < fmessages.length; i++) {
      TestUtil.logMsg("Checking fault message: " + fmessages[i]);
      if (!fmessages[i].equals("ConfigHeaderFault")
          && !fmessages[i].equals("MyFault")
          && !fmessages[i].equals("YesItsMyOtherFault")) {
        TestUtil.logErr("    fault message name is <" + fmessages[i]
            + "> expected <YesItsMyOtherFault>");
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("VerifyWsdlPartNamesAndMessageNamesForMessages failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifySchemaNamespacesImportsElementsAndTypes
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035;
   *
   * @test_Strategy: Verify schema namespace, schema imports, schema element
   * types.
   *
   */
  public void VerifySchemaNamespacesImportsElementsAndTypes() throws Fault {
    TestUtil.logMsg("VerifySchemaNamespacesImportsElementsAndTypes");
    boolean pass = true;

    pass = ProcessWsdlDocument(client.getDocument());
    if (!pass)
      throw new Fault("VerifySchemaNamespacesImportsElementsAndTypes failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyDocumentWrapped
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3024; JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020;
   * JAXWS:SPEC:3033; JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3025;
   * JAXWS:SPEC:2037; JAXWS:SPEC:3021; JAXWS:SPEC:3022;
   *
   * @test_Strategy: Verify document wrapped style for generated classes
   * Conformance requirement done: - Default wrapper bean names - Default
   * wrapper bean package - Default Customized wrapper bean names
   *
   *
   */
  public void VerifyDocumentWrapped() throws Fault {
    TestUtil.logMsg("VerifyDocumentWrapped");
    boolean pass = true;

    TestUtil.logMsg("Checking for default document/literal wrapped bean names "
        + "for method " + DEFAULT_DOCUMENT_WRAPPED_METHOD);
    try {
      TestUtil
          .logMsg("Loading wrapper bean " + DEFAULT_STRING_OPERATION_REQUEST);
      Class c = Class.forName(DEFAULT_STRING_OPERATION_REQUEST);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    try {
      TestUtil
          .logMsg("Loading wrapper bean " + DEFAULT_STRING_OPERATION_RESPONSE);
      Class c = Class.forName(DEFAULT_STRING_OPERATION_RESPONSE);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    TestUtil
        .logMsg("Checking for customized document/literal wrapped bean names "
            + "for method " + CUSTOMIZED_DOCUMENT_WRAPPED_METHOD);
    try {
      TestUtil.logMsg(
          "Loading wrapper bean " + CUSTOMIZED_STRING_OPERATION_REQUEST);
      Class c = Class.forName(CUSTOMIZED_STRING_OPERATION_REQUEST);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    try {
      TestUtil.logMsg(
          "Loading wrapper bean " + CUSTOMIZED_STRING_OPERATION_RESPONSE);
      Class c = Class.forName(CUSTOMIZED_STRING_OPERATION_RESPONSE);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyDocumentWrapped failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyInputOutputReturnAndFaultTypeElementMappings
   *
   * @assertion_ids: JAXWS:SPEC:3000; JAXWS:SPEC:3001; JAXWS:SPEC:3002;
   * JAXWS:SPEC:3004; JAXWS:SPEC:3005; JAXWS:SPEC:3007; JAXWS:SPEC:3008;
   * JAXWS:SPEC:3012; JAXWS:SPEC:3019; JAXWS:SPEC:3020; JAXWS:SPEC:3033;
   * JAXWS:SPEC:3034; JAXWS:SPEC:3035; JAXWS:SPEC:3058;
   *
   * @test_Strategy: Verify that the Java types for all in, out, in/out
   * parameter's and return value's are mapped to named XML Schema types using
   * the mapping defined in JAXB. Conformance requirement done: - Each method
   * parameter and return type is mapped to a valid XML schema type - Exception
   * naming: In the absence of customizations, the name of the wsdl:message
   * element MUST be the name of the Java exception
   *
   */
  public void VerifyInputOutputReturnAndFaultTypeElementMappings()
      throws Fault {
    TestUtil.logMsg("VerifyInputOutputReturnAndFaultTypeElementMappings");
    boolean pass = true;

    TestUtil.logMsg(
        "Get all operation elements for portType: " + EXPECTED_PORTTYPE_NAME);
    Element operations[] = DescriptionUtils.getPortTypeOperationNameElements(
        client.getDocument(), EXPECTED_PORTTYPE_NAME);
    for (int i = 0; i < operations.length; i++) {
      String opname = operations[i].getAttribute(WSDL_NAME_ATTR);
      Element input = DescriptionUtils.getChildElement(operations[i],
          WSDL_NAMESPACE_URI, WSDL_INPUT_LOCAL_NAME);
      Element output = DescriptionUtils.getChildElement(operations[i],
          WSDL_NAMESPACE_URI, WSDL_OUTPUT_LOCAL_NAME);
      Element fault[] = DescriptionUtils.getChildElements(operations[i],
          WSDL_NAMESPACE_URI, WSDL_FAULT_LOCAL_NAME);
      String imsg = null;
      String omsg = null;
      String fmsg[] = null;
      String imsgstr = null;
      String omsgstr = null;
      String fmsgstr[] = null;
      ArrayList alist1 = new ArrayList();
      ArrayList alist2 = new ArrayList();
      if (input != null) {
        imsg = input.getAttribute(WSDL_MESSAGE_ATTR);
        imsgstr = imsg.substring(imsg.indexOf(":") + 1);
      }
      if (output != null) {
        omsg = output.getAttribute(WSDL_MESSAGE_ATTR);
        omsgstr = omsg.substring(omsg.indexOf(":") + 1);
      }
      if (fault != null) {
        for (int k = 0; k < fault.length; k++) {
          String str1 = fault[k].getAttribute(WSDL_MESSAGE_ATTR);
          alist1.add(str1);
          String str2 = str1.substring(str1.indexOf(":") + 1);
          alist2.add(str2);
        }
        if (alist1.size() > 0)
          fmsg = (String[]) alist1.toArray(new String[alist1.size()]);
        if (alist2.size() > 0)
          fmsgstr = (String[]) alist2.toArray(new String[alist2.size()]);
      }
      TestUtil.logMsg("operation name is " + opname);
      TestUtil.logMsg("input message name is " + imsgstr);
      TestUtil.logMsg("output message name is " + omsgstr);
      if (fmsgstr == null)
        TestUtil.logMsg("fault message name is null");
      else {
        for (int k = 0; k < fmsgstr.length; k++)
          TestUtil.logMsg("fault message name is " + fmsgstr[k]);
      }
      Element[] iparts = null;
      Element[] oparts = null;
      Element[] fparts = null;
      if (imsgstr != null) {
        iparts = DescriptionUtils.getPartElements(client.getDocument(),
            imsgstr);
      }
      if (omsgstr != null) {
        oparts = DescriptionUtils.getPartElements(client.getDocument(),
            omsgstr);
      }
      if (fmsgstr != null) {
        Element[] parts = null;
        ArrayList alist = new ArrayList();
        for (int k = 0; k < fmsgstr.length; k++) {
          parts = DescriptionUtils.getPartElements(client.getDocument(),
              fmsgstr[k]);
          if (parts != null) {
            for (int l = 0; l < parts.length; l++)
              alist.add(parts[l]);
          }
        }
        if (alist.size() > 0)
          fparts = (Element[]) alist.toArray(new Element[alist.size()]);
      }
      if (!VerifyTypeElementMappings(iparts, oparts, fparts, opname))
        pass = false;
    }
    if (!pass)
      throw new Fault(
          "VerifyInputOutputReturnAndFaultTypeElementMappings failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyJAXBMappingsTest
   *
   * @assertion_ids: JAXWS:SPEC:2088; JAXWS:SPEC:2084; JAXWS:SPEC:2085;
   * JAXWS:SPEC:3052; JAXWS:SPEC:3054; JAXWS:SPEC:3057;
   *
   * @test_Strategy: Verify that the JAXBMappings are generated
   *
   */
  public void VerifyJAXBMappingsTest() throws Fault {
    TestUtil.logMsg("JAXBMappingsTest");
    boolean pass = true;

    // Load generated JAXB JavaBean Object for each type
    try {
      TestUtil.logMsg("Loading wrapper bean " + JAXB_ANNOTATIONS_TEST1);
      Class c = Class.forName(JAXB_ANNOTATIONS_TEST1);
      if (!AnnotationUtils.isAnnotationOnFieldPresent(c, "xmljavatypeadapter",
          jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter.class)) {
        TestUtil.logErr(
            "jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter not present for field (xmljavatypeadapter)");
        pass = false;
      }
      XmlJavaTypeAdapter a = (XmlJavaTypeAdapter) AnnotationUtils
          .getAnnotationOnField(c, "xmljavatypeadapter",
              jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter.class);
      String type = a.value().getName();
      if (type.equals("jakarta.xml.bind.annotation.adapters.HexBinaryAdapter"))
        TestUtil.logMsg("XmlJavaTypeAdapter value is of expected type: "
            + "jakarta.xml.bind.annotation.adapters.HexBinaryAdapter");
      else
        TestUtil
            .logErr("XmlJavaTypeAdapter value is of unexpected type: " + type);

      XmlElement x = (XmlElement) AnnotationUtils.getAnnotationOnField(c,
          "xmljavatypeadapter", jakarta.xml.bind.annotation.XmlElement.class);
      if (!AnnotationUtils.verifyXmlElement(x, "xmljavatypeadapter", "foo",
          true, true))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Loading wrapper bean " + JAXB_ANNOTATIONS_TEST2);
      Class c = Class.forName(JAXB_ANNOTATIONS_TEST2);
      if (!AnnotationUtils.isAnnotationOnFieldPresent(c, "xmlmimetype",
          jakarta.xml.bind.annotation.XmlMimeType.class)) {
        TestUtil.logErr(
            "jakarta.xml.bind.annotation.XmlMimeType not present for field (xmlmimetype)");
        pass = false;
      }
      XmlMimeType a = (XmlMimeType) AnnotationUtils.getAnnotationOnField(c,
          "xmlmimetype", jakarta.xml.bind.annotation.XmlMimeType.class);
      String type = a.value();
      if (type.equals("application/octet-stream"))
        TestUtil.logMsg("XmlMimeType value is of expected type: "
            + "application/octet-stream");
      else
        TestUtil.logErr("XmlMimeType value is of unexpected type: " + type);

      XmlElement x = (XmlElement) AnnotationUtils.getAnnotationOnField(c,
          "xmlmimetype", jakarta.xml.bind.annotation.XmlElement.class);
      if (!AnnotationUtils.verifyXmlElement(x, "xmlmimetype", "foo", false,
          false))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Loading wrapper bean " + JAXB_ANNOTATIONS_TEST3);
      Class c = Class.forName(JAXB_ANNOTATIONS_TEST3);
      if (!AnnotationUtils.isAnnotationOnFieldPresent(c, "xmlattachmentref",
          jakarta.xml.bind.annotation.XmlAttachmentRef.class)) {
        TestUtil.logErr(
            "jakarta.xml.bind.annotation.XmlAttachmentRef not present for field (xmlattachmentref)");
        pass = false;
      }

      XmlElement x = (XmlElement) AnnotationUtils.getAnnotationOnField(c,
          "xmlattachmentref", jakarta.xml.bind.annotation.XmlElement.class);
      if (!AnnotationUtils.verifyXmlElement(x, "xmlattachmentref", "foo", false,
          false))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Loading wrapper bean " + JAXB_ANNOTATIONS_TEST4);
      Class c = Class.forName(JAXB_ANNOTATIONS_TEST4);
      if (!AnnotationUtils.isAnnotationOnFieldPresent(c, "xmllist",
          jakarta.xml.bind.annotation.XmlList.class)) {
        TestUtil.logErr(
            "jakarta.xml.bind.annotation.XmlList not present for field (xmllist)");
        pass = false;
      }

      XmlElement x = (XmlElement) AnnotationUtils.getAnnotationOnField(c,
          "xmllist", jakarta.xml.bind.annotation.XmlElement.class);
      if (!AnnotationUtils.verifyXmlElement(x, "xmllist", "foo", false, true))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyJAXBMappingsTest failed");
    else
      TestUtil.logMsg("Verification passed");
  }

  /*
   * @testName: VerifyXmlTypeAnnotationOnFaultBean
   *
   * @assertion_ids: JAXWS:SPEC:3030;
   *
   * @test_Strategy: Verify that the generated fault bean has the correct
   * XmlType
   *
   */
  public void VerifyXmlTypeAnnotationOnFaultBean() throws Fault {
    TestUtil.logMsg("VerifyXmlTypeAnnotationOnFaultBean");
    boolean pass = true;

    // Load generated JAXB FaultBean Class
    try {
      TestUtil.logMsg("Loading fault bean " + JAXB_ANNOTATIONS_FAULTBEAN);
      Class c = Class.forName(JAXB_ANNOTATIONS_FAULTBEAN);
      XmlType x = (XmlType) AnnotationUtils.getAnnotationOnClass(c,
          jakarta.xml.bind.annotation.XmlType.class);
      if (!AnnotationUtils.verifyXmlType(x, "Bozo", "http://bozo.org/wsdl"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("VerifyXmlTypeAnnotationOnFaultBean failed");
    else
      TestUtil.logMsg("Verification passed");
  }
}
