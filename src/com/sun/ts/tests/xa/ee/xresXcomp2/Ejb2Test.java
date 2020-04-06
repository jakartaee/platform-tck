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

/*
 * @(#)Ejb2Test.java	1.7 02/02/22
 */

package com.sun.ts.tests.xa.ee.xresXcomp2;

import jakarta.ejb.EJBObject;
import java.rmi.RemoteException;
import jakarta.ejb.EJBException;
//import javax.jms.*;
import java.util.*;

public interface Ejb2Test extends EJBObject {
  public void dbConnect(String tName) throws RemoteException;

  public void insert(String tName, int key) throws RemoteException;

  public void dbUnConnect(String tName) throws RemoteException;

  public void initLogging(Properties p) throws RemoteException;

  public void throwEJBException() throws RemoteException;

}
