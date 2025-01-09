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



package com.sun.ts.tests.jstl.spec.core.urlresource.redirect;

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
    setContextRoot("/jstl_core_url_redirect_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_url_redirect_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_url_redirect_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeRedirectContextUrlInvalidValueTest.jsp")), "negativeRedirectContextUrlInvalidValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeRedirectExcBodyContentTest.jsp")), "negativeRedirectExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRedirectParamsBodyTest.jsp")), "positiveRedirectParamsBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRedirectTest.jsp")), "positiveRedirectTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveRedirectTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1; JSTL:SPEC:43.1.1
   * 
   * @testStrategy: Validate that the action can properly redirect when the url
   * attribute is provided either a dynamic or static values.
   */
  @Test
  public void positiveRedirectTest() throws Exception {

    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_url_redirect_web/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_url_redirect_web/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_url_redirect_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_url_redirect_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_web/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_core_web/urlresource/param/import.jsp");
    invoke();

  }

  /*
   * @testName: positiveRedirectParamsBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1
   * 
   * @testStrategy: Validate that the action can properly redirect when the the
   * body content consists of param subtags. The params should be added to the
   * redirect URI.
   */
  @Test
  public void positiveRedirectParamsBodyTest() throws Exception {

    /* RT */
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectParamsBodyTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST,
        "positiveRedirectParamsBodyTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port
        + "/jstl_core_url_redirect_web/urlresource/param/import.jsp?testparm=testval");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectParamsTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST,
        "positiveRedirectParamsBodyTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_core_web/urlresource/param/import.jsp?testparm=testval");
    invoke();
  }

  /*
   * @testName: negativeRedirectExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:43.5
   * 
   * @testStrategy: Validate that if the body content of the action causes an
   * exception that it is properly propagated.
   */
  @Test
  public void negativeRedirectExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeRedirectExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "negativeRedirectExcBodyContentTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeRedirectExcBodyContentTest.jsp?el=true");
    // TEST_PROPS.setProperty(GOLDENFILE, "negativeRedirectExcBodyContentTest.gf");
    invoke();
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "negativeRedirectExcBodyContentTest");
    TEST_PROPS.setProperty(REQUEST,
        "negativeRedirectExcBodyContentTest.jsp?rt=true");
    // TEST_PROPS.setProperty(GOLDENFILE, "negativeRedirectExcBodyContentTest.gf");
    invoke();
  }

  /*
   * @testName: negativeRedirectContextUrlInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:43.6.3; JSTL:SPEC:43.6.4
   * 
   * @testStrategy: Validate that if the context attribute is specified, and
   * either context or url are provided values that don't start with a leading
   * slash, an exception occurs.
   */
  @Test
  public void negativeRedirectContextUrlInvalidValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeRedirectContextUrlInvalidValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "negativeRedirectContextUrlInvalidValueTest");
    invoke();
  }
}
