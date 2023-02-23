/*
 * Copyright (c) 2010, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.managedbean;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import com.sun.ts.tests.jaxrs.common.provider.PrintingErrorHandler;

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

  private static final long serialVersionUID = -4731342614849053652L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_platform_managedbean_web/managedbean");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    InputStream inStream = JAXRSClientIT.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/platform/managedbean/web.xml.template");
    // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
    String webXml = editWebXmlString(inStream);

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_managedbean_web.war");
    archive.addClasses(
      TSAppConfig.class, 
      ApplicationHolderSingleton.class, 
      InterceptorSingleton.class,
      ManagedBeanRootResource.class,
      StringBuilderProvider.class,
      PrintingErrorHandler.class
      );

    //archive.addAsWebInfResource(new StringAsset(beansXml), "beans.xml");
    archive.setWebXML(new StringAsset(webXml));
    //archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "web.xml.template", "web.xml"); //can use if the web.xml.template doesn't need to be modified.    
    
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
   * @testName: postConstructOnResourceTest
   * 
   * @assertion_ids: JAXRS:SPEC:49;
   * 
   * @test_Strategy: In a product that also supports Managed Beans,
   * implementations MUST support use of Managed Beans as root resource classes
   * 
   * check postconstruct has been called (managed been property)
   */
  @Test
  public void postConstructOnResourceTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "resourcevalue"));
    setProperty(SEARCH_STRING, "1000");
    invoke();
  }

  /*
   * @testName: postConstructOnProviderTest
   * 
   * @assertion_ids: JAXRS:SPEC:49;
   * 
   * @test_Strategy: In a product that also supports Managed Beans,
   * implementations MUST support use of Managed Beans as provider subclasses
   * 
   * check postconstruct has been called (managed been property)
   */
  @Test
  public void postConstructOnProviderTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "providervalue"));
    setProperty(SEARCH_STRING, "1000");
    invoke();
  }

  /*
   * @testName: postConstructOnApplicationTest
   * 
   * @assertion_ids: JAXRS:SPEC:49;
   * 
   * @test_Strategy: In a product that also supports Managed Beans,
   * implementations MUST support use of Managed Beans as Application subclasses
   * 
   * check postconstruct has been called (managed been property)
   */
  @Test
  public void postConstructOnApplicationTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "applicationvalue"));
    setProperty(SEARCH_STRING, "100");
    invoke();
  }

  /*
   * @testName: interceptorOnResourceTest
   * 
   * @assertion_ids: JAXRS:SPEC:49;
   * 
   * @test_Strategy: In a product that also supports Managed Beans,
   * implementations MUST support use of Managed Beans as provider subclasses
   * 
   * increase value on stringbuilderprovider by writing then checked it was
   * intercepted (managed bean property)
   */
  @Test
  public void interceptorOnResourceTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "interceptedresourcevalue"));
    setProperty(SEARCH_STRING, "10005");
    invoke();
  }

  /*
   * @testName: rootResourceManagedBeanJndiLookupTest
   * 
   * @assertion_ids: JAXRS:SPEC:49;
   * 
   * @test_Strategy: In a product that also supports Managed Beans,
   * implementations MUST support use of Managed Beans as root resource classes
   * 
   * try JNDI lookup
   */
  @Test
  public void rootResourceManagedBeanJndiLookupTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "lookup"));
    setProperty(SEARCH_STRING, "1000");
    invoke();
  }

  /*
   * @testName: injectPriorPostConstructOnRootResourceTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * Managed Beans as resource classes:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void injectPriorPostConstructOnRootResourceTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorroot"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }

  /*
   * @testName: injectPriorPostConstructOnApplicationTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * Managed Beans as Application subclasses:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void injectPriorPostConstructOnApplicationTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorapp"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }

  /*
   * @testName: injectPriorPostConstructOnProviderTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1;
   * 
   * @test_Strategy: The following additional requirements apply when using
   * Managed Beans as providers:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   */
  @Test
  public void injectPriorPostConstructOnProviderTest() throws Exception {
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
   * Managed Beans as resource classes:
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
