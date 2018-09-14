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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_encoding.XMLType;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.encoding.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

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
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: XMLTypeTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:81;JAXRPC:JAVADOC:82;JAXRPC:JAVADOC:83;
   * JAXRPC:JAVADOC:84;JAXRPC:JAVADOC:85;JAXRPC:JAVADOC:86;
   * JAXRPC:JAVADOC:87;JAXRPC:JAVADOC:88;JAXRPC:JAVADOC:89;
   * JAXRPC:JAVADOC:90;JAXRPC:JAVADOC:91;JAXRPC:JAVADOC:92;
   * JAXRPC:JAVADOC:93;JAXRPC:JAVADOC:94;JAXRPC:JAVADOC:95;
   * JAXRPC:JAVADOC:96;JAXRPC:JAVADOC:97;JAXRPC:JAVADOC:98;
   * JAXRPC:JAVADOC:99;JAXRPC:JAVADOC:100;JAXRPC:JAVADOC:101;
   * JAXRPC:JAVADOC:102;JAXRPC:JAVADOC:103;JAXRPC:JAVADOC:104;
   * JAXRPC:JAVADOC:105; WS4EE:SPEC:70
   *
   * @test_Strategy: Verify access to all public XMLType.
   */
  public void XMLTypeTest() throws Fault {
    TestUtil.logTrace("XMLTypeTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Verify access to all public XMLType");
      TestUtil.logMsg("XMLType.XSD_STRING=" + XMLType.XSD_STRING);
      TestUtil.logMsg("XMLType.XSD_FLOAT=" + XMLType.XSD_FLOAT);
      TestUtil.logMsg("XMLType.XSD_BOOLEAN=" + XMLType.XSD_BOOLEAN);
      TestUtil.logMsg("XMLType.XSD_DOUBLE=" + XMLType.XSD_DOUBLE);
      TestUtil.logMsg("XMLType.XSD_INTEGER=" + XMLType.XSD_INTEGER);
      TestUtil.logMsg("XMLType.XSD_INT=" + XMLType.XSD_INT);
      TestUtil.logMsg("XMLType.XSD_LONG=" + XMLType.XSD_LONG);
      TestUtil.logMsg("XMLType.XSD_SHORT=" + XMLType.XSD_SHORT);
      TestUtil.logMsg("XMLType.XSD_DECIMAL=" + XMLType.XSD_DECIMAL);
      TestUtil.logMsg("XMLType.XSD_BASE64=" + XMLType.XSD_BASE64);
      TestUtil.logMsg("XMLType.XSD_HEXBINARY=" + XMLType.XSD_HEXBINARY);
      TestUtil.logMsg("XMLType.XSD_BYTE=" + XMLType.XSD_BYTE);
      TestUtil.logMsg("XMLType.XSD_DATETIME=" + XMLType.XSD_DATETIME);
      TestUtil.logMsg("XMLType.XSD_QNAME=" + XMLType.XSD_QNAME);
      TestUtil.logMsg("XMLType.SOAP_STRING=" + XMLType.SOAP_STRING);
      TestUtil.logMsg("XMLType.SOAP_BOOLEAN=" + XMLType.SOAP_BOOLEAN);
      TestUtil.logMsg("XMLType.SOAP_DOUBLE=" + XMLType.SOAP_DOUBLE);
      TestUtil.logMsg("XMLType.SOAP_BASE64=" + XMLType.SOAP_BASE64);
      TestUtil.logMsg("XMLType.SOAP_FLOAT=" + XMLType.SOAP_FLOAT);
      TestUtil.logMsg("XMLType.SOAP_INT=" + XMLType.SOAP_INT);
      TestUtil.logMsg("XMLType.SOAP_LONG=" + XMLType.SOAP_LONG);
      TestUtil.logMsg("XMLType.SOAP_SHORT=" + XMLType.SOAP_SHORT);
      TestUtil.logMsg("XMLType.SOAP_BYTE=" + XMLType.SOAP_BYTE);
      TestUtil.logMsg("XMLType.SOAP_ARRAY=" + XMLType.SOAP_ARRAY);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("XMLTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("XMLTypeTest failed");
  }
}
