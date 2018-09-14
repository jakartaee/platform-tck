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
public class ImportTest extends TestCaseBase {

  private final static String OVERLOADED_WSDL_DOCUMENT = "overloaded.wsdl";

  private final static String FAULT01_WSDL_DOCUMENT = "fault01.wsdl";

  private final static String FAULT02_WSDL_DOCUMENT = "fault02.wsdl";

  private final static String FAULT03_WSDL_DOCUMENT = "fault03.wsdl";

  private final static String FAULT04_WSDL_DOCUMENT = "fault04.wsdl";

  private final static String ENUM_WSDL_DOCUMENT = "enum.wsdl";

  private final static String ENUM2_WSDL_DOCUMENT = "enum2.wsdl";

  private final static String ANYTYPE_WSDL_DOCUMENT = "anytype.wsdl";

  private final static String HEADER_WSDL_DOCUMENT = "header.wsdl";

  private static final String MIME_MULTIPART_DOCUMENT = "mimetypesmultipart.wsdl";

  private final static String NILLABLE_WSDL_DOCUMENT = "nillable.wsdl";

  private final static String PARAMETER_ORDER_WSDL_DOCUMENT = "parameterorder.wsdl";

  private final static String PARAMETER_ORDER2_WSDL_DOCUMENT = "parameterorder2.wsdl";

  private final static String MARSHALL_TEST_WSDL_DOCUMENT = "marshalltest.wsdl";

  private final static String EXCEPTION_TEST_WSDL_DOCUMENT = "sm11.wsdl";

  private final static String SUBTYPING_TEST_WSDL_DOCUMENT = "Interface_Service.wsdl";

  private final static String MULTIPLE_PORTS_TEST_WSDL_DOCUMENT = "hello_ports.wsdl";

  public ImportTest(String name) {
    super(name);
  }

  public void runTests() {
    try {
      testImportOverloaded();
    } catch (Exception e) {
    }
    try {
      testImportFault2();
    } catch (Exception e) {
    }
    try {
      testImportFault3();
    } catch (Exception e) {
    }
    try {
      testImportFault4();
    } catch (Exception e) {
    }
    try {
      testImportEnum();
    } catch (Exception e) {
    }
    try {
      testImportEnum2();
    } catch (Exception e) {
    }
    try {
      testImportAnyType();
    } catch (Exception e) {
    }
    try {
      testImportHeader();
    } catch (Exception e) {
    }
    try {
      testImportNillableSimpleType();
    } catch (Exception e) {
    }
    try {
      testImportParameterOrder();
    } catch (Exception e) {
    }
    try {
      testImportParameterOrder2();
    } catch (Exception e) {
    }
    try {
      testImportExceptionTest();
    } catch (Exception e) {
    }
    try {
      testImportMarshallTest();
    } catch (Exception e) {
    }
    try {
      testImportMIMEMultiPart();
    } catch (Exception e) {
    }
    try {
      testSubtyping();
    } catch (Exception e) {
    }
    try {
      testSubtyping2();
    } catch (Exception e) {
    }
    try {
      testMultiplePorts();
    } catch (Exception e) {
    }
  }

  public void testImportOverloaded() throws Exception {
    importWSDLClientOnly(OVERLOADED_WSDL_DOCUMENT);
  }

  public void testImportFault() throws Exception {
    importWSDL(FAULT01_WSDL_DOCUMENT);
  }

  public void testImportFault2() throws Exception {
    importWSDL(FAULT02_WSDL_DOCUMENT);
  }

  public void testImportFault3() throws Exception {
    importWSDL(FAULT03_WSDL_DOCUMENT);
  }

  public void testImportFault4() throws Exception {
    importWSDL(FAULT04_WSDL_DOCUMENT);
  }

  public void testImportEnum() throws Exception {
    importWSDL(ENUM_WSDL_DOCUMENT);
  }

  public void testImportEnum2() throws Exception {
    importWSDL(ENUM2_WSDL_DOCUMENT);
  }

  public void testImportAnyType() throws Exception {
    importWSDL(ANYTYPE_WSDL_DOCUMENT);
  }

  public void testImportHeader() throws Exception {
    importWSDL(HEADER_WSDL_DOCUMENT);
  }

  public void testImportNillableSimpleType() throws Exception {
    importWSDL(NILLABLE_WSDL_DOCUMENT);
  }

  public void testImportParameterOrder() throws Exception {
    importWSDL(PARAMETER_ORDER_WSDL_DOCUMENT);
  }

  public void testImportParameterOrder2() throws Exception {
    importWSDL(PARAMETER_ORDER2_WSDL_DOCUMENT);
  }

  public void testImportExceptionTest() throws Exception {
    importWSDL(EXCEPTION_TEST_WSDL_DOCUMENT);
  }

  public void testImportMarshallTest() throws Exception {
    importWSDL(MARSHALL_TEST_WSDL_DOCUMENT);
  }

  public void testImportMIMEMultiPart() throws Exception {
    // importWSDL(MIME_MULTIPART_DOCUMENT);
  }

  public void testSubtyping() throws Exception {
    importWSDL(SUBTYPING_TEST_WSDL_DOCUMENT);
  }

  public void testSubtyping2() throws Exception {
    _useSearchSchemaOption = true;
    importWSDL(SUBTYPING_TEST_WSDL_DOCUMENT);
  }

  public void testMultiplePorts() throws Exception {
    importWSDL(MULTIPLE_PORTS_TEST_WSDL_DOCUMENT);
  }
}
