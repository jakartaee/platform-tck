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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPPart;

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

	private static final String TESTSERVLET = "/SOAPPart_web/SOAPPartTestServlet";

	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPPart_web");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPPart");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		final String CONTENT_ROOT = URLClient.class.getPackageName().replace(".", "/") + "/contentRoot/";
		String[] attachement = { "attach.xml", "attach12.xml" };
		addFilesToArchive(CONTENT_ROOT, attachement, archive);
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	/*
	 * @testName: addMimeHeader1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MIMEHeader object. Add a single header.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Add two headers.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Add two headers that have different
	 * values.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Attempt to add an empty header and
	 * value.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Attempt to add an empty header and
	 * non-empty value.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Attempt to add a non-empty header and
	 * empty value.
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
	 * @assertion_ids: SAAJ:JAVADOC:12;
	 *
	 * @test_Strategy: Call SOAPPart.addMimeHeader(String,String) method and verify
	 * creation of a new MimeHeader object. Attempt to add a null header and null
	 * value.
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
	 * @assertion_ids: SAAJ:JAVADOC:10;
	 *
	 * @test_Strategy: Call SOAPPart.getMimeHeader(String,String) method and verify
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
	 * @assertion_ids: SAAJ:JAVADOC:10;
	 *
	 * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify return
	 * of the MimeHeader object. Get single header from multiple headers.
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
	 * @assertion_ids: SAAJ:JAVADOC:10;
	 *
	 * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify return
	 * of the MimeHeader object. Get header that contains two entries.
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
	 * @assertion_ids: SAAJ:JAVADOC:10;
	 *
	 * @test_Strategy: Call SOAPPart.getMimeHeader(String) method and verify return
	 * of the MimeHeader object. Attempt to get a header that doesn't exist
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
	 * @assertion_ids: SAAJ:JAVADOC:13;
	 *
	 * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return of
	 * all MimeHeader objects. Get single header.
	 *
	 * Description: Retrieve a MimeHeader object.
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
	 * @assertion_ids: SAAJ:JAVADOC:13;
	 *
	 * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return of
	 * all MimeHeader objects. Get multiple headers.
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
	 * @assertion_ids: SAAJ:JAVADOC:13;
	 *
	 * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return of
	 * all MimeHeader objects. Get single header that contains mulitple entries.
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
	 * @assertion_ids: SAAJ:JAVADOC:13;
	 *
	 * @test_Strategy: Call SOAPPart.getAllMimeHeaders() method and verify return of
	 * all MimeHeader objects. Attempt to get all headers when none exist.
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
	 * @assertion_ids: SAAJ:JAVADOC:9;
	 *
	 * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
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
	 * @assertion_ids: SAAJ:JAVADOC:9;
	 *
	 * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
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
	 * @assertion_ids: SAAJ:JAVADOC:9;
	 *
	 * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
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
	 * @assertion_ids: SAAJ:JAVADOC:9;
	 *
	 * @test_Strategy: Call SOAPPart.removeAllMimeHeaders() method and verify
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
	 * @assertion_ids: SAAJ:JAVADOC:11;
	 *
	 * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and verify
	 * return of a the MimeHeader object. Set exist header.
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
	 * @assertion_ids: SAAJ:JAVADOC:11;
	 *
	 * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and verify
	 * return of the MimeHeader object. Set existing header from list of two
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
	 * @assertion_ids: SAAJ:JAVADOC:11;
	 *
	 * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and verify
	 * return of the MimeHeader object. Set existing header that contains multiple
	 * values.
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
	 * @assertion_ids: SAAJ:JAVADOC:11;
	 *
	 * @test_Strategy: Call SOAPPart.setMimeHeader(String,String) method and verify
	 * return of the MimeHeader object. Set header that doesn't exist
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
	 * @assertion_ids: SAAJ:JAVADOC:11;
	 *
	 * @test_Strategy: Call SOAPPart.setMimeHeader(Name,emptyvalue) method and
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
	 * @assertion_ids: SAAJ:JAVADOC:8;
	 *
	 * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
	 * return of a the MimeHeader object. Remove single header.
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
	 * @assertion_ids: SAAJ:JAVADOC:8;
	 *
	 * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
	 * return of the MimeHeader object. Remove single header from list of two.
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
	 * @assertion_ids: SAAJ:JAVADOC:8;
	 *
	 * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
	 * return of the MimeHeader object. Remove single header that contains multiple
	 * values.
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
	 * @assertion_ids: SAAJ:JAVADOC:8;
	 *
	 * @test_Strategy: Call SOAPPart.removeMimeHeader(String) method and verify
	 * return of the MimeHeader object. Remove header that doesn't exist
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
	 * @assertion_ids: SAAJ:JAVADOC:14;
	 *
	 * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
	 * verify return of a the MimeHeader object. Get single header.
	 *
	 * Description: Retrieve a MimeHeader object.
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
	 * @assertion_ids: SAAJ:JAVADOC:14;
	 *
	 * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Get single header from list of two.
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
	 * @assertion_ids: SAAJ:JAVADOC:14;
	 *
	 * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Get single header that contains
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
	 * @assertion_ids: SAAJ:JAVADOC:14;
	 *
	 * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Attempt to get a header that doesn't
	 * exist
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
	 * @assertion_ids: SAAJ:JAVADOC:14;
	 *
	 * @test_Strategy: Call SOAPPart.getMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Attempt to get a headers and a
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
	 * @assertion_ids: SAAJ:JAVADOC:15;
	 *
	 * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method and
	 * verify return of a the MimeHeader object.
	 *
	 * Description: Retrieve a MimeHeader object. Get single header.
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
	 * @assertion_ids: SAAJ:JAVADOC:15;
	 *
	 * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Get single header from list of two.
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
	 * @assertion_ids: SAAJ:JAVADOC:15;
	 *
	 * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Get single header that contains
	 * multiple values.
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
	 * @assertion_ids: SAAJ:JAVADOC:15;
	 *
	 * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Attempt to get header that results in
	 * no headers being returned.
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
	 * @assertion_ids: SAAJ:JAVADOC:15;
	 *
	 * @test_Strategy: Call SOAPPart.getNonMatchingMimeHeaders(String[]) method and
	 * verify return of the MimeHeader object. Attempt to get a header and a
	 * non-existent header
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
	 * @assertion_ids: SAAJ:JAVADOC:6;
	 *
	 * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no errors
	 * occur. Attempt to set id to a valid value.
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
	 * @assertion_ids: SAAJ:JAVADOC:6;
	 *
	 * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no errors
	 * occur. Attempt to set id to null.
	 *
	 * Description: Set the Mime Header named Content-Id
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
	 * @assertion_ids: SAAJ:JAVADOC:6;
	 *
	 * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no errors
	 * occur. Set the id twice in a row.
	 *
	 * Description: Set the Mime Header named Content-Id
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
	 * @assertion_ids: SAAJ:JAVADOC:6;
	 *
	 * @test_Strategy: Call SOAPPart.setContentId(value) method and verify no errors
	 * occur. Attempt to set id to a valid value then to another string.
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
	 * @assertion_ids: SAAJ:JAVADOC:4;
	 *
	 * @test_Strategy: Call SOAPPart.getContentId() method and verify correct
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
	 * @assertion_ids: SAAJ:JAVADOC:4;
	 *
	 * @test_Strategy: Call SOAPPart.getContentId() method and verify correct
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
	 * @testName: getContentId3Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:4;
	 *
	 * @test_Strategy: Call SOAPPart.getContentId(value) method and verify correct
	 * content id is returned. Get id when non has been set.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void getContentId3Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentId3Test");
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
				throw new Exception("getContentId3Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentId3Test failed", e);
		}
	}

	/*
	 * @testName: setContentLocation1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:7;
	 *
	 * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify no
	 * errors occur. Set URL location
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
	 * @assertion_ids: SAAJ:JAVADOC:7;
	 *
	 * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify no
	 * errors occur.
	 *
	 * Description: Set the Mime Header named Content-Id Set empty string location.
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
	 * @assertion_ids: SAAJ:JAVADOC:7;
	 *
	 * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify no
	 * errors occur. Set URI location
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
	 * @assertion_ids: SAAJ:JAVADOC:7;
	 *
	 * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify no
	 * errors occur. Set location twice.
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
	 * @assertion_ids: SAAJ:JAVADOC:7;
	 *
	 * @test_Strategy: Call SOAPPart.setContentLocation(String) method and verify no
	 * errors occur. Set location to null.
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
	 * @assertion_ids: SAAJ:JAVADOC:5;
	 *
	 * @test_Strategy: Call SOAPPart.getContentLocation() method and verify correct
	 * content location is returned. Get URL location.
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
	 * @assertion_ids: SAAJ:JAVADOC:5;
	 *
	 * @test_Strategy: Call SOAPPart.getContentLocation() method and verify correct
	 * content location is returned. Get location when none has been set.
	 *
	 * Description: Get the Mime Header named Content-Id
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
	 * @assertion_ids: SAAJ:JAVADOC:5;
	 *
	 * @test_Strategy: Call SOAPPart.getContentLocation() method and verify correct
	 * content location is returned. Get location after it has been set twice.
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
	 * @testName: getContentLocation4Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:5;
	 *
	 * @test_Strategy: Call SOAPPart.getContentLocation() method and verify correct
	 * content location is returned. Get URI location.
	 *
	 * Description: Set the Mime Header named Content-Id
	 */
	@Test
	public void getContentLocation4Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getContentLocation4Test");
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
				throw new Exception("getContentLocation4Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getContentLocation4Test failed", e);
		}
	}

	/*
	 * @testName: setContent1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:16; SAAJ:JAVADOC:17;
	 *
	 * @test_Strategy: Call SOAPPart.setContent(Source) method and verify no
	 * exception is thrown.
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
	 * @testName: getContent1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:18; SAAJ:JAVADOC:19;
	 *
	 * @test_Strategy: Call SOAPPart.getContent() method and verify a non null
	 * source object is returned.
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
	 * @testName: getEnvelope1Test
	 *
	 * @assertion_ids: SAAJ:JAVADOC:2; SAAJ:JAVADOC:3;
	 *
	 * @test_Strategy: Call SOAPPart.getEnvelope() method and verify envelope is
	 * returned.
	 *
	 * Description: Get the Envelope
	 */
	@Test
	public void getEnvelope1Test() throws Exception {
		try {
			boolean pass = true;

			logger.log(Logger.Level.INFO, "Creating url to test servlet.....");
			url = tsurl.getURL(PROTOCOL, hostname, portnum, TESTSERVLET);
			logger.log(Logger.Level.INFO, url.toString());
			for (int i = 0; i < 2; i++) {
				logger.log(Logger.Level.INFO, "Sending post request to test servlet.....");
				props.setProperty("TESTNAME", "getEnvelope1Test");
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
				throw new Exception("getEnvelope1Test failed");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("getEnvelope1Test failed", e);
		}
	}
}
