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
 *  @(#)MsgBean.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.stateful.mdb;

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import javax.sql.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class MsgBean implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into
  // the onMessage method.
  private java.util.Properties p = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private QueueConnectionFactory qFactory = null;

  private QueueConnection qConnection = null;

  private Queue queue = null;

  private QueueSender mSender = null;

  private QueueSession qSession = null;

  private static final String ejbname = "java:comp/env/ejb/Test";

  private static TestHome ejbhome = null;

  private static Test ejbref = null;

  public MsgBean() {
    TestUtil.logTrace("@MsgBean()!");
  };

  public void ejbCreate() throws RemoteException {
    TestUtil.logTrace("@MsgBean-ejbCreate() !!");
    try {
      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      if (qFactory == null) {
        TestUtil.logTrace("qFactory error");
      }
      TestUtil.logTrace("got a qFactory !!");
      queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      if (queue == null) {
        TestUtil.logTrace("queue error");
      }

      TestUtil.logTrace("got  a queue ");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new RemoteException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {
    TestUtil.logTrace("@MsgBean() onMessage! Message " + msg);

    try {
      if (msg.getObjectProperty("properties") != null) {
        initLogging((java.util.Properties) msg.getObjectProperty("properties"));
      }

      // Send a message back to acknowledge that the mdb received the message.
      qConnection = qFactory.createQueueConnection();
      qSession = qConnection.createQueueSession(false, 0);

      TestUtil.logTrace("@MsgBean() onMessage! createing naming context");
      context = new TSNamingContext();
      TestUtil
          .logTrace("@MsgBean() onMessage! Looking up the ejb home " + context);
      ejbhome = (TestHome) context.lookup("java:comp/env/ejb/Test",
          TestHome.class);
      TestUtil.logTrace("@MsgBean() onMessage! got home" + ejbhome);

      if (msg.getStringProperty("MessageType").equals("TextMessage")) {
        ejbref = ejbhome.create();
        TestUtil.logTrace("@MsgBean() onMessage! got ejb object" + ejbref);

        TestUtil.logTrace("@MsgBean() onMessage! It is a TextMessage");
        if (ejbref.EjbIsAuthz())
          sendATextMessage();
        else
          sendABytesMessage();
      } else if (msg.getStringProperty("MessageType").equals("BytesMessage")) {
        ejbref = ejbhome.create();
        TestUtil.logTrace("@MsgBean() onMessage! got ejb object" + ejbref);

        TestUtil.logTrace("@MsgBean() onMessage! It is a BytesMessage");
        try {
          ejbref.EjbNotAuthz();
          sendABytesMessage();
        } catch (Exception ex) {
          TestUtil.logMsg(
              "@OnMessage!  Got expected exception: " + ex.getMessage());
          sendATextMessage();
        }
      } else if (msg.getStringProperty("MessageType").equals("ObjectMessage")) {
        TestUtil.logTrace("@MsgBean() onMessage! It is an ObjectMessage");
        TestUtil.logTrace("Remove test stateful session bean");

        if (ejbref != null)
          try {
            ejbref.remove();
          } catch (Exception ex) {
            TestUtil.logErr("Error removing stateful session bean", ex);
          }
      }

    } catch (Exception e) {
      TestUtil.logTrace("@MsgBean() onMessage! Got exception:");
      TestUtil.printStackTrace(e);

    }

  }

  // message bean helper methods follow.
  // Each method will send a simple message of the type requested.
  // this will send a text message to a Queue

  // must call init for logging to be properly performed
  public void initLogging(java.util.Properties p) throws RemoteException {
    try {
      TestUtil.init(p);
      TestUtil.logTrace("MsgBean initLogging OK.");
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("MsgBean initLogging failed.");
      throw new RemoteException(e.getMessage());
    }
  }

  private void sendATextMessage() {
    TestUtil.logTrace("@sendATextMessage");
    try {
      String myMsg = "I am sending a text message as requested";
      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = qSession.createSender(queue);
      TextMessage msg = qSession.createTextMessage();
      msg.setText(myMsg);
      msg.setStringProperty("MessageType", "TextMessageFromMsgBean");
      mSender.send(msg);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private void sendABytesMessage() {
    TestUtil.logTrace("@sendABytesMessage");
    try {
      byte aByte = 10;
      // send a text message as requested to MDB_QUEUE_REPLY
      mSender = qSession.createSender(queue);
      BytesMessage msg = qSession.createBytesMessage();
      msg.writeByte(aByte);
      msg.setStringProperty("MessageType", "BytesMessageFromMsgBean");
      mSender.send(msg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
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
