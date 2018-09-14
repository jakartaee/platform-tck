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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.basic;

import com.sun.ts.tests.websocket.common.client.BinaryAndTextClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.CoderSuperClass;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCBinaryClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCBinaryStreamClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCSuperEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCTextClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCTextStreamClientEndpoint;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = 7637718042723179933L;

  public WSClient() {
    setContextRoot("wsc_ee_javax_websocket_remoteendpoint_usercoder_basic_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: sendObjectBooleanTextEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectBooleanTextEncoderOnServerTest() throws Fault {
    invoke("text", OPS.BOOL, CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectCharTextEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectCharTextEncoderOnServerTest() throws Fault {
    invoke("text", OPS.CHAR, CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectNumberTextEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectNumberTextEncoderOnServerTest() throws Fault {
    invoke("text", OPS.BYTE, CoderSuperClass.COMMON_CODED_STRING);
    invoke("text", OPS.SHORT, CoderSuperClass.COMMON_CODED_STRING);
    invoke("text", OPS.INT, CoderSuperClass.COMMON_CODED_STRING);
    invoke("text", OPS.LONG, CoderSuperClass.COMMON_CODED_STRING);
    invoke("text", OPS.FLOAT, CoderSuperClass.COMMON_CODED_STRING);
    invoke("text", OPS.DOUBLE, CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectBooleanTextEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectBooleanTextEncoderOnClientTest() throws Fault {
    WSCTextClientEndpoint client = new WSCTextClientEndpoint();
    setAnnotatedClientEndpoint(client);
    invoke("echo", Boolean.valueOf(WSCSuperEndpoint.BOOL),
        CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectCharTextEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectCharTextEncoderOnClientTest() throws Fault {
    WSCTextClientEndpoint client = new WSCTextClientEndpoint();
    setAnnotatedClientEndpoint(client);
    invoke("echo", Character.valueOf(WSCSuperEndpoint.CHAR),
        CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectNumberTextEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectNumberTextEncoderOnClientTest() throws Fault {
    WSCTextClientEndpoint client = new WSCTextClientEndpoint();
    setAnnotatedClientEndpoint(client);
    invoke("echo", WSCSuperEndpoint.NUMERIC,
        CoderSuperClass.COMMON_CODED_STRING);

    setAnnotatedClientEndpoint(client);
    invoke("echo", Short.valueOf(WSCSuperEndpoint.NUMERIC.shortValue()),
        CoderSuperClass.COMMON_CODED_STRING);

    setAnnotatedClientEndpoint(client);
    invoke("echo", WSCSuperEndpoint.NUMERIC.intValue(),
        CoderSuperClass.COMMON_CODED_STRING);

    setAnnotatedClientEndpoint(client);
    invoke("echo", WSCSuperEndpoint.NUMERIC.longValue(),
        CoderSuperClass.COMMON_CODED_STRING);

    setAnnotatedClientEndpoint(client);
    invoke("echo", WSCSuperEndpoint.NUMERIC.doubleValue(),
        CoderSuperClass.COMMON_CODED_STRING);

    setAnnotatedClientEndpoint(client);
    invoke("echo", WSCSuperEndpoint.NUMERIC.floatValue(),
        CoderSuperClass.COMMON_CODED_STRING);
  }

  // ----------------------------------------------------------------------
  /*
   * @testName: sendObjectPrimitivesTextStreamEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:63;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.TextStream.encode
   */
  public void sendObjectPrimitivesTextStreamEncoderOnServerTest() throws Fault {
    for (OPS op : OPS.values())
      invoke("textstream", op, CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectPrimitivesTextStreamEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:63;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.TextStream.encode
   */
  public void sendObjectPrimitivesTextStreamEncoderOnClientTest() throws Fault {
    WSCTextStreamClientEndpoint client = new WSCTextStreamClientEndpoint();

    for (Object entity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:56;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Binary.encode
   */
  public void sendObjectPrimitivesBinaryEncoderOnServerTest() throws Fault {
    for (OPS op : OPS.values()) {
      setClientEndpoint(BinaryAndTextClientEndpoint.class);
      invoke("binary", op, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:56;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Binary.encode
   */
  public void sendObjectPrimitivesBinaryEncoderOnClientTest() throws Fault {
    WSCBinaryClientEndpoint client = new WSCBinaryClientEndpoint();

    for (Object entity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryStreamEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:58;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.BinaryStream.encode
   */
  public void sendObjectPrimitivesBinaryStreamEncoderOnServerTest()
      throws Fault {
    for (OPS op : OPS.values()) {
      setClientEndpoint(BinaryAndTextClientEndpoint.class);
      invoke("binarystream", op, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryStreamEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:58;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.BinaryStream.encode
   */
  public void sendObjectPrimitivesBinaryStreamEncoderOnClientTest()
      throws Fault {
    WSCBinaryStreamClientEndpoint client = new WSCBinaryStreamClientEndpoint();

    for (Object entity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }
}
