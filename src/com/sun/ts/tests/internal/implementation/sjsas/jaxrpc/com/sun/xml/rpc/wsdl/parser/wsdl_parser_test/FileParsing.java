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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.parser.wsdl_parser_test;

import java.io.*;
import java.net.MalformedURLException;
import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.parser.WSDLParser;
import com.sun.xml.rpc.wsdl.framework.ParseException;
import com.sun.xml.rpc.wsdl.framework.ValidationException;
import com.sun.xml.rpc.wsdl.document.WSDLDocument;
import com.sun.xml.rpc.wsdl.parser.SOAPEntityReferenceValidator;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 *
 * @author JAX-RPC RI Development Team
 */
public class FileParsing {

  private final static String FS = System.getProperty("file.separator");

  private final static String PS = System.getProperty("path.separator");

  private static final String BASEDIR = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl";

  private static final String TEST_DOCUMENT_DIR = "data" + FS + "test" + FS
      + "etc" + FS + "wsdl" + FS;

  private static final String TEST_DOC_1 = "test01.wsdl";

  private static final String TEST_DOC_2 = "test02.wsdl";

  private static final String TEST_DOC_3 = "test03.wsdl";

  private static final String TEST_DOC_4 = "test04.wsdl";

  private static final String TEST_DOC_5 = "test05.wsdl";

  private static final String TEST_DOC_6 = "test06.wsdl";

  private static final String TEST_DOC_7 = "test07.wsdl";

  private static final String TEST_DOC_8 = "test08.wsdl";

  private static final String TEST_DOC_9 = "test09.wsdl";

  private static final String TEST_DOC_10 = "test10.wsdl";

  private static final String TEST_DOC_11 = "test11.wsdl";

  private static final String TEST_DOC_12 = "test12.wsdl";

  private static final String TEST_DOC_13 = "test13.wsdl";

  private static final String TEST_DOC_17 = "test17.wsdl";

  private static final String TEST_DOC_18 = "test18.wsdl";

  private static final String TEST_DOC_19 = "test19.wsdl";

  private static final String TEST_DOC_20 = "test20.wsdl";

  private static final String TEST_DOC_STOCKQUOTE_SERVICE = "stockquoteservice.wsdl";

  private static final String TEST_DOC_MIME_TYPES_MULTIPART = "mimetypesmultipart.wsdl";

  private String _tsHome;

  private String _baseDir;

  private int fail = 0;

  private void fail(String s) {
    TestUtil.logErr(s);
    fail++;
  }

  public FileParsing(String tsHome) {
    _tsHome = tsHome;
    _baseDir = tsHome + "/" + BASEDIR + "/"
        + TEST_DOCUMENT_DIR.replaceAll("/", FS);
  }

  public int getFail() {
    return fail;
  }

  public void runTests() {
    try {
      testEmptyDefsDefaultNamespace();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testEmptyDefsNoDefaultNamespace();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testEmptyDefsWithTypes();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testExtension();
    } catch (Exception e) {
      fail("Exception: " + e);
    }

    // ParseException test
    try {
      testRequiredExtension();
    }

    catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testTestService();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testTestService2();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testSpecSample();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testDotNetDocumentLiteral();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testDotNetDocumentLiteral2();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testDotNetDocumentLiteral3();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testDotNetRpcEncoded();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testDotNetRpcEncoded2();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testStockQuoteService();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testMimeTypesMultipart();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
    try {
      testXSDInclude();
    } catch (Exception e) {
      fail("Exception: " + e);
    }

    // ValidationException test
    try {
      testInvalidBindingForPort();
    } catch (Exception e) {
      fail("Exception: " + e);
    }

    // ParseException test
    try {
      testMissingSOAPAddressLocation();
    } catch (Exception e) {
      fail("Exception: " + e);
    }

    // ValidationException test
    try {
      testInvalidMessage();
    } catch (Exception e) {
      fail("Exception: " + e);
    }
  }

  public void testEmptyDefsDefaultNamespace() throws MalformedURLException {
    doParse(TEST_DOC_1);
  }

  public void testEmptyDefsNoDefaultNamespace() throws MalformedURLException {
    doParse(TEST_DOC_2);
  }

  public void testEmptyDefsWithTypes() throws MalformedURLException {
    doParse(TEST_DOC_3);
  }

  public void testExtension() throws MalformedURLException {
    doParse(TEST_DOC_4);
  }

  public void testRequiredExtension() throws MalformedURLException {
    failToParse(TEST_DOC_5);
  }

  public void testTestService() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_6);
  }

  public void testTestService2() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_7);
  }

  public void testSpecSample() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_8);
  }

  public void testDotNetDocumentLiteral() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_9);
  }

  public void testDotNetDocumentLiteral2() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_10);
  }

  public void testDotNetDocumentLiteral3() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_11);
  }

  public void testDotNetRpcEncoded() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_12);
  }

  public void testDotNetRpcEncoded2() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_13);
  }

  public void testStockQuoteService() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_STOCKQUOTE_SERVICE);
  }

  public void testMimeTypesMultipart() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_MIME_TYPES_MULTIPART);
  }

  public void testXSDInclude() throws MalformedURLException {
    doParseAndValidate(TEST_DOC_17);
  }

  public void testInvalidBindingForPort() throws MalformedURLException {
    failToValidate(TEST_DOC_18);
  }

  public void testMissingSOAPAddressLocation() throws MalformedURLException {
    failToParse(TEST_DOC_19);
  }

  public void testInvalidMessage() throws MalformedURLException {
    failToValidate(TEST_DOC_20);
  }

  protected WSDLDocument doParse(String filename) throws MalformedURLException {
    TestUtil.logMsg("Parse WSDL document: " + filename);
    WSDLParser parser = new WSDLParser();
    InputSource inputSource = new InputSource(
        new File(_baseDir + filename).toURL().toString());
    WSDLDocument document = parser.parse(inputSource);
    document.validateLocally();
    return document;
  }

  protected void doValidate(WSDLDocument document) {
    // global validation
    document.validate(new SOAPEntityReferenceValidator());
  }

  protected void doParseAndValidate(String filename)
      throws MalformedURLException {
    doValidate(doParse(filename));
  }

  protected void failToParse(String filename) throws MalformedURLException {
    try {
      doParse(filename);
      fail("parsing-should-have-failed");
    } catch (ParseException e) {
      TestUtil.logMsg("ParseException occurred as expected: " + e);
    } catch (ValidationException e) {
      fail("parsing-should-have-failed: " + e);
    }
  }

  protected void failToValidate(String filename) throws MalformedURLException {
    try {
      doValidate(doParse(filename));
      fail("validation-should-have-failed");
    } catch (ParseException e) {
      fail("validation-should-have-failed: " + e);
    } catch (ValidationException e) {
      TestUtil.logMsg("ValidationException occurred as expected: " + e);
    }
  }
}
