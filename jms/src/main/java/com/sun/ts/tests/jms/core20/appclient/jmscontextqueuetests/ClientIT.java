/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.jms.core20.appclient.jmscontextqueuetests;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.InvalidClientIDRuntimeException;
import jakarta.jms.InvalidDestinationRuntimeException;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageFormatRuntimeException;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;

public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.appclient.jmscontextqueuetests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient Queue queue = null;

	private transient Destination destination = null;

	private transient JMSContext context = null;

	private transient JMSContext context2 = null;

	private transient JMSContext contextToSendMsg = null;

	private transient JMSContext contextToCreateMsg = null;

	private transient JMSConsumer consumer = null;

	private transient JMSProducer producer = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	// used for tests
	private static final int numMessages = 3;

	private static final int iterations = 5;

	ArrayList queues = null;

	ArrayList connections = null;

	/*
	 * Utility method to return the session mode as a String
	 */
	private String printSessionMode(int sessionMode) {
		switch (sessionMode) {
		case JMSContext.SESSION_TRANSACTED:
			return "SESSION_TRANSACTED";
		case JMSContext.AUTO_ACKNOWLEDGE:
			return "AUTO_ACKNOWLEDGE";
		case JMSContext.CLIENT_ACKNOWLEDGE:
			return "CLIENT_ACKNOWLEDGE";
		case JMSContext.DUPS_OK_ACKNOWLEDGE:
			return "DUPS_OK_ACKNOWLEDGE";
		default:
			return "UNEXPECTED_SESSIONMODE";
		}
	}

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

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}
			queues = new ArrayList(3);
			connections = new ArrayList(5);

			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q setup");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			tool.getDefaultConnection().close();
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			contextToSendMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			contextToCreateMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
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
			logger.log(Logger.Level.INFO, "Close JMSContext objects");
			if (context != null) {
				context.close();
				context = null;
			}
			if (contextToSendMsg != null) {
				contextToSendMsg.close();
				contextToSendMsg = null;
			}
			if (contextToCreateMsg != null) {
				contextToCreateMsg.close();
				contextToCreateMsg = null;
			}
			logger.log(Logger.Level.INFO, "Close JMSConsumer objects");
			if (consumer != null) {
				consumer.close();
				consumer = null;
			}
			logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
			tool.flushDestination();
			tool.closeAllResources();
			producer = null;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: setGetClientIDTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:970; JMS:JAVADOC:1040; JMS:SPEC:264.5;
	 * JMS:SPEC:173; JMS:SPEC:198; JMS:SPEC:91;
	 * 
	 * @test_Strategy: Test the following APIs:
	 * 
	 * JMSContext.setClientID(String clientID) JMSContext.getClientID()
	 */
	@Test
	public void setGetClientIDTest() throws Exception {
		boolean pass = true;
		try {
			String clientid = "myclientid";
			logger.log(Logger.Level.INFO, "Calling setClientID(" + clientid + ")");
			context.setClientID(clientid);
			logger.log(Logger.Level.INFO, "Calling getClientID and expect " + clientid + " to be returned");
			String cid = context.getClientID();
			if (!cid.equals(clientid)) {
				logger.log(Logger.Level.ERROR, "getClientID() returned " + cid + ", expected " + clientid);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("setGetClientIDTest");
		}

		if (!pass) {
			throw new Exception("setGetClientIDTest failed");
		}
	}

	/*
	 * @testName: setClientIDLateTest
	 * 
	 * @assertion_ids: JMS:SPEC:173; JMS:SPEC:198; JMS:SPEC:94; JMS:SPEC:91;
	 * JMS:JAVADOC:1040; JMS:JAVADOC:1043; JMS:SPEC:264.5;
	 * 
	 * @test_Strategy: Create a JMSContext, send and receive a message, then try to
	 * set the ClientID. Verify that IllegalStateRuntimeException is thrown.
	 * 
	 * JMSContext.setClientID(String clientID)
	 */
	@Test
	public void setClientIDLateTest() throws Exception {
		boolean pass = true;

		try {
			TextMessage messageSent;
			TextMessage messageReceived;
			String message = "This is my message.";

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating JMSConsumer");
			consumer = context.createConsumer(destination);

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "setClientIDLateTest");
			logger.log(Logger.Level.INFO, "Sending TestMessage");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage != null) {
				logger.log(Logger.Level.INFO, "actTextMessage=" + actTextMessage.getText());
			}

			logger.log(Logger.Level.TRACE, "Attempt to set Client ID too late (expect IllegalStateRuntimeException)");
			try {
				context.setClientID("setClientIDLateTest");
				pass = false;
				logger.log(Logger.Level.ERROR, "IllegalStateRuntimeException was not thrown");
			} catch (jakarta.jms.IllegalStateRuntimeException is) {
				logger.log(Logger.Level.INFO, "IllegalStateRuntimeException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("setClientIDLateTest", e);
		}
		if (!pass) {
			throw new Exception("setClientIDLateTest failed");
		}
	}

	/*
	 * @testName: setGetChangeClientIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:93; JMS:SPEC:95; JMS:SPEC:198; JMS:JAVADOC:1040;
	 * JMS:JAVADOC:970; JMS:JAVADOC:1042; JMS:JAVADOC:1043; JMS:SPEC:264.5;
	 * 
	 * 
	 * @test_Strategy: Test setClientID()/getClientID(). Make sure that the clientID
	 * set is the clientID returned. Then try and reset the clientID. Verify that
	 * the IllegalStateRuntimeException is thrown. 1) Use a JMSContext that has no
	 * ClientID set, then call setClientID twice. 2) Try and set the clientID on a
	 * second JMSContext to the clientID value of the first JMSContext. Verify that
	 * InvalidClientIDRuntimeException is thrown.
	 * 
	 * JMSContext.setClientID(String clientID) JMSContext.getClientID()
	 */
	@Test
	public void setGetChangeClientIDTest() throws Exception {
		boolean pass = true;
		String lookup = "MyTopicConnectionFactory";

		try {
			logger.log(Logger.Level.INFO, "Create second JMSContext with AUTO_ACKNOWLEDGE");
			context2 = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);

			logger.log(Logger.Level.INFO, "Setting clientID!");
			context.setClientID("ctstest");

			logger.log(Logger.Level.INFO, "Getting clientID!");
			String clientid = context.getClientID();

			if (!clientid.equals("ctstest")) {
				logger.log(Logger.Level.ERROR, "getClientID() returned " + clientid + ", expected ctstest");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "setClientID/getClientID correct");
			}

			logger.log(Logger.Level.INFO, "Resetting clientID! (expect IllegalStateRuntimeException)");
			context.setClientID("changeIt");
			logger.log(Logger.Level.ERROR, "No exception was thrown on ClientID reset");
			pass = false;
		} catch (jakarta.jms.IllegalStateRuntimeException e) {
			logger.log(Logger.Level.INFO, "IllegalStateRuntimeException thrown as expected");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Incorrect exception received: " + e);
			pass = false;
		}

		try {
			logger.log(Logger.Level.INFO, "Set clientID on second context to value of clientID on first "
					+ "context (expect InvalidClientIDRuntimeException)");
			context2.setClientID("ctstest");
			logger.log(Logger.Level.ERROR, "No exception was thrown on ClientID that already exists");
			pass = false;
		} catch (InvalidClientIDRuntimeException e) {
			logger.log(Logger.Level.INFO, "InvalidClientIDRuntimeException thrown as expected");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Incorrect exception received: " + e);
			pass = false;
		} finally {
			try {
				if (context2 != null) {
					context2.close();
					context2 = null;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception in finally block: " + e);
				throw new Exception("setGetChangeClientIDTest", e);
			}
		}
		if (!pass) {
			throw new Exception("setGetChangeClientIDTest");
		}
	}

	/*
	 * @testName: setGetExceptionListenerTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1052; JMS:JAVADOC:980;
	 * 
	 * @test_Strategy: Test the following APIs:
	 * 
	 * JMSContext.setExceptionListener(ExceptionListener).
	 * JMSContext.getExceptionListener().
	 */
	@Test
	public void setGetExceptionListenerTest() throws Exception {
		boolean pass = true;
		try {
			MyExceptionListener expExceptionListener = new MyExceptionListener();
			logger.log(Logger.Level.INFO, "Calling setExceptionListener(" + expExceptionListener + ")");
			context.setExceptionListener(expExceptionListener);
			logger.log(Logger.Level.INFO,
					"Calling getExceptionListener and expect " + expExceptionListener + " to be returned");
			MyExceptionListener actExceptionListener = (MyExceptionListener) context.getExceptionListener();
			if (!actExceptionListener.equals(expExceptionListener)) {
				logger.log(Logger.Level.ERROR, "getExceptionListener() returned " + actExceptionListener + ", expected "
						+ expExceptionListener);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling setExceptionListener(null)");
			context.setExceptionListener(null);
			if (context.getExceptionListener() != null) {
				logger.log(Logger.Level.ERROR,
						"getExceptionListener() returned " + context.getExceptionListener() + ", expected null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("setGetExceptionListenerTest");
		}

		if (!pass) {
			throw new Exception("setGetExceptionListenerTest failed");
		}
	}

	/*
	 * @testName: setGetAsyncTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1182; JMS:JAVADOC:1255;
	 * 
	 * @test_Strategy: Test the following APIs:
	 * 
	 * JMSProducer.setAsync(CompletionListener). JMSProducer.getAsync().
	 */
	@Test
	public void setGetAsyncTest() throws Exception {
		boolean pass = true;
		try {
			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// Set and get asyncronous CompletionListener
			MyCompletionListener expCompletionListener = new MyCompletionListener();
			logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(" + expCompletionListener + ")");
			producer.setAsync(expCompletionListener);
			logger.log(Logger.Level.INFO,
					"Calling JMSProducer.getAsync() and expect " + expCompletionListener + " to be returned");
			MyCompletionListener actCompletionListener = (MyCompletionListener) producer.getAsync();
			if (!actCompletionListener.equals(expCompletionListener)) {
				logger.log(Logger.Level.ERROR,
						"getAsync() returned " + actCompletionListener + ", expected " + expCompletionListener);
				pass = false;
			}

			// Now set and get null asyncronous CompletionListener
			logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(null)");
			producer.setAsync(null);
			logger.log(Logger.Level.INFO, "Calling JMSProducer.getAsync() and expect NULL to be returned");
			if (producer.getAsync() != null) {
				logger.log(Logger.Level.ERROR, "getAsync() returned " + producer.getAsync() + ", expected null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("setGetAsyncTest");
		}

		if (!pass) {
			throw new Exception("setGetAsyncTest failed");
		}
	}

	/*
	 * @testName: startStopTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:1076; JMS:JAVADOC:1078;
	 * JMS:JAVADOC:942; JMS:JAVADOC:1102; JMS:SPEC:264; JMS:SPEC:264.5;
	 * 
	 * @test_Strategy: Test the following APIs:
	 * 
	 * ConnectionFactory.createContext(String, String, int) JMSContext.start()
	 * JMSContext.stop() JMSContext.createConsumer(Destination)
	 * JMSContext.createProducer() JMSProducer.send(Destination, Message)
	 * JMSConsumer.receive(long timeout)
	 * 
	 * 1. Create JMSContext with AUTO_ACKNOWLEDGE. This is done in the setup()
	 * routine. 2. Call stop. 3. Send x messages to a Queue. 4. Create a JMSConsumer
	 * to consume the messages in the Queue. 5. Try consuming messages from the
	 * Queue. Should not receive any messages since the connection has been stopped.
	 * 6. Call start. 7. Try consuming messages from the Queue. Should receive all
	 * messages from the Queue since the connection has been started.
	 */
	@Test
	public void startStopTest() throws Exception {
		boolean pass = true;
		try {
			TextMessage tempMsg = null;

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Stop delivery of incoming messages on JMSContext's connection
			logger.log(Logger.Level.INFO, "Call stop() to stop delivery of incoming messages");
			context.stop();

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// Send "numMessages" messages to Queue
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = context.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "startStopTest" + i);
				producer.send(destination, tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			// Try consuming a message from the Queue (should not receive a
			// message)
			logger.log(Logger.Level.INFO, "Try consuming a message on a STOPPED connection");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR, "Received a message on a STOPPED connection");
				logger.log(Logger.Level.ERROR, "Message is: " + tempMsg.getText());
				pass = false;
			}

			// Start delivery of incoming messages on JMSContext's connection
			logger.log(Logger.Level.INFO, "Call start() to start delivery of incoming messages");
			context.start();

			logger.log(Logger.Level.INFO, "Consume all the messages in the Queue on a STARTED connection");
			for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
				tempMsg = (TextMessage) consumer.receive(timeout);
				if (tempMsg == null) {
					logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned NULL");
					logger.log(Logger.Level.ERROR, "Message " + msgCount + " missing from Queue");
					pass = false;
				} else if (!tempMsg.getText().equals("Message " + msgCount)) {
					logger.log(Logger.Level.ERROR,
							"Received [" + tempMsg.getText() + "] expected [Message " + msgCount + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
				}
			}

			// Try to receive one more message (should return null for no more
			// messages)
			logger.log(Logger.Level.INFO, "Queue should now be empty");
			logger.log(Logger.Level.INFO, "Try consuming one more message should return NULL");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR, "JMSConsumer.receive() did not return NULL");
				logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned a message (unexpected)");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("startStopTest");
		}

		if (!pass) {
			throw new Exception("startStopTest failed");
		}
	}

	/*
	 * @testName: createContextTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:931; JMS:SPEC:265.3;
	 * 
	 * @test_Strategy: Creates a JMSContext with the default user identity and the
	 * specified sessionMode. Tests API:
	 * 
	 * JMSContext.createContext(int)
	 */
	@Test
	public void createContextTest() throws Exception {
		boolean pass = true;
		try {

			JMSContext newContext = null;

			// Test all possible session modes
			int expSessionMode[] = { JMSContext.SESSION_TRANSACTED, JMSContext.AUTO_ACKNOWLEDGE,
					JMSContext.CLIENT_ACKNOWLEDGE, JMSContext.DUPS_OK_ACKNOWLEDGE, };

			// Cycle through all session modes
			for (int i = 0; i < expSessionMode.length; i++) {
				logger.log(Logger.Level.INFO,
						"Creating context with session mode (" + printSessionMode(expSessionMode[i]) + ")");
				logger.log(Logger.Level.INFO, "Call API QueueConnectionFactory.createContext(int)");
				newContext = context.createContext(expSessionMode[i]);
				logger.log(Logger.Level.INFO, "Now call API JMSContext.getSessionMode()");
				logger.log(Logger.Level.INFO,
						"Calling getSessionMode and expect " + printSessionMode(expSessionMode[i]) + " to be returned");
				int actSessionMode = newContext.getSessionMode();
				if (actSessionMode != expSessionMode[i]) {
					logger.log(Logger.Level.ERROR, "getSessionMode() returned " + printSessionMode(actSessionMode)
							+ ", expected " + printSessionMode(expSessionMode[i]));
					pass = false;
				}
				newContext.close();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("createContextTest");
		}

		if (!pass) {
			throw new Exception("createContextTest failed");
		}
	}

	/*
	 * @testName: sendAndRecvCLTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:835; JMS:JAVADOC:1177;
	 * JMS:JAVADOC:1255; JMS:SPEC:275.1; JMS:SPEC:275.5; JMS:SPEC:275.8;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data as well as onCompletion() being called. Set some
	 * properties on JMSProducer and check that these properties exist on the
	 * returned message after the CompletionListener's onCompletion() method has
	 * been called.
	 * 
	 * JMSContext.createProducer() JMSProducer.setAsync(CompletionListener)
	 * JMSProducer.send(Destination, Message)
	 */
	@Test
	public void sendAndRecvCLTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";

		try {
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvCLTest1");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// ------------------------------------------------------------------------------
			// Set JMSProducer message properties
			// Set properties for boolean, byte, short, int, long, float,
			// double, and String.
			// ------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Set primitive property types on JMSProducer");
			producer.setProperty("TESTBOOLEAN", bool);
			producer.setProperty("TESTBYTE", bValue);
			producer.setProperty("TESTDOUBLE", nDouble);
			producer.setProperty("TESTFLOAT", nFloat);
			producer.setProperty("TESTINT", nInt);
			producer.setProperty("TESTLONG", nLong);
			producer.setProperty("TESTSHORT", nShort);
			producer.setProperty("TESTSTRING", "test");

			// ------------------------------------------------------------------------------
			// Set JMSProducer message properties
			// Set properties for Boolean, Byte, Short, Int, Long, Float,
			// Double, and String.
			// ------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Set Object property types on JMSProducer");
			producer.setProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
			producer.setProperty("OBJTESTBYTE", Byte.valueOf(bValue));
			producer.setProperty("OBJTESTDOUBLE", Double.valueOf(nDouble));
			producer.setProperty("OBJTESTFLOAT", Float.valueOf(nFloat));
			producer.setProperty("OBJTESTINT", Integer.valueOf(nInt));
			producer.setProperty("OBJTESTLONG", Long.valueOf(nLong));
			producer.setProperty("OBJTESTSHORT", Short.valueOf(nShort));
			producer.setProperty("OBJTESTSTRING", "test");

			logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(CompletionListener)");
			producer.setAsync(listener);
			logger.log(Logger.Level.INFO, "Calling JMSProducer.send(Destination,Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Poll listener until we receive TextMessage");
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			TextMessage actTextMessage = null;
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}

			// --------------------------------------------------------------------------------------
			// Retrieve the properties from the received TextMessage and verify
			// that they are correct
			// Get properties for boolean, byte, short, int, long, float,
			// double, and String.
			// -------------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Retrieve and verify that TextMessage message properties were set correctly");
			if (actTextMessage.getBooleanProperty("TESTBOOLEAN") == bool) {
				logger.log(Logger.Level.INFO, "Pass: getBooleanProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getBooleanProperty");
				pass = false;
			}
			if (actTextMessage.getByteProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getByteProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getByteProperty");
				pass = false;
			}
			if (actTextMessage.getLongProperty("TESTLONG") == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getLongProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getLongProperty");
				pass = false;
			}
			if (actTextMessage.getStringProperty("TESTSTRING").equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getStringProperty");
				pass = false;
			}
			if (actTextMessage.getDoubleProperty("TESTDOUBLE") == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getDoubleProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getDoubleProperty");
				pass = false;
			}
			if (actTextMessage.getFloatProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getFloatProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getFloatProperty");
				pass = false;
			}
			if (actTextMessage.getIntProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getIntProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getIntProperty");
				pass = false;
			}
			if (actTextMessage.getShortProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getShortProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getShortProperty");
				pass = false;
			}

			// --------------------------------------------------------------------------------------
			// Retrieve the properties from the received TextMessage and verify
			// that they are correct
			// Get properties for Boolean, Byte, Short, Integer, Long, Float,
			// Double, and String.
			// --------------------------------------------------------------------------------------
			if (((Boolean) actTextMessage.getObjectProperty("OBJTESTBOOLEAN")).booleanValue() == bool) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Boolean value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Boolean value returned from getObjectProperty");
				pass = false;
			}
			if (((Byte) actTextMessage.getObjectProperty("OBJTESTBYTE")).byteValue() == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Byte value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Byte value returned from getObjectProperty");
				pass = false;
			}
			if (((Long) actTextMessage.getObjectProperty("OBJTESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Long value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Long value returned from getObjectProperty");
				pass = false;
			}
			if (((String) actTextMessage.getObjectProperty("OBJTESTSTRING")).equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct String value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect String value returned from getObjectProperty");
				pass = false;
			}
			if (((Double) actTextMessage.getObjectProperty("OBJTESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Double value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Double value returned from getObjectProperty");
				pass = false;
			}
			if (((Float) actTextMessage.getObjectProperty("OBJTESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Float value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Float value returned from getObjectProperty");
				pass = false;
			}
			if (((Integer) actTextMessage.getObjectProperty("OBJTESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Integer value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Integer value returned from getObjectProperty");
				pass = false;
			}
			if (((Short) actTextMessage.getObjectProperty("OBJTESTSHORT")).shortValue() == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Short value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Short value returned from getObjectProperty");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvCLTest1", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvCLTest1 failed");
		}
	}

	/*
	 * @testName: sendAndRecvCLTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:835; JMS:JAVADOC:1177;
	 * JMS:JAVADOC:1255; JMS:JAVADOC:1259; JMS:JAVADOC:1303;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data as well as onCompletion() being called.
	 * 
	 * JMSContext.createProducer() JMSProducer.setDeliveryMode(int)
	 * JMSProducer.setPriority(int) JMSProducer.setTimeToLive(long)
	 * JMSProducer.setAsync(CompletionListener) JMSProducer.send(Destination,
	 * Message)
	 */
	@Test
	public void sendAndRecvCLTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvCLTest2");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(CompletionListener)");
			producer.setAsync(listener);
			logger.log(Logger.Level.INFO, "Set delivery mode, priority, and time to live");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setPriority(Message.DEFAULT_PRIORITY);
			producer.setTimeToLive(0L);
			logger.log(Logger.Level.INFO, "Calling JMSProducer.send(Destination,Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Poll listener until we receive TextMessage");
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			TextMessage actTextMessage = null;
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.PERSISTENT
					|| actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvCLTest2", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvCLTest2 failed");
		}
	}

	/*
	 * @testName: sendAndRecvMsgOfEachTypeCLTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:835; JMS:JAVADOC:1177;
	 * JMS:JAVADOC:1255; JMS:JAVADOC:1259; JMS:JAVADOC:1303;
	 * 
	 * @test_Strategy: Send and receive messages of each message type: Message,
	 * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Verify
	 * the send and recv of data as well as onCompletion() being called in
	 * CompletionListener.
	 * 
	 * JMSContext.createProducer() JMSProducer.setAsync(CompletionListener)
	 * JMSContext.createMessage() JMSContext.createBytesMessage()
	 * JMSContext.createMapMessage() JMSContext.createObjectMessage()
	 * JMSContext.createStreamMessage() JMSContext.createTextMessage(String)
	 * JMSProducer.send(Destination, Message)
	 */
	@Test
	public void sendAndRecvMsgOfEachTypeCLTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send and receive Message");
			Message msg = context.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			msg.setBooleanProperty("booleanProperty", true);
			MyCompletionListener2 listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, msg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive Message");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			Message actMessage = null;
			if (listener.isComplete())
				actMessage = (Message) listener.getMessage();

			if (actMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive Message");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in Message");
				if (actMessage.getBooleanProperty("booleanProperty") == true) {
					logger.log(Logger.Level.INFO, "booleanproperty is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanproperty is incorrect");
					pass = false;
				}
			}

			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send and receive BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			bMsg.reset();
			listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive BytesMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			BytesMessage bMsgRecv = null;
			if (listener.isComplete())
				bMsgRecv = (BytesMessage) listener.getMessage();
			if (bMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive BytesMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in BytesMessage");
				if (bMsgRecv.readByte() == (byte) 1) {
					logger.log(Logger.Level.INFO, "bytevalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "bytevalue is incorrect");
					pass = false;
				}
				if (bMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send and receive MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive MapMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			MapMessage mMsgRecv = null;
			if (listener.isComplete())
				mMsgRecv = (MapMessage) listener.getMessage();
			if (mMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive MapMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in MapMessage");
				Enumeration list = mMsgRecv.getMapNames();
				String name = null;
				while (list.hasMoreElements()) {
					name = (String) list.nextElement();
					if (name.equals("booleanvalue")) {
						if (mMsgRecv.getBoolean(name) == true) {
							logger.log(Logger.Level.INFO, "booleanvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "booleanvalue is incorrect");
							pass = false;
						}
					} else if (name.equals("intvalue")) {
						if (mMsgRecv.getInt(name) == 10) {
							logger.log(Logger.Level.INFO, "intvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "intvalue is incorrect");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Unexpected name of [" + name + "] in MapMessage");
						pass = false;
					}
				}
			}

			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send and receive ObjectMessage");
			StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage oMsg = context.createObjectMessage();
			oMsg.setObject(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, oMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive ObjectMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			ObjectMessage oMsgRecv = null;
			if (listener.isComplete())
				oMsgRecv = (ObjectMessage) listener.getMessage();
			if (oMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive ObjectMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in ObjectMessage");
				StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
				if (sb2.toString().equals(sb1.toString())) {
					logger.log(Logger.Level.INFO, "objectvalue is correct");
				} else {
					logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
					pass = false;
				}
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send and receive StreamMessage");
			StreamMessage sMsg = context.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			sMsg.reset();
			listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, sMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive StreamMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			StreamMessage sMsgRecv = null;
			if (listener.isComplete())
				sMsgRecv = (StreamMessage) listener.getMessage();
			if (sMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive StreamMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in StreamMessage");
				if (sMsgRecv.readBoolean() == true) {
					logger.log(Logger.Level.INFO, "booleanvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanvalue is incorrect");
					pass = false;
				}
				if (sMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Send and receive TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeCLTest");
			logger.log(Logger.Level.INFO, "Calling JMSProducer.send(Destination,Message)");
			listener = new MyCompletionListener2();
			producer = context.createProducer();
			producer.setAsync(listener);
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Poll listener until we receive TextMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			TextMessage actTextMessage = null;
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage");
			if (!actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvMsgOfEachTypeCLTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvMsgOfEachTypeCLTest failed");
		}
	}

	/*
	 * @testName: sendAndRecvMsgOfEachTypeMLTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:835; JMS:JAVADOC:1177;
	 * JMS:JAVADOC:1255; JMS:JAVADOC:1259; JMS:JAVADOC:1303;
	 * 
	 * @test_Strategy: Send and receive messages of each message type: Message,
	 * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Verify
	 * the send and recv of data as well as onMessage() being called in
	 * MessageListener.
	 * 
	 * JMSContext.createProducer() JMSProducer.setAsync(CompletionListener)
	 * JMSContext.createMessage() JMSContext.createBytesMessage()
	 * JMSContext.createMapMessage() JMSContext.createObjectMessage()
	 * JMSContext.createStreamMessage() JMSContext.createTextMessage(String)
	 * JMSProducer.send(Destination, Message)
	 */
	@Test
	public void sendAndRecvMsgOfEachTypeMLTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send and receive Message");
			Message msg = context.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			msg.setBooleanProperty("booleanProperty", true);
			MyMessageListener2 listener = new MyMessageListener2();
			consumer = context.createConsumer(destination);
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, msg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive Message");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			Message actMessage = null;
			if (listener.isComplete())
				actMessage = (Message) listener.getMessage();

			if (actMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive Message");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in Message");
				if (actMessage.getBooleanProperty("booleanProperty") == true) {
					logger.log(Logger.Level.INFO, "booleanproperty is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanproperty is incorrect");
					pass = false;
				}
			}

			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send and receive BytesMessage");
			BytesMessage bMsg = contextToCreateMsg.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			listener = new MyMessageListener2();
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive BytesMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			BytesMessage bMsgRecv = null;
			if (listener.isComplete())
				bMsgRecv = (BytesMessage) listener.getMessage();
			if (bMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive BytesMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in BytesMessage");
				if (bMsgRecv.readByte() == (byte) 1) {
					logger.log(Logger.Level.INFO, "bytevalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "bytevalue is incorrect");
					pass = false;
				}
				if (bMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send and receive MapMessage");
			MapMessage mMsg = contextToCreateMsg.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			listener = new MyMessageListener2();
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive MapMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			MapMessage mMsgRecv = null;
			if (listener.isComplete())
				mMsgRecv = (MapMessage) listener.getMessage();
			if (mMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive MapMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in MapMessage");
				Enumeration list = mMsgRecv.getMapNames();
				String name = null;
				while (list.hasMoreElements()) {
					name = (String) list.nextElement();
					if (name.equals("booleanvalue")) {
						if (mMsgRecv.getBoolean(name) == true) {
							logger.log(Logger.Level.INFO, "booleanvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "booleanvalue is incorrect");
							pass = false;
						}
					} else if (name.equals("intvalue")) {
						if (mMsgRecv.getInt(name) == 10) {
							logger.log(Logger.Level.INFO, "intvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "intvalue is incorrect");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Unexpected name of [" + name + "] in MapMessage");
						pass = false;
					}
				}
			}

			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send and receive ObjectMessage");
			StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage oMsg = contextToCreateMsg.createObjectMessage();
			oMsg.setObject(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			listener = new MyMessageListener2();
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, oMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive ObjectMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			ObjectMessage oMsgRecv = null;
			if (listener.isComplete())
				oMsgRecv = (ObjectMessage) listener.getMessage();
			if (oMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive ObjectMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in ObjectMessage");
				StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
				if (sb2.toString().equals(sb1.toString())) {
					logger.log(Logger.Level.INFO, "objectvalue is correct");
				} else {
					logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
					pass = false;
				}
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send and receive StreamMessage");
			StreamMessage sMsg = contextToCreateMsg.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			listener = new MyMessageListener2();
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, sMsg);
			logger.log(Logger.Level.INFO, "Poll listener until we receive StreamMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			StreamMessage sMsgRecv = null;
			if (listener.isComplete())
				sMsgRecv = (StreamMessage) listener.getMessage();
			if (sMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive StreamMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in StreamMessage");
				if (sMsgRecv.readBoolean() == true) {
					logger.log(Logger.Level.INFO, "booleanvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanvalue is incorrect");
					pass = false;
				}
				if (sMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Send and receive TextMessage");
			TextMessage expTextMessage = contextToCreateMsg.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgOfEachTypeMLTest");
			logger.log(Logger.Level.INFO, "Calling JMSProducer.send(Destination,Message)");
			listener = new MyMessageListener2();
			consumer.setMessageListener(listener);
			producer = contextToSendMsg.createProducer();
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Poll listener until we receive TextMessage");
			for (int i = 0; !listener.isComplete() && i < 15; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for message to arrive at listener");
				TestUtil.sleepSec(2);
			}
			TextMessage actTextMessage = null;
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage");
			if (!actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvMsgOfEachTypeMLTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvMsgOfEachTypeMLTest failed");
		}
	}

	/*
	 * @testName: messageOrderCLQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:275.2; JMS:SPEC:275.7;
	 * 
	 * @test_Strategy: Send async messages to a queue and receive them. Verify that
	 * the text of each matches the order of the text in the sent messages.
	 */
	@Test
	public void messageOrderCLQueueTest() throws Exception {
		boolean pass = true;
		try {
			TextMessage sentTextMessage;
			String text[] = new String[numMessages];

			// Create CompletionListener for Message to be sent
			logger.log(Logger.Level.INFO, "Creating MyCompletionListener");
			MyCompletionListener listener = new MyCompletionListener(numMessages);

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(CompletionListener)");
			producer.setAsync(listener);

			// create and send async messages to queue
			logger.log(Logger.Level.INFO, "Sending " + numMessages + " asynchronous messages to queue");
			for (int i = 0; i < numMessages; i++) {
				text[i] = "message order test " + i;
				sentTextMessage = context.createTextMessage();
				sentTextMessage.setText(text[i]);
				sentTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "messageOrderCLQueueTest" + i);
				logger.log(Logger.Level.INFO, "Sending TextMessage: " + sentTextMessage.getText());
				producer.send(destination, sentTextMessage);
			}

			logger.log(Logger.Level.INFO,
					"Poll listener until we receive all " + numMessages + " TextMessage's from CompletionListener");
			for (int i = 0; !listener.gotAllMsgs() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}

			for (int i = 0; i < numMessages; i++) {
				TextMessage actTextMessage = null;
				if (listener.haveMsg(i))
					actTextMessage = (TextMessage) listener.getMessage(i);
				if (actTextMessage == null) {
					logger.log(Logger.Level.INFO, "Did not receive TextMessage " + i + " (unexpected)");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + actTextMessage.getText());
					if (!actTextMessage.getText().equals(text[i])) {
						logger.log(Logger.Level.INFO, "Received message: " + actTextMessage.getText());
						logger.log(Logger.Level.INFO, "Should have received: " + text[i]);
						logger.log(Logger.Level.ERROR, "Received wrong message (wrong order)");
						pass = false;
					}
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("messageOrderCLQueueTest", e);
		}

		if (!pass)
			throw new Exception("messageOrderCLQueueTest failed");
	}

	/*
	 * @testName: commitRollbackTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:914; JMS:JAVADOC:995;
	 * JMS:JAVADOC:942; JMS:JAVADOC:1102; JMS:JAVADOC:847; JMS:SPEC:275.3;
	 * 
	 * @test_Strategy: Test the following APIs:
	 * 
	 * ConnectionFactory.createContext(String, String, int)
	 * JMSProducer.send(Destination, Message) JMSContext.commit()
	 * JMSContext.rollback() JMSContext.createConsumer(Destination)
	 * JMSConsumer.receive(long timeout)
	 * 
	 * 1. Create JMSContext with SESSION_TRANSACTED. This is done in the setup()
	 * routine. 2. Send x messages to a Queue. 3. Call rollback() to rollback the
	 * sent messages. 4. Create a JMSConsumer to consume the messages in the Queue.
	 * Should not receive any messages since the sent messages were rolled back.
	 * Verify that no messages are received. 5. Send x messages to a Queue. 6. Call
	 * commit() to commit the sent messages. 7. Create a JMSConsumer to consume the
	 * messages in the Queue. Should receive all the messages since the sent
	 * messages were committed. Verify that all messages are received.
	 */
	@Test
	public void commitRollbackTest() throws Exception {
		boolean pass = true;
		try {
			TextMessage tempMsg = null;

			// Close conttext created in setup()
			context.close();

			// create JMSContext with SESSION_TRANSACTED then create
			// consumer/producer
			logger.log(Logger.Level.INFO, "Create transacted JMSContext, JMSConsumer and JMSProducer");
			context = cf.createContext(user, password, JMSContext.SESSION_TRANSACTED);
			producer = context.createProducer();
			consumer = context.createConsumer(destination);

			// Send "numMessages" messages to Queue and call rollback
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue and call rollback()");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = context.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "commitRollbackTest" + i);
				producer.send(destination, tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			// Call rollback() to rollback the sent messages
			logger.log(Logger.Level.INFO, "Calling rollback() to rollback the sent messages");
			context.rollback();

			logger.log(Logger.Level.INFO, "Should not consume any messages in Queue since rollback() was called");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR,
						"Received message " + tempMsg.getText() + ", expected NULL (NO MESSAGES)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received no messages (CORRECT)");
			}

			// Send "numMessages" messages to Queue and call commit
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue and call commit()");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = context.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "commitRollbackTest" + i);
				producer.send(destination, tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			// Call commit() to commit the sent messages
			logger.log(Logger.Level.INFO, "Calling commit() to commit the sent messages");
			context.commit();

			logger.log(Logger.Level.INFO, "Should consume all messages in Queue since commit() was called");
			for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
				tempMsg = (TextMessage) consumer.receive(timeout);
				if (tempMsg == null) {
					logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned NULL");
					logger.log(Logger.Level.ERROR, "Message " + msgCount + " missing from Queue");
					pass = false;
				} else if (!tempMsg.getText().equals("Message " + msgCount)) {
					logger.log(Logger.Level.ERROR,
							"Received [" + tempMsg.getText() + "] expected [Message " + msgCount + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("commitRollbackTest");
		}

		if (!pass) {
			throw new Exception("commitRollbackTest failed");
		}
	}

	/*
	 * @testName: recoverAckTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:992; JMS:JAVADOC:909;
	 * 
	 * @test_Strategy: Send messages to destination in a CLIENT_ACKNOWLEDGE session.
	 * Receive all the messages without acknowledge and call recover on context.
	 * Receive for a second time all the messages and follow with an acknowledge.
	 * 
	 * JMSContext.recover() JMSContext.acknowledge()
	 */
	@Test
	public void recoverAckTest() throws Exception {
		boolean pass = true;
		TextMessage textMessage = null;
		try {
			// Close JMSContext created in setup() method
			logger.log(Logger.Level.INFO, "Close JMSContext created in setup() method");
			context.close();

			// Create JMSContext with CLIENT_ACKNOWLEDGE
			logger.log(Logger.Level.INFO, "Close JMSContext with CLIENT_ACKNOWLEDGE and create consumer/producer");
			context = cf.createContext(user, password, JMSContext.CLIENT_ACKNOWLEDGE);

			// Create JMSConsumer from JMSContext
			consumer = context.createConsumer(destination);

			// Create JMSProducer from JMSContext
			producer = context.createProducer();

			// send messages
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages");
			for (int i = 0; i < numMessages; i++) {
				String message = "text message " + i;
				textMessage = context.createTextMessage(message);
				textMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "recoverAckTest" + i);
				producer.send(destination, textMessage);
				logger.log(Logger.Level.INFO, "Sent message " + i);
			}

			// receive messages but do not acknowledge
			logger.log(Logger.Level.INFO, "Receive " + numMessages + " messages but do not acknowledge");
			for (int i = 0; i < numMessages; i++) {
				logger.log(Logger.Level.INFO, "Receiving message " + i);
				textMessage = (TextMessage) consumer.receive(timeout);
				if (textMessage == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Did not receive message " + i);
				} else {
					logger.log(Logger.Level.INFO, "Received message " + i);
				}
			}

			logger.log(Logger.Level.INFO, "Call JMSContext.recover()");
			context.recover();

			logger.log(Logger.Level.INFO, "Receive " + numMessages + " messages again then acknowledge");
			// receive messages a second time followed by acknowledge
			for (int i = 0; i < numMessages; i++) {
				logger.log(Logger.Level.INFO, "Receiving message " + i);
				textMessage = (TextMessage) consumer.receive(timeout);
				if (textMessage == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Did not receive message " + i);
				} else {
					logger.log(Logger.Level.INFO, "Received message " + i);
				}
			}

			// Acknowledge all messages
			logger.log(Logger.Level.INFO, "Acknowledge all messages by calling JMSContext.acknowledge()");
			context.acknowledge();

			logger.log(Logger.Level.INFO, "Now try receiving a message (should get NONE)");
			textMessage = (TextMessage) consumer.receive(timeout);
			if (textMessage != null) {
				logger.log(Logger.Level.ERROR, "Received message " + textMessage.getText() + " (Expected NONE)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received no message (Correct)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "recoverAckTest failed: ", e);
			throw new Exception("recoverAckTest failed", e);
		}

		if (!pass)
			throw new Exception("recoverAckTest failed!!!");
	}

	/*
	 * @testName: invalidDestinationRuntimeExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1237;
	 * 
	 * @test_Strategy: Test InvalidDestinationRuntimeException conditions from API
	 * methods with CompletionListener.
	 * 
	 * Tests the following exception conditions:
	 * 
	 * InvalidDestinationRuntimeException
	 */
	@Test
	public void invalidDestinationRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		Destination invalidDestination = null;
		Queue invalidQueue = null;
		String message = "Where are you!";
		Map<String, Object> mapMsgSend = new HashMap<String, Object>();
		mapMsgSend.put("StringValue", "sendAndRecvTest7");
		mapMsgSend.put("BooleanValue", true);
		mapMsgSend.put("IntValue", (int) 10);
		try {

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSConsumer");
			consumer = context.createConsumer(destination);

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// send to an invalid destination
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "invalidDestinationRuntimeExceptionTests");

			// Create CompetionListener
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Message) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(CompletionListener)");
				producer.setAsync(listener);
				logger.log(Logger.Level.INFO,
						"Calling JMSProducer.send(Destination, Message) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, expTextMessage);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				if (listener.isComplete()) {
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof InvalidDestinationRuntimeException) {
						logger.log(Logger.Level.INFO, "Exception is expected InvalidDestinationRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected InvalidDestinationRuntimeException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("invalidDestinationRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("invalidDestinationRuntimeExceptionTests failed");
		}
	}

	/*
	 * @testName: messageFormatRuntimeExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1236;
	 * 
	 * @test_Strategy: Test MessageFormatRuntimeException conditions from API
	 * methods with CompletionListener.
	 * 
	 * Tests the following exception conditions:
	 * 
	 * MessageFormatRuntimeException
	 */
	@Test
	public void messageFormatRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSConsumer");
			consumer = context.createConsumer(destination);

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// Create CompetionListener
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Message) for MessageFormatRuntimeException");
			try {
				logger.log(Logger.Level.INFO, "Calling JMSProducer.setAsync(CompletionListener)");
				producer.setAsync(listener);
				logger.log(Logger.Level.INFO,
						"Calling JMSProducer.send(Destination, Message) -> expect MessageFormatRuntimeException");
				producer.send(destination, (Message) null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				if (listener.isComplete()) {
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatRuntimeException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatRuntimeException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatRuntimeException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("messageFormatRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("messageFormatRuntimeExceptionTests failed");
		}
	}

	/*
	 * @testName: jMSRuntimeExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1235;
	 * 
	 * @test_Strategy: Test JMSRuntimeException conditions from API methods with
	 * CompletionListener.
	 * 
	 * Set delivery mode to -1 on JMSProducer and then try and send async message to
	 * CompletionListener. The CompletionListener MUST throw JMSRuntimeException.
	 * 
	 * Set priority to -1 on JMSProducer and then try and send async message to
	 * CompletionListener. The CompletionListener MUST throw JMSRuntimeException.
	 */
	@Test
	public void jMSRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSConsumer");
			consumer = context.createConsumer(destination);

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer");
			producer = context.createProducer();

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "jMSRuntimeExceptionTests");
			try {
				logger.log(Logger.Level.INFO, "Set completion listener");
				producer.setAsync(listener);
				logger.log(Logger.Level.INFO, "Try and set an invalid delivbery mode of -1 on send");
				producer.setDeliveryMode(-1);
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				if (listener.isComplete()) {
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSRuntimeException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR, "Exception is incorrect expected JMSRuntimeException, received "
								+ exception.getCause());
						pass = false;
					}
				}
			} catch (JMSRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSRuntimeException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Set completion listener");
				producer.setAsync(listener);
				listener.setComplete(false);
				logger.log(Logger.Level.INFO, "Try and set an invalid priority of -1 on send");
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				producer.setPriority(-1);
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				if (listener.isComplete()) {
					listener.setComplete(false);
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSRuntimeException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR, "Exception is incorrect expected JMSRuntimeException, received "
								+ exception.getCause());
						pass = false;
					}
				}
			} catch (JMSRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSRuntimeException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("jMSRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("jMSRuntimeExceptionTests failed");
		}
	}

	/*
	 * @testName: illegalStateRuntimeExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1353; JMS:JAVADOC:1354; JMS:JAVADOC:917;
	 * JMS:JAVADOC:997;
	 * 
	 * @test_Strategy: Test IllegalStateRuntimeException conditions.
	 * 
	 * 1) Calling JMSContext.stop() in a MessageListener MUST throw
	 * IllegalStateRuntimeException. 2) Calling JMSContext.close() from a
	 * CompletionListener MUST throw IllegalStateRuntimeException. 3) Calling
	 * JMSContext.commit() or JMSContext.rollback() in a CompletionListener MUST
	 * throw IllegalStateRuntimeException.
	 */
	@Test
	public void illegalStateRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// Create JMSProducer/JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Creating JMSProducer/JMSConsumer");
			producer = contextToSendMsg.createProducer();
			consumer = context.createConsumer(destination);

			logger.log(Logger.Level.INFO, "-----------------------------------------------------");
			logger.log(Logger.Level.INFO, "CompletionListener IllegalStateRuntimeException Tests");
			logger.log(Logger.Level.INFO, "-----------------------------------------------------");
			logger.log(Logger.Level.INFO,
					"Testing JMSContext.commit() from CompletionListener (expect IllegalStateRuntimeException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = context.createTextMessage("Call commit method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call JMSContext.commit() (expect IllegalStateRuntimeException)");
				MyCompletionListener listener = new MyCompletionListener(context);
				producer.setAsync(listener);
				producer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from JMSContext.commit()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateRuntimeException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.rollback() from CompletionListener (expect IllegalStateRuntimeException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = context.createTextMessage("Call rollback method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call JMSContext.rollback() (expect IllegalStateRuntimeException)");
				MyCompletionListener listener = new MyCompletionListener(contextToSendMsg);
				producer.setAsync(listener);
				listener.setComplete(false);
				producer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from JMSContext.rollback()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateRuntimeException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.stop() from CompletionListener is allowed (expect no exception)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = context.createTextMessage("Call stop method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO, "CompletionListener will call JMSContext.stop() (expect no exception)");
				MyCompletionListener listener = new MyCompletionListener(contextToSendMsg);
				producer.setAsync(listener);
				listener.setComplete(false);
				producer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO,
						"Check if we got correct exception from JMSContext.stop() (expect no exception)");
				if (listener.gotException()) {
					logger.log(Logger.Level.ERROR, "Got exception calling JMSContext.stop() (expected no exception)");
					logger.log(Logger.Level.ERROR, "Exception was: " + listener.getException());
					pass = false;
				} else {
					logger.log(Logger.Level.ERROR, "Got no exception (Correct)");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, received " + e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught exception calling cleanup: " + e);
			}
			try {
				context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
				contextToSendMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
				producer = contextToSendMsg.createProducer();
				consumer = context.createConsumer(destination);
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught exception creating JMSContext: " + e);
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.close() from CompletionListener (expect IllegalStateRuntimeException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = context.createTextMessage("Call close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call JMSContext.close() (expect IllegalStateRuntimeException)");
				MyCompletionListener listener = new MyCompletionListener(contextToSendMsg);
				producer.setAsync(listener);
				listener.setComplete(false);
				producer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from JMSContext.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateRuntimeException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, received " + e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught exception calling cleanup: " + e);
			}
			try {
				context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
				contextToSendMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
				producer = contextToSendMsg.createProducer();
				consumer = context.createConsumer(destination);
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught exception creating JMSContext: " + e);
			}

			logger.log(Logger.Level.INFO, "--------------------------------------------------");
			logger.log(Logger.Level.INFO, "MessageListener IllegalStateRuntimeException Tests");
			logger.log(Logger.Level.INFO, "--------------------------------------------------");
			logger.log(Logger.Level.INFO,
					"Testing JMSContext.stop() from MessageListener (expect IllegalStateRuntimeException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = context.createTextMessage("Call stop method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTests");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				logger.log(Logger.Level.INFO,
						"MessageListener will call JMSContext.stop() (expect IllegalStateRuntimeException)");
				MyMessageListener listener = new MyMessageListener(context);
				consumer.setMessageListener(listener);
				listener.setComplete(false);
				producer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from JMSContext.stop()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateRuntimeException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateRuntimeException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateRuntimeException, received " + e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught exception calling cleanup: " + e);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("illegalStateRuntimeExceptionTests failed");
		}
	}
}
