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

package com.sun.ts.tests.webservices.handlerEjb.HandlerLifecycle;

import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.xml.rpc.handler.*;

// Service Defintion Interface - as outlined in JAX-RPC Specification

public interface Hello extends Remote {
  public String hello(String s) throws RemoteException;

  public String howdy(String s) throws RemoteException;

  public String hi(String s) throws RemoteException;

  public String enventry(String s) throws RemoteException;

  public boolean wasInitCalled() throws RemoteException;

  public boolean wasDestroyCalled() throws RemoteException;

  public boolean getHttpSessionTest() throws RemoteException;

  public boolean getMessageContextTest() throws RemoteException;

  public boolean getServletContextTest() throws RemoteException;

  public boolean getUserPrincipalTest() throws RemoteException;
}
