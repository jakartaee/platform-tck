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

package com.sun.ts.tests.saaj.ee.RestrictionsAllowables;

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

	private static final String SERVLET = "/RestrictionsAllowables_web/RestrictionsAllowablesServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "RestrictionsAllowables_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.ee.RestrictionsAllowabless");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: encodingStyleAttrSOAP11Test1
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify encodingStyle attribute can be set on
	 * Envelope. This is allowed for SOAP1.1 protocol.
	 *
	 */
	@Test
	public void encodingStyleAttrSOAP11Test1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "encodingStyleAttrSOAP11Test1");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "encodingStyleAttrSOAP11Test1");
			props.setProperty("SOAPVERSION", "soap11");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("encodingStyleAttrSOAP11Test1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("encodingStyleAttrSOAP11Test1 failed", e);
		}
	}

	/*
	 * @testName: encodingStyleAttrSOAP11Test2
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify encodingStyle attribute can be set on
	 * Envelope. This is allowed for SOAP1.1 protocol.
	 *
	 */
	@Test
	public void encodingStyleAttrSOAP11Test2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "encodingStyleAttrSOAP11Test2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "encodingStyleAttrSOAP11Test2");
			props.setProperty("SOAPVERSION", "soap11");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("encodingStyleAttrSOAP11Test2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("encodingStyleAttrSOAP11Test2 failed", e);
		}
	}

	/*
	 * @testName: noTrailingBlockBodySOAP11Test
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify trailing blocks are allowed after Body. This
	 * is allowed for SOAP1.1 protocol.
	 *
	 */
	@Test
	public void noTrailingBlockBodySOAP11Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "noTrailingBlockBodySOAP11Test");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "noTrailingBlockBodySOAP11Test");
			props.setProperty("SOAPVERSION", "soap11");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("noTrailingBlockBodySOAP11Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("noTrailingBlockBodySOAP11Test failed", e);
		}
	}

	/*
	 * @testName: enforcedQNameBodyElemSOAP11Test
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify non qualified QNames are not enforced on
	 * BodyElements. This is allowed for SOAP1.1 protocol.
	 *
	 */
	@Test
	public void enforcedQNameBodyElemSOAP11Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "enforcedQNameBodyElemSOAP11Test");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "enforcedQNameBodyElemSOAP11Test");
			props.setProperty("SOAPVERSION", "soap11");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("enforcedQNameBodyElemSOAP11Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("enforcedQNameBodyElemSOAP11Test failed", e);
		}
	}

	/*
	 * @testName: encodingStyleAttrSOAP12Test1
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify encodingStyle attribute cannot be set on
	 * Envelope. This is restricted for SOAP1.2 protocol.
	 *
	 */
	@Test
	public void encodingStyleAttrSOAP12Test1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "encodingStyleAttrSOAP12Test1");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "encodingStyleAttrSOAP12Test1");
			props.setProperty("SOAPVERSION", "soap12");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("encodingStyleAttrSOAP12Test1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("encodingStyleAttrSOAP12Test1 failed", e);
		}
	}

	/*
	 * @testName: encodingStyleAttrSOAP12Test2
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify encodingStyle attribute cannot be set on
	 * Envelope. This is restricted for SOAP1.2 protocol.
	 *
	 */
	@Test
	public void encodingStyleAttrSOAP12Test2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "encodingStyleAttrSOAP12Test2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "encodingStyleAttrSOAP12Test2");
			props.setProperty("SOAPVERSION", "soap12");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("encodingStyleAttrSOAP12Test2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("encodingStyleAttrSOAP12Test2 failed", e);
		}
	}

	/*
	 * @testName: noTrailingBlockBodySOAP12Test
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify no trailing blocks allowed after Body. This is
	 * restricted for SOAP1.2 protocol.
	 *
	 */
	@Test
	public void noTrailingBlockBodySOAP12Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "noTrailingBlockBodySOAP12Test");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "noTrailingBlockBodySOAP12Test");
			props.setProperty("SOAPVERSION", "soap12");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("noTrailingBlockBodySOAP12Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("noTrailingBlockBodySOAP12Test failed", e);
		}
	}

	/*
	 * @testName: enforcedQNameHdrElemTest1
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify that unqualified QNames are enforced on
	 * HeaderElements. This is restricted for for both SOAP1.1 and SOAP1.2 protocol.
	 *
	 */
	@Test
	public void enforcedQNameHdrElemTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "enforcedQNameHdrElemTest1");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "enforcedQNameHdrElemTest1");
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
			if (!pass)
				throw new Exception("enforcedQNameHdrElemTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("enforcedQNameHdrElemTest1 failed", e);
		}
	}

	/*
	 * @testName: enforcedQNameHdrElemTest2
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify that unqualified QNames are enforced on
	 * ChildElements of Headers. This is restricted for both SOAP1.1 and SOAP1.2
	 * protocol.
	 */
	@Test
	public void enforcedQNameHdrElemTest2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "enforcedQNameHdrElemTest2");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "enforcedQNameHdrElemTest2");
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

			if (!pass)
				throw new Exception("enforcedQNameHdrElemTest2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("enforcedQNameHdrElemTest2 failed", e);
		}
	}

	/*
	 * @testName: enforcedQNameBodyElemSOAP12Test
	 *
	 * @assertion_ids: SAAJ:SPEC:22;
	 *
	 * @test_Strategy: Test to verify non qualified QNames are not enforced on
	 * BodyElements. This is restricted for SOAP1.2 protocol.
	 *
	 */
	@Test
	public void enforcedQNameBodyElemSOAP12Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "enforcedQNameBodyElemSOAP12Test");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			props.setProperty("TESTNAME", "enforcedQNameBodyElemSOAP12Test");
			props.setProperty("SOAPVERSION", "soap12");
			logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
			urlConn = TestUtil.sendPostData(props, url);
			logger.log(Logger.Level.INFO, "Getting response from test servlet.....");
			Properties resProps = TestUtil.getResponseProperties(urlConn);
			if (!resProps.getProperty("TESTRESULT").equals("pass"))
				pass = false;

			if (!pass)
				throw new Exception("enforcedQNameBodyElemSOAP12Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("enforcedQNameBodyElemSOAP12Test failed", e);
		}
	}

	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}
}
