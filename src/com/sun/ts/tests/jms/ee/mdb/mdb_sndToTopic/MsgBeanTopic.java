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

package com.sun.ts.tests.jms.ee.mdb.mdb_sndToTopic;

import java.io.Serializable;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import java.util.Properties;
import javax.sql.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBeanTopic implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into
  // the onMessage method.
  private java.util.Properties p = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private QueueConnectionFactory queueConFactory = null;

  private TopicConnectionFactory topicConFactory = null;

  private DataSource dataSource = null;

  private QueueConnectionFactory qcFactory = null;

  private QueueConnection connection = null;

  private Queue replyQueue = null;

  public MsgBeanTopic() {
    TestUtil.logTrace("@MsgBeanTopic - @MsgBean()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace("@MsgBeanTopic - @MsgBean-ejbCreate() !!");
    try {
      TSNamingContext context = new TSNamingContext();
      qcFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      replyQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      p = new Properties();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {

    JmsUtil.initHarnessProps(msg, p);
    TestUtil.logTrace("@MsgBeanTopic - MsgBeanTopic - onMessage! " + msg);

    try {
      TestUtil.logTrace("In MsgBeanForTopic::onMessage() : " + msg);
      TestUtil.logTrace("calling sendReply");
      sendReply(msg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

  }

  private void sendReply(Message msg) {
    try {
      connection = qcFactory.createQueueConnection();

      // get the reply to queue
      System.out.println("From sendReply");
      connection.start();
      System.out.println("started the connection");

      QueueSession session = connection.createQueueSession(true, 0);
      javax.jms.TextMessage reply = session.createTextMessage();
      QueueSender replier = session.createSender(replyQueue);
      reply.setText("MDB Responding to message receipt");
      reply.setStringProperty("MessageType",
          msg.getStringProperty("MessageType"));

      TestUtil.logTrace("sending a msg to MDB_QUEUE_REPLY");
      replier.send(reply);
      System.out.println("Sent the message");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception ee) {
          TestUtil.printStackTrace(ee);
        }
      }
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
