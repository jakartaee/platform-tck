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
import javax.resource.spi.*;
import com.sun.ts.tests.common.connector.util.*;

public class LocalTransactionImpl implements LocalTransaction {

  private TSManagedConnection mc;

  /*
   * @name LocalTransactionImpl
   * 
   * @desc LocalTransactionImpl constructor
   * 
   * @param TSManagedConnection
   */
  public LocalTransactionImpl(TSManagedConnection mc) {
    this.mc = mc;
  }

  /*
   * @name begin
   * 
   * @desc sends the event that local transaction has started
   * 
   * @exception ResourceException
   * 
   */
  @Override
  public void begin() throws ResourceException {
    try {
      TSConnection con = mc.getTSConnection();
      ConnectorStatus.getConnectorStatus().logAPI("LocalTransaction.begin", "",
          "");
      con.setAutoCommit(false);
      System.out.println("LocalTransaction.begin");
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    }
  }

  /*
   * @name commit
   * 
   * @desc Sends an event that local transaction has been commited.
   * 
   * @exception ResourceException
   */
  @Override
  public void commit() throws ResourceException {
    TSConnection con = null;
    try {
      con = mc.getTSConnection();
      ConnectorStatus.getConnectorStatus().logAPI("LocalTransaction.commit", "",
          "");
      System.out.println("LocalTransaction.commit");
      con.commit();
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    } finally {
      try {
        if (con != null) {
          con.setAutoCommit(true);
        }
      } catch (Exception ex) {
        ex.getMessage();
      }
    }
  }

  /*
   * @name rollback
   * 
   * @desc Sends an event to rollback the transaction
   * 
   * @exception ResourceException
   */
  @Override
  public void rollback() throws ResourceException {
    TSConnection con = null;
    try {
      con = mc.getTSConnection();
      ConnectorStatus.getConnectorStatus().logAPI("LocalTransaction.rollback",
          "", "");
      con.rollback();
    } catch (Exception ex) {
      ResourceException re = new EISSystemException(ex.getMessage());
      re.initCause(ex);
      throw re;
    } finally {
      try {
        if (con != null) {
          con.setAutoCommit(true);
        }
      } catch (Exception ex) {
      }
    }
  }
}
