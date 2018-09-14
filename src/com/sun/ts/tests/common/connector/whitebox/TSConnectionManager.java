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

import javax.resource.*;
import javax.resource.spi.*;
import java.io.Serializable;

/**
 * The default ConnectionManager implementation for the non-managed scenario
 *
 */
public class TSConnectionManager implements ConnectionManager, Serializable {

  public TSConnectionManager() {
  }

  @Override
  public Object allocateConnection(ManagedConnectionFactory mcf,
      ConnectionRequestInfo info) throws ResourceException {

    ManagedConnection mc = mcf.createManagedConnection(null, info);

    return mc.getConnection(null, info);
  }
}
