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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.webservice.parser.ParserUtil;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import org.xml.sax.InputSource;
import com.sun.xml.rpc.client.dii.webservice.parser.*;
import com.sun.xml.rpc.streaming.*;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

public class Client extends EETest {
  private final static String FS = System.getProperty("file.separator");

  private final static String WSDL_DOC = "HelloTestService.wsdl";

  private final static String NAME = "HelloTestService";

  private final static String TARGETNAMESPACE = "http://hellotestservice.org/wsdl";

  private final static String DEFINITIONS = "definitions";

  private final static String WSDLURI = "http://schemas.xmlsoap.org/wsdl/";

  private final static QName QNAME = new QName(WSDLURI, DEFINITIONS);

  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/client/dii/webservice/parser/ParserUtil/";

  private FileInputStream fis;

  private XMLReader reader;

  private int fail;

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
      tsBase = tsHome + FS + baseDir.replaceAll("/", FS);
      logMsg("tsBase=" + tsBase);
      fis = new FileInputStream(tsBase + WSDL_DOC);
      reader = XMLReaderFactory.newInstance().createXMLReader(fis);
      reader.next();
      fail = 0;
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void assertIt(String s, boolean b) {
    if (b)
      TestUtil.logMsg("PASSED ... " + s);
    else {
      TestUtil.logMsg("FAILED ... " + s);
      fail++;
    }
  }

  /*
   * @testName: ParserUtilTests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ParserUtilTests() throws Fault {
    TestUtil.logTrace("ParserUtilTests");
    boolean pass = true;
    try {
      TestUtil.logMsg("Calling getAttribute(XMLReader, String)");
      String name = ParserUtil.getAttribute(reader, "name");
      TestUtil.logMsg("name=" + name);
      assertIt("getAttribute(XMLReader, String)", name.equals(NAME));
      TestUtil.logMsg("Calling getNonEmptyAttribute(XMLReader, String)");
      String targetNamespace = ParserUtil.getNonEmptyAttribute(reader,
          "targetNamespace");
      TestUtil.logMsg("targetNamespace=" + targetNamespace);
      assertIt("getNonEmptyAttribute(XMLReader, String)",
          targetNamespace.equals(TARGETNAMESPACE));
      TestUtil.logMsg("Calling getMandatoryAttribute(XMLReader, String)");
      targetNamespace = ParserUtil.getMandatoryAttribute(reader,
          "targetNamespace");
      TestUtil.logMsg("targetNamespace=" + targetNamespace);
      assertIt("getMandatoryAttribute(XMLReader, String)",
          targetNamespace.equals(TARGETNAMESPACE));
      TestUtil
          .logMsg("Calling getMandatoryNonEmptyAttribute(XMLReader, String)");
      name = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
      TestUtil.logMsg("name=" + name);
      assertIt("getNonEmptyAttribute(XMLReader, String)", name.equals(NAME));
      TestUtil.logMsg("Calling getQNameAttribute(XMLReader, String)");
      QName qname = ParserUtil.getQNameAttribute(reader, "name");
      TestUtil.logMsg("qname=" + qname);
      try {
        ParserUtil.fail("FAIL1", reader);
        fail++;
        TestUtil.logErr("no exception - unexpected");
      } catch (Exception e) {
        TestUtil.logMsg("Caught exception as expected: " + e);
      }
      try {
        ParserUtil.failWithFullName("FAIL2", reader);
        fail++;
        TestUtil.logErr("no exception - unexpected");
      } catch (Exception e) {
        TestUtil.logMsg("Caught exception as expected: " + e);
      }
      try {
        ParserUtil.failWithLocalName("FAIL3", reader);
        fail++;
        TestUtil.logErr("no exception - unexpected");
      } catch (Exception e) {
        TestUtil.logMsg("Caught exception as expected: " + e);
      }
      try {
        ParserUtil.failWithLocalName("FAIL4", reader, "arg4");
        fail++;
        TestUtil.logErr("no exception - unexpected");
      } catch (Exception e) {
        TestUtil.logMsg("Caught exception as expected: " + e);
      }
    } catch (Exception e) {
      throw new Fault("ParserUtilTests failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("ParserUtilTests failed");
  }
}
