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



package com.sun.ts.tests.jstl.spec.xml.xtransform.param;

import java.io.IOException;
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
    setContextRoot("/jstl_xml_xformparam_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xformparam_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xformparam_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/param.xsl")), "param.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXParamBodyValueTest.jsp")), "positiveXParamBodyValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXParamNameTest.jsp")), "positiveXParamNameTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXParamValueTest.jsp")), "positiveXParamValueTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveXParamNameTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.1; JSTL:SPEC:74.1.1
   * 
   * @testStrategy: Validate the name attribute of the x:param action is able to
   * accept both static and dynamic values.
   */
  @Test
  public void positiveXParamNameTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamNameTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "10pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

  /*
   * @testName: positiveXParamValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.2; JSTL:SPEC:74.2.1
   * 
   * @testStrategy: Validate the value attribute of the x:param action is able
   * to accept both static and dynamic values.
   */
  @Test
  public void positiveXParamValueTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamValueTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "13pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

  /*
   * @testName: positiveXParamBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74; JSTL:SPEC:74.3
   * 
   * @testStrategy: Validate the value of the param can be provided as body
   * content to the action.
   */
  @Test
  public void positiveXParamBodyValueTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD, "positiveXParamBodyValueTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xformparam_web/positiveXParamBodyValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "8pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

}
