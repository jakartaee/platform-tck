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

/*
 * $Id$
 */
package com.sun.ts.tests.jms.core20.appclient.jmscontextqueuetests;

import com.sun.ts.lib.util.TestUtil;
import javax.jms.*;
import java.util.ArrayList;

public class MyMessageListener implements MessageListener {

  private String name = null;

  private Message message = null;

  private JMSContext context = null;

  private ArrayList<Message> messages = new ArrayList<Message>();

  private Exception exception = null;

  private int numMessages = 1;

  boolean complete = false;

  boolean gotCorrectException = false;

  boolean gotException = false;

  public MyMessageListener() {
    this("MyMessageListener");
  }

  public MyMessageListener(String name) {
    this.name = name;
  }

  public MyMessageListener(int numMessages) {
    this.numMessages = numMessages;
    messages.clear();
  }

  public MyMessageListener(JMSContext context) {
    this.context = context;
  }

  // getters/setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Message getMessage() {
    return message;
  }

  public Message getMessage(int index) {
    return messages.get(index);
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public boolean isComplete() {
    return complete;
  }

  public boolean gotCorrectException() {
    return gotCorrectException;
  }

  public boolean gotException() {
    return gotException;
  }

  public boolean gotAllMsgs() {
    return (messages.size() == numMessages) ? true : false;
  }

  public boolean haveMsg(int i) {
    return (messages.size() > i) ? true : false;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  public void onMessage(Message message) {
    try {
      TestUtil.logMsg(
          "onMessage(): Got Message: " + ((TextMessage) message).getText());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
    }
    this.message = message;
    messages.add(message);
    if (message instanceof TextMessage) {
      TextMessage tMsg = (TextMessage) message;
      try {
        if (tMsg.getText().equals("Call close method")) {
          TestUtil.logMsg(
              "Calling JMSContext.close() MUST throw IllegalStateRuntimeException");
          if (context != null)
            context.close();
        } else if (tMsg.getText().equals("Call stop method")) {
          TestUtil.logMsg(
              "Calling JMSContext.stop() MUST throw IllegalStateRuntimeException");
          if (context != null)
            context.stop();
        } else if (tMsg.getText().equals("Call commit method")) {
          TestUtil.logMsg(
              "Calling JMSContext.commit() MUST throw IllegalStateRuntimeException");
          if (context != null)
            context.commit();
        } else if (tMsg.getText().equals("Call rollback method")) {
          TestUtil.logMsg(
              "Calling JMSContext.rollback() MUST throw IllegalStateRuntimeException");
          if (context != null)
            context.rollback();
        }
      } catch (IllegalStateRuntimeException e) {
        TestUtil.logMsg("Caught expected IllegalStateRuntimeException");
        gotCorrectException = true;
        gotException = true;
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        gotCorrectException = false;
        gotException = true;
        exception = e;
      }
    }
    complete = true;
  }
}
