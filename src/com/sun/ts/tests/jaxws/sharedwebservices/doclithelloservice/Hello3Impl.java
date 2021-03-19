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
 * $Id: Hello3Impl.java 52501 2009-01-24 02:29:49Z adf $
 */

package com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.*;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "Hello3Port", targetNamespace = "http://helloservice.org/wsdl", serviceName = "HelloService", wsdlLocation = "WEB-INF/wsdl/WSDLHelloService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.doclithelloservice.Hello3")

public class Hello3Impl implements Hello3 {

  public Hello3Response hello(Hello3Request hreq) {

    TestUtil.logTrace("*** in Hello3Impl ***");
    String testName = hreq.getTestname();
    TestUtil.logTrace("*** testName = " + testName + " ***");
    String argument = hreq.getArgument();
    TestUtil.logTrace("*** argument = " + argument + " ***");

    argument += ":Hello3Impl";
    Hello3Response hres = new Hello3Response();
    hres.setTestname(testName);
    hres.setArgument(argument);
    return hres;
  }

}
