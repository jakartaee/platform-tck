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
 * @(#)TSXaTransaction.java	1.0 06/06/02
 */

package com.sun.ts.tests.common.connector.whitebox;

import javax.transaction.xa.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Class representing one Global Transaction.
 *
 * @version 1.0, 06/06/02
 * @author Binod P.G
 */
public class TSXaTransaction {

  /** No Transaction . Default status **/
  static final int NOTRANSACTION = 0;

  /** Status indicating that Transaction has started **/
  static final int STARTED = 1;

  /** Status indicating that Transaction has been suspended **/
  static final int SUSPENDED = 2;

  /** Status indicating that Transaction has been ended with a failure **/
  static final int ENDFAILED = 3;

  /** Status indicating that Transaction has been ended with a success **/
  static final int ENDSUCCESSFUL = 4;

  /** Status indicating that Transaction has been prepared **/
  static final int PREPARED = 5;

  /** Field storing the status values **/
  private int status = 0;

  /** Connections involved in one transaction branch. Specifications doesnt **/
  /** stop from having more than one connection in global transaction **/
  private Hashtable connections = new Hashtable();

  /** Id of the transaction **/
  private Xid xid;

  /**
   * Creates a new Global Transaction object.
   *
   * @param id
   *          Global Transaction Identifier.
   */
  public TSXaTransaction(Xid id) {
    this.xid = id;
  }

  /**
   * Adds the connection to this transaction.
   *
   * @param con
   *          Connection involved in the transaction branch.
   */
  public void addConnection(TSConnection con) {
    connections.put(con, "");
  }

  /**
   * Return the connections involved as a hashtable.
   * 
   * @return Connections.
   */
  Hashtable getConnections() {
    return connections;
  }

  /**
   * Sets the Status of the transaction.
   *
   * @param Status
   *          of the transaction
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Get the status of the transaction.
   * 
   * @return Status of the transaction.
   */
  public int getStatus() {
    return this.status;
  }

  /**
   * Prepare the transaction.
   * 
   * @return The Vote of this branch in this transaction.
   * @throws XAException
   *           If prepare fails.
   */
  public int prepare() throws XAException {
    System.out.println("TsXaTransaction.prepare");
    Hashtable ht = prepareTempTable();

    Enumeration e = ht.keys();
    while (e.hasMoreElements()) {
      System.out.println("TSXaTransaction.prepare.inTheLoop");
      String key = (String) e.nextElement();
      DataElement de = (DataElement) ht.get(key);

      if (de.getStatus() == DataElement.INSERTED) {
        try {
          TSeis.getTSeis().prepareInsert(de, xid);
        } catch (TSEISException ex) {
          throw new XAException(XAException.XA_RBROLLBACK);
        }
      }
      if (de.getStatus() == DataElement.UPDATED
          || de.getStatus() == DataElement.DELETED) {
        System.out.println("TSXaTransaction.prepare.updatePrepare");
        try {
          TSeis.getTSeis().prepareChange(de, xid);
        } catch (TSEISException ex) {
          throw new XAException(XAException.XA_RBROLLBACK);
        }
      }
    }
    return XAResource.XA_OK;
  }

  // Creates one temp table for the transaction.
  private Hashtable prepareTempTable() {
    System.out.println("TSXaTransaction.prepareTempTable");
    Hashtable temptable = new Hashtable();

    Enumeration e = connections.keys();

    while (e.hasMoreElements()) {
      System.out.println("TSXaTransaction.prepareTempTable.inTheLoop");
      TSConnection con = (TSConnection) e.nextElement();
      Hashtable temp = con.getTempTable();

      Enumeration e1 = temp.keys();
      while (e1.hasMoreElements()) {
        String key = (String) e1.nextElement();
        DataElement de = (DataElement) temp.get(key);

        if (temptable.containsKey(key)) {
          DataElement de1 = (DataElement) temptable.get(key);
          // Latest version should be used
          if (de.getVersion() >= de1.getVersion()) {
            temptable.put(key, de);
          }
        } else {
          temptable.put(key, de);
        }
      }
      con.rollback();
    }

    return temptable;
  }

  /**
   * Commits this Transaction.
   * 
   * @param Boolean
   *          indicating, whether it is a single-phase or 2-phase commit.
   * @throws XAException
   *           If commit fails.
   */
  public void commit(boolean onePhase) throws XAException {
    System.out.println("TsXaTransaction.commit." + onePhase);
    if (onePhase) {
      Hashtable ht = prepareTempTable();

      Enumeration e = ht.keys();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        DataElement de = (DataElement) ht.get(key);

        if (de.getStatus() == DataElement.INSERTED) {
          try {
            TSeis.getTSeis().actualInsert(de);
          } catch (TSEISException ex) {
            throw new XAException(XAException.XA_RBROLLBACK);
          }
        }
        if (de.getStatus() == DataElement.UPDATED) {
          try {
            TSeis.getTSeis().actualUpdate(de);
          } catch (TSEISException ex) {
            throw new XAException(XAException.XA_RBROLLBACK);
          }
        }
        if (de.getStatus() == DataElement.DELETED) {
          try {
            TSeis.getTSeis().actualDelete(de.getKey());
          } catch (TSEISException ex) {
            throw new XAException(XAException.XA_RBROLLBACK);
          }
        }
      }
    } else {
      TSeis.getTSeis().commit(xid);
    }
  }

  /**
   * Rolls back the transaction
   */
  public void rollback() {
    if (status == PREPARED) {
      TSeis.getTSeis().rollback(xid);
    } else {
      Enumeration e = connections.keys();

      while (e.hasMoreElements()) {
        TSConnection con = (TSConnection) e.nextElement();
        con.rollback();
      }
    }
  }

  /**
   * Check for the key in the transaction. If it is not readable return null.
   *
   * @param elementkey
   *          Key to be read.
   * @return DataElement object.
   * @throws TSEISException
   *           In case, read fails.
   */
  public DataElement read(String elementkey) throws TSEISException {
    Hashtable temptable = new Hashtable();
    Enumeration e = connections.keys();

    while (e.hasMoreElements()) {
      TSConnection con = (TSConnection) e.nextElement();
      Hashtable temp = con.getTempTable();

      Enumeration e1 = temp.keys();
      while (e1.hasMoreElements()) {
        String key = (String) e1.nextElement();
        DataElement de = (DataElement) temp.get(key);

        if (temptable.containsKey(key)) {
          DataElement de1 = (DataElement) temptable.get(key);
          // Latest version should be used
          if (de.getVersion() >= de1.getVersion()) {
            temptable.put(key, de);
          }
        } else {
          temptable.put(key, de);
        }
      }
    }

    if (temptable.containsKey(elementkey)) {
      DataElement de = (DataElement) temptable.get(elementkey);
      if (de.getStatus() == DataElement.DELETED) {
        throw new TSEISException("Data deleted");
      } else {
        return de;
      }
    } else {
      return null;
    }
  }
}
