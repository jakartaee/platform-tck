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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.el.expressionevaluator;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }



  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_expreval_web");
    setTestJsp("ExpressionEvaluatorTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_expreval_web.war");
    archive.addClasses(
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.util.TSFunctionMapper.class,
            com.sun.ts.tests.jsp.common.util.JspFunctions.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_expreval_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ExpressionEvaluatorTest.jsp")), "ExpressionEvaluatorTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: expressionEvaluatorParseExpressionTest
   * 
   * @assertion_ids: JSP:JAVADOC:168
   * 
   * @test_Strategy: Validate the following: - An expression can be prepared
   * using a FunctionMapper. - An expression can be prepared passing a null
   * reference for the FunctionMapper - If the expression uses a function an no
   * prefix is provided, the default prefix will be used.
   */
  @Test
  public void expressionEvaluatorParseExpressionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "expressionEvaluatorParseExpressionTest");
    invoke();
  }

  /*
   * @testName: expressionEvaluatorEvaluateTest
   * 
   * @assertion_ids: JSP:JAVADOC:171;JSP:JAVADOC:165
   * 
   * @test_Strategy: Validate the following: - Evaluation can occur using a
   * FunctionMapper. - Evaluation can occur when a null reference passed as the
   * FunctionMapper - If the expression uses a function an no prefix is
   * provided, the default prefix will be used. - When the FunctionMapper is
   * used, the resolveFunction method must be called. - Validate the the
   * provided VariableResolver is used. - Validate the result of the
   * expressions.
   */
  @Test
  public void expressionEvaluatorEvaluateTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "expressionEvaluatorEvaluateTest");
    invoke();
  }
}
