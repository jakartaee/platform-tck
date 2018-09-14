/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.narrow;

import com.sun.ts.lib.util.TestUtil;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class InheritedInterfaceTest2Impl implements InheritedInterfaceTest2 {

  public String hello1(String v) throws RemoteException {
    TestUtil.logTrace("hello1");
    TestUtil.logMsg("String=" + v);
    return "interface1:" + v;
  }

  public String hello2(String v) throws RemoteException {
    TestUtil.logTrace("hello2");
    TestUtil.logMsg("String=" + v);
    return "interface2:" + v;
  }
}
