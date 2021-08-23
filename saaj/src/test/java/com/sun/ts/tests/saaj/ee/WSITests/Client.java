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
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class Client extends ServiceEETest {
  private String GoodSoapMessage = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/451/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/451/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

  private String GoodSoapMessageSOAP12 = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:soapenc=\"http://www.w3.org/2003/05/soap-encoding\" xmlns:tns=\"http://helloservice.org/wsdl\" xmlns:xsi=\"http://www.w3.org/451/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/451/XMLSchema\"><soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><tns:hello><String_1 xsi:type=\"xsd:string\">&lt;Bozo&gt;</String_1></tns:hello></soap:Body></soap:Envelope>";

  private StreamSource ssrc = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: TestSetCharEncodingUtf16
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test character encoding for utf-16 encoding.
   */
  public void TestSetCharEncodingUtf16() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestSetCharEncodingUtf16");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        TestUtil.logMsg("Set character encoding to utf-16");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping utf-16 encoded SOAP message");
        String utf16message = new String(baos.toByteArray());
        TestUtil.logMsg(utf16message);
        TestUtil.logMsg("No exception means (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestSetCharEncodingUtf16 failed", e);
    }

    if (!pass)
      throw new Fault("TestSetCharEncodingUtf16 failed");
  }

  /*
   * @testName: TestSetCharEncodingUtf8
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test character encoding for utf-8 encoding.
   */
  public void TestSetCharEncodingUtf8() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestSetCharEncodingUtf8");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        TestUtil.logMsg("Set character encoding to utf-8");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping utf-8 encoded SOAP message");
        String utf8message = new String(baos.toByteArray());
        TestUtil.logMsg(utf8message);
        TestUtil.logMsg("No exception means (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestSetCharEncodingUtf8 failed", e);
    }

    if (!pass)
      throw new Fault("TestSetCharEncodingUtf8 failed");
  }

  /*
   * @testName: TestVerifyXmlDeclarationUtf16
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test outputing xml declaration for utf-16 encoding.
   */
  public void TestVerifyXmlDeclarationUtf16() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestVerifyXmlDeclarationUtf16");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        TestUtil.logMsg("Set character encoding to utf-16");
        TestUtil.logMsg("Allow xml declaration in SOAP message");
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-16");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping utf-16 encoded SOAP message");
        String utf16message = new String(baos.toByteArray());
        TestUtil.logMsg(utf16message);
        String decoded = new String(baos.toByteArray(), "utf-16");
        TestUtil.logMsg("Dumping utf-16 decoded SOAP message");
        TestUtil.logMsg(decoded);
        TestUtil.logMsg("Verify xml declaration is present with utf-16");
        if (decoded.indexOf("<?xml") != -1 && decoded.indexOf("utf-16") != -1)
          TestUtil.logMsg("xml declaration is present (expected)");
        else {
          TestUtil.logErr("xml declaration is not present (unexpected)");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestVerifyXmlDeclarationUtf16 failed", e);
    }

    if (!pass)
      throw new Fault("TestVerifyXmlDeclarationUtf16 failed");
  }

  /*
   * @testName: TestVerifyXmlDeclarationUtf8
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test outputing xml declaration for utf-8 encoding.
   */
  public void TestVerifyXmlDeclarationUtf8() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestVerifyXmlDeclarationUtf8");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        TestUtil.logMsg("Set character encoding to utf-8");
        TestUtil.logMsg("Allow xml declaration in SOAP message");
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping utf-8 encoded SOAP message");
        String utf8message = new String(baos.toByteArray());
        TestUtil.logMsg(utf8message);
        TestUtil.logMsg("Verify xml declaration is present with utf-8");
        if (utf8message.indexOf("<?xml") != -1
            && utf8message.indexOf("utf-8") != -1)
          TestUtil.logMsg("xml declaration is present (expected)");
        else {
          TestUtil.logErr("xml declaration is not present (unexpected)");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestVerifyXmlDeclarationUtf8 failed", e);
    }

    if (!pass)
      throw new Fault("TestVerifyXmlDeclarationUtf8 failed");
  }

  /*
   * @testName: TestVerifyNoXmlDeclarationOutput
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test that no xml declaration is output.
   */
  public void TestVerifyNoXmlDeclarationOutput() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestVerifyNoXmlDeclarationOutput");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        TestUtil.logMsg("Disallow xml declaration in SOAP message");
        message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "false");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping SOAP message");
        String soapmessage = new String(baos.toByteArray());
        TestUtil.logMsg(soapmessage);
        TestUtil.logMsg("Verify xml declaration is not present");
        if (soapmessage.indexOf("<?xml") != -1) {
          TestUtil.logErr("xml declaration is present (unexpected)");
          pass = false;
        } else {
          TestUtil.logMsg("xml declaration is not present (expected)");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestVerifyNoXmlDeclarationOutput failed", e);
    }

    if (!pass)
      throw new Fault("TestVerifyNoXmlDeclarationOutput failed");
  }

  /*
   * @testName: TestVerifyNoXmlDeclarationDefaultCase
   *
   * @assertion_ids: SAAJ:JAVADOC:45;
   *
   * @test_Strategy: Test that no xml declaration is output. Default behavior
   * case.
   */
  public void TestVerifyNoXmlDeclarationDefaultCase() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("TestVerifyNoXmlDeclarationDefaultCase");
      for (int i = 0; i < 2; i++) {
        MessageFactory factory = null;
        ByteArrayInputStream bais = null;
        if (i == 0) {
          TestUtil.logMsg("Testing SOAP Version 1.1 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessage.getBytes());
        } else {
          TestUtil.logMsg("Testing SOAP Version 1.2 Protocol");
          factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
          bais = new ByteArrayInputStream(GoodSoapMessageSOAP12.getBytes());
        }
        SOAPMessage message = factory.createMessage();
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        TestUtil.logMsg("Read in SOAP Message from ByteArrayInputStream");
        ssrc = new StreamSource(bais);
        sp.setContent(ssrc);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        TestUtil.logMsg("Dumping SOAP message");
        String soapmessage = new String(baos.toByteArray());
        TestUtil.logMsg(soapmessage);
        TestUtil.logMsg("Verify xml declaration is not present");
        if (soapmessage.indexOf("<?xml") != -1) {
          TestUtil.logErr("xml declaration is present (unexpected)");
          pass = false;
        } else {
          TestUtil.logMsg("xml declaration is not present (expected)");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestVerifyNoXmlDeclarationDefaultCase failed", e);
    }

    if (!pass)
      throw new Fault("TestVerifyNoXmlDeclarationDefaultCase failed");
  }
}
