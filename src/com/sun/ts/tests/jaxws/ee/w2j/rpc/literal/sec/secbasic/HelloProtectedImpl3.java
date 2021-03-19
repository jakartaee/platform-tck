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
 * @(#)HelloProtectedImpl3.java	1.1 08/09/05
 */

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.sec.secbasic;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import jakarta.xml.ws.WebServiceException;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "HelloProtectedPort3", serviceName = "BasicAuthServiceTestService", targetNamespace = "http://BasicAuthServiceTestService.org/wsdl", wsdlLocation = "WEB-INF/wsdl/BasicAuthServiceTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.sec.secbasic.HelloProtected3")
public class HelloProtectedImpl3 implements HelloProtected3 {

  public String helloProtected3(String s) {
    return "Hello, " + s + "!";
  }
}
