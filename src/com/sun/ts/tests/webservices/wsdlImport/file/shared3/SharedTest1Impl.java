/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002, 2020 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.wsdlImport.file.shared3;

import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;

public class SharedTest1Impl implements SessionBean {
  public void ejbCreate() {
  }

  public void ejbActivate() {
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public AllStruct echoAllStruct(AllStruct inputAllStruct)
      throws java.rmi.RemoteException {
    inputAllStruct
        .setVarString(inputAllStruct.getVarString() + "_SharedTestImpl1");
    inputAllStruct.setVarInt(inputAllStruct.getVarInt() + 1);
    return inputAllStruct;
  }
}
