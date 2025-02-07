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

package com.sun.ts.tests.jstl.spec.core.conditional.iftag;

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

  public JSTLClientIT() {
    setContextRoot("/jstl_core_cond_if_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_cond_if_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_cond_if_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfExcBodyContentTest.jsp")), "negativeIfExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfTestTypeTest.jsp")), "negativeIfTestTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfBodyBehaviorTest.jsp")), "positiveIfBodyBehaviorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfExportedVarTypeTest.jsp")), "positiveIfExportedVarTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfScopeTest.jsp")), "positiveIfScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfTest.jsp")), "positiveIfTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveIfTest
   * 
   * @assertion_ids: JSTL:SPEC:14.2
   * 
   * @testStrategy: Verify 'test' and 'var' attribute behavior of the 'if'
   * action with no content body
   */
  @Test
  public void positiveIfTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfTest");
    invoke();
  }

  /*
   * @testName: positiveIfBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:14.1.1; JSTL:SPEC:14.1.2
   * 
   * @testStrategy: Verify the behavior of the 'if' action with regards to the
   * result of it's test and it's body content
   */
  @Test
  public void positiveIfBodyBehaviorTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfBodyBehaviorTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.3.1; JSTL:SPEC:14.3.2; JSTL:SPEC:14.3.3;
   * JSTL:SPEC:14.3.4; JSTL:SPEC:14.3.5
   * 
   * @testStrategy: Verify the behavior of the 'if' action when using the scope
   * attribute. If scope is not specified, the exported var should be in the
   * page scope, otherwise the exported var should be in the designated scope.
   */
  @Test
  public void positiveIfScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: positiveIfExportedVarTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.2.1
   * 
   * @testStrategy: Validate that the variable exported by the 'if' action is of
   * type 'java.lang.Boolean'
   */
  @Test
  public void positiveIfExportedVarTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfExportedVarTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfExportedVarTypeTest");
    invoke();
  }

  /*
   * @testName: negativeIfTestTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.1.3
   * 
   * @testStrategy: Validate that an instance of
   * jakarta.servlet.jsp.JspTagException is thrown if the resulting expression
   * passed ot the 'test' attribute is not of the expected type (boolean/Boolean
   * for EL, and boolean for RT).
   */
  @Test
  public void negativeIfTestTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeIfTestTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeIfTestTypeTest");
    invoke();
  }

  /*
   * @testName: negativeIfExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:14.7
   * 
   * @testStrategy: Validate that exceptions caused by the body content are
   * propagated.
   */
  @Test
  public void negativeIfExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeIfExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeIfExcBodyContentTest");
    invoke();
  }
}
