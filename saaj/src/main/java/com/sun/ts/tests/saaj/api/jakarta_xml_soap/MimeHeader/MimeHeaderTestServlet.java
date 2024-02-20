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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.MimeHeader;

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
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class MimeHeaderTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(MimeHeaderTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	MimeHeader mimeHeader = null;

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

		// Retrieve the envelope from the soap part to start building
		// the soap message.
		envelope = sp.getEnvelope();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("getNameTest")) {
			logger.log(Logger.Level.INFO, "Starting getNameTest");
			getNameTest(req, res);
		} else if (testname.equals("getValueTest")) {
			logger.log(Logger.Level.INFO, "Starting getValueTest");
			getValueTest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"MimeHeaderTestServlet:init (Entering)");
		logger.log(Logger.Level.TRACE,"MimeHeaderTestServlet:init (Leaving)");
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

	private void getNameTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNameTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeader object ...");
			mimeHeader = new MimeHeader("Content-Type", "application/xml");

			logger.log(Logger.Level.INFO, "Obtaining name");
			String name = mimeHeader.getName();

			logger.log(Logger.Level.INFO, "Validating name results ...");
			if (name == null) {
				logger.log(Logger.Level.ERROR, "name is null");
				pass = false;
			} else if (!name.equals("Content-Type")) {
				logger.log(Logger.Level.ERROR, "name mismatch - expected: Content-Type, received: " + name);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "name matches: " + name);
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

	private void getValueTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getValueTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating MimeHeader object ...");
			mimeHeader = new MimeHeader("Content-Type", "application/xml");

			logger.log(Logger.Level.INFO, "Obtaining value");
			String value = mimeHeader.getValue();

			logger.log(Logger.Level.INFO, "Validating value results ...");
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null");
				pass = false;
			} else if (!value.equals("application/xml")) {
				logger.log(Logger.Level.ERROR, "value mismatch - expected: application/xml, received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value matches: " + value);
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
