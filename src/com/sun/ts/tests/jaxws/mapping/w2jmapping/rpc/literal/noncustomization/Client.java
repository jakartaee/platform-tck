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

package com.sun.ts.tests.jaxws.mapping.w2jmapping.rpc.literal.noncustomization;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.net.URL;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import java.util.Properties;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.AnnotationUtils;

public class Client extends ServiceEETest {
  private static final String WSDL_PKG = "w2jrlnoncustomization.wsdl.";

  private static final String TYPES_PKG = "w2jrlnoncustomization.types.";

  // Expected mappings for wsdl:fault element mapping
  private static final String EXPECTED_FAULT_WRAPPER = WSDL_PKG + "MyFault";

  private static final String EXPECTED_FAULT_BEAN = TYPES_PKG + "MyFaultReason";

  // Expected mappings for wsdl:service and wsdl:port element mapping
  private static final String EXPECTED_SERVICE = WSDL_PKG
      + "W2JRLNoncustomization";

  private static final String EXPECTED_ENDPOINT = WSDL_PKG
      + "W2JRLNoncustomizationEndpoint";

  private static final String EXPECTED_SERVICE_INTERFACE = "jakarta.xml.ws.Service";

  private static final String EXPECTED_SERVICE_EXCEPTION = "jakarta.xml.ws.WebServiceException";

  private static final String EXPECTED_GET_PORTNAME_METHOD = "getW2JRLNoncustomizationEndpointPort";

  // Used for soap:header binding test
  private static final String EXPECTED_HEADER_TYPE = TYPES_PKG + "MyHeader";

  // Used for wrapper style tests
  private static final String WRAPPER_METHOD = "wrapperElement1";

  private static final String EXPECTED_WRAPPER_RETURN_TYPE = "w2jrlnoncustomization.types.WrapperElement11";

  private static final String EXPECTED_WRAPPER_PARAMETER_TYPE = "w2jrlnoncustomization.types.WrapperElement1";

  private static final String NONWRAPPER_METHOD = "wrapperElement2";

  private static final String EXPECTED_NONWRAPPER_RETURN_TYPE = "w2jrlnoncustomization.types.WrapperElement22";

  private static final String EXPECTED_NONWRAPPER_PARAMETER_TYPE = "w2jrlnoncustomization.types.WrapperElement1";

  // Used for soap:header and soap:fault binding test
  private static final String EXPECTED_HEADER2_TYPE = TYPES_PKG
      + "ConfigHeader";

  private static final String EXPECTED_FAULT1_EXCEPTION = WSDL_PKG + "AFault1";

