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

package com.sun.ts.tests.jsp.api.jakarta_el.compelresolver;

import java.io.IOException;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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
    setContextRoot("/jsp_compelresolver_web");
    setTestJsp("CompositeELResolverTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_compelresolver_web.war");
    archive.addClasses(CompositeELResolverTag.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class, 
            com.sun.ts.tests.jsp.common.util.InstallCompositeELResolverListener.class,
            com.sun.ts.tests.common.el.api.resolver.ResolverTest.class, 
            com.sun.ts.tests.common.el.api.resolver.BarELResolver.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_compelresolver_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/compelresolver.tld", "compelresolver.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/CompositeELResolverTest.jsp")), "CompositeELResolverTest.jsp");
    
    return archive;

  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: compositeElResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:26; EL:JAVADOC:27; EL:JAVADOC:28;
   * EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31
   * 
   * @test_Strategy: Obtain a CompositeELResolver via the PageContext and verify
   * that API calls work as expected: add() setValue() setValue() throws
   * PropertyNotWritableException getValue() getType() isReadOnly()
   * getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void compositeElResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "compositeElResolverTest");
    invoke();
  }
}
