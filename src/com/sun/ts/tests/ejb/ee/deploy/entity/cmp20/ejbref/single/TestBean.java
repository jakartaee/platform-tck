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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.ejbref.single;

import java.util.Properties;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.EJBException;

public interface TestBean extends EJBObject {

  public boolean testStatelessInternal(Properties props) throws RemoteException;

  public boolean testStatelessExternal(Properties props) throws RemoteException;

  public boolean testStatefulInternal(Properties props) throws RemoteException;

  public boolean testStatefulExternal(Properties props) throws RemoteException;

  public boolean testBMPInternal(Properties props) throws RemoteException;

  public boolean testBMPExternal(Properties props) throws RemoteException;

  public boolean testCMP11Internal(Properties props) throws RemoteException;

  public boolean testCMP11External(Properties props) throws RemoteException;

  public boolean testCMP20Internal(Properties props) throws RemoteException;

  public boolean testCMP20External(Properties props) throws RemoteException;

  public void cleanUpBean() throws RemoteException;

}
