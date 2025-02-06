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


package com.sun.ts.tests.jstl.spec.etu.uri;

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


  /** Creates new URLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_etu_uri_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_etu_uri_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_etu_uri_web.xml"));
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveJSTLURITest.jsp")), "positiveJSTLURITest.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveJSTLURITest
   * 
   * @assertion_ids: JSTL:SPEC:1; JSTL:SPEC:2; JSTL:SPEC:3; JSTL:SPEC:4;
   * JSTL:SPEC:16; JSTL:SPEC:17; JSTL:SPEC:18; JSTL:SPEC:19
   * 
   * @testStrategy: Import all defined taglib URI definitions for both EL and RT
   * tags. If defined correctly, a fatal translation error should not occur (
   * per section 7.3.6.2 of the JavaServer Pages 1.2 Specification.
   */
  @Test
  public void positiveJSTLURITest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveJSTLURITest");
    TEST_PROPS.setProperty(REQUEST, "positiveJSTLURITest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }
}
