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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpointreturntype;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.AnnotatedClientEndpoint;
import com.sun.ts.tests.websocket.common.client.ByteBufferClientEndpoint;
import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 3375865828117749296L;

  public WSClient() {
    setContextRoot("wsc_ee_clientendpointreturntype_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  // TEXT ------------------------------------------

  /*
   * @testName: dataTypesTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test primitive and boxed datatypes
   */
  public void dataTypesTest() throws Fault {
    invokeSequence("true", new WSCPrimitiveBooleanClientEndpoint(),
        new WSCFullBooleanClientEndpoint());
    invokeSequence("123", new WSCPrimitiveByteClientEndpoint(),
        new WSCFullByteClientEndpoint());
    invokeSequence(String.valueOf(Short.MAX_VALUE),
        new WSCPrimitiveShortClientEndpoint(),
        new WSCFullShortClientEndpoint());
    invokeSequence(String.valueOf(Short.MIN_VALUE),
        new WSCPrimitiveIntClientEndpoint(), new WSCFullIntClientEndpoint());
    invokeSequence(String.valueOf(Short.MIN_VALUE),
        new WSCPrimitiveLongClientEndpoint(), new WSCFullLongClientEndpoint());
    invokeSequence(String.valueOf(123.456f),
        new WSCPrimitiveFloatClientEndpoint(),
        new WSCFullFloatClientEndpoint());
    invokeSequence(String.valueOf(789.012),
        new WSCPrimitiveDoubleClientEndpoint(),
        new WSCFullDoubleClientEndpoint());
    invokeSequence(String.valueOf('A'), new WSCPrimitiveCharClientEndpoint(),
        new WSCFullCharClientEndpoint());
  }

  /*
   * @testName: textEncoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test text encoder
   */
  public void textEncoderTest() throws Fault {
    invokeSequence("textEncoderTest", new WSCTextEncoderClientEndpoint());
  }

  /*
   * @testName: textStreamEncoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test text stream encoder
   */
  public void textStreamEncoderTest() throws Fault {
    invokeSequence("textStreamEncoderTest",
        new WSCTextStreamEncoderClientEndpoint());
  }

  // -----------------Binary --------------------------------

  /*
   * @testName: binaryEncoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test binary encoder
   */
  public void binaryEncoderTest() throws Fault {
    setClientEndpoint(ByteBufferClientEndpoint.class);
    invokeSequence("binaryEncoderTest", new WSCBinaryEncoderClientEndpoint());
  }

  /*
   * @testName: binaryStreamEncoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test binary stream encoder
   */
  public void binaryStreamEncoderTest() throws Fault {
    setClientEndpoint(ByteBufferClientEndpoint.class);
    invokeSequence("binaryStreamEncoderTest",
        new WSCBinaryStreamEncoderClientEndpoint());
  }

  /*
   * @testName: byteArrayTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test byte array
   */
  public void byteArrayTest() throws Fault {
    setClientEndpoint(ByteBufferClientEndpoint.class);
    invokeSequence("byteArrayTest", new WSCByteArrayClientEndpoint());
  }

  /*
   * @testName: byteBufferTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: test byte array
   */
  public void byteBufferTest() throws Fault {
    setClientEndpoint(ByteBufferClientEndpoint.class);
    invokeSequence("byteBufferTest", new WSCByteBufferClientEndpoint());
  }

  // Private -----------------------------------------
  private void invokeSequence(String search,
      AnnotatedClientEndpoint<?>... endpoints) throws Fault {
    for (AnnotatedClientEndpoint<?> endpoint : endpoints) {
      setCountDownLatchCount(3);
      setAnnotatedClientEndpoint(endpoint);
      addClientCallback(new EndpointCallback() {
        @Override
        public void onClose(Session session, CloseReason closeReason) {
          getCountDownLatch().countDown();
          super.onClose(session, closeReason);
        }
      });
      invoke("srv", search, search);
    }
  }

}
