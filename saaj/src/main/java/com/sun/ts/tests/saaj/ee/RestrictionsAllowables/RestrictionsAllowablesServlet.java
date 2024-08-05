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

package com.sun.ts.tests.saaj.ee.RestrictionsAllowables;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class RestrictionsAllowablesServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(RestrictionsAllowablesServlet.class.getName());

	private SOAPConnection con = null;

	private Properties harnessProps = null;

	private boolean debug = false;

	private String hostname = "localhost";

	private int portnum = 8080;

	private static final String cntxroot = "/RestrictionsAllowables_web";

	private static final String SOAPVERSION = "SOAPVERSION";

	private static final String NS_PREFIX = "ns-prefix";

	private static final String NS_URI = "ns-uri";

	private static final String PROTOCOL = "http";

	private String soapVersion = null;

	private TSURL tsurl = new TSURL();

	private SOAPMessage message = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPElement se = null;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"RestrictionsAllowablesServlet:init (Entering)");
		try {
			SOAP_Util.setup();
			con = SOAP_Util.getSOAPConnection();
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace(System.err);
			throw new ServletException("Exception occurred: " + e.getMessage());
		}
		logger.log(Logger.Level.TRACE,"RestrictionsAllowablesServlet:init (Leaving)");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "RestrictionsAllowablesServlet:doGet");
		logger.log(Logger.Level.TRACE,"RestrictionsAllowablesServlet:doGet");
		if (harnessProps.getProperty("TESTNAME").equals("encodingStyleAttrSOAP11Test1")) {
			logger.log(Logger.Level.INFO, "Starting encodingStyleAttrSOAP11Test1");
			logger.log(Logger.Level.TRACE,"Starting encodingStyleAttrSOAP11Test1");
			encodingStyleAttrSOAP11Test1(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("encodingStyleAttrSOAP11Test2")) {
			logger.log(Logger.Level.INFO, "Starting encodingStyleAttrSOAP11Test2");
			logger.log(Logger.Level.TRACE,"Starting encodingStyleAttrSOAP11Test2");
			encodingStyleAttrSOAP11Test2(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("noTrailingBlockBodySOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting noTrailingBlockBodySOAP11Test");
			logger.log(Logger.Level.TRACE,"Starting noTrailingBlockBodySOAP11Test");
			noTrailingBlockBodySOAP11Test(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("enforcedQNameBodyElemSOAP11Test")) {
			logger.log(Logger.Level.INFO, "Starting enforcedQNameBodyElemSOAP11Test");
			logger.log(Logger.Level.TRACE,"Starting enforcedQNameBodyElemSOAP11Test");
			enforcedQNameBodyElemSOAP11Test(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("encodingStyleAttrSOAP12Test1")) {
			logger.log(Logger.Level.INFO, "Starting encodingStyleAttrSOAP12Test1");
			logger.log(Logger.Level.TRACE,"Starting encodingStyleAttrSOAP12Test1");
			encodingStyleAttrSOAP12Test1(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("encodingStyleAttrSOAP12Test2")) {
			logger.log(Logger.Level.INFO, "Starting encodingStyleAttrSOAP12Test2");
			logger.log(Logger.Level.TRACE,"Starting encodingStyleAttrSOAP12Test2");
			encodingStyleAttrSOAP12Test2(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("noTrailingBlockBodySOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting noTrailingBlockBodySOAP12Test");
			logger.log(Logger.Level.TRACE,"Starting noTrailingBlockBodySOAP12Test");
			noTrailingBlockBodySOAP12Test(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("enforcedQNameHdrElemTest1")) {
			logger.log(Logger.Level.INFO, "Starting enforcedQNameHdrElemTest1");
			logger.log(Logger.Level.TRACE,"Starting enforcedQNameHdrElemTest1");
			enforcedQNameHdrElemTest1(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("enforcedQNameHdrElemTest2")) {
			logger.log(Logger.Level.INFO, "Starting enforcedQNameHdrElemTest2");
			logger.log(Logger.Level.TRACE,"Starting enforcedQNameHdrElemTest2");
			enforcedQNameHdrElemTest2(req, res);
		} else if (harnessProps.getProperty("TESTNAME").equals("enforcedQNameBodyElemSOAP12Test")) {
			logger.log(Logger.Level.INFO, "Starting enforcedQNameBodyElemSOAP12Test");
			logger.log(Logger.Level.TRACE,"Starting enforcedQNameBodyElemSOAP12Test");
			enforcedQNameBodyElemSOAP12Test(req, res);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "RestrictionsAllowablesServlet:doPost");
		logger.log(Logger.Level.TRACE,"RestrictionsAllowablesServlet:doPost");
		SOAP_Util.doServletPost(req, res);
		hostname = SOAP_Util.getHostname();
		portnum = SOAP_Util.getPortnum();
		soapVersion = SOAP_Util.getSOAPVersion();
		harnessProps = SOAP_Util.getHarnessProps();
		doGet(req, res);
	}

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.INFO, "Create message from message factory");
		message = SOAP_Util.getMessageFactory().createMessage();

		// Message creation takes care of creating the SOAPPart
		logger.log(Logger.Level.INFO, "Get SOAP Part");
		sp = message.getSOAPPart();

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

	/*
	 * Test to verify encodingStyle attribute can be set on Envelope. This is
	 * allowed for the SOAP1.1 protocol.
	 */
	private void encodingStyleAttrSOAP11Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.1 allows the encodingStyle attribute" + " to be set on Envelope");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.setEncodingStyle() and " + "(expect SOAPException)");
			envelope.setEncodingStyle("http://example.com/MyEncodings");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify encodingStyle attribute can be set on Envelope. This is
	 * allowed for the SOAP1.1 protocol.
	 */
	private void encodingStyleAttrSOAP11Test2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.1 does not allow encodingStyle attribute" + " to be set on Envelope");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.addAttribute() and " + "(expect SOAPException)");
			Name encodingStyle = envelope.createName("encodingStyle", "es", SOAPConstants.URI_NS_SOAP_ENVELOPE);
			envelope.addAttribute(encodingStyle, "http://example.com/MyEncodings");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify that trailing blocks are allowed after Body. This is allowed
	 * for the SOAP1.1 protocol.
	 */
	private void noTrailingBlockBodySOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.1 allows trailing blocks after" + "the Body");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.addChildElement() and " + "(expect success)");
			Name afterBody = envelope.createName("AfterBody", "e", "some-uri");
			envelope.addChildElement(afterBody);
			logger.log(Logger.Level.INFO, "Successfully added trailing block after Body");
		} catch (SOAPException e) {
			logger.log(Logger.Level.ERROR, "Unexpected SOAPException: " + e.getMessage());
			pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify non qualified QNames in BodyElements. This is allowed for the
	 * SOAP1.1 protocol.
	 */
	private void enforcedQNameBodyElemSOAP11Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.1 does not require Body elements to be" + " namespace qualified");
			logger.log(Logger.Level.INFO, "Specifying no namespace on Body should succeed");
			body.addChildElement("nouri-just-localname");
			logger.log(Logger.Level.INFO, "Successfully created BodyElement with unqualified QName");
		} catch (SOAPException e) {
			logger.log(Logger.Level.ERROR, "Unexpected SOAPException: " + e.getMessage());
			pass = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify encodingStyle attribute cannot be set on Envelope. This is
	 * restricted for the SOAP1.2 protocol.
	 */
	private void encodingStyleAttrSOAP12Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.2 does not allow encodingStyle attribute" + " to be set on Envelope");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.setEncodingStyle() and " + "(expect SOAPException)");
			envelope.setEncodingStyle("http://example.com/MyEncodings");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify encodingStyle attribute cannot be set on Envelope. This is
	 * restricted for the SOAP1.2 protocol.
	 */
	private void encodingStyleAttrSOAP12Test2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.2 does not allow encodingStyle attribute" + " to be set on Envelope");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.addAttribute() and " + "(expect SOAPException)");
			Name encodingStyle = envelope.createName("encodingStyle", "es", SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
			envelope.addAttribute(encodingStyle, "http://example.com/MyEncodings");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify that no trailing blocks are allowed after Body. This is
	 * restricted for the SOAP1.2 protocol.
	 */
	private void noTrailingBlockBodySOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.2 does not allow trailing blocks after" + "the Body");
			logger.log(Logger.Level.INFO, "Call SOAPEnvelope.addChildElement() and " + "(expect SOAPException)");
			Name afterBody = envelope.createName("AfterBody", "e", "some-uri");
			envelope.addChildElement(afterBody);
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify that unqualified QNames are not allowed on HeaderElements for
	 * both SOAP 1.1 and SOAP 1.2 protocols.
	 */
	private void enforcedQNameHdrElemTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		// Try to add a headerelement not belonging to any namespace.
		try {
			setup();
			logger.log(Logger.Level.INFO,
					"SOAP1.1 and SOAP1.2 requires all HeaderElements to be" + " namespace qualified");
			logger.log(Logger.Level.INFO, "Try adding HeaderElement with unqualified QName "
					+ "not belonging to any namespace (expect SOAPException)");
			logger.log(Logger.Level.INFO, "No URI and no PREFIX in QName");
			hdr.addHeaderElement(envelope.createName("Transaction"));
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify that unqualified QNames are not allowed for Header
	 * ChildElements for both SOAP 1.1 and SOAP 1.2 protocols.
	 */
	private void enforcedQNameHdrElemTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO,
					"SOAP1.1 and SOAP1.2 requires all Header ChildElements to be" + " namespace qualified");
			logger.log(Logger.Level.INFO, "Try adding ChildElement to Header with unqualified QName "
					+ "not belonging to any namespace (expect SOAPException)");
			logger.log(Logger.Level.INFO, "No URI and no PREFIX in QName");
			hdr.addChildElement("MyChildElement");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	/*
	 * Test to verify non qualified QNames in BodyElements. According to the SOAP
	 * 1.2 spec, body elements need not be namespace qualified, but header elements
	 * must be. This is allowed for the SOAP1.2 protocol.
	 */
	private void enforcedQNameBodyElemSOAP12Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		boolean pass = true;
		Properties resultProps = new Properties();

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "SOAP1.2 does not require Body elements to be" + " namespace qualified");
			logger.log(Logger.Level.INFO, "Specifying no namespace on Body should succeed");
			body.addChildElement("nouri-just-localname");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception: " + e.getMessage());
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
