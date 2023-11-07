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

/*
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.functioninfo;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.tags.tck.SimpleTag;
import com.sun.ts.tests.jsp.common.util.JspFunctions;
import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

/**
 * Test client for FunctionInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
 */


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_functioninfo_web.war");
    archive.addClasses(FunctionInfoTEI.class,
            JspTestUtil.class,
            SimpleTag.class,
            JspFunctions.class,
            BaseTCKExtraInfo.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_functioninfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/functioninfo.tld", "functioninfo.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetFunctionClassTest.jsp")), "GetFunctionClassTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetFunctionSignatureTest.jsp")), "GetFunctionSignatureTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetNameTest.jsp")), "GetNameTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: functionInfoGetNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:315
   * 
   * @test_Strategy: Validate the container properly parses the function
   * information in the provided TLD and the method calls, in this case,
   * FunctionInfo.getName(), returns the expected value.
   */
  @Test
  public void functionInfoGetNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_functioninfo_web/GetNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionInfoGetFunctionClassTest
   * 
   * @assertion_ids: JSP:JAVADOC:316
   * 
   * @test_Strategy: Validate the container properly parses the function
   * information in the provided TLD and the method calls, in this case,
   * FunctionInfo.getFunctionClass(), returns the expected value.
   */
  @Test
  public void functionInfoGetFunctionClassTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_functioninfo_web/GetFunctionClassTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: functionInfoGetFunctionSignatureTest
   * 
   * @assertion_ids: JSP:JAVADOC:317
   * 
   * @test_Strategy: Validate the container properly parses the function
   * information in the provided TLD and the method calls, in this case,
   * FunctionInfo.getFunctionSignature(), returns the expected value.
   */
  @Test
  public void functionInfoGetFunctionSignatureTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_functioninfo_web/GetFunctionSignatureTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
