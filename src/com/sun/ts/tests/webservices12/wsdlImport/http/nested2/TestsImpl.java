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

package com.sun.ts.tests.webservices12.wsdlImport.http.nested2;

import javax.jws.WebService;

@WebService(portName = "TestsPort", serviceName = "Nested2HttpSvc", targetNamespace = "http://Nested2HttpSvc.org/wsdl", wsdlLocation = "WEB-INF/wsdl/nestedimportwsdl.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.wsdlImport.http.nested2.Tests")

public class TestsImpl implements Tests {
  public com.sun.ts.tests.webservices12.wsdlImport.http.nested2.Astring invokeTest1() {
    com.sun.ts.tests.webservices12.wsdlImport.http.nested2.Astring result = new com.sun.ts.tests.webservices12.wsdlImport.http.nested2.Astring();
    result.setResult("Hello");
    return result;
  }

}
