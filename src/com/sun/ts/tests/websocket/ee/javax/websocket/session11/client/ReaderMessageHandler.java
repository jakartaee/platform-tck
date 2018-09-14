/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.client;

import java.io.IOException;
import java.io.Reader;

import javax.websocket.MessageHandler;

import com.sun.ts.tests.websocket.common.client.ClientEndpoint;
import com.sun.ts.tests.websocket.common.util.IOUtil;

public class ReaderMessageHandler implements MessageHandler.Whole<Reader> {
  ClientEndpoint<String> endpoint;

  public static final String HANDLER_SAYS = "ReaderMessageHandler says: ";

  public ReaderMessageHandler(ClientEndpoint<String> endpoint) {
    this.endpoint = endpoint;
  }

  @Override
  public void onMessage(Reader message) {
    String msg = null;
    try {
      msg = IOUtil.readFromReader(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    endpoint.onMessage(HANDLER_SAYS + msg);
  }

}
