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


package com.sun.ts.tests.jstl.spec.core.conditional.cwo;

import java.io.IOException;
import java.io.InputStream;

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

  public JSTLClientIT() throws Exception {
    setContextRoot("/jstl_core_cond_cwo_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_cond_cwo_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_cond_cwo_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWONoWhenActionsTest.jsp")), "negativeCWONoWhenActionsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOOtherwiseExcBodyContentTest.jsp")), "negativeCWOOtherwiseExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOOtherwiseNoParentTest.jsp")), "negativeCWOOtherwiseNoParentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenBeforeOtherwiseTest.jsp")), "negativeCWOWhenBeforeOtherwiseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenExcBodyContentTest.jsp")), "negativeCWOWhenExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenNoParentTest.jsp")), "negativeCWOWhenNoParentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenTestReqAttrTest.jsp")), "negativeCWOWhenTestReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenTypeTest.jsp")), "negativeCWOWhenTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveCWOTest.jsp")), "positiveCWOTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveCWOTest
   * 
   * @assertion_ids: JSTL:SPEC:15.1.1; JSTL:SPEC:15.1.1.1; JSTL:SPEC:15.1.1.2;
   * JSTL:SPEC:15.2.1.1; JSTL:SPEC:15.2.1
   * 
   * @testStrategy: Validate the behavior/interaction of 'choose', 'when'
   * 'otherwise' actions. - single 'when' action evaluating to true - one 'when'
   * action evaluating to false and two 'when' actions evaluating to true. Only
   * the first when that evaluates to true should process it's body content -
   * single 'when' evaluating to false which should cause the 'otherwise' action
   * to process its body
   */
  @Test
  public void positiveCWOTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveCWOTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveCWOTest");
    invoke();
  }

  /*
   * @testName: negativeCWOWhenTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:15.1.1.2
   * 
   * @testStrategy: Validate that the tag throws an instance of
   * jakarta.servlet.jsp.JspTagException if the expression evaluates to a type not
   * expected by the tag.
   */
  @Test
  public void negativeCWOWhenTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeCWOWhenTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeCWOWhenTypeTest");
    invoke();
  }

  /*
   * @testName: negativeCWOWhenExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:15.2.1.6
   * 
   * @testStrategy: Validate that an exception caused by the body content of a
   * when action is properly propagated to and by the choose action.
   */
  @Test
  public void negativeCWOWhenExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeCWOWhenExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeCWOWhenExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeCWOOtherwiseExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:15.3.4
   * 
   * @testStrategy: Validate that an exception caused by the body content of an
   * otherwise action is properly propagaged to and by the choose action.
   */
  @Test
  public void negativeCWOOtherwiseExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeCWOOtherwiseExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeCWOOtherwiseExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeCWONoWhenActionsTest
   * 
   * @assertion_ids: JSTL:SPEC:15.1.1.2
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the choose
   * action has no nested when actions.
   */
  @Test
  public void negativeCWONoWhenActionsTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWONoWhenActionsTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWONoWhenActionsTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenBeforeOtherwiseTest
   * 
   * @assertion_ids: JSTL:SPEC:15.2.1.5
   * 
   * @testStrategy: Validate that a fatal translation error occurs if otherwise
   * appears before when or if when appears after otherwise.
   */
  @Test
  public void negativeCWOWhenBeforeOtherwiseTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenBeforeOtherwiseTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenBeforeOtherwiseTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenNoParentTest
   * 
   * @assertion_ids: JSTL:SPEC:15.2.1.4
   * 
   * @testStrategy: Validate a fatal translation error occurs if a when action
   * has no choose as an immediate parent.
   */
  @Test
  public void negativeCWOWhenNoParentTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenNoParentTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenNoParentTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOOtherwiseNoParentTest
   * 
   * @assertion_ids: JSTL:SPEC:15.3.2
   * 
   * @testStrategy: Validate a fatal translation error occurs if an otherwise
   * action has no choose as an immediate parent.
   */
  @Test
  public void negativeCWOOtherwiseNoParentTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOOtherwiseNoParentTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOOtherwiseNoParentTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenTestReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:15.2.1.3
   * 
   * @testStrategy: Validate that a fatal translation error occurs if a nested
   * when action has no 'test' attribute.
   */
  @Test
  public void negativeCWOWhenTestReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenTestReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenTestReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
