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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPPart;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFault.URLClient;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPPartTestServlet extends HttpServlet {
	private static final Logger logger = (Logger) System.getLogger(URLClient.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private MimeHeader mh = null;

	private Iterator iterator = null;

	private InputStream in = null;

	private InputStream in2 = null;

	private StreamSource ssrc = null;

	private StreamSource ssrc2 = null;

	private DataHandler dh = null;

	private DataHandler dh2 = null;

	private URL url = null;

	private URL url2 = null;

	private TSURL tsurl = new TSURL();

	private String cntxroot = "/SOAPPart_web";

	private String host = null;

	private int port = 0;

	String soapVersion = null;

	private static final String SOAP12 = "soap12";

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "SOAPElement_web");
		archive.addPackages(false, Filters.exclude(URLClient.class),
				"com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElement");
		archive.addPackages(false, "com.sun.ts.tests.saaj.common");
		archive.addAsWebInfResource(URLClient.class.getPackage(), "standalone.web.xml", "web.xml");
		return archive;
	};

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.INFO, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();

		// Message creation takes care of creating the SOAPPart - a
		// required part of the message as per the SOAP 1.1
		// specification.
		sp = msg.getSOAPPart();

		// Remove any MimeHeaders if any
		sp.removeAllMimeHeaders();

		// Get host and port info
		host = SOAP_Util.getHostname();
		port = SOAP_Util.getPortnum();

		// soap 11 envelope
		url = tsurl.getURL("http", host, port, cntxroot + "/attach.xml");
		logger.log(Logger.Level.INFO, "Get DataHandler to xml attachment: attach.xml");
		logger.log(Logger.Level.INFO, "URL = " + url);
		dh = new DataHandler(url);
		logger.log(Logger.Level.INFO, "Get InputStream of DataHandler");
		in = dh.getInputStream();
		logger.log(Logger.Level.INFO, "in = " + in);
		logger.log(Logger.Level.INFO, "Get StreamSource from InputStream");
		ssrc = new StreamSource(in);
		logger.log(Logger.Level.INFO, "ssrc = " + ssrc);

		// soap 12 envelope
		url2 = tsurl.getURL("http", host, port, cntxroot + "/attach12.xml");
		logger.log(Logger.Level.INFO, "Get DataHandler to xml attachment: attach12.xml");
		logger.log(Logger.Level.INFO, "URL = " + url2);
		dh2 = new DataHandler(url2);
		logger.log(Logger.Level.INFO, "Get InputStream of DataHandler");
		in2 = dh2.getInputStream();
		logger.log(Logger.Level.INFO, "in2 = " + in2);
		logger.log(Logger.Level.INFO, "Get StreamSource from InputStream");
		ssrc2 = new StreamSource(in2);
		logger.log(Logger.Level.INFO, "ssrc2 = " + ssrc2);
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		soapVersion = SOAP_Util.getHarnessProps().getProperty("SOAPVERSION");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addMimeHeader1Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader1Test");
			addMimeHeader1Test(req, res);
		} else if (testname.equals("addMimeHeader2Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader2Test");
			addMimeHeader2Test(req, res);
		} else if (testname.equals("addMimeHeader3Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader3Test");
			addMimeHeader3Test(req, res);
		} else if (testname.equals("addMimeHeader4Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader4Test");
			addMimeHeader4Test(req, res);
		} else if (testname.equals("addMimeHeader5Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader5Test");
			addMimeHeader5Test(req, res);
		} else if (testname.equals("addMimeHeader6Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader6Test");
			addMimeHeader6Test(req, res);
		} else if (testname.equals("addMimeHeader7Test")) {
			logger.log(Logger.Level.INFO, "Starting addMimeHeader7Test");
			addMimeHeader7Test(req, res);
		} else if (testname.equals("getAllMimeHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders1Test");
			getAllMimeHeaders1Test(req, res);
		} else if (testname.equals("getAllMimeHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders2Test");
			getAllMimeHeaders2Test(req, res);
		} else if (testname.equals("getAllMimeHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders3Test");
			getAllMimeHeaders3Test(req, res);
		} else if (testname.equals("getAllMimeHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders4Test");
			getAllMimeHeaders4Test(req, res);
		} else if (testname.equals("getContentId1Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentId1Test");
			getContentId1Test(req, res);
		} else if (testname.equals("getContentId2Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentId2Test");
			getContentId2Test(req, res);
		} else if (testname.equals("getContentId3Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentId3Test");
			getContentId3Test(req, res);
		} else if (testname.equals("getContentLocation1Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentLocation1Test");
			getContentLocation1Test(req, res);
		} else if (testname.equals("getContentLocation2Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentLocation2Test");
			getContentLocation2Test(req, res);
		} else if (testname.equals("getContentLocation3Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentLocation3Test");
			getContentLocation3Test(req, res);
		} else if (testname.equals("getContentLocation4Test")) {
			logger.log(Logger.Level.INFO, "Starting getContentLocation4Test");
			getContentLocation4Test(req, res);
		} else if (testname.equals("getEnvelope1Test")) {
			logger.log(Logger.Level.INFO, "Starting getEnvelope1Test");
			getEnvelope1Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders1Test")) {
			logger.log(Logger.Level.INFO, "Starting getMatchingMimeHeaders1Test");
			getMatchingMimeHeaders1Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders2Test")) {
			logger.log(Logger.Level.INFO, "Starting getMatchingMimeHeaders2Test");
			getMatchingMimeHeaders2Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders3Test")) {
			logger.log(Logger.Level.INFO, "Starting getMatchingMimeHeaders3Test");
			getMatchingMimeHeaders3Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders4Test")) {
			logger.log(Logger.Level.INFO, "Starting getMatchingMimeHeaders4Test");
			getMatchingMimeHeaders4Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders5Test")) {
			logger.log(Logger.Level.INFO, "Starting getMatchingMimeHeaders5Test");
			getMatchingMimeHeaders5Test(req, res);
		} else if (testname.equals("getMimeHeader1Test")) {
			logger.log(Logger.Level.INFO, "Starting getMimeHeader1Test");
			getMimeHeader1Test(req, res);
		} else if (testname.equals("getMimeHeader2Test")) {
			logger.log(Logger.Level.INFO, "Starting getMimeHeader2Test");
			getMimeHeader2Test(req, res);
		} else if (testname.equals("getMimeHeader3Test")) {
			logger.log(Logger.Level.INFO, "Starting getMimeHeader3Test");
			getMimeHeader3Test(req, res);
		} else if (testname.equals("getMimeHeader4Test")) {
			logger.log(Logger.Level.INFO, "Starting getMimeHeader4Test");
			getMimeHeader4Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders1Test")) {
			logger.log(Logger.Level.INFO, "Starting getNonMatchingMimeHeaders1Test");
			getNonMatchingMimeHeaders1Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders2Test")) {
			logger.log(Logger.Level.INFO, "Starting getNonMatchingMimeHeaders2Test");
			getNonMatchingMimeHeaders2Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders3Test")) {
			logger.log(Logger.Level.INFO, "Starting getNonMatchingMimeHeaders3Test");
			getNonMatchingMimeHeaders3Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders4Test")) {
			logger.log(Logger.Level.INFO, "Starting getNonMatchingMimeHeaders4Test");
			getNonMatchingMimeHeaders4Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders5Test")) {
			logger.log(Logger.Level.INFO, "Starting getNonMatchingMimeHeaders5Test");
			getNonMatchingMimeHeaders5Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders1Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllMimeHeaders1Test");
			removeAllMimeHeaders1Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders2Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllMimeHeaders2Test");
			removeAllMimeHeaders2Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders3Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllMimeHeaders3Test");
			removeAllMimeHeaders3Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders4Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllMimeHeaders4Test");
			removeAllMimeHeaders4Test(req, res);
		} else if (testname.equals("removeMimeHeader1Test")) {
			logger.log(Logger.Level.INFO, "Starting removeMimeHeader1Test");
			removeMimeHeader1Test(req, res);
		} else if (testname.equals("removeMimeHeader2Test")) {
			logger.log(Logger.Level.INFO, "Starting removeMimeHeader2Test");
			removeMimeHeader2Test(req, res);
		} else if (testname.equals("removeMimeHeader3Test")) {
			logger.log(Logger.Level.INFO, "Starting removeMimeHeader3Test");
			removeMimeHeader3Test(req, res);
		} else if (testname.equals("removeMimeHeader4Test")) {
			logger.log(Logger.Level.INFO, "Starting removeMimeHeader4Test");
			removeMimeHeader4Test(req, res);
		} else if (testname.equals("setContentId1Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentId1Test");
			setContentId1Test(req, res);
		} else if (testname.equals("setContentId2Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentId2Test");
			setContentId2Test(req, res);
		} else if (testname.equals("setContentId3Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentId3Test");
			setContentId3Test(req, res);
		} else if (testname.equals("setContentId4Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentId4Test");
			setContentId4Test(req, res);
		} else if (testname.equals("setContentLocation1Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentLocation1Test");
			setContentLocation1Test(req, res);
		} else if (testname.equals("setContentLocation2Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentLocation2Test");
			setContentLocation2Test(req, res);
		} else if (testname.equals("setContentLocation3Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentLocation3Test");
			setContentLocation3Test(req, res);
		} else if (testname.equals("setContentLocation4Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentLocation4Test");
			setContentLocation4Test(req, res);
		} else if (testname.equals("setContentLocation5Test")) {
			logger.log(Logger.Level.INFO, "Starting setContentLocation5Test");
			setContentLocation5Test(req, res);
		} else if (testname.equals("setContent1Test")) {
			logger.log(Logger.Level.INFO, "Starting setContent1Test");
			setContent1Test(req, res);
		} else if (testname.equals("getContent1Test")) {
			logger.log(Logger.Level.INFO, "Starting getContent1Test");
			getContent1Test(req, res);
		} else if (testname.equals("setMimeHeader1Test")) {
			logger.log(Logger.Level.INFO, "Starting setMimeHeader1Test");
			setMimeHeader1Test(req, res);
		} else if (testname.equals("setMimeHeader2Test")) {
			logger.log(Logger.Level.INFO, "Starting setMimeHeader2Test");
			setMimeHeader2Test(req, res);
		} else if (testname.equals("setMimeHeader3Test")) {
			logger.log(Logger.Level.INFO, "Starting setMimeHeader3Test");
			setMimeHeader3Test(req, res);
		} else if (testname.equals("setMimeHeader4Test")) {
			logger.log(Logger.Level.INFO, "Starting setMimeHeader4Test");
			setMimeHeader4Test(req, res);
		} else if (testname.equals("setMimeHeader5Test")) {
			logger.log(Logger.Level.INFO, "Starting setMimeHeader5Test");
			setMimeHeader5Test(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");

		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("AddHeaderTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("AddHeaderTestServlet:init (Leaving)");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "doGet");
		dispatch(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "doPost");
		SOAP_Util.doServletPost(req, res);
		doGet(req, res);
	}

	private void displayArray(String[] array) {
		int len = array.length;
		for (int i = 0; i < len; i++) {
			logger.log(Logger.Level.INFO, "array[" + i + "]=" + array[i]);
		}
	}

	private void displayHeaders() {
		logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
		iterator = sp.getAllMimeHeaders();
		while (iterator.hasNext()) {
			mh = (MimeHeader) iterator.next();
			String name = mh.getName();
			String value = mh.getValue();
			logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
		}
	}

	private void addMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with invalid arguments");
			sp.addMimeHeader("", "");

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with invalid arguments");
			sp.addMimeHeader("", "some text");

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader6Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader6Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with null value");
			sp.addMimeHeader("Content-Description", null);

			logger.log(Logger.Level.INFO, "Adding MimeHeader with null string value");
			sp.addMimeHeader("Content-Description", "");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader7Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader7Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with invalid arguments");
			sp.addMimeHeader(null, null);

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeader did match");
				} else {
					logger.log(Logger.Level.ERROR, "MimeHeader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}

			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			boolean foundDefaultHeader = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			boolean foundDefaultHeader = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Type") && value.equals("text/xml")) {
					if (!foundDefaultHeader) {
						foundDefaultHeader = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for the default MimeHeader ");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same the default MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentId1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentId1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			sp.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Getting Content Id");
			String result = sp.getContentId();

			if (!result.equals("id@abc.com")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid value from getContentId()");
				logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentId2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentId2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			sp.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Setting Content Id again");
			sp.setContentId("id2@abc2.com");

			logger.log(Logger.Level.INFO, "Getting Content Id");
			String result = sp.getContentId();

			if (!result.equals("id2@abc2.com")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid value from getContentId()");
				logger.log(Logger.Level.ERROR, "expected result: id2@abc2.com");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentId3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentId3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String result = null;
			logger.log(Logger.Level.INFO, "Remove all MimeHeader's");
			sp.removeAllMimeHeaders();
			logger.log(Logger.Level.INFO, "Getting Content Id");
			result = sp.getContentId();

			if (result != null && !result.equals("")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid value from getContentId()");
				logger.log(Logger.Level.ERROR, "expected result: null or null string");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			sp.setContentLocation("http://localhost/someapp");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = sp.getContentLocation();

			if (!result.equals("http://localhost/someapp")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: http://localhost/someapp");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = sp.getContentLocation();

			if (result != null) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: null");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			sp.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = sp.getContentLocation();

			if (!result.equals("/WEB-INF/somefile")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: /WEB-INF/somefile");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			sp.setContentLocation("http://localhost/someapp");

			logger.log(Logger.Level.INFO, "Setting Content Location again");
			sp.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = sp.getContentLocation();

			if (!result.equals("/WEB-INF/somefile")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: /WEB-INF/somefile");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getEnvelope1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getEnvelope1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			envelope = sp.getEnvelope();
			if (envelope == null) {
				logger.log(Logger.Level.ERROR, "Error: received null value from getEnvelope()");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			displayHeaders();

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = sp.getMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			displayHeaders();

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = sp.getMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

			displayHeaders();

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = sp.getMatchingMimeHeaders(sArray);
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected three items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			displayHeaders();

			String sArray[] = { "doesnotexist" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non-existent MimeHeader");
			iterator = sp.getMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void getMatchingMimeHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 3 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			displayHeaders();

			String sArray[] = { "Content-Description", "Content-Id", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = sp.getMatchingMimeHeaders(sArray);
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			boolean foundHeader3 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader3) {
						foundHeader3 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header3");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header3 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2 && foundHeader3)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 3) {
				logger.log(Logger.Level.ERROR, "Error: expected three items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");
			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			sArray = sp.getMimeHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Id), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getMimeHeader(Content-Id)");
					logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");
			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 2) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text") && !temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text or some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting non-existent MimeHeader");
			String sArray[] = sp.getMimeHeader("doesnotexist");
			if (sArray != null && sArray.length > 0) {
				logger.log(Logger.Level.ERROR, "Error: was able to get a non-existent MimeHeader");
				pass = false;
				int len = sArray.length;
				for (int i = 0; i < len; i++) {
					logger.log(Logger.Level.ERROR, "actual result:" + sArray[i]);
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			displayHeaders();

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = sp.getNonMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			displayHeaders();

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = sp.getNonMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Remove any MimeHeaders if any
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

			displayHeaders();

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = sp.getNonMatchingMimeHeaders(sArray);
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Remove any MimeHeaders if any
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			displayHeaders();

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = sp.getNonMatchingMimeHeaders(sArray);
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Remove any MimeHeaders if any
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Adding 3 MimeHeaders");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			displayHeaders();

			String sArray[] = { "Content-Id", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = sp.getNonMatchingMimeHeaders(sArray);
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders...");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders...");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected Mimeheader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders...");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders...");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected Mimeheader");
				logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders...");
			sp.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders...");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected Mimeheader");
				logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders...");
			sp.removeAllMimeHeaders();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			sp.removeMimeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got atotal of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			sp.removeMimeHeader("Content-Id");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeader did match");
				} else {
					logger.log(Logger.Level.ERROR, "MimeHeader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			sp.removeMimeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			sp.removeMimeHeader("doesnotexist");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = sp.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void setContentId1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Id");
			sp.setContentId("name@abc.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.TRACE, "Setting Content Id to null value");
			sp.setContentId(null);
			logger.log(Logger.Level.TRACE, "Setting Content Id to null string value");
			sp.setContentId("");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Id");
			sp.setContentId("id@abc.com");

			logger.log(Logger.Level.TRACE, "Setting Content Id again");
			sp.setContentId("id2@abc2.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Id");
			sp.setContentId("id@abc.com");

			logger.log(Logger.Level.TRACE, "Setting Content Id again");
			sp.setContentId("id@cde.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Location");
			sp.setContentLocation("http://localhost/someapp");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Location to null string value");
			sp.setContentLocation("");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Location again");
			sp.setContentLocation("/WEB-INF/somefile");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Location again");
			sp.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.TRACE, "Setting Content Location again");
			sp.setContentLocation("http://localhost/someapp");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content Location to null value");
			sp.setContentLocation(null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContent1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContent1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.TRACE, "Setting Content ");
			if (!soapVersion.equals(SOAP12)) {
				sp.setContent(ssrc);
			} else {
				sp.setContent(ssrc2);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContent1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting Content ");
			if (!soapVersion.equals(SOAP12)) {
				sp.setContent(ssrc);
			} else {
				sp.setContent(ssrc2);
			}

			logger.log(Logger.Level.TRACE, "Getting Content ");
			Source ssrc3 = null;
			ssrc3 = sp.getContent();
			if (ssrc3 == null) {
				logger.log(Logger.Level.ERROR, "Error: getContent() returned null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.TRACE, "Setting MimeHeader");
			sp.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from setMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");
			sp.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.TRACE, "Setting MimeHeader");
			sp.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from setMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			sArray = sp.getMimeHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(name2), got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getMimeHeader(Content-Id)");
					logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Adding MimeHeader");
			sp.setMimeHeader("Content-Description", "some text");
			sp.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.TRACE, "Setting MimeHeader");
			sp.setMimeHeader("Content-Description", "image/jpeg");

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("image/jpeg")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: image/jpeg");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Setting MimeHeader");
			sp.setMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void setMimeHeader5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Adding MimeHeader");
			sp.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.TRACE, "Setting MimeHeader");
			sp.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.TRACE, "Setting MimeHeader again");
			sp.setMimeHeader("Content-Description", "impage/jpeg");

			logger.log(Logger.Level.TRACE, "Getting MimeHeader");
			String sArray[] = sp.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("impage/jpeg")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: impage/jpeg");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

}
