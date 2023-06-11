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

/*
 * @(#)URLClient.java	
 */

package com.sun.ts.tests.jsp.api.jakarta_el.resourcebundleelresolver;

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

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_resourcebundleelresolver_web");
    setTestJsp("ResourceBundleELResolverTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_resourcebundleelresolver_web.war");
    archive.addClasses(ResourceBundleELResolverTag.class,
              com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
              com.sun.ts.tests.common.el.api.resolver.ResolverTest.class, 
              com.sun.ts.tests.common.el.api.resolver.BarELResolver.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_resourcebundleelresolver_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/resourcebundleelresolver.tld", "resourcebundleelresolver.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ResourceBundleELResolverTest.jsp")), "ResourceBundleELResolverTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: resourceBundleElResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:103; EL:JAVADOC:104; EL:JAVADOC:105;
   * EL:JAVADOC:106; EL:JAVADOC:107; EL:JAVADOC:109
   * 
   * @test_Strategy: Obtain an ResourceBundleELResolver via the PageContext and
   * verify that API calls work as expected: setValue() getValue() getType()
   * isReadOnly() getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void resourceBundleElResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resourcebundleElResolverTest");
    invoke();
  }
}
