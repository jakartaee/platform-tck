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
 * $Id: AddNumbersImpl.java 51075 2003-03-27 10:44:21Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.epr;

import jakarta.annotation.Resource;
import jakarta.jws.HandlerChain;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.Action;
import jakarta.xml.ws.FaultAction;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;

@WebService(name = "AddNumbers", portName = "AddNumbersPort", targetNamespace = "http://foobar.org/", serviceName = "AddNumbersService")
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@Addressing(enabled = true, required = true)
public class AddNumbersImpl {

  @Resource
  WebServiceContext wsc;

  public W3CEndpointReference getW3CEPR1() {
    System.out.println("wsc=" + wsc);
    return (W3CEndpointReference) wsc.getEndpointReference();
  }

  public W3CEndpointReference getW3CEPR2() {
    System.out.println("wsc=" + wsc);
    return (W3CEndpointReference) wsc.getEndpointReference(
        jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
  }

  public int doAddNumbers(int number1, int number2) {
    return number1 + number2;
  }
}
