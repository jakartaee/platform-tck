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

import com.sun.ts.lib.util.TestUtil;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.*;

public class TSeis {

  private static TSeis eis;

  private Hashtable permtable = new Hashtable();

  private boolean commitflag = true;

  private TSConnectionImpl con1 = null;

  private TSConnectionImpl con2 = null;

  private TSConnectionImpl con3 = null;

  private TSConnectionImpl con4 = null;

  private TSConnectionImpl con5 = null;

  private Vector conset = null;

  public TSResourceManager rm = new TSResourceManager();

  /**
   * Singleton constructor
   */
  private TSeis() {
    conset = new Vector();
    con1 = new TSConnectionImpl();
    con2 = new TSConnectionImpl();
    con3 = new TSConnectionImpl();
    con4 = new TSConnectionImpl();
    con5 = new TSConnectionImpl();
    conset.addElement(con1);
    conset.addElement(con2);
    conset.addElement(con3);
    conset.addElement(con4);
    conset.addElement(con5);
  }

  /**
   * Singleton accessor
   */
  public static TSeis getTSeis() {
    if (eis == null) {
      eis = new TSeis();
    }
    return eis;
  }

  public synchronized TSConnection getConnection() {
    ConnectorStatus.getConnectorStatus().logAPI("TSeis.getConnection", "", "");
    TSConnectionImpl con;

    for (int i = 0; i < conset.size(); i++) {
      con = (TSConnectionImpl) conset.elementAt(i);
      if (con.lease()) {
        return con;
      }
    }

    con = new TSConnectionImpl();
    con.lease();
    conset.addElement(con);
    return con;
  }

  public synchronized TSConnection getConnection(String usr, char[] passwd) {
    ConnectorStatus.getConnectorStatus().logAPI("TSeis.getConnection",
        "usr,passwd", "");
    System.out.println("User is " + usr);
    if ((usr.equals("cts1") && passwd[0] == 'c' && passwd[1] == 't'
        && passwd[2] == 's' && passwd[3] == '1')
        || (usr.equals("cts2") && passwd[0] == 'c' && passwd[1] == 't'
            && passwd[2] == 's' && passwd[3] == '2')) {
      System.out.println("Passwd lenght is " + passwd.length);

      for (int in = 0; in < passwd.length; in++) {
        System.out.println("Password 3 is " + passwd[in]);
      }
      TSConnectionImpl con;

      for (int i = 0; i < conset.size(); i++) {
        con = (TSConnectionImpl) conset.elementAt(i);
        if (con.lease()) {
          return con;
        }
      }

      con = new TSConnectionImpl();
      con.lease();
      conset.addElement(con);
      return con;
    } else {
      System.out.println("Connection null returned");
      return null;
    }
  }

  public DataElement read(String key, TSConnection con) throws TSEISException {

    DataElement de = null;
    if (permtable.containsKey(key)) {
      de = (DataElement) permtable.get(key);
    }

    // Lets see if resource manager can read from a distributed transaction?
    DataElement element = rm.read(key, con);

    // May be connection is only in a local transaction. So read from
    // the connection cache.
    DataElement localElement = null;
    Hashtable tempTable = con.getTempTable();
    if (tempTable.containsKey(key)) {
      localElement = (DataElement) tempTable.get(key);
    }

    if (de == null && element == null && localElement == null) {
      throw new TSEISException("Data not found.");
    }
    if (element != null) {
      return element;
    } else if (localElement != null) {
      return localElement;
    } else {
      return de;
    }
  }

  public String readValue(String key, TSConnection con) throws TSEISException {

    DataElement de = read(key, con);
    return de.getValue();
  }

  public void insert(String key, String value, TSConnection con)
      throws TSEISException {

    boolean datapresent = false;

    try {
      read(key, con);
      datapresent = true;
    } catch (TSEISException tse) {
    }

    if (datapresent)
      throw new TSEISException("Duplicate Key");

    DataElement de = new DataElement(key, value);

    if ((getResourceManager()
        .getTransactionStatus(con) == TSXaTransaction.NOTRANSACTION)
        && con.getAutoCommit()) {
      de.setStatus(DataElement.COMMITTED);
      System.out.println("TSeis.insert.permtable");
      permtable.put(key, de);
    } else { // if (getResourceManager().getTransactionStatus(con) ==
             // TSResourceManager.INTRANSACTION)
      System.out.println("TSeis.insert.temptable");
      de.setStatus(DataElement.INSERTED);
      con.getTempTable().put(key, de);
    }
  }

  public void update(String key, String value, TSConnection con)
      throws TSEISException {
    DataElement de = read(key, con);
    de.updateVersion();
    de.setValue(value);
    if ((getResourceManager()
        .getTransactionStatus(con) == TSXaTransaction.NOTRANSACTION)
        && con.getAutoCommit()) {
      de.setStatus(DataElement.COMMITTED);
      System.out.println("TSeis.update.permtable");
      permtable.put(key, de);
    } else { // if (getResourceManager().getTransactionStatus(con) ==
             // TSResourceManager.INTRANSACTION)
      System.out.println("TSeis.update.temptable");
      if (de.getStatus() != DataElement.INSERTED) {
        de.setStatus(DataElement.UPDATED);
      }
      System.out.println("TSeis.update." + de.getKey() + de.getValue());
      con.getTempTable().put(key, de);
    }
  }

