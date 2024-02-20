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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Name;

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
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class NameTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(NameTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	Name name = null;

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
		if (testname.equals("getLocalNameTest")) {
			logger.log(Logger.Level.INFO, "Starting getLocalNameTest");
			getLocalNameTest(req, res);
		} else if (testname.equals("getPrefixTest")) {
			logger.log(Logger.Level.INFO, "Starting getPrefixTest");
			getPrefixTest(req, res);
		} else if (testname.equals("getQualifiedNameTest")) {
			logger.log(Logger.Level.INFO, "Starting getQualifiedNameTest");
			getQualifiedNameTest(req, res);
		} else if (testname.equals("getURITest")) {
			logger.log(Logger.Level.INFO, "Starting getURITest");
			getURITest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");

		}

	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"GetLocalNameTestServlet:init (Entering)");
		logger.log(Logger.Level.TRACE,"GetLocalNameTestServlet:init (Leaving)");
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

	private void getLocalNameTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getLocalNameTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating Name object ...");
			name = envelope.createName("namespace", "ns", "namespace-uri");

			logger.log(Logger.Level.INFO, "Obtaining local name");
			String localName = name.getLocalName();

			logger.log(Logger.Level.INFO, "Validating local name results ...");
			if (localName == null) {
				logger.log(Logger.Level.ERROR, "localName is null");
				pass = false;
			} else if (!localName.equals("namespace")) {
				logger.log(Logger.Level.ERROR, "local name mismatch - expected: namespace, received: " + localName);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "local name matches: " + localName);
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

	private void getPrefixTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getPrefixTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating Name object ...");
			name = envelope.createName("namespace", "ns", "namespace-uri");

			logger.log(Logger.Level.INFO, "Obtaining prefix name");
			String prefix = name.getPrefix();

			logger.log(Logger.Level.INFO, "Validating prefix results ...");
			if (prefix == null) {
				logger.log(Logger.Level.ERROR, "prefix is null");
				pass = false;
			} else if (!prefix.equals("ns")) {
				logger.log(Logger.Level.ERROR, "prefix mismatch - expected: ns, received: " + prefix);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "prefix matches: " + prefix);
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

	private void getQualifiedNameTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getQualifiedNameTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating Name object ...");
			name = envelope.createName("namespace", "ns", "namespace-uri");

			logger.log(Logger.Level.INFO, "Obtaining qualified name");
			String qualifiedName = name.getQualifiedName();

			logger.log(Logger.Level.INFO, "Validating qualified name results ...");
			if (qualifiedName == null) {
				logger.log(Logger.Level.ERROR, "qualifiedName is null");
				pass = false;
			} else if (!qualifiedName.equals("ns:namespace")) {
				TestUtil.logErr("qualified name mismatch - expected: namespace, received: " + qualifiedName);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "qualified name matches: " + qualifiedName);
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

	private void getURITest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getURITest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating Name object ...");
			name = envelope.createName("namespace", "ns", "namespace-uri");

			logger.log(Logger.Level.INFO, "Obtaining URI name");
			String uri = name.getURI();

			logger.log(Logger.Level.INFO, "Validating URI results ...");
			if (uri == null) {
				logger.log(Logger.Level.ERROR, "uri is null");
				pass = false;
			} else if (!uri.equals("namespace-uri")) {
				TestUtil.logErr("uri mismatch - expected: namespace-uri, received: " + uri);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "uri matches: " + uri);
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
