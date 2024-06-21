/*
 * Copyright (c) 2007, 2022 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPEnvelope;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPEnvelopeTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPEnvelopeTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPElement se = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.INFO, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();

		// Message creation takes care of creating the SOAPPart - a
		// required part of the message as per the SOAP 1.1 spec.
		logger.log(Logger.Level.INFO, "Get SOAP Part");
		sp = msg.getSOAPPart();

		// Retrieve the envelope from the soap part to start building
		// the soap message.
		logger.log(Logger.Level.INFO, "Get SOAP Envelope");
		envelope = sp.getEnvelope();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Header");
		hdr = envelope.getHeader();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addBodyTest")) {
			logger.log(Logger.Level.INFO, "Starting addBodyTest");
			addBodyTest(req, res);
		} else if (testname.equals("getBodyTest")) {
			logger.log(Logger.Level.INFO, "Starting getBodyTest");
			getBodyTest(req, res);
		} else if (testname.equals("addHeaderTest")) {
			logger.log(Logger.Level.INFO, "Starting addHeaderTest");
			addHeaderTest(req, res);
		} else if (testname.equals("getHeaderTest")) {
			logger.log(Logger.Level.INFO, "Starting getHeaderTest");
			getHeaderTest(req, res);
		} else if (testname.equals("createNameTest1")) {
			logger.log(Logger.Level.INFO, "Starting createNameTest1");
			createNameTest1(req, res);
		} else if (testname.equals("createNameTest2")) {
			logger.log(Logger.Level.INFO, "Starting createNameTest2");
			createNameTest2(req, res);
		} else if (testname.equals("createNameTest3")) {
			logger.log(Logger.Level.INFO, "Starting createNameTest3");
			createNameTest3(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"SOAPEnvelopeTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		logger.log(Logger.Level.TRACE,"SOAPEnvelopeTestServlet:init (Leaving)");
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

	private void addBodyTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addBodyTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			try {
				logger.log(Logger.Level.INFO,
						"Add SOAPBody to envelope that already" + " has a SOAPBody (expect SOAPException)");
				SOAPBody mybdy = envelope.addBody();
				logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Got expected SOAPException");
			}
			logger.log(Logger.Level.INFO, "Detach SOAPBody from this envelope");
			envelope.getBody().detachNode();
			logger.log(Logger.Level.INFO, "Add a SOAPBody to this envelope");
			SOAPBody mybdy = envelope.addBody();
			if (mybdy == null) {
				logger.log(Logger.Level.ERROR, "SOAPBody return value is null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "addBodyTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "addBodyTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getBodyTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getBodyTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			SOAPBody mybdy = null;
			SOAPBody mybdy2 = null;

			setup();
			logger.log(Logger.Level.INFO, "Detach SOAPBody from this envelope");
			envelope.getBody().detachNode();
			logger.log(Logger.Level.INFO,
					"Get SOAPBody from envelope that has no" + " SOAPBody (expect return of null)");
			mybdy = envelope.getBody();
			if (mybdy != null) {
				logger.log(Logger.Level.ERROR, "Did not get return of null");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Add a SOAPBody to this envelope");
			mybdy = envelope.addBody();
			logger.log(Logger.Level.INFO, "Get the SOAPBody of this envelope");
			mybdy2 = envelope.getBody();
			if (mybdy2 == null) {
				logger.log(Logger.Level.ERROR, "SOAPBody return value is null");
				pass = false;
			} else if (!mybdy2.equals(mybdy)) {
				logger.log(Logger.Level.ERROR, "SOAPBody mybdy2 not equal to SOAPBody mybdy");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "getBodyTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "getBodyTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addHeaderTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addHeaderTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			try {
				logger.log(Logger.Level.INFO,
						"Add SOAPHeader to envelope that already" + " has a SOAPHeader (expect SOAPException)");
				SOAPHeader myhdr = envelope.addHeader();
				logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Got expected SOAPException");
			}
			logger.log(Logger.Level.INFO, "Detach SOAPHeader from this envelope");
			envelope.getHeader().detachNode();
			logger.log(Logger.Level.INFO, "Add a SOAPHeader to this envelope");
			SOAPHeader myhdr = envelope.addHeader();
			if (myhdr == null) {
				logger.log(Logger.Level.ERROR, "SOAPHeader return value is null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "addHeaderTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "addHeaderTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getHeaderTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getHeaderTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			SOAPHeader myhdr = null;
			SOAPHeader myhdr2 = null;

			setup();
			logger.log(Logger.Level.INFO, "Detach SOAPHeader from this envelope");
			envelope.getHeader().detachNode();
			logger.log(Logger.Level.INFO,
					"Get SOAPHeader from envelope that has no" + " SOAPHeader (expect return of null)");
			myhdr = envelope.getHeader();
			if (myhdr != null) {
				logger.log(Logger.Level.ERROR, "Did not get expected return of null");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Add a SOAPHeader to this envelope");
			myhdr = envelope.addHeader();
			logger.log(Logger.Level.INFO, "Get the SOAPHeader of this envelope");
			myhdr2 = envelope.getHeader();
			if (myhdr2 == null) {
				logger.log(Logger.Level.ERROR, "SOAPHeader return value is null");
				pass = false;
			} else if (!myhdr2.equals(myhdr)) {
				logger.log(Logger.Level.ERROR, "SOAPHeader myhdr2 not equal to SOAPHeader myhdr");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "getHeaderTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "getHeaderTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createNameTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createNameTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create name element localName=MyName1");
			Name name = envelope.createName("MyName1");
			if (name == null) {
				logger.log(Logger.Level.ERROR, "createName() returned null");
				pass = false;
			} else {
				String localName = name.getLocalName();
				String prefix = name.getPrefix();
				String uri = name.getURI();
				logger.log(Logger.Level.INFO, "localName=" + localName);
				logger.log(Logger.Level.INFO, "prefix=" + prefix);
				logger.log(Logger.Level.INFO, "uri=" + uri);
				if (localName == null) {
					logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
					pass = false;
				} else if (!localName.equals("MyName1")) {
					logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
					pass = false;
				} else if (prefix != null && !prefix.equals("")) {
					logger.log(Logger.Level.ERROR, "prefix is wrong (expected null or null string)");
					pass = false;
				} else if (uri != null && !uri.equals("")) {
					logger.log(Logger.Level.ERROR, "uri is wrong (expected null or null string)");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createNameTest1() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createNameTest1() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createNameTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createNameTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create name element localName=MyName1, " + "prefix=MyPrefix1, uri=MyUri1");
			Name name = envelope.createName("MyName1", "MyPrefix1", "MyUri1");
			if (name == null) {
				logger.log(Logger.Level.ERROR, "createName() returned null");
				pass = false;
			} else {
				String localName = name.getLocalName();
				String prefix = name.getPrefix();
				String uri = name.getURI();
				logger.log(Logger.Level.INFO, "localName=" + localName);
				logger.log(Logger.Level.INFO, "prefix=" + prefix);
				logger.log(Logger.Level.INFO, "uri=" + uri);
				if (localName == null) {
					logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
					pass = false;
				} else if (!localName.equals("MyName1")) {
					logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
					pass = false;
				} else if (prefix == null) {
					logger.log(Logger.Level.ERROR, "prefix is null (expected MyPrefix1)");
					pass = false;
				} else if (!prefix.equals("MyPrefix1")) {
					logger.log(Logger.Level.ERROR, "prefix is wrong (expected MyPrefix1)");
					pass = false;
				} else if (uri == null) {
					logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
					pass = false;
				} else if (!uri.equals("MyUri1")) {
					logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createNameTest2() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createNameTest2() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createNameTest3(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createNameTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create name element localName=MyName1, " + "uri=MyUri1");
			Name name = envelope.createName("MyName1", "MyUri1");
			if (name == null) {
				logger.log(Logger.Level.ERROR, "createName() returned null");
				pass = false;
			} else {
				String localName = name.getLocalName();
				String uri = name.getURI();
				logger.log(Logger.Level.INFO, "localName=" + localName);
				logger.log(Logger.Level.INFO, "uri=" + uri);
				if (localName == null) {
					logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
					pass = false;
				} else if (!localName.equals("MyName1")) {
					logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
					pass = false;
				} else if (uri == null) {
					logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
					pass = false;
				} else if (!uri.equals("MyUri1")) {
					logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createNameTest3() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createNameTest3() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

}
