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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_soap.SOAPFaultException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.namespace.QName;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private jakarta.xml.soap.Detail detail = null;

  private jakarta.xml.soap.DetailEntry detailentry = null;

  private jakarta.xml.soap.SOAPFault soapfault = null;

  private jakarta.xml.soap.Name name = null;

  private jakarta.xml.soap.MessageFactory msgfactory = null;

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
    try {
      // Create a soap message factory instance.
      TestUtil.logMsg("Create a SOAP MessageFactory instance");
      msgfactory = jakarta.xml.soap.MessageFactory.newInstance();

      // Create a soap message.
      TestUtil.logMsg("Create a SOAPMessage");
      jakarta.xml.soap.SOAPMessage soapmsg = msgfactory.createMessage();

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
   * @assertion_ids: JAXWS:JAVADOC:113;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Verify
   * SOAPFaultException object created successfully.
   */
  public void SOAPFaultExceptionConstructorTest() throws Fault {
    TestUtil.logTrace("SOAPFaultExceptionConstructorTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Create instance via SOAPFaultException(jakarta.xml.soap.SOAPFault");
    SOAPFaultException sf = new SOAPFaultException(soapfault);
    if (sf != null) {
      TestUtil.logMsg("SOAPFaultException object created successfully");
    } else {
      TestUtil.logErr("SOAPFaultException object not created");
      pass = false;
    }

    if (!pass)
      throw new Fault("SOAPFaultExceptionConstructorTest failed");
  }

  /*
   * @testName: getFaultTest
   *
   * @assertion_ids: JAXWS:JAVADOC:112;
   *
   * @test_Strategy: Create instance via SOAPFaultException constructor. Get the
   * embedded SOAPFault instance and verify it is what was set.
   */
  public void getFaultTest() throws Fault {
    TestUtil.logTrace("getFaultTest");
    SOAPFault theFault;
    boolean pass = true;
    TestUtil.logMsg(
        "Create instance via SOAPFaultException(jakarta.xml.soap.SOAPFault");
    SOAPFaultException sf = new SOAPFaultException(soapfault);
    if (sf != null) {
      TestUtil.logMsg("SOAPFaultException object created successfully");
      theFault = sf.getFault();
      if (theFault.equals(soapfault)) {
        TestUtil.logMsg("SOAPFault returned match");
      } else {
        TestUtil.logErr("SOAPFault returned mismatch - expected: " + soapfault
            + ", received: " + theFault);
        pass = false;
      }
    } else {
      TestUtil.logErr("SOAPFaultException object not created");
      pass = false;
    }

    if (!pass)
      throw new Fault("getFaultTest failed");
  }
}
