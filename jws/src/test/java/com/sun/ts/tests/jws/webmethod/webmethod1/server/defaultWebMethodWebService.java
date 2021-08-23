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
 */
/*
 * $Id$
 */

package com.sun.ts.tests.jws.webmethod.webmethod1.server;

import jakarta.jws.WebService;

@WebService(wsdlLocation = "WEB-INF/wsdl/DefaultWebMethodWebServiceService.wsdl")
public class defaultWebMethodWebService {

  public String hello(String name) {
    return "Hello " + name + " to Web Service";
  }

  public String hello2(String name, String name2) {
    return "Hello " + name + ", " + name2 + " to Web Service";
  }

  public String hello3(String name, String name2, String name3) {
    return "Hello " + name + ", " + name2 + ", " + name3 + " to Web Service";
  }

}
