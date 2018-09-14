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

/*
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.javax.websocket.deploymentException;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import java.io.PrintWriter;
import java.util.Properties;
import javax.websocket.DeploymentException;

public class WSClient extends ServiceEETest {

  public static void main(String[] args) {
    WSClient theTests = new WSClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  /* Run test */
  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:49;
   *
   * @test_Strategy: Test constructor DeploymentException(String)
   */
  public void constructorTest() throws Fault {
    String reason = "TCK: testing the DeploymentException(String)";

    DeploymentException dex = new DeploymentException(reason);

  }

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:50;
   *
   * @test_Strategy: Test constructor DeploymentException(String, Throwable)
   */
  public void constructorTest1() throws Fault {
    String reason = "TCK: testing the DeploymentException(String)";

    DeploymentException dex = new DeploymentException(reason,
        new Throwable("TCK_Test"));
  }

  public void cleanup() {
  }
}
