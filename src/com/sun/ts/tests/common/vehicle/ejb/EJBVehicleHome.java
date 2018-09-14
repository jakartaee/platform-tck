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

package com.sun.ts.tests.common.vehicle.ejb;

import com.sun.ts.lib.harness.*;
import java.io.*;
import java.net.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.*;
import java.util.*;

public interface EJBVehicleHome extends EJBHome {
  // public EJBVehicleRemote create ( EETest testImpl, String args, Properties p
  // ) throws RemoteException, CreateException;
  // call below works now so am commenting out call above-- Vella
  public EJBVehicleRemote create(String[] args, Properties p)
      throws RemoteException, CreateException;
}
