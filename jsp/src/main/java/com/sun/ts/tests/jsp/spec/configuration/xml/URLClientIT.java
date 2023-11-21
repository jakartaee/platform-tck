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

package com.sun.ts.tests.jsp.spec.configuration.xml;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  public URLClientIT() throws Exception {


    setContextRoot("/jsp_config_xml_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_xml_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_xml_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/xmltrue/XmlJspTest.xsp")), "xmltrue/XmlJspTest.xsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/xmlfalse/NonXmlTest.jsp")), "xmlfalse/NonXmlTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/unspec/NonXmlTest.jsp")), "unspec/NonXmlTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspConfigurationIsXmlUnspecTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if is-xml is not specified for a property
   * group, that the files matched by the url-pattern will not be considered JSP
   * documents.
   * 
   */
  @Test
  public void jspConfigurationIsXmlUnspecTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/unspec/NonXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationIsXmlFalseTest
   * 
   * @assertion_ids: JSP:SPEC:150.2
   * 
   * @test_Strategy: Validate that if is-xml is set to false for a property
   * group, that the files matched by the url-pattern will not be considered JSP
   * documents.
   */
  @Test
  public void jspConfigurationIsXmlFalseTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/xmlfalse/NonXmlTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspConfigurationIsXmlTrueTest
   * 
   * @assertion_ids: JSP:SPEC:150.1
   * 
   * @test_Strategy: Validate that if is-xml is set to true for a property
   * group, that the files matched by the url-pattern will be considered JSP
   * documents.
   */
  @Test
  public void jspConfigurationIsXmlTrueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_xml_web/xmltrue/XmlJspTest.xsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:page|directive");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
