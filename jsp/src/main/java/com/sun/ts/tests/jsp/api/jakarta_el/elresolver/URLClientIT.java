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
 * @(#)URLClient.java	
 */

package com.sun.ts.tests.jsp.api.jakarta_el.elresolver;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
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
    setContextRoot("/jsp_elresolver_web");
    setTestJsp("ELResolverTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_elresolver_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.common.el.api.resolver.ResolverTest.class, 
            com.sun.ts.tests.common.el.api.resolver.BarELResolver.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_elresolver_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELResolverTest.jsp")), "ELResolverTest.jsp");

    return archive;

  }


  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: elResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:50; EL:JAVADOC:51; EL:JAVADOC:52; EL:JAVADOC:53;
   * EL:JAVADOC:54; EL:JAVADOC:55
   * 
   * @test_Strategy: Obtain an ELResolver via the PageContext and verify that
   * API calls work as expected: getValue() getType() setValue() isReadOnly()
   * getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void elResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elResolverTest");
    invoke();
  }
}
