/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

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
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//-------------------------------------
// JMS Destination Resource Definitions
//-------------------------------------
@JMSDestinationDefinition(description = "Define Queue ServletMyTestQueue", interfaceName = "jakarta.jms.Queue", name = "java:global/env/ServletMyTestQueue", destinationName = "ServletMyTestQueue")

@JMSDestinationDefinition(description = "Define Topic ServletMyTestTopic", interfaceName = "jakarta.jms.Topic", name = "java:app/env/ServletMyTestTopic", destinationName = "ServletMyTestTopic")

//-------------------------------------------
// JMS ConnectionFactory Resource Definitions
//-------------------------------------------
@JMSConnectionFactoryDefinition(description = "Define ConnectionFactory ServletMyTestConnectionFactory", interfaceName = "jakarta.jms.ConnectionFactory", name = "java:global/ServletMyTestConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define QueueConnectionFactory ServletMyTestQueueConnectionFactory", interfaceName = "jakarta.jms.QueueConnectionFactory", name = "java:app/ServletMyTestQueueConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define TopicConnectionFactory ServletMyTestTopicConnectionFactory", interfaceName = "jakarta.jms.TopicConnectionFactory", name = "java:module/ServletMyTestTopicConnectionFactory", user = "j2ee", password = "j2ee")

@JMSConnectionFactoryDefinition(description = "Define Durable TopicConnectionFactory ServletMyTestDurableTopicConnectionFactory", interfaceName = "jakarta.jms.TopicConnectionFactory", name = "java:comp/env/jms/ServletMyTestDurableTopicConnectionFactory", user = "j2ee", password = "j2ee", clientId = "MyClientID", properties = {
		"Property1=10", "Property2=20" }, transactional = false, maxPoolSize = 30, minPoolSize = 20)

@WebServlet("/ServletTest")
public class ServletClient extends HttpServlet {
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
	protected Properties harnessProps = null;

	// properties read
	protected long timeout;

	protected String user;

	protected String password;

	protected String mode;

	private static final Logger logger = (Logger) System.getLogger(ServletClient.class.getName());

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

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		boolean pass = true;
		Properties p = new Properties();
		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			String test = System.getProperty("TEST");
			System.out.println("doGet: test to execute is: " + test);
			if (test.equals("sendAndRecvQueueTestFromServletClient")) {
				if (sendAndRecvQueueTestFromServletClient())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("sendAndRecvTopicTestFromServletClient")) {
				if (sendAndRecvTopicTestFromServletClient())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("checkClientIDTestFromServletClient")) {
				if (checkClientIDTestFromServletClient())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else {
				p.setProperty("TESTRESULT", "fail");
			}
			doCleanup();
			p.list(out);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "doGet: Exception: " + e);
			System.out.println("doGet: Exception: " + e);
			p.setProperty("TESTRESULT", "fail");
			p.list(out);
		}
		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		harnessProps = new Properties();
		Enumeration enumlist = req.getParameterNames();
		while (enumlist.hasMoreElements()) {
			String name = (String) enumlist.nextElement();
			String value = req.getParameter(name);
			harnessProps.setProperty(name, value);
		}

		try {
			TestUtil.init(harnessProps);
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
				throw new Exception("'user' in must not be null ");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
			}
			doLookupJMSObjects();
		} catch (Exception e) {
			System.out.println("doPost: Exception: " + e);
			e.printStackTrace();
			throw new ServletException("unable to initialize remote logging");
		}
		doGet(req, res);
		harnessProps = null;
	}

	/*
	 * Lookup JMS Connection Factory and Destination Objects
	 */
	private void doLookupJMSObjects() throws Exception {
		try {
			logger.log(Logger.Level.INFO, "Lookup JMS factories defined by @JMSConnectionFactoryDefinitions");
			logger.log(Logger.Level.INFO, "Lookup JMS destinations defined by @JMSDestinationDefinitions");
			TSNamingContext namingctx = new TSNamingContext();
			logger.log(Logger.Level.INFO, "Lookup java:comp/DefaultJMSConnectionFactory");
			dcf = (ConnectionFactory) namingctx.lookup("java:comp/DefaultJMSConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:global/ServletMyTestConnectionFactory");
			cf = (ConnectionFactory) namingctx.lookup("java:global/ServletMyTestConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:app/ServletMyTestQueueConnectionFactory");
			qcf = (QueueConnectionFactory) namingctx.lookup("java:app/ServletMyTestQueueConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:module/ServletMyTestTopicConnectionFactory");
			tcf = (TopicConnectionFactory) namingctx.lookup("java:module/ServletMyTestTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:comp/env/jms/ServletMyTestDurableTopicConnectionFactory");
			dtcf = (TopicConnectionFactory) namingctx
					.lookup("java:comp/env/jms/ServletMyTestDurableTopicConnectionFactory");
			logger.log(Logger.Level.INFO, "Lookup java:global/env/ServletMyTestQueue");
			queue = (Queue) namingctx.lookup("java:global/env/ServletMyTestQueue");
			logger.log(Logger.Level.INFO, "Lookup java:app/env/ServletMyTestTopic");
			topic = (Topic) namingctx.lookup("java:app/env/ServletMyTestTopic");

			logger.log(Logger.Level.INFO, "Create JMSContext, JMSProducer's and JMSConsumer's");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			producerQ = context.createProducer();
			consumerQ = context.createConsumer(queue);
			producerT = context.createProducer();
			consumerT = context.createConsumer(topic);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw e;
		}
	}

	public boolean sendAndRecvQueueTestFromServletClient() {
		boolean pass = true;
		String message = "Where are you!";
		try {
			queueTest = true;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvQueueTestFromServletClient");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerQ.send(queue, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
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
			pass = false;
		}

		return pass;
	}

	public boolean sendAndRecvTopicTestFromServletClient() {
		boolean pass = true;
		String message = "Where are you!";
		try {
			queueTest = false;
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage via JMSContext.createTextMessage(String)");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTopicTestFromServletClient");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producerT.send(topic, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
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
			pass = false;
		}
		return pass;
	}

	public boolean checkClientIDTestFromServletClient() {
		boolean pass = true;
		JMSContext context = null;
		try {
			queueTest = false;
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
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				if (context != null)
					context.close();
				doCleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error in cleanup " + e);
			}
		}
		return pass;
	}

}
