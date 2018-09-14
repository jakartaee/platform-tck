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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.parser.SchemaWSDLTests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

// Internal implementation specific classes to tes
import com.sun.xml.rpc.wsdl.parser.*;
import com.sun.xml.rpc.wsdl.framework.*;
import com.sun.xml.rpc.wsdl.document.*;
import com.sun.xml.rpc.wsdl.document.schema.*;
import org.xml.sax.InputSource;

public class Client extends EETest {
  private final static String FS = System.getProperty("file.separator");

  private final static String SCHEMA1 = "MarshallTestServiceDefs.xsd";

  private final static String WSDL1 = "MultiInterfaceTestService.wsdl";

  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl/parser/SchemaWSDLTests";

  private String tsBase;

  private SchemaDocument doParseSchema(String filename)
      throws MalformedURLException {
    TestUtil.logMsg("Parse Schema document: " + filename);
    SchemaParser parser = new SchemaParser();
    boolean followImports = parser.getFollowImports();
    parser.setFollowImports(followImports);
    InputSource inputSource = new InputSource(
        new File(tsBase + filename).toURL().toString());
    SchemaDocument document = parser.parse(inputSource);
    return document;
  }

  private WSDLDocument doParseWSDL(String filename)
      throws MalformedURLException {
    TestUtil.logMsg("Parse WSDL document: " + filename);
    WSDLParser parser = new WSDLParser();
    InputSource inputSource = new InputSource(
        new File(tsBase + WSDL1).toURL().toString());
    WSDLDocument document = parser.parse(inputSource);
    return document;
  }

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
    String tsHome;
    try {
      tsHome = p.getProperty("ts_home").replaceAll("/", FS);
      logMsg("tsHome=" + tsHome);
      tsBase = tsHome + FS + baseDir.replaceAll("/", FS) + FS;
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: SchemaWriterTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SchemaWriterTest() throws Fault {
    TestUtil.logTrace("SchemaWriterTest()");
    boolean pass = true;
    boolean status;
    try {
      SchemaDocument sd = doParseSchema(SCHEMA1);
      SchemaWriter sw = new SchemaWriter();
      sw.write(sd, System.out);
    } catch (Exception e) {
      throw new Fault("SchemaWriterTest() failed", e);
    }

    if (!pass)
      throw new Fault("SchemaWriterTest() failed");
  }

  /*
   * @testName: SchemaDocumentTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SchemaDocumentTest() throws Fault {
    TestUtil.logTrace("SchemaDocumentTest()");
    boolean pass = true;
    boolean status;
    try {
      SchemaDocument sd = doParseSchema(SCHEMA1);
      Schema schema = sd.getSchema();
      sd.setSchema(schema);
      Set set = sd.collectAllNamespaces();
      sd.validate(new SOAPEntityReferenceValidator());
    } catch (Exception e) {
      throw new Fault("SchemaDocumentTest() failed", e);
    }

    if (!pass)
      throw new Fault("SchemaDocumentTest() failed");
  }

  /*
   * @testName: WSDLDocumentTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WSDLDocumentTest() throws Fault {
    TestUtil.logTrace("WSDLDocumentTest()");
    boolean pass = true;
    boolean status;
    try {
      WSDLDocument wd = doParseWSDL(WSDL1);
    } catch (Exception e) {
      throw new Fault("WSDLDocumentTest() failed", e);
    }

    if (!pass)
      throw new Fault("WSDLDocumentTest() failed");
  }

  /*
   * @testName: WSDLWriterTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void WSDLWriterTest() throws Fault {
    TestUtil.logTrace("WSDLWriterTest()");
    boolean pass = true;
    boolean status;
    try {
      WSDLDocument wd = doParseWSDL(WSDL1);
      TestUtil.logMsg("Create WSDLWriter and write out parsed WSDL document");
      WSDLWriter ww = new WSDLWriter();
      ww.write(wd, System.out);
      TestUtil.logMsg("unregister SOAPExtensionHandler ...");
      ww.unregister(new SOAPExtensionHandler());
      TestUtil.logMsg("unregister namespaceURI for HTTPExtensionHandler ...");
      ww.unregister(new HTTPExtensionHandler().getNamespaceURI());
    } catch (Exception e) {
      throw new Fault("WSDLWriterTest() failed", e);
    }

    if (!pass)
      throw new Fault("WSDLWriterTest() failed");
  }
}
