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
 * $Id: MyOtherFault.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.mapping.j2wmapping.document.literal.wrapped;

@jakarta.xml.ws.WebFault(name = "MyOtherFault", messageName = "YesItsMyOtherFault", targetNamespace = "http://doclitservice.org/wsdl", faultBean = "MyOtherFaultBean")
public class MyOtherFault extends Exception {
  private MyOtherFaultBean faultInfo;

  public MyOtherFault(String message, MyOtherFaultBean faultInfo) {
    super(message);
    this.faultInfo = faultInfo;
  }

  public MyOtherFault(String message, MyOtherFaultBean faultInfo,
      Throwable cause) {
    super(message, cause);
    this.faultInfo = faultInfo;
  }

  public MyOtherFaultBean getFaultInfo() {
    return faultInfo;
  }
}
