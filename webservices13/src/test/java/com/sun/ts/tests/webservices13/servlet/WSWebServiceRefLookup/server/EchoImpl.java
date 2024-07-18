/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: EchoImpl.java 52492 2007-01-24 00:59:57Z adf $
 */

package com.sun.ts.tests.webservices13.servlet.WSWebServiceRefLookup.server;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

@WebService(name = "Echo", serviceName = "EchoService", targetNamespace = "http://echo.org/wsdl")
public class EchoImpl {
  @WebMethod
  public String echoString(String str) {
    return str;
  }
}
