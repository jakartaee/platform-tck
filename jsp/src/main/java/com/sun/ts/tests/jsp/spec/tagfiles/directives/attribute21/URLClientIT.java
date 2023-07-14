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

package com.sun.ts.tests.jsp.spec.tagfiles.directives.attribute21;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
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

  private static final String CONTEXT_ROOT = "/jsp_tagfile_directives_attribute21_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_directives_attribute21_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_directives_attribute21_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/onlyOneOfDeferredValueOrDeferredMethod.tag", "tags/onlyOneOfDeferredValueOrDeferredMethod.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicit.tld", "tags/implicit.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/deferredValueTypeNotSpecified.tag", "tags/deferredValueTypeNotSpecified.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultDeferredValue2.tag", "tags/defaultDeferredValue2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultDeferredValue1.tag", "tags/defaultDeferredValue1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultDeferredMethodSignature.tag", "tags/defaultDeferredMethodSignature.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultDeferredMethod2.tag", "tags/defaultDeferredMethod2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultDeferredMethod1.tag", "tags/defaultDeferredMethod1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/bothDeferredValueTypeAndDeferredValue.tag", "tags/bothDeferredValueTypeAndDeferredValue.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/bothDeferredMethodAndSignature.tag", "tags/bothDeferredMethodAndSignature.tag");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/onlyOneOfDeferredValueOrDeferredMethod.jsp")), "onlyOneOfDeferredValueOrDeferredMethod.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredValueTypeNotSpecified.jsp")), "deferredValueTypeNotSpecified.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultDeferredValue2.jsp")), "defaultDeferredValue2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultDeferredValue1.jsp")), "defaultDeferredValue1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultDeferredMethodSignature.jsp")), "defaultDeferredMethodSignature.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultDeferredMethod2.jsp")), "defaultDeferredMethod2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/defaultDeferredMethod1.jsp")), "defaultDeferredMethod1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bothDeferredValueTypeAndDeferredValue.jsp")), "bothDeferredValueTypeAndDeferredValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bothDeferredMethodAndSignature.jsp")), "bothDeferredMethodAndSignature.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: onlyOneOfDeferredValueOrDeferredMethodTest
   * 
   * @assertion_ids: JSP:SPEC:230.8.1
   * 
   * @test_Strategy: [OnlyOneOfDeferredValueOrMethod] A translation error must
   * result when both deferredValue and deferredMethod appear in the same tag.
   */

  @Test
  public void onlyOneOfDeferredValueOrDeferredMethodTest() throws Exception {
    String testName = "onlyOneOfDeferredValueOrDeferredMethod";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredValue1Test
   * 
   * @assertion_ids: JSP:SPEC:230.8.2
   * 
   * @test_Strategy: [DefaultDeferredValue] Specify an attribute tag with no
   * deferredValue attribute and a deferredValueType attribute. Verify that the
   * attribute's value represents a deferred value expression.
   */
  @Test
  public void defaultDeferredValue1Test() throws Exception {
    String testName = "defaultDeferredValue1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultDeferredValue2Test
   * 
   * @assertion_ids: JSP:SPEC:230.8.2
   * 
   * @test_Strategy: [DefaultDeferredValue] Specify an attribute tag with no
   * deferredValue attribute and no deferredValueType attribute. Verify that the
   * attribute's value does not represent a deferred value expression by
   * generating a translation error when '#{' is used.
   */
  @Test
  public void defaultDeferredValue2Test() throws Exception {
    String testName = "defaultDeferredValue2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredValueTypeNotSpecifiedTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.1
   * 
   * @test_Strategy: [DeferredValueTypeNotSpecified] Specify an attribute tag
   * with a deferredValue attribute and no deferredValueType attribute. Verify
   * that the type resulting from the evaluation of the attribute's value
   * expression is java.lang.Object.
   */
  @Test
  public void deferredValueTypeNotSpecifiedTest() throws Exception {
    String testName = "deferredValueTypeNotSpecified";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bothDeferredValueTypeAndDeferredValueTest
   * 
   * @assertion_ids: JSP:SPEC:230.9.2
   * 
   * @test_Strategy: [BothDeferredValueTypeAndDeferredValue] A translation error
   * must result when both deferredValueType and and deferredValue appear in the
   * same tag where deferredValue is set to false.
   */
  @Test
  public void bothDeferredValueTypeAndDeferredValueTest() throws Exception {
    String testName = "bothDeferredValueTypeAndDeferredValue";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredMethod1Test
   * 
   * @assertion_ids: JSP:SPEC:230.10.1
   * 
   * @test_Strategy: [DefaultDeferredMethod] Specify an attribute tag with no
   * deferredMethod attribute and a deferredMethodSignature attribute. Verify
   * that the attribute's value represents a deferred method expression.
   */
  @Test
  public void defaultDeferredMethod1Test() throws Exception {
    String testName = "defaultDeferredMethod1";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: defaultDeferredMethod2Test
   * 
   * @assertion_ids: JSP:SPEC:230.10.1
   * 
   * @test_Strategy: [DefaultDeferredMethod] Specify an attribute tag with no
   * deferredMethod attribute and no deferredMethodSignature attribute. Verify
   * that the attribute's value does not represent a deferred method expression
   * by generating a translation error when '#{' is used.
   */
  @Test
  public void defaultDeferredMethod2Test() throws Exception {
    String testName = "defaultDeferredMethod2";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: bothDeferredMethodAndSignatureTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.1
   * 
   * @test_Strategy: [BothDeferredMethodAndSignature] A translation error must
   * result when both deferredMethodSignature and and deferredMethod appear in
   * the same tag where deferredMethod is set to false.
   */
  @Test
  public void bothDeferredMethodAndSignatureTest() throws Exception {
    String testName = "bothDeferredMethodAndSignature";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: defaultDeferredMethodSignatureTest
   * 
   * @assertion_ids: JSP:SPEC:230.11.2
   * 
   * @test_Strategy: [DefaultDeferredMethodSignature] Specify an attribute tag
   * with a deferredMethod attribute set to true and no
   * deferredMethodMethodSignature attribute. Verify that the method signature
   * defaults to void methodname().
   */
  @Test
  public void defaultDeferredMethodSignatureTest() throws Exception {
    String testName = "defaultDeferredMethodSignature";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

}
