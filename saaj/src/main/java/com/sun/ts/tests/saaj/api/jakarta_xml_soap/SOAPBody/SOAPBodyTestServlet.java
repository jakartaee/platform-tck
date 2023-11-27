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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPBodyTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPBodyTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPFault fault = null;

	private SOAPFault fault1 = null;

	private SOAPFault fault2 = null;

	private String prefix = null;

	private String uri = null;

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
		prefix = envelope.getElementName().getPrefix();
		uri = envelope.getElementName().getURI();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Header");
		hdr = envelope.getHeader();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addBodyElementTest1")) {
			logger.log(Logger.Level.INFO, "Starting addBodyElementTest1");
			addBodyElementTest1(req, res);
		} else if (testname.equals("addBodyElementTest2")) {
			logger.log(Logger.Level.INFO, "Starting addBodyElementTest2");
			addBodyElementTest2(req, res);
		} else if (testname.equals("addFaultTest1")) {
			logger.log(Logger.Level.INFO, "Starting addFaultTest1");
			addFaultTest1(req, res);
		} else if (testname.equals("addFaultTest2")) {
			logger.log(Logger.Level.INFO, "Starting addFaultTest2");
			addFaultTest2(req, res);
		} else if (testname.equals("addFaultTest3")) {
			logger.log(Logger.Level.INFO, "Starting addFaultTest3");
			addFaultTest3(req, res);
		} else if (testname.equals("addFaultTest4")) {
			logger.log(Logger.Level.INFO, "Starting addFaultTest4");
			addFaultTest4(req, res);
		} else if (testname.equals("addFaultTest5")) {
			logger.log(Logger.Level.INFO, "Starting addFaultTest5");
			addFaultTest5(req, res);
		} else if (testname.equals("addDocumentTest")) {
			logger.log(Logger.Level.INFO, "Starting addDocumentTest");
			addDocumentTest(req, res);
		} else if (testname.equals("getFaultTest")) {
			logger.log(Logger.Level.INFO, "Starting getFaultTest");
			getFaultTest(req, res);
		} else if (testname.equals("hasFaultTest")) {
			logger.log(Logger.Level.INFO, "Starting hasFaultTest");
			hasFaultTest(req, res);
		} else if (testname.equals("extractContentAsDocumentTest1")) {
			logger.log(Logger.Level.INFO, "Starting extractContentAsDocumentTest1");
			extractContentAsDocumentTest1(req, res);
		} else if (testname.equals("extractContentAsDocumentTest2")) {
			logger.log(Logger.Level.INFO, "Starting extractContentAsDocumentTest2");
			extractContentAsDocumentTest2(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");

		}

	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("SOAPBodyTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("SOAPBodyTestServlet:init (Leaving)");
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

	private void addBodyElementTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addBodyElementTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap body element to the soap body
			logger.log(Logger.Level.INFO, "Add Name SOAPBodyElement to SOAPBody object");
			Name name = envelope.createName("GetLastTradePrice", "ztrade", "http://wombat.ztrade.com");
			bodye = body.addBodyElement(name);

			logger.log(Logger.Level.INFO, "Validating SOAPBodyElement object creation ...");
			if (bodye == null) {
				logger.log(Logger.Level.ERROR, "SOAPBodyElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPBodyElement was created");
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

	private void addBodyElementTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addBodyElementTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap body element to the soap body
			logger.log(Logger.Level.INFO, "Add QName SOAPBodyElement to SOAPBody object");
			QName name = new QName("http://wombat.ztrade.com", "GetLastTradePrice", "ztrade");
			bodye = body.addBodyElement(name);

			logger.log(Logger.Level.INFO, "Validating SOAPBodyElement object creation ...");
			if (bodye == null) {
				logger.log(Logger.Level.ERROR, "SOAPBodyElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPBodyElement was created");
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

	private void addFaultTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			logger.log(Logger.Level.INFO, "Add SOAPFault to SOAPBody object");
			fault = body.addFault();

			logger.log(Logger.Level.INFO, "Validating SOAPFault object creation ...");
			if (fault == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault was created");
				logger.log(Logger.Level.INFO, "SOAPFault = " + fault);
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

	private void addFaultTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			Name name = envelope.createName("Server", prefix, uri);
			logger.log(Logger.Level.INFO, "Add Name object SOAPFault to SOAPBody object");
			fault = body.addFault(name, "This is a Server fault");

			logger.log(Logger.Level.INFO, "Validating SOAPFault object creation ...");
			if (fault == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault was created");
				logger.log(Logger.Level.INFO, "SOAPFault = " + fault);
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

	private void addFaultTest3(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			Name name = envelope.createName("Server", prefix, uri);
			logger.log(Logger.Level.INFO, "Add Name object SOAPFault to SOAPBody object with Locale");
			Locale l = new Locale("en", "US");
			fault = body.addFault(name, "This is a Server fault", l);

			logger.log(Logger.Level.INFO, "Validating SOAPFault object creation ...");
			if (fault == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault was created");
				logger.log(Logger.Level.INFO, "SOAPFault = " + fault);
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

	private void addFaultTest4(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			logger.log(Logger.Level.INFO, "Add QName object SOAPFault to SOAPBody object");
			QName name = new QName(uri, "Server", prefix);
			fault = body.addFault(name, "This is a Server fault");

			logger.log(Logger.Level.INFO, "Validating SOAPFault object creation ...");
			if (fault == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault was created");
				logger.log(Logger.Level.INFO, "SOAPFault = " + fault);
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

	private void addFaultTest5(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addFaultTest5");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			logger.log(Logger.Level.INFO, "Add QName object SOAPFault to SOAPBody object with Locale");
			QName name = new QName(uri, "Server", prefix);
			Locale l = new Locale("en", "US");
			fault = body.addFault(name, "This is a Server fault", l);

			logger.log(Logger.Level.INFO, "Validating SOAPFault object creation ...");
			if (fault == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault was created");
				logger.log(Logger.Level.INFO, "SOAPFault = " + fault);
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

	private void getFaultTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getFaultTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a soap fault to the soap body
			logger.log(Logger.Level.INFO, "Add SOAPFault fault1 to SOAPBody object");
			fault1 = body.addFault();

			// Get soap fault from the soap body
			logger.log(Logger.Level.INFO, "Get SOAPFault fault2 from SOAPBody object");
			fault2 = body.getFault();

			logger.log(Logger.Level.INFO, "Validating SOAPFault fault1 and fault2 equality ...");
			if (fault1 == null || fault2 == null) {
				logger.log(Logger.Level.ERROR, "SOAPFault fault1 or fault2 is null");
				pass = false;
			} else if (!(fault1 instanceof SOAPFault) || !(fault2 instanceof SOAPFault)) {
				logger.log(Logger.Level.ERROR, "SOAPFault fault1 or fault2 not instance of SOAPFault");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPFault fault1 was created");
				logger.log(Logger.Level.INFO, "SOAPFault fault2 was created");
				logger.log(Logger.Level.INFO, "SOAPFault fault1 = " + fault1);
				logger.log(Logger.Level.INFO, "SOAPFault fault2 = " + fault2);
				if (!fault1.equals(fault2)) {
					logger.log(Logger.Level.ERROR, "SOAPFault fault1 and fault2 are not equal");
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

	private void hasFaultTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "hasFaultTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Test if soap body has a soap fault
			logger.log(Logger.Level.INFO, "Test that SOAPBody does not have a SOAPFault");
			if (body.hasFault()) {
				logger.log(Logger.Level.ERROR, "SOAPBody has a fault (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPBody does not have a SOAPFault (expected)");
			}

			// Now add a soap fault to the soap body
			logger.log(Logger.Level.INFO, "Add SOAPFault to SOAPBody object");
			fault = body.addFault();

			// Test if soap body has a soap fault
			logger.log(Logger.Level.INFO, "Test that SOAPBody does have a SOAPFault");
			if (body.hasFault()) {
				logger.log(Logger.Level.INFO, "SOAPBody has a fault (expected)");
			} else {
				logger.log(Logger.Level.ERROR, "SOAPBody does not have a SOAPFault (unexpected)");
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

	private void addDocumentTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addDocumentTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a document to the soap body
			logger.log(Logger.Level.INFO, "Add Document to SOAPBody object");
			Document document = null;
			org.w3c.dom.Document myDomDocument = makeDocument();
			SOAPBodyElement sbe = null;
			if (myDomDocument == null) {
				pass = false;
			} else {
				sbe = body.addDocument(myDomDocument);
				SOAP_Util.dumpSOAPMessage(msg);
			}
			logger.log(Logger.Level.INFO, "Validating addDocument object creation ...");
			if (sbe == null) {
				logger.log(Logger.Level.ERROR, "SOAPBodyElement is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPBodyElement was created");
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

	private Document makeDocument() {

		DocumentBuilder docBuilder = null;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			return null;
		}
		DOMImplementation domImpl = docBuilder.getDOMImplementation();
		DocumentType docType = domImpl.createDocumentType("root", "//Test/addDocument", null);
		Document document = domImpl.createDocument(null, "root", docType);
		return document;
	}

	private void extractContentAsDocumentTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "extractContentAsDocumentTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			// Add a child soap element to the soap body
			QName qname1 = new QName("http://wombat.ztrade.com", "GetLastTradePrice", "ztrade");
			logger.log(Logger.Level.INFO, "Qname1=" + qname1);
			logger.log(Logger.Level.INFO, "Add child soap element QName1 to the SOAPBody");
			body.addChildElement(qname1);
			SOAP_Util.dumpSOAPMessage(msg);

			// Extract soap body content as a DOM document
			logger.log(Logger.Level.INFO, "Extract SOAPBody content as a DOM Document");
			Document document = body.extractContentAsDocument();

			logger.log(Logger.Level.INFO, "Validate that a DOM Document was returned");
			if (document == null) {
				logger.log(Logger.Level.ERROR, "Document is null");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "DOM Document was created");
				logger.log(Logger.Level.INFO, "Get the Element from the DOM Document");
				Element element = document.getDocumentElement();
				logger.log(Logger.Level.INFO, "Now get the Element Name");
				String elementName = element.getTagName();
				logger.log(Logger.Level.INFO, "Element Name=" + elementName);
			}

			logger.log(Logger.Level.INFO, "Retreive the children of the SOAPBody (should be none)");
			Iterator i = body.getChildElements();
			int count = SOAP_Util.getIteratorCount(i);
			if (count != 0) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 0");
				logger.log(Logger.Level.ERROR, "The child soap element was not removed");
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

	private void extractContentAsDocumentTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "extractContentAsDocumentTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();
		try {
			setup();

			// Add a 2 child elements to the soap body
			QName qname1 = new QName("http://wombat.ztrade.com", "GetLastTradePrice", "ztrade");
			QName qname2 = new QName("foo");
			logger.log(Logger.Level.INFO, "Qname1=" + qname1);
			logger.log(Logger.Level.INFO, "Qname2=" + qname2);
			logger.log(Logger.Level.INFO, "Add child soap element QName1 to the SOAPBody");
			body.addChildElement(qname1);
			logger.log(Logger.Level.INFO, "Add another child soap element QName2 to the SOAPBody");
			body.addChildElement(qname2);
			SOAP_Util.dumpSOAPMessage(msg);
			logger.log(Logger.Level.INFO, "The SOAPBody content cannot be extraced " + "if more than 1 child element");
			try {
				// Extract soap body content as a DOM document
				logger.log(Logger.Level.INFO, "Extract SOAPBody content as a DOM Document");
				logger.log(Logger.Level.INFO, "Expect a SOAPException to be thrown");
				body.extractContentAsDocument();
				logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Caught expected SOAPException");
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
