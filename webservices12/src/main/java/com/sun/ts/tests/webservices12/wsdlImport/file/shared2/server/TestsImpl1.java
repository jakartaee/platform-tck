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

package com.sun.ts.tests.webservices12.wsdlImport.file.shared2.server;

import jakarta.jws.WebService;

@WebService(portName = "Tests1Port", serviceName = "Shared2FileSvc1", targetNamespace = "http://Shared2FileSvc1.org/wsdl", wsdlLocation = "WEB-INF/wsdl/std/svc1/Shared2FileSvc1.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.wsdlImport.file.shared2.server.Tests1")

public class TestsImpl1 implements Tests1 {
  public com.sun.ts.tests.webservices12.wsdlImport.file.shared2.server.Astring invokeTest1() {
    com.sun.ts.tests.webservices12.wsdlImport.file.shared2.server.Astring result = new com.sun.ts.tests.webservices12.wsdlImport.file.shared2.server.Astring();
    result.setResult("Hello");
    return result;
  }

}
