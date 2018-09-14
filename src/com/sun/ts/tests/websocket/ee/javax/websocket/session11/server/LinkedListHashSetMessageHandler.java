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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.websocket.MessageHandler;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.ListHashSetTextEncoder;

public class LinkedListHashSetMessageHandler
    implements MessageHandler.Whole<LinkedList<HashSet<String>>> {

  public static final String HANDLER_SAYS = "LinkedListHashSetMessageHandler says: ";

  private Session session;

  public LinkedListHashSetMessageHandler(Session session) {
    super();
    this.session = session;
  }

  @Override
  public void onMessage(LinkedList<HashSet<String>> arg0) {
    try {
      session.getBasicRemote().sendText(
          HANDLER_SAYS + ListHashSetTextEncoder.listHashSetToString(arg0));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
