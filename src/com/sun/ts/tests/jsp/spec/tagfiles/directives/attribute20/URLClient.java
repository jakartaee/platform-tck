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
 * @(#)URLClient.java	1.1 03/09/16
 */

package com.sun.ts.tests.jsp.spec.tagfiles.directives.attribute20;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_attribute20_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativeName1Test
   * 
   * @assertion_ids: JSP:SPEC:230.2
   * 
   * @test_Strategy: A translation error must result if more than one attribute
   * directive appears in the same translation unit with the same name
   */

  public void negativeName1Test() throws Fault {
    String testName = "negativeName1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName1IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.2
   * 
   * @test_Strategy: A translation error must result if more than one attribute
   * directive appears in the same translation unit with the same name
   */

  public void negativeName1IncludeTest() throws Fault {
    String testName = "negativeName1Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName2Test
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to the name-given of a variable.
   */

  public void negativeName2Test() throws Fault {
    String testName = "negativeName2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName2IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to the name-given of a variable.
   */

  public void negativeName2IncludeTest() throws Fault {
    String testName = "negativeName2Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName3Test
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to dynamic-attributes of a tag directive.
   */

  public void negativeName3Test() throws Fault {
    String testName = "negativeName3";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeName3IncludeTest
   * 
   * @assertion_ids: JSP:SPEC:230.1.2
   * 
   * @test_Strategy: A translation error must result if the attribute name
   * equals to dynamic-attributes of a tag directive.
   */

  public void negativeName3IncludeTest() throws Fault {
    String testName = "negativeName3Include";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultRequiredTest
   * 
   * @assertion_ids: JSP:SPEC:230.3.3
   * 
   * @test_Strategy: required default is false
   */

  public void defaultRequiredTest() throws Fault {
    String testName = "defaultRequired";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.6.1
   * 
   * @test_Strategy: type defaults to java.lang.String, and also verify an Float
   * type attribute.
   */

  public void defaultTypeTest() throws Fault {
    String testName = "defaultType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.6.2
   * 
   * @test_Strategy: A translation error must result if the type is a primitive
   */

  public void negativeTypeTest() throws Fault {
    String testName = "negativeType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeFragmentTypeTest
   * 
   * @assertion_ids: JSP:SPEC:230.4.5
   * 
   * @test_Strategy: A translation error must result if fragment is true and
   * type is specified
   */

  public void negativeFragmentTypeTest() throws Fault {
    String testName = "negativeFragmentType";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeFragmentRtexprvalueTest
   * 
   * @assertion_ids: JSP:SPEC:230.4.2.1
   * 
   * @test_Strategy: A translation error must result if fragment is true and
   * rtexprvalue is specified
   */

  public void negativeFragmentRtexprvalueTest() throws Fault {
    String testName = "negativeFragmentRtexprvalue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultFragmentTest
   * 
   * @assertion_ids: JSP:SPEC:230
   * 
   * @test_Strategy: fragment defaults to false. Also verifies the default attr
   * type is String.
   */

  public void defaultFragmentTest() throws Fault {
    String testName = "defaultFragment";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultRtexprvalueTest
   * 
   * @assertion_ids: JSP:SPEC:230
   * 
   * @test_Strategy: rtexprvalue defaults to true.
   */

  public void defaultRtexprvalueTest() throws Fault {
    String testName = "defaultRtexprvalue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: deferredValueMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.3
   * 
   * @test_Strategy: [deferredValueMinimumJspVersion] The deferredValue
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  public void deferredValueMinimumJspVersionTest() throws Fault {
    String testName = "deferredValueMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredValueTypeMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.4
   * 
   * @test_Strategy: [deferredValueTypeMinimumJspVersion] The deferredValueType
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  public void deferredValueTypeMinimumJspVersionTest() throws Fault {
    String testName = "deferredValueTypeMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredMethodMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.10.2
   * 
   * @test_Strategy: [deferredMethodMinimumJspVersion] The deferredMethod
   * attribute causes a translation error if specified in a tag file with a JSP
   * version less than 2.1.
   */

  public void deferredMethodMinimumJspVersionTest() throws Fault {
    String testName = "deferredMethodMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredMethodSignatureMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.3
   * 
   * @test_Strategy: [deferredMethodSignatureMinimumJspVersion] The
   * deferredMethodSignature attribute causes a translation error if specified
   * in a tag file with a JSP version less than 2.1.
   */

  public void deferredMethodSignatureMinimumJspVersionTest() throws Fault {
    String testName = "deferredMethodSignatureMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
