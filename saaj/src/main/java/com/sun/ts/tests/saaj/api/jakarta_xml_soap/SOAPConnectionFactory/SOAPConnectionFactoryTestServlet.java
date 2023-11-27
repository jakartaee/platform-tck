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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnectionFactory;

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
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;

public class SOAPConnectionFactoryTestServlet extends HttpServlet {
	private static final Logger logger = (Logger) System.getLogger(SOAPConnectionFactoryTestServlet.class.getName());

	private String hostname = "localhost";

	private int portnum = 8080;

	private String cntxroot = "/SOAPConnectionFactory_web";

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		SOAP_Util.setup();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("newInstanceTest")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest");
			newInstanceTest(req, res);
		} else if (testname.equals("createConnectionTest")) {
			logger.log(Logger.Level.INFO, "Starting createConnectionTest");
			createConnectionTest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("SOAPEnvelopeTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("SOAPEnvelopeTestServlet:init (Leaving)");
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

	private void newInstanceTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Check if SOAPConnectionFactory is supported
			logger.log(Logger.Level.INFO, "Check if SOAPConnectionFactory is supported");
			if (!SOAP_Util.isSOAPConnectionFactorySupported()) {
				logger.log(Logger.Level.INFO,
						"SOAPConnectionFactory.newInstance() is " + "unsupported (skipping test)");
				resultProps.setProperty("TESTRESULT", "pass");
				resultProps.list(out);
				return;
			}
			logger.log(Logger.Level.INFO, "Create SOAPConnectionFactory object");
			SOAPConnectionFactory sfactory = SOAP_Util.getSOAPConnectionFactory();
			if (sfactory == null) {
				logger.log(Logger.Level.ERROR, "SOAPConnectionFactory.newInstance() returned null");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "newInstanceTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "newInstanceTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createConnectionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createConnectionTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			// Check if SOAPConnectionFactory is supported
			logger.log(Logger.Level.INFO, "Check if SOAPConnectionFactory is supported");
			if (!SOAP_Util.isSOAPConnectionFactorySupported()) {
				logger.log(Logger.Level.INFO,
						"SOAPConnectionFactory.newInstance() is " + "unsupported (skipping test)");
				resultProps.setProperty("TESTRESULT", "pass");
				resultProps.list(out);
				return;
			}
			logger.log(Logger.Level.INFO, "Create SOAPConnectionFactory object");
			SOAPConnectionFactory sfactory = SOAP_Util.getSOAPConnectionFactory();
			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnection scon = sfactory.createConnection();
			if (scon == null) {
				logger.log(Logger.Level.ERROR, "SOAPConnection.createConnection() returned null");
				pass = false;
			} else
				scon.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createConnectionTest() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createConnectionTest() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}
}
