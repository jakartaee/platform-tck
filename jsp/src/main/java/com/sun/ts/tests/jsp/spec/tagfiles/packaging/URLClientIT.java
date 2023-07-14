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
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.tagfiles.packaging;


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

    setContextRoot("/jsp_tagfile_pkg_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_pkg_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_pkg_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tag.tld", "tag.tld");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/WebTag1.tag", "tags/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/badtldversion/WebTag1.tag", "tags/badtldversion/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/badtldversion/implicit.tld", "tags/badtldversion/implicit.tld");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/defaultjspversion/WebTag1.tag", "tags/defaultjspversion/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/dir/WebTag1.tag", "tags/dir/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname20/DeferredSyntaxAsLiteral.tag", "tags/reservedname20/DeferredSyntaxAsLiteral.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname20/ImplicitTld20.tag", "tags/reservedname20/ImplicitTld20.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname20/implicit.tld", "tags/reservedname20/implicit.tld");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname21/DeferredSyntaxAsLiteral.tag", "tags/reservedname21/DeferredSyntaxAsLiteral.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname21/ImplicitTld21.tag", "tags/reservedname21/ImplicitTld21.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/reservedname21/implicit.tld", "tags/reservedname21/implicit.tld");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/toomanytldelements/WebTag1.tag", "tags/toomanytldelements/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/toomanytldelements/implicit.tld", "tags/toomanytldelements/implicit.tld");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/versionnotmatch/WebTag1.tag", "tags/versionnotmatch/WebTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/versionnotmatch/implicit.tld", "tags/versionnotmatch/implicit.tld");
  
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IgnoreTag.tag")), "IgnoreTag.tag");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/Tag1.tag")), "Tag1.tag");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitTldAdditionalElements.jsp")), "ImplicitTldAdditionalElements.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitTldDefaultJspVersion.jsp")), "ImplicitTldDefaultJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitTldMinimumJspVersion.jsp")), "ImplicitTldMinimumJspVersion.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitTldReservedName20.jsp")), "ImplicitTldReservedName20.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ImplicitTldReservedName21.jsp")), "ImplicitTldReservedName21.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagFilePackagedJarIgnoredTagTest.jsp")), "JspTagFilePackagedJarIgnoredTagTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagFilePackagedJarTest.jsp")), "JspTagFilePackagedJarTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagFilePackagedWarTest.jsp")), "JspTagFilePackagedWarTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTagFilePackagedWarTldTest.jsp")), "JspTagFilePackagedWarTldTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TldImplicitTldJspVersionNotMatch.jsp")), "TldImplicitTldJspVersionNotMatch.jsp");
  
    JavaArchive tagfileJar = ShrinkWrap.create(JavaArchive.class, "tagfile.jar");
    tagfileJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/jartagfile.tld")), "META-INF/jartagfile.tld");
    tagfileJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IgnoreTag.tag")), "META-INF/tags/IgnoreTag.tag");
    tagfileJar.addAsResource(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/Tag1.tag")), "META-INF/tags/Tag1.tag");

    archive.addAsLibrary(tagfileJar);

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspTagFilePackagedJarTest
   * 
   * @assertion_ids: JSP:SPEC:220.1
   * 
   * @test_Strategy: Validate that tag files packaged in a JAR file and
   * referenced in a TLD, can be recognized by the container and invoked within
   * a Page.
   */
  @Test
  public void jspTagFilePackagedJarTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedJarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedJarIgnoredTagTest
   * 
   * @assertion_ids: JSP:SPEC:220.2
   * 
   * @test_Strategy: Validate that if a Tag file is packaged in a JAR but not
   * referenced by a TLD, the container ignores the tag file. Since the Page
   * will refer to the ignored tag, a translation error should occur by its use.
   */
  @Test
  public void jspTagFilePackagedJarIgnoredTagTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedJarIgnoredTagTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedWarTest
   * 
   * @assertion_ids: JSP:SPEC:221
   * 
   * @test_Strategy: Validate that tag files can be properly detected by the
   * container and that they can be used in a Page.
   */
  @Test
  public void jspTagFilePackagedWarTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedWarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED1|Test PASSED2");
    invoke();
  }

  /*
   * @testName: jspTagFilePackagedWarTldTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate tag files packaged in a web application can be
   * explicity referenced in a TLD to be used by a a Page.
   */
  @Test
  public void jspTagFilePackagedWarTldTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/JspTagFilePackagedWarTldTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED1");
    invoke();
  }

  /*
   * @testName: implicitTldMinimumJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:311
   * 
   * @test_Strategy: [ImplicitTldMinimumJspVersion] Show that if the JSP version
   * specified in an implicit.tld file is less than 2.0 a translation error will
   * result.
   */
  @Test
  public void implicitTldMinimumJspVersionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldMinimumJspVersion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: implicitTldAdditionalElementsTest
   * 
   * @assertion_ids: JSP:SPEC:310
   * 
   * @test_Strategy: [ImplicitTldAdditionalElements] Show that if the JSP
   * version specified in an implicit.tld file is less than 2.0 a translation
   * error will result.
   */
  @Test
  public void implicitTldAdditionalElementsTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldAdditionalElements.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tldImplicitTldJspVersionNotMatchTest
   * 
   * @assertion_ids: JSP:SPEC:313
   * 
   * @test_Strategy: [TldImplicitTldJspVersionNotMatch] Show that if a tag file
   * is referenced by both a TLD and an implicit TLD, the JSP versions of the
   * TLD and implicit TLD do not need to match.
   */
  @Test
  public void tldImplicitTldJspVersionNotMatchTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/TldImplicitTldJspVersionNotMatch.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitTldReservedName20Test
   * 
   * @assertion_ids: JSP:SPEC:308
   * 
   * @test_Strategy: [ImplicitTldReservedName] The JSP version of an implicit
   * tag library may be configured by placing a TLD with the reserved name
   * "implicit.tld" in the same directory as the implicit tag library's
   * constituent tag files. Verify this for version 2.0 by embedding '{#' in an
   * action without generating a translation error.
   */
  @Test
  public void implicitTldReservedName20Test() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldReservedName20.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: implicitTldReservedName21Test
   * 
   * @assertion_ids: JSP:SPEC:308
   * 
   * @test_Strategy: [ImplicitTldReservedName] The JSP version of an implicit
   * tag library may be configured by placing a TLD with the reserved name
   * "implicit.tld" in the same directory as the implicit tag library's
   * constituent tag files. Verify this for version 2.1 by embedding '{#' in an
   * action to cause a translation error.
   */
  @Test
  public void implicitTldReservedName21Test() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldReservedName21.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: implicitTldDefaultJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:307
   * 
   * @test_Strategy: [ImplicitTldDefaultJspVersion] Show that the jsp version of
   * an implicit tag library defaults to 2.0 by embedding an unescaped '#{"
   * character sequence in template text.
   */
  @Test
  public void implicitTldDefaultJspVersionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_pkg_web/ImplicitTldDefaultJspVersion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

}
