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

import java.util.Vector;
import java.util.Hashtable;
import com.sun.ts.tests.common.connector.util.*;

public class TSConnectionImpl implements TSConnection {

  private boolean inuse = false;

  private boolean autocommit = true;

  private Hashtable tempTable = new Hashtable();

  public TSConnectionImpl() {

  }

  public TSConnection getConnection() throws Exception {
    try {

      TSConnection ctscon = TSeis.getTSeis().getConnection();
      ConnectorStatus.getConnectorStatus()
          .logAPI("TSConnectionImpl.getConnection", "", "");
      return ctscon;
    } catch (Exception ex) {
      ex.getMessage();
      return null;
    }
  }

  public TSConnection getConnection(String user, char[] passwd)
      throws Exception {
    try {

      System.out.println("TSConnectionImpl.getConnection(u,p):  user=" + user
          + " passwd = " + passwd);

      TSConnection ctscon = TSeis.getTSeis().getConnection(user, passwd);
      ConnectorStatus.getConnectorStatus()
          .logAPI("TSConnectionImpl.getConnection", "", "");
      return ctscon;
    } catch (Exception ex) {
      ex.getMessage();
      return null;
    }
  }

  public synchronized boolean lease() {
    if (inuse) {
      return false;
    } else {
      inuse = true;
      return true;
    }
  }

  protected void expireLease() {
    inuse = false;
  }

  @Override
  public void insert(String key, String value) throws Exception {
    try {
      ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.insert", "",
          "");
      TSeis.getTSeis().insert(key, value, this);
      System.out.println("TSConnectionImpl.insert");
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public void delete(String str) throws Exception {
    try {
      ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.delete", "",
          "");
      TSeis.getTSeis().delete(str, this);
      System.out.println("TSConnectionImpl.delete");
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public void update(String key, String value) throws Exception {
    try {
      ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.update", "",
          "");
      TSeis.getTSeis().update(key, value, this);
      System.out.println("TSConnectionImpl.update");
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public Hashtable getTempTable() {
    return tempTable;
  }

  public boolean inUse() throws Exception {
    return inuse;
  }

  @Override
  public void setAutoCommit(boolean flag) {
    autocommit = flag;
  }

  @Override
  public boolean getAutoCommit() {
    return autocommit;
  }

  @Override
  public void dropTable() throws Exception {

    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.dropTable",
        "", "");
    TSeis.getTSeis().dropTable();
    tempTable.clear();
  }

  @Override
  public void begin() throws Exception {

    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.begin", "",
        "");
    TSeis.getTSeis().begin();
  }

  @Override
  public void rollback() {

    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.rollback", "",
        "");
    tempTable.clear();
  }

  @Override
  public void commit() throws Exception {
    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.commit", "",
        "");
    TSeis.getTSeis().commit(this);
    tempTable.clear();
  }

  @Override
  public void close() throws Exception {
    TSeis.getTSeis().returnConnection(this);
    System.out.println("TSConnectionImpl.close");
  }

  @Override
  public Vector readData() throws Exception {
    Vector table = TSeis.getTSeis().readData();
    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.readData", "",
        "");
    return table;
  }

  @Override
  public String readValue(String key) throws Exception {
    String value = TSeis.getTSeis().readValue(key, this);
    ConnectorStatus.getConnectorStatus().logAPI("TSConnectionImpl.readValue",
        "", "");
    return value;
  }

}
