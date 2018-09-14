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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.variable;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static final String CONTEXT_ROOT = "/jsp_tagfiles_directives_variable_web";

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
   * @testName: negativeNameGivenBothTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Either the name-given attribute or the name- from-attribute
   * must be specified. Specifying neither or both will result in a translation
   * error.
   */

  public void negativeNameGivenBothTest() throws Fault {
    String testName = "negativeNameGivenBoth";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenNeitherTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Either the name-given attribute or the name- from-attribute
   * must be specified. Specifying neither or both will result in a translation
   * error.
   */

  public void negativeNameGivenNeitherTest() throws Fault {
    String testName = "negativeNameGivenNeither";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-given.
   */

  public void negativeNameGivenSameTest() throws Fault {
    String testName = "negativeNameGivenSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-given in the same translation unit.
   */

  public void negativeNameGivenSameIncludeTest() throws Fault {
    String testName = "negativeNameGivenSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if a variable name-given is
   * the same as a dynamic-attributes of a tag directive.
   */

  public void negativeNameGivenDynamicTest() throws Fault {
    String testName = "negativeNameGivenDynamic";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameGivenDynamicIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if a variable name-given is
   * the same as a dynamic-attributes of a tag directive in the same translation
   * unit.
   */

  public void negativeNameGivenDynamicIncludeTest() throws Fault {
    String testName = "negativeNameGivenDynamicInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-from-attribute.
   */

  public void negativeNameFromAttributeSameTest() throws Fault {
    String testName = "negativeNameFromAttributeSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if two variable directives
   * have the same name-from-attribute.
   */

  public void negativeNameFromAttributeSameIncludeTest() throws Fault {
    String testName = "negativeNameFromAttributeSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNoAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result the attribute referred to
   * by name-from-attribute does not exist.
   */

  public void negativeNameFromAttributeNoAttributeTest() throws Fault {
    String testName = "negativeNameFromAttributeNoAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNotStringTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is not of type java.lang.String
   */

  public void negativeNameFromAttributeNotStringTest() throws Fault {
    String testName = "negativeNameFromAttributeNotString";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeNotRequiredTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is not required.
   */

  public void negativeNameFromAttributeNotRequiredTest() throws Fault {
    String testName = "negativeNameFromAttributeNotRequired";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeNameFromAttributeRtexprTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if the attribute referred
   * to by name-from-attribute is rtexprvalue
   */

  public void negativeNameFromAttributeRtexprTest() throws Fault {
    String testName = "negativeNameFromAttributeRtexpr";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasNameGivenTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is used without
   * name-from-attribute
   */

  public void negativeAliasNameGivenTest() throws Fault {
    String testName = "negativeAliasNameGiven";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasAttributeSameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is the same as the
   * name of an attribute directive in the same translation unit.
   */

  public void negativeAliasAttributeSameTest() throws Fault {
    String testName = "negativeAliasAttributeSame";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeAliasAttributeSameIncludeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error will result if alias is the same as the
   * name of an attribute directive in the same translation unit.
   */

  public void negativeAliasAttributeSameIncludeTest() throws Fault {
    String testName = "negativeAliasAttributeSameInclude";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveVariableClassTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable-class specifies the name of the class of the
   * variable
   */

  public void positiveVariableClassTest() throws Fault {
    String testName = "positiveVariableClass";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: syncAtBeginTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncAtBeginTest() throws Fault {
    String testName = "syncAtBegin";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|2|2|4");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncAtBeginNameFromAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncAtBeginNameFromAttributeTest() throws Fault {
    String testName = "syncAtBeginNameFromAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "tagFileResult|callingPageResult|ignoredInCallingPage|tagFileResult|4|callingPageResult");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncNestedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncNestedTest() throws Fault {
    String testName = "syncNested";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|2|2|1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncAtEndTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncAtEndTest() throws Fault {
    String testName = "syncAtEnd";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "1|1|2|4");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveNestedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncRemoveNestedTest() throws Fault {
    String testName = "syncRemoveNested";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'1'|''|2");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveAtEndTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncRemoveAtEndTest() throws Fault {
    String testName = "syncRemoveAtEnd";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'2'|'2'|''");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: syncRemoveAtBeginTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: variable synchronization
   */

  public void syncRemoveAtBeginTest() throws Fault {
    String testName = "syncRemoveAtBegin";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "2|'1'|''|''");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveDeclareTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: if true (default), a corresponding scripting variable is
   * declared in the calling page or tag file.
   */

  public void positiveDeclareTest() throws Fault {
    String testName = "positiveDeclare";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "declared2:declared2|Test PASSED");
    invoke();
  }

  /*
   * @testName: falseDeclareTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: if false, a corresponding scripting variable is not
   * declared in the calling page or tag file.
   */

  public void falseDeclareTest() throws Fault {
    String testName = "falseDeclareTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "was declared2|was declared");
    invoke();
  }

}
