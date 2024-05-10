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


package com.sun.ts.tests.jstl.spec.etu.tlv.permitted;

import java.io.IOException;

import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");


  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_etu_tlv_perm_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_etu_tlv_perm_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_etu_tlv_perm_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativePermittedTlvTest.jsp")), "negativePermittedTlvTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePermittedTlvTest.jsp")), "positivePermittedTlvTest.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positivePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:109; JSTL:SPEC:104; JSTL:SPEC:104.1;
   * JSTL:SPEC:104.2; JSTL:SPEC:104.3; JSTL:SPEC:104.4
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is specified as a parameter to the PermittedTaglibsTLV, that the
   * use of this library doesn't generate a translation error.
   */
  @Test
  public void positivePermittedTlvTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positivePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "positivePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:109; JSTL:SPEC:104; JSTL:SPEC:104.5
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is not specified as a parameter to the PermittedTaglibsTLV, that
   * the use of this library generates a translation error.
   */
  @Test
  public void negativePermittedTlvTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "negativePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
