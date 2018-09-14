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
package com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests;

import com.sun.ts.lib.util.TestUtil;
import javax.jms.*;
import java.util.ArrayList;

public class MyMessageListener implements MessageListener {

  private String name = null;

  private Message message = null;

  private Connection connection = null;

  private Session session = null;

  private JMSConsumer jmsconsumer = null;

  private MessageConsumer msgconsumer = null;

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

  public MyMessageListener(Connection connection) {
    this.connection = connection;
  }

  public MyMessageListener(Session session) {
    this.session = session;
  }

  public MyMessageListener(JMSConsumer consumer) {
    this.jmsconsumer = consumer;
  }

  public MyMessageListener(MessageConsumer consumer) {
    this.msgconsumer = consumer;
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
        if (tMsg.getText().equals("Call connection close method")) {
          TestUtil.logMsg(
              "Calling Connection.close() MUST throw IllegalStateException");
          if (connection != null)
            connection.close();
        } else if (tMsg.getText().equals("Call connection stop method")) {
          TestUtil.logMsg(
              "Calling Connection.stop() MUST throw IllegalStateException");
          if (connection != null)
            connection.stop();
        } else if (tMsg.getText().equals("Call session close method")) {
          TestUtil.logMsg(
              "Calling Session.close() MUST throw IllegalStateException");
          if (session != null)
            session.close();
        } else if (tMsg.getText().equals("Call MessageConsumer close method")) {
          TestUtil.logMsg("Calling MessageConsumer.close() MUST be allowed");
          if (msgconsumer != null)
            msgconsumer.close();
        }
      } catch (javax.jms.IllegalStateException e) {
        TestUtil.logMsg("onMessage(): Caught expected IllegalStateException");
        gotCorrectException = true;
        gotException = true;
      } catch (Exception e) {
        TestUtil.logErr("onMessage(): Caught unexpected exception: " + e);
        gotCorrectException = false;
        gotException = true;
        exception = e;
      }
      try {
        if (tMsg.getText().equals("Call JMSConsumer close method")) {
          TestUtil.logMsg("Calling JMSConsumer.close() MUST be allowed");
          if (jmsconsumer != null)
            jmsconsumer.close();
        }
      } catch (javax.jms.IllegalStateRuntimeException e) {
        TestUtil.logMsg(
            "onMessage(): Caught expected IllegalStateRuntimeException");
        gotCorrectException = true;
        gotException = true;
      } catch (Exception e) {
        TestUtil.logErr("onMessage(): Caught unexpected exception: " + e);
        gotCorrectException = false;
        gotException = true;
        exception = e;
      }
    }
    complete = true;
    TestUtil.logMsg("onMessage(): Leaving");
  }
}
