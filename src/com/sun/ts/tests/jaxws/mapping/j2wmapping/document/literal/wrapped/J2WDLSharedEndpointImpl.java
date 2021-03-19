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

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped;

import jakarta.xml.ws.WebServiceException;

import jakarta.jws.WebService;

@WebService(portName = "J2WDLSharedEndpointPort", serviceName = "J2WDLSharedService", targetNamespace = "http://doclitservice.org/wsdl", endpointInterface = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.J2WDLSharedEndpoint")
public class J2WDLSharedEndpointImpl implements J2WDLSharedEndpoint {
  public String oneTwoThree(int one, long two, double three) {
    return "" + one + ":" + two + ":" + three;
  }

  public String holderMethodDefault(
      jakarta.xml.ws.Holder<java.lang.String> varStringDefault) {
    varStringDefault.value = "holderMethodDefault";
    return varStringDefault.value;
  }

  public String holderMethodInOut(
      jakarta.xml.ws.Holder<java.lang.String> varStringInOut) {
    varStringInOut.value = "holderMethodInOut";
    return varStringInOut.value;
  }

  public String holderMethodOut(
      jakarta.xml.ws.Holder<java.lang.String> varStringOut) {
    varStringOut.value = "holderMethodOut";
    return varStringOut.value;
  }

  public String helloWorld() {
    return "hello world";
  }

  public String helloWorld(String hello) {
    return hello;
  }

  public String stringOperation(String param) {
    return param;
  }

  public String stringOperation2(String param) {
    return param;
  }

  public String[] arrayOperation() {
    return new String[] { "one", "two", "three" };
  }

  public J2WDLSharedBean getBean() {
    return new J2WDLSharedBean(5, "A String");
  }

  public String arrayOperationFromClient(String[] array) {
    return "success";
  }

  public void oneWayOperation() {
  }

  public void operationWithHeaderAndHeaderFaultAndFault(
      ConfigHeader configheader) throws ConfigHeaderFault, MyFault {
  }

  public String hello(String hello) {
    return hello;
  }

  public String bye(String bye) {
    return bye;
  }

  public void methodWithNoReturn(int a, int b) {
  }

  public void methodWithNoReturn2(String s) {
  }

  public void operationThatThrowsAFault() throws MyOtherFault {
  }
}
