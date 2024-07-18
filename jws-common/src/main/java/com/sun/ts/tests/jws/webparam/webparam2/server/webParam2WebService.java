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

package com.sun.ts.tests.jws.webparam.webparam2.server;

import com.sun.ts.tests.jws.common.Address;
import com.sun.ts.tests.jws.common.Employee;
import com.sun.ts.tests.jws.common.Name;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.Holder;

@WebService(wsdlLocation = "WEB-INF/wsdl/WebParam2WebServiceService.wsdl")
public class webParam2WebService {

  @WebMethod(operationName = "helloString", action = "urn:HelloString")
  public String hello(@WebParam(name = "string1") Name name) {
    return "Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString2", action = "urn:HelloString2")
  public String hello2(
      @WebParam(name = "name", partName = "string2") String name, String name2,
      @WebParam(name = "Address", partName = "address", targetNamespace = "helloString2/Address") Address address) {
    System.out.println(" Address : " + address);
    return new StringBuffer().append("Hello ").append(name).append(", ")
        .append(name2).append(" to Web Service").toString();
  }

  @WebMethod
  public String hello3(String name, int number, double number2) {
    return "Hello " + name + ", " + number + ", " + number2 + " to Web Service";
  }

  @WebMethod(operationName = "helloString4", action = "urn:HelloString4")
  public String hello4(
      @WebParam(name = "Name", targetNamespace = "helloString4/Name", mode = WebParam.Mode.INOUT) Holder<Name> name,
      @WebParam(name = "Employee", mode = WebParam.Mode.OUT) Holder<com.sun.ts.tests.jws.common.Employee> employee) {
    return "Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString5", action = "urn:HelloString5")
  public String hello5(
      @WebParam(name = "employee", header = true, targetNamespace = "helloString5/Employee") Employee employee,
      String name) {
    return "Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString6", action = "urn:HelloString6")
  public String hello6(@WebParam(name = "string1", header = true) Name name) {
    return "Hello " + name + " to Web Service";
  }

}
