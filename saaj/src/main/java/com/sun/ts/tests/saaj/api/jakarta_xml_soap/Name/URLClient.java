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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Name;

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

	private static final String TESTSERVLET = "/Name_web/NameTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "Name_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class), "com.sun.ts.tests.saaj.api.jakarta_xml_soap.Name");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: getPrefixTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:308;
	 *
	 * @test_Strategy: Name.getPrefix() will return the prefix associated with this
	 * namespace object.
	 *
	 * Description: get prefix associated with Name
	 */
	@Test
	public void getPrefixTest() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO, "getPrefixTest: get prefix associated with Name");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getPrefixTest");
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
			throw new Exception("getPrefixTest failed", e);
		}

		if (!pass)
			throw new Exception("getPrefixTest failed");
	}

	/*
	 * @testName: getURITest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:309;
	 *
	 * @test_Strategy: Name.getURI() will return the URI associated with this
	 * namespace object.
	 *
	 * Description: get URI associated with Name
	 */
	@Test
	public void getURITest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getURITest: get URI associated with Name");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getURITest");
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
			throw new Exception("getURITest failed", e);
		}

		if (!pass)
			throw new Exception("getURITest failed");
	}

	/*
	 * @testName: getLocalNameTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:306;
	 *
	 * @test_Strategy: Name.getLocalName() will return the local name associated
	 * with this namespace object.
	 *
	 * Description: get local name associated with Name
	 */
	@Test
	public void getLocalNameTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getLocalNameTest: get local name associated with Name");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getLocalNameTest");
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
			throw new Exception("getLocalNameTest failed", e);
		}

		if (!pass)
			throw new Exception("getLocalNameTest failed");
	}

	/*
	 * @testName: getQualifiedNameTest
	 *
	 * @assertion_ids: SAAJ:JAVADOC:307;
	 *
	 * @test_Strategy: Name.getQualified() will return the qualified name associated
	 * with this namespace object.
	 *
	 * Description: get qualified name associated with Name
	 */
	@Test
	public void getQualifiedNameTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "getQualifiedNameTest: get qualified name associated with Name");
			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getQualifiedNameTest");
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
			throw new Exception("getQualifiedNameTest failed", e);
		}

		if (!pass)
			throw new Exception("getQualifiedNameTest failed");
	}
}
