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
public class ImportTest3 extends TestCaseBase {

  private final static String SCHEMA_FEATURES_WSDL_DOCUMENT = "schemafeatures.wsdl";

  private final static String SIMPLETYPE_FEATURES_WSDL_DOCUMENT = "simpleType.wsdl";

  private final static String SIMPLETYPE_FEATURES_RPCLITWSDL_DOCUMENT = "wsi-wsdl/simpleTyperpclit.wsdl";

  private final static String BASIC_VALID_WSDL_DOCUMENT = "base_modified.wsdl";

  private final static String WSITEST1_FEATURES_WSDL_DOCUMENT = "wsi-wsdl/WsiTestWSDL1.wsdl";

  private final static String WSITEST2_FEATURES_WSDL_DOCUMENT = "wsi-wsdl/WsiTestWSDL2.wsdl";

  private final static String WSITEST3_FEATURES_WSDL_DOCUMENT = "wsi-wsdl/WsiTestWSDL3.wsdl";

  private final static String WSITEST4_FEATURES_WSDL_DOCUMENT = "wsi-wsdl/WsiTestWSDL4.wsdl";

  private final static String WSITEST5_FEATURES_WSDL_DOCUMENT = "wsi-wsdl/WsiTestWSDL5.wsdl";

  public ImportTest3(String name) {
    super(name);
  }

  public void runTests() {
    try {
      testAdditionalSchemaFeatures();
    } catch (Exception e) {
    }
    try {
      testBasicValidityOFWSDL();
    } catch (Exception e) {
    }
    try {
      testWSIWsdl();
    } catch (Exception e) {
    }
    try {
      testSimpleTypeWsdl();
    } catch (Exception e) {
    }
  }

  public void testAdditionalSchemaFeatures() throws Exception {

    importWSDLWsCompileImport(SCHEMA_FEATURES_WSDL_DOCUMENT);
  }

  public void testBasicValidityOFWSDL() throws Exception {

    isWSDLValid(BASIC_VALID_WSDL_DOCUMENT);
  }

  public void testWSIWsdl() throws Exception {

    /*
     * Test the restrictions imposed by WSI on WSDL 1.1 R2007 R2020 R2023 R2021
     */

    /* The following 2 tests prove together the validity of R2022 & 2023 */

    importWSIWSDLWsCompileImport(WSITEST1_FEATURES_WSDL_DOCUMENT);
    importWSIWSDLWsCompileImport(WSITEST2_FEATURES_WSDL_DOCUMENT);

    System.out.println("\n Warning as Expected for non conformant WSDL");
    importWSIWSDLWsCompileImport(WSITEST3_FEATURES_WSDL_DOCUMENT);

    importWSIWSDLWsCompileImport(WSITEST4_FEATURES_WSDL_DOCUMENT);
    importWSIWSDLWsCompileImport(WSITEST5_FEATURES_WSDL_DOCUMENT);

  }

  public void testSimpleTypeWsdl() throws Exception {

    /*
     * Test jaxrpc changelist 2.2.3 where simple type derive by restrictions
     * from a supported siimple type where no other mapping is defined in
     * JAX-RPC
     */
    importWSDLWsCompileImport(SIMPLETYPE_FEATURES_WSDL_DOCUMENT);
    importWSIWSDLWsCompileImport(SIMPLETYPE_FEATURES_WSDL_DOCUMENT);
    importWSIWSDLWsCompileImport(SIMPLETYPE_FEATURES_RPCLITWSDL_DOCUMENT);
  }
}
