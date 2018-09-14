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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefTest2;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.jws.WebService;
import javax.annotation.Resource;

@WebService(portName = "Hello", serviceName = "HelloService", targetNamespace = "http://Hello.org", wsdlLocation = "META-INF/wsdl/HelloService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefTest2.Hello")
@Stateless(name = "WSEjbWebServiceRefTest2")
@Remote({ HelloIF.class })
public class HelloBean implements HelloIF {

  @Resource
  private SessionContext sc;

  public String hello(String str) {
    return str + " to you too!";
  }

  public String bye(String str) {
    return str + " and take care";
  }

  public String extra(String str, int i) {
    String tmp = str + i + ", all from yours truly";
    return tmp;
  }

  public void extraExtra(long i) {
    long ll = i * 2002 + 1999 + 8734895;
    System.out.println("Long i=" + i + ",  Long ll=" + ll);
  }
}
