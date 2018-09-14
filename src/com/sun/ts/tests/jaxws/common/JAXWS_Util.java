/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.lang.reflect.*;
import java.net.*;

import javax.xml.ws.*;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.soap.*;
import java.io.*;
import javax.naming.*;

import org.w3c.dom.Node;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;

import java.lang.reflect.*;

import javax.xml.soap.SOAPMessage;

public final class JAXWS_Util {
  public static final String SOAP11 = "soap11";

  public static final String SOAP12 = "soap12";

  private static MessageFactory mfactorySOAP11 = null;

  private static MessageFactory mfactorySOAP12 = null;

  private static SOAPFactory sfactorySOAP11 = null;

  private static SOAPFactory sfactorySOAP12 = null;

  private static boolean debug = false;

  private static Context nctx = null;

  private static String content = null;

  public static Object doJNDILookup(String name) throws NamingException {
    System.out.println("JAXWS_Util:doJNDILookup");
    if (nctx == null) {
      System.out.println("Create JAXWS naming context...");
      nctx = new InitialContext();
      System.out.println("List all names in JAXWS naming context...");
      NamingEnumeration list = nctx.list("");
      while (list.hasMore()) {
        NameClassPair nc = (NameClassPair) list.next();
        System.out.println(nc);
      }
    }
    return nctx.lookup(name);
  }

  /*************************************************************************
   * Porting wrapper methods
   *************************************************************************/
  public static String getURLFromProp(String urlProp) throws Exception {
    return TestUtil.getProperty(urlProp);
  }

  /**************************************************************************/
  // Logging methods below should be null methods (implementation specific).

  public static void setSOAPLogging(java.lang.Object stub,
      java.io.OutputStream s) throws Exception {
    boolean enableSoapLogging = Boolean
        .parseBoolean(TestUtil.getProperty("EnableSoapLogging"));
    boolean debug = false;
    if (!enableSoapLogging)
      return;
    System.out.println("DEBUG: setSOAPLogging - NOT USED");
  }

  public static void setSOAPLogging(java.lang.Object stub) throws Exception {
    setSOAPLogging(stub, System.out);
  }

  /*************************************************************************
   * javax.xml.ws.Service.create and service.getPort wrapper methods
   *************************************************************************/

