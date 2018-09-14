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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.lang.reflect.*;
import java.net.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;
import javax.xml.rpc.handler.*;
import javax.xml.soap.*;
import java.io.*;
import javax.naming.*;

import java.lang.reflect.*;

public final class JAXRPC_Util {
  public static final String SOAP11 = "soap11";

  public static final String SOAP12 = "soap12";

  private static MessageFactory mfactorySOAP11 = null;

  private static MessageFactory mfactorySOAP12 = null;

  private static SOAPFactory sfactorySOAP11 = null;

  private static SOAPFactory sfactorySOAP12 = null;

  private static boolean debug = false;

  private static Context nctx = null;

  private static ServiceFactory sfactory = null;

  private static String content = null;

  public static Object doJNDILookup(String name) throws NamingException {
    System.out.println("JAXRPC_Util:doJNDILookup");
    if (nctx == null) {
      System.out.println("Create JAXRPC naming context...");
      nctx = new InitialContext();
      System.out.println("List all names in JAXRPC naming context...");
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

  public static void setSOAPLogging(java.io.OutputStream s) throws Exception {
    // Uncomment for DEBUG of soap messages
    /***
     * System.out.println("DEBUG: setSOAPLogging (make null method before
     * release)");
     * com.sun.xml.rpc.client.dii.CallInvokerImpl.setDefaultTransportFactory(
     * new com.sun.xml.rpc.client.http.HttpClientTransportFactory(s));
     ***/
  }

  public static void setSOAPLogging(javax.xml.rpc.Stub o,
      java.io.OutputStream s) throws Exception {
    // Uncomment for DEBUG of soap messages
    /***
     * System.out.println("DEBUG: setSOAPLogging (make null method before
     * release)"); Class c = o.getClass(); try { Object testArgs[] = { new
     * com.sun.xml.rpc.client.http.HttpClientTransportFactory(s) }; Method
     * methods[] = c.getMethods(); Method m = null; for (int i=0;
     * i<methods.length; i++) { if
     * (methods[i].getName().equals("_setTransportFactory")) { m = methods[i];
     * break; } } if (m != null) m.invoke(o, testArgs); } catch (Exception e) {
     * System.out.println("Exception: " + e); e.printStackTrace(System.out); }
     ***/
  }

  /*************************************************************************
   * ServiceFactory wrapper methods
   *************************************************************************/

  public static ServiceFactory getServiceFactory() {
    TestUtil.logMsg("JAXRPC_Util:getServiceFactory");
    try {
      TestUtil.logMsg("Creating ServiceFactory instance");
      if (sfactory == null)
        sfactory = ServiceFactory.newInstance();
      return sfactory;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: ", e);
      return null;
    }
  }

  public static Service getService(QName sname) throws Exception {
    TestUtil.logMsg("JAXRPC_Util:getService");
    if (sfactory == null)
      sfactory = ServiceFactory.newInstance();
    TestUtil.logMsg("Creating Service instance for QNAME: " + sname);
    Service service = sfactory.createService(sname);
    return service;
  }

  public static Service getService(URL wsdlurl, QName sname) throws Exception {
    TestUtil.logMsg("JAXRPC_Util:getService");
    if (sfactory == null)
      sfactory = ServiceFactory.newInstance();
    TestUtil.logMsg("Creating Service instance");
    TestUtil.logMsg("URL=" + wsdlurl + ", QNAME=" + sname);
    Service service = sfactory.createService(wsdlurl, sname);
    return service;
  }

  public static Service getService(String serviceInterfaceName)
      throws Exception {
    TestUtil.logMsg("JAXRPC_Util.getService()");
    TestUtil.logMsg("Get service implementation class for "
        + "service interface=" + serviceInterfaceName);
    ServiceFactory sf = getServiceFactory();
    Class si = Class.forName(serviceInterfaceName);
    Service svc = (Service) sf.loadService(si);
    TestUtil.logMsg("ServiceImplClass=" + svc);
    return svc;
  }

  public static Object getStub(String serviceInterfaceName, String portMethod)
      throws Exception {
    TestUtil.logMsg("JAXRPC_Util.getStub()");
    TestUtil.logMsg("Get service implementation class for "
        + "service interface=" + serviceInterfaceName);
    ServiceFactory sf = getServiceFactory();
    Class si = Class.forName(serviceInterfaceName);
    Service svc = (Service) sf.loadService(si);
    TestUtil.logMsg("ServiceImplClass=" + svc);
    TestUtil.logMsg("Get stub class for port method=" + portMethod);
    Method m = svc.getClass().getMethod(portMethod);
    return m.invoke(svc);
  }

  public static Object getStub(Service svc, String portMethod)
      throws Exception {
    TestUtil.logMsg("JAXRPC_Util.getStub()");
    TestUtil.logMsg(
        "Get stub class from service instance for port method=" + portMethod);
    Method m = svc.getClass().getMethod(portMethod);
    return m.invoke(svc);
  }

  /*************************************************************************
   * Other methods
   *************************************************************************/

  public static Call setCallProperties(Call call, String uri) throws Exception {
    TestUtil.logMsg("Set standard call properties");
    TestUtil
        .logMsg("" + Call.SOAPACTION_USE_PROPERTY + "=" + new Boolean(true));
    call.setProperty(Call.SOAPACTION_USE_PROPERTY, new Boolean(true));
    TestUtil.logMsg("" + Call.SOAPACTION_URI_PROPERTY + "=" + uri);
    call.setProperty(Call.SOAPACTION_URI_PROPERTY, uri);
    TestUtil.logMsg(
        "" + Call.ENCODINGSTYLE_URI_PROPERTY + "=" + Constants.URI_ENCODING);
    call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, Constants.URI_ENCODING);
    call.setProperty(Call.OPERATION_STYLE_PROPERTY, "rpc");
    return call;
  }

  private static void DumpTypeMappingRegistry(Service service) {
    TestUtil.logMsg("Get TypeMappingRegistry for Service instance");
    try {
      TypeMappingRegistry registry = service.getTypeMappingRegistry();
      if (registry != null) {
        TestUtil.logMsg("TypeMappingRegistry exists for Service instance");
        String s[] = registry.getRegisteredEncodingStyleURIs();
        if (s != null) {
          TestUtil.logMsg("EncodingStyleURIs exists for Service instance");
          for (int i = 0; i < s.length; i++) {
            TestUtil.logMsg("Registered EncodingStyleURI[" + i + "]=" + s[i]);
            TypeMapping t = registry.getTypeMapping(s[i]);
            TestUtil.logMsg("Registered TypeMapping = " + t);
          }
        }
        TestUtil.logMsg(
            "Default TypeMapping = " + registry.getDefaultTypeMapping());
      }
    } catch (UnsupportedOperationException e) {
    } catch (Exception e) {
    }
  }

  public static void printSOAPMessage(SOAPMessage msg, PrintWriter writer) {
    writer.println(returnSOAPMessageAsString(msg));
  }

  public static void printSOAPMessage(SOAPMessage msg, PrintStream out) {
    out.println("" + returnSOAPMessageAsString(msg));
  }

  public static void dumpSOAPMessage(SOAPMessage msg) {
    TestUtil.logMsg("***** Begin Dumping SOAPMessage *****");
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      TestUtil.logMsg(baos.toString());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    TestUtil.logMsg("***** Done Dumping SOAPMessage *****");
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
    String requestContent = GetSOAPElementContent("Request", request);
    String responseContent = GetSOAPElementContent("Response", response);
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

  public static String GetSOAPElementContent(String which, SOAPElement se) {
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
          /*
           * content += " "+attr.getLocalName()+" "+ se.getAttributeValue(attr);
           */
        } else {
          TestUtil.logMsg("  AttrName=" + attr.getLocalName() + " URI="
              + attr.getURI() + " AttrValue=" + se.getAttributeValue(attr));
          /*
           * content += " "+attr.getLocalName()+" "+
           * attr.getURI()+" "+se.getAttributeValue(attr);
           */
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

  public static MessageFactory getMessageFactory(String which)
      throws Exception {
    System.out.println("JAXRPC_Util:getMessageFactory");
    if (which.equals(SOAP11)) {
      if (mfactorySOAP11 == null)
        mfactorySOAP11 = MessageFactory.newInstance();
      return mfactorySOAP11;
    } else {
      /*
       * Comment out until SOAP1.2 if(mfactorySOAP12 == null) mfactorySOAP12 =
       * MessageFactory.newInstance( MessageFactory.SOAP1_2_MESSAGE_FACTORY);
       */
      return mfactorySOAP12;
    }
  }

  public static SOAPFactory getSOAPFactory(String which) throws Exception {
    System.out.println("JAXRPC_Util:getSOAPFactory");
    if (which.equals(SOAP11)) {
      if (sfactorySOAP11 == null)
        sfactorySOAP11 = SOAPFactory.newInstance();
      return sfactorySOAP11;
    } else {
      /*
       * Comment out until SOAP1.2 if(sfactorySOAP12 == null) sfactorySOAP12 =
       * SOAPFactory.newInstance( SOAPFactory.SOAP_1_2_FACTORY);
       */
      return sfactorySOAP12;
    }
  }
}
