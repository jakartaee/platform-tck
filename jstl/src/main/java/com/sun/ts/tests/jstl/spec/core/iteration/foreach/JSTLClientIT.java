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



package com.sun.ts.tests.jstl.spec.core.iteration.foreach;

import java.io.IOException;
import java.io.PrintWriter;

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
    setContextRoot("/jstl_core_iter_foreach_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_iter_foreach_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_iter_foreach_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveVarTest.jsp")), "positiveVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveVarStatusTest.jsp")), "positiveVarStatusTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveNoItemsIterationTest.jsp")), "positiveNoItemsIterationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsStepTest.jsp")), "positiveItemsStepTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsPrimArrayTest.jsp")), "positiveItemsPrimArrayTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsObjArrayTest.jsp")), "positiveItemsObjArrayTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsNullTest.jsp")), "positiveItemsNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsMapTest.jsp")), "positiveItemsMapTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsIteratorTest.jsp")), "positiveItemsIteratorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsEnumerationTest.jsp")), "positiveItemsEnumerationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsEndTest.jsp")), "positiveItemsEndTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsCommaSepStringTest.jsp")), "positiveItemsCommaSepStringTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsCollectionTest.jsp")), "positiveItemsCollectionTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsBeginTest.jsp")), "positiveItemsBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachEndLTBeginTest.jsp")), "positiveForEachEndLTBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachDeferredValueTest3.jsp")), "positiveForEachDeferredValueTest3.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachDeferredValueTest2.jsp")), "positiveForEachDeferredValueTest2.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachDeferredValueTest1.jsp")), "positiveForEachDeferredValueTest1.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBodyBehaviorTest.jsp")), "positiveBodyBehaviorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFEStepTypeTest.jsp")), "negativeFEStepTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFEItemsTypeTest.jsp")), "negativeFEItemsTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFEExcBodyContentTest.jsp")), "negativeFEExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFEEndTypeTest.jsp")), "negativeFEEndTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFEBeginTypeTest.jsp")), "negativeFEBeginTypeTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());
    
    return archive;
  }

  /*
   * @testName: positiveVarTest
   * 
   * @assertion_ids: JSTL:SPEC:21.1; JSTL:SPEC:21.1.1
   * 
   * @testStrategy: Validated the behavior of the 'var' attribute. - the type
   * should be the type of the object in the underlying collection. - the
   * exported var has nested visibility meaning if the variable name reference
   * by var, previously existed, it should no longer exist after completion of
   * the action.
   */
  @Test
  public void positiveVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveVarTest");
    invoke();
  }

  /*
   * @testName: positiveItemsPrimArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1.2
   * 
   * @testStrategy: Validate that arrays of all primitive types can be handled
   * by 'forEach' and that the values in the underlying array are wrapped with
   * its corresponding wrapper type.
   */
  @Test
  public void positiveItemsPrimArrayTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsPrimArrayTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsPrimArrayTest");
    invoke();
  }

  /*
   * @testName: positiveItemsObjArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1.1
   * 
   * @testStrategy: Validate that arrays of Object types can be handled by
   * 'forEach'.
   */
  @Test
  public void positiveItemsObjArrayTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsObjArrayTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsObjArrayTest");
    invoke();
  }

  /*
   * @testName: positiveItemsCollectionTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.2
   * 
   * @testStrategy: Validate that 'forEach' can handle various Collection
   * objects.
   */
  @Test
  public void positiveItemsCollectionTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsCollectionTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsCollectionTest");
    invoke();
  }

  /*
   * @testName: positiveItemsEnumerationTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.4
   * 
   * @testStrategy: Validate that 'forEach' can handle an Enumeration
   */
  @Test
  public void positiveItemsEnumerationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsEnumerationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsEnumerationTest");
    invoke();
  }

  /*
   * @testName: positiveItemsIteratorTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.3
   * 
   * @testStrategy: Validate that 'forEach' can handle an Iterator
   */
  @Test
  public void positiveItemsIteratorTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsIteratorTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveItemsIteratorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveItemsMapTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.5.1; JSTL:SPEC:21.2.5.1.1;
   * JSTL:SPEC:21.2.5.1.2
   * 
   * @testStrategy: Validate that 'forEach' can handle Map objects
   */
  @Test
  public void positiveItemsMapTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsMapTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveItemsMapTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveItemsCommaSepStringTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.7
   * 
   * @testStrategy: Validate that 'forEach' can handle a comma-separated string
   */
  @Test
  public void positiveItemsCommaSepStringTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsCommaSepStringTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsCommaSepStringTest");
    invoke();
  }

  /*
   * @testName: positiveItemsBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:21.5.1; JSTL:SPEC:21.5.1.1; JSTL:SPEC:21.5.3
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'begin'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'begin' attribute is specified
   */
  @Test
  public void positiveItemsBeginTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsBeginTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsBeginTest");
    invoke();
  }

  /*
   * @testName: positiveItemsEndTest
   * 
   * @assertion_ids: JSTL:SPEC:21.6.1; JSTL:SPEC:21.6.3
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'end'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'end' attribute is specified
   */
  @Test
  public void positiveItemsEndTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsEndTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsEndTest");
    invoke();
  }

  /*
   * @testName: positiveItemsStepTest
   * 
   * @assertion_ids: JSTL:SPEC:21.7.1
   * 
   * @testStrategy: Validate the following: - Both EL and RT actions' 'step'
   * attribute accept dynamic and static attribute values - Behavior of the
   * action when the 'step' attribute is specified
   */
  @Test
  public void positiveItemsStepTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsStepTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsStepTest");
    invoke();
  }

  /*
   * @testName: positiveVarStatusTest
   * 
   * @assertion_ids: JSTL:SPEC:21.4
   * 
   * @testStrategy: Validate that varStatus is properly exported with nested
   * scope and is of type jakarta.servlet.jsp.jstl. LoopTagStatus.
   */
  @Test
  public void positiveVarStatusTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveVarStatusTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveVarStatusTest");
    invoke();
  }

  /*
   * @testName: positiveNoItemsIterationTest
   * 
   * @assertion_ids: JSTL:SPEC:21.3; JSTL:SPEC:21.5.2; JSTL:SPEC:21.6.2
   * 
   * @testStrategy: Validate tag behavior when no 'items' attribute is
   * specified.
   */
  @Test
  public void positiveNoItemsIterationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveNoItemsIterationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveNoItemsIterationTest");
    invoke();
  }

  /*
   * @testName: positiveBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:21.8
   * 
   * @testStrategy: Validate that the 'forEach' action can handle body content
   * as well as an empty body.
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
   * @assertion_ids: JSTL:SPEC:21.12
   * 
   * @testStrategy: Validate that no iteration is performed by forEach if items
   * is null.
   */
  @Test
  public void positiveItemsNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveItemsNullTest");
    invoke();
  }

  /*
   * @testName: negativeFEItemsTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.8
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to items evaluates to an incorrect type.
   */
  @Test
  public void negativeFEItemsTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFEItemsTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFEItemsTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEBeginTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.5.4
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to begin evaluates to an incorrect type.
   */
  @Test
  public void negativeFEBeginTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFEBeginTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFEBeginTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEEndTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.6.3
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to end evaluates to an incorrect type.
   */
  @Test
  public void negativeFEEndTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFEEndTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFEEndTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEStepTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:21.7.2
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the EL expression passed to step evaluates to an incorrect type.
   */
  @Test
  public void negativeFEStepTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFEStepTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFEStepTypeTest");
    invoke();
  }

  /*
   * @testName: negativeFEExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:21.13
   * 
   * @testStrategy: Validate that an exception caused by the body content is
   * propagated.
   */
  @Test
  public void negativeFEExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFEExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFEExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveForEachEndLTBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:21.15
   * 
   * @test_Strategy: Validate an end attribute value that is less than the begin
   * attribute value will result in the action not being executed.
   */
  @Test
  public void positiveForEachEndLTBeginTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_iter_foreach_web/positiveForEachEndLTBeginTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest1
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Add some items to a Vector. In a c:forEach tag, reference
   * the Vector as a deferred value in the items attribute. In the body of the
   * tag, set each item to have application scope. Verify that the items can be
   * retrieved after the execution of the tag.
   */
  @Test
  public void positiveForEachDeferredValueTest1() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachDeferredValueTest1.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest1");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest2
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Create a String containing several items delimited by
   * spaces. In a c:forEach tag, reference the String as a deferred value in the
   * items attribute. In the body of the tag, set each item to have application
   * scope. Verify that the items can be retrieved after the execution of the
   * tag.
   */
  @Test
  public void positiveForEachDeferredValueTest2() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachDeferredValueTest2.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest2");
    invoke();
  }

  /*
   * @testName: positiveForEachDeferredValueTest3
   * 
   * @assertion_ids: JSTL:SPEC:21.14
   * 
   * @test_Strategy: Create a HashMap containing several items. In a c:forEach
   * tag, reference the HashMap as a deferred value in the items attribute. In
   * the body of the tag, set each item to have application scope. Verify that
   * the items can be retrieved after the execution of the tag.
   */
  @Test
  public void positiveForEachDeferredValueTest3() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachDeferredValueTest3.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachDeferredValueTest3");
    invoke();
  }
}
