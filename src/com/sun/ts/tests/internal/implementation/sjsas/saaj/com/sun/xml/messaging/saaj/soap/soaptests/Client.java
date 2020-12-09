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

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.soap.soaptests;

import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
// Import implementation specific classes to test
import com.sun.xml.messaging.saaj.soap.*;

import jakarta.activation.FileDataSource;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class Client extends EETest {
  private Properties props = null;

  private String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/saaj/com/sun/xml/messaging/saaj/soap/soaptests";

  private String testDir = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    String tsHome = p.getProperty("ts_home");
    testDir = tsHome + "/" + srcDir;
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: SOAPIOExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPIOExceptionTest() throws Fault {
    TestUtil.logTrace("SOAPIOExceptionTest");
    boolean pass = true;
    String s = "This is my SOAPIOException";
    SOAPIOException ex;
    Exception foo = new Exception("foo");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      TestUtil.logMsg("Call SOAPIOException() constructor");
      ex = new SOAPIOException();
      TestUtil.logMsg("Call SOAPIOException(String) constructor");
      ex = new SOAPIOException(s);
      TestUtil.logMsg("Call SOAPIOException(String, Throwable) constructor");
      ex = new SOAPIOException(s, foo);
      TestUtil.logMsg("Call SOAPIOException(Throwable) constructor");
      ex = new SOAPIOException(foo);
      TestUtil.logMsg("Call SOAPIOException.initCause(Throwable) method");
      ex = new SOAPIOException();
      ex.initCause(foo);
      TestUtil.logMsg("Call SOAPIOException.getCause() method");
      Throwable t = ex.getCause();
      ex = new SOAPIOException(s);
      TestUtil.logMsg("Call SOAPIOException.fillInStackTrace() method");
      ex.fillInStackTrace();
      TestUtil.logMsg("Call SOAPIOException.getLocalizedMessage() method");
      ex.getLocalizedMessage();
      TestUtil.logMsg("Call SOAPIOException.getMessage() method");
      ex.getMessage();
      TestUtil.logMsg("Call SOAPIOException.printStackTrace() method");
      PrintStream saveErrStream = System.err;
      System.setErr(new PrintStream(baos));
      ex.printStackTrace();
      System.setErr(saveErrStream);
      TestUtil
          .logMsg("Call SOAPIOException.printStackTrace(PrintStream) method");
      ex.printStackTrace(new PrintStream(baos));
      TestUtil
          .logMsg("Call SOAPIOException.printStackTrace(PrintWriter) method");
      ex.printStackTrace(new PrintWriter(new PrintStream(baos)));
      TestUtil.logMsg("Call SOAPIOException.toString() method");
      ex.toString();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPIOExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPIOExceptionTest failed");
  }

  /*
   * @testName: SOAPVersionMismatchExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPVersionMismatchExceptionTest() throws Fault {
    TestUtil.logTrace("SOAPVersionMismatchExceptionTest");
    boolean pass = true;
    String s = "This is my SOAPVersionMismatchException";
    SOAPVersionMismatchException ex;
    Exception foo = new Exception("foo");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      TestUtil.logMsg("Call SOAPVersionMismatchException() constructor");
      ex = new SOAPVersionMismatchException();
      TestUtil.logMsg("Call SOAPVersionMismatchException(String) constructor");
      ex = new SOAPVersionMismatchException(s);
      TestUtil.logMsg(
          "Call SOAPVersionMismatchException(String, Throwable) constructor");
      ex = new SOAPVersionMismatchException(s, foo);
      TestUtil
          .logMsg("Call SOAPVersionMismatchException(Throwable) constructor");
      ex = new SOAPVersionMismatchException(foo);
      TestUtil.logMsg(
          "Call SOAPVersionMismatchException.initCause(Throwable) method");
      ex = new SOAPVersionMismatchException();
      ex.initCause(foo);
      TestUtil.logMsg("Call SOAPVersionMismatchException.getCause() method");
      Throwable t = ex.getCause();
      ex = new SOAPVersionMismatchException(s);
      TestUtil.logMsg("Call SOAPVersionMismatchException.getMessage() method");
      ex.getMessage();
      TestUtil
          .logMsg("Call SOAPVersionMismatchException.printStackTrace() method");
      PrintStream saveErrStream = System.err;
      System.setErr(new PrintStream(baos));
      ex.printStackTrace();
      System.setErr(saveErrStream);
      TestUtil.logMsg(
          "Call SOAPVersionMismatchException.printStackTrace(PrintStream) method");
      ex.printStackTrace(new PrintStream(baos));
      TestUtil.logMsg(
          "Call SOAPVersionMismatchException.printStackTrace(PrintWriter) method");
      ex.printStackTrace(new PrintWriter(new PrintStream(baos)));
      TestUtil.logMsg("Call SOAPVersionMismatchException.toString() method");
      ex.toString();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPVersionMismatchExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPVersionMismatchExceptionTest failed");
  }

  /*
   * @testName: MessageFactoryImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MessageFactoryImplTest() throws Fault {
    TestUtil.logTrace("MessageFactoryImplTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call MessageFactoryImpl() constructor");
      MessageFactoryImpl mfi = new MessageFactoryImpl();
      TestUtil.logMsg("Call MessageFactoryImpl.listen(OutputStream) method");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStream os = mfi.listen(baos);
      TestUtil.logMsg("Call MessageFactoryImpl.createMessage() method");
      try {
        SOAPMessage soapmsg = mfi.createMessage();
      } catch (Exception e) {
      }
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      SOAPMessage msg1 = MessageFactory.newInstance().createMessage();
      msg1.writeTo(baos1);
      MimeHeaders headers = new MimeHeaders();
      headers.addHeader("Content-Type", "text/xml");
      TestUtil.logMsg(
          "Call MessageFactoryImpl.createMessage(MimeHeaders, InputStream) method");
      SOAPMessage msg2 = mfi.createMessage(headers,
          new ByteArrayInputStream(baos1.toString().getBytes()));
      msg2.writeTo(baos2);
      if (msg2 == null) {
        TestUtil
            .logErr("createMessage(MimeHeaders, InputStream) returned null");
        pass = false;
      } else if (!(msg2 instanceof SOAPMessage)) {
        TestUtil.logErr("createMessage(MimeHeaders, InputStream) "
            + "did not return SOAPMessage object");
        pass = false;
      }
      if (!(baos1.toString().equals(baos2.toString()))) {
        TestUtil.logMsg("msg1 = " + baos1.toString());
        TestUtil.logMsg("msg2 = " + baos2.toString());
        TestUtil.logErr("msg1 and msg2 are not equal (they should be)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MessageFactoryImplTest failed", e);
    }

    if (!pass)
      throw new Fault("MessageFactoryImplTest failed");
  }

  /*
   * @testName: SOAPDocumentFragmentTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPDocumentFragmentTest() throws Fault {
    TestUtil.logTrace("SOAPDocumentFragmentTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call SOAPDocumentFragment() constructor");
      SOAPDocumentFragment sdf = new SOAPDocumentFragment();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPDocumentFragmentTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPDocumentFragmentTest failed");
  }

  /*
   * @testName: SOAPDocumentImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPDocumentImplTest() throws Fault {
    TestUtil.logTrace("SOAPDocumentImplTest");
    boolean pass = true;
    try {
      org.w3c.dom.Node node = null;
      TestUtil.logMsg("Call SOAPDocumentImpl(SOAPPartImpl) constructor");
      SOAPMessage msg = MessageFactory.newInstance().createMessage();
      SOAPDocumentImpl sdi = new SOAPDocumentImpl(
          (SOAPPartImpl) msg.getSOAPPart());
      SOAPMessage msg2 = MessageFactory.newInstance().createMessage();
      SOAPDocumentImpl sdi2 = new SOAPDocumentImpl(
          (SOAPPartImpl) msg2.getSOAPPart());
      TestUtil.logMsg("Call SOAPDocumentImpl.getSOAPPart() method");
      SOAPPartImpl partImpl = sdi.getSOAPPart();
      TestUtil.logMsg("Call SOAPDocumentImpl.getDocument() method");
      sdi = sdi.getDocument();
      TestUtil.logMsg("Call SOAPDocumentImpl.getDoctype() method");
      DocumentType doctype = sdi.getDoctype();
      TestUtil.logMsg("Call SOAPDocumentImpl.getDOMImplementation() method");
      DOMImplementation domImpl = sdi.getImplementation();
      TestUtil.logMsg("Call SOAPDocumentImpl.getDocumentElement() method");
      Element element = sdi.getDocumentElement();
      TestUtil.logMsg("Call SOAPDocumentImpl.createElement(String) method");
      try {
        element = sdi.createElement("element1");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPDocumentImpl.createDocumentFragment() method");
      DocumentFragment docFrag = sdi.createDocumentFragment();
      TestUtil.logMsg("Call SOAPDocumentImpl.createTextNode(String) method");
      org.w3c.dom.Text text = sdi.createTextNode("my text");
      TestUtil
          .logMsg("Call SOAPDocumentImpl.createCDATASection(String) method");
      CDATASection cdata = sdi.createCDATASection("my data");
      TestUtil.logMsg("Call SOAPDocumentImpl.createComment(String) method");
      Comment comment = sdi.createComment("my comment");
      TestUtil.logMsg(
          "Call SOAPDocumentImpl.createProcessingInstruction(String, String) method");
      try {
        ProcessingInstruction pi = sdi.createProcessingInstruction("piinst",
            "pidata");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPDocumentImpl.createAttribute(String) method");
      try {
        Attr attr = sdi.createAttribute("attribute1");
      } catch (Exception e) {
      }
      TestUtil
          .logMsg("Call SOAPDocumentImpl.createEntityReference(String) method");
      try {
        EntityReference entityRef = sdi.createEntityReference("entity1");
      } catch (Exception e) {
      }
      TestUtil
          .logMsg("Call SOAPDocumentImpl.getElementsByTagName(String) method");
      NodeList nl = sdi.getElementsByTagName("element1");
      TestUtil.logMsg("Call SOAPDocumentImpl.importNode(String) method");
      element = sdi2.createElement("element3");
      sdi.importNode((org.w3c.dom.Node) element, true);
      TestUtil.logMsg(
          "Call SOAPDocumentImpl.createElementNS(String, String) method");
      try {
        element = sdi.createElementNS("http://MyNameSpace.org/", "element2");
      } catch (Exception e) {
      }
      TestUtil.logMsg(
          "Call SOAPDocumentImpl.createAttributeNS(String, String) method");
      try {
        Attr attr = sdi.createAttributeNS("http://MyNameSpace.org/",
            "attribute2");
      } catch (Exception e) {
      }
      TestUtil.logMsg(
          "Call SOAPDocumentImpl.getElementsByTagName(String, String) method");
      nl = sdi.getElementsByTagNameNS("http://MyNameSpace.org/", "element2");
      element = sdi2.createElement("element4");
      TestUtil.logMsg("Call SOAPDocumentImpl.getElementById(String) method");
      element = sdi.getElementById("my id");
      TestUtil.logMsg("Call SOAPDocumentImpl.cloneNode(boolean) method");
      node = sdi.cloneNode(true);
      TestUtil.logMsg(
          "Call SOAPDocumentImpl.cloneNode(SOAPDocumentImpl, boolean) method");
      sdi.cloneNode(sdi.getDocument(), true);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPDocumentImplTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPDocumentImplTest failed");
  }

  /*
   * @testName: EnvelopeFactoryTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void EnvelopeFactoryTest() throws Fault {
    TestUtil.logTrace("EnvelopeFactoryTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call EnvelopeFactory() constructor");
      EnvelopeFactory envFactory = new EnvelopeFactory();
      SOAPMessage msg = MessageFactory.newInstance().createMessage();
      SOAPPart part = msg.getSOAPPart();
      TestUtil.logMsg(
          "Call EnvelopeFactory.createEnvelope(Source, SOAPPartImpl) method");
      String fileToOpen = testDir + "/data/data.soapmsg";
      fileToOpen = fileToOpen.replace('/', File.separatorChar);
      if (File.separator.equals("\\"))
        fileToOpen = "C:" + fileToOpen;
      StreamSource ssrc = new StreamSource(new File(fileToOpen));
      Envelope env = envFactory.createEnvelope(ssrc, (SOAPPartImpl) part);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EnvelopeFactoryTest failed", e);
    }

    if (!pass)
      throw new Fault("EnvelopeFactoryTest failed");
  }

  /*
   * @testName: SOAPPartImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPPartImplTest() throws Fault {
    TestUtil.logTrace("SOAPPartImplTest");
    boolean pass = true;
    try {
      org.w3c.dom.Node node = null;
      TestUtil.logMsg("Call MessageFactory.createMessage()");
      SOAPMessage msg = MessageFactory.newInstance().createMessage();
      SOAPMessage msg2 = MessageFactory.newInstance().createMessage();
      TestUtil.logMsg("Call SOAPMessage.getSOAPPart()");
      SOAPPart part = msg.getSOAPPart();
      SOAPPart part2 = msg2.getSOAPPart();
      TestUtil.logMsg("Cast SOAPPart object to SOAPPartImpl object");
      SOAPPartImpl partImpl = (SOAPPartImpl) part;
      SOAPPartImpl partImpl2 = (SOAPPartImpl) part2;
      TestUtil.logMsg("Call SOAPPartImpl.getEnvelope() method");
      SOAPEnvelope env = partImpl.getEnvelope();
      TestUtil.logMsg("Call SOAPPartImpl.getSOAPPart() method");
      partImpl = partImpl.getSOAPPart();
      partImpl2 = partImpl2.getSOAPPart();
      TestUtil.logMsg("Call SOAPPartImpl.getDoctype() method");
      DocumentType docType = partImpl.getDoctype();
      DocumentType docType2 = partImpl2.getDoctype();
      TestUtil.logMsg("Call SOAPPartImpl.getImplementation() method");
      DOMImplementation domImpl = partImpl.getImplementation();
      TestUtil.logMsg("Call SOAPPartImpl.getDocumentElement() method");
      Element element = partImpl.getDocumentElement();
      TestUtil.logMsg("Call SOAPPartImpl.createElement(String) method");
      try {
        element = partImpl.createElement("element1");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.createDocumentFragment() method");
      DocumentFragment docFrag = partImpl.createDocumentFragment();
      TestUtil.logMsg("Call SOAPPartImpl.createTextNode(String) method");
      org.w3c.dom.Text text = partImpl.createTextNode("my text");
      TestUtil.logMsg("Call SOAPPartImpl.createCDATASection(String) method");
      CDATASection cdata = partImpl.createCDATASection("my data");
      TestUtil.logMsg("Call SOAPPartImpl.createComment(String) method");
      Comment comment = partImpl.createComment("my comment");
      TestUtil.logMsg(
          "Call SOAPPartImpl.createProcessingInstruction(String, String) method");
      try {
        ProcessingInstruction pi = partImpl
            .createProcessingInstruction("piinst", "pidata");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.createAttribute(String) method");
      try {
        Attr attr = partImpl.createAttribute("attribute1");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.createEntityReference(String) method");
      try {
        EntityReference entityRef = partImpl.createEntityReference("entity1");
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.getElementsByTagName(String) method");
      NodeList nl = partImpl.getElementsByTagName("element1");
      TestUtil.logMsg("Call SOAPPartImpl.importNode(String) method");
      element = partImpl2.createElement("element3");
      partImpl.importNode((org.w3c.dom.Node) element, true);
      TestUtil
          .logMsg("Call SOAPPartImpl.createElementNS(String, String) method");
      try {
        element = partImpl.createElementNS("http://MyNameSpace.org/",
            "element2");
      } catch (Exception e) {
      }
      TestUtil
          .logMsg("Call SOAPPartImpl.createAttributeNS(String, String) method");
      try {
        Attr attr = partImpl.createAttributeNS("http://MyNameSpace.org/",
            "attribute2");
      } catch (Exception e) {
      }
      TestUtil.logMsg(
          "Call SOAPPartImpl.getElementsByTagName(String, String) method");
      nl = partImpl.getElementsByTagNameNS("http://MyNameSpace.org/",
          "element2");
      element = partImpl2.createElement("element4");
      TestUtil.logMsg("Call SOAPPartImpl.appendChild(org.w3c.dom.Node) method");
      element = partImpl.createElement("elementtoadd");
      try {
        node = partImpl.appendChild((org.w3c.dom.Node) element);
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.cloneNode(boolean) method");
      node = partImpl.cloneNode(true);
      TestUtil.logMsg("Call SOAPPartImpl.getAttributes() method");
      NamedNodeMap nnm = partImpl.getAttributes();
      TestUtil.logMsg("Call SOAPPartImpl.getChildNode() method");
      nl = partImpl.getChildNodes();
      TestUtil.logMsg("Call SOAPPartImpl.getFirstChild() method");
      node = partImpl.getFirstChild();
      TestUtil.logMsg("Call SOAPPartImpl.getLastChild() method");
      node = partImpl.getLastChild();
      TestUtil.logMsg("Call SOAPPartImpl.getLocalName() method");
      String string = partImpl.getLocalName();
      TestUtil.logMsg("Call SOAPPartImpl.getNamespaceURI() method");
      string = partImpl.getNamespaceURI();
      TestUtil.logMsg("Call SOAPPartImpl.getNextSibling() method");
      node = partImpl.getNextSibling();
      TestUtil.logMsg("Call SOAPPartImpl.getNodeName() method");
      string = partImpl.getNodeName();
      TestUtil.logMsg("Call SOAPPartImpl.getNodeType() method");
      short type = partImpl.getNodeType();
      TestUtil.logMsg("Call SOAPPartImpl.getNodeValue() method");
      string = partImpl.getNodeValue();
      TestUtil.logMsg("Call SOAPPartImpl.getOwnerDocument() method");
      Document doc1 = partImpl.getOwnerDocument();
      TestUtil.logMsg("Call SOAPPartImpl.getParentNode() method");
      node = partImpl.getParentNode();
      TestUtil.logMsg("Call SOAPPartImpl.getPrefix() method");
      string = partImpl.getPrefix();
      TestUtil.logMsg("Call SOAPPartImpl.getPreviousSibling() method");
      node = partImpl.getPreviousSibling();
      TestUtil.logMsg("Call SOAPPartImpl.hasAttributes() method");
      boolean b = partImpl.hasAttributes();
      TestUtil.logMsg("Call SOAPPartImpl.hasChildNodes() method");
      b = partImpl.hasChildNodes();
      TestUtil.logMsg("Call SOAPPartImpl.isSupported(String, String) method");
      b = partImpl.isSupported("arg0", "arg1");
      TestUtil.logMsg("Call SOAPPartImpl.normalize() method");
      partImpl.normalize();
      TestUtil.logMsg("Call SOAPPartImpl.removeChild(org.w3c.dom.Node) method");
      element = partImpl.createElement("element3");
      try {
        node = partImpl.removeChild((org.w3c.dom.Node) element);
      } catch (Exception e) {
      }
      TestUtil.logMsg(
          "Call SOAPPartImpl.replaceChild(org.w3c.dom.Node, org.w3c.dom.Node) method");
      Element element1 = partImpl.createElement("element3");
      Element element2 = partImpl.createElement("element4");
      try {
        node = partImpl.replaceChild((org.w3c.dom.Node) element1,
            (org.w3c.dom.Node) element2);
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call SOAPPartImpl.setNodeValue(String) method");
      partImpl.setNodeValue("value");
      TestUtil.logMsg("Call SOAPPartImpl.setPrefix(String) method");
      try {
        partImpl.setPrefix("prefix");
      } catch (Exception e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPPartImplTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPPartImplTest failed");
  }

  /*
   * @testName: GifDataContentHandlerTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void GifDataContentHandlerTest() throws Fault {
    TestUtil.logTrace("GifDataContentHandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call GifDataContentHandler() constructor");
      GifDataContentHandler gifdch = new GifDataContentHandler();
      TestUtil
          .logMsg("Call GifDataContentHandler.getTransferDataFlavors() method");
      DataFlavor df[] = gifdch.getTransferDataFlavors();
      TestUtil.logMsg(
          "Call GifDataContentHandler.getTransferData(DataFlavor, DataSource) method");
      String fileToOpen = testDir + "/data/data.gif";
      fileToOpen = fileToOpen.replace('/', File.separatorChar);
      if (File.separator.equals("\\"))
        fileToOpen = "C:" + fileToOpen;
      FileDataSource ds = new FileDataSource(fileToOpen);
      Object data = gifdch.getTransferData(df[0], ds);
      TestUtil
          .logMsg("Call GifDataContentHandler.getContext(DataSource) method");
      data = gifdch.getContent(ds);
      TestUtil.logMsg(
          "Call GifDataContentHandler.writeTo(Object, String, OutputStream) method");
      TestUtil.logMsg(
          "GIF encoding is not supported so this should throw IOException");
      try {
        gifdch.writeTo(data, "image/gif", new ByteArrayOutputStream());
      } catch (IOException e) {
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GifDataContentHandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("GifDataContentHandlerTest failed");
  }

  /*
   * @testName: JpegDataContentHandlerTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void JpegDataContentHandlerTest() throws Fault {
    TestUtil.logTrace("JpegDataContentHandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call JpegDataContentHandler() constructor");
      JpegDataContentHandler jpegdch = new JpegDataContentHandler();
      TestUtil.logMsg(
          "Call JpegDataContentHandler.getTransferDataFlavors() method");
      DataFlavor df[] = jpegdch.getTransferDataFlavors();
      TestUtil.logMsg(
          "Call JpegDataContentHandler.getTransferData(DataFlavor, DataSource) method");
      String fileToOpen = testDir + "/data/data.jpeg";
      fileToOpen = fileToOpen.replace('/', File.separatorChar);
      if (File.separator.equals("\\"))
        fileToOpen = "C:" + fileToOpen;
      FileDataSource ds = new FileDataSource(fileToOpen);
      Object data = jpegdch.getTransferData(df[0], ds);
      TestUtil
          .logMsg("Call JpegDataContentHandler.getContext(DataSource) method");
      data = jpegdch.getContent(ds);
      TestUtil.logMsg(
          "Call JpegDataContentHandler.writeTo(Object, String, OutputStream) method");
      jpegdch.writeTo(data, "image/jpeg", new ByteArrayOutputStream());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JpegDataContentHandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("JpegDataContentHandlerTest failed");
  }

  /*
   * @testName: XmlDataContentHandlerTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void XmlDataContentHandlerTest() throws Fault {
    TestUtil.logTrace("XmlDataContentHandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call XmlDataContentHandler() constructor");
      XmlDataContentHandler xmldch = new XmlDataContentHandler();
      TestUtil
          .logMsg("Call XmlDataContentHandler.getTransferDataFlavors() method");
      DataFlavor df[] = xmldch.getTransferDataFlavors();
      TestUtil.logMsg(
          "Call XmlDataContentHandler.getTransferData(DataFlavor, DataSource) method");
      String fileToOpen = testDir + "/data/data.xml";
      fileToOpen = fileToOpen.replace('/', File.separatorChar);
      if (File.separator.equals("\\"))
        fileToOpen = "C:" + fileToOpen;
      FileDataSource ds = new FileDataSource(fileToOpen);
      Object data = xmldch.getTransferData(df[0], ds);
      TestUtil
          .logMsg("Call XmlDataContentHandler.getContext(DataSource) method");
      data = xmldch.getContent(ds);
      TestUtil.logMsg(
          "Call XmlDataContentHandler.writeTo(Object, String, OutputStream) method");
      xmldch.writeTo(data, "text/xml", new ByteArrayOutputStream());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("XmlDataContentHandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("XmlDataContentHandlerTest failed");
  }

  /*
   * @testName: StringDataContentHandlerTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void StringDataContentHandlerTest() throws Fault {
    TestUtil.logTrace("StringDataContentHandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call StringDataContentHandler() constructor");
      StringDataContentHandler strdch = new StringDataContentHandler();
      TestUtil.logMsg(
          "Call StringDataContentHandler.getTransferDataFlavors() method");
      DataFlavor df[] = strdch.getTransferDataFlavors();
      TestUtil.logMsg(
          "Call StringDataContentHandler.getTransferData(DataFlavor, DataSource) method");
      String fileToOpen = testDir + "/data/data.text";
      fileToOpen = fileToOpen.replace('/', File.separatorChar);
      if (File.separator.equals("\\"))
        fileToOpen = "C:" + fileToOpen;
      FileDataSource ds = new FileDataSource(fileToOpen);
      Object data = strdch.getTransferData(df[0], ds);
      TestUtil.logMsg(
          "Call StringDataContentHandler.getContext(DataSource) method");
      data = strdch.getContent(ds);
      TestUtil.logMsg(
          "Call StringDataContentHandler.writeTo(Object, String, OutputStream) method");
      strdch.writeTo(data, "text/plain", new ByteArrayOutputStream());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("StringDataContentHandlerTest failed", e);
    }

    if (!pass)
      throw new Fault("StringDataContentHandlerTest failed");
  }
}
