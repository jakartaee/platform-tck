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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.async;

import com.sun.ts.tests.websocket.common.client.BinaryAndTextClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.CoderSuperClass;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCBinaryClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCBinaryStreamClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCTextClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder.WSCTextStreamClientEndpoint;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = -948555257662550131L;

  public WSClient() {
    setContextRoot("wsc_ee_javax_websocket_remoteendpoint_usercoder_async_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: sendObjectPrimitivesTextEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectPrimitivesTextEncoderOnServerTest() throws Fault {
    for (OPS op : OPS.values())
      invoke("text", op, CoderSuperClass.COMMON_CODED_STRING);
  }

  /*
   * @testName: sendObjectPrimitivesTextEncoderOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:61;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Text.encode
   */
  public void sendObjectPrimitivesTextEncoderOnClientTest() throws Fault {
    WSCTextClientEndpoint client = new WSCTextClientEndpoint();

    for (Object clientEntity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      setEntity(clientEntity);
      setClientCallback(new AsyncEndpointCallback(entity));
      invoke("echo", clientEntity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesTextStreamEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:63;
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
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:63;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.TextStream.encode
   */
  public void sendObjectPrimitivesTextStreamEncoderOnClientTest() throws Fault {
    WSCTextStreamClientEndpoint client = new WSCTextStreamClientEndpoint();

    for (Object clientEntity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      setEntity(clientEntity);
      setClientCallback(new AsyncEndpointCallback(entity));
      invoke("echo", clientEntity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:56;
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
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:56;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.Binary.encode
   */
  public void sendObjectPrimitivesBinaryEncoderOnClientTest() throws Fault {
    WSCBinaryClientEndpoint client = new WSCBinaryClientEndpoint();

    for (Object clientEntity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      setEntity(clientEntity);
      setClientCallback(new AsyncEndpointCallback(entity));
      invoke("echo", clientEntity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }

  /*
   * @testName: sendObjectPrimitivesBinaryStreamEncoderOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:58;
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
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:58;
   * 
   * @test_Strategy: A developer-provided encoder for a Java primitive type
   * overrides the container default encoder. Encoder.BinaryStream.encode
   */
  public void sendObjectPrimitivesBinaryStreamEncoderOnClientTest()
      throws Fault {
    WSCBinaryStreamClientEndpoint client = new WSCBinaryStreamClientEndpoint();

    for (Object clientEntity : OPS.getClientEntities()) {
      setAnnotatedClientEndpoint(client);
      setEntity(clientEntity);
      setClientCallback(new AsyncEndpointCallback(entity));
      invoke("echo", clientEntity, CoderSuperClass.COMMON_CODED_STRING);
    }
  }
}
