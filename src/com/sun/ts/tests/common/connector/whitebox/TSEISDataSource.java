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

import java.io.*;
import javax.resource.*;
import javax.resource.spi.*;
import javax.naming.Reference;
import com.sun.ts.tests.common.connector.util.*;
import java.util.Vector;

public class TSEISDataSource
    implements TSDataSource, Serializable, Referenceable {

  private String desc;

  private ManagedConnectionFactory mcf;

  private ConnectionManager cm;

  private Reference reference;

  /*
   * @name createTSConnectionFactory
   * 
   * @desc TSConnectionFactory constructor
   * 
   * @param ManagedConnectionFactor, ConnectionManager
   */
  public TSEISDataSource(ManagedConnectionFactory mcf, ConnectionManager cm) {
    this.mcf = mcf;
    if (cm == null) {

    } else {
      this.cm = cm;
    }
  }

  /*
   * @name getConnection
   * 
   * @desc Gets a connection to the EIS.
   * 
   * @return Connection
   * 
   * @exception Exception
   */
  public TSConnection getConnection() throws Exception {
    try {
      return (TSConnection) cm.allocateConnection(mcf, null);
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }

  /*
   * @name getConnection
   * 
   * @desc Gets a connection to the EIS.
   * 
   * @return Connection
   * 
   * @exception Exception
   */
  public TSConnection getConnection(String username, String password)
      throws Exception {
    try {
      ConnectionRequestInfo info = new TSConnectionRequestInfo(username,
          password);
      return (TSConnection) cm.allocateConnection(mcf, info);
    } catch (ResourceException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  /*
   * @name getLog
   * 
   * @desc Returns Log to client. Used for verification of callbacks.
   * 
   * @return Log
   */
  @Override
  public Vector getLog() {
    return (ConnectorStatus.getConnectorStatus().getLogVector());
  }

  /*
   * @name getStateLog
   * 
   * @desc Returns Log to client. Used for verification of callbacks.
   * 
   * @return Log
   */
  @Override
  public Vector getStateLog() {
    return (ConnectorStatus.getConnectorStatus().getStateLogVector());
  }

  /*
   * @name checkConnectionManager
   * 
   * @desc return true if ConnectionManager is Serializable
   * 
   * @return boolean
   */
  public boolean checkConnectionManager() {

    if (cm instanceof Serializable)
      return true;
    else
      return false;
  }

  /*
   * @name clearLog
   * 
   * @desc Empties the Log
   */
  @Override
  public void clearLog() {
    // In order to support the case where we want to be able to deploy one
    // time and then run through all tests, we want to ensure that the log is
    // not accidentally deleted by a client side test. (In the past, it was
    // acceptable to delete this log at the end of a client tests run because
    // rars would be undeployed and then re-deployed, thus re-creating this
    // log.)
    // but this may not be true for case of standalone connector.
    // ConnectorStatus.getConnectorStatus().purge();
  }

  /*
   * @name setLogFlag
   * 
   * @desc Turns logging on/off
   */
  @Override
  public void setLogFlag(boolean b) {
    ConnectorStatus.getConnectorStatus().setLogFlag(b);
  }

  /*
   * @name setReference
   * 
   * @desc
   */
  public void setReference(Reference reference) {
    this.reference = reference;
  }

  /*
   * @name getReference
   * 
   * @desc
   */
  public Reference getReference() {
    return reference;
  }

}
