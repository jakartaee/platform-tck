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

package com.sun.ts.tests.saaj.ee.Standalone;

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.SOAP_Util_Client;

import jakarta.activation.DataHandler;
import jakarta.xml.soap.AttachmentPart;
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

public class Client extends ServiceEETest {
  private SOAPConnection con = null;

  private Properties props = null;

  private boolean debug = false;

  private String hostname = "localhost";

  private int portnum = 8080;

  private static final String cntxroot = "/Standalone_web";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String SOAPVERSION = "SOAPVERSION";

  private static final String NS_PREFIX = "ns-prefix";

  private static final String NS_URI = "ns-uri";

  private static final String PROTOCOL = "http";

  private static final String SERVLET1 = cntxroot + "/ReceivingSOAP11Servlet";

  private static final String SERVLET2 = cntxroot + "/ReceivingSOAP12Servlet";

  private TSURL tsurl = new TSURL();

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
      SOAP_Util_Client.setup();
      con = SOAP_Util_Client.getSOAPConnection();
    } catch (Exception e) {
      throw new Fault("setup failed: ", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: SASendVariousMimeAttachmentsSOAP11Test
   *
   * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
   * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
   * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
   * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
   *
   * @test_Strategy: Create a soap message with various MIME attachments and
   * then send the soap message. Verify that all MIME attachments are received
   * correctly. Sends a soap 1.1 protocol message. Standalone version soap test.
   *
   * Description: Send soap message with various MIME attachments. Standalone
   * version soap test.
   *
   */
  public void SASendVariousMimeAttachmentsSOAP11Test() throws Fault {
    boolean pass = true;

    try {
      // Check if SOAPConnectionFactory is supported
      TestUtil.logMsg("Check if SOAPConnectionFactory is supported");
      if (!SOAP_Util_Client.isSOAPConnectionFactorySupported()) {
        TestUtil.logMsg("SOAPConnectionFactory.newInstance() is "
            + "unsupported (skipping test)");
        return;
      }

      SOAP_Util_Client.setSOAPVersion(SOAP_Util_Client.SOAP11);
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util_Client.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap header from the envelope.
      TestUtil.logMsg("Create SOAP Header");
      SOAPHeader hdr = envelope.getHeader();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add some soap header elements
      TestUtil.logMsg("Add SOAP HeaderElement Header1");
      SOAPElement se = hdr
          .addHeaderElement(envelope.createName("Header1", NS_PREFIX, NS_URI))
          .addTextNode("This is Header1");
      SOAPHeaderElement she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header2");
      se = hdr
          .addHeaderElement(envelope.createName("Header2", NS_PREFIX, NS_URI))
          .addTextNode("This is Header2");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      TestUtil.logMsg("Add SOAP HeaderElement Header3");
      se = hdr
          .addHeaderElement(envelope.createName("Header3", NS_PREFIX, NS_URI))
          .addTextNode("This is Header3");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header4");
      se = hdr
          .addHeaderElement(envelope.createName("Header4", NS_PREFIX, NS_URI))
          .addTextNode("This is Header4");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a some child elements
      TestUtil.logMsg("Add ChildElement Child1");
      sbe.addChildElement(envelope.createName("Child1", NS_PREFIX, NS_URI))
          .addTextNode("This is Child1");
      TestUtil.logMsg("Add ChildElement Child2");
      sbe.addChildElement(envelope.createName("Child2", NS_PREFIX, NS_URI))
          .addTextNode("This is Child2");

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      URL url1 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.xml");
      URL url2 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.gif");
      URL url3 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.txt");
      URL url4 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.html");
      URL url5 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.jpeg");

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap2.setContentType("image/gif");
      ap3.setContentType("text/plain");
      ap4.setContentType("text/html");
      ap5.setContentType("image/jpeg");

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      // Create a url endpoint for the recipient of the message.
      URL urlEndpoint = null;
      urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET1);
      TestUtil.logMsg("URLEndpoint = " + urlEndpoint);

      // Send the message to the endpoint using the connection.
      TestUtil.logMsg("Send SOAP message with various MIME attachments");
      SOAPMessage replymsg = con.call(msg, urlEndpoint);
      TestUtil.logMsg("Message sent successfully (PASSED)");

      // Check if reply message
      TestUtil.logMsg("Check the reply message");
      if (ValidateReplyMessage(replymsg)) {
        TestUtil.logMsg("Reply message is correct (PASSED)");
      } else {
        pass = false;
        TestUtil.logErr("Reply message is incorrect (FAILED)");
      }

      if (!pass)
        throw new Fault("SASendVariousMimeAttachmentsSOAP11Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SASendVariousMimeAttachmentsSOAP11Test failed", e);
    }
  }

  /*
   * @testName: SASendVariousMimeAttachmentsSOAP12Test
   *
   * @assertion_ids: SAAJ:SPEC:1; SAAJ:SPEC:2; SAAJ:SPEC:3; SAAJ:SPEC:4;
   * SAAJ:SPEC:5; SAAJ:SPEC:6; SAAJ:SPEC:7; SAAJ:SPEC:8; SAAJ:SPEC:9;
   * SAAJ:SPEC:10; SAAJ:SPEC:11; SAAJ:SPEC:12; SAAJ:SPEC:13; SAAJ:SPEC:14;
   * SAAJ:SPEC:15; SAAJ:SPEC:16; SAAJ:SPEC:17; SAAJ:SPEC:18;
   *
   * @test_Strategy: Create a soap message with various MIME attachments and
   * then send the soap message. Verify that all MIME attachments are received
   * correctly. Sends a soap 1.2 protocol message. Standalone version soap test.
   *
   * Description: Send soap message with various MIME attachments. Standalone
   * version soap test.
   *
   */
  public void SASendVariousMimeAttachmentsSOAP12Test() throws Fault {
    boolean pass = true;

    try {
      // Check if SOAPConnectionFactory is supported
      TestUtil.logMsg("Check if SOAPConnectionFactory is supported");
      if (!SOAP_Util_Client.isSOAPConnectionFactorySupported()) {
        TestUtil.logMsg("SOAPConnectionFactory.newInstance() is "
            + "unsupported (skipping test)");
        return;
      }

      SOAP_Util_Client.setSOAPVersion(SOAP_Util_Client.SOAP12);
      TestUtil.logMsg("Create SOAP message from message factory");
      SOAPMessage msg = SOAP_Util_Client.getMessageFactory().createMessage();

      // Message creation takes care of creating the SOAPPart - a
      // required part of the message as per the SOAP 1.1 spec.
      TestUtil.logMsg("Get SOAP Part");
      SOAPPart sp = msg.getSOAPPart();

      // Retrieve the envelope from the soap part to start building
      // the soap message.
      TestUtil.logMsg("Get SOAP Envelope");
      SOAPEnvelope envelope = sp.getEnvelope();

      // Create a soap header from the envelope.
      TestUtil.logMsg("Create SOAP Header");
      SOAPHeader hdr = envelope.getHeader();

      // Create a soap body from the envelope.
      TestUtil.logMsg("Create SOAP Body");
      SOAPBody bdy = envelope.getBody();

      // Add some soap header elements
      TestUtil.logMsg("Add SOAP HeaderElement Header1");
      SOAPElement se = hdr
          .addHeaderElement(envelope.createName("Header1", NS_PREFIX, NS_URI))
          .addTextNode("This is Header1");
      SOAPHeaderElement she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header2");
      se = hdr
          .addHeaderElement(envelope.createName("Header2", NS_PREFIX, NS_URI))
          .addTextNode("This is Header2");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      TestUtil.logMsg("Add SOAP HeaderElement Header3");
      se = hdr
          .addHeaderElement(envelope.createName("Header3", NS_PREFIX, NS_URI))
          .addTextNode("This is Header3");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(true);

      TestUtil.logMsg("Add SOAP HeaderElement Header4");
      se = hdr
          .addHeaderElement(envelope.createName("Header4", NS_PREFIX, NS_URI))
          .addTextNode("This is Header4");
      she = (SOAPHeaderElement) se;
      she.setMustUnderstand(false);

      // Add a soap body element
      TestUtil.logMsg("Add SOAP BodyElement Body1");
      SOAPBodyElement sbe = bdy
          .addBodyElement(envelope.createName("Body1", NS_PREFIX, NS_URI));

      // Add a some child elements
      TestUtil.logMsg("Add ChildElement Child1");
      sbe.addChildElement(envelope.createName("Child1", NS_PREFIX, NS_URI))
          .addTextNode("This is Child1");
      TestUtil.logMsg("Add ChildElement Child2");
      sbe.addChildElement(envelope.createName("Child2", NS_PREFIX, NS_URI))
          .addTextNode("This is Child2");

      TestUtil.logMsg("Add various mime type attachments to SOAP message");
      URL url1 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.xml");
      URL url2 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.gif");
      URL url3 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.txt");
      URL url4 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.html");
      URL url5 = tsurl.getURL(PROTOCOL, hostname, portnum,
          cntxroot + "/attach.jpeg");

      TestUtil.logMsg("Create SOAP Attachment (XML document)");
      TestUtil.logMsg("URL1=" + url1);
      AttachmentPart ap1 = msg.createAttachmentPart(new DataHandler(url1));

      TestUtil.logMsg("Create SOAP Attachment (GIF image)");
      TestUtil.logMsg("URL2=" + url2);
      AttachmentPart ap2 = msg.createAttachmentPart(new DataHandler(url2));

      TestUtil.logMsg("Create SOAP Attachment (Plain text)");
      TestUtil.logMsg("URL3=" + url3);
      AttachmentPart ap3 = msg.createAttachmentPart(new DataHandler(url3));

      TestUtil.logMsg("Create SOAP Attachment (HTML document)");
      TestUtil.logMsg("URL4=" + url4);
      AttachmentPart ap4 = msg.createAttachmentPart(new DataHandler(url4));

      TestUtil.logMsg("Create SOAP Attachment (JPEG image)");
      TestUtil.logMsg("URL5=" + url5);
      AttachmentPart ap5 = msg.createAttachmentPart(new DataHandler(url5));

      ap1.setContentType("text/xml");
      ap2.setContentType("image/gif");
      ap3.setContentType("text/plain");
      ap4.setContentType("text/html");
      ap5.setContentType("image/jpeg");

      // Add the attachments to the message.
      TestUtil.logMsg("Add SOAP Attachment (XML document) to SOAP message");
      msg.addAttachmentPart(ap1);
      TestUtil.logMsg("Add SOAP Attachment (GIF image) to SOAP message");
      msg.addAttachmentPart(ap2);
      TestUtil.logMsg("Add SOAP Attachment (Plain text) to SOAP message");
      msg.addAttachmentPart(ap3);
      TestUtil.logMsg("Add SOAP Attachment (HTML document) to SOAP message");
      msg.addAttachmentPart(ap4);
      TestUtil.logMsg("Add SOAP Attachment (JPEG image) to SOAP message");
      msg.addAttachmentPart(ap5);
      msg.saveChanges();
      TestUtil.logMsg("Done creating SOAP message");

      // Create a url endpoint for the recipient of the message.
      URL urlEndpoint = null;
      urlEndpoint = tsurl.getURL(PROTOCOL, hostname, portnum, SERVLET2);
      TestUtil.logMsg("URLEndpoint = " + urlEndpoint);

      // Send the message to the endpoint using the connection.
      TestUtil.logMsg("Send SOAP message with various MIME attachments");
      SOAPMessage replymsg = con.call(msg, urlEndpoint);
      TestUtil.logMsg("Message sent successfully (PASSED)");

      // Check if reply message
      TestUtil.logMsg("Check the reply message");
      if (ValidateReplyMessage(replymsg)) {
        TestUtil.logMsg("Reply message is correct (PASSED)");
      } else {
        pass = false;
        TestUtil.logErr("Reply message is incorrect (FAILED)");
      }

      if (!pass)
        throw new Fault("SASendVariousMimeAttachmentsSOAP12Test failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("SASendVariousMimeAttachmentsSOAP12Test failed", e);
    }
  }

  private boolean ValidateReplyMessage(SOAPMessage msg) {
    try {
      boolean pass = true;
      SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

      TestUtil.logMsg("Verify soap headers");
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
        else if (value.equals("This is Header1")
            && name.getLocalName().equals("Header1"))
          foundHeader1 = true;
        else if (value.equals("This is Header2")
            && name.getLocalName().equals("Header2"))
          foundHeader2 = true;
        else if (value.equals("This is Header3")
            && name.getLocalName().equals("Header3"))
          foundHeader3 = true;
        else if (value.equals("This is Header4")
            && name.getLocalName().equals("Header4"))
          foundHeader4 = true;
      }
      if (!foundHeader1 || !foundHeader2 || !foundHeader3 || !foundHeader4) {
        TestUtil.logErr("Did not find expected soap headers in reply message");
        pass = false;
      } else
        TestUtil.logMsg("Did find expected soap headers in reply message");
      TestUtil.logMsg("Verify soap body");
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
          if (value.equals("This is Child1")
              && name.getLocalName().equals("Child1"))
            foundChild1 = true;
          else if (value.equals("This is Child2")
              && name.getLocalName().equals("Child2"))
            foundChild2 = true;
        }
      }
      if (!foundBody1) {
        TestUtil.logErr("Did not find expected soap body in reply message");
        pass = false;
      } else
        TestUtil.logMsg("Did find expected soap body in reply message");
      if (!foundChild1 || !foundChild2) {
        TestUtil.logErr("Did not find expected soap body "
            + "child elements in reply message");
        pass = false;
      } else
        TestUtil.logMsg(
            "Did find expected soap body child " + "elements in reply message");
      TestUtil.logMsg("Verify attachments");
      int count = msg.countAttachments();
      if (count == 5) {
        TestUtil
            .logMsg("Got expected " + count + " attachments in reply message");
        i = msg.getAttachments();
        TestUtil.logMsg("Verify correct MIME types of attachments");
        boolean gifFound = false;
        boolean xmlFound = false;
        boolean textFound = false;
        boolean htmlFound = false;
        boolean jpegFound = false;
        while (i.hasNext()) {
          AttachmentPart a = (AttachmentPart) i.next();
          String type = a.getContentType();
          TestUtil.logMsg("MIME type of attachment = " + type);
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
            TestUtil.logErr("Got unexpected MIME type: " + type);
            pass = false;
          }
        }
        TestUtil.logMsg("GIF attachment found = " + gifFound);
        TestUtil.logMsg("XML attachment found = " + xmlFound);
        TestUtil.logMsg("TEXT attachment found = " + textFound);
        TestUtil.logMsg("HTML attachment found = " + htmlFound);
        TestUtil.logMsg("JPEG attachment found = " + jpegFound);
        if (!gifFound || !xmlFound || !textFound || !htmlFound || !jpegFound) {
          TestUtil
              .logErr("Did not find all expected MIME types in reply message");
          pass = false;
        } else
          TestUtil.logMsg("Did find all expected MIME types in reply message");
        return pass;
      } else {
        TestUtil.logErr("Got unexpected " + count
            + " attachments in reply message, expected 5");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("ValidateReplyMessage Exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }
  }
}
