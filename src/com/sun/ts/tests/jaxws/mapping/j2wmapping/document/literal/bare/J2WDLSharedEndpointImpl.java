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

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.bare;

import jakarta.xml.ws.WebServiceException;

import javax.jws.WebService;

@WebService(name = "J2WDLSharedEndpoint", serviceName = "J2WDLSharedService", targetNamespace = "http://doclitservice.org/wsdl")
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
public class J2WDLSharedEndpointImpl {

  public void foo(jakarta.xml.ws.Holder<String> fooString) {
  }

  public Bar bar(String barString) {
    Bar bar = new Bar();
    bar.setBar(barString);
    return bar;
  }
}
