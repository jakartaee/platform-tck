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

package com.sun.ts.tests.saaj.ee.VerifyInformationItems;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Iterator;
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
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class VerifyInformationItemsTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(VerifyInformationItemsTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPFault sf = null;

	private SOAPElement se = null;

	private String envelopePrefix = null;

	private String envelopeURI = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Create a message from the message factory.
		logger.log(Logger.Level.INFO, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();

		// Message creation takes care of creating the soap part
		// so retrieve the soap part
		logger.log(Logger.Level.INFO, "Get SOAP Part");
		sp = msg.getSOAPPart();

		// Retrieve the envelope from the soap part to start building
		// the soap message.
		logger.log(Logger.Level.INFO, "Get SOAP Envelope");
		envelope = sp.getEnvelope();
		envelopePrefix = envelope.getElementName().getPrefix();
		envelopeURI = envelope.getElementName().getURI();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Header");
		hdr = envelope.getHeader();

		// Retrieve the soap body from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("VerifyEncodingStyleAttributeInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyEncodingStyleAttributeInfoItem");
			VerifyEncodingStyleAttributeInfoItem(req, res);
		} else if (testname.equals("VerifyRoleAttributeInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyRoleAttributeInfoItem");
			VerifyRoleAttributeInfoItem(req, res);
		} else if (testname.equals("VerifyRelayAttributeInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyRelayAttributeInfoItem");
			VerifyRelayAttributeInfoItem(req, res);
		} else if (testname.equals("VerifyMustUnderstandAttributeInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyMustUnderstandAttributeInfoItem");
			VerifyMustUnderstandAttributeInfoItem(req, res);
		} else if (testname.equals("VerifyEnvelopeElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyEnvelopeElementInfoItem");
			VerifyEnvelopeElementInfoItem(req, res);
		} else if (testname.equals("VerifyHeaderElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyHeaderElementInfoItem");
			VerifyHeaderElementInfoItem(req, res);
		} else if (testname.equals("VerifyBodyElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyBodyElementInfoItem");
			VerifyBodyElementInfoItem(req, res);
		} else if (testname.equals("VerifyBodyChildElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyBodyChildElementInfoItem");
			VerifyBodyChildElementInfoItem(req, res);
		} else if (testname.equals("VerifyFaultElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyFaultElementInfoItem");
			VerifyFaultElementInfoItem(req, res);
		} else if (testname.equals("VerifyDetailElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyDetailElementInfoItem");
			VerifyDetailElementInfoItem(req, res);
		} else if (testname.equals("VerifyCodeElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyCodeElementInfoItem");
			VerifyCodeElementInfoItem(req, res);
		} else if (testname.equals("VerifySubcodeElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifySubcodeElementInfoItem");
			VerifySubcodeElementInfoItem(req, res);
		} else if (testname.equals("VerifyUpgradeElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyUpgradeElementInfoItem");
			VerifyUpgradeElementInfoItem(req, res);
		} else if (testname.equals("VerifyNotUnderstoodElementInfoItem")) {
			logger.log(Logger.Level.INFO, "Starting VerifyNotUnderstoodElementInfoItem");
			VerifyNotUnderstoodElementInfoItem(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("VerifyInformationItemsTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("VerifyInformationItemsTestServlet:init (Leaving)");
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

	private void VerifyEncodingStyleAttributeInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyEncodingStyleAttributeInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Add encodingStyle attribute to SOAP Header element");
			Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
			Name name2 = envelope.createName("encodingStyle", envelopePrefix, envelopeURI);
			SOAPHeaderElement she = hdr.addHeaderElement(name1);
			she.addAttribute(name2, SOAPConstants.URI_NS_SOAP_1_2_ENCODING);

			SOAP_Util.dumpSOAPMessage(msg);

			Name name = null;
			Iterator i = she.getAllAttributes();
			if (!i.hasNext()) {
				logger.log(Logger.Level.ERROR, "No attributes (unexpected)");
				pass = false;
			} else {
				name = (Name) i.next();
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating encodingStyle attribute");
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be encodingStyle");
				if (!localName.equals("encodingStyle")) {
					logger.log(Logger.Level.ERROR,
							"Got LocalName: " + localName + ", Expected LocalName: encodingStyle");
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

	private void VerifyRoleAttributeInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyRoleAttributeInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Add role attribute to SOAP Header element");
			Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
			Name name2 = envelope.createName("role", envelopePrefix, envelopeURI);
			SOAPHeaderElement she = hdr.addHeaderElement(name1);
			she.addAttribute(name2, SOAPConstants.URI_SOAP_1_2_ROLE_NEXT);

			SOAP_Util.dumpSOAPMessage(msg);

			Name name = null;
			Iterator i = she.getAllAttributes();
			if (!i.hasNext()) {
				logger.log(Logger.Level.ERROR, "No attributes (unexpected)");
				pass = false;
			} else {
				name = (Name) i.next();
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating role attribute");
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be role");
				if (!localName.equals("role")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: role");
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

	private void VerifyRelayAttributeInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyRelayAttributeInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Add relay attribute to SOAP Header element");
			Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
			Name name2 = envelope.createName("relay", envelopePrefix, envelopeURI);
			SOAPHeaderElement she = hdr.addHeaderElement(name1);
			she.addAttribute(name2, SOAPConstants.URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER);

			SOAP_Util.dumpSOAPMessage(msg);

			Name name = null;
			Iterator i = she.getAllAttributes();
			if (!i.hasNext()) {
				logger.log(Logger.Level.ERROR, "No attributes (unexpected)");
				pass = false;
			} else {
				name = (Name) i.next();
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating relay SOAPElement Name");
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be relay");
				if (!localName.equals("relay")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: relay");
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

	private void VerifyMustUnderstandAttributeInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyMustUnderstandAttributeInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Add mustUnderstand attribute to SOAP Header element");
			Name name1 = envelope.createName("foo", "f", "http://foo.org/foo");
			Name name2 = envelope.createName("mustUnderstand", envelopePrefix, envelopeURI);
			SOAPHeaderElement she = hdr.addHeaderElement(name1);
			she.addAttribute(name2, "true");

			SOAP_Util.dumpSOAPMessage(msg);

			Name name = null;
			Iterator i = she.getAllAttributes();
			if (!i.hasNext()) {
				logger.log(Logger.Level.ERROR, "No attributes (unexpected)");
				pass = false;
			} else {
				name = (Name) i.next();
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating mustUnderstand SOAPElement Name");
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be mustUnderstand");
				if (!localName.equals("mustUnderstand")) {
					logger.log(Logger.Level.ERROR,
							"Got LocalName: " + localName + ", Expected LocalName: mustUnderstand");
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

	private void VerifyEnvelopeElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyEnvelopeElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Envelope element");
			se = (SOAPElement) sp.getEnvelope();

			SOAP_Util.dumpSOAPMessage(msg);

			logger.log(Logger.Level.INFO, "Validating SOAPElement object creation");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "SOAPElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Envelope SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Envelope");
				if (!localName.equals("Envelope")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Envelope");
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

	private void VerifyHeaderElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyHeaderElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Header element");
			se = (SOAPElement) envelope.getHeader();

			SOAP_Util.dumpSOAPMessage(msg);

			logger.log(Logger.Level.INFO, "Validating SOAPElement object creation");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "SOAPElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Header SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Header");
				if (!localName.equals("Header")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Header");
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

	private void VerifyBodyElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyBodyElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Body element");
			se = (SOAPElement) envelope.getBody();

			SOAP_Util.dumpSOAPMessage(msg);

			logger.log(Logger.Level.INFO, "Validating SOAPElement object creation");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "SOAPElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Body SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Body");
				if (!localName.equals("Body")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Body");
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

	private void VerifyBodyChildElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyBodyChildElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Body Child element");
			se = body.addChildElement(envelope.createName("MyName", "MyPrefix", "MyURI"));

			SOAP_Util.dumpSOAPMessage(msg);

			logger.log(Logger.Level.INFO, "Validating SOAPElement object creation");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "SOAPElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating BodyChild SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO, "Validate the URI which must be MyURI");
				if (!uri.equals("MyURI")) {
					logger.log(Logger.Level.ERROR, "Got URI: " + uri + "\nExpected URI: " + "MyURI");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be MyName");
				if (!localName.equals("MyName")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: MyName");
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

	private void VerifyFaultElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyFaultElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Fault element");
			se = (SOAPElement) body.addFault();

			SOAP_Util.dumpSOAPMessage(msg);

			logger.log(Logger.Level.INFO, "Validating SOAPElement object creation");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "SOAPElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Fault SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Fault");
				if (!localName.equals("Fault")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Fault");
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

	private void VerifyCodeElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyCodeElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Fault Code element");
			SOAPFault sf = body.addFault(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT,
					"This is the fault string (boo hoo hoo)");

			SOAP_Util.dumpSOAPMessage(msg);

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Code SOAPElement");
				logger.log(Logger.Level.INFO, "Get the Code SOAPElement");
				QName name = sf.getFaultCodeAsQName();
				logger.log(Logger.Level.INFO, "URI = " + name.getNamespaceURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalPart());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.toString());
				String uri = name.getNamespaceURI();
				String localName = name.getLocalPart();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be "
						+ SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart());
				if (!localName.equals(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart())) {
					TestUtil.logErr("Got LocalName: " + localName + ", Expected LocalName: "
							+ SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.getLocalPart());
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

	private void VerifySubcodeElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifySubcodeElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating SOAP Fault Subcode element");
			SOAPFault sf = body.addFault(SOAPConstants.SOAP_VERSIONMISMATCH_FAULT,
					"This is the fault string (boo hoo hoo)");
			QName subcode = new QName("http://www.w3.org/2003/05/soap-rpc", "BadArguments", "rpc");
			sf.appendFaultSubcode(subcode);

			SOAP_Util.dumpSOAPMessage(msg);

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Subcode SOAPElement");
				logger.log(Logger.Level.INFO, "Get the Subcode SOAPElement");
				Iterator i = sf.getFaultSubcodes();
				QName name = (QName) i.next();
				logger.log(Logger.Level.INFO, "URI = " + name.getNamespaceURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalPart());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.toString());
				String uri = name.getNamespaceURI();
				String localName = name.getLocalPart();
				logger.log(Logger.Level.INFO, "Validate the URI which must be " + "http://www.w3.org/2003/05/soap-rpc");
				if (!uri.equals("http://www.w3.org/2003/05/soap-rpc")) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + "http://www.w3.org/2003/05/soap-rpc");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Subcode");
				if (!localName.equals("BadArguments")) {
					logger.log(Logger.Level.ERROR,
							"Got LocalName: " + localName + ", Expected LocalName: BadArguments");
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

	private void VerifyDetailElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyDetailElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating a Detail element");
			SOAPFault sf = body.addFault();
			Detail d = sf.addDetail();

			logger.log(Logger.Level.INFO, "Validating Detail element creation");
			if (d == null) {
				logger.log(Logger.Level.ERROR, "Detail element is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Detail element was created");
			}
			SOAPElement se = (SOAPElement) d;

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Detail SOAPElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = se.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Detail");
				if (!localName.equals("Detail")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Detail");
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

	private void VerifyUpgradeElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyUpgradeElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Create a list of supported URIs.
			ArrayList supported = new ArrayList();
			supported.add(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
			supported.add(SOAPConstants.URI_NS_SOAP_ENVELOPE);

			logger.log(Logger.Level.INFO, "Creating Upgrade SOAPHeaderElement");
			she = hdr.addUpgradeHeaderElement(supported.iterator());

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement object creation");
			if (she == null) {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating Upgrade SOAPHeaderElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = she.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be Upgrade");
				if (!localName.equals("Upgrade")) {
					logger.log(Logger.Level.ERROR, "Got LocalName: " + localName + ", Expected LocalName: Upgrade");
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

	private void VerifyNotUnderstoodElementInfoItem(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "VerifyNotUnderstoodElementInfoItem");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating NotUnderstood SOAPHeaderElement");
			she = hdr.addNotUnderstoodHeaderElement(new QName("http://foo.org", "foo", "f"));

			logger.log(Logger.Level.INFO, "Validating SOAPHeaderElement object creation");
			if (she == null) {
				logger.log(Logger.Level.ERROR, "SOAPHeaderElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPHeaderElement was created");
			}

			if (pass) {
				logger.log(Logger.Level.INFO, "Validating NotUnderstood SOAPHeaderElement Name");
				logger.log(Logger.Level.INFO, "Get the ElementName");
				Name name = she.getElementName();
				logger.log(Logger.Level.INFO, "URI = " + name.getURI());
				logger.log(Logger.Level.INFO, "Prefix = " + name.getPrefix());
				logger.log(Logger.Level.INFO, "LocalName = " + name.getLocalName());
				logger.log(Logger.Level.INFO, "QualifiedName = " + name.getQualifiedName());
				String uri = name.getURI();
				String localName = name.getLocalName();
				logger.log(Logger.Level.INFO,
						"Validate the URI which must be " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
				if (!uri.equals(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE)) {
					logger.log(Logger.Level.ERROR,
							"Got URI: " + uri + "\nExpected URI: " + SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Validate the LocalName which must be NotUnderstood");
				if (!localName.equals("NotUnderstood")) {
					logger.log(Logger.Level.ERROR,
							"Got LocalName: " + localName + ", Expected LocalName: NotUnderstood");
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
