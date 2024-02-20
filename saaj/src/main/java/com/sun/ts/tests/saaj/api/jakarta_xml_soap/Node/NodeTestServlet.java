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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class NodeTestServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(NodeTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private SOAPPart sp = null;

	private SOAPEnvelope envelope = null;

	private SOAPHeader hdr = null;

	private SOAPBody bdy = null;

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

		// Retrieve the soap body from the envelope.
		logger.log(Logger.Level.INFO, "Get SOAP Body");
		bdy = envelope.getBody();

		logger.log(Logger.Level.INFO, "Creating SOAPHeaderElement");
		she = hdr.addHeaderElement(envelope.createName("foo", "f", "foo-URI"));

		if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
			logger.log(Logger.Level.INFO, "Set the actor associated with SOAPHeaderElement");
			she.setActor("actor-URI");
		} else {
			logger.log(Logger.Level.INFO, "Set the role associated with SOAPHeaderElement");
			she.setRole("role-URI");
		}

	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("detachNodeTest")) {
			logger.log(Logger.Level.INFO, "Starting detachNodeTest");
			detachNodeTest(req, res);
		} else if (testname.equals("getParentElementTest1")) {
			logger.log(Logger.Level.INFO, "Starting getParentElementTest1");
			getParentElementTest1(req, res);
		} else if (testname.equals("getParentElementTest2")) {
			logger.log(Logger.Level.INFO, "Starting getParentElementTest2");
			getParentElementTest2(req, res);
		} else if (testname.equals("setParentElementTest1")) {
			logger.log(Logger.Level.INFO, "Starting setParentElementTest1");
			setParentElementTest1(req, res);
		} else if (testname.equals("setParentElementTest2")) {
			logger.log(Logger.Level.INFO, "Starting setParentElementTest2");
			setParentElementTest2(req, res);
		} else if (testname.equals("setParentElementTest3")) {
			logger.log(Logger.Level.INFO, "Starting setParentElementTest3");
			setParentElementTest3(req, res);
		} else if (testname.equals("recycleNodeTest")) {
			logger.log(Logger.Level.INFO, "Starting recycleNodeTest");
			recycleNodeTest(req, res);
		} else if (testname.equals("getValueTest1")) {
			logger.log(Logger.Level.INFO, "Starting getValueTest1");
			getValueTest1(req, res);
		} else if (testname.equals("getValueTest2")) {
			logger.log(Logger.Level.INFO, "Starting getValueTest2");
			getValueTest2(req, res);
		} else if (testname.equals("setValueTest1")) {
			logger.log(Logger.Level.INFO, "Starting setValueTest1");
			setValueTest1(req, res);
		} else if (testname.equals("setValueTest2")) {
			logger.log(Logger.Level.INFO, "Starting setValueTest2");
			setValueTest2(req, res);
		} else if (testname.equals("setValueTest3")) {
			logger.log(Logger.Level.INFO, "Starting setValueTest3");
			setValueTest3(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"NodeTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		logger.log(Logger.Level.TRACE,"NodeTestServlet:init (Leaving)");
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

	private void detachNodeTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "detachNodeTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Detach SOAPHeaderElement Node ...");
			she.detachNode();

			logger.log(Logger.Level.INFO, "Validate SOAPHeaderElement Node was detached ...");

			// Examine the soap header element from the SOAPHeader.
			Iterator iterator = null;
			if (SOAP_Util.getSOAPVersion().equals(SOAP_Util.SOAP11)) {
				logger.log(Logger.Level.INFO, "Examine SOAPHeaderElements with actor actor-URI");
				iterator = hdr.examineHeaderElements("actor-URI");
			} else {
				logger.log(Logger.Level.INFO, "Examine SOAPHeaderElements with role role-URI");
				iterator = hdr.examineHeaderElements("role-URI");
			}
			if (iterator.hasNext()) {
				logger.log(Logger.Level.ERROR, "SOAPHeader element is not detached - unexpected");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "SOAPHeader element is detached - expected");
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

	private void recycleNodeTest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "recycleNodeTest");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Detach SOAPHeaderElement Node ...");
			she.detachNode();

			logger.log(Logger.Level.INFO, "Recycle Node that was just detached ...");
			she.recycleNode();

			logger.log(Logger.Level.INFO, "Node has been recycled ...");
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

	private void getParentElementTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getParentElementTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Get Parent Element of SOAPHeaderElement ...");
			SOAPElement se = she.getParentElement();

			logger.log(Logger.Level.INFO, "Validate SOAPElement Node is the SOAPHeader ...");
			if (!(se instanceof SOAPHeader)) {
				logger.log(Logger.Level.ERROR, "SOAPHeader element not returned - unexpected");
				pass = false;
			} else {
				SOAPHeader sh = (SOAPHeader) se;
				if (!sh.equals(hdr)) {
					logger.log(Logger.Level.ERROR, "SOAPHeader element does not match");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "SOAPHeader element does match");
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

	private void getParentElementTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getParentElementTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Detach SOAPHeaderElement Node ...");
			she.detachNode();

			logger.log(Logger.Level.INFO, "Get Parent Element of SOAPHeaderElement ...");
			SOAPElement se = she.getParentElement();

			logger.log(Logger.Level.INFO, "Validate Parent Element is null ...");
			if (se != null) {
				logger.log(Logger.Level.ERROR, "Parent Element is not null - unexpected");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Parent Element is null - expected");
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

	private void setParentElementTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setParentElementTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Detach SOAPHeaderElement Node ...");
			she.detachNode();

			logger.log(Logger.Level.INFO, "Set Parent Element of SOAPHeaderElement ...");
			she.setParentElement(hdr);

			logger.log(Logger.Level.INFO, "Get Parent Element of SOAPHeaderElement ...");
			SOAPElement se = she.getParentElement();

			logger.log(Logger.Level.INFO, "Validate Parent Element is set ...");
			if (!(se instanceof SOAPHeader)) {
				logger.log(Logger.Level.ERROR, "SOAPHeader element not returned - unexpected");
				pass = false;
			} else {
				SOAPHeader sh = (SOAPHeader) se;
				if (!sh.equals(hdr)) {
					logger.log(Logger.Level.ERROR, "SOAPHeader element does not match");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "SOAPHeader element does match");
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

	private void setParentElementTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setParentElementTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Attempt to set parent element of SOAPHeaderElement to a body parent ...");
			try {
				she.setParentElement(bdy);
				logger.log(Logger.Level.ERROR, "no exception occurred, unexpected ...");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "IllegalArgumentException occurred");
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "SOAPException occurred");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "no IllegalArgumentException or SOAPException, received exception " + e);
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

	private void setParentElementTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setParentElementTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Attempt to set parent element of SOAPHeaderElement to a null parent ...");
			try {
				she.setParentElement(null);
				logger.log(Logger.Level.ERROR, "no exception occurred, unexpected ...");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "IllegalArgumentException occurred");
			} catch (SOAPException e) {
				logger.log(Logger.Level.INFO, "SOAPException occurred");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "no IllegalArgumentException or SOAPException, received exception " + e);
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

	private void getValueTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getValueTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "adding child element to SOAPHeaderElement ...");
			she.addChildElement("foo-bar");

			logger.log(Logger.Level.INFO, "get value of non-existant child Text Node should return null ...");
			String value = she.getValue();
			if (value != null) {
				logger.log(Logger.Level.ERROR, "value is not null - unexpected");
				logger.log(Logger.Level.ERROR, "value=" + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value is null - expected");

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

	private void getValueTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getValueTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "adding Text Node \"foo-bar\" to SOAPHeaderElement ...");
			she.addTextNode("foo-bar");
			logger.log(Logger.Level.INFO,
					"get value of existant child Text Node should return the text nodes value (foo-bar) ...");
			String value = she.getValue();
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null - unexpected");
				pass = false;
			} else if (!value.equals("foo-bar")) {
				logger.log(Logger.Level.ERROR, "value incorrect - expected: " + "foo-bar" + ", received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value returned is correct: " + value);
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

	private void setValueTest1(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setValueTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Add a Text Node to SOAPHeaderElement initialized to \"foo-bar\"");
			she.addTextNode("foo-bar");
			logger.log(Logger.Level.INFO, "Getting value of SOAPHeaderElement should return \"foo-bar\"");
			String value = she.getValue();
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null - unexpected");
				pass = false;
			} else if (!value.equals("foo-bar")) {
				logger.log(Logger.Level.ERROR, "value incorrect - expected: " + "foo-bar" + ", received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value returned is correct: " + value);
			logger.log(Logger.Level.INFO, "Resetting value of SOAPHeaderElement to \"foo-bar2\"");
			she.setValue("foo-bar2");
			logger.log(Logger.Level.INFO, "Getting value of SOAPHeaderElement should return \"foo-bar2\"");
			value = she.getValue();
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null - unexpected");
				pass = false;
			} else if (!value.equals("foo-bar2")) {
				logger.log(Logger.Level.ERROR, "value incorrect - expected: " + "foo-bar2" + ", received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value returned is correct: " + value);
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

	private void setValueTest2(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setValueTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding child element \"child1\" to SOAPHeaderElement");
			SOAPElement se = she.addChildElement("child1");
			logger.log(Logger.Level.INFO,
					"Add a Text Node to child element \"child1\"" + " initialized to \"foo-bar\"");
			se.addTextNode("foo-bar");
			logger.log(Logger.Level.INFO, "Getting value of child element should be \"foo-bar\"");
			String value = se.getValue();
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null - unexpected");
				pass = false;
			} else if (!value.equals("foo-bar")) {
				logger.log(Logger.Level.ERROR, "value incorrect - expected: " + "foo-bar" + ", received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value returned is correct: " + value);
			logger.log(Logger.Level.INFO, "Resetting value of child element to \"foo-bar2\"");
			se.setValue("foo-bar2");
			logger.log(Logger.Level.INFO, "Getting value of child element should return \"foo-bar2\"");
			value = se.getValue();
			if (value == null) {
				logger.log(Logger.Level.ERROR, "value is null - unexpected");
				pass = false;
			} else if (!value.equals("foo-bar2")) {
				logger.log(Logger.Level.ERROR, "value incorrect - expected: " + "foo-bar2" + ", received: " + value);
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "value returned is correct: " + value);
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

	private void setValueTest3(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setValueTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Try setting a value on SOAPHeaderElement which is not a Text Node");
			logger.log(Logger.Level.INFO, "This should throw an IllegalStateException");
			she.setValue("foo-bar");
			logger.log(Logger.Level.INFO, "Adding a child element \"child1\" to SOAPHeaderElement");
			SOAPElement se = she.addChildElement("child1");
			logger.log(Logger.Level.INFO, "Try setting a value on a Node that is not a Text Node");
			she.setValue("foo-bar");
			logger.log(Logger.Level.ERROR, "Did not throw expected IllegalStateException");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.INFO, "Caught expected IllegalStateException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}

		try {
			logger.log(Logger.Level.INFO, "Adding a child element \"child1\" to SOAPHeaderElement");
			SOAPElement se = she.addChildElement("child1");
			logger.log(Logger.Level.INFO, "Try setting a value on a Node that is not a Text Node");
			logger.log(Logger.Level.INFO, "Try setting a value on child element which is not a Text Node");
			logger.log(Logger.Level.INFO, "This should throw an IllegalStateException");
			se.setValue("foo-bar");
			logger.log(Logger.Level.ERROR, "Did not throw expected IllegalStateException");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.INFO, "Caught expected IllegalStateException");
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
