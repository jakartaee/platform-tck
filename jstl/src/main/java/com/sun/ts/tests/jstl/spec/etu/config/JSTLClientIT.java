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


package com.sun.ts.tests.jstl.spec.etu.config;

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
    setContextRoot("/jstl_etu_config_web");    
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_etu_config_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_etu_config_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigStaticMemebersTest.jsp")), "positiveConfigStaticMemebersTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigGetSetRemoveSessionTest.jsp")), "positiveConfigGetSetRemoveSessionTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigGetSetRemoveRequestTest.jsp")), "positiveConfigGetSetRemoveRequestTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigGetSetRemovePageContextTest.jsp")), "positiveConfigGetSetRemovePageContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigGetSetRemoveApplicationTest.jsp")), "positiveConfigGetSetRemoveApplicationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveConfigFindTest.jsp")), "positiveConfigFindTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  
  /*
   * @testName: positiveConfigStaticMembersTest
   * 
   * @assertion_ids: JSTL:SPEC:99.1; JSTL:SPEC:99.2; JSTL:SPEC:99.3;
   * JSTL:SPEC:99.4; JSTL:SPEC:99.5; JSTL:SPEC:99.6
   * 
   * @testStrategy: Validate that the public static member values of the
   * jakarta.servlet.jsp.jstl.core.Config class agree with the javadoc.
   */
  @Test
  public void positiveConfigStaticMembersTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveConfigStaticMemebersTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveConfigStaticMemebersTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemovePageContextTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a PageContext object. Using the same variable, verify that the Config class
   * sets variables in the PageContext in such a way that even if the variable
   * names specified are all the same, they are unique in each scope. Validate
   * the the values returned by get() are the same as those set. Additionally
   * validate that once the variables are removed, that additional calls to get
   * for the same variables, will return null.
   */
  @Test
  public void positiveConfigGetSetRemovePageContextTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveConfigGetSetRemovePageContextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveConfigGetSetRemovePageContextTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveRequestTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a ServletRequest object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  @Test
  public void positiveConfigGetSetRemoveRequestTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveConfigGetSetRemoveRequestTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveConfigGetSetRemoveRequestTest");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveSessionTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   *
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * an HttpSession object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  @Test
  public void positiveConfigGetSetRemoveSessionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_etu_config_web/positiveConfigGetSetRemoveSessionTest.jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveConfigGetSetRemoveApplicationTest
   * 
   * @assertion_ids: JSTL:SPEC:100; JSTL:SPEC:101; JSTL:SPEC:102
   * 
   * @testStrategy: Validate the set(), get(), and remove() methods when passing
   * a ServletContext object. Additionally validate that once the variable is
   * removed, that additional calls to get for the same variable, will return
   * null.
   */
  @Test
  public void positiveConfigGetSetRemoveApplicationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveConfigGetSetRemoveApplicationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveConfigGetSetRemoveApplicationTest");
    invoke();
  }

  /*
   * @testName: positiveConfigFindTest
   * 
   * @assertion_ids: JSTL:SPEC:103; JSTL:SPEC:103.1
   * 
   * @testStrategy: Validate that the find() method is able to find the and
   * return the specified variable from the PageContext without specifying a
   * scope. Also validate that if no variable is found in the PageContext, that
   * method will attempt to find a context initialization parameter by the name
   * provided.
   */
  @Test
  public void positiveConfigFindTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveConfigFindTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveConfigFindTest");
    invoke();
  }
}
