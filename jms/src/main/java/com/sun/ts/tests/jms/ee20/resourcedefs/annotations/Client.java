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

import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJB;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
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
@JMSDestinationDefinition(description = "Define Queue AppClientMyTestQueue", interfaceName = "jakarta.jms.Queue", name = "java:global/env/AppClientMyTestQueue", destinationName = "AppClientMyTestQueue")

@JMSDestinationDefinition(description = "Define Topic AppClientMyTestTopic", interfaceName = "jakarta.jms.Topic", name = "java:app/env/AppClientMyTestTopic", destinationName = "AppClientMyTestTopic")

//-------------------------------------------
// JMS ConnectionFactory Resource Definitions
//-------------------------------------------
@JMSConnectionFactoryDefinition(description = "Define ConnectionFactory AppClientMyTestConnectionFactory", interfaceName = "jakarta.jms.ConnectionFactory", name = "java:global/AppClientMyTestConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define QueueConnectionFactory AppClientMyTestQueueConnectionFactory", interfaceName = "jakarta.jms.QueueConnectionFactory", name = "java:app/AppClientMyTestQueueConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define TopicConnectionFactory AppClientMyTestTopicConnectionFactory", interfaceName = "jakarta.jms.TopicConnectionFactory", name = "java:module/AppClientMyTestTopicConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define Durable TopicConnectionFactory AppClientMyTestDurableTopicConnectionFactory", interfaceName = "jakarta.jms.TopicConnectionFactory", name = "java:comp/env/jms/AppClientMyTestDurableTopicConnectionFactory", user = "j2ee", password = "j2ee", clientId = "MyClientID", properties = {
		"Property1=10", "Property2=20" }, transactional = false, maxPoolSize = 30, minPoolSize = 20)

public class Client {
	private static final long serialVersionUID = 1L;

	// JMS objects
	protected transient ConnectionFactory dcf = null;

	protected transient ConnectionFactory cf = null;

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

	// properties read
	protected long timeout;

	protected String user;

	protected String password;

	protected String mode;

	// The webserver defaults (overidden by harness properties)
	private static final String PROTOCOL = "http";

	private static final String HOSTNAME = "localhost";

	private static final int PORTNUM = 8000;

	private TSURL ctsurl = new TSURL();

	private String hostname = HOSTNAME;

	private int portnum = PORTNUM;

	// URL properties used by the test
	private URL url = null;

	private transient URLConnection urlConn = null;

	private String SERVLET = "/resourcedefs_annotations_web/ServletTest";

	private String JSP = "/resourcedefs_annotations_web/JspClient.jsp";

	@EJB(name = "ejb/JMSResourceDefsEjbClientBean")
	static EjbClientIF ejbclient;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	/* Test setup */

	/*
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * webServerHost; webServerPort;
	 */
	@BeforeEach
	public void setup() throws Exception {
		boolean pass = true;
		try {
			// get props
			timeout = Integer.parseInt(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			hostname = System.getProperty("webServerHost");

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
			if (hostname == null) {
				throw new Exception("'webServerHost' in must not be null");
			}
			try {
				portnum = Integer.parseInt(System.getProperty("webServerPort"));
			} catch (Exception e) {
				throw new Exception("'webServerPort' in must be a number");
			}
			if (ejbclient == null) {
				throw new Exception("setup failed: ejbclient injection failure");
			}
		} catch (Exception e) {
			throw new Exception("setup failed:", e);
		}
		logger.log(Logger.Level.INFO, "setup ok");
	}

	/*
	 * cleanup() is called after each test
	 * 
	 * @exception Fault
	 */
	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}

