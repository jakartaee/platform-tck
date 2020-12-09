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

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.api.jakarta_xml_soap.apitests;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPHeader;

public class Client extends EETest {
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
    props = p;
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: SOAPMessageTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPMessageTest() throws Fault {
    TestUtil.logTrace("SOAPMessageTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call MySOAPMessageImpl() constructor");
      MySOAPMessageImpl smi = new MySOAPMessageImpl();
      TestUtil.logMsg("Call SOAPMessage().getSOAPBody() method");
      try {
        SOAPBody body = smi.getSOAPBody();
        pass = false;
        TestUtil.logErr("Did not throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException (expected)");
      }
      TestUtil.logMsg("Call SOAPMessage().getSOAPHeader() method");
      try {
        SOAPHeader header = smi.getSOAPHeader();
        pass = false;
        TestUtil.logErr("Did not throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException (expected)");
      }
      TestUtil.logMsg("Call SOAPMessage().setProperty(String, Object) method");
      try {
        smi.setProperty("MyProperty", "MyValue");
        pass = false;
        TestUtil.logErr("Did not throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException (expected)");
      }
      TestUtil.logMsg("Call SOAPMessage().getProperty(String) method");
      try {
        Object object = smi.getProperty("MyProperty");
        pass = false;
        TestUtil.logErr("Did not throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught UnsupportedOperationException (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SOAPMessageTest failed", e);
    }

    if (!pass)
      throw new Fault("SOAPMessageTest failed");
  }
}
