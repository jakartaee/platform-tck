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

package com.sun.ts.tests.webservices12.wsdlImport.http.twin2.server;

import javax.jws.WebService;

@WebService(portName = "Tests2Port", serviceName = "Twin2HttpSvc2", targetNamespace = "http://Twin2HttpSvc2.org/wsdl", wsdlLocation = "WEB-INF/wsdl/svc2/Twin2HttpSvc2.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.wsdlImport.http.twin2.server.Tests2")

public class TestsImpl2 implements Tests2 {
  public String invokeTest2() {
    return "Hello";
  }

}
