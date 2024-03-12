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



package com.sun.ts.tests.jstl.spec.core.iteration.fortokens;

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

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_core_iter_fortok_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_iter_fortok_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_iter_fortok_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFTBeginTypeTest.jsp")), "negativeFTBeginTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFTEndTypeTest.jsp")), "negativeFTEndTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFTExcBodyContentTest.jsp")), "negativeFTExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFTStepTypeTest.jsp")), "negativeFTStepTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBeginTest.jsp")), "positiveBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBodyBehaviorTest.jsp")), "positiveBodyBehaviorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDelimsNullTest.jsp")), "positiveDelimsNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveEndTest.jsp")), "positiveEndTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForTokensDeferredValueTest.jsp")), "positiveForTokensDeferredValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForTokensEndLTBeginTest.jsp")), "positiveForTokensEndLTBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForTokensTest.jsp")), "positiveForTokensTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsDelimsNullTest.jsp")), "positiveItemsDelimsNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsNullTest.jsp")), "positiveItemsNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveStepTest.jsp")), "positiveStepTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveVarStatusTest.jsp")), "positiveVarStatusTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveForTokensTest
   * 
   * @assertion_ids: JSTL:SPEC:22; JSTL:SPEC:22.1; JSTL:SPEC:22.2;
   * JSTL:SPEC:22.3
   * 
   * @testStrategy: Validate that forTokens can properly Iterate over a String
   * provided with specified delimiters.
   */
  @Test
  public void positiveForTokensTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForTokensTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForTokensTest");
    invoke();
  }

  /*
   * @testName: positiveVarStatusTest
   * 
   * @assertion_ids: JSTL:SPEC:22.4; JSTL:SPEC:22.4.1
   * 
   * @testStrategy: Validate that when varStatus is specified that the exported
   * var name is of type jakarta.servlet.jsp.jstl.LoopTagStatus
   */
  @Test
  public void positiveVarStatusTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveVarStatusTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveVarStatusTest");
    invoke();
  }

  /*
   * @testName: positiveBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:22.5
   * 
   * @testStrategy: Validate that 'begin' starts in the proper location of the
   * tokens created from the passed String.
   */
  @Test
  public void positiveBeginTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBeginTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveBeginTest");
    invoke();
  }

  /*
   * @testName: positiveEndTest
   * 
   * @assertion_ids: JSTL:SPEC:22.6
   * 
   * @testStrategy: Validate that when 'end' is specified, that the action stops
   * processing once it reaches the appropriate token in the list.
   */
  @Test
  public void positiveEndTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveEndTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveEndTest");
    invoke();
  }

  /*
   * @testName: positiveStepTest
   * 
   * @assertion_ids: JSTL:SPEC:22.7
   * 
   * @testStrategy: Validate that when 'step' is specified, the action only
   * processes every 'step' tokens.
   */
  @Test
  public void positiveStepTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveStepTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveStepTest");
    invoke();
  }

  /*
   * @testName: positiveBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:22.8
   * 
   * @testStrategy: Validate that the 'forEach' action can handle bodies
   * containing content as well as empty bodies.
   */
  @Test
  public void positiveBodyBehaviorTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBodyBehaviorTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveItemsNullTest
   * 
   * @assertion_ids: JSTL:SPEC:22.15
   * 
   * @testStrategy: Validate that if items is null, no iteration is performed.
   */
  @Test
  public void positiveItemsNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsNullTest");
    invoke();
  }

  /*
   * @testName: positiveDelimsNullTest
   * 
   * @assertion_ids: JSTL:SPEC:22.16
   * 
   * @testStrategy: Validate that if delims is null, items is treated as a
   * single token, i.e, only one iteration is performed.
   */
  @Test
  public void positiveDelimsNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDelimsNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveDelimsNullTest");
    invoke();
  }

  /*
   * @testName: positiveItemsDelimsNullTest
   * 
   * @assertion_ids: JSTL:SPEC:22.15
   * 
   * @testStrategy: Validate that if both delims and items is null, no iteration
   * is performed.
   */
  @Test
  public void positiveItemsDelimsNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsDelimsNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsDelimsNullTest");
    invoke();
  }

  /*
   * @testName: negativeFTBeginTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:22.5.3
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to begin evaluates to an incorrect type.
   */
  @Test
  public void negativeFTBeginTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFTBeginTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFTBeginTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFTEndTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:22.6.2
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to end evaluates to an incorrect type.
   */
  @Test
  public void negativeFTEndTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFTEndTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFTEndTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFTStepTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:22.7.2
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to step evaluates to an incorrect type.
   */
  @Test
  public void negativeFTStepTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFTStepTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFTStepTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFTExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:22.17
   * 
   * @testStrategy: Validate that an exception caused by the actions's body
   * content is properly propagated.
   */
  @Test
  public void negativeFTExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFTExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFTExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveForTokensEndLTBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:22.18
   * 
   * @test_Strategy: Validate an end attribute value that is less than the begin
   * attribute value will result in the action not being executed.
   */
  @Test
  public void positiveForTokensEndLTBeginTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_fortok_web/positiveForTokensEndLTBeginTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveForTokensDeferredValueTest
   * 
   * @assertion_ids: JSTL:SPEC:22.19
   * 
   * @test_Strategy: Create a String containing several tokens. In a c:forTokens
   * tag, reference the String as a deferred value in the items attribute. In
   * the body of the tag, set each item to have application scope. Verify that
   * the items can be retrieved after the execution of the tag.
   */
  @Test
  public void positiveForTokensDeferredValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForTokensDeferredValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForTokensDeferredValueTest");
    invoke();
  }
}