	/*
	 * Lookup JMS Connection Factory and Destination Objects
	 */
	private void doLookupJMSObjects() throws Exception {
		try {
			logger.log(Logger.Level.INFO, "Lookup JMS factories defined in @JMSConnectionFactoryDefinitions");
			logger.log(Logger.Level.INFO, "Lookup JMS destinations defined in @JMSDestinationDefinitions");
			TSNamingContext namingctx = new TSNamingContext();
			logger.log(Logger.Level.INFO, "Lookup java:comp/DefaultJMSConnectionFactory");
			dcf = (ConnectionFactory) namingctx.lookup("java:comp/DefaultJMSConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:global/AppClientMyTestConnectionFactory");
			cf = (ConnectionFactory) namingctx.lookup("java:global/AppClientMyTestConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:app/AppClientMyTestQueueConnectionFactory");
			qcf = (QueueConnectionFactory) namingctx.lookup("java:app/AppClientMyTestQueueConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:module/AppClientMyTestTopicConnectionFactory");
			tcf = (TopicConnectionFactory) namingctx.lookup("java:module/AppClientMyTestTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:comp/env/jms/AppClientMyTestDurableTopicConnectionFactory");
			dtcf = (TopicConnectionFactory) namingctx
					.lookup("java:comp/env/jms/AppClientMyTestDurableTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:global/env/AppClientMyTestQueue");
			queue = (Queue) namingctx.lookup("java:global/env/AppClientMyTestQueue");
			logger.log(Logger.Level.INFO, "Lookup java:app/env/AppClientMyTestTopic");
			topic = (Topic) namingctx.lookup("java:app/env/AppClientMyTestTopic");

			logger.log(Logger.Level.INFO, "Create JMSContext, JMSProducer's and JMSConsumer's");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			producerQ = context.createProducer();
			consumerQ = context.createConsumer(queue);
			producerT = context.createProducer();
			consumerT = context.createConsumer(topic);
		} catch (Exception e) {
			throw new Exception("doLookupJMSObjects failed:", e);
		}
	}

