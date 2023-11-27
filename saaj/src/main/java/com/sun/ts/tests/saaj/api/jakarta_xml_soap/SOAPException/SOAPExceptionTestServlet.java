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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPException;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.SOAPException;

public class SOAPExceptionTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPExceptionTestServlet.class.getName());

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");

		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");

		if (testname.equals("SOAPExceptionConstructor1Test")) {
			logger.log(Logger.Level.INFO, "Starting SOAPExceptionConstructor1Test");
			SOAPExceptionConstructor1Test(req, res);
		} else if (testname.equals("SOAPExceptionConstructor2Test")) {
			logger.log(Logger.Level.INFO, "Starting SOAPExceptionConstructor2Test");
			SOAPExceptionConstructor2Test(req, res);
		} else if (testname.equals("SOAPExceptionConstructor3Test")) {
			logger.log(Logger.Level.INFO, "Starting SOAPExceptionConstructor3Test");
			SOAPExceptionConstructor3Test(req, res);
		} else if (testname.equals("SOAPExceptionConstructor4Test")) {
			logger.log(Logger.Level.INFO, "Starting SOAPExceptionConstructor4Test");
			SOAPExceptionConstructor4Test(req, res);
		} else if (testname.equals("InitGetCauseTest")) {
			logger.log(Logger.Level.INFO, "Starting InitGetCauseTest");
			InitGetCauseTest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");

		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println("SOAPExceptionTestServlet:init (Entering)");
		super.init(servletConfig);
		System.out.println("SOAPExceptionTestServlet:init (Leaving)");

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

	private void SOAPExceptionConstructor1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SOAPExceptionConstructor1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			throw new SOAPException();
		} catch (Exception e) {
			if (e instanceof SOAPException) {
				logger.log(Logger.Level.INFO, "SOAPExceptionConstructor1Test test PASSED");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor1Test test FAILED");
				logger.log(Logger.Level.ERROR, "Exception thrown was not of type SOAPException");
				pass = false;
			}
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SOAPExceptionConstructor2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SOAPExceptionConstructor2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			throw new SOAPException("foobar");
		} catch (Exception e) {
			if (e instanceof SOAPException) {
				String reason = e.getMessage();
				if (reason.equals("foobar"))
					logger.log(Logger.Level.INFO, "SOAPExceptionConstructor2Test test PASSED");
				else {
					logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor2Test test FAILED");
					logger.log(Logger.Level.ERROR, "reason: expected foobar, received " + reason);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor2Test test FAILED");
				logger.log(Logger.Level.ERROR, "Exception thrown was not of type SOAPException");
				pass = false;
			}
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SOAPExceptionConstructor3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SOAPExceptionConstructor3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		Exception foo = new Exception("foo");
		try {
			throw new SOAPException("foobar", foo);
		} catch (Exception e) {
			if (e instanceof SOAPException) {
				String reason = e.getMessage();
				if (reason.equals("foobar"))
					logger.log(Logger.Level.INFO, "SOAPExceptionConstructor3Test test PASSED");
				else {
					logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor3Test test FAILED");
					logger.log(Logger.Level.ERROR, "reason: expected foobar, received " + reason);
					pass = false;
				}
				/*
				 * Throwable t = ((SOAPException)e).getCause(); if (t.equals(foo))
				 * logger.log(Logger.Level.INFO,"SOAPExceptionConstructor3Test test PASSED");
				 * else {
				 * logger.log(Logger.Level.ERROR,"SOAPExceptionConstructor3Test test FAILED");
				 * logger.log(Logger.Level.ERROR,"Throwable objects do not match"); pass =
				 * false; }
				 */
			} else {
				logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor3Test test FAILED");
				logger.log(Logger.Level.ERROR, "Exception thrown was not of type SOAPException");
				pass = false;
			}
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SOAPExceptionConstructor4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SOAPExceptionConstructor4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		Exception foo = new Exception("foo");
		try {
			throw new SOAPException(foo);
		} catch (Exception e) {
			if (e instanceof SOAPException) {
				logger.log(Logger.Level.INFO, "SOAPExceptionConstructor4Test test PASSED");
				Throwable t = ((SOAPException) e).getCause();
				if (t.equals(foo))
					logger.log(Logger.Level.INFO, "SOAPExceptionConstructor4Test test PASSED");
				else {
					logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor4Test test FAILED");
					logger.log(Logger.Level.ERROR, "Throwable objects do not match");
				}
			} else {
				logger.log(Logger.Level.ERROR, "SOAPExceptionConstructor4Test test FAILED");
				logger.log(Logger.Level.ERROR, "Exception thrown was not of type SOAPException");
				pass = false;
			}
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void InitGetCauseTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "InitGetCauseTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		Exception foo = new Exception("foo");
		try {
			SOAPException soape = new SOAPException("foobar");
			soape.initCause(foo);
			throw soape;
		} catch (Exception e) {
			if (e instanceof SOAPException) {
				Throwable t = ((SOAPException) e).getCause();
				if (t.equals(foo))
					logger.log(Logger.Level.INFO, "InitGetCauseTest test PASSED");
				else {
					logger.log(Logger.Level.ERROR, "InitGetCauseTest test FAILED");
					logger.log(Logger.Level.ERROR, "Throwable objects do not match");
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "InitGetCauseTest test FAILED");
				logger.log(Logger.Level.ERROR, "Exception thrown was not of type SOAPException");
				pass = false;
			}
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}
}
