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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejblink.single;

import java.util.Properties;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface TestBean extends EJBObject {

  public void initLogging(Properties p) throws RemoteException;

  public boolean testStatelessInternal(Properties p) throws RemoteException;

  public boolean testStatelessExternal(Properties p) throws RemoteException;

  public boolean testStatefulInternal(Properties p) throws RemoteException;

  public boolean testStatefulExternal(Properties p) throws RemoteException;

  public boolean testBMPInternal(Properties p) throws RemoteException;

  public boolean testBMPExternal(Properties p) throws RemoteException;

  public boolean testCMP11Internal(Properties p) throws RemoteException;

  public boolean testCMP11External(Properties p) throws RemoteException;

  public boolean testCMP20Internal(Properties p) throws RemoteException;

  public boolean testCMP20External(Properties p) throws RemoteException;

  public void cleanUpBean() throws RemoteException;

}
