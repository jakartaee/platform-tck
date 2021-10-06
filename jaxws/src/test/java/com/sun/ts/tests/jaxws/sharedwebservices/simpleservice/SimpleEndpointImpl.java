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

package com.sun.ts.tests.jaxws.sharedwebservices.simpleservice;

import jakarta.xml.ws.WebServiceException;

@jakarta.jws.WebService(targetNamespace = "http://simpletestservice.org/wsdl", serviceName = "SimpleTest", portName = "SimpleEndpointPort", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.simpleservice.SimpleEndpoint")
public class SimpleEndpointImpl implements SimpleEndpoint {
  public String helloWorld() {
    return "hello world";
  }

  public void oneWayOperation() {
  }

  public String overloadedOperation(String param) {
    return param;
  }

  public String overloadedOperation(String param, String param2) {
    return param + param2;
  }

  public String[] arrayOperation() {
    return new String[] { "one", "two", "three" };
  }

  public SimpleBean getBean() {
    SimpleBean sb = new SimpleBean();
    sb.setMyInt(5);
    sb.setMyString("A String");
    return sb;
  }

  public String arrayOperationFromClient(String[] array) {
    return "success";
  }

  public String holderOperation(jakarta.xml.ws.Holder<java.lang.String> holder1,
      jakarta.xml.ws.Holder<java.lang.String> holder2) {
    return "success";
  }
}
