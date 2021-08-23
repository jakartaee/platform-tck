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

package com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withannotations;

@jakarta.jws.WebService(name = "MYJ2WRLSharedEndpoint", targetNamespace = "http://rpclitservice.org/wsdl")
@jakarta.jws.soap.SOAPBinding(style = jakarta.jws.soap.SOAPBinding.Style.RPC, use = jakarta.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = jakarta.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface J2WRLSharedEndpoint extends InheritedInterface {

  public java.lang.String arrayOperationFromClient(java.lang.String[] arg0);

  public com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withannotations.J2WRLSharedBean getBean();

  public java.lang.String[] arrayOperation();

  @jakarta.jws.WebMethod(operationName = "stringOperation")
  @jakarta.jws.WebResult(name = "rvalue", targetNamespace = "http://rpclitservice.org/wsdl")
  public java.lang.String stringOperation(
      @jakarta.jws.WebParam(name = "ivalue0") java.lang.String ivalue0);

  // Holder method with annotations for parameters
  public String holderMethodDefault(
      jakarta.xml.ws.Holder<java.lang.String> varStringDefault);

  public String holderMethodInOut(
      @jakarta.jws.WebParam(name = "varStringInOut", mode = jakarta.jws.WebParam.Mode.INOUT) jakarta.xml.ws.Holder<java.lang.String> varStringInOut);

  public String holderMethodOut(
      @jakarta.jws.WebParam(name = "varStringOut", mode = jakarta.jws.WebParam.Mode.OUT) jakarta.xml.ws.Holder<java.lang.String> varStringOut);

  // A method with more than 1 input parts
  public java.lang.String oneTwoThree(int one, long two, double three);

  // An overloaded method helloWorld
  public java.lang.String helloWorld();

  // Annotation to disambiguate name of overloaded method helloWorld
  @jakarta.jws.WebMethod(operationName = "helloWorld2")
  public java.lang.String helloWorld(String hello);

  @jakarta.jws.WebMethod
  @jakarta.jws.Oneway
  public void oneWayOperation();

  @jakarta.jws.WebMethod
  @jakarta.jws.Oneway
  public void oneWayOperationWithParams(Integer i, Long l, Double d);

  @jakarta.jws.WebMethod
  public void operationWithHeaderAndHeaderFaultAndFault(
      @jakarta.jws.WebParam(name = "ConfigHeader", header = true, mode = jakarta.jws.WebParam.Mode.IN) ConfigHeader configheader)
      throws ConfigHeaderFault, MyFault, MyOtherFault;
}
