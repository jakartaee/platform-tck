/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

@javax.jws.WebService(name = "MYJ2WRLSharedEndpoint", targetNamespace = "http://rpclitservice.org/wsdl")
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface J2WRLSharedEndpoint extends InheritedInterface {

  public java.lang.String arrayOperationFromClient(java.lang.String[] arg0);

  public com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withannotations.J2WRLSharedBean getBean();

  public java.lang.String[] arrayOperation();

  @javax.jws.WebMethod(operationName = "stringOperation")
  @javax.jws.WebResult(name = "rvalue", targetNamespace = "http://rpclitservice.org/wsdl")
  public java.lang.String stringOperation(
      @javax.jws.WebParam(name = "ivalue0") java.lang.String ivalue0);

  // Holder method with annotations for parameters
  public String holderMethodDefault(
      javax.xml.ws.Holder<java.lang.String> varStringDefault);

  public String holderMethodInOut(
      @javax.jws.WebParam(name = "varStringInOut", mode = javax.jws.WebParam.Mode.INOUT) javax.xml.ws.Holder<java.lang.String> varStringInOut);

  public String holderMethodOut(
      @javax.jws.WebParam(name = "varStringOut", mode = javax.jws.WebParam.Mode.OUT) javax.xml.ws.Holder<java.lang.String> varStringOut);

  // A method with more than 1 input parts
  public java.lang.String oneTwoThree(int one, long two, double three);

  // An overloaded method helloWorld
  public java.lang.String helloWorld();

  // Annotation to disambiguate name of overloaded method helloWorld
  @javax.jws.WebMethod(operationName = "helloWorld2")
  public java.lang.String helloWorld(String hello);

  @javax.jws.WebMethod
  @javax.jws.Oneway
  public void oneWayOperation();

  @javax.jws.WebMethod
  @javax.jws.Oneway
  public void oneWayOperationWithParams(Integer i, Long l, Double d);

  @javax.jws.WebMethod
  public void operationWithHeaderAndHeaderFaultAndFault(
      @javax.jws.WebParam(name = "ConfigHeader", header = true, mode = javax.jws.WebParam.Mode.IN) ConfigHeader configheader)
      throws ConfigHeaderFault, MyFault, MyOtherFault;
}
