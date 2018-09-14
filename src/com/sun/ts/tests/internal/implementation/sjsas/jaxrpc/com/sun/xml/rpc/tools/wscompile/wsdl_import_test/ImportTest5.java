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
public class ImportTest5 extends TestCaseBase {

  /*
   * These test compilation of xsd:include. wscompile may fail when there is a
   * bug.
   */
  private final static String DOCUMENT_LITERAL_WSDL_DOCUMENT = "test17.wsdl";

  public ImportTest5(String name) {
    super(name);
  }

  public void runTests() throws Exception {
    try {
      testXSDIncludeDocumentLiteral();
    } catch (Exception e) {
    }
    try {
      testXSDIncludeDocumentLiteralInWSI();
    } catch (Exception e) {
    }
  }

  public void testXSDIncludeDocumentLiteral() throws Exception {
    importWSDLWsCompileImport(DOCUMENT_LITERAL_WSDL_DOCUMENT);
  }

  public void testXSDIncludeDocumentLiteralInWSI() throws Exception {
    importWSIWSDLWsCompileImport(DOCUMENT_LITERAL_WSDL_DOCUMENT);
  }
}
