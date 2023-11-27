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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;
import com.sun.ts.tests.saaj.common.XMLUtils;

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
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPFactoryTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPFactoryTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPElement se = null;

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

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("newInstanceTest1")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest1");
			newInstanceTest1(req, res);
		} else if (testname.equals("newInstanceTest2")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest2");
			newInstanceTest2(req, res);
		} else if (testname.equals("newInstanceTest3")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest3");
			newInstanceTest3(req, res);
		} else if (testname.equals("newInstanceTest4")) {
			logger.log(Logger.Level.INFO, "Starting newInstanceTest4");
			newInstanceTest4(req, res);
		} else if (testname.equals("createElementTest1")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest1");
			createElementTest1(req, res);
		} else if (testname.equals("createElementTest2")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest2");
			createElementTest2(req, res);
		} else if (testname.equals("createElementTest3")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest3");
			createElementTest3(req, res);
		} else if (testname.equals("createElementTest4")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest4");
			createElementTest4(req, res);
		} else if (testname.equals("createElementTest5")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest5");
			createElementTest5(req, res);
		} else if (testname.equals("createElementTest6")) {
			logger.log(Logger.Level.INFO, "Starting createElementTest6");
			createElementTest6(req, res);
		} else if (testname.equals("createDetailTest1")) {
			logger.log(Logger.Level.INFO, "Starting createDetailTest1");
			createDetailTest1(req, res);
		} else if (testname.equals("createNameTest1")) {
			logger.log(Logger.Level.INFO, "Starting createNameTest1");
			createNameTest1(req, res);
		} else if (testname.equals("createNameTest2")) {
			logger.log(Logger.Level.INFO, "Starting createNameTest2");
			createNameTest2(req, res);
		} else if (testname.equals("createFaultTest1")) {
			logger.log(Logger.Level.INFO, "Starting createFaultTest1");
			createFaultTest1(req, res);
		} else if (testname.equals("createFaultTest2")) {
			logger.log(Logger.Level.INFO, "Starting createFaultTest2");
			createFaultTest2(req, res);
		} else if (testname.equals("createFaultSOAPExceptionTest1")) {
			logger.log(Logger.Level.INFO, "Starting createFaultSOAPExceptionTest1");
			createFaultSOAPExceptionTest1(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("SOAPFactoryTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("SOAPFactoryTestServlet:init (Leaving)");
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

	private void newInstanceTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create SOAPFactory object");
			SOAPFactory sf = SOAPFactory.newInstance();
			if (sf == null) {
				logger.log(Logger.Level.ERROR, "SOAPFactory.newInstance() returned null");
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

	private void newInstanceTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create a SOAP1.1 SOAPFactory object");
			SOAPFactory sf = SOAPFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
			if (sf == null) {
				logger.log(Logger.Level.ERROR, "SOAPFactory.newInstance() returned null");
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

	private void newInstanceTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create a SOAP1.2 SOAPFactory object");
			SOAPFactory sf = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			if (sf == null) {
				logger.log(Logger.Level.ERROR, "SOAPFactory.newInstance() returned null");
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

	private void newInstanceTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "newInstanceTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Try and create a BOGUS SOAPFactory object");
			SOAPFactory sf = SOAPFactory.newInstance("BOGUS");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Caught expected SOAPException");
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

	private void createElementTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Create Name object with localName=MyName1, " + "prefix=MyPrefix1, uri=MyUri1");
				Name name = envelope.createName("MyName1", "MyPrefix1", "MyUri1");
				logger.log(Logger.Level.INFO, "Create SOAPElement object with above Name object");
				SOAPElement se = sf.createElement(name);
				if (se == null) {
					logger.log(Logger.Level.ERROR, "createElementTest1() could not create SOAPElement object");
					pass = false;
				} else {
					name = se.getElementName();
					String localName = name.getLocalName();
					String prefix = name.getPrefix();
					String uri = name.getURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix == null) {
						logger.log(Logger.Level.ERROR, "prefix is null (expected MyPrefix1)");
						pass = false;
					} else if (!prefix.equals("MyPrefix1")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected MyPrefix1)");
						pass = false;
					} else if (uri == null) {
						logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
						pass = false;
					} else if (!uri.equals("MyUri1")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
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

	private void createElementTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest2() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create SOAPElement object with localName=MyName1");
				SOAPElement se = sf.createElement("MyName1");
				if (se == null) {
					logger.log(Logger.Level.ERROR, "createElementTest2() could not create SOAPElement object");
					pass = false;
				} else {
					Name name = se.getElementName();
					String localName = name.getLocalName();
					String prefix = name.getPrefix();
					String uri = name.getURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix != null && !prefix.equals("")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected null or null string)");
						pass = false;
					} else if (uri != null && !uri.equals("")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected null or null string)");
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

	private void createElementTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest3() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Create SOAPElement object with localName=MyName1" + ", prefix=MyPrefix1, uri=MyUri1");
				SOAPElement se = sf.createElement("MyName1", "MyPrefix1", "MyUri1");
				if (se == null) {
					logger.log(Logger.Level.ERROR, "createElementTest3() could not create SOAPElement object");
					pass = false;
				} else {
					Name name = se.getElementName();
					String localName = name.getLocalName();
					String prefix = name.getPrefix();
					String uri = name.getURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix == null) {
						logger.log(Logger.Level.ERROR, "prefix is null (expected MyPrefix1)");
						pass = false;
					} else if (!prefix.equals("MyPrefix1")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected MyPrefix1)");
						pass = false;
					} else if (uri == null) {
						logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
						pass = false;
					} else if (!uri.equals("MyUri1")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
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

	private void createElementTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest4() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Create QName object with localName=MyName1, " + "prefix=MyPrefix1, uri=MyUri1");
				QName name = new QName("MyUri1", "MyName1", "MyPrefix1");
				logger.log(Logger.Level.INFO, "Create SOAPElement object with above QName object");
				SOAPElement se = sf.createElement(name);
				if (se == null) {
					logger.log(Logger.Level.ERROR, "createElementTest4() could not create SOAPElement object");
					pass = false;
				} else {
					name = se.getElementQName();
					String localName = name.getLocalPart();
					String prefix = name.getPrefix();
					String uri = name.getNamespaceURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix == null) {
						logger.log(Logger.Level.ERROR, "prefix is null (expected MyPrefix1)");
						pass = false;
					} else if (!prefix.equals("MyPrefix1")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected MyPrefix1)");
						pass = false;
					} else if (uri == null) {
						logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
						pass = false;
					} else if (!uri.equals("MyUri1")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
						pass = false;
					}
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createElementTest4() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createElementTest4() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createElementTest5(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest5");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest5() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create a DOMElement");
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				Document document = builder.newDocument();
				Element de = document.createElementNS("http://MyNamespace.org/", "MyTag");
				XMLUtils.XmlDumpDOMNodes(de);
				logger.log(Logger.Level.INFO, "Create a SOAPElement from a DOMElement");
				logger.log(Logger.Level.INFO, "Calling SOAPFactory.createElement(org.w3c.dom.Element)");
				SOAPElement se = sf.createElement(de);
				XMLUtils.XmlDumpDOMNodes(se);
				logger.log(Logger.Level.INFO, "Check that DOMElement and SOAPElement names are equal");
				logger.log(Logger.Level.INFO, "DOMElement name=" + de.getNodeName());
				logger.log(Logger.Level.INFO, "DOMElement prefix=" + de.getPrefix());
				logger.log(Logger.Level.INFO, "DOMElement uri=" + de.getNamespaceURI());
				logger.log(Logger.Level.INFO, "SOAPElement name=" + se.getNodeName());
				logger.log(Logger.Level.INFO, "SOAPElement prefix=" + se.getPrefix());
				logger.log(Logger.Level.INFO, "SOAPElement uri=" + se.getNamespaceURI());
				if (!de.getNodeName().equals(se.getNodeName()) || !de.getNamespaceURI().equals(se.getNamespaceURI())) {
					logger.log(Logger.Level.ERROR, "Node names are not equal");
					logger.log(Logger.Level.ERROR, "Got: <URI=" + se.getNamespaceURI() + ", PREFIX=" + se.getPrefix()
							+ ", NAME=" + se.getNodeName() + ">");
					logger.log(Logger.Level.ERROR, "Expected: <URI=" + de.getNamespaceURI() + ", PREFIX="
							+ de.getPrefix() + ", NAME=" + de.getNodeName() + ">");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		if (pass)
			logger.log(Logger.Level.INFO, "createElementTest5() test PASSED");
		else
			logger.log(Logger.Level.ERROR, "createElementTest5() test FAILED");
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void createElementTest6(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createElementTest6");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createElementTest6() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create first SOAPElement");
				QName qname = new QName("http://MyNamespace.org/", "MyTag");
				SOAPElement se1 = sf.createElement(qname);
				XMLUtils.XmlDumpDOMNodes(se1);
				logger.log(Logger.Level.INFO, "Create second SOAPElement from first SOAPElement");
				logger.log(Logger.Level.INFO, "Calling SOAPFactory.createElement(SOAPElement)");
				SOAPElement se2 = sf.createElement(se1);
				XMLUtils.XmlDumpDOMNodes(se2);
				logger.log(Logger.Level.INFO, "Check the two SOAPElement's for equality and sameness");
				if (!se1.isEqualNode(se2) && !se1.isSameNode(se2)) {
					logger.log(Logger.Level.ERROR, "The SOAPElement's are not equal and not the same (unexpected)");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "The SOAPElement's are equal and the same (expected)");
				logger.log(Logger.Level.INFO, "Check that SOAPElement names are equal");
				logger.log(Logger.Level.INFO, "SOAPElement1 name=" + se1.getNodeName());
				logger.log(Logger.Level.INFO, "SOAPElement1 prefix=" + se1.getPrefix());
				logger.log(Logger.Level.INFO, "SOAPElement1 uri=" + se1.getNamespaceURI());
				logger.log(Logger.Level.INFO, "SOAPElement2 name=" + se2.getNodeName());
				logger.log(Logger.Level.INFO, "SOAPElement2 prefix=" + se2.getPrefix());
				logger.log(Logger.Level.INFO, "SOAPElement2 uri=" + se2.getNamespaceURI());
				if (!se1.getNodeName().equals(se2.getNodeName())
						|| !se1.getNamespaceURI().equals(se2.getNamespaceURI())) {
					logger.log(Logger.Level.ERROR, "Node names are not equal");
					logger.log(Logger.Level.ERROR, "Got: <URI=" + se1.getNamespaceURI() + ", PREFIX=" + se1.getPrefix()
							+ ", NAME=" + se1.getNodeName() + ">");
					TestUtil.logErr("Expected: <URI=" + se2.getNamespaceURI() + ", PREFIX=" + se2.getPrefix()
							+ ", NAME=" + se2.getNodeName() + ">");
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

	private void createDetailTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createDetailTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createDetailTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create Detail object");
				Detail d = sf.createDetail();
				if (d == null) {
					logger.log(Logger.Level.ERROR, "createDetailTest1() could not create Detail object");
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

	private void createNameTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createNameTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createNameTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Create Name object localName=MyName1");
				Name name = sf.createName("MyName1");
				if (name == null) {
					logger.log(Logger.Level.ERROR, "createName() returned null");
					pass = false;
				} else {
					String localName = name.getLocalName();
					String prefix = name.getPrefix();
					String uri = name.getURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix != null && !prefix.equals("")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected null or null string)");
						pass = false;
					} else if (uri != null && !uri.equals("")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected null or null string)");
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

	private void createNameTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createNameTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			if (sf == null) {
				TestUtil.logErr("createNameTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Create Name object localName=MyName1, " + "prefix=MyPrefix1, uri=MyUri1");
				Name name = sf.createName("MyName1", "MyPrefix1", "MyUri1");
				if (name == null) {
					logger.log(Logger.Level.ERROR, "createName() returned null");
					pass = false;
				} else {
					String localName = name.getLocalName();
					String prefix = name.getPrefix();
					String uri = name.getURI();
					logger.log(Logger.Level.INFO, "localName=" + localName);
					logger.log(Logger.Level.INFO, "prefix=" + prefix);
					logger.log(Logger.Level.INFO, "uri=" + uri);
					if (localName == null) {
						logger.log(Logger.Level.ERROR, "localName is null (expected MyName1)");
						pass = false;
					} else if (!localName.equals("MyName1")) {
						logger.log(Logger.Level.ERROR, "localName is wrong (expected MyName1)");
						pass = false;
					} else if (prefix == null) {
						logger.log(Logger.Level.ERROR, "prefix is null (expected MyPrefix1)");
						pass = false;
					} else if (!prefix.equals("MyPrefix1")) {
						logger.log(Logger.Level.ERROR, "prefix is wrong (expected MyPrefix1)");
						pass = false;
					} else if (uri == null) {
						logger.log(Logger.Level.ERROR, "uri is null (expected MyUri1)");
						pass = false;
					} else if (!uri.equals("MyUri1")) {
						logger.log(Logger.Level.ERROR, "uri is wrong (expected MyUri1)");
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

	private void createFaultTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createFaultTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory factory = SOAP_Util.getSOAPFactory();
			if (factory == null) {
				TestUtil.logErr("createFaultTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				SOAPFault sf = factory.createFault();
				if (sf == null) {
					logger.log(Logger.Level.ERROR, "createFault() returned null");
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

	private void createFaultTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createFaultTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory factory = SOAP_Util.getSOAPFactory();
			if (factory == null) {
				TestUtil.logErr("createFaultTest2() could not create SOAPFactory object");
				pass = false;
			} else {
				if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
					SOAPFault sf = factory.createFault("This is the fault reason.", SOAPConstants.SOAP_RECEIVER_FAULT);
				} else {
					SOAPFault sf = factory.createFault("This is the fault reason.", SOAPConstants.SOAP_RECEIVER_FAULT);
					if (sf == null) {
						logger.log(Logger.Level.ERROR, "createFault() returned null");
						pass = false;
					} else {
						QName fc = sf.getFaultCodeAsQName();
						logger.log(Logger.Level.INFO, "Expected FaultCode=" + SOAPConstants.SOAP_RECEIVER_FAULT);
						logger.log(Logger.Level.INFO, "Expected ReasonText=This is the fault reason.");
						logger.log(Logger.Level.INFO, "Actual FaultCode=" + fc);
						Iterator i = sf.getFaultReasonTexts();
						if (i == null) {
							logger.log(Logger.Level.ERROR, "Call to getFaultReasonTexts() returned null iterator");
							pass = false;
						} else {
							String reason = "";
							while (i.hasNext())
								reason += (String) i.next();
							logger.log(Logger.Level.INFO, "Actual ReasonText=" + reason);
							if (reason == null || !reason.contains("This is the fault reason.")) {
								logger.log(Logger.Level.ERROR, "Actual ReasonText is not equal expected ReasonText");
								pass = false;
							}
							if (!fc.equals(SOAPConstants.SOAP_RECEIVER_FAULT)) {
								TestUtil.logErr("Actual FaultCode is not equal expected FaultCode");
								pass = false;
							}
						}
					}
				}
			}
		} catch (SOAPException e) {
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				logger.log(Logger.Level.INFO, "Caught expected SOAPException");
			} else {
				logger.log(Logger.Level.ERROR, "Caught unexpected SOAPException");
				TestUtil.printStackTrace(e);
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

	private void createFaultSOAPExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createFaultSOAPExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory factory = SOAP_Util.getSOAPFactory();
			if (factory == null) {
				logger.log(Logger.Level.ERROR, "createFaultSOAPExceptionTest1() could not create SOAPFactory object");
				pass = false;
			} else {
				if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
					SOAPFault sf = factory.createFault("This is the fault reason.",
							new QName("http://MyNamespaceURI.org/", "My Fault Code"));
				} else {
					SOAPFault sf = factory.createFault("This is the fault reason.",
							new QName("http://MyNamespaceURI.org/", "My Fault Code"));
					logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
					pass = false;
				}
			}
		} catch (UnsupportedOperationException e) {
			logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Caught expected SOAPException");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
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
