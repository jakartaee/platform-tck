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

package com.sun.ts.tests.common.connector.whitebox;

import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.XAResource;
import javax.resource.spi.*;

public class TSXAConnectionImpl implements TSXAConnection {

  public TSXAConnectionImpl() {

  }

  public XAResource getXAResource(TSManagedConnection mc) throws Exception {
    System.out.println("TSXAConnectionImpl.getXAResource");
    XAResourceImpl xaimpl = new XAResourceImpl(mc);
    return xaimpl;
  }

  public TSConnection getConnection() throws Exception {
    try {

      TSConnection ctscon = TSeis.getTSeis().getConnection();
      System.out.println("TSXAConnectionImpl.getConnection");
      ConnectorStatus.getConnectorStatus()
          .logAPI("TSConnectionImpl.getConnection", "", "");
      return ctscon;
    } catch (Exception ex) {
      ex.getMessage();
      return null;
    }
  }

  public TSConnection getConnection(String user, char[] password)
      throws Exception {
    try {

      TSConnection ctscon = TSeis.getTSeis().getConnection(user, password);
      ConnectorStatus.getConnectorStatus()
          .logAPI("TSConnectionImpl.getConnection", "", "");
      return ctscon;
    } catch (Exception ex) {
      ex.getMessage();
      return null;
    }
  }

  public void close() throws Exception {

  }

  public void addConnectionEventListener(ConnectionEventListener listener) {

  }

}