	/*
	 * doCleanup()
	 */
	private void doCleanup() throws Exception {
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
			throw new Exception("doCleanup failed!", e);
		}
	}

	/*
	 * @testName: sendAndRecvQueueTestFromAppClient
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
	public void sendAndRecvQueueTestFromAppClient() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			doLookupJMSObjects();
			queueTest = true;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvQueueTestFromAppClient");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerQ.send(queue, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
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
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("sendAndRecvQueueTestFromAppClient", e);
		} finally {
			try {
				doCleanup();
			} catch (Exception e) {
				throw new Exception("doCleanup failed: ", e);
			}
		}

		if (!pass) {
			throw new Exception("sendAndRecvQueueTestFromAppClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvTopicTestFromAppClient
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
	public void sendAndRecvTopicTestFromAppClient() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			doLookupJMSObjects();
			queueTest = false;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTopicTestFromAppClient");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerT.send(topic, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
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
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("sendAndRecvTopicTestFromAppClient", e);
		} finally {
			try {
				doCleanup();
			} catch (Exception e) {
				throw new Exception("doCleanup failed: ", e);
			}
		}

		if (!pass) {
			throw new Exception("sendAndRecvTopicTestFromAppClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvQueueTestFromServletClient
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
	public void sendAndRecvQueueTestFromServletClient() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "-------------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvQueueTestFromServletClient");
			logger.log(Logger.Level.INFO, "-------------------------------------");
			url = ctsurl.getURL("http", hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, "Servlet URL: " + url);
			props.setProperty("TEST", "sendAndRecvQueueTestFromServletClient");
			urlConn = TestUtil.sendPostData(props, url);
			Properties p = TestUtil.getResponseProperties(urlConn);
			String passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Servlet");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Servlet");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Servlet");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Servlet");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Servlet");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Servlet");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvQueueTestFromServletClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvTopicTestFromServletClient
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
	public void sendAndRecvTopicTestFromServletClient() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "-------------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvTopicTestFromServletClient");
			logger.log(Logger.Level.INFO, "-------------------------------------");
			url = ctsurl.getURL("http", hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, "Servlet URL: " + url);
			props.setProperty("TEST", "sendAndRecvTopicTestFromServletClient");
			urlConn = TestUtil.sendPostData(props, url);
			Properties p = TestUtil.getResponseProperties(urlConn);
			String passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Servlet");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Servlet");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Servlet");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Servlet");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Servlet");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Servlet");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvTopicTestFromServletClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvQueueTestFromJspClient
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
	public void sendAndRecvQueueTestFromJspClient() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "---------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvQueueTestFromJspClient");
			logger.log(Logger.Level.INFO, "---------------------------------");
			url = ctsurl.getURL("http", hostname, portnum, JSP);
			logger.log(Logger.Level.INFO, "Jsp URL: " + url);
			props.setProperty("TEST", "sendAndRecvQueueTestFromJspClient");
			urlConn = TestUtil.sendPostData(props, url);
			Properties p = TestUtil.getResponseProperties(urlConn);
			String passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Jsp");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Jsp");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Jsp");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Jsp");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Jsp");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Jsp");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvQueueTestFromJspClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvTopicTestFromJspClient
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
	public void sendAndRecvTopicTestFromJspClient() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "---------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvTopicTestFromJspClient");
			logger.log(Logger.Level.INFO, "---------------------------------");
			url = ctsurl.getURL("http", hostname, portnum, JSP);
			logger.log(Logger.Level.INFO, "Jsp URL: " + url);
			props.setProperty("TEST", "sendAndRecvTopicTestFromJspClient");
			urlConn = TestUtil.sendPostData(props, url);
			Properties p = TestUtil.getResponseProperties(urlConn);
			String passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Jsp");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Jsp");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Jsp");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Jsp");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Jsp");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Jsp");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvTopicTestFromJspClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvQueueTestFromEjbClient
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
	public void sendAndRecvQueueTestFromEjbClient() throws Exception {
		boolean pass = true;
		try {
			ejbclient.init(props);
			logger.log(Logger.Level.INFO, "---------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvQueueTestFromEjbClient");
			logger.log(Logger.Level.INFO, "---------------------------------");
			boolean passEjb = ejbclient.echo("sendAndRecvQueueTestFromEjbClient");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Ejb");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Ejb");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Ejb");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Ejb");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Ejb");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvQueueTestFromEjbClient failed");
		}
	}

	/*
	 * @testName: sendAndRecvTopicTestFromEjbClient
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
	public void sendAndRecvTopicTestFromEjbClient() throws Exception {
		boolean pass = true;
		try {
			ejbclient.init(props);
			logger.log(Logger.Level.INFO, "---------------------------------");
			logger.log(Logger.Level.INFO, "sendAndRecvTopicTestFromEjbClient");
			logger.log(Logger.Level.INFO, "---------------------------------");
			boolean passEjb = ejbclient.echo("sendAndRecvTopicTestFromEjbClient");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Ejb");
				logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "JMSConnectionFactoryDefinitions test passed from Ejb");
				logger.log(Logger.Level.INFO, "JMSDestinationDefinitions test passed from Ejb");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "JMSConnectionFactoryDefinitions test failed from Ejb");
			logger.log(Logger.Level.ERROR, "JMSDestinationDefinitions test failed from Ejb");
			pass = false;
		}

		if (!pass) {
			throw new Exception("sendAndRecvTopicTestFromEjbClient failed");
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
			doLookupJMSObjects();
			logger.log(Logger.Level.INFO, "==============================================================");
			logger.log(Logger.Level.INFO, "Verify admin configured client id is MyClientID from AppClient");
			logger.log(Logger.Level.INFO, "==============================================================");
			logger.log(Logger.Level.INFO, "Create JMSContext from durable topic connection factory");
			logger.log(Logger.Level.INFO, "Check the client id which is configured as MyClientID in the "
					+ "JMSConnectionFactoryDefinition annotation");
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
			context.close();
			context = null;
			try {
				doCleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error in cleanup");
			}
			logger.log(Logger.Level.INFO, "==================================================================");
			logger.log(Logger.Level.INFO, "Verify admin configured client id is MyClientID from ServletClient");
			logger.log(Logger.Level.INFO, "==================================================================");
			url = ctsurl.getURL("http", hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, "Servlet URL: " + url);
			props.setProperty("TEST", "checkClientIDTestFromServletClient");
			urlConn = TestUtil.sendPostData(props, url);
			Properties p = TestUtil.getResponseProperties(urlConn);
			String passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Check ClientID test failed from Servlet");
			} else {
				logger.log(Logger.Level.INFO, "Check ClientID test passed from Servlet");
			}
			logger.log(Logger.Level.INFO, "==============================================================");
			logger.log(Logger.Level.INFO, "Verify admin configured client id is MyClientID from JspClient");
			logger.log(Logger.Level.INFO, "==============================================================");
			url = ctsurl.getURL("http", hostname, portnum, JSP);
			logger.log(Logger.Level.INFO, "Jsp URL: " + url);
			props.setProperty("TEST", "checkClientIDTestFromJspClient");
			urlConn = TestUtil.sendPostData(props, url);
			p = TestUtil.getResponseProperties(urlConn);
			passStr = System.getProperty("TESTRESULT");
			if (passStr.equals("fail")) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Check ClientID test failed from Jsp");
			} else {
				logger.log(Logger.Level.INFO, "Check ClientID test passed from Jsp");
			}
			logger.log(Logger.Level.INFO, "==============================================================");
			logger.log(Logger.Level.INFO, "Verify admin configured client id is MyClientID from EjbClient");
			logger.log(Logger.Level.INFO, "==============================================================");
			boolean passEjb = ejbclient.echo("checkClientIDTestFromEjbClient");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Check ClientID test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "Check ClientID test passed from Ejb");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("checkClientIDOnDurableConnFactoryTest", e);
		} finally {
			try {
				if (context != null)
					context.close();
				doCleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error cleanup " + e);
			}
		}

		if (!pass) {
			throw new Exception("checkClientIDOnDurableConnFactoryTest failed");
		}
	}

}
