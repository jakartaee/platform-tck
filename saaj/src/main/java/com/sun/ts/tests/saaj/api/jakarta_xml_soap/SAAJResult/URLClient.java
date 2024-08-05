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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SAAJResult;

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
	private static final String SAAJRESULT_TESTSERVLET = "/SAAJResult_web/SAAJResultTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SAAJResult_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SAAJResult");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: SAAJResultConstructorTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:92;
	 *
	 * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
	 * SAAJResult object.
	 *
	 * Description: Create a SAAJResult object.
	 *
	 */
	@Test
	public void SAAJResultConstructorTest1() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "SAAJResultConstructorTest1: create SAAJResult object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SAAJResultConstructorTest1");
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
			throw new Exception("SAAJResultConstructorTest1 failed", e);
		}

		if (!pass)
			throw new Exception("SAAJResultConstructorTest1 failed");
	}

	/*
	 * @testName: SAAJResultConstructorTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:95;
	 *
	 * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
	 * SAAJResult object.
	 *
	 * Description: Create a SAAJResult object.
	 *
	 */
	@Test
	public void SAAJResultConstructorTest2() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "SAAJResultConstructorTest2: create SAAJResult object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SAAJResultConstructorTest2");
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
			throw new Exception("SAAJResultConstructorTest2 failed", e);
		}

		if (!pass)
			throw new Exception("SAAJResultConstructorTest2 failed");
	}

	/*
	 * @testName: SAAJResultConstructorTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:94;
	 *
	 * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
	 * SAAJResult object.
	 *
	 * Description: Create a SAAJResult object.
	 *
	 */
	@Test
	public void SAAJResultConstructorTest3() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "SAAJResultConstructorTest3: create SAAJResult object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SAAJResultConstructorTest3");
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
			throw new Exception("SAAJResultConstructorTest3 failed", e);
		}

		if (!pass)
			throw new Exception("SAAJResultConstructorTest3 failed");
	}

	/*
	 * @testName: SAAJResultConstructorTest4
	 *
	 * @assertion_ids: SAAJ:JAVADOC:93;
	 *
	 * @test_Strategy: Call SAAJResult() constructor and verify creation of a new
	 * SAAJResult object.
	 *
	 * Description: Create a SAAJResult object.
	 *
	 */
	@Test
	public void SAAJResultConstructorTest4() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "SAAJResultConstructorTest4: create SAAJResult object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SAAJResultConstructorTest4");
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
			throw new Exception("SAAJResultConstructorTest4 failed", e);
		}

		if (!pass)
			throw new Exception("SAAJResultConstructorTest4 failed");
	}

	/*
	 * @testName: getResultTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:96;
	 *
	 * @test_Strategy: Call getResultTest1() and verify a result object is returned.
	 *
	 * Description: Get a Node object.
	 *
	 */
	@Test
	public void getResultTest1() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "getResultTest1: get result object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getResultTest1");
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
			throw new Exception("getResultTest1 failed", e);
		}

		if (!pass)
			throw new Exception("getResultTest1 failed");
	}

	/*
	 * @testName: getResultTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:96;
	 *
	 * @test_Strategy: Call getResultTest2() and verify a result object is returned.
	 *
	 * Description: Get a Node object.
	 *
	 */
	@Test
	public void getResultTest2() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "getResultTest2: get result object");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SAAJRESULT_TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getResultTest2");
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
			throw new Exception("getResultTest2 failed", e);
		}

		if (!pass)
			throw new Exception("getResultTest2 failed");
	}
}
