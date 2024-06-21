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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnection;

import java.io.IOException;
import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.Client;

public class URLClient extends Client {
	private static final String SOAPCONNECTION_TESTSERVLET = "/SOAPConnection_web/SOAPConnectionTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPConnection_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnection");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: closeTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:90; SAAJ:JAVADOC:91;
	 *
	 * @test_Strategy: Call SOAPConnection.close().
	 *
	 * Description: Closes this SOAPConntection object.
	 *
	 */
	@Test
	public void closeTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "closeTest: close a SOAPConnection object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPCONNECTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "closeTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("closeTest failed", e);
		}

		if (!pass)
			throw new Exception("closeTest failed");
	}

	/*
	 * @testName: callTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:86; SAAJ:JAVADOC:87;
	 *
	 * @test_Strategy: Call SOAPConnection.call().
	 *
	 * Description: Sends the given message to the specified endpoint and blocks
	 * until it has returned the response.
	 *
	 */
	@Test
	public void callTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "callTest: send SOAPMessage and block for reply");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPCONNECTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "callTest");
				if (i == 0)
					props.setProperty("SOAPVERSION", "soap11");
				else
					props.setProperty("SOAPVERSION", "soap12");
				urlConn = TestUtil.sendPostData(props, url);
				logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
				Properties resProps = TestUtil.getResponseProperties(urlConn);
				if (!resProps.getProperty("TESTRESULT").equals("pass"))
					pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("callTest failed", e);
		}

		if (!pass)
			throw new Exception("callTest failed");
	}

	/*
	 * @testName: getTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
	 *
	 * @test_Strategy: Call SOAPConnection.get().
	 *
	 * Description: Gets a SOAP response message from a specific endpoint and blocks
	 * until it has received the response. HTTP-GET from a valid endpoint that
	 * contains a valid webservice resource should succeed. The endpoint tested
	 * contains a valid webservice resource that must return a SOAP response.
	 * HTTP-GET must succeed.
	 *
	 */
	@Test
	public void getTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getTest1: Get SOAPMessage from valid endpoint");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPCONNECTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "getTest1");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getTest1 failed", e);
		}

		if (!pass)
			throw new Exception("getTest1 failed");
	}

	/*
	 * @testName: getTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
	 *
	 * @test_Strategy: Call SOAPConnection.get().
	 *
	 * Description: Gets a SOAP response message from a specific endpoint and blocks
	 * until it has received the response. HTTP-GET from a valid endpoint that does
	 * not contain a valid webservice resource should fail with a SOAPExcpetion. The
	 * endpoint tested does not contain a valid webservice resource but contains an
	 * html resource. HTTP-GET must throw a SOAPException.
	 *
	 */
	@Test
	public void getTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getTest2: Get SOAPMessage from valid endpoint");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPCONNECTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "getTest2");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getTest2 failed", e);
		}

		if (!pass)
			throw new Exception("getTest2 failed");
	}

	/*
	 * @testName: getTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:104; SAAJ:JAVADOC:105;
	 *
	 * @test_Strategy: Call SOAPConnection.get().
	 *
	 * Description: Gets a message from a specific endpoint and blocks until it has
	 * received a response. HTTP-GET from an invalid endpoint (no such host) must
	 * throw a SOAPException. Endpoint does not exist.
	 * 
	 */
	@Test
	public void getTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getTest3: Get SOAPMessage from an invalid endpoint");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPCONNECTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "getTest3");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getTest3 failed", e);
		}

		if (!pass)
			throw new Exception("getTest3 failed");
	}
}
