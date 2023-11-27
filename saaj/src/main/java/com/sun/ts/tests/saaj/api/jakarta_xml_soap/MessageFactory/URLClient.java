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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MessageFactory;

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
	private static final String SOAPMESSAGEFACTORY_TESTSERVLET = "/MessageFactory_web/MessageFactoryTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "MessageFactory_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.MessageFactory");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: createMessageTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:114; SAAJ:JAVADOC:115;
	 *
	 * @test_Strategy: Call MessageFactory.createMessage().
	 *
	 * Description: Creates a new SOAPMessage object with default SOAPPart,
	 * SOAPEnvelope, SOAPBody, and SOAPHeader objects.
	 *
	 */
	@Test
	public void createMessageTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createMessageTest1: create SOAPMessage object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createMessageTest1");
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
			throw new Exception("createMessageTest1 failed", e);
		}

		if (!pass)
			throw new Exception("createMessageTest1 failed");
	}

	/*
	 * @testName: createMessageTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:116; SAAJ:JAVADOC:117; SAAJ:JAVADOC:118;
	 *
	 * @test_Strategy: Call MessageFactory.createMessage(MimeHeaders,
	 * java.io.InputStream)
	 *
	 * Description: Internalizes the contents of the given InputStream object into a
	 * new SOAPMessage object and returns the SOAPMessage object.
	 *
	 */
	@Test
	public void createMessageTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "createMessageTest2: create SOAPMessage object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "createMessageTest2");
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
			throw new Exception("createMessageTest2 failed", e);
		}

		if (!pass)
			throw new Exception("createMessageTest2 failed");
	}

	/*
	 * @testName: newInstanceTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:110; SAAJ:JAVADOC:111;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance()
	 *
	 * Description: Creates a new MessageFactory object that is an instance of the
	 * default implementation.
	 *
	 */
	@Test
	public void newInstanceTest1() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest1: create MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance()");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
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
	 * @assertion_ids: SAAJ:JAVADOC:112;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance(
	 * SOAPConstants.SOAP_1_1_PROTOCOL)
	 *
	 * Description: Creates a new MessageFactory object that is a SOAP 1.1
	 * implementation.
	 *
	 */
	@Test
	public void newInstanceTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest2: create SOAP1.1 MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(SOAPConstants." + "SOAP11_PROTOCOL)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest2");
			props.setProperty("SOAPVERSION", "soap11");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest2 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest3 failed");
	}

	/*
	 * @testName: newInstanceTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:112;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance(
	 * SOAPConstants.SOAP_1_2_PROTOCOL)
	 *
	 * Description: Creates a new MessageFactory object that is a SOAP1.2
	 * implementation.
	 *
	 */
	@Test
	public void newInstanceTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest3: create SOAP1.2 MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(SOAPConstants." + "SOAP12_PROTOCOL)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest3");
			props.setProperty("SOAPVERSION", "soap12");
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
	 * @assertion_ids: SAAJ:JAVADOC:112;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance()
	 * SOAPConstants.DYNAMIC_SOAP_PROTOCOL)
	 *
	 * Description: Creates a new MessageFactory object that is a dynamic message
	 * factory implementation.
	 *
	 */
	@Test
	public void newInstanceTest4() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest4: create Dynamic MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(SOAPConstants." + "DYNAMIC_PROTOCOL)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest4");
			props.setProperty("SOAPVERSION", "soap12");
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
	 * @testName: newInstanceTest4b
	 *
	 * @assertion_ids: SAAJ:JAVADOC:113;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance()
	 * SOAPConstants.DYNAMIC_SOAP_PROTOCOL)
	 *
	 * Description: Creates a new MessageFactory object that is a dynamic message
	 * factory implementation.
	 *
	 */
	@Test
	public void newInstanceTest4b() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest4b: create Dynamic MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(SOAPConstants." + "DYNAMIC_PROTOCOL)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest4b");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest4b failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest4b failed");
	}

	/*
	 * @testName: newInstanceTest5
	 *
	 * @assertion_ids: SAAJ:JAVADOC:113;
	 *
	 * @test_Strategy: Call MessageFactory.newInstance(BOGUS)
	 *
	 * Description: Call MessageFactory.newInstance(String) with a BOGUS value and
	 * expect a SOAPException.
	 *
	 */
	@Test
	public void newInstanceTest5() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest5: create BOGUS MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(BOGUS)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest5");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest5 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest5 failed");
	}

	/*
	 * @testName: newInstanceTest6
	 *
	 * @assertion_ids: SAAJ:JAVADOC:112
	 *
	 * @test_Strategy: Call MessageFactory.newInstance()
	 * SOAPConstants.DEFAULT_SOAP_PROTOCOL)
	 *
	 * Description: Creates a new MessageFactory object that is a dynamic message
	 * factory implementation.
	 *
	 */
	@Test
	public void newInstanceTest6() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "newInstanceTest6: create Dynamic MessageFactory object");
			logger.log(Logger.Level.INFO, "Call MessageFactory.newInstance(SOAPConstants." + "DEFAULT_PROTOCOL)");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SOAPMESSAGEFACTORY_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			props.setProperty("TESTNAME", "newInstanceTest6");
			props.setProperty("SOAPVERSION", "soap12");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("newInstanceTest6 failed", e);
		}

		if (!pass)
			throw new Exception("newInstanceTest6 failed");
	}
}
