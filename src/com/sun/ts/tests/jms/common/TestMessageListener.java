/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)TestMessageListener.java	1.11 03/05/16
 */
package com.sun.ts.tests.jms.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;

/**
 * Message Listener implementation for JMS testing
 */
public class TestMessageListener implements javax.jms.MessageListener {
  public MessageConsumer mConsumer;

  public DoneLatch monitor;

  public ArrayList messageArray = new ArrayList();

  /**
   * Constructor takes a MessageConsumer argument
   * 
   * @param MessageConsumer
   */
  public TestMessageListener(MessageConsumer mc, DoneLatch dl) {
    mConsumer = mc;
    monitor = dl;
  }

  /**
   * Returns the list of messages received.
   * 
   * @return ArrayList the list of Messages that have been received
   */
  public ArrayList getMessageArray() {
    return messageArray;
  }

  /**
   * Clears the list of messages received.
   * 
   */
  public DoneLatch getLatch() {
    return monitor;
  }

  /**
   * Clears the list of messages received.
   * 
   */
  public void clearMessageArray() {
    messageArray.clear();
  }

  /**
   * Responds to incoming Messages. A TextMessage is the end of stream signal.
   * 
   * @param Message
   *          the message passed to the listener
   */
  public void onMessage(Message message) {
    try {
      TestUtil.logTrace("MessageListener for " + mConsumer.toString()
          + " received message: " + message.toString());
      messageArray.add(message);
    } catch (Exception e) {
      TestUtil.logErr("Error in MessageListener: " + e.toString(), e);
    }
    if (message instanceof TextMessage) {
      monitor.allDone();
    }
  }
}
