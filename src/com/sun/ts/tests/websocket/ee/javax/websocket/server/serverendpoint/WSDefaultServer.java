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

import java.io.IOException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/default")
public class WSDefaultServer extends WSAbstractServer {

  @OnMessage
  public String operation(String op) {
    return super.op(op);
  }

  @Override
  protected String getConfigurator() {
    String config = super.getConfigurator();
    return config;
  }

  @Override
  protected String getEncoders() {
    String encoders = super.getEncoders();
    return "{" + encoders + "}";
  }

  @Override
  protected String getDecoders() {
    String decoders = super.getDecoders();
    return "{" + decoders + "}";
  }

  @Override
  protected String getSubprotocols() {
    String subprotocols = super.getSubprotocols();
    return "{" + subprotocols + "}";
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    super.onError(session, t);
  }

}
