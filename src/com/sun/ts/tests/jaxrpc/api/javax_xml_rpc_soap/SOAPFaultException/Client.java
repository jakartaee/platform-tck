/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_soap.SOAPFaultException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.namespace.QName;
import javax.xml.rpc.soap.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  private javax.xml.soap.Detail detail = null;

  private javax.xml.soap.Detail detail2 = null;

  private javax.xml.soap.DetailEntry detailentry = null;

  private javax.xml.soap.SOAPFault soapfault = null;

  private javax.xml.soap.Name name = null;

  private javax.xml.soap.MessageFactory msgfactory = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance");
      msgfactory = javax.xml.soap.MessageFactory.newInstance();

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      javax.xml.soap.SOAPMessage soapmsg = msgfactory.createMessage();

      // Retrieve the soap part from the soap message..
      TestUtil.logMsg("Get SOAP Part");
      javax.xml.soap.SOAPPart sp = soapmsg.getSOAPPart();

      // Retrieve the envelope from the soap part.
      TestUtil.logMsg("Get SOAP Envelope");
      javax.xml.soap.SOAPEnvelope envelope = sp.getEnvelope();

      // Retrieve the soap header from the envelope.
      TestUtil.logMsg("Get SOAP Header");
      javax.xml.soap.SOAPHeader hdr = envelope.getHeader();

      // Retrieve the soap body from the envelope.
      TestUtil.logMsg("Get SOAP Body");
      javax.xml.soap.SOAPBody body = envelope.getBody();

      // Add a soap fault to the soap body.
      soapfault = body.addFault();

      // Add a detail to the soap fault.
      detail = soapfault.addDetail();
      name = envelope.createName("GetLastTradePrice", "WOMBAT",
          "http://www.wombat.org/trader");
      detailentry = detail.addDetailEntry(name);
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: SOAPFaultExceptionConstructorTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:246; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Verify
   * SOAPFaultException object created successfully.
   */
  public void SOAPFaultExceptionConstructorTest() throws Fault {
    TestUtil.logTrace("SOAPFaultExceptionConstructorTest");
    boolean pass = true;
    QName faultCode = new QName("http://foo.bar", "faultCode");
    try {
      TestUtil.logMsg("Create instance via SOAPFaultException(QName faultCode,"
          + "String faultString,\nString faultActor, javax.xml.soap.Detail "
          + "detail) ...");
      SOAPFaultException sf = new SOAPFaultException(faultCode, "faultString",
          "faultActor", detail);
      if (sf != null) {
        TestUtil.logMsg("SOAPFaultException object created successfully");
      } else {
        TestUtil.logErr("SOAPFaultException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPFaultExceptionConstructorTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPFaultExceptionConstructorTest failed");
  }

  /*
   * @testName: getFaultActorTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:249; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Get the
   * faultactor element and verify it is what was set.
   */
  public void getFaultActorTest() throws Fault {
    TestUtil.logTrace("getFaultActorTest");
    QName faultCode = new QName("http://foo.bar", "faultCode");
    String expected = "faultActor";
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via SOAPFaultException(QName faultCode,"
          + "String faultString,\nString faultActor, javax.xml.soap.Detail "
          + "detail) ...");
      SOAPFaultException sf = new SOAPFaultException(faultCode, "faultString",
          "faultActor", detail);
      if (sf != null) {
        TestUtil.logMsg("SOAPFaultException object created successfully");
        String s = sf.getFaultActor();
        if (s.equals(expected))
          TestUtil.logMsg("FaultActor returned as expected: " + s);
        else {
          TestUtil
              .logErr("FaultActor expected " + expected + ", received " + s);
          pass = false;
        }
      } else {
        TestUtil.logErr("SOAPFaultException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getFaultActorTest failed", e);
    }

    if (!pass)
      throw new Fault("getFaultActorTest failed");
  }

  /*
   * @testName: getFaultStringTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:248; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Get the
   * faultstring element and verify it is what was set.
   */
  public void getFaultStringTest() throws Fault {
    TestUtil.logTrace("getFaultStringTest");
    QName faultCode = new QName("http://foo.bar", "faultCode");
    String expected = "faultString";
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via SOAPFaultException(QName faultCode,"
          + "String faultString,\nString faultActor, javax.xml.soap.Detail "
          + "detail) ...");
      SOAPFaultException sf = new SOAPFaultException(faultCode, "faultString",
          "faultActor", detail);
      if (sf != null) {
        TestUtil.logMsg("SOAPFaultException object created successfully");
        String s = sf.getFaultString();
        if (s.equals(expected))
          TestUtil.logMsg("FaultString returned as expected: " + s);
        else {
          TestUtil
              .logErr("FaultString expected " + expected + ", received " + s);
          pass = false;
        }
      } else {
        TestUtil.logErr("SOAPFaultException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getFaultStringTest failed", e);
    }

    if (!pass)
      throw new Fault("getFaultStringTest failed");
  }

  /*
   * @testName: getFaultCodeTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:330; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Get the
   * faultcode element and verify it is what was set.
   */
  public void getFaultCodeTest() throws Fault {
    TestUtil.logTrace("getFaultCodeTest");
    QName faultCode = new QName("http://foo.bar", "faultCode");
    QName expected = faultCode;
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via SOAPFaultException(QName faultCode,"
          + "String faultString,\nString faultActor, javax.xml.soap.Detail "
          + "detail) ...");
      SOAPFaultException sf = new SOAPFaultException(faultCode, "faultString",
          "faultActor", detail);
      if (sf != null) {
        TestUtil.logMsg("SOAPFaultException object created successfully");
        QName s = sf.getFaultCode();
        if (s.equals(faultCode))
          TestUtil.logMsg("FaultCode returned as expected: " + s);
        else {
          TestUtil.logErr("FaultCode expected " + expected + ", received " + s);
          pass = false;
        }
      } else {
        TestUtil.logErr("SOAPFaultException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getFaultCodeTest failed", e);
    }

    if (!pass)
      throw new Fault("getFaultCodeTest failed");
  }

  /*
   * @testName: getDetailTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:250; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Get the
   * detail element and verify it is what was set.
   */
  public void getDetailTest() throws Fault {
    TestUtil.logTrace("getDetailTest");
    QName faultCode = new QName("http://foo.bar", "faultCode");
    String expected = "faultString";
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via SOAPFaultException(QName faultCode,"
          + "String faultString,\nString faultActor, javax.xml.soap.Detail "
          + "detail) ...");
      SOAPFaultException sf = new SOAPFaultException(faultCode, "faultString",
          "faultActor", detail);
      TestUtil.logMsg("..........detail=" + detail);
      if (sf != null) {
        TestUtil.logMsg("SOAPFaultException object created successfully");
        TestUtil.logMsg("Get Detail element");
        detail2 = sf.getDetail();
        TestUtil.logMsg("..........detail2=" + detail2);
        TestUtil.logMsg("Get DetailEntry element");
        Iterator i = detail2.getDetailEntries();
        if (i == null || !i.hasNext()) {
          TestUtil.logErr("No DetailEntry in Detail object");
          pass = false;
        } else {
          javax.xml.soap.DetailEntry detailentry = (javax.xml.soap.DetailEntry) i
              .next();
          TestUtil.logMsg("Get Name element");
          javax.xml.soap.Name n = detailentry.getElementName();
          String lname = n.getLocalName();
          String prefix = n.getPrefix();
          String uri = n.getURI();
          TestUtil.logMsg("Verify Name element of Detail element");
          TestUtil.logMsg(
              "LocalName=" + lname + " Prefix=" + prefix + " URI=" + uri);
          if (lname.equals("GetLastTradePrice") && prefix.equals("WOMBAT")
              && uri.equals("http://www.wombat.org/trader")) {
            TestUtil.logMsg("Name element of Detail element is correct");
          } else {
            TestUtil.logErr("Name element of Detail element is in correct");
            pass = false;
          }
        }
      } else {
        TestUtil.logErr("SOAPFaultException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getDetailTest failed", e);
    }

    if (!pass)
      throw new Fault("getDetailTest failed");
  }
}
