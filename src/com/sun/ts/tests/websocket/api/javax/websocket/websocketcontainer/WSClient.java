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
package com.sun.ts.tests.websocket.api.javax.websocket.websocketcontainer;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.websocket.ContainerProvider;
import javax.websocket.Extension;
import javax.websocket.Extension.Parameter;
import javax.websocket.WebSocketContainer;

public class WSClient extends ServiceEETest {

  public static void main(String[] args) {
    WSClient theTests = new WSClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  /* Run test */
  /*
   * @testName: getMaxSessionIdleTimeoutTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:167;
   *
   * @test_Strategy: Test method getMaxSessionIdleTimeout
   */
  public void getMaxSessionIdleTimeoutTest() throws Fault {
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();

    if (client.getDefaultMaxSessionIdleTimeout() != 0L) {
      TestUtil.logTrace(
          "Default timeout is: " + client.getDefaultMaxSessionIdleTimeout());
    }
  }

  /*
   * @testName: setMaxSessionIdleTimeoutTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:167;
   * WebSocket:JAVADOC:172;
   *
   * @test_Strategy: Test method setMaxSessionIdleTimeout
   */
  public void setMaxSessionIdleTimeoutTest() throws Fault {
    long timeout = 987654321L;
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    client.setDefaultMaxSessionIdleTimeout(timeout);

    if (client.getDefaultMaxSessionIdleTimeout() != timeout) {
      throw new Fault(
          "Test failed. getMaxSessionIdleTimeout didn't return set value."
              + "Expecting " + timeout + "; got "
              + client.getDefaultMaxSessionIdleTimeout());
    }
  }

  /*
   * @testName: getMaxTextMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:168;
   *
   * @test_Strategy: Test method getMaxTextMessageBufferSize
   */
  public void getMaxTextMessageBufferSizeTest() throws Fault {
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    long default_msgsize = client.getDefaultMaxTextMessageBufferSize();
    System.out
        .println("Default MaxTextMessageBufferSize is " + default_msgsize);
  }

  /*
   * @testName: setMaxTextMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:168;
   * WebSocket:JAVADOC:173;
   *
   * @test_Strategy:
   */
  public void setMaxTextMessageBufferSizeTest() throws Fault {
    int expected_msgsize = 987654321;

    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    long default_msgsize = client.getDefaultMaxTextMessageBufferSize();
    System.out
        .println("Default MaxTextMessageBufferSize is " + default_msgsize);

    client.setDefaultMaxTextMessageBufferSize(expected_msgsize);
    long actual_msgsize = client.getDefaultMaxTextMessageBufferSize();
    if (expected_msgsize != actual_msgsize) {
      throw new Fault("DefaultMaxTextMessageBufferSize does not match. "
          + "Expecting " + expected_msgsize + ", got " + actual_msgsize);
    }
  }

  /*
   * @testName: getMaxBinaryMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:166;
   *
   * @test_Strategy:
   */
  public void getMaxBinaryMessageBufferSizeTest() throws Fault {
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    long default_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
    System.out
        .println("Default MaxTextMessageBufferSize is " + default_msgsize);
  }

  /*
   * @testName: setMaxBinaryMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:166;
   * WebSocket:JAVADOC:171;
   *
   * @test_Strategy:
   */
  public void setMaxBinaryMessageBufferSizeTest() throws Fault {
    int expected_msgsize = 987654321;

    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    long default_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
    System.out
        .println("Default MaxBinaryMessageBufferSize is " + default_msgsize);

    client.setDefaultMaxBinaryMessageBufferSize(expected_msgsize);
    long actual_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
    if (expected_msgsize != actual_msgsize) {
      throw new Fault("DefaultMaxBinaryMessageBufferSize does not match. "
          + "Expecting " + expected_msgsize + ", got " + actual_msgsize);
    }
  }

  /*
   * @testName: getDefaultAsyncSendTimeoutTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:165;
   * 
   * @test_Strategy: Test method getMaxSessionIdleTimeout
   */
  public void getDefaultAsyncSendTimeoutTest() throws Fault {
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();

    if (client.getDefaultAsyncSendTimeout() != 0L) {
      TestUtil.logTrace(
          "Default timeout is: " + client.getDefaultMaxSessionIdleTimeout());
    }
  }

  /*
   * @testName: setAsyncSendTimeoutTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:165;
   * WebSocket:JAVADOC:170;
   *
   * @test_Strategy:
   */
  public void setAsyncSendTimeoutTest() throws Fault {
    long expected_timeout = 987654321L;
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();
    client.setAsyncSendTimeout(expected_timeout);

    long actual_timeout = client.getDefaultAsyncSendTimeout();
    if (actual_timeout != expected_timeout) {
      throw new Fault(
          "Test failed. getDefaultAsyncSendTimeout didn't return set value."
              + "Expecting " + expected_timeout + "; got " + actual_timeout);
    }
  }

  /*
   * @testName: getInstalledExtensionsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:169;
   *
   * @test_Strategy:
   */
  public void getInstalledExtensionsTest() throws Fault {
    WebSocketContainer client = ContainerProvider.getWebSocketContainer();

    Set<Extension> extensions = client.getInstalledExtensions();
    if (extensions != null) {
      if (!extensions.isEmpty()) {
        for (Extension tmp : extensions) {
          System.out.println("Installed Extension: " + tmp.getName());
          List<Parameter> params = tmp.getParameters();
          for (Parameter tmp1 : params) {
            System.out.println("Parameter's name= " + tmp1.getName()
                + "Parameter's value= " + tmp1.getValue());
          }
        }
      } else {
        System.out.println("Installed Extension returned empty set");
      }
    } else {
      System.out.println("getInstalledExtensions() returned null");
    }
  }

  public void cleanup() {
  }
}
