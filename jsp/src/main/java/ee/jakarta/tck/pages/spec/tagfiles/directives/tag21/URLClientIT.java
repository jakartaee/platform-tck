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

package ee.jakarta.tck.pages.spec.tagfiles.directives.tag21;


import java.io.IOException;
import java.io.InputStream;

import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  private static String CONTEXT_ROOT = "/jsp_tagfile_directives_tag21_web";

  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {


    setGeneralURI("/jsp/spec/tagfiles/directives/tag21");
    setContextRoot("/jsp_tagfile_directives_tag21_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfile_directives_tag21_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfile_directives_tag21_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/implicit.tld", "tags/implicit.tld"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeferredSyntaxAsLiteral.tag", "tags/DeferredSyntaxAsLiteral.tag"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeferredSyntaxAllowedAsLiteralTrueTemplateText.tag", "tags/DeferredSyntaxAllowedAsLiteralTrueTemplateText.tag"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeferredSyntaxAllowedAsLiteralTrueAction.tag", "tags/DeferredSyntaxAllowedAsLiteralTrueAction.tag"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeferredSyntaxAllowedAsLiteralFalseTemplateText.tag", "tags/DeferredSyntaxAllowedAsLiteralFalseTemplateText.tag"); 
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DeferredSyntaxAllowedAsLiteralFalseAction.tag", "tags/DeferredSyntaxAllowedAsLiteralFalseAction.tag"); 
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueTemplateText.jsp")), "DeferredSyntaxAllowedAsLiteralTrueTemplateText.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueAction.jsp")), "DeferredSyntaxAllowedAsLiteralTrueAction.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralFalseTemplateText.jsp")), "DeferredSyntaxAllowedAsLiteralFalseTemplateText.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralFalseAction.jsp")), "DeferredSyntaxAllowedAsLiteralFalseAction.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */
  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to false, a translation error occurs when the '{#' character
   * sequence is used in template text and the jsp version is 2.1 or greater.
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Exception {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseTemplateText";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to true, the '{#' character sequence is allowed in template text
   * when the jsp version is 2.1 or greater.
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueTemplateText.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueTemplateText");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to false, a translation error occurs when the '{#' character
   * sequence is used in actions and the jsp version is 2.1 or greater.
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Exception {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseAction";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:229.26
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralTagDirectiveAttribute]
   * Verify that when the DeferredSyntaxAllowedAsLiteral tag directive attribute
   * is set to true, the '{#' character sequence is allowed in actions when the
   * jsp version is 2.1 or greater.
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueAction.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueAction");
    invoke();
  }
}
