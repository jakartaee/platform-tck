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

package com.sun.ts.tests.jaxrpc.sharedwebservices.simpleservice;

import javax.xml.rpc.holders.StringHolder;
import java.rmi.RemoteException;

public class SimpleEndpointImpl implements SimpleEndpoint {
  public String helloWorld() throws RemoteException {
    return "hello world";
  }

  public void oneWayOperation() throws RemoteException {
  }

  public String overloadedOperation(String param) throws RemoteException {
    return param;
  }

  public String overloadedOperation(String param, String param2)
      throws RemoteException {
    return param + param2;
  }

  public String[] arrayOperation() throws RemoteException {
    return new String[] { "one", "two", "three" };
  }

  public SimpleBean getBean() throws RemoteException {
    return new SimpleBean(5, "A String");
  }

  public String arrayOperationFromClient(String[] array)
      throws RemoteException {
    return "success";
  }

  public String holderOperation(StringHolder holder1, StringHolder holder2)
      throws RemoteException {
    return "success";
  }
}
