/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.ee.ejb.sessionTtests;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.TestsT;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;

public class Client {

	private static final String testName = "com.sun.ts.tests.jms.ee.ejb.sessionTtests.Client";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private transient Connection connr = null;

	@Resource(name = "jms/DURABLE_SUB_CONNECTION_FACTORY")
	private static ConnectionFactory cf;

	@Resource(name = "jms/MY_TOPIC")
	// private static Destination testDestination;
	private static Topic testDestination;

	private String name = "ctssub";

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	@EJB(name = "ejb/SessionTestsT")
	private static TestsT beanRef;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue and/or
	 * Topic Connection, as well as a default Queue and Topic. TestsT that require
	 * multiple Destinations create the extras within the test
	 * 
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * 
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {

			if (beanRef == null) {
				throw new Exception("@EJB injection failed");
			}

			if (cf == null || testDestination == null) {
				throw new Exception("@Resource injection failed");
			}

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

			beanRef.initLogging(props);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/*
	 * cleanup() is called after each test
	 */
	@AfterEach
	public void cleanup() throws Exception {
	}

	/*
	 * @testName: simpleSendReceiveT
	 * 
	 * @assertion_ids: JMS:JAVADOC:504; JMS:JAVADOC:510; JMS:JAVADOC:242;
	 * JMS:JAVADOC:244; JMS:JAVADOC:317; JMS:JAVADOC:334; JMS:JAVADOC:221;
	 * 
	 * @test_Strategy: Create a Text Message, send use a MessageProducer and receive
	 * it use a MessageConsumer via a Topic
	 */
	@Test
	public void simpleSendReceiveT() throws Exception {
		String testMessage = "Just a test from simpleSendReceiveT";
		String messageReceived = null;

		try {
			connr = cf.createConnection(user, password);
			if (connr.getClientID() == null)
				connr.setClientID("cts");

			Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);
			TopicSubscriber recr = sessr.createDurableSubscriber(testDestination, name);

			try {
				recr.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception closing topic subscriber: ", e);
			}
			try {
				connr.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception closing connection: ", e);
			}

			beanRef.sendTextMessage_CT(testName, testMessage);
			messageReceived = beanRef.receiveTextMessage_CT();

			// Check to see if correct message received
			if (messageReceived == null) {
				throw new Exception("Null message received!");
			} else if (!messageReceived.equals(testMessage)) {
				throw new Exception("EJB didn't get the right message");
			} else {
				logger.log(Logger.Level.INFO, "Correct Message received");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("simpleSendReceiveT");
		} finally {
			try {
				connr = cf.createConnection(user, password);

				Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);

				try {
					sessr.unsubscribe(name);
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Unexpected exception unsubscribing: ", e);
				}

				try {
					connr.close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Unexpected exception closing connection: ", e);
				}

			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception creating Connection: ", e);
			}

			try {
				if (null != beanRef)
					beanRef.remove();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "[Client] Ignoring Exception on " + "bean remove", e);
			}
		}
	}
}
