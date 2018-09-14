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
package com.sun.ts.tests.websocket.api.javax.websocket.decodeexception;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Properties;
import javax.websocket.DecodeException;

public class WSClient extends ServiceEETest {
  // properties read from ts.jte file

  int ws_wait;

  String webServerHost;

  String webServerPort;

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
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException rlex) {
      System.err.println("Failed to initialize logging.");
    }

    webServerHost = p.getProperty("webServerHost").trim();
    webServerPort = p.getProperty("webServerPort").trim();
    ws_wait = Integer.parseInt(p.getProperty("ws_wait"));

    // check props for errors
    if (ws_wait < 1) {
      throw new Fault("'ws_wait' (milliseconds) in ts.jte must be > 0");
    }
    if (webServerHost == null) {
      throw new Fault("'webServerHost' in ts.jte must not be null ");
    }
    if (webServerPort == null) {
      throw new Fault("'webServerPort' in ts.jte must not be null ");
    }
  }

  /* Run test */
  /*
   * @testName: constructorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:32; WebSocket:JAVADOC:33;
   * WebSocket:JAVADOC:34;
   *
   * @test_Strategy: Test constructor DecodeException(String, String)
   */
  public void constructorTest() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot decode the message";
    String encoded_message = "xyz for now";
    StringBuilder tmp = new StringBuilder();

    DecodeException dex = new DecodeException(encoded_message, reason);

    if (!encoded_message.equals(dex.getText())) {
      passed = false;
      tmp.append("Expected message ").append(encoded_message)
          .append(", returned").append(dex.getText());
    }

    if (dex.getBytes() != null) {
      passed = false;
      tmp.append("Expected ByteBuffer  to be null, returned")
          .append(dex.getBytes());
    }

    if (passed == false) {
      throw new Fault("Test failed: " + tmp.toString());
    }
  }

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:31; WebSocket:JAVADOC:33;
   * WebSocket:JAVADOC:34;
   *
   * @test_Strategy: Test constructor DecodeException(ByteBuffer, String)
   */
  public void constructorTest1() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot decode the message";
    ByteBuffer encoded_message = ByteBuffer.allocate(20);
    encoded_message.put("xyz for now".getBytes());

    DecodeException dex = new DecodeException(encoded_message, reason);

    if (dex.getText() != null) {
      passed = false;
      TestUtil
          .logErr("Expected encoded_message null, returned" + dex.getText());
    }

    if (!encoded_message.equals(dex.getBytes())) {
      passed = false;
      TestUtil.logErr("Expected ByteBuffer " + encoded_message + ", returned"
          + dex.getBytes());
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: constructorTest2
   * 
   * @assertion_ids: WebSocket:JAVADOC:30; WebSocket:JAVADOC:33;
   * WebSocket:JAVADOC:34;
   *
   * @test_Strategy: Test constructor DecodeException(String, String, Throwable)
   */
  public void constructorTest2() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot decode the message";
    String encoded_message = "xyz for now";

    DecodeException dex = new DecodeException(encoded_message, reason,
        new Throwable("CocntructorTest2"));

    if (!encoded_message.equals(dex.getText())) {
      passed = false;
      TestUtil.logErr("Expected encoded_message " + encoded_message
          + ", returned" + dex.getText());
    }

    if (dex.getBytes() != null) {
      passed = false;
      TestUtil.logErr("Expected ByteBuffer null, returned" + dex.getBytes());
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: constructorTest3
   * 
   * @assertion_ids: WebSocket:JAVADOC:29; WebSocket:JAVADOC:33;
   * WebSocket:JAVADOC:34;
   *
   * @test_Strategy: Test constructor DecodeException(ByteBuffer, String,
   * Throwable)
   */
  public void constructorTest3() throws Fault {
    boolean passed = true;
    String reason = "TCK: Cannot decode the message";
    ByteBuffer encoded_message = ByteBuffer.allocate(20);
    encoded_message.put("xyz for now".getBytes());

    DecodeException dex = new DecodeException(encoded_message, reason,
        new Throwable("constructorTest3"));

    if (dex.getText() != null) {
      passed = false;
      TestUtil
          .logErr("Expected encoded_message null, returned" + dex.getText());
    }

    if (!encoded_message.equals(dex.getBytes())) {
      passed = false;
      TestUtil.logErr("Expected ByteBuffer " + encoded_message + ", returned"
          + dex.getBytes());
    }

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  public void cleanup() {
  }
}
