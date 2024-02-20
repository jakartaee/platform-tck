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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPHeaderElement;

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
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPHeaderElementTestServlet extends HttpServlet {
	private static final Logger logger = (Logger) System.getLogger(SOAPHeaderElementTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

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

		logger.log(Logger.Level.INFO, "Creating SOAPHeaderElement");
		she = hdr.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));

	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("setRelaySOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting setRelaySOAP11Test");
			setRelaySOAP11Test(req, res);
		} else if (testname.equals("setRelaySOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting setRelaySOAP12Test");
			setRelaySOAP12Test(req, res);
		} else if (testname.equals("getRelaySOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting getRelaySOAP11Test");
			getRelaySOAP11Test(req, res);
		} else if (testname.equals("getRelaySOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting getRelaySOAP12Test");
			getRelaySOAP12Test(req, res);
		} else if (testname.equals("getActorTest")) {
			logger.log(Logger.Level.INFO, "Starting getActorTest");
			getActorTest(req, res);
		} else if (testname.equals("getRoleSOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting getRoleSOAP11Test");
			getRoleSOAP11Test(req, res);
		} else if (testname.equals("getRoleSOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting getRoleSOAP12Test");
			getRoleSOAP12Test(req, res);
		} else if (testname.equals("getMustUnderstandTrueTest")) {
			logger.log(Logger.Level.INFO, "Starting getMustUnderstandTrueTest");
			getMustUnderstandTrueTest(req, res);
		} else if (testname.equals("getMustUnderstandFalseTest")) {
			logger.log(Logger.Level.INFO, "Starting getMustUnderstandFalseTest");
			getMustUnderstandFalseTest(req, res);
		} else if (testname.equals("setActorTest")) {
			logger.log(Logger.Level.INFO, "Starting setActorTest");
			setActorTest(req, res);
		} else if (testname.equals("setRoleSOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting setRoleSOAP11Test");
			setRoleSOAP11Test(req, res);
		} else if (testname.equals("setRoleSOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting setRoleSOAP12Test");
			setRoleSOAP12Test(req, res);
		} else if (testname.equals("setMustUnderstandTrueTest")) {
			logger.log(Logger.Level.INFO, "Starting setMustUnderstandTrueTest");
			setMustUnderstandTrueTest(req, res);
		} else if (testname.equals("setMustUnderstandFalseTest")) {
			logger.log(Logger.Level.INFO, "Starting setMustUnderstandFalseTest");
			setMustUnderstandFalseTest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"SOAPHeaderElementTestServlet:init (Entering)");
		logger.log(Logger.Level.TRACE,"SOAPHeaderElementTestServlet:init (Leaving)");
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

	private void setRelaySOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRelaySOAP11Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling setRelay() should throw UnsupportedOperationException");
			she.setRelay(true);
			logger.log(Logger.Level.ERROR, "Did not throw UnsupportedOperationException");
			pass = false;
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Did throw UnsupportedOperationException");
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

	private void setRelaySOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRelaySOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set relay attribute for this SOAPHeaderElement to true");
			she.setRelay(true);

			logger.log(Logger.Level.INFO, "Set relay attribute for this SOAPHeaderElement to false");
			she.setRelay(false);
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

	private void getRelaySOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getRelaySOAP11Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getRelay() should throw UnsupportedOperationException");
			boolean relay = she.getRelay();
			logger.log(Logger.Level.ERROR, "Did not throw UnsupportedOperationException");
			pass = false;
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Did throw UnsupportedOperationException");
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

	private void getRelaySOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getRelaySOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set relay attribute for this SOAPHeaderElement to true");
			she.setRelay(true);

			logger.log(Logger.Level.INFO, "Get relay attribute for this SOAPHeaderElement");
			boolean relay = she.getRelay();
			logger.log(Logger.Level.INFO, "Verify that relay attribute is true");
			if (relay) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement relay attribute is true (expected)");
			} else {
				TestUtil.logErr("SOAPHeaderElement relay attribute is false (unexpected)");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Set relay attribute for this SOAPHeaderElement to false");
			she.setRelay(false);

			logger.log(Logger.Level.INFO, "Get relay attribute for this SOAPHeaderElement");
			relay = she.getRelay();
			logger.log(Logger.Level.INFO, "Verify that relay attribute is false");
			if (!relay) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement relay attribute is false (expected)");
			} else {
				TestUtil.logErr("SOAPHeaderElement relay attribute is true (unexpected)");
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

	private void getActorTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getActorTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the actor associated with SOAPHeaderElement");
			she.setActor("actor-URI");

			logger.log(Logger.Level.INFO, "Validating actor associated with SOAPHeaderElement");
			String actor = she.getActor();
			if (actor.equals("actor-URI")) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement actor setting is actor-URI");
			} else {
				logger.log(Logger.Level.ERROR,
						"SOAPHeaderElement actor setting: expected " + "actor-URI" + ", received " + actor);
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

	private void getRoleSOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getRoleSOAP11Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getRole() should throw UnsupportedOperationException");
			String role = she.getRole();
			logger.log(Logger.Level.ERROR, "Did not throw UnsupportedOperationException");
			pass = false;
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Did throw UnsupportedOperationException");
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

	private void getRoleSOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getRoleSOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the role associated with SOAPHeaderElement");
			she.setRole("role-URI");

			logger.log(Logger.Level.INFO, "Validating role associated with SOAPHeaderElement");
			String role = she.getRole();
			if (role.equals("role-URI")) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement role setting is role-URI");
			} else {
				logger.log(Logger.Level.ERROR,
						"SOAPHeaderElement role setting: expected " + "role-URI" + ", received " + role);
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

	private void getMustUnderstandTrueTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMustUnderstandTrueTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the SOAPHeaderElement mustunderstand attribute to true");
			she.setMustUnderstand(true);

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement mustunderstand attribute setting to true ...");
			if (she.getMustUnderstand()) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement mustunderstand attribute setting is true");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement mustunderstand attribute is false");
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

	private void getMustUnderstandFalseTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMustUnderstandFalseTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the SOAPHeaderElement mustunderstand attribute to false");
			she.setMustUnderstand(false);

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement mustunderstand attribute setting to false ...");
			if (!she.getMustUnderstand()) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement mustunderstand attribute setting is false");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement mustunderstand attribute is true");
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

	private void setActorTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setActorTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the actor associated with SOAPHeaderElement");
			she.setActor("actor-URI");

			logger.log(Logger.Level.INFO, "Validating actor associated with SOAPHeaderElement");
			String actor = she.getActor();
			if (actor.equals("actor-URI")) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement actor setting is actor-URI");
			} else {
				logger.log(Logger.Level.ERROR,
						"SOAPHeaderElement actor setting: expected " + "actor-URI" + ", received " + actor);
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

	private void setRoleSOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRoleSOAP11Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling setRole() should throw UnsupportedOperationException");
			she.setRole("role-URI");
			logger.log(Logger.Level.ERROR, "Did not throw UnsupportedOperationException");
			pass = false;
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Did throw UnsupportedOperationException");
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

	private void setRoleSOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRoleSOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the role associated with SOAPHeaderElement");
			she.setRole("role-URI");

			logger.log(Logger.Level.INFO, "Validating role associated with SOAPHeaderElement");
			String role = she.getRole();
			if (role.equals("role-URI")) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement role setting is role-URI");
			} else {
				logger.log(Logger.Level.ERROR,
						"SOAPHeaderElement role setting: expected " + "role-URI" + ", received " + role);
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

	private void setMustUnderstandTrueTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMustUnderstandTrueTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the SOAPHeaderElement mustunderstand attribute to true");
			she.setMustUnderstand(true);

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement mustunderstand attribute setting to true ...");
			if (she.getMustUnderstand()) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement mustunderstand attribute setting is true");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement mustunderstand attribute is false");
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

	private void setMustUnderstandFalseTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMustUnderstandFalseTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Set the SOAPHeaderElement mustunderstand attribute to false");
			she.setMustUnderstand(false);

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement mustunderstand attribute setting to false ...");
			if (!she.getMustUnderstand()) {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement mustunderstand attribute setting is false");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement mustunderstand attribute is true");
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
}
