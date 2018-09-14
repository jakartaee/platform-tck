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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefWithNoDDsTest;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@Stateless
@SOAPBinding(style = Style.RPC)
public class WSEjbWSRefWithNoDDsTestHelloEJB {

  @WebMethod
  public String hello(String str) {
    return str + " to you too!";
  }

  @WebMethod
  public String bye(String str) {
    return str + " and take care";
  }

  @WebMethod
  public String extra(String str, int i) {
    String tmp = str + i + ", all from yours truly";
    return tmp;
  }

  @WebMethod
  public void extraExtra(long i) {
    long ll = i * 2002 + 1999 + 8734895;
  }
}