  public void delete(String key, TSConnection con) throws TSEISException {
    DataElement de = read(key, con);
    if ((getResourceManager()
        .getTransactionStatus(con) == TSXaTransaction.NOTRANSACTION)
        && con.getAutoCommit()) {
      System.out.println("TSeis.delete.permtable");
      permtable.remove(key);
    } else { // if (getResourceManager().getTransactionStatus(con) ==
             // TSResourceManager.INTRANSACTION)
      System.out.println("TSeis.delete.temptable");
      de.setStatus(DataElement.DELETED);
      con.getTempTable().put(key, de);
    }
  }

  public void returnConnection(TSConnectionImpl con) {
    con.expireLease();
  }

  public void dropTable() {
    ConnectorStatus.getConnectorStatus().logAPI("TSeis.dropTable", "", "");
    permtable.clear();
    ConnectorStatus.getConnectorStatus().logAPI("Table Dropped ", "", "");
  }

  public void setAutoCommit(boolean flag) {
    commitflag = flag;
  }

  public boolean getAutoCommit() {
    return commitflag;
  }

  public Vector readData() {
    System.out.println("TSeis.readData");
    Vector v = new Vector();
    Enumeration e = permtable.keys();
    while (e.hasMoreElements()) {
      DataElement de = (DataElement) permtable.get((String) e.nextElement());
      System.out.println("TSeis.readData." + de.getValue());
      v.add(de.getValue());
    }
    return v;
  }

  private void copyVector(Vector v1, Vector v2) {
    int size = v1.size();

    v2.clear();
    for (int i = 0; i < size; i++) {
      String str1 = (String) v1.get(i);
      v2.add(i, new String(str1));
    }
  }

  // **** Implementing Resource Manager for Local Transaction **********//

  public void begin() {
    ConnectorStatus.getConnectorStatus().logAPI("TSeis.begin", "", "");
  }

  public void commit(TSConnection con) throws TSEISException {
    Hashtable ht = con.getTempTable();
    Enumeration e = ht.keys();
    while (e.hasMoreElements()) {
      String key = (String) e.nextElement();
      DataElement de = (DataElement) ht.get(key);
      if (de.getStatus() == DataElement.INSERTED) {
        actualInsert(de);
      }
      if (de.getStatus() == DataElement.UPDATED) {
        actualUpdate(de);
      }
      if (de.getStatus() == DataElement.DELETED) {
        actualDelete(de.getKey());
      }
    }
  }

  void actualInsert(DataElement de) throws TSEISException {
    if (permtable.containsKey(de.getKey())) {
      throw new TSEISException("Duplicate Key");
    }
    de.setStatus(DataElement.COMMITTED);
    permtable.put(de.getKey(), de);
  }

  void actualUpdate(DataElement de) throws TSEISException {
    DataElement element = (DataElement) permtable.get(de.getKey());
    if (element.getStatus() == DataElement.PREPARED) {
      throw new TSEISException("Cannot update now. 2PC in progress");
    }
    de.setStatus(DataElement.COMMITTED);
    permtable.put(de.getKey(), de);
  }

  void actualDelete(String key) throws TSEISException {
    if (permtable.containsKey(key)) {
      DataElement element = (DataElement) permtable.get(key);
      if (element.getStatus() == DataElement.PREPARED) {
        throw new TSEISException("Cannot update now. 2PC in progress");
      }
      permtable.remove(key);
    }
  }

  void prepareInsert(DataElement de, Xid xid) throws TSEISException {
    if (permtable.containsKey(de.getKey())) {
      throw new TSEISException("Duplicate Key");
    }
    DataElement element = new DataElement(de.getKey(), de.getValue());
    element.prepare(de, xid);
    permtable.put(element.getKey(), element);
  }

  void prepareChange(DataElement de, Xid xid) throws TSEISException {
    DataElement element = (DataElement) permtable.get(de.getKey());
    DataElement prepElement = new DataElement(de.getKey());
    prepElement.prepare(de, xid);
    permtable.put(element.getKey(), prepElement);
  }

  // ****** Ending implementation for Resource Manager for Local Transaction
  // *****//

  // ******Implementating Resource Manager for XA Transaction *****//

  public void commit(Xid xid) {
    System.out.println("Tseis.commit.xid");
    Enumeration e = permtable.keys();
    while (e.hasMoreElements()) {
      System.out.println("commit.inTheLoop");
      String key = (String) e.nextElement();
      DataElement de = (DataElement) permtable.get(key);
      if (de.getStatus() == DataElement.PREPARED && de.getXid() == xid) {
        DataElement element = de.getPreparedValue();
        if (element.getStatus() == DataElement.DELETED) {
          System.out.println("commit.delete");
          permtable.remove(element.getKey());
        } else {
          System.out.println("commit.update");
          element.setStatus(DataElement.COMMITTED);
          permtable.put(element.getKey(), element);
        }
      }
    }
  }

  public void rollback(Xid xid) {
    System.out.println("Tseis.rollback.xid");
    Enumeration e = permtable.keys();
    while (e.hasMoreElements()) {
      String key = (String) e.nextElement();
      DataElement de = (DataElement) permtable.get(key);
      if (de.getStatus() == DataElement.PREPARED && de.getXid() == xid) {
        // Revert the state to commit so that, prepared value becomes unusable
        de.setStatus(DataElement.COMMITTED);
        permtable.put(de.getKey(), de);
      }
    }
  }

  public TSResourceManager getResourceManager() {
    return rm;
  }

  // ****** Ending implementation for Resource Manager for XA Transaction
  // *****//

}
