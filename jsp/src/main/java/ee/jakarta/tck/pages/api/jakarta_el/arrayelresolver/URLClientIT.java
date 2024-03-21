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

package ee.jakarta.tck.pages.api.jakarta_el.arrayelresolver;

import java.io.IOException;

import ee.jakarta.tck.pages.common.client.AbstractUrlClient;
import ee.jakarta.tck.pages.common.util.JspTestUtil; 
import com.sun.ts.tests.common.el.api.resolver.ResolverTest; 
import com.sun.ts.tests.common.el.api.resolver.BarELResolver;

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

    setContextRoot("/jsp_arrayelresolver_web");
    setTestJsp("ArrayELResolverTest");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_arrayelresolver_web.war");
    archive.addClasses(ArrayELResolverTag.class, JspTestUtil.class, 
            ResolverTest.class, 
            BarELResolver.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_arrayelresolver_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/arrayelresolver.tld", "arrayelresolver.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ArrayELResolverTest.jsp")), "ArrayELResolverTest.jsp");

    return archive;

  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: arrayElResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:3; EL:JAVADOC:4; EL:JAVADOC:5; EL:JAVADOC:6;
   * EL:JAVADOC:7; EL:JAVADOC:8
   * 
   * @test_Strategy: Obtain an ArrayELResolver via the PageContext and verify
   * that API calls work as expected: setValue() getValue() getType()
   * isReadOnly() getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  public void arrayElResolverTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "arrayElResolverTest");
    invoke();
  }
}
