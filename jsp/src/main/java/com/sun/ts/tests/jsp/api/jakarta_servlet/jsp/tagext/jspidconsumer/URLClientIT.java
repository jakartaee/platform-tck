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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.jspidconsumer;


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
    setContextRoot("/jsp_jspidconsumer_web");
    // setTestJsp("SetJspIdTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspidconsumer_web.war");
    archive.addClasses(MultiOneTag.class, MultiTwoTag.class,
            MultiThreeTag.class, SameJspIdTag.class, SetJspIdTag.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspidconsumer_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/jspidconsumer.tld", "jspidconsumer.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SetJspIdTest.jsp")), "SetJspIdTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/MultipleJspIdTest.jsp")), "MultipleJspIdTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SameJspIdTest.jsp")), "SameJspIdTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: setJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in a tag handler. Verify that the ID generated
   * conforms to the rules set forth in the javadoc.
   */
  @Test
  public void setJspIdTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/SetJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: multipleJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in multiple tag handlers. Verify that each tag has a
   * unique ID. [JspConsumerIdUniqueIdString]
   */
  @Test
  public void multipleJspIdTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/MultipleJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: sameJspIdTest
   * 
   * @assertion_ids: JSP:JAVADOC:434
   * 
   * @test_Strategy: Validate the behavior of JspIdConsumer.setJspId() Implement
   * the setJspId() method in a single tag handler. Verify that when the jsp
   * page is invoked multiple times, the tag's ID does not change.
   * [JspConsumerIdUniqueIdString]
   */
  @Test
  public void sameJspIdTest() throws Exception {

    for (int i = 1; i < SameJspIdTag.NUM_INVOC; ++i) {
      TEST_PROPS.setProperty(REQUEST,
          "GET /jsp_jspidconsumer_web/SameJspIdTest.jsp HTTP 1.1");
      TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
      TEST_PROPS.setProperty(IGNORE_BODY, "true");
      invoke();
    }
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspidconsumer_web/SameJspIdTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "false");
    TEST_PROPS.setProperty(IGNORE_BODY, "false");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
