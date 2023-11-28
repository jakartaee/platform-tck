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

package com.sun.ts.tests.jsp.spec.configuration.charsequence;


import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  private static final String CONTEXT_ROOT = "/jsp_config_charsequence_web";

  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {

    setGeneralURI("/jsp/spec/configuration/charsequence");
    setContextRoot(CONTEXT_ROOT);
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_charsequence_web.war");
    archive.addClasses(DeferredSyntaxAllowedAsLiteralTag.class,
            JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_charsequence_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/config_charsequence.tld", "config_charsequence.tld");    
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noDeferredSyntaxAllowedAsLiteralTemplateTextTest.jsp")), "noDeferredSyntaxAllowedAsLiteralTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noDeferredSyntaxAllowedAsLiteralActionTest.jsp")), "noDeferredSyntaxAllowedAsLiteralActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredSyntaxAllowedAsLiteralTrueTemplateTextTest.jsp")), "deferredSyntaxAllowedAsLiteralTrueTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredSyntaxAllowedAsLiteralTrueActionTest.jsp")), "deferredSyntaxAllowedAsLiteralTrueActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp")), "deferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/deferredSyntaxAllowedAsLiteralFalseActionTest.jsp")), "deferredSyntaxAllowedAsLiteralFalseActionTest.jsp");
    
    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:296
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to true. Verify
   * that when the character sequence "#{" appears in template text, the
   * sequence is treated as literal characters.
   * [DeferredSyntaxAllowedAsLiteralElement]
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/deferredSyntaxAllowedAsLiteralTrueTemplateTextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "deferredSyntaxAllowedAsLiteralTrueTemplateTextTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to false.
   * Verify that when the character sequence "#{" appears in template text, an
   * internal server error results. [TranslationError#{Sequence]
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/deferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:296
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to true. Verify
   * that when the character sequence "#{" is passed as an attribute to an
   * action, the sequence is treated as literal characters.
   * [DeferredSyntaxAllowedAsLiteralElement]
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/deferredSyntaxAllowedAsLiteralTrueActionTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "deferredSyntaxAllowedAsLiteralTrueActionTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which the deferred-syntax-allowed-as-literal element is set to false.
   * Verify that when the character sequence "#{" is passed as an attribute to
   * an action, and the tld does not specify a deferred-value element for the
   * attribute, an internal server error results. [TranslationError#{Sequence]
   */
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/deferredSyntaxAllowedAsLiteralTrueTemplateTextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/deferredSyntaxAllowedAsLiteralFalseActionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: noDeferredSyntaxAllowedAsLiteralTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which there is no deferred-syntax-allowed-as-literal element. Verify that
   * when the character sequence "#{" appears in template text, an internal
   * server error results. [TranslationError#{Sequence]
   */
  @Test
  public void noDeferredSyntaxAllowedAsLiteralTemplateTextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/noDeferredSyntaxAllowedAsLiteralTemplateTextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: noDeferredSyntaxAllowedAsLiteralActionTest
   * 
   * @assertion_ids: JSP:SPEC:295
   * 
   * @test_Strategy: Invoke a jsp that is a member of a jsp-property-group in
   * which there is no deferred-syntax-allowed-as-literal element. Verify that
   * when the character sequence "#{" is passed as an attribute to an action,
   * and the tld does not specify a deferred-value element for the attribute, an
   * internal server error results. [TranslationError#{Sequence]
   */
  @Test
  public void noDeferredSyntaxAllowedAsLiteralActionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/noDeferredSyntaxAllowedAsLiteralActionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
