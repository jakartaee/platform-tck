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



package com.sun.ts.tests.jstl.spec.xml.xmlcore.bindings;

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
    setContextRoot("/jstl_xml_bindings_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_bindings_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_bindings_web.xml"));
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXPathVariableBindingsTest.jsp")), "positiveXPathVariableBindingsTest.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveXPathVariableBindingsTest
   * 
   * @assertion_ids: JSTL:SPEC:64; JSTL:SPEC:64.1; JSTL:SPEC:64.2;
   * JSTL:SPEC:64.3; JSTL:SPEC:64.4; JSTL:SPEC:64.5; JSTL:SPEC:64.6;
   * JSTL:SPEC:64.7; JSTL:SPEC:64.8; JSTL:SPEC:64.9
   * 
   * @testStrategy: Validate the following bindings are available:
   *
   * $foo - pageContext.findAttribute("foo") $param.foo -
   * request.getParameter("foo") $header:foo - request.getHeader("foo")
   * $initParam:foo - application.getInitParamter("foo") $cooke:foo - maps to
   * the cookies value for name foo $pageScope:foo -
   * pageContext.getAttribute("foo", PageContext.PAGE_SCOPE) $requestScope:foo -
   * pageContext.getAttribute("foo", PageContext.REQUEST_SCOPE)
   * $sessionScope:foo - pageContext.getAttribute("foo",
   * PageContext.SESSION_SCOPE) $applicationScope:foo -
   * pageContext.getAttribute("foo", PageContext.APPLICATION_SCOPE)
   */
  @Test
  public void positiveXPathVariableBindingsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveXPathVariableBindingsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveXPathVariableBindingsTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveXPathVariableBindingsTest.jsp?param1=RequestParameter1");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "reqheader:RequestHeader|Cookie: $Version=1; mycookie=CookieFound; $Domain="
            + _hostname + "; $Path=/jstl_xml_bindings_web");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveXPathVariableBindingsTest.gf");
    invoke();
  }
}
