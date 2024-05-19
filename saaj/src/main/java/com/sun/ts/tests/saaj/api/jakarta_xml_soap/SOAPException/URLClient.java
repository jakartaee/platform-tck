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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPException;

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

	private static final String SOAPEXCEPTION_TESTSERVLET = "/SOAPException_web/SOAPExceptionTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPException_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPException");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: SOAPExceptionConstructor1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:70;
	 *
	 * @test_Strategy: Call SOAPException() constructor and verify creation of a new
	 * SOAPException object.
	 *
	 * Description: Create a SOAPException object
	 *
	 */
	@Test
	public void SOAPExceptionConstructor1Test() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "SOAPExceptionConstructor1Test: call SOAPException() constructor");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPEXCEPTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SOAPExceptionConstructor1Test");
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
			throw new Exception("SOAPExceptionConstructor1Test failed", e);
		}

		if (!pass)
			throw new Exception("SOAPExceptionConstructor1Test failed");
	}

	/*
	 * @testName: SOAPExceptionConstructor2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:71;
	 *
	 * @test_Strategy: Call SOAPException(String) constructor and verify creation of
	 * a new SOAPException object.
	 *
	 * Description: Create a SOAPException object
	 *
	 */
	@Test
	public void SOAPExceptionConstructor2Test() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "SOAPExceptionConstructor2Test: call SOAPException(String) constructor");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPEXCEPTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SOAPExceptionConstructor2Test");
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
			throw new Exception("SOAPExceptionConstructor2Test failed", e);
		}

		if (!pass)
			throw new Exception("SOAPExceptionConstructor2Test failed");
	}

	/*
	 * @testName: SOAPExceptionConstructor3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:72;
	 *
	 * @test_Strategy: Call SOAPException(String, Throwable) constructor and verify
	 * creation of a new SOAPException object.
	 *
	 * Description: Create a SOAPException object
	 *
	 */
	@Test
	public void SOAPExceptionConstructor3Test() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"SOAPExceptionConstructor3Test: call SOAPException(String, Throwable) constructor");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPEXCEPTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SOAPExceptionConstructor3Test");
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
			throw new Exception("SOAPExceptionConstructor3Test failed", e);
		}

		if (!pass)
			throw new Exception("SOAPExceptionConstructor3Test failed");
	}

	/*
	 * @testName: SOAPExceptionConstructor4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:73;
	 *
	 * @test_Strategy: Call SOAPException(Throwable) constructor and verify creation
	 * of a new SOAPException object.
	 *
	 * Description: Create a SOAPException object
	 *
	 */
	@Test
	public void SOAPExceptionConstructor4Test() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"SOAPExceptionConstructor4Test: call SOAPException(String, Throwable) constructor");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPEXCEPTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SOAPExceptionConstructor4Test");
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
			throw new Exception("SOAPExceptionConstructor4Test failed", e);
		}

		if (!pass)
			throw new Exception("SOAPExceptionConstructor4Test failed");
	}

	/*
	 * @testName: InitGetCauseTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:75; SAAJ:JAVADOC:76;
	 *
	 * @test_Strategy: Call the SOAPException.initCause(Throwable) method to set the
	 * cause field to the Throwable object. Then call the SOAPException.getCause()
	 * method to return the Throwable object embedded in this SOAPException.
	 *
	 * Description: Initialize the cause field of this SOAPException with the given
	 * Throwable object. Then retrieve the cause field of this SOAPException to get
	 * the Throwable object.
	 */
	@Test
	public void InitGetCauseTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "InitGetCauseTest: call SOAPException.initCause(Throwable)");
			logger.log(Logger.Level.INFO, "InitGetCauseTest: call SOAPException.getCause()");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPEXCEPTION_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "InitGetCauseTest");
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
			throw new Exception("InitGetCauseTest failed", e);
		}

		if (!pass)
			throw new Exception("InitGetCauseTest failed");
	}
}
