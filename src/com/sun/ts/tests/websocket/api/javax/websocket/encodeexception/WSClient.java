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
package com.sun.ts.tests.websocket.api.javax.websocket.encodeexception;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Properties;
import javax.websocket.EncodeException;

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
   * @assertion_ids: WebSocket:JAVADOC:51; WebSocket:JAVADOC:53;
   *
   * @test_Strategy: Test constructor EncodeException(Object, String)
   */
  public void constructorTest() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot encode the message";
    String encoded_message = "xyz for now";

    EncodeException eex = new EncodeException(encoded_message, reason);

    if (!encoded_message.equals(eex.getObject())) {
      passed = false;
      TestUtil.logErr("Expected message " + encoded_message + ", returned"
          + eex.getObject());
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:52; WebSocket:JAVADOC:53;
   *
   * @test_Strategy: Test constructor EncodeException(Object, String, Throwable)
   */
  public void constructorTest1() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot decode the message";
    ByteBuffer encoded_message = ByteBuffer.allocate(20);
    encoded_message.put("xyz for now".getBytes());

    EncodeException eex = new EncodeException(encoded_message, reason,
        new Throwable("TCK Cannot encode"));

    if (!encoded_message.equals(eex.getObject())) {
      passed = false;
      TestUtil.logErr("Expected message " + encoded_message + ", returned"
          + eex.getObject());
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  public void cleanup() {
  }
}
