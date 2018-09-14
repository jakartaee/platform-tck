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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.framework.WriterContext;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.framework.*;

public class Client extends EETest {
  private Properties props = null;

  private final static String DEFAULT_NAMESPACE = "uri:defaultnamespace";

  private final static String DEFAULT_PREFIX = "";

  private final static String MY_NAMESPACE = "uri:mynamespace";

  private final static String MY_PREFIX = "myn";

  private final static String NAME1 = "name1";

  private final static String NAME2 = "name2";

  private final static QName QNAME1 = new QName(MY_NAMESPACE, NAME1);

  private final static QName QNAME2 = new QName(MY_NAMESPACE, NAME2);

  private final static String ATTR1 = "attribute1";

  private final static String VALU1 = "value1";

  private final static String ATTR2 = "attribute2";

  private final static QName VALU2 = new QName(MY_NAMESPACE, "value2");

  private final static String ATTR3 = "attribute3";

  private final static boolean VALU3 = true;

  private final static String ATTR4 = "attribute4";

  private final static Boolean VALU4 = new Boolean(true);

  private final static String ATTR5 = "attribute5";

  private final static int VALU5 = 5;

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
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  public void assertIt(String s, boolean b) {
    if (b)
      TestUtil.logMsg("PASSED ... " + s);
    else
      TestUtil.logMsg("FAILED ... " + s);
    logMsg("cleanup ok");
  }

  /*
   * @testName: WriterContextTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WriterContextTest() throws Fault {
    TestUtil.logTrace("WriterContextTest");
    boolean pass = true;
    String name = "myname";
    String rb;
    DuplicateEntityException v;
    WriterContext wc;
    try {
      wc = new WriterContext(System.out);
      TestUtil.logMsg("Call getDefaultNamespaceURI()");
      assertIt("getDefaultNamespaceURI", wc.getDefaultNamespaceURI() == null);
      TestUtil.logMsg("Call declarePrefix(String, String)");
      wc.declarePrefix(DEFAULT_PREFIX, DEFAULT_NAMESPACE);
      TestUtil.logMsg("Call getDefaultNamespaceURI()");
      assertIt("getDefaultNamespaceURI",
          wc.getDefaultNamespaceURI().equals(DEFAULT_NAMESPACE));
      TestUtil.logMsg("Call setTargetNamespaceURI(String)");
      wc.setTargetNamespaceURI(MY_NAMESPACE);
      assertIt("set/getTargetNamespaceURI",
          wc.getTargetNamespaceURI().equals(MY_NAMESPACE));
      TestUtil.logMsg("Call declarePrefix(String, String)");
      wc.declarePrefix(MY_PREFIX, MY_NAMESPACE);
      assertIt("getPrefixFor", wc.getPrefixFor(MY_NAMESPACE).equals(MY_PREFIX));
      TestUtil.logMsg("Call findNewPrefix(String)");
      TestUtil.logMsg("findNewPrefix=" + wc.findNewPrefix("base"));

      TestUtil.logMsg("Call getPrefixes()");
      Iterator iter = wc.getPrefixes();
      int cnt = 0;
      while (iter.hasNext()) {
        TestUtil.logMsg("Prefix: " + iter.next());
        cnt++;
      }

      TestUtil.logMsg("Call getQNameString(QName)");
      TestUtil.logMsg(" value=" + wc.getQNameString(QNAME1));
      assertIt("getQNameString",
          wc.getQNameString(QNAME1).equals(MY_PREFIX + ":" + NAME1));
      TestUtil.logMsg("Call getQNameStringWithTargetNamespaceCheck(QName)");
      TestUtil.logMsg(
          " value=" + wc.getQNameStringWithTargetNamespaceCheck(QNAME1));
      assertIt("getQNameStringWithTargetNamespaceCheck",
          wc.getQNameStringWithTargetNamespaceCheck(QNAME1).equals(NAME1));

      assertIt("iterator cnt", cnt == 2);
      TestUtil.logMsg("Call writeStartTag(QName method)");
      wc.writeStartTag(QNAME1);
      TestUtil.logMsg("Call writeAttribute(String, String) method");
      wc.writeAttribute(ATTR1, VALU1);
      TestUtil.logMsg("Call writeAttribute(String, QName) method");
      wc.writeAttribute(ATTR2, VALU2);
      TestUtil.logMsg("Call writeAttribute(String, boolean) method");
      wc.writeAttribute(ATTR3, VALU3);
      TestUtil.logMsg("Call writeAttribute(String, Boolean) method");
      wc.writeAttribute(ATTR4, VALU4);
      TestUtil.logMsg("Call writeAttribute(String, int) method");
      wc.writeAttribute(ATTR5, VALU5);
      TestUtil.logMsg("Call writeTag(QName, String) method");
      wc.writeTag(QNAME2, "value2");
      TestUtil.logMsg("Call writeEndTag(QName method)");
      wc.writeEndTag(QNAME1);

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WriterContextTest failed", e);
    }

    if (!pass)
      throw new Fault("WriterContextTest failed");
  }
}
