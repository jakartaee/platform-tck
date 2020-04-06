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

package com.sun.ts.tests.rmiiiop.ee.objecttests;

import java.rmi.RemoteException;
import jakarta.ejb.EJBObject;

public interface TestBean extends EJBObject {

  public boolean pass_a_rmiiiop_object(HelloRMIIIOPObjectIntf p1)
      throws RemoteException;

  public HelloRMIIIOPObjectIntf return_a_rmiiiop_object(
      HelloRMIIIOPObjectIntf p1) throws RemoteException;

  public boolean pass_a_javaidl_object(HelloJAVAIDLObjectIntf p1)
      throws RemoteException;

  public HelloJAVAIDLObjectIntf return_a_javaidl_object(
      HelloJAVAIDLObjectIntf p1) throws RemoteException;

  public String hello() throws RemoteException;

}
