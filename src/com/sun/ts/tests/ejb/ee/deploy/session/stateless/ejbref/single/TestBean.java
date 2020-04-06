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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejbref.single;

import java.util.Properties;
import java.rmi.RemoteException;
import jakarta.ejb.EJBObject;
import jakarta.ejb.EJBException;

public interface TestBean extends EJBObject {

  public void initLogging(Properties p) throws RemoteException;

  public boolean testStatelessInternal(java.util.Properties p)
      throws RemoteException;

  public boolean testStatelessExternal(java.util.Properties p)
      throws RemoteException;

  public boolean testStatefulInternal(java.util.Properties p)
      throws RemoteException;

  public boolean testStatefulExternal(java.util.Properties p)
      throws RemoteException;

  public boolean testBMPInternal(java.util.Properties p) throws RemoteException;

  public boolean testBMPExternal(java.util.Properties p) throws RemoteException;

  public boolean testCMP11Internal(java.util.Properties p)
      throws RemoteException;

  public boolean testCMP11External(java.util.Properties p)
      throws RemoteException;

  public boolean testCMP20Internal(java.util.Properties p)
      throws RemoteException;

  public boolean testCMP20External(java.util.Properties p)
      throws RemoteException;

  public void cleanUpBean() throws RemoteException;

}
