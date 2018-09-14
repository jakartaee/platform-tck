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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverapplicationconfig;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = -6963654147324631018L;

  private final static String CONTENT = WSClient.class.getName();

  public WSClient() {
    setContextRoot("ws_ee_javax_websocket_server_appconfig_web");

  }

  public static void main(String[] args) {
    new WSClient().run(args);
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
    setProperty(Property.REQUEST, buildRequest("used"));
    setProperty(Property.SEARCH_STRING, CONTENT);
    setProperty(Property.CONTENT, CONTENT);
    invoke();
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
    setProperty(Property.REQUEST, buildRequest("unused"));
    setProperty(Property.CONTENT, CONTENT);
    invokeFail();
    String message = getResponseAsString();
    assertTrue(isNullOrEmpty(message),
        "The unused server endpoint should not be registered");
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
    setProperty(Property.REQUEST, buildRequest("other"));
    setProperty(Property.SEARCH_STRING, CONTENT);
    setProperty(Property.CONTENT, CONTENT);
    invoke();
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
    setProperty(Property.REQUEST, buildRequest("configured"));
    setProperty(Property.SEARCH_STRING, CONTENT);
    setProperty(Property.CONTENT, CONTENT);
    invoke();
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
    setProperty(Property.REQUEST, buildRequest("unusedconfig"));
    setProperty(Property.CONTENT, CONTENT);
    invokeFail();
    String message = getResponseAsString();
    assertTrue(isNullOrEmpty(message),
        "The unused server endpoint config should not register an endpoint");
  }

  ///////////////////////////////////////////////////////////////////////
  // Chances are that an exception is thrown when 404
  private void invokeFail() {
    try {
      invoke(false);
    } catch (Fault e) {
      e.printStackTrace();
    }
  }
}
