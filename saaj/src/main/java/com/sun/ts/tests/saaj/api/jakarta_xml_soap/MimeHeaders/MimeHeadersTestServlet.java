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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MimeHeaders;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class MimeHeadersTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(MimeHeadersTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private MimeHeaders mimeHeaders = null;

	private MimeHeader mh = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.TRACE, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();

		// Message creation takes care of creating the SOAPPart - a
		// required part of the message as per the SOAP 1.1
		// specification.
		sp = msg.getSOAPPart();

	}

	private void displayArray(String[] array) {
		int len = array.length;
		for (int i = 0; i < len; i++) {
			logger.log(Logger.Level.TRACE, "array[" + i + "]=" + array[i]);
		}
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader1Test");
			addHeader1Test(req, res);
		} else if (testname.equals("addHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader2Test");
			addHeader2Test(req, res);
		} else if (testname.equals("addHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader3Test");
			addHeader3Test(req, res);
		} else if (testname.equals("addHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader4Test");
			addHeader4Test(req, res);
		} else if (testname.equals("addHeader5Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader5Test");
			addHeader5Test(req, res);
		} else if (testname.equals("addHeader6Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader6Test");
			addHeader6Test(req, res);
		} else if (testname.equals("addHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader1Test");
			addHeader1Test(req, res);
		} else if (testname.equals("addHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader2Test");
			addHeader2Test(req, res);
		} else if (testname.equals("addHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader3Test");
			addHeader3Test(req, res);
		} else if (testname.equals("addHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader4Test");
			addHeader4Test(req, res);
		} else if (testname.equals("addHeader5Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader5Test");
			addHeader5Test(req, res);
		} else if (testname.equals("addHeader6Test")) {
			logger.log(Logger.Level.TRACE, "Starting addHeader6Test");
			addHeader6Test(req, res);
		} else if (testname.equals("getAllHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllHeaders1Test");
			getAllHeaders1Test(req, res);
		} else if (testname.equals("getAllHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllHeaders2Test");
			getAllHeaders2Test(req, res);
		} else if (testname.equals("getAllHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllHeaders3Test");
			getAllHeaders3Test(req, res);
		} else if (testname.equals("getAllHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllHeaders4Test");
			getAllHeaders4Test(req, res);
		} else if (testname.equals("getHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getHeader1Test");
			getHeader1Test(req, res);
		} else if (testname.equals("getHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getHeader2Test");
			getHeader2Test(req, res);
		} else if (testname.equals("getHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getHeader3Test");
			getHeader3Test(req, res);
		} else if (testname.equals("getHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getHeader4Test");
			getHeader4Test(req, res);
		} else if (testname.equals("getMatchingHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingHeaders1Test");
			getMatchingHeaders1Test(req, res);
		} else if (testname.equals("getMatchingHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingHeaders2Test");
			getMatchingHeaders2Test(req, res);
		} else if (testname.equals("getMatchingHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingHeaders3Test");
			getMatchingHeaders3Test(req, res);
		} else if (testname.equals("getMatchingHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingHeaders4Test");
			getMatchingHeaders4Test(req, res);
		} else if (testname.equals("getMatchingHeaders5Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingHeaders5Test");
			getMatchingHeaders5Test(req, res);
		} else if (testname.equals("getNonMatchingHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingHeaders1Test");
			getNonMatchingHeaders1Test(req, res);
		} else if (testname.equals("getNonMatchingHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingHeaders2Test");
			getNonMatchingHeaders2Test(req, res);
		} else if (testname.equals("getNonMatchingHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingHeaders3Test");
			getNonMatchingHeaders3Test(req, res);
		} else if (testname.equals("getNonMatchingHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingHeaders4Test");
			getNonMatchingHeaders4Test(req, res);
		} else if (testname.equals("getNonMatchingHeaders5Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingHeaders5Test");
			getNonMatchingHeaders5Test(req, res);
		} else if (testname.equals("removeAllHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllHeaders1Test");
			removeAllHeaders1Test(req, res);
		} else if (testname.equals("removeAllHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllHeaders2Test");
			removeAllHeaders2Test(req, res);
		} else if (testname.equals("removeAllHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllHeaders3Test");
			removeAllHeaders3Test(req, res);
		} else if (testname.equals("removeAllHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllHeaders4Test");
			removeAllHeaders4Test(req, res);
		} else if (testname.equals("removeHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeHeader1Test");
			removeHeader1Test(req, res);
		} else if (testname.equals("removeHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeHeader2Test");
			removeHeader2Test(req, res);
		} else if (testname.equals("removeHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeHeader3Test");
			removeHeader3Test(req, res);
		} else if (testname.equals("removeHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeHeader4Test");
			removeHeader4Test(req, res);
		} else if (testname.equals("setHeader1Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader1Test");
			setHeader1Test(req, res);
		} else if (testname.equals("setHeader2Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader2Test");
			setHeader2Test(req, res);
		} else if (testname.equals("setHeader3Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader3Test");
			setHeader3Test(req, res);
		} else if (testname.equals("setHeader4Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader4Test");
			setHeader4Test(req, res);
		} else if (testname.equals("setHeader5Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader5Test");
			setHeader5Test(req, res);
		} else if (testname.equals("setHeader6Test")) {
			logger.log(Logger.Level.INFO, "Starting setHeader6Test");
			setHeader6Test(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");

		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"MimeHeadersTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		logger.log(Logger.Level.TRACE,"MimeHeadersTestServlet:init (Leaving)");
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

	private void addHeader1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

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

	private void addHeader2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

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

	private void addHeader3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

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

	private void addHeader4Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader(null, "some text");

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			pass = true;
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

	private void addHeader5Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header with null value");
			mimeHeaders.addHeader("Content-Description", null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header with null string value");
			mimeHeaders.addHeader("Content-Description", "");
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

	private void addHeader6Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeader6Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader(null, null);

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			pass = true;
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

	private void getAllHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (!(name.equals("Content-Description") && value.equals("some text"))) {
					logger.log(Logger.Level.ERROR, "Mimeheader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MimeHeader did match");
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + cnt);
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Try a second time");
			Iterator iterator2 = mimeHeaders.getAllHeaders();
			int cnt2 = 0;
			while (iterator2.hasNext()) {
				cnt2++;
				mh = (MimeHeader) iterator2.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (!(name.equals("Content-Description") && value.equals("some text"))) {
					logger.log(Logger.Level.ERROR, "Mimeheader did not match, second time through");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MimeHeader did match second time through");
				}
			}
			if (cnt2 != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned second time through, got a total of:" + cnt2);
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

	private void getAllHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			Iterator iterator = mimeHeaders.getAllHeaders();
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
				pass = false;
			}

			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Try a second time");
			Iterator iterator2 = mimeHeaders.getAllHeaders();
			int cnt2 = 0;
			foundHeader1 = false;
			foundHeader2 = false;
			while (iterator2.hasNext()) {
				cnt2++;
				mh = (MimeHeader) iterator2.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1, second time through");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 header twice, second time through");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2, second time through");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 header twice, second time through");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header , the second time through");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				TestUtil.logErr("Error: did not receive both headers, second time through");
				pass = false;
			}

			if (cnt != 2) {
				logger.log(Logger.Level.ERROR,
						"Error: expected two items to be returned second time through, got a total of:" + cnt);
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

	private void getAllHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

			Iterator iterator = mimeHeaders.getAllHeaders();
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Try a second time");
			Iterator iterator2 = mimeHeaders.getAllHeaders();
			int cnt2 = 0;
			foundHeader1 = false;
			foundHeader2 = false;
			while (iterator2.hasNext()) {
				cnt2++;
				mh = (MimeHeader) iterator2.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1, second time through");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 header twice, second time through");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2, second time through");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 header twice, second time through");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header , the second time through");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				TestUtil.logErr("Error: did not receive both headers, second time through");
				pass = false;
			}

			if (cnt != 2) {
				logger.log(Logger.Level.ERROR,
						"Error: expected two items to be returned second time through, got a total of:" + cnt);
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

	private void getAllHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getHeader1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
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

	private void getHeader2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			sArray = mimeHeaders.getHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Id), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Id)");
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

	private void getHeader3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 2) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text") && !temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
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

	private void getHeader4Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting non-existent header");
			String sArray[] = mimeHeaders.getHeader("doesnotexist");
			if (sArray != null && sArray.length > 0) {
				logger.log(Logger.Level.ERROR, "Error: was able to get a non-existent Header");
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

	private void getMatchingHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching headers");
			iterator = mimeHeaders.getMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getMatchingHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching headers");
			iterator = mimeHeaders.getMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getMatchingHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching headers");
			iterator = mimeHeaders.getMatchingHeaders(sArray);
			cnt = 0;
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
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

	private void getMatchingHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "doesnotexist" };
			logger.log(Logger.Level.INFO, "List of Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non-existent header");
			iterator = mimeHeaders.getMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getMatchingHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching headers");
			iterator = mimeHeaders.getMatchingHeaders(sArray);
			cnt = 0;
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
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

	private void getNonMatchingHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of Non Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching headers");
			iterator = mimeHeaders.getNonMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getNonMatchingHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of Non Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching headers");
			iterator = mimeHeaders.getNonMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equals("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getNonMatchingHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of Non Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching headers");
			iterator = mimeHeaders.getNonMatchingHeaders(sArray);
			cnt = 0;
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
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

	private void getNonMatchingHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of Non Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching headers");
			iterator = mimeHeaders.getNonMatchingHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
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

	private void getNonMatchingHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all headers");
			iterator = mimeHeaders.getAllHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of Non Matching headers contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching headers");
			iterator = mimeHeaders.getNonMatchingHeaders(sArray);
			cnt = 0;
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
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equals("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
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

	private void removeAllHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders object ...");
			mimeHeaders.removeAllHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders object ...");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void removeAllHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders object ...");
			mimeHeaders.removeAllHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders object ...");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void removeAllHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders object ...");
			mimeHeaders.removeAllHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders object ...");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void removeAllHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders object ...");
			mimeHeaders.removeAllHeaders();

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

	private void removeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing header");
			mimeHeaders.removeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting headers");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void removeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing header");
			mimeHeaders.removeHeader("Content-Id");

			logger.log(Logger.Level.INFO, "Getting headers");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (!(name.equals("Content-Description") && value.equals("some text"))) {
					logger.log(Logger.Level.ERROR, "Mimeheader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MimeHeader did match");
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + cnt);
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

	private void removeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing header");
			mimeHeaders.removeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting headers");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void removeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Removing header");
			mimeHeaders.removeHeader("doesnotexist");

			logger.log(Logger.Level.INFO, "Getting header");
			Iterator iterator = mimeHeaders.getAllHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received invalid Mimeheader");
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

	private void setHeader1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Setting header");
			mimeHeaders.setHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting header");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from setHeader(Content-Description)");
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

	private void setHeader2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");
			mimeHeaders.addHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Setting header");
			mimeHeaders.setHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting header");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from setHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Getting header");
			sArray = mimeHeaders.getHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(name2), got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Id)");
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

	private void setHeader3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.setHeader("Content-Description", "some text");
			mimeHeaders.setHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Setting header");
			mimeHeaders.setHeader("Content-Description", "image/jpeg");

			logger.log(Logger.Level.INFO, "Getting header");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("image/jpeg")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
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

	private void setHeader4Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Setting header");
			mimeHeaders.setHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting header");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
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

	private void setHeader5Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Adding header");
			mimeHeaders.addHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Setting header");
			mimeHeaders.setHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Setting header again");
			mimeHeaders.setHeader("Content-Description", "impage/jpeg");

			logger.log(Logger.Level.INFO, "Getting header");
			String sArray[] = mimeHeaders.getHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("impage/jpeg")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getHeader(Content-Description)");
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

	private void setHeader6Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setHeader6Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Setting header with null value");
			mimeHeaders.setHeader("Content-Description", null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeaders object ...");
			mimeHeaders = new MimeHeaders();

			logger.log(Logger.Level.INFO, "Setting header with null string value");
			mimeHeaders.setHeader("Content-Description", "");
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
