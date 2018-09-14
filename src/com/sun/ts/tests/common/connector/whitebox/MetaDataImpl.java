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

import javax.resource.ResourceException;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.*;
import javax.resource.spi.security.PasswordCredential;

public class MetaDataImpl implements ManagedConnectionMetaData {

  private TSManagedConnection mc;

  public MetaDataImpl(TSManagedConnection mc) {
    this.mc = mc;
  }

  /*
   * @name getEISProductName
   * 
   * @desc Gets product name of underlying EIS.
   * 
   * @return String
   * 
   * @exception ResourceException
   */
  @Override
  public String getEISProductName() throws ResourceException {
    try {
      String str = "Simple TS EIS";
      return str;
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    }
  }

  /*
   * @name getEISProductVersion
   * 
   * @desc Gets product version of underlying EIS.
   * 
   * @return String
   * 
   * @exception ResourceException
   */
  @Override
  public String getEISProductVersion() throws ResourceException {
    try {
      String str = "1.0";
      return str;
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    }
  }

  /*
   * @name getMaxConnections
   * 
   * @desc Returns maximum limit on number of active concurrent connections that
   * an EIS instance can support across client processes.
   * 
   * @return int
   * 
   * @exception ResourceException
   */
  @Override
  public int getMaxConnections() throws ResourceException {
    try {
      int i = 0;
      return i;
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    }
  }

  /*
   * @name getUserName
   * 
   * @desc Return name of the user currently associated with ManagedConnection
   * instance. The returned username corresponds to the resource principal under
   * whose security context the connection to the EIS instance has been
   * established.
   *
   * @return String
   * 
   * @exception ResourceException
   */
  @Override
  public String getUserName() throws ResourceException {
    if (mc.isDestroyed()) {
      throw new IllegalStateException("ManagedConnection has been destroyed");
    }

    PasswordCredential pc = null;
    String str = null;

    pc = mc.getPasswordCredential();
    if (pc != null) {
      str = pc.getUserName();
    }

    if (pc != null && str != null && !str.equals(""))
      return str;
    else
      return null;

  }

}
