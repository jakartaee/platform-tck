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

package com.sun.ts.tests.webservices12.wsdlImport.http.nested4;

import jakarta.ejb.Stateless;
import javax.jws.WebService;

@WebService(portName = "TestsPort", serviceName = "Nested4HttpSvc", targetNamespace = "http://Nested4HttpSvc.org/wsdl", wsdlLocation = "META-INF/wsdl/nestedimportwsdl.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.wsdlImport.http.nested4.Tests")

@Stateless(name = "WSNested4Http")
public class TestsBean {

  public com.sun.ts.tests.webservices12.wsdlImport.http.nested4.Astring invokeTest1() {
    com.sun.ts.tests.webservices12.wsdlImport.http.nested4.Astring result = new com.sun.ts.tests.webservices12.wsdlImport.http.nested4.Astring();
    result.setResult("Hello");
    return result;
  }

}
