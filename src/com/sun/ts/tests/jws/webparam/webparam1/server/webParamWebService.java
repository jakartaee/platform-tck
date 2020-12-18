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

package com.sun.ts.tests.jws.webparam.webparam1.server;

import com.sun.ts.tests.jws.common.Address;
import com.sun.ts.tests.jws.common.Employee;
import com.sun.ts.tests.jws.common.Name;
import com.sun.ts.tests.jws.common.NameException;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Holder;

@WebService(wsdlLocation = "WEB-INF/wsdl/WebParamWebServiceService.wsdl")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)

public class webParamWebService {

  @WebMethod(operationName = "helloString", action = "urn:HelloString")
  public String hello(@WebParam(name = "string1") String name) {
    return "hello : Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString2", action = "urn:HelloString2")
  public String hello2(
      @WebParam(name = "name2", partName = "string2", targetNamespace = "helloString2/name") String name) {
    return "hello2 : Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString3", action = "urn:HelloString3")
  public void hello3(
      @WebParam(name = "name3", partName = "string3", targetNamespace = "helloString3/name", header = true) String name,
      @WebParam(name = "Name", targetNamespace = "helloString3/Name", mode = WebParam.Mode.INOUT) Holder<Name> name2) {

    System.out.println(" Invoking hello3 ");

    Name newName = new Name();

    newName.setFirstName("jsr181");
    newName.setLastName("jsr109");

    name2.value = newName;

  }

  @WebMethod(operationName = "helloString4", action = "urn:HelloString4")
  public void hello4(
      @WebParam(name = "name4", partName = "string4") String name,
      @WebParam(name = "Employee", mode = WebParam.Mode.OUT) Holder<Employee> employee)
      throws com.sun.ts.tests.jws.common.NameException {

    System.out.println(" Invoking hello4 ");

    Name newName = new Name();
    com.sun.ts.tests.jws.common.Employee oldEmployee = new Employee();

    newName.setFirstName("jsr181");
    newName.setLastName("jaxws");

    oldEmployee.setName(newName);

    employee.value = oldEmployee;

  }

  @WebMethod(operationName = "helloString5", action = "urn:HelloString5")
  public String hello5(String name) {
    return "hello5 : Hello " + name + " to Web Service";
  }

  @WebMethod(operationName = "helloString6", action = "urn:HelloString6")
  public String hello6(@WebParam() int age,
      @WebParam(name = "name6", header = true) String name) {
    return "hello6 : Hello " + name + " " + age + " to Web Service";
  }

  @WebMethod(operationName = "helloString7", action = "urn:HelloString7")
  public void hello7(
      @WebParam(name = "name7", partName = "string7", header = true) String name,
      @WebParam(name = "name8", partName = "string8") Name name2,
      @WebParam(name = "MyEmployee", mode = WebParam.Mode.OUT) Holder<Employee> employee)
      throws NameException {

    System.out.println(" Invoking hello4 ");

    Employee oldEmployee = new com.sun.ts.tests.jws.common.Employee();
    oldEmployee.setName(name2);

    employee.value = oldEmployee;

  }

  @WebMethod(operationName = "helloString8", action = "urn:HelloString8")
  @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
  public String hello8(@WebParam(name = "string8") String name,
      Address address) {
    return "hello8 : " + address.getCity() + " to Web Service";
  }

}
