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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.document.soap.soaptests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.framework.*;
import com.sun.xml.rpc.wsdl.document.soap.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends EETest {
  int fail;

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
      logMsg("tsHome=" + tsHome);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    fail = 0;
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private int iteratorSize(Iterator i) {
    int cnt = 0;
    while (i.hasNext()) {
      cnt++;
      i.next();
    }
    return cnt;
  }

  private void assertIt(String s, boolean b) {
    if (!b) {
      TestUtil.logMsg("FAILED ... " + s);
      fail++;
    } else {
      TestUtil.logMsg("PASSED ... " + s);
    }
  }

  /*
   * @testName: SOAPOperationTests
   *
   * @assertItion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPOperationTests() throws Fault {
    TestUtil.logTrace("SOAPOperationTests");
    boolean pass = true;
    String SOAPACTION = "mySOAPAction";

    try {
      SOAPOperation so = new SOAPOperation();
      TestUtil.logMsg("ElementName=" + so.getElementName());
      so.setSOAPAction(SOAPACTION);
      String soapAction = so.getSOAPAction();
      assertIt("set/getSOAPAction", soapAction.equals(SOAPACTION));
      so.setStyle(SOAPStyle.DOCUMENT);
      SOAPStyle soapStyle = so.getStyle();
      assertIt("set/getSOAPStyle", soapStyle == SOAPStyle.DOCUMENT);
      assertIt("isDocument", so.isDocument());
      assertIt("!isRPC", !so.isRPC());
      so.validateThis();
      if (fail > 0)
        pass = false;
    } catch (Exception e) {
      throw new Fault("SOAPOperationTests failed", e);
    }

    if (!pass)
      throw new Fault("SOAPOperationTests failed");
  }

  /*
   * @testName: SOAPHeaderFaultTests
   *
   * @assertItion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPHeaderFaultTests() throws Fault {
    TestUtil.logTrace("SOAPHeaderFaultTests");
    boolean pass = true;
    String NAMESPACE = "uri:mynamespace";
    String STYLE = "rpc";
    String PART = "myPArt";
    QName MESSAGE = new QName(NAMESPACE, "myMessage");

    try {
      SOAPHeaderFault shf = new SOAPHeaderFault();
      TestUtil.logMsg("ElementName=" + shf.getElementName());
      shf.setNamespace(NAMESPACE);
      String namespace = shf.getNamespace();
      assertIt("set/getNamespace", namespace.equals(NAMESPACE));
      shf.setUse(SOAPUse.ENCODED);
      SOAPUse soapUse = shf.getUse();
      assertIt("set/getSOAPUse", soapUse == SOAPUse.ENCODED);
      assertIt("isEncoded", shf.isEncoded());
      assertIt("!isLiteral", !shf.isLiteral());
      shf.setEncodingStyle(STYLE);
      String encodingStyle = shf.getEncodingStyle();
      assertIt("set/getEncodingStyle", encodingStyle == STYLE);
      shf.setPart(PART);
      String part = shf.getPart();
      assertIt("set/getPart", part == PART);
      shf.setMessage(MESSAGE);
      QName message = shf.getMessage();
      assertIt("set/getMessage", message.equals(MESSAGE));
      shf.validateThis();
      if (fail > 0)
        pass = false;
    } catch (Exception e) {
      throw new Fault("SOAPHeaderFaultTests failed", e);
    }

    if (!pass)
      throw new Fault("SOAPHeaderFaultTests failed");
  }

  /*
   * @testName: SOAPHeaderTests
   *
   * @assertItion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPHeaderTests() throws Fault {
    TestUtil.logTrace("SOAPHeaderTests");
    boolean pass = true;
    String NAMESPACE = "uri:mynamespace";
    String STYLE = "rpc";
    String PART = "myPArt";
    QName MESSAGE = new QName(NAMESPACE, "myMessage");

    try {
      SOAPHeaderFault shf = new SOAPHeaderFault();
      shf.setNamespace(NAMESPACE);
      shf.setUse(SOAPUse.ENCODED);
      shf.setEncodingStyle(STYLE);
      shf.setPart(PART);
      shf.setMessage(MESSAGE);
      SOAPHeader sh = new SOAPHeader();
      TestUtil.logMsg("ElementName=" + sh.getElementName());
      Iterator faults = sh.faults();
      assertIt("faults iterator 0", iteratorSize(faults) == 0);
      sh.add(shf);
      faults = sh.faults();
      assertIt("faults iterator 1", iteratorSize(faults) == 1);
      sh.setNamespace(NAMESPACE);
      String namespace = sh.getNamespace();
      assertIt("set/getNamespace", namespace.equals(NAMESPACE));
      sh.setUse(SOAPUse.ENCODED);
      SOAPUse soapUse = sh.getUse();
      assertIt("set/getSOAPUse", soapUse == SOAPUse.ENCODED);
      assertIt("isEncoded", sh.isEncoded());
      assertIt("!isLiteral", !sh.isLiteral());
      sh.setEncodingStyle(STYLE);
      String encodingStyle = sh.getEncodingStyle();
      assertIt("set/getEncodingStyle", encodingStyle == STYLE);
      sh.setPart(PART);
      String part = sh.getPart();
      assertIt("set/getPart", part == PART);
      sh.setMessage(MESSAGE);
      QName message = sh.getMessage();
      assertIt("set/getMessage", message.equals(MESSAGE));
      sh.validateThis();
      if (fail > 0)
        pass = false;
    } catch (Exception e) {
      throw new Fault("SOAPHeaderTests failed", e);
    }

    if (!pass)
      throw new Fault("SOAPHeaderTests failed");
  }

  /*
   * @testName: SOAPFaultTests
   *
   * @assertItion_ids:
   *
   * @test_Strategy:
   */
  public void SOAPFaultTests() throws Fault {
    TestUtil.logTrace("SOAPFaultTests");
    boolean pass = true;
    String NAMESPACE = "uri:mynamespace";
    String NAME = "MyName";
    String STYLE = "rpc";
    SOAPUse USE = SOAPUse.LITERAL;

    try {
      SOAPFault sf = new SOAPFault();
      sf.setNamespace(NAMESPACE);
      sf.setUse(USE);
      sf.setEncodingStyle(STYLE);
      sf.setName(NAME);
      TestUtil.logMsg("ElementName=" + sf.getElementName());
      assertIt("set/getUse", sf.getUse() == USE);
      assertIt("set/getName", sf.getName().equals(NAME));
      assertIt("set/getNamespace", sf.getNamespace().equals(NAMESPACE));
      assertIt("set/getEncodingStyle", sf.getEncodingStyle().equals(STYLE));
      assertIt("!isEncoded", !sf.isEncoded());
      assertIt("isLiteral", sf.isLiteral());
      sf.validateThis();
      if (fail > 0)
        pass = false;
    } catch (Exception e) {
      throw new Fault("SOAPFaultTests failed", e);
    }

    if (!pass)
      throw new Fault("SOAPFaultTests failed");
  }
}
