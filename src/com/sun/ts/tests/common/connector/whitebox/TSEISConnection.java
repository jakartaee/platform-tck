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

/*
 * @(#)TSEISConnection.java	1.5 02/06/06
 */

package com.sun.ts.tests.common.connector.whitebox;

import java.util.Vector;
import java.util.Hashtable;
import javax.resource.spi.ConnectionEvent;
import com.sun.ts.tests.common.connector.util.*;

public class TSEISConnection implements TSConnection {

  private TSManagedConnection mc;

  private boolean supportsLocalTx;

  /*
   * @name TSEISConnection
   * 
   * @desc TSEISConnection constructor
   * 
   * @param TSManagedConnection
   */
  public TSEISConnection(TSManagedConnection mc, boolean supportsLocalTx) {
    this.mc = mc;
    this.supportsLocalTx = supportsLocalTx;
  }

  public void insert(String key, String value) throws Exception {
    try {
      TSConnection con = getTSEISConnection();
      con.insert(key, value);
    } catch (Exception ex) {
      throw ex;
    }
  }

  public void delete(String key) throws Exception {
    try {
      TSConnection con = getTSEISConnection();
      con.delete(key);
    } catch (Exception ex) {
      throw ex;
    }

  }

  public void update(String key, String value) throws Exception {
    try {
      TSConnection con = getTSEISConnection();
      con.update(key, value);
    } catch (Exception ex) {
      throw ex;
    }

  }

  public Vector readData() throws Exception {
    TSConnection con = getTSEISConnection();
    return con.readData();

  }

  public String readValue(String key) throws Exception {
    TSConnection con = getTSEISConnection();
    return con.readValue(key);

  }

  public void setAutoCommit(boolean flag) {
    TSConnection con = getTSEISConnection();
    con.setAutoCommit(flag);
  }

  public boolean getAutoCommit() {
    TSConnection con = getTSEISConnection();
    return con.getAutoCommit();

  }

  /*
   * @name commit
   * 
   * @desc Commit a transaction.
   */
  public void commit() throws Exception {
    TSConnection con = getTSEISConnection();
    con.commit();
  }

  /*
   * @name dropTable
   * 
   * @desc drop a table.
   */
  public void dropTable() throws Exception {
    TSConnection con = getTSEISConnection();
    System.out.println("TSEISConnectin.dropTable." + con);
    con.dropTable();
  }

  /*
   * @name begin
   * 
   * @desc begin a transaction.
   */
  public void begin() throws Exception {
    TSConnection con = getTSEISConnection();
    con.begin();
  }

  /*
   * @name rollback
   * 
   * @desc rollback a transaction
   */
  public void rollback() {
    TSConnection con = getTSEISConnection();
    con.rollback();
  }

  /*
   * @name close
   * 
   * @desc close a connection to the EIS.
   */
  public void close() throws Exception {
    if (mc == null)
      return; // already be closed
    mc.removeTSConnection(this);
    mc.sendEvent(ConnectionEvent.CONNECTION_CLOSED, null, this);
    mc = null;
  }

  /*
   * @name isClosed
   * 
   * @desc checks if the connection is close.
   */
  public boolean isClosed() {
    return (mc == null);
  }

  /*
   * @name associateConnection
   * 
   * @desc associate connection
   * 
   * @param TSManagedConnection newMc
   */
  void associateConnection(TSManagedConnection newMc) {

    if (checkIfValid()) {
      mc.removeTSConnection(this);
    }
    newMc.addTSConnection(this);
    mc = newMc;
  }

  public boolean checkIfValid() {
    if (mc == null) {
      return false;
    }
    return true;
  }

  /*
   * @name getTSEISConnection
   * 
   * @desc get a Jdbc connection
   * 
   * @return Connection
   */
  TSConnection getTSEISConnection() {
    try {
      if (checkIfValid()) {
        return mc.getTSConnection();
      } else {
        return null;
      }
    } catch (Exception ex) {
      return null;
    }
  }

  void invalidate() {
    mc = null;
  }

  public Hashtable getTempTable() {
    return getTSEISConnection().getTempTable();
  }
}
