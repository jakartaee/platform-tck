/*
 * Copyright (c) 2012, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.managedbean299;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
@ExtendWith(ArquillianExtension.class)
public class JAXRSClientIT extends JAXRSCommonClient {

  private static final long serialVersionUID = 6991563942877137460L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_platform_managedbean299_web/resource");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_managedbean299_web.war");
    InputStream inStream = JAXRSClientIT.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/platform/managedbean299/beans.xml");
    String beansXml = toString(inStream);
    
    archive.addClasses(Resource.class, TSAppConfig.class,
    ApplicationHolderSingleton.class, StringBuilderProvider.class);

    archive.addAsWebInfResource(new StringAsset(beansXml), "beans.xml");
    archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "jaxrs_platform_managedbean299_web.xml", "web.xml"); //can use if the web.xml.template doesn't need to be modified.    
    return archive;

  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  // public static void main(String[] args) {
  //   new JAXRSClient().run(args);
  // }

  /* Run test */
  /*
   * @testName: postConstructTest
   * 
   * @assertion_ids: JAXRS:SPEC:50;
   * 
   * @test_Strategy: In a product that also supports JSR 299, implementations
   * MUST similarly support use of JSR299-style managed beans
   * 
   * check root resource class is CDI managed bean
   */
  @Test
  public void postConstructTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "root"));
    setProperty(SEARCH_STRING, "999");
    invoke();
  }

  /*
   * @testName: applicationCDIManagedBeanTest
   * 
   * @assertion_ids: JAXRS:SPEC:50;
   * 
   * @test_Strategy: In a product that also supports JSR 299, implementations
   * MUST similarly support use of JSR299-style managed beans Application
   * subclasses MUST be singletons or use application scope
   * 
   * check application subclass is CDI managed bean
   */
  @Test
  public void applicationCDIManagedBeanTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "app"));
    setProperty(SEARCH_STRING, "1000");
    invoke();
  }

  /*
   * @testName: providerCDIManagedBeanTest
   * 
   * @assertion_ids: JAXRS:SPEC:50;
   * 
   * @test_Strategy: In a product that also supports JSR 299, implementations
   * MUST similarly support use of JSR299-style managed beans Providers MUST be
   * singletons or use application scope
   * 
   * check Provider subclass is CDI managed bean
   */
  @Test
  public void providerCDIManagedBeanTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "provider"));
    setProperty(SEARCH_STRING, "1001");
    invoke();
  }

  /*
   * @testName: jaxrsInjectPriorPostConstructOnRootResourceTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * JSR299-style Managed Beans as resource classes:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void jaxrsInjectPriorPostConstructOnRootResourceTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorroot"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }

  /*
   * @testName: jaxrsInjectPriorPostConstructOnApplicationTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * JSR299-style Managed Beans as Application subclasses:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void jaxrsInjectPriorPostConstructOnApplicationTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorapp"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }

  /*
   * @testName: jaxrsInjectPriorPostConstructOnProviderTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * JSR299-style Managed Beans as providers:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void jaxrsInjectPriorPostConstructOnProviderTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorprovider"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }

  /*
   * @testName: noInjectOrResourceKeywordTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.3;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * JSR299-style Managed Beans as resource classes:
   * 
   * Implementations MUST NOT require use of @Inject or
   * 
   * @Resource to trigger injection of JAX-RS annotated fields or properties.
   * Implementations MAY support such usage but SHOULD warn users about
   * non-portability.
   */
  @Test
  public void noInjectOrResourceKeywordTest() throws Exception {
    String req = buildRequest(Request.GET, "nokeyword;matrix=",
        String.valueOf(serialVersionUID));
    setProperty(Property.REQUEST, req);
    setProperty(Property.SEARCH_STRING, String.valueOf(serialVersionUID));
    invoke();
  }
}
