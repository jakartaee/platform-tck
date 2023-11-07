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

package com.sun.ts.tests.jsp.api.jakarta_el.listelresolver;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.common.el.api.resolver.BarELResolver;

import java.io.IOException;

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
    setup();
    setContextRoot("/jsp_listelresolver_web");
    setTestJsp("ListELResolverTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_listelresolver_web.war");
    archive.addClasses(ListELResolverTag.class,
            JspTestUtil.class,
            ResolverTest.class, 
            BarELResolver.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_listelresolver_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/listelresolver.tld", "listelresolver.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ListELResolverTest.jsp")), "ListELResolverTest.jsp");

    return archive;

  }
  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: listElResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:68; EL:JAVADOC:69; EL:JAVADOC:70; EL:JAVADOC:71;
   * EL:JAVADOC:72; EL:JAVADOC:73
   * 
   * @test_Strategy: Obtain an ListELResolver via the PageContext and verify
   * that API calls work as expected: setValue() getValue() getType()
   * isReadOnly() getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void listElResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "listElResolverTest");
    invoke();
  }
}
