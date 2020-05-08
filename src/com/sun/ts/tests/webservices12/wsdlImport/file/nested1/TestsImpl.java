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

package com.sun.ts.tests.webservices12.wsdlImport.file.nested1;

import jakarta.jws.WebService;

@WebService(portName = "TestsPort", serviceName = "Nested1FileSvc", targetNamespace = "http://Nested1FileSvc.org/wsdl", wsdlLocation = "WEB-INF/wsdl/nestedimportwsdl.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.wsdlImport.file.nested1.Tests")

public class TestsImpl implements Tests {
  public com.sun.ts.tests.webservices12.wsdlImport.file.nested1.Astring invokeTest1() {
    com.sun.ts.tests.webservices12.wsdlImport.file.nested1.Astring result = new com.sun.ts.tests.webservices12.wsdlImport.file.nested1.Astring();
    result.setResult("Hello");
    return result;
  }

}
