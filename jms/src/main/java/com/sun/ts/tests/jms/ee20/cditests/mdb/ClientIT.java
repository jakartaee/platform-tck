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

package com.sun.ts.tests.jms.ee20.cditests.mdb;

import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.porting.TSURL;

import jakarta.ejb.EJB;


public class ClientIT {
	// The webserver defaults
	private static final String PROTOCOL = "http";

	private static final String HOSTNAME = "localhost";

	private static final int PORTNUM = 8000;

	private TSURL ctsurl = new TSURL();

	private Properties props = null;

	private String hostname = HOSTNAME;

	private int portnum = PORTNUM;

	// URL properties used by the test
	private URL url = null;

	private URLConnection urlConn = null;

	private String SERVLET = "/cditestsmdb_web/ServletTest";

	@EJB(name = "ejb/CDITestsMDBClntBean")
	static EjbClientIF ejbclient;

	private static final long serialVersionUID = 1L;

	long timeout;

	String user;

	String password;

	String mode;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

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
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}
			if (hostname == null) {
				throw new Exception("'webServerHost' is null");
			}
			try {
				portnum = Integer.parseInt(System.getProperty("webServerPort"));
			} catch (Exception e) {
				throw new Exception("'webServerPort' in must be a number");
			}
			logger.log(Logger.Level.INFO, "AppClient DEBUG: ejbclient=" + ejbclient);
			if (ejbclient == null) {
				throw new Exception("setup failed: ejbclient injection failure");
			} else {
				// ejbclient.init(p);
			}
		} catch (Exception e) {
			throw new Exception("setup failed:", e);
		}
		// ejbclient.init(p);
		logger.log(Logger.Level.INFO, "setup ok");
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}

	/*
	 * @testName: testCDIInjectionOfMDBWithQueueReplyFromEjb
	 * 
	 * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
	 * JMS:JAVADOC:1128; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Test CDI injection in a MDB. Send a message to the MDB and
	 * MDB sends a reply back to the Reply Queue using the CDI injected JMSContext.
	 */
	@Test
	public void testCDIInjectionOfMDBWithQueueReplyFromEjb() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------------------------------------");
			logger.log(Logger.Level.INFO, "testCDIInjectionOfMDBWithQueueReplyFromEjb");
			logger.log(Logger.Level.INFO, "------------------------------------------");
			boolean passEjb = ejbclient.echo("testCDIInjectionOfMDBWithQueueReplyFromEjb");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "CDI injection test passed from Ejb");
			}
			passEjb = ejbclient.echo("testCDIInjectionOfMDBWithQueueReplyFromEjb");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "CDI injection test passed from Ejb");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			pass = false;
		}

		if (!pass) {
			throw new Exception("testCDIInjectionOfMDBWithQueueReplyFromEjb failed");
		}
	}

	/*
	 * @testName: testCDIInjectionOfMDBWithTopicReplyFromEjb
	 * 
	 * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
	 * JMS:JAVADOC:1128; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Test CDI injection in a MDB. Send a message to the MDB and
	 * MDB sends a reply back to the Reply Topic using the CDI injected JMSContext.
	 */
	@Test
	public void testCDIInjectionOfMDBWithTopicReplyFromEjb() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------------------------------------");
			logger.log(Logger.Level.INFO, "testCDIInjectionOfMDBWithTopicReplyFromEjb");
			logger.log(Logger.Level.INFO, "------------------------------------------");
			boolean passEjb = ejbclient.echo("testCDIInjectionOfMDBWithTopicReplyFromEjb");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "CDI injection test passed from Ejb");
			}
			passEjb = ejbclient.echo("testCDIInjectionOfMDBWithTopicReplyFromEjb");
			if (!passEjb) {
				pass = false;
				logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			} else {
				logger.log(Logger.Level.INFO, "CDI injection test passed from Ejb");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "CDI injection test failed from Ejb");
			pass = false;
		}

		if (!pass) {
			throw new Exception("testCDIInjectionOfMDBWithTopicReplyFromEjb failed");
		}
	}
}