  public static javax.xml.ws.Service getService(QName sname) throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(QName)");
    javax.xml.ws.Service service = null;
    TestUtil.logMsg("QNAME=" + sname);
    TestUtil.logMsg("Creating Service via javax.xml.ws.Service.create(QName)");
    service = javax.xml.ws.Service.create(sname);
    if (service == null)
      TestUtil
          .logErr("FATAL: javax.xml.ws.Service.create(QName) returned a null");
    return service;
  }

  public static javax.xml.ws.Service getService(URL wsdlurl, QName sname)
      throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(URL, QName)");
    javax.xml.ws.Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("QName=" + sname);
    TestUtil
        .logMsg("Creating Service via javax.xml.ws.Service.create(URL, QName)");
    service = javax.xml.ws.Service.create(wsdlurl, sname);
    if (service == null)
      TestUtil.logErr(
          "FATAL: javax.xml.ws.Service.create(URL, QName) returned a null");
    return service;
  }

  public static javax.xml.ws.Service getService(Class siClass)
      throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(Class)");
    javax.xml.ws.Service service = null;
    TestUtil.logMsg("siClass=" + siClass.getName());
    service = (javax.xml.ws.Service) siClass.newInstance();
    if (service == null)
      TestUtil
          .logErr("FATAL: JAXWS_Util.getService(Class) returned service=null");
    return service;
  }

  public static javax.xml.ws.Service getService(URL wsdlurl, QName siName,
      Class siClass) throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(URL, QName, Class)");
    javax.xml.ws.Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("siName=" + siName);
    Modules.ensureReadable(JAXWS_Util.class, siClass);
    Modules.ensureReadable(JAXWS_Util.class, siClass.getSuperclass());
    TestUtil.logMsg("siClass=" + siClass.getName());
    Constructor ctr = siClass.getConstructor(URL.class, QName.class);
    service = (javax.xml.ws.Service) ctr.newInstance(wsdlurl, siName);
    if (service == null)
      TestUtil.logErr(
          "FATAL: JAXWS_Util.getService(URL, QName, Class) returned service=null");
    return service;
  }

  public static Object getPort(URL wsdlurl, QName siName, Class siClass,
      QName portName, Class seiClass) throws Exception {
    TestUtil.logMsg("JAXWS_Util.getPort(URL, QName, Class, QName, Class)");
    Object stub = getPort(wsdlurl, siName, siClass, portName, seiClass, null);
    return stub;
  }

  // Addressing getPort call
  public static Object getPort(URL wsdlurl, QName siName, Class siClass,
      QName portName, Class seiClass, WebServiceFeature[] wsf)
      throws Exception {
    TestUtil.logMsg(
        "JAXWS_Util.getPort(URL, QName, Class, QName, Class, WebServiceFeature[])");
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("siName=" + siName);
    TestUtil.logMsg("siClass=" + siClass.getName());
    TestUtil.logMsg("portName=" + portName);
    TestUtil.logMsg("seiClass=" + seiClass.getName());
    if (wsf != null) {
      TestUtil.logMsg("wsf.length=" + wsf.length);
      for (int i = 0; i < wsf.length; i++) {
        TestUtil.logMsg("wsf[" + i + "]=" + wsf[i].toString());
      }
    }
    Constructor ctr = siClass.getConstructor(URL.class, QName.class);
    javax.xml.ws.Service svc = (javax.xml.ws.Service) ctr.newInstance(wsdlurl,
        siName);
    TestUtil.logMsg("Get stub/proxy for seiClass -> " + seiClass.getName()
        + ", port ->" + portName);
    Object stub = null;
    if (wsf != null)
      stub = svc.getPort(portName, seiClass, wsf);
    else
      stub = svc.getPort(portName, seiClass);
    if (stub == null) {
      if (wsf != null)
        TestUtil.logErr(
            "FATAL: JAXWS_Util.getPort(URL, QName, Class, QName, Class, WebServiceFeature[]) returned stub/proxy=null");
      else
        TestUtil.logErr(
            "FATAL: JAXWS_Util.getPort(URL, QName, Class, QName, Class) returned stub/proxy=null");
    } else
      TestUtil.logMsg("Obtained stub/proxy=" + stub);
    return stub;
  }

  public static Object getPort(javax.xml.ws.Service svc, QName port,
      Class seiClass) throws Exception {
    TestUtil.logMsg("JAXWS_Util.getPort(javax.xml.ws.Service, QName, Class)");
    Object stub = getPort(svc, port, seiClass, null);
    return stub;
  }

  // Addressing getPort call
  public static Object getPort(javax.xml.ws.Service svc, QName port,
      Class seiClass, WebServiceFeature[] wsf) throws Exception {
    TestUtil.logMsg(
        "JAXWS_Util.getPort(javax.xml.ws.Service, QName, Class, WebServiceFeature[])");
    TestUtil.logMsg("Get stub/proxy for port qname=" + port);
    Object stub = null;
    if (wsf != null)
      stub = svc.getPort(port, seiClass, wsf);
    else
      stub = svc.getPort(port, seiClass);
    if (stub == null) {
      if (wsf != null)
        TestUtil.logErr(
            "FATAL: JAXWS_Util.getPort(javax.xml.ws.Service, QName, Class, WebServiceFeature[]) returned stub/proxy=null");
      else
        TestUtil.logErr(
            "FATAL: JAXWS_Util.getPort(javax.xml.ws.Service, QName, Class) returned stub/proxy=null");
    } else
      TestUtil.logMsg("Obtained stub/proxy=" + stub);
    return stub;
  }

  public static void setTargetEndpointAddress(Object stub, String url)
      throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    TestUtil.logMsg("Set target endpoint address to=" + url + " ...");
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    JAXWS_Util.setSOAPLogging(stub);
  }

  public static String getTargetEndpointAddress(Object stub) throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    String url = (String) context
        .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    TestUtil.logMsg("Get target endpoint address=" + url);
    return url;
  }

  public static void dumpTargetEndpointAddress(Object stub) throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    String url = (String) context
        .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    TestUtil.logMsg("Dump target endpoint address=" + url);
  }

  public static void setUserNameAndPassword(Object stub, String username,
      String password) throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    context.put(BindingProvider.USERNAME_PROPERTY, username);
    context.put(BindingProvider.PASSWORD_PROPERTY, password);
  }

  /*************************************************************************
   * Other methods
   *************************************************************************/

  public static void dumpWSDLLocation(javax.xml.ws.Service service) {
    TestUtil.logMsg("service wsdl loc=" + service.getWSDLDocumentLocation());
  }

  public static void dumpServiceName(javax.xml.ws.Service service) {
    TestUtil.logMsg("service name=" + service.getServiceName());
  }

  public static void dumpPorts(javax.xml.ws.Service service) {
    for (Iterator iterator = service.getPorts(); iterator.hasNext();) {
      QName name = (QName) iterator.next();
      TestUtil.logMsg("port:" + name.toString());
    }
  }

  public static String getMessageEncoding(SOAPMessage msg)
      throws SOAPException {
    String encoding = "utf-8";
    if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {
      encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
    }
    return encoding;
  }

  public static void printSOAPMessage(SOAPMessage msg, PrintWriter writer) {
    writer.println(returnSOAPMessageAsString(msg));
  }

  public static void printSOAPMessage(SOAPMessage msg, PrintStream out) {
    out.println("" + returnSOAPMessageAsString(msg));
  }

  public static void dumpSOAPMessage(SOAPMessage msg, boolean output) {
    if (msg == null) {
      TestUtil.logMsg("SOAP Message is null");
      return;
    }
    if (output) {
      TestUtil.logMsg("");
      TestUtil.logMsg("--------------------");
      TestUtil.logMsg("DUMP OF SOAP MESSAGE");
      TestUtil.logMsg("--------------------");
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        TestUtil.logMsg(baos.toString(getMessageEncoding(msg)));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    } else {
      System.out.println("\n--------------------\nDUMP OF SOAP MESSAGE"
          + "\n--------------------");
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        System.out.println(baos.toString(getMessageEncoding(msg)));
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
  }

  public static void dumpSOAPMessage(SOAPMessage msg) {
    dumpSOAPMessage(msg, true);
  }

  public static String returnSOAPMessageAsString(SOAPMessage msg) {
    if (msg == null) {
      TestUtil.logMsg("SOAP Message is null");
      return null;
    }
    ByteArrayOutputStream baos = null;
    String s = null;
    try {
      baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      s = baos.toString(getMessageEncoding(msg));
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return s;
  }

  public static void dumpSOAPMessageWOA(SOAPMessage msg) {
    dumpSOAPMessageWOA(msg, true);
  }

  public static void dumpSOAPMessageWOA(SOAPMessage msg, boolean output) {
    ByteArrayOutputStream baos = null;
    if (msg == null) {
      TestUtil.logMsg("SOAP Message is null");
      return;
    }
    if (output) {
      TestUtil.logMsg("");
      TestUtil.logMsg("------------------------");
      TestUtil.logMsg("DUMP OF SOAP MESSAGE WOA");
      TestUtil.logMsg("------------------------");
      try {
        baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage tmpMsg = mf.createMessage(msg.getMimeHeaders(),
            new ByteArrayInputStream(baos.toString().getBytes()));
        tmpMsg.removeAllAttachments();
        tmpMsg.saveChanges();
        baos.close();
        baos = new ByteArrayOutputStream();
        tmpMsg.writeTo(baos);
        TestUtil.logMsg(baos.toString(getMessageEncoding(tmpMsg)));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      } finally {
        try {
          baos.close();
        } catch (Exception e) {
        }
      }
    } else {
      System.out.println("\n------------------------\nDUMP OF SOAP "
          + "MESSAGE WOA\n------------------------");
      try {
        baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage tmpMsg = mf.createMessage(msg.getMimeHeaders(),
            new ByteArrayInputStream(baos.toString().getBytes()));
        tmpMsg.removeAllAttachments();
        tmpMsg.saveChanges();
        baos.close();
        baos = new ByteArrayOutputStream();
        tmpMsg.writeTo(baos);
        System.out.println(baos.toString(getMessageEncoding(tmpMsg)));
      } catch (Exception e) {
        e.printStackTrace(System.err);
      } finally {
        try {
          baos.close();
        } catch (Exception e) {
        }
      }
    }
  }

  public static boolean SOAPElementContentsEqual(SOAPElement request,
      SOAPElement response) {
    String requestContent = getSOAPElementContent("Request", request);
    String responseContent = getSOAPElementContent("Response", response);
    TestUtil.logMsg("Comparing request and response SOAPElement content");
    if (requestContent.equals(responseContent)) {
      TestUtil.logMsg(
          "Request and Response SOAPElement content is equal (expected)");
      return true;
    } else {
      TestUtil.logErr(
          "Request and Response SOAPElement content is not equal (unexpected)");
      return false;
    }
  }

  public static String getSOAPElementContent(String which, SOAPElement se) {
    content = null;
    TestUtil.logMsg("Dumping SOAPElement " + which + " Content");
    _GetSOAPElementContent(se);
    return content;
  }

  private static void _GetSOAPElementContent(SOAPElement se) {
    javax.xml.soap.Name name = se.getElementName();
    if (name.getURI() == null || name.getURI().equals("")) {
      TestUtil.logMsg(" Element=" + name.getLocalName());
      content += name.getLocalName();
    } else {
      TestUtil
          .logMsg(" Element=" + name.getLocalName() + " URI=" + name.getURI());
      content += name.getLocalName() + " " + name.getURI();
    }
    Iterator i = se.getAllAttributes();
    while (i.hasNext()) {
      Object o = i.next();
      if (o instanceof javax.xml.soap.Name) {
        javax.xml.soap.Name attr = (javax.xml.soap.Name) o;
        if (attr.getURI() == null || attr.getURI().equals("")) {
          TestUtil.logMsg("  AttrName=" + attr.getLocalName() + " AttrValue="
              + se.getAttributeValue(attr));
          // content += " "+attr.getLocalName()+" "+
          // se.getAttributeValue(attr);
        } else {
          TestUtil.logMsg("  AttrName=" + attr.getLocalName() + " URI="
              + attr.getURI() + " AttrValue=" + se.getAttributeValue(attr));
          // content += " "+attr.getLocalName()+" "+
          // attr.getURI()+" "+se.getAttributeValue(attr);
        }
      }
    }
    i = se.getChildElements();
    while (i.hasNext()) {
      Object o = i.next();
      if (o instanceof javax.xml.soap.Name) {
        javax.xml.soap.Name elem = (javax.xml.soap.Name) o;
        if (elem.getURI() == null || elem.getURI().equals("")) {
          TestUtil.logMsg(" Element=" + elem.getLocalName());
          content += " " + elem.getLocalName();
        } else {
          TestUtil.logMsg(
              " Element=" + elem.getLocalName() + " URI=" + elem.getURI());
          content += " " + elem.getLocalName() + " " + elem.getURI();
        }
      } else if (o instanceof javax.xml.soap.Text) {
        javax.xml.soap.Text text = (javax.xml.soap.Text) o;
        TestUtil.logMsg("  Text=" + text.getValue());
        content += " " + text.getValue();
      } else {
        SOAPElement se2 = (SOAPElement) o;
        _GetSOAPElementContent(se2);
      }
    }
  }

  public static SOAPFault createSOAPFault(String soapVer) throws Exception {
    javax.xml.soap.SOAPFault soapFault = null;
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance - " + soapVer);
      javax.xml.soap.MessageFactory mfactory = getMessageFactory(soapVer);

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      javax.xml.soap.SOAPMessage soapmsg = mfactory.createMessage();

      // Retrieve the soap part from the soap message..
      TestUtil.logMsg("Get SOAP Part");
      javax.xml.soap.SOAPPart sp = soapmsg.getSOAPPart();

      // Retrieve the envelope from the soap part.
      TestUtil.logMsg("Get SOAP Envelope");
      javax.xml.soap.SOAPEnvelope envelope = sp.getEnvelope();

      // Retrieve the soap body from the envelope.
      TestUtil.logMsg("Get SOAP Body");
      javax.xml.soap.SOAPBody body = envelope.getBody();

      // Add a soap fault to the soap body.
      soapFault = body.addFault();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught: " + e);
      return null;
    }
    return soapFault;
  }

  public static SOAPFault createSOAPFault(String soapVer,
      javax.xml.namespace.QName faultCode, String faultActor,
      String faultString, javax.xml.soap.Name name) throws Exception {
    javax.xml.soap.SOAPFault soapFault = null;
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance - " + soapVer);
      javax.xml.soap.MessageFactory mfactory = getMessageFactory(soapVer);

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      javax.xml.soap.SOAPMessage soapmsg = mfactory.createMessage();

      // Retrieve the soap part from the soap message..
      TestUtil.logMsg("Get SOAP Part");
      javax.xml.soap.SOAPPart sp = soapmsg.getSOAPPart();

      // Retrieve the envelope from the soap part.
      TestUtil.logMsg("Get SOAP Envelope");
      javax.xml.soap.SOAPEnvelope envelope = sp.getEnvelope();

      // Retrieve the soap body from the envelope.
      TestUtil.logMsg("Get SOAP Body");
      javax.xml.soap.SOAPBody body = envelope.getBody();

      // Add a soap fault to the soap body.
      soapFault = body.addFault();
      soapFault.setFaultCode(faultCode);
      soapFault.setFaultActor(faultActor);
      soapFault.setFaultString(faultString);
      javax.xml.soap.Detail detail = soapFault.addDetail();
      detail.addDetailEntry(name);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught: " + e);
      return null;
    }
    return soapFault;
  }

  public static MessageFactory getMessageFactory(String soapVer)
      throws Exception {
    System.out.println("JAXWS_Util:getMessageFactory");
    if (soapVer.equals(SOAP11)) {
      if (mfactorySOAP11 == null)
        mfactorySOAP11 = MessageFactory.newInstance();
      return mfactorySOAP11;
    } else {
      if (mfactorySOAP12 == null)
        mfactorySOAP12 = MessageFactory
            .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
      return mfactorySOAP12;
    }
  }

  public static SOAPFactory getSOAPFactory(String soapVer) throws Exception {
    System.out.println("JAXWS_Util:getSOAPFactory");
    if (soapVer.equals(SOAP11)) {
      if (sfactorySOAP11 == null)
        sfactorySOAP11 = SOAPFactory.newInstance();
      return sfactorySOAP11;
    } else {
      if (sfactorySOAP12 == null)
        sfactorySOAP12 = SOAPFactory
            .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
      return sfactorySOAP12;
    }
  }

  public static SOAPMessage createEmtpySOAPMessage(String soapVer) {
    try {
      SOAPMessage message = getMessageFactory(soapVer).createMessage();
      return message;
    } catch (Exception e) {
      return null;
    }
  }

  public static SOAPMessage makeSOAPMessage(String msg) {
    try {
      Source srcMsg = makeSource(msg, "StreamSource");
      MessageFactory factory = MessageFactory.newInstance();
      SOAPMessage message = factory.createMessage();
      message.getSOAPPart().setContent((Source) srcMsg);
      message.saveChanges();
      return message;
    } catch (Exception e) {
      return null;
    }
  }

  public static Source makeSource(String msg, String type) {
    Reader reader = new StringReader(msg);
    if (type.equals("StreamSource"))
      return new StreamSource(reader);
    else if (type.equals("DOMSource"))
      return new DOMSource(createDOMNode(reader));
    else if (type.equals("SAXSource"))
      return new SAXSource(new InputSource(reader));
    else
      return null;
  }

  public static Node createDOMNode(Reader reader) {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    dbf.setValidating(false);
    try {
      DocumentBuilder builder = dbf.newDocumentBuilder();
      try {
        return builder.parse(new InputSource(reader));
      } catch (SAXException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (ParserConfigurationException pce) {
      IllegalArgumentException iae = new IllegalArgumentException(
          pce.getMessage());
      iae.initCause(pce);
      throw iae;
    }
    return null;
  }

  public static String getDOMResultAsString(DOMResult dr) throws Exception {
    DOMSource ds = new DOMSource(dr.getNode());
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    Writer out = new StringWriter();
    StreamResult streamResult = new StreamResult();
    streamResult.setWriter(out);
    transformer.transform(ds, streamResult);
    return streamResult.getWriter().toString();
  }

  public static DOMResult getSourceAsDOMResult(Source s) throws Exception {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    DOMResult result = new DOMResult();
    transformer.transform(s, result);
    return result;
  }

  public static String getSourceAsString(Source s) throws Exception {
    DOMResult dr = getSourceAsDOMResult(s);
    return getDOMResultAsString(dr);
  }

  public static String getSOAPMessageAsString(SOAPMessage message) {
    return getMsgAsString(message);
  }

  public static String getMsgAsString(SOAPMessage message) {
    String msg = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      message.writeTo(baos);
      msg = baos.toString(getMessageEncoding(message));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return msg;
  }

  public static int looseIndexOf(List<String> s, String searchStr) {
    int index = -1;
    int currentIndex = -1;
    TestUtil.logTrace("In looseIndexOf");
    TestUtil.logTrace("List.size()=" + s.size());
    TestUtil.logTrace("List.isEmpty()=" + s.isEmpty());
    if (!s.isEmpty()) {
      Iterator i = s.iterator();
      while (i.hasNext()) {
        currentIndex++;
        String item = (String) i.next();
        if (item != null) {
          TestUtil.logTrace("item=|" + item + "|");
          TestUtil.logTrace("searchStr=" + searchStr);
          if (item.indexOf(searchStr) != -1) {
            TestUtil.logTrace("found item");
            index = currentIndex;
            break;
          }
        } else {
          TestUtil.logErr("A null object was returned while iterating through");
          TestUtil.logErr("the list of callbacks");
        }
      }
    }
    return index;
  }

  public static int looseLastIndexOf(List<String> s, String searchStr) {
    TestUtil.logTrace("In looseLastIndexOf");
    TestUtil.logTrace("List.size()=" + s.size());
    TestUtil.logTrace("List.isEmpty()=" + s.isEmpty());
    int lastIndex = -1;
    int currentIndex = -1;
    if (!s.isEmpty()) {
      Iterator i = s.iterator();
      while (i.hasNext()) {
        currentIndex++;
        String item = (String) i.next();
        if (item != null) {
          TestUtil.logTrace("item=|" + item + "|");
          TestUtil.logTrace("searchStr=" + searchStr);
          if (item.indexOf(searchStr) != -1) {
            TestUtil.logTrace("found item");
            lastIndex = currentIndex;
          }
        } else {
          TestUtil.logErr("A null object was returned while iterating through");
          TestUtil.logErr("the list of callbacks");
        }
      }
    }
    return lastIndex;
  }

  public static void dumpList(List<String> s) {
    TestUtil.logTrace("------------------------------------");
    TestUtil.logTrace("Dumping List<String>");
    TestUtil.logTrace("------------------------------------");
    TestUtil.logTrace("\n");
    TestUtil.logTrace("List.size()=" + s.size());
    TestUtil.logTrace("List.isEmpty()=" + s.isEmpty());
    if (!s.isEmpty()) {
      Iterator i = s.iterator();
      while (i.hasNext()) {
        Object o = i.next();
        if (o != null) {
          if (o instanceof String) {
            TestUtil.logTrace((String) o);
          } else {
            TestUtil.logTrace(
                "An unexpected object was returned while iterating through");
            TestUtil
                .logTrace("the list of callbacks, expected String got:" + o);
          }
        } else {
          TestUtil
              .logTrace("A null object was returned while iterating through");
          TestUtil.logTrace("the list of callbacks");
        }
      }
    }
  }

  public static List<String> getMessagesStartingFrom(List<String> calls,
      String searchItem) {
    boolean pass = true;

    TestUtil.logTrace("The complete list of messages are the following:");
    dumpList(calls);

    TestUtil.logTrace("Getting all messages after " + searchItem);
    List<String> messages = new Vector<String>();
    TestUtil.logTrace("\n");
    TestUtil.logTrace("The first item found is:");
    if (!calls.isEmpty()) {
      boolean found = false;
      Iterator i = calls.iterator();
      while (i.hasNext()) {
        Object o = i.next();
        if (o != null) {
          if (o instanceof String) {
            String item = (String) o;
            if (found == false) {
              if (item.indexOf(searchItem) > -1) {
                TestUtil.logTrace(item);
                found = true;
              }
            }
            if (found) {
              messages.add(item);
            }
          } else {
            TestUtil.logErr(
                "An unexpected object was returned while iterating through");
            TestUtil.logErr("the list of callbacks, expected String got:" + o);
          }
        } else {
          TestUtil.logErr("A null object was returned while iterating through");
          TestUtil.logErr("the list of callbacks");
        }
      }
    }
    return messages;

  }

  public static boolean doesMethodExist(Class c, String methodName) {
    boolean found = false;
    Method[] methods = c.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      if (methodName.equals(method.getName())) {
        found = true;
      }
    }
    return found;
  }

  public static Method getMethod(Class c, String methodName) {
    TestUtil.logTrace("in getMethod");
    Method result = null;
    for (Method method : c.getDeclaredMethods()) {
      String m = method.getName();
      TestUtil.logTrace("method=" + m);
      if (methodName.equals(m)) {
        result = method;
        TestUtil.logTrace("found:" + methodName);
      }
    }
    return result;
  }

  public static Class getMethodReturnType(Class c, String methodName) {
    Class result = null;
    Method m = getMethod(c, methodName);
    if (m != null) {
      TestUtil
          .logTrace("The method:" + methodName + " was found for class:" + c);
      TestUtil.logTrace("now returning the return type");
      result = m.getReturnType();
    } else {
      TestUtil.logTrace(
          "The method:" + methodName + " was not found for class:" + c);
    }
    return result;
  }

  public static Class getMethodParameterType(Class c, String methodName,
      int location) {
    Class result = null;
    Method m = getMethod(c, methodName);
    if (m != null) {
      TestUtil
          .logTrace("The method:" + methodName + " was found for class:" + c);
      Class[] pTypes = m.getParameterTypes();
      if (location <= pTypes.length) {
        TestUtil.logTrace("Found specified parameter, now returning it");
        result = pTypes[location];
      } else {
        TestUtil.logTrace("The method:" + methodName
            + " did not contain a parameter in position " + location);
      }
    } else {
      TestUtil.logTrace(
          "The method:" + methodName + " was not found for class:" + c);
    }
    return result;
  }

  public static boolean doesExceptionExistForAllMethods(Class c,
      String exceptionName) {
    boolean result = true;
    for (Method method : c.getDeclaredMethods()) {
      String m = method.getName();
      TestUtil.logTrace("method=" + m);
      int count = 0;
      String eName = "";
      for (Class c1 : method.getExceptionTypes()) {
        eName = c1.getName();
        TestUtil.logTrace("exception name=" + eName);
        if (eName.equals(exceptionName)) {
          count++;
        }
      }
      if (count == 0) {
        TestUtil.logErr("The method:" + m + " did not declare " + exceptionName
            + " on all of it's methods");
        result = false;
      }
    }
    return result;
  }

  public static boolean validateURL(String theURL) {
    boolean valid = false;
    URL url = null;
    URLConnection urlConnection = null;
    TestUtil.logMsg("validateURL: -> " + theURL);
    try {
      url = new URL(theURL);
      Object o = url.openConnection().getContent();
      valid = true;
      TestUtil.logMsg("validateURL passed");
    } catch (Exception e) {
      TestUtil.logMsg("validateURL failed");
    }
    return valid;
  }

  public static int getFreePort() {
    int port = -1;
    ServerSocket soc = null;
    int minPort = -1;
    int maxPort = -1;

    try {
      minPort = Integer.parseInt(TestUtil.getProperty("port.range.min", "-1"));

    } catch (Exception e) {
    }

    try {
      maxPort = Integer.parseInt(TestUtil.getProperty("port.range.max", "-1"));

    } catch (Exception e) {
    }

    TestUtil.logMsg("minPort=" + minPort);
    TestUtil.logMsg("maxPort=" + maxPort);

    SecurityManager sec = System.getSecurityManager();

    if (minPort == -1 || minPort > maxPort) { // usual case: port range is
                                              // not specified in
                                              // interview
      if (sec != null) {
        try {
          sec.checkListen(0);
        } catch (SecurityException secex) {
          TestUtil.logErr("Security configuration does not allow to "
              + "wait for the connection on ports 1024-... ", secex);

          return -1;
        }
      }
      try {
        soc = new ServerSocket(0);
        port = soc.getLocalPort();
      } catch (IOException e) {
        TestUtil.logErr(
            "Unable to get a free port because of an exception: " + e, e);
        return -1;
      } finally {
        try {
          soc.close();
        } catch (Exception e) {
        }
      }
      return port;
    } else if (minPort == maxPort && minPort != -1) {
      return minPort;
    } else { // special case: port range is specified in interview for
             // some reason
      for (port = minPort; port <= maxPort; port++) {
        try {
          if (sec != null) {
            sec.checkListen(port);
          }
          soc = new ServerSocket(port);
          soc.close();
          return port;
        } catch (java.io.IOException e) {
        } catch (SecurityException e) {
        } finally {
          try {
            soc.close();
          } catch (Exception e) {
          }
        }
      }
      TestUtil.logErr("Ports in range [" + minPort + "," + maxPort + "] are "
          + "not available on local machine "
          + "or security configuration does not allow "
          + "to wait for the connection on these ports.");
      return -1; // can not find out a free port
    }
  }

  public static void dumpHTTPHeaders(SOAPMessageContext context) {
    dumpHTTPHeaders(context, false);
  }

  public static void dumpHTTPHeaders(SOAPMessageContext context,
      boolean output) {

    Map<String, List<String>> mreq = (Map<String, List<String>>) context
        .get(SOAPMessageContext.HTTP_REQUEST_HEADERS);
    Map<String, List<String>> mresp = (Map<String, List<String>>) context
        .get(SOAPMessageContext.HTTP_RESPONSE_HEADERS);
    StringBuffer sbreq = new StringBuffer();
    int cnt = 0;
    Iterator iterator = null;
    try {
      if (mreq != null) {
        if (mreq.size() > 0) {
          iterator = mreq.keySet().iterator();
          while (iterator.hasNext()) {
            String key = (String) iterator.next();
            List<String> list = (List<String>) mreq.get(key);
            Iterator i = list.iterator();
            String values = "";
            while (i.hasNext())
              values = (String) i.next() + " ";
            sbreq.append("" + key + "=" + values);
            cnt++;
          }
        }
      } else
        sbreq.append("http_request_headers are null");
    } catch (Exception e) {
    }
    StringBuffer sbresp = new StringBuffer();
    cnt = 0;
    iterator = null;
    try {
      if (mresp != null) {
        if (mresp.size() > 0) {
          iterator = mresp.keySet().iterator();
          while (iterator.hasNext()) {
            String key = (String) iterator.next();
            List<String> list = (List<String>) mresp.get(key);
            Iterator i = list.iterator();
            String values = "";
            while (i.hasNext())
              values = (String) i.next() + " ";
            sbresp.append("" + key + "=" + values);
            cnt++;
          }
        }
      } else
        sbresp.append("http_response_headers are null");
    } catch (Exception e) {
    }

    if (output) {
      TestUtil.logMsg("");
      TestUtil.logMsg("--------------------");
      TestUtil.logMsg("DUMP OF HTTP Headers");
      TestUtil.logMsg("--------------------");
      TestUtil.logMsg("HTTP_REQUEST_HEADERS=" + sbreq.toString());
      TestUtil.logMsg("HTTP_RESPONSE_HEADERS=" + sbresp.toString());
    } else {
      System.out.println("\n--------------------\nDUMP OF HTTP Headers"
          + "\n--------------------");
      System.out.println("HTTP_REQUEST_HEADERS=" + sbreq.toString());
      System.out.println("HTTP_RESPONSE_HEADERS=" + sbresp.toString());
    }
  }

  public static Map<String, List<String>> convertKeysToLowerCase(
      Map<String, List<String>> in) {

    HashMap out = new HashMap();
    if (in != null) {
      for (Map.Entry<String, List<String>> e : in.entrySet()) {
        if (e.getKey() != null)
          out.put(e.getKey().toLowerCase(), e.getValue());
        else
          out.put(e.getKey(), e.getValue());
      }
    }
    return out;
  }

  public static void setSOAPACTIONURI(Object o, String action) {
    BindingProvider bp = (BindingProvider) o;
    java.util.Map<String, Object> requestContext = bp.getRequestContext();
    if (requestContext == null) {
      TestUtil.logErr("setSOAPACTIONURI:getRequestContext() returned null");
    } else {
      requestContext.put(BindingProvider.SOAPACTION_URI_PROPERTY, action);
      requestContext.put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      TestUtil.logMsg("SOAPACTION_USE_PROPERTY being set to: "
          + requestContext.get(BindingProvider.SOAPACTION_USE_PROPERTY));
      TestUtil.logMsg("SOAPACTION_URI_PROPERTY being set to: "
          + requestContext.get(BindingProvider.SOAPACTION_URI_PROPERTY));
    }
  }

}
