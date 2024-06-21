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

package com.sun.ts.tests.saaj.ee.SendVariousMimeAttachments;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SendingServlet extends HttpServlet {

	private static final Logger logger = (Logger) System.getLogger(SendingServlet.class.getName());

	private SOAPConnection con = null;

	private Properties harnessProps = null;

	private boolean debug = false;

	private String hostname = "localhost";

	private int portnum = 8080;

	private static final String cntxroot = "/SendVariousMimeAttachments_web";

	private static final String SOAPVERSION = "SOAPVERSION";

	private static final String NS_PREFIX = "ns-prefix";

	private static final String NS_URI = "ns-uri";

	private static final String PROTOCOL = "http";

	private static final String SERVLET1 = cntxroot + "/ReceivingSOAP11Servlet";

	private static final String SERVLET2 = cntxroot + "/ReceivingSOAP12Servlet";

	private String soapVersion = null;

	private TSURL tsurl = new TSURL();

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"SendingServlet:init (Entering)");
		try {
			SOAP_Util.setup();
			con = SOAP_Util.getSOAPConnection();
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace(System.err);
			throw new ServletException("Exception occurred: " + e.getMessage());
		}
		logger.log(Logger.Level.TRACE,"SendingServlet:init (Leaving)");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SendingServlet:doGet");
		logger.log(Logger.Level.TRACE,"SendingServlet:doGet");
		if (harnessProps.getProperty("TESTNAME").equals("SendVariousMimeAttachmentsTest")) {
			logger.log(Logger.Level.INFO, "Starting SendVariousMimeAttachmentsTest");
			logger.log(Logger.Level.TRACE,"Starting SendVariousMimeAttachmentsTest");
			SendVariousMimeAttachmentsTest(req, res);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SendingServlet:doPost");
		logger.log(Logger.Level.TRACE,"SendingServlet:doPost");
		SOAP_Util.doServletPost(req, res);
		hostname = SOAP_Util.getHostname();
		portnum = SOAP_Util.getPortnum();
		soapVersion = SOAP_Util.getSOAPVersion();
		harnessProps = SOAP_Util.getHarnessProps();
		doGet(req, res);
	}

	private void SendVariousMimeAttachmentsTest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Properties resultProps = new Properties();
		boolean pass = true;
		MessageFactory mf = null;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			// Check if SOAPConnectionFactory is supported
			logger.log(Logger.Level.INFO, "Check if SOAPConnectionFactory is supported");
			if (!SOAP_Util.isSOAPConnectionFactorySupported()) {
				logger.log(Logger.Level.INFO,
						"SOAPConnectionFactory.newInstance() is " + "unsupported (skipping test)");
				resultProps.setProperty("TESTRESULT", "pass");
				resultProps.list(out);
				return;
			}

			logger.log(Logger.Level.INFO, "Create SOAP message from message factory");
			SOAPMessage msg = SOAP_Util.getMessageFactory().createMessage();

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

			// Add some soap header elements
			logger.log(Logger.Level.INFO, "Add SOAP HeaderElement Header1");
			SOAPElement se = hdr.addHeaderElement(envelope.createName("Header1", NS_PREFIX, NS_URI))
					.addTextNode("This is Header1");
			SOAPHeaderElement she = (SOAPHeaderElement) se;
			she.setMustUnderstand(true);

			logger.log(Logger.Level.INFO, "Add SOAP HeaderElement Header2");
			se = hdr.addHeaderElement(envelope.createName("Header2", NS_PREFIX, NS_URI)).addTextNode("This is Header2");
			she = (SOAPHeaderElement) se;
			she.setMustUnderstand(false);

			logger.log(Logger.Level.INFO, "Add SOAP HeaderElement Header3");
			se = hdr.addHeaderElement(envelope.createName("Header3", NS_PREFIX, NS_URI)).addTextNode("This is Header3");
			she = (SOAPHeaderElement) se;
			she.setMustUnderstand(true);

			logger.log(Logger.Level.INFO, "Add SOAP HeaderElement Header4");
			se = hdr.addHeaderElement(envelope.createName("Header4", NS_PREFIX, NS_URI)).addTextNode("This is Header4");
			she = (SOAPHeaderElement) se;
			she.setMustUnderstand(false);

			// Add a soap body element
			logger.log(Logger.Level.INFO, "Add SOAP BodyElement Body1");
			SOAPBodyElement sbe = bdy.addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

			// Add a some child elements
			logger.log(Logger.Level.INFO, "Add ChildElement Child1");
			sbe.addChildElement(envelope.createName("Child1", NS_PREFIX, NS_URI)).addTextNode("This is Child1");
			logger.log(Logger.Level.INFO, "Add ChildElement Child2");
			sbe.addChildElement(envelope.createName("Child2", NS_PREFIX, NS_URI)).addTextNode("This is Child2");

			logger.log(Logger.Level.INFO, "Add various mime type attachments to SOAP message");
			URL url1 = tsurl.getURL(PROTOCOL, hostname, portnum, cntxroot + "/attach.xml");
			URL url2 = tsurl.getURL(PROTOCOL, hostname, portnum, cntxroot + "/attach.gif");
			URL url3 = tsurl.getURL(PROTOCOL, hostname, portnum, cntxroot + "/attach.txt");
			URL url4 = tsurl.getURL(PROTOCOL, hostname, portnum, cntxroot + "/attach.html");
			URL url5 = tsurl.getURL(PROTOCOL, hostname, portnum, cntxroot + "/attach.jpeg");

			logger.log(Logger.Level.INFO, "Create SOAP Attachment (XML document)");
			logger.log(Logger.Level.INFO, "URL1=" + url1);
			AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

			logger.log(Logger.Level.INFO, "Create SOAP Attachment (GIF image)");
			logger.log(Logger.Level.INFO, "URL2=" + url2);
			AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

			logger.log(Logger.Level.INFO, "Create SOAP Attachment (Plain text)");
			logger.log(Logger.Level.INFO, "URL3=" + url3);
			AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

			logger.log(Logger.Level.INFO, "Create SOAP Attachment (HTML document)");
			logger.log(Logger.Level.INFO, "URL4=" + url4);
			AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

			logger.log(Logger.Level.INFO, "Create SOAP Attachment (JPEG image)");
			logger.log(Logger.Level.INFO, "URL5=" + url5);
			AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

			ap1.setContentType("text/xml");
			ap2.setContentType("image/gif");
			ap3.setContentType("text/plain");
			ap4.setContentType("text/html");
			ap5.setContentType("image/jpeg");

			// Add the attachments to the message.
			logger.log(Logger.Level.INFO, "Add SOAP Attachment (XML document) to SOAP message");
			msg.addAttachmentPart(ap1);
			logger.log(Logger.Level.INFO, "Add SOAP Attachment (GIF image) to SOAP message");
			msg.addAttachmentPart(ap2);
			logger.log(Logger.Level.INFO, "Add SOAP Attachment (Plain text) to SOAP message");
			msg.addAttachmentPart(ap3);
			logger.log(Logger.Level.INFO, "Add SOAP Attachment (HTML document) to SOAP message");
			msg.addAttachmentPart(ap4);
			logger.log(Logger.Level.INFO, "Add SOAP Attachment (JPEG image) to SOAP message");
			msg.addAttachmentPart(ap5);
			msg.saveChanges();
			logger.log(Logger.Level.INFO, "Done creating SOAP message");

			// Create a url endpoint for the recipient of the message.
			URL urlEndpoint = null;
			if (soapVersion == null || soapVersion.equals(SOAP_Util.SOAP11))
				urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET1);
			else
				urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET2);
			logger.log(Logger.Level.INFO, "URLEndpoint = " + urlEndpoint);

			// Send the message to the endpoint using the connection.
			logger.log(Logger.Level.INFO, "Send SOAP message with various MIME attachments");
			SOAPMessage replymsg = con.call(msg, urlEndpoint);
			logger.log(Logger.Level.INFO, "Message sent successfully (PASSED)");

			// Check if reply message
			logger.log(Logger.Level.INFO, "Check the reply message");
			if (ValidateReplyMessage(replymsg)) {
				logger.log(Logger.Level.INFO, "Reply message is correct (PASSED)");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Reply message is incorrect (FAILED)");
			}

			// Set status code to OK
			res.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "SendVariousMimeAttachmentsTest Exception: " + e);
			TestUtil.printStackTrace(e);
			System.err.println("SendVariousMimeAttachmentsTest Exception: " + e);
			e.printStackTrace(System.err);
			// Set status code to INTERNAL SERVER ERROR
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		logger.log(Logger.Level.INFO, "TESTRESULT=" + resultProps.getProperty("TESTRESULT"));
		resultProps.list(out);
	}

	private boolean ValidateReplyMessage(SOAPMessage msg) {
		try {
			boolean pass = true;
			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

			logger.log(Logger.Level.INFO, "Verify soap headers");
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			boolean foundHeader3 = false;
			boolean foundHeader4 = false;
			Iterator i = envelope.getHeader().examineAllHeaderElements();
			while (i.hasNext()) {
				SOAPElement se = (SOAPElement) i.next();
				Name name = se.getElementName();
				String value = se.getValue();
				if (value == null || name == null)
					continue;
				else if (value.equals("This is Header1") && name.getLocalName().equals("Header1"))
					foundHeader1 = true;
				else if (value.equals("This is Header2") && name.getLocalName().equals("Header2"))
					foundHeader2 = true;
				else if (value.equals("This is Header3") && name.getLocalName().equals("Header3"))
					foundHeader3 = true;
				else if (value.equals("This is Header4") && name.getLocalName().equals("Header4"))
					foundHeader4 = true;
			}
			if (!foundHeader1 || !foundHeader2 || !foundHeader3 || !foundHeader4) {
				logger.log(Logger.Level.ERROR, "Did not find expected soap headers in reply message");
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "Did find expected soap headers in reply message");
			logger.log(Logger.Level.INFO, "Verify soap body");
			boolean foundBody1 = false;
			boolean foundChild1 = false;
			boolean foundChild2 = false;
			SOAPBody bdy = envelope.getBody();
			i = bdy.getChildElements();
			while (i.hasNext()) {
				SOAPBodyElement sbe = (SOAPBodyElement) i.next();
				Name name = sbe.getElementName();
				if (name.getLocalName().equals("Body1"))
					foundBody1 = true;
				Iterator c = sbe.getChildElements();
				while (c.hasNext()) {
					SOAPElement se = (SOAPElement) c.next();
					name = se.getElementName();
					String value = se.getValue();
					if (value.equals("This is Child1") && name.getLocalName().equals("Child1"))
						foundChild1 = true;
					else if (value.equals("This is Child2") && name.getLocalName().equals("Child2"))
						foundChild2 = true;
				}
			}
			if (!foundBody1) {
				logger.log(Logger.Level.ERROR, "Did not find expected soap body in reply message");
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "Did find expected soap body in reply message");
			if (!foundChild1 || !foundChild2) {
				logger.log(Logger.Level.ERROR, "Did not find expected soap body " + "child elements in reply message");
				pass = false;
			} else
				logger.log(Logger.Level.INFO, "Did find expected soap body child " + "elements in reply message");
			logger.log(Logger.Level.INFO, "Verify attachments");
			int count = msg.countAttachments();
			if (count == 5) {
				logger.log(Logger.Level.INFO, "Got expected " + count + " attachments in reply message");
				i = msg.getAttachments();
				logger.log(Logger.Level.INFO, "Verify correct MIME types of attachments");
				boolean gifFound = false;
				boolean xmlFound = false;
				boolean textFound = false;
				boolean htmlFound = false;
				boolean jpegFound = false;
				while (i.hasNext()) {
					AttachmentPart a = (AttachmentPart) i.next();
					String type = a.getContentType();
					logger.log(Logger.Level.INFO, "MIME type of attachment = " + type);
					if (type.equals("image/gif"))
						gifFound = true;
					else if (type.equals("text/xml"))
						xmlFound = true;
					else if (type.equals("text/plain"))
						textFound = true;
					else if (type.equals("text/html"))
						htmlFound = true;
					else if (type.equals("image/jpeg"))
						jpegFound = true;
					else {
						logger.log(Logger.Level.ERROR, "Got unexpected MIME type: " + type);
						pass = false;
					}
				}
				logger.log(Logger.Level.INFO, "GIF attachment found = " + gifFound);
				logger.log(Logger.Level.INFO, "XML attachment found = " + xmlFound);
				logger.log(Logger.Level.INFO, "TEXT attachment found = " + textFound);
				logger.log(Logger.Level.INFO, "HTML attachment found = " + htmlFound);
				logger.log(Logger.Level.INFO, "JPEG attachment found = " + jpegFound);
				if (!gifFound || !xmlFound || !textFound || !htmlFound || !jpegFound) {
					TestUtil.logErr("Did not find all expected MIME types in reply message");
					pass = false;
				} else
					logger.log(Logger.Level.INFO, "Did find all expected MIME types in reply message");
				return pass;
			} else {
				logger.log(Logger.Level.ERROR, "Got unexpected " + count + " attachments in reply message, expected 5");
				return false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "ValidateReplyMessage Exception: " + e);
			TestUtil.printStackTrace(e);
			return false;
		}
	}
}
