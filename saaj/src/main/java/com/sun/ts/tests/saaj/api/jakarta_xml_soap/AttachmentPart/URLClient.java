/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.AttachmentPart;

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

	private static final String TESTSERVLET = "/AttachmentPart_web/AttachmentPartTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "AttachmentPart_web.war");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.AttachmentPart");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		final String CONTENT_ROOT = URLClient.class.getPackageName().replace(".", "/") + "/contentRoot/";
		String[] attachement = { "attach2.xml", "attach.gif", "attach.html", "attach.jpeg", "attach.null",
				"attach.xml" };
		addFilesToArchive(CONTENT_ROOT, attachement, archive);
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	}

	/* Test setup */

	/*
	 * @testName: addMimeHeader1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MIMEHeader object. Add a single header.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader1Test");
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
				throw new Exception("addMimeHeader1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader1Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Add two headers.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader2Test");
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
				throw new Exception("addMimeHeader2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader2Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Add two headers that have
	 * different values.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader3Test");
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
				throw new Exception("addMimeHeader3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader3Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Attempt to add an empty header
	 * and value.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader4Test");
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
				throw new Exception("addMimeHeader4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader4Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Attempt to add an empty header
	 * and non-empty value.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader5Test");
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
				throw new Exception("addMimeHeader5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader5Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader6Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Attempt to add a non-empty header
	 * and empty value.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader6Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader6Test");
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
				throw new Exception("addMimeHeader6Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader6Test failed", e);
		}
	}

	/*
	 * @testName: addMimeHeader7Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:147;
	 *
	 * @test_Strategy: Call AttachmentPart.addMimeHeader(String,String) method and
	 * verify creation of a new MimeHeader object. Attempt to add a null header and
	 * null value.
	 *
	 * Description: Construct a MimeHeader object.
	 */
	@Test
	public void addMimeHeader7Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());

			for (Object name : props.keySet()) {
				String key = name.toString();
				String value = props.get(name).toString();
			}

			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "addMimeHeader7Test");
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
				throw new Exception("addMimeHeader7Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("addMimeHeader7Test failed", e);
		}
	}

	/*
	 * @testName: getMimeHeader1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:145;
	 *
	 * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
	 * return of a the MimeHeader object. Get a single header.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMimeHeader1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMimeHeader1Test");
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
				throw new Exception("getMimeHeader1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMimeHeader1Test failed", e);
		}
	}

	/*
	 * @testName: getMimeHeader2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:145;
	 *
	 * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
	 * return of the MimeHeader object. Get single header from multiple headers.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMimeHeader2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMimeHeader2Test");
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
				throw new Exception("getMimeHeader2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMimeHeader2Test failed", e);
		}
	}

	/*
	 * @testName: getMimeHeader3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:145;
	 *
	 * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
	 * return of the MimeHeader object. Get header that contains two entries.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMimeHeader3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMimeHeader3Test");
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
				throw new Exception("getMimeHeader3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMimeHeader3Test failed", e);
		}
	}

	/*
	 * @testName: getMimeHeader4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:145;
	 *
	 * @test_Strategy: Call AttachmentPart.getMimeHeader(String) method and verify
	 * return of the MimeHeader object. Attempt to get a header that doesn't exist
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMimeHeader4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMimeHeader4Test");
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
				throw new Exception("getMimeHeader4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMimeHeader4Test failed", e);
		}
	}

	/*
	 * @testName: getAllMimeHeaders1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:148;
	 *
	 * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
	 * return of all MimeHeader objects.
	 *
	 * Description: Retrieve a MimeHeader object. Get single.
	 */
	@Test
	public void getAllMimeHeaders1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAllMimeHeaders1Test");
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
				throw new Exception("getAllMimeHeaders1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getAllMimeHeaders1Test failed", e);
		}
	}

	/*
	 * @testName: getAllMimeHeaders2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:148;
	 *
	 * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
	 * return of all MimeHeader objects. Get multiple.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getAllMimeHeaders2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAllMimeHeaders2Test");
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
				throw new Exception("getAllMimeHeaders2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getAllMimeHeaders2Test failed", e);
		}
	}

	/*
	 * @testName: getAllMimeHeaders3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:148;
	 *
	 * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
	 * return of all MimeHeader objects. Get single header that contains mulitple
	 * entries.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getAllMimeHeaders3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAllMimeHeaders3Test");
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
				throw new Exception("getAllMimeHeaders3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getAllMimeHeaders3Test failed", e);
		}
	}

	/*
	 * @testName: getAllMimeHeaders4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:148;
	 *
	 * @test_Strategy: Call AttachmentPart.getAllMimeHeaders() method and verify
	 * return of all MimeHeader objects. Attempt to get all headers when none exist.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getAllMimeHeaders4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getAllMimeHeaders4Test");
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
				throw new Exception("getAllMimeHeaders4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getAllMimeHeaders4Test failed", e);
		}
	}

	/*
	 * @testName: removeAllMimeHeaders1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:144;
	 *
	 * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and verify
	 * removal of all MimeHeader objects. Remove single header.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeAllMimeHeaders1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAllMimeHeaders1Test");
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
				throw new Exception("removeAllMimeHeaders1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeAllMimeHeaders1Test failed", e);
		}
	}

	/*
	 * @testName: removeAllMimeHeaders2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:144;
	 *
	 * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and verify
	 * removal of all MimeHeader objects. Remove multiple headers.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeAllMimeHeaders2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAllMimeHeaders2Test");
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
				throw new Exception("removeAllMimeHeaders2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeAllMimeHeaders2Test failed", e);
		}
	}

	/*
	 * @testName: removeAllMimeHeaders3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:144;
	 *
	 * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and verify
	 * removal of all MimeHeader objects. Remove header that contains multiple
	 * entries.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeAllMimeHeaders3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAllMimeHeaders3Test");
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
				throw new Exception("removeAllMimeHeaders3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeAllMimeHeaders3Test failed", e);
		}
	}

	/*
	 * @testName: removeAllMimeHeaders4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:144;
	 *
	 * @test_Strategy: Call AttachmentPart.removeAllMimeHeaders() method and verify
	 * removal of all MimeHeader objects. Remove headers when none exist.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeAllMimeHeaders4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeAllMimeHeaders4Test");
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
				throw new Exception("removeAllMimeHeaders4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeAllMimeHeaders4Test failed", e);
		}
	}

	/*
	 * @testName: setMimeHeader1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:146;
	 *
	 * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
	 * verify return of a the MimeHeader object. Set exist header.
	 *
	 * Description: Replace a MimeHeader object.
	 */
	@Test
	public void setMimeHeader1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMimeHeader1Test");
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
				throw new Exception("setMimeHeader1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMimeHeader1Test failed", e);
		}
	}

	/*
	 * @testName: setMimeHeader2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:146;
	 *
	 * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
	 * verify return of the MimeHeader object. Set existing header from list of two
	 * headers.
	 *
	 * Description: Replace a MimeHeader object.
	 */
	@Test
	public void setMimeHeader2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMimeHeader2Test");
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
				throw new Exception("setMimeHeader2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMimeHeader2Test failed", e);
		}
	}

	/*
	 * @testName: setMimeHeader3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:146;
	 *
	 * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
	 * verify return of the MimeHeader object. Set existing header that contains
	 * multiple values.
	 *
	 * Description: Replace/Construct a MimeHeader object.
	 */
	@Test
	public void setMimeHeader3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMimeHeader3Test");
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
				throw new Exception("setMimeHeader3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMimeHeader3Test failed", e);
		}
	}

	/*
	 * @testName: setMimeHeader4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:146;
	 *
	 * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
	 * verify return of the MimeHeader object. Set header that doesn't exist
	 *
	 * Description: Replace/Construct a MimeHeader object.
	 */
	@Test
	public void setMimeHeader4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMimeHeader4Test");
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
				throw new Exception("setMimeHeader4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMimeHeader4Test failed", e);
		}
	}

	/*
	 * @testName: setMimeHeader5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:146;
	 *
	 * @test_Strategy: Call AttachmentPart.setMimeHeader(String,String) method and
	 * verify return of the MimeHeader object. Set an existing header twice.
	 *
	 * Description: Replace/Construct a MimeHeader object.
	 */
	@Test
	public void setMimeHeader5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setMimeHeader5Test");
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
				throw new Exception("setMimeHeader5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setMimeHeader5Test failed", e);
		}
	}

	/*
	 * @testName: removeMimeHeader1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:143;
	 *
	 * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
	 * verify return of a the MimeHeader object. Remove single header.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeMimeHeader1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeMimeHeader1Test");
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
				throw new Exception("removeMimeHeader1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeMimeHeader1Test failed", e);
		}
	}

	/*
	 * @testName: removeMimeHeader2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:143;
	 *
	 * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
	 * verify return of the MimeHeader object. Remove single header from list of
	 * two.
	 *
	 * Description: Replace a MimeHeader object.
	 */
	@Test
	public void removeMimeHeader2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeMimeHeader2Test");
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
				throw new Exception("removeMimeHeader2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeMimeHeader2Test failed", e);
		}
	}

	/*
	 * @testName: removeMimeHeader3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:143;
	 *
	 * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
	 * verify return of the MimeHeader object. Remove single header that contains
	 * multiple values.
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeMimeHeader3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeMimeHeader3Test");
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
				throw new Exception("removeMimeHeader3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeMimeHeader3Test failed", e);
		}
	}

	/*
	 * @testName: removeMimeHeader4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:143;
	 *
	 * @test_Strategy: Call AttachmentPart.removeMimeHeader(String) method and
	 * verify return of the MimeHeader object. Remove header that doesn't exist
	 *
	 * Description: Remove a MimeHeader object.
	 */
	@Test
	public void removeMimeHeader4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "removeMimeHeader4Test");
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
				throw new Exception("removeMimeHeader4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("removeMimeHeader4Test failed", e);
		}
	}

	/*
	 * @testName: getMatchingMimeHeaders1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:149;
	 *
	 * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
	 * and verify return of a the MimeHeader object.
	 *
	 * Description: Retrieve a MimeHeader object. Get single header.
	 */
	@Test
	public void getMatchingMimeHeaders1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMatchingMimeHeaders1Test");
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
				throw new Exception("getMatchingMimeHeaders1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMatchingMimeHeaders1Test failed", e);
		}
	}

	/*
	 * @testName: getMatchingMimeHeaders2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:149;
	 *
	 * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
	 * and verify return of the MimeHeader object. Get single header from list of
	 * two.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMatchingMimeHeaders2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMatchingMimeHeaders2Test");
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
				throw new Exception("getMatchingMimeHeaders2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMatchingMimeHeaders2Test failed", e);
		}
	}

	/*
	 * @testName: getMatchingMimeHeaders3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:149;
	 *
	 * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
	 * and verify return of the MimeHeader object. Get single header that contains
	 * multiple values.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMatchingMimeHeaders3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMatchingMimeHeaders3Test");
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
				throw new Exception("getMatchingMimeHeaders3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMatchingMimeHeaders3Test failed", e);
		}
	}

	/*
	 * @testName: getMatchingMimeHeaders4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:149;
	 *
	 * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
	 * and verify return of the MimeHeader object. Attempt to get a header that
	 * doesn't exist
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMatchingMimeHeaders4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMatchingMimeHeaders4Test");
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
				throw new Exception("getMatchingMimeHeaders4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMatchingMimeHeaders4Test failed", e);
		}
	}

	/*
	 * @testName: getMatchingMimeHeaders5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:149;
	 *
	 * @test_Strategy: Call AttachmentPart.getMatchingMimeHeaders(String[]) method
	 * and verify return of the MimeHeader object. Attempt to get a headers and a
	 * non-existent header
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getMatchingMimeHeaders5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getMatchingMimeHeaders5Test");
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
				throw new Exception("getMatchingMimeHeaders5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getMatchingMimeHeaders5Test failed", e);
		}
	}

	/*
	 * @testName: getNonMatchingMimeHeaders1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:150;
	 *
	 * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
	 * method and verify return of a the MimeHeader object. Get single header.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getNonMatchingMimeHeaders1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getNonMatchingMimeHeaders1Test");
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
				throw new Exception("getNonMatchingMimeHeaders1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getNonMatchingMimeHeaders1Test failed", e);
		}
	}

	/*
	 * @testName: getNonMatchingMimeHeaders2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:150;
	 *
	 * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
	 * method and verify return of the MimeHeader object. Get single header from
	 * list of two.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getNonMatchingMimeHeaders2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getNonMatchingMimeHeaders2Test");
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
				throw new Exception("getNonMatchingMimeHeaders2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getNonMatchingMimeHeaders2Test failed", e);
		}
	}

	/*
	 * @testName: getNonMatchingMimeHeaders3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:150;
	 *
	 * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
	 * method and verify return of the MimeHeader object. Get single header that
	 * contains multiple values.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getNonMatchingMimeHeaders3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getNonMatchingMimeHeaders3Test");
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
				throw new Exception("getNonMatchingMimeHeaders3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getNonMatchingMimeHeaders3Test failed", e);
		}
	}

	/*
	 * @testName: getNonMatchingMimeHeaders4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:150;
	 *
	 * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
	 * method and verify return of the MimeHeader object. Attempt to get header that
	 * results in no headers being returned.
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getNonMatchingMimeHeaders4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getNonMatchingMimeHeaders4Test");
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
				throw new Exception("getNonMatchingMimeHeaders4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getNonMatchingMimeHeaders4Test failed", e);
		}
	}

	/*
	 * @testName: getNonMatchingMimeHeaders5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:150;
	 *
	 * @test_Strategy: Call AttachmentPart.getNonMatchingMimeHeaders(String[])
	 * method and verify return of the MimeHeader object. Attempt to get a header
	 * and a non-existent header
	 *
	 * Description: Retrieve a MimeHeader object.
	 */
	@Test
	public void getNonMatchingMimeHeaders5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getNonMatchingMimeHeaders5Test");
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
				throw new Exception("getNonMatchingMimeHeaders5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getNonMatchingMimeHeaders5Test failed", e);
		}
	}

	/*
	 * @testName: setContentId1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:140;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify no
	 * errors occur. Attempt to set id to a valid value.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentId1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentId1Test");
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
				throw new Exception("setContentId1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentId1Test failed", e);
		}
	}

	/*
	 * @testName: setContentId2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:140;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify no
	 * errors occur.
	 *
	 * Description: Set the Mime Header named Content-Id Set the id to null.
	 */
	@Test
	public void setContentId2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentId2Test");
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
				throw new Exception("setContentId2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentId2Test failed", e);
		}
	}

	/*
	 * @testName: setContentId3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:140;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify no
	 * errors occur.
	 *
	 * Description: Set the Mime Header named Content-Id Set the id twice in a row.
	 */
	@Test
	public void setContentId3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentId3Test");
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
				throw new Exception("setContentId3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentId3Test failed", e);
		}
	}

	/*
	 * @testName: setContentId4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:140;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentId(String) method and verify no
	 * errors occur. Attempt to set id to a valid value then to another string.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentId4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentId4Test");
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
				throw new Exception("setContentId4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentId4Test failed", e);
		}
	}

	/*
	 * @testName: getContentId1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:139;
	 *
	 * @test_Strategy: Call AttachmentPart.getContentId() method and verify correct
	 * content id is returned. Get Id.
	 *
	 * Description: Get the Mime Header named Content-Id
	 */
	@Test
	public void getContentId1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentId1Test");
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
				throw new Exception("getContentId1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentId1Test failed", e);
		}
	}

	/*
	 * @testName: getContentId2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:139;
	 *
	 * @test_Strategy: Call AttachmentPart.getContentId() method and verify correct
	 * content id is returned. Get id after it has been set twice in a row.
	 *
	 * Description: Get the Mime Header named Content-Id
	 */
	@Test
	public void getContentId2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentId2Test");
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
				throw new Exception("getContentId2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentId2Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:141;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
	 * verify no errors occur. Set URL location
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentLocation1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentLocation1Test");
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
				throw new Exception("setContentLocation1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentLocation1Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:141;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
	 * verify no errors occur. Set empty string location.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentLocation2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentLocation2Test");
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
				throw new Exception("setContentLocation2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentLocation2Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:141;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
	 * verify no errors occur. Set URI location
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentLocation3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentLocation3Test");
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
				throw new Exception("setContentLocation3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentLocation3Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:141;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
	 * verify no errors occur. Set location twice.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentLocation4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentLocation4Test");
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
				throw new Exception("setContentLocation4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentLocation4Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:141;
	 *
	 * @test_Strategy: Call AttachmentPart.setContentLocation(String) method and
	 * verify no errors occur. Set location to null.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void setContentLocation5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContentLocation5Test");
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
				throw new Exception("setContentLocation5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContentLocation5Test failed", e);
		}
	}

	/*
	 * @testName: getContentLocation1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:138;
	 *
	 * @test_Strategy: Call AttachmentPart.getContentLocation() method and verify
	 * correct content location is returned. Get URL location.
	 *
	 * Description: Get the Mime Header named Content-Id
	 */
	@Test
	public void getContentLocation1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentLocation1Test");
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
				throw new Exception("getContentLocation1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentLocation1Test failed", e);
		}
	}

	/*
	 * @testName: getContentLocation2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:138;
	 *
	 * @test_Strategy: Call AttachmentPart.getContentLocation(String) method and
	 * verify correct content location is returned. Get location after it has been
	 * set twice.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void getContentLocation2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentLocation2Test");
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
				throw new Exception("getContentLocation2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentLocation2Test failed", e);
		}
	}

	/*
	 * @testName: getContentLocation3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:138;
	 *
	 * @test_Strategy: Call AttachmentPart.getContentLocation(String) method and
	 * verify correct content location is returned. Get URI location.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void getContentLocation3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentLocation3Test");
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
				throw new Exception("getContentLocation3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentLocation3Test failed", e);
		}
	}

	/*
	 * @testName: setContent1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:129;
	 *
	 * @test_Strategy: Call AttachmentPart.setContent(Object, String) method passing
	 * a null and verify exception is thrown.
	 *
	 * Description: Set the Content
	 */
	@Test
	public void setContent1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContent1Test");
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
				throw new Exception("setContent1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContent1Test failed", e);
		}
	}

	/*
	 * @testName: setContent2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:129;
	 *
	 * @test_Strategy: Call AttachmentPart.setContent(Object, String) method passing
	 * an invalid object type verify exception is thrown.
	 *
	 * Description: Set the Content
	 */
	@Test
	public void setContent2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContent2Test");
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
				throw new Exception("setContent2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContent2Test failed", e);
		}
	}

	/*
	 * @testName: setContent3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:129;
	 *
	 * @test_Strategy: Call AttachmentPart.setContent(Object, String) method passing
	 * invalid object types and verify exception is thrown.
	 *
	 * Description: Set the Content
	 */
	@Test
	public void setContent3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setContent3Test");
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
				throw new Exception("setContent3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setContent3Test failed", e);
		}
	}

	/*
	 * @testName: getContent1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
	 *
	 * @test_Strategy: Call AttachmentPart.getContent() method and verify that a
	 * StreamSource object is returned properly for the text/xml mime type.
	 *
	 * Description: Get the Content
	 */
	@Test
	public void getContent1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContent1Test");
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
				throw new Exception("getContent1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContent1Test failed", e);
		}
	}

	/*
	 * @testName: getContent2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
	 *
	 * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
	 * InputStream object or String object is returned properly for the text/html
	 * mime type.
	 *
	 * Description: Get the Content
	 */
	@Test
	public void getContent2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContent2Test");
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
				throw new Exception("getContent2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContent2Test failed", e);
		}
	}

	/*
	 * @testName: getContent3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
	 *
	 * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
	 * Image object is returned properly for the image/gif mime type.
	 *
	 * Description: Get the Content
	 */
	@Test
	public void getContent3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContent3Test");
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
				throw new Exception("getContent3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContent3Test failed", e);
		}
	}
	

	/*
	 * @testName: getContent4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
	 *
	 * @test_Strategy: Call AttachmentPart.getContent() method and verify that an
	 * Image object is returned properly for the image/jpeg mime type.
	 *
	 * Description: Get the Content
	 */
	@Test
	public void getContent4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContent4Test");
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
				throw new Exception("getContent4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContent4Test failed", e);
		}
	}

	/*
	 * @testName: getContent5Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:123; SAAJ:JAVADOC:124;
	 *
	 * @test_Strategy: Call AttachmentPart.getContent() method and verify that a
	 * String object is returned properly for the text/plain mime type.
	 *
	 * Description: Get the Content
	 */
	@Test
	public void getContent5Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContent5Test");
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
				throw new Exception("getContent5Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContent5Test failed", e);
		}
	}

	/*
	 * @testName: setDataHandler1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:136;
	 *
	 * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) with valid
	 * object
	 *
	 * Description: Set the DataHandler object
	 */
	@Test
	public void setDataHandler1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setDataHandler1Test");
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
				throw new Exception("setDataHandler1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setDataHandler1Test failed", e);
		}
	}

	/*
	 * @testName: setDataHandler2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:136;
	 *
	 * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) with a null
	 * object.
	 *
	 * Description: Set the DataHandler object
	 */
	@Test
	public void setDataHandler2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setDataHandler2Test");
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
				throw new Exception("setDataHandler2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setDataHandler2Test failed", e);
		}
	}

	/*
	 * @testName: setDataHandler3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:136;
	 *
	 * @test_Strategy: Call AttachmentPart.setDataHandler(DataHandler) twice and
	 * verify no errors occur.
	 *
	 * Description: Set the DataHandler object
	 */
	@Test
	public void setDataHandler3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setDataHandler3Test");
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
				throw new Exception("setDataHandler3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("setDataHandler3Test failed", e);
		}
	}

	/*
	 * @testName: getDataHandler1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
	 *
	 * @test_Strategy: Call AttachmentPart.getDataHandler() and verify the contents
	 * of the data.
	 *
	 * Description: Get the DataHandler object
	 */
	@Test
	public void getDataHandler1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getDataHandler1Test");
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
				throw new Exception("getDataHandler1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getDataHandler1Test failed", e);
		}
	}

	/*
	 * @testName: getDataHandler2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
	 *
	 * @test_Strategy: Call AttachmentPart.getDataHandler() when no DataHandler has
	 * been set, should result in an exception.
	 *
	 * Description: Get the DataHandler object
	 */
	@Test
	public void getDataHandler2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getDataHandler2Test");
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
				throw new Exception("getDataHandler2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getDataHandler2Test failed", e);
		}
	}

	/*
	 * @testName: getDataHandler3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:134; SAAJ:JAVADOC:135;
	 *
	 * @test_Strategy: Call AttachmentPart.getDataHandler() twice and verify the
	 * contents of the data.
	 *
	 * Description: Get the DataHandler object
	 */
	@Test
	public void getDataHandler3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getDataHandler3Test");
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
				throw new Exception("getDataHandler3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getDataHandler3Test failed", e);
		}
	}

	/*
	 * @testName: getSize1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
	 *
	 * @test_Strategy: Call AttachmentPart.getSize() on empty Attachment.
	 *
	 * Description: Get the size of the AttachmentPart Object
	 */
	@Test
	public void getSize1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSize1Test");
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
				throw new Exception("getSize1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getSize1Test failed", e);
		}
	}

	/*
	 * @testName: getSize2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
	 *
	 * @test_Strategy: Call AttachmentPart.getSize() after Mime header is added.
	 *
	 * Description: Get the size of the AttachmentPart Object
	 */
	@Test
	public void getSize2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSize2Test");
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
				throw new Exception("getSize2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getSize2Test failed", e);
		}
	}

	/*
	 * @testName: getSize3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
	 *
	 * @test_Strategy: Call AttachmentPart.getSize() after AttachmentPart.
	 * setContent is done. A SOAPException should be thrown.
	 *
	 * Description: Get the size of the AttachmentPart Object
	 */
	@Test
	public void getSize3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSize3Test");
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
				throw new Exception("getSize3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getSize3Test failed", e);
		}
	}

	/*
	 * @testName: getSize4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:120; SAAJ:JAVADOC:121;
	 *
	 * @test_Strategy: Call AttachmentPart.getSize() after AttachmentPart.
	 * setDataHandler is done.
	 *
	 * Description: Get the size of the AttachmentPart Object
	 */
	@Test
	public void getSize4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getSize4Test");
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
				throw new Exception("getSize4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getSize4Test failed", e);
		}
	}

	/*
	 * @testName: clearContent1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:122;
	 *
	 * @test_Strategy: Call AttachmentPart.clearContent() after setting content.
	 *
	 * Description: Clear content of AttachmentPart object
	 */
	@Test
	public void clearContent1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "clearContent1Test");
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
				throw new Exception("clearContent1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("clearContent1Test failed", e);
		}
	}

	/*
	 * @testName: clearContent2Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:122;
	 *
	 * @test_Strategy: Call AttachmentPart.clearContent() after setting content
	 * twice.
	 *
	 * Description: Clear content of AttachmentPart object
	 */
	@Test
	public void clearContent2Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "clearContent2Test");
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
				throw new Exception("clearContent2Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("clearContent2Test failed", e);
		}
	}

	/*
	 * @testName: clearContent3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:122;
	 *
	 * @test_Strategy: Call AttachmentPart.clearContent() after setting content and
	 * mime headers. Mime Headers should remain untouched.
	 *
	 * Description: Clear content of AttachmentPart object
	 */
	@Test
	public void clearContent3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "clearContent3Test");
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
				throw new Exception("clearContent3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("clearContent3Test failed", e);
		}
	}

	/*
	 * @testName: clearContent4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:122;
	 *
	 * @test_Strategy: Call AttachmentPart.clearContent() when no content has been
	 * set.
	 *
	 * Description: Clear content of AttachmentPart object
	 */
	@Test
	public void clearContent4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "clearContent4Test");
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
				throw new Exception("clearContent4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("clearContent4Test failed", e);
		}
	}

	/*
	 * @testName: SetGetBase64ContentTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
	 * verify correct behavior.
	 */
	@Test
	public void SetGetBase64ContentTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetBase64ContentTest1");
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
				throw new Exception("SetGetBase64ContentTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetBase64ContentTest1 failed", e);
		}
	}

	/*
	 * @testName: SetGetBase64ContentTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
	 * verify correct behavior.
	 */
	@Test
	public void SetGetBase64ContentTest2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetBase64ContentTest2");
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
				throw new Exception("SetGetBase64ContentTest2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetBase64ContentTest2 failed", e);
		}
	}

	/*
	 * @testName: SetGetBase64ContentTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:127; SAAJ:JAVADOC:132;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content/getBase64Content and
	 * verify correct behavior.
	 */
	@Test
	public void SetGetBase64ContentTest3() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetBase64ContentTest3");
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
				throw new Exception("SetGetBase64ContentTest3 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetBase64ContentTest3 failed", e);
		}
	}

	/*
	 * @testName: SetBase64ContentIOExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:133;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content and test for
	 * IOException.
	 */
	@Test
	public void SetBase64ContentIOExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setBase64ContentIOExceptionTest1");
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
				throw new Exception("SetBase64ContentIOExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetBase64ContentIOExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: SetBase64ContentNullPointerExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:133;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content and test for
	 * NullPointerException.
	 */
	@Test
	public void SetBase64ContentNullPointerExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setBase64ContentNullPointerExceptionTest1");
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
				throw new Exception("SetBase64ContentNullPointerExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetBase64ContentNullPointerExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: SetBase64ContentSOAPExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:133;
	 *
	 * @test_Strategy: Call AttachmentPart.setBase64Content and test for
	 * SOAPException.
	 */
	@Test
	public void SetBase64ContentSOAPExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setBase64ContentSOAPExceptionTest1");
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
				throw new Exception("SetBase64ContentSOAPExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetBase64ContentSOAPExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: GetBase64ContentSOAPExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:128;
	 *
	 * @test_Strategy: Call AttachmentPart.getBase64Content and test for
	 * SOAPException.
	 */
	@Test
	public void GetBase64ContentSOAPExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getBase64ContentSOAPExceptionTest1");
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
				throw new Exception("GetBase64ContentSOAPExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("GetBase64ContentSOAPExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: SetGetRawContentTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
	 * correct behavior.
	 */
	@Test
	public void SetGetRawContentTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetRawContentTest1");
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
				throw new Exception("SetGetRawContentTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetRawContentTest1 failed", e);
		}
	}

	/*
	 * @testName: SetGetRawContentTest2
	 *
	 * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
	 * correct behavior.
	 */
	@Test
	public void SetGetRawContentTest2() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetRawContentTest2");
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
				throw new Exception("SetGetRawContentTest2 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetRawContentTest2 failed", e);
		}
	}

	/*
	 * @testName: SetGetRawContentTest3
	 *
	 * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
	 * correct behavior.
	 */
	@Test
	public void SetGetRawContentTest3() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetRawContentTest3");
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
				throw new Exception("SetGetRawContentTest3 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetRawContentTest3 failed", e);
		}
	}

	/*
	 * @testName: SetGetRawContentTest4
	 *
	 * @assertion_ids: SAAJ:JAVADOC:125; SAAJ:JAVADOC:130;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent/getRawContent and verify
	 * correct behavior.
	 */
	@Test
	public void SetGetRawContentTest4() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetRawContentTest4");
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
				throw new Exception("SetGetRawContentTest4 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetRawContentTest4 failed", e);
		}
	}

	/*
	 * @testName: SetGetRawContentBytesTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:319; SAAJ:JAVADOC:321;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContentBytes/getRawContentBytes and
	 * verify correct behavior.
	 */
	@Test
	public void SetGetRawContentBytesTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetGetRawContentBytesTest1");
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
				throw new Exception("SetGetRawContentBytesTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetGetRawContentBytesTest1 failed", e);
		}
	}

	/*
	 * @testName: SetRawContentBytesSOAPOrNullPointerExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:322;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContentBytes with a null byte[]
	 * array and verify correct behavior.
	 */
	@Test
	public void SetRawContentBytesSOAPOrNullPointerExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "SetRawContentBytesSOAPOrNullPointerExceptionTest1");
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
				throw new Exception("SetRawContentBytesSOAPOrNullPointerExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetRawContentBytesSOAPOrNullPointerExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: GetRawContentBytesSOAPOrNullPointerExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:320;
	 *
	 * @test_Strategy: Call AttachmentPart.getRawContentBytes on an empty Attachment
	 * object and verify correct behavior.
	 */
	@Test
	public void GetRawContentBytesSOAPOrNullPointerExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "GetRawContentBytesSOAPOrNullPointerExceptionTest1");
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
				throw new Exception("GetRawContentBytesSOAPOrNullPointerExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("GetRawContentBytesSOAPOrNullPointerExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: SetRawContentIOExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:131;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent and test for IOException.
	 */
	@Test
	public void SetRawContentIOExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setRawContentIOExceptionTest1");
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
				throw new Exception("SetRawContentIOExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetRawContentIOExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: SetRawContentNullPointerExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:131;
	 *
	 * @test_Strategy: Call AttachmentPart.setRawContent and test for
	 * NullPointerException.
	 */
	@Test
	public void SetRawContentNullPointerExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "setRawContentNullPointerExceptionTest1");
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
				throw new Exception("SetRawContentNullPointerExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("SetRawContentNullPointerExceptionTest1 failed", e);
		}
	}

	/*
	 * @testName: GetRawContentSOAPExceptionTest1
	 *
	 * @assertion_ids: SAAJ:JAVADOC:126;
	 *
	 * @test_Strategy: Call AttachmentPart.getRawContent and test for SOAPException.
	 */
	@Test
	public void GetRawContentSOAPExceptionTest1() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getRawContentSOAPExceptionTest1");
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
				throw new Exception("GetRawContentSOAPExceptionTest1 failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("GetRawContentSOAPExceptionTest1 failed", e);
		}
	}
}
