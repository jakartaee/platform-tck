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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_import_test;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

/**
 *
 * @author JAX-RPC RI Development Team
 */
public class ImportTest2 extends TestCaseBase {

  private final static String TEST09_WSDL_DOCUMENT = "test09.wsdl";

  private final static String TEST12_WSDL_DOCUMENT = "test12.wsdl";

  private final static String STOCKQUOTESERVICE_WSDL_DOCUMENT = "stockquoteservice.wsdl";

  private final static String INTEROPTEST_WSDL_DOCUMENT = "interoptest.wsdl";

  private final static String INTEROPTEST_UPDATED_WSDL_DOCUMENT = "interoptest_updated.wsdl";

  private final static String IONAB_WSDL_DOCUMENT = "ionaB.wsdl";

  private final static String UDDI_INQUIRE_WSDL_DOCUMENT = "inquire_v1.wsdl";

  private final static String GOOGLE_WSDL_DOCUMENT = "GoogleSearch.wsdl";

  private final static String AMAZON_WSDL_DOCUMENT = "AmazonWebServices.wsdl";

  private final static String TEST14_WSDL_DOCUMENT = "test14.wsdl";

  private final static String TEST15_WSDL_DOCUMENT = "test15.wsdl";

  private final static String TEST16_WSDL_DOCUMENT = "test16.wsdl";

  private final static String FAULTSERVICE_WSDL_DOCUMENT = "Fault_Service.wsdl";

  public ImportTest2(String name) {
    super(name);
  }

  public void runTests() {
    try {
      testImportDotNetDocumentLiteral();
    } catch (Exception e) {
    }
    try {
      testImportDotNetRpcEncoded();
    } catch (Exception e) {
    }
    try {
      testImportAnnotatedSimpleType();
    } catch (Exception e) {
    }
    try {
      testImportSimpleReturnDocLiteral();
    } catch (Exception e) {
    }
    try {
      testImportStockQuoteService();
    } catch (Exception e) {
    }
    try {
      testImportInterop();
    } catch (Exception e) {
    }
    try {
      testImportUpdatedInterop();
    } catch (Exception e) {
    }
    try {
      testFaultServiceSearchSchema();
    } catch (Exception e) {
    }
    try {
      testIonaB();
    } catch (Exception e) {
    }
    try {
      testImportUDDI();
    } catch (Exception e) {
    }
    try {
      testImportUDDI2();
    } catch (Exception e) {
    }
    try {
      testImportGoogle();
    } catch (Exception e) {
    }
    try {
      testImportGoogle2();
    } catch (Exception e) {
    }
    try {
      testImportAmazon();
    } catch (Exception e) {
    }
    try {
      testImportAmazon2();
    } catch (Exception e) {
    }
    try {
      testAdditionalSchemaTypes2();
    } catch (Exception e) {
    }
    try {
      testAdditionalSchemaTypes();
    } catch (Exception e) {
    }
  }

  public void testImportDotNetDocumentLiteral() throws Exception {
    importWSDL(TEST09_WSDL_DOCUMENT);
  }

  public void testImportDotNetRpcEncoded() throws Exception {
    importWSDL(TEST12_WSDL_DOCUMENT);
  }

  public void testImportAnnotatedSimpleType() throws Exception {
    importWSDL(TEST15_WSDL_DOCUMENT);
  }

  public void testImportSimpleReturnDocLiteral() throws Exception {
    importWSDL(TEST14_WSDL_DOCUMENT);
  }

  public void testImportStockQuoteService() throws Exception {
    importWSDL(STOCKQUOTESERVICE_WSDL_DOCUMENT);
  }

  public void testImportInterop() throws Exception {
    importWSDL(INTEROPTEST_WSDL_DOCUMENT);
  }

  public void testImportUpdatedInterop() throws Exception {
    importWSDL(INTEROPTEST_UPDATED_WSDL_DOCUMENT);
  }

  /*
   * This test suite has been added in to test for searchschema for wsdl modeler
   */
  public void testFaultServiceSearchSchema() throws Exception {
    importWSDL(FAULTSERVICE_WSDL_DOCUMENT);
  }

  public void testIonaB() throws Exception {
    importWSDL(IONAB_WSDL_DOCUMENT);
  }

  public void testImportUDDI() throws Exception {
    importWSDL(UDDI_INQUIRE_WSDL_DOCUMENT);
  }

  public void testImportUDDI2() throws Exception {
    importWSDLWsCompileImport(UDDI_INQUIRE_WSDL_DOCUMENT);
  }

  public void testImportGoogle() throws Exception {
    importWSDL(GOOGLE_WSDL_DOCUMENT);
  }

  public void testImportGoogle2() throws Exception {
    importWSDLWsCompileImport(GOOGLE_WSDL_DOCUMENT);
  }

  public void testImportAmazon() throws Exception {
    importWSDL(AMAZON_WSDL_DOCUMENT);
  }

  public void testImportAmazon2() throws Exception {
    importWSDLWsCompileImport(AMAZON_WSDL_DOCUMENT);
  }

  /*
   * the wsdl used in the following 2 testcase will not pass with
   * -f:searchschema as used here in this unit test. As these wsdl have a mix of
   * literal and encoded use in their operations. Thus they are made to fail
   * gracefully
   */

  public void testAdditionalSchemaTypes2() throws Exception {
    importWSDLWsCompileImport(TEST16_WSDL_DOCUMENT);
  }

  public void testAdditionalSchemaTypes() throws Exception {
    importSearchSchemaWSDL(TEST16_WSDL_DOCUMENT);
  }
}
