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

package com.sun.ts.tests.jws.webparam.webparam3.server;

import com.sun.ts.tests.jws.common.Address;
import com.sun.ts.tests.jws.common.Employee;
import com.sun.ts.tests.jws.common.Name;
import com.sun.ts.tests.jws.common.NameException;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Holder;

@WebService(wsdlLocation = "WEB-INF/wsdl/WebParam3WebServiceService.wsdl")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)

public class webParam3WebService {

  @WebMethod(operationName = "helloString", action = "urn:HelloString")
  public String hello(@WebParam(name = "string1") String name) {
    return "Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString2", action = "urn:HelloString2")
  public String hello2(
      @WebParam(name = "name", partName = "string2") String name, String name2,
      @WebParam(name = "Address", partName = "address", targetNamespace = "helloString2/Address") Address address) {
    System.out.println(" Address : " + address.toString());
    return "Hello " + name + ", " + name2 + " to Web Service";
  }

  @WebMethod
  public String hello3(
      @WebParam(name = "id", targetNamespace = "hello3/Name", header = true) String name,
      @WebParam(name = "Name", mode = WebParam.Mode.OUT) Holder<Name> name2,
      @WebParam(name = "Employee", mode = WebParam.Mode.INOUT) Holder<Employee> employee)
      throws NameException {

    System.out.println(" Invoking hello3 ");
    Name newName = new com.sun.ts.tests.jws.common.Name();
    Employee oldEmployee = employee.value;

    newName.setFirstName("jsr181");
    newName.setLastName("jsr109");

    name2.value = newName;

    oldEmployee.setName(newName);

    employee.value = oldEmployee;

    return "Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString4", action = "urn:HelloString4")
  public String hello4(@WebParam(name = "string1", header = true) String name) {
    return "Hello " + name + " to Web Service";
  }

}
