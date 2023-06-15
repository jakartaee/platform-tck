/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.ee20.resourcedefs.descriptor;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TSNamingContext;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;

public class Client {
	private static final String testName = "com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient ConnectionFactory cfra = null;

	private transient QueueConnectionFactory qcf = null;

	private transient TopicConnectionFactory tcf = null;

	private transient TopicConnectionFactory dtcf = null;

	private transient Topic topic = null;

	private transient Topic topica = null;

	private transient Queue queue = null;

	private transient JMSContext context = null;

	private transient JMSConsumer consumerQ = null;

	private transient JMSProducer producerQ = null;

	private transient JMSConsumer consumerT = null;

	private transient JMSProducer producerT = null;

	private boolean queueTest = false;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	String vehicle;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * 
	 * @exception Fault
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			vehicle = System.getProperty("vehicle");

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' in must not be null ");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
			}

			logger.log(Logger.Level.INFO, "Lookup JMS factories defined in Deployment Descriptors");
			logger.log(Logger.Level.INFO, "Lookup JMS destinations defined in Deployment Descriptors");
			logger.log(Logger.Level.INFO, "See <jms-connectionfactory> and <jms-destination> tags in DD's");
			TSNamingContext namingctx = new TSNamingContext();
			String prefix = null;
			if (vehicle.equals("appclient"))
				prefix = "AppClient";
			else if (vehicle.equals("ejb"))
				prefix = "EJB";
			else if (vehicle.equals("servlet"))
				prefix = "Servlet";
			else if (vehicle.equals("jsp"))
				prefix = "JSP";
			logger.log(Logger.Level.INFO, "Lookup java:global/" + prefix + "MyTestConnectionFactory");
			cf = (ConnectionFactory) namingctx.lookup("java:global/" + prefix + "MyTestConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:app/" + prefix + "MyTestQueueConnectionFactory");
			qcf = (QueueConnectionFactory) namingctx.lookup("java:app/" + prefix + "MyTestQueueConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:module/" + prefix + "MyTestTopicConnectionFactory");
			tcf = (TopicConnectionFactory) namingctx.lookup("java:module/" + prefix + "MyTestTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:comp/env/jms/" + prefix + "MyTestDurableTopicConnectionFactory");
			dtcf = (TopicConnectionFactory) namingctx
					.lookup("java:comp/env/jms/" + prefix + "MyTestDurableTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:global/env/" + prefix + "MyTestQueue");
			queue = (Queue) namingctx.lookup("java:global/env/" + prefix + "MyTestQueue");
			logger.log(Logger.Level.INFO, "Lookup java:app/env/" + prefix + "MyTestTopic");
			topic = (Topic) namingctx.lookup("java:app/env/" + prefix + "MyTestTopic");

			logger.log(Logger.Level.INFO, "Create JMSContext, JMSProducer's and JMSConsumer's");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			producerQ = context.createProducer();
			consumerQ = context.createConsumer(queue);
			producerT = context.createProducer();
			consumerT = context.createConsumer(topic);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setup failed!", e);
		}
	}

	/* cleanup */

	/*
	 * cleanup() is called after each test
	 * 
	 * @exception Fault
	 */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			if (queueTest && consumerQ != null) {
				logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
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
			logger.log(Logger.Level.INFO, "Close JMSContext Objects");
			if (context != null)
				context.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: sendAndRecvQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
	 * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
	 * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
	 * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
	 * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
	 * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
	 *
	 * @test_Strategy: Send and receive a message to/from a Queue.
	 *
	 */
	@Test
	public void sendAndRecvQueueTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			queueTest = true;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvQueueTest");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerQ.send(queue, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("sendAndRecvQueueTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvQueueTest failed");
		}
	}

	/*
	 * @testName: sendAndRecvTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
	 * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
	 * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
	 * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
	 * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
	 * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
	 *
	 * @test_Strategy: Send and receive a message to/from a Topic.
	 *
	 */
	@Test
	public void sendAndRecvTopicTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			queueTest = false;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTopicTest");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerT.send(topic, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("sendAndRecvTopicTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTopicTest failed");
		}
	}

	/*
	 * @testName: checkClientIDOnDurableConnFactoryTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1324; JMS:JAVADOC:1325; JMS:JAVADOC:1327;
	 * JMS:JAVADOC:1330; JMS:JAVADOC:1331; JMS:JAVADOC:1332; JMS:JAVADOC:1333;
	 * JMS:JAVADOC:1334; JMS:JAVADOC:1335; JMS:JAVADOC:1336; JMS:JAVADOC:1338;
	 * JMS:JAVADOC:1339; JMS:JAVADOC:1342; JMS:JAVADOC:1343; JMS:JAVADOC:1344;
	 * JMS:JAVADOC:1345; JMS:JAVADOC:1346; JMS:JAVADOC:1347; JMS:JAVADOC:1348;
	 * JMS:JAVADOC:1451; JMS:JAVADOC:1452;
	 *
	 * @test_Strategy: Check client id setting on durable connection factory
	 *
	 */
	@Test
	public void checkClientIDOnDurableConnFactoryTest() throws Exception {
		boolean pass = true;
		JMSContext context = null;
		try {
			queueTest = false;
			logger.log(Logger.Level.INFO, "Create JMSContext from durable topic connection factory");
			logger.log(Logger.Level.INFO,
					"Check the client id which is configured as MyClientID in the deployment descriptors");
			context = dtcf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			String clientid = context.getClientID();
			if (clientid == null) {
				logger.log(Logger.Level.ERROR, "Client ID value is null (expected MyClientID)");
				pass = false;
			} else if (clientid.equals("MyClientID")) {
				logger.log(Logger.Level.INFO, "Client ID value is correct (MyClientID)");
			} else {
				logger.log(Logger.Level.ERROR,
						"Client ID value is incorrect (expected MyClientID, got " + clientid + ")");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("checkClientIDOnDurableConnFactoryTest", e);
		} finally {
			try {
				if (context != null)
					context.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("checkClientIDOnDurableConnFactoryTest failed");
		}
	}

}
