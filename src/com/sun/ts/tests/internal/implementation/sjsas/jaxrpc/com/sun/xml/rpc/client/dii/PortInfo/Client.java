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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.PortInfo;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.dii.*;

public class Client extends EETest {
  private Properties props = null;

  private static final String NS_URI = "http://hellotestservice.org/wsdl";

  private static final String ENDPOINT_URL = "http://localhost:8000/HS/Hello";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORTTYPE_NAME = "Hello";

  private static final QName PORT_QNAME = new QName(NS_URI, PORT_NAME);

  private static final QName PORTTYPE_QNAME = new QName(NS_URI, PORTTYPE_NAME);

  private static final String OP_NAME1 = "hello";

  private static final String OP_NAME2 = "bye";

  private static final String OP_UNK = "unknown";

  PortInfo portInfo;

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
    portInfo = new PortInfo(PORT_QNAME);
    if (portInfo == null)
      throw new Fault("setup failed, unable to create PortInfo object");
    else {
      logMsg("Call PortInfo.setDefaultNamespace(String) method.");
      portInfo.setDefaultNamespace(NS_URI);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: PortInfoTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void PortInfoTest() throws Fault {
    TestUtil.logTrace("PortInfoTest");
    boolean pass = true;
    PortInfo pi;
    try {
      TestUtil.logMsg("Call PortInfo(QName name) constructor");
      pi = new PortInfo(PORT_QNAME);
      if (pi == null) {
        TestUtil.logErr("failed to create PortInfo object");
        pass = false;
      } else {
        TestUtil.logMsg("Verify PortInfo.getName() method");
        QName v = pi.getName();
        if (!PORT_QNAME.equals(v)) {
          TestUtil.logErr("PortInfo.getName() yields different value");
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("PortInfoTest failed", e);
    }

    if (!pass)
      throw new Fault("PortInfoTest failed");
  }

  /*
   * @testName: setGetTargetEndpointTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setGetTargetEndpointTest() throws Fault {
    TestUtil.logTrace("setGetTargetEndpointTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call PortInfo.setTargetEndpoint(String) method");
      portInfo.setTargetEndpoint(ENDPOINT_URL);
      TestUtil.logMsg("Call PortInfo.getTargetEndpoint() method");
      String v = portInfo.getTargetEndpoint();
      if (!ENDPOINT_URL.equals(v)) {
        TestUtil.logErr("PortInfo.getTargetEndpoint() yields different value");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("setGetTargetEndpointTest failed", e);
    }

    if (!pass)
      throw new Fault("setGetTargetEndpointTest failed");
  }

  /*
   * @testName: setGetPortTypeNameTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setGetPortTypeNameTest() throws Fault {
    TestUtil.logTrace("setGetPortTypeNameTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call PortInfo.setPortTypeName(QName) method");
      portInfo.setPortTypeName(PORTTYPE_QNAME);
      TestUtil.logMsg("Call PortInfo.getPortTypeName() method");
      QName v = portInfo.getPortTypeName();
      if (!PORTTYPE_QNAME.equals(v)) {
        TestUtil.logErr("PortInfo.getPortTypeName() yields different value");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("setGetPortTypeNameTest failed", e);
    }

    if (!pass)
      throw new Fault("setGetPortTypeNameTest failed");
  }

  /*
   * @testName: opNameTestsTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void opNameTestsTest() throws Fault {
    TestUtil.logTrace("opNameTestsTest");
    boolean pass = true;
    OperationInfo oi1, oi2;
    try {
      TestUtil.logMsg("Call PortInfo.createOperationForName(String) method");
      oi1 = portInfo.createOperationForName(OP_NAME1);
      oi2 = portInfo.createOperationForName(OP_NAME2);
      TestUtil.logMsg("Call PortInfo.getOperationCount() method");
      int cnt = portInfo.getOperationCount();
      TestUtil.logMsg("cnt=" + cnt);
      TestUtil.logMsg("Call PortInfo.getOperations() method");
      Iterator iter = portInfo.getOperations();
      int i = 1;
      while (iter.hasNext()) {
        TestUtil.logMsg("Operation" + i + "=" + iter.next());
        i++;
      }
      TestUtil.logMsg("Call PortInfo.isOperationKnown(String) method");
      if (!portInfo.isOperationKnown(OP_NAME1)) {
        TestUtil.logErr("Operation " + OP_NAME1 + " should be known");
        pass = false;
      }
      if (portInfo.isOperationKnown(OP_UNK)) {
        TestUtil.logErr("Operation " + OP_UNK + " should be unknown");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("opNameTestsTest failed", e);
    }

    if (!pass)
      throw new Fault("opNameTestsTest failed");
  }
}