  private static final String EXPECTED_FAULT2_EXCEPTION = WSDL_PKG + "AFault2";

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
   * @testName: PortTypeTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2005; JAXWS:SPEC:2009; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify mapping of wsdl:portType
   */
  public void PortTypeTest() throws Fault {
    TestUtil.logTrace("PortTypeTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Verify wsdl:portType mapping");
      Class.forName(EXPECTED_ENDPOINT, false, this.getClass().getClassLoader());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("PortTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("PortTypeTest failed");
  }

  /*
   * @testName: OperationTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2013; JAXWS:SPEC:2018; JAXWS:SPEC:2017;
   * JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify mapping of wsdl:operation
   */
  public void OperationTest() throws Fault {
    TestUtil.logTrace("OperationTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Verify wsdl:operation mapping");
      Class c = Class.forName(EXPECTED_ENDPOINT, false,
          this.getClass().getClassLoader());
      String methodName = "helloOperation";
      if (!JAXWS_Util.doesMethodExist(c, methodName)) {
        TestUtil.logErr("Method " + methodName + ", was not found");
        pass = false;
      }
      methodName = "onewayOperation";
      if (!JAXWS_Util.doesMethodExist(c, methodName)) {
        TestUtil.logErr("Method " + methodName + ", was not found");
        pass = false;
      }
      methodName = "mode1Operation";
      if (!JAXWS_Util.doesMethodExist(c, methodName)) {
        TestUtil.logErr("Method " + methodName + ", was not found");
        pass = false;
      }
      methodName = "mode2Operation";
      if (!JAXWS_Util.doesMethodExist(c, methodName)) {
        TestUtil.logErr("Method " + methodName + ", was not found");
        pass = false;
      }
      methodName = "mode3Operation";
      if (!JAXWS_Util.doesMethodExist(c, methodName)) {
        TestUtil.logErr("Method " + methodName + ", was not found");
        pass = false;
      }
      if (!pass) {
        TestUtil.logErr("One ofthe operations does not exist in the SEI");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("OperationTest failed", e);
    }

    if (!pass)
      throw new Fault("OperationTest failed");
  }

  /*
   * @testName: FaultTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2043; JAXWS:SPEC:2044; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify wsdl:fault element mapping
   */
  public void FaultTest() throws Fault {
    TestUtil.logTrace("FaultTest");
    boolean pass = true;

    TestUtil.logMsg("Verify wsdl:fault mapping");
    TestUtil.logMsg("Loading fault wrapper " + EXPECTED_FAULT_WRAPPER);
    Class faultWrapper = null;
    try {
      faultWrapper = Class.forName(EXPECTED_FAULT_WRAPPER);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // Check to ensure Wrapper Exception class is annotated using the WebFault
    // annotation.
    boolean found = AnnotationUtils.verifyWebFaultAnnotation(faultWrapper,
        "MyFaultReason", "http://w2jrlnoncustomization/types",
        EXPECTED_FAULT_BEAN);
    if (!found) {
      TestUtil.logErr(
          "Wrapper Exception Class is not annotated with WebFault annotation - "
              + EXPECTED_FAULT_WRAPPER);
      pass = false;
    } else
      TestUtil.logMsg(
          "Wrapper Exception Class is annotated with WebFault annotation - "
              + EXPECTED_FAULT_WRAPPER);

    TestUtil.logMsg("Loading fault bean " + EXPECTED_FAULT_BEAN);
    Class faultBean = null;
    try {
      faultBean = Class.forName(EXPECTED_FAULT_BEAN);
      TestUtil.logMsg("faultBean=" + faultBean);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    TestUtil.logMsg("Instantiate fault bean and call its methods ... "
        + EXPECTED_FAULT_BEAN);
    w2jrlnoncustomization.types.MyFaultReason mfr = new w2jrlnoncustomization.types.MyFaultReason();
    TestUtil.logMsg("setMessage to foo");
    mfr.setMessage("foo");
    TestUtil.logMsg("getMessage=" + mfr.getMessage());

    TestUtil.logMsg("Instantiate fault wrapper exception constructor 1 ... "
        + EXPECTED_FAULT_WRAPPER);
    w2jrlnoncustomization.wsdl.MyFault mf = new w2jrlnoncustomization.wsdl.MyFault(
        "myfault", mfr);

    TestUtil.logMsg("getFaultInfo from wrapper exception ... ");
    mfr = mf.getFaultInfo();

    TestUtil.logMsg("Instantiate fault wrapper exception constructor 2 ... "
        + EXPECTED_FAULT_WRAPPER);
    mf = new w2jrlnoncustomization.wsdl.MyFault("myfault", mfr,
        new Exception("foo"));
    TestUtil.logMsg("mf=" + mf);

    // A generate endpoint interface must be created from wsdl:portType name
    TestUtil.logMsg("Loading endpoint interface " + EXPECTED_ENDPOINT);
    Class endpointClass = null;
    try {
      endpointClass = Class.forName(EXPECTED_ENDPOINT);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // Fault Equivalence Test
    try {
      Method[] methods = endpointClass.getMethods();
      Method helloOp = null;
      Method helloOp2 = null;
      for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equals("helloOperation"))
          helloOp = methods[i];
        if (methods[i].getName().equals("helloOperation2"))
          helloOp2 = methods[i];
      }

      found = false;
      Class exceptions[] = helloOp.getExceptionTypes();
      for (int i = 0; i < exceptions.length; i++) {
        String name = exceptions[i].getName();
        TestUtil.logMsg("exceptions[" + i + "]=" + name);
        if (name.equals(EXPECTED_FAULT_WRAPPER))
          found = true;
      }
      if (!found) {
        TestUtil.logErr("helloOperation does not declare throws of exception "
            + EXPECTED_FAULT_WRAPPER);
        pass = false;
      } else
        TestUtil.logMsg("helloOperation does declare throws of exception "
            + EXPECTED_FAULT_WRAPPER);

      found = false;
      exceptions = helloOp2.getExceptionTypes();
      for (int i = 0; i < exceptions.length; i++) {
        String name = exceptions[i].getName();
        TestUtil.logMsg("exceptions[" + i + "]=" + name);
        if (name.equals(EXPECTED_FAULT_WRAPPER))
          found = true;
      }
      if (!found) {
        TestUtil.logErr("helloOperation2 does not declare throws of exception "
            + EXPECTED_FAULT_WRAPPER);
        pass = false;
      } else
        TestUtil.logMsg("helloOperation2 does declare throws of exception "
            + EXPECTED_FAULT_WRAPPER);
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
    }

    if (!pass)
      throw new Fault("FaultTest failed");
  }

  /*
   * @testName: ServiceAndPortTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2055; JAXWS:SPEC:2056; JAXWS:SPEC:2040;
   * JAXWS:SPEC:2070; JAXWS:SPEC:2071;
   *
   * @test_Strategy: Verify wsdl:service and wsdl:port element mapping
   */
  public void ServiceAndPortTest() throws Fault {
    TestUtil.logTrace("ServiceAndPortTest");
    boolean pass = true;

    TestUtil.logMsg("Verify wsdl:service and wsdl:port mapping");
    // A generate service interface must be created from wsdl:service name
    TestUtil.logMsg("Loading service interface " + EXPECTED_SERVICE);
    Class serviceClass = null;
    try {
      serviceClass = Class.forName(EXPECTED_SERVICE);
      TestUtil.logMsg("serviceClass=" + serviceClass);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // A generate endpoint interface must be created from wsdl:portType name
    TestUtil.logMsg("Loading endpoint interface " + EXPECTED_ENDPOINT);
    try {
      Class.forName(EXPECTED_ENDPOINT);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // Service Class Interface MUST extend Service interface
    boolean found = false;
    if (serviceClass != null) {
      String name = serviceClass.getSuperclass().getName();
      if (name.equals(EXPECTED_SERVICE_INTERFACE)) {
        found = true;
      }
      if (!found) {
        TestUtil.logErr("Service Class Interface " + name + " does not extend "
            + EXPECTED_SERVICE_INTERFACE);
        pass = false;
      } else
        TestUtil.logMsg("Service Class Interface " + name + " does extend "
            + EXPECTED_SERVICE_INTERFACE);

    }

    // Service Class Interface MUST have a getPortName() method based on
    // wsd:port name
    found = false;
    Method m = null;
    if (serviceClass != null) {
      try {
        m = serviceClass.getDeclaredMethod(EXPECTED_GET_PORTNAME_METHOD,
            (Class[]) null);
      } catch (Exception e) {
        TestUtil.logErr("Exception: " + e);
        pass = false;
      }

      // getPortName() method MUST return Endpoint Interface Type
      if (m != null) {
        TestUtil.logMsg("Service Class Interface " + serviceClass.getName()
            + " does have port method " + EXPECTED_GET_PORTNAME_METHOD);
        Class<?> returnType = m.getReturnType();
        found = false;
        if (returnType != null) {
          String name = returnType.getName();
          TestUtil.logMsg("returnType=" + name);
          if (name.equals(EXPECTED_ENDPOINT))
            found = true;
        }
        if (!found) {
          TestUtil.logErr("Service Port Method " + EXPECTED_GET_PORTNAME_METHOD
              + " does not return type as " + EXPECTED_ENDPOINT);
          pass = false;
        } else
          TestUtil.logMsg("Service Port Method " + EXPECTED_GET_PORTNAME_METHOD
              + " does return type as " + EXPECTED_ENDPOINT);
      } else {
        TestUtil.logErr("Service Class Interface " + serviceClass.getName()
            + " does not have port method " + EXPECTED_GET_PORTNAME_METHOD);
        pass = false;
      }

      // Service must have 2 constructors
      Constructor ctr1, ctr2;
      try {
        ctr1 = serviceClass.getConstructor();
        TestUtil.logMsg("constructor1=" + ctr1);
        if (ctr1 == null) {
          TestUtil.logErr("Default constructor non-existant");
          pass = false;
        }
        ctr2 = serviceClass.getConstructor(URL.class, QName.class);
        TestUtil.logMsg("constructor2=" + ctr2);
        if (ctr2 == null) {
          TestUtil.logErr("2 arg constructor non-existant");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception: " + e);
        pass = false;
      }
    }

    if (!pass)
      throw new Fault("ServiceAndPortTest failed");
  }

  /*
   * @testName: HeaderTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify soap:header (Header Binding Extension)
   */
  public void HeaderTest() throws Fault {
    TestUtil.logTrace("HeaderTest");
    boolean pass = true;

    TestUtil.logMsg("Verify soap:header (Header Binding Extension)");
    // A generate endpoint interface must be created from wsdl:portType name
    TestUtil.logMsg("Loading endpoint interface " + EXPECTED_ENDPOINT);
    Class endpointClass = null;
    try {
      endpointClass = Class.forName(EXPECTED_ENDPOINT);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // The Header generated type
    TestUtil.logMsg("Loading header type " + EXPECTED_HEADER_TYPE);
    try {
      Class.forName(EXPECTED_HEADER_TYPE);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // no customization via jaxws:enableAdditionalSOAPHeaderMapping means no
    // header type
    // as parameter to this method.
    try {
      Method helloOp = endpointClass.getMethod("helloOperation",
          new Class[] { w2jrlnoncustomization.types.StringElement.class });

      boolean found = false;
      Class parameters[] = helloOp.getParameterTypes();
      for (int i = 0; i < parameters.length; i++) {
        String name = parameters[i].getName();
        TestUtil.logMsg("parameters[" + i + "]=" + name);
        if (name.equals(EXPECTED_HEADER_TYPE))
          found = true;
      }
      if (!found) {
        TestUtil.logMsg(
            "helloOperation does not declare a header as parameter as expected for type "
                + EXPECTED_HEADER_TYPE);
      } else {
        TestUtil.logErr(
            "helloOperation does declare a header as parameter - unexpected for type "
                + EXPECTED_HEADER_TYPE);
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
    }

    if (!pass)
      throw new Fault("HeaderTest failed");
  }

  /*
   * @testName: SoapHeaderAndFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2004; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify soap:headerfault (Header Fault Binding Extension)
   */
  public void SoapHeaderAndFaultTest() throws Fault {
    TestUtil.logTrace("SoapHeaderAndFaultTest");
    boolean pass = true;

    TestUtil.logMsg("Verify soap:header and soap:fault mappings");
    // The generated endpoint interface must be created from wsdl:portType name
    TestUtil.logMsg("Loading endpoint interface " + EXPECTED_ENDPOINT);
    Class endpointClass = null;
    try {
      endpointClass = Class.forName(EXPECTED_ENDPOINT);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // The generated Header type
    TestUtil.logMsg("Loading header type " + EXPECTED_HEADER2_TYPE);
    try {
      Class.forName(EXPECTED_HEADER2_TYPE);
    } catch (Exception e) {
      TestUtil.logErr("Exception loading class: " + e);
      pass = false;
    }

    // No customization via jaxws:enableAdditionalSOAPHeaderMapping means no
    // header type
    // as parameter to this method.
    try {
      Method theMethod = null;
      Method[] methods = endpointClass.getMethods();
      for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equals("operationWithHeaderAndFaults")) {
          theMethod = methods[i];
          break;
        }
      }

      if (theMethod != null) {
        boolean found = false;
        Class parameters[] = theMethod.getParameterTypes();
        TestUtil.logMsg(
            "Verify that the soap:header is mapped to a parameter of the operation");
        for (int i = 0; i < parameters.length; i++) {
          String name = parameters[i].getName();
          TestUtil.logMsg("parameters[" + i + "]=" + name);
          if (name.equals(EXPECTED_HEADER2_TYPE))
            found = true;
        }
        if (!found) {
          TestUtil.logErr(
              "operationWithHeaderAndFaults does not declare a header as parameter for type "
                  + EXPECTED_HEADER2_TYPE);
          pass = false;
        } else {
          TestUtil.logMsg(
              "operationWithHeaderAndFaults does declare a header as parameter for type "
                  + EXPECTED_HEADER2_TYPE);
        }

        Class[] exceptions = theMethod.getExceptionTypes();
        boolean exception1 = false;
        boolean exception2 = false;
        TestUtil.logMsg(
            "Verify that each soap:fault is mapped to the expected exception name");
        for (int j = 0; j < exceptions.length; j++) {
          String exceptName = exceptions[j].getName();
          TestUtil.logMsg("exception[" + j + "]=" + exceptName);
          if (exceptName.equals(EXPECTED_FAULT1_EXCEPTION)) {
            exception1 = true;
          } else if (exceptName.equals(EXPECTED_FAULT2_EXCEPTION)) {
            exception2 = true;
          }
        }
        if (!exception1) {
          TestUtil.logErr("The method: " + theMethod.getName()
              + " did not declare exception\n" + EXPECTED_FAULT1_EXCEPTION);
          pass = false;
        }
        if (!exception2) {
          TestUtil.logErr("The method: " + theMethod.getName()
              + " did not declare exception\n" + EXPECTED_FAULT2_EXCEPTION);
          pass = false;
        }
      } else {
        TestUtil.logErr("operationWithHeaderAndFaults was not found!");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception: " + e);
    }

    if (!pass)
      throw new Fault("SoapHeaderAndFaultTest failed");
  }

  /*
   * @testName: WrapperStyleTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2024; JAXWS:SPEC:2025; JAXWS:SPEC:2027; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify that each message results in a paramenter
   */
  public void WrapperStyleTest() throws Fault {
    TestUtil.logTrace("WrapperStyleTest");
    boolean pass = true;
    try {
      Class c = Class.forName(EXPECTED_ENDPOINT, false,
          this.getClass().getClassLoader());
      Class returnType = JAXWS_Util.getMethodReturnType(c, WRAPPER_METHOD);
      if (returnType != null) {
        String sReturnType = returnType.getName();
        if (!sReturnType.equals(EXPECTED_WRAPPER_RETURN_TYPE)) {
          TestUtil.logErr(
              "The return type for method: " + WRAPPER_METHOD + " was wrong");
          TestUtil.logErr("expected=" + EXPECTED_WRAPPER_RETURN_TYPE);
          TestUtil.logErr("actual=" + sReturnType);
          pass = false;
        }
      } else {
        TestUtil.logErr("The method: " + WRAPPER_METHOD
            + " was not found for class:" + EXPECTED_ENDPOINT);
        pass = false;
      }
      Class parameterType = JAXWS_Util.getMethodParameterType(c, WRAPPER_METHOD,
          0);
      if (parameterType != null) {
        String sParameterType = parameterType.getName();
        if (!sParameterType.equals(EXPECTED_WRAPPER_PARAMETER_TYPE)) {
          TestUtil.logErr("The parameter type for method: " + WRAPPER_METHOD
              + " was wrong");
          TestUtil.logErr("expected=" + EXPECTED_WRAPPER_PARAMETER_TYPE);
          TestUtil.logErr("actual=" + sParameterType);
          pass = false;
        }
      } else {
        TestUtil.logErr("The method: " + WRAPPER_METHOD
            + " was not found for class:" + EXPECTED_ENDPOINT
            + " or the specified parameter did not exist");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WrapperStyleTest failed", e);
    }

    if (!pass)
      throw new Fault("WrapperStyleTest failed");
  }

  /*
   * @testName: NonWrapperStyleTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2002; JAXWS:SPEC:2003;
   * JAXWS:SPEC:2024; JAXWS:SPEC:2025; JAXWS:SPEC:2027; JAXWS:SPEC:2040;
   *
   * @test_Strategy: Verify that each message results in a paramenter
   */
  public void NonWrapperStyleTest() throws Fault {
    TestUtil.logTrace("NonWrapperStyleTest");
    boolean pass = true;
    try {
      Class c = Class.forName(EXPECTED_ENDPOINT, false,
          this.getClass().getClassLoader());
      Class returnType = JAXWS_Util.getMethodReturnType(c, NONWRAPPER_METHOD);
      if (returnType != null) {
        String sReturnType = returnType.getName();
        if (!sReturnType.equals(EXPECTED_NONWRAPPER_RETURN_TYPE)) {
          TestUtil.logErr("The return type for method: " + NONWRAPPER_METHOD
              + " was wrong");
          TestUtil.logErr("expected=" + EXPECTED_NONWRAPPER_RETURN_TYPE);
          TestUtil.logErr("actual=" + sReturnType);
          pass = false;
        }
      } else {
        TestUtil.logErr("The method: " + NONWRAPPER_METHOD
            + " was not found for class:" + EXPECTED_ENDPOINT);
        pass = false;
      }
      Class parameterType = JAXWS_Util.getMethodParameterType(c,
          NONWRAPPER_METHOD, 0);
      if (parameterType != null) {
        String sParameterType = parameterType.getName();
        if (!sParameterType.equals(EXPECTED_NONWRAPPER_PARAMETER_TYPE)) {
          TestUtil.logErr("The parameter type for method: " + NONWRAPPER_METHOD
              + " was wrong");
          TestUtil.logErr("expected=" + EXPECTED_NONWRAPPER_PARAMETER_TYPE);
          TestUtil.logErr("actual=" + sParameterType);
          pass = false;
        }
      } else {
        TestUtil.logErr("The method: " + NONWRAPPER_METHOD
            + " was not found for class:" + EXPECTED_ENDPOINT
            + " or the specified parameter did not exist");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("NonWrapperStyleTest failed", e);
    }

    if (!pass)
      throw new Fault("NonWrapperStyleTest failed");
  }

}
