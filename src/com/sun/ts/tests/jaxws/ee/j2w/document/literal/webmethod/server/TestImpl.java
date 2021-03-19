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
 * $Id: TestImpl.java 52492 2007-01-24 00:59:57Z adf $
 */

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.webmethod.server;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

@WebService(name = "Test", serviceName = "TestService", targetNamespace = "http://test.org/wsdl")
public class TestImpl extends TestImplBase {

  @WebMethod
  public String method3(String str) {
    return str;
  }

  // This is also a WebMethod since declaring class
  // has WebService
  public String method4(String str) {
    return str;
  }

  // This is a WebMethod since exclude=false
  @WebMethod(exclude = false)
  public String method5(String str) {
    return str;
  }

  // Not a web method since exclude=true
  @WebMethod(exclude = true)
  public String method6(String str) {
    return str;
  }

  // Not a web method a static method plus exclude=true
  @WebMethod(exclude = true)
  public static String method7(String str) {
    return str;
  }

  // Not a web method a final method plus exclude=true
  @WebMethod(exclude = true)
  public final String method8(String str) {
    return str;
  }

  // Not a web method a static method
  public static String method9(String str) {
    return str;
  }

  // Not a web method a final method
  public final String method10(String str) {
    return str;
  }
}
