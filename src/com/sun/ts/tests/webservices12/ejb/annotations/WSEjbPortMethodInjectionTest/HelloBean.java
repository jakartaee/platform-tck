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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbPortMethodInjectionTest;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService(portName = "Hello", serviceName = "HelloService", targetNamespace = "http://Hello.org", wsdlLocation = "META-INF/wsdl/HelloService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.ejb.annotations.WSEjbPortMethodInjectionTest.Hello")
@Stateless(name = "WSEjbPortMethodInjectionTest")
public class HelloBean {

  public String hello(String str) {
    return "WebSvcTest-Hello " + str;
  }

  public String bye(String str) {
    return "WebSvcTest-Bye and take care " + str;
  }
}
