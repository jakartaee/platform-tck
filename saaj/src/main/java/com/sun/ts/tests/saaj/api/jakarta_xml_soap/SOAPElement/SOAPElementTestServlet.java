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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.SOAPElement;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
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
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.Text;

public class SOAPElementTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SOAPElementTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPHeaderElement she = null;

	private SOAPBody body = null;

	private SOAPBodyElement bodye = null;

	private SOAPElement se = null;

	private String eprefix = null;

	private String euri = null;

	private String bprefix = null;

	private String buri = null;

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
		eprefix = envelope.getElementName().getPrefix();
		euri = envelope.getElementName().getURI();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Header");
		hdr = envelope.getHeader();

		// Retrieve the soap header from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		body = envelope.getBody();
		bprefix = body.getElementName().getPrefix();
		buri = body.getElementName().getURI();
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addAttributeTest1")) {
			logger.log(Logger.Level.INFO, "Starting addAttributeTest1");
			addAttributeTest1(req, res);
		} else if (testname.equals("addAttributeTest2")) {
			logger.log(Logger.Level.INFO, "Starting addAttributeTest2");
			addAttributeTest2(req, res);
		} else if (testname.equals("getAttributeValueTest1")) {
			logger.log(Logger.Level.INFO, "Starting getAttributeValueTest1");
			getAttributeValueTest1(req, res);
		} else if (testname.equals("getAttributeValueTest2")) {
			logger.log(Logger.Level.INFO, "Starting getAttributeValueTest2");
			getAttributeValueTest2(req, res);
		} else if (testname.equals("getAllAttributesTest")) {
			logger.log(Logger.Level.INFO, "Starting getAllAttributesTest");
			getAllAttributesTest(req, res);
		} else if (testname.equals("removeAttributeTest1")) {
			logger.log(Logger.Level.INFO, "Starting removeAttributeTest1");
			removeAttributeTest1(req, res);
		} else if (testname.equals("removeAttributeTest2")) {
			logger.log(Logger.Level.INFO, "Starting removeAttributeTest2");
			removeAttributeTest2(req, res);
		} else if (testname.equals("getElementNameTest")) {
			logger.log(Logger.Level.INFO, "Starting getElementNameTest");
			getElementNameTest(req, res);
		} else if (testname.equals("getElementQNameTest")) {
			logger.log(Logger.Level.INFO, "Starting getElementQNameTest");
			getElementQNameTest(req, res);
		} else if (testname.equals("addNamespaceDeclarationTest")) {
			logger.log(Logger.Level.INFO, "Starting addNamespaceDeclarationTest");
			addNamespaceDeclarationTest(req, res);
		} else if (testname.equals("removeNamespaceDeclarationTest")) {
			logger.log(Logger.Level.INFO, "Starting removeNamespaceDeclarationTest");
			removeNamespaceDeclarationTest(req, res);
		} else if (testname.equals("getNamespacePrefixesTest")) {
			logger.log(Logger.Level.INFO, "Starting getNamespacePrefixesTest");
			getNamespacePrefixesTest(req, res);
		} else if (testname.equals("getNamespaceURITest")) {
			logger.log(Logger.Level.INFO, "Starting getNamespaceURITest");
			getNamespaceURITest(req, res);
		} else if (testname.equals("addTextNodeTest1")) {
			logger.log(Logger.Level.INFO, "Starting addTextNodeTest1");
			addTextNodeTest1(req, res);
		} else if (testname.equals("addTextNodeSOAP11Test2")) {
			logger.log(Logger.Level.INFO, "Starting addTextNodeSOAP11Test2");
			addTextNodeSOAP11Test2(req, res);
		} else if (testname.equals("addTextNodeSOAP12Test2")) {
			logger.log(Logger.Level.INFO, "Starting addTextNodeSOAP12Test2");
			addTextNodeSOAP12Test2(req, res);
		} else if (testname.equals("setEncodingStyleSOAP11Test1")) {
			logger.log(Logger.Level.INFO, "Starting setEncodingStyleSOAP11Test1");
			setEncodingStyleSOAP11Test1(req, res);
		} else if (testname.equals("setEncodingStyleSOAP12Test1")) {
			logger.log(Logger.Level.INFO, "Starting setEncodingStyleSOAP12Test1");
			setEncodingStyleSOAP12Test1(req, res);
		} else if (testname.equals("getEncodingStyleSOAP11Test1")) {
			logger.log(Logger.Level.INFO, "Starting getEncodingStyleSOAP11Test1");
			getEncodingStyleSOAP11Test1(req, res);
		} else if (testname.equals("getEncodingStyleSOAP12Test1")) {
			logger.log(Logger.Level.INFO, "Starting getEncodingStyleSOAP12Test1");
			getEncodingStyleSOAP12Test1(req, res);
		} else if (testname.equals("addChildElementTest1")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest1");
			addChildElementTest1(req, res);
		} else if (testname.equals("addChildElementTest2")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest2");
			addChildElementTest2(req, res);
		} else if (testname.equals("addChildElementTest3")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest3");
			addChildElementTest3(req, res);
		} else if (testname.equals("addChildElementTest4")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest4");
			addChildElementTest4(req, res);
		} else if (testname.equals("addChildElementTest5")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest5");
			addChildElementTest5(req, res);
		} else if (testname.equals("addChildElementTest6")) {
			logger.log(Logger.Level.INFO, "Starting addChildElementTest6");
			addChildElementTest6(req, res);
		} else if (testname.equals("getChildElementsTest1")) {
			logger.log(Logger.Level.INFO, "Starting getChildElementsTest1");
			getChildElementsTest1(req, res);
		} else if (testname.equals("getChildElementsTest2")) {
			logger.log(Logger.Level.INFO, "Starting getChildElementsTest2");
			getChildElementsTest2(req, res);
		} else if (testname.equals("getChildElementsTest3")) {
			logger.log(Logger.Level.INFO, "Starting getChildElementsTest3");
			getChildElementsTest3(req, res);
		} else if (testname.equals("getChildElementsTest4")) {
			logger.log(Logger.Level.INFO, "Starting getChildElementsTest4");
			getChildElementsTest4(req, res);
		} else if (testname.equals("removeContentsTest")) {
			logger.log(Logger.Level.INFO, "Starting removeContentsTest");
			removeContentsTest(req, res);
		} else if (testname.equals("getVisibleNamespacePrefixesTest")) {
			logger.log(Logger.Level.INFO, "Starting getVisibleNamespacePrefixesTest");
			getVisibleNamespacePrefixesTest(req, res);
		} else if (testname.equals("setElementQNameTest1")) {
			logger.log(Logger.Level.INFO, "Starting setElementQNameTest1");
			setElementQNameTest1(req, res);
		} else if (testname.equals("setElementQNameTest2")) {
			logger.log(Logger.Level.INFO, "Starting setElementQNameTest2");
			setElementQNameTest2(req, res);
		} else if (testname.equals("createQNameTest1")) {
			logger.log(Logger.Level.INFO, "Starting createQNameTest1");
			createQNameTest1(req, res);
		} else if (testname.equals("createQNameTest2")) {
			logger.log(Logger.Level.INFO, "Starting createQNameTest2");
			createQNameTest2(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}

	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		System.out.println("SOAPElementTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		System.out.println("SOAPElementTestServlet:init (Leaving)");
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

	private void addAttributeTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addAttributeTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyAttr1");
			String value = "MyValue1";
			logger.log(Logger.Level.INFO, "Add attribute name = " + name.getLocalName() + ", value = " + value);
			SOAPElement se = body.addAttribute(name, value);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addAttribute() did not return SOAPElement");
				pass = false;
			} else if (!body.getAttributeValue(name).equals(value)) {
				logger.log(Logger.Level.ERROR, "getAttribute() return wrong value, expected " + value + ", got "
						+ body.getAttributeValue(name));
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

	private void addAttributeTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addAttributeTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName name = new QName("MyAttr1");
			String value = "MyValue1";
			logger.log(Logger.Level.INFO, "Add attribute qname = " + name.getLocalPart() + ", value = " + value);
			SOAPElement se = body.addAttribute(name, value);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addAttribute() did not return SOAPElement");
				pass = false;
			} else if (body.getAttributeValue(name).equals(value)) {
				logger.log(Logger.Level.INFO, "getAttribute() did return expected value");
			} else {
				logger.log(Logger.Level.ERROR, "getAttribute() did not return expected value");
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

	private void getAttributeValueTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAttributeValueTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name1 = envelope.createName("MyAttr1");
			String value1 = "MyValue1";
			Name name2 = envelope.createName("MyAttr2");
			String value2 = "MyValue2";
			logger.log(Logger.Level.INFO, "Add attribute name = " + name1.getLocalName() + ", value = " + value1);
			body.addAttribute(name1, value1);
			logger.log(Logger.Level.INFO, "Add attribute name = " + name2.getLocalName() + ", value = " + value2);
			body.addAttribute(name2, value2);
			String s1 = body.getAttributeValue(name1);
			String s2 = body.getAttributeValue(name2);
			if (!s1.equals(value1) && !s2.equals(value2)) {
				logger.log(Logger.Level.ERROR, "getAttribute() return wrong value, expected " + value1 + ", got " + s1);
				logger.log(Logger.Level.ERROR, "getAttribute() return wrong value, expected " + value2 + ", got " + s2);
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

	private void getAttributeValueTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAttributeValueTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName name1 = new QName("MyAttr1");
			String value1 = "MyValue1";
			QName name2 = new QName("MyAttr2");
			String value2 = "MyValue2";
			logger.log(Logger.Level.INFO, "Add attribute qname = " + name1.getLocalPart() + ", value = " + value1);
			body.addAttribute(name1, value1);
			logger.log(Logger.Level.INFO, "Add attribute qname = " + name2.getLocalPart() + ", value = " + value2);
			body.addAttribute(name2, value2);
			String s1 = body.getAttributeValue(name1);
			String s2 = body.getAttributeValue(name2);
			if (s1.equals(value1) && s2.equals(value2))
				logger.log(Logger.Level.INFO, "getAttribute() test PASSED");
			else {
				logger.log(Logger.Level.ERROR, "getAttribute() test FAILED");
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

	private void getAllAttributesTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllAttributesTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name1 = envelope.createName("MyAttr1");
			String value1 = "MyValue1";
			Name name2 = envelope.createName("MyAttr2");
			String value2 = "MyValue2";
			Name name3 = envelope.createName("MyAttr3");
			String value3 = "MyValue3";
			logger.log(Logger.Level.INFO, "Add attribute name1 = " + name1.getLocalName() + ", value1 = " + value1);
			body.addAttribute(name1, value1);
			logger.log(Logger.Level.INFO, "Add attribute name2 = " + name2.getLocalName() + ", value2 = " + value2);
			body.addAttribute(name2, value2);
			logger.log(Logger.Level.INFO, "Add attribute name3 = " + name3.getLocalName() + ", value3 = " + value3);
			body.addAttribute(name3, value3);
			Iterator i = body.getAllAttributes();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getAllAttributes();
			if (count != 3) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 3");
				pass = false;
			} else {
				boolean foundName1 = false;
				boolean foundName2 = false;
				boolean foundName3 = false;
				while (i.hasNext()) {
					Name name = (Name) i.next();
					logger.log(Logger.Level.INFO, "Got Name = " + name.getLocalName());
					if (name.equals(name1))
						foundName1 = true;
					else if (name.equals(name2))
						foundName2 = true;
					else if (name.equals(name3))
						foundName3 = true;
					else {
						logger.log(Logger.Level.ERROR, "Wrong Name returned of " + name);
						pass = false;
					}
				}
				if (!foundName1 || !foundName2 || !foundName3) {
					logger.log(Logger.Level.ERROR, "Did not find all names of MyAttr1, " + "MyAttr2, MyAttr3");
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

	private void removeAttributeTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAttributeTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyAttr1");
			String value = "MyValue1";
			logger.log(Logger.Level.INFO, "Add attribute name = " + name.getLocalName() + ", value = " + value);
			body.addAttribute(name, value);
			logger.log(Logger.Level.INFO, "Remove attribute name = " + name.getLocalName() + ", value = " + value);
			boolean b = body.removeAttribute(name);
			if (!b) {
				logger.log(Logger.Level.ERROR, "removeAttribute() did not return true");
				pass = false;
			}
			if (pass) {
				b = body.removeAttribute(name);
				if (b) {
					logger.log(Logger.Level.ERROR, "removeAttribute() did not return false");
					pass = false;
				}
			}
			if (pass) {
				String s = body.getAttributeValue(name);
				if (s != null) {
					logger.log(Logger.Level.ERROR,
							"getAttributeValue() returned wrong value," + " expected null, got \"" + s + "\"");
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

	private void removeAttributeTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAttributeTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName name = new QName("MyAttr1");
			String value = "MyValue1";
			logger.log(Logger.Level.INFO, "Add attribute qname = " + name.getLocalPart() + ", value = " + value);
			body.addAttribute(name, value);
			logger.log(Logger.Level.INFO, "Remove attribute qname = " + name.getLocalPart() + ", value = " + value);
			boolean b = body.removeAttribute(name);
			if (!b) {
				logger.log(Logger.Level.ERROR, "removeAttribute() did not return true");
				logger.log(Logger.Level.ERROR, "removeAttributeTest() test FAILED");
				pass = false;
			}
			if (pass) {
				b = body.removeAttribute(name);
				if (b) {
					logger.log(Logger.Level.ERROR, "removeAttribute() did not return false");
					logger.log(Logger.Level.ERROR, "removeAttributeTest() test FAILED");
					pass = false;
				}
			}
			if (pass) {
				if (body.getAttributeValue(name) == null) {
					logger.log(Logger.Level.INFO, "removeAttributeTest() test PASSED");
				} else {
					logger.log(Logger.Level.ERROR, "removeAttributeTest() test FAILED");
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

	private void getElementNameTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getElementNameTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Get element name of SOAPBody = " + body.getElementName().getLocalName());
			logger.log(Logger.Level.INFO,
					"Get element name of SOAPEnvelope = " + envelope.getElementName().getLocalName());
			logger.log(Logger.Level.INFO, "Get element name of SOAPHeader = " + hdr.getElementName().getLocalName());
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

	private void getElementQNameTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getElementQNameTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Get element qname of SOAPBody = " + body.getElementQName().getLocalPart());
			logger.log(Logger.Level.INFO,
					"Get element qname of SOAPEnvelope = " + envelope.getElementQName().getLocalPart());
			logger.log(Logger.Level.INFO, "Get element qname of SOAPHeader = " + hdr.getElementQName().getLocalPart());
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

	private void addNamespaceDeclarationTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addNamespaceDeclarationTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String prefix = "myPrefix";
			String uri = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + prefix + ", uri=" + uri);
			SOAPElement se = body.addNamespaceDeclaration(prefix, uri);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addNamespaceDeclaration() did not return SOAPElement");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "addNamespaceDeclaration() did return SOAPElement");
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

	private void removeNamespaceDeclarationTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeNamespaceDeclarationTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String prefix = "myPrefix";
			String uri = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + prefix + ", uri=" + uri);
			body.addNamespaceDeclaration(prefix, uri);
			logger.log(Logger.Level.INFO, "Remove namespace declaration: prefix=" + prefix + ", uri=" + uri);
			boolean b = body.removeNamespaceDeclaration(prefix);
			if (!b) {
				logger.log(Logger.Level.ERROR, "removeNamespaceDeclaration() did not return true");
				pass = false;
			}
			if (pass) {
				b = body.removeNamespaceDeclaration(prefix);
				if (b) {
					logger.log(Logger.Level.ERROR, "removeNamespaceDeclaration() did not return false");
					pass = false;
				}
			}
			if (pass) {
				logger.log(Logger.Level.INFO, "Try and get URI of removed namespace: uri=" + uri);
				if (body.getNamespaceURI(prefix) != null) {
					logger.log(Logger.Level.ERROR, "getNamespaceURI() returned wrong value, expected" + " null, got "
							+ body.getNamespaceURI(prefix));
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

	private void getNamespacePrefixesTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNamespacePrefixesTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String prefix = "myPrefix";
			String uri = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + prefix + ", uri=" + uri);
			body.addNamespaceDeclaration(prefix, uri);
			logger.log(Logger.Level.INFO, "Get the namespace prefixes");
			Iterator i = body.getNamespacePrefixes();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getNamespacePrefixes();
			if (count != 1) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
				pass = false;
			} else {
				boolean foundPrefix = false;
				while (i.hasNext()) {
					String s = (String) i.next();
					logger.log(Logger.Level.INFO, "Got Prefix = " + s);
					if (s.equals(prefix)) {
						foundPrefix = true;
						logger.log(Logger.Level.INFO, "Namespace prefix was found");
					} else {
						logger.log(Logger.Level.ERROR, "Wrong prefix returned of " + s);
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

	private void getNamespaceURITest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNamespaceURITest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String prefix = "myPrefix";
			String uri = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + prefix + ", uri=" + uri);
			body.addNamespaceDeclaration(prefix, uri);
			logger.log(Logger.Level.INFO, "Get the namespace URI");
			String s = body.getNamespaceURI(prefix);
			logger.log(Logger.Level.INFO, "The namespace URI = " + s);
			if (s.equals(uri))
				logger.log(Logger.Level.INFO, "The namespace URI is correct");
			else {
				logger.log(Logger.Level.ERROR, "The namespace URI is wrong, expected " + uri + ", got " + s);
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

	private void addTextNodeTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addTextNodeTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Add text node to SOAPBody " + "with text <txt>This is text</txt>");
			logger.log(Logger.Level.INFO, "For SOAP1.1 this must succeed");
			Iterator iStart = body.getChildElements();
			int countStart = SOAP_Util.getIteratorCount(iStart);
			SOAPElement se = body.addTextNode("<txt>This is text</txt>");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addTextNode() did not return SOAPElement");
				pass = false;
			} else if (!body.getValue().equals("<txt>This is text</txt>")) {
				String s = body.getValue();
				logger.log(Logger.Level.ERROR, "addTextNode() did not return expected text");
				logger.log(Logger.Level.ERROR, "Returned " + s + ", Expected <txt>" + "This is text</txt>");
				pass = false;
			}
			if (pass) {
				Iterator i = body.getChildElements();
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements();
				if (count != ++countStart) {
					logger.log(Logger.Level.ERROR,
							"Wrong iterator count returned of " + count + ", expected " + countStart);
					pass = false;
				} else {
					Object obj = null;
					while (i.hasNext()) {
						obj = i.next();
						if (obj instanceof Text)
							break;
					}
					if (obj instanceof Text) {
						Text t = (Text) obj;
						logger.log(Logger.Level.INFO, "obj is instanceof Text");
						logger.log(Logger.Level.INFO, "Text isComment() = " + t.isComment());
					} else {
						logger.log(Logger.Level.ERROR, "obj is not instanceof Text");
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

	private void addTextNodeSOAP11Test2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addTextNodeSOAP11Test2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Add text node to SOAPHeader " + "with text <txt>This is text</txt>");
			logger.log(Logger.Level.INFO, "This is legal for a SOAP1.1 message");
			Iterator iStart = hdr.getChildElements();
			int countStart = SOAP_Util.getIteratorCount(iStart);
			SOAPElement se = hdr.addTextNode("<txt>This is text</txt>");
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addTextNode() did not return SOAPElement");
				pass = false;
			} else if (!hdr.getValue().equals("<txt>This is text</txt>")) {
				String s = body.getValue();
				logger.log(Logger.Level.ERROR, "addTextNode() did not return expected text");
				logger.log(Logger.Level.ERROR, "Returned " + s + ", Expected <txt>" + "This is text</txt>");
				pass = false;
			}
			if (pass) {
				Iterator i = hdr.getChildElements();
				int count = SOAP_Util.getIteratorCount(i);
				i = hdr.getChildElements();
				if (count != ++countStart) {
					logger.log(Logger.Level.ERROR,
							"Wrong iterator count returned of " + count + ", expected " + countStart);
					pass = false;
				} else {
					Object obj = null;
					while (i.hasNext()) {
						obj = i.next();
						if (obj instanceof Text)
							break;
					}
					if (obj instanceof Text) {
						Text t = (Text) obj;
						logger.log(Logger.Level.INFO, "obj is instanceof Text");
						logger.log(Logger.Level.INFO, "Text isComment() = " + t.isComment());
					} else {
						logger.log(Logger.Level.ERROR, "obj is not instanceof Text");
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

	private void addTextNodeSOAP12Test2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addTextNodeSOAP12Test2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Add text node to SOAPHeader " + "with text <txt>This is text</txt>");
			Iterator iStart = hdr.getChildElements();
			SOAP_Util.getIteratorCount(iStart);
			logger.log(Logger.Level.INFO, "This is illegal for a SOAP1.2 message (expect SOAPException)");
			hdr.addTextNode("<txt>This is text</txt>");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
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

	private void setEncodingStyleSOAP11Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setEncodingStyleSOAP11Test1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Set encoding style on body to URI_NS_SOAP_ENCODING");
			logger.log(Logger.Level.INFO, "For SOAP1.1 this is allowed and should succeed");
			body.setEncodingStyle(SOAPConstants.URI_NS_SOAP_ENCODING);
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

	private void setEncodingStyleSOAP12Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setEncodingStyleSOAP12Test1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Set encoding style on body to URI_NS_SOAP_1_2_ENCODING");
			logger.log(Logger.Level.INFO, "For SOAP1.2 this is not allowed and must throw a SOAPException");
			body.setEncodingStyle(SOAPConstants.URI_NS_SOAP_1_2_ENCODING);
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
			pass = true;
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

	private void getEncodingStyleSOAP11Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getEncodingStyleSOAP11Test1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Set encoding style on body to URI_NS_SOAP_ENCODING");
			body.setEncodingStyle(SOAPConstants.URI_NS_SOAP_ENCODING);
			logger.log(Logger.Level.INFO,
					"Get encoding style from body (should return " + "URI_NS_SOAP_ENCODING) value");
			String s = body.getEncodingStyle();
			if (!s.equals(SOAPConstants.URI_NS_SOAP_ENCODING)) {
				TestUtil.logErr("getEncodingStyle() returned wrong value, " + "expected: "
						+ SOAPConstants.URI_NS_SOAP_ENCODING + ", got: " + s);
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

	private void getEncodingStyleSOAP12Test1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getEncodingStyleSOAP12Test1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Get encoding style from body (should return null) value");
			String s = body.getEncodingStyle();
			if (s != null) {
				logger.log(Logger.Level.ERROR,
						"getEncodingStyle() returned wrong value, " + "expected: null, got: " + s);
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

	private void addChildElementTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyChild1");
			logger.log(Logger.Level.INFO, "Add child element Name object with localName=MyChild1");
			logger.log(Logger.Level.INFO, "Calling addChildElement(Name)");
			SOAPElement se = body.addChildElement(name);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Find the child element just added");
				Iterator i = body.getChildElements(name);
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements(name);
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
				}
				if (pass) {
					logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
					Name n = se.getElementName();
					logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix() + " URI="
							+ n.getURI() + " qualifiedName=" + n.getQualifiedName());
					if (!n.equals(name)) {
						logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return " + "correct Name object expected localName="
										+ name.getLocalName() + ", got localName=" + n.getLocalName());
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void addChildElementTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String s = "Mychild1";
			logger.log(Logger.Level.INFO, "Add child element String object with localName=" + s);
			logger.log(Logger.Level.INFO, "Calling addChildElement(String localName)");
			SOAPElement se = body.addChildElement(s);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				pass = false;
			} else {
				Iterator i = body.getChildElements();
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements();
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
				}
				if (pass) {
					logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
					Name n = se.getElementName();
					logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix() + " URI="
							+ n.getURI() + " qualifiedName=" + n.getQualifiedName());
					if (!n.getLocalName().equals(s)) {
						logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return " + "correct Name object expected localName=" + s
										+ ", got localName=" + n.getLocalName());
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void addChildElementTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String s = "MyName1";
			String p = "MyPrefix1";
			String u = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + p + ", uri=" + u);
			SOAPElement myse = body.addNamespaceDeclaration(p, u);
			logger.log(Logger.Level.INFO, "SOAPElementName = " + myse.getElementName().getLocalName());
			logger.log(Logger.Level.INFO, "Add child element with localName=" + s + ", prefix=" + p);
			logger.log(Logger.Level.INFO, "Calling addChildElement(String localName, String prefix)");
			SOAPElement se = body.addChildElement(s, p);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPElementName = " + se.getElementName().getLocalName());
				Iterator i = body.getChildElements();
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements();
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
				}
				if (pass) {
					logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
					String name = se.getElementName().getLocalName();
					Name n = se.getElementName();
					logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix() + " URI="
							+ n.getURI() + " qualifiedName=" + n.getQualifiedName());
					String prefix = se.getElementName().getPrefix();
					if (!name.equals(s) || !prefix.equals(p)) {
						logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return" + " correct localName and prefix");
						logger.log(Logger.Level.ERROR, "Expected localName=" + s + " prefix=" + p + ", got localName="
								+ name + " prefix=" + prefix);
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void addChildElementTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String s = "MyName1";
			String p = "MyPrefix1";
			String u = "MyURI1";
			logger.log(Logger.Level.INFO, "Add child element localName=" + s + ", prefix=" + p + ", URI=" + u);
			logger.log(Logger.Level.INFO, "Calling addChildElement(String localName, " + "String prefix, String URI)");
			SOAPElement se = body.addChildElement(s, p, u);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				pass = false;
			} else {
				Iterator i = body.getChildElements();
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements();
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
				}
				if (pass) {
					String name = se.getElementName().getLocalName();
					String prefix = se.getElementName().getPrefix();
					String uri = se.getElementName().getURI();
					logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
					Name n = se.getElementName();
					logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix() + " URI="
							+ n.getURI() + " qualifiedName=" + n.getQualifiedName());
					if (!name.equals(s) || !prefix.equals(p) || !uri.equals(u)) {
						logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return" + " correct localName, prefix, and uri");
						logger.log(Logger.Level.ERROR, "Expected localName=" + s + " prefix=" + p + ", got localName="
								+ name + " prefix=" + prefix + " uri=" + uri);
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void addChildElementTest5(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest5");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			SOAPFactory sf = SOAP_Util.getSOAPFactory();
			String s = "MyName1";
			String p = "MyPrefix1";
			String u = "MyUri1";
			logger.log(Logger.Level.INFO,
					"Create Name object with localName=MyName1, " + "prefix=MyPrefix1, uri=MyUri1");
			Name name1 = envelope.createName("MyName1", "MyPrefix1", "MyUri1");
			logger.log(Logger.Level.INFO, "Create SOAPElement object with Name object above");
			SOAPElement myse = sf.createElement(name1);
			logger.log(Logger.Level.INFO, "Add SOAPElement object to SOAPBody");
			logger.log(Logger.Level.INFO, "Calling addChildElement(SOAPElement)");
			SOAPElement se = body.addChildElement(myse);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				pass = false;
			} else {
				Iterator i = body.getChildElements(name1);
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements(name1);
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
				}
				if (pass) {
					String name = se.getElementName().getLocalName();
					String prefix = se.getElementName().getPrefix();
					String uri = se.getElementName().getURI();
					logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
					Name n = se.getElementName();
					logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix() + " URI="
							+ n.getURI() + " qualifiedName=" + n.getQualifiedName());
					if (!name.equals(s) || !prefix.equals(p) || !uri.equals(u)) {
						logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return" + " correct localName, prefix, and uri");
						logger.log(Logger.Level.ERROR, "Expected localName=" + s + " prefix=" + p + ", got localName="
								+ name + " prefix=" + prefix + " uri=" + uri);
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void addChildElementTest6(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addChildElementTest6");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName name = new QName("MyChild1");
			logger.log(Logger.Level.INFO, "Add child element QName object = " + name.toString());
			SOAPElement se = body.addChildElement(name);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				logger.log(Logger.Level.ERROR, "addChildElementTest6() test FAILED");
				pass = false;
			} else {
				Iterator i = body.getChildElements(name);
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements(name);
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					logger.log(Logger.Level.ERROR, "addChildElementTest6() test FAILED");
					pass = false;
				} else {
					SOAPElement se2 = (SOAPElement) i.next();
					if (!se.equals(se2)) {
						logger.log(Logger.Level.ERROR, "addChildElementTest6() test FAILED");
						pass = false;
					}
				}
				if (pass) {
					QName n = se.getElementQName();
					if (!n.equals(name)) {
						logger.log(Logger.Level.ERROR,
								"addChildElement() did not return " + "correct qname object expected localpart="
										+ name.getLocalPart() + ", got localpart=" + n.getLocalPart());
						logger.log(Logger.Level.ERROR, "addChildElementTest6() test FAILED");
						pass = false;
					} else
						logger.log(Logger.Level.INFO, "addChildElementTest6() test PASSED");
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

	private void getChildElementsTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getChildElementsTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyChild1");
			logger.log(Logger.Level.INFO, "Add child element name = " + name.getLocalName());
			SOAPElement se = body.addChildElement(name);
			Iterator i = body.getChildElements();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getChildElements();
			if (count != 1) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
				pass = false;
			} else {
				SOAPElement se2 = (SOAPElement) i.next();
				if (!se.equals(se2)) {
					logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
			}
			if (pass) {
				logger.log(Logger.Level.INFO, "Retrieve the SOAPElement Name");
				Name n = se.getElementName();
				if (!n.equals(name)) {
					logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
					logger.log(Logger.Level.ERROR,
							"getChildElement() did not return " + "correct Name object expected localName="
									+ name.getLocalName() + ", got localName=" + n.getLocalName());
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void getChildElementsTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getChildElementsTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyChild1");
			logger.log(Logger.Level.INFO, "Add child element name = " + name.getLocalName());
			SOAPElement se = body.addChildElement(name);
			Iterator i = body.getChildElements(name);
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getChildElements(name);
			if (count != 1) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
				pass = false;
			} else {
				SOAPElement se2 = (SOAPElement) i.next();
				if (!se.equals(se2)) {
					logger.log(Logger.Level.ERROR, "SOAPElement se != se2 (unexpected)");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "SOAPElement se = se2 (expected)");
			}
			if (pass) {
				Name n = se.getElementName();
				logger.log(Logger.Level.INFO, "Name expected = " + name.toString());
				logger.log(Logger.Level.INFO, "Name got = " + n.toString());
				if (!n.equals(name)) {
					logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
					logger.log(Logger.Level.ERROR,
							"getChildElement() did not return " + "correct Name object expected localName="
									+ name.getLocalName() + ", got localName=" + n.getLocalName());
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void getChildElementsTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getChildElementsTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String s = "MyName1";
			String p = "MyPrefix1";
			String u = "MyURI1";
			String p2 = "MyPrefix2";
			String u2 = "MyURI2";
			logger.log(Logger.Level.INFO, "Add child element name = " + s + ", prefix = " + p + ", URI = " + u);
			SOAPElement se = body.addChildElement(s, p, u);
			logger.log(Logger.Level.INFO, "Add child element name = " + s + ", prefix = " + p2 + ", URI = " + u2);
			SOAPElement se2 = body.addChildElement(s, p2, u2);
			Iterator i = body.getChildElements();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getChildElements();
			if (count != 2) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 2");
				pass = false;
			}
			if (pass) {
				SOAPElement sei = (SOAPElement) i.next();
				SOAPElement sei2 = (SOAPElement) i.next();
				if (!se.equals(sei)) {
					logger.log(Logger.Level.ERROR, "SOAPElement se != sei (unexpected)");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "SOAPElement se = sei (expected)");
				if (!se2.equals(sei2)) {
					logger.log(Logger.Level.ERROR, "SOAPElement se2 != sei2 (unexpected)");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "SOAPElement se2 = sei2 (expected)");
					if (pass) {
						String name = se.getElementName().getLocalName();
						String prefix = se.getElementName().getPrefix();
						String uri = se.getElementName().getURI();
						String name2 = se2.getElementName().getLocalName();
						String prefix2 = se2.getElementName().getPrefix();
						String uri2 = se2.getElementName().getURI();
						Name n = se.getElementName();
						Name n2 = se2.getElementName();
						logger.log(Logger.Level.INFO, "localName=" + n.getLocalName() + " prefix=" + n.getPrefix()
								+ " URI=" + n.getURI() + " qualifiedName=" + n.getQualifiedName());
						logger.log(Logger.Level.INFO, "localName=" + n2.getLocalName() + " prefix=" + n2.getPrefix()
								+ " URI=" + n2.getURI() + " qualifiedName=" + n2.getQualifiedName());
						if (!name.equals(s) || !prefix.equals(p) || !uri.equals(u)) {
							logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
							logger.log(Logger.Level.ERROR,
									"getChildElement() did not return" + " correct localName, prefix, and uri");
							logger.log(Logger.Level.ERROR, "Expected localName=" + s + " prefix=" + p
									+ ", got localName=" + name + " prefix=" + prefix + " uri=" + uri);
							pass = false;
						} else if (!name2.equals(s) || !prefix2.equals(p2) || !uri2.equals(u2)) {
							logger.log(Logger.Level.INFO, "Name objects are not equal (unexpected)");
							logger.log(Logger.Level.ERROR,
									"getChildElement() did not return" + " correct localName2, prefix2, and uri2");
							logger.log(Logger.Level.ERROR, "Expected localName2=" + s + " prefix2=" + p2
									+ ", got localName2=" + name2 + " prefix2=" + prefix2 + " uri2=" + uri2);
							pass = false;
						} else
							logger.log(Logger.Level.INFO, "Name objects are equal (expected)");
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

	private void getChildElementsTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getChildElementsTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName name = new QName("MyChild1");
			logger.log(Logger.Level.INFO, "Add child element name = " + name.getLocalPart());
			SOAPElement se = body.addChildElement(name);
			Iterator i = body.getChildElements();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getChildElements();
			if (count != 1) {
				logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
				logger.log(Logger.Level.ERROR, "getChildElementsTest4() test FAILED");
				pass = false;
			} else {
				SOAPElement se2 = (SOAPElement) i.next();
				if (!se.equals(se2)) {
					logger.log(Logger.Level.ERROR, "getChildElementsTest4() test FAILED");
					pass = false;
				}
			}
			if (pass) {
				QName n = se.getElementQName();
				if (!n.equals(name)) {
					logger.log(Logger.Level.ERROR,
							"getChildElement() did not return " + "correct qname object expected localpart="
									+ name.getLocalPart() + ", got localpart=" + n.getLocalPart());
					logger.log(Logger.Level.ERROR, "getChildElementsTest4() test FAILED");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "getChildElementsTest4() test PASSED");
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

	private void removeContentsTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeContentsTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			Name name = envelope.createName("MyChild");
			logger.log(Logger.Level.INFO, "Add child element Name object = " + name.toString());
			SOAPElement se = body.addChildElement(name);
			if (se == null) {
				logger.log(Logger.Level.ERROR, "addChildElement() did not return SOAPElement");
				logger.log(Logger.Level.ERROR, "removeContentsTest() test FAILED");

				pass = false;
			} else {
				Iterator i = body.getChildElements(name);
				int count = SOAP_Util.getIteratorCount(i);
				i = body.getChildElements(name);
				if (count != 1) {
					logger.log(Logger.Level.ERROR, "Wrong iterator count returned of " + count + ", expected 1");
					logger.log(Logger.Level.ERROR, "removeContentsTest() test FAILED");
					pass = false;
				}

				if (pass) {
					Name n = se.getElementName();
					if (!n.equals(name)) {
						logger.log(Logger.Level.ERROR,
								"removeContentsTest() did not return " + "correct name object expected localname="
										+ name.getLocalName() + ", got localname=" + n.getLocalName());
						logger.log(Logger.Level.ERROR, "removeContentsTest() test FAILED");
						pass = false;
					}
				}
				logger.log(Logger.Level.INFO, "Child addition verified, now call removeContents to delete it");
				se.removeContents();
				i = se.getChildElements();
				count = SOAP_Util.getIteratorCount(i);
				if (count != 0) {
					logger.log(Logger.Level.ERROR,
							"Wrong iterator count returned of " + count + ", expected 0, after calling removeContents");
					logger.log(Logger.Level.ERROR, "removeContentsTest() test FAILED");
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

	private void getVisibleNamespacePrefixesTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getVisibleNamespacePrefixesTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			String prefix = "myPrefix";
			String uri = "myURI";
			logger.log(Logger.Level.INFO, "Add namespace declaration: prefix=" + prefix + ", uri=" + uri);
			body.addNamespaceDeclaration(prefix, uri);
			logger.log(Logger.Level.INFO, "Get the namespace prefixes");
			Iterator i = body.getVisibleNamespacePrefixes();
			int count = SOAP_Util.getIteratorCount(i);
			i = body.getVisibleNamespacePrefixes();
			logger.log(Logger.Level.INFO, "There are " + count + " visible namespace prefixes");
			boolean foundPrefix = false;
			while (i.hasNext()) {
				String s = (String) i.next();
				logger.log(Logger.Level.INFO, "Got Prefix = " + s);
				if (s.equals(prefix))
					foundPrefix = true;
			}
			if (!foundPrefix)
				logger.log(Logger.Level.INFO, "Did not find prefix " + prefix);
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

	private void setElementQNameTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setElementQNameTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName qname1 = new QName("http://fooURI.com", "fooElement", "foo");
			logger.log(Logger.Level.INFO, "Create QName1 of: " + qname1);
			QName qname2 = new QName("http://foo2URI.com", "fooElement2", "foo2");
			logger.log(Logger.Level.INFO, "Create QName2 of: " + qname2);
			logger.log(Logger.Level.INFO, "Add a child SOAPElement of: " + qname1);
			se = body.addChildElement(qname1);
			QName qname = se.getElementQName();
			logger.log(Logger.Level.INFO, "Get element qname of child SOAPElement: " + qname);
			SOAP_Util.dumpSOAPMessage(msg);
			logger.log(Logger.Level.INFO, "Reset element qname of child SOAPElement to: " + qname2);
			se = se.setElementQName(qname2);
			SOAP_Util.dumpSOAPMessage(msg);
			qname = se.getElementQName();
			logger.log(Logger.Level.INFO, "Get element qname of child SOAPElement again: " + qname);
			if (!qname.getNamespaceURI().equals(qname2.getNamespaceURI())
					|| !qname.getLocalPart().equals(qname2.getLocalPart())
					|| !qname.getPrefix().equals(qname2.getPrefix())) {
				logger.log(Logger.Level.ERROR,
						"setElementQName() did not reset " + "element qname\nexpected: <URI=" + qname2.getNamespaceURI()
								+ ", prefix=" + qname2.getPrefix() + ", localpart=" + qname2.getLocalPart()
								+ ">\ngot:      <URI=" + qname.getNamespaceURI() + ", prefix=" + qname.getPrefix()
								+ ", localpart=" + qname.getLocalPart() + ">");
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

	private void setElementQNameTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setElementQNameTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			QName qname = new QName("qname");
			logger.log(Logger.Level.INFO, "Create QName of: " + qname);
			logger.log(Logger.Level.INFO, "Try and change element name of SOAPEnvelope (expect SOAPException)");
			try {
				envelope.setElementQName(qname);
				logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Caught expected SOAPException");
			}
			logger.log(Logger.Level.INFO, "Try and change element name of SOAPHeader (expect SOAPException)");
			try {
				hdr.setElementQName(qname);
				logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
				pass = false;
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "Caught expected SOAPException");
			}
			logger.log(Logger.Level.INFO, "Try and change element name of SOAPBody (expect SOAPException)");
			try {
				body.setElementQName(qname);
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

	private void createQNameTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createQNameTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create QName from SOAPEnvelope");
			QName qname = envelope.createQName("qname", eprefix);
			logger.log(Logger.Level.INFO, "Create QName of: " + qname);
			String tprefix = qname.getPrefix();
			String turi = qname.getNamespaceURI();
			String tname = qname.getLocalPart();
			logger.log(Logger.Level.INFO, "qname prefix=" + tprefix);
			logger.log(Logger.Level.INFO, "qname uri=" + turi);
			logger.log(Logger.Level.INFO, "qname localpart=" + tname);
			logger.log(Logger.Level.INFO, "Verify correct uri and prefix");
			if (!tprefix.equals(eprefix) || !turi.equals(euri)) {
				logger.log(Logger.Level.ERROR,
						"createQName() did not create correct qname\n" + "expected: <uri=" + euri + ", prefix="
								+ eprefix + ", localpart=qname>\n" + "got:      <uri=" + turi + ", prefix=" + tprefix
								+ ", localpart=" + tname + ">");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Create QName from SOAPBody");
			qname = body.createQName("qname", bprefix);
			logger.log(Logger.Level.INFO, "Create QName of: " + qname);
			tprefix = qname.getPrefix();
			turi = qname.getNamespaceURI();
			tname = qname.getLocalPart();
			logger.log(Logger.Level.INFO, "qname prefix=" + tprefix);
			logger.log(Logger.Level.INFO, "qname uri=" + turi);
			logger.log(Logger.Level.INFO, "qname localpart=" + tname);
			logger.log(Logger.Level.INFO, "Verify correct uri and prefix");
			if (!tprefix.equals(bprefix) || !turi.equals(buri)) {
				logger.log(Logger.Level.ERROR,
						"createQName() did not create correct qname\n" + "expected: <uri=" + buri + ", prefix="
								+ bprefix + ", localpart=qname>\n" + "got:      <uri=" + turi + ", prefix=" + tprefix
								+ ", localpart=" + tname + ">");
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

	private void createQNameTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "createQNameTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Create QName from SOAPBody with bogus prefix (expect SOAPException)");
			QName qname = body.createQName("qname", "bogus");
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
}
