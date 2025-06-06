/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: Client.java 59995 2009-10-14 12:05:29Z af70133 $
 */

package com.sun.ts.tests.jms.commonee;

import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;

import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;

public class Client extends EETest {

    // Naming specific member variables
    protected TSNamingContextInterface context = null;

    protected Properties props = null;

    protected Queue rcvrQueue;

    protected QueueConnection qConnect;

    protected QueueSession session;

    protected QueueConnectionFactory qFactory;

    protected QueueSender qSender;

    protected TopicConnection tConnect;

    protected TopicSession tSession;

    protected TopicConnectionFactory tFactory;

    protected TopicPublisher tPub;

    protected String jmsUser = null;

    protected String jmsPassword = null;

    protected String hostname = null;

    protected String traceFlag = null;

    protected String logPort = null;

    protected TextMessage msg = null;

    // get this from ts.jte
    protected long timeout = 0;

    /*
     * @class.setup_props:
     * 
     * jms_timeout; user; password; harness.log.traceflag; harness.log.port; generateSQL;
     *
     * @class.testArgs: -ap tssql.stmt
     *
     *
     */
    public void setup(String[] args, Properties p) throws Exception {
        props = p;

        try {
            jmsUser = TestUtil.getProperty(p, "user");
            if (jmsUser == null) {
                TestUtil.logTrace("user is null");
                throw new Exception("Error getting user");
            }

            jmsPassword = TestUtil.getProperty(p, "password");
            if (jmsPassword == null) {
                TestUtil.logTrace("password is null");
                throw new Exception("Error getting password");
            }

            String time = TestUtil.getProperty(p, "jms_timeout");
            if (time == null) {
                TestUtil.logTrace("jms_timeout is null");
                throw new Exception("Error getting jms_timeout");
            }

            hostname = TestUtil.getProperty(p, "harness.host");
            if (hostname == null) {
                TestUtil.logTrace("harness.host is null");
                throw new Exception("Error getting harness.host");
            }
            traceFlag = TestUtil.getProperty(p, "harness.log.traceflag");
            if (traceFlag == null) {
                TestUtil.logTrace("harness.log.traceflag is null");
                throw new Exception("Error getting harness.log.traceflag");
            }
            logPort = TestUtil.getProperty(p, "harness.log.port");
            if (logPort == null) {
                TestUtil.logTrace("harness.log.port is null");
                throw new Exception("Error getting harness.log.port");
            }

            timeout = Long.parseLong(time);

            TestUtil.logTrace("in client setup");

            context = new TSNamingContext();
            TestUtil.logTrace("Client: Do lookups!");

            rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");

            qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
            qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
            session = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            qConnect.start();

            tFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
            tConnect = tFactory.createTopicConnection(jmsUser, jmsPassword);
            tSession = tConnect.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            tConnect.start();

            TestUtil.logTrace("get the connection and starter up");
            TestUtil.logTrace("Client: connection started, now send initialization msg!");

        } catch (Exception e) {
            throw new Exception("Setup Failed!", e);
        }
    }

