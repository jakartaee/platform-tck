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
 * @(#)TSResourceManager.java	1.0 06/06/02
 */

package com.sun.ts.tests.common.connector.whitebox;

import javax.transaction.xa.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Resource Manager for the TSeis. Completely based on JTS.
 *
 * @version 1.0, 06/06/02
 * @author Binod P.G
 */
public class TSResourceManager {

  private boolean opened = false;

  private Hashtable association = new Hashtable();

  /**
   * Creates the Resource Manager.
   */
  public TSResourceManager() {
    openRM();
  }

  /**
   * Opens the Resource Manager.
   */
  public void openRM() {
    if (!opened) {
      opened = true;
    }
  }

  /**
   * Closes the Resource Manager.
   */
  public void closeRM() {
    opened = false;
    association.clear();
  }

  /**
   * Starts the new Global Transaction branch. transaction can be started in
   * three ways.
   * <p>
   * 1. With no flags (TMNOFLAGS) : This is starting a new transaction
   * </p>
   * <p>
   * 2. With join flag(TMJOIN) : This is joining new transaction
   * </p>
   * <p>
   * 3. With resume flag(TRESUME) : This is resuming a suspended transaction
   * </p>
   *
   * @param xid
   *          Global Id for the transaction.
   * @param flags
   *          Flags used for Transaction. For more details see JTA spec.
   * @param con
   *          Connection involved in the Global Transaction.
   * @throws XAExcpetion
   *           In case of a failure / Invalid flag / Invalid XA protocol.
   */
  public void start(Xid xid, int flags, TSConnection con) throws XAException {
    System.out.println("start." + flags + "." + xid + "..." + con);
    sanityCheck(xid, flags, "start");

    if (flags == XAResource.TMNOFLAGS) {
      TSXaTransaction txn = new TSXaTransaction(xid);
      txn.setStatus(TSXaTransaction.STARTED);
      if (con != null)
        txn.addConnection(con);
      association.put(xid, txn);
    } else if (flags == XAResource.TMJOIN) {
      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (con != null)
        txn.addConnection(con);
      association.put(xid, txn);
    } else if (flags == XAResource.TMRESUME) {
      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      txn.setStatus(TSXaTransaction.STARTED);
      association.put(xid, txn);
    }
  }

  /**
   * Ends the Global Transaction branch.
   *
   * @param xid
   *          Global Id for the transaction.
   * @param flags
   *          Flags used for Transaction. For more details see JTA spec.
   * @throws XAExcpetion
   *           In case of a failure / Invalid flag / Invalid XA protocol.
   */
  public void end(Xid xid, int flags) throws XAException {
    sanityCheck(xid, flags, "end");

    int status = 0;
    if (flags == XAResource.TMFAIL)
      status = TSXaTransaction.ENDFAILED;
    if (flags == XAResource.TMSUSPEND)
      status = TSXaTransaction.SUSPENDED;
    if (flags == XAResource.TMSUCCESS)
      status = TSXaTransaction.ENDSUCCESSFUL;

    TSXaTransaction txn = (TSXaTransaction) association.get(xid);
    txn.setStatus(status);
    association.put(xid, txn);
  }

  /**
   * Prepare the Global Transaction branch.
   *
   * @param xid
   *          Global Id for the transaction.
   * @throws XAExcpetion
   *           In case of a failure / Invalid XA protocol.
   */
  public int prepare(Xid xid) throws XAException {
    sanityCheck(xid, XAResource.TMNOFLAGS, "prepare");

    TSXaTransaction txn = (TSXaTransaction) association.get(xid);
    int ret;
    try {
      ret = txn.prepare();
    } catch (XAException xe) {
      // If an prepare fails, Transaction Manager doesnt need to rollback
      // the transaction explicitely. So remove the particular xid.
      System.out.println("Self.rollbak");
      txn.rollback();
      association.remove(xid);
      throw xe;
    }
    txn.setStatus(TSXaTransaction.PREPARED);
    association.put(xid, txn);
    return ret;
  }

  /**
   * Commits the Global Transaction branch.
   *
   * @param xid
   *          Global Id for the transaction.
   * @throws XAExcpetion
   *           In case of a failure / Invalid XA protocol.
   */
  public void commit(Xid xid, boolean onePhase) throws XAException {

    if (onePhase) {
      sanityCheck(xid, XAResource.TMNOFLAGS, "1pccommit");
    } else {
      sanityCheck(xid, XAResource.TMNOFLAGS, "2pccommit");
    }

    TSXaTransaction txn = (TSXaTransaction) association.get(xid);

    if (txn != null) {
      try {
        txn.commit(onePhase);
      } catch (XAException eb) {
        throw new XAException(XAException.XA_RBROLLBACK);
      } catch (Exception e) {
        throw new XAException(XAException.XAER_RMERR);
      } finally {
        association.remove(xid);
      }
    }
  }

  /**
   * Rolls back the Global Transaction branch.
   *
   * @param xid
   *          Global Id for the transaction.
   * @throws XAExcpetion
   *           In case of a failure / Invalid XA protocol.
   */
  public void rollback(Xid xid) throws XAException {

    sanityCheck(xid, XAResource.TMNOFLAGS, "rollback");

    TSXaTransaction txn = (TSXaTransaction) association.get(xid);
    if (txn != null) {
      try {
        txn.rollback();
      } catch (Exception e) {
        throw new XAException(XAException.XAER_RMERR);
      } finally {
        association.remove(xid);
      }
    }
  }

  /**
   * Get the Transaction status of a Connection.
   *
   * @param con
   *          Connection involved.
   */
  int getTransactionStatus(TSConnection con) {
    Enumeration e = association.keys();
    while (e.hasMoreElements()) {
      Xid id = (Xid) e.nextElement();
      TSXaTransaction txn = (TSXaTransaction) association.get(id);
      Hashtable connections = txn.getConnections();
      Enumeration e1 = connections.keys();
      while (e1.hasMoreElements()) {
        TSConnection temp = (TSConnection) e1.nextElement();
        if (con == temp) {
          return txn.getStatus();
        }
      }
    }
    return TSXaTransaction.NOTRANSACTION;
  }

  /**
   * Reads a particular key in a distributed transaction.
   *
   * @param key
   *          Key to be read.
   * @param con
   *          Connection involved.
   * @throws TSEIExcpetion
   *           If an error occurs.
   */
  DataElement read(String key, TSConnection con) throws TSEISException {
    System.out.println("ResourceManager.read");
    Enumeration e = association.keys();
    while (e.hasMoreElements()) {
      Xid xid = (Xid) e.nextElement();
      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      Hashtable connections = txn.getConnections();
      if (connections.containsKey(con)) {
        return txn.read(key);
      }
    }
    return null;
  }

  /**
   * Do the sanity check.
   *
   * @param xid
   *          Global Id for the transaction branch.
   * @param flags
   *          Flag sent by the Transaction Manager.
   * @throws XAExcpetion
   *           In case of an invalid XA protocol.
   */
  private void sanityCheck(Xid xid, int flags, String operation)
      throws XAException {
    // Sanity checks for the xa_start operation.
    if ((operation != null) && (operation.equals("start"))) {

      if (!(flags == XAResource.TMNOFLAGS || flags == XAResource.TMJOIN
          || flags == XAResource.TMRESUME)) {
        throw new XAException(XAException.XAER_INVAL);
      }

      // For TMNOFLAGS xid should not be known to RM.
      if (flags == XAResource.TMNOFLAGS) {
        if (association.containsKey(xid)) {
          throw new XAException(XAException.XAER_DUPID);
        }
      }

      // For TMJOIN xid should be known to RM.
      if (flags == XAResource.TMJOIN) {
        if (!association.containsKey(xid)) {
          throw new XAException(XAException.XAER_INVAL);
        }

        TSXaTransaction txn = (TSXaTransaction) association.get(xid);
        if (!(txn.getStatus() == TSXaTransaction.STARTED
            || txn.getStatus() == TSXaTransaction.ENDSUCCESSFUL)) {
          throw new XAException(XAException.XAER_PROTO);
        }
      }

      if (flags == XAResource.TMRESUME) {
        if (!association.containsKey(xid)) {
          throw new XAException(XAException.XAER_INVAL);
        }

        TSXaTransaction txn = (TSXaTransaction) association.get(xid);
        if (txn.getStatus() != TSXaTransaction.SUSPENDED) {
          throw new XAException(XAException.XAER_PROTO);
        }
      }
    }

    // End operation.
    if ((operation != null) && (operation.equals("end"))) {

      if (!(flags == XAResource.TMSUSPEND || flags == XAResource.TMFAIL
          || flags == XAResource.TMSUCCESS)) {
        throw new XAException(XAException.XAER_INVAL);
      }

      if (!association.containsKey(xid)) {
        throw new XAException(XAException.XAER_INVAL);
      }

      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (!(txn.getStatus() == TSXaTransaction.STARTED
          || txn.getStatus() == TSXaTransaction.ENDSUCCESSFUL)) {
        throw new XAException(XAException.XAER_PROTO);
      }

    }

    // Prepare operation.
    if ((operation != null) && (operation.equals("prepare"))) {

      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (txn.getStatus() != TSXaTransaction.ENDFAILED
          && txn.getStatus() != TSXaTransaction.ENDSUCCESSFUL) {
        throw new XAException(XAException.XAER_PROTO);
      }

    }

    // 2PC Commit operation.
    if ((operation != null) && (operation.equals("2pccommit"))) {

      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (txn.getStatus() != TSXaTransaction.PREPARED) {
        throw new XAException(XAException.XAER_PROTO);
      }

    }

    // 1PC Commit operation.
    if ((operation != null) && (operation.equals("1pccommit"))) {

      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (txn.getStatus() != TSXaTransaction.ENDSUCCESSFUL
          && txn.getStatus() != TSXaTransaction.ENDFAILED) {
        throw new XAException(XAException.XAER_PROTO);
      }

    }

    // Rollback operation.
    if ((operation != null) && (operation.equals("rollback"))) {

      TSXaTransaction txn = (TSXaTransaction) association.get(xid);
      if (txn != null) {
        if (txn.getStatus() != TSXaTransaction.PREPARED
            && txn.getStatus() != TSXaTransaction.ENDSUCCESSFUL
            && txn.getStatus() != TSXaTransaction.ENDFAILED) {
          throw new XAException(XAException.XAER_PROTO);
        }
      }
    }
  }

}
