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
 * $Id: Hello2Impl.java 51047 2006-01-17 19:39:40Z adf $
 */

package com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.MTOM;

@WebService(portName = "Hello2Port", serviceName = "HelloService", targetNamespace = "http://helloservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSDLHelloService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice.Hello2")
@Addressing
@MTOM
public class Hello2Impl implements Hello2 {
  private static final ObjectFactory of = new ObjectFactory();

  public HelloResponse hello(HelloRequest req) {
    System.out.println("Hello2Impl received: " + req.getArgument());
    HelloResponse resp = null;
    resp = of.createHelloResponse();
    resp.setArgument(req.getArgument());
    return resp;
  }

  public void helloOneWay(HelloOneWayRequest req) {
    System.out.println("Hello2Impl received: " + req.getArgument());
  }
}
