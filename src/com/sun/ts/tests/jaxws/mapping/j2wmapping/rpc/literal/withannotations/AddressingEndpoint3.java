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
 * $Id: AddressingEndpoint3.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withannotations;

import jakarta.xml.ws.WebServiceException;

@jakarta.jws.WebService(name = "AddressingEndpoint3", targetNamespace = "http://rpclitservice.org/wsdl")
@jakarta.jws.soap.SOAPBinding(style = jakarta.jws.soap.SOAPBinding.Style.RPC, use = jakarta.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = jakarta.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface AddressingEndpoint3 {

  @jakarta.xml.ws.Action(input = "input1")
  public void address1(String s);

  @jakarta.xml.ws.Action(output = "output2")
  public String address2();

}
