/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2022 Contributors to Eclipse Foundation. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.resourcedefs.annotations;

import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSConnectionFactoryDefinitions;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSDestinationDefinitions;
import jakarta.jms.JMSProducer;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;

//-------------------------------------
// JMS Destination Resource Definitions
//-------------------------------------
  @JMSDestinationDefinition(
	description="Define Queue EJBMyTestQueue",
	interfaceName="jakarta.jms.Queue",
	name="java:global/env/EJBMyTestQueue",
    	destinationName="EJBMyTestQueue"
   )

  @JMSDestinationDefinition(
	description="Define Topic EJBMyTestTopic",
	interfaceName="jakarta.jms.Topic",
	name="java:app/env/EJBMyTestTopic",
	destinationName="EJBMyTestTopic"
   )

//-------------------------------------------
// JMS ConnectionFactory Resource Definitions
//-------------------------------------------
  @JMSConnectionFactoryDefinition(
	description="Define ConnectionFactory EJBMyTestConnectionFactory",
	interfaceName="jakarta.jms.ConnectionFactory",
    	name="java:global/EJBMyTestConnectionFactory",
	user = "j2ee",
	password = "j2ee"
   )

  @JMSConnectionFactoryDefinition(
	description="Define QueueConnectionFactory EJBMyTestQueueConnectionFactory",
	interfaceName="jakarta.jms.QueueConnectionFactory",
    	name="java:app/EJBMyTestQueueConnectionFactory",
	user = "j2ee",
	password = "j2ee"
   )

  @JMSConnectionFactoryDefinition(
	description="Define TopicConnectionFactory EJBMyTestTopicConnectionFactory",
	interfaceName="jakarta.jms.TopicConnectionFactory",
    	name="java:module/EJBMyTestTopicConnectionFactory",
	user = "j2ee",
	password = "j2ee"
   )

  @JMSConnectionFactoryDefinition(
	description="Define Durable TopicConnectionFactory EJBMyTestDurableTopicConnectionFactory",
	interfaceName="jakarta.jms.TopicConnectionFactory",
    	name="java:comp/env/jms/EJBMyTestDurableTopicConnectionFactory",
	user = "j2ee",
	password = "j2ee",
	clientId = "MyClientID",
	properties = { "Property1=10", "Property2=20" },
	transactional = false,
	maxPoolSize = 30,
	minPoolSize = 20
   )

@Stateful(name = "JMSResourceDefsEjbClientBean")
@Remote({ EjbClientIF.class })
@TransactionManagement(TransactionManagementType.BEAN)
public class EjbClient implements EjbClientIF {
  private static final long serialVersionUID = 1L;

  // JMS objects
  protected transient ConnectionFactory cf = null;

  protected transient ConnectionFactory dcf = null;

  protected transient ConnectionFactory cfra = null;

  protected transient QueueConnectionFactory qcf = null;

  protected transient TopicConnectionFactory tcf = null;

  protected transient TopicConnectionFactory dtcf = null;

  protected transient Topic topic = null;

  protected transient Topic topica = null;

  protected transient Queue queue = null;

  protected transient JMSContext context = null;

  protected transient JMSConsumer consumerQ = null;

  protected transient JMSProducer producerQ = null;

  protected transient JMSConsumer consumerT = null;

  protected transient JMSProducer producerT = null;

  protected boolean queueTest = false;

  // Harness req's
  protected Properties props = null;

  // properties read from ts.jte file
  protected long timeout;

  protected String user;

  protected String password;

  protected String mode;

  /*
   * doCleanup()
   */
  private void doCleanup() throws Exception {
    try {
      if (queueTest && consumerQ != null) {
        TestUtil.logMsg("Flush any messages left on Queue");
        Message rmsg = consumerQ.receive(timeout);
        while (rmsg != null) {
          rmsg = consumerQ.receiveNoWait();
          if (rmsg == null) {
            rmsg = consumerQ.receiveNoWait();
          }
        }
        consumerQ.close();
      }
      if (consumerT != null)
        consumerT.close();
      TestUtil.logMsg("Close JMSContext Objects");
      if (context != null)
        context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Exception("doCleanup failed!", e);
    }
  }

  public void init(Properties p) {
    try {
      TestUtil.init(p);
      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      doLookupJMSObjects();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    }
  }

