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

package com.sun.ts.tests.webservices12.narrow;

import com.sun.ts.lib.util.TestUtil;

import javax.xml.ws.WebServiceException;
import javax.jws.WebService;

@WebService(portName = "InterfaceTest2Port", serviceName = "InterfaceTestService", targetNamespace = "http://interfacetestservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/InterfaceTestService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.narrow.InterfaceTest2")

public class InterfaceTest2Impl implements InterfaceTest2 {

  public String hello1(String v) {
    System.out.println("hello1");
    System.out.println("String=" + v);
    return "interface2:" + v;
  }

  public String hello2(String v) {
    System.out.println("hello2");
    System.out.println("String=" + v);
    return "interface2:" + v;
  }
}
