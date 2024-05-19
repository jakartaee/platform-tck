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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPEnvelope;

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

	private static final String SOAPENVELOPE_TESTSERVLET = "/SOAPEnvelope_web/SOAPEnvelopeTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPEnvelope_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPEnvelope");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: addBodyTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:234; SAAJ:JAVADOC:235;
	 *
	 * @test_Strategy: Call SOAPEnvelope.addBody().
	 *
	 * Description: Creates a SOAPBody object and sets it as the SOAPBody object for
	 * this SOAPEnvelope object.
	 *
	 */
	@Test
	public void addBodyTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "addBodyTest: create a SOAPBody object for " + "this SOAPEnvelope object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addBodyTest");
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
			throw new Exception("addBodyTest failed", e);
		}

		if (!pass)
			throw new Exception("addBodyTest failed");
	}

	/*
	 * @testName: getBodyTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:230; SAAJ:JAVADOC:231;
	 *
	 * @test_Strategy: Call SOAPEnvelope.getBody().
	 *
	 * Description: Returns the SOAPBody object associated with this SOAPEnvelope
	 * object.
	 *
	 */
	@Test
	public void getBodyTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getBodyTest: retrieve a SOAPBody object for " + "this SOAPEnvelope object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getBodyTest");
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
			throw new Exception("getBodyTest failed", e);
		}

		if (!pass)
			throw new Exception("getBodyTest failed");
	}

	/*
	 * @testName: addHeaderTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:232; SAAJ:JAVADOC:233;
	 *
	 * @test_Strategy: Call SOAPEnvelope.addHeader().
	 *
	 * Description: Creates a SOAPHeader object and sets it as the SOAPHeader object
	 * for this SOAPEnvelope object.
	 *
	 */
	@Test
	public void addHeaderTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"addHeaderTest: create a SOAPHeader object for " + "this SOAPEnvelope object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addHeaderTest");
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
			throw new Exception("addHeaderTest failed", e);
		}

		if (!pass)
			throw new Exception("addHeaderTest failed");
	}

	/*
	 * @testName: getHeaderTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:228; SAAJ:JAVADOC:229;
	 *
	 * @test_Strategy: Call SOAPEnvelope.getHeader().
	 *
	 * Description: Returns the SOAPHeader object associated with this SOAPEnvelope
	 * object.
	 */
	@Test
	public void getHeaderTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"getHeaderTest: retrieve a SOAPHeader object for " + "this SOAPEnvelope object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getHeaderTest");
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
			throw new Exception("getHeaderTest failed", e);
		}

		if (!pass)
			throw new Exception("getHeaderTest failed");
	}

	/*
	 * @testName: createNameTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:226; SAAJ:JAVADOC:227;
	 *
	 * @test_Strategy: Call SOAPEnvelope.createName(String).
	 *
	 * Description: Creates a new Name object initialized with the given local name.
	 *
	 */
	@Test
	public void createNameTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createNameTest1: create a Name object constructor1");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createNameTest1");
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
			throw new Exception("createNameTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createNameTest1 failed");
	}

	/*
	 * @testName: createNameTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:224; SAAJ:JAVADOC:225;
	 *
	 * @test_Strategy: Call SOAPEnvelope.createName(String, String, String).
	 *
	 * Description: Creates a new Name object initialized with the given local name,
	 * prefix, and URI.
	 *
	 */
	@Test
	public void createNameTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createNameTest2: create a Name object constructor2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createNameTest2");
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
			throw new Exception("createNameTest2 failed", e);
		}

		if (!pass)
			throw new Exception("createNameTest2 failed");
	}

	/*
	 * @testName: createNameTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:329;
	 *
	 * @test_Strategy: Call SOAPEnvelope.createName(String, String).
	 *
	 * Description: Creates a new Name object initialized with the given local name,
	 * prefix, and URI.
	 *
	 */
	@Test
	public void createNameTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createNameTest3: create a Name object constructor2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPENVELOPE_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createNameTest3");
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
			throw new Exception("createNameTest3 failed", e);
		}

		if (!pass)
			throw new Exception("createNameTest3 failed");
	}
}