  public boolean echo(String testName) {
    boolean pass = false;

    if (testName.equals("sendAndRecvQueueTestFromEjbClient"))
      pass = sendAndRecvQueueTestFromEjbClient();
    else if (testName.equals("sendAndRecvTopicTestFromEjbClient"))
      pass = sendAndRecvTopicTestFromEjbClient();
    else if (testName.equals("checkClientIDTestFromEjbClient"))
      pass = checkClientIDTestFromEjbClient();
    try {
      doCleanup();
    } catch (Exception e) {
      pass = false;
    }
    return pass;
  }

  /*
   * Lookup JMS Connection Factory and Destination Objects
   */
  private void doLookupJMSObjects() throws Exception {
    try {
      TestUtil.logMsg(
          "Lookup JMS factories defined in @JMSConnectionFactoryDefinitions");
      TestUtil.logMsg(
          "Lookup JMS destinations defined in @JMSDestinationDefinitions");
      TSNamingContext namingctx = new TSNamingContext();
      TestUtil.logMsg("Lookup java:comp/DefaultJMSConnectionFactory");
      dcf = (ConnectionFactory) namingctx
          .lookup("java:comp/DefaultJMSConnectionFactory");
      TestUtil.logMsg("Lookup java:global/EJBMyTestConnectionFactory");
      cf = (ConnectionFactory) namingctx
          .lookup("java:global/EJBMyTestConnectionFactory");
      TestUtil.logMsg("Lookup java:app/EJBMyTestQueueConnectionFactory");
      qcf = (QueueConnectionFactory) namingctx
          .lookup("java:app/EJBMyTestQueueConnectionFactory");
      TestUtil.logMsg("Lookup java:module/EJBMyTestTopicConnectionFactory");
      tcf = (TopicConnectionFactory) namingctx
          .lookup("java:module/EJBMyTestTopicConnectionFactory");
      TestUtil.logMsg(
          "Lookup java:comp/env/jms/EJBMyTestDurableTopicConnectionFactory");
      dtcf = (TopicConnectionFactory) namingctx
          .lookup("java:comp/env/jms/EJBMyTestDurableTopicConnectionFactory");
      TestUtil.logMsg("Lookup java:global/env/EJBMyTestQueue");
      queue = (Queue) namingctx.lookup("java:global/env/EJBMyTestQueue");
      TestUtil.logMsg("Lookup java:app/env/EJBMyTestTopic");
      topic = (Topic) namingctx.lookup("java:app/env/EJBMyTestTopic");

      TestUtil.logMsg("Create JMSContext, JMSProducer's and JMSConsumer's");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      producerQ = context.createProducer();
      consumerQ = context.createConsumer(queue);
      producerT = context.createProducer();
      consumerT = context.createConsumer(topic);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw e;
    }
  }

  public boolean sendAndRecvQueueTestFromEjbClient() {
    boolean pass = true;
    String message = "Where are you!";
    try {
      queueTest = true;
      // send and receive TextMessage
      TestUtil.logMsg(
          "Creating TextMessage via JMSContext.createTextMessage(String)");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvQueueTestFromEjbClient");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producerQ.send(queue, expTextMessage);
      TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
      TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }

    return pass;
  }

  public boolean sendAndRecvTopicTestFromEjbClient() {
    boolean pass = true;
    String message = "Where are you!";
    try {
      queueTest = false;
      // send and receive TextMessage
      TestUtil.logMsg(
          "Creating TextMessage via JMSContext.createTextMessage(String)");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvTopicTestFromEjbClient");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producerT.send(topic, expTextMessage);
      TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
      TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }
    return pass;
  }

  public boolean checkClientIDTestFromEjbClient() {
    boolean pass = true;
    JMSContext context = null;
    try {
      queueTest = false;
      doLookupJMSObjects();
      TestUtil
          .logMsg("Create JMSContext from durable topic connection factory");
      TestUtil.logMsg(
          "Check the client id which is configured as MyClientID in the "
              + "JMSConnectionFactoryDefinition annotation");
      context = dtcf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      String clientid = context.getClientID();
      if (clientid == null) {
        TestUtil.logErr("Client ID value is null (expected MyClientID)");
        pass = false;
      } else if (clientid.equals("MyClientID")) {
        TestUtil.logMsg("Client ID value is correct (MyClientID)");
      } else {
        TestUtil
            .logErr("Client ID value is incorrect (expected MyClientID, got "
                + clientid + ")");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        if (context != null)
          context.close();
      } catch (Exception e) {
      }
    }
    return pass;
  }

}
