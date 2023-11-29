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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFault;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.Detail;
import jakarta.xml.soap.DetailEntry;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPFaultTestServlet extends HttpServlet {
	private static final Logger logger = (Logger) System.getLogger(SOAPFaultTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPFactory sfactory = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPBody body = null;

	private SOAPFault sf = null;

	private String prefix = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.INFO, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();
		sfactory = SOAP_Util.getSOAPFactory();

		// Message creation takes care of creating the SOAPPart - a
		// required part of the message as per the SOAP 1.1 spec.
		logger.log(Logger.Level.INFO, "Get SOAP Part");
		sp = msg.getSOAPPart();

		// Retrieve the envelope from the soap part to start building
		// the soap message.
		logger.log(Logger.Level.INFO, "Get SOAP Envelope");
		envelope = sp.getEnvelope();
		prefix = envelope.getElementName().getPrefix();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();

		logger.log(Logger.Level.INFO, "Creating SOAPFault");
		sf = body.addFault();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("SetGetFaultString1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultString1Test");
			SetGetFaultString1Test(req, res);
		} else if (testname.equals("SetGetFaultCode1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultCode1Test");
			SetGetFaultCode1Test(req, res);
		} else if (testname.equals("SetGetFaultActor1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultActor1Test");
			SetGetFaultActor1Test(req, res);
		} else if (testname.equals("AddGetDetail1Test")) {
			logger.log(Logger.Level.INFO, "Starting AddGetDetail1Test");
			AddGetDetail1Test(req, res);
		} else if (testname.equals("addFaultReasonText1Test")) {
			logger.log(Logger.Level.INFO, "Starting addFaultReasonText1Test");
			addFaultReasonText1Test(req, res);
		} else if (testname.equals("addFaultReasonText2Test")) {
			logger.log(Logger.Level.INFO, "Starting addFaultReasonText2Test");
			addFaultReasonText2Test(req, res);
		} else if (testname.equals("addFaultReasonText3Test")) {
			logger.log(Logger.Level.INFO, "Starting addFaultReasonText3Test");
			addFaultReasonText3Test(req, res);
		} else if (testname.equals("addFaultReasonText4Test")) {
			logger.log(Logger.Level.INFO, "Starting addFaultReasonText4Test");
			addFaultReasonText4Test(req, res);
		} else if (testname.equals("getFaultReasonText1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonText1Test");
			getFaultReasonText1Test(req, res);
		} else if (testname.equals("getFaultReasonText2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonText2Test");
			getFaultReasonText2Test(req, res);
		} else if (testname.equals("getFaultReasonText3Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonText3Test");
			getFaultReasonText3Test(req, res);
		} else if (testname.equals("getFaultReasonTexts1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonTexts1Test");
			getFaultReasonTexts1Test(req, res);
		} else if (testname.equals("getFaultReasonTexts2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonTexts2Test");
			getFaultReasonTexts2Test(req, res);
		} else if (testname.equals("getFaultReasonTexts3Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonTexts3Test");
			getFaultReasonTexts3Test(req, res);
		} else if (testname.equals("setFaultNode1Test")) {
			logger.log(Logger.Level.INFO, "Starting setFaultNode1Test");
			setFaultNode1Test(req, res);
		} else if (testname.equals("SetGetFaultNode1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultNode1Test");
			SetGetFaultNode1Test(req, res);
		} else if (testname.equals("SetGetFaultNode2Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultNode2Test");
			SetGetFaultNode2Test(req, res);
		} else if (testname.equals("getFaultNode1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultNode1Test");
			getFaultNode1Test(req, res);
		} else if (testname.equals("getFaultNode2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultNode2Test");
			getFaultNode2Test(req, res);
		} else if (testname.equals("setFaultRole1Test")) {
			logger.log(Logger.Level.INFO, "Starting setFaultRole1Test");
			setFaultRole1Test(req, res);
		} else if (testname.equals("SetGetFaultRole1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultRole1Test");
			SetGetFaultRole1Test(req, res);
		} else if (testname.equals("getFaultRole1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultRole1Test");
			getFaultRole1Test(req, res);
		} else if (testname.equals("getFaultRole2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultRole2Test");
			getFaultRole2Test(req, res);
		} else if (testname.equals("SetGetFaultStringLocale1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultStringLocale1Test");
			SetGetFaultStringLocale1Test(req, res);
		} else if (testname.equals("setFaultStringLocale1Test")) {
			logger.log(Logger.Level.INFO, "Starting setFaultStringLocale1Test");
			setFaultStringLocale1Test(req, res);
		} else if (testname.equals("getFaultStringLocale1SOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultStringLocale1SOAP11Test");
			getFaultStringLocale1SOAP11Test(req, res);
		} else if (testname.equals("getFaultStringLocale1SOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultStringLocale1SOAP12Test");
			getFaultStringLocale1SOAP12Test(req, res);
		} else if (testname.equals("SetGetFaultCodeAsName1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultCodeAsName1Test");
			SetGetFaultCodeAsName1Test(req, res);
		} else if (testname.equals("SetGetFaultCodeAsQName1Test")) {
			logger.log(Logger.Level.INFO, "Starting SetGetFaultCodeAsQName1Test");
			SetGetFaultCodeAsQName1Test(req, res);
		} else if (testname.equals("getFaultReasonLocales1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonLocales1Test");
			getFaultReasonLocales1Test(req, res);
		} else if (testname.equals("getFaultReasonLocales2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultReasonLocales2Test");
			getFaultReasonLocales2Test(req, res);
		} else if (testname.equals("appendFaultSubcode1Test")) {
			logger.log(Logger.Level.INFO, "Starting appendFaultSubcode1Test");
			appendFaultSubcode1Test(req, res);
		} else if (testname.equals("appendFaultSubcode2Test")) {
			logger.log(Logger.Level.INFO, "Starting appendFaultSubcode2Test");
			appendFaultSubcode2Test(req, res);
		} else if (testname.equals("getFaultSubcodes1Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultSubcodes1Test");
			getFaultSubcodes1Test(req, res);
		} else if (testname.equals("getFaultSubcodes2Test")) {
			logger.log(Logger.Level.INFO, "Starting getFaultSubcodes2Test");
			getFaultSubcodes2Test(req, res);
		} else if (testname.equals("hasDetail1Test")) {
			logger.log(Logger.Level.INFO, "Starting hasDetail1Test");
			hasDetail1Test(req, res);
		} else if (testname.equals("hasDetail2Test")) {
			logger.log(Logger.Level.INFO, "Starting hasDetail2Test");
			hasDetail2Test(req, res);
		} else if (testname.equals("removeAllFaultSubcodes1Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllFaultSubcodes1Test");
			removeAllFaultSubcodes1Test(req, res);
		} else if (testname.equals("removeAllFaultSubcodes2Test")) {
			logger.log(Logger.Level.INFO, "Starting removeAllFaultSubcodes2Test");
			removeAllFaultSubcodes2Test(req, res);
		} else if (testname.equals("SetFaultCodeNameSOAPExceptionTest")) {
			logger.log(Logger.Level.INFO, "Starting SetFaultCodeNameSOAPExceptionTest");
			SetFaultCodeNameSOAPExceptionTest(req, res);
		} else if (testname.equals("SetFaultCodeQNameSOAPExceptionTest")) {
			logger.log(Logger.Level.INFO, "Starting SetFaultCodeQNameSOAPExceptionTest");
			SetFaultCodeQNameSOAPExceptionTest(req, res);
		} else if (testname.equals("SetFaultCodeStringSOAPExceptionTest")) {
			logger.log(Logger.Level.INFO, "Starting SetFaultCodeStringSOAPExceptionTest");
			SetFaultCodeStringSOAPExceptionTest(req, res);
		} else if (testname.equals("AppendFaultSubcodeSOAPExceptionTest")) {
			logger.log(Logger.Level.INFO, "Starting AppendFaultSubcodeSOAPExceptionTest");
			AppendFaultSubcodeSOAPExceptionTest(req, res);
		} else if (testname.equals("AddDetailSOAPExceptionTest")) {
			logger.log(Logger.Level.INFO, "Starting AddDetailSOAPExceptionTest");
			AddDetailSOAPExceptionTest(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"SOAPFaultTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		logger.log(Logger.Level.TRACE,"SOAPFaultTestServlet:init (Leaving)");
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

	private void SetGetFaultString1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultString1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected = "this is the fault string";
			logger.log(Logger.Level.INFO, "Setting fault string");
			sf.setFaultString(expected);

			logger.log(Logger.Level.INFO, "Getting fault string");
			String result = sf.getFaultString();
			if (result != null) {
				if (!result.equals(expected)) {
					TestUtil.logErr("setFaultString()/getFaultString() behaved incorrectly");
					logger.log(Logger.Level.ERROR, "expected=" + expected);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "result=" + result);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getFaultString() returned a null result");
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

	private void SetGetFaultCode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultCode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				String expected = prefix + ":Server";
				logger.log(Logger.Level.INFO, "Setting fault code to:" + expected);
				sf.setFaultCode(expected);
				logger.log(Logger.Level.INFO, "Getting fault code");
				String result = sf.getFaultCode();
				if (result != null) {
					if (!result.equals(expected)) {
						TestUtil.logErr("setFaultCode()/getFaultCode() behaved incorrectly");
						pass = false;
					}
					logger.log(Logger.Level.INFO, "result=" + result);
				} else {
					logger.log(Logger.Level.ERROR, "getFaultCode() returned a null");
					pass = false;
				}
			} else {
				QName qname = SOAPConstants.SOAP_SENDER_FAULT;
				String expected = prefix + ":" + qname.getLocalPart();
				logger.log(Logger.Level.INFO, "Setting fault code to:" + expected);
				sf.setFaultCode(expected);
				logger.log(Logger.Level.INFO, "Getting fault code");
				String result = sf.getFaultCode();
				if (result != null) {
					if (!result.equals(expected)) {
						TestUtil.logErr("setFaultCode()/getFaultCode() behaved incorrectly");
						pass = false;
					}
					logger.log(Logger.Level.INFO, "result=" + result);
				} else {
					logger.log(Logger.Level.ERROR, "getFaultCode() returned a null");
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

	private void SetGetFaultActor1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultActor1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected = "http://www.my.org/faultActorURI";
			logger.log(Logger.Level.INFO, "Calling setFaultActor()");
			sf.setFaultActor(expected);
			logger.log(Logger.Level.INFO, "Calling getFaultActor()");
			String result = sf.getFaultActor();
			if (result != null) {
				if (!result.equals(expected)) {
					TestUtil.logErr("setFaultActor()/getFaultActor() behaved incorrectly");
					logger.log(Logger.Level.ERROR, "setFaultActor=" + expected + ", getFaultActor=" + result);
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "setFaultActor()/getFaultActor() behaved correctly");
			} else {
				logger.log(Logger.Level.ERROR, "getFaultActor() returned a null result, eventhough an actor was set");
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

	private void getFaultCode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultCode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting fault code");
			sf.setFaultCode(prefix + ":Server");

			logger.log(Logger.Level.INFO, "Getting fault code");
			String result = sf.getFaultCode();
			if (result != null) {
				if (!result.equals(prefix + ":Server")) {
					logger.log(Logger.Level.ERROR, "Error: getFaultCode() returned an incorrect result");
					logger.log(Logger.Level.ERROR, "result=" + result);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "result=" + result);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getFaultCode() returned a null");
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

	private void AddGetDetail1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "AddGetDetail1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			Detail d1 = null;
			d1 = sf.addDetail();
			Name name = envelope.createName("GetLastTradePrice", "WOMBAT", "http://www.wombat.org/trader");
			d1.addDetailEntry(name);

			Detail d2 = sf.getDetail();
			if (d2 == null) {
				logger.log(Logger.Level.ERROR, "getDetail() returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getDetail() returned a non-null detail");
				Iterator i = d2.getDetailEntries();
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getAllAttributes();
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					boolean foundName1 = false;
					while (i.hasNext()) {
						DetailEntry de = (DetailEntry) i.next();
						logger.log(Logger.Level.INFO, "Got DetailEntry = " + de.toString());
						String s = de.getValue();
						if (s.equals("GetLastTradePrice"))
							foundName1 = true;
						else {
							logger.log(Logger.Level.ERROR, "Bad DetailEntry has name of " + s);
							pass = false;
						}
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

	private void addFaultReasonText1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultReasonText1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling addFaultReasonText() must" + " throw UnsupportedOperationException");
			sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
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

	private void addFaultReasonText2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultReasonText2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected1 = "Its my fault";
			boolean found1 = false;

			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected1, Locale.ENGLISH);

			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
						} else {
							TestUtil.logErr("Received a duplicate Reason text:'" + actual + "'");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						logger.log(Logger.Level.ERROR, "expected= '" + expected1 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 1) {
				logger.log(Logger.Level.ERROR, "More than one reason text was returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Reason text was not received: '" + expected1 + "'");
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

	private void addFaultReasonText3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultReasonText3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected1 = "Its my fault";
			String expected2 = "Its my fault again";
			boolean found1 = false;
			boolean found2 = false;
			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected1, Locale.UK);
			logger.log(Logger.Level.INFO, "Adding another FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected2, Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
						} else {
							TestUtil.logErr("Received a duplicate Reason text:'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected2)) {
						if (!found2) {
							found2 = true;
							logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
						} else {
							TestUtil.logErr("Received a duplicate Reason text:'" + actual + "'");
							pass = false;
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						TestUtil.logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 2) {
				logger.log(Logger.Level.ERROR, "More than two reason texts were returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Reason text was not received: '" + expected1 + "'");
				pass = false;
			}
			if (!found2) {
				logger.log(Logger.Level.ERROR, "The following Reason text was not received: '" + expected2 + "'");
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

	private void addFaultReasonText4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultReasonText4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected = "Its my fault again";
			boolean found = false;
			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Adding another FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected, Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if (actual.equals(expected)) {
						if (!found) {
							found = true;
							logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
						} else {
							TestUtil.logErr("Received a duplicate Reason text:'" + actual + "'");
							pass = false;
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						logger.log(Logger.Level.ERROR, "expected= '" + expected + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 1) {
				logger.log(Logger.Level.ERROR, "More than one reason text was returned");
				pass = false;
			}
			if (!found) {
				logger.log(Logger.Level.ERROR, "The following Reason text was not received: '" + expected + "'");
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

	private void getFaultReasonLocales1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonLocales1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO,
					"Calling getFaultReasonLocales() must" + " throw UnsupportedOperationException");
			sf.getFaultReasonLocales();
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

	private void getFaultReasonLocales2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonLocales2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			Locale expected1 = Locale.ENGLISH;
			Locale expected2 = Locale.UK;
			Locale expected3 = Locale.GERMAN;
			boolean found1 = false;
			boolean found2 = false;
			boolean found3 = false;

			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText("Its my fault1", expected1);
			sf.addFaultReasonText("Its my fault2", expected2);
			sf.addFaultReasonText("Its my fault3", expected3);
			logger.log(Logger.Level.INFO, "Getting FaultReasonLocales from SOAPFault");
			Iterator i = sf.getFaultReasonLocales();
			logger.log(Logger.Level.INFO, "Locale iterator count=" + SOAP_Util.getIteratorCount(i));
			i = sf.getFaultReasonLocales();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof Locale) {
					Locale actual = (Locale) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Locale= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Locale:'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected2)) {
						if (!found2) {
							found2 = true;
							logger.log(Logger.Level.INFO, "Locale '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Locale:'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected3)) {
						if (!found3) {
							found3 = true;
							logger.log(Logger.Level.INFO, "Locale '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Locale:'" + actual + "'");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						logger.log(Logger.Level.ERROR,
								"expected= '" + expected1 + "' or '" + expected2 + "' or '" + expected3 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of Locale was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 3) {
				logger.log(Logger.Level.ERROR, "More than 3 Locales were returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Locale was not received: '" + expected1 + "'");
				pass = false;
			}
			if (!found2) {
				logger.log(Logger.Level.ERROR, "The following Locale was not received: '" + expected2 + "'");
				pass = false;
			}
			if (!found3) {
				logger.log(Logger.Level.ERROR, "The following Locale was not received: '" + expected3 + "'");
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

	private void getFaultReasonText1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonText1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO,
					"Calling getFaultReasonText(Locale) must" + " throw UnsupportedOperationException");

			String result = sf.getFaultReasonText(Locale.ENGLISH);
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

	private void getFaultReasonText2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonText2Test");
		Properties resultProps = new Properties();
		boolean pass = true;
		String expected = "Its my fault1";

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText("Its my fault", Locale.ENGLISH);
			sf.addFaultReasonText("Its my fault2", Locale.ENGLISH);
			sf.addFaultReasonText(expected, Locale.UK);
			logger.log(Logger.Level.INFO, "Getting FaultReasonText from SOAPFault");
			String actual = sf.getFaultReasonText(Locale.UK);
			if (actual != null) {
				if (actual.equals(expected)) {
					logger.log(Logger.Level.INFO, "Reason = " + actual);
				} else {
					logger.log(Logger.Level.ERROR, "An incorrect result was returned:");
					logger.log(Logger.Level.ERROR, "expected :" + expected);
					logger.log(Logger.Level.ERROR, "actual :" + actual);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Null result was returned(unexpected)");
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

	private void getFaultReasonText3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonText3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();
		String expected = "Its my fault2";

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding a FaultReasonText to SOAPFault");
			sf.addFaultReasonText("Its my fault1", Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Adding another FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected, Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "All FaultReasonTexts were added to SOAPFault");
			String actual = sf.getFaultReasonText(Locale.ENGLISH);
			if (actual != null) {
				if (actual.equals(expected)) {
					logger.log(Logger.Level.INFO, "Reason = " + actual);
				} else {
					logger.log(Logger.Level.ERROR, "An incorrect result was returned:");
					logger.log(Logger.Level.ERROR, "expected :" + expected);
					logger.log(Logger.Level.ERROR, "actual :" + actual);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "A null was returned for the reason text");
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

	private void getFaultReasonTexts1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonTexts1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO,
					"Calling getFaultReasonTexts() must" + " throw UnsupportedOperationException");
			sf.getFaultReasonTexts();
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

	private void getFaultReasonTexts2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonTexts2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String expected = "Its my fault";
			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected, Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if (actual.equals(expected)) {
						logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						logger.log(Logger.Level.ERROR, "expected= '" + expected + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 1) {
				logger.log(Logger.Level.ERROR, "More than one reason text was returned");
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

	private void getFaultReasonTexts3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultReasonTexts3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected1 = "Its my fault";
			String expected2 = "Its my fault again";
			logger.log(Logger.Level.INFO, "Adding FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected1, Locale.ENGLISH);
			logger.log(Logger.Level.INFO, "Adding another FaultReasonText to SOAPFault");
			sf.addFaultReasonText(expected2, Locale.UK);
			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if ((actual.equals(expected1)) || (actual.equals(expected2))) {
						logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						TestUtil.logErr("expected='" + expected1 + "' or '" + expected2 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 2) {
				logger.log(Logger.Level.ERROR, "More than two reason text's were returned");
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

	private void setFaultNode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setFaultNode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling setFaultNode() must" + " throw UnsupportedOperationException");
			sf.setFaultNode("http://faultnode.com");
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

	private void SetGetFaultNode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultNode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected = "http://faultnode.com";
			logger.log(Logger.Level.INFO, "Calling setFaultNode()");
			sf.setFaultNode("http://faultnode.com");
			String result = sf.getFaultNode();
			if (result != null) {
				if (!result.equals(expected)) {
					logger.log(Logger.Level.ERROR, "The wrong node was returned");
					logger.log(Logger.Level.ERROR, "expected = " + expected);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "result = " + result);
			} else {
				TestUtil.logErr("getFaultNode returned a null when a node was configured");
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

	private void SetGetFaultNode2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultNode2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected1 = "http://faultnode.com";
			logger.log(Logger.Level.INFO, "Calling setFaultNode()");
			sf.setFaultNode(expected1);

			String expected2 = "http://www.faultnode.com";
			logger.log(Logger.Level.INFO, "Calling setFaultNode() a second time");
			sf.setFaultNode(expected2);

			String result = sf.getFaultNode();
			if (result != null) {
				if (!result.equals(expected2)) {
					logger.log(Logger.Level.ERROR, "The wrong node was returned");
					logger.log(Logger.Level.ERROR, "expected = " + expected2);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "result = " + result);
			} else {
				TestUtil.logErr("getFaultNode returned a null when a node was configured");
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

	private void getFaultNode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultNode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getFaultNode() must" + " throw UnsupportedOperationException");
			sf.getFaultNode();
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

	private void getFaultNode2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultNode2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getFaultNode() to retrieve Fault Node");
			String result = sf.getFaultNode();
			if (result == null) {
				logger.log(Logger.Level.INFO, "getFaultNode returned null (expected)");
			} else {
				logger.log(Logger.Level.ERROR, "Calling getFaultNode returned a non null node");
				logger.log(Logger.Level.ERROR, "result = " + result);
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

	private void setFaultRole1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setFaultRole1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling setFaultRole() must" + " throw UnsupportedOperationException");
			sf.setFaultRole("http://faultrole.com");
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

	private void SetGetFaultRole1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultRole1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String expected = "http://faultrole.com";
			logger.log(Logger.Level.INFO, "Calling setFaultRole()");
			sf.setFaultRole(expected);
			logger.log(Logger.Level.INFO, "Calling getFaultRole()");
			String result = sf.getFaultRole();
			if (result != null) {
				if (!result.equals(expected)) {
					logger.log(Logger.Level.ERROR, "setFaultRole()/getFaultRole() behaved incorrectly");
					TestUtil.logErr("setFaultRole=" + expected + ", getFaultRole=" + result);
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "setFaultRole()/getFaultRole() behaved correctly");
			} else {
				logger.log(Logger.Level.ERROR, "getFaultRole() returned a null");
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

	private void getFaultRole1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultRole1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getFaultRole() must" + " throw UnsupportedOperationException");
			sf.getFaultRole();
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

	private void getFaultRole2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultRole2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Calling getFaultRole() to retrieve Fault Role");
			String s = sf.getFaultRole();
			if (s == null) {
				logger.log(Logger.Level.INFO, "Calling getFaultRole returned null (expected)");
			} else {
				logger.log(Logger.Level.ERROR, "Calling getFaultRole returned a non null");
				logger.log(Logger.Level.ERROR, "getFaultRole result=" + s);
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

	private void SetGetFaultCodeAsName1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultCodeAsName1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				logger.log(Logger.Level.INFO, "Creating Name of: <myfault, flt, http://example.com>");
				Name name = sfactory.createName("myfault", "flt", "http://example.com");
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with " + name.getQualifiedName());
				sf.setFaultCode(name);
				logger.log(Logger.Level.INFO, "Calling getFaultCodeAsName() to retrieve Fault Code");
				Name name2 = sf.getFaultCodeAsName();
				if (name2 != null)
					logger.log(Logger.Level.INFO, "Qualified name=" + name2.getQualifiedName());
				if (name2 == null) {
					logger.log(Logger.Level.ERROR, "Calling getFaultCodeAsName " + "returned null (unexpected)");
					pass = false;
				} else if (!name2.getLocalName().equals(name.getLocalName())
						|| !name2.getPrefix().equals(name.getPrefix()) || !name2.getURI().equals(name.getURI())) {
					logger.log(Logger.Level.ERROR,
							"Calling getFaultCodeAsName returned <" + name2.getLocalName() + "," + name2.getPrefix()
									+ "," + name2.getURI() + ">, expected " + "<myfault,flt,http://example.com>");
					pass = false;
				}
			} else {
				QName qname = SOAPConstants.SOAP_SENDER_FAULT;
				logger.log(Logger.Level.INFO, "Creating Name of: " + qname);
				Name name = SOAP_Util.getSOAPFactory().createName(qname.getLocalPart(), qname.getPrefix(),
						qname.getNamespaceURI());
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with " + name.getQualifiedName());
				sf.setFaultCode(name);
				logger.log(Logger.Level.INFO, "Calling getFaultCodeAsName() to retrieve Fault Code");
				Name name2 = sf.getFaultCodeAsName();
				if (name2 != null)
					logger.log(Logger.Level.INFO, "Qualified name=" + name2.getQualifiedName());
				if (name2 == null) {
					logger.log(Logger.Level.ERROR, "Calling getFaultCodeAsName " + "returned null (unexpected)");
					pass = false;
				} else if (!name2.getLocalName().equals(name.getLocalName())
						|| !name2.getPrefix().equals(name.getPrefix()) || !name2.getURI().equals(name.getURI())) {
					logger.log(Logger.Level.ERROR,
							"Calling getFaultCodeAsName returned <" + name2.getLocalName() + "," + name2.getPrefix()
									+ "," + name2.getURI() + ">, expected " + "<" + name.getLocalName() + ","
									+ name.getPrefix() + "," + name.getURI() + ">");
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

	private void SetGetFaultCodeAsQName1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultCodeAsQName1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				logger.log(Logger.Level.INFO, "Creating QName of: <myfault, flt, http://example.com>");
				QName name = new QName("http://example.com", "myfault", "flt");
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with " + name.toString());
				sf.setFaultCode(name);
				logger.log(Logger.Level.INFO, "Calling getFaultCodeAsQName() to retrieve Fault Code");
				QName name2 = sf.getFaultCodeAsQName();
				if (name2 != null)
					logger.log(Logger.Level.INFO, "Qualified name=" + name2.toString());
				if (name2 == null) {
					logger.log(Logger.Level.ERROR, "Calling getFaultCodeAsQName " + "returned null (unexpected)");
					pass = false;
				} else if (!name2.getLocalPart().equals(name.getLocalPart())
						|| !name2.getPrefix().equals(name.getPrefix())
						|| !name2.getNamespaceURI().equals(name.getNamespaceURI())) {
					logger.log(Logger.Level.ERROR,
							"Calling getFaultCodeAsQName returned <" + name2.getLocalPart() + "," + name2.getPrefix()
									+ "," + name2.getNamespaceURI() + ">, expected "
									+ "<myfault,flt,http://example.com>");
					pass = false;
				}
			} else {
				QName name = SOAPConstants.SOAP_SENDER_FAULT;
				logger.log(Logger.Level.INFO, "Creating QName of: " + name);
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with " + name.toString());
				sf.setFaultCode(name);
				logger.log(Logger.Level.INFO, "Calling getFaultCodeAsQName() to retrieve Fault Code");
				QName name2 = sf.getFaultCodeAsQName();
				if (name2 != null)
					logger.log(Logger.Level.INFO, "Qualified name=" + name2.toString());
				if (name2 == null) {
					logger.log(Logger.Level.ERROR, "Calling getFaultCodeAsQName " + "returned null (unexpected)");
					pass = false;
				} else if (!name2.getLocalPart().equals(name.getLocalPart())
						|| !name2.getPrefix().equals(name.getPrefix())
						|| !name2.getNamespaceURI().equals(name.getNamespaceURI())) {
					logger.log(Logger.Level.ERROR,
							"Calling getFaultCodeAsQName returned <" + name2.getLocalPart() + "," + name2.getPrefix()
									+ "," + name2.getNamespaceURI() + ">, expected " + "<" + name.getLocalPart() + ","
									+ name.getPrefix() + name.getNamespaceURI() + ">");
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

	private void SetGetFaultStringLocale1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetFaultStringLocale1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			Locale expected = Locale.ENGLISH;
			logger.log(Logger.Level.INFO, "Setting fault string to Locale " + expected);
			sf.setFaultString("this is the fault string", expected);

			logger.log(Logger.Level.INFO, "calling getFaultStringLocale()");
			Locale result = sf.getFaultStringLocale();
			if (result != null) {
				if (!result.equals(expected)) {
					logger.log(Logger.Level.ERROR,
							"setFaultString(string,Locale)/getFaultStringLocale behaved incorrectly");
					logger.log(Logger.Level.ERROR, "expected=" + expected);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "result=" + result);
			} else {
				logger.log(Logger.Level.ERROR,
						"getFaultStringLocale() returned a null result, eventhough the fault string has a locale");
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

	private void setFaultStringLocale1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setFaultStringLocale2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			String expected1 = "this is the fault string one";
			logger.log(Logger.Level.INFO, "Setting fault string one to Locale " + Locale.ENGLISH);
			sf.addFaultReasonText(expected1, Locale.ENGLISH);

			String expected2 = "this is the fault string two";
			logger.log(Logger.Level.INFO, "Setting fault string two to Locale " + Locale.ENGLISH);
			sf.setFaultString(expected2, Locale.ENGLISH);

			logger.log(Logger.Level.INFO, "Getting FaultReasonTexts from SOAPFault");
			Iterator i = sf.getFaultReasonTexts();
			boolean found = false;
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof String) {
					String actual = (String) o;
					if (actual.equals(expected2)) {
						if (!found) {
							found = true;
							logger.log(Logger.Level.INFO, "Reason= '" + actual + "'");
						} else {
							TestUtil.logErr("Received a duplicate Reason text:'" + actual + "'");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected reason text:");
						logger.log(Logger.Level.ERROR, "expected= '" + expected1 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of String was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No reason text was returned");
				pass = false;
			}
			if (j > 1) {
				logger.log(Logger.Level.ERROR, "More than one reason text was returned");
				pass = false;
			}
			if (!found) {
				logger.log(Logger.Level.ERROR, "The following Reason text was not received: '" + expected1 + "'");
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

	private void getFaultStringLocale1SOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultStringLocale1SOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting fault string with no Locale");
			sf.setFaultString("this is the fault string");

			logger.log(Logger.Level.INFO, "calling getFaultStringLocale()");
			Locale result = sf.getFaultStringLocale();
			if (result == null) {
				logger.log(Logger.Level.INFO, "null Locale returned (expected)");
			} else {
				logger.log(Logger.Level.ERROR, "getFaultStringLocale() returned a non-null result");
				logger.log(Logger.Level.ERROR, "result=" + result);
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

	private void getFaultStringLocale1SOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultStringLocale1SOAP12Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting fault string with no Locale");
			sf.setFaultString("this is the fault string");

			logger.log(Logger.Level.INFO, "calling getFaultStringLocale()");
			Locale result = sf.getFaultStringLocale();
			if (result == null) {
				logger.log(Logger.Level.ERROR, "null Locale returned (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getFaultStringLocale() returned a non-null result (expected)");
				logger.log(Logger.Level.INFO, "result=" + result);
				if (!result.equals(Locale.getDefault())) {
					TestUtil.logErr("Got: " + result + ", Expected: " + Locale.getDefault());
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

	private void appendFaultSubcode1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "appendFaultSubcode1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Appending fault Subcode");
			QName expected1 = new QName("http://example.com", "myfault1", "flt1");
			sf.appendFaultSubcode(expected1);
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

	private void appendFaultSubcode2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "appendFaultSubcode2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {

			setup();

			QName expected1 = new QName("http://example.com", "myfault1", "flt1");
			QName expected2 = new QName("http://example.com", "myfault2", "flt2");
			boolean found1 = false;
			boolean found2 = false;

			logger.log(Logger.Level.INFO, "Appending fault Subcode");
			sf.appendFaultSubcode(expected1);
			logger.log(Logger.Level.INFO, "Appending a second fault Subcode");
			sf.appendFaultSubcode(expected2);

			logger.log(Logger.Level.INFO, "Getting FaultSubCodes from SOAPFault");
			Iterator i = sf.getFaultSubcodes();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof QName) {
					QName actual = (QName) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected2)) {
						if (!found2) {
							found2 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected Subcodes:");
						TestUtil.logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of QName was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No Subcode was returned");
				pass = false;
			}
			if (j > 2) {
				logger.log(Logger.Level.ERROR, "More than two Subcodes were returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected1 + "'");
				pass = false;
			}
			if (!found2) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected2 + "'");
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

	private void getFaultSubcodes1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultSubcodes1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Appending fault Subcode");
			sf.getFaultSubcodes();
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

	private void getFaultSubcodes2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultSubcodes2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {

			setup();

			QName expected1 = new QName("http://example.com", "myfault1", "flt1");
			QName expected2 = new QName("http://example.com", "myfault2", "flt2");
			boolean found1 = false;
			boolean found2 = false;

			logger.log(Logger.Level.INFO, "Appending fault Subcode");
			sf.appendFaultSubcode(expected1);
			logger.log(Logger.Level.INFO, "Appending a second fault Subcode");
			sf.appendFaultSubcode(expected2);
			logger.log(Logger.Level.INFO, "Getting FaultSubCodes from SOAPFault for the first time");
			Iterator i = sf.getFaultSubcodes();
			int j = 0;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof QName) {
					QName actual = (QName) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected2)) {
						if (!found2) {
							found2 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected Subcodes:");
						TestUtil.logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of QName was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No Subcode was returned");
				pass = false;
			}
			if (j > 2) {
				logger.log(Logger.Level.ERROR, "More than two Subcodes were returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected1 + "'");
				pass = false;
			}
			if (!found2) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected2 + "'");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Getting FaultSubCodes from SOAPFault for the second time");
			i = sf.getFaultSubcodes();
			j = 0;
			found1 = false;
			found2 = false;
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof QName) {
					QName actual = (QName) o;
					if (actual.equals(expected1)) {
						if (!found1) {
							found1 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}
					} else if (actual.equals(expected2)) {
						if (!found2) {
							found2 = true;
							logger.log(Logger.Level.INFO, "Subcode= '" + actual + "'");
						} else {
							logger.log(Logger.Level.ERROR, "Received a duplicate Subcode :'" + actual + "'");
							pass = false;
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not receive expected Subcodes:");
						TestUtil.logErr("expected= '" + expected1 + "' or '" + expected2 + "'");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "An object that is not an instance of QName was returned");
					logger.log(Logger.Level.ERROR, "The object is:" + o);
					pass = false;
				}
				j++;
			}
			if (j < 1) {
				logger.log(Logger.Level.ERROR, "No Subcode was returned");
				pass = false;
			}
			if (j > 2) {
				logger.log(Logger.Level.ERROR, "More than two Subcodes were returned");
				pass = false;
			}
			if (!found1) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected1 + "'");
				pass = false;
			}
			if (!found2) {
				logger.log(Logger.Level.ERROR, "The following Subcode was not received: '" + expected2 + "'");
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

	private void hasDetail1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "hasDetail1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			boolean result = sf.hasDetail();
			if (result == false) {
				logger.log(Logger.Level.INFO, "hasDetail() returned false");
			} else {
				logger.log(Logger.Level.ERROR, "hasDetail() returned true when no detail existed");
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

	private void hasDetail2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "hasDetail2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			Detail d = null;
			d = sf.addDetail();
			if (d == null) {
				logger.log(Logger.Level.ERROR, "addDetail() returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "addDetail() called successfully");
				boolean result = sf.hasDetail();
				if (result == false) {
					logger.log(Logger.Level.ERROR, "hasDetail() returned false when a detail did exist");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "hasDetail() returned true");
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

	private void removeAllFaultSubcodes1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllFaultSubcodes1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			sf.removeAllFaultSubcodes();
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

	private void removeAllFaultSubcodes2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllFaultSubcodes2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {

			setup();
			QName expected1 = new QName("http://example.com", "myfault1", "flt1");
			QName expected2 = new QName("http://example.com", "myfault2", "flt2");

			logger.log(Logger.Level.INFO, "Appending fault Subcode");
			sf.appendFaultSubcode(expected1);
			logger.log(Logger.Level.INFO, "Appending a second fault Subcode");
			sf.appendFaultSubcode(expected2);

			logger.log(Logger.Level.INFO, "Removing all  FaultSubCodes from SOAPFault");
			sf.removeAllFaultSubcodes();

			logger.log(Logger.Level.INFO, "Getting FaultSubCodes from SOAPFault");
			Iterator i = sf.getFaultSubcodes();
			if (i.hasNext() == false) {
				logger.log(Logger.Level.INFO, "All FaultSubcodes were removed");
			} else {
				logger.log(Logger.Level.INFO, "Not all FaultSubcodes were removed:");
				while (i.hasNext()) {
					Object o = i.next();
					if (o instanceof QName) {
						QName actual = (QName) o;
						logger.log(Logger.Level.ERROR, "Received unexpected Subcodes:");
						logger.log(Logger.Level.ERROR, "actual= '" + actual + "'");
						pass = false;
					} else {
						logger.log(Logger.Level.ERROR, "An object that is not an instance of QName was returned");
						logger.log(Logger.Level.ERROR, "The object is:" + o);
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

	private void SetFaultCodeNameSOAPExceptionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetFaultCodeNameSOAPExceptionTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				try {
					logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified Name object"
							+ " may succeed for SOAP1.1 protocol");
					Name name = sfactory.createName("myfault");
					sf.setFaultCode(name);
				} catch (SOAPException e) {
				}
			} else {
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified Name object"
						+ " must throw SOAPException for SOAP1.2 protocol");
				Name name = sfactory.createName("myfault");
				sf.setFaultCode(name);
				logger.log(Logger.Level.ERROR, "Did not throw SOAPException");
				pass = false;
			}
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw SOAPException");
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

	private void SetFaultCodeQNameSOAPExceptionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetFaultCodeQNameSOAPExceptionTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				try {
					logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified QName object"
							+ " may succeed for SOAP1.1 protocol");
					sf.setFaultCode(new QName("myfault"));
				} catch (SOAPException e) {
				}
			} else {
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified QName object"
						+ " must throw SOAPException for SOAP1.2 protocol");
				sf.setFaultCode(new QName("myfault"));
				logger.log(Logger.Level.ERROR, "Did not throw SOAPException");
				pass = false;
			}
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw SOAPException");
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

	private void SetFaultCodeStringSOAPExceptionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetFaultCodeStringSOAPExceptionTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				try {
					logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified String object"
							+ " may succeed for SOAP1.1 protocol");
					sf.setFaultCode("Server");
				} catch (SOAPException e) {
				}
			} else {
				logger.log(Logger.Level.INFO, "Calling setFaultCode() with unqualified String object"
						+ " must throw SOAPException for SOAP1.2 protocol");
				sf.setFaultCode("Server");
				logger.log(Logger.Level.ERROR, "Did not throw SOAPException");
				pass = false;
			}
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw SOAPException");
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

	private void AppendFaultSubcodeSOAPExceptionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "AppendFaultSubcodeSOAPExceptionTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO,
					"Calling appendFaultSubcode() with unqualified QName object" + " must throw SOAPException");
			QName name = new QName("mysubcode");
			sf.appendFaultSubcode(name);
			logger.log(Logger.Level.ERROR, "Did not throw SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw SOAPException");
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

	private void AddDetailSOAPExceptionTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "AddDetailSOAPExceptionTest");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Detail d1 = null;
			d1 = sf.addDetail();
			if (d1 == null) {
				logger.log(Logger.Level.ERROR, "addDetail() returned null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "addDetail() returned a non-null detail");
			}

			try {
				sf.addDetail();
				logger.log(Logger.Level.ERROR, "A SOAPException should have been thrown");
				pass = false;
			} catch (SOAPException se) {
				pass = true;
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
