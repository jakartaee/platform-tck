/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.multiplepaths;

import com.sun.ts.tests.websocket.negdep.NegativeDeploymentClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 *                     tslib.name;
 */
/**
 * @since 1.11
 */
public class WSCClient extends NegativeDeploymentClient {

  private static final long serialVersionUID = 111L;

  public WSCClient() {
    setContextRoot("wsc_negdep_multiplepaths_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /*
   * @testName: twoEndpointsWithTheSamePathThirdEndpointTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3; WebSocket:SPEC:WSC-4.1.1-4;
   * 
   * @test_Strategy: In both cases, a deployment error raised during the
   * deployment process must halt the deployment of the application, any well
   * formed endpoints deployed prior to the error being raised must be removed
   * from service and no more websocket endpoints from that application may be
   * deployed by the container, even if they are valid.
   * 
   * The websocket implementation must reject such an application at deployment
   * time with an informative error message that there is a duplicate path that
   * it cannot resolve. [WSC-4.1.1-4]
   * 
   * To verify the test fails when the deployment was successful, change path in
   * SamePathServerEndpoint2
   */
  public void twoEndpointsWithTheSamePathThirdEndpointTest() throws Fault {
    setProperty(Property.CONTENT, "anything");
    setProperty(Property.REQUEST, buildRequest("echo"));
    setProperty(Property.STATUS_CODE, "-1");
    // logExceptionOnInvocation(false);
    try {
      invoke(false);
    } catch (Fault tfe) {
      // DeploymentException
    }
    String response = getResponseAsString();
    if ("anything".equals(response))
      throwValidEndpointMustBeRemoved();
  }

  /*
   * @testName: twoEndpointsWithTheSamePathTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3; WebSocket:SPEC:WSC-4.1.1-4;
   * 
   * @test_Strategy: In both cases, a deployment error raised during the
   * deployment process must halt the deployment of the application, any well
   * formed endpoints deployed prior to the error being raised must be removed
   * from service and no more websocket endpoints from that application may be
   * deployed by the container, even if they are valid.
   * 
   * The websocket implementation must reject such an application at deployment
   * time with an informative error message that there is a duplicate path that
   * it cannot resolve. [WSC-4.1.1-4]
   * 
   * To verify the test fails when the deployment was successful, change path in
   * SamePathServerEndpoint2
   */
  public void twoEndpointsWithTheSamePathTest() throws Fault {
    setProperty(Property.CONTENT, "anything");
    setProperty(Property.REQUEST, buildRequest("path"));
    setProperty(Property.STATUS_CODE, "-1");
    // logExceptionOnInvocation(false);
    try {
      invoke(false);
    } catch (Fault tfe) {
      // DeploymentException
    }
    String response = getResponseAsString();
    if ("anything".equals(response))
      throwValidEndpointMustBeRemoved();
  }
}
