/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 6991563942877137460L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_managedbean299_web/resource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

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
  public void postConstructTest() throws Fault {
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
  public void applicationCDIManagedBeanTest() throws Fault {
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
  public void providerCDIManagedBeanTest() throws Fault {
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
  public void jaxrsInjectPriorPostConstructOnRootResourceTest() throws Fault {
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
  public void jaxrsInjectPriorPostConstructOnApplicationTest() throws Fault {
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
  public void jaxrsInjectPriorPostConstructOnProviderTest() throws Fault {
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
  public void noInjectOrResourceKeywordTest() throws Fault {
    String req = buildRequest(Request.GET, "nokeyword;matrix=",
        String.valueOf(serialVersionUID));
    setProperty(Property.REQUEST, req);
    setProperty(Property.SEARCH_STRING, String.valueOf(serialVersionUID));
    invoke();
  }
}
