/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.jspwriter;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil; 

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  private static final char[] CHARS = new char[89];

  private static final String EOL = "#eol#";

  private static final String JSP_WRITER_VALIDATOR = "com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.jspwriter.JspWriterValidator";

  public URLClientIT() throws Exception {
    for (short i = 33, idx = 0; i < 90; i++, idx++) {
      CHARS[idx] = (char) i;
    }

    setContextRoot("/jsp_jspwriter_web");
    setTestJsp("JspWriterTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspwriter_web.war");
    archive.addClasses(JspWriterValidator.class, JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspwriter_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspWriterTest.jsp")), "JspWriterTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/CloseValidator.jsp")), "CloseValidator.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: jspWriterNewLineTest
   * 
   * @assertion_ids: JSP:JAVADOC:63
   * 
   * @test_Strategy: Validate the behavior of JspWriter.newLine().
   */
  @Test
  public void jspWriterNewLineTest() throws Exception {
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
  @Test
  public void jspWriterPrintBooleanTest() throws Exception {
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
  @Test
  public void jspWriterPrintCharTest() throws Exception {
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
  @Test
  public void jspWriterPrintIntTest() throws Exception {
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
  @Test
  public void jspWriterPrintLongTest() throws Exception {
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
  @Test
  public void jspWriterPrintFloatTest() throws Exception {
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
  @Test
  public void jspWriterPrintDoubleTest() throws Exception {
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
  @Test
  public void jspWriterPrintCharArrayTest() throws Exception {
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
  @Test
  public void jspWriterPrintStringTest() throws Exception {
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
  @Test
  public void jspWriterPrintNullStringTest() throws Exception {
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
  @Test
  public void jspWriterPrintObjectTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnBooleanTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnCharTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnIntTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnLongTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnFloatTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnDoubleTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnCharArrayTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnStringTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnNullStringTest() throws Exception {
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
  @Test
  public void jspWriterPrintlnObjectTest() throws Exception {
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
  @Test
  public void jspWriterClearTest() throws Exception {
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
  @Test
  public void jspWriterClearIOExceptionTest() throws Exception {
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
  @Test
  public void jspWriterClearBufferTest() throws Exception {
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
  @Test
  public void jspWriterFlushTest() throws Exception {
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
  @Test
  public void jspWriterCloseTest() throws Exception {
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
  @Test
  public void jspWriterGetBufferSizeTest() throws Exception {
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
  @Test
  public void jspWriterGetRemainingTest() throws Exception {
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
  @Test
  public void jspWriterIsAutoFlushTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspWriterIsAutoFlushTest");
    invoke();
  }
}
