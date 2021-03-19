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
 * $Id: AddressingEndpointImpl.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.bare;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.Addressing;

@WebService(name = "AddressingEndpoint", serviceName = "AddressingService", targetNamespace = "http://doclitservice.org/wsdl")
@jakarta.jws.soap.SOAPBinding(style = jakarta.jws.soap.SOAPBinding.Style.DOCUMENT, use = jakarta.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = jakarta.jws.soap.SOAPBinding.ParameterStyle.BARE)
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing
public class AddressingEndpointImpl {

  @jakarta.xml.ws.Action(input = "input1")
  public void address1(String s) {
  }

  @jakarta.xml.ws.Action(output = "output2")
  public String address2() {
    return "hello";
  }

  @jakarta.xml.ws.Action(fault = {
      @jakarta.xml.ws.FaultAction(className = MyFault1.class, value = "fault1") })
  public String address3(String s) throws MyFault1 {
    throw new MyFault1("MyFault1");
  }

  @jakarta.xml.ws.Action(output = "output4", fault = {
      @jakarta.xml.ws.FaultAction(className = MyFault1.class, value = "fault1") })
  public String address4() throws MyFault1 {
    throw new MyFault1("MyFault1");
  }

  @jakarta.xml.ws.Action(input = "input5", output = "output5", fault = {
      @jakarta.xml.ws.FaultAction(className = MyFault1.class, value = "fault1") })
  public String address5(String s) throws MyFault1 {
    throw new MyFault1("MyFault1");
  }

  @jakarta.xml.ws.Action(input = "")
  public void address6(String s) throws MyFault1 {
  }

  @jakarta.xml.ws.Action(output = "")
  public String address7() throws MyFault1 {
    return "hello";
  }
}
