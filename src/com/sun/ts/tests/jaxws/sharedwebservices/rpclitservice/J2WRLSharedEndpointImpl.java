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

package com.sun.ts.tests.jaxws.sharedwebservices.rpclitservice;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;

@WebService(portName = "J2WRLSharedEndpointPort", serviceName = "J2WRLShared", targetNamespace = "http://rpclitservice.org/wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.rpclitservice.J2WRLSharedEndpoint")
public class J2WRLSharedEndpointImpl implements J2WRLSharedEndpoint {
  public String helloWorld() {
    return "hello world";
  }

  public String stringOperation(String param) {
    return param;
  }

  public String[] arrayOperation() {
    return new String[] { "one", "two", "three" };
  }

  public J2WRLSharedBean getBean() {
    return new J2WRLSharedBean(5, "A String");
  }

  public String arrayOperationFromClient(String[] array) {
    return "success";
  }
}
