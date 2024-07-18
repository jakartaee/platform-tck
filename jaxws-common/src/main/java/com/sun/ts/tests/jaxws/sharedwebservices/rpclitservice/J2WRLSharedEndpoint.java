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

package com.sun.ts.tests.jaxws.sharedwebservices.rpclitservice;

@jakarta.jws.WebService(targetNamespace = "http://rpclitservice.org/wsdl")
@jakarta.jws.soap.SOAPBinding(style = jakarta.jws.soap.SOAPBinding.Style.RPC, use = jakarta.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = jakarta.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface J2WRLSharedEndpoint {

  @jakarta.jws.WebMethod(operationName = "arrayOperationFromClient")
  @jakarta.jws.WebResult(name = "return", targetNamespace = "http://rpclitservice.org/wsdl")
  public java.lang.String arrayOperationFromClient(
      @jakarta.jws.WebParam(name = "arg0") java.lang.String[] arg0);

  public com.sun.ts.tests.jaxws.sharedwebservices.rpclitservice.J2WRLSharedBean getBean();

  public java.lang.String[] arrayOperation();

  @jakarta.jws.WebMethod(operationName = "stringOperation")
  @jakarta.jws.WebResult(name = "return", targetNamespace = "http://rpclitservice.org/wsdl")
  public java.lang.String stringOperation(
      @jakarta.jws.WebParam(name = "arg0") java.lang.String arg0);

  public java.lang.String helloWorld();

}
