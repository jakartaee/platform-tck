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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpoint;

import static com.sun.ts.tests.websocket.common.util.StringUtil.objectsToString;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.sun.ts.tests.websocket.common.util.IOUtil;

public class WSAbstractServer {

  public String op(String op) {
    if (op.equals("configurator"))
      return getConfigurator();
    if (op.equals("encoders"))
      return getEncoders();
    if (op.equals("decoders"))
      return getDecoders();
    if (op.equals("subprotocols"))
      return getSubprotocols();
    if (op.equals("value"))
      return getValue();
    return null;
  }

  protected String getConfigurator() {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    Class<? extends Configurator> config = endpoint.configurator();
    return config.getName();
  }

  protected String getEncoders() {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    Class<? extends Encoder>[] encoders = endpoint.encoders();
    String encs = objectsToString((Object[]) encoders);
    return encs;
  }

  protected String getDecoders() {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    Class<? extends Decoder>[] decoders = endpoint.decoders();
    String decs = objectsToString((Object[]) decoders);
    return decs;
  }

  protected String getSubprotocols() {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    String[] subprotocols = endpoint.subprotocols();
    String subs = objectsToString((Object[]) subprotocols);
    return subs;
  }

  public String getValue() {
    Annotation ann = getClass().getAnnotations()[0];
    ServerEndpoint endpoint = (ServerEndpoint) ann;
    return endpoint.value();
  }

  public void onError(Session session, Throwable t) throws IOException {
    System.out.println("@OnError in " + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }

}
