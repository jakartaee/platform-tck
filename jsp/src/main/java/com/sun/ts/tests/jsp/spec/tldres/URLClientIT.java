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

/*
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.tldres;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

    setContextRoot("/jsp_tldres_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tldres_web.war");
    archive.addClasses(HSListenerWebInf.class, HSListenerWebInfSub.class,
              UriTag.class, WebXmlTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tldres_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/sub/webinfsub.tld", "sub/webinfsub.tld"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tlds/uri.tld", "tlds/uri.tld"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/webinfpres.tld", "webinfpres.tld"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/webxml.tld", "webxml.tld"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/a12.tld", "a12.tld"); 

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/listenerTldTest.jsp")), "listenerTldTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeJSPPrefix.jsp")), "negativeJSPPrefix.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTaglibAfterActionTest.jsp")), "negativeTaglibAfterActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/path/TldResPathRelativeUriTest.jsp")), "path/TldResPathRelativeUriTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/tld12DefaultBodyContent.jsp")), "tld12DefaultBodyContent.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldExplicitWebXmlPrecedenceTest.jsp")), "TldExplicitWebXmlPrecedenceTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPath11Test.jsp")), "TldResPath11Test.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPathAbsUriNotFoundTest.jsp")), "TldResPathAbsUriNotFoundTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPathDirectTldReferenceTest.jsp")), "TldResPathDirectTldReferenceTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPathExplicitWebXmlTest.jsp")), "TldResPathExplicitWebXmlTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPathMultiTldTest.jsp")), "TldResPathMultiTldTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldResPathWebInfUriTest.jsp")), "TldResPathWebInfUriTest.jsp");
    
    JavaArchive jsp11taglibJar = ShrinkWrap.create(JavaArchive.class, "jsp11taglib.jar");
    jsp11taglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/taglib.tld")), "META-INF/taglib.tld");
    jsp11taglibJar.addClass(Tld11Tag.class);

    archive.addAsLibrary(jsp11taglibJar);

    JavaArchive multitaglibJar = ShrinkWrap.create(JavaArchive.class, "multitaglib.jar");
    multitaglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/multi1.tld")), "META-INF/multi1.tld");
    multitaglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/jartagpres.tld")), "META-INF/jartagpres.tld");
    multitaglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/multi1.tld")), "META-INF/multi1.tld");
    multitaglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/metainfsub.tld")), "META-INF/sub/metainfsub.tld");
    multitaglibJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/multi2.tld")), "META-INF/tlds/multi2.tld");

    multitaglibJar.addClasses(HSListenerMetaInf.class, HSListenerMetaInfSub.class, Multi1Tag.class, Multi2Tag.class);
  
    archive.addAsLibrary(multitaglibJar);

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: tldResourcePathJsp11Test
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a taglibrary packaged as specified by the JSP
   * 1.1 specification will be properly added to the containers taglib map and
   * that the defined tag within can be used.
   */
  @Test
  public void tldResourcePathJsp11Test() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPath11Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Tld11Tag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathMultiTldJarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container can properly process a JAR
   * containing multiple TLDs and adds the TLDs that are found containing a
   * <uri> element to the taglib map.
   */
  @Test
  public void tldResourcePathMultiTldJarTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathMultiTldTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Multi1Tag: Test PASSED|Multi2Tag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathWebInfUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if a TLD exists in the WEB-INF directory or
   * some subdirectory thereof, and that TLD contains a <uri> element, the
   * container will add that taglibrary to the taglibrary map.
   */
  @Test
  public void tldResourcePathWebInfUriTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathWebInfUriTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "UriTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathWebXmlTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Valdiate that the container adds any explicit taglibrary
   * entries found in the deployment descriptor to the taglib map and that the
   * tag or tags within can be properly used.
   */
  @Test
  public void tldResourcePathWebXmlTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathExplicitWebXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResourcePathDirectTldReference
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the TLD can be directly referenced in the taglib
   * directive and that the tag(s) declared in this TLD can be properly used.
   */
  @Test
  public void tldResourcePathDirectTldReference() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathDirectTldReferenceTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldExplicitWebXmlPrecedenceTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that tag library entries explicity defined in the
   * web deployment descriptor (web.xml) have precedence over other tag
   * libraries defined with the same URI.
   */
  @Test
  public void tldExplicitWebXmlPrecedenceTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldExplicitWebXmlPrecedenceTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResPathRelativeUriTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate a tag library can be resolved when referenced
   * though a relative path (no leading '/'). No translation error should occur
   * and the tag should be usable within the translation unit.
   */
  @Test
  public void tldResPathRelativeUriTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/path/TldResPathRelativeUriTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "WebXmlTag: Test PASSED");
    invoke();
  }

  /*
   * @testName: tldResPathAbsUriNotFoundTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if the uri attribute of the taglib directive
   * is an absolute URI that is not present in the container's tag library map,
   * that a translation time error is raised.
   */
  @Test
  public void tldResPathAbsUriNotFoundTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/TldResPathAbsUriNotFoundTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tld12DefaultBodyContentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: omit body-content in a 1.2 style tld and a container is
   * required to apply default JSP.
   */

  @Test
  public void tld12DefaultBodyContentTest() throws Exception {
    String testName = "tld12DefaultBodyContent";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeJSPPrefixTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use jsp prefix for an element that is not a standard action
   * and expect a translation error.
   */

  @Test
  public void negativeJSPPrefixTest() throws Exception {
    String testName = "negativeJSPPrefix";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: listenerTldTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A container is required to read listener element in all TLD
   * files and treat them as extension to web.xml.
   */

  @Test
  public void listenerTldTest() throws Exception {
    String testName = "listenerTldTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "session created meta inf.|session created meta inf sub.|session created web inf.|session created web inf sub.");
    invoke();
  }

  /*
   * @testName: negativeTaglibAfterActionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: It is a fatal translation error for the taglib directive to
   * appear after actions or functions using the prefix.
   */
  @Test
  public void negativeTaglibAfterActionTest() throws Exception {
    String testName = "negativeTaglibAfterActionTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tldres_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
