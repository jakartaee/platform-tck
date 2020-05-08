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
 */
/*
 * $Id$
 */
package com.sun.ts.tests.jws.webservice.webservice4.server;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService(endpointInterface = "com.sun.ts.tests.jws.webservice.webservice4.server.endpointInterface2WebServiceEI", wsdlLocation = "WEB-INF/wsdl/EndpointInterface2WebServiceService.wsdl")
public class endpointInterface2WebService {

  public String hello(String name) {
    return "hello " + name + " to Web Service";
  }

  public String hello1(String name1) {
    return "hello1 " + name1 + " to Web Service";
  }

  public String hello2(String myname) {
    return "hello2 " + myname + " to Web Service";
  }
}