    protected void createTestMessage(String TestCase, int num) {
        String myMessage = "MDB deploy tests";
        Enumeration e;
        String key = null;
        try {
            msg = session.createTextMessage();
            e = props.propertyNames();
            // we need to extract the properties passed from the harness
            // and set them in the message properties
            // This is so we can send them to the mdb
            msg.setStringProperty("user", jmsUser);
            msg.setStringProperty("password", jmsPassword);
            msg.setStringProperty("harnesshost", hostname);
            msg.setStringProperty("harnesslogtraceflag", traceFlag);
            msg.setStringProperty("harnesslogport", logPort);
            // TODO: Not sure why we should be loading all properties
            e = props.propertyNames();
            key = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (!key.contains("-") && !key.contains(".") && !key.contains("***")) {
                    String value = TestUtil.getProperty(props, key);
                    try {
                        msg.setStringProperty(key, value);
                    } catch (JMSException ex) {
                        // ignore
                    }
                }
            }

            msg.setText(myMessage);
            msg.setIntProperty("TestCaseNum", num);
            msg.setStringProperty("COM_SUN_JMS_TESTNAME", TestCase);

        } catch (Exception ee) {
            TestUtil.printStackTrace(ee);
            TestUtil.logMsg("key was: " + key);
            TestUtil.logMsg("props was: " + props.getProperty(key));
            TestUtil.logMsg("Error setting properties");
        }
    }

    public boolean checkOnResponse(String TestCase) {
        boolean status = false;
        try {
            TestUtil.logMsg("@checkOnResponse");
            status = recvMessageInternal(session, TestCase);
            TestUtil.logMsg("Close the session");
        } catch (Exception e) {
            TestUtil.printStackTrace(e);
        }
        return status;
    }

    protected boolean recvMessageInternal(QueueSession session, String TestCase) throws JMSException {
        boolean retcode = false;
        TestUtil.logMsg("@recvMessageInternal");
        // Create a message consumer.
        QueueReceiver rcvr = session.createReceiver(rcvrQueue);
        // dequeue the response from the mdb
        Message msgRec = null;

        for (int i = 0; i < 10; ++i) {
            TestUtil.logMsg("@recvMessageInternal trying to receive the message: " + i);
            msgRec = rcvr.receive(timeout);
            if (msgRec != null) {
                break;
            }
        } // end for loop
        if (msgRec != null) {
            if (msgRec instanceof TextMessage) {
                TestUtil.logMsg("**** Received msg text = " + ((TextMessage) msgRec).getText() + " ****");
            }
            TestUtil.logMsg("**** Received msg getStringProperty('TestCase') = " + msgRec.getStringProperty("TestCase"));
            TestUtil.logMsg("**** Received msg getStringProperty('Status') = " + msgRec.getStringProperty("Status"));
            if (msgRec.getStringProperty("TestCase") == null || msgRec.getStringProperty("Status") == null) {
                TestUtil.logMsg("Fail: unexpected message received from MDB_QUEUE_REPLY msgRec=" + msgRec);
            } else if (msgRec.getStringProperty("TestCase").equals(TestCase) && msgRec.getStringProperty("Status").equals("Pass")) {
                TestUtil.logMsg("TestCase: " + msgRec.getStringProperty("TestCase"));
                TestUtil.logMsg("Status from msg: " + msgRec.getStringProperty("Status"));
                TestUtil.logMsg("Pass: we got the expected msg back! ");
                retcode = true;
            } else if (msgRec.getStringProperty("Status").equals("Fail")) {
                TestUtil.logMsg("TestCase: " + msgRec.getStringProperty("TestCase"));
                TestUtil.logMsg("Status from msg: " + msgRec.getStringProperty("Status"));
                TestUtil.logMsg("Fail: Error(s) occurred! ");
            } else {
                TestUtil.logMsg("Fail: we didnt get the expected msg back! ");
                TestUtil.logMsg("TestCase:  " + msgRec.getStringProperty("TestCase"));
            }
        } else if (msgRec == null) {
            TestUtil.logMsg("Fail: we didnt get any msg back! ");
        }
        return retcode;
    }

    public void cleanup() throws Exception {
        try {
            closeDefaultConnections();
            flushQueue();
        } catch (Exception e) {
            TestUtil.logErr("Cleanup error: " + e.toString());
            TestUtil.printStackTrace(e);
        }
    }

    /**
     * Use this method at cleanup time to remove any messages that have remained on the queue.
     *
     */

    public void flushQueue() throws Exception {
        QueueConnection qc = null;
        QueueReceiver qr = null;
        QueueSession qs = null;
        int numMsgsFlushed = 0;
        try {
            qc = qFactory.createQueueConnection(jmsUser, jmsPassword);
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            qc.start(); // start the connections so that messages may be received.

            qr = qs.createReceiver(rcvrQueue);
            // flush the queue
            Message msg = qr.receive(timeout);
            while (msg != null) {
                if (msg instanceof TextMessage) {
                    TestUtil.logMsg("**** Flushed TextMessage =" + ((TextMessage) msg).getText() + " ****");
                } else {
                    String msgType = "Message";
                    if (msg instanceof BytesMessage)
                        msgType = "BytesMessage";
                    else if (msg instanceof MapMessage)
                        msgType = "MapMessage";
                    else if (msg instanceof ObjectMessage)
                        msgType = "ObjectMessage";
                    else if (msg instanceof StreamMessage)
                        msgType = "StreamMessage";
                    TestUtil.logMsg("**** Flushed Message of type " + msgType + " ****");
                }
                numMsgsFlushed++;
                msg = qr.receiveNoWait();
            }
            if (numMsgsFlushed > 0) {
                TestUtil.logMsg("flushed " + numMsgsFlushed + " messages");
            }

        } catch (Exception e) {
            TestUtil.logErr("Cleanup error attempting to flush Queue: " + e.toString());
            TestUtil.printStackTrace(e);
        } finally {
            qc.close();
        }
    }

    /**
     * Close default connections if open
     *
     * @exception Exception
     *
     * @see
     */
    public void closeDefaultConnections() throws Exception {
        try {
            if (qConnect != null) {
                TestUtil.logMsg("Client: Closing QueueConnection");
                qConnect.close();
            }
            if (tConnect != null) {
                TestUtil.logMsg("Client: Closing TopicConnection");
                tConnect.close();
            }
        } catch (Exception e) {
            TestUtil.logErr("Cleanup error: " + e.toString());
            TestUtil.printStackTrace(e);

        }
    }
}
