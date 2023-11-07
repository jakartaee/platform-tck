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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.el.elparseexception;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.tags.tck.SetTag;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;

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




  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_elparseexc_web");
    setTestJsp("ELParseExceptionTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_elparseexc_web.war");
    archive.addClasses(
            JspTestUtil.class,
            SetTag.class,
            ExpressionTest.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_elparseexc_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELParseExceptionTest.jsp")), "ELParseExceptionTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: elParseExceptionDefaultCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:157
   * 
   * @test_Strategy: Validate default constructor of ELParseException
   */
  @Test
  public void elParseExceptionDefaultCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elParseExceptionDefaultCtorTest");
    invoke();
  }

  /*
   * @testName: elParseExceptionMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:158
   * 
   * @test_Strategy: Validate contructor taking single string argument as the
   * message of the Exception.
   */
  @Test
  public void elParseExceptionMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elParseExceptionMessageCtorTest");
    invoke();
  }
}
