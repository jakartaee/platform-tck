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

package com.sun.ts.tests.jaxrpc.sharedwebservices.faultservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;

public interface SoapFaultTest extends Remote {
  public String alwaysThrowsException() throws RemoteException, DummyException;

  public String alwaysThrowsServerException()
      throws RemoteException, ServerException;

  public String alwaysThrowsSOAPFaultExceptionDetailNoChildren()
      throws RemoteException;

  public String alwaysThrowsSOAPFaultExceptionDetailUnqualifiedChildren()
      throws RemoteException;

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedChildren()
      throws RemoteException;

  public String alwaysThrowsSOAPFaultExceptionDetailNoAttributes()
      throws RemoteException;

  public String alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes()
      throws RemoteException;
}
