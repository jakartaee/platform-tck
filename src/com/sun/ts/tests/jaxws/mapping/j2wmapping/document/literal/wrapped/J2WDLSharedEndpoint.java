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

@javax.jws.WebService(name = "MYJ2WDLSharedEndpoint", targetNamespace = "http://doclitservice.org/wsdl")
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface J2WDLSharedEndpoint extends InheritedInterface {

  @javax.jws.WebMethod(operationName = "arrayOperationFromClient")
  @javax.jws.WebResult(name = "return", targetNamespace = "http://doclitservice.org/wsdl")
  public java.lang.String arrayOperationFromClient(
      @javax.jws.WebParam(name = "arg0") java.lang.String[] arg0);

  public com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.J2WDLSharedBean getBean();

  public java.lang.String[] arrayOperation();

  @javax.jws.WebMethod(operationName = "stringOperation")
  @javax.jws.WebResult(name = "rvalue", targetNamespace = "http://doclitservice.org/wsdl")
  public java.lang.String stringOperation(
      @javax.jws.WebParam(name = "ivalue0") java.lang.String ivalue0);

  @javax.jws.WebMethod(operationName = "stringOperation2")
  @javax.jws.WebResult(name = "response", targetNamespace = "http://doclitservice.org/wsdl")
  @jakarta.xml.ws.RequestWrapper(localName = "myStringOperation", targetNamespace = "http://doclitservice.org/wsdl", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MyStringOperation")
  @jakarta.xml.ws.ResponseWrapper(localName = "myStringOperationResponse", targetNamespace = "http://doclitservice.org/wsdl", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MyStringOperationResponse")
  public java.lang.String stringOperation2(
      @javax.jws.WebParam(name = "request") java.lang.String request);

  // Holder method with annotations for parameters
  public String holderMethodDefault(
      jakarta.xml.ws.Holder<java.lang.String> varStringDefault);

  public String holderMethodInOut(
      @javax.jws.WebParam(name = "varStringInOut", mode = javax.jws.WebParam.Mode.INOUT) jakarta.xml.ws.Holder<java.lang.String> varStringInOut);

  public String holderMethodOut(
      @javax.jws.WebParam(name = "varStringOut", mode = javax.jws.WebParam.Mode.OUT) jakarta.xml.ws.Holder<java.lang.String> varStringOut);

  // A method with more than 1 input parts
  public java.lang.String oneTwoThree(int one, long two, double three);

  // An overloaded method helloWorld
  public java.lang.String helloWorld();

  // Annotation to disambiguate name of overloaded method helloWorld
  // and to disambiguate name of Wrappers from HelloWorld - > HelloWorld2
  @javax.jws.WebMethod(operationName = "helloWorld2")
  @jakarta.xml.ws.RequestWrapper(localName = "helloWorld2", targetNamespace = "http://doclitservice.org/wsdl", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.HelloWorld2")
  @jakarta.xml.ws.ResponseWrapper(localName = "helloWorld2Response", targetNamespace = "http://doclitservice.org/wsdl", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.HelloWorld2Response")
  public java.lang.String helloWorld(String hello);

  @javax.jws.WebMethod
  @javax.jws.Oneway
  public void oneWayOperation();

  @javax.jws.WebMethod
  public void operationWithHeaderAndHeaderFaultAndFault(
      @javax.jws.WebParam(name = "ConfigHeader", header = true, mode = javax.jws.WebParam.Mode.IN) ConfigHeader configheader)
      throws ConfigHeaderFault, MyFault;

  @javax.jws.WebMethod
  public void methodWithNoReturn(int a, int b);

  @javax.jws.WebMethod
  @jakarta.xml.ws.RequestWrapper(partName = "request", localName = "", targetNamespace = "", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MethodWithNoReturn2Request")
  @jakarta.xml.ws.ResponseWrapper(partName = "response", localName = "", targetNamespace = "", className = "com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped.MethodWithNoReturn2Response")
  public void methodWithNoReturn2(String s);

  @javax.jws.WebMethod
  public void operationThatThrowsAFault() throws MyOtherFault;
}
