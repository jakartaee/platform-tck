/*
 * Copyright (c) 2003, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.saaj.ee.WSITests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.System.Logger;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class Client extends com.sun.ts.tests.saaj.common.Client {
	private String GoodSoapMessage = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/451/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/451/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

	private String GoodSoapMessageSOAP12 = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:soapenc=\"http://www.w3.org/2003/05/soap-encoding\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/451/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/451/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

	private StreamSource ssrc = null;

	private Properties props = new Properties();

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	/*
	 * @testName: TestSetCharEncodingUtf16
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test character encoding for utf-16 encoding.
	 */
	@Test
	public void TestSetCharEncodingUtf16() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestSetCharEncodingUtf16");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				logger.log(Logger.Level.INFO, "Set character encoding to utf-16");
				message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping utf-16 encoded SOAP message");
				String utf16message = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, utf16message);
				logger.log(Logger.Level.INFO, "No exception means (expected)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestSetCharEncodingUtf16 failed", e);
		}

		if (!pass)
			throw new Exception("TestSetCharEncodingUtf16 failed");
	}

	/*
	 * @testName: TestSetCharEncodingUtf8
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test character encoding for utf-8 encoding.
	 */
	@Test
	public void TestSetCharEncodingUtf8() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestSetCharEncodingUtf8");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				logger.log(Logger.Level.INFO, "Set character encoding to utf-8");
				message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping utf-8 encoded SOAP message");
				String utf8message = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, utf8message);
				logger.log(Logger.Level.INFO, "No exception means (expected)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestSetCharEncodingUtf8 failed", e);
		}

		if (!pass)
			throw new Exception("TestSetCharEncodingUtf8 failed");
	}

	/*
	 * @testName: TestVerifyXmlDeclarationUtf16
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test outputing xml declaration for utf-16 encoding.
	 */
	@Test
	public void TestVerifyXmlDeclarationUtf16() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestVerifyXmlDeclarationUtf16");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				logger.log(Logger.Level.INFO, "Set character encoding to utf-16");
				logger.log(Logger.Level.INFO, "Allow xml declaration in SOAP message");
				message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
				message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping utf-16 encoded SOAP message");
				String utf16message = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, utf16message);
				String decoded = new String(baos.toByteArray(), "utf-16");
				logger.log(Logger.Level.INFO, "Dumping utf-16 decoded SOAP message");
				logger.log(Logger.Level.INFO, decoded);
				logger.log(Logger.Level.INFO, "Verify xml declaration is present with utf-16");
				if (decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1)
					logger.log(Logger.Level.INFO, "xml declaration is present (expected)");
				else {
					logger.log(Logger.Level.ERROR, "xml declaration is not present (unexpected)");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestVerifyXmlDeclarationUtf16 failed", e);
		}

		if (!pass)
			throw new Exception("TestVerifyXmlDeclarationUtf16 failed");
	}

	/*
	 * @testName: TestVerifyXmlDeclarationUtf8
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test outputing xml declaration for utf-8 encoding.
	 */
	@Test
	public void TestVerifyXmlDeclarationUtf8() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestVerifyXmlDeclarationUtf8");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				logger.log(Logger.Level.INFO, "Set character encoding to utf-8");
				logger.log(Logger.Level.INFO, "Allow xml declaration in SOAP message");
				message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
				message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping utf-8 encoded SOAP message");
				String utf8message = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, utf8message);
				logger.log(Logger.Level.INFO, "Verify xml declaration is present with utf-8");
				if (utf8message.indexOf("<?xml") != -1 && utf8message.indexOf("utf-8") != -1)
					logger.log(Logger.Level.INFO, "xml declaration is present (expected)");
				else {
					logger.log(Logger.Level.ERROR, "xml declaration is not present (unexpected)");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestVerifyXmlDeclarationUtf8 failed", e);
		}

		if (!pass)
			throw new Exception("TestVerifyXmlDeclarationUtf8 failed");
	}

	/*
	 * @testName: TestVerifyNoXmlDeclarationOutput
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test that no xml declaration is output.
	 */
	@Test
	public void TestVerifyNoXmlDeclarationOutput() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestVerifyNoXmlDeclarationOutput");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				logger.log(Logger.Level.INFO, "Disallow xml declaration in SOAP message");
				message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "false");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping SOAP message");
				String soapmessage = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, soapmessage);
				logger.log(Logger.Level.INFO, "Verify xml declaration is not present");
				if (soapmessage.indexOf("<?xml") != -1) {
					logger.log(Logger.Level.ERROR, "xml declaration is present (unexpected)");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "xml declaration is not present (expected)");
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestVerifyNoXmlDeclarationOutput failed", e);
		}

		if (!pass)
			throw new Exception("TestVerifyNoXmlDeclarationOutput failed");
	}

	/*
	 * @testName: TestVerifyNoXmlDeclarationDefaultCase
	 *
	 * @assertion_ids: SAAJ:JAVADOC:45;
	 *
	 * @test_Strategy: Test that no xml declaration is output. Default behavior
	 * case.
	 */
	@Test
	public void TestVerifyNoXmlDeclarationDefaultCase() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "TestVerifyNoXmlDeclarationDefaultCase");
			for (int i = 0; i < 2; i++) {
				MessageFactory factory = null;
				ByteArrayInputStream bais = null;
				if (i == 0) {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.1 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
				} else {
					logger.log(Logger.Level.INFO, "Testing SOAP Version 1.2 Protocol");
					factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
					bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
				}
				SOAPMessage message = factory.createMessage();
				SOAPPart sp = message.getSOAPPart();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				logger.log(Logger.Level.INFO, "Read in SOAP Message from ByteArrayInputStream");
				ssrc = new StreamSource(bais);
				sp.setContent(ssrc);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				logger.log(Logger.Level.INFO, "Dumping SOAP message");
				String soapmessage = new String(baos.toByteArray());
				logger.log(Logger.Level.INFO, soapmessage);
				logger.log(Logger.Level.INFO, "Verify xml declaration is not present");
				if (soapmessage.indexOf("<?xml") != -1) {
					logger.log(Logger.Level.ERROR, "xml declaration is present (unexpected)");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "xml declaration is not present (expected)");
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			TestUtil.printStackTrace(e);
			throw new Exception("TestVerifyNoXmlDeclarationDefaultCase failed", e);
		}

		if (!pass)
			throw new Exception("TestVerifyNoXmlDeclarationDefaultCase failed");
	}
}
