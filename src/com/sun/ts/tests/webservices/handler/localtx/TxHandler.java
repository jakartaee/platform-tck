/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handler.localtx;

import javax.xml.rpc.handler.MessageContext;
import com.sun.ts.tests.jaxrpc.common.HandlerBase;

import javax.naming.InitialContext;
import javax.jms.*;

public class TxHandler extends HandlerBase {
  public boolean handleRequest(MessageContext context) {
    boolean result = false;
    try {
      preinvoke();
      System.out.println("<<<<<<<<<<<<<<TxHandler.handleRequest");
      try {
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx
            .lookup("java:comp/env/jms/QueueConnectionFactory");
        Queue queue = (Queue) ctx.lookup("java:comp/env/jms/MY_QUEUE");
        QueueConnection conn = factory.createQueueConnection();
        QueueSession session = conn.createQueueSession(false,
            Session.AUTO_ACKNOWLEDGE);
        QueueSender sender = session.createSender(queue);
        TextMessage msg = session.createTextMessage();

        msg.setText("This is an important message from LocalTxHandler");
        System.out.println("Sending msg: " + msg.getText());
        sender.send(msg);

        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println(">>>>>>>>>>>>>>TxHandler.handleRequest");
      result = super.handleRequest(context);
    } finally {
      postinvoke();
    }
    return result;
  }

  public boolean handleResponse(MessageContext context) {
    boolean result = false;
    try {
      preinvoke();
      System.out.println("<<<<<<<<<<<<<<TxHandler.handleResponse");
      try {
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx
            .lookup("java:comp/env/jms/QueueConnectionFactory");
        Queue queue = (Queue) ctx.lookup("java:comp/env/jms/MY_QUEUE");
        QueueConnection conn = factory.createQueueConnection();
        QueueSession session = conn.createQueueSession(false,
            Session.AUTO_ACKNOWLEDGE);

        QueueReceiver receiver = session.createReceiver(queue);
        receiver.receive(200);

        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println(">>>>>>>>>>>>>>TxHandler.handleResponse");
      result = super.handleResponse(context);
    } finally {
      postinvoke();
    }
    return result;
  }
}
