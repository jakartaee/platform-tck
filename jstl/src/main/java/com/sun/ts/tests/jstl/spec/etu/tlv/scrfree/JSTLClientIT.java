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


package com.sun.ts.tests.jstl.spec.etu.tlv.scrfree;

import java.io.IOException;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_etu_tlv_scrfree_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_etu_tlv_scrfree_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_etu_tlv_scrfree_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeScriptFreeTlvNoDeclTest.jsp")), "negativeScriptFreeTlvNoDeclTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeScriptFreeTlvNoExprTest.jsp")), "negativeScriptFreeTlvNoExprTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeScriptFreeTlvNoRTExprTest.jsp")), "negativeScriptFreeTlvNoRTExprTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeScriptFreeTlvNoScrTest.jsp")), "negativeScriptFreeTlvNoScrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveScriptFreeTlvNoDeclTest.jsp")), "positiveScriptFreeTlvNoDeclTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveScriptFreeTlvNoExprTest.jsp")), "positiveScriptFreeTlvNoExprTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveScriptFreeTlvNoRTExprTest.jsp")), "positiveScriptFreeTlvNoRTExprTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveScriptFreeTlvNoScrTest.jsp")), "positiveScriptFreeTlvNoScrTest.jsp");
    
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.1
   * 
   * @testStrategy: Validate that if the validator specifies JSP declarations
   * aren't allowed, that scriptlets, expressions and RT expressions still work
   * as expected.
   */
  @Test
  public void positiveScriptFreeTlvNoDeclTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoScrTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.2
   * 
   * @testStrategy: Validate that if the validator specifies JSP scriptlets
   * aren't allowed, that declarations, expressions and RT expressions still
   * work as expected.
   */
  @Test
  public void positiveScriptFreeTlvNoScrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoScrTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoScrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if the validator specifies JSP expressions
   * aren't allowed, that scriptlets, declarations and RT expressions still work
   * as expected.
   */
  @Test
  public void positiveScriptFreeTlvNoExprTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoExprTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveScriptFreeTlvNoRTExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if the validator specifies JSP RT expressions
   * aren't allowed, that declarations, scriptlets, and expressions still work
   * as expected.
   */
  @Test
  public void positiveScriptFreeTlvNoRTExprTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveScriptFreeTlvNoRTExprTest");
    TEST_PROPS.setProperty(REQUEST, "positiveScriptFreeTlvNoRTExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.1
   * 
   * @testStrategy: Validate that if declarations aren't allowed per the
   * configured validator, that a translation error occurs if a declaration
   * exists.
   */
  @Test
  public void negativeScriptFreeTlvNoDeclTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoScrTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.2
   * 
   * @testStrategy: Validate that if scriptlets aren't allowed per the
   * configured validator, that a translation error occurs if a scriptlet
   * exists.
   */
  @Test
  public void negativeScriptFreeTlvNoScrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoScrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoScrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if expressions aren't allowed per the
   * configured validator, that a translation error occurs if an expression
   * exists.
   */
  @Test
  public void negativeScriptFreeTlvNoExprTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoExprTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoRTExprTest
   * 
   * @assertion_ids: JSTL:SPEC:104; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if RT expressions aren't allowed per the
   * configured validator, that a translation error occurs if an RT expression
   * exists.
   */
  @Test
  public void negativeScriptFreeTlvNoRTExprTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoRTExprTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoRTExprTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
