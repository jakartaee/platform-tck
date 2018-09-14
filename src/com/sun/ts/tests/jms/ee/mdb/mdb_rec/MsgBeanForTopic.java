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

package com.sun.ts.tests.jms.ee.mdb.mdb_rec;

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

public class MsgBeanForTopic implements MessageDrivenBean, MessageListener {

  private MessageDrivenContext mdc = null;

  private QueueConnectionFactory qcFactory;

  private QueueConnection connection = null;

  private Properties p = null;

  public MsgBeanForTopic() {
    TestUtil.logTrace("In MsgBeanForTopic::MsgBeanForTopic()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace("In MsgBeanForTopic::ejbCreate() !!");
    p = new Properties();
  }

  public void onMessage(Message msg) {
    try {
      JmsUtil.initHarnessProps(msg, p);
      TestUtil.logTrace("In MsgBeanForTopic::onMessage() : " + msg);
      sendReply(msg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private void sendReply(Message msg) {
    try {
      TSNamingContext context = new TSNamingContext();
      qcFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      connection = qcFactory.createQueueConnection();

      // get the reply to queue
      Queue replyQueue = (Queue) context
          .lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      connection.start();

      QueueSession session = connection.createQueueSession(true, 0);
      javax.jms.TextMessage reply = session.createTextMessage();
      QueueSender replier = session.createSender(replyQueue);
      reply.setText("MDB Responding to message receipt");
      reply.setStringProperty("Verify", msg.getStringProperty("Verify"));

      replier.send(reply);
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
    TestUtil
        .logTrace("In MsgBeanForTopicForQueue::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("In MsgBeanForTopicForQueue::remove()!!");
  }
}
