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
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded;

import com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded.custom.pkg.HelloException;

import jakarta.jws.WebService;

@WebService(portName = "HelloPort", serviceName = "myService", targetNamespace = "http://customizationembeddedtest.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLCustomizationEmbeddedTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded.Hello")

public class HelloImpl implements Hello {
  public String hello1(String helloArgument) {
    System.out.println("in CustomizationEmbeddedTestService:HelloImpl:hello1");
    return helloArgument + ", World!";
  }

  public HelloResponse hello2(Hello2 helloArgument) {
    System.out.println("in CustomizationEmbeddedTestService:HelloImpl:hello2");
    HelloResponse h = new HelloResponse();
    h.setResponse(helloArgument.getArgument() + ", World!");
    return h;
  }

  public HelloResponse3 hello3(HelloRequest3 helloRequest3)
      throws HelloException {
    System.out.println("in CustomizationEmbeddedTestService:HelloImpl:hello3");
    if (helloRequest3.getHelloRequest1().equals("HelloException")) {
      HelloFaultMessage hfm = new HelloFaultMessage();
      hfm.setFault1("foo");
      hfm.setFault2("bar");
      throw new HelloException("This is the HelloException fault", hfm);
    } else {
      HelloResponse3 h = new HelloResponse3();
      h.setResponse(helloRequest3.getHelloRequest1() + ", "
          + helloRequest3.getHelloRequest2() + "!");
      return h;
    }
  }

  public EchoResponse echo(EchoRequest echoRequest) {
    System.out.println("in CustomizationEmbeddedTestService:HelloImpl:echo");
    Name[] names = echoRequest.getName();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < names.length; i++)
      sb.append(names[i].getFirst() + names[i].getLast());
    EchoResponse echoResponse = new EchoResponse();
    echoResponse.setReturn(sb.toString());
    return echoResponse;
  }
}
