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

package com.sun.ts.tests.websocket.ee.javax.websocket.programaticcoder;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.util.IOUtil;

public class WSCBinaryDecoderServer extends Endpoint
    implements MessageHandler.Whole<StringBean> {

  private Session session;

  @Override
  public void onMessage(StringBean bean) {
    try {
      session.getBasicRemote().sendText(bean.get());
    } catch (IOException e) {
      onError(session, e);
    }
  }

  @Override
  public void onError(Session session, Throwable t) {
    System.out.println("@OnError in" + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = IOUtil.printStackTrace(t);
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.session.addMessageHandler(this);
  }

}
