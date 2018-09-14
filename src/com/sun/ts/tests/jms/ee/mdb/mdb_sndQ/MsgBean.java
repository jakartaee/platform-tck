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
 * $Id$
 */

package com.sun.ts.tests.jms.ee.mdb.mdb_sndQ;

import java.io.Serializable;
import javax.ejb.*;
import javax.naming.*;
import javax.jms.*;
import java.util.Properties;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBean implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into the onMessage method.
  private java.util.Properties p = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private ConnectionFactory cf = null;

  private Destination Dest = null;

  private MessageProducer mSender = null;

  private Session sess = null;

  public MsgBean() {
    TestUtil.logTrace("@MsgBean()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace("@MsgBean-ejbCreate() !!");
    try {
      context = new TSNamingContext();

      cf = (ConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      if (cf == null)
        TestUtil.logErr("error looking up ConnectionFactory");
      TestUtil.logTrace("got a ConnectionFactory!!");

      Dest = (Destination) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      if (Dest == null)
        TestUtil.logErr("Dest error");
      TestUtil.logTrace("got a Dest ");

      p = new Properties();

    } catch (Exception e) {
      TestUtil.logErr("MDB ejbCreate Error!", e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {
    JmsUtil.initHarnessProps(msg, p);
    TestUtil.logTrace("@onMessage! " + msg);

    Connection Conn = null;
    try {
      if (msg.getObjectProperty("properties") != null) {
        initLogging((java.util.Properties) msg.getObjectProperty("properties"));
      }

      Conn = cf.createConnection();
      if (Conn == null)
        TestUtil.logErr("connection error");
      else {
        Conn.start();
        sess = Conn.createSession(true, 0);
      }

      TestUtil.logTrace("started the connection !!");

      // Send a message back to acknowledge that the mdb received the message.
      if (msg.getStringProperty("MessageType").equals("TextMessage")) {
        sendATextMessage();
      } else if (msg.getStringProperty("MessageType").equals("BytesMessage")) {
        sendABytesMessage();
      } else if (msg.getStringProperty("MessageType").equals("MapMessage")) {
        sendAMapMessage();
      } else if (msg.getStringProperty("MessageType").equals("StreamMessage")) {
        sendAStreamMessage();
      } else if (msg.getStringProperty("MessageType").equals("ObjectMessage")) {
        sendAnObjectMessage();
      } else {
        TestUtil.logTrace(
            "@onMessage - invalid message type found in StringProperty");
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception caught in onMessage!", e);
    } finally {
      if (Conn != null) {
        try {
          Conn.close();
        } catch (Exception e) {
          TestUtil.logErr("Error closing Connection", e);
        }
      }
    }
  }

  // message bean helper methods follow.
  // Each method will send a simple message of the type requested.
  // this will send a text message to a Destination

  // must call init for logging to be properly performed
  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
      TestUtil.logTrace("MsgBean initLogging OK.");
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("MsgBean initLogging failed.");
      throw new EJBException(e.getMessage());
    }
  }

  private void sendATextMessage() {
    TestUtil.logTrace("@sendATextMessage");
    try {
      String myMsg = "I am sending a text message as requested";

      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = sess.createProducer(Dest);

      TextMessage msg = sess.createTextMessage();
      msg.setText(myMsg);
      msg.setStringProperty("MessageType", "TextMessageFromMsgBean");

      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught sending a TextMessage!", e);
    }
  }

  private void sendABytesMessage() {
    TestUtil.logTrace("@sendABytesMessage");
    try {
      byte aByte = 10;

      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = sess.createProducer(Dest);

      BytesMessage msg = sess.createBytesMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.writeByte(aByte);
      msg.setStringProperty("MessageType", "BytesMessageFromMsgBean");

      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught sending a BytesMessage!", e);
    }
  }

  private void sendAMapMessage() {
    TestUtil.logTrace("@sendAMapMessage");
    try {
      String myMsg = "I am sending a map message as requested";

      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = sess.createProducer(Dest);

      MapMessage msg = sess.createMapMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setString("MapMessage", myMsg);
      msg.setStringProperty("MessageType", "MapMessageFromMsgBean");

      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught sending a MapMessage!", e);
    }
  }

  private void sendAStreamMessage() {
    TestUtil.logTrace("@sendAStreamMessage");
    try {
      String myMsg = "I am sending a stream message as requested";

      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = sess.createProducer(Dest);

      StreamMessage msg = sess.createStreamMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.writeString(myMsg);
      msg.setStringProperty("MessageType", "StreamMessageFromMsgBean");

      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught sending a StreamMessage!", e);
    }
  }

  private void sendAnObjectMessage() {
    TestUtil.logTrace("@sendAnObjectMessage");
    try {
      String myMsg = "I am sending a text message as requested";

      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = sess.createProducer(Dest);

      ObjectMessage msg = sess.createObjectMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setObject(myMsg);
      msg.setStringProperty("MessageType", "ObjectMessageFromMsgBean");

      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught sending an ObjectMessage!", e);
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    TestUtil.logTrace("In MsgBean::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("In MsgBean::remove()!!");
  }
}
