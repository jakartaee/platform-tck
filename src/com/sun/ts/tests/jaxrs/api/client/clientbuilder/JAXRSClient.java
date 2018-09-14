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

package com.sun.ts.tests.jaxrs.api.client.clientbuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 7395392827433641768L;

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
   * @testName: newClientNoParamTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1019;
   * 
   * @test_Strategy: Create new client instance using the default client builder
   * factory provided by the JAX-RS implementation provider.
   */
  public void newClientNoParamTest() throws Fault {
    Client client = ClientBuilder.newClient();
    assertFault(client != null, "could not create Client instance");
  }

  /*
   * @testName: newClientWithConfigurationTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1020;
   * 
   * @test_Strategy: Create new configured client instance using the default
   * client builder factory provided by the JAX-RS implementation provider.
   */
  public void newClientWithConfigurationTest() throws Fault {
    String property = "JAXRSTCK";
    Client client = ClientBuilder.newClient();
    client.property(property, property);
    Configuration config = client.getConfiguration();
    client = ClientBuilder.newClient(config);
    assertNotNull(client, "could not create Client instance");
    assertEquals(property, client.getConfiguration().getProperty(property),
        "client does not contain given config");
  }

}
