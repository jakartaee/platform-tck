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
 * @(#)JWS_Util.java	1.146 05/11/04
 */

package com.sun.ts.tests.jws.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;

public final class JWS_Util {
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
    System.out.println("JWS_Util:doJNDILookup");
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

  /**
   * **********************************************************************
   * Porting wrapper methods
   * ***********************************************************************
   */
  public static String getURLFromProp(String urlProp) throws Exception {
    return TestUtil.getProperty(urlProp);
  }

  /**
   * **********************************************************************
   */
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

  /**
   * **********************************************************************
   * Service.create and service.getPort wrapper methods
   * ***********************************************************************
   */

  public static Service getService(QName sname) throws Exception {
    TestUtil.logMsg("JWS_Util:getService(QName)");
    Service service = null;
    TestUtil.logMsg("QNAME=" + sname);
    TestUtil.logMsg("Creating Service via Service.create(QName)");
    service = Service.create(sname);
    if (service == null)
      TestUtil.logErr("FATAL: Service.create(QName) returned a null");
    return service;
  }

  public static Service getService(URL wsdlurl, QName sname) throws Exception {
    TestUtil.logMsg("JWS_Util:getService(URL, QName)");
    Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("QName=" + sname);
    TestUtil.logMsg("Creating Service via Service.create(URL, QName)");
    service = Service.create(wsdlurl, sname);
    if (service == null)
      TestUtil.logErr("FATAL: Service.create(URL, QName) returned a null");
    return service;
  }

  public static Service getService(Class siClass) throws Exception {
    TestUtil.logMsg("JWS_Util:getService(Class)");
    Service service = null;
    TestUtil.logMsg("siClass=" + siClass.getName());
    service = (Service) siClass.newInstance();
    if (service == null)
      TestUtil
          .logErr("FATAL: JWS_Util.getService(Class) returned service=null");
    return service;
  }

  public static Service getService(URL wsdlurl, QName siName, Class siClass)
      throws Exception {
    TestUtil.logMsg("JWS_Util:getService(URL, QName, Class)");
    Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("siName=" + siName);
    TestUtil.logMsg("siClass=" + siClass.getName());
    Constructor ctr = siClass.getConstructor(URL.class, QName.class);
    service = (Service) ctr.newInstance(wsdlurl, siName);
    if (service == null)
      TestUtil.logErr(
          "FATAL: JWS_Util.getService(URL, QName, Class) returned service=null");
    return service;
  }

  public static Object getPort(URL wsdlurl, QName siName, Class siClass,
      QName seiName, Class seiClass) throws Exception {
    TestUtil.logMsg("JWS_Util.getPort(URL, QName, Class, QName, Class)");
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("siName=" + siName);
    TestUtil.logMsg("siClass=" + siClass.getName());
    TestUtil.logMsg("seiName=" + seiName);
    TestUtil.logMsg("seiClass=" + seiClass.getName());
    Constructor ctr = siClass.getConstructor(URL.class, QName.class);
    Service svc = (Service) ctr.newInstance(wsdlurl, siName);
    TestUtil.logMsg("Get stub/proxy for seiClass -> " + seiClass.getName()
        + ", port ->" + seiName);
    Object stub = svc.getPort(seiName, seiClass);
    if (stub == null)
      TestUtil.logErr(
          "FATAL: JWS_Util.getPort(URL, QName, Class, QName, Class) returned stub/proxy=null");
    return stub;
  }

  public static Object getPort(Service svc, QName port, Class seiClass)
      throws Exception {
    TestUtil.logMsg("JWS_Util.getPort(Service, QName)");
    TestUtil.logMsg("Get stub/proxy for port qname=" + port);
    Object stub = svc.getPort(port, seiClass);
    TestUtil.logMsg("Obtained stub/proxy=" + stub);
    if (stub == null)
      TestUtil.logErr(
          "FATAL: JWS_Util.getPort(Service, QName) returned stub/proxy=null");
    return stub;
  }

  public static void setTargetEndpointAddress(Object stub, String url)
      throws Exception {
    BindingProvider bindingprovider = (BindingProvider) stub;
    java.util.Map<String, Object> context = bindingprovider.getRequestContext();
    TestUtil.logMsg("Set target endpoint address to=" + url + " ...");
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    JWS_Util.setSOAPLogging(stub);
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

  /**
   * **********************************************************************
   * Other methods
   * ***********************************************************************
   */

  public static void printSOAPMessage(SOAPMessage msg, PrintWriter writer) {
    writer.println(returnSOAPMessageAsString(msg));
  }

  public static void printSOAPMessage(SOAPMessage msg, PrintStream out) {
    out.println("" + returnSOAPMessageAsString(msg));
  }

  public static void dumpSOAPMessage(SOAPMessage msg, boolean output) {
    if (output) {
      TestUtil.logMsg("***** Begin Dumping SOAPMessage *****");
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        TestUtil.logMsg(baos.toString());
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      TestUtil.logMsg("***** Done Dumping SOAPMessage *****");
    } else {
      System.out.println("***** Begin Dumping SOAPMessage *****");
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.writeTo(baos);
        System.out.println(baos.toString());
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      System.out.println("***** Done Dumping SOAPMessage *****");
    }
  }

  public static void dumpSOAPMessage(SOAPMessage msg) {
    dumpSOAPMessage(msg, true);
  }

  public static String returnSOAPMessageAsString(SOAPMessage msg) {
    ByteArrayOutputStream baos = null;
    String s = null;
    try {
      baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      s = baos.toString();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return s;
  }

  public static void dumpSOAPMessageWOA(SOAPMessage msg) {
    TestUtil
        .logMsg("***** Begin Dumping SOAPMessage Without Attachments *****");
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      MessageFactory mf = MessageFactory.newInstance();
      SOAPMessage tmpMsg = mf.createMessage(msg.getMimeHeaders(),
          new ByteArrayInputStream(baos.toString().getBytes()));
      tmpMsg.removeAllAttachments();
      tmpMsg.saveChanges();
      tmpMsg.writeTo(baos);
      TestUtil.logMsg(baos.toString());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    TestUtil.logMsg("***** Done Dumping SOAPMessage Without Attachments *****");
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
    _getSOAPElementContent(se);
    return content;
  }

  private static void _getSOAPElementContent(SOAPElement se) {
    jakarta.xml.soap.Name name = se.getElementName();
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
      if (o instanceof jakarta.xml.soap.Name) {
        jakarta.xml.soap.Name attr = (jakarta.xml.soap.Name) o;
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
      if (o instanceof jakarta.xml.soap.Name) {
        jakarta.xml.soap.Name elem = (jakarta.xml.soap.Name) o;
        if (elem.getURI() == null || elem.getURI().equals("")) {
          TestUtil.logMsg(" Element=" + elem.getLocalName());
          content += " " + elem.getLocalName();
        } else {
          TestUtil.logMsg(
              " Element=" + elem.getLocalName() + " URI=" + elem.getURI());
          content += " " + elem.getLocalName() + " " + elem.getURI();
        }
      } else if (o instanceof jakarta.xml.soap.Text) {
        jakarta.xml.soap.Text text = (jakarta.xml.soap.Text) o;
        TestUtil.logMsg("  Text=" + text.getValue());
        content += " " + text.getValue();
      } else {
        SOAPElement se2 = (SOAPElement) o;
        _getSOAPElementContent(se2);
      }
    }
  }

  public static SOAPFault createSOAPFault(String soapVer) throws Exception {
    jakarta.xml.soap.SOAPFault soapFault = null;
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance - " + soapVer);
      jakarta.xml.soap.MessageFactory mfactory = getMessageFactory(soapVer);

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      jakarta.xml.soap.SOAPMessage soapmsg = mfactory.createMessage();

      // Retrieve the soap part from the soap message..
      TestUtil.logMsg("Get SOAP Part");
      jakarta.xml.soap.SOAPPart sp = soapmsg.getSOAPPart();

      // Retrieve the envelope from the soap part.
      TestUtil.logMsg("Get SOAP Envelope");
      jakarta.xml.soap.SOAPEnvelope envelope = sp.getEnvelope();

      // Retrieve the soap body from the envelope.
      TestUtil.logMsg("Get SOAP Body");
      jakarta.xml.soap.SOAPBody body = envelope.getBody();

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
      String faultString, jakarta.xml.soap.Name name) throws Exception {
    jakarta.xml.soap.SOAPFault soapFault = null;
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance - " + soapVer);
      jakarta.xml.soap.MessageFactory mfactory = getMessageFactory(soapVer);

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      jakarta.xml.soap.SOAPMessage soapmsg = mfactory.createMessage();

      // Retrieve the soap part from the soap message..
      TestUtil.logMsg("Get SOAP Part");
      jakarta.xml.soap.SOAPPart sp = soapmsg.getSOAPPart();

      // Retrieve the envelope from the soap part.
      TestUtil.logMsg("Get SOAP Envelope");
      jakarta.xml.soap.SOAPEnvelope envelope = sp.getEnvelope();

      // Retrieve the soap body from the envelope.
      TestUtil.logMsg("Get SOAP Body");
      jakarta.xml.soap.SOAPBody body = envelope.getBody();

      // Add a soap fault to the soap body.
      soapFault = body.addFault();
      soapFault.setFaultCode(faultCode);
      soapFault.setFaultActor(faultActor);
      soapFault.setFaultString(faultString);
      jakarta.xml.soap.Detail detail = soapFault.addDetail();
      detail.addDetailEntry(name);
    } catch (Exception e) {
      TestUtil.logErr("Exception caught: " + e);
      return null;
    }
    return soapFault;
  }

  public static MessageFactory getMessageFactory(String soapVer)
      throws Exception {
    System.out.println("JWS_Util:getMessageFactory");
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
    System.out.println("JWS_Util:getSOAPFactory");
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
    byte[] bytes = msg.getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    if (type.equals("StreamSource"))
      return new StreamSource(inputStream);
    else if (type.equals("DOMSource"))
      return new DOMSource(createDOMNode(inputStream));
    else if (type.equals("SAXSource"))
      return new SAXSource(new InputSource(inputStream));
    else
      return null;
  }

  public static Node createDOMNode(InputStream inputStream) {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(false);
    dbf.setValidating(false);
    try {
      DocumentBuilder builder = dbf.newDocumentBuilder();
      try {
        return builder.parse(inputStream);
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

  public static String getSourceAsString(Source s) throws Exception {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    OutputStream out = new ByteArrayOutputStream();
    StreamResult streamResult = new StreamResult();
    streamResult.setOutputStream(out);
    transformer.transform(s, streamResult);
    return streamResult.getOutputStream().toString();
  }

  public static DOMResult getSourceAsDOMResult(Source s) throws Exception {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    DOMResult result = new DOMResult();
    transformer.transform(s, result);

    return result;
  }

  public static String getSOAPMessageAsString(SOAPMessage message) {
    return getMsgAsString(message);
  }

  public static String getMsgAsString(SOAPMessage message) {
    String msg = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      message.writeTo(baos);
      msg = baos.toString();
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
    TestUtil.logMsg("validateURL: -> " + theURL);
    try {
      url = new URL(theURL);
      url.openConnection();
      valid = true;
      TestUtil.logMsg("validateURL passed");
    } catch (Exception e) {
      TestUtil.logMsg("validateURL failed");
    }
    return valid;
  }

}
