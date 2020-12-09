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
package com.sun.ts.tests.jws.webresult.webresult1.server;

import com.sun.ts.tests.jws.common.Name;

import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(wsdlLocation = "WEB-INF/wsdl/WebResultWebServiceService.wsdl")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)

public class webResultWebService {

  @WebMethod(operationName = "helloString", action = "urn:HelloString")
  public String hello(String name) {
    return "hello1 : Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString2", action = "urn:HelloString2")
  @WebResult()
  public String hello2(Name name2) {
    return "hello2 : Hello " + name2 + " to Web Service";
  }

  @WebMethod(operationName = "helloString3", action = "urn:HelloString3")
  @WebResult(name = "name3")
  public Name hello3(boolean input) {

    System.out.println(" Invoking hello3 ");
    Name newName = new Name();
    newName.setFirstName("jsr181");
    newName.setLastName("jsr109");

    return newName;
  }

  @WebMethod(operationName = "helloString4", action = "urn:HelloString4")
  @WebResult(name = "employee", partName = "Employee", targetNamespace = "hello4/employee")
  public com.sun.ts.tests.jws.common.Employee hello4()
      throws com.sun.ts.tests.jws.common.NameException {

    System.out.println(" Invoking hello4 ");

    Name newName = new Name();
    newName.setFirstName("jsr181");
    newName.setLastName("jaxws");

    com.sun.ts.tests.jws.common.Employee employee = new com.sun.ts.tests.jws.common.Employee();
    employee.setName(newName);

    return employee;

  }

  @WebMethod(operationName = "helloString5", action = "urn:HelloString")
  @WebResult(name = "name5", partName = "name5", targetNamespace = "hello5/name", header = true)
  public String hello5(int age) {
    return "Hello " + age + " to Web Service";
  }

}
