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



package com.sun.ts.tests.jstl.spec.core.general.set;

import java.io.IOException;
import java.io.InputStream;
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
    setContextRoot("/jstl_core_gen_set_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_gen_set_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_gen_set_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetBodyContentExcTest.jsp")), "negativeSetBodyContentExcTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetTargetNullInvalidObjTest.jsp")), "negativeSetTargetNullInvalidObjTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetBodyBehaviorTest.jsp")), "positiveSetBodyBehaviorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDeferredValueTest.jsp")), "positiveSetDeferredValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetNullValueTest.jsp")), "positiveSetNullValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropertyTest.jsp")), "positiveSetPropertyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetScopeTest.jsp")), "positiveSetScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTest.jsp")), "positiveSetTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveSetTest
   * 
   * @assertion_ids: JSTL:SPEC:13.1; JSTL:SPEC:13.1.1; JSTL:SPEC:13.3
   * 
   * @testStrategy: Validate that the dynamic and static values provided to the
   * 'value' attribute are made available to the test page by using <c:out> to
   * print the values.
   */
  @Test
  public void positiveSetTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetTest");
    invoke();
  }

  /*
   * @testName: positiveSetBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:13.2
   * 
   * @testStrategy: Validate the value of the exported var can be set within the
   * body of the <c:set> action. Verify the result using <c:out>
   */
  @Test
  public void positiveSetBodyBehaviorTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetBodyBehaviorTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:13.4.1; JSTL:SPEC:13.4.2; JSTL:SPEC:13.4.3;
   * JSTL:SPEC:13.4.4; JSTL:SPEC:13.5
   * 
   * @testStrategy: Validated the different scope behaviors (default and
   * explicitly set scopes) by exporting different vairables to the assorted
   * scopes and then print the values using PageContext.getAttribute(String,
   * int).
   */
  @Test
  public void positiveSetScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetNullValueTest
   * 
   * @assertion_ids: JSTL:SPEC:13.7
   * 
   * @testStrategy: Validate the following: - if setting a scoped variable and
   * value is null, the variable referenced by var and scope is removed. This is
   * validated by setting a scoped variable, and then calling remove using that
   * var name. Then validate that the variable is indeed removed by using an out
   * action with that variable (which is now null. - If setting a property in a
   * Map and value is null, the key/value is removed from the map. - If setting
   * a property in a Bean and value is null, will return null when calling the
   * get method for that propery after the set completes.
   */
  @Test
  public void positiveSetNullValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetNullValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetNullValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetPropertyTest
   * 
   * @assertion_ids: JSTL:SPEC:13.10.1
   * 
   * @testStrategy: Validate that the set action can set properties of JavaBeans
   * and set key/values in Map instances using the target and property
   * attributes.
   */
  @Test
  public void positiveSetPropertyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropertyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropertyTest");
    invoke();
  }

  /*
   * @testName: negativeSetBodyContextExcTest
   * 
   * @assertion_ids: JSTL:SPEC:13.9
   * 
   * @testStrategy: Validate that if the body content of the action is thrown,
   * that it is properly propagated.
   */
  @Test
  public void negativeSetBodyContextExcTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetBodyContentExcTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeSetBodyContentExcTest");
    invoke();
  }

  /*
   * @testName: negativeSetTargetNullInvalidObjTest
   * 
   * @assertion_ids: JSTL:SPEC:13.10.2; JSTL:SPEC:13.10.3
   * 
   * @testStrategy: Validate that a jakarta.servet.jsp.JspException is thrown if
   * the target attribute is null, or is provided an object that is no a
   * JavaBean or an instance of java.util.Map.
   */
  @Test
  public void negativeSetTargetNullInvalidObjTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetTargetNullInvalidObjTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeSetTargetNullInvalidObjTest");
    invoke();
  }

  /*
   * @testName: positiveSetDeferredValueTest
   * 
   * @assertion_ids: JSTL:SPEC:13.18
   * 
   * @test_Strategy: In a c:set tag, assign a deferred value to a variable. In a
   * second tag, set the variable to have application scope. Verify that the
   * value can be retrieved after the execution of the tag.
   */
  @Test
  public void positiveSetDeferredValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDeferredValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDeferredValueTest");
    invoke();
  }
}
