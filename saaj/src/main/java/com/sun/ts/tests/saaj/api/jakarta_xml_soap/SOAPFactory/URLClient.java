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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFactory;

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

	private static final String SOAPFACTORY_TESTSERVLET = "/SOAPFactory_web/SOAPFactoryTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPFactory_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFactory");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: newInstanceTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:66; SAAJ:JAVADOC:67;
	 *
	 * @test_Strategy: Call SOAPFactory.newInstance()
	 *
	 * Description: Creates a new SOAPFactory object that is an instance of the
	 * default implementation.
	 *
	 */
	@Test
	public void newInstanceTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest1: create a SOAPFactory object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest1");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest1 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest1 failed");
	}

	/*
	 * @testName: newInstanceTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:68;
	 *
	 * @test_Strategy: Call SOAPFactory.newInstance(
	 * SOAPConstants.SOAP_1_1_PROTOCOL)
	 *
	 * Description: Creates a new SOAPFactory object that is a SOAP 1.1
	 * implementation.
	 *
	 */
	@Test
	public void newInstanceTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest2: create a SOAP1.1 SOAPFactory object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest2");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest1 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest1 failed");
	}

	/*
	 * @testName: newInstanceTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:68;
	 *
	 * @test_Strategy: Call SOAPFactory.newInstance(
	 * SOAPConstants.SOAP_1_2_PROTOCOL)
	 *
	 * Description: Creates a new SOAPFactory object that is a SOAP 1.2
	 * implementation.
	 *
	 */
	@Test
	public void newInstanceTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest3: create a SOAP1.2 SOAPFactory object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest3");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest3 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest3 failed");
	}

	/*
	 * @testName: newInstanceTest4
	 *
	 * @assertion_ids: SAAJ:JAVADOC:69;
	 *
	 * @test_Strategy: Call SOAPFactory.newInstance(BOGUS)
	 *
	 * Description: Call SOAPFactory.newInstance(String) with a BOGUS value and
	 * expect a SOAPException.
	 *
	 */
	@Test
	public void newInstanceTest4() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest4: create a BOGUS SOAPFactory object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest4");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest4 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest4 failed");
	}

	/*
	 * @testName: createElementTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:52; SAAJ:JAVADOC:53;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(Name)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given Name
	 * object.
	 *
	 */
	@Test
	public void createElementTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest1: create a SOAPElement object constructor1");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest1");
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
			throw new Exception("createElementTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest1 failed");
	}

	/*
	 * @testName: createElementTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:56; SAAJ:JAVADOC:57;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(String)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given
	 * local name.
	 *
	 */
	@Test
	public void createElementTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest2: create a SOAPElement object constructor2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest2");
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
			throw new Exception("createElementTest2 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest2 failed");
	}

	/*
	 * @testName: createElementTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:58; SAAJ:JAVADOC:59;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(String, String, String)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given
	 * local name, prefix, and uri.
	 *
	 */
	@Test
	public void createElementTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest3: create a SOAPElement object constructor3");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest3");
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
			throw new Exception("createElementTest3 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest3 failed");
	}

	/*
	 * @testName: createElementTest4
	 *
	 * @assertion_ids: SAAJ:JAVADOC:54; SAAJ:JAVADOC:55;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(QName)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given
	 * QName object.
	 *
	 */
	@Test
	public void createElementTest4() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest4: create a SOAPElement object constructor4");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest4");
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
			throw new Exception("createElementTest4 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest4 failed");
	}

	/*
	 * @testName: createElementTest5
	 *
	 * @assertion_ids: SAAJ:JAVADOC:50; SAAJ:JAVADOC:51;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(org.w3c.dom.Element)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given DOM
	 * Element object. This test case creates and passes in a DOM
	 * org.w3c.dom.Element.
	 *
	 */
	@Test
	public void createElementTest5() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest5: create a SOAPElement object constructor5");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest5");
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
			throw new Exception("createElementTest5 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest5 failed");
	}

	/*
	 * @testName: createElementTest6
	 *
	 * @assertion_ids: SAAJ:JAVADOC:50; SAAJ:JAVADOC:51;
	 *
	 * @test_Strategy: Call SOAPFactory.createElement(org.w3c.dom.Element)
	 *
	 * Description: Creates a new SOAPElement object initialized with the given DOM
	 * Element object. This test case creates and passes in a SOAPElement which
	 * extends org.w3c.dom.Element.
	 *
	 */
	@Test
	public void createElementTest6() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createElementTest6: create a SOAPElement object constructor5");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createElementTest6");
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
			throw new Exception("createElementTest6 failed", e);
		}

		if (!pass)
			throw new Exception("createElementTest6 failed");
	}

	/*
	 * @testName: createDetailTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:60; SAAJ:JAVADOC:61;
	 *
	 * @test_Strategy: Call SOAPFactory.createDetail(String)
	 *
	 * Description: Creates a new Detail object.
	 *
	 */
	@Test
	public void createDetailTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createDetailTest1: create a Detail object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createDetailTest1");
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
			throw new Exception("createDetailTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createDetailTest1 failed");
	}

	/*
	 * @testName: createNameTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:64; SAAJ:JAVADOC:65;
	 *
	 * @test_Strategy: Call SOAPFactory.createName(String)
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
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
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
	 * @assertion_ids: SAAJ:JAVADOC:62; SAAJ:JAVADOC:63;
	 *
	 * @test_Strategy: Call SOAPFactory.createName(String, String, String)
	 *
	 * Description: Creates a new Name object initialized with the given local name,
	 * namespace prefix, and namespace uri.
	 *
	 */
	@Test
	public void createNameTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createNameTest2: create a Name object constructor2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
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
	 * @testName: createFaultTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:317; SAAJ:JAVADOC:318;
	 *
	 * @test_Strategy: Call SOAPFactory.createFault()
	 *
	 * Description: Creates a new SOAPFault object using default constructor.
	 *
	 */
	@Test
	public void createFaultTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createFaultTest1: create a SOAPFault object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createFaultTest1");
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
			throw new Exception("createFaultTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createFaultTest1 failed");
	}

	/*
	 * @testName: createFaultTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:315; SAAJ:JAVADOC:316;
	 *
	 * @test_Strategy: Call SOAPFactory.createFault(String, QName).
	 *
	 * Description: Creates a new SOAPFault object passing in the reason text as a
	 * String and the fault code as a QName.
	 * 
	 *
	 */
	@Test
	public void createFaultTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createFaultTest2: create a SOAPFault object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createFaultTest2");
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
			throw new Exception("createFaultTest2 failed", e);
		}

		if (!pass)
			throw new Exception("createFaultTest2 failed");
	}

	/*
	 * @testName: createFaultSOAPExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:316;
	 *
	 * @test_Strategy: Call SOAPFactory.createFault(String, QName).
	 *
	 * Description: Creates a new SOAPFault object passing in the reason text as a
	 * String and the fault code as a QName. Pass in an invalid fault code. Should
	 * get a SOAPException.
	 *
	 */
	@Test
	public void createFaultSOAPExceptionTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createFaultSOAPExceptionTest1: test for a SOAPException");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createFaultSOAPExceptionTest1");
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
			throw new Exception("createFaultSOAPExceptionTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createFaultSOAPExceptionTest1 failed");
	}
}
