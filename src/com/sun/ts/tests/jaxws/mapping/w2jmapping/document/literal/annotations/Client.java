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

package com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import java.util.Properties;

import com.sun.ts.tests.jaxws.common.AnnotationUtils;

public class Client extends ServiceEETest {
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
   * @class.setup_props: ts.home;
   */
  public void setup(String[] args, Properties properties) throws Fault {
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  /*
   * @testName: RequestWrapperAnnotationTest
   *
   * @assertion_ids: JAXWS:SPEC:7004; JAXWS:SPEC:7013; JAXWS:JAVADOC:39;
   * JAXWS:JAVADOC:40; JAXWS:JAVADOC:41;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void RequestWrapperAnnotationTest() throws Fault {
    TestUtil.logTrace("RequestWrapperAnnotationTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint");

      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing WebService for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyRequestWrapperAnnotation(c,
          "wrapperElement1", "WrapperElement1",
          "http://w2jdlannotations.org/types",
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.WrapperElement1");
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RequestWrapperAnnotationTest failed", e);
    }

    if (!pass)
      throw new Fault("RequestWrapperAnnotationTest failed");
  }

  /*
   * @testName: ResponseWrapperAnnotationTest
   *
   * @assertion_ids: JAXWS:SPEC:7005; JAXWS:SPEC:7013; JAXWS:JAVADOC:43;
   * JAXWS:JAVADOC:44; JAXWS:JAVADOC:45;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void ResponseWrapperAnnotationTest() throws Fault {
    TestUtil.logTrace("ResponseWrapperAnnotationTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint");

      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing WebService for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyResponseWrapperAnnotation(c,
          "wrapperElement1", "WrapperElement11",
          "http://w2jdlannotations.org/types",
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.WrapperElement11");
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ResponseWrapperAnnotationTest failed", e);
    }

    if (!pass)
      throw new Fault("ResponseWrapperAnnotationTest failed");
  }

  /*
   * @testName: WebServiceAnnotationTest
   *
   * @assertion_ids: JAXWS:SPEC:2011; JAXWS:SPEC:7011; JAXWS:SPEC:7013;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void WebServiceAnnotationTest() throws Fault {
    TestUtil.logTrace("WebServiceAnnotationTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());

      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing WebService for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyWebServiceAnnotation(c,
          "W2JDLAnnotationsEndpoint", "http://w2jdlannotations.org/wsdl",
          "W2JDLAnnotations", "W2JDLAnnotations.wsdl", "");
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceAnnotationTest failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceAnnotationTest failed");
  }

  /*
   * @testName: SOAPBindingAnnotationTest
   *
   * @assertion_ids: JAXWS:SPEC:7011; JAXWS:SPEC:2019; JAXWS:SPEC:7013;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void SOAPBindingAnnotationTest() throws Fault {
    TestUtil.logTrace("SOAPBindingAnnotationTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());

      boolean result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "wrapperElement1", "DOCUMENT", "LITERAL", "WRAPPED");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "helloOperation", "DOCUMENT", "LITERAL", "BARE");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "onewayOperation", "DOCUMENT", "LITERAL", "BARE");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "mode1Operation", "DOCUMENT", "LITERAL", "BARE");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "mode2Operation", "DOCUMENT", "LITERAL", "BARE");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifySOAPBindingAnnotationPerMethod(c,
          "mode3Operation", "DOCUMENT", "LITERAL", "BARE");
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPBindingAnnotationTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPBindingAnnotationTest failed");
  }

  /*
   * @testName: HelloOperationAnnotationsTest
   *
   * @assertion_ids: JAXWS:SPEC:7009; JAXWS:SPEC:7011; JAXWS:SPEC:7013;
   * JAXWS:SPEC:2020; JAXWS:SPEC:2021; JAXWS:SPEC:2015;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void HelloOperationAnnotationsTest() throws Fault {
    TestUtil.logTrace("HelloOperationAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());

      boolean result = AnnotationUtils.verifyWebMethodAnnotation(c,
          "helloOperation", "helloOperation", "");
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyWebResultAnnotation(c, "helloOperation",
          "HelloStringElement", "http://w2jdlannotations.org/types");
      if (result == false) {
        pass = false;
      }

      result = AnnotationUtils.verifyWebParamAnnotation(c, 0, "helloOperation",
          "HelloStringElement", "http://w2jdlannotations.org/types", "IN",
          false);
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HelloOperationAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("HelloOperationAnnotationsTest failed");
  }

  /*
   * @testName: OnewayAnnotationsTest
   *
   * @assertion_ids: JAXWS:SPEC:7010; JAXWS:SPEC:7011; JAXWS:SPEC:2018;
   * JAXWS:SPEC:7013; JAXWS:SPEC:2020;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void OnewayAnnotationsTest() throws Fault {
    TestUtil.logTrace("OnewayAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());

      boolean result = AnnotationUtils.verifyOnewayAnnotation(c,
          "onewayOperation");
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyWebParamAnnotation(c, 0, "onewayOperation",
          "OneWayStringElement", "http://w2jdlannotations.org/types", "IN",
          false);
      if (result == false) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("OnewayAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("OnewayAnnotationsTest failed");
  }

  /*
   * @testName: WebParamModesAnnotationsTest
   *
   * @assertion_ids: JAXWS:SPEC:7010; JAXWS:SPEC:7011; JAXWS:SPEC:2020;
   * JAXWS:SPEC:7013; JAXWS:SPEC:2015;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void WebParamModesAnnotationsTest() throws Fault {
    TestUtil.logTrace("WebParamModesAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());
      TestUtil.logTrace("------------------------");
      TestUtil.logTrace("testing mode1Operation annotations");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyWebMethodAnnotation(c,
          "mode1Operation", "mode1Operation", "");
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyWebParamAnnotation(c, 0, "mode1Operation",
          "Mode1StringElement", "http://w2jdlannotations.org/types", "IN",
          false);
      if (result == false) {
        pass = false;
      }

      TestUtil.logTrace("------------------------");
      TestUtil.logTrace("testing mode2Operation annotations");
      TestUtil.logTrace("------------------------");
      result = AnnotationUtils.verifyWebMethodAnnotation(c, "mode2Operation",
          "mode2Operation", "");
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyWebParamAnnotation(c, 0, "mode2Operation",
          "Mode2StringElement", "http://w2jdlannotations.org/types", "INOUT",
          false);
      if (result == false) {
        pass = false;
      }

      TestUtil.logTrace("------------------------");
      TestUtil.logTrace("testing mode3Operation annotations");
      TestUtil.logTrace("------------------------");
      result = AnnotationUtils.verifyWebMethodAnnotation(c, "mode3Operation",
          "mode3Operation", "");
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyWebParamAnnotation(c, 0, "mode3Operation",
          "Mode3StringElement", "http://w2jdlannotations.org/types", "OUT",
          false);
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebParamModesAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("WebParamModesAnnotationsTest failed");
  }

  /*
   * @testName: WebFaultAnnotationsTest
   *
   * @assertion_ids: JAXWS:SPEC:7003; JAXWS:SPEC:7013; JAXWS:JAVADOC:63;
   * JAXWS:JAVADOC:64; JAXWS:JAVADOC:65;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void WebFaultAnnotationsTest() throws Fault {
    TestUtil.logTrace("WebFaultAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.MyFault_Exception",
          false, this.getClass().getClassLoader());
      boolean result = AnnotationUtils.verifyWebFaultAnnotation(c, "MyFault",
          "http://w2jdlannotations.org/types",
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.MyFault");
      if (result == false) {
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebFaultAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("WebFaultAnnotationsTest failed");
  }

  /*
   * @testName: WebServiceClientAnnotationsTest
   *
   * @assertion_ids: JAXWS:JAVADOC:66; JAXWS:JAVADOC:67; JAXWS:JAVADOC:68;
   * JAXWS:SPEC:7013; JAXWS:SPEC:7006; JAXWS:SPEC:2063;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void WebServiceClientAnnotationsTest() throws Fault {
    TestUtil.logTrace("WebServiceClientAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotations",
          false, this.getClass().getClassLoader());
      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing WebServiceClient for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyWebServiceClientAnnotation(c,
          "W2JDLAnnotations", "http://w2jdlannotations.org/wsdl",
          "W2JDLAnnotations.wsdl");
      if (result == false) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceClientAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceClientAnnotationsTest failed");
  }

  /*
   * @testName: WebEndpointAnnotationsTest
   *
   * @assertion_ids: JAXWS:JAVADOC:62; JAXWS:SPEC:7007; JAXWS:SPEC:2065;
   * JAXWS:SPEC:7013;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void WebEndpointAnnotationsTest() throws Fault {
    TestUtil.logTrace("WebEndpointAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotations",
          false, this.getClass().getClassLoader());
      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing WebEndpoint for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyWebEndpointAnnotation(c,
          "getW2JDLAnnotationsEndpointPort", "W2JDLAnnotationsEndpointPort");
      if (result == false) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebEndpointAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("WebEndpointAnnotationsTest failed");
  }

  /*
   * @testName: HandlerChainAnnotationsTest
   *
   * @assertion_ids: JAXWS:SPEC:9008; JAXWS:SPEC:7013;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void HandlerChainAnnotationsTest() throws Fault {
    TestUtil.logTrace("HandlerChainAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.W2JDLAnnotationsEndpoint",
          false, this.getClass().getClassLoader());
      TestUtil.logMsg("Processing class level annnotations");
      TestUtil.logTrace("========================");
      TestUtil.logTrace("testing HandlerChain for the class");
      TestUtil.logTrace("------------------------");
      boolean result = AnnotationUtils.verifyHandlerChainAnnotation(c, null);
      if (result == false) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerChainAnnotationsTest failed", e);
    }

    if (!pass)
      throw new Fault("HandlerChainAnnotationsTest failed");
  }

  /*
   * @testName: ActionFaultActionAndAddressingAnnotationsTest
   *
   * @assertion_ids: JAXWS:JAVADOC:130; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:3000; WSAMD:SPEC:3000.1;
   * JAXWS:SPEC:2075; JAXWS:SPEC:2077; JAXWS:SPEC:2078; JAXWS:SPEC:2079;
   * JAXWS:SPEC:2080; JAXWS:SPEC:2081; JAXWS:SPEC:2082; JAXWS:SPEC:2083;
   * WSAMD:SPEC:3001.5; WSAMD:SPEC:3001.8; JAXWS:SPEC:2089;
   *
   * @test_Strategy: Generate classes from a wsdl and verify existence of
   * annotations
   */
  public void ActionFaultActionAndAddressingAnnotationsTest() throws Fault {
    TestUtil.logTrace("ActionFaultActionAndAddressingAnnotationsTest");
    boolean pass = true;
    try {
      Class c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.AddressAnnotationsEndpoint1",
          false, this.getClass().getClassLoader());
      String[][] faults = {};
      boolean result = AnnotationUtils.verifyActionFaultActionAnnotation(c,
          "address1", true, "input1", null, faults);
      if (result == false) {
        pass = false;
      }
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address2",
          true, null, "output2", faults);
      if (result == false) {
        pass = false;
      }
      String faults3[][] = { {
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.Myfault3AException",
          "fault3a" },
          { "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.Myfault3BException",
              "fault3b" } };
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address3",
          true, null, null, faults3);
      if (result == false) {
        pass = false;
      }
      c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.AddressAnnotationsEndpoint2",
          false, this.getClass().getClassLoader());
      String faults4[][] = { {
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.Myfault4Exception",
          "fault4" } };
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address4",
          true, null, "output4", faults4);
      if (result == false) {
        pass = false;
      }
      String faults5[][] = { {
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.Myfault5Exception",
          "fault5" } };
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address5",
          true, "input5", "output5", faults5);
      if (result == false) {
        pass = false;
      }
      c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.AddressAnnotationsEndpoint3",
          false, this.getClass().getClassLoader());
      String faults6[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address6",
          false, "", "", faults6);
      if (result == false) {
        pass = false;
      }
      String faults7[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address7",
          false, "", "", faults7);
      if (result == false) {
        pass = false;
      }
      c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.AddressAnnotationsEndpoint4",
          false, this.getClass().getClassLoader());
      String faults8[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address8",
          true, "input8", null, faults8);
      if (result == false) {
        pass = false;
      }
      String faults9[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address9",
          true, null, "output9", faults9);
      if (result == false) {
        pass = false;
      }
      c = Class.forName(
          "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.annotations.AddressAnnotationsEndpoint5",
          false, this.getClass().getClassLoader());
      String faults10[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address10",
          true, "input10", null, faults10);
      if (result == false) {
        pass = false;
      }
      String faults11[][] = {};
      result = AnnotationUtils.verifyActionFaultActionAnnotation(c, "address11",
          true, null, "output11", faults11);
      if (result == false) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ActionFaultActionAndAddressingAnnotationsTest failed",
          e);
    }

    if (!pass)
      throw new Fault("ActionFaultActionAndAddressingAnnotationsTest failed");
  }

}
