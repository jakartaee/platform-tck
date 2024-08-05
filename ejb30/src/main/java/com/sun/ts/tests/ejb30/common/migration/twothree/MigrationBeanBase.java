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

package com.sun.ts.tests.ejb30.common.migration.twothree;

import jakarta.ejb.EJBContext;

abstract public class MigrationBeanBase implements ThreeIF {
  abstract protected EJBContext getEJBContext();

  //////////////////////////////////////////////////////////////////////
  // ThreeIF methods
  //////////////////////////////////////////////////////////////////////

  public String from3Client() {
    return "from3Client";
  }

  //////////////////////////////////////////////////////////////////////
  // TwoRemoteIF methods
  //////////////////////////////////////////////////////////////////////
  public String from2RemoteClient() {
    return "from2RemoteClient";
  }

  public void remoteSameTxContext() {
    getEJBContext().setRollbackOnly();
  }

  //////////////////////////////////////////////////////////////////////
  // TwoLocalIF methods
  //////////////////////////////////////////////////////////////////////
  public String from2LocalClient() {
    return "from2LocalClient";
  }

  public void localSameTxContext() {
    getEJBContext().setRollbackOnly();
  }
}
