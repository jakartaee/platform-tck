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

package com.sun.ts.tests.internal.implementation.sjsas.saaj.com.sun.xml.messaging.saaj.soap.impl.impltests;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
// Import implementation specific classes to test
import com.sun.xml.messaging.saaj.soap.*;
import com.sun.xml.messaging.saaj.soap.impl.*;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPMessage;

public class Client extends EETest {
  private Properties props = null;

  private String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/saaj/com/sun/xml/messaging/saaj/soap/impl/impltests";

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
   * @testName: CDATAImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void CDATAImplTest() throws Fault {
    TestUtil.logTrace("CDATAImplTest");
    boolean pass = true;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      org.w3c.dom.Node node = null;
      SOAPMessage msg = MessageFactory.newInstance().createMessage();
      SOAPDocumentImpl sdi = new SOAPDocumentImpl(
          (SOAPPartImpl) msg.getSOAPPart());
      TestUtil.logMsg("Call CDATAImpl(SOAPDocumentImpl, String) constructor");
      CDATAImpl cdata = new CDATAImpl(sdi, "This is my text string 1");
      TestUtil.logMsg("Call CDATAImpl.getValue() method");
      String value = cdata.getValue();
      TestUtil.logMsg("Call CDATAImpl.setValue(String) method");
      cdata.setValue("This is my test string 2");
      TestUtil.logMsg("Call CDATAImpl.setParentElement(SOAPElement) method");
      SOAPElement p = cdata.getParentElement();
      try {
        cdata.setParentElement(p);
      } catch (Exception e) {
      }
      TestUtil.logMsg("Call CDATAImpl.getParentElement() method");
      p = cdata.getParentElement();
      TestUtil.logMsg("Call CDATAImpl.detachNode() method");
      cdata.detachNode();
      TestUtil.logMsg("Call CDATAImpl.recycleNode() method");
      cdata.recycleNode();
      TestUtil.logMsg("Call CDATAImpl.isComment() method");
      boolean b = cdata.isComment();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CDATAImplTest failed", e);
    }

    if (!pass)
      throw new Fault("CDATAImplTest failed");
  }

  /*
   * @testName: TreeExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void TreeExceptionTest() throws Fault {
    TestUtil.logTrace("TreeExceptionTest");
    boolean pass = true;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      TestUtil.logMsg("Call TreeException(String) constructor");
      TreeException tx = new TreeException("This is my TreeException!");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TreeExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("TreeExceptionTest failed");
  }
}
