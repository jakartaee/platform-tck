/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverapplicationconfiginlib;

import com.sun.ts.tests.websocket.ee.javax.websocket.server.serverapplicationconfig.WSClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSLibClient extends WSClient {

  private static final long serialVersionUID = 1544727768161691523L;

  public WSLibClient() {
    setContextRoot("wsc_ee_server_appconfig_lib_web");

  }

  public static void main(String[] args) {
    new WSLibClient().run(args);
  }

  /* Run test */

  /*
   * @testName: usedServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:182; WebSocket:SPEC:WSC-6.2-1;
   * WebSocket:SPEC:WSC-6.2-2; WebSocket:SPEC:WSC-6.2-5;
   * 
   * @test_Strategy: Test the Endpoint which should be used by
   * ServerApplicationConfig is really used Return a set of annotated endpoint
   * classes that the server container must deploy. The set of classes passed in
   * to this method is the set obtained by scanning the archive containing the
   * implementation of this interface.
   */
  public void usedServerTest() throws Fault {
    super.usedServerTest();
  }

  /*
   * @testName: unusedServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:182; WebSocket:SPEC:WSC-6.2-1;
   * WebSocket:SPEC:WSC-6.2-2; WebSocket:SPEC:WSC-6.2-5;
   * 
   * @test_Strategy: Test the Endpoint which should be NOT used by
   * ServerApplicationConfig is really NOT used
   * 
   * Therefore, this set passed in contains all the annotated endpoint classes
   * in the JAR or WAR file containing the implementation of this interface.
   */
  public void unusedServerTest() throws Fault {
    super.unusedServerTest();
  }

  /*
   * @testName: otherUsedServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:182; WebSocket:SPEC:WSC-6.2-1;
   * WebSocket:SPEC:WSC-6.2-2; WebSocket:SPEC:WSC-6.2-4;
   * WebSocket:SPEC:WSC-6.2-5;
   * 
   * @test_Strategy: Test all
   * ServerApplicationConfig#getAnnotatedEndpointClasses methods are really used
   */
  public void otherUsedServerTest() throws Fault {
    super.otherUsedServerTest();
  }

  /*
   * @testName: configuredServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:183; WebSocket:SPEC:WSC-6.2-1;
   * WebSocket:SPEC:WSC-6.2-2; WebSocket:SPEC:WSC-6.2-4;
   * WebSocket:SPEC:WSC-6.2-5;
   * 
   * @test_Strategy: Test the correct ServerEndpointConfig is used
   * 
   * Return a set of ServerEndpointConfig instances that the server container
   * will use to deploy the programmatic endpoints. The set of Endpoint classes
   * passed in to this method is the set obtained by scanning the archive
   * containing the implementation of this ServerApplication Config. This set
   * passed in may be used the build the set of ServerEndpointConfig instances
   * to return to the container for deployment.
   */
  public void configuredServerTest() throws Fault {
    super.configuredServerTest();
  }

  /*
   * @testName: unusedConfiguredServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:183; WebSocket:SPEC:WSC-6.2-1;
   * WebSocket:SPEC:WSC-6.2-2; WebSocket:SPEC:WSC-6.2-4;
   * WebSocket:SPEC:WSC-6.2-5;
   * 
   * @test_Strategy: Test the incorrect ServerEndpointConfig is NOT used
   */
  public void unusedConfiguredServerTest() throws Fault {
    super.unusedConfiguredServerTest();
  }

}
