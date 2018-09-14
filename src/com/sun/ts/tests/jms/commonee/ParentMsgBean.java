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
 * $Id: ParentMsgBean.java 59995 2009-10-14 12:05:29Z af70133 $
 */

package com.sun.ts.tests.jms.commonee;

import com.sun.ts.tests.jms.common.*;
import java.io.Serializable;
import java.util.Properties;
import java.util.Enumeration;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import javax.transaction.UserTransaction;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class ParentMsgBean implements MessageDrivenBean, MessageListener {

  // properties object needed for logging,
  // get this from the message object passed into
  // the onMessage method.
  protected java.util.Properties p = null;

  protected TSNamingContext context = null;

  protected MessageDrivenContext mdc = null;

  // JMS PTP
  protected QueueConnectionFactory qFactory;

  protected QueueConnection qConnection = null;

  protected Queue queueR = null;

  protected Queue queue = null;

  protected QueueSender mSender = null;

  protected boolean result = false;

  public ParentMsgBean() {
    TestUtil.logTrace("@MsgBean()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace("@EJBCreate()!");

    try {

      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error", e);
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    TestUtil.logTrace("@MsgBean:setMessageDrivenContext()!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("@ejbRemove()");
  }

  public void onMessage(Message msg) {
    QueueSession qSession = null;
    TextMessage messageSent = null;
    String testName = null;
    String hostname = null;
    String traceflag = null;
    String logport = null;

    p = new Properties();
    try {
      // because a jms property name cannot contain '.' the
      // following properties are a special case
      hostname = msg.getStringProperty("harnesshost");
      traceflag = msg.getStringProperty("harnesslogtraceflag");
      logport = msg.getStringProperty("harnesslogport");
      p.put("harness.host", hostname);
      p.put("harness.log.traceflag", traceflag);
      p.put("harness.log.port", logport);

      // now pull out the rest of the properties from the message
      Enumeration e = msg.getPropertyNames();
      String key = null;
      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        p.put(key, msg.getStringProperty(key));
      }

      testName = msg.getStringProperty("COM_SUN_JMS_TESTNAME");
      qConnection = qFactory.createQueueConnection();
      if (qConnection == null)
        throw new EJBException("MDB connection Error!");

      qSession = qConnection.createQueueSession(true,
          Session.SESSION_TRANSACTED);

      // Diagnostic - pull out after testing
      // for (Enumeration enum = p.propertyNames(); enum.hasMoreElements();){
      // System.out.println(enum.nextElement());
      // }
      TestUtil.init(p);
      TestUtil.logTrace("will run TestCase: " + testName);
      runTests(msg, qSession, testName, p);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      if (qConnection != null) {
        try {
          qConnection.close();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    }

  }

  protected void runTests(Message msg, QueueSession qSession, String testName,
      java.util.Properties p) {
    TestUtil.logTrace("ParentMsgBean - runTests");

  }

}
