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



package com.sun.ts.tests.jstl.spec.xml.xconditional.xcwo;

import java.io.IOException;
import java.io.InputStream;
import java.io.IOException;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@Tag("jstl")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_xml_xcwo_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xcwo_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xcwo_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWONoWhenActionsTest.jsp")), "negativeCWONoWhenActionsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOOtherwiseNoParentTest.jsp")), "negativeCWOOtherwiseNoParentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenBeforeOtherwiseTest.jsp")), "negativeCWOWhenBeforeOtherwiseTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenNoParentTest.jsp")), "negativeCWOWhenNoParentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenSelectFailureTest.jsp")), "negativeCWOWhenSelectFailureTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeCWOWhenSelectReqAttrTest.jsp")), "negativeCWOWhenSelectReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveCWOTest.jsp")), "positiveCWOTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveCWOWhiteSpaceTest.jsp")), "positiveCWOWhiteSpaceTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveCWOTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.3; JSTL:SPEC:71.2
   * 
   * @testStrategy: Validate the following: - The first c:when action that
   * evaluates to true will process it's body content. Subsequent when action
   * that evaluate to true will not be processed. - Validate that if a when
   * action evaluates to true, it's body content is processed and that the body
   * content of an c:otherwise action is not. - Validate that if no c:when
   * action evaluates to true, and no c:otherwise action is present, nothing is
   * written to the current JspWriter. - Validate that if no c:when action
   * evaluates to true, and an c:otherwise action is present, the body content
   * of the action is processed.
   */
  @Test
  public void positiveCWOTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveCWOTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveCWOTest");
    invoke();
  }

  /*
   * @testName: positiveCWOWhiteSpaceTest
   * 
   * @assertion_ids: JSTL:SPEC:71.5; JSTL:SPEC:71.5.5; JSTL:SPEC:71.5.4
   * 
   * @testStrategy: Validate that the CWO action behaves as expected if
   * whitespace is intermixed with choose's nested when and otherwise actions.
   * No translation error should occur.
   */
  @Test
  public void positiveCWOWhiteSpaceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveCWOWhiteSpaceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveCWOWhiteSpaceTest");
    invoke();
  }

  /*
   * @testName: negativeCWONoWhenActionsTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.6.4
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
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.7
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
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
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
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
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
   * @testName: negativeCWOWhenSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate that a fatal translation error occurs if a nested
   * when action has no select attribute.
   */
  @Test
  public void negativeCWOWhenSelectReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeCWOWhenSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeCWOWhenSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeCWOWhenSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:71; JSTL:SPEC:71.5.6
   * 
   * @testStrategy: Validate that if the XPath expression provided to select of
   * the when action fails to evaluated an instance of
   * jakarta.servlet.jsp.JspException is thrown.
   */
  @Test
  public void negativeCWOWhenSelectFailureTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeCWOWhenSelectFailureTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeCWOWhenSelectFailureTest");
    invoke();
  }
}
