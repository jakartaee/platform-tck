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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.document.http.httptests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.parser.*;
import com.sun.xml.rpc.wsdl.framework.*;
import com.sun.xml.rpc.wsdl.document.*;
import com.sun.xml.rpc.wsdl.document.http.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl/document/http/httptests/";

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
    String tsHome, tsBase;
    try {
      tsHome = p.getProperty("ts_home");
      TestUtil.logMsg("tsHome=" + tsHome);
      tsBase = tsHome + "/" + baseDir.replaceAll("/", File.separator);
      TestUtil.logMsg("tsBase=" + tsBase);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: HTTPAddressTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPAddressTest() throws Fault {
    TestUtil.logTrace("HTTPAddressTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Call HTTPAddress() constructor");
      HTTPAddress address = new HTTPAddress();
      TestUtil.logMsg("Call HTTPAddress.getElementName() method");
      QName name = address.getElementName();
      TestUtil.logMsg("Call HTTPAddress.setLocation(String) method");
      address.setLocation("location");
      TestUtil.logMsg("Call HTTPAddress.getLocation() method");
      String location = address.getLocation();
      if (!location.equals("location")) {
        TestUtil.logErr("HTTPAddress.getLocation() returned " + location
            + ", expected location");
        pass = false;
      }
      TestUtil.logMsg("Call HTTPAddressTest.validateThis() method");
      address.validateThis();
    } catch (Exception e) {
      throw new Fault("HTTPAddressTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPAddressTest failed");
  }

  /*
   * @testName: HTTPBindingTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPBindingTest() throws Fault {
    TestUtil.logTrace("HTTPBindingTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Call HTTPBinding() constructor");
      HTTPBinding binding = new HTTPBinding();
      TestUtil.logMsg("Call HTTPBinding.getElementName() method");
      QName name = binding.getElementName();
      TestUtil.logMsg("Call HTTPBinding.setVerb(String) method");
      binding.setVerb("verb");
      TestUtil.logMsg("Call HTTPBinding.getVerb() method");
      String verb = binding.getVerb();
      if (!verb.equals("verb")) {
        TestUtil.logErr(
            "HTTPBinding.getVerb() returned " + verb + ", expected verb");
        pass = false;
      }
      TestUtil.logMsg("Call HTTPBindingTest.validateThis() method");
      binding.validateThis();
    } catch (Exception e) {
      throw new Fault("HTTPBindingTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPBindingTest failed");
  }

  /*
   * @testName: HTTPOperationTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPOperationTest() throws Fault {
    TestUtil.logTrace("HTTPOperationTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Call HTTPOperation() constructor");
      HTTPOperation operation = new HTTPOperation();
      TestUtil.logMsg("Call HTTPOperation.getElementName() method");
      QName name = operation.getElementName();
      TestUtil.logMsg("Call HTTPOperation.setLocation(String) method");
      operation.setLocation("location");
      TestUtil.logMsg("Call HTTPOperation.getLocation() method");
      String location = operation.getLocation();
      if (!location.equals("location")) {
        TestUtil.logErr("HTTPOperation.getLocation() returned " + location
            + ", expected location");
        pass = false;
      }
      TestUtil.logMsg("Call HTTPOperationTest.validateThis() method");
      operation.validateThis();
    } catch (Exception e) {
      throw new Fault("HTTPOperationTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPOperationTest failed");
  }

  /*
   * @testName: HTTPUrlEncodedTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPUrlEncodedTest() throws Fault {
    TestUtil.logTrace("HTTPUrlEncodedTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Call HTTPUrlEncoded() constructor");
      HTTPUrlEncoded urlencoded = new HTTPUrlEncoded();
      TestUtil.logMsg("Call HTTPUrlEncoded.getElementName() method");
      QName name = urlencoded.getElementName();
      TestUtil.logMsg("Call HTTPUrlEncodedTest.validateThis() method");
      urlencoded.validateThis();
    } catch (Exception e) {
      throw new Fault("HTTPUrlEncodedTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPUrlEncodedTest failed");
  }

  /*
   * @testName: HTTPUrlReplacementTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPUrlReplacementTest() throws Fault {
    TestUtil.logTrace("HTTPUrlReplacementTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Call HTTPUrlReplacement() constructor");
      HTTPUrlReplacement urlreplacement = new HTTPUrlReplacement();
      TestUtil.logMsg("Call HTTPUrlReplacement.getElementName() method");
      QName name = urlreplacement.getElementName();
      TestUtil.logMsg("Call HTTPUrlReplacementTest.validateThis() method");
      urlreplacement.validateThis();
    } catch (Exception e) {
      throw new Fault("HTTPUrlReplacementTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPUrlReplacementTest failed");
  }

  /*
   * @testName: HTTPConstantsTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HTTPConstantsTest() throws Fault {
    TestUtil.logTrace("HTTPConstantsTest");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Print out HTTPConstants");
      TestUtil
          .logMsg("HTTPConstants.NS_WSDL_HTTP=" + HTTPConstants.NS_WSDL_HTTP);
      TestUtil
          .logMsg("HTTPConstants.QNAME_ADDRESS=" + HTTPConstants.QNAME_ADDRESS);
      TestUtil
          .logMsg("HTTPConstants.QNAME_BINDING=" + HTTPConstants.QNAME_BINDING);
      TestUtil.logMsg(
          "HTTPConstants.QNAME_OPERATION=" + HTTPConstants.QNAME_OPERATION);
      TestUtil.logMsg(
          "HTTPConstants.QNAME_URL_ENCODED=" + HTTPConstants.QNAME_URL_ENCODED);
      TestUtil.logMsg("HTTPConstants.QNAME_URL_REPLACEMENT="
          + HTTPConstants.QNAME_URL_REPLACEMENT);
    } catch (Exception e) {
      throw new Fault("HTTPConstantsTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPConstantsTest failed");
  }
}
