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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.net.URL;
import java.util.Properties;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPConnectionTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPConnectionTestServlet.class.getName());

	private String hostname = "localhost";

	private int portnum = 8080;

	private static final String cntxroot = "/SOAPConnection_web";

	private static final String PROTOCOL = "http";

	private static final String SERVLET = cntxroot + "/ReceivingServlet";

	private static final String GETSERVLET = cntxroot + "/GetServlet";

	private TSURL tsurl = new TSURL();

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		SOAP_Util.setup();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("closeTest")) {
			logger.log(Logger.Level.INFO, "Starting closeTest");
			closeTest(req, res);
		} else if (testname.equals("callTest")) {
			logger.log(Logger.Level.INFO, "Starting callTest");
			callTest(req, res);
		} else if (testname.equals("getTest1")) {
			logger.log(Logger.Level.INFO, "Starting getTest1");
			getTest1(req, res);
		} else if (testname.equals("getTest2")) {
			logger.log(Logger.Level.INFO, "Starting getTest2");
			getTest2(req, res);
		} else if (testname.equals("getTest3")) {
			logger.log(Logger.Level.INFO, "Starting getTest3");
			getTest3(req, res);
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

	private void closeTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "closeTest");
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
			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnectionFactory sf = SOAP_Util.getSOAPConnectionFactory();
			SOAPConnection soapcon = sf.createConnection();
			logger.log(Logger.Level.INFO, "Close SOAPConnection object");
			soapcon.close();
			try {
				logger.log(Logger.Level.INFO, "Try and close SOAPConnection object again");
				soapcon.close();
				logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Did get expected SOAPException");
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

	private void callTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "callTest");
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
			// Create a soap connection object
			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnectionFactory sf = SOAP_Util.getSOAPConnectionFactory();
			SOAPConnection soapcon = sf.createConnection();

			System.out.println("Create MessageFactory object");
			MessageFactory mf = MessageFactory.newInstance();

			// Create a soap message from the message factory.
			logger.log(Logger.Level.INFO, "Create SOAP message from message factory");
			SOAPMessage msg = mf.createMessage();

			// Message creation takes care of creating the SOAPPart - a
			// required part of the message as per the SOAP 1.1 spec.
			logger.log(Logger.Level.INFO, "Get SOAP Part");
			SOAPPart sp = msg.getSOAPPart();

			// Retrieve the envelope from the soap part to start building
			// the soap message.
			logger.log(Logger.Level.INFO, "Get SOAP Envelope");
			SOAPEnvelope envelope = sp.getEnvelope();

			// Create a soap header from the envelope.
			logger.log(Logger.Level.INFO, "Create SOAP Header");
			SOAPHeader hdr = envelope.getHeader();

			// Create a soap body from the envelope.
			logger.log(Logger.Level.INFO, "Create SOAP Body");
			SOAPBody bdy = envelope.getBody();

			// Add a soap header element to the header.
			logger.log(Logger.Level.INFO, "Add SOAP Header element [MyTransaction]");
			SOAPHeaderElement transaction = hdr
					.addHeaderElement(envelope.createName("MyTransaction", "t", "request-uri"));
			transaction.setMustUnderstand(true);
			transaction.addTextNode("5");

			// Add a soap body element to the soap body
			logger.log(Logger.Level.INFO, "Add SOAP Body element [GetLastTradePrice]");
			SOAPBodyElement gltp = bdy
					.addBodyElement(envelope.createName("GetLastTradePrice", "ztrade", "http://wombat.ztrade.com"));

			gltp.addChildElement(envelope.createName("symbol", "ztrade", "http://wombat.ztrade.com"))
					.addTextNode("SUNW");

			msg.saveChanges();
			logger.log(Logger.Level.INFO, "Done creating SOAP message");

			// Create a url endpoint for the recipient of the message.
			hostname = SOAP_Util.getHostname();
			portnum = SOAP_Util.getPortnum();
			URL urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET);
			logger.log(Logger.Level.INFO, "URLEndpoint = " + urlEndpoint);

			// Send the message to the endpoint using the connection.
			logger.log(Logger.Level.INFO, "Send sync message with no attachments");
			soapcon.call(msg, urlEndpoint);
			logger.log(Logger.Level.INFO, "Message sent successfully .....");

			// Try and send message on a closed SOAPConnection object
			logger.log(Logger.Level.INFO, "Try and send message on a closed SOAPConnection object");
			try {
				logger.log(Logger.Level.INFO, "Close SOAPConnection object");
				soapcon.close();
				logger.log(Logger.Level.INFO, "Send message on closed SOAPConnection object");
				soapcon.call(msg, urlEndpoint);
				logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Did get expected SOAPException");
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

	private void getTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getTest1");
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

			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnectionFactory sf = SOAP_Util.getSOAPConnectionFactory();
			SOAPConnection con = sf.createConnection();
			logger.log(Logger.Level.INFO, "Create a valid webservice endpoint for invoking HTTP-GET");
			hostname = SOAP_Util.getHostname();
			portnum = SOAP_Util.getPortnum();
			URL urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, GETSERVLET);
			logger.log(Logger.Level.INFO, "Valid Webservice Endpoint=" + urlEndpoint);

			logger.log(Logger.Level.INFO, "Invoking HTTP-GET with a valid webservice " + "endpoint should succeed");
			SOAPMessage reply = con.get(urlEndpoint);
			logger.log(Logger.Level.INFO, "HTTP-GET succeeded (expected)");
			SOAP_Util.dumpSOAPMessage(reply);
		} catch (SOAPException e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected SOAPException");
			pass = false;
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

	private void getTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getTest2");
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

			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnectionFactory sf = SOAP_Util.getSOAPConnectionFactory();
			SOAPConnection con = sf.createConnection();
			logger.log(Logger.Level.INFO, "Create a valid non webservice endpoint for invoking HTTP-GET");
			hostname = SOAP_Util.getHostname();
			portnum = SOAP_Util.getPortnum();
			URL urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, "/");
			logger.log(Logger.Level.INFO, "Valid Non Webservice Endpoint=" + urlEndpoint);

			logger.log(Logger.Level.INFO,
					"Invoking HTTP-GET with a valid non webservice " + "endpoint should throw a SOAPException");
			con.get(urlEndpoint);
			logger.log(Logger.Level.ERROR, "HTTP-GET succeeded (unexpected)");
			logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did get expected SOAPException");
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

	private void getTest3(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getTest3");
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

			logger.log(Logger.Level.INFO, "Create SOAPConnection object");
			SOAPConnectionFactory sf = SOAP_Util.getSOAPConnectionFactory();
			SOAPConnection con = sf.createConnection();
			logger.log(Logger.Level.INFO, "Create an invalid endpoint for invoking HTTP-GET");
			URL urlEndpoint = new URL(PROTOCOL, "bogus.com", 80, "/bogus/bogus");
			logger.log(Logger.Level.INFO, "Invalid Non Existant Endpoint=" + urlEndpoint);

			logger.log(Logger.Level.INFO,
					"Invoking HTTP-GET with an invalid " + "endpoint should throw a SOAPException");
			con.get(urlEndpoint);
			logger.log(Logger.Level.ERROR, "HTTP-GET succeeded (unexpected)");
			logger.log(Logger.Level.ERROR, "Did not get expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did get expected SOAPException");
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
