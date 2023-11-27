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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

public class MessageFactoryTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(MessageFactoryTestServlet.class.getName());

	private MessageFactory mf = null;

	private MessageFactory mf2 = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Get MessageFactory object.
		logger.log(Logger.Level.INFO, "Get MessageFactory object");
		mf = SOAP_Util.getMessageFactory();
		mf2 = MessageFactory.newInstance();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("createMessageTest1")) {
			logger.log(Logger.Level.INFO, "Starting createMessageTest1");
			createMessageTest1(req, res);
		} else if (testname.equals("createMessageTest2")) {
			logger.log(Logger.Level.INFO, "Starting createMessageTest2");
			createMessageTest2(req, res);
		} else if (testname.equals("newInstanceTest1")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest1");
			newInstanceTest1(req, res);
		} else if (testname.equals("newInstanceTest2")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest2");
			newInstanceTest2(req, res);
		} else if (testname.equals("newInstanceTest3")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest3");
			newInstanceTest3(req, res);
		} else if (testname.equals("newInstanceTest4")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest4");
			newInstanceTest4(req, res);
		} else if (testname.equals("newInstanceTest4b")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest4b");
			newInstanceTest4b(req, res);
		} else if (testname.equals("newInstanceTest5")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest5");
			newInstanceTest5(req, res);
		} else if (testname.equals("newInstanceTest6")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest6");
			newInstanceTest6(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("MessageFactoryTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("MessageFactoryTestServlet:init (Leaving)");
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

	private void createMessageTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createMessageTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Create SOAPMessage from MessageFactory object
			logger.log(Logger.Level.INFO, "Create SOAPMessage from MessageFactory object");
			SOAPMessage msg = mf.createMessage();
			if (msg == null) {
				logger.log(Logger.Level.ERROR, "createMessage() returned null");
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

	private void createMessageTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createMessageTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

			logger.log(Logger.Level.INFO, "Create SOAPMessage msg1 using createMessage()");
			SOAPMessage msg1 = mf.createMessage();
			msg1.writeTo(baos1);

			MimeHeaders headers = new MimeHeaders();
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11))
				headers.addHeader("Content-Type", "text/xml");
			else
				headers.addHeader("Content-Type", "application/soap+xml");

			// Create SOAPMessage from MessageFactory object using InputStream
			logger.log(Logger.Level.INFO, "Create SOAPMessage msg2 using SOAPMessage msg1" + " as the InputStream");
			logger.log(Logger.Level.INFO, "Create SOAPMessage msg2 using createMessage(" + "MimeHeaders, InputStream)");
			SOAPMessage msg2 = mf.createMessage(headers, new ByteArrayInputStream(baos1.toByteArray()));

			if (msg2 == null) {
				TestUtil.logErr("createMessage(MimeHeaders, InputStream) returned null");
				pass = false;
			} else {
				msg2.writeTo(baos2);
				logger.log(Logger.Level.INFO, "Compare msg1 and msg2 (should be equal)");
				if (!(baos1.toString().equals(baos2.toString()))) {
					logger.log(Logger.Level.INFO, "msg1 = " + baos1.toString());
					logger.log(Logger.Level.INFO, "msg2 = " + baos2.toString());
					logger.log(Logger.Level.ERROR, "msg1 and msg2 are not equal (they should be)");
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

	private void newInstanceTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a MessageFactory object for default implementation
			logger.log(Logger.Level.INFO, "Create MessageFactory object for default implementation");
			MessageFactory mf = MessageFactory.newInstance();
			if (mf == null) {
				logger.log(Logger.Level.ERROR, "MessageFactory.newInstance() returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPMessage from MessageFactory object");
				SOAPMessage msg = mf.createMessage();
				if (msg == null) {
					logger.log(Logger.Level.ERROR, "Could not create SOAPMessage (msg = null)");
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

	private void newInstanceTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a SOAP 1.1 MessageFactory object
			logger.log(Logger.Level.INFO, "Create SOAP1.1 MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			if (mf == null) {
				logger.log(Logger.Level.ERROR,
						"MessageFactory.newInstance(SOAPConstants." + "SOAP_1_1_PROTOCOL) returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPMessage from MessageFactory object");
				SOAPMessage msg = mf.createMessage();
				if (msg == null) {
					logger.log(Logger.Level.ERROR, "Could not create SOAPMessage (msg = null)");
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

	private void newInstanceTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a SOAP 1.2 MessageFactory object
			logger.log(Logger.Level.INFO, "Create SOAP1.2 MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			if (mf == null) {
				logger.log(Logger.Level.ERROR,
						"MessageFactory.newInstance(SOAPConstants." + "SOAP_1_2_PROTOCOL) returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPMessage from MessageFactory object");
				SOAPMessage msg = mf.createMessage();
				if (msg == null) {
					logger.log(Logger.Level.ERROR, "Could not create SOAPMessage (msg = null)");
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

	private void newInstanceTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a Dynamic MessageFactory object
			logger.log(Logger.Level.INFO, "Create Dynamic MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
			if (mf == null) {
				logger.log(Logger.Level.ERROR, "MessageFactory.newInstance(" + "DYNAMIC_SOAP_PROTOCOL) returned null");
				pass = false;
			} else {

				ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
				ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

				logger.log(Logger.Level.INFO, "Create SOAPMessage msg1 using createMessage()");
				SOAPMessage msg1 = mf2.createMessage();
				msg1.writeTo(baos1);

				MimeHeaders headers = new MimeHeaders();
				headers.addHeader("Content-Type", "text/xml");

				// Create SOAPMessage from MessageFactory object using InputStream
				logger.log(Logger.Level.INFO, "Create SOAPMessage msg2 using SOAPMessage msg1" + " as the InputStream");
				logger.log(Logger.Level.INFO,
						"Create SOAPMessage msg2 using createMessage(" + "MimeHeaders, InputStream)");
				SOAPMessage msg2 = mf.createMessage(headers, new ByteArrayInputStream(baos1.toByteArray()));
				if (msg2 == null) {
					logger.log(Logger.Level.ERROR, "Could not create SOAPMessage (msg = null)");
					pass = false;
				} else {
					msg2.writeTo(baos2);
					logger.log(Logger.Level.INFO, "Compare msg1 and msg2 (should be equal)");
					if (!(baos1.toString().equals(baos2.toString()))) {
						logger.log(Logger.Level.INFO, "msg1 = " + baos1.toString());
						logger.log(Logger.Level.INFO, "msg2 = " + baos2.toString());
						logger.log(Logger.Level.ERROR, "msg1 and msg2 are not equal (they should be)");
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

	private void newInstanceTest4b(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest4b");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a Dynamic MessageFactory object
			logger.log(Logger.Level.INFO, "Create Dynamic MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
			if (mf == null) {
				logger.log(Logger.Level.ERROR, "MessageFactory.newInstance(" + "DYNAMIC_SOAP_PROTOCOL) returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPMessage from Dynamic MessageFactory object");
				logger.log(Logger.Level.INFO,
						"Call MessageFactory.createMessage() and expect UnsupportedOperationException");
				mf.createMessage();
				logger.log(Logger.Level.ERROR, "Did not throw expected UnsupportedOperationException");
				pass = false;
			}
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
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

	private void newInstanceTest5(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest5");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a BOGUS MessageFactory object
			logger.log(Logger.Level.INFO, "Create BOGUS MessageFactory object");
			MessageFactory.newInstance("BOGUS");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Caught expected SOAPException");
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

	private void newInstanceTest6(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest6");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Create a Dynamic MessageFactory object
			logger.log(Logger.Level.INFO, "Create Dynamic MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.DEFAULT_SOAP_PROTOCOL);
			if (mf == null) {
				logger.log(Logger.Level.ERROR, "MessageFactory.newInstance(" + "DEFAULT_SOAP_PROTOCOL) returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPMessage from MessageFactory object");
				SOAPMessage msg = mf.createMessage();
				if (msg == null) {
					logger.log(Logger.Level.ERROR, "Could not create SOAPMessage (msg = null)");
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
