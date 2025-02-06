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



package com.sun.ts.tests.jstl.spec.xml.xmlcore.types;

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
    setContextRoot("/jstl_xml_types_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_types_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_types_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveJavaToXPathTypesTest.jsp")), "positiveJavaToXPathTypesTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXPathToJavaTypesTest.jsp")), "positiveXPathToJavaTypesTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveJavaToXPathTypesTest
   * 
   * @assertion_ids: JSTL:SPEC:65; JSTL:SPEC:65.1; JSTL:SPEC:65.2;
   * JSTL:SPEC:65.3; JSTL:SPEC:65.4; JSTL:SPEC:65.5
   * 
   * @testStrategy: Validate that XPath variables of Java types, can be properly
   * used in XPath expressions. The supported type mappings are: Java XPath
   * java.lang.Boolean boolean java.lang.Number number java.lang.String string
   * Object exported by parse, set, or forEach node-set
   */
  @Test
  public void positiveJavaToXPathTypesTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveJavaToXPathTypesTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveJavaToXPathTypesTest");
    invoke();
  }

  /*
   * @testName: positiveXPathToJavaTypesTest
   * 
   * @assertion_ids: JSTL:SPEC:66; JSTL:SPEC:66.1; JSTL:SPEC:66.2;
   * JSTL:SPEC:66.3; JSTL:SPEC:66.4
   * 
   * @testStrategy: Validate that the result of an XPath expression yeilds the
   * correct type based of the specified XPath to Java type mapping: XPath Java
   * boolean java.lang.Boolean number java.lang.Number string java.lang.String
   * node-set Implementation specified (test will check for java.lang.Object)
   */
  @Test
  public void positiveXPathToJavaTypesTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveXPathToJavaTypesTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveXPathToJavaTypesTest");
    invoke();
  }

}
