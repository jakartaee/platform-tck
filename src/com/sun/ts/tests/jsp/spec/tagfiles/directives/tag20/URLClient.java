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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.tag20;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  private static String CONTEXT_ROOT = "/jsp_tagfile_directives_tag20_web";

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

    setGeneralURI("/jsp/spec/tagfiles/directives/tag20");
    setContextRoot("/jsp_tagfile_directives_tag20_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: negativeDuplicateIsELIgnoredFatalTranslationErrorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Declare a tag directive with two isELIgnored attributes.
   */

  public void negativeDuplicateIsELIgnoredFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateIsELIgnoredFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveImportTest
   * 
   * @assertion_ids: JSP:SPEC:229.19
   * 
   * @test_Strategy: Use the import attribute to import 'java.util.ArrayList'.
   * Validated that a ArrayList object can be created and used.
   */

  public void positiveImportTest() throws Fault {
    String testName = "positiveImport";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportLangTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the java.lang package are
   * implicitly imported by creating and using a java.lang.Integer object.
   */

  public void implicitImportLangTest() throws Fault {
    String testName = "implicitImportLang";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportJspTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the javax.servlet.jsp package
   * are implicitly imported by calling JspFactory.getDefaultFactory() method.
   */

  public void implicitImportJspTest() throws Fault {
    String testName = "implicitImportJsp";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportServletTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the javax.servlet package are
   * implicitly imported by creating and using an instance of RequestDispatcher.
   */

  public void implicitImportServletTest() throws Fault {
    String testName = "implicitImportServlet";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitImportHttpTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.1
   * 
   * @test_Strategy: Validate that classes from the javax.servlet.http package
   * are implicitly imported by creating and using an instance of HttpUtils.
   */

  public void implicitImportHttpTest() throws Fault {
    String testName = "implicitImportHttp";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveMultipleImportTest
   * 
   * @assertion_ids: JSP:SPEC:229.19.2
   * 
   * @test_Strategy: Declare a tag directive with two import attributes.
   *
   */

  public void positiveMultipleImportTest() throws Fault {
    String testName = "positiveMultipleImport";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeMultiplePageEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:232.1.21
   * 
   * @test_Strategy: Declare a tag directive with two pageEncoding attributes.
   *
   */

  public void negativeMultiplePageEncodingTest() throws Fault {
    String testName = "negativeMultiplePageEncoding";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveLangTest
   * 
   * @assertion_ids: JSP:SPEC:229.17
   * 
   * @test_Strategy: Validate that the language attribute can be set to "java"
   * without an error.
   */

  public void positiveLangTest() throws Fault {
    String testName = "positiveLang";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeDuplicateLanguageFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:229.18
   * 
   * @test_Strategy: Declare a tag directive with two language attributes. of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateLanguageFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateLanguageFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeBodyContentTest
   * 
   * @assertion_ids: JSP:SPEC:232.1.5.5
   * 
   * @test_Strategy: A translation error will result if JSP is used as tag
   * directive body-content
   */

  public void negativeBodyContentTest() throws Fault {
    String testName = "negativeBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveDuplicateAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: There shall be only one occurrence of any attribute /value
   * defined in a given translation unit, unless the values for the duplicate
   * attributes are identical for all occurences.
   */

  public void positiveDuplicateAttributesTest() throws Fault {
    String testName = "positiveDuplicateAttributes";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "tag invoked|Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeUnrecognizedAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Unrecognized attributes or values result in fatal
   * translation error.
   */

  public void negativeUnrecognizedAttributeTest() throws Fault {
    String testName = "negativeUnrecognizedAttribute";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDisplayNameTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateDisplayNameTest() throws Fault {
    String testName = "negativeDuplicateDisplayName";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateBodyContentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateBodyContentTest() throws Fault {
    String testName = "negativeDuplicateBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDynamicAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateDynamicAttributesTest() throws Fault {
    String testName = "negativeDuplicateDynamicAttributes";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateDescriptionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateDescriptionTest() throws Fault {
    String testName = "negativeDuplicateDescription";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateExampleTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateExampleTest() throws Fault {
    String testName = "negativeDuplicateExample";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateSmallIconTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateSmallIconTest() throws Fault {
    String testName = "negativeDuplicateSmallIcon";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateLargeIconTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: other such multiple attribute/value (re) definitions result
   * in a fatal translation error if the value do not match.
   */

  public void negativeDuplicateLargeIconTest() throws Fault {
    String testName = "negativeDuplicateLargeIcon";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: dynamicAttributesNoUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Only dynamic attributes with no uri are to be present in
   * the Map; all other are ignored.
   */

  public void dynamicAttributesNoUriTest() throws Fault {
    String testName = "dynamicAttributesNoUri";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: deferredValueMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.3
   * 
   * @test_Strategy: [deferredValueMinimumJspVersion] Show that the use of the
   * deferredValue attribute for the tag directive causes a translation error if
   * specified in a tag file with a JSP version less than 2.1.
   */

  public void deferredValueMinimumJspVersionTest() throws Fault {
    String testName = "deferredValueMinimumJspVersion";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
