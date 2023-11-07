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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.el.scopedattrelresolver;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.util.JspResolverTest;
import com.sun.ts.tests.common.el.api.resolver.BarELResolver;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;

import java.io.IOException;
import java.io.InputStream;

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
    setContextRoot("/jsp_scopedattrelresolver_web");
    setTestJsp("ScopedAttrELResolverTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_scopedattrelresolver_web.war");
    archive.addClasses(ScopedAttrELResolverTag.class,
            JspTestUtil.class,
            JspResolverTest.class,
            BarELResolver.class,
            ResolverTest.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_scopedattrelresolver_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/scopedattrelresolver.tld", "scopedattrelresolver.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ScopedAttrELResolverTest.jsp")), "ScopedAttrELResolverTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: scopedAttrElResolverTest
   * 
   * @assertion_ids: JSP:JAVADOC:427; JSP:JAVADOC:428; JSP:JAVADOC:429;
   * JSP:JAVADOC:430; JSP:JAVADOC:431; JSP:JAVADOC:433
   * 
   * @test_Strategy: Obtain an ScopedAttributeELResolver via the PageContext and
   * verify that API calls work as expected: setValue() getValue() getType()
   * isReadOnly() getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void scopedAttrElResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "scopedAttrElResolverTest");
    invoke();
  }
}
