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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.jspwriter;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  private static final char[] CHARS = new char[89];

  private static final String EOL = "#eol#";

  private static final String JSP_WRITER_VALIDATOR = "com.sun.ts.tests.jsp.api.javax_servlet.jsp.jspwriter.JspWriterValidator";

  public URLClient() {
    for (short i = 33, idx = 0; i < 90; i++, idx++) {
      CHARS[idx] = (char) i;
    }
  }

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

    setContextRoot("/jsp_jspwriter_web");
    setTestJsp("JspWriterTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: jspWriterNewLineTest
   * 
   * @assertion_ids: JSP:JAVADOC:63
   * 
   * @test_Strategy: Validate the behavior of JspWriter.newLine().
   */
  public void jspWriterNewLineTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterNewLineTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, "new line" + EOL);
    invoke();
  }

  /*
   * @testName: jspWriterPrintBooleanTest
   * 
   * @assertion_ids: JSP:JAVADOC:65
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(boolean).
   */
  public void jspWriterPrintBooleanTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintBooleanTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "truefalse");
    invoke();
  }

  /*
   * @testName: jspWriterPrintCharTest
   * 
   * @assertion_ids: JSP:JAVADOC:67
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(char).
   */
  public void jspWriterPrintCharTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintCharTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "cb");
    invoke();
  }

  /*
   * @testName: jspWriterPrintIntTest
   * 
   * @assertion_ids: JSP:JAVADOC:69
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(int).
   */
  public void jspWriterPrintIntTest() throws Fault {
    String result = new StringBuffer().append(Integer.MIN_VALUE)
        .append(Integer.MAX_VALUE).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintIntTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintLongTest
   * 
   * @assertion_ids: JSP:JAVADOC:71
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(long).
   */
  public void jspWriterPrintLongTest() throws Fault {
    String result = new StringBuffer().append(Long.MIN_VALUE)
        .append(Long.MAX_VALUE).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintLongTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintFloatTest
   * 
   * @assertion_ids: JSP:JAVADOC:73
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(Float).
   */
  public void jspWriterPrintFloatTest() throws Fault {
    String result = new StringBuffer().append(Float.MIN_VALUE)
        .append(Float.MAX_VALUE).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintFloatTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintDoubleTest
   * 
   * @assertion_ids: JSP:JAVADOC:75
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(double).
   */
  public void jspWriterPrintDoubleTest() throws Fault {
    String result = new StringBuffer().append(Double.MIN_VALUE)
        .append(Double.MAX_VALUE).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintDoubleTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintCharArrayTest
   * 
   * @assertion_ids: JSP:JAVADOC:77
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(char[]).
   */
  public void jspWriterPrintCharArrayTest() throws Fault {
    String result = new StringBuffer().append(CHARS).append(CHARS).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintCharArrayTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:80
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(String).
   */
  public void jspWriterPrintStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintStringTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test Passed");
    invoke();
  }

  /*
   * @testName: jspWriterPrintNullStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:80
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(String) where the
   * argument is null.
   */
  public void jspWriterPrintNullStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintNullStringTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "null");
    invoke();
  }

  /*
   * @testName: jspWriterPrintObjectTest
   * 
   * @assertion_ids: JSP:JAVADOC:82
   * 
   * @test_Strategy: Validate the behavior of JspWriter.print(Object).
   */
  public void jspWriterPrintObjectTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintObjectTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test Passed");
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnTest
   * 
   * @assertion_ids: JSP:JAVADOC:84
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println().
   */
  public void jspWriterPrintlnTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test Passed" + EOL);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnBooleanTest
   * 
   * @assertion_ids: JSP:JAVADOC:86
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(boolean).
   */
  public void jspWriterPrintlnBooleanTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnBooleanTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, "true" + EOL + "false" + EOL);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnCharTest
   * 
   * @assertion_ids: JSP:JAVADOC:88
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(char).
   */
  public void jspWriterPrintlnCharTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnCharTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, "a" + EOL + "B" + EOL);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnIntTest
   * 
   * @assertion_ids: JSP:JAVADOC:90
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(int).
   */
  public void jspWriterPrintlnIntTest() throws Fault {
    String result = new StringBuffer().append(Integer.MIN_VALUE).append(EOL)
        .append(Integer.MAX_VALUE).append(EOL).toString();
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnIntTest");
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnLongTest
   * 
   * @assertion_ids: JSP:JAVADOC:92
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(long).
   */
  public void jspWriterPrintlnLongTest() throws Fault {
    String result = new StringBuffer().append(Long.MIN_VALUE).append(EOL)
        .append(Long.MAX_VALUE).append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnLongTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnFloatTest
   * 
   * @assertion_ids: JSP:JAVADOC:94
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(float).
   */
  public void jspWriterPrintlnFloatTest() throws Fault {
    String result = new StringBuffer().append(Float.MIN_VALUE).append(EOL)
        .append(Float.MAX_VALUE).append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnFloatTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnDoubleTest
   * 
   * @assertion_ids: JSP:JAVADOC:96
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(double).
   */
  public void jspWriterPrintlnDoubleTest() throws Fault {
    String result = new StringBuffer().append(Double.MIN_VALUE).append(EOL)
        .append(Double.MAX_VALUE).append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnDoubleTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnCharArrayTest
   * 
   * @assertion_ids: JSP:JAVADOC:98
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(double).
   */
  public void jspWriterPrintlnCharArrayTest() throws Fault {
    String result = new StringBuffer().append(CHARS).append(EOL).append(CHARS)
        .append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnCharArrayTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:100
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(String).
   */
  public void jspWriterPrintlnStringTest() throws Fault {
    String result = new StringBuffer().append("Test ").append(EOL)
        .append("Passed").append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnStringTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnNullStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:100
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(String) where
   * the argument is null.
   */
  public void jspWriterPrintlnNullStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnNullStringTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "null");
    invoke();
  }

  /*
   * @testName: jspWriterPrintlnObjectTest
   * 
   * @assertion_ids: JSP:JAVADOC:102
   * 
   * @test_Strategy: Validate the behavior of JspWriter.println(Object).
   */
  public void jspWriterPrintlnObjectTest() throws Fault {
    String result = new StringBuffer().append("Test ").append(EOL)
        .append("Passed").append(EOL).toString();
    TEST_PROPS.setProperty(APITEST1, "jspWriterPrintlnObjectTest");
    TEST_PROPS.setProperty(STRATEGY, JSP_WRITER_VALIDATOR);
    TEST_PROPS.setProperty(SEARCH_STRING, result);
    invoke();
  }

  /*
   * @testName: jspWriterClearTest
   * 
   * @assertion_ids: JSP:JAVADOC:104
   * 
   * @test_Strategy: Validate the behavior of JspWriter.clear().
   */
  public void jspWriterClearTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterClearTest");
    invoke();
  }

  /*
   * @testName: jspWriterClearIOExceptionTest
   * 
   * @assertion_ids: JSP:JAVADOC:105
   * 
   * @test_Strategy: Validate the behavior of JspWriter.clear(). An IOException
   * must be thrown if the response has been committed, and JspWriter.clear() is
   * subsequently called.
   */
  public void jspWriterClearIOExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterClearIOExceptionTest");
    invoke();
  }

  /*
   * @testName: jspWriterClearBufferTest
   * 
   * @assertion_ids: JSP:JAVADOC:106
   * 
   * @test_Strategy: Validate the behavior of JspWriter.clearBuffer().
   */
  public void jspWriterClearBufferTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterClearBufferTest");
    invoke();
  }

  /*
   * @testName: jspWriterFlushTest
   * 
   * @assertion_ids: JSP:JAVADOC:108
   * 
   * @test_Strategy: Validate the behavior of JspWriter.flush().
   */
  public void jspWriterFlushTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterFlushTest");
    invoke();
  }

  /*
   * @testName: jspWriterCloseTest
   * 
   * @assertion_ids: JSP:JAVADOC:110;JSP:JAVADOC:109
   * 
   * @test_Strategy: Validate the behavior of JspWriter.close().
   */
  public void jspWriterCloseTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspwriter_web/JspWriterTest.jsp?testname=jspWriterCloseTest HTTP/1.1");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspwriter_web/CloseValidator.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspWriterGetBufferSizeTest
   * 
   * @assertion_ids: JSP:JAVADOC:112
   * 
   * @test_Strategy: Validate the behavior of JspWriter.getBufferSize().
   */
  public void jspWriterGetBufferSizeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterGetBufferSizeTest");
    invoke();
  }

  /*
   * @testName: jspWriterGetRemainingTest
   * 
   * @assertion_ids: JSP:JAVADOC:113
   * 
   * @test_Strategy: Validate the behavior of JspWriter.getRemaining().
   */
  public void jspWriterGetRemainingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterGetRemainingTest");
    invoke();
  }

  /*
   * @testName: jspWriterIsAutoFlushTest
   * 
   * @assertion_ids: JSP:JAVADOC:114
   * 
   * @test_Strategy: Validate the behavior of JspWriter.isAutoFlush().
   */
  public void jspWriterIsAutoFlushTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "jspWriterIsAutoFlushTest");
    invoke();
  }
}
