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

package com.sun.ts.tests.common.ejb.wrappers;

import java.lang.reflect.Method;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.Queue;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.jms.common.JmsUtil;

/**
 * Life cyle and test invocation methods for MDB. Actual test methods are
 * defined in subclasses of this class.
 *
 * WARNING: We assume the MDB is CMT. Do not use this wrapper for a BMT MDB!
 */
public class MDBWrapper implements MessageDrivenBean, MessageListener {

  protected static final String prefix = "java:comp/env/jms/";

  protected static final String qFactoryLookup = prefix
      + "myQueueConnectionFactory";

  protected static final String replyQueueLookup = prefix + "replyQueue";

  public TSNamingContext nctx = null;

  protected MessageDrivenContext mctx = null;

  protected QueueConnectionFactory qFactory;

  protected QueueConnection conn = null;

  protected Queue replyQueue = null;

  protected QueueSender mSender = null;

  protected boolean result = false;

  public void ejbCreate() {
    try {
      TestUtil.logTrace("[MDBWrapper] ejbCreate()");
      nctx = new TSNamingContext();
      TestUtil.logTrace("[MDBWrapper] Looking up " + qFactoryLookup);
      qFactory = (QueueConnectionFactory) nctx.lookup(qFactoryLookup);
      TestUtil.logTrace("[MDBWrapper] Looking up " + replyQueueLookup);
      replyQueue = (Queue) nctx.lookup(replyQueueLookup);
    } catch (Exception e) {
      TestUtil.logErr("[MDBWrapper] Exception in ejbCreate()", e);
      /* Send instance to "does not exist" state */
      throw new EJBException("Exception in ejbCreate()", e);
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mctx) {
    TestUtil.logTrace("[MDBWrapper] setMessageDrivenContext()");
    this.mctx = mctx;
  }

  public void ejbRemove() {
    TestUtil.logTrace("[MDBWrapper] ejbRemove()");
  }

  public void onMessage(Message msg) {
    QueueSession session = null;
    TextMessage messageSent = null;
    String testName = "Undefined";
    Properties props = null;
    boolean pass = false;

    try {
      props = getProperties(msg);
      TestUtil.init(props);
      testName = msg.getStringProperty("COM_SUN_JMS_TESTNAME");

      // if we're cleaning up, no need to report test results
      if (testName.equals("cleanUpBean")) {
        TestUtil.logTrace("[MDBWrapper] Removing stateful session bean");
        runTest(testName, msg, session, props);
        return;
      }
      pass = runTest(testName, msg, session, props);

      conn = qFactory.createQueueConnection();
      session = conn.createQueueSession(true, 0);
      JmsUtil.sendTestResults(testName, pass, session, replyQueue);
    } catch (Exception e) {
      TestUtil.logErr("[MDBWrapper] Exception in onMessage(): ", e);
    } finally {
      cleanup(conn);
    }

  }

  /**
   * Run corresponding test by invoking test method on the current instance
   * (also used for cleanup of stateful session beans).
   */
  protected boolean runTest(String testName, Message msg, QueueSession session,
      Properties props) {

    Boolean pass = Boolean.FALSE;
    Class testDriverClass;
    Method testMethod;
    Class params[] = { Properties.class };
    Object args[] = new Object[1];

    TestUtil.logTrace("[MDBWrapper] runTest()");

    try {
      TestUtil.logTrace("[MDBWrapper] run test '" + testName + "'");
      testDriverClass = this.getClass();
      testMethod = testDriverClass.getMethod(testName, params);
      args[0] = props;
      pass = (Boolean) testMethod.invoke(this, args);
    } catch (NoSuchMethodException e) {
      TestUtil.logErr("[MDBWrapper] Cannot find method '" + testName
          + "' make sure it is defined " + "in sub-class", e);
      pass = Boolean.FALSE;
    } catch (Exception e) {
      TestUtil.logErr("[MDBWrapper] Unexpected exception", e);
      pass = Boolean.FALSE;
    }

    return pass.booleanValue();
  }

  /**
   * Construct a property object needed by TS harness for logging. We retrieve
   * the properties from the Message object passed into the MDB onMessage()
   * method
   */
  protected Properties getProperties(Message msg) throws JMSException {
    Properties props;
    String hostname = null;
    String traceflag = null;
    String logport = null;
    Enumeration propNames;

    props = new Properties();

    /*
     * Because a JMS property name cannot contain '.' the following properties
     * are a special case
     */
    hostname = msg.getStringProperty("harnesshost");
    props.put("harness.host", hostname);
    traceflag = msg.getStringProperty("harnesslogtraceflag");
    props.put("harness.log.traceflag", traceflag);
    logport = msg.getStringProperty("harnesslogport");
    props.put("harness.log.port", logport);

    /*
     * now pull out the rest of the properties from the message
     */
    propNames = msg.getPropertyNames();
    for (String name = null; propNames.hasMoreElements();) {
      name = (String) propNames.nextElement();
      props.put(name, msg.getStringProperty(name));
    }

    return props;
  }

  /** Cleanup method designed to be called within a finally block */
  protected void cleanup(QueueConnection conn) {
    try {
      if (null != conn) {
        conn.close();
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "[MDBWrapper] Ignoring Exception on " + "QueueConnection cleanup: ",
          e);
    }
  }

}
